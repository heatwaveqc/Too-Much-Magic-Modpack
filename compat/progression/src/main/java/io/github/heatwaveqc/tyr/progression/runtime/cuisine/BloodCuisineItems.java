package io.github.heatwaveqc.tyr.progression.runtime.cuisine;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Two small first-party foods; Blood Wine is native Brewin' and Chewin' red wine. */
public final class BloodCuisineItems {
    private BloodCuisineItems() {}
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(ProgressionMod.MOD_ID);
    public static final DeferredItem<BloodCuisineFoodItem> BLOOD_SAUSAGE =
            ITEMS.registerItem("blood_sausage",
                    props -> new BloodCuisineFoodItem(BloodCuisinePolicy.Product.SAUSAGE, props),
                    new Item.Properties().stacksTo(16).food(new FoodProperties.Builder()
                            .nutrition(8).saturationModifier(0.9f).alwaysEdible().build()));
    public static final DeferredItem<BloodCuisineFoodItem> BLOOD_PIE_SLICE =
            ITEMS.registerItem("blood_pie_slice",
                    props -> new BloodCuisineFoodItem(BloodCuisinePolicy.Product.PIE, props),
                    new Item.Properties().stacksTo(16).food(new FoodProperties.Builder()
                            .nutrition(5).saturationModifier(0.6f).alwaysEdible().build()));
}
