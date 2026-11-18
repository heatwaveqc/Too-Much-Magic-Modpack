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
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.common.util.TriState;
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
            return ProgressionApi.setMode(p,ARMOR_MODE,next);
        }
        if ("treasure".equals(what) && unlocked(p,"treasure_hand")) {
            String next = ProgressionApi.mode(p,TREASURE_MODE,"fortune").equals("fortune") ? "silk" : "fortune";
            return ProgressionApi.setMode(p,TREASURE_MODE,next);
        }
        if ("glide".equals(what) && unlocked(p,"momentum_glide")) {
            String next = ProgressionApi.mode(p,GLIDE_MODE,"off").equals("on") ? "off" : "on";
            return ProgressionApi.setMode(p,GLIDE_MODE,next);
        }
        if ("ordeal".equals(what) && unlocked(p,"blindness_ordeal")
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

    /** No item deletion, including full inventories or creative/admin inserted equipment. */
    private static int emptyInventorySlot(ServerPlayer p) {
        for (int i = 1; i < 36; i++)
            if (p.getInventory().getItem(i).isEmpty()) return i;
        return -1;
    }
    /** Ground pickups must never use reserved index 0 as the only available slot. */
    public static void pickup(ServerPlayer p, ItemEntityPickupEvent.Pre event) {
        if (!unlocked(p,"reserved_fist")) return;
        ItemStack item = event.getItemEntity().getItem();
        if (item.isEmpty()) return;
        int capacity = 0;
        for (int i = 1; i < 36; i++) {
            ItemStack slot = p.getInventory().getItem(i);
            if (slot.isEmpty()) capacity += item.getMaxStackSize();
            else if (ItemStack.isSameItemSameComponents(slot,item))
                capacity += Math.max(0,slot.getMaxStackSize()-slot.getCount());
            if (capacity >= item.getCount()) return;
        }
        // Leave the original item entity untouched on the ground.
        event.setCanPickup(TriState.FALSE);
    }

    public static void tick(ServerPlayer p) {
        // The mob UUID retaliation ledger is transient; expire entries to avoid
        // retaining every hunted mob for the lifetime of a dedicated server.
        if (p.tickCount % 200 == 0) {
            long now = p.serverLevel().getGameTime();
            RETALIATION.entrySet().removeIf(entry -> entry.getValue().until() < now);
        }
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

        VirtualElytra.reconcile(p);
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
        if(old==null||p.isCreative()||p.isSpectator())return;
        boolean nativeHunger=unlocked(p,"hunger_discipline");
        double rate=0;
        for(var a:EffectRuntime.active(p))if(a.effect().type().equals("hunger_efficiency"))
            rate+=a.effect().amountPerRank()*a.rank();
        double scale=(nativeHunger?0.5*TyzMonkBridge.hungerMultiplier(p):1)*Math.max(.05,1-rate);
        if (now > old && scale<1) {
            float reduced = old + (now - old) * (float)scale;
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
        double factor=active==null?1:Math.max(1.0,Math.min(1.5,number(active,"factor",1.2)));
        double bonus=0;
        for(var a:EffectRuntime.active(p))if(a.effect().type().equals("jump_bonus"))bonus+=a.effect().amountPerRank()*a.rank();
        factor=Math.min(3,factor*(1+Math.max(0,bonus)));
        if(factor==1||p.isCreative()||p.isSpectator())return;
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

    /**
     * The vanilla Elytra pipeline supplies all glide/turn/pitch physics.
     * Our ONLY movement addition is a light persistent firework-like
     * impulse during real fall-flying, never simulated gliding or hovering.
     *
     * Keep it below the strength needed for a steep climb: looking far
     * upward reduces both components to zero, and normal Elytra stalls.
     */
    private static void glide(ServerPlayer p) {
        if (!p.isFallFlying() || !VirtualElytra.eligible(p)
                || p.isPassenger() || p.isInWater() || p.isInLava()) return;

        Vec3 velocity = p.getDeltaMovement();
        Vec3 look = p.getLookAngle();
        double horizontalLook = Math.hypot(look.x,look.z);
        double horizontalSpeed = Math.hypot(velocity.x,velocity.z);
        // A near-vertical pitch receives NO boost; the player cannot rocket skyward.
        double levelFactor = Math.max(0.0, 1.0 - Math.pow(Math.abs(look.y) / 0.65, 2));
        if (horizontalLook < 0.2 || levelFactor <= 0.0) return;

        // Monk's existing Tyz control skill improves the small thrust, not eligibility.
        double monkControl = NewbieMode.isNewbie(p) ? 0.0 : TyzMonkBridge.glideSpeedBonus(p);
        double forwardBoost = (0.016 + monkControl * 0.04) * levelFactor;
        Vec3 heading = new Vec3(look.x / horizontalLook, 0, look.z / horizontalLook);
        Vec3 horizontal = new Vec3(velocity.x,0,velocity.z);
        // We never snap/reduce natural Elytra or firework speed; only stop adding thrust.
        if (horizontalSpeed < 1.05)
            horizontal = horizontal.add(heading.scale(Math.min(forwardBoost,1.05-horizontalSpeed)));

        // At level pitch with existing airspeed, the ~0.026/tick lift offsets
        // vanilla Elytra's shallow descent and supports a slow altitude gain.
        // Starting from a standstill cannot generate vertical thrust.
        double airspeedFactor = Math.min(1.0,Math.max(0.0,(horizontalSpeed-0.2)/0.35));
        double lift = 0.026 * levelFactor * airspeedFactor;
        double vertical = velocity.y;
        if (vertical < 0.065) vertical = Math.min(0.065,vertical+lift);
        p.setDeltaMovement(horizontal.x,vertical,horizontal.z);
        p.hurtMarked = true;
    }

    private static boolean ordealComplete(ServerPlayer p,EffectRuntime.Active active) {
        var params=active.effect().params();
        String path=params.getOrDefault("completion_path","");
        String milestone=params.getOrDefault("completion_milestone","");
        return !path.isEmpty()&&!milestone.isEmpty()&&ProgressionApi.track(p,path).completed(milestone);
    }
    private static void ordeal(ServerPlayer p) {
        if (!ProgressionApi.mode(p,ORDEAL_MODE,"off").equals("active")) return;
        var active=effect(p,"blindness_ordeal");
        if(active==null){
            ProgressionApi.setMode(p,ORDEAL_MODE,"off");
            return; // Do not remove external blindness; the temporary ordeal refresh expires.
        }
        if (ordealComplete(p,active)) {
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
                || !(event.getEntity() instanceof Monster)
                || event.getSource().getDirectEntity() != p
                || !p.getMainHandItem().isEmpty()) return;
        var active=effect(p,"blindness_ordeal");
        if(active==null)return;
        String eventId=active.effect().params().getOrDefault("kill_event","");
        if(eventId.isEmpty())return;
        ProgressionApi.recordExternal(p,eventId);
        if (ordealComplete(p,active)) {
            ProgressionApi.setMode(p,ORDEAL_MODE,"done");
            p.removeEffect(MobEffects.BLINDNESS);
        }
    }

    public static void fall(ServerPlayer p,LivingFallEvent event) {
        double reduction=unlocked(p,"monk_leap")?3:0;
        for(var a:EffectRuntime.active(p))if(a.effect().type().equals("fall_reduction"))reduction+=a.effect().amountPerRank()*a.rank();
        if(reduction>0&&!p.isCreative()&&!p.isSpectator())event.setDistance((float)Math.max(0,event.getDistance()-Math.min(128,reduction)));
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
            ProgressionApi.recordExternal(p,"tmm_progression:creeper_kissed");
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
