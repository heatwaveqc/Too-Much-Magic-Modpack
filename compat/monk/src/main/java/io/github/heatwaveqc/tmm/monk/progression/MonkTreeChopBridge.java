package io.github.heatwaveqc.tmm.monk.progression;

import io.github.heatwaveqc.tmm.monk.MonkMod;
import io.github.heatwaveqc.tmm.monk.rules.MonkEligibility;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Optional HT TreeChop 0.19.3 NeoForge bridge. Register its public BeforeFell
 * and FinishChop events by class name so the Monk JAR has no hard dependency.
 * TreeChop fells are credited for logs actually removed, never per partial chop.
 */
public final class MonkTreeChopBridge {
    private static final int MAX_LOGS = 256;
    private static final Map<UUID, PendingFell> PENDING = new HashMap<>();

    private record PendingFell(ServerLevel level, BlockPos target, Map<BlockPos, BlockState> logs) { }

    private MonkTreeChopBridge() { }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void register() {
        if (!ModList.get().isLoaded("treechop")) return;
        try {
            Class<? extends Event> before = (Class<? extends Event>) Class.forName(
                    "ht.treechop.api.ChopEvent$BeforeFellEvent");
            Class<? extends Event> finish = (Class<? extends Event>) Class.forName(
                    "ht.treechop.api.ChopEvent$FinishChopEvent");
            NeoForge.EVENT_BUS.addListener((Class) before, (Consumer) (Consumer<Event>) MonkTreeChopBridge::onBefore);
            NeoForge.EVENT_BUS.addListener((Class) finish, (Consumer) (Consumer<Event>) MonkTreeChopBridge::onFinish);
            MonkMod.LOGGER.info("Optional TreeChop confirmed-log Monk mastery listeners registered");
        } catch (ClassNotFoundException | LinkageError exception) {
            MonkMod.LOGGER.warn("TreeChop API incompatible: bulk-log Monk mastery disabled; ordinary breaks still credited", exception);
        }
    }

    private static Object call(Object receiver, String method) throws ReflectiveOperationException {
        Method m = receiver.getClass().getMethod(method);
        return m.invoke(receiver);
    }

    private static boolean isLog(BlockState state) {
        return state.is(BlockTags.LOGS)
                || BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString().equals("treechop:chopped_log");
    }

    private static void onBefore(Event event) {
        try {
            if (!(call(event, "getPlayer") instanceof ServerPlayer player)
                    || !(call(event, "getLevel") instanceof ServerLevel level)
                    || !player.getMainHandItem().isEmpty()
                    || !MonkEligibility.eligibleForMonkPower(player)) return;
            Object fellData = call(event, "getFellData");
            Object tree = call(fellData, "getTree");
            @SuppressWarnings("unchecked")
            Stream<BlockPos> stream = (Stream<BlockPos>) call(tree, "streamLogs");
            Map<BlockPos, BlockState> logs = new HashMap<>();
            try (stream) {
                stream.limit(MAX_LOGS).forEach(p -> {
                    BlockPos pos = p.immutable();
                    BlockState state = level.getBlockState(pos);
                    if (isLog(state)) logs.put(pos, state);
                });
            }
            if (logs.isEmpty()) return;
            BlockPos target = ((BlockPos) call(event, "getChoppedBlockPos")).immutable();
            PENDING.put(player.getUUID(), new PendingFell(level, target, logs));
        } catch (ReflectiveOperationException | ClassCastException ex) {
            MonkMod.LOGGER.warn("Could not inspect TreeChop pre-fell data", ex);
        }
    }

    /** Called by ordinary drops logic to prevent double credit inside one fell. */
    static boolean isPendingLog(ServerPlayer player, BlockPos pos) {
        PendingFell fell = PENDING.get(player.getUUID());
        return fell != null && fell.level == player.serverLevel() && fell.logs.containsKey(pos);
    }

    private static void onFinish(Event event) {
        try {
            if (!(call(event, "getPlayer") instanceof ServerPlayer player)) return;
            PendingFell fell = PENDING.remove(player.getUUID());
            if (fell == null || fell.level != player.serverLevel()
                    || !fell.target.equals(call(event, "getChoppedBlockPos"))
                    || !Boolean.TRUE.equals(call(event, "getFelled"))) return;
            int removed = 0;
            for (var entry : fell.logs.entrySet()) {
                // A complete loss of the original log, not one chopping action.
                BlockState after = fell.level.getBlockState(entry.getKey());
                if (!isLog(after) && !after.is(entry.getValue().getBlock())) removed++;
            }
            if (removed > 0) MonkMasteryEvents.awardConfirmedFell(player, removed);
        } catch (ReflectiveOperationException ex) {
            MonkMod.LOGGER.warn("Could not finish TreeChop bulk-log credit", ex);
        }
    }

    public static void onLogout(ServerPlayer player) {
        PENDING.remove(player.getUUID());
    }
}
