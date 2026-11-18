package io.github.heatwaveqc.tmm.tradersvengence.mixin;

import io.github.heatwaveqc.tmm.tradersvengence.runtime.GolemMinigunEvents;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Exact vanilla villager summon call, not the global spawn or command path. */
@Mixin(Villager.class)
public abstract class VillagerGolemSpawnMixin {
    @Redirect(method = "spawnGolemIfNeeded", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/util/SpawnUtil;trySpawnMob(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;IIILnet/minecraft/util/SpawnUtil$Strategy;)Ljava/util/Optional;"))
    private <T extends Mob> Optional<T> tradersVengence$markVillageSpawn(EntityType<T> type,
            MobSpawnType reason, ServerLevel level, BlockPos pos, int tries, int horizontal,
            int vertical, SpawnUtil.Strategy strategy) {
        Optional<T> result = SpawnUtil.trySpawnMob(type, reason, level, pos, tries, horizontal, vertical, strategy);
        if (reason == MobSpawnType.MOB_SUMMONED) {
            result.ifPresent(mob -> {
                if (mob instanceof IronGolem golem) GolemMinigunEvents.markVillagerSpawn(golem);
            });
        }
        return result;
    }
}
