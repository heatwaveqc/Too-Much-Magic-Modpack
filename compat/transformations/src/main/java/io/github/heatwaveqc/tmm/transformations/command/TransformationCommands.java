package io.github.heatwaveqc.tmm.transformations.command;

import com.mojang.brigadier.Command;
import io.github.heatwaveqc.tmm.transformations.abomination.AbominationActionPilot;
import io.github.heatwaveqc.tmm.transformations.adapters.ProbeRegistry;
import io.github.heatwaveqc.tmm.transformations.core.EffectiveForm;
import io.github.heatwaveqc.tmm.transformations.core.FormResolver;
import io.github.heatwaveqc.tmm.transformations.core.Observation;
import io.github.heatwaveqc.tmm.transformations.core.ProviderState;
import io.github.heatwaveqc.tmm.transformations.core.StateSnapshot;
import io.github.heatwaveqc.tmm.transformations.storage.ReviewRecord;
import io.github.heatwaveqc.tmm.transformations.vampire.VampireDiagnostics;
import io.github.heatwaveqc.tmm.transformations.vampire.VampireOwnership;
import io.github.heatwaveqc.tmm.transformations.vampire.VampireSubsystem;
import io.github.heatwaveqc.tmm.transformations.vampire.VampireChassisConfig;
import io.github.heatwaveqc.tmm.transformations.vampire.VampireChassisRuntime;
import io.github.heatwaveqc.tmm.transformations.storage.TransformationAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/** Read-only server-side diagnostics; cannot set or cure upstream states. */
public final class TransformationCommands {
    private TransformationCommands() { }

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("tmmtransform")
                .then(Commands.literal("status").executes(ctx -> status(ctx.getSource())))
                .then(Commands.literal("vampire").executes(ctx -> vampire(ctx.getSource()))
                        .then(Commands.literal("cure").requires(s -> s.hasPermission(2))
                                .executes(ctx -> cure(ctx.getSource()))))
                .then(Commands.literal("abomination").requires(s -> s.hasPermission(2))
                        .then(Commands.literal("pilot")
                                .executes(ctx -> abominationStatus(ctx.getSource()))
                                .then(Commands.literal("vampire")
                                        .executes(ctx -> abominationAbility(ctx.getSource(), AbominationActionPilot.Ability.HISS)))
                                .then(Commands.literal("werewolf")
                                        .executes(ctx -> abominationAbility(ctx.getSource(), AbominationActionPilot.Ability.HOWL))))));
    }

    private static int abominationStatus(CommandSourceStack source)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        var state = AbominationActionPilot.readiness(player);
        source.sendSuccess(() -> Component.literal("Abomination model: planned ONE new faction + independent vampire/werewolf/JAWR progressions; faction not yet registered."), false);
        source.sendSuccess(() -> Component.literal("Two-native-ability pilot: " + state.verdict()
                + "; JAWR vampire=" + state.vampire() + ", JAWR lycanthrope=" + state.wolf()
                + ", Hunter=" + state.hunter() + "; " + state.reason()), false);
        source.sendSuccess(() -> Component.literal("Native Hiss skill: " + AbominationActionPilot.nativeEntitlement(player, AbominationActionPilot.Ability.HISS)
                + "; native Howl skill: " + AbominationActionPilot.nativeEntitlement(player, AbominationActionPilot.Ability.HOWL)), false);
        source.sendSuccess(() -> Component.literal("Test uses native HissingAction and HowlingAction effects via their original player attachments, "
                + "but bypasses original skill unlocks and normal action event/packet handlers. It NEVER grants levels or skills."), false);
        return state.ready() ? Command.SINGLE_SUCCESS : 0;
    }

    private static int abominationAbility(CommandSourceStack source, AbominationActionPilot.Ability ability)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        var result = AbominationActionPilot.activate(source.getPlayerOrException(), ability);
        if (result.success()) {
            source.sendSuccess(() -> Component.literal("Abomination pilot: " + result.message()), false);
            return Command.SINGLE_SUCCESS;
        }
        source.sendFailure(Component.literal("Abomination pilot refused: " + result.message()));
        return 0;
    }

    private static int cure(CommandSourceStack source) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        if (!VampireChassisConfig.cure()) {
            source.sendFailure(Component.literal("Operator cure disabled. Requires both experimentalVampireChassis and experimentalOperatorCure in the world SERVER config. Back up the world first."));
            return 0;
        }
        if (VampireChassisRuntime.testCure(player)) {
            source.sendSuccess(() -> Component.literal("Both vampire states were removed. Verify the player skills, items and food HUD."), false);
            return 1;
        }
        source.sendFailure(Component.literal("Coordinated cure refused or failed. Check native state and admin hold; do not retry automatically."));
        return 0;
    }

    private static int vampire(CommandSourceStack source) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        StateSnapshot snapshot = ProbeRegistry.observe(player);
        var vampirism = snapshot.get(ProviderState.VAMPIRISM_VAMPIRE);
        var jawr = snapshot.get(ProviderState.JAWR_VAMPIRE);
        source.sendSuccess(() -> Component.literal("Vampire fusion: chassis=" + VampireChassisConfig.chassis()
                + ", autoAcquisition=" + VampireChassisConfig.acquisition()
                + ", operatorCure=" + VampireChassisConfig.cure()), false);
        source.sendSuccess(() -> Component.literal("Vampire fusion diagnostic: Vampirism=" + vampirism
                + ", JAWR=" + jawr + "; experimental interception only when enabled in SERVER config"), false);
        source.sendSuccess(() -> Component.literal("Vampirism raw meters: "
                + VampireDiagnostics.vampirism(player).map(v ->
                        "native level " + v.level() + ", blood " + v.blood() + "/" + v.maxBlood())
                        .orElse("unreadable or mod absent")), false);
        source.sendSuccess(() -> Component.literal("JAWR raw meters: "
                + VampireDiagnostics.jawr(player).map(v ->
                        "native level " + v.level() + ", blood " + v.blood() + "/" + v.maxBlood())
                        .orElse("unreadable or mod absent")), false);
        source.sendSuccess(() -> Component.literal("Intended single blood owner: " +
                VampireOwnership.target(vampirism, jawr, VampireSubsystem.BLOOD_STORAGE)
                + " (design target only; runtime arbitration NOT active)"), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int status(CommandSourceStack source) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        StateSnapshot snapshot = ProbeRegistry.observe(player);
        EffectiveForm form = FormResolver.resolve(snapshot);
        ReviewRecord review = player.getData(TransformationAttachments.REVIEW.get());
        source.sendSuccess(() -> Component.literal("TMM bridge: " + form
                + " (" + snapshot.verifiedCount() + "/" + ProviderState.values().length
                + " upstream states observed; " + ProbeRegistry.registeredCount() + " probes registered)"), false);
        for (ProviderState provider : ProviderState.values()) {
            Observation observation = snapshot.get(provider);
            source.sendSuccess(() -> Component.literal(" - " + provider + ": " + observation), false);
        }
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
