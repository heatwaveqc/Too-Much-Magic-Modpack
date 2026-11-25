package io.github.heatwaveqc.tyr.progression.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.client.mixin.KeyMappingDefaultAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.settings.KeyModifier;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Client-only, registry-driven first-run controls policy:
 *   - NEVER touch the 34 vanilla mapping IDs;
 *   - change modded "Reset" defaults to unbound, except contextual inventory sorting and approved native JEI controls;
 *   - only clear a current modded binding if no matching key_ line was present
 *     in options.txt BEFORE mod configuration may rewrite that file;
 *   - process a KeyMapping identity once per launch, never repeatedly reset.
 *
 * Default Options must omit the matching JEI IDs and set contextual sort to middle mouse.
 * A dedicated server never loads this class or scans client-only mappings.
 */
@EventBusSubscriber(modid = ProgressionMod.MOD_ID, value = Dist.CLIENT)
public final class ClientKeyDefaults {
    private static final Set<KeyMapping> PROCESSED =
        Collections.newSetFromMap(new IdentityHashMap<>());
    private static Set<String> originalSavedNames;
    private static boolean snapshotFailed;
    private static boolean initialLog;
    private static final InputConstants.Key UNBOUND = InputConstants.UNKNOWN;
    private static final InputConstants.Key SORT_MIDDLE = InputConstants.Type.MOUSE.getOrCreate(2);

    private ClientKeyDefaults() {}

    /** Called by the client Options.load() HEAD mixin, exactly once per launch. */
    public static synchronized void captureOriginalOptions(Path originalFile) {
        if (originalSavedNames != null || snapshotFailed) return;
        try {
            originalSavedNames = ControlsDefaultsPolicy.readSavedNames(originalFile);
            ProgressionMod.LOGGER.info("TMM Controls: captured {} pre-launch saved key IDs",
                originalSavedNames.size());
        } catch (IOException | SecurityException failure) {
            snapshotFailed = true; // fail closed: never risk clobbering personal bindings
            ProgressionMod.LOGGER.error("TMM Controls: cannot read saved keys; automatic unbinding DISABLED", failure);
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        // Snapshot must precede any third-party options rewrite or save.
        if (snapshotFailed || originalSavedNames == null) return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options == null) return;

        int defaultsSet = 0;
        int firstRunUnbound = 0;
        int firstRunBound = 0;
        int playerPreserved = 0;
        int vanillaPreserved = 0;
        int nativePreserved = 0;
        for (KeyMapping mapping : minecraft.options.keyMappings) {
            if (mapping == null || !PROCESSED.add(mapping)) continue;
            ControlsDefaultsPolicy.Decision decision =
                ControlsDefaultsPolicy.decide(mapping.getName(), originalSavedNames);
            switch (decision) {
                case VANILLA -> vanillaPreserved++;
                case NATIVE_DEFAULT -> nativePreserved++;
                case PRESERVE_PLAYER, DEFAULT_UNBOUND, DEFAULT_PACK_BOUND -> {
                    InputConstants.Key targetDefault =
                        ControlsDefaultsPolicy.isContextualSort(mapping.getName())
                            ? SORT_MIDDLE : UNBOUND;
                    // Never rewrite saved player controls. Only update Reset's
                    // target, and initialize unsaved bindings on first launch.
                    if (!mapping.getDefaultKey().equals(targetDefault)
                        || mapping.getDefaultKeyModifier() != KeyModifier.NONE) {
                        KeyMappingDefaultAccessor accessor = (KeyMappingDefaultAccessor) mapping;
                        accessor.tmm$setDefaultKey(targetDefault);
                        accessor.tmm$setDefaultKeyModifier(KeyModifier.NONE);
                        defaultsSet++;
                    }
                    if (decision == ControlsDefaultsPolicy.Decision.PRESERVE_PLAYER) {
                        playerPreserved++;
                    } else if (!mapping.getKey().equals(targetDefault)
                        || mapping.getKeyModifier() != KeyModifier.NONE) {
                        mapping.setKeyModifierAndCode(KeyModifier.NONE, targetDefault);
                        if (decision == ControlsDefaultsPolicy.Decision.DEFAULT_PACK_BOUND) {
                            firstRunBound++;
                        } else {
                            firstRunUnbound++;
                        }
                    }
                }
            }
        }

        if (defaultsSet > 0 || firstRunUnbound > 0 || firstRunBound > 0) {
            KeyMapping.resetMapping();
        }
        if (firstRunUnbound > 0 || firstRunBound > 0) {
            // Persist the new defaults ONCE; subsequent sessions recognize all
            // saved key_ entries as player-owned and leave them unchanged.
            minecraft.options.save();
        }
        if (!initialLog || defaultsSet > 0 || firstRunUnbound > 0 || firstRunBound > 0
            || playerPreserved > 0 || vanillaPreserved > 0 || nativePreserved > 0) {
            if (!initialLog || defaultsSet > 0 || firstRunUnbound > 0 || firstRunBound > 0) {
                ProgressionMod.LOGGER.info(
                    "TMM Controls: registry scan: vanilla={}, native defaults={}, saved mod keys={}, updated mod defaults={}, newly unbound={}, new pack bindings={}",
                    vanillaPreserved, nativePreserved, playerPreserved, defaultsSet, firstRunUnbound, firstRunBound);
            }
            initialLog = true;
        }
    }
}
