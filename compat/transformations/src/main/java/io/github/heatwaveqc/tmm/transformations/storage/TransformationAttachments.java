package io.github.heatwaveqc.tmm.transformations.storage;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import io.github.heatwaveqc.tmm.transformations.vampire.VampireBloodCarry;
import io.github.heatwaveqc.tmm.transformations.vampire.VampireBloodCarryCodec;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class TransformationAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, TransformationsMod.MOD_ID);

    public static final Supplier<AttachmentType<ReviewRecord>> REVIEW = ATTACHMENTS.register(
            "review", () -> AttachmentType.builder(ReviewRecord::initial)
                    .serialize(ReviewRecord.CODEC).copyOnDeath().build()
    );

    public static final Supplier<AttachmentType<VampireBloodCarry>> BLOOD_CARRY = ATTACHMENTS.register(
            "vampire_blood_carry", () -> AttachmentType.builder(VampireBloodCarry::empty)
                    .serialize(VampireBloodCarryCodec.CODEC).copyOnDeath().build()
    );

    /**
     * True only after both native vampire providers have been observed
     * together. Allows cure reconciliation when an upstream cure changes
     * its own attachment without calling the bridge's pre-hook.
     */
    public static final Supplier<AttachmentType<Boolean>> ASCENDED_VAMPIRE = ATTACHMENTS.register(
            "was_ascended_vampire", () -> AttachmentType.builder(() -> false)
                    .serialize(Codec.BOOL).copyOnDeath().build()
    );

    private TransformationAttachments() { }
}
