package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.JawrRitualOutput;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
@Pseudo
@Mixin(targets="dev.sterner.witchery.core.registry.WitcheryCommands",remap=false)
public abstract class JawrWolfCurseRitualMixin {
    @WrapOperation(method="registerWerewolfCommands$lambda$1(Lcom/mojang/brigadier/context/CommandContext;)I",at=@At(value="INVOKE",target="Ldev/sterner/witchery/feature/affliction/werewolf/WerewolfLeveling;infectPlayer(Lnet/minecraft/server/level/ServerPlayer;)V"),require=0,remap=false)
    private static void tmm$infect(@Coerce Object handler,ServerPlayer target,Operation<Void> original){
        Integer before=JawrRitualOutput.wolfBefore(target);original.call(handler,target);
        JawrRitualOutput.wolfAfter(target,before);
    }
}
