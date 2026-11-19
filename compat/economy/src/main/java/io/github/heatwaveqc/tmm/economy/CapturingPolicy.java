package io.github.heatwaveqc.tmm.economy;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

/** One source of truth: the effective Apothic Spawners entity type tag.
 * Never maintain a second boss list or modify native Capturing. */
final class CapturingPolicy {
    static final TagKey<EntityType<?>> BLACKLIST = TagKey.create(Registries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath("apothic_spawners", "blacklisted_from_spawners"));
    static boolean resolved() { return BuiltInRegistries.ENTITY_TYPE.getTag(BLACKLIST).isPresent(); }
    static boolean blocked(EntityType<?> type) { return !resolved() || type.is(BLACKLIST); }
    static boolean blocked(ResourceLocation id) {
        return BuiltInRegistries.ENTITY_TYPE.getOptional(id).map(CapturingPolicy::blocked).orElse(true);
    }
    private CapturingPolicy() {}
}
