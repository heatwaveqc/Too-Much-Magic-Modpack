package io.github.heatwaveqc.tmm.economy;

import java.util.Iterator;
import java.util.Map;
import dev.shadowsoffire.apotheosis.Apoth;
import dev.shadowsoffire.apotheosis.item.PotionCharmItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingInput;

/**
 * Small bridge for the unchanged Apotheosis shaped recipe: three vanilla
 * potion bottles plus blaze powder. Does not touch any other potion carriers,
 * any native potion ticks, or the ProjectE Repair Talisman integration.
 */
public final class ApotheosisCharmPotionBridge {
    private static final TagKey<MobEffect> CUSTOM_EFFECT_ALLOWLIST = TagKey.create(
            Registries.MOB_EFFECT,
            ResourceLocation.fromNamespaceAndPath("tmm", "potion_charm_custom_effect_allowlist"));
    private static final TagKey<MobEffect> EFFECT_DENYLIST = TagKey.create(
            Registries.MOB_EFFECT,
            ResourceLocation.fromNamespaceAndPath("tmm", "potion_charm_denied"));

    private ApotheosisCharmPotionBridge() {}

    /**
     * Intercept only custom PotionContents/unsafe payload cases and denied
     * effects. Leave plain registered-base recipes on their native path.
     */
    public static Selection inspect(CraftingInput input) {
        PotionContents found = null;
        boolean special = false;
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
            if (contents == null) continue;

            // This is not a general carrier/dose converter. Reject an altered
            // or modded carrier rather than discard its non-PotionContents data.
            if (stack.getItem() != Items.POTION || hasForeignComponents(stack)) {
                return Selection.reject();
            }
            if (found != null && !found.equals(contents)) {
                return Selection.reject();
            }
            if (!contents.customEffects().isEmpty() || contents.customColor().isPresent()) {
                special = true;
            }
            if (!permits(contents)) {
                return Selection.reject();
            }
            found = contents;
        }

        if (!special) return Selection.nativePath();
        return found == null ? Selection.reject() : Selection.accept(found);
    }

    private static boolean hasForeignComponents(ItemStack stack) {
        for (Map.Entry<DataComponentType<?>, java.util.Optional<?>> component :
                stack.getComponentsPatch().entrySet()) {
            if (component.getKey() != DataComponents.POTION_CONTENTS) return true;
        }
        return false;
    }

    private static boolean permits(PotionContents contents) {
        Iterator<MobEffectInstance> effects = contents.getAllEffects().iterator();
        if (!effects.hasNext()) return false;
        MobEffectInstance effect = effects.next();
        if (effects.hasNext()) return false;

        boolean customOnly = contents.potion().isEmpty()
                || contents.potion().get().equals(Potions.WATER);
        boolean nativeAllowed = customOnly
                ? contents.potion().map(holder -> !holder.is(Apoth.Tags.POTION_CHARM_BLACKLIST)).orElse(true)
                        && !contents.customEffects().isEmpty()
                : PotionCharmItem.isValidPotion(contents.potion().get());
        ResourceLocation effectId = BuiltInRegistries.MOB_EFFECT.getKey(effect.getEffect().value());
        if (effectId == null) return false;
        // Native temporary flight stays valid; infinitely reusable charm flight
        // does not. These backstops also survive an edited/removed denylist tag.
        boolean denied = effect.getEffect().is(EFFECT_DENYLIST)
                || "neovitae:flight".equals(effectId.toString())
                || "minecraft:bad_omen".equals(effectId.toString())
                || "minecraft:raid_omen".equals(effectId.toString())
                || "minecraft:trial_omen".equals(effectId.toString())
                || (customOnly && CharmPotionEligibility.privilegedAmplifier(
                        effectId.toString(), effect.getAmplifier()));
        boolean ordinary = "minecraft".equals(effectId.getNamespace())
                || effect.getEffect().is(CUSTOM_EFFECT_ALLOWLIST);
        return CharmPotionEligibility.permits(
                1, effect.getEffect().value().isInstantenous(), nativeAllowed,
                customOnly, ordinary, denied, true, true);
    }

    public record Selection(boolean overrideNative, PotionContents contents) {
        private static Selection nativePath() {
            return new Selection(false, PotionContents.EMPTY);
        }
        private static Selection reject() {
            return new Selection(true, PotionContents.EMPTY);
        }
        private static Selection accept(PotionContents contents) {
            return new Selection(true, contents);
        }
    }
}
