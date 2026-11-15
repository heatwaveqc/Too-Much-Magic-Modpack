package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
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
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::consumedFood);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::entityInteract);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::blockDrops);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::projectileImpact);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::equipmentChange);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::jump);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::advancement);
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
    private static void consumedFood(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        // getItem() is the original pre-consumption stack; successful completion only.
        if (event.getItem().getFoodProperties(player) == null) return;
        ProgressionApi.recordConsumedFood(player,event.getItem());
        EffectHandlers.foodConsumed(player,event.getItem());
    }
    private static void entityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || event.isCanceled()) return;
        if (!EffectRuntime.animalFriendship(player,event))
            EffectHandlers.entityInteract(player,event);
    }
    /** Loot-aware callback needed for Monk Glass Touch and Treasure Hand; no generic drop mutation. */
    private static void blockDrops(BlockDropsEvent event) {
        if (!event.isCanceled() && event.getBreaker() instanceof ServerPlayer player)
            EffectHandlers.blockDrops(player,event);
    }
    private static void projectileImpact(ProjectileImpactEvent event) {
        if (event.getRayTraceResult() instanceof EntityHitResult hit && hit.getEntity() instanceof ServerPlayer defender)
            EffectHandlers.projectileImpact(defender,event);
    }
    private static void equipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof ServerPlayer player)
            EffectHandlers.equipmentChange(player,event);
    }
    private static void jump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof ServerPlayer player)
            EffectHandlers.jump(player,event);
    }
    private static void advancement(AdvancementEvent.AdvancementEarnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player)
            ProgressionApi.record(player,new io.github.heatwaveqc.tmm.progression.core.Signal("advancement",
                event.getAdvancement().id().toString(),java.util.Set.of()));
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
            EffectRuntime.tick(player);
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
