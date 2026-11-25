package io.github.heatwaveqc.tyr.progression.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Vanilla 1.21.1 goalSelector, not a replacement entity or native goal deletion. */
@Mixin(Mob.class)
public interface CompanionMobGoalsAccess {
    @Accessor("goalSelector") GoalSelector tyr$companionGoals();
}
