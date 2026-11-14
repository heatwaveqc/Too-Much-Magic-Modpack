package io.github.heatwaveqc.tmm.monk.gameplay;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.factions.IFactionPlayerHandler;
import de.teamlapen.vampirism.api.event.PlayerFactionEvent;
import io.github.heatwaveqc.tmm.monk.MonkMod;
import io.github.heatwaveqc.tmm.monk.faction.MonkFaction;
import io.github.heatwaveqc.tmm.monk.rules.MonkEligibility;
import io.github.heatwaveqc.tmm.monk.rules.SunsetTrialRules;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * First Sunset: uninterrupted western-sky meditation in the Overworld.
 *
 * No client packets are trusted. The temporary transition permit prevents
 * other mods/commands from bypassing the initial faction rite. Once a player
 * belongs to the Monk faction, Vampirism owns the saved membership.
 */
public final class SunsetInitiation {
    private static final Map<UUID, Integer> PROGRESS = new HashMap<>();
    private static final Set<UUID> APPROVED_TRANSITIONS = new HashSet<>();
    private static final Vec3 WEST = new Vec3(-1, 0, 0);

    private SunsetInitiation() { }

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        UUID id = player.getUUID();
        if (!mayContinue(player)) {
            PROGRESS.remove(id);
            return;
        }

        int ticks = SunsetTrialRules.consecutiveTicks(PROGRESS.getOrDefault(id, 0), true);
        if (ticks < SunsetTrialRules.REQUIRED_TICKS) {
            PROGRESS.put(id, ticks);
            if (ticks % 40 == 0) {
                player.displayClientMessage(Component.translatable(
                        "message.tmm_monk.sunset_progress", ticks / 20, SunsetTrialRules.REQUIRED_TICKS / 20), true);
            }
            return;
        }

        PROGRESS.remove(id);
        IFactionPlayerHandler factions = VampirismAPI.factionPlayerHandler(player);
        APPROVED_TRANSITIONS.add(id);
        try {
            // CanJoin is rechecked immediately before the authoritative write.
            if (MonkEligibility.canBeginRite(player)) {
                factions.joinFaction(MonkFaction.FACTION);
            }
        } finally {
            APPROVED_TRANSITIONS.remove(id);
        }

        if (factions.isInFaction(MonkFaction.FACTION) && factions.getCurrentLevel() > 0) {
            player.sendSystemMessage(Component.translatable("message.tmm_monk.sunset_complete"));
            MonkMod.LOGGER.info("First Sunset completed for an eligible player; joined Monk faction");
        } else {
            player.sendSystemMessage(Component.translatable("message.tmm_monk.sunset_interrupted"));
        }
    }

    private static boolean mayContinue(ServerPlayer player) {
        if (!MonkEligibility.canBeginRite(player)) return false;
        if (!(player.level() instanceof ServerLevel level) || level.dimension() != Level.OVERWORLD) return false;
        if (!SunsetTrialRules.duringSunset(level.getDayTime())) return false;
        if (level.isRainingAt(player.blockPosition())) return false;
        Vec3 gaze = player.getLookAngle();
        if (!SunsetTrialRules.facingWesternSun(gaze.x, gaze.y, gaze.z)) return false;
        Vec3 eye = player.getEyePosition();
        // A sky check disallows roofs; the horizontal block ray rejects a
        // local wall/mountain directly between the player and the western sky.
        if (!level.canSeeSky(BlockPos.containing(eye))) return false;
        return level.clip(new ClipContext(eye, eye.add(WEST.scale(48)),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getType() == HitResult.Type.MISS;
    }

    public static void onFactionChangePre(PlayerFactionEvent.FactionLevelChangePre event) {
        if (MonkFaction.FACTION == null || event.getNewFaction() != MonkFaction.FACTION
                || event.getNewLevel() <= 0 || event.getCurrentFaction() == MonkFaction.FACTION) return;
        if (!(event.getPlayer().asEntity() instanceof ServerPlayer player)
                || !APPROVED_TRANSITIONS.contains(player.getUUID())
                || !MonkEligibility.canBeginRite(player)) {
            event.setCanceled(true);
        }
    }

    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PROGRESS.remove(player.getUUID());
            APPROVED_TRANSITIONS.remove(player.getUUID());
        }
    }

    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PROGRESS.remove(player.getUUID());
        }
    }
}
