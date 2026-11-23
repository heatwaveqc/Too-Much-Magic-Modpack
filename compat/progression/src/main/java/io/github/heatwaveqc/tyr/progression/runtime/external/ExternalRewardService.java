package io.github.heatwaveqc.tyr.progression.runtime.external;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy;
import io.github.heatwaveqc.tyr.progression.data.ExperienceEconomyRuntime;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpPlayerPolicy;
import io.github.heatwaveqc.tyr.progression.runtime.NewbieMode;
import io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import java.util.Map;

/**
 * First generic, Path-independent, server-side external progression purchase.
 *
 * The shared XP paid-rank ledger owns one immutable purchase receipt per point.
 * Native Astral owns actual perk tokens, research access and UI allocation.
 * No new XP wallet, token item, Path skill rank or direct native data mutation.
 */
public final class ExternalRewardService {
    private ExternalRewardService() {}
    private static final int MAX_RESTORES_PER_PASS = 32;
    private static final String ASTRAL_KIND = "astral_perk_token";

    public static void register() {
        NeoForge.EVENT_BUS.addListener(ExternalRewardService::onLogin);
        NeoForge.EVENT_BUS.addListener(ExternalRewardService::onTick);
    }

    private static boolean actor(ServerPlayer p) {
        return MxpPlayerPolicy.eligible(p) && !NewbieMode.isNewbie(p);
    }

    private static ExternalRewardPolicy.Offer approved(String id) {
        var offer = ExternalRewardConfig.get(id);
        return offer != null && ASTRAL_KIND.equals(offer.kind())
                && "tyr_progression:mxp".equals(offer.currency()) ? offer : null;
    }

    public static int owned(ServerPlayer player,String id) {
        var offer=approved(id);
        if(offer==null)return 0;
        return ExternalRewardPolicy.owned(
                player.getData(ProgressionAttachments.LEDGER.get()).economy(),offer);
    }

    public static int nextPrice(ServerPlayer player,String id) {
        var offer=approved(id);
        if(offer==null)return -1;
        return ExternalRewardPolicy.price(offer, owned(player,id));
    }

    public static boolean canBuy(ServerPlayer player,String id) {
        if (!actor(player)) return false;
        var offer=approved(id);
        if (offer==null || !AstralPerkTokens.snapshot(player).ready()) return false;
        var spec=ExperienceEconomyRuntime.current().systems().get(offer.currency());
        return ExternalRewardPolicy.prepare(
                player.getData(ProgressionAttachments.LEDGER.get()).economy(),spec,offer)!=null;
    }

    public static boolean purchase(ServerPlayer player,String id) {
        if (!actor(player)) return false;
        var offer=approved(id);
        if (offer==null) return false;
        var nativeBefore=AstralPerkTokens.snapshot(player);
        if (!nativeBefore.ready()) return false;
        var original=player.getData(ProgressionAttachments.LEDGER.get());
        var system=ExperienceEconomyRuntime.current().systems().get(offer.currency());
        var plan=ExternalRewardPolicy.prepare(original.economy(), system, offer);
        if (plan==null || nativeBefore.has(plan.tokenId())) return false;

        // Record the charge FIRST. In the event of a server crash between
        // this operation and native grant, the login reconciliation completes
        // the paid purchase exactly once. Never grant an unpaid bonus.
        player.setData(ProgressionAttachments.LEDGER.get(),original.withEconomy(plan.charged()));
        AstralPerkTokens.Result granted=AstralPerkTokens.grant(player,plan.tokenId());
        if (granted==AstralPerkTokens.Result.GRANTED) return true;

        // If the provider failed AFTER mutating native progress, preserve the
        // already-paid ledger receipt; reconciliation must never mint a free
        // token or refund a native award we cannot prove absent.
        if (AstralPerkTokens.snapshot(player).has(plan.tokenId())) {
            ProgressionMod.LOGGER.warn("Astral perk purchase {} was granted natively but result was {}",
                    plan.tokenId(),granted);
            return true;
        }
        // No native token exists: refund exactly the proposed fresh charge.
        player.setData(ProgressionAttachments.LEDGER.get(),original);
        return false;
    }

    /** Restoration is neither a purchase nor a second debit. */
    public static int reconcile(ServerPlayer player) {
        if (player==null || player.getServer()==null || !player.getServer().isSameThread()
                || NewbieMode.isNewbie(player) || ExternalRewardConfig.current().isEmpty()) return 0;
        AstralPerkTokens.Snapshot snapshot=AstralPerkTokens.snapshot(player);
        if (!snapshot.ready()) return 0; // Wiped research: defer until attuned again.
        int restored=0;
        var ledger=player.getData(ProgressionAttachments.LEDGER.get()).economy();
        for (Map.Entry<String,ExternalRewardPolicy.Offer> row:ExternalRewardConfig.current().entrySet()) {
            var offer=approved(row.getKey());
            if(offer==null)continue;
            int count=ExternalRewardPolicy.owned(ledger,offer);
            // Crash-consistent two-authority repair: Progression paid receipts
            // are canonical. Astral tokens under OUR reserved prefix without
            // a matching paid rank are orphaned native grants; revoke them
            // rather than letting a crash or duplicate packet mint free points.
            for (var token:snapshot.tokens()) {
                if (!"tyr_progression".equals(token.getNamespace())) continue;
                String path=token.getPath();
                if (!path.matches("astral_purchase_[0-9]{6,7}")) continue;
                int index=Integer.parseInt(path.substring("astral_purchase_".length()));
                if (index>count && !AstralPerkTokens.revoke(player,token.toString()))
                    ProgressionMod.LOGGER.warn("Unpaid Astral perk token {} could not be revoked for {}",
                            token,player.getUUID());
            }
            for(int i=1;i<=count;i++) {
                String token=ExternalRewardPolicy.tokenId(offer.kind(),i);
                if(snapshot.has(token)) continue;
                var result=AstralPerkTokens.grant(player,token);
                if(result==AstralPerkTokens.Result.GRANTED) {
                    restored++;
                    if(restored>=MAX_RESTORES_PER_PASS)return restored;
                } else if(result==AstralPerkTokens.Result.ERROR
                        ||result==AstralPerkTokens.Result.NOT_READY)return restored;
            }
        }
        return restored;
    }

    private static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if(event.getEntity() instanceof ServerPlayer player) reconcile(player);
    }
    private static void onTick(PlayerTickEvent.Post event) {
        if(event.getEntity() instanceof ServerPlayer player && player.tickCount%100==0) reconcile(player);
    }
}
