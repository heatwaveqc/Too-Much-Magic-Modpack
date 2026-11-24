package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import io.github.heatwaveqc.tmm.tradersvengence.TradersVengence;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/** NPC-owned projectile, not an item or a separately installed rocket mod. */
public final class GolemRocketTypes {
    private static final DeferredRegister<EntityType<?>> TYPES =
        DeferredRegister.create(Registries.ENTITY_TYPE, TradersVengence.MOD_ID);

    public static final Supplier<EntityType<GolemRocket>> ROCKET =
        TYPES.register("golem_rocket", () ->
            EntityType.Builder.<GolemRocket>of(GolemRocket::new, MobCategory.MISC)
                .sized(1.0F, 1.0F).clientTrackingRange(8).updateInterval(1)
                .build("traders_vengence:golem_rocket"));

    private GolemRocketTypes() {}
    public static void register(IEventBus bus) { TYPES.register(bus); }
}
