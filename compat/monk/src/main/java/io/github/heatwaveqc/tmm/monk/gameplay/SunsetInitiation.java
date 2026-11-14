package io.github.heatwaveqc.tmm.monk.gameplay;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.factions.IFactionPlayerHandler;
import de.teamlapen.vampirism.api.event.PlayerFactionEvent;
import io.github.heatwaveqc.tmm.monk.MonkMod;
import io.github.heatwaveqc.tmm.monk.faction.MonkFaction;
import io.github.heatwaveqc.tmm.monk.rules.MonkEligibility;
import io.github.heatwaveqc.tmm.monk.rules.SunsetTrialRules;
import io.github.heatwaveqc.tmm.monk.skills.MonkSkills;
import net.minecraft.world.entity.ai.attributes.Attributes;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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


    /** Operator QA: explain each independent requirement, without changing state. */
    public static List<String> diagnostics(ServerPlayer player) {
        List<String> out = new ArrayList<>();
        UUID id = player.getUUID();
        IFactionPlayerHandler factions = VampirismAPI.factionPlayerHandler(player);
        if (MonkFaction.FACTION == null) {
            out.add("[Monk] ERROR: faction tmm_monk:monk is not registered");
            return out;
        }
        boolean neutral = factions.getCurrentFaction() == null;
        String current = neutral ? "none" : factions.getCurrentFaction().getID().toString();
        boolean gear = MonkEligibility.equipmentAllowsDiscipline(player);
        boolean mode = !player.isCreative() && !player.isSpectator();
        boolean canJoin = neutral && factions.canJoin(MonkFaction.FACTION);
        boolean world = player.level().dimension() == Level.OVERWORLD;
        ServerLevel level = (ServerLevel) player.level();
        long clock = Math.floorMod(level.getDayTime(), 24000L);
        boolean time = SunsetTrialRules.duringSunset(level.getDayTime());
        boolean raining = level.isRainingAt(player.blockPosition());
        Vec3 look = player.getLookAngle();
        boolean gaze = SunsetTrialRules.facingWesternSun(look.x, look.y, look.z);
        Vec3 eye = player.getEyePosition();
        boolean sky = level.canSeeSky(BlockPos.containing(eye));
        boolean ray = level.clip(new ClipContext(eye, eye.add(WEST.scale(48)),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getType() == HitResult.Type.MISS;
        boolean root = MonkFaction.FACTION.getPlayerCapability(player)
                .map(p -> p.getLevel() > 0 && p.getSkillHandler().isSkillEnabled(MonkSkills.ROOT.get()))
                .orElse(false);
        double step = player.getAttribute(Attributes.STEP_HEIGHT) == null
                ? Double.NaN : player.getAttribute(Attributes.STEP_HEIGHT).getValue();
        out.add("[Monk] Registered=YES; current faction=" + current
                + ", faction level=" + factions.getCurrentLevel()
                + ", root enabled=" + root
                + ", step height=" + String.format(Locale.ROOT, "%.2f", step));
        out.add("[Monk] Can start: survival=" + mode + ", neutral+canJoin=" + canJoin
                + ", no conventional gear=" + gear);
        out.add("[Monk] Sunset window: overworld=" + world + ", day clock=" + clock
                + " (requires 11500..12500), in window=" + time + ", clear weather=" + !raining);
        out.add("[Monk] Western gaze: " + gaze + " (within 8 degrees of -X),"
                + " can see sky=" + sky + ", west 48-block ray unobstructed=" + ray);
        int ticks = PROGRESS.getOrDefault(id, 0);
        out.add("[Monk] Consecutive progress=" + ticks + "/200 ticks; all conditions now="
                + (MonkEligibility.canBeginRite(player) && world && time && !raining && gaze && sky && ray));
        out.add("[Monk] No animated graphic exists yet. Actionbar starts after 40 valid ticks."
                + " For independent faction/tree testing, use /tmmmonk testjoin (operator, disposable world).");
        return out;
    }

    /** Explicit operator QA bypass for the sky/time test only. */
    public static boolean operatorTestJoin(ServerPlayer player) {
        if (!MonkEligibility.canBeginRite(player)) {
            return false;
        }
        UUID id = player.getUUID();
        IFactionPlayerHandler factions = VampirismAPI.factionPlayerHandler(player);
        APPROVED_TRANSITIONS.add(id);
        try {
            factions.joinFaction(MonkFaction.FACTION);
        } finally {
            APPROVED_TRANSITIONS.remove(id);
            PROGRESS.remove(id);
        }
        return factions.isInFaction(MonkFaction.FACTION) && factions.getCurrentLevel() > 0;
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

    public static void onClone(PlayerEvent.Clone event) {
        // A death/respawn is not a continuation of the meditation.
        PROGRESS.remove(event.getEntity().getUUID());
        APPROVED_TRANSITIONS.remove(event.getEntity().getUUID());
    }

    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PROGRESS.remove(player.getUUID());
        }
    }
}
