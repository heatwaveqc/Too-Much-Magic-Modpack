package io.github.heatwaveqc.tmm.transformations.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Pinned JAWR 0.5.39 private method verified by javap. */
@Pseudo
@Mixin(targets = "dev.sterner.witchery.feature.affliction.vampire.VampireEvents", remap = false)
public interface JAWRVampireSunAccessor {
    @Invoker(value = "decreaseSunTick", remap = false)
    void tmm$decreaseSunTick(ServerPlayer player);
}
