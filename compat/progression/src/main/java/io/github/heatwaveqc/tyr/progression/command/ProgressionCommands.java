package io.github.heatwaveqc.tyr.progression.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.runtime.Enrollment;
import io.github.heatwaveqc.tyr.progression.runtime.ClassAbilities;
import io.github.heatwaveqc.tyr.progression.runtime.HunterExperience;
import io.github.heatwaveqc.tyr.progression.runtime.HunterMutations;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.state.Track;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/** Minimal command UI for integration testing; graphical skill UI is separate work. */
public final class ProgressionCommands {
    private ProgressionCommands() {}

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("tyrprogress")
            .then(Commands.literal("list")
                .executes(ctx -> {
                    ctx.getSource().sendSuccess(() -> Component.literal(
                        "Paths: " + String.join(", ",PathRegistry.all().keySet())),false);
                    return 1;
                }))
            .then(Commands.literal("enroll")
                .then(Commands.argument("path",ResourceLocationArgument.id())
                    .executes(ctx -> {
                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                        String id = ResourceLocationArgument.getId(ctx,"path").toString();
                        boolean ok = Enrollment.enrollManual(player,id);
                        ctx.getSource().sendSuccess(() -> Component.literal(
                            ok ? "Joined " + id : "Enrollment unavailable or requirements unmet"),false);
                        return ok ? 1 : 0;
                    })))
            .then(Commands.literal("status")
                .then(Commands.argument("path",ResourceLocationArgument.id())
                    .executes(ctx -> {
                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                        String path = ResourceLocationArgument.getId(ctx,"path").toString();
                        Track t = ProgressionApi.track(player,path);
                        ctx.getSource().sendSuccess(() -> Component.literal(
                            path + ": joined=" + t.joined() + ", XP=" + t.xp() +
                            ", rank=" + t.rank() + ", points=" + t.points() +
                            ", skills=" + t.skills() + ", milestones=" + t.milestones()),false);
                        return 1;
                    })))
            .then(Commands.literal("buy")
                .then(Commands.argument("path",ResourceLocationArgument.id())
                    .then(Commands.argument("skill",ResourceLocationArgument.id())
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            boolean success = ProgressionApi.buy(player,
                                ResourceLocationArgument.getId(ctx,"path").toString(),
                                ResourceLocationArgument.getId(ctx,"skill").toString());
                            ctx.getSource().sendSuccess(() -> Component.literal(
                                success ? "Skill rank purchased" : "Skill purchase blocked"),false);
                            return success ? 1 : 0;
                        }))))
            .then(Commands.literal("mutations")
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    ctx.getSource().sendSuccess(() -> Component.literal(HunterMutations.summary(player)),false);
                    return 1;
                }))
            .then(Commands.literal("specialty")
                .then(Commands.literal("list")
                    .executes(ctx -> {
                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                        ctx.getSource().sendSuccess(() -> Component.literal(
                            "Hunter specialty: " + HunterExperience.selectedSpecialty(player)
                                + " | choices: " + String.join(", ",HunterExperience.availableSpecialties())),false);
                        return 1;
                    }))
                .then(Commands.argument("family",StringArgumentType.word())
                    .executes(ctx -> {
                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                        String family = StringArgumentType.getString(ctx,"family");
                        boolean ok = HunterExperience.availableSpecialties().contains(family)
                            && ProgressionApi.buy(player,HunterExperience.HUNTER,
                                "tyr_progression:specialty_" + family);
                        ctx.getSource().sendSuccess(() -> Component.literal(ok ?
                            "Hunter specialty selected: " + family :
                            "Selection blocked: join Hunter first, or your specialty is already set"),false);
                        return ok ? 1 : 0;
                    })))
            .then(Commands.literal("respec")
                .then(Commands.argument("path",ResourceLocationArgument.id())
                    .executes(ctx -> {
                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                        boolean success = ProgressionApi.respec(player,ResourceLocationArgument.getId(ctx,"path").toString());
                        ctx.getSource().sendSuccess(() -> Component.literal(
                            success ? "Purchased skills refunded; free milestones retained" :
                                      "Respec unavailable"),false);
                        return success ? 1 : 0;
                    })))
            .then(Commands.literal("power")
                .then(Commands.literal("blink")
                    .executes(ctx -> {
                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                        boolean ok = ClassAbilities.blink(player);
                        player.displayClientMessage(Component.literal(ok ?
                            "Blink" : "Blink blocked, unavailable or cooling down"),true);
                        return ok ? 1 : 0;
                    }))
                .then(Commands.literal("armor")
                    .executes(ctx -> {
                        ServerPlayer p = ctx.getSource().getPlayerOrException();
                        boolean ok = ClassAbilities.toggle(p,"armor");
                        p.displayClientMessage(Component.literal(ok ?
                            "Armor Lock: " + ClassAbilities.mode(p,"armor") :
                            "Armor Lock not unlocked"),true);
                        return ok ? 1 : 0;
                    }))
                .then(Commands.literal("glide")
                    .executes(ctx -> {
                        ServerPlayer p = ctx.getSource().getPlayerOrException();
                        boolean ok = ClassAbilities.toggle(p,"glide");
                        p.displayClientMessage(Component.literal(ok ?
                            "Momentum Glide: " + ClassAbilities.mode(p,"glide") :
                            "Momentum Glide requires verified dragon defeat and mastery"),true);
                        return ok ? 1 : 0;
                    }))
                .then(Commands.literal("ordeal")
                    .executes(ctx -> {
                        ServerPlayer p = ctx.getSource().getPlayerOrException();
                        boolean ok = ClassAbilities.toggle(p,"ordeal");
                        p.displayClientMessage(Component.literal(ok ?
                            "Blindness Ordeal: " + ClassAbilities.mode(p,"ordeal") :
                            "Blindness Ordeal unavailable"),true);
                        return ok ? 1 : 0;
                    }))
                .then(Commands.literal("treasure")
                    .executes(ctx -> {
                        ServerPlayer p = ctx.getSource().getPlayerOrException();
                        boolean ok = ClassAbilities.toggle(p,"treasure");
                        p.displayClientMessage(Component.literal(ok ?
                            "Treasure Hand: " + ClassAbilities.mode(p,"treasure") :
                            "Treasure Hand not unlocked"),true);
                        return ok ? 1 : 0;
                    })))
            .then(Commands.literal("admin").requires(s -> s.hasPermission(2))
                .then(Commands.literal("join")
                    .then(Commands.argument("path",ResourceLocationArgument.id())
                        .executes(ctx -> {
                            boolean ok = ProgressionApi.join(ctx.getSource().getPlayerOrException(),
                                ResourceLocationArgument.getId(ctx,"path").toString());
                            ctx.getSource().sendSuccess(() -> Component.literal(
                                ok ? "Joined progression path" : "Path missing or exclusive"),false);
                            return ok ? 1 : 0;
                        })))
                .then(Commands.literal("xp")
                    .then(Commands.argument("path",ResourceLocationArgument.id())
                        .then(Commands.argument("amount",LongArgumentType.longArg(1L,1_000_000L))
                            .executes(ctx -> {
                                ServerPlayer player = ctx.getSource().getPlayerOrException();
                                String path = ResourceLocationArgument.getId(ctx,"path").toString();
                                long amount = LongArgumentType.getLong(ctx,"amount");
                                boolean ok = ProgressionApi.awardServerXp(player,path,amount);
                                ctx.getSource().sendSuccess(() -> Component.literal(ok ?
                                    "Granted " + amount + " progression XP to " + path :
                                    "XP grant blocked: join that path first, or invalid context"),false);
                                return ok ? 1 : 0;
                            }))))
                .then(Commands.literal("event")
                    .then(Commands.argument("event_id",ResourceLocationArgument.id())
                        .executes(ctx -> {
                            ProgressionApi.recordExternal(ctx.getSource().getPlayerOrException(),
                                ResourceLocationArgument.getId(ctx,"event_id").toString());
                            ctx.getSource().sendSuccess(() -> Component.literal("Event recorded"),false);
                            return 1;
                        })))));
    }
}
