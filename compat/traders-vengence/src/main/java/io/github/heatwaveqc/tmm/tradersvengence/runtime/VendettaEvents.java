package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import io.github.heatwaveqc.tmm.tradersvengence.TradersVengence;
import io.github.heatwaveqc.tmm.tradersvengence.mixin.MobGoalsAccessor;
import io.github.heatwaveqc.tmm.tradersvengence.runtime.WantedPlayerTargetGoal;
import io.github.heatwaveqc.tmm.tradersvengence.runtime.WantedRangedAttackGoal;
import io.github.heatwaveqc.tmm.tradersvengence.state.TraderBlacklist;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.RangedAttackMob;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

/** Player-specific retaliation, including newly spawned or reloaded defenders. */
public final class VendettaEvents {
    private VendettaEvents() {}

    public static void onDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof WanderingTrader)
            || !(event.getEntity().level() instanceof ServerLevel level)
            || !(event.getSource().getEntity() instanceof ServerPlayer killer)) {
            return;
        }
        if (TraderBlacklist.get(level).accuse(killer.getUUID())) {
            TradersVengence.LOGGER.info("Player {} permanently blacklisted for killing wandering trader",
                killer.getGameProfile().getName());
        }
    }

    public static void onJoin(EntityJoinLevelEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        if (event.getEntity() instanceof WanderingTrader trader && !event.loadedFromDisk()) {
            // EntityJoinLevelEvent doesn't expose a spawn reason; v0 restricts all NEW traders.
            // Saved traders are never removed. Future: use a spawner-specific hook for
            // command/summon bypass and pre-spawn retries.
            if (!TraderSpawnRules.allowed(level, trader.blockPosition())) {
                event.setCanceled(true);
                return;
            }
        }

        if (!(event.getEntity() instanceof Mob mob)) {
            return;
        }
        if (!(mob instanceof Villager || mob instanceof IronGolem || mob instanceof Llama)) {
            return;
        }

        MobGoalsAccessor access = (MobGoalsAccessor) mob;
        access.tradersVengence$targetSelector().addGoal(1,
            new NearestAttackableTargetGoal<>(mob, Player.class, 10, true, false,
                target -> target instanceof ServerPlayer player
                    && !player.isCreative() && !player.isSpectator()
                    && TraderBlacklist.get(level).contains(player.getUUID())));

        if (mob instanceof Villager villager) {
            access.tradersVengence$goalSelector().addGoal(2,
                new WantedRangedAttackGoal((RangedAttackMob) villager,
                    0.9, VengeanceRules.VILLAGER_COOLDOWN_TICKS,
                    VengeanceRules.VILLAGER_RANGE));
        } else if (mob instanceof Llama && mob instanceof RangedAttackMob ranged) {
            access.tradersVengence$goalSelector().addGoal(2,
                new WantedRangedAttackGoal(ranged, 1.0, 50, 12.0F));
        }
        // Iron golems already have melee combat goals; only target selection changes.
    }
}
