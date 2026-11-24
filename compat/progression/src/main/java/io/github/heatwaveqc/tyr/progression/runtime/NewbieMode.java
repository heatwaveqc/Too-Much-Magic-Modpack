package io.github.heatwaveqc.tyr.progression.runtime;

import net.minecraft.commands.arguments.EntityArgument;
import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.core.BaselinePaths;
import io.github.heatwaveqc.tyr.progression.state.Ledger;
import io.github.heatwaveqc.tyr.progression.state.Modes;
import io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Locale;
import java.util.Set;

/**
 * Individual, persisted Newbie -> Regular graduation. This is not a gamemode or gamerule.
 * Server is authoritative; scoreboard tag is a projection for Smart Keep Inventory predicates.
 * Initial migration preserves established players as Regular where playtime already exists.
 */
public final class NewbieMode {
    public static final String KEY = "tyr_progression:player_mode";
    public static final String NEWBIE = "newbie";
    public static final String REGULAR = "regular";
    public static final String TAG = "tmm_newbie";
    private static final Set<String> NEWBIE_ONLY_COMMANDS = Set.of(
        "home", "sethome", "delhome", "listhomes", "rtp", "back"
    );

    private NewbieMode() {}

    public static void register() {
        NeoForge.EVENT_BUS.addListener(NewbieMode::onLogin);
        NeoForge.EVENT_BUS.addListener(NewbieMode::onClone);
        NeoForge.EVENT_BUS.addListener(NewbieMode::onRespawn);
        NeoForge.EVENT_BUS.addListener(NewbieMode::onRegisterCommands);
        NeoForge.EVENT_BUS.addListener(NewbieMode::onCommand);
    }

    public static boolean isNewbie(ServerPlayer player) {
        // First-class Path ownership is authoritative after initial registration.
        // The legacy mode is only a fallback before the first login migration.
        Ledger ledger=player.getData(ProgressionAttachments.LEDGER.get());
        if (ledger.get(BaselinePaths.NEWBIE).joined()) return true;
        if (ledger.get(BaselinePaths.NORMIE).joined()
                || ledger.get(BaselinePaths.CORE).joined()) return false;
        return NEWBIE.equals(ProgressionApi.mode(player, KEY, NEWBIE));
    }

    /** Repair baseline bookkeeping without economic penalties on login/clone. */
    public static void reconcileBaselines(ServerPlayer player) {
        boolean newbie=NEWBIE.equals(ProgressionApi.mode(player,KEY,NEWBIE));
        Ledger before=player.getData(ProgressionAttachments.LEDGER.get());
        Ledger after=BaselinePaths.reconcile(before,newbie);
        if(after!=before)player.setData(ProgressionAttachments.LEDGER.get(),after);
        if(!newbie && !after.get(BaselinePaths.CORE).joined())
            ProgressionApi.join(player,BaselinePaths.CORE);
    }

    /** Integration point for the separate MXP dispatcher. Never award XP to Newbies. */
    public static boolean canEarnMxp(ServerPlayer player) {
        return !isNewbie(player);
    }

    /** Can be invoked only by a trusted server-side quest reward or an operator. */
    public static boolean graduate(ServerPlayer player) {
        if (!isNewbie(player)) return true;
        set(player, REGULAR);
        // Core enrollment is performed by reconcileBaselines in the same
        // transaction boundary as the first-class Newbie->Normie transition.
        player.sendSystemMessage(Component.literal(
            "You're now in Regular Mode. Newbie inventory and travel protection has ended."));
        return true;
    }

    public static void set(ServerPlayer player, String mode) {
        if (!NEWBIE.equals(mode) && !REGULAR.equals(mode))
            throw new IllegalArgumentException("Unrecognized player mode: " + mode);
        // One-way graduation for players; an operator restoring Newbie is a
        // real Path change with full invested/unallocated XP forfeiture.
        if (NEWBIE.equals(mode) && !isNewbie(player))
            ProgressionApi.forfeitAllPathsForNewbie(player);
        ProgressionApi.setMode(player, KEY, mode);
        reconcileBaselines(player);
        syncTag(player);
        VirtualElytra.reconcile(player,true);
    }

    private static void syncTag(ServerPlayer player) {
        if (isNewbie(player)) player.addTag(TAG);
        else player.removeTag(TAG);
    }

    private static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        Modes stored = player.getData(ProgressionAttachments.PLAYER_MODES.get());
        if (!stored.values().containsKey(KEY)) {
            // Never silently downgrade pre-existing character saves to Newbie.
            // PLAY_TIME is zero before the first tick of a genuinely new character.
            boolean existingCharacter = player.getStats()
                .getValue(Stats.CUSTOM.get(Stats.PLAY_TIME)) > 0
                || ProgressionApi.anyJoined(player);
            set(player, existingCharacter ? REGULAR : NEWBIE);
            if (!existingCharacter)
                player.sendSystemMessage(Component.literal(
                    "Welcome! You are in Newbie Mode. Complete the Introduction graduation quest when ready."));
        }
        reconcileBaselines(player);
        syncTag(player);
        VirtualElytra.reconcile(player,true);
    }

    private static void onClone(PlayerEvent.Clone event) {
        if (!(event.getEntity() instanceof ServerPlayer current)
                || !(event.getOriginal() instanceof ServerPlayer previous)) return;
        // PLAYER_MODES is copyOnDeath, but copy it explicitly for non-death clones too.
        current.setData(ProgressionAttachments.PLAYER_MODES.get(),
            previous.getData(ProgressionAttachments.PLAYER_MODES.get()));
        reconcileBaselines(current);
        syncTag(current);
    }

    private static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            reconcileBaselines(player);
            syncTag(player);
            VirtualElytra.reconcile(player,true);
        }
    }

    private static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("tmmnewbie")
            .then(Commands.literal("status").executes(ctx -> {
                ServerPlayer player = ctx.getSource().getPlayerOrException();
                ctx.getSource().sendSuccess(() -> Component.literal(
                    "Your mode: " + (isNewbie(player) ? "Newbie" : "Regular")), false);
                return 1;
            }))
            .then(Commands.literal("admin").requires(source -> source.hasPermission(2))
                .then(Commands.literal("graduate")
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(ctx -> graduate(EntityArgument.getPlayer(ctx, "player")) ? 1 : 0)))
                .then(Commands.literal("newbie")
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(ctx -> {
                            set(EntityArgument.getPlayer(ctx, "player"), NEWBIE);
                            return 1;
                        })))));
    }

    /** Backstop for Essentials commands until a permissions mod provides rank-aware nodes. */
    private static void onCommand(CommandEvent event) {
        ServerPlayer player = event.getParseResults().getContext().getSource().getPlayer();
        if (player == null || isNewbie(player) || player.hasPermissions(2)) return;
        String raw = event.getParseResults().getReader().getString().trim();
        if (raw.startsWith("/")) raw = raw.substring(1);
        if (raw.isEmpty()) return;
        String root = raw.split("\\s+", 2)[0].toLowerCase(Locale.ROOT);
        // The namespaced Brigadier alias must not bypass the regular-mode policy.
        if (root.contains(":")) root = root.substring(root.lastIndexOf(":") + 1);
        if (!NEWBIE_ONLY_COMMANDS.contains(root)) return;
        event.setCanceled(true);
        player.sendSystemMessage(Component.literal(
            "This convenience travel command is only available in Newbie Mode."));
    }
}
