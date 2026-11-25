package io.github.heatwaveqc.tyr.progression.mixin;

import net.minecraft.world.entity.monster.PatrollingMonster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PatrollingMonster.class)
public interface CompanionPatrolAccess {
    @Accessor("patrolling") boolean tyr$isPatrolling();
    @Accessor("patrolling") void tyr$setPatrolling(boolean value);
}
