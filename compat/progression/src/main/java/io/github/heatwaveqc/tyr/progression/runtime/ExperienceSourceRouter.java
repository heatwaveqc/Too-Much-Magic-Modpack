package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.state.Ledger;
import java.math.BigDecimal;
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
                || NewbieMode.isNewbie(player)) return prior;
        var catalog = PathRegistry.economyCatalog();
        var next = prior;
        for (var destination : catalog.routing().destinations(source,fallbackSystem)) {
            var system = catalog.systems().get(destination.system());
            if (system == null) continue;
            // Preserve existing registered-channel conditions where applicable.
            if (PathRegistry.economy(system.id()) != null
                    && !PathRegistry.economyEligible(player,system.id())) continue;
            boolean first = completion != null && !next.economy().rewardReceipts()
                    .contains(system.id()+"|"+source+"|"+completion);
            if (completion != null && !first && !repeat) continue;
            BigDecimal nominal = repeat && !first ? repeatNominal : firstNominal;
            boolean fixed = repeat && !first ? repeatFixed : firstFixed;
            if (nominal == null || nominal.signum() <= 0) continue;
            // A multiplier of zero is a settled zero, unlike a hard Path gate.
            // Never publish intermediate ledgers; if validation throws the
            // caller retains prior and no currency receives a partial award.
            var award = ExperienceModifiers.earn(player,next,system,source,transaction,
                    first ? completion : null,nominal,fixed,catalog.growth(system.id()),
                    destination.multiplier());
            if (award.accepted()) next = next.withEconomy(award.ledger());
        }
        return next;
    }
}
