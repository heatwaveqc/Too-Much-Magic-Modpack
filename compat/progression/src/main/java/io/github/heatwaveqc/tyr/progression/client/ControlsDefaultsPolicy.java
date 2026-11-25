package io.github.heatwaveqc.tyr.progression.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * 1.21.1 client-side binding classification. This list is based on the exact
 * unmodded Options constructor, NOT on key categories or mod-name guesses.
 * New modded IDs therefore need no manifest updates or manual export.
 */
public final class ControlsDefaultsPolicy {
    private ControlsDefaultsPolicy() {}

    private static final Set<String> VANILLA = Set.of(
        "key.advancements", "key.attack", "key.back", "key.chat", "key.command",
        "key.drop", "key.forward", "key.fullscreen", "key.hotbar.1",
        "key.hotbar.2", "key.hotbar.3", "key.hotbar.4", "key.hotbar.5",
        "key.hotbar.6", "key.hotbar.7", "key.hotbar.8", "key.hotbar.9",
        "key.inventory", "key.jump", "key.left", "key.loadToolbarActivator",
        "key.pickItem", "key.playerlist", "key.right",
        "key.saveToolbarActivator", "key.screenshot", "key.smoothCamera",
        "key.sneak", "key.socialInteractions", "key.spectatorOutlines",
        "key.sprint", "key.swapOffhand", "key.togglePerspective", "key.use"
    );

    public enum Decision { VANILLA, PRESERVE_PLAYER, DEFAULT_UNBOUND }

    public static Decision decide(String keyName, Set<String> namesPresentBeforeLaunch) {
        if (VANILLA.contains(keyName)) return Decision.VANILLA;
        if (namesPresentBeforeLaunch.contains(keyName)) return Decision.PRESERVE_PLAYER;
        return Decision.DEFAULT_UNBOUND;
    }

    public static Set<String> vanillaNames() {
        return VANILLA;
    }

    /**
     * Must run at Options.load() HEAD, before Default Options can save and
     * populate options.txt with entries the player has never configured.
     * A saved line is deliberately treated as player-owned, even if it is
     * identical to the original mod default (no reliable intent signal).
     */
    public static Set<String> readSavedNames(Path optionsFile) throws IOException {
        if (Files.notExists(optionsFile)) return Set.of();
        if (!Files.isRegularFile(optionsFile)) {
            throw new IOException("options.txt is not a readable regular file");
        }
        if (Files.size(optionsFile) > 1024 * 1024) {
            throw new IOException("Unexpected oversized options.txt; preserve all bindings");
        }
        Set<String> result = new HashSet<>();
        for (String line : Files.readAllLines(optionsFile, StandardCharsets.UTF_8)) {
            if (!line.startsWith("key_")) continue;
            int separator = line.indexOf(':');
            if (separator > 4) result.add(line.substring(4, separator));
        }
        return Set.copyOf(result);
    }
}
