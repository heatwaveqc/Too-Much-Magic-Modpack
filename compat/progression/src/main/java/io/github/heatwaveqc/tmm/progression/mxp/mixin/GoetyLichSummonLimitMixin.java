package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.heatwaveqc.tmm.progression.runtime.forms.LichServantLimits;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets="com.Polarice3.Goety.common.magic.SummonSpell",remap=false)
public abstract class GoetyLichSummonLimitMixin{
    @ModifyExpressionValue(method="conditionsMet(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)Z",at=@At(value="INVOKE",target="Lcom/Polarice3/Goety/common/magic/SummonSpell;summonLimit()I"),require=0,allow=1,remap=false)
    private int tmm$limit(int limit,ServerLevel world,LivingEntity owner){return LichServantLimits.spell(limit,this,owner);}
}
