package io.github.heatwaveqc.tmm.progression;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.heatwaveqc.tmm.progression.core.ProgressionMath;
import io.github.heatwaveqc.tmm.progression.data.PathJson;
import io.github.heatwaveqc.tmm.progression.model.Definition;
import io.github.heatwaveqc.tmm.progression.runtime.BedrockRest;
import io.github.heatwaveqc.tmm.progression.runtime.EffectHandlers;
import io.github.heatwaveqc.tmm.progression.state.Track;
import net.minecraft.core.BlockPos;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

final class BedrockRestTest {
    private static Definition.Path installedMonk() throws Exception {
        String resource = "data/tmm_progression/tmm_progression/paths/monk.json";
        try (InputStream input = BedrockRestTest.class.getClassLoader()
                .getResourceAsStream(resource)) {
            assertNotNull(input);
            JsonObject source = JsonParser.parseReader(
                    new InputStreamReader(input, StandardCharsets.UTF_8)).getAsJsonObject();
            return PathJson.read("tmm_progression:monk", source);
        }
    }

    @Test void bedrockRestIsRealNativeFreeSkillAndNotPurchasable() throws Exception {
        Definition.Path monk = installedMonk();
        assertTrue(EffectHandlers.known("bedrock_rest"));
        Definition.Skill skill = monk.skill("tmm_progression:bedrock_rest");
        assertNotNull(skill);
        assertEquals(1, skill.maxRank());
        assertEquals(17, skill.requiredRank());
        assertEquals(java.util.List.of(17), skill.autoRanks());
        assertEquals(0, skill.baseCost());
        assertEquals(0, skill.stepCost());
        assertEquals("", skill.choiceGroup());
        assertEquals(1, skill.effects().size());
        assertEquals("bedrock_rest", skill.effects().getFirst().type());
        assertTrue(skill.effects().getFirst().emptyHand());
        assertTrue(skill.effects().getFirst().unarmored());

        Track initial = ProgressionMath.join(monk, Track.empty());
        assertEquals(0, initial.skillRank(skill.id()));
        Track before = ProgressionMath.grantXp(monk, initial, monk.rankXp().get(15));
        assertEquals(16, before.rank());
        assertEquals(0, before.skillRank(skill.id()));
        Track unlocked = ProgressionMath.grantXp(monk, before,
                monk.rankXp().get(16) - monk.rankXp().get(15));
        assertEquals(17, unlocked.rank());
        assertEquals(1, unlocked.skillRank(skill.id()));
        assertSame(unlocked, ProgressionMath.buy(monk, unlocked, skill.id()),
                "Unpurchasable native classic skill must never deduct technique points");
    }

    @Test void historicRestBonusIsOneTimeAndNeverRequiredForRank() throws Exception {
        Definition.Path monk = installedMonk();
        var trial = monk.milestones().stream()
                .filter(m -> m.id().equals("tmm_progression:trial_bedrock_sleep"))
                .findFirst().orElseThrow();
        assertEquals(230, trial.xp());
        assertEquals(0, trial.points());
        assertEquals(1, trial.criteria().size());
        assertEquals("external", trial.criteria().getFirst().event());
        assertEquals("tmm_progression:bedrock_rest_completed",
                trial.criteria().getFirst().selector());
        assertEquals(1, trial.criteria().getFirst().count());

        Definition.Skill rest = monk.skill("tmm_progression:bedrock_rest");
        assertTrue(rest.requiresMilestones().isEmpty());
        Track initial = ProgressionMath.join(monk, Track.empty());
        Track earned = ProgressionMath.grantXp(monk,initial,monk.rankXp().get(16));
        assertEquals(17, earned.rank(),"No original bedrock sleeping deed needed for rank17");
        assertFalse(earned.completed(trial.id()));
        Track completed = earned.complete(trial.id());
        assertEquals(completed,completed.complete(trial.id()),"No second milestone completion");
    }

    @Test void geometryRequiresTwoAdjacentFloorBedrocksAndTwoBlocksOfClearance() {
        BlockPos origin = new BlockPos(0,0,0);
        BlockPos adjacent = origin.east();
        Set<BlockPos> bedrock = new HashSet<>(Set.of(origin,adjacent));
        Set<BlockPos> air = new HashSet<>(Set.of(origin.above(),origin.above(2),
                adjacent.above(),adjacent.above(2)));
        assertTrue(BedrockRest.hasExposedPair(origin,bedrock::contains,air::contains));
        assertTrue(BedrockRest.hasExposedPair(adjacent,bedrock::contains,air::contains));
        assertFalse(BedrockRest.hasExposedPair(origin,at->false,air::contains));
        air.remove(adjacent.above(2));
        assertFalse(BedrockRest.hasExposedPair(origin,bedrock::contains,air::contains),
                "No sleep with blocks suffocating a head over the other bedrock");
        air.add(adjacent.above(2));
        bedrock.remove(adjacent);
        bedrock.add(origin.north().east());
        assertFalse(BedrockRest.hasExposedPair(origin,bedrock::contains,air::contains),
                "Diagonal pair cannot become a bed");
        bedrock.remove(origin.north().east());
        bedrock.add(origin.east().above());
        assertFalse(BedrockRest.hasExposedPair(origin,bedrock::contains,air::contains),
                "Vertical bedrock does not count as horizontal sleep platform");
        bedrock.add(adjacent);
        air.remove(origin.above());
        assertFalse(BedrockRest.hasExposedPair(origin,bedrock::contains,air::contains),
                "Cannot sleep inside solid blocks");
    }
}
