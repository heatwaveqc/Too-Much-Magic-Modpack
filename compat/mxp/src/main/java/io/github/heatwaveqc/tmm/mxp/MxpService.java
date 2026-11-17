package io.github.heatwaveqc.tmm.mxp;

import io.github.heatwaveqc.tmm.mxp.core.MxpAccount;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Public server-only integration surface for compatible casting/ritual addons.
 * Call after a confirmed expenditure or successful completion, never on preview.
 *
 * A transaction ID MUST uniquely identify one committed world action, and must
 * be passed unchanged by every mod bridge observing the same logical action.
 */
public final class MxpService {
    public record AwardStatus(boolean accepted, boolean duplicate, boolean first,
                              double effectiveXP, float deliveredToTyz,
                              long lifetimeLevels, double progress, double pendingXP,
                              String note) {}

    /** Every first registered boss victory awards exactly 1,000 effective MXP. */
    public static final double FIRST_BOSS_MXP = 1000.0;

    public record BossRule(double repeat, boolean repeatFixed) {
        public BossRule {
            if (!Double.isFinite(repeat) || repeat <= 0)
                throw new IllegalArgumentException("Invalid MXP repeat-boss reward");
        }
    }

    private static final Map<String, BossRule> BOSS_RULES = new HashMap<>();

    private MxpService() {}

    public static void registerBossRule(String entityId,
                                        double repeat, boolean repeatFixed) {
        ResourceLocation id = ResourceLocation.tryParse(entityId);
        if (id == null || !id.toString().equals(entityId))
            throw new IllegalArgumentException("Use canonical boss entity ID");
        BOSS_RULES.put(id.toString(), new BossRule(repeat, repeatFixed));
    }

    public static BossRule bossRule(String entityId) {
        return BOSS_RULES.get(entityId);
    }

    public static void clearBossRules() {
        BOSS_RULES.clear();
    }

    public static MxpAccount inspect(ServerPlayer player) {
        return MxpSavedData.get(Objects.requireNonNull(player.getServer()))
                .snapshot(player.getUUID());
    }

    public static AwardStatus resource(ServerPlayer player, String sourceId, String transactionId,
                                       double nominalCost, double referencePool, double actualDebit) {
        if (actualDebit <= 0 || !Double.isFinite(actualDebit))
            return ignored(player, "No verified resource debit");
        return award(player, record -> record.resource(sourceId, transactionId,
                nominalCost, referencePool, actualDebit));
    }

    /**
     * Future JAWR-native blood debit adapter entry point. A real 0.5.39
     * successful supernatural action MUST have committed an authoritative
     * negative blood delta before this method is called. Feeding, refilling,
     * blood bottle transfers, previews, cancellations and failed attempts do
     * not count. Caller must reuse the same transaction ID as any parallel
     * spell payment hook: only ONE MXP receipt per logical action.
     *
     * This public API does not itself observe JAWR or grant XP; no automated
     * blood source is installed until an actual post-commit native hook has
     * passed dedicated-server compatibility tests.
     */
    public static AwardStatus jawrCommittedBloodDebit(ServerPlayer player,
            String transactionId, double originalBloodCost, double actualBloodDebit) {
        double pool = MxpSourceConfig.JAWR_INITIAL_BLOOD_POOL.get();
        if (!Double.isFinite(originalBloodCost) || originalBloodCost <= 0
                || !Double.isFinite(actualBloodDebit) || actualBloodDebit <= 0
                || pool <= 0) return ignored(player, "No verified JAWR blood payment");
        return resource(player, "witchery:player_blood", transactionId,
                originalBloodCost, pool, actualBloodDebit);
    }

    public static AwardStatus ritual(ServerPlayer player, String ritualId, String transactionId,
                                     double firstMXP, double repeatMXP) {
        return award(player, record -> record.ritual(ritualId, transactionId,
                firstMXP, repeatMXP));
    }

    public static AwardStatus craft(ServerPlayer player, String craftId, String transactionId,
                                    double firstMXP, double repeatMXP) {
        return award(player, record -> record.craft(craftId, transactionId,
                firstMXP, repeatMXP));
    }

    public static AwardStatus boss(ServerPlayer player, String bossId, String transactionId) {
        BossRule rule = bossRule(bossId);
        if (rule == null) return ignored(player, "Boss not in the explicit MXP registry");
        return award(player, record -> record.boss(bossId, transactionId,
                FIRST_BOSS_MXP, rule.repeat(), rule.repeatFixed()));
    }

    public static AwardStatus fixedDiagnostic(ServerPlayer player, String transactionId,
                                              double nominalFixedXP) {
        // Explicitly admin-command-only, not a general scripting grant API.
        return award(player, record -> record.ritual(
                "tmm_mxp:operator_diagnostic", transactionId, nominalFixedXP,
                nominalFixedXP));
    }

    private static AwardStatus ignored(ServerPlayer player, String why) {
        MxpAccount a = inspect(player);
        return new AwardStatus(false, false, false, 0, 0,
                a.counter().lifetimeLevels(), a.counter().progress(),
                a.pendingDelivery(), why);
    }

    private static AwardStatus award(ServerPlayer player, Function<MxpAccount, MxpAccount.Receipt> fun) {
        if (player == null || player.getServer() == null
                || !player.getServer().isSameThread()) {
            throw new IllegalStateException("MXP awards must execute on the logical server thread");
        }
        if (!MxpProgressionEligibility.canEarn(player))
            return ignored(player, "Newbie Mode cannot earn MXP");
        MxpSavedData data = MxpSavedData.get(player.getServer());
        MxpAccount staged = data.snapshot(player.getUUID());
        // Server config controls subsequent ordinary awards without changing
        // persisted lifetime P, pending Tyz delivery or first-completion flags.
        staged.setGrowthFactor(MxpProgressionConfig.GROWTH_FACTOR.get());
        MxpAccount.Receipt receipt = fun.apply(staged);
        if (receipt.duplicate()) {
            return new AwardStatus(false, true, false, 0, 0,
                    staged.counter().lifetimeLevels(), staged.counter().progress(),
                    staged.pendingDelivery(), "Duplicate committed action");
        }
        if (receipt.effectiveXP() <= 0 && staged.pendingDelivery() <= 0)
            return ignored(player, "No representable MXP from this award");

        float due = staged.deliverableNow();
        float delivered = 0;
        if (due > 0) {
            // Do NOT commit the staged transaction or discovery state on an
            // unaccepted Tyz XP grant: the caller may safely retry.
            delivered = TyzBridge.deliver(player, due);
            if (delivered <= 0) {
                return new AwardStatus(false, false, false, 0, 0,
                        data.snapshot(player.getUUID()).counter().lifetimeLevels(),
                        data.snapshot(player.getUUID()).counter().progress(),
                        data.snapshot(player.getUUID()).pendingDelivery(),
                        "Tyz rejected or canceled award; no ledger commit");
            }
            staged.confirmedDelivery(delivered);
        }
        // Fractional awards <.01 are saved even if not yet delivered to Tyz.
        data.commit(player.getUUID(), staged);
        return new AwardStatus(true, false, receipt.firstCompletion(),
                receipt.effectiveXP(), delivered,
                staged.counter().lifetimeLevels(), staged.counter().progress(),
                staged.pendingDelivery(), due == 0 ? "Stored fractional award" : "Recorded");
    }
}
