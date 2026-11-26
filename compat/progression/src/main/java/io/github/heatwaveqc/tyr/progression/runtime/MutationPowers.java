package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.MutationPowerPolicy;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import java.util.*;

/** Derived from existing saved mutations. No MobEffect is ever inserted in the active effect map. */
public final class MutationPowers {
    private MutationPowers(){}
    private record Key(Holder<Attribute> attr,ResourceLocation id){}
    private static final Map<ServerPlayer,Set<Key>> APPLIED=new WeakHashMap<>();
    private static final Set<ServerPlayer> INVISIBLE=Collections.newSetFromMap(new WeakHashMap<>());
    public static int amplifier(LivingEntity entity,Holder<MobEffect> effect){
        if(!(entity instanceof ServerPlayer)&&!entity.level().isClientSide())return -1;
        var location=BuiltInRegistries.MOB_EFFECT.getKey(effect.value());
        if(location==null)return -1;
        String id=location.toString();
        if(!MutationPowerPolicy.validId(id)||effect.value().isInstantenous())return -1;
        if(entity instanceof ServerPlayer p)return HunterMutations.active(p).getOrDefault(id,-1);
        return ClientPowerSnapshot.mutation(entity,id);
    }
    /** A detached query descriptor for native predicates only. Never stored, ticked or sent as potion state. */
    public static MobEffectInstance query(LivingEntity entity,Holder<MobEffect> effect,MobEffectInstance actual){
        int amp=amplifier(entity,effect);
        if(amp<0||actual!=null&&actual.getAmplifier()>=amp)return actual;
        return new MobEffectInstance(effect,240,amp,true,false,false);
    }
    public static void preTick(PlayerTickEvent.Pre event){
        if(event.getEntity() instanceof ServerPlayer p){
            reconcile(p);
            int regen=amplifier(p,MobEffects.REGENERATION);
            var nativeRegen=p.getActiveEffectsMap().get(MobEffects.REGENERATION);
            if(regen>=0&&!FeignDeath.active(p)&&(nativeRegen==null||nativeRegen.getAmplifier()<regen)
                &&p.tickCount%MutationPowerPolicy.regenerationInterval(regen)==0&&p.getHealth()<p.getMaxHealth())
                p.heal(1.0F);
            // Opted-in effects use the native periodic callback without adding a
            // status instance. Ordinary temporary versions own their own ticks;
            // no double application from the generic callback.
            for(var entry:HunterMutations.active(p).entrySet()){
                if(entry.getKey().equals("minecraft:regeneration"))continue;
                var holder=BuiltInRegistries.MOB_EFFECT.getHolder(ResourceLocation.parse(entry.getKey())).orElse(null);
                if(holder==null||p.getActiveEffectsMap().containsKey(holder))continue;
                var effect=holder.value();int amp=entry.getValue();
                if(effect.shouldApplyEffectTickThisTick(p.tickCount,amp))effect.applyEffectTick(p,amp);
            }
        }
    }
    public static void login(PlayerEvent.PlayerLoggedInEvent event){
        if(event.getEntity() instanceof ServerPlayer p)reconcile(p);
    }
    public static void reconcile(ServerPlayer p){
        var active=HunterMutations.active(p);
        Set<Key> previous=APPLIED.getOrDefault(p,Set.of());
        if(active.isEmpty()&&previous.isEmpty()&&!INVISIBLE.contains(p))return;
        Set<Key> desired=new HashSet<>();
        for(var entry:active.entrySet()){
            String id=entry.getKey();int amp=entry.getValue();
            var holder=BuiltInRegistries.MOB_EFFECT.getHolder(ResourceLocation.parse(id)).orElse(null);
            if(holder==null)continue;
            var actual=p.getActiveEffectsMap().get(holder);
            if(actual!=null&&actual.getAmplifier()>=amp)continue;
            holder.value().createModifiers(amp,(attr,modifier)->{
                var instance=p.getAttribute(attr);if(instance==null)return;
                // Subtract only the native modifier actually present, not an inferred potion value.
                var nativeModifier=instance.getModifier(modifier.id());
                double amount=nativeModifier==null?modifier.amount():MutationPowerPolicy.supplemental(
                    modifier.amount(),nativeModifier.amount(),
                    modifier.operation()==AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
                if(!Double.isFinite(amount))return;
                var ownId=ResourceLocation.fromNamespaceAndPath("tyr_progression",
                    "mutation/"+id.replace(':','/')+"/"+modifier.id().getNamespace()+"/"+modifier.id().getPath());
                var key=new Key(attr,ownId);desired.add(key);
                var old=instance.getModifier(ownId);
                if(old!=null&&old.amount()==amount&&old.operation()==modifier.operation())return;
                instance.removeModifier(ownId);
                instance.addTransientModifier(new AttributeModifier(ownId,amount,modifier.operation()));
            });
        }
        for(var key:previous)if(!desired.contains(key)){
            var instance=p.getAttribute(key.attr());if(instance!=null)instance.removeModifier(key.id());
        }
        if(desired.isEmpty())APPLIED.remove(p);else APPLIED.put(p,desired);
        // Vanilla invisibility flag is synchronized to tracking clients; own only a flag we actually set.
        if(active.containsKey("minecraft:invisibility")){
            if(!p.isInvisible()){p.setInvisible(true);INVISIBLE.add(p);}
        }else if(INVISIBLE.remove(p)&&!p.getActiveEffectsMap().containsKey(MobEffects.INVISIBILITY))p.setInvisible(false);
        if(!previous.isEmpty()&&p.getHealth()>p.getMaxHealth())p.setHealth(p.getMaxHealth());
    }
    public static void forget(ServerPlayer p){
        for(var key:APPLIED.getOrDefault(p,Set.of())){
            var instance=p.getAttribute(key.attr());if(instance!=null)instance.removeModifier(key.id());
        }
        APPLIED.remove(p);
        if(INVISIBLE.remove(p)&&!p.getActiveEffectsMap().containsKey(MobEffects.INVISIBILITY))p.setInvisible(false);
    }
}
