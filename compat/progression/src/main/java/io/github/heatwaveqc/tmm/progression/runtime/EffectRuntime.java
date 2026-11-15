package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.data.PathRegistry;
import io.github.heatwaveqc.tmm.progression.model.Definition;
import io.github.heatwaveqc.tmm.progression.state.Ledger;
import io.github.heatwaveqc.tmm.progression.state.ProgressionAttachments;
import io.github.heatwaveqc.tmm.progression.state.Track;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/** Reusable built-in effect handlers. No Monk/Hunter ID is hard-coded here. */
public final class EffectRuntime {
    private EffectRuntime() {}
    public record Active(String id, Definition.Effect effect, int rank) {}
    private static final Map<UUID,Map<ResourceLocation,String>> previousAttributes = new HashMap<>();

    public static List<Active> active(ServerPlayer player) {
        Ledger ledger = player.getData(ProgressionAttachments.LEDGER.get());
        List<Active> out = new ArrayList<>();
        for (Definition.Path path : PathRegistry.all().values()) {
            Track track = ledger.get(path.id());
            if (!track.joined()) continue;
            for (Definition.Skill skill : path.skills()) {
                int rank = track.skillRank(skill.id());
                if (rank < 1) continue;
                for (int n = 0; n < skill.effects().size(); n++) {
                    Definition.Effect effect = skill.effects().get(n);
                    if (eligible(player,effect,track)) {
                        String id = path.id().replace(':','/') + "/" +
                            skill.id().replace(':','/') + "/" + n;
                        out.add(new Active(id,effect,rank));
                    }
                }
            }
        }
        return out;
    }
    private static boolean eligible(ServerPlayer p, Definition.Effect effect, Track track) {
        String milestone = effect.params().get("requires_milestone");
        if (milestone != null && !track.completed(milestone)) return false;
        if ("true".equals(effect.params().get("requires_sprinting")) && !p.isSprinting()) return false;
        if ("true".equals(effect.params().get("requires_not_sneaking")) && p.isCrouching()) return false;
        if (effect.emptyHand() && !p.getMainHandItem().isEmpty()) return false;
        if (effect.unarmored()) {
            for (ItemStack worn : p.getArmorSlots()) if (!worn.isEmpty()) return false;
        }
        return true;
    }
    private static ResourceLocation modifierId(Active active) {
        return ResourceLocation.fromNamespaceAndPath("tmm_progression",active.id());
    }
    private static Holder<Attribute> attribute(String id) {
        return BuiltInRegistries.ATTRIBUTE.getHolder(ResourceLocation.parse(id)).orElse(null);
    }
    private static AttributeModifier.Operation operation(String id) {
        return AttributeModifier.Operation.valueOf(id.toUpperCase(Locale.ROOT));
    }

    /** Reconcile transient modifiers so changing armor, trees, logout and /reload cannot strand buffs. */
    public static void reconcile(ServerPlayer player) {
        Map<ResourceLocation,String> desired = new HashMap<>();
        for (Active active : active(player)) {
            Definition.Effect effect = active.effect();
            if (!effect.type().equals("attribute")) continue;
            Holder<Attribute> attr = attribute(effect.attribute());
            if (attr == null) continue;
            AttributeInstance instance = player.getAttribute(attr);
            if (instance == null) continue;
            ResourceLocation id = modifierId(active);
            double amount = effect.amountPerRank() * active.rank();
            AttributeModifier.Operation op = operation(effect.operation());
            AttributeModifier old = instance.getModifier(id);
            if (old != null && (old.amount() != amount || old.operation() != op)) {
                instance.removeModifier(id);
                old = null;
            }
            if (old == null && amount != 0)
                instance.addTransientModifier(new AttributeModifier(id,amount,op));
            desired.put(id,effect.attribute());
        }
        Map<ResourceLocation,String> previous = previousAttributes.getOrDefault(player.getUUID(),Map.of());
        previous.forEach((id,attrName) -> {
            if (!desired.containsKey(id)) {
                Holder<Attribute> holder = attribute(attrName);
                AttributeInstance instance = holder == null ? null : player.getAttribute(holder);
                if (instance != null) instance.removeModifier(id);
            }
        });
        previousAttributes.put(player.getUUID(),desired);
    }

    public static void logout(ServerPlayer player) {
        Map<ResourceLocation,String> previous = previousAttributes.remove(player.getUUID());
        if (previous == null) return;
        previous.forEach((id,name) -> {
            Holder<Attribute> holder = attribute(name);
            AttributeInstance instance = holder == null ? null : player.getAttribute(holder);
            if (instance != null) instance.removeModifier(id);
        });
    }

    public static void incoming(LivingIncomingDamageEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer attacker
            && event.getSource().getDirectEntity() == attacker) {
            double bonus = 0;
            for (Active a : active(attacker)) {
                Definition.Effect fx = a.effect();
                if (fx.type().equals("melee_bonus")
                    && Selectors.entity(fx.selector(),event.getEntity())
                    && Selectors.item(fx.weapon(),attacker.getMainHandItem())) {
                    bonus += fx.amountPerRank() * a.rank();
                }
            }
            if (bonus > 0) event.setAmount((float) Math.min(1_000_000,event.getAmount() + bonus));
        }
        // Damage from a fired bow/crossbow uses the weapon captured by the arrow,
        // not whatever the player happens to hold when it lands.
        if (event.getSource().getEntity() instanceof ServerPlayer owner) {
            boolean melee = event.getSource().getDirectEntity() == owner;
            boolean arrow = event.getSource().getDirectEntity() instanceof AbstractArrow;
            ItemStack weapon = melee ? owner.getMainHandItem() :
                (arrow ? ((AbstractArrow) event.getSource().getDirectEntity()).getWeaponItem() : ItemStack.EMPTY);
            for (Active a : active(owner)) {
                Definition.Effect fx = a.effect();
                if (!fx.type().equals("damage_bonus")) continue;
                String delivery = fx.params().getOrDefault("delivery","melee");
                if (!(melee && (delivery.equals("melee") || delivery.equals("any")) ||
                      arrow && (delivery.equals("arrow") || delivery.equals("any")))) continue;
                if (Selectors.entity(fx.selector(),event.getEntity()) && Selectors.item(fx.weapon(),weapon)) {
                    double add = fx.amountPerRank() * a.rank();
                    if (add > 0) event.setAmount((float) Math.min(1000000,event.getAmount() + add));
                }
            }
        }
        if (event.getEntity() instanceof ServerPlayer defender) {
            double multiplier = 1;
            for (Active a : active(defender)) {
                Definition.Effect fx = a.effect();
                if (fx.type().equals("damage_multiplier") && sourceMatches(fx.damageTag(),event.getSource())) {
                    multiplier *= Math.max(0,1.0 - fx.amountPerRank() * a.rank());
                }
            }
            if (multiplier != 1) event.setAmount((float) Math.max(0,event.getAmount() * multiplier));
        }
    }
    private static boolean sourceMatches(String selector, DamageSource source) {
        if (selector.isEmpty() || selector.equals("*")) return true;
        String key = selector.startsWith("#") ? selector.substring(1) : selector;
        ResourceLocation id = ResourceLocation.parse(key);
        return selector.startsWith("#") ?
            source.is(TagKey.create(Registries.DAMAGE_TYPE,id)) :
            source.is(net.minecraft.resources.ResourceKey.create(Registries.DAMAGE_TYPE,id));
    }

    public static void breakSpeed(PlayerEvent.BreakSpeed event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        double scale = 1;
        for (Active a : active(player)) {
            Definition.Effect fx = a.effect();
            if (fx.type().equals("break_speed") && Selectors.block(fx.selector(),event.getState()))
                scale += fx.amountPerRank() * a.rank();
        }
        if (scale != 1) event.setNewSpeed((float) Math.min(100000,Math.max(0,event.getNewSpeed() * scale)));
    }

    public static void harvest(PlayerEvent.HarvestCheck event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        BlockState state = event.getTargetBlock();
        int required = requiredTier(state);
        for (Active a : active(player)) {
            Definition.Effect fx = a.effect();
            if (fx.type().equals("harvest_tier") && Selectors.block(fx.selector(),state)
                && Math.min(fx.tier(),a.rank()) >= required && required >= 0) {
                event.setCanHarvest(true);
                return;
            }
        }
    }

    /** Vanilla mining tiers. Extra modded material tiers require a registered adapter. */
    static int requiredTier(BlockState state) {
        if (state.is(BlockTags.NEEDS_DIAMOND_TOOL)) return 3;
        if (state.is(BlockTags.NEEDS_IRON_TOOL)) return 2;
        if (state.is(BlockTags.NEEDS_STONE_TOOL)) return 1;
        return 0;
    }
    /** Reusable bare-hand animal interaction: successful affection initiates vanilla love. */
    public static boolean animalFriendship(ServerPlayer player, PlayerInteractEvent.EntityInteract event) {
        if (event.getHand() != InteractionHand.MAIN_HAND || !player.getMainHandItem().isEmpty()
                || !(event.getTarget() instanceof Animal animal)) return false;
        if (!animal.canFallInLove()) return false; // Adults, non-breeding state, cooldown.
        for (Active active : active(player)) {
            Definition.Effect fx = active.effect();
            if (!fx.type().equals("animal_friendship") || !Selectors.entity(fx.selector(),animal)) continue;
            animal.setInLove(player);
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
            ProgressionApi.record(player,Selectors.entityAction("animal_petted",animal));
            return true;
        }
        return false;
    }

    /** Rank-scaled wear/repair; damage respects normal Unbreaking and break callbacks. */
    public static void tick(ServerPlayer player) {
        for (Active active : active(player)) {
            Definition.Effect fx = active.effect();
            if (!fx.type().equals("gear_durability")) continue;
            int interval = Integer.parseInt(fx.params().getOrDefault("interval_ticks","20"));
            if (player.tickCount % interval != 0) continue;
            int amount = (int) Math.min(64,Math.floor(fx.amountPerRank() * active.rank()));
            if (amount <= 0) continue;
            String slot = fx.params().getOrDefault("slot","mainhand");
            boolean repair = fx.params().getOrDefault("action","repair").equals("repair");
            for (EquipmentSlot equipment : equipmentSlots(slot)) {
                ItemStack stack = player.getItemBySlot(equipment);
                if (!stack.isDamageableItem() || !Selectors.item(fx.selector(),stack)) continue;
                if (repair) {
                    if (stack.isDamaged()) stack.setDamageValue(Math.max(0,stack.getDamageValue() - amount));
                } else {
                    stack.hurtAndBreak(amount,player,equipment);
                }
            }
        }
    }

    private static List<EquipmentSlot> equipmentSlots(String slot) {
        return switch (slot) {
            case "mainhand" -> List.of(EquipmentSlot.MAINHAND);
            case "offhand" -> List.of(EquipmentSlot.OFFHAND);
            case "head" -> List.of(EquipmentSlot.HEAD);
            case "chest" -> List.of(EquipmentSlot.CHEST);
            case "legs" -> List.of(EquipmentSlot.LEGS);
            case "feet" -> List.of(EquipmentSlot.FEET);
            case "armor" -> List.of(EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.LEGS,EquipmentSlot.FEET);
            case "all" -> List.of(EquipmentSlot.MAINHAND,EquipmentSlot.OFFHAND,
                EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.LEGS,EquipmentSlot.FEET);
            default -> List.of();
        };
    }

}
