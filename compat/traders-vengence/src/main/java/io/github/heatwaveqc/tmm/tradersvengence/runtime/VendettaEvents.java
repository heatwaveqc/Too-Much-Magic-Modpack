package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import io.github.heatwaveqc.tmm.tradersvengence.TradersVengence;
import io.github.heatwaveqc.tmm.tradersvengence.mixin.MobGoalsAccessor;
import io.github.heatwaveqc.tmm.tradersvengence.runtime.WantedPlayerTargetGoal;
import io.github.heatwaveqc.tmm.tradersvengence.runtime.WantedRangedAttackGoal;
import io.github.heatwaveqc.tmm.tradersvengence.state.TraderBlacklist;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

/** Player-specific retaliation, including newly spawned or reloaded defenders. */
public final class VendettaEvents {
    private static final ResourceLocation PRIEST =
        ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "priest");
    private static final String LAST_PRIEST_TARGET =
        "traders_vengence:last_priest_target";

    private static final ResourceLocation PISTOL_RANGE_MODIFIER =
        ResourceLocation.fromNamespaceAndPath("traders_vengence", "pistol_follow_range");
    private VendettaEvents() {}

    public static void accuse(ServerLevel level, ServerPlayer offender, String reason) {
        if (TraderBlacklist.get(level).accuse(offender.getUUID())) {
            TradersVengence.LOGGER.info("Player {} permanently blacklisted: {}",
                offender.getGameProfile().getName(), reason);
        }
    }

    public static void onDeath(LivingDeathEvent event) {
        if (!(event.getEntity().level() instanceof ServerLevel level)) {
            return;
        }

        if (event.getEntity() instanceof IronGolem golem
            && event.getSource().is(DamageTypes.LAVA)) {
            for (ServerPlayer player : level.players()) {
                if (VengeanceRules.withinGolemVendettaRadius(
                    player.getX() - golem.getX(),
                    player.getY() - golem.getY(),
                    player.getZ() - golem.getZ())) {
                    accuse(level, player, "near iron golem killed by lava");
                }
            }
        }

        if (!(event.getSource().getEntity() instanceof ServerPlayer killer)) {
            return;
        }
        if (event.getEntity() instanceof WanderingTrader) {
            accuse(level, killer, "killed a wandering trader");
        } else if (event.getEntity() instanceof Villager) {
            // Vanilla VILLAGER_KILLED gossip is witness-dependent. Preserve the
            // global vendetta even if the victim was the only villager nearby.
            // The blacklist is a UUID set: reputation mixin + death hook are idempotent.
            accuse(level, killer, "killed a villager");
        }
    }

    /**
     * Vanilla's VILLAGER_HURT / VILLAGER_KILLED reputation callbacks are observed
     * by VillagerReputationMixin. This remaining hook is ONLY for iron golems:
     * vanilla reputation has GOLEM_KILLED, but no GOLEM_HURT event.
     *
     * POST damage avoids treating invulnerable/canceled attacks as new crimes.
     */
    public static void onDefenderHurt(LivingDamageEvent.Post event) {
        if (!VengeanceRules.isActualHealthDamage(event.getNewDamage())
            || !(event.getEntity() instanceof IronGolem)
            || !(event.getEntity().level() instanceof ServerLevel level)
            || !(event.getSource().getEntity() instanceof ServerPlayer attacker)) {
            return;
        }
        accuse(level, attacker, "attacked an iron golem");
    }

    /**
     * Iron's Priest extends NeutralWizard. Only NEW actual player combat targets
     * cause a crime. Rechecking the same already-hostile target on every tick
     * would immediately undo a Hero of the Village pardon.
     *
     * The last observed target is stored on each priest, so reloading a chunk
     * also does not retroactively turn a continuing vendetta into a new crime.
     */
    public static void onPriestAggro(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof Mob mob)
            || !(mob.level() instanceof ServerLevel level)
            || !BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType()).equals(PRIEST)) {
            return;
        }

        CompoundTag data = mob.getPersistentData();
        java.util.UUID previous = data.hasUUID(LAST_PRIEST_TARGET)
            ? data.getUUID(LAST_PRIEST_TARGET) : null;
        ServerPlayer player = mob.getTarget() instanceof ServerPlayer target ? target : null;
        java.util.UUID current = player == null ? null : player.getUUID();

        if (current == null) {
            if (previous != null) data.remove(LAST_PRIEST_TARGET);
        } else if (VengeanceRules.isNewPriestTarget(previous, current)) {
            data.putUUID(LAST_PRIEST_TARGET, current);
            accuse(level, player, "newly targeted by Iron's Priest");
        }
    }

    public static void onJoin(EntityJoinLevelEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        if (!(event.getEntity() instanceof Mob mob)) {
            return;
        }
        if (!(mob instanceof Villager || mob instanceof WanderingTrader || mob instanceof IronGolem || mob instanceof Llama)) {
            return;
        }

        // Persisted vanilla-villager provenance is independent from vendetta targeting.
        if (mob instanceof IronGolem golem) GolemMinigunEvents.attach(golem);
        MobGoalsAccessor access = (MobGoalsAccessor) mob;
        // The same entity instance can re-enter on chunk changes/portals.
        // Inspect the entity's own GoalSelector instead of keeping global
        // strong/weak sets or adding duplicate attack goals on every join.
        if (access.tradersVengence$targetSelector().getAvailableGoals().stream()
                .anyMatch(goal -> goal.getGoal() instanceof WantedPlayerTargetGoal)) {
            return;
        }
        access.tradersVengence$targetSelector().addGoal(1, new WantedPlayerTargetGoal(mob, level));

        if (mob instanceof AbstractVillager villager) {
            // Existing blacklist and individual self-defense policy also applies to wandering traders.
            var follow = villager.getAttribute(Attributes.FOLLOW_RANGE);
            if (follow != null && !follow.hasModifier(PISTOL_RANGE_MODIFIER)
                    && follow.getValue() < PistolCycle.RANGE) {
                follow.addTransientModifier(new AttributeModifier(PISTOL_RANGE_MODIFIER,
                    PistolCycle.RANGE - follow.getValue(), AttributeModifier.Operation.ADD_VALUE));
            }
            access.tradersVengence$targetSelector().addGoal(0, new VillagerSelfDefenseGoal(villager));
            access.tradersVengence$goalSelector().addGoal(2, new NpcPistolGoal(villager));
        } else if (mob instanceof Llama && mob instanceof RangedAttackMob ranged) {
            access.tradersVengence$goalSelector().addGoal(2,
                new WantedRangedAttackGoal(ranged, 1.0, 50, 12.0F));
        }
        // Existing melee goals remain; confirmed villager-spawned golems also receive the minigun goal.
    }
}
