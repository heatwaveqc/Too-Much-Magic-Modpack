package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MahouResourceSkills;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
@Pseudo
@Mixin(targets="stepsword.mahoutsukai.effects.exchange.DamageExchangeSpellEffect",remap=false)
public abstract class MahouDamageExchangeCapacityMixin {
    @WrapOperation(method="damageExchangeLivingHurt(Lnet/neoforged/neoforge/event/entity/living/LivingIncomingDamageEvent;)V",at=@At(value="INVOKE",target="Lstepsword/mahoutsukai/dataattachments/mahou/IMahou;getMaxMana()I"),require=0,remap=false)
    private static int tyr$cap(@Coerce Object holder,Operation<Integer> original,LivingIncomingDamageEvent event){
        int earned=original.call(holder);
        return event.getEntity() instanceof Player p?MahouResourceSkills.capacity(p,earned):earned;
    }
}
