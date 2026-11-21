package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.JawrRitualOutput;
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
@Mixin(targets="dev.sterner.witchery.core.registry.WitcheryCommands",remap=false)
public abstract class JawrInfusionRitualMixin {
    @WrapOperation(method="registerInfusionCommands$lambda$1(Lcom/mojang/brigadier/context/CommandContext;)I",at=@At(value="INVOKE",target="Ldev/sterner/witchery/feature/infusion/InfusionPlayerAttachment;setData(Lnet/minecraft/world/entity/player/Player;Ldev/sterner/witchery/feature/infusion/InfusionPlayerAttachment$Data;)V"),require=0,remap=false)
    private static void tmm$set(Player target,@Coerce Object data,Operation<Void> original){
        String expected=JawrRitualOutput.infusionExpected(target),before=expected==null?null:JawrRitualOutput.infusionType(target);
        original.call(target,data);
        JawrRitualOutput.infusionChanged(target,expected,before);
    }
}
