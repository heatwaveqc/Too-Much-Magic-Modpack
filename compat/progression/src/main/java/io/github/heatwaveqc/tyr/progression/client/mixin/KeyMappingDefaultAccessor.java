package io.github.heatwaveqc.tyr.progression.client.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Change the Controls > Reset target as well as a fresh player's current
 * binding. NeoForge stores the default modifier separately from the key.
 */
@Mixin(KeyMapping.class)
public interface KeyMappingDefaultAccessor {
    @Accessor("defaultKey")
    @Mutable
    void tmm$setDefaultKey(InputConstants.Key key);

    @Accessor("keyModifierDefault")
    @Mutable
    void tmm$setDefaultKeyModifier(KeyModifier modifier);
}
