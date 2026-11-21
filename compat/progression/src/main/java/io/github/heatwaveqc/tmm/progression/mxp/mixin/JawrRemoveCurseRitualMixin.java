package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.JawrRitualOutput;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import java.util.Set;
@Pseudo
@Mixin(targets="dev.sterner.witchery.content.ritual_event.RemoveCurseRitualEvent",remap=false)
public abstract class JawrRemoveCurseRitualMixin {
    @WrapOperation(method="removeOldestCurse(Ldev/sterner/witchery/content/block/ritual/ritual_event/RitualContext;)V",at=@At(value="INVOKE",target="Ldev/sterner/witchery/feature/curse/CurseHandler;removeCurseWithPower(Lnet/minecraft/world/entity/player/Player;Ldev/sterner/witchery/core/api/Curse;IZ)Z"),require=0,remap=false)
    private boolean tmm$remove(@Coerce Object handler,Player target,@Coerce Object curse,int power,boolean flag,Operation<Boolean> original){
        boolean success=original.call(handler,target,curse,power,flag);
        JawrRitualOutput.removal(this,success);return success;
    }
    @WrapOperation(method="cureAfflictions(Ldev/sterner/witchery/content/block/ritual/ritual_event/RitualContext;)V",at=@At(value="INVOKE",target="Ldev/sterner/witchery/core/api/event/AfflictionEvents;cureAfflictions(Lnet/minecraft/server/level/ServerPlayer;)Ljava/util/Set;"),require=0,remap=false)
    private Set<?> tmm$cure(@Coerce Object handler,ServerPlayer target,Operation<Set<?>> original){
        Set<?> result=original.call(handler,target);JawrRitualOutput.removal(this,result!=null&&!result.isEmpty());return result;
    }
}
