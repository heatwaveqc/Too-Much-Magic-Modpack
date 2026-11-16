package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
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
    private static final String GLIDE_MODE = "tmm_progression:momentum_glide";
    private static final String ORDEAL_MODE = "tmm_progression:blind_ordeal";
    private static final Map<UUID,Retaliation> RETALIATION = new HashMap<>();
    private static final Map<UUID,Float> LAST_EXHAUSTION = new HashMap<>();
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
            if (!ProgressionApi.setMode(p,ARMOR_MODE,next)) return false;
            if (next.equals("on")) enforceArmorLock(p,false);
            return true;
        }
        if ("treasure".equals(what) && unlocked(p,"treasure_hand")) {
            String next = ProgressionApi.mode(p,TREASURE_MODE,"fortune").equals("fortune") ? "silk" : "fortune";
            return ProgressionApi.setMode(p,TREASURE_MODE,next);
        }
        if ("glide".equals(what) && unlocked(p,"momentum_glide")) {
            String next = ProgressionApi.mode(p,GLIDE_MODE,"off").equals("on") ? "off" : "on";
            return ProgressionApi.setMode(p,GLIDE_MODE,next);
        }
        if ("ordeal".equals(what) && ProgressionApi.track(p,"tmm_progression:monk").rank() >= 17
                && !ProgressionApi.mode(p,ORDEAL_MODE,"off").equals("done")) {
            String next = ProgressionApi.mode(p,ORDEAL_MODE,"off").equals("active") ? "off" : "active";
            if (next.equals("off")) p.removeEffect(MobEffects.BLINDNESS);
            return ProgressionApi.setMode(p,ORDEAL_MODE,next);
        }
        return false;
    }
    public static String mode(ServerPlayer p,String type) {
        return switch(type) {
            case "armor" -> ProgressionApi.mode(p,ARMOR_MODE,"on");
            case "glide" -> ProgressionApi.mode(p,GLIDE_MODE,"off");
            case "ordeal" -> ProgressionApi.mode(p,ORDEAL_MODE,"off");
            default -> ProgressionApi.mode(p,TREASURE_MODE,"fortune");
        };
    }

    /**
     * Armor Lock acts on actually equipped vanilla armor. Removable pieces are
     * tossed safely into the world, so a full inventory never traps armor.
     * Binding armor remains worn and takes one ordinary durability wear attempt
     * per second while the lock is on.
     *
     * The lock is not an item-placement ban: it corrects equipment state.
     */
    public static void enforceArmorLock(ServerPlayer p, boolean applyWear) {
        if (!unlocked(p,"armor_lock") || !ProgressionApi.mode(p,ARMOR_MODE,"on").equals("on"))
            return;
        for (EquipmentSlot slot : List.of(EquipmentSlot.HEAD,EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,EquipmentSlot.FEET)) {
            ItemStack worn = p.getItemBySlot(slot);
            if (worn.isEmpty()) continue;
            boolean bound = EnchantmentHelper.has(worn,EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE);
            if (bound) {
                // Bound armor stays equipped. Life Mending and Unbreaking still apply normally.
                if (applyWear && worn.isDamageableItem() && p.tickCount % 20 == 0)
                    worn.hurtAndBreak(1,p,slot);
                continue;
            }
            // Remove first so a canceled toss can be rolled back with no duplication.
            p.setItemSlot(slot,ItemStack.EMPTY);
            var tossed = p.drop(worn,false);
            if (tossed == null) {
                p.setItemSlot(slot,worn);
                if (p.tickCount % 100 == 0)
                    p.displayClientMessage(Component.literal("Armor Lock: could not safely drop armor"),true);
            }
        }
    }

    public static void tick(ServerPlayer p) {
        // The mob UUID retaliation ledger is transient; expire entries to avoid
        // retaining every hunted mob for the lifetime of a dedicated server.
        if (p.tickCount % 200 == 0) {
            long now = p.serverLevel().getGameTime();
            RETALIATION.entrySet().removeIf(entry -> entry.getValue().until() < now);
        }
        enforceArmorLock(p,true);

        exhaustion(p);
        stare(p);
        waterWalk(p);
        glide(p);
        ordeal(p);
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

    /** Halve new exhaustion increments; never give food levels or override ordinary eating. */
    private static void exhaustion(ServerPlayer p) {
        float now = p.getFoodData().getExhaustionLevel();
        Float old = LAST_EXHAUSTION.put(p.getUUID(),now);
        if (old == null || !unlocked(p,"hunger_discipline")) return;
        if (now > old) {
            float reduced = old + (now - old) * 0.5f;
            p.getFoodData().setExhaustion(reduced);
            LAST_EXHAUSTION.put(p.getUUID(),reduced);
        }
    }

    /** Conservative gaze implementation: line of sight, range, no invulnerable bosses. */
    private static void stare(ServerPlayer p) {
        var active = effect(p,"withering_stare");
        if (active == null || p.tickCount % 20 != 0) return;
        double max = Math.max(2,Math.min(24,number(active,"range",20)));
        var candidates = p.serverLevel().getEntitiesOfClass(LivingEntity.class,
            p.getBoundingBox().inflate(max), living ->
                living.isAlive() && living != p && !(living instanceof EnderDragon)
                && Selectors.entity(active.effect().selector(),living));
        LivingEntity candidate = null;
        double nearest = max*max;
        for (LivingEntity living : candidates) {
            Vec3 direction = living.getEyePosition().subtract(p.getEyePosition());
            double dist = direction.lengthSqr();
            if (dist >= nearest || dist < 0.1 || !p.hasLineOfSight(living)) continue;
            if (p.getLookAngle().dot(direction.normalize()) < 0.985) continue;
            nearest = dist;
            candidate = living;
        }
        if (candidate == null) return;
        if (candidate instanceof WitherBoss) {
            // Let the vanilla boss's damage code enforce spawn invulnerability
            // and current phase, rather than forcing effects or ignoring immunity.
            candidate.hurt(p.damageSources().magic(),1.0f);
            return;
        }
        boolean applied = candidate.addEffect(new MobEffectInstance(MobEffects.WITHER,50,0),p);
        // Vanilla undead can reject Wither status; still permit a modest direct gaze effect.
        if (!applied && candidate.getType().is(EntityTypeTags.UNDEAD))
            candidate.hurt(p.damageSources().magic(),1.0f);
    }

    public static void jump(ServerPlayer p, LivingEvent.LivingJumpEvent event) {
        var active = effect(p,"monk_leap");
        if (active == null) return;
        double factor = Math.max(1.0,Math.min(1.5,number(active,"factor",1.2)));
        Vec3 speed = p.getDeltaMovement();
        p.setDeltaMovement(speed.x,speed.y*factor,speed.z);
        p.hurtMarked = true;
    }

    /** Small native surface buoyancy: crouching always allows intentional diving. */
    private static void waterWalk(ServerPlayer p) {
        if (!unlocked(p,"water_walk") || p.isCrouching() || p.isPassenger()
                || p.isUnderWater() || p.isFallFlying()) return;
        BlockPos below = p.blockPosition().below();
        if (p.serverLevel().getFluidState(below).is(FluidTags.WATER)) {
            Vec3 speed = p.getDeltaMovement();
            if (speed.y < 0.08) p.setDeltaMovement(speed.x,0.08,speed.z);
            p.fallDistance = 0;
        }
    }

    /** Controlled descent with limited thrust, no hovering or Creative flight flag. */
    private static void glide(ServerPlayer p) {
        if (!unlocked(p,"momentum_glide")
                || !ProgressionApi.mode(p,GLIDE_MODE,"off").equals("on")
                || p.onGround() || p.isPassenger() || p.isFallFlying()
                || p.isInWater() || p.isInLava()) return;
        Vec3 speed = p.getDeltaMovement();
        if (speed.y >= 0) return; // never use native thrust to climb
        Vec3 heading = p.getLookAngle().multiply(1,0,1);
        if (heading.lengthSqr() > 0.001) heading = heading.normalize().scale(0.025);
        Vec3 horizontal = new Vec3(speed.x,0,speed.z).add(heading);
        if (horizontal.lengthSqr() > 0.36) horizontal = horizontal.normalize().scale(0.6);
        p.setDeltaMovement(horizontal.x,Math.max(-0.16,speed.y),horizontal.z);
        p.fallDistance = 0;
        p.hurtMarked = true;
    }

    private static void ordeal(ServerPlayer p) {
        if (!ProgressionApi.mode(p,ORDEAL_MODE,"off").equals("active")) return;
        if (ProgressionApi.track(p,"tmm_progression:monk").rank() >= 18) {
            ProgressionApi.setMode(p,ORDEAL_MODE,"done");
            p.removeEffect(MobEffects.BLINDNESS);
            return;
        }
        if (p.tickCount % 20 == 0)
            p.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,45,0,true,false));
    }
    /** Only a player-attributed hostile kill under actual blindness completes the ordeal. */
    public static void creditedKill(ServerPlayer p, LivingDeathEvent event) {
        if (!ProgressionApi.mode(p,ORDEAL_MODE,"off").equals("active")
                || !p.hasEffect(MobEffects.BLINDNESS)
                || !(event.getEntity() instanceof Monster)) return;
        ProgressionApi.setMode(p,ORDEAL_MODE,"done");
        p.removeEffect(MobEffects.BLINDNESS);
        ProgressionApi.recordExternal(p,"tmm_progression:blind_hostile_kill");
    }

    public static void fall(ServerPlayer p,LivingFallEvent event) {
        if (unlocked(p,"monk_leap"))
            event.setDistance(Math.max(0.0f,event.getDistance()-3.0f));
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
        if (event.getEntity() instanceof ServerPlayer defender
                && event.getSource().is(DamageTypes.LAVA)
                && !defender.isOnFire() && unlocked(defender,"lava_protection")) {
            // Distinct from the generic fire resistance: one-off protection on
            // otherwise non-burning players contacting lava, not blanket immunity.
            event.setAmount(event.getAmount() * 0.5f);
        }
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
        LAST_EXHAUSTION.remove(p.getUUID());
        if (ProgressionApi.mode(p,ORDEAL_MODE,"off").equals("active")) {
            ProgressionApi.setMode(p,ORDEAL_MODE,"off");
            p.removeEffect(MobEffects.BLINDNESS);
        }
        RETALIATION.entrySet().removeIf(x -> x.getValue().attacker().equals(p.getUUID()));
    }
}
