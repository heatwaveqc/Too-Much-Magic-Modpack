package io.github.heatwaveqc.tyr.progression.command;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.core.AdminExperience;
import io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy;
import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.data.ExperienceEconomyRuntime;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;

/** Operator-only branches mounted beneath the existing administrative permission gate. */
final class DebugCommands {
    private DebugCommands() {}

    static LiteralArgumentBuilder<CommandSourceStack> currency() {
        var system = Commands.argument("system", ResourceLocationArgument.id())
            .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(
                ExperienceEconomyRuntime.current().systems().keySet(), builder))
            .then(Commands.literal("get").executes(ctx -> xp(ctx, null, 0, false)));
        for (var op : AdminExperience.Operation.values()) {
            var action = Commands.literal(op.name().toLowerCase(java.util.Locale.ROOT));
            if (op == AdminExperience.Operation.RESET) {
                action.executes(ctx -> xp(ctx, op, 0, true))
                    .then(Commands.literal("confirm").executes(ctx -> xp(ctx, op, 0, false)));
            } else {
                var amount = Commands.argument("amount", LongArgumentType.longArg(0));
                if (op == AdminExperience.Operation.ADD)
                    amount.executes(ctx -> xp(ctx, op, LongArgumentType.getLong(ctx,"amount"), false));
                else
                    amount.executes(ctx -> xp(ctx, op, LongArgumentType.getLong(ctx,"amount"), true))
                        .then(Commands.literal("confirm").executes(ctx -> xp(ctx, op,
                            LongArgumentType.getLong(ctx,"amount"), false)));
                action.then(amount);
            }
            system.then(action);
        }
        return Commands.literal("currency").requires(s -> s.hasPermission(2))
            .then(Commands.argument("player", EntityArgument.player()).then(system));
    }

    private static int xp(CommandContext<CommandSourceStack> ctx, AdminExperience.Operation op,
                          long amount, boolean preview) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        var source = ctx.getSource();
        var player = EntityArgument.getPlayer(ctx,"player");
        var id = ResourceLocationArgument.getId(ctx,"system").toString();
        var system = ExperienceEconomyRuntime.current().systems().get(id);
        if (system == null) {
            source.sendFailure(Component.literal("Unknown configured XP system: " + id));
            return 0;
        }
        var ledger = player.getData(ProgressionAttachments.LEDGER.get());
        try {
            var before = ledger.economy();
            var after = op == null ? before : AdminExperience.edit(before, system, op, amount);
            var oldXp = AdminExperience.balance(before, system);
            var newXp = AdminExperience.balance(after, system);
            var detail = player.getScoreboardName() + " " + id + ": " + oldXp.toPlainString()
                + (op == null ? "" : " -> " + newXp.toPlainString()) + " XP; level "
                + ExperienceEconomy.level(after,system) + ", committed "
                + ExperienceEconomy.committed(after,id) + ", available " + ExperienceEconomy.available(after,system);
            if (op != null && !preview) {
                player.setData(ProgressionAttachments.LEDGER.get(),ledger.withEconomy(after));
                ProgressionMod.LOGGER.info("Admin XP {} by {} for {} ({}): {}",op,source.getTextName(),
                    player.getScoreboardName(),player.getUUID(),detail);
            }
            source.sendSuccess(() -> Component.literal((preview ? "Preview; append confirm: " : "") + detail), false);
            return 1;
        } catch (IllegalArgumentException | IllegalStateException ex) {
            source.sendFailure(Component.literal("XP operation rejected: " + ex.getMessage()));
            return 0;
        }
    }

    static LiteralArgumentBuilder<CommandSourceStack> inspect() {
        return Commands.literal("inspect").requires(s -> s.hasPermission(2))
            .then(Commands.argument("player",EntityArgument.player()).executes(ctx -> {
                var player = EntityArgument.getPlayer(ctx,"player");
                var ledger = player.getData(ProgressionAttachments.LEDGER.get());
                var source = ctx.getSource();
                source.sendSuccess(() -> Component.literal("Progression state: " + player.getScoreboardName()),false);
                for (var path : PathRegistry.all().keySet().stream().sorted().toList()) {
                    var track = ProgressionApi.track(player,path);
                    if (track.joined()) source.sendSuccess(() -> Component.literal(path + ": rank "
                        + track.rank() + ", skills " + new java.util.TreeMap<>(track.skills())),false);
                }
                for (var system : ExperienceEconomyRuntime.current().systems().values()) {
                    var xp = AdminExperience.balance(ledger.economy(),system);
                    long committed = ExperienceEconomy.committed(ledger.economy(),system.id());
                    source.sendSuccess(() -> Component.literal(system.id() + ": " + xp.toPlainString()
                        + " XP, committed points " + committed),false);
                }
                source.sendSuccess(() -> Component.literal("Reward receipts: " + ledger.economy().rewardReceipts().size()
                    + "; achievements: " + ledger.economy().achievements().size()),false);
                return 1;
            }));
    }
}
