package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import io.github.heatwaveqc.tmm.tradersvengence.mixin.MobGoalsAccessor;
import io.github.heatwaveqc.tmm.tradersvengence.network.GolemVisualPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.animal.IronGolem;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/** Server-authoritative provenance and per-entity goal installation. */
public final class GolemMinigunEvents {
    private static final String MARKER = "traders_vengence:villager_summoned_golem";
    private GolemMinigunEvents() {}
    public static boolean armed(IronGolem golem) {
        return ArmedGolemPolicy.shouldArm(golem.getPersistentData().getBoolean(MARKER), golem.isPlayerCreated());
    }
    public static void markVillagerSpawn(IronGolem golem) {
        if (golem.level() instanceof ServerLevel && !golem.isPlayerCreated()) {
            golem.getPersistentData().putBoolean(MARKER, true);
            attach(golem);
        }
    }
    public static void attach(IronGolem golem) {
        if (!(golem.level() instanceof ServerLevel) || !armed(golem)) return;
        GoalSelector goals = ((MobGoalsAccessor) golem).tradersVengence$goalSelector();
        if (goals.getAvailableGoals().stream().noneMatch(g -> g.getGoal() instanceof GolemMinigunGoal)) {
            // Vanilla melee remains available as fallback when the minigun cannot engage.
            goals.addGoal(0, new GolemMinigunGoal(golem));
        }
    }
    public static void onTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof IronGolem golem)
                || !(golem.level() instanceof ServerLevel) || !armed(golem)) return;
        for (var wrapped : ((MobGoalsAccessor) golem).tradersVengence$goalSelector().getAvailableGoals()) {
            if (wrapped.getGoal() instanceof GolemMinigunGoal goal) {
                goal.updateSpin();
                // Periodic authoritative snapshot also covers players who just began tracking.
                if (golem.tickCount % 10 == 0) {
                    PacketDistributor.sendToPlayersTrackingEntity(golem,
                        new GolemVisualPayload(golem.getUUID(), goal.angle(), goal.speed()));
                }
                return;
            }
        }
    }
}
