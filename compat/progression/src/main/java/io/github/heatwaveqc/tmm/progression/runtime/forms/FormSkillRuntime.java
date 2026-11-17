package io.github.heatwaveqc.tmm.progression.runtime.forms;

import com.tyzsskills.api.TyzsSkillsAPI;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
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
    private static final String VIGOR = "tmm_werewolf_feral_vigor";
    private static final String APEX_VIGOR = "tmm_werewolf_unbreakable_beast";
    private static final String REGEN = "tmm_werewolf_regenerative_flesh";
    private static final ResourceLocation HEALTH_ID =
            ResourceLocation.fromNamespaceAndPath("tmm_progression", "jawr_werewolf_max_health");
    private static final long REGEN_AFTER_HURT_TICKS = 80;
    private static final long KILL_COOLDOWN_TICKS = 60; // 3 seconds
    private static final Map<UUID, Long> LAST_HEAL = new HashMap<>();
    private static final Map<UUID, Long> LAST_HURT = new HashMap<>();

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
        if (now >= previous && now - previous < KILL_COOLDOWN_TICKS) return;
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

    /** Re-apply conditionally on every second: no unconditional Tyz health attribute. */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        long now = player.serverLevel().getGameTime();
        if (now % 20 != 0) return;

        boolean wolf = JawrFormState.present(player, "werewolf");
        int vigor = wolf ? Math.min(5, TyzsSkillsAPI.skills().getSkillLevel(player, VIGOR)) : 0;
        int apex = wolf ? Math.min(3, TyzsSkillsAPI.skills().getSkillLevel(player, APEX_VIGOR)) : 0;
        syncHealth(player, 10d * vigor + 10d * apex);

        if (!wolf || player.isDeadOrDying()) return;
        int regenRank = Math.min(5, TyzsSkillsAPI.skills().getSkillLevel(player, REGEN));
        if (regenRank <= 0 || player.getHealth() >= player.getMaxHealth()) return;
        long last = LAST_HURT.getOrDefault(player.getUUID(), -1_000_000L);
        if (now >= last && now - last < REGEN_AFTER_HURT_TICKS) return;
        // No mana or other supernatural resource gains: HP only.
        player.heal(0.2f * regenRank); // max 1 HP/s (half a heart per second)
    }

    private static void syncHealth(ServerPlayer player, double bonus) {
        AttributeInstance instance = player.getAttribute(Attributes.MAX_HEALTH);
        if (instance == null) return;
        AttributeModifier old = instance.getModifier(HEALTH_ID);
        if (old != null && old.amount() == bonus
                && old.operation() == AttributeModifier.Operation.ADD_VALUE) return;
        if (old != null) instance.removeModifier(HEALTH_ID);
        if (bonus > 0) instance.addTransientModifier(
                new AttributeModifier(HEALTH_ID, bonus, AttributeModifier.Operation.ADD_VALUE));
        // Cure must never leave the player above the reduced maximum.
        if (player.getHealth() > player.getMaxHealth()) player.setHealth(player.getMaxHealth());
    }

    @SubscribeEvent
    public static void onDamaged(LivingDamageEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        LAST_HURT.put(player.getUUID(), player.serverLevel().getGameTime());
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID id = event.getEntity().getUUID();
        LAST_HEAL.remove(id);
        LAST_HURT.remove(id);
    }

    // Future listener requirements (not implemented):
    // 1. Immediate native JAWR cure/transform triggers (tick reconciliation is <= 1 s);
    // 2. Real JAWR bite tagging, blood gain/debit and health expenditure;
    // 3. HarvestCheck/loot-context compatibility and category tool tiers;
    // 4. LivingIncomingDamageEvent for silver, stun/bleed and dodge;
    // 5. Client break-speed synchrony, cooldown UX and dedicated-server QA.
}
