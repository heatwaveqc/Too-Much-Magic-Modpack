package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.EnchantedCurseWitness;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
@Pseudo
@Mixin(targets="net.favouriteless.enchanted.common.enchanted.circle_magic.rites.TotalEclipseRite",remap=false)
public abstract class EnchantedEclipseEffectMixin{
    @WrapOperation(method="onStart(Lnet/favouriteless/enchanted/common/enchanted/circle_magic/rites/Rite$RiteParams;)Z",at=@At(value="INVOKE",target="Lnet/minecraft/server/level/ServerLevel;setDayTime(J)V"),require=0,remap=false)
    private void tmm$time(ServerLevel level,long time,Operation<Void> original){
        long before=level.getDayTime();original.call(level,time);
        EnchantedCurseWitness.effect(this,level,"enchanted:time",level.getDayTime()!=before);
    }
}
