package io.github.heatwaveqc.tmm.transformations.core;

import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Form;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Presence;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Snapshot;
import java.util.EnumMap;
import java.util.Objects;

/**
 * One-way adapter from Progression's authoritative per-species observations
 * into the historical five-provider transition model.
 *
 * Do not independently re-probe native attachments at the acquisition boundary:
 * Path compatibility and native transition preflight must use the same instant.
 * This does not transfer physiology ownership away from the provider mods.
 */
public final class ProgressionNativeSnapshot {
    private ProgressionNativeSnapshot() {}

    public static StateSnapshot from(Snapshot snapshot) {
        Objects.requireNonNull(snapshot, "snapshot");
        EnumMap<ProviderState, Observation> mapped = new EnumMap<>(ProviderState.class);
        mapped.put(ProviderState.JAWR_VAMPIRE, observation(snapshot.get(Form.JAWR_VAMPIRE)));
        mapped.put(ProviderState.JAWR_LYCANTHROPE, observation(snapshot.get(Form.JAWR_WEREWOLF)));
        mapped.put(ProviderState.GOETY_LICH, observation(snapshot.get(Form.GOETY_LICH)));
        mapped.put(ProviderState.FORBIDDEN_MAGIC_LICH,
            observation(snapshot.get(Form.FORBIDDEN_MAGIC_LICH)));

        int speciesPresent = 0;
        boolean speciesUnknown = false;
        for (Form form : new Form[]{Form.CAVE_DRAGON, Form.FOREST_DRAGON, Form.SEA_DRAGON}) {
            Presence presence = snapshot.get(form);
            if (presence == Presence.PRESENT) speciesPresent++;
            if (presence == Presence.UNKNOWN) speciesUnknown = true;
        }
        // A single Dragon Survival player cannot have two native species;
        // partial observations must not be inferred to mean one known species.
        Observation dragon = speciesUnknown || speciesPresent > 1
            ? Observation.UNKNOWN
            : speciesPresent == 1 ? Observation.PRESENT : Observation.ABSENT;
        mapped.put(ProviderState.DRAGON_SURVIVAL_DRAGON, dragon);
        return new StateSnapshot(mapped);
    }

    private static Observation observation(Presence presence) {
        return switch (presence) {
            case PRESENT -> Observation.PRESENT;
            case ABSENT -> Observation.ABSENT;
            case UNKNOWN -> Observation.UNKNOWN;
        };
    }
}
