package io.github.heatwaveqc.tmm.economy;

import java.util.Iterator;
import java.util.Map;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.CraftingInput;

/** Preserve the complete contents of three matching ordinary potion bottles;
 * no effect-specific policy. Native matches still checks recipe geometry. */
public final class ApotheosisCharmPotionBridge {
    private ApotheosisCharmPotionBridge() {}

    public static PotionContents inspect(CraftingInput input) {
        PotionContents found = null;
        int bottles = 0;
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
            if (contents == null) {
                if (stack.getItem() == Items.POTION) return PotionContents.EMPTY;
                continue;
            }
            // Never convert a different carrier or discard unrelated stack data.
            if (stack.getItem() != Items.POTION || hasForeignComponents(stack)
                    || !permits(contents)) return PotionContents.EMPTY;
            if (found != null && !found.equals(contents)) return PotionContents.EMPTY;
            found = contents;
            bottles++;
        }
        return CharmPotionEligibility.matchingBottles(bottles) && found != null
                ? found : PotionContents.EMPTY;
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
        return CharmPotionEligibility.permits(effects.hasNext() ? 2 : 1,
                effect.getEffect().value().isInstantenous(),
                BuiltInRegistries.MOB_EFFECT.getKey(effect.getEffect().value()) != null);
    }
}
