package io.github.heatwaveqc.tmm.progression.core;

import io.github.heatwaveqc.tmm.progression.data.PathRegistry;
import io.github.heatwaveqc.tmm.progression.model.Definition;
import io.github.heatwaveqc.tmm.progression.state.Ledger;
import io.github.heatwaveqc.tmm.progression.state.ProgressionAttachments;
import io.github.heatwaveqc.tmm.progression.state.Track;
import net.minecraft.server.level.ServerPlayer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Stable entry points for standalone Monk/Hunter initiation, KubeJS bridges and future mods.
 * All methods must run on the logical server thread; no client-supplied XP amounts.
 */
public final class ProgressionApi {
    private ProgressionApi() {}
    private static final Map<UUID,Map<String,Long>> cooldowns = new HashMap<>();

    public static Track track(ServerPlayer player, String pathId) {
        return player.getData(ProgressionAttachments.LEDGER.get()).get(pathId);
    }

    public static boolean join(ServerPlayer player, String pathId) {
        Definition.Path path = PathRegistry.get(pathId);
        if (path == null) return false;
        Ledger ledger = player.getData(ProgressionAttachments.LEDGER.get());
        if (ledger.get(pathId).joined()) return true;
        if (!path.exclusiveGroup().isBlank()) {
            for (Definition.Path other : PathRegistry.all().values()) {
                if (!pathId.equals(other.id()) && path.exclusiveGroup().equals(other.exclusiveGroup())
                    && ledger.get(other.id()).joined()) return false;
            }
        }
        player.setData(ProgressionAttachments.LEDGER.get(),
            ledger.with(pathId,ProgressionMath.join(path,ledger.get(pathId))));
        return true;
    }

    /** Fail-closed if the path is missing, gated, full, or short on skill points. */
    public static boolean buy(ServerPlayer player, String pathId, String skillId) {
        Definition.Path path = PathRegistry.get(pathId);
        if (path == null) return false;
        Ledger ledger = player.getData(ProgressionAttachments.LEDGER.get());
        Track before = ledger.get(pathId);
        Track after = ProgressionMath.buy(path,before,skillId);
        if (after == before) return false;
        player.setData(ProgressionAttachments.LEDGER.get(),ledger.with(pathId,after));
        return true;
    }

    public static boolean respec(ServerPlayer player, String pathId) {
        Definition.Path path = PathRegistry.get(pathId);
        if (path == null || !track(player,pathId).joined()) return false;
        Ledger ledger = player.getData(ProgressionAttachments.LEDGER.get());
        player.setData(ProgressionAttachments.LEDGER.get(),
            ledger.with(pathId,ProgressionMath.refundAll(path,ledger.get(pathId))));
        return true;
    }

    /** For verified external milestones and compatible mods; cannot mint arbitrary XP. */
    public static void recordExternal(ServerPlayer player, String namespacedEventId) {
        record(player,Signal.external(namespacedEventId));
    }

    /** Standardized activity events: one authoritative signal, all joined paths. */
    public static void record(ServerPlayer player, Signal signal) {
        if (player.isCreative() || player.isSpectator()) return;
        Ledger ledger = player.getData(ProgressionAttachments.LEDGER.get());
        Ledger next = ledger;
        for (Definition.Path path : PathRegistry.all().values()) {
            Track state = next.get(path.id());
            if (!state.joined()) continue;

            int activityIndex = 0;
            for (Definition.Activity activity : path.activities()) {
                String cooldownKey = path.id() + "/" + activityIndex++;
                if (!signal.matches(activity.event(), activity.selector())) continue;
                long now = player.level().getGameTime();
                Map<String,Long> recent = cooldowns.computeIfAbsent(player.getUUID(), ignored -> new HashMap<>());
                long last = recent.getOrDefault(cooldownKey, Long.MIN_VALUE);
                // Handles fresh worlds/time resets without overflow.
                if (last != Long.MIN_VALUE && now >= last && now - last < activity.cooldownTicks())
                    continue;
                recent.put(cooldownKey,now);
                state = ProgressionMath.grantXp(path,state,activity.xp());
            }

            for (Definition.Milestone milestone : path.milestones()) {
                if (state.completed(milestone.id())) continue;
                int index = 0;
                for (Definition.Criterion criterion : milestone.criteria()) {
                    if (signal.matches(criterion.event(),criterion.selector())) {
                        String key = ProgressionMath.counterKey(milestone.id(),index);
                        if (state.criterionCount(key) < criterion.count())
                            state = ProgressionMath.recordCriterion(state,key,1);
                    }
                    index++;
                }
            }
            // A single event may satisfy a chain of previously completed prerequisite counters.
            boolean changed;
            do {
                changed = false;
                for (Definition.Milestone milestone : path.milestones()) {
                    if (state.completed(milestone.id()) || !state.milestones().containsAll(milestone.after()))
                        continue;
                    boolean ready = true;
                    for (int i = 0; i < milestone.criteria().size(); i++) {
                        if (state.criterionCount(ProgressionMath.counterKey(milestone.id(),i))
                                < milestone.criteria().get(i).count()) {
                            ready = false; break;
                        }
                    }
                    if (ready) {
                        state = ProgressionMath.grantXp(path,state.complete(milestone.id()),milestone.xp())
                            .grantPoints(milestone.points());
                        changed = true;
                    }
                }
            } while(changed);
            if (!state.equals(next.get(path.id()))) next = next.with(path.id(),state);
        }
        if (next != ledger) player.setData(ProgressionAttachments.LEDGER.get(),next);
    }

    public static void forgetTransient(UUID playerId) { cooldowns.remove(playerId); }
}
