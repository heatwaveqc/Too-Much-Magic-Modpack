package io.github.heatwaveqc.tmm.monk.progression;

import io.github.heatwaveqc.tmm.monk.rules.MonkEligibility;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Fallback for completed player-controlled magic-focus breaks without drops.
 * Attempts are NEVER credited: on a later server tick the target must be air.
 * Ordinary tools, synthetic/fake players and nearby unrelated block changes
 * do not qualify as a source. Spell APIs bypassing player break/drop events
 * still need dedicated source-specific event hooks.
 */
public final class MonkMagicBreakTracker {
    private record Attempt(ServerLevel level, BlockPos pos, BlockState before, long time) { }
    private static final Map<UUID, Map<BlockPos, Attempt>> PENDING = new HashMap<>();
    private static final int MAX_PENDING_PER_PLAYER = 128;

    private MonkMagicBreakTracker() { }

    public static void onBreakAttempt(BlockEvent.BreakEvent event) {
        if (event.isCanceled()
                || !(event.getPlayer() instanceof ServerPlayer player)
                || !MonkEligibility.eligibleForMonkPower(player)
                || !MonkMasteryEvents.isMagicImplement(player.getMainHandItem())
                || event.getState().isAir()) return;
        Map<BlockPos, Attempt> attempts = PENDING.computeIfAbsent(player.getUUID(), ignored -> new HashMap<>());
        if (attempts.size() >= MAX_PENDING_PER_PLAYER) attempts.clear();
        BlockPos pos = event.getPos().immutable();
        attempts.put(pos, new Attempt(player.serverLevel(), pos, event.getState(), player.level().getGameTime()));
    }

    /** Drop-hook credit owns the break if it ran first, preventing two awards. */
    public static void onDropsCredited(ServerPlayer player, BlockPos pos) {
        Map<BlockPos, Attempt> attempts = PENDING.get(player.getUUID());
        if (attempts != null) attempts.remove(pos);
    }

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        Map<BlockPos, Attempt> attempts = PENDING.get(player.getUUID());
        if (attempts == null || attempts.isEmpty()) return;
        long now = player.level().getGameTime();
        Iterator<Attempt> iter = attempts.values().iterator();
        while (iter.hasNext()) {
            Attempt attempt = iter.next();
            if (attempt.level != player.serverLevel() || now > attempt.time + 3) {
                iter.remove();
                continue;
            }
            if (now <= attempt.time) continue;
            BlockState after = player.serverLevel().getBlockState(attempt.pos);
            if (after.isAir() && !attempt.before.isAir()) {
                iter.remove();
                MonkMasteryEvents.awardConfirmedMagicBreak(player);
            }
        }
    }

    public static void onLogout(ServerPlayer player) {
        PENDING.remove(player.getUUID());
    }
}
