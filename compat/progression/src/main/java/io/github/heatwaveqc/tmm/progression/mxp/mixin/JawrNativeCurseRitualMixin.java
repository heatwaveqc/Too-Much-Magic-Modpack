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
@Pseudo
@Mixin(targets="dev.sterner.witchery.feature.curse.CurseHandler",remap=false)
public abstract class JawrNativeCurseRitualMixin {
    @WrapMethod(method="addCurse(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/resources/ResourceLocation;ZILjava/lang/Integer;)Z",require=0,remap=false)
    private boolean tmm$apply(Player target,ServerPlayer caster,ResourceLocation id,boolean cat,int power,Integer duration,Operation<Boolean> original){
        var before=JawrRitualOutput.curseTarget(target,id)?JawrRitualOutput.curseState(target,id):null;
        boolean success=original.call(target,caster,id,cat,power,duration);
        if(before!=null)JawrRitualOutput.curseChanged(target,id,before,success);
        return success;
    }
}
