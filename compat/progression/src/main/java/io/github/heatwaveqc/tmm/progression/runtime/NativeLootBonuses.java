package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

/**
 * Provides virtual vanilla loot-enchantment levels at the actual formula site.
 * Does not alter held ItemStacks, generate entities, or run on player/entity ticks.
 */
public final class NativeLootBonuses {
    private NativeLootBonuses() {}

    public static int monkFortune(ServerPlayer player, LootContext context) {
        if (player.isCreative() || player.isSpectator()
                || context.getParamOrNull(LootContextParams.THIS_ENTITY) != player)
            return 0;

        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        BlockState block = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        // Only the player's actual empty fist; no doubling Fortune on equipped tools.
        if (tool == null || !tool.isEmpty() || block == null
                || !player.getMainHandItem().isEmpty())
            return 0;
        if (!ClassAbilities.unlocked(player, "treasure_hand")
                || !HarvestAbilities.canHarvestBarehand(player, block))
            return 0;

        return LootBonusMath.monkFortune(1, TyzMonkBridge.fortuneBonus(player),
                "fortune".equals(ProgressionApi.mode(player,
                        "tmm_progression:treasure_mode", "fortune")), true);
    }

    public static int hunterLooting(ServerPlayer player, LootContext context) {
        if (player.isCreative() || player.isSpectator()) return 0;
        if (!(context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof LivingEntity victim))
            return 0;
        DamageSource source = context.getParamOrNull(LootContextParams.DAMAGE_SOURCE);
        // The killer must really be this player. Projectile kills qualify;
        // spawners, simulated loot, other players and unrelated damage do not.
        if (source == null || source.getEntity() != player) return 0;
        return HunterExperience.favoredLootingBonus(player, victim);
    }
}
