package io.github.heatwaveqc.tyr.progression.command;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.core.AdminExperience;
import io.github.heatwaveqc.tyr.progression.core.AdminSkills;
import io.github.heatwaveqc.tyr.progression.core.ChannelEngine;
import com.mojang.brigadier.arguments.IntegerArgumentType;
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
                + ExperienceEconomy.committed(after,id) + ", available " + ExperienceEconomy.available(after,system)
                + ", debt " + ExperienceEconomy.debt(after,system);
            if (op != null && !preview) {
                player.setData(ProgressionAttachments.LEDGER.get(),ledger.withEconomy(after));
                reconcile(player);
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

    static LiteralArgumentBuilder<CommandSourceStack> skill() {
        var node = Commands.argument("skill",ResourceLocationArgument.id())
            .suggests((ctx,builder) -> {
                var path = PathRegistry.get(ResourceLocationArgument.getId(ctx,"path").toString());
                return SharedSuggestionProvider.suggest(path == null ? java.util.List.<String>of()
                    : path.skills().stream().map(s -> s.id()).toList(),builder);
            })
            .then(Commands.literal("inspect").executes(ctx -> skillEdit(ctx,"inspect",false,false,true)));
        var grant = Commands.argument("system",ResourceLocationArgument.id())
            .suggests((ctx,builder) -> {
                var rule = ExperienceEconomyRuntime.current().skills().get(
                    new io.github.heatwaveqc.tyr.progression.data.ExperienceEconomyConfig.SkillKey(
                        ResourceLocationArgument.getId(ctx,"path").toString(),
                        ResourceLocationArgument.getId(ctx,"skill").toString()));
                return SharedSuggestionProvider.suggest(rule == null ? java.util.List.<String>of()
                    : rule.sources().stream().map(s -> s.system()).toList(),builder);
            }).then(Commands.argument("ranks",IntegerArgumentType.integer(1,10000))
                .executes(ctx -> skillEdit(ctx,"grant",false,false,false)));
        node.then(Commands.literal("grant").then(grant));
        var reset = Commands.literal("reset");
        reset.executes(ctx -> skillEdit(ctx,"reset",false,false,true))
            .then(Commands.literal("confirm").executes(ctx -> skillEdit(ctx,"reset",false,false,false)));
        var cascade = Commands.literal("cascade")
            .executes(ctx -> skillEdit(ctx,"reset",true,false,true))
            .then(Commands.literal("confirm").executes(ctx -> skillEdit(ctx,"reset",true,false,false)))
            .then(Commands.literal("no_refund")
                .executes(ctx -> skillEdit(ctx,"reset",true,true,true))
                .then(Commands.literal("confirm").executes(ctx -> skillEdit(ctx,"reset",true,true,false))));
        reset.then(cascade).then(Commands.literal("no_refund")
            .executes(ctx -> skillEdit(ctx,"reset",false,true,true))
            .then(Commands.literal("confirm").executes(ctx -> skillEdit(ctx,"reset",false,true,false))));
        node.then(reset);
        return Commands.literal("skill").requires(s -> s.hasPermission(2))
            .then(Commands.argument("player",EntityArgument.player())
                .then(Commands.argument("path",ResourceLocationArgument.id())
                    .suggests((ctx,builder) -> SharedSuggestionProvider.suggest(PathRegistry.all().keySet(),builder))
                    .then(node)));
    }

    private static int skillEdit(CommandContext<CommandSourceStack> ctx,String operation,
                                 boolean cascade,boolean forfeit,boolean preview)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        var source = ctx.getSource();
        var player = EntityArgument.getPlayer(ctx,"player");
        var pathId = ResourceLocationArgument.getId(ctx,"path").toString();
        var skillId = ResourceLocationArgument.getId(ctx,"skill").toString();
        var path = PathRegistry.get(pathId);
        var skill = path == null ? null : path.skill(skillId);
        if (skill == null) {
            source.sendFailure(Component.literal("Unknown path or skill")); return 0;
        }
        var before = player.getData(ProgressionAttachments.LEDGER.get());
        var config = PathRegistry.config();
        try {
            var prior = ChannelEngine.view(before,path,config);
            if (operation.equals("inspect")) {
                var rule = ExperienceEconomyRuntime.current().skills().get(
                    new io.github.heatwaveqc.tyr.progression.data.ExperienceEconomyConfig.SkillKey(pathId,skillId));
                source.sendSuccess(() -> Component.literal(skillId + ": rank " + prior.skillRank(skillId)
                    + "/" + skill.maxRank() + "; currencies " + (rule == null ? "not configured" : rule.sources())
                    + "; requires all " + skill.requiresSkills() + "; any " + skill.requiresAnySkills()
                    + "; milestones " + skill.requiresMilestones()),false);
                return 1;
            }
            var next = before;
            String detail;
            if (operation.equals("grant")) {
                var system = ResourceLocationArgument.getId(ctx,"system").toString();
                int count = IntegerArgumentType.getInteger(ctx,"ranks");
                if (!PathRegistry.purchaseEligible(player,pathId,skillId))
                    throw new IllegalArgumentException("Skill purchase eligibility not met");
                var binding = config.skills().get(new io.github.heatwaveqc.tyr.progression.data.ChannelConfig.SkillKey(pathId,skillId));
                if (binding != null && binding.awardPoints() > 0)
                    throw new IllegalArgumentException("Native currency reward requires explicit recovery handler");
                for (int i=0;i<count;i++) {
                    var granted = ChannelEngine.grantRank(next,path,config,skillId,system);
                    if (granted == next) throw new IllegalArgumentException("Grant rejected by currency, rank cap or prerequisites; no ranks changed");
                    next = granted;
                }
                detail = skillId + " rank " + prior.skillRank(skillId) + " -> "
                    + ChannelEngine.view(next,path,config).skillRank(skillId) + " via " + system;
            } else {
                var result = AdminSkills.reset(before,path,config,skillId,cascade,forfeit);
                next = result.ledger();
                detail = "Reset " + result.removed().stream()
                    .map(id -> id + " " + prior.skillRank(id) + " -> 0").toList()
                    + (forfeit ? " (forfeit funded XP)" : " (release commitments)");
            }
            if (!preview) {
                player.setData(ProgressionAttachments.LEDGER.get(),next);
                ProgressionMod.LOGGER.info("Admin skill {} by {} for {} ({}): {}",operation,source.getTextName(),
                    player.getScoreboardName(),player.getUUID(),detail);
                reconcile(player);
            }
            source.sendSuccess(() -> Component.literal((preview ? "Preview; append confirm: " : "") + detail),false);
            return 1;
        } catch (IllegalArgumentException | IllegalStateException ex) {
            source.sendFailure(Component.literal("Skill operation rejected: " + ex.getMessage()));
            return 0;
        }
    }

    private static void reconcile(net.minecraft.server.level.ServerPlayer player) {
        io.github.heatwaveqc.tyr.progression.runtime.SkillActivation.reconcile(player);
        io.github.heatwaveqc.tyr.progression.runtime.EffectRuntime.reconcile(player);
        io.github.heatwaveqc.tyr.progression.runtime.PassivePowers.reconcile(player,true);
        io.github.heatwaveqc.tyr.progression.runtime.VirtualElytra.reconcile(player,true);
        io.github.heatwaveqc.tyr.progression.runtime.VisionRuntime.reconcile(player,true);
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
