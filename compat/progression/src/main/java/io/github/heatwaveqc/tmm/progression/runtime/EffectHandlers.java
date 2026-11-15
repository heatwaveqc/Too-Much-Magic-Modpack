package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.model.Definition;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Mod-to-mod extension seam. A custom effect handler is written ONCE; a datapack can
 * then instantiate it on any path/skill with rank, conditions and string parameters.
 * This is trusted Java registration, never script execution from player/config JSON.
 */
public final class EffectHandlers {
    private EffectHandlers() {}
    private static final Set<String> BUILTIN = Set.of(
        "attribute","melee_bonus","damage_bonus","damage_multiplier","break_speed",
        "harvest_tier","gear_durability","animal_friendship",
        "blink","catch_arrows","armor_lock","reserved_fist","water_breathing",
        "purification","faction_neutrality","creeper_peace","treasure_hand",
        "glass_touch","hunger_discipline","monk_leap","withering_stare",
        "water_walk","momentum_glide","blindness_ordeal","lava_protection",
        "bedrock_rest");
    private static final Map<String,Handler> CUSTOM = new HashMap<>();

    public interface Handler {
        default void tick(ServerPlayer player, Definition.Effect effect, int rank) {}
        default void incoming(ServerPlayer owner, LivingIncomingDamageEvent event,
                              Definition.Effect effect, int rank) {}
        default void harvest(ServerPlayer player, PlayerEvent.HarvestCheck event,
                             Definition.Effect effect, int rank) {}
        default void breakSpeed(ServerPlayer player, PlayerEvent.BreakSpeed event,
                                Definition.Effect effect, int rank) {}
        default void blockBreak(ServerPlayer player, BlockEvent.BreakEvent event,
                                Definition.Effect effect, int rank) {}
        default void kill(ServerPlayer player, LivingDeathEvent event,
                          Definition.Effect effect, int rank) {}
        default void foodConsumed(ServerPlayer player, ItemStack food,
                                  Definition.Effect effect, int rank) {}
        default void entityInteract(ServerPlayer player, PlayerInteractEvent.EntityInteract event,
                                    Definition.Effect effect, int rank) {}
        default void blockDrops(ServerPlayer player, BlockDropsEvent event,
                                Definition.Effect effect, int rank) {}
        default void projectileImpact(ServerPlayer player, ProjectileImpactEvent event,
                                      Definition.Effect effect, int rank) {}
        default void equipmentChange(ServerPlayer player, LivingEquipmentChangeEvent event,
                                     Definition.Effect effect, int rank) {}
        default void targetChange(ServerPlayer player, LivingChangeTargetEvent event,
                                  Definition.Effect effect, int rank) {}
        default void jump(ServerPlayer player, LivingEvent.LivingJumpEvent event,
                          Definition.Effect effect, int rank) {}
        default void fall(ServerPlayer player, LivingFallEvent event,
                          Definition.Effect effect, int rank) {}
    }

    public static void register(String id, Handler handler) {
        if (!id.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
            throw new IllegalArgumentException("Custom effect requires namespaced type");
        if (CUSTOM.putIfAbsent(id,handler) != null || BUILTIN.contains(id))
            throw new IllegalArgumentException("Duplicate effect type " + id);
    }
    public static boolean known(String id) { return BUILTIN.contains(id) || CUSTOM.containsKey(id); }

    private static void dispatch(ServerPlayer player, Invoker call) {
        for (EffectRuntime.Active active : EffectRuntime.active(player)) {
            Handler handler = CUSTOM.get(active.effect().type());
            if (handler != null) call.call(handler,active.effect(),active.rank());
        }
    }
    @FunctionalInterface private interface Invoker {
        void call(Handler handler, Definition.Effect effect, int rank);
    }

    public static void tick(ServerPlayer player) {
        dispatch(player,(h,e,r) -> h.tick(player,e,r));
    }
    public static void incoming(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof ServerPlayer victim)
            dispatch(victim,(h,e,r) -> h.incoming(victim,event,e,r));
        if (event.getSource().getEntity() instanceof ServerPlayer attacker
                && attacker != event.getEntity())
            dispatch(attacker,(h,e,r) -> h.incoming(attacker,event,e,r));
    }
    public static void harvest(ServerPlayer player, PlayerEvent.HarvestCheck event) {
        dispatch(player,(h,e,r) -> h.harvest(player,event,e,r));
    }
    public static void breakSpeed(ServerPlayer player, PlayerEvent.BreakSpeed event) {
        dispatch(player,(h,e,r) -> h.breakSpeed(player,event,e,r));
    }
    public static void blockBreak(ServerPlayer player, BlockEvent.BreakEvent event) {
        dispatch(player,(h,e,r) -> h.blockBreak(player,event,e,r));
    }
    public static void kill(ServerPlayer player, LivingDeathEvent event) {
        dispatch(player,(h,e,r) -> h.kill(player,event,e,r));
    }
    public static void foodConsumed(ServerPlayer player, ItemStack item) {
        dispatch(player,(h,e,r) -> h.foodConsumed(player,item,e,r));
    }
    public static void entityInteract(ServerPlayer player, PlayerInteractEvent.EntityInteract event) {
        dispatch(player,(h,e,r) -> h.entityInteract(player,event,e,r));
    }
    public static void blockDrops(ServerPlayer player, BlockDropsEvent event) {
        dispatch(player,(h,e,r) -> h.blockDrops(player,event,e,r));
    }
    public static void projectileImpact(ServerPlayer player, ProjectileImpactEvent event) {
        dispatch(player,(h,e,r) -> h.projectileImpact(player,event,e,r));
    }
    public static void equipmentChange(ServerPlayer player, LivingEquipmentChangeEvent event) {
        dispatch(player,(h,e,r) -> h.equipmentChange(player,event,e,r));
    }
    public static void targetChange(ServerPlayer player, LivingChangeTargetEvent event) {
        dispatch(player,(h,e,r) -> h.targetChange(player,event,e,r));
    }
    public static void jump(ServerPlayer player, LivingEvent.LivingJumpEvent event) {
        dispatch(player,(h,e,r) -> h.jump(player,event,e,r));
    }
    public static void fall(ServerPlayer player, LivingFallEvent event) {
        dispatch(player,(h,e,r) -> h.fall(player,event,e,r));
    }
}
