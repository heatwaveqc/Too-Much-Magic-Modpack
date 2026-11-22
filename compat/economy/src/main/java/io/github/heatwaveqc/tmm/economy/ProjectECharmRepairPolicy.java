package io.github.heatwaveqc.tmm.economy;

import java.util.Iterator;
import moze_intel.projecte.api.capabilities.PECapabilities;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;

/** Narrow ProjectE-only exception for Apotheosis potion charms.
 *
 * <p>This does not change {@link ItemStack#isRepairable()}, so anvils, Mending,
 * crafting repair, and unrelated repair systems continue to reject charms.</p>
 */
public final class ProjectECharmRepairPolicy {
    private static final ResourceLocation POTION_CHARM =
            ResourceLocation.fromNamespaceAndPath("apotheosis", "potion_charm");

    private ProjectECharmRepairPolicy() {
    }

    public static boolean permits(ItemStack stack) {
        if (stack.isEmpty()
                || !POTION_CHARM.equals(BuiltInRegistries.ITEM.getKey(stack.getItem()))
                || stack.getCapability(PECapabilities.MODE_CHANGER_ITEM_CAPABILITY) != null) {
            return false;
        }

        PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        Iterator<MobEffectInstance> effects = contents.getAllEffects().iterator();
        if (!effects.hasNext()) {
            return false;
        }
        MobEffectInstance onlyEffect = effects.next();
        return permits(true, stack.isDamageableItem(), stack.getDamageValue(),
                !effects.hasNext(), onlyEffect.getEffect().value().isInstantenous(), false);
    }

    static boolean permits(boolean potionCharm, boolean damageable, int damage,
            boolean exactlyOneEffect, boolean instantEffect, boolean modeChanger) {
        return potionCharm && damageable && damage > 0 && exactlyOneEffect
                && !instantEffect && !modeChanger;
    }
}
