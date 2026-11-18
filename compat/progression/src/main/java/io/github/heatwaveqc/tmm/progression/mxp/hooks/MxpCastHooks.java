package io.github.heatwaveqc.tmm.progression.mxp.hooks;

import io.github.heatwaveqc.tmm.progression.mxp.MxpMod;
import io.github.heatwaveqc.tmm.progression.mxp.MxpService;
import io.github.heatwaveqc.tmm.progression.mxp.MxpSourceConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
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

    private MxpCastHooks() {}

    /** Suppress Botania source awards while its items merely fund another spell. */
    public static boolean isProcessingSpell() {
        return IRON.get() != null || ARS.get() != null;
    }

    /** Invoked by the Iron's SpellOnCastEvent constructor after its fields are set. */
    public static void ironQuoted(Object event) {
        Object player = MxpReflect.call(event, "getEntity");
        if (!(player instanceof ServerPlayer sp) || sp.isCreative()) return;
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
        if (pending == null || pending.player.level().getGameTime() < pending.tick) return;
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
        if (!(caster instanceof ServerPlayer sp) || sp.isCreative()) return;
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
        if (pending == null) return;
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
        if (!handled || !(player instanceof ServerPlayer sp) || sp.isCreative()) return;
        try {
            Class<?> owner = Class.forName(
                    "com.otectus.arsnspells.casting.ArsCastPayments");
            Object map = MxpReflect.field(owner, "PLANS");
            if (!(map instanceof Map<?, ?> plans)) return;
            Object plan = plans.get(context);
            if (plan == null || !Boolean.TRUE.equals(MxpReflect.field(plan, "committed"))
                    || MxpReflect.field(plan, "alternative") != null
                    || MxpReflect.field(plan, "attempt") == null) return;
            Object quote = MxpReflect.field(plan, "quote");
            Object legs = MxpReflect.call(quote, "legs");
            if (!(legs instanceof Iterable<?> iterable)) return;
            double debit = 0;
            for (Object leg : iterable) {
                Object unit = MxpReflect.call(leg, "unit");
                String id = String.valueOf(unit);
                if (id.equals("ARS_MANA") || id.equals("IRONS_MANA")) {
                    debit += MxpReflect.number(MxpReflect.call(leg, "amount"));
                }
            }
            if (!Double.isFinite(debit) || debit <= 0) return;
            Object spell = MxpReflect.call(context, "getSpell");
            double nominal = MxpReflect.number(MxpReflect.call(spell, "getCost"));
            if (!Double.isFinite(nominal) || nominal <= 0) return;
            Cast current = ARS.get();
            Cast parent = IRON.get();
            String tx = parent != null && parent.player == sp ? parent.tx
                    : current != null && current.context == context
                        ? current.tx : "cast:" + UUID.randomUUID();
            Cast settled = new Cast(sp, nominal, Double.NaN,
                    MxpManaProbe.Origin.ARS, tx, sp.level().getGameTime(), context);
            deliver(settled, MxpSourceConfig.ARS_FIRST_POOL.get(), debit);
            if (current != null && current.context == context) ARS.remove();
        } catch (ClassNotFoundException | LinkageError failure) {
            MxpMod.LOGGER.warn("ANS MXP settlement introspection unavailable: {}", failure.toString());
        }
    }

    private static void deliver(Cast cast, double firstPool, double actuallySpent) {
        if (!Double.isFinite(firstPool) || firstPool <= 0) return;
        try {
            MxpService.resource(cast.player, cast.origin == MxpManaProbe.Origin.ARS
                    ? "ars_nouveau:spell" : "irons_spellbooks:spell",
                    cast.tx, cast.originalCost, firstPool, actuallySpent);
        } catch (RuntimeException e) {
            MxpMod.LOGGER.warn("MXP spell observation rejected: {}", e.toString(), e);
        }
    }
}
