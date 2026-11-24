package io.github.heatwaveqc.tyr.progression.runtime.taming;

import io.github.heatwaveqc.tyr.progression.mixin.CompanionMobGoalsAccess;
import io.github.heatwaveqc.tyr.progression.mixin.CompanionPatrolAccess;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.ai.goal.Goal;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import java.util.*;

/** Entity-owned permanent companionship. No world scans, spawn/clone, teleport,
 * force-loaded chunks, companion cap, XP proxy or arbitrary target packet. */
public final class TamingRuntime {
    private TamingRuntime(){}
    private static final TagKey<EntityType<?>> EXCLUDED=TagKey.create(Registries.ENTITY_TYPE,
        ResourceLocation.fromNamespaceAndPath("tyr_progression","boss_exempt"));
    private record Threat(UUID entity,ServerLevel level,long until){}
    // At most one recent threat per connected owner, not one entry per pet.
    private static final Map<UUID,Threat> THREATS=new HashMap<>();
    public static void register(){
        NeoForge.EVENT_BUS.addListener(EventPriority.LOW,TamingRuntime::interact);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST,TamingRuntime::target);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST,TamingRuntime::guardDamage);
        NeoForge.EVENT_BUS.addListener(TamingRuntime::recordDamage);
        NeoForge.EVENT_BUS.addListener(TamingRuntime::join);
        NeoForge.EVENT_BUS.addListener(TamingRuntime::tick);
        NeoForge.EVENT_BUS.addListener(TamingRuntime::logout);
        NeoForge.EVENT_BUS.addListener(TamingRuntime::stopped);
    }
    public static boolean hasCompanionReceipt(Mob mob){
        return mob.hasData(ProgressionAttachments.COMPANION.get());
    }
    private static boolean supported(Mob mob){
        return TamePolicy.supported(BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType()).toString())
            &&mob instanceof Raider;
    }
    private static Optional<TamePolicy.State> state(Mob mob){
        return !supported(mob)||!hasCompanionReceipt(mob)?Optional.empty():
            TamePolicy.decode(mob.getData(ProgressionAttachments.COMPANION.get()),mob.getUUID());
    }
    private static boolean canAdopt(Mob mob){
        if(!supported(mob)||!mob.isAlive()||mob.hasCustomName()||mob.isPassenger()||mob.isVehicle()
            ||mob.getType().is(EXCLUDED)||hasCompanionReceipt(mob)
            ||mob instanceof OwnableEntity owned&&owned.getOwnerUUID()!=null)return false;
        Raider raider=(Raider)mob;
        return !raider.hasRaid()&&!raider.isCaptain()&&!raider.isPatrolLeader()
            &&!raider.hasPatrolTarget()&&!((CompanionPatrolAccess)raider).tyr$isPatrolling();
    }
    private static long now(ServerLevel level){return level.getServer().overworld().getGameTime();}
    private static ServerPlayer owner(Mob mob,TamePolicy.State s){
        if(!(mob.level() instanceof ServerLevel level))return null;
        var p=level.getServer().getPlayerList().getPlayer(s.owner());
        return p!=null&&p.level()==level&&p.isAlive()&&!p.isSpectator()?p:null;
    }
    private static UUID nativeOwner(LivingEntity e){
        if(e instanceof OwnableEntity owned)return owned.getOwnerUUID();
        return null;
    }
    private static boolean protectedTarget(LivingEntity e,ServerPlayer p){
        return !(e instanceof Mob)||!(e instanceof Enemy)||e.getType().is(EXCLUDED)||!e.isAlive()
            ||e==p||p.isAlliedTo(e)||nativeOwner(e)!=null
            ||e instanceof Mob m&&hasCompanionReceipt(m);
    }
    private static LivingEntity threat(Mob mob,TamePolicy.State s){
        if(s.waiting()||!(mob.level() instanceof ServerLevel level))return null;
        var p=owner(mob,s);var t=THREATS.get(s.owner());
        if(p==null||t==null||t.level()!=level||!TamePolicy.threatFresh(now(level),t.until())
            ||mob.distanceToSqr(p)>32*32)return null;
        var e=level.getEntity(t.entity());
        return e instanceof LivingEntity target&&!protectedTarget(target,p)&&mob.distanceToSqr(target)<=32*32?target:null;
    }
    private static boolean allowed(Mob mob,TamePolicy.State s,LivingEntity candidate){
        return candidate!=null&&candidate==threat(mob,s);
    }
    private static void install(Mob mob){
        var goals=((CompanionMobGoalsAccess)mob).tyr$companionGoals();
        if(goals.getAvailableGoals().stream().noneMatch(w->w.getGoal() instanceof CompanionGoal))
            goals.addGoal(1,new CompanionGoal(mob));
        mob.setPersistenceRequired();
        if(mob instanceof Raider raider){
            raider.setCanJoinRaid(false);
            ((CompanionPatrolAccess)raider).tyr$setPatrolling(false);
        }
    }
    private static void stop(Mob mob){
        mob.setTarget(null);
        mob.getNavigation().stop();
        mob.setAggressive(false);
    }
    private static void interact(PlayerInteractEvent.EntityInteract event){
        if(event.isCanceled()||event.getHand()!=InteractionHand.MAIN_HAND
            ||!(event.getEntity() instanceof ServerPlayer p)||!MxpPlayerPolicy.eligible(p)
            ||!p.isAlive()||!p.isCrouching()||!p.getMainHandItem().isEmpty()
            ||!(event.getTarget() instanceof Mob mob)||mob.level()!=p.level()
            ||!p.canInteractWithEntity(mob,0)||!p.hasLineOfSight(mob))return;
        // Ownership commands remain available after Path departure; acquisition does not.
        if(hasCompanionReceipt(mob)){
            var s=state(mob).orElse(null);
            if(s==null||!s.belongsTo(p.getUUID()))return;
            var next=s.toggle();
            mob.setData(ProgressionAttachments.COMPANION.get(),next.encode());
            stop(mob);
            handled(event,p,next.waiting()?"Companion waiting.":"Companion following.");
            return;
        }
        if(NewbieMode.isNewbie(p)||!canAdopt(mob))return;
        var effects=EffectRuntime.active(p).stream().filter(a->a.effect().type().equals("tame"))
            .sorted(Comparator.comparing(EffectRuntime.Active::id)).toList();
        for(var a:effects){
            var fx=a.effect();var spec=TamePolicy.validate(fx);
            if(!Selectors.entity(fx.selector(),mob))continue;
            if(spec.cost()>0&&!PathResources.spend(p,spec.resource(),spec.cost(),"tame/"+mob.getUUID()))continue;
            // Existing mob, existing UUID/equipment, no entity creation.
            var s=new TamePolicy.State(p.getUUID(),mob.getUUID(),false);
            mob.setData(ProgressionAttachments.COMPANION.get(),s.encode());
            if(mob.hasData(ProgressionAttachments.CHARM_UNTIL.get()))
                mob.removeData(ProgressionAttachments.CHARM_UNTIL.get());
            install(mob);stop(mob);
            handled(event,p,"Tamed "+mob.getDisplayName().getString()+". Sneak-right-click to wait/follow.");
            return;
        }
    }
    private static void handled(PlayerInteractEvent.EntityInteract event,ServerPlayer p,String message){
        event.setCancellationResult(InteractionResult.SUCCESS);event.setCanceled(true);
        p.displayClientMessage(Component.literal(message),true);
    }
    private static void join(EntityJoinLevelEvent event){
        if(!event.getLevel().isClientSide()&&event.getEntity() instanceof Mob mob
            &&state(mob).isPresent()){install(mob);stop(mob);}
    }
    private static void tick(EntityTickEvent.Pre event){
        if(!(event.getEntity() instanceof Mob mob)||!(mob.level() instanceof ServerLevel)
            ||!hasCompanionReceipt(mob))return;
        var s=state(mob).orElse(null);
        if(s==null)return; // Invalid copied receipt never grants a new owner.
        if(mob instanceof Raider raider)raider.setCanJoinRaid(false);
        // Native goals still handle melee/crossbow attacks, but may only target
        // the recent actual hostile actor selected for the owner.
        LivingEntity wanted=threat(mob,s);
        if(mob.getTarget()!=wanted){
            mob.setTarget(wanted);
            if(wanted==null)mob.getNavigation().stop();
        }
        if(wanted==null&&mob.isAggressive())mob.setAggressive(false);
    }
    private static void target(LivingChangeTargetEvent event){
        if(!(event.getEntity() instanceof Mob mob)||!(mob.level() instanceof ServerLevel))return;
        var s=state(mob).orElse(null);
        if(s!=null&&event.getNewAboutToBeSetTarget()!=null&&!allowed(mob,s,event.getNewAboutToBeSetTarget()))
            event.setNewAboutToBeSetTarget(null);
        // Native owner pets must not acquire their own companion as a target.
        if(event.getNewAboutToBeSetTarget() instanceof Mob target){
            var t=state(target).orElse(null);
            if(t!=null&&t.belongsTo(nativeOwner(mob)))event.setNewAboutToBeSetTarget(null);
        }
    }
    private static void guardDamage(LivingIncomingDamageEvent event){
        if(event.getEntity().level().isClientSide())return;
        var source=event.getSource().getEntity();
        if(source instanceof Mob mob){
            var s=state(mob).orElse(null);
            if(s!=null&&!allowed(mob,s,event.getEntity())){event.setCanceled(true);return;}
        }
        if(event.getEntity() instanceof Mob mob){
            var s=state(mob).orElse(null);
            if(s!=null&&(source instanceof ServerPlayer p&&s.belongsTo(p.getUUID())
                ||source instanceof LivingEntity e&&s.belongsTo(nativeOwner(e))))
                event.setCanceled(true);
        }
    }
    private static void recordDamage(LivingDamageEvent.Post event){
        if(!Float.isFinite(event.getNewDamage())||event.getNewDamage()<=0)return;
        if(event.getEntity() instanceof ServerPlayer p&&event.getSource().getEntity() instanceof LivingEntity enemy)
            record(p,enemy);
        else if(event.getSource().getEntity() instanceof ServerPlayer p)
            record(p,event.getEntity());
    }
    private static void record(ServerPlayer p,LivingEntity target){
        if(!MxpPlayerPolicy.eligible(p)||!(p.level() instanceof ServerLevel level)
            ||target.level()!=level||protectedTarget(target,p))return;
        long now=now(level);
        if(now>=0&&now<=Long.MAX_VALUE-TamePolicy.THREAT_TICKS)
            THREATS.put(p.getUUID(),new Threat(target.getUUID(),level,now+TamePolicy.THREAT_TICKS));
    }
    private static void logout(PlayerEvent.PlayerLoggedOutEvent event){THREATS.remove(event.getEntity().getUUID());}
    private static void stopped(ServerStoppedEvent event){THREATS.clear();}
    /** Idle/follow/wait navigation only. Yield MOVE/LOOK to native combat while a
     * verified threat is current. Float/emergency native goal priority 0 wins. */
    private static final class CompanionGoal extends Goal{
        private final Mob mob;
        CompanionGoal(Mob mob){this.mob=mob;setFlags(EnumSet.of(Flag.MOVE,Flag.LOOK));}
        @Override public boolean canUse(){var s=state(mob).orElse(null);return s!=null&&threat(mob,s)==null;}
        @Override public boolean canContinueToUse(){return canUse();}
        @Override public boolean requiresUpdateEveryTick(){return true;}
        @Override public void start(){mob.getNavigation().stop();}
        @Override public void stop(){mob.getNavigation().stop();}
        @Override public void tick(){
            var s=state(mob).orElse(null);if(s==null)return;
            var p=owner(mob,s);
            if(s.waiting()||p==null||mob.distanceToSqr(p)<=3*3||mob.distanceToSqr(p)>64*64){
                mob.getNavigation().stop();return;
            }
            if(mob.tickCount%10==0)mob.getNavigation().moveTo(p,1.0);
        }
    }
}
