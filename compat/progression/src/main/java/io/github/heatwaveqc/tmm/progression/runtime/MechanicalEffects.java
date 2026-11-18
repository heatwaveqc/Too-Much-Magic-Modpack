package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.data.MechanicalSpec;
import io.github.heatwaveqc.tmm.progression.foundation.MagnetMotion;
import io.github.heatwaveqc.tmm.progression.model.Definition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import java.util.*;
import java.util.function.BiPredicate;

/** Shared event-based mechanics. No skill/tree names, no inventory teleport and no runtime grants. */
public final class MechanicalEffects {
    private MechanicalEffects(){}
    private static final Map<UUID,Map<String,Long>> COOLDOWNS=new HashMap<>();
    private static final List<BiPredicate<ServerPlayer,ItemEntity>> MAGNET_GUARDS=new java.util.concurrent.CopyOnWriteArrayList<>();
    /** Claim/provider bridges can veto attraction; vanilla reservation is always honored. */
    public static void registerMagnetGuard(BiPredicate<ServerPlayer,ItemEntity> guard){MAGNET_GUARDS.add(Objects.requireNonNull(guard));}
    public static void forget(ServerPlayer player){COOLDOWNS.remove(player.getUUID());}
    private static boolean usable(ServerPlayer p){return p.isAlive()&&!p.isCreative()&&!p.isSpectator()&&!NewbieMode.isNewbie(p);}
    private static double amount(EffectRuntime.Active a,double cap){return MechanicalSpec.scaled(a.effect().amountPerRank(),a.rank(),cap);}
    private static double num(EffectRuntime.Active a,String key,double d,double min,double max){return MechanicalSpec.number(a.effect().params(),key,d,min,max);}
    private static int integer(EffectRuntime.Active a,String key,int d,int min,int max){return MechanicalSpec.integer(a.effect().params(),key,d,min,max);}
    private static boolean roll(ServerPlayer p,EffectRuntime.Active a,double chance){
        if(chance<=0)return false;
        long now=p.server.overworld().getGameTime();
        var map=COOLDOWNS.computeIfAbsent(p.getUUID(),ignored->new HashMap<>());
        if(map.size()>256)map.entrySet().removeIf(entry->entry.getValue()<=now);
        Long until=map.get(a.id());
        if(until!=null&&now<until)return false;
        if(p.getRandom().nextDouble()>=Math.min(1,chance))return false;
        int delay=integer(a,"cooldown_ticks",0,0,72000);
        if(delay>0)map.put(a.id(),now+delay);
        return true;
    }
    private static boolean match(ServerPlayer p,LivingEntity target,DamageSource source,Definition.Effect fx){
        if(target==p||target instanceof Player||p.isAlliedTo(target)||
            (target instanceof TamableAnimal tame&&tame.isTame())||!Selectors.entity(fx.selector(),target)||!EffectRuntime.sourceMatches(fx.damageTag(),source))return false;
        boolean melee=source.getDirectEntity()==p,projectile=source.getDirectEntity() instanceof Projectile;
        String delivery=fx.params().getOrDefault("delivery","any");
        if(delivery.equals("melee")&&!melee||delivery.equals("projectile")&&!projectile)return false;
        ItemStack weapon=melee?p.getMainHandItem():source.getDirectEntity() instanceof AbstractArrow arrow?arrow.getWeaponItem():ItemStack.EMPTY;
        return fx.weapon().isEmpty()||weapon!=null&&Selectors.item(fx.weapon(),weapon);
    }
    public static void incoming(LivingIncomingDamageEvent event){
        if(event.isCanceled()||event.getAmount()<=0||!(event.getSource().getEntity() instanceof ServerPlayer p)||!usable(p))return;
        double multiplier=1;
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();
            if(!fx.type().equals("critical_hit")&&!fx.type().equals("damage_at_low_health"))continue;
            if(!match(p,event.getEntity(),event.getSource(),fx))continue;
            if(fx.type().equals("critical_hit")&&roll(p,a,amount(a,1)*num(a,"chance",1,0,1)))
                multiplier*=num(a,"multiplier",2,1,5);
            else if(fx.type().equals("damage_at_low_health")&&p.getHealth()/Math.max(1,p.getMaxHealth())<=num(a,"threshold",0.3,0.01,1))
                multiplier*=1+amount(a,5);
        }
        if(multiplier!=1)event.setAmount((float)Math.min(1000000,event.getAmount()*Math.min(10,multiplier)));
    }
    public static void postDamage(LivingDamageEvent.Post event){
        if(event.getNewDamage()<=0)return;
        if(event.getEntity() instanceof ServerPlayer defender&&usable(defender))react(defender,event);
        if(!(event.getSource().getEntity() instanceof ServerPlayer p)||!usable(p))return;
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();if(!fx.type().equals("status_on_hit")&&!fx.type().equals("heal_on_hit"))continue;
            if(!match(p,event.getEntity(),event.getSource(),fx))continue;
            if(fx.type().equals("status_on_hit")){
                if(event.getEntity().isAlive()&&roll(p,a,amount(a,1)*num(a,"chance",1,0,1)))status(event.getEntity(),a);
            }else if(roll(p,a,num(a,"chance",1,0,1)))heal(p,event.getEntity(),a,event.getNewDamage());
        }
    }
    private static void react(ServerPlayer owner,LivingDamageEvent.Post event){
        var source=event.getSource();
        // Reactive powers use the actual damage source owner, not a client target.
        if(!(source.getEntity() instanceof LivingEntity attacker)||attacker==owner||attacker instanceof Player||
            owner.isAlliedTo(attacker)||attacker instanceof TamableAnimal tame&&tame.isTame())return;
        for(var a:EffectRuntime.active(owner)){
            var fx=a.effect();
            if(!fx.type().equals("status_on_hurt")||!Selectors.entity(fx.selector(),attacker)||
               !EffectRuntime.sourceMatches(fx.damageTag(),source))continue;
            String delivery=fx.params().getOrDefault("delivery","any");
            if(delivery.equals("melee")&&source.getDirectEntity()!=attacker||
               delivery.equals("projectile")&&!(source.getDirectEntity() instanceof Projectile))continue;
            if(roll(owner,a,amount(a,1)*num(a,"chance",1,0,1)))
                status(fx.params().getOrDefault("recipient","self").equals("self")?owner:attacker,a);
        }
    }
    public static void kill(ServerPlayer p,LivingDeathEvent event){
        if(event.isCanceled()||!usable(p))return;
        for(var a:EffectRuntime.active(p)){
            if(!a.effect().type().equals("heal_on_kill")||!match(p,event.getEntity(),event.getSource(),a.effect()))continue;
            if(roll(p,a,num(a,"chance",1,0,1)))heal(p,event.getEntity(),a,0);
        }
    }
    private static void heal(ServerPlayer owner,LivingEntity target,EffectRuntime.Active a,float damage){
        String basis=a.effect().params().getOrDefault("basis",a.effect().type().equals("heal_on_kill")?"target_health":"damage");
        double reference=switch(basis){case "flat"->1;case "damage"->damage;default->target.getMaxHealth();};
        double cap=num(a,"max_heal",20,0.01,100);
        double n=basis.equals("flat")?amount(a,cap):MechanicalSpec.heal(a.effect().amountPerRank(),a.rank(),reference,cap);
        if(n>0)owner.heal((float)n);
    }
    private static void status(LivingEntity target,EffectRuntime.Active a){
        if(!target.isAlive())return;
        var key=ResourceLocation.parse(a.effect().params().get("effect"));
        var holder=BuiltInRegistries.MOB_EFFECT.getHolder(key).orElse(null);
        if(holder==null||holder.value().isInstantenous())return;
        int amp=integer(a,"amplifier",0,0,9),duration=integer(a,"duration_ticks",60,1,12000);
        var existing=target.getEffect(holder);
        if(existing!=null&&(existing.getAmplifier()>amp||existing.getAmplifier()==amp&&existing.getDuration()>=duration))return;
        target.addEffect(new MobEffectInstance(holder,duration,amp,true,false,true));
    }
    public static void tick(ServerPlayer p){
        if(!usable(p)||p.tickCount%5!=0)return;
        int budget=32,operations=4;
        var due=EffectRuntime.active(p).stream().filter(a->{
            String type=a.effect().type();
            if(!type.equals("magnet")&&!type.equals("status_pulse"))return false;
            int interval=integer(a,"interval_ticks",type.equals("magnet")?5:20,type.equals("magnet")?5:10,200);
            return p.tickCount%(((interval+4)/5)*5)==0;
        }).sorted(Comparator.comparing(EffectRuntime.Active::id)).toList();
        if(due.isEmpty())return;
        int start=Math.floorMod(p.tickCount/5,due.size());
        for(int n=0;n<due.size()&&operations>0&&budget>0;n++){
            var a=due.get((start+n)%due.size());
            String type=a.effect().type();
            int targets=Math.min(budget,integer(a,"max_targets",type.equals("magnet")?16:8,1,32));
            operations--; // even empty queries consume an operation slot
            budget-=type.equals("magnet")?magnet(p,a,targets):pulse(p,a,targets);
        }
    }
    private static boolean magnetAllowed(ServerPlayer p,ItemEntity item){
        for(var guard:MAGNET_GUARDS){
            try{if(!guard.test(p,item))return false;}
            catch(RuntimeException|LinkageError bad){return false;}
        }
        return true;
    }
    private static int pulse(ServerPlayer p,EffectRuntime.Active a,int limit){
        String scope=a.effect().params().getOrDefault("scope","self");
        if(scope.equals("self")){status(p,a);return 1;}
        double r=amount(a,32);if(r<=0)return 0;
        var found=new ArrayList<LivingEntity>();
        p.serverLevel().getEntities(EntityTypeTest.forClass(LivingEntity.class),p.getBoundingBox().inflate(r),
            t->t!=p&&t.isAlive()&&!(t instanceof Player)&&t.distanceToSqr(p)<=r*r&&Selectors.entity(a.effect().selector(),t)
                &&(scope.equals("hostile")?t instanceof Monster&&!p.isAlliedTo(t):p.isAlliedTo(t)||t instanceof TamableAnimal animal&&animal.isOwnedBy(p)),found,limit);
        found.forEach(t->status(t,a));return found.size();
    }
    private static int magnet(ServerPlayer p,EffectRuntime.Active a,int limit){
        if(p.isCrouching())return 0;
        double r=amount(a,32);if(r<=0)return 0;
        var box=p.getBoundingBox().inflate(r);var items=new ArrayList<ItemEntity>();
        p.serverLevel().getEntities(EntityTypeTest.forClass(ItemEntity.class),box,
            item->item.isAlive()&&!item.hasPickUpDelay()&&item.distanceToSqr(p)<=r*r
              &&(item.getTarget()==null||item.getTarget().equals(p.getUUID()))
              &&Selectors.item(a.effect().selector(),item.getItem())&&magnetAllowed(p,item),items,limit);
        for(var item:items)attract(p,item);
        int left=limit-items.size();
        if(left>0&&MechanicalSpec.bool(a.effect().params(),"move_xp",false)){
            var orbs=new ArrayList<ExperienceOrb>();
            p.serverLevel().getEntities(EntityTypeTest.forClass(ExperienceOrb.class),box,
                orb->orb.isAlive()&&orb.distanceToSqr(p)<=r*r,orbs,left);
            orbs.forEach(orb->attract(p,orb));return items.size()+orbs.size();
        }
        return items.size();
    }
    private static void attract(ServerPlayer p,net.minecraft.world.entity.Entity entity){
        var v=MagnetMotion.toward(entity.getX()-p.getX(),entity.getY()-p.getY(),entity.getZ()-p.getZ(),entity.horizontalCollision);
        if(v.x()!=0||v.y()!=0||v.z()!=0)entity.setDeltaMovement(v.x(),v.y(),v.z());
    }
}
