package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.client.ClientPassivePowers;
import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import java.util.Set;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Equipment-free vanilla Elytra via the same NeoForge canElytraFly /
 * elytraFlightTick hooks already used by the modpack. Only the permission
 * transport changes: it is now a server-owned, client-synced passive power,
 * NOT a MobEffect. No mayfly, fake Elytra, wing armor or status HUD entry.
 *
 * Origins' natural gliding and JDT's upgrade are behavioral precedents,
 * NOT dependencies or new flight systems.
 */
public final class VirtualElytra {
    public static final String POWER = "tyr_progression:elytra_glide";
    private static final String NEWBIE = "tyr_progression:newbie";
    private static final String MONK = "tyr_progression:monk_momentum_glide";

    /**
     * Deprecated read-only registration retained for old saved MobEffect IDs.
     * New code NEVER grants this effect. Reconcile removes it on first login,
     * and continuously during the transition so it cannot flash in the HUD.
     */
    private static final DeferredRegister<MobEffect> LEGACY =
            DeferredRegister.create(Registries.MOB_EFFECT, ProgressionMod.MOD_ID);
    private static final DeferredHolder<MobEffect, MobEffect> OLD_ACCESS =
            LEGACY.register("virtual_elytra", () ->
                    new MobEffect(MobEffectCategory.BENEFICIAL, 0x7fbad6) {});

    private VirtualElytra() {}

    public static void register(IEventBus bus) {
        LEGACY.register(bus);
        PassivePowers.register(POWER, VirtualElytra::sources);
    }

    /** Resolves real entitlement on each authoritative server query. */
    public static Set<String> sources(ServerPlayer player) {
        if (player == null || !player.isAlive() || player.isCreative() || player.isSpectator())
            return Set.of();
        if (NewbieMode.isNewbie(player)) return Set.of(NEWBIE);
        if (ClassAbilities.unlocked(player, "momentum_glide")
                && "on".equals(ProgressionApi.mode(player,
                        "tyr_progression:momentum_glide", "off"))) return Set.of(MONK);
        return Set.of();
    }

    /** Compatibility API for the existing Monk gentle propulsion logic. */
    public static boolean eligible(ServerPlayer player) {
        return !sources(player).isEmpty();
    }

    /** Vanilla item/other providers may still grant Elytra; this is additive. */
    public static boolean canGlide(LivingEntity wearer) {
        if (wearer instanceof ServerPlayer server)
            return PassivePowers.active(server, POWER);
        if (wearer.level().isClientSide())
            return ClientPassivePowers.active(wearer, POWER);
        return false;
    }

    public static void reconcile(ServerPlayer player) {
        reconcile(player, false);
    }

    public static void reconcile(ServerPlayer player, boolean force) {
        if (player.hasEffect(OLD_ACCESS)) player.removeEffect(OLD_ACCESS);
        PassivePowers.reconcile(player, force);
    }

    public static void revoke(ServerPlayer player) {
        // Existing callers revoke after their own authoritative mode changes;
        // re-project all remaining authorized sources rather than erase them.
        reconcile(player, true);
    }
}
