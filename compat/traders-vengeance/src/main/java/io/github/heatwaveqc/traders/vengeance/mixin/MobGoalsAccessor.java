package io.github.heatwaveqc.traders.vengeance.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Mob.class)
public interface MobGoalsAccessor {
    @Accessor("goalSelector")
    GoalSelector tradersVengeance$goalSelector();

    @Accessor("targetSelector")
    GoalSelector tradersVengeance$targetSelector();
}
