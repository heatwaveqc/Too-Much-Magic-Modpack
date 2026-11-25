package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.state.Ledger;
import java.math.BigDecimal;
import io.github.heatwaveqc.tyr.progression.admin.XpTrace;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.util.FakePlayer;

/**
 * Shared destination selection for trusted, committed gameplay events.
 * A caller authenticates the event and publishes the returned immutable ledger
 * once. It MUST NOT also credit the old explicit destination or invoke this
 * once per Path with the same verified event.
 */
public final class ExperienceSourceRouter {
    private ExperienceSourceRouter() {}

    /** Settle independently named sources once per authenticated event. */
    public static Ledger batch(ServerPlayer player, Ledger prior, String transaction,
            java.util.List<io.github.heatwaveqc.tyr.progression.data.XpSourceRouting.Candidate> candidates) {
        var next=prior;
        var normalized=PathRegistry.economyCatalog().routing().normalize(candidates);
        if(!candidates.isEmpty())XpTrace.note(player,()->"batch "+transaction+": candidates="+candidates.size()+", normalized="+normalized.size());
        for(var c:normalized)
            next=award(player,next,c.fallback(),c.source(),transaction,c.completion(),c.nominal(),c.fixed());
        return next;
    }

    public static Ledger award(ServerPlayer player, Ledger prior, String fallbackSystem,
            String source, String transaction, String completion, BigDecimal nominal, boolean fixed) {
        return settle(player,prior,fallbackSystem,source,transaction,completion,
                nominal,BigDecimal.ZERO,fixed,fixed,false);
    }

    /** First/repeat is selected independently for each destination currency. */
    public static Ledger completion(ServerPlayer player, Ledger prior, String fallbackSystem,
            String source, String transaction, String completion, BigDecimal firstNominal,
            BigDecimal repeatNominal, boolean firstFixed, boolean repeatFixed) {
        java.util.Objects.requireNonNull(completion,"completion");
        return settle(player,prior,fallbackSystem,source,transaction,completion,
                firstNominal,repeatNominal,firstFixed,repeatFixed,true);
    }

    private static Ledger settle(ServerPlayer player, Ledger prior, String fallbackSystem,
            String source, String transaction, String completion, BigDecimal firstNominal,
            BigDecimal repeatNominal, boolean firstFixed, boolean repeatFixed, boolean repeat) {
        if (player == null || player.getServer() == null || !player.getServer().isSameThread()
                || player instanceof FakePlayer || player.isCreative() || player.isSpectator()
                || NewbieMode.isNewbie(player)
                || player.getServer().getPlayerList().getPlayer(player.getUUID())!=player) {
            XpTrace.note(player,()->source+" rejected: player eligibility/thread");
            return prior;
        }
        var catalog = PathRegistry.economyCatalog();
        var next = prior;
        var destinations=catalog.routing().destinations(source,fallbackSystem);
        if(destinations.isEmpty())XpTrace.note(player,()->source+" rejected: no routed destination");
        for (var destination : destinations) {
            var system = catalog.systems().get(destination.system());
            if(system==null){XpTrace.note(player,()->source+" rejected: unknown system "+destination.system());continue;}
            // Preserve existing registered-channel conditions where applicable.
            if (PathRegistry.economy(system.id()) != null
                    && !PathRegistry.economyEligible(player,system.id())) {XpTrace.note(player,()->source+" -> "+system.id()+" rejected: channel eligibility");continue;}
            boolean first = completion != null && !next.economy().rewardReceipts()
                    .contains(system.id()+"|"+source+"|"+completion);
            if (completion != null && !first && !repeat) {XpTrace.note(player,()->source+" -> "+system.id()+" rejected: first-completion receipt already paid");continue;}
            BigDecimal nominal = repeat && !first ? repeatNominal : firstNominal;
            boolean fixed = repeat && !first ? repeatFixed : firstFixed;
            if (nominal == null || nominal.signum() <= 0) {XpTrace.note(player,()->source+" -> "+system.id()+" rejected: nonpositive nominal reward");continue;}
            // A multiplier of zero is a settled zero, unlike a hard Path gate.
            // Never publish intermediate ledgers; if validation throws the
            // caller retains prior and no currency receives a partial award.
            var award = ExperienceModifiers.earn(player,next,system,source,transaction,
                    first ? completion : null,nominal,fixed,catalog.growth(system.id()),
                    destination.multiplier());
            XpTrace.note(player,()->source+" -> "+system.id()+" nominal="+nominal+" fixed="+fixed+" first="+first
                +" accepted="+award.accepted()+" delta="+award.effectiveXp());
            if (award.accepted()) next = next.withEconomy(award.ledger());
        }
        return next;
    }
}
