package io.github.heatwaveqc.tyr.progression.mxp.hooks;

import io.github.heatwaveqc.tyr.progression.mxp.MxpMod;
import io.github.heatwaveqc.tyr.progression.mxp.MxpService;
import io.github.heatwaveqc.tyr.progression.mxp.MxpSourceConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import io.github.heatwaveqc.tyr.progression.mxp.core.PaymentReceiptPolicy;
import java.util.UUID;

/**
 * Source-backed casting boundaries; no rewards on spell-cost preview events.
 * Native Iron's cast: original cost event -> committed native/write -> method return.
 * Native Ars: expendMana entry -> actual debit -> method return.
 * ANS Ars: ANS reservation/quote -> commit confirmation (native debit skipped).
 */
public final class MxpCastHooks {
    private record Cast(ServerPlayer player, double originalCost, double before,
                        MxpManaProbe.Origin origin, String tx,
                        long tick, Object context) {}

    private static final ThreadLocal<Cast> IRON = new ThreadLocal<>();
    private static final ThreadLocal<Cast> ARS = new ThreadLocal<>();
    private record ManagedQuote(Object frame,ServerPlayer player,double originalCost,long tick){}
    private static final ThreadLocal<ManagedQuote> MANAGED_IRON = new ThreadLocal<>();

    private MxpCastHooks() {}

    /** Suppress Botania source awards while its items merely fund another spell. */
    public static boolean isProcessingSpell() {
        return IRON.get() != null || ARS.get() != null;
    }

    /** Invoked by the Iron's SpellOnCastEvent constructor after its fields are set. */
    public static void ironQuoted(Object event) {
        Object player = MxpReflect.call(event, "getEntity");
        if (!(player instanceof ServerPlayer sp) || !MxpPlayerPolicy.eligible(sp)) return;
        // The exact ANS Iron lifecycle owns actual reservation + successful effect
        // receipts, including full-cost scrolls and each paid channel pulse.
        Object frame = MxpReflect.staticCall("com.otectus.arsnspells.casting.IronsCastLifecycle", "current");
        if (MxpReflect.field(frame, "player") == sp) {
            double original=MxpReflect.number(MxpReflect.call(event,"getOriginalManaCost"));
            if(PaymentReceiptPolicy.positive(original)) MANAGED_IRON.set(new ManagedQuote(frame,sp,original,sp.level().getGameTime()));
            else MANAGED_IRON.remove();
            return;
        }
        Object source = MxpReflect.call(event, "getCastSource");
        if (!Boolean.TRUE.equals(MxpReflect.call(source, "consumesMana"))) return;
        double cost = MxpReflect.number(MxpReflect.call(event, "getOriginalManaCost"));
        if (!Double.isFinite(cost) || cost <= 0) return;
        double before = MxpManaProbe.balance(sp, MxpManaProbe.Origin.IRON);
        if (!Double.isFinite(before)) return;
        // A cross-cast inherits its parent's logical transaction identity.
        Cast parent = ARS.get();
        String tx = parent != null && parent.player == sp
                ? parent.tx : "cast:" + UUID.randomUUID();
        IRON.set(new Cast(sp, cost, before, MxpManaProbe.Origin.IRON,
                tx, sp.level().getGameTime(), event));
    }

    public static int ironCost(Object event,int price){
        var pending=IRON.get();
        return pending!=null&&pending.context==event?MagicCostPolicy.quote(pending.player,"irons_spellbooks:spell",price):price;
    }
    public static int arsCost(Object resolver,int price){
        var pending=ARS.get();
        return pending!=null&&pending.context==MxpReflect.field(resolver,"spellContext")?
            MagicCostPolicy.quote(pending.player,"ars_nouveau:spell",price):price;
    }

    /** Must run only when the native Iron's castSpell method completes. */
    public static void ironCastReturned() {
        Cast pending = IRON.get();
        IRON.remove();
        if (pending == null || !MxpPlayerPolicy.eligible(pending.player) || pending.player.level().getGameTime() != pending.tick) return;
        double after = MxpManaProbe.balance(pending.player, pending.origin);
        double debit = pending.before - after;
        if (Double.isFinite(debit) && debit > 0) {
            deliver(pending, MxpSourceConfig.IRON_FIRST_POOL.get(), debit);
        }
    }

    /** Called at entry to the Ars native expenditure boundary (not during previews). */
    public static void arsSpendEntered(Object resolver) {
        Object ctx = MxpReflect.field(resolver, "spellContext");
        if (ctx == null) return;
        Object caster = MxpReflect.call(ctx, "getUnwrappedCaster");
        if (!(caster instanceof ServerPlayer sp) || !MxpPlayerPolicy.eligible(sp)) return;
        if (Boolean.TRUE.equals(MxpReflect.staticCall("com.otectus.arsnspells.casting.ArsCastPayments", "handles", ctx))) return;
        Object spell = MxpReflect.call(ctx, "getSpell");
        double nominal = MxpReflect.number(MxpReflect.call(spell, "getCost"));
        if (!Double.isFinite(nominal) || nominal <= 0) return;
        double before = MxpManaProbe.balance(sp, MxpManaProbe.Origin.ARS);
        Cast parent = IRON.get();
        String tx = parent != null && parent.player == sp
                ? parent.tx : "cast:" + UUID.randomUUID();
        ARS.set(new Cast(sp, nominal, before, MxpManaProbe.Origin.ARS,
                tx, sp.level().getGameTime(), ctx));
    }

    /** Only native path: ANS returning true may cancel the original spend method. */
    public static void arsNativeSpendReturned() {
        Cast pending = ARS.get();
        ARS.remove();
        if (pending == null || !MxpPlayerPolicy.eligible(pending.player) || pending.player.level().getGameTime() != pending.tick) return;
        double after = MxpManaProbe.balance(pending.player, pending.origin);
        double debit = pending.before - after;
        if (Double.isFinite(debit) && debit > 0) {
            deliver(pending, MxpSourceConfig.ARS_FIRST_POOL.get(), debit);
        }
    }

    /**
     * Ars 'n Spells reserves native mana during prepare() BEFORE Ars.expendMana().
     * The post-commit bit + the quote's actual mana legs are the authoritative
     * evidence; a before/after snapshot during commit would incorrectly be 0.
     * Alternative LP/aura payments don't count as Ars mana.
     */
    public static void ansArsCommitted(Object player, Object context, boolean handled) {
        if (!handled || !(player instanceof ServerPlayer sp) || !MxpPlayerPolicy.eligible(sp)) return;
        try {
            Class<?> owner = Class.forName("com.otectus.arsnspells.casting.ArsCastPayments");
            Object map = MxpReflect.field(owner, "PLANS");
            if (!(map instanceof Map<?, ?> plans)) return;
            Object plan = plans.get(context);
            if (plan == null || !Boolean.TRUE.equals(MxpReflect.field(plan, "prepared"))
                    || !Boolean.TRUE.equals(MxpReflect.field(plan, "committed"))
                    || MxpReflect.field(plan, "alternative") != null) return;
            Object attempt = MxpReflect.field(plan, "attempt");
            if (!sp.getUUID().equals(MxpReflect.call(attempt, "playerId"))
                    || !Boolean.TRUE.equals(MxpReflect.call(attempt, "paymentAccepted"))) return;
            Object id = MxpReflect.call(attempt, "attemptId");
            if (!(id instanceof UUID paymentId)) return;
            double debit = manaLegs(MxpReflect.call(attempt, "paidLegs"));
            double nominal = MxpReflect.number(MxpReflect.call(MxpReflect.call(context, "getSpell"), "getCost"));
            if (!PaymentReceiptPolicy.positive(debit) || !PaymentReceiptPolicy.positive(nominal)) return;
            deliver(new Cast(sp, nominal, Double.NaN, MxpManaProbe.Origin.ARS,
                    PaymentReceiptPolicy.ansTransaction(paymentId), sp.level().getGameTime(), context),
                    MxpSourceConfig.ARS_FIRST_POOL.get(), debit);
        } catch (ClassNotFoundException | LinkageError | RuntimeException failure) {
            MxpMod.LOGGER.warn("ANS Ars MXP settlement rejected: {}", failure.toString());
        }
    }

    /** 3.3.5 lifecycle: successful effect return, never a quote, reserve or waived native write. */
    public static void ansIronEffectFinished() {
        try {
            Object frame = MxpReflect.staticCall("com.otectus.arsnspells.casting.IronsCastLifecycle", "current");
            Object rawPlayer = MxpReflect.field(frame, "player");
            if (!(rawPlayer instanceof ServerPlayer sp) || !MxpPlayerPolicy.eligible(sp)
                    || !Boolean.TRUE.equals(MxpReflect.field(frame, "started"))
                    || !Boolean.TRUE.equals(MxpReflect.field(frame, "finished"))
                    || Boolean.TRUE.equals(MxpReflect.field(frame, "aborted"))
                    || !Boolean.TRUE.equals(MxpReflect.field(frame, "owns"))
                    || MxpReflect.field(frame, "alternativeId") != null) return;
            Object outcome = MxpReflect.field(frame, "outcome");
            if (!Boolean.TRUE.equals(MxpReflect.call(outcome, "allowed")) ||
                    MxpReflect.call(outcome, "alternative") != null) return;
            Object payment = MxpReflect.call(outcome, "payment");
            if (!Boolean.TRUE.equals(MxpReflect.call(payment, "paid"))) return;
            Object id = MxpReflect.call(payment, "id");
            if (!(id instanceof UUID paymentId)) return;
            Object refunded = MxpReflect.call(payment, "refunded");
            if (!(refunded instanceof java.util.Collection<?> refunds) || !refunds.isEmpty()) return;
            double debit = manaLegs(MxpReflect.call(payment, "debited"));
            Object origin = MxpReflect.call(MxpReflect.field(frame, "quote"), "origin");
            if (!"IRONS_MANA".equals(String.valueOf(MxpReflect.call(origin, "unit")))) return;
            double nominal = MxpReflect.number(MxpReflect.call(origin, "amount"));
            ManagedQuote original=MANAGED_IRON.get();
            if(original!=null&&original.frame==frame&&original.player==sp&&original.tick==sp.level().getGameTime())
                nominal=original.originalCost;
            if (!PaymentReceiptPolicy.positive(debit) || !PaymentReceiptPolicy.positive(nominal)) return;
            deliver(new Cast(sp, nominal, Double.NaN, MxpManaProbe.Origin.IRON,
                    PaymentReceiptPolicy.ansTransaction(paymentId), sp.level().getGameTime(), frame),
                    MxpSourceConfig.IRON_FIRST_POOL.get(), debit);
        } catch (LinkageError | RuntimeException failure) {
            MxpMod.LOGGER.warn("ANS Iron MXP settlement rejected: {}", failure.toString());
        }
    }

    // All positive legs must be mana; no alternative LP/aura or nonfinite mixed receipts.
    private static double manaLegs(Object legs) {
        if (!(legs instanceof Iterable<?> iterable)) return 0;
        double debit = 0;
        for (Object leg : iterable) {
            String unit = String.valueOf(MxpReflect.call(leg, "unit"));
            double amount = MxpReflect.number(MxpReflect.call(leg, "amount"));
            if (!Double.isFinite(amount) || amount < 0) return 0;
            if (amount == 0) continue;
            if (!unit.equals("ARS_MANA") && !unit.equals("IRONS_MANA")) return 0;
            debit += amount;
        }
        return Double.isFinite(debit) ? debit : 0;
    }

    /** Native callbacks are synchronous; discard incomplete observations at tick end. */
    public static void clear() { IRON.remove(); ARS.remove(); MANAGED_IRON.remove(); }

    private static void deliver(Cast cast, double firstPool, double actuallySpent) {
        if (!MxpPlayerPolicy.eligible(cast.player) || !Double.isFinite(firstPool) || firstPool <= 0) return;
        try {
            MxpService.resource(cast.player, cast.origin == MxpManaProbe.Origin.ARS
                    ? "ars_nouveau:spell" : "irons_spellbooks:spell",
                    cast.tx, cast.originalCost, firstPool, actuallySpent);
        } catch (RuntimeException e) {
            MxpMod.LOGGER.warn("MXP spell observation rejected: {}", e.toString(), e);
        }
    }
}
