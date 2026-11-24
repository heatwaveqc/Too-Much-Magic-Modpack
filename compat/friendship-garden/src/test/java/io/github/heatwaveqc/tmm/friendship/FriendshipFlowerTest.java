package io.github.heatwaveqc.tmm.friendship;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

final class FriendshipFlowerTest {
    @Test void identitySpeciesAndFormSurviveEveryStateTransition() {
        UUID crafter = UUID.randomUUID();
        FriendshipFlower flower = FriendshipFlower.craftedBy(
            crafter, "OldName", "minecraft:sunflower", FriendshipFlower.Form.TALL);
        FriendshipFlower mourning = flower.onCrafterDeath(100, false);
        assertEquals(crafter, mourning.crafterId());
        assertEquals("OldName", mourning.crafterDisplayName());
        assertEquals("minecraft:sunflower", mourning.originalFlowerId());
        assertEquals(FriendshipFlower.Form.TALL, mourning.form());
    }

    @Test void appearanceUsesCrafterPresenceAndExactFiveMinuteMourningWindow() {
        FriendshipFlower flower = FriendshipFlower.craftedBy(UUID.randomUUID(), "Friend",
            "minecraft:poppy", FriendshipFlower.Form.SHORT);
        assertEquals(FriendshipFlower.Appearance.BLOOMING, flower.appearance(true, 10));
        assertEquals(FriendshipFlower.Appearance.BUDDING, flower.appearance(false, 10));
        FriendshipFlower dead = flower.onCrafterDeath(20, false);
        assertEquals(6020, dead.mourningUntilTick());
        assertEquals(FriendshipFlower.Appearance.WILTED, dead.appearance(true, 6019));
        assertEquals(FriendshipFlower.Appearance.BLOOMING, dead.appearance(true, 6020));
        assertEquals(FriendshipFlower.Appearance.BUDDING, dead.appearance(false, 6020));
    }

    @Test void repeatedDeathExtendsMourningAndPermanentDeathNeverRecovers() {
        FriendshipFlower flower = FriendshipFlower.craftedBy(UUID.randomUUID(), "Friend",
            "minecraft:poppy", FriendshipFlower.Form.SHORT);
        FriendshipFlower twice = flower.onCrafterDeath(100, false).onCrafterDeath(200, false);
        assertEquals(6200, twice.mourningUntilTick());
        FriendshipFlower permanent = twice.onCrafterDeath(300, true);
        assertEquals(FriendshipFlower.Appearance.WILTED, permanent.appearance(true, Long.MAX_VALUE));
    }

    @Test void invalidIdentityOrRegistryDataFailsClosed() {
        UUID id = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class,
            () -> FriendshipFlower.craftedBy(id, "", "minecraft:poppy", FriendshipFlower.Form.SHORT));
        assertThrows(IllegalArgumentException.class,
            () -> FriendshipFlower.craftedBy(id, "Friend", "not an id", FriendshipFlower.Form.SHORT));
    }
}
