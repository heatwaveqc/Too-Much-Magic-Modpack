package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.ProgressionMod;
import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Iron's Angel Wings / Ars Glide pattern: a synced, invisible mob-effect flag
 * makes vanilla's canElytraFly + elytraFlightTick accept a player without Elytra.
 * The vanilla client still initiates glide via the ordinary second jump keypress.
 * Never grant creative flight, replace equipment, or copy inventory items.
 */
public final class VirtualElytra {
    private static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, ProgressionMod.MOD_ID);
    public static final DeferredHolder<MobEffect, MobEffect> ACCESS =
            EFFECTS.register("virtual_elytra", () ->
                    new MobEffect(MobEffectCategory.BENEFICIAL, 0x7fbad6) {});

    private VirtualElytra() {}

    public static void register(IEventBus bus) {
        EFFECTS.register(bus);
    }

    /** Client needs only the vanilla-synchronized effect, never the progression ledger. */
    public static boolean canGlide(LivingEntity player) {
        return player.hasEffect(ACCESS);
    }

    public static boolean eligible(ServerPlayer player) {
        if (player.isCreative() || player.isSpectator()) return false;
        if (NewbieMode.isNewbie(player)) return true;
        // Original Monk progression requirements and player toggle remain authoritative.
        return ClassAbilities.unlocked(player, "momentum_glide")
                && "on".equals(ProgressionApi.mode(player,
                    "tmm_progression:momentum_glide", "off"));
    }

    /**
     * Never persist a stale permission through graduation or skill respec.
     * Hidden, particle-free effect is synchronized by vanilla effect packets.
     * A short renewal interval avoids sending a packet on every tick.
     */
    public static void reconcile(ServerPlayer player) {
        boolean wanted = eligible(player);
        MobEffectInstance current = player.getEffect(ACCESS);
        if (!wanted) {
            if (current != null) player.removeEffect(ACCESS);
            return;
        }
        if (current == null || current.getDuration() <= 70)
            player.addEffect(new MobEffectInstance(ACCESS, 180, 0, true, false, false));
    }

    public static void revoke(ServerPlayer player) {
        if (player.hasEffect(ACCESS)) player.removeEffect(ACCESS);
    }
}
