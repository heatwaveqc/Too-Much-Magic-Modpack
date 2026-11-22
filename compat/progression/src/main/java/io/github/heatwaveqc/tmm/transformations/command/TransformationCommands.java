package io.github.heatwaveqc.tmm.transformations.command;

import com.mojang.brigadier.Command;
import io.github.heatwaveqc.tmm.transformations.adapters.ProbeRegistry;
import io.github.heatwaveqc.tmm.transformations.core.*;
import io.github.heatwaveqc.tmm.transformations.storage.*;
import io.github.heatwaveqc.tmm.transformations.lich.ArchlichDiagnostics;
import io.github.heatwaveqc.tmm.transformations.dracolich.DracolichDiagnostics;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/** Read-only diagnostics: JAWR alone owns its forms and native cure actions. */
public final class TransformationCommands {
    private TransformationCommands() { }

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("tmmtransform")
            .then(Commands.literal("status").executes(c -> status(c.getSource())))
            .then(Commands.literal("vampire").executes(c -> probe(c.getSource(), ProviderState.JAWR_VAMPIRE)))
            .then(Commands.literal("werewolf").executes(c -> probe(c.getSource(), ProviderState.JAWR_LYCANTHROPE)))
            .then(Commands.literal("archlich").executes(c -> diagnostic(c.getSource(), true)))
            .then(Commands.literal("dracolich").executes(c -> diagnostic(c.getSource(), false))));
    }

    private static int probe(CommandSourceStack source, ProviderState state)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        var result = ProbeRegistry.observe(player).get(state);
        source.sendSuccess(() -> Component.literal("Native JAWR " + state + " = " + result
                + ". Transformation, blood, hunger and curing remain JAWR-owned."), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int diagnostic(CommandSourceStack source, boolean archlich)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        String report = archlich ? ArchlichDiagnostics.describe(player)
                                 : DracolichDiagnostics.describe(player);
        source.sendSuccess(() -> Component.literal(report), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int status(CommandSourceStack source)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        StateSnapshot snapshot = ProbeRegistry.observe(player);
        var form = FormResolver.resolve(snapshot);
        ReviewRecord review = player.getData(TransformationAttachments.REVIEW.get());
        source.sendSuccess(() -> Component.literal("TMM native form = " + form
            + " (" + snapshot.verifiedCount() + "/" + ProviderState.values().length
            + " providers observed; JAWR-only physiology)"), false);
        for (ProviderState state : ProviderState.values()) {
            Observation observation = snapshot.get(state);
            source.sendSuccess(() -> Component.literal(" - " + state + ": " + observation), false);
        }
        if (!snapshot.isComplete())
            source.sendSuccess(() -> Component.literal(
                "UNVERIFIED providers; classification is fail-closed. Specialized resurrection handlers have separate gates."), false);
        source.sendSuccess(() -> Component.literal(
                "Review metadata: schema " + review.schemaVersion() + ", admin hold " + review.adminHold()
                + (review.note().isBlank() ? "" : ", note: " + review.note())), false);
        return Command.SINGLE_SUCCESS;
    }
}
