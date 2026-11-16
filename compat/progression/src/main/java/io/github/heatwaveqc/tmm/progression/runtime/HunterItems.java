package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.ProgressionMod;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Both items are Hunter-owned; no third-party ability mod required. */
public final class HunterItems {
    private HunterItems() {}

    public static final DeferredRegister.Items ITEMS =
        DeferredRegister.createItems(ProgressionMod.MOD_ID);
    public static final DeferredItem<Item> BIO_MUTAGEN =
        ITEMS.registerItem("bio_mutagen",Item::new,new Item.Properties().stacksTo(64));
    public static final DeferredItem<MutagenicBrewItem> MUTAGENIC_BREW =
        ITEMS.registerItem("mutagenic_brew",MutagenicBrewItem::new,new Item.Properties().stacksTo(1));
}
