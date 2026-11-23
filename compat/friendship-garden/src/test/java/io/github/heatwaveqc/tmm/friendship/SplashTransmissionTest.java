package io.github.heatwaveqc.tmm.friendship;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

final class SplashTransmissionTest {
    private static FriendshipFlower flower(UUID owner) {
        return FriendshipFlower.craftedBy(owner, "Friend", "minecraft:poppy", FriendshipFlower.Form.SHORT);
    }
    private static SplashTransmission.Effect effect(String id, SplashTransmission.Classification kind) {
        return new SplashTransmission.Effect(id, 2, 72_000, false, kind, true);
    }

    @Test void repeatedFlowersDeduplicatePerOwnerAndEffectWhileMultipleOwnersReceiveIt() {
        UUID first = UUID.randomUUID(), second = UUID.randomUUID();
        var speed = effect("minecraft:speed", SplashTransmission.Classification.BENEFICIAL);
        var result = SplashTransmission.resolve("minecraft:long_swiftness", "minecraft:overworld", 50,
            List.of(speed), List.of(flower(first), flower(first), flower(second)),
            Map.of(first, new SplashTransmission.Recipient(first, "minecraft:the_nether", true, false),
                   second, new SplashTransmission.Recipient(second, "minecraft:overworld", true, false)),
            SplashTransmission.Config.defaults(), true);
        assertEquals(2, result.size());
        assertEquals(Set.of(first, second), result.stream().map(SplashTransmission.Delivery::playerId).collect(java.util.stream.Collectors.toSet()));
        assertTrue(result.stream().allMatch(delivery -> delivery.effect().equals(speed)),
            "amplifier and long duration must pass through unchanged");
    }

    @Test void harmfulAndUnknownEffectsRequireBothOptInAndPvpPermission() {
        UUID owner = UUID.randomUUID();
        var harm = effect("minecraft:poison", SplashTransmission.Classification.HARMFUL);
        var unknown = effect("modded:mystery", SplashTransmission.Classification.UNKNOWN);
        var opted = Map.of(owner, new SplashTransmission.Recipient(owner, "minecraft:overworld", true, true));
        var notOpted = Map.of(owner, new SplashTransmission.Recipient(owner, "minecraft:overworld", true, false));
        assertTrue(SplashTransmission.resolve("minecraft:poison", "minecraft:overworld", 0,
            List.of(harm, unknown), List.of(flower(owner)), notOpted,
            SplashTransmission.Config.defaults(), true).isEmpty());
        assertTrue(SplashTransmission.resolve("minecraft:poison", "minecraft:overworld", 0,
            List.of(harm, unknown), List.of(flower(owner)), opted,
            SplashTransmission.Config.defaults(), false).isEmpty());
        assertEquals(2, SplashTransmission.resolve("minecraft:poison", "minecraft:overworld", 0,
            List.of(harm, unknown), List.of(flower(owner)), opted,
            SplashTransmission.Config.defaults(), true).size());
    }

    @Test void offlineDeadAndMourningRecipientsReceiveNothing() {
        UUID offline = UUID.randomUUID(), dead = UUID.randomUUID(), mourning = UUID.randomUUID();
        var speed = effect("minecraft:speed", SplashTransmission.Classification.BENEFICIAL);
        var recipients = Map.of(
            dead, new SplashTransmission.Recipient(dead, "minecraft:overworld", false, false),
            mourning, new SplashTransmission.Recipient(mourning, "minecraft:overworld", true, false));
        FriendshipFlower wilted = flower(mourning).onCrafterDeath(10, false);
        assertTrue(SplashTransmission.resolve("minecraft:swiftness", "minecraft:overworld", 20,
            List.of(speed), List.of(flower(offline), flower(dead), wilted, flower(mourning)), recipients,
            SplashTransmission.Config.defaults(), true).isEmpty());
    }

    @Test void masterDimensionAndBothBlacklistControlsFailClosed() {
        UUID owner = UUID.randomUUID();
        var speed = effect("minecraft:speed", SplashTransmission.Classification.BENEFICIAL);
        var recipients = Map.of(owner, new SplashTransmission.Recipient(owner, "minecraft:the_nether", true, false));
        var disabled = new SplashTransmission.Config(false, true, true, Set.of(), Set.of());
        var localOnly = new SplashTransmission.Config(true, true, false, Set.of(), Set.of());
        var potionBlocked = new SplashTransmission.Config(true, true, true, Set.of("minecraft:swiftness"), Set.of());
        var effectBlocked = new SplashTransmission.Config(true, true, true, Set.of(), Set.of("minecraft:speed"));
        for (var config : List.of(disabled, localOnly, potionBlocked, effectBlocked))
            assertTrue(SplashTransmission.resolve("minecraft:swiftness", "minecraft:overworld", 0,
                List.of(speed), List.of(flower(owner)), recipients, config, true).isEmpty());
    }

    @Test void nonstandardScriptedEffectsAreNotTransmitted() {
        UUID owner = UUID.randomUUID();
        var scripted = new SplashTransmission.Effect("modded:permanent_mutation", 0, 1,
            false, SplashTransmission.Classification.BENEFICIAL, false);
        assertTrue(SplashTransmission.resolve("modded:brew", "minecraft:overworld", 0,
            List.of(scripted), List.of(flower(owner)),
            Map.of(owner, new SplashTransmission.Recipient(owner, "minecraft:overworld", true, false)),
            SplashTransmission.Config.defaults(), true).isEmpty());
    }
}
