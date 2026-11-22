package io.github.heatwaveqc.tyr.progression.state;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import java.util.function.Supplier;

public final class ProgressionAttachments {
    private ProgressionAttachments() {}

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ProgressionMod.MOD_ID);
    public static final Supplier<AttachmentType<Modes>> PLAYER_MODES =
        ATTACHMENTS.register("player_modes", () -> AttachmentType.builder(Modes::empty)
            .serialize(Modes.CODEC).copyOnDeath().build());
    public static final Supplier<AttachmentType<Discoveries>> FOOD_HISTORY =
        ATTACHMENTS.register("food_history", () -> AttachmentType.builder(Discoveries::empty)
            .serialize(Discoveries.CODEC).copyOnDeath().build());
    public static final Supplier<AttachmentType<MutationLedger>> MUTATIONS =
        ATTACHMENTS.register("mutations", () -> AttachmentType.builder(MutationLedger::empty)
            .serialize(MutationLedger.codec()).copyOnDeath().build());
    public static final Supplier<AttachmentType<TooltipMilestones>> TOOLTIP_MILESTONES =
        ATTACHMENTS.register("tooltip_milestones", () -> AttachmentType.builder(TooltipMilestones::empty)
            .serialize(TooltipMilestones.CODEC).copyOnDeath().build());
    public static final Supplier<AttachmentType<Ledger>> LEDGER =
        ATTACHMENTS.register("ledger", () -> AttachmentType.builder(Ledger::empty)
            .serialize(LedgerCodec.CODEC).copyOnDeath().build());
}
