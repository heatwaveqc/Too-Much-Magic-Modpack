package io.github.heatwaveqc.tmm.monk.progression;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.factions.IFactionPlayerHandler;
import io.github.heatwaveqc.tmm.monk.faction.MonkFaction;
import io.github.heatwaveqc.tmm.monk.faction.MonkPlayer;
import io.github.heatwaveqc.tmm.monk.rules.MonkEligibility;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

/**
 * Native faction rank XP, independent from the primary global MXP->Tyz wallet.
 * This pilot intentionally stops at Monk rank 4. Rewards are issued on completed
 * actions, not attempts. Protected blocks and canceled kills require regression
 * tests in an integrated pack before release.
 */
public final class MonkMasteryEvents {
    private static final TagKey<Block> TRAINING_BLOCKS = TagKey.create(
            Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("tmm_monk", "training_blocks"));

    private MonkMasteryEvents() { }

    public static void onBlockDrops(BlockDropsEvent event) {
        if (!(event.getBreaker() instanceof ServerPlayer player)) return;
        if (!event.getTool().isEmpty() || !event.getState().is(TRAINING_BLOCKS)) return;
        award(player, 1, "training");
    }

    public static void onHostileDeath(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
        if (!(event.getEntity() instanceof Enemy) || event.getEntity() == player) return;
        // This first pilot credits directly attributed kills, including legitimate
        // spells. Indirect owner/summon credit requires validated source APIs.
        award(player, 4, "combat");
    }

    public static void onFoodFinished(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!event.getItem().has(DataComponents.FOOD)) return;
        MonkPlayer state = getActivePlayer(player);
        if (state == null) return;
        ResourceLocation foodId = BuiltInRegistries.ITEM.getKey(event.getItem().getItem());
        if (state.discoverFood(foodId)) {
            awardTo(player, state, 8, "new_food");
        }
    }

    private static void award(ServerPlayer player, int amount, String source) {
        MonkPlayer state = getActivePlayer(player);
        if (state != null) awardTo(player, state, amount, source);
    }

    private static MonkPlayer getActivePlayer(ServerPlayer player) {
        if (!MonkEligibility.eligibleForMonkPower(player) || MonkFaction.FACTION == null) return null;
        return MonkFaction.FACTION.getPlayerCapability(player)
                .filter(MonkPlayer.class::isInstance)
                .map(MonkPlayer.class::cast)
                .filter(state -> state.getLevel() > 0)
                .orElse(null);
    }

    private static void awardTo(ServerPlayer player, MonkPlayer state, int amount, String source) {
        IFactionPlayerHandler factions = VampirismAPI.factionPlayerHandler(player);
        if (!factions.isInFaction(MonkFaction.FACTION)) return;
        int oldXp = state.getMasteryXp();
        state.awardMastery(amount);
        int nextRank = MonkMasteryRules.eligibleLevel(factions.getCurrentLevel(), state.getMasteryXp());
        if (nextRank > factions.getCurrentLevel()) {
            // Vampirism is the sole authority on faction rank and skill unlocks.
            if (factions.setFactionLevel(MonkFaction.FACTION, nextRank)) {
                player.sendSystemMessage(Component.translatable("message.tmm_monk.rank_up", nextRank));
            }
        }
        if (oldXp != state.getMasteryXp()) {
            player.displayClientMessage(Component.translatable(
                    "message.tmm_monk.mastery_gained", amount, source,
                    state.getMasteryXp(), MonkMasteryRules.totalXpForLevel(MonkMasteryRules.HIGHEST_PILOT_LEVEL)), true);
        }
    }
}
