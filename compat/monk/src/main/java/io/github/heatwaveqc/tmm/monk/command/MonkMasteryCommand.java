package io.github.heatwaveqc.tmm.monk.command;

import io.github.heatwaveqc.tmm.monk.faction.MonkFaction;
import io.github.heatwaveqc.tmm.monk.faction.MonkPlayer;
import io.github.heatwaveqc.tmm.monk.progression.MonkMasteryRules;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

/** Operator read-only inspection for early cultivation smoke tests. */
public final class MonkMasteryCommand {
    private MonkMasteryCommand() { }

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("tmmmonk")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("mastery").executes(ctx -> show(ctx.getSource().getPlayerOrException()))));
    }

    private static int show(ServerPlayer player) throws CommandSyntaxException {
        if (MonkFaction.FACTION == null) {
            player.sendSystemMessage(Component.literal("Monk faction has not been registered."));
            return 0;
        }
        var optional = MonkFaction.FACTION.getPlayerCapability(player)
                .filter(MonkPlayer.class::isInstance).map(MonkPlayer.class::cast);
        if (optional.isEmpty()) {
            player.sendSystemMessage(Component.literal("No Monk player attachment on this character."));
            return 0;
        }
        MonkPlayer state = optional.get();
        int level = state.getLevel();
        player.sendSystemMessage(Component.literal("Monk rank " + level
                + ", native mastery " + state.getMasteryXp() + "/"
                + MonkMasteryRules.totalXpForLevel(MonkMasteryRules.HIGHEST_PILOT_LEVEL)
                + ", distinct foods " + state.getFoodDiscoveryCount()
                + ", unspent native skill points " + state.getSkillHandler().getLeftSkillPoints()));
        return state.getMasteryXp();
    }
}
