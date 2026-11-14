package io.github.heatwaveqc.tmm.transformations.storage;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import io.github.heatwaveqc.tmm.transformations.vampire.VampireBloodCarry;
import java.util.function.Supplier;
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
                    .serialize(VampireBloodCarry.CODEC).copyOnDeath().build()
    );

    private TransformationAttachments() { }
}
