package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import io.github.heatwaveqc.tmm.progression.model.Definition;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * Gilded's player-scoped mechanics. All bonuses are transient: no stored item upgrades,
 * no general change to vanilla tags or material definitions, and no offhand repair.
 * Virtual enchantment effects are read at their actual loot/wear call sites by Mixins.
 */
public final class GildedRules {
    private GildedRules() {}
    public static final String PATH = "tmm_progression:gilded";
    private static final TagKey<Item> NETHER_FOOD = itemTag("gilded_nether_foods");
    private static final TagKey<Item> GOLD_FOOD = itemTag("gilded_gold_foods");
    private static final TagKey<Item> RITE_FOOD = itemTag("gilded_rite_foods");
    private static final TagKey<Item> FORTUNE_TOOLS = itemTag("gilded_fortune_tools");
    private static final TagKey<Item> GOLD_WEAPONS = itemTag("gilded_gold_weapons");
    private static final TagKey<Item> WEARABLE_GEAR = itemTag("gilded_repairable_equipment");
    private static final TagKey<net.minecraft.world.level.block.Block> GOLD_ORES =
        TagKey.create(Registries.BLOCK,ResourceLocation.fromNamespaceAndPath("tmm_progression","gilded_gold_ores"));
    private static final ResourceLocation ARMOR_ID =
        ResourceLocation.fromNamespaceAndPath("tmm_progression","gilded_effective_gold_armor");
    private static final ResourceLocation TOUGHNESS_ID =
        ResourceLocation.fromNamespaceAndPath("tmm_progression","gilded_effective_gold_toughness");

    private static TagKey<Item> itemTag(String name) {
        return TagKey.create(Registries.ITEM,ResourceLocation.fromNamespaceAndPath("tmm_progression",name));
    }
    public static boolean joined(ServerPlayer player) {
        return ProgressionApi.track(player,PATH).joined();
    }
    public static int rank(ServerPlayer player, String skill) {
        return joined(player) ? ProgressionApi.track(player,PATH).skillRank("tmm_progression:" + skill) : 0;
    }
    public static void register() {
        NeoForge.EVENT_BUS.addListener(GildedRules::foodStart);

        EffectHandlers.register("tmm_progression:food_whitelist", new EffectHandlers.Handler() {
            @Override public void foodConsumed(ServerPlayer player, ItemStack food, Definition.Effect fx, int rank) {
                if (rank > 0 && isGoldenFood(player,food) && food.getFoodProperties(player) != null)
                    // Vanilla consumption already supplied the first portion. Do not apply potion effects twice.
                    player.getFoodData().eat(food.getFoodProperties(player));
            }
        });
        EffectHandlers.register("tmm_progression:piglin_affinity", new EffectHandlers.Handler() {
            @Override public void targetChange(ServerPlayer player, LivingChangeTargetEvent event,
                                                 Definition.Effect fx, int rank) {
                if (rank < 1 || !(event.getEntity() instanceof AbstractPiglin piglin)
                    || !event.getEntity().getType().is(net.minecraft.tags.TagKey.create(
                        Registries.ENTITY_TYPE,ResourceLocation.fromNamespaceAndPath("tmm_progression","gilded_piglin_allies"))))
                    return;
                // Existing explicit anger and retaliatory targeting must survive.
                if (ClassAbilities.isRecentRetaliation(piglin,player)
                    || piglin.getBrain().hasMemoryValue(MemoryModuleType.ANGRY_AT)) return;
                event.setNewAboutToBeSetTarget(null);
            }
        });
        EffectHandlers.register("tmm_progression:gold_diamond_equivalence", new EffectHandlers.Handler() {
            @Override public void incoming(ServerPlayer player, LivingIncomingDamageEvent event,
                                            Definition.Effect fx, int rank) {
                if (rank > 0 && event.getSource().getDirectEntity() == player
                    && event.getSource().getEntity() == player) {
                    int bonus = goldWeaponBonus(player.getMainHandItem());
                    if (bonus > 0) event.setAmount(event.getAmount() + bonus);
                }
            }
            @Override public void harvest(ServerPlayer player, PlayerEvent.HarvestCheck event,
                                           Definition.Effect fx, int rank) {
                if (rank > 0 && player.getMainHandItem().is(Items.GOLDEN_PICKAXE)
                    && event.getTargetBlock().is(BlockTags.NEEDS_DIAMOND_TOOL))
                    event.setCanHarvest(true);
            }
            @Override public void tick(ServerPlayer player, Definition.Effect fx, int rank) {
                reconcileArmor(player);
            }
            @Override public void equipmentChange(ServerPlayer player, LivingEquipmentChangeEvent event,
                                                   Definition.Effect fx, int rank) {
                reconcileArmor(player);
            }
        });
        // Applied by GildedFortuneMixin inside the actual ore-loot formula.
        EffectHandlers.register("tmm_progression:virtual_fortune", new EffectHandlers.Handler() {});
        // Applied by GildedLootingMixin inside mob loot functions, mirroring Ars Thread of the Drygmy.
        EffectHandlers.register("tmm_progression:virtual_looting", new EffectHandlers.Handler() {});
        // Applied by GildedDurabilityMixin at the owner-scoped durability wear call.
        EffectHandlers.register("tmm_progression:virtual_unbreaking", new EffectHandlers.Handler() {});
    }

    private static int goldWeaponBonus(ItemStack stack) {
        if (stack.is(Items.GOLDEN_SWORD) || stack.is(Items.GOLDEN_PICKAXE)
            || stack.is(Items.GOLDEN_SHOVEL)) return 3;
        if (stack.is(Items.GOLDEN_AXE)) return 2;
        return 0;
    }
    public static void reconcileArmor(ServerPlayer player) {
        int armor = 0, pieces = 0;
        if (joined(player) && rank(player,"gilded_gold_diamond_equivalence") > 0) {
            if (player.getItemBySlot(EquipmentSlot.HEAD).is(Items.GOLDEN_HELMET)) { armor += 1; pieces++; }
            if (player.getItemBySlot(EquipmentSlot.CHEST).is(Items.GOLDEN_CHESTPLATE)) { armor += 3; pieces++; }
            if (player.getItemBySlot(EquipmentSlot.LEGS).is(Items.GOLDEN_LEGGINGS)) { armor += 3; pieces++; }
            if (player.getItemBySlot(EquipmentSlot.FEET).is(Items.GOLDEN_BOOTS)) { armor += 2; pieces++; }
        }
        modify(player.getAttribute(Attributes.ARMOR),ARMOR_ID,armor);
        modify(player.getAttribute(Attributes.ARMOR_TOUGHNESS),TOUGHNESS_ID,2*pieces);
    }
    private static void modify(AttributeInstance instance, ResourceLocation id, double amount) {
        if (instance == null) return;
        AttributeModifier old = instance.getModifier(id);
        if (old != null && old.amount() == amount) return;
        if (old != null) instance.removeModifier(id);
        if (amount > 0) instance.addTransientModifier(new AttributeModifier(id,amount,AttributeModifier.Operation.ADD_VALUE));
    }
    public static void clearArmor(ServerPlayer player) {
        modify(player.getAttribute(Attributes.ARMOR),ARMOR_ID,0);
        modify(player.getAttribute(Attributes.ARMOR_TOUGHNESS),TOUGHNESS_ID,0);
    }
    private static boolean validRiteFood(ItemStack food) {
        return food.is(RITE_FOOD) || food.is(Items.GOLDEN_APPLE)
            || food.is(Items.ENCHANTED_GOLDEN_APPLE) || food.is(Items.GOLDEN_CARROT);
    }
    public static void finishedFood(ServerPlayer player, ItemStack food) {
        if (joined(player) || player.isCreative() || player.isSpectator()
            || player.level().dimension() != Level.NETHER || !validRiteFood(food)
            || !player.getItemBySlot(EquipmentSlot.HEAD).is(Items.GOLDEN_HELMET)
            || !player.getItemBySlot(EquipmentSlot.CHEST).is(Items.GOLDEN_CHESTPLATE)
            || !player.getItemBySlot(EquipmentSlot.LEGS).is(Items.GOLDEN_LEGGINGS)
            || !player.getItemBySlot(EquipmentSlot.FEET).is(Items.GOLDEN_BOOTS)) return;
        if (ProgressionApi.join(player,PATH))
            player.displayClientMessage(Component.literal("Gilded initiated: the Nether recognizes your gold."),false);
    }
    private static void foodStart(LivingEntityUseItemEvent.Start event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || !joined(player)
            || event.getItem().getFoodProperties(player) == null) return;
        if (!isAllowedFood(player,event.getItem())) {
            event.setCanceled(true);
            player.displayClientMessage(Component.literal("Gilded can eat only Nether or gold-crafted foods."),true);
        }
    }
    public static boolean isAllowedFood(ServerPlayer player, ItemStack stack) {
        return stack.is(NETHER_FOOD) || isGoldenFood(player,stack);
    }
    public static boolean isGoldenFood(ServerPlayer player, ItemStack stack) {
        if (stack.is(GOLD_FOOD) || stack.is(Items.GOLDEN_APPLE)
            || stack.is(Items.ENCHANTED_GOLDEN_APPLE) || stack.is(Items.GOLDEN_CARROT)) return true;
        if (stack.getFoodProperties(player) == null) return false;
        // Evaluate current server recipes; no stale cache after datapack /reload.
        Set<Item> golden = new HashSet<>();
        golden.add(Items.GOLDEN_APPLE);
        golden.add(Items.ENCHANTED_GOLDEN_APPLE);
        golden.add(Items.GOLDEN_CARROT);
        for (int depth = 0; depth < 8; depth++) {
            boolean updated = false;
            for (RecipeHolder<?> holder : player.server.getRecipeManager().getRecipes()) {
                if (!(holder.value() instanceof CraftingRecipe recipe)) continue;
                ItemStack result = recipe.getResultItem(player.registryAccess());
                if (result.isEmpty() || result.getFoodProperties(player) == null
                    || golden.contains(result.getItem())) continue;
                boolean usesGold = false;
                for (Ingredient ingredient : recipe.getIngredients()) {
                    if (ingredient.test(new ItemStack(Items.GOLD_NUGGET))
                        || ingredient.test(new ItemStack(Items.GOLD_INGOT))
                        || ingredient.test(new ItemStack(Items.GOLD_BLOCK))) { usesGold = true; break; }
                    for (ItemStack possible : ingredient.getItems()) {
                        if (possible.getFoodProperties(player) != null
                            && (possible.is(GOLD_FOOD) || golden.contains(possible.getItem()))) {
                            usesGold = true;
                            break;
                        }
                    }
                    if (usesGold) break;
                }
                if (usesGold) { golden.add(result.getItem()); updated = true; }
            }
            if (!updated) break;
        }
        return golden.contains(stack.getItem());
    }

    public static void oreBroken(BlockDropsEvent event) {
        if (event.isCanceled() || !(event.getBreaker() instanceof ServerPlayer player)
            || !joined(player) || !event.getState().is(GOLD_ORES)
            || hasSilkTouch(event.getTool(),player)) return;
        ProgressionApi.recordExternal(player,"tmm_progression:gilded_gold_ore_broken_without_silk");
    }
    private static boolean hasSilkTouch(ItemStack tool, ServerPlayer player) {
        return tool.getEnchantmentLevel(player.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
            .getHolderOrThrow(Enchantments.SILK_TOUCH)) > 0;
    }
    public static int extraFortune(ServerPlayer player, ItemStack tool, LootContext context) {
        if (!joined(player) || !tool.is(FORTUNE_TOOLS)
            || hasSilkTouch(tool,player)
            || context.getParamOrNull(LootContextParams.THIS_ENTITY) != player) return 0;
        return rank(player,"gilded_fortunes_favor");
    }
    public static int extraLooting(ServerPlayer player, LootContext context) {
        if (!joined(player) || !player.getMainHandItem().is(GOLD_WEAPONS)
            || context.getParamOrNull(LootContextParams.ATTACKING_ENTITY) != player) return 0;
        return rank(player,"gilded_looting");
    }
    public static int extraUnbreaking(ServerPlayer player, ItemStack tool) {
        if (!joined(player) || !tool.is(WEARABLE_GEAR) || rank(player,"gilded_last_gilt") < 1) return 0;
        boolean worn = player.getItemBySlot(EquipmentSlot.HEAD) == tool
            || player.getItemBySlot(EquipmentSlot.CHEST) == tool
            || player.getItemBySlot(EquipmentSlot.LEGS) == tool
            || player.getItemBySlot(EquipmentSlot.FEET) == tool;
        if (player.getMainHandItem() != tool && !worn) return 0;
        return rank(player,"gilded_last_gilt");
    }
}
