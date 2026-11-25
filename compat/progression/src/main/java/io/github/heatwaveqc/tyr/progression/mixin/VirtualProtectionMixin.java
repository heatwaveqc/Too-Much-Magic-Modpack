package io.github.heatwaveqc.tyr.progression.mixin;

import io.github.heatwaveqc.tyr.progression.foundation.PhysicalMasteryMath;
import io.github.heatwaveqc.tyr.progression.runtime.EffectRuntime;
import io.github.heatwaveqc.tyr.progression.runtime.NewbieMode;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public abstract class VirtualProtectionMixin {
    @Inject(method="getDamageProtection",at=@At("RETURN"),cancellable=true)
    private static void tmm$protection(ServerLevel level,LivingEntity entity,DamageSource source,CallbackInfoReturnable<Float> cir){
        if(!(entity instanceof ServerPlayer p)||!p.isAlive()||p.isCreative()||p.isSpectator()
            ||p instanceof net.neoforged.neoforge.common.util.FakePlayer||NewbieMode.isNewbie(p)
            ||source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))return;
        double earned=0;
        for(var a:EffectRuntime.active(p))
            if(a.effect().type().equals("virtual_protection"))
                earned+=Math.max(0,a.effect().amountPerRank()*a.rank());
        if(earned>0)cir.setReturnValue(PhysicalMasteryMath.protection(cir.getReturnValue(),Math.min(20,earned)));
    }
}
