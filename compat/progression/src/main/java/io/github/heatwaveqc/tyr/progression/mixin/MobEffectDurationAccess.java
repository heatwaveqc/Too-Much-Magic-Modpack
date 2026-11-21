package io.github.heatwaveqc.tyr.progression.mixin;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
/** Read hidden chain and adjust a private copy, never mutate a shared incoming effect. */
@Mixin(MobEffectInstance.class)
public interface MobEffectDurationAccess {
    @Accessor("hiddenEffect") MobEffectInstance tmm$getHiddenEffect();
    @Accessor("duration") void tmm$setDuration(int ticks);
}
