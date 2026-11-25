package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.heatwaveqc.tyr.progression.runtime.forms.LichServantLimits;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets="com.Polarice3.Goety.common.ritual.RitualRequirements",remap=false)
public abstract class GoetyLichRitualLimitMixin{
    @ModifyExpressionValue(method="canSummon(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/EntityType;)Z",at=@At(value="INVOKE",target="Lcom/Polarice3/Goety/api/entities/IOwned;getSummonLimit(Lnet/minecraft/world/entity/LivingEntity;)I"),require=0,allow=1,remap=false)
    private static int tmm$limit(int limit,Level world,Player owner,EntityType<?> type){return LichServantLimits.ritual(limit,owner,type);}
}
