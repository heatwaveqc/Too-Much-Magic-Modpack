package io.github.heatwaveqc.tmm.transformations.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Persistent bridge-owned review metadata. NEVER a source of truth for the
 * underlying transformations or a substitute for reading upstream states.
 */
public record ReviewRecord(int schemaVersion, boolean adminHold, String note) {
    public static final int CURRENT_SCHEMA = 1;

    public static final Codec<ReviewRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("schema_version", CURRENT_SCHEMA).forGetter(ReviewRecord::schemaVersion),
            Codec.BOOL.optionalFieldOf("admin_hold", false).forGetter(ReviewRecord::adminHold),
            Codec.STRING.optionalFieldOf("note", "").forGetter(ReviewRecord::note)
    ).apply(instance, ReviewRecord::new));

    public static ReviewRecord initial() {
        return new ReviewRecord(CURRENT_SCHEMA, false, "");
    }
}
