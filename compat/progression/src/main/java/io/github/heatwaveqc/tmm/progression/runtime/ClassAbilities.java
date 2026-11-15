package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Reusable, server-owned handlers: class powers are enabled only by active JSON effect ranks. */
public final class ClassAbilities {
    private ClassAbilities() {}
    private static final String ARMOR_MODE = "tmm_progression:armor_lock";
    private static final String TREASURE_MODE = "tmm_progression:treasure_mode";
    private static final Map<UUID,Retaliation> RETALIATION = new HashMap<>();
    private record Retaliation(UUID attacker, long until) {}

    private static EffectRuntime.Active effect(ServerPlayer p, String type) {
        for (var a : EffectRuntime.active(p))
            if (a.effect().type().equals(type)) return a;
        return null;
    }
    public static boolean unlocked(ServerPlayer p, String type) { return effect(p,type) != null; }

    public static boolean toggle(ServerPlayer p, String what) {
        if ("armor".equals(what) && unlocked(p,"armor_lock")) {
            String next = ProgressionApi.mode(p,ARMOR_MODE,"on").equals("on") ? "off" : "on";
            return ProgressionApi.setMode(p,ARMOR_MODE,next);
        }
        if ("treasure".equals(what) && unlocked(p,"treasure_hand")) {
            String next = ProgressionApi.mode(p,TREASURE_MODE,"fortune").equals("fortune") ? "silk" : "fortune";
            return ProgressionApi.setMode(p,TREASURE_MODE,next);
        }
        return false;
    }
    public static String mode(ServerPlayer p,String type) {
        return "armor".equals(type) ? ProgressionApi.mode(p,ARMOR_MODE,"on") :
            ProgressionApi.mode(p,TREASURE_MODE,"fortune");
    }

    /** No item deletion, including full inventories or creative/admin inserted equipment. */
    private static int emptyInventorySlot(ServerPlayer p) {
        for (int i = 1; i < 36; i++)
            if (p.getInventory().getItem(i).isEmpty()) return i;
        return -1;
    }
    public static void tick(ServerPlayer p) {
        if (unlocked(p,"reserved_fist") && !p.getInventory().getItem(0).isEmpty()) {
            int free = emptyInventorySlot(p);
            if (free >= 0) {
                ItemStack old = p.getInventory().getItem(0);
                p.getInventory().setItem(0,ItemStack.EMPTY);
                p.getInventory().setItem(free,old);
                p.getInventory().setChanged();
            } else if (p.tickCount % 100 == 0) {
                p.displayClientMessage(Component.literal("Reserved fist: clear an inventory slot to move the item safely"),true);
            }
        }
        if (unlocked(p,"armor_lock") && ProgressionApi.mode(p,ARMOR_MODE,"on").equals("on")) {
            for (EquipmentSlot slot : List.of(EquipmentSlot.HEAD,EquipmentSlot.CHEST,
                    EquipmentSlot.LEGS,EquipmentSlot.FEET)) {
                ItemStack worn = p.getItemBySlot(slot);
                if (worn.isEmpty()) continue;
                boolean bound = EnchantmentHelper.has(worn,EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE);
                if (bound) {
                    // Must remain equipped. Ordinary wear triggers Unbreaking/break handlers,
                    // and any Life Mending repair still runs under its own mechanics.
                    if (worn.isDamageableItem() && p.tickCount % 20 == 0)
                        worn.hurtAndBreak(1,p,slot);
                    continue;
                }
                int free = emptyInventorySlot(p);
                if (free >= 0) {
                    p.setItemSlot(slot,ItemStack.EMPTY);
                    p.getInventory().setItem(free,worn);
                    p.getInventory().setChanged();
                } else if (p.tickCount % 100 == 0) {
                    p.displayClientMessage(Component.literal("Armor Lock: inventory full; armor cannot be moved safely"),true);
                }
            }
        }

        if (unlocked(p,"water_breathing") && p.isUnderWater()) {
            int level = effect(p,"water_breathing").rank();
            if (level >= 2) p.setAirSupply(p.getMaxAirSupply());
            else if (p.tickCount % 5 == 0)
                p.setAirSupply(Math.min(p.getMaxAirSupply(),p.getAirSupply()+2));
        }
        if (unlocked(p,"purification") && p.tickCount % 10 == 0) {
            for (MobEffectInstance instance : new ArrayList<>(p.getActiveEffects()))
                if (instance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL)
                    p.removeEffect(instance.getEffect());
        }
    }

    /** Server-authoritative aimed movement; does not load chunks or teleport through solid blocks. */
    public static boolean blink(ServerPlayer p) {
        var active = effect(p,"blink");
        if (active == null || p.isSpectator() || p.isPassenger()) return false;
        double max = Math.min(32,Math.max(2,number(active,"range",16)));
        long delay = Math.max(1,Math.min(1200,(long) number(active,"cooldown_ticks",40)));
        long now = p.serverLevel().getGameTime();
        long until = 0;
        try { until = Long.parseLong(ProgressionApi.mode(p,"tmm_progression:blink_until","0")); }
        catch (NumberFormatException ignored) {}
        if (now < until && until-now <= delay) return false;

        Vec3 eye = p.getEyePosition();
        Vec3 forward = p.getLookAngle().normalize();
        HitResult hit = p.pick(max,0,false);
        double visible = Math.min(max,eye.distanceTo(hit.getLocation())-0.35);
        Vec3 chosen = null;
        // Farthest safe step along visible ray. Keeps both head and feet collision-free.
        for (double d = 1.5; d <= visible; d += 0.25) {
            Vec3 feet = eye.add(forward.scale(d)).subtract(0,p.getEyeHeight(),0);
            BlockPos floor = BlockPos.containing(feet.x,feet.y-0.15,feet.z);
            BlockPos destination = BlockPos.containing(feet);
            if (!p.serverLevel().hasChunkAt(destination)
                    || !p.serverLevel().getWorldBorder().isWithinBounds(destination)) break;
            if (p.serverLevel().getBlockState(floor).getCollisionShape(p.serverLevel(),floor).isEmpty())
                continue;
            if (!p.serverLevel().getFluidState(destination).isEmpty()) continue;
            if (!p.serverLevel().noCollision(p,p.getBoundingBox().move(feet.subtract(p.position()))))
                continue;
            chosen = feet;
        }
        if (chosen == null || chosen.distanceTo(p.position()) < 1.0) return false;
        p.teleportTo(p.serverLevel(),chosen.x,chosen.y,chosen.z,
            java.util.Set.of(),p.getYRot(),p.getXRot());
        ProgressionApi.setMode(p,"tmm_progression:blink_until",Long.toString(now+delay));
        return true;
    }
    private static double number(EffectRuntime.Active active,String key,double fallback) {
        try { return Double.parseDouble(active.effect().params().getOrDefault(key,Double.toString(fallback))); }
        catch (NumberFormatException ignored) { return fallback; }
    }

    /** Physical reaction: only arrow (not trident), frontal approach, truly empty hands. */
    public static void arrowImpact(ServerPlayer defender, ProjectileImpactEvent event) {
        if (!unlocked(defender,"catch_arrows") || !(event.getProjectile() instanceof AbstractArrow arrow)
                || arrow instanceof ThrownTrident || arrow.getOwner() == defender
                || !defender.getMainHandItem().isEmpty() || !defender.getOffhandItem().isEmpty()
                || event.isCanceled()) return;
        Vec3 incoming = arrow.getDeltaMovement();
        if (incoming.lengthSqr() < 0.001
                || defender.getLookAngle().dot(incoming.normalize().scale(-1)) < 0.5) return;
        // Store only pickup-allowed physical ammo; never materialize creative/infinity arrows.
        ItemStack recovered = arrow.pickup == AbstractArrow.Pickup.ALLOWED ?
            arrow.getPickupItemStackOrigin().copy() : ItemStack.EMPTY;
        event.setCanceled(true);
        arrow.discard();
        if (!recovered.isEmpty()) {
            if (!defender.getInventory().add(recovered) && !recovered.isEmpty())
                defender.drop(recovered,false);
        }
    }

    /** Per-mob retaliation. Neutral creatures still fight back after a qualifying strike. */
    public static void incoming(LivingIncomingDamageEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player
                && event.getEntity() instanceof Mob mob) {
            RETALIATION.put(mob.getUUID(),new Retaliation(player.getUUID(),
                player.serverLevel().getGameTime()+600));
        }
    }
    public static void targetChange(ServerPlayer p, LivingChangeTargetEvent event) {
        if (!(event.getEntity() instanceof Mob mob)) return;
        if (mob instanceof net.minecraft.world.entity.boss.enderdragon.EnderDragon
                || mob instanceof net.minecraft.world.entity.boss.wither.WitherBoss) return;
        if (mob instanceof Creeper creeper &&
                creeper.getPersistentData().getBoolean("tmm_progression:pacified")) {
            event.setNewAboutToBeSetTarget(null);
            return;
        }
        var last = RETALIATION.get(mob.getUUID());
        if (last != null && last.attacker().equals(p.getUUID())
                && p.serverLevel().getGameTime() <= last.until()) return;
        for (var a : EffectRuntime.active(p))
            if (a.effect().type().equals("faction_neutrality")
                    && Selectors.entity(a.effect().selector(),mob)) {
                event.setNewAboutToBeSetTarget(null);
                return;
            }
    }
    public static boolean entityInteract(ServerPlayer p,PlayerInteractEvent.EntityInteract event) {
        if (event.getHand() != InteractionHand.MAIN_HAND || !p.getMainHandItem().isEmpty())
            return false;
        if (event.getTarget() instanceof Creeper creeper && unlocked(p,"creeper_peace")) {
            creeper.getPersistentData().putBoolean("tmm_progression:pacified",true);
            creeper.setTarget(null);
            creeper.setSwellDir(-1);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return true;
        }
        return false;
    }
    public static void forget(ServerPlayer p) {
        RETALIATION.entrySet().removeIf(x -> x.getValue().attacker().equals(p.getUUID()));
    }
}
