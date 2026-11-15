package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/** Server-side game hooks. Source XP is awarded only by configured events. */
public final class ProgressionEvents {
    private ProgressionEvents() {}
    public static void register() {
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::breakBlock);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::kill);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::incoming);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::breakSpeed);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::harvest);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::tick);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::logout);
    }

    private static void breakBlock(BlockEvent.BreakEvent event) {
        if (!event.isCanceled() && event.getPlayer() instanceof ServerPlayer player
            && !event.getState().isAir()) {
            // The break event occurs at the verified block-breaking attempt.
            // Modded bulk mining may need a post-break adapter to count all actual blocks.
            ProgressionApi.record(player,Selectors.blockBreak(event.getState()));
            EffectHandlers.blockBreak(player,event);
        }
    }
    private static void kill(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player
            && event.getEntity() != player) {
            ProgressionApi.record(player,Selectors.kill(event.getEntity()));
            EffectHandlers.kill(player,event);
        }
    }
    private static void incoming(LivingIncomingDamageEvent event) {
        EffectRuntime.incoming(event);
        EffectHandlers.incoming(event);
    }
    private static void breakSpeed(PlayerEvent.BreakSpeed event) {
        EffectRuntime.breakSpeed(event);
        if (event.getEntity() instanceof ServerPlayer player)
            EffectHandlers.breakSpeed(player,event);
    }
    private static void harvest(PlayerEvent.HarvestCheck event) {
        EffectRuntime.harvest(event);
        if (event.getEntity() instanceof ServerPlayer player)
            EffectHandlers.harvest(player,event);
    }
    private static void tick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.tickCount % 10 == 0) EffectRuntime.reconcile(player);
            EffectHandlers.tick(player);
        }
    }
    private static void logout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            EffectRuntime.logout(player);
            ProgressionApi.forgetTransient(player.getUUID());
        }
    }
}
