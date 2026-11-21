package io.github.heatwaveqc.tmm.economy;

import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

/** Shared EvilCraft spirit generation and machine policy; fail closed until tags resolve. */
public final class EvilCraftReplicationPolicy {
    private static final TagKey<EntityType<?>> BLACKLIST = TagKey.create(
            Registries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath("tmm", "replication_blacklist"));
    private static final ResourceLocation SAFE_EMPTY_SPIRIT =
            ResourceLocation.fromNamespaceAndPath("evilcraft", "vengeance_spirit");

    private EvilCraftReplicationPolicy() {}

    public static boolean blocked(EntityType<?> type) {
        return type == null
                || BuiltInRegistries.ENTITY_TYPE.getTag(BLACKLIST).isEmpty()
                || type.is(BLACKLIST);
    }

    /** Select only allowed monsters; a self-spirit sentinel produces no farmable inner mob. */
    public static EntityType<?> randomAllowedMonster(RandomSource random) {
        List<EntityType<?>> allowed = BuiltInRegistries.ENTITY_TYPE.stream()
                .filter(type -> type.getCategory() == MobCategory.MONSTER)
                .filter(type -> !blocked(type))
                .toList();
        if (!allowed.isEmpty()) {
            return allowed.get(random.nextInt(allowed.size()));
        }
        return BuiltInRegistries.ENTITY_TYPE.get(SAFE_EMPTY_SPIRIT);
    }
}
