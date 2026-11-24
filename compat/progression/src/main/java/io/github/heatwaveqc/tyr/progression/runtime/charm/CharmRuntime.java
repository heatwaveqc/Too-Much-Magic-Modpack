package io.github.heatwaveqc.tyr.progression.runtime.charm;

import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpPlayerPolicy;
import io.github.heatwaveqc.tyr.progression.runtime.EffectRuntime;
import io.github.heatwaveqc.tyr.progression.runtime.NewbieMode;
import io.github.heatwaveqc.tyr.progression.runtime.Selectors;
import io.github.heatwaveqc.tyr.progression.runtime.resources.PathResources;
import io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.raid.Raider;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import java.util.Comparator;

/** Temporary pacification, not taming. No client-specified target/amount RPC,
 * no permanent allegiance, no noAI flag and no goal deletion. */
public final class CharmRuntime {
    private CharmRuntime(){}
    private static final String COOLDOWN="tyr_progression:charm_cooldown";
    private static final TagKey<EntityType<?>> EXCLUDED=TagKey.create(Registries.ENTITY_TYPE,
        ResourceLocation.fromNamespaceAndPath("tyr_progression","boss_exempt"));
    public static void register(){
        // Existing entity interactions and cancellation/permission handlers get first opportunity.
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST,CharmRuntime::interact);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST,CharmRuntime::target);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST,CharmRuntime::outgoing);
        NeoForge.EVENT_BUS.addListener(CharmRuntime::damage);
        NeoForge.EVENT_BUS.addListener(CharmRuntime::tick);
    }
    private static long now(Mob mob){
        return ((ServerLevel)mob.level()).getServer().overworld().getGameTime();
    }
    private static boolean supported(Mob mob){
        return mob.isAlive()&&!mob.hasCustomName()&&!mob.isPassenger()&&!mob.isVehicle()
            &&!mob.getType().is(EXCLUDED)
            &&CharmPolicy.supported(BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType()).toString())
            &&!(mob instanceof Raider raider&&(raider.hasActiveRaid()||raider.isCaptain()));
    }
    private static boolean charmed(Mob mob){
        if(!(mob.level() instanceof ServerLevel)||!mob.hasData(ProgressionAttachments.CHARM_UNTIL.get()))return false;
        return supported(mob)&&CharmPolicy.active(now(mob),mob.getData(ProgressionAttachments.CHARM_UNTIL.get()));
    }
    private static void clear(Mob mob){
        if(mob.hasData(ProgressionAttachments.CHARM_UNTIL.get()))
            mob.removeData(ProgressionAttachments.CHARM_UNTIL.get());
    }
    private static long cooldown(ServerPlayer p){
        try{return Long.parseLong(ProgressionApi.mode(p,COOLDOWN,"0"));}
        catch(NumberFormatException invalid){return 0;}
    }
    private static void interact(PlayerInteractEvent.EntityInteract event){
        if(event.isCanceled()||event.getHand()!=InteractionHand.MAIN_HAND
            ||!(event.getEntity() instanceof ServerPlayer p)||!MxpPlayerPolicy.eligible(p)
            ||!p.isAlive()||NewbieMode.isNewbie(p)||!p.getMainHandItem().isEmpty()
            ||!(event.getTarget() instanceof Mob mob)||mob.level()!=p.level()||!supported(mob)
            ||!p.canInteractWithEntity(mob,0)||!p.hasLineOfSight(mob)||charmed(mob))return;
        long now=now(mob);
        if(!CharmPolicy.cooldownReady(now,cooldown(p)))return;
        // Stable choice: one eligible effect and one charge, never stack several
        // Path skills on the same click. A paid option that cannot settle may be skipped.
        var effects=EffectRuntime.active(p).stream().filter(a->a.effect().type().equals("charm"))
            .sorted(Comparator.comparing(EffectRuntime.Active::id)).toList();
        for(var a:effects){
            var fx=a.effect();var spec=CharmPolicy.validate(fx);
            if((spec.sneaking()&&!p.isCrouching())||!Selectors.entity(fx.selector(),mob))continue;
            long expiry=CharmPolicy.until(now,CharmPolicy.DURATION);
            long nextUse=CharmPolicy.until(now,spec.cooldown());
            if(spec.cost()>0&&!PathResources.spend(p,spec.resource(),spec.cost(),
                "charm/"+mob.getUUID()+"/"+now))continue;
            // All target and affordability checks precede mutation. This changes
            // only local entity state; no spawn, conversion or inventory transaction.
            mob.setData(ProgressionAttachments.CHARM_UNTIL.get(),expiry);
            ProgressionApi.setMode(p,COOLDOWN,Long.toString(nextUse));
            mob.setTarget(null);
            mob.getNavigation().stop();
            mob.setAggressive(false);
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
            p.displayClientMessage(Component.literal("Charmed "+mob.getDisplayName().getString()+" for 30 seconds."),true);
            return;
        }
    }
    private static void target(LivingChangeTargetEvent event){
        if(event.getEntity() instanceof Mob mob&&event.getNewAboutToBeSetTarget()!=null&&charmed(mob))
            event.setNewAboutToBeSetTarget(null);
    }
    private static void tick(EntityTickEvent.Pre event){
        if(!(event.getEntity() instanceof Mob mob)||!(mob.level() instanceof ServerLevel)
            ||!mob.hasData(ProgressionAttachments.CHARM_UNTIL.get()))return;
        if(!charmed(mob)){clear(mob);return;}
        if(mob.getTarget()!=null){
            mob.setTarget(null);
            mob.getNavigation().stop();
        }
        if(mob.isAggressive())mob.setAggressive(false);
    }
    /** Damage backstop for attacks already queued by native goals. Does not
     * make the charmed mob invulnerable, block environment damage or alter PvP. */
    private static void outgoing(LivingIncomingDamageEvent event){
        if(event.getSource().getEntity() instanceof Mob mob&&mob!=event.getEntity()&&charmed(mob))
            event.setCanceled(true);
    }
    private static void damage(LivingDamageEvent.Post event){
        if(event.getEntity() instanceof Mob mob&&mob.level() instanceof ServerLevel
            &&CharmPolicy.breaksOnDamage(event.getNewDamage()))clear(mob);
    }
}
