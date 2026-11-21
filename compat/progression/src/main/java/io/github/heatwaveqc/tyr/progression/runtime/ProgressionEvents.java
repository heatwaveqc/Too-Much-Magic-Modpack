package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import net.minecraft.server.level.ServerPlayer;
import io.github.heatwaveqc.tyr.progression.runtime.Enrollment;
import io.github.heatwaveqc.tyr.progression.network.AbilityNetwork;
import io.github.heatwaveqc.tyr.progression.network.ProgressionMenuNetwork;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/** Server-side game hooks. Source XP is awarded only by configured events. */
public final class ProgressionEvents {
    private ProgressionEvents() {}
    public static void register() {
        TreeChopXpBridge.register();
        GildedMechanics.register();
        NeoForge.EVENT_BUS.addListener(net.neoforged.bus.api.EventPriority.LOWEST,MiningUtilities::observe);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::breakBlock);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::kill);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::mobDrops);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::fall);
        NeoForge.EVENT_BUS.addListener(net.neoforged.bus.api.EventPriority.LOWEST, MechanicalEffects::landing);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::consumedFood);
        NeoForge.EVENT_BUS.addListener(UtilityEffects::foodTick);
        NeoForge.EVENT_BUS.addListener(UtilityEffects::foodStopped);
        NeoForge.EVENT_BUS.addListener(UtilityEffects::vanillaXp);
        NeoForge.EVENT_BUS.addListener(UtilityEffects::immunity);
        NeoForge.EVENT_BUS.addListener(UtilityEffects::crafted);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::useTick);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::entityInteract);
        NeoForge.EVENT_BUS.addListener(BedrockRest::interact);
        NeoForge.EVENT_BUS.addListener(BedrockRest::continueSleeping);
        NeoForge.EVENT_BUS.addListener(BedrockRest::wake);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::pickup);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::blockDrops);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::projectileImpact);
        NeoForge.EVENT_BUS.addListener(net.neoforged.bus.api.EventPriority.LOWEST, GrowthEffects::breed);
        NeoForge.EVENT_BUS.addListener(net.neoforged.bus.api.EventPriority.LOWEST, GrowthEffects::bonemeal);
        NeoForge.EVENT_BUS.addListener(GrowthEffects::finish);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::equipmentChange);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::targetChange);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::jump);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::advancement);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::incoming);
        NeoForge.EVENT_BUS.addListener(MechanicalEffects::postDamage);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::breakSpeed);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::harvest);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::tick);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::logout);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::respawn);
        NeoForge.EVENT_BUS.addListener(ProgressionEvents::dimension);
    }

    private static void breakBlock(BlockEvent.BreakEvent event) {
        if (!event.isCanceled() && event.getPlayer() instanceof ServerPlayer player
            && !event.getState().isAir()) {
            // The break event occurs at the verified block-breaking attempt.
            // Modded bulk mining may need a post-break adapter to count all actual blocks.
            // XP is NOT earned here: this is pre-commit and can still be vetoed.
            EffectHandlers.blockBreak(player,event);
        }
    }
    private static void kill(LivingDeathEvent event) {
        if(event.isCanceled())return;
        // One Hunter settlement for native player or exact scoped magical damage only.
        // This does not expand the native branch's other kill/weapon/loot/class effects.
        ServerPlayer hunter=OwnedMagicDamage.hunterOwner(event.getEntity(),event.getSource());
        var credited=hunter==null?java.util.Set.<String>of():HunterExperience.kill(hunter,event.getEntity());
        if (event.getSource().getEntity() instanceof ServerPlayer player
            && event.getEntity() != player) {
            ProgressionApi.recordFiltered(player,Selectors.kill(event.getEntity()),
                path -> HunterExperience.allowsMilestone(path.id(),credited));
            if (event.getSource().getDirectEntity() == player
                    && player.getMainHandItem().isEmpty())
                ProgressionApi.recordFiltered(player,Selectors.unarmedKill(event.getEntity()),path->HunterExperience.allowsMilestone(path.id(),credited));
            ClassAbilities.creditedKill(player,event);
            EffectHandlers.kill(player,event);
            MechanicalEffects.kill(player,event);
        }
    }
    private static void mobDrops(LivingDropsEvent event) {
        if (!event.isCanceled()) UtilityEffects.mobDrops(event);
    }
    private static void consumedFood(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        // Detect completed vanilla potion drinking independently of whether it counts as food.
        UtilityEffects.foodFinished(player,event.getItem());
        Enrollment.potionFinished(player,event.getItem());
        HunterExperience.potionFinished(player,event.getItem());
        // getItem() is the original pre-consumption stack; successful completion only.
        if (event.getItem().getFoodProperties(player) == null) return;
        Enrollment.foodFinished(player,event.getItem());
        ProgressionApi.recordConsumedFood(player,event.getItem());
        EffectHandlers.foodConsumed(player,event.getItem());
    }
    /** Server-authoritative bow draw and crossbow charging adjustments. */
    private static void useTick(LivingEntityUseItemEvent.Tick event) {
        if (event.getEntity() instanceof ServerPlayer player && !event.isCanceled()) {
            EffectRuntime.useTick(player,event);
            EffectHandlers.useTick(player,event);
        }
    }
    private static void pickup(ItemEntityPickupEvent.Pre event) {
        if (event.getPlayer() instanceof ServerPlayer p) ClassAbilities.pickup(p,event);
    }
    private static void entityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || event.isCanceled()) return;
        if (!ClassAbilities.entityInteract(player,event)
                && !EffectRuntime.animalFriendship(player,event))
            EffectHandlers.entityInteract(player,event);
    }
    /** Loot-aware callback needed for Monk Glass Touch and Treasure Hand; no generic drop mutation. */
    private static void blockDrops(BlockDropsEvent event) {
        if (!event.isCanceled() && event.getBreaker() instanceof ServerPlayer player) {
            // TreeChop and normal drops can report the same physical log.
            if (!event.getState().is(net.minecraft.tags.BlockTags.LOGS)
                    || RecentBlockCredits.first(player.getUUID(),
                    event.getLevel().dimension().location().toString(),
                    event.getPos().asLong(),event.getLevel().getGameTime())) {
                ProgressionApi.record(player,Selectors.blockBreak(event.getState()));
                if (event.getTool().isEmpty() && player.getMainHandItem().isEmpty())
                    ProgressionApi.record(player,Selectors.barehandBlockBreak(event.getState()));
            }
            HarvestAbilities.blockDrops(player,event);
            GildedMechanics.ore(player,event);
            UtilityEffects.crops(player,event);
            EffectHandlers.blockDrops(player,event);
        }
    }
    private static void projectileImpact(ProjectileImpactEvent event) {
        if (event.getRayTraceResult() instanceof EntityHitResult hit && hit.getEntity() instanceof ServerPlayer defender)
            { ClassAbilities.arrowImpact(defender,event);
              if (!event.isCanceled()) MechanicalEffects.deflect(defender,event);
              if (!event.isCanceled()) EffectHandlers.projectileImpact(defender,event); }
    }
    private static void equipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof ServerPlayer player)
            EffectHandlers.equipmentChange(player,event);
    }
    private static void targetChange(LivingChangeTargetEvent event) {
        if (event.getNewAboutToBeSetTarget() instanceof ServerPlayer player)
            { ClassAbilities.targetChange(player,event);
              EffectHandlers.targetChange(player,event); }
    }
    private static void jump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof ServerPlayer player)
            { ClassAbilities.jump(player,event);
              EffectHandlers.jump(player,event); }
    }
    private static void advancement(AdvancementEvent.AdvancementEarnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        String path = event.getAdvancement().id().getPath();
        if (path.startsWith("recipes/") || path.contains("/recipes/")) return;
        ProgressionApi.record(player,new io.github.heatwaveqc.tyr.progression.core.Signal(
                "advancement",event.getAdvancement().id().toString(),java.util.Set.of()));
        String id=event.getAdvancement().id().toString();
        if(io.github.heatwaveqc.tyr.progression.foundation.BossRewardPolicy.dragonAdvancementKey(id)!=null
            &&io.github.heatwaveqc.tyr.progression.mxp.MxpMod.active()){
            try {
                var receipt=io.github.heatwaveqc.tyr.progression.mxp.MxpService.dragonAdvancement(player,id);
                if(receipt.accepted())player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                    "MXP: "+String.format("%.2f",receipt.effectiveXP())+" (dragon milestone)"));
            } catch(RuntimeException ex) {
                io.github.heatwaveqc.tyr.progression.mxp.MxpMod.LOGGER.error("Dragon advancement MXP failed ({})",id,ex);
            }
        }
    }
    private static void fall(LivingFallEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ClassAbilities.fall(player,event);
            EffectHandlers.fall(player,event);
        }
    }
    private static void incoming(LivingIncomingDamageEvent event) {
        if (event.isCanceled()) return;
        ClassAbilities.incoming(event);
        if(!event.isCanceled())GildedMechanics.fireDamage(event);
        if (event.isCanceled()) return;
        EffectRuntime.incoming(event);
        if (!event.isCanceled()) EffectHandlers.incoming(event);
        if (!event.isCanceled()) MechanicalEffects.incoming(event);
        if (!event.isCanceled()) io.github.heatwaveqc.tyr.progression.runtime.forms.LichServantEffects.incoming(event);
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
            if (player.tickCount % 10 == 0) { SkillActivation.reconcile(player); EffectRuntime.reconcile(player); }
            Enrollment.tick(player);
            if (player.tickCount % 20 == 0) HunterMutations.tick(player);
            ClassAbilities.tick(player);
            io.github.heatwaveqc.tyr.progression.runtime.forms.JawrBloodCapacity.tick(player);
            io.github.heatwaveqc.tyr.progression.runtime.forms.LichSoulCapacity.tick(player);
            GildedMechanics.tick(player);
            EffectRuntime.tick(player);
            EffectHandlers.tick(player);
            MechanicalEffects.tick(player);
            MiningUtilities.tick(player);
            VillagerAttraction.tick(player);
            if(player.tickCount%10==0){
                VisionRuntime.reconcile(player,false);
                io.github.heatwaveqc.tyr.progression.runtime.forms.DietPermissions.sync(player,false);
            }
        }
    }
    private static void respawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Enrollment.clear(player);
            BedrockRest.forget(player);
            ClassAbilities.forget(player);
            MechanicalEffects.forget(player);
            UtilityEffects.forget(player);
            MiningUtilities.forget(player);
            VisionRuntime.forget(player);
            io.github.heatwaveqc.tyr.progression.runtime.forms.DietPermissions.forget(player);
            RecentBlockCredits.forget(player.getUUID());
            TreeChopXpBridge.forget(player);
            AbilityNetwork.forget(player);
            ProgressionMenuNetwork.forget(player);
            SkillActivation.stopAll(player);
            EffectRuntime.reconcile(player);
            VisionRuntime.reconcile(player,true);
            io.github.heatwaveqc.tyr.progression.runtime.forms.DietPermissions.sync(player,true);
        }
    }
    private static void dimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            MiningUtilities.forget(player);
            Enrollment.clear(player);
            BedrockRest.forget(player);
            VisionRuntime.reconcile(player,true);
            io.github.heatwaveqc.tyr.progression.runtime.forms.DietPermissions.sync(player,true);
        }
    }
    private static void logout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            EffectRuntime.logout(player);
            ProgressionApi.forgetTransient(player.getUUID());
            Enrollment.clear(player);
            BedrockRest.forget(player);
            ClassAbilities.forget(player);
            MechanicalEffects.forget(player);
            UtilityEffects.forget(player);
            MiningUtilities.forget(player);
            VisionRuntime.forget(player);
            io.github.heatwaveqc.tyr.progression.runtime.forms.DietPermissions.forget(player);
            RecentBlockCredits.forget(player.getUUID());
            TreeChopXpBridge.forget(player);
            AbilityNetwork.forget(player);
            ProgressionMenuNetwork.forget(player);
        }
    }
}
