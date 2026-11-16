package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.ProgressionMod;
import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.ModList;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Optional HT's TreeChop 0.19.3 NeoForge event adapter.
 *
 * TreeChop uses Block.dropResources directly for indirect felled logs, which
 * need not issue the player BlockDropsEvent credited by normal Monk training.
 * The before-fell event supplies actual positions; the finish-chop event runs
 * after mutations. Award each *removed* log, never each "chop" animation or
 * hypothetical tree-detection entry. No TreeChop hard dependency.
 *
 * Compatible with the verified NeoForge ChopEvent API, loaded by reflection
 * ONLY when the actual mod ID "treechop" is present.
 */
public final class TreeChopXpBridge {
    private TreeChopXpBridge() {}

    private record OriginalLog(BlockPos pos, BlockState state) {}
    private record Pending(BlockPos clicked, String dimension, long tick, List<OriginalLog> logs) {}
    private static final Map<UUID, Pending> PENDING = new HashMap<>();

    public static void register() {
        if (!ModList.get().isLoaded("treechop")) return;
        try {
            Class<?> before = Class.forName("ht.treechop.api.ChopEvent$BeforeFellEvent");
            Class<?> finish = Class.forName("ht.treechop.api.ChopEvent$FinishChopEvent");
            if (!Event.class.isAssignableFrom(before) || !Event.class.isAssignableFrom(finish))
                throw new IllegalStateException("Unexpected TreeChop event type");
            subscribe(before, TreeChopXpBridge::beforeFell);
            subscribe(finish, TreeChopXpBridge::finishChop);
            ProgressionMod.LOGGER.info("Installed optional TreeChop per-felled-log Monk XP adapter");
        } catch (ReflectiveOperationException | LinkageError failure) {
            ProgressionMod.LOGGER.error("TreeChop is installed but Monk per-log XP adapter could not register", failure);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void subscribe(Class<?> type, Consumer<Event> consumer) {
        NeoForge.EVENT_BUS.addListener((Class) type, (Consumer) consumer);
    }

    private static Object invoke(Object target, String name) throws ReflectiveOperationException {
        Method method = target.getClass().getMethod(name);
        return method.invoke(target);
    }

    private static void beforeFell(Event event) {
        try {
            if (!(invoke(event, "getPlayer") instanceof ServerPlayer player)
                    || !(invoke(event, "getLevel") instanceof ServerLevel level)
                    || !player.getMainHandItem().isEmpty()
                    || player.isCreative() || player.isSpectator()
                    || !ProgressionApi.track(player, "tmm_progression:monk").joined()) return;
            Object tree = invoke(invoke(event, "getFellData"), "getTree");
            if (!(invoke(tree, "streamLogs") instanceof Stream<?> stream)) return;
            List<OriginalLog> logs = new ArrayList<>();
            try (stream) {
                stream.limit(8192).forEach(candidate -> {
                    if (!(candidate instanceof BlockPos pos) || !level.hasChunkAt(pos)) return;
                    BlockState state = level.getBlockState(pos);
                    // TreeChop replaces partially chopped trunks with its
                    // own block entity; recover the underlying log material.
                    if (!state.is(BlockTags.LOGS)
                            && net.minecraft.core.registries.BuiltInRegistries.BLOCK
                            .getKey(state.getBlock()).toString().equals("treechop:chopped_log")) {
                        var entity = level.getBlockEntity(pos);
                        if (entity != null) {
                            try {
                                Object original = invoke(entity, "getOriginalState");
                                if (original instanceof BlockState originalState) state = originalState;
                            } catch (ReflectiveOperationException ignored) {
                                // Do not fabricate log XP for missing source state.
                            }
                        }
                    }
                    if (state.is(BlockTags.LOGS)) logs.add(new OriginalLog(pos.immutable(), state));
                });
            }
            if (!logs.isEmpty()) {
                PENDING.put(player.getUUID(), new Pending(
                        ((BlockPos) invoke(event, "getChoppedBlockPos")).immutable(),
                        level.dimension().location().toString(),
                        level.getGameTime(), List.copyOf(logs)));
            }
        } catch (ReflectiveOperationException | RuntimeException failure) {
            ProgressionMod.LOGGER.warn("Unable to capture TreeChop pre-fell log positions", failure);
        }
    }

    private static void finishChop(Event event) {
        try {
            if (!(invoke(event, "getPlayer") instanceof ServerPlayer player)
                    || !(invoke(event, "getLevel") instanceof ServerLevel level)) return;
            Pending pending = PENDING.remove(player.getUUID());
            if (pending == null || !Boolean.TRUE.equals(invoke(event, "getFelled"))
                    || player.isCreative() || player.isSpectator()
                    || !player.getMainHandItem().isEmpty()
                    || !pending.clicked().equals(invoke(event, "getChoppedBlockPos"))
                    || !pending.dimension().equals(level.dimension().location().toString())
                    || level.getGameTime() - pending.tick() > 2) return;

            int credited = 0;
            for (OriginalLog original : pending.logs()) {
                BlockPos pos = original.pos();
                if (!level.hasChunkAt(pos)) continue;
                BlockState result = level.getBlockState(pos);
                // Only logs actually removed to air/fluid; not merely damaged
                // "chopped_log", protected logs, or leaves.
                if (!(result.isAir() || !result.getFluidState().isEmpty())) continue;
                if (!RecentBlockCredits.first(player.getUUID(), pending.dimension(),
                        pos.asLong(), level.getGameTime())) continue;
                ProgressionApi.record(player, Selectors.blockBreak(original.state()));
                ProgressionApi.record(player, Selectors.barehandBlockBreak(original.state()));
                credited++;
            }
            if (credited > 0) ProgressionMod.LOGGER.debug(
                    "Credited {} verified TreeChop logs to Monk {}", credited, player.getScoreboardName());
        } catch (ReflectiveOperationException | RuntimeException failure) {
            ProgressionMod.LOGGER.warn("TreeChop felling detected but Monk XP credit failed", failure);
        }
    }

    public static void forget(ServerPlayer player) {
        PENDING.remove(player.getUUID());
    }
}
