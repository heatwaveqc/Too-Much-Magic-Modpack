/*
 * Adapted from Direwolf20-MC/JustDireThings, revision 506416a9c7675cacc246a47d2b0c9d6b27eff9a8.
 * src/main/java/com/direwolf20/justdirethings/mixin/LightTextureMixin.java
 * Original license: MIT; see third_party_licenses/JustDireThings.txt in source and JAR.
 * Modifications: Too Much Magic contributors, 2026.
 */
package io.github.heatwaveqc.tyr.progression.mixin;
import io.github.heatwaveqc.tyr.progression.client.ClientVision;
import io.github.heatwaveqc.tyr.progression.foundation.VisionMath;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Only the NIGHT_VISION lightmap check is overridden; unrelated effects retain vanilla semantics. */
@Mixin(LightTexture.class)
public abstract class EnhancedNightVisionMixin {
    @WrapOperation(method="updateLightTexture",at=@At(value="INVOKE",
        target="Lnet/minecraft/client/player/LocalPlayer;hasEffect(Lnet/minecraft/core/Holder;)Z"),require=0)
    private boolean tmm$nightEffect(LocalPlayer player,Holder<MobEffect> effect,Operation<Boolean> original){
        boolean vanilla=original.call(player,effect);
        return VisionMath.hasNightVision(vanilla,MobEffects.NIGHT_VISION.equals(effect)&&ClientVision.night(player));
    }
    @WrapOperation(method="updateLightTexture",at=@At(value="INVOKE",
        target="Lnet/minecraft/client/renderer/GameRenderer;getNightVisionScale(Lnet/minecraft/world/entity/LivingEntity;F)F"),require=0)
    private float tmm$nightScale(LivingEntity entity,float partial,Operation<Float> original){
        // Vanilla getNightVisionScale assumes a real potion instance. Do not call it when emulating the effect.
        if(ClientVision.night(entity))return VisionMath.nightVisionScale(0,true);
        return original.call(entity,partial);
    }
}
