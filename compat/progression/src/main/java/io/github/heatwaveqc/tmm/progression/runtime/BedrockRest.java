package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.entity.player.CanContinueSleepingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Repeatable, rank-unlocked actual sleep on a pair of bedrock blocks.
 *
 * <p>Starting sleep directly rather than via startSleepInBed is intentional:
 * ordinary bed startup sets a player's respawn point. Bedrock Rest never does.
 * The narrow CanContinueSleepingEvent override applies only to an active
 * server-owned session, and never overrides daytime or other sleep problems.</p>
 */
public final class BedrockRest {
    private BedrockRest() {}

    private record RestSession(BlockPos bedrock, ResourceKey<Level> dimension) {}
    private static final Map<UUID, RestSession> SESSIONS = new HashMap<>();

    /**
     * Structural predicate extracted for deterministic unit testing and to
     * avoid treating all of vanilla bedrock as a bed for other players.
     */
    public static boolean hasExposedPair(BlockPos clicked,
                                         Predicate<BlockPos> bedrock,
                                         Predicate<BlockPos> air) {
        if (!bedrock.test(clicked) || !air.test(clicked.above())
                || !air.test(clicked.above(2))) return false;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos other = clicked.relative(direction);
            if (bedrock.test(other) && air.test(other.above())
                    && air.test(other.above(2))) return true;
        }
        return false;
    }

    /** Match the original stage-17 pair of horizontally adjacent bedrock. */
    public static boolean validBedrock(ServerLevel world, BlockPos clicked) {
        return hasExposedPair(clicked,
                pos -> world.hasChunkAt(pos) && world.getBlockState(pos).is(Blocks.BEDROCK),
                pos -> world.hasChunkAt(pos) && world.getBlockState(pos).isAir());
    }

    private static boolean canStart(ServerPlayer player, BlockPos clicked) {
        ServerLevel world = player.serverLevel();
        if (!ClassAbilities.unlocked(player, "bedrock_rest")
                || !player.isAlive() || player.isSleeping()
                || player.isPassenger() || player.isSpectator()
                || !player.getMainHandItem().isEmpty()
                || !world.dimensionType().bedWorks()
                || (world.isDay() && !world.isThundering())
                || !world.mayInteract(player, clicked)
                || player.distanceToSqr(clicked.getX() + 0.5,
                        clicked.getY() + 1.0, clicked.getZ() + 0.5) > 25.0
                || !validBedrock(world, clicked)) return false;

        // Conservative approximation to the vanilla nearby-monster check.
        // Do not let monks bypass dangers that would deny an ordinary bed.
        return world.getEntitiesOfClass(Monster.class,
                new AABB(clicked).inflate(8.0, 5.0, 8.0), Monster::isAlive).isEmpty();
    }

    /** Called by NeoForge on the server after vanilla claim/event cancellation. */
    public static void interact(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCanceled() || event.getHand() != InteractionHand.MAIN_HAND
                || !(event.getEntity() instanceof ServerPlayer player)) return;
        BlockPos clicked = event.getPos();
        if (!player.serverLevel().getBlockState(clicked).is(Blocks.BEDROCK)
                || !ClassAbilities.unlocked(player, "bedrock_rest")) return;
        if (!canStart(player, clicked)) {
            player.displayClientMessage(Component.literal(
                    "Bedrock Rest needs two exposed bedrock blocks, nighttime, and a safe area."), true);
            return;
        }

        BlockPos sleepingAt = clicked.above();
        SESSIONS.put(player.getUUID(),
                new RestSession(clicked.immutable(), player.serverLevel().dimension()));
        // This is genuine vanilla sleeping pose and sleep-vote participation.
        // Do NOT call startSleepInBed: it changes the player's respawn point.
        player.startSleeping(sleepingAt);
        player.serverLevel().updateSleepingPlayerList();
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    /** Let bedrock count as the bed only for this exact active rest session. */
    public static void continueSleeping(CanContinueSleepingEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        RestSession session = SESSIONS.get(player.getUUID());
        if (session == null || !session.dimension().equals(player.serverLevel().dimension()))
            return;
        boolean correctPosition = player.getSleepingPos()
                .map(pos -> pos.equals(session.bedrock().above())).orElse(false);
        if (!correctPosition || !validBedrock(player.serverLevel(), session.bedrock())) {
            SESSIONS.remove(player.getUUID());
            return;
        }
        // Do not override daytime, mod-imposed restrictions, or safety failures.
        if (event.getProblem() == Player.BedSleepingProblem.NOT_POSSIBLE_HERE)
            event.setContinueSleeping(true);
    }

    /** Reward the original sleep-on-bedrock deed once, without XP per nap. */
    public static void wake(PlayerWakeUpEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        RestSession previous = SESSIONS.remove(player.getUUID());
        if (previous == null) return;
        if (previous.dimension().equals(player.serverLevel().dimension())
                && player.getSleepingPos().map(p -> p.equals(previous.bedrock().above()))
                        .orElse(false)
                && player.isSleepingLongEnough())
            ProgressionApi.recordExternal(player, "tmm_progression:bedrock_rest_completed");
    }

    public static void forget(ServerPlayer player) {
        SESSIONS.remove(player.getUUID());
    }
}
