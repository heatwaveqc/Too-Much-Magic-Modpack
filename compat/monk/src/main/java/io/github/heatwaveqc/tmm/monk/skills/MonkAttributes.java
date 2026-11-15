package io.github.heatwaveqc.tmm.monk.skills;

import io.github.heatwaveqc.tmm.monk.MonkMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Monk-only investment targets used by the primary Tyz backend. Tyz modifies
 * these inert attributes; this mod translates their values into real benefits
 * ONLY while Monk faction, skill and equipment restrictions are satisfied.
 */
public final class MonkAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(Registries.ATTRIBUTE, MonkMod.MOD_ID);

    public static final DeferredHolder<Attribute, Attribute> UNARMED_TRAINING =
            ATTRIBUTES.register("unarmed_training",
                    () -> new RangedAttribute("attribute.tmm_monk.unarmed_training", 0, 0, 64).setSyncable(true));

    public static final DeferredHolder<Attribute, Attribute> HAND_HARVEST_TRAINING =
            ATTRIBUTES.register("hand_harvest_training",
                    () -> new RangedAttribute("attribute.tmm_monk.hand_harvest_training", 0, 0, 64).setSyncable(true));

    private MonkAttributes() { }

    public static void addToPlayer(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, UNARMED_TRAINING);
        event.add(EntityType.PLAYER, HAND_HARVEST_TRAINING);
    }
}
