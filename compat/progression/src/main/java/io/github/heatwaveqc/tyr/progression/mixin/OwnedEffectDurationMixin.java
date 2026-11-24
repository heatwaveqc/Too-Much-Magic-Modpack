package io.github.heatwaveqc.tyr.progression.mixin;
import io.github.heatwaveqc.tyr.progression.data.MechanicalSpec;
import io.github.heatwaveqc.tyr.progression.runtime.EffectRuntime;
import io.github.heatwaveqc.tyr.progression.runtime.NewbieMode;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
/** Copy incoming finite effects once before native applicability and merge.
 * Shorten harmful effects and extend beneficial effects independently by source effect category,
 * not by projectile/spell/potion origin. Never extend queued hidden-effect chains.
 */
@Mixin(LivingEntity.class)
public abstract class OwnedEffectDurationMixin {
    @ModifyVariable(method="addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",at=@At("HEAD"),argsOnly=true,ordinal=0)
    private MobEffectInstance tmm$adjust(MobEffectInstance input){
        if(!((Object)this instanceof ServerPlayer p)||p.isCreative()||p.isSpectator()||NewbieMode.isNewbie(p)
            ||input.getDuration()<=1
            ||((MobEffectDurationAccess)(Object)input).tmm$getHiddenEffect()!=null)return input;
        MobEffectCategory category=input.getEffect().value().getCategory();
        if(category!=MobEffectCategory.HARMFUL&&category!=MobEffectCategory.BENEFICIAL)return input;
        String id=BuiltInRegistries.MOB_EFFECT.getKey(input.getEffect().value()).toString();
        double rate=0;
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();
            if(category==MobEffectCategory.HARMFUL&&fx.type().equals("status_duration_reduction")&&fx.selector().equals(id))
                rate+=MechanicalSpec.scaled(fx.amountPerRank(),a.rank(),0.95);
            else if(category==MobEffectCategory.BENEFICIAL&&fx.type().equals("beneficial_duration_bonus")
                &&(fx.selector().isEmpty()||fx.selector().equals("*")||fx.selector().equals(id)))
                rate+=MechanicalSpec.scaled(fx.amountPerRank(),a.rank(),0.95);
        }
        if(rate<=0)return input;
        int duration=category==MobEffectCategory.HARMFUL
            ?MechanicalSpec.reducedDuration(input.getDuration(),rate)
            :MechanicalSpec.increasedDuration(input.getDuration(),rate);
        var copy=new MobEffectInstance(input);
        ((MobEffectDurationAccess)(Object)copy).tmm$setDuration(duration);
        return copy;
    }
}
