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

    // Intentional GUI-only exception to the pack's otherwise unbound mod controls.
    // Do not change the vanilla middle-mouse pick-block mapping.
    private static final String CONTEXTUAL_SORT = "key.invtweaks_sort_either.desc";

    // Exact IDs shared with the pack's Default Options generator (#510).
    // Preserve upstream key AND modifier defaults, including intentionally unbound ones.
    private static final Set<String> JEI_NATIVE_DEFAULTS = Set.of(
        "key.jei.bookmark",
        "key.jei.clearSearchBar",
        "key.jei.closeRecipeGui",
        "key.jei.focusSearch",
        "key.jei.nextCategory",
        "key.jei.nextPage",
        "key.jei.nextRecipePage",
        "key.jei.nextSearch",
        "key.jei.previousCategory",
        "key.jei.previousPage",
        "key.jei.previousRecipePage",
        "key.jei.previousSearch",
        "key.jei.recipeBack",
        "key.jei.recipeForward",
        "key.jei.showRecipe",
        "key.jei.showUses",
        "key.jei.toggleBookmarkOverlay",
        "key.jei.toggleOverlay",
        "key.jei.transferRecipeGui",
        "key.jei.transferRecipeBookmark"
    );

    public enum Decision { VANILLA, NATIVE_DEFAULT, PRESERVE_PLAYER, DEFAULT_UNBOUND, DEFAULT_PACK_BOUND }

    public static Set<String> nativeDefaults() { return JEI_NATIVE_DEFAULTS; }

    public static boolean isContextualSort(String keyName) {
        return CONTEXTUAL_SORT.equals(keyName);
    }

    public static Decision decide(String keyName, Set<String> namesPresentBeforeLaunch) {
        if (VANILLA.contains(keyName)) return Decision.VANILLA;
        // No changes to current or Reset for these IDs, saved or fresh.
        if (JEI_NATIVE_DEFAULTS.contains(keyName)) return Decision.NATIVE_DEFAULT;
        if (namesPresentBeforeLaunch.contains(keyName)) return Decision.PRESERVE_PLAYER;
        if (isContextualSort(keyName)) return Decision.DEFAULT_PACK_BOUND;
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
