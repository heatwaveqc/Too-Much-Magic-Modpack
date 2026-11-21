package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.state.Track;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.CropBlock;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.minecraft.world.entity.item.ItemEntity;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Optional 6.4.2 Tyz Monk tree runtime. Tyz IMMUTABLE skills hold ranks in
 * Tyz's ONE global SP wallet. This bridge owns their conditional gameplay
 * implementations and native-Monk eligibility. No Tyz class or JAR dependency
 * exists in the standalone progression module.
 *
 * Every effect is computed from server-side Tyz purchased ranks and the native
 * Monk ledger. Invalid admin grants persist as dormant Tyz investment, never
 * become free native Monk abilities.
 */
public final class TyzMonkBridge {
    private TyzMonkBridge() {}
    public static final String MONK = "tyr_progression:monk";
    private static final String PREFIX = "tmm_monk_";
    private static final String DRAGON = "tyr_progression:dragon_defeated";
    private static final String FOCUS_PREFIX = "tyr_progression:tyz_focused_advancement_";

    private static boolean initialized;
    private static Object skillManager;
    private static Method getSkillLevel;
    private static final Map<UUID, Map<ResourceLocation, String>> ATTRIBUTES = new HashMap<>();
    private static final Map<UUID, Long> COUNTERS = new HashMap<>();

    private static final Set<String> ALL_SKILLS = Set.of(
            "tmm_monk_empty_hand_force", "tmm_monk_flowing_counter",
            "tmm_monk_unarmed_precision", "tmm_monk_natural_poise",
            "tmm_monk_hunger_discipline", "tmm_monk_fire_blast_tempering",
            "tmm_monk_body_speed", "tmm_monk_balanced_landing",
            "tmm_monk_glide_control", "tmm_monk_barehand_efficiency",
            "tmm_monk_fortunate_hands", "tmm_monk_looting_palm",
            "tmm_monk_monastic_learning", "tmm_monk_focused_advancement",
            "tmm_monk_inner_fortune", "tmm_monk_inner_reservoir",
            "tmm_monk_worldly_cultivation", "tmm_monk_capstone");

    public static boolean installed() {
        return ModList.get().isLoaded("tyzs_skills");
    }

    public static void register() {
        if (!installed()) return;
        try {
            Class<?> pre = Class.forName("com.tyzsskills.api.events.SkillActionEvent$PurchasePre");
            Class<?> refund = Class.forName("com.tyzsskills.api.events.SkillActionEvent$RefundPre");
            if (!Event.class.isAssignableFrom(pre) || !Event.class.isAssignableFrom(refund))
                throw new IllegalStateException("Tyz 6.4.2 event ABI changed");
            subscribe(pre, TyzMonkBridge::purchase);
            subscribe(refund, TyzMonkBridge::refund);
            ProgressionMod.LOGGER.info("Registered 18-skill native-licensed Monk Tyz bridge");
        } catch (ReflectiveOperationException | LinkageError failure) {
            ProgressionMod.LOGGER.error("Tyz is installed but Monk purchase gates failed to register; unsafe to enable Monk tree", failure);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void subscribe(Class<?> type, Consumer<Event> callback) {
        NeoForge.EVENT_BUS.addListener((Class) type, (Consumer) callback);
    }

    private static void initialize() {
        if (initialized || !installed()) return;
        try {
            Class<?> api = Class.forName("com.tyzsskills.api.TyzsSkillsAPI");
            skillManager = api.getMethod("skills").invoke(null);
            getSkillLevel = skillManager.getClass().getMethod(
                    "getSkillLevel", ServerPlayer.class, String.class);
            initialized = true;
        } catch (ReflectiveOperationException | RuntimeException e) {
            // Tyz may not have registered its manager at early mod startup.
            skillManager = null;
            getSkillLevel = null;
        }
    }

    public static int rawRank(ServerPlayer player, String id) {
        if (!ALL_SKILLS.contains(id) || !installed()) return 0;
        initialize();
        if (!initialized) return 0;
        try {
            Object n = getSkillLevel.invoke(skillManager, player, id);
            return n instanceof Number value ? Math.max(0, value.intValue()) : 0;
        } catch (ReflectiveOperationException | RuntimeException failure) {
            return 0;
        }
    }

    private static int minimumRank(String id) {
        return switch (id) {
            case "tmm_monk_flowing_counter", "tmm_monk_hunger_discipline",
                 "tmm_monk_balanced_landing", "tmm_monk_fortunate_hands",
                 "tmm_monk_monastic_learning" -> 10;
            case "tmm_monk_unarmed_precision", "tmm_monk_fire_blast_tempering",
                 "tmm_monk_looting_palm", "tmm_monk_focused_advancement" -> 17;
            case "tmm_monk_glide_control", "tmm_monk_inner_fortune",
                 "tmm_monk_inner_reservoir", "tmm_monk_worldly_cultivation",
                 "tmm_monk_capstone" -> 20;
            default -> 1;
        };
    }

    private static boolean unarmored(ServerPlayer player) {
        for (ItemStack armor : player.getArmorSlots())
            if (!armor.isEmpty()) return false;
        return true;
    }

    private static boolean emptyHands(ServerPlayer player) {
        return player.getMainHandItem().isEmpty() && player.getOffhandItem().isEmpty();
    }

    private static boolean qualifies(ServerPlayer player, String id) {
        Track monk = ProgressionApi.track(player, MONK);
        if (!monk.joined() || monk.rank() < minimumRank(id) || !unarmored(player))
            return false;
        if (id.equals("tmm_monk_fortunate_hands")
                && monk.skillRank("tyr_progression:treasure_hand") == 0) return false;
        if (id.equals("tmm_monk_glide_control")
                && (!monk.completed(DRAGON)
                    || monk.skillRank("tyr_progression:momentum_glide") == 0))
            return false;
        if (id.equals("tmm_monk_worldly_cultivation")
                && monk.skillRank("tyr_progression:hand_harvest") == 0) return false;
        if (id.equals("tmm_monk_capstone")
                && (!monk.completed(DRAGON) || !emptyHands(player) || !emptyCurios(player)))
            return false;
        return true;
    }

    /** Native membership and protection at USE TIME; purchased Tyz ranks can be dormant. */
    public static int level(ServerPlayer player, String id) {
        if (!ALL_SKILLS.contains(id) || !qualifies(player, id)) return 0;
        if (requiresEmptyHands(id) && !emptyHands(player)) return 0;
        return rawRank(player, id);
    }

    private static boolean requiresEmptyHands(String id) {
        return switch (id) {
            case "tmm_monk_empty_hand_force", "tmm_monk_flowing_counter",
                 "tmm_monk_unarmed_precision", "tmm_monk_barehand_efficiency",
                 "tmm_monk_fortunate_hands", "tmm_monk_looting_palm",
                 "tmm_monk_worldly_cultivation", "tmm_monk_capstone" -> true;
            default -> false;
        };
    }

    private static int lvl(ServerPlayer player, String id) { return level(player, PREFIX + id); }

    /** Tyz fires PurchasePre once for either single purchase or bulk purchase. */
    private static void purchase(Event event) {
        String id = eventSkillId(event);
        if (!ALL_SKILLS.contains(id)) return;
        ServerPlayer player = eventPlayer(event);
        if (player == null || !canPurchase(player, id))
            ((ICancellableEvent)event).setCanceled(true);
    }

    private static void refund(Event event) {
        String id = eventSkillId(event);
        if (id.equals("tmm_monk_focused_advancement"))
            ((ICancellableEvent)event).setCanceled(true);
    }

    private static String eventSkillId(Event event) {
        try {
            Object skill = event.getClass().getMethod("getSkill").invoke(event);
            Object id = skill.getClass().getMethod("getID").invoke(skill);
            return String.valueOf(id);
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            return "";
        }
    }

    private static ServerPlayer eventPlayer(Event event) {
        try {
            Object player = event.getClass().getMethod("getPlayer").invoke(event);
            return player instanceof ServerPlayer p ? p : null;
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            return null;
        }
    }

    private static boolean canPurchase(ServerPlayer player, String id) {
        if (!qualifies(player,id) || (requiresEmptyHands(id) && !emptyHands(player)))
            return false;
        if (id.equals("tmm_monk_capstone")) {
            // One mastery lane and a DISTINCT advanced lane, beyond ordinary
            // Tyz prerequisites (which only express AND).
            String[] mastery = {"unarmed_precision","fire_blast_tempering",
                    "glide_control","looting_palm"};
            String[] advanced = {"flowing_counter","hunger_discipline",
                    "balanced_landing","fortunate_hands"};
            for (int i=0;i<mastery.length;i++) {
                if (rawRank(player,PREFIX+mastery[i]) <= 0) continue;
                for (int j=0;j<advanced.length;j++) {
                    if (i!=j && rawRank(player,PREFIX+advanced[j]) > 0) return true;
                }
            }
            return false;
        }
        return true;
    }

    /** Fail closed if Curios is loaded but its actual slots cannot be read. */
    public static boolean emptyCurios(ServerPlayer player) {
        if (!ModList.get().isLoaded("curios")) return true;
        try {
            Class<?> api=Class.forName("top.theillusivec4.curios.api.CuriosApi");
            Object raw=api.getMethod("getCuriosInventory",
                    net.minecraft.world.entity.LivingEntity.class).invoke(null,player);
            if (!(raw instanceof java.util.Optional<?> optional) || optional.isEmpty())
                return false;
            Object handler=optional.get();
            Object map=handler.getClass().getMethod("getCurios").invoke(handler);
            if (!(map instanceof Map<?,?> entries)) return false;
            for (Object entry : entries.values()) {
                Object stacks=entry.getClass().getMethod("getStacks").invoke(entry);
                int slots=((Number)stacks.getClass().getMethod("getSlots").invoke(stacks)).intValue();
                for (int i=0;i<slots;i++) {
                    Object stack=stacks.getClass().getMethod("getStackInSlot",int.class).invoke(stacks,i);
                    if (stack instanceof ItemStack item && !item.isEmpty()) return false;
                }
            }
            return true;
        } catch (ReflectiveOperationException | RuntimeException error) {
            return false;
        }
    }

    private static void put(ServerPlayer player, Map<ResourceLocation,String> desired,
                            String id, String attribute, double amount) {
        ResourceLocation key=ResourceLocation.fromNamespaceAndPath("tyr_progression","tyz_monk/"+id);
        Holder<Attribute> holder=BuiltInRegistries.ATTRIBUTE.getHolder(ResourceLocation.parse(attribute))
                .orElse(null);
        if (holder==null) return;  // Optional magic mod not installed.
        AttributeInstance instance=player.getAttribute(holder);
        if (instance==null) return;
        AttributeModifier old=instance.getModifier(key);
        if (old!=null && (old.amount()!=amount ||
                old.operation()!=AttributeModifier.Operation.ADD_VALUE)) {
            instance.removeModifier(key);
            old=null;
        }
        if (amount!=0 && old==null)
            instance.addTransientModifier(new AttributeModifier(
                    key,amount,AttributeModifier.Operation.ADD_VALUE));
        desired.put(key,attribute);
    }

    /** Reconcile every tick: swapping into a weapon/armor cannot retain Monk buffs. */
    public static void tick(ServerPlayer player) {
        if (!installed()) return;
        Map<ResourceLocation,String> desired=new HashMap<>();
        int force=lvl(player,"empty_hand_force");
        int poise=lvl(player,"natural_poise");
        int speed=lvl(player,"body_speed");
        int landing=lvl(player,"balanced_landing");
        int fortune=lvl(player,"inner_fortune");
        int reservoir=lvl(player,"inner_reservoir");
        int capstone=lvl(player,"capstone");
        if (force>0) put(player,desired,"empty_hand_force",
                "minecraft:attack_damage",Math.min(8.75,0.35*force));
        if (poise>0) put(player,desired,"natural_poise",
                "minecraft:armor",Math.min(10,0.4*poise));
        if (speed>0) put(player,desired,"body_speed",
                "minecraft:movement_speed",Math.min(0.075,0.003*speed));
        if (landing>0) put(player,desired,"balanced_landing",
                "minecraft:knockback_resistance",Math.min(0.25,0.025*landing));
        if (fortune>0) put(player,desired,"inner_fortune",
                "minecraft:luck",Math.min(10,0.2*fortune));
        if (reservoir>0) {
            double value=Math.min(125,5*reservoir);
            put(player,desired,"inner_reservoir_ars",
                    "ars_nouveau:ars_nouveau.perk.max_mana",value);
            put(player,desired,"inner_reservoir_irons",
                    "irons_spellbooks:max_mana",value);
            put(player,desired,"inner_reservoir_psi",
                    "psi:total_psi",value*10);
        }
        if (capstone>0) {
            put(player,desired,"capstone_attack","minecraft:attack_damage",5);
            put(player,desired,"capstone_armor","minecraft:armor",3);
        }
        Map<ResourceLocation,String> prior=ATTRIBUTES.getOrDefault(player.getUUID(),Map.of());
        prior.forEach((key,attr)-> {
            if (desired.containsKey(key)) return;
            Holder<Attribute> holder=BuiltInRegistries.ATTRIBUTE
                    .getHolder(ResourceLocation.parse(attr)).orElse(null);
            if (holder!=null && player.getAttribute(holder)!=null)
                player.getAttribute(holder).removeModifier(key);
        });
        ATTRIBUTES.put(player.getUUID(),desired);
        int focus=lvl(player,"focused_advancement");
        Track nativeMonk=ProgressionApi.track(player,MONK);
        if (focus>0) {
            for (int stage=1;stage<=Math.min(5,focus);stage++) {
                if (nativeMonk.rank()>=Math.max(17,stage*4))
                    ProgressionApi.awardMonkTyzPointOnce(player,FOCUS_PREFIX+stage);
            }
        }
        if (player.tickCount % 600==0)
            COUNTERS.entrySet().removeIf(e -> e.getValue()<player.serverLevel().getGameTime()-100);
    }

    public static void incoming(LivingIncomingDamageEvent event) {
        if (event.isCanceled()) return;
        if (event.getSource().getEntity() instanceof ServerPlayer attacker
                && event.getSource().getDirectEntity()==attacker
                && emptyHands(attacker)) {
            int precision=lvl(attacker,"unarmed_precision");
            if (precision>0 && attacker.getRandom().nextDouble()<Math.min(0.10,0.02*precision))
                event.setAmount(event.getAmount()*1.5f);
        }
        if (!(event.getEntity() instanceof ServerPlayer defender)) return;
        int temper=lvl(defender,"fire_blast_tempering");
        if (temper>0 && (event.getSource().is(DamageTypeTags.IS_FIRE)
                         || event.getSource().is(DamageTypeTags.IS_EXPLOSION)))
            event.setAmount((float)(event.getAmount()*(1-Math.min(0.125,0.025*temper))));
        int landing=lvl(defender,"balanced_landing");
        if (landing>0 && event.getSource().is(DamageTypes.FALL))
            event.setAmount((float)(event.getAmount()*(1-Math.min(0.30,0.03*landing))));
        int counter=lvl(defender,"flowing_counter");
        if (counter>0 && event.getSource().getEntity() instanceof LivingEntity enemy
                && enemy instanceof Monster && event.getSource().getDirectEntity()==enemy
                && !event.getSource().is(DamageTypes.MAGIC)) {
            long now=defender.serverLevel().getGameTime();
            long last=COUNTERS.getOrDefault(defender.getUUID(),Long.MIN_VALUE);
            if (last==Long.MIN_VALUE || now<last || now-last>=40) {
                COUNTERS.put(defender.getUUID(),now);
                // Anonymous magic source prevents the retaliation from being
                // misclassified as a second bare-fist melee or XP reward.
                enemy.hurt(defender.damageSources().magic(),
                        Math.min(4,0.4f*counter));
            }
        }
    }

    public static void breakSpeed(PlayerEvent.BreakSpeed event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        int rank=lvl(player,"barehand_efficiency");
        if (rank>0)
            event.setNewSpeed((float)Math.min(100000,
                    event.getNewSpeed()*(1+Math.min(1.5,rank*0.06))));
    }

    public static int fortuneBonus(ServerPlayer player) {
        if (!ProgressionApi.mode(player,"tyr_progression:treasure_mode","fortune")
                .equals("fortune")) return 0;
        return Math.min(3,lvl(player,"fortunate_hands")/3);
    }

    /** Native Monk XP ONLY; no Tyz MXP or free Tyz SP minting. */
    public static long bonusMonkXp(ServerPlayer player, String path, long base) {
        if (!MONK.equals(path) || base<=0) return base;
        int rank=lvl(player,"monastic_learning");
        return Math.min(1_000_000L,base+Math.round(base*Math.min(0.5,rank*0.02)));
    }

    /** Applies to REMAINING exhaustion after native 50% hunger reduction. */
    public static float hungerMultiplier(ServerPlayer player) {
        return (float)(1.0-Math.min(0.4,lvl(player,"hunger_discipline")*0.04));
    }

    public static double glideSpeedBonus(ServerPlayer player) {
        return Math.min(0.1,0.02*lvl(player,"glide_control"));
    }

    /** Use only source-verified ordinary mob drops; never copy bosses/rare keys. */
    public static void mobDrops(LivingDropsEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)
                || event.getSource().getDirectEntity()!=player
                || !(event.getEntity() instanceof Monster)
                || event.getEntity() instanceof WitherBoss
                || event.getEntity() instanceof EnderDragon) return;
        int rank=lvl(player,"looting_palm");
        if (rank<=0 || player.getRandom().nextDouble()>=Math.min(0.20,0.04*rank))
            return;
        for (ItemEntity drop : event.getDrops()) {
            ItemStack stack=drop.getItem();
            if (stack.is(Items.ROTTEN_FLESH) || stack.is(Items.BONE)
                    || stack.is(Items.STRING) || stack.is(Items.SPIDER_EYE)
                    || stack.is(Items.GUNPOWDER) || stack.is(Items.SLIME_BALL)) {
                event.getDrops().add(new ItemEntity(event.getEntity().level(),
                        drop.getX(),drop.getY(),drop.getZ(),stack.copyWithCount(1)));
                break;
            }
        }
    }

    /** Verified mature crop bonus, no spawn-egg, ore, block entity or quest-key duplication. */
    public static void crops(BlockDropsEvent event, ServerPlayer player) {
        int rank=lvl(player,"worldly_cultivation");
        if (rank<=0 || event.isCanceled()
                || !(event.getState().getBlock() instanceof CropBlock crop)
                || !crop.isMaxAge(event.getState())
                || player.getRandom().nextDouble()>=Math.min(0.25,0.01*rank)) return;
        for (ItemEntity drop : event.getDrops()) {
            ItemStack stack=drop.getItem();
            if (stack.is(Items.WHEAT) || stack.is(Items.CARROT)
                    || stack.is(Items.POTATO) || stack.is(Items.BEETROOT)) {
                event.getDrops().add(new ItemEntity(event.getLevel(),
                        drop.getX(),drop.getY(),drop.getZ(),stack.copyWithCount(1)));
                break;
            }
        }
    }

    public static void logout(ServerPlayer player) {
        Map<ResourceLocation,String> prior=ATTRIBUTES.remove(player.getUUID());
        if (prior!=null) prior.forEach((id,name) -> {
            Holder<Attribute> holder=BuiltInRegistries.ATTRIBUTE
                    .getHolder(ResourceLocation.parse(name)).orElse(null);
            if (holder!=null && player.getAttribute(holder)!=null)
                player.getAttribute(holder).removeModifier(id);
        });
        COUNTERS.remove(player.getUUID());
    }
}
