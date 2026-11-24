package io.github.heatwaveqc.tyr.progression.mixin;
import io.github.heatwaveqc.tyr.progression.runtime.ClassAbilities;
import io.github.heatwaveqc.tyr.progression.runtime.GildedMechanics;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinBruteAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Optional;
/** Port of first-party GildedBruteMixin; do not reset anger or block retaliation. */
@Mixin(PiglinBruteAi.class)
public abstract class OwnedPiglinBruteMixin {
    @Inject(method="findNearestValidAttackTarget",at=@At("RETURN"),cancellable=true)
    private static void tmm$unprovoked(AbstractPiglin brute,CallbackInfoReturnable<Optional<? extends LivingEntity>> cir){
        var candidate=cir.getReturnValue();
        if(candidate!=null&&candidate.isPresent()&&candidate.get() instanceof ServerPlayer p
            &&GildedMechanics.piglinKin(p,false)&&!ClassAbilities.isRecentRetaliation(brute,p)
            &&!brute.getBrain().hasMemoryValue(MemoryModuleType.ANGRY_AT))cir.setReturnValue(Optional.empty());
    }
}
