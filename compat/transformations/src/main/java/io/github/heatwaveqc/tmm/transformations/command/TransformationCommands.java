package io.github.heatwaveqc.tmm.transformations.command;

import com.mojang.brigadier.Command;
import io.github.heatwaveqc.tmm.transformations.adapters.ProbeRegistry;
import io.github.heatwaveqc.tmm.transformations.core.EffectiveForm;
import io.github.heatwaveqc.tmm.transformations.core.FormResolver;
import io.github.heatwaveqc.tmm.transformations.core.ProviderState;
import io.github.heatwaveqc.tmm.transformations.core.StateSnapshot;
import io.github.heatwaveqc.tmm.transformations.storage.ReviewRecord;
import io.github.heatwaveqc.tmm.transformations.storage.TransformationAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/** Read-only server-side diagnostics. No ability to set upstream state. */
public final class TransformationCommands {
    private TransformationCommands() { }

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("tmmtransform")
                .then(Commands.literal("status").executes(ctx -> status(ctx.getSource()))));
    }

    private static int status(CommandSourceStack source) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        StateSnapshot snapshot = ProbeRegistry.observe(player);
        EffectiveForm form = FormResolver.resolve(snapshot);
        ReviewRecord review = player.getData(TransformationAttachments.REVIEW.get());
        source.sendSuccess(() -> Component.literal("TMM bridge: " + form
                + " (" + snapshot.verifiedCount() + "/" + ProviderState.values().length
                + " source states observed; " + ProbeRegistry.registeredCount() + " probes registered)"), false);
        if (!snapshot.isComplete()) {
            source.sendSuccess(() -> Component.literal(
                    "UNVERIFIED: upstream adapters are incomplete. No transformation blocking, merging or resurrection arbitration is active."), false);
        }
        source.sendSuccess(() -> Component.literal(
                "Review metadata: schema " + review.schemaVersion()
                        + ", admin hold " + review.adminHold()
                        + (review.note().isBlank() ? "" : ", note: " + review.note())), false);
        return Command.SINGLE_SUCCESS;
    }
}
