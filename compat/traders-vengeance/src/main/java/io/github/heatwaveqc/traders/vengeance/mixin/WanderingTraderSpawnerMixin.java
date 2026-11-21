package io.github.heatwaveqc.traders.vengeance.mixin;

import io.github.heatwaveqc.traders.vengeance.runtime.TraderSpawnRules;
import io.github.heatwaveqc.traders.vengeance.runtime.VengeanceRules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Filter only the vanilla WanderingTraderSpawner's trader candidate.
 *
 * Do not filter EntityJoinLevelEvent: that also fires for /summon, spawn eggs,
 * custom scripted traders, and entities loaded/teleported by other mods.
 * Llama position selection is a different caller and remains vanilla.
 * The native search already checks up to ten candidate columns per call.
 */
@Mixin(WanderingTraderSpawner.class)
public abstract class WanderingTraderSpawnerMixin {
    @Shadow @Nullable
    private BlockPos findSpawnPositionNear(LevelReader level, BlockPos center, int radius) {
        throw new AssertionError("shadow");
    }

    @Redirect(
        method = "spawn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/npc/WanderingTraderSpawner;findSpawnPositionNear(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/core/BlockPos;"
        ),
        require = 1
    )
    private BlockPos tradersVengeance$findLegalNaturalTraderPos(
            WanderingTraderSpawner spawner, LevelReader level, BlockPos origin, int range) {
        if (!(level instanceof ServerLevel server)) return null;
        // One rare, vanilla-scheduled spawn attempt: no player/entity tick scan
        // and no extra attempts for llamas or manually summoned traders.
        for (int attempt = 0; attempt < VengeanceRules.TRADER_POSITION_GROUPS; attempt++) {
            BlockPos candidate = findSpawnPositionNear(level, origin, range);
            if (candidate == null) return null;
            if (TraderSpawnRules.allowed(server, candidate)) return candidate;
        }
        return null;
    }
}
