package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.data.MechanicalSpec;
import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.foundation.MagnetMotion;
import io.github.heatwaveqc.tyr.progression.model.Definition;
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
    private record Streak(UUID target,String dimension,long until,int count){}
    private static final Map<UUID,Map<String,Streak>> STREAKS=new HashMap<>();
    private static final String LAST_DAMAGE="tyr_progression:last_regeneration_damage";
    private static final List<BiPredicate<ServerPlayer,LivingEntity>> AREA_GUARDS=new java.util.concurrent.CopyOnWriteArrayList<>();
    public static void registerAreaGuard(BiPredicate<ServerPlayer,LivingEntity> guard){AREA_GUARDS.add(Objects.requireNonNull(guard));}
    private static final List<BiPredicate<ServerPlayer,ItemEntity>> MAGNET_GUARDS=new java.util.concurrent.CopyOnWriteArrayList<>();
    /** Claim/provider bridges can veto attraction; vanilla reservation is always honored. */
    public static void registerMagnetGuard(BiPredicate<ServerPlayer,ItemEntity> guard){MAGNET_GUARDS.add(Objects.requireNonNull(guard));}
    public static void forget(ServerPlayer player){COOLDOWNS.remove(player.getUUID());STREAKS.remove(player.getUUID());}
    private static boolean usable(ServerPlayer p){return p.isAlive()&&!p.isCreative()&&!p.isSpectator()&&!NewbieMode.isNewbie(p);}
    private static double amount(EffectRuntime.Active a,double cap){return MechanicalSpec.scaled(a.effect().amountPerRank(),a.rank(),cap);}
    private static double num(EffectRuntime.Active a,String key,double d,double min,double max){return MechanicalSpec.number(a.effect().params(),key,d,min,max);}
    private static int integer(EffectRuntime.Active a,String key,int d,int min,int max){return MechanicalSpec.integer(a.effect().params(),key,d,min,max);}
    private static boolean roll(ServerPlayer p,EffectRuntime.Active a,double chance){
        if(chance<=0)return false;
        long now=p.server.overworld().getGameTime();
        var map=COOLDOWNS.computeIfAbsent(p.getUUID(),ignored->new HashMap<>());
        if(map.size()>256)map.entrySet().removeIf(entry->entry.getValue()<=now);
        int delay=integer(a,"cooldown_ticks",0,0,72000);
        boolean persistent=MechanicalSpec.bool(a.effect().params(),"persistent_cooldown",false);
        String key="tyr_progression:proc/"+UUID.nameUUIDFromBytes(a.id().getBytes(java.nio.charset.StandardCharsets.UTF_8));
        long until=persistent?readTime(p,key,0):map.getOrDefault(a.id(),0L);
        if(now<until&&until-now<=delay)return false;
        if(p.getRandom().nextDouble()>=Math.min(1,chance))return false;
        if(delay>0){
            if(persistent)ProgressionApi.setMode(p,key,Long.toString(now+delay));
            else map.put(a.id(),now+delay);
        }
        return true;
    }
    private static long readTime(ServerPlayer p,String key,long fallback){
        try{return Long.parseLong(ProgressionApi.mode(p,key,Long.toString(fallback)));}
        catch(NumberFormatException bad){return fallback;}
    }
    private static boolean areaAllowed(ServerPlayer owner,LivingEntity target){
        for(var guard:AREA_GUARDS)try{if(!guard.test(owner,target))return false;}
        catch(RuntimeException|LinkageError denied){return false;}
        return true;
    }
    /** Neutralize a hostile vanilla arrow; no ammo, ownership transfer, or retaliatory damage. */
    public static void deflect(ServerPlayer p,net.neoforged.neoforge.event.entity.ProjectileImpactEvent event){
        if(event.isCanceled()||!usable(p)||!(event.getProjectile() instanceof AbstractArrow arrow)
            ||arrow instanceof net.minecraft.world.entity.projectile.ThrownTrident
            ||!(arrow.getOwner() instanceof Monster hostile)||p.isAlliedTo(hostile)||!areaAllowed(p,hostile))return;
        var velocity=arrow.getDeltaMovement();
        if(velocity.lengthSqr()<0.001)return;
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();
            if(!fx.type().equals("projectile_deflection")||!Selectors.entity(fx.selector(),hostile)
                ||p.getLookAngle().dot(velocity.normalize().scale(-1))<num(a,"min_front_cosine",0.5,0,1))continue;
            if(!roll(p,a,amount(a,1)*num(a,"chance",1,0,1)))continue;
            event.setCanceled(true);
            arrow.discard();
            return;
        }
    }
    private static boolean match(ServerPlayer p,LivingEntity target,DamageSource source,Definition.Effect fx){
        if(target==p||target instanceof Player||p.isAlliedTo(target)||
            (target instanceof TamableAnimal tame&&tame.isTame())||!Selectors.entity(fx.selector(),target)||!EffectRuntime.sourceMatches(fx.damageTag(),source))return false;
        if(!io.github.heatwaveqc.tyr.progression.runtime.forms.FormCombatContext.attack(p,target,source,fx))return false;
        boolean melee=source.getDirectEntity()==p,projectile=source.getDirectEntity() instanceof Projectile;
        String delivery=fx.params().getOrDefault("delivery","any");
        if(delivery.equals("melee")&&!melee||delivery.equals("projectile")&&!projectile)return false;
        ItemStack weapon=melee?p.getMainHandItem():source.getDirectEntity() instanceof AbstractArrow arrow?arrow.getWeaponItem():ItemStack.EMPTY;
        return fx.weapon().isEmpty()||weapon!=null&&Selectors.item(fx.weapon(),weapon);
    }
    public static void incoming(LivingIncomingDamageEvent event){
        if(event.isCanceled()||event.getAmount()<=0)return;
        if(event.getEntity() instanceof ServerPlayer defender&&usable(defender)&&reflect(defender,event))return;
        if(!(event.getSource().getEntity() instanceof ServerPlayer p)||!usable(p))return;
        double multiplier=1;
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();
            if(!fx.type().equals("critical_hit")&&!fx.type().equals("damage_at_low_health")&&!fx.type().equals("backstab")&&!fx.type().equals("hit_streak"))continue;
            if(!match(p,event.getEntity(),event.getSource(),fx))continue;
            if(fx.type().equals("critical_hit")&&roll(p,a,amount(a,1)*num(a,"chance",1,0,1)))
                multiplier*=num(a,"multiplier",2,1,5);
            else if(fx.type().equals("damage_at_low_health")&&p.getHealth()/Math.max(1,p.getMaxHealth())<=num(a,"threshold",0.3,0.01,1))
                multiplier*=1+amount(a,5);
            else if(fx.type().equals("backstab")&&event.getSource().getDirectEntity()==p&&rear(p,event.getEntity(),num(a,"min_rear_cosine",0.5,0,1)))
                multiplier*=1+amount(a,2);
            else if(fx.type().equals("hit_streak"))multiplier*=1+Math.min(5,amount(a,2)*streak(p,a,event.getEntity()));
        }
        if(multiplier!=1)event.setAmount((float)Math.min(1000000,event.getAmount()*Math.min(10,multiplier)));
    }
    private static boolean reflect(ServerPlayer p,LivingIncomingDamageEvent event){
        var src=event.getSource();
        if(!(src.getEntity() instanceof Monster mob)||src.getDirectEntity()!=mob||!mob.isAlive()||p.isAlliedTo(mob)||!areaAllowed(p,mob)
            ||src.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY))return false;
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();
            if(!fx.type().equals("melee_reflection")||!Selectors.entity(fx.selector(),mob)||!EffectRuntime.sourceMatches(fx.damageTag(),src))continue;
            if(!roll(p,a,amount(a,0.75)*num(a,"chance",1,0,1)))continue;
            float reflected=(float)Math.min(num(a,"max_damage",20,0.01,100),event.getAmount()*num(a,"reflect_fraction",1,0,1));
            event.setCanceled(true);
            if(reflected>0)mob.hurt(p.damageSources().magic(),reflected);
            return true;
        }
        return false;
    }
    private static int streak(ServerPlayer p,EffectRuntime.Active a,LivingEntity target){
        var map=STREAKS.get(p.getUUID());var s=map==null?null:map.get(a.id());
        long now=p.server.overworld().getGameTime();
        return s!=null&&s.until()>=now&&s.dimension().equals(p.level().dimension().location().toString())
            &&(!MechanicalSpec.bool(a.effect().params(),"same_target",true)||s.target().equals(target.getUUID()))?s.count():0;
    }
    private static void creditStreak(ServerPlayer p,EffectRuntime.Active a,LivingEntity target){
        var map=STREAKS.computeIfAbsent(p.getUUID(),k->new HashMap<>());
        long now=p.server.overworld().getGameTime();
        map.entrySet().removeIf(x->x.getValue().until()<now);
        if(map.size()>=64&&!map.containsKey(a.id()))return;
        int previous=streak(p,a,target);
        int count=MechanicalSpec.nextStreak(previous,integer(a,"max_stacks",5,1,20),previous>0);
        map.put(a.id(),new Streak(target.getUUID(),p.level().dimension().location().toString(),now+integer(a,"expiry_ticks",100,1,1200),count));
    }
    private static boolean rear(ServerPlayer p,LivingEntity target,double min){
        var look=target.getLookAngle();
        double dx=p.getX()-target.getX(),dz=p.getZ()-target.getZ(),len=Math.hypot(dx,dz),facing=Math.hypot(look.x,look.z);
        return len>1e-6&&facing>1e-6&&(dx*look.x+dz*look.z)/(len*facing)<=-min;
    }
    public static void postDamage(LivingDamageEvent.Post event){
        if(event.getNewDamage()<=0)return;
        if(event.getEntity() instanceof ServerPlayer defender&&usable(defender)){
            if(EffectRuntime.active(defender).stream().anyMatch(a->a.effect().type().equals("delayed_regeneration")))
                ProgressionApi.setMode(defender,LAST_DAMAGE,Long.toString(defender.server.overworld().getGameTime()));
            for(var a:EffectRuntime.active(defender)){
                if(!a.effect().type().equals("health_threshold_status")||!EffectRuntime.sourceMatches(a.effect().damageTag(),event.getSource()))continue;
                double max=Math.max(1,defender.getMaxHealth()),after=defender.getHealth()/max,before=Math.min(1,after+event.getNewDamage()/max);
                if(MechanicalSpec.crossesBelow(before,after,num(a,"threshold",0.3,0.01,0.99))&&roll(defender,a,amount(a,1)*num(a,"chance",1,0,1)))status(defender,a);
            }
            react(defender,event);
        }
        if(!(event.getSource().getEntity() instanceof ServerPlayer p)||!usable(p))return;
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();if(!fx.type().equals("status_on_hit")&&!fx.type().equals("heal_on_hit")&&!fx.type().equals("hit_streak"))continue;
            if(!match(p,event.getEntity(),event.getSource(),fx))continue;
            if(fx.type().equals("hit_streak")){creditStreak(p,a,event.getEntity());continue;}
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
            if(!fx.type().equals("status_on_hurt")&&!fx.type().equals("counter_attack")||!Selectors.entity(fx.selector(),attacker)||
               !EffectRuntime.sourceMatches(fx.damageTag(),source))continue;
            String delivery=fx.params().getOrDefault("delivery","any");
            if(delivery.equals("melee")&&source.getDirectEntity()!=attacker||
               delivery.equals("projectile")&&!(source.getDirectEntity() instanceof Projectile))continue;
            if(fx.type().equals("counter_attack")){
                // Non-player hostile direct melee only; anonymous damage cannot re-enter player-credit hooks.
                if(!(attacker instanceof Monster)||source.getDirectEntity()!=attacker||!attacker.isAlive()||!areaAllowed(owner,attacker))continue;
                if(roll(owner,a,num(a,"chance",1,0,1)))attacker.hurt(owner.damageSources().magic(),(float)amount(a,num(a,"max_damage",20,.01,100)));
            }else if(roll(owner,a,amount(a,1)*num(a,"chance",1,0,1)))
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
        double healing=0;
        long now=p.server.overworld().getGameTime(),hurt=readTime(p,LAST_DAMAGE,-1);
        for(var a:EffectRuntime.active(p)){
            if(!a.effect().type().equals("delayed_regeneration"))continue;
            int interval=integer(a,"interval_ticks",20,20,200);
            if(p.tickCount%(((interval+4)/5)*5)==0&&
                (hurt<0||hurt>now||now-hurt>=integer(a,"after_damage_ticks",80,0,72000)))
                healing+=amount(a,20);
        }
        if(healing>0&&p.getHealth()<p.getMaxHealth())p.heal((float)Math.min(20,healing));
        int budget=32,operations=4;
        var due=EffectRuntime.active(p).stream().filter(a->{
            String type=a.effect().type();
            if(!Set.of("magnet","status_pulse","quarry_mark","area_pull").contains(type))return false;
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
            budget-=switch(type){
                case "magnet"->magnet(p,a,targets);
                case "quarry_mark"->mark(p,a,targets);
                case "area_pull"->motion(p,a,targets,true);
                default->pulse(p,a,targets);
            };
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
            t->t!=p&&t.isAlive()&&t.distanceToSqr(p)<=r*r&&Selectors.entity(a.effect().selector(),t)&&areaAllowed(p,t)
                &&(scope.equals("quarry")?quarry(p,t):scope.equals("hostile")?t instanceof Monster&&!(t instanceof TamableAnimal animal&&animal.isTame())&&!p.isAlliedTo(t):
                    (!(t instanceof Player)||MechanicalSpec.bool(a.effect().params(),"allow_allied_players",false))
                    &&(p.isAlliedTo(t)||t instanceof TamableAnimal owned&&owned.isOwnedBy(p))),found,limit);
        found.forEach(t->status(t,a));return found.size();
    }
    private static boolean quarry(ServerPlayer p,LivingEntity t){
        return t!=p&&t.isAlive()&&!(t instanceof Player)&&!p.isAlliedTo(t)&&!(t instanceof TamableAnimal animal&&animal.isTame());
    }
    private static int mark(ServerPlayer p,EffectRuntime.Active a,int limit){
        double r=amount(a,32);if(r<=0)return 0;
        var found=new ArrayList<LivingEntity>();
        p.serverLevel().getEntities(EntityTypeTest.forClass(LivingEntity.class),p.getBoundingBox().inflate(r),
            t->quarry(p,t)&&Selectors.entity(a.effect().selector(),t)&&areaAllowed(p,t)&&t.distanceToSqr(p)<=r*r,found,limit);
        LivingEntity best=null;double nearest=r*r;
        for(var t:found){
            var d=t.getEyePosition().subtract(p.getEyePosition());double ds=d.lengthSqr();
            if(ds>0.01&&ds<nearest&&p.getLookAngle().dot(d.normalize())>=num(a,"min_look_cosine",0.985,0.5,1)&&p.hasLineOfSight(t)){best=t;nearest=ds;}
        }
        if(best!=null)best.addEffect(new MobEffectInstance(net.minecraft.world.effect.MobEffects.GLOWING,integer(a,"duration_ticks",80,1,1200),0,true,false,true));
        return found.size();
    }
    /** Late fall callback: momentum only, not terrain changes, additional damage or XP. */
    public static void landing(net.neoforged.neoforge.event.entity.living.LivingFallEvent event){
        if(event.isCanceled()||!(event.getEntity() instanceof ServerPlayer p)||!usable(p)||p.isFallFlying())return;
        int remaining=16;
        for(var a:EffectRuntime.active(p)){
            if(remaining<=0)break;
            if(!a.effect().type().equals("landing_shockwave")||event.getDistance()<num(a,"min_fall_distance",3,1,64))continue;
            remaining-=motion(p,a,Math.min(remaining,integer(a,"max_targets",8,1,16)),false);
        }
    }
    private static int motion(ServerPlayer p,EffectRuntime.Active a,int limit,boolean pull){
        double r=num(a,"radius",4,0.5,16),force=amount(a,2);
        if(force<=0||limit<=0)return 0;
        var found=new ArrayList<LivingEntity>();
        p.serverLevel().getEntities(EntityTypeTest.forClass(LivingEntity.class),p.getBoundingBox().inflate(r),
            t->quarry(p,t)&&t instanceof Monster&&!(t instanceof net.minecraft.world.entity.boss.wither.WitherBoss)
                &&!(t instanceof net.minecraft.world.entity.boss.enderdragon.EnderDragon)&&t.distanceToSqr(p)<=r*r
                &&Selectors.entity(a.effect().selector(),t)&&areaAllowed(p,t)&&p.hasLineOfSight(t),found,limit);
        if(found.isEmpty()||!roll(p,a,num(a,"chance",1,0,1)))return found.size();
        for(var t:found){
            double dx=p.getX()-t.getX(),dz=p.getZ()-t.getZ(),distance=Math.hypot(dx,dz);
            if(distance<0.3)continue;
            double sign=pull?-1:1;
            t.knockback(Math.min(force,pull?Math.max(0,distance-1):force),sign*dx,sign*dz);
        }
        return found.size();
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
