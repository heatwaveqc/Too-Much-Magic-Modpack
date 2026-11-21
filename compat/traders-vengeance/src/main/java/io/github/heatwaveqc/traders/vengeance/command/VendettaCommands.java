package io.github.heatwaveqc.traders.vengeance.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.heatwaveqc.traders.vengeance.state.TraderBlacklist;
import java.util.UUID;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/** Operator-only test and recovery surface. Accepts offline player UUIDs. */
public final class VendettaCommands {
    private VendettaCommands() {}

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("tradersvengeance")
            .requires(source -> source.hasPermission(2))
            .then(Commands.literal("status")
                .then(Commands.argument("uuid", StringArgumentType.word())
                    .executes(ctx -> apply(ctx.getSource().getLevel(),
                        StringArgumentType.getString(ctx, "uuid"), "status", ctx.getSource()))))
            .then(Commands.literal("pardon")
                .then(Commands.argument("uuid", StringArgumentType.word())
                    .executes(ctx -> apply(ctx.getSource().getLevel(),
                        StringArgumentType.getString(ctx, "uuid"), "pardon", ctx.getSource()))))
            .then(Commands.literal("accuse")
                .then(Commands.argument("uuid", StringArgumentType.word())
                    .executes(ctx -> apply(ctx.getSource().getLevel(),
                        StringArgumentType.getString(ctx, "uuid"), "accuse", ctx.getSource())))));
    }

    private static int apply(ServerLevel level, String raw, String action,
                             net.minecraft.commands.CommandSourceStack source) {
        final UUID uuid;
        try {
            uuid = UUID.fromString(raw);
        } catch (IllegalArgumentException exception) {
            source.sendFailure(Component.literal("Expected a player UUID; got: " + raw));
            return 0;
        }
        TraderBlacklist list = TraderBlacklist.get(level);
        boolean result = switch (action) {
            case "pardon" -> list.pardon(uuid);
            case "accuse" -> list.accuse(uuid);
            default -> list.contains(uuid);
        };
        source.sendSuccess(() -> Component.literal(action + " " + uuid + " -> " + result), true);
        return 1;
    }
}
