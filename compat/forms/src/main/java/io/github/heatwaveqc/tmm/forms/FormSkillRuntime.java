package io.github.heatwaveqc.tmm.forms;

import com.tyzsskills.api.TyzsSkillsAPI;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Enemy;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Experimental source handlers. They are intentionally inert until the
 * corresponding SkillPrefabs and overrides are separately activated.
 * These are TWO pilots, not a claim all 37 planned skills are implemented.
 */
public final class FormSkillRuntime {
    private FormSkillRuntime() {}
    private static final String KILL_HEAL = "tmm_werewolf_bloodlust_recovery";
    private static final String DIG = "tmm_werewolf_feral_excavation";
    private static final long KILL_COOLDOWN_TICKS = 60; // 3 seconds
    private static final Map<UUID, Long> LAST_HEAL = new HashMap<>();

    /** Heal only on the event's actual direct player kill of a hostile mob. */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onKill(LivingDeathEvent event) {
        if (event.isCanceled()) return;
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
        if (!(event.getEntity() instanceof Enemy)) return;
        if (!JawrFormState.present(player, "werewolf")) return;

        int lvl = TyzsSkillsAPI.skills().getSkillLevel(player, KILL_HEAL);
        if (lvl < 1) return;
        long now = player.serverLevel().getGameTime();
        UUID playerId = player.getUUID();
        long previous = LAST_HEAL.getOrDefault(playerId, -1000000L);
        if (now < previous || now - previous < KILL_COOLDOWN_TICKS) return;
        LAST_HEAL.put(playerId, now);
        // Finite 2/4/6/8/10 HP (1–5 hearts), NOT target max HP:
        // an endgame boss may have thousands of hit points.
        player.heal(Math.min(10.0f, lvl * 2.0f));
    }

    /**
     * Barehand mining SPEED only. This does not grant harvest tier or drops.
     * Real claw tool-tier and loot integration requires an independent
     * HarvestCheck + loot-context / correct-tool adapter and claim tests.
     *
     * Client break-speed feedback/sync has NOT been implemented here.
     */
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!player.getMainHandItem().isEmpty()) return;
        if (!JawrFormState.present(player, "werewolf")) return;
        int lvl = TyzsSkillsAPI.skills().getSkillLevel(player, DIG);
        if (lvl < 1) return;
        float original = event.getOriginalSpeed();
        if (original <= 0f) return;
        event.setNewSpeed(original * (1.0f + 0.2f * Math.min(5, lvl)));
    }

    // Future listener requirements (not implemented):
    // 1. Cure/relog/dimension event for dynamic health/armor/speed modifiers;
    // 2. Real JAWR bite tagging, blood gain/debit and health expenditure;
    // 3. HarvestCheck/loot-context compatibility and category tool tiers;
    // 4. LivingIncomingDamageEvent for silver, stun/bleed and dodge;
    // 5. Per-player cooldown persistence and explicit client notifications.
}
