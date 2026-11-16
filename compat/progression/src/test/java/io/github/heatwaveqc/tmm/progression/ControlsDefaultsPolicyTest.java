package io.github.heatwaveqc.tmm.progression;

import io.github.heatwaveqc.tmm.progression.client.ControlsDefaultsPolicy;
import io.github.heatwaveqc.tmm.progression.client.ControlsDefaultsPolicy.Decision;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ControlsDefaultsPolicyTest {
    @TempDir Path tmp;

    @Test void vanillaBindingsAreNeverUnbound() {
        assertEquals(34, ControlsDefaultsPolicy.vanillaNames().size());
        for (String key : ControlsDefaultsPolicy.vanillaNames()) {
            assertEquals(Decision.VANILLA, ControlsDefaultsPolicy.decide(key, Set.of()));
        }
    }

    @Test void newModMappingsAreUnboundWithoutMaintainingAList() {
        assertEquals(Decision.DEFAULT_UNBOUND, ControlsDefaultsPolicy.decide("keyset.open", Set.of()));
        assertEquals(Decision.DEFAULT_UNBOUND, ControlsDefaultsPolicy.decide("key.brand_new_mod.cast", Set.of()));
    }

    @Test void allSavedKeysIncludingDefaultValuedOnesArePlayerOwned() {
        Set<String> saved = Set.of("keyset.open", "key.dragonsurvival.ability", "key.attack");
        assertEquals(Decision.PRESERVE_PLAYER, ControlsDefaultsPolicy.decide("keyset.open", saved));
        assertEquals(Decision.PRESERVE_PLAYER, ControlsDefaultsPolicy.decide("key.dragonsurvival.ability", saved));
        assertEquals(Decision.VANILLA, ControlsDefaultsPolicy.decide("key.attack", saved));
    }

    @Test void parsesFullOptionsFileAndIgnoresOtherPreferences() throws Exception {
        Path options = tmp.resolve("options.txt");
        Files.writeString(options, "version:3955\nkey_keyset.open:key.keyboard.grave.accent\n" +
            "key_key.dragonsurvival.ability:key.keyboard.unknown\n" +
            "key_key.attack:key.mouse.left\nguiScale:3\n");
        assertEquals(Set.of("keyset.open", "key.dragonsurvival.ability", "key.attack"),
            ControlsDefaultsPolicy.readSavedNames(options));
    }

    @Test void freshProfileHasNoSavedKeys() throws Exception {
        assertTrue(ControlsDefaultsPolicy.readSavedNames(tmp.resolve("missing.txt")).isEmpty());
    }

    @Test void invalidOptionsPathFailsClosedInsteadOfClearingKeys() {
        assertThrows(IOException.class, () -> ControlsDefaultsPolicy.readSavedNames(tmp));
    }
}
