package io.github.heatwaveqc.tmm.monk.gameplay;

import de.teamlapen.vampirism.api.VampirismAPI;
import io.github.heatwaveqc.tmm.monk.faction.MonkFaction;
import io.github.heatwaveqc.tmm.monk.rules.MonkEligibility;
import io.github.heatwaveqc.tmm.monk.skills.MonkSkills;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/** The first finite *harvest tier* unlock, not another unlimited Tyz mining-speed rank. */
public final class MonkHarvestEffects {
    private MonkHarvestEffects() { }

    /** Equal to a stone pickaxe's harvest tier, only for empty-handed Monk mining. */
    public static boolean stoneFistCanHarvest(BlockState state) {
        return state.is(BlockTags.MINEABLE_WITH_PICKAXE)
                && !state.is(BlockTags.NEEDS_IRON_TOOL)
                && !state.is(BlockTags.NEEDS_DIAMOND_TOOL);
    }

    public static void onHarvestCheck(PlayerEvent.HarvestCheck event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || !player.getMainHandItem().isEmpty()
                || !MonkEligibility.eligibleForMonkPower(player)
                || !stoneFistCanHarvest(event.getTargetBlock())) return;
        if (!VampirismAPI.factionPlayerHandler(player).isInFaction(MonkFaction.FACTION)) return;
        boolean active = MonkFaction.FACTION.getPlayerCapability(player)
                .map(state -> state.getSkillHandler().isSkillEnabled(MonkSkills.STONE_FIST.get()))
                .orElse(false);
        if (active) event.setCanHarvest(true);
    }
}
