package io.github.heatwaveqc.traders.vengeance.runtime;

import io.github.heatwaveqc.traders.vengeance.mixin.MobGoalsAccessor;
import io.github.heatwaveqc.traders.vengeance.network.GolemVisualPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.animal.IronGolem;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/** Server-authoritative provenance and per-entity goal installation. */
public final class GolemMinigunEvents {
    private static final String MARKER = "traders_vengeance:villager_summoned_golem";
    private static final ResourceLocation RANGE_MODIFIER = ResourceLocation.fromNamespaceAndPath("traders_vengeance", "minigun_follow_range");
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
        if (!(golem.level() instanceof ServerLevel)) return;
        var follow = golem.getAttribute(Attributes.FOLLOW_RANGE);
        if (!armed(golem)) {
            if (follow != null) follow.removeModifier(RANGE_MODIFIER);
            return;
        }
        // Only confirmed village golems: raise the native follow range to at least
        // the base gun range where no additional mod modifiers interfere.
        // Transient modifier, recreated from persisted provenance on reload.
        if (follow != null && !follow.hasModifier(RANGE_MODIFIER)
                && follow.getValue() < MinigunBalance.RANGE) {
            follow.addTransientModifier(new AttributeModifier(RANGE_MODIFIER,
                MinigunBalance.RANGE - follow.getValue(), AttributeModifier.Operation.ADD_VALUE));
        }
        GoalSelector goals = ((MobGoalsAccessor) golem).tradersVengeance$goalSelector();
        if (goals.getAvailableGoals().stream().noneMatch(g -> g.getGoal() instanceof GolemMinigunGoal)) {
            // Vanilla melee remains available as fallback when the minigun cannot engage.
            goals.addGoal(0, new GolemMinigunGoal(golem));
        }
    }
    private static GolemMinigunGoal installedGoal(IronGolem golem) {
        for (var wrapped : ((MobGoalsAccessor) golem).tradersVengeance$goalSelector().getAvailableGoals()) {
            if (wrapped.getGoal() instanceof GolemMinigunGoal goal) return goal;
        }
        return null;
    }

    private static GolemVisualPayload snapshot(IronGolem golem, GolemMinigunGoal goal) {
        boolean deployed = goal.currentDeployment();
        return new GolemVisualPayload(golem.getUUID(), goal.angle(), goal.speed(),
            deployed && goal.firing(), deployed);
    }

    /** Newly tracking clients get the state now, not only on the next heartbeat. */
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || !(event.getTarget() instanceof IronGolem golem) || !armed(golem)) return;
        var goal = installedGoal(golem);
        if (goal != null) PacketDistributor.sendToPlayer(player, snapshot(golem, goal));
    }

    /** Read-only operator diagnosis; never marks, equips or retargets a golem. */
    public static String describe(IronGolem golem) {
        var goal = installedGoal(golem);
        var target = golem.getTarget();
        return "golem=" + golem.getUUID() + " marker=" + golem.getPersistentData().getBoolean(MARKER)
            + " playerCreated=" + golem.isPlayerCreated() + " armed=" + armed(golem)
            + " goalInstalled=" + (goal != null) + " target=" + (target == null ? "none" : target.getUUID())
            + " running=" + (goal != null && goal.running())
            + " deployed=" + (goal != null && goal.currentDeployment())
            + " canEngage=" + (goal != null && goal.canUse())
            + " firing=" + (goal != null && goal.firing())
            + " followRange=" + golem.getAttributeValue(Attributes.FOLLOW_RANGE);
    }

    public static void onTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof IronGolem golem)
                || !(golem.level() instanceof ServerLevel) || !armed(golem)) return;
        for (var wrapped : ((MobGoalsAccessor) golem).tradersVengeance$goalSelector().getAvailableGoals()) {
            if (wrapped.getGoal() instanceof GolemMinigunGoal goal) {
                boolean wasFiring = goal.firing();
                boolean wasDeployed = goal.deployed();
                goal.updateSpin();
                // Periodic authoritative snapshot also covers players who just began tracking.
                if (wasFiring != goal.firing() || wasDeployed != goal.deployed() || golem.tickCount % 10 == 0) {
                    PacketDistributor.sendToPlayersTrackingEntity(golem,
                        snapshot(golem, goal));
                }
                return;
            }
        }
    }
}
