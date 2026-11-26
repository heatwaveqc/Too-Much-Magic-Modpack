package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.foundation.ClothingPolicy;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.ItemEnchantments;

/** Shared clothing entitlement API. Datapack item tags are the server policy:
 * non_protective_clothing is an explicit allowlist (IDs or nested tags);
 * clothing_blacklist wins. Unknown or modified equipment fails closed.
 * No optional mod class links and no client-only dependencies. */
public final class ClothingApi {
    private ClothingApi(){}
    public static final TagKey<Item> CLOTHING=TagKey.create(Registries.ITEM,
        ResourceLocation.fromNamespaceAndPath("tyr_progression","non_protective_clothing"));
    public static final TagKey<Item> BLACKLIST=TagKey.create(Registries.ITEM,
        ResourceLocation.fromNamespaceAndPath("tyr_progression","clothing_blacklist"));

    /** Empty equipment is not clothing. Call isUnarmored for the equipment predicate. */
    public static boolean isNonProtective(ItemStack stack){
        if(stack.isEmpty()||!stack.is(CLOTHING)||stack.is(BLACKLIST))return false;
        // Conservative: any nonzero stack attribute modifier is incompatible with
        // this cosmetic allowlist, including offensive or movement modifiers.
        // Weighted training's reversible entity modifier is not an item modifier.
        var modifiers=stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS,ItemAttributeModifiers.EMPTY);
        boolean modified=modifiers.modifiers().stream().anyMatch(entry->entry.modifier().amount()!=0);
        boolean enchanted=stack.isEnchanted()||
            !stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS,ItemEnchantments.EMPTY).isEmpty();
        boolean augmented=stack.has(DataComponents.TRIM)||stack.getComponents().keySet().stream()
            .map(BuiltInRegistries.DATA_COMPONENT_TYPE::getKey)
            .filter(java.util.Objects::nonNull).map(ResourceLocation::toString)
            .anyMatch(ClothingPolicy::augmentationComponent);
        return ClothingPolicy.allows(true,false,stack.getMaxDamage()>0,enchanted,modified,augmented);
    }
    /** Only standard armor slots are inspected, exactly as the former unarmored rules.
     * Empty-hand, Curios and explicitly empty-slot requirements remain separate. */
    public static boolean isUnarmored(LivingEntity entity){
        for(ItemStack worn:entity.getArmorSlots())if(!worn.isEmpty()&&!isNonProtective(worn))return false;
        return true;
    }
}
