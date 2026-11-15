package io.github.heatwaveqc.tmm.progression.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import io.github.heatwaveqc.tmm.progression.runtime.Enrollment;
import io.github.heatwaveqc.tmm.progression.data.PathRegistry;
import io.github.heatwaveqc.tmm.progression.state.Track;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/** Minimal command UI for integration testing; graphical skill UI is separate work. */
public final class ProgressionCommands {
    private ProgressionCommands() {}

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("tmmprogress")
            .then(Commands.literal("list")
                .executes(ctx -> {
                    ctx.getSource().sendSuccess(() -> Component.literal(
                        "Paths: " + String.join(", ",PathRegistry.all().keySet())),false);
                    return 1;
                }))
            .then(Commands.literal("enroll")
                .then(Commands.argument("path",StringArgumentType.word())
                    .executes(ctx -> {
                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                        String id = StringArgumentType.getString(ctx,"path");
                        boolean ok = Enrollment.enrollManual(player,id);
                        ctx.getSource().sendSuccess(() -> Component.literal(
                            ok ? "Joined " + id : "Enrollment unavailable or requirements unmet"),false);
                        return ok ? 1 : 0;
                    })))
            .then(Commands.literal("status")
                .then(Commands.argument("path",StringArgumentType.word())
                    .executes(ctx -> {
                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                        String path = StringArgumentType.getString(ctx,"path");
                        Track t = ProgressionApi.track(player,path);
                        ctx.getSource().sendSuccess(() -> Component.literal(
                            path + ": joined=" + t.joined() + ", XP=" + t.xp() +
                            ", rank=" + t.rank() + ", points=" + t.points() +
                            ", skills=" + t.skills() + ", milestones=" + t.milestones()),false);
                        return 1;
                    })))
            .then(Commands.literal("buy")
                .then(Commands.argument("path",StringArgumentType.word())
                    .then(Commands.argument("skill",StringArgumentType.word())
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            boolean success = ProgressionApi.buy(player,
                                StringArgumentType.getString(ctx,"path"),
                                StringArgumentType.getString(ctx,"skill"));
                            ctx.getSource().sendSuccess(() -> Component.literal(
                                success ? "Skill rank purchased" : "Skill purchase blocked"),false);
                            return success ? 1 : 0;
                        }))))
            .then(Commands.literal("respec")
                .then(Commands.argument("path",StringArgumentType.word())
                    .executes(ctx -> {
                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                        boolean success = ProgressionApi.respec(player,StringArgumentType.getString(ctx,"path"));
                        ctx.getSource().sendSuccess(() -> Component.literal(
                            success ? "Purchased skills refunded; free milestones retained" :
                                      "Respec unavailable"),false);
                        return success ? 1 : 0;
                    })))
            .then(Commands.literal("admin").requires(s -> s.hasPermission(2))
                .then(Commands.literal("join")
                    .then(Commands.argument("path",StringArgumentType.word())
                        .executes(ctx -> {
                            boolean ok = ProgressionApi.join(ctx.getSource().getPlayerOrException(),
                                StringArgumentType.getString(ctx,"path"));
                            ctx.getSource().sendSuccess(() -> Component.literal(
                                ok ? "Joined progression path" : "Path missing or exclusive"),false);
                            return ok ? 1 : 0;
                        })))
                .then(Commands.literal("event")
                    .then(Commands.argument("event_id",StringArgumentType.word())
                        .executes(ctx -> {
                            ProgressionApi.recordExternal(ctx.getSource().getPlayerOrException(),
                                StringArgumentType.getString(ctx,"event_id"));
                            ctx.getSource().sendSuccess(() -> Component.literal("Event recorded"),false);
                            return 1;
                        })))));
    }
}
