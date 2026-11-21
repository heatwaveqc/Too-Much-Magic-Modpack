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
@Mixin(targets="dev.sterner.witchery.feature.spirit_world.ManifestationHandler",remap=false)
public abstract class JawrManifestationRitualMixin {
    @WrapMethod(method="setHasRiteOfManifestation(Lnet/minecraft/world/entity/player/Player;Z)V",require=0,remap=false)
    private void tmm$set(Player target,boolean enabled,Operation<Void> original){
        Boolean before=enabled?JawrRitualOutput.manifestationBefore(target):null;
        original.call(target,enabled);
        JawrRitualOutput.manifestationChanged(target,before);
    }
}
