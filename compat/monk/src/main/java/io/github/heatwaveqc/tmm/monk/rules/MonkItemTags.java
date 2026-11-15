package io.github.heatwaveqc.tmm.monk.rules;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class MonkItemTags {
    public static final TagKey<Item> CONVENTIONAL_WEAPONS = TagKey.create(
            Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tmm_monk", "conventional_weapons"));

    private MonkItemTags() { }
}
