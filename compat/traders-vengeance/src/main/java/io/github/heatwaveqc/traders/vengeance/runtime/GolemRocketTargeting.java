package io.github.heatwaveqc.traders.vengeance.runtime;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

/**
 * Rocket-only exclusion zones. Not a claim/POI system: the bound comes from
 * the actual generated structure start of each village in the target chunk.
 * Minigun and vanilla melee retain their existing targeting policy.
 */
public final class GolemRocketTargeting {
    static final double MIN_DISTANCE_SQUARED = 100.0D;

    private GolemRocketTargeting() {}

    static boolean farEnough(double distanceSquared) {
        return GolemRocketGeometry.farEnough(distanceSquared);
    }

    static boolean withinFootprint(BoundingBox bounds, int x, int z) {
        return GolemRocketGeometry.insideRectangle(
            bounds.minX(), bounds.maxX(), bounds.minZ(), bounds.maxZ(), x, z);
    }

    /** Called at firing time, not per tick during idle goal selection. */
    public static boolean mayLaunch(ServerLevel level, IronGolem golem, LivingEntity target) {
        if (!farEnough(golem.distanceToSqr(target))) return false;
        if (!level.getEntitiesOfClass(Villager.class, target.getBoundingBox().inflate(10.0D),
                villager -> villager.isAlive()
                    && !farEnough(villager.distanceToSqr(target))).isEmpty()) {
            return false;
        }
        return !inGeneratedVillage(level, target.blockPosition());
    }

    static boolean inGeneratedVillage(ServerLevel level, BlockPos target) {
        // StructureManager consults the target chunk's stored structure references;
        // unlike locateNearestMapStructure this does not search or generate a region.
        if (!level.hasChunkAt(target)) return true; // fail closed for unknown chunks
        var registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        return level.structureManager().startsForStructure(new ChunkPos(target),
                structure -> registry.getHolder(registry.getId(structure))
                    .map(holder -> holder.is(StructureTags.VILLAGE)).orElse(false))
            .stream().anyMatch(start -> start.isValid()
                && withinFootprint(start.getBoundingBox(), target.getX(), target.getZ()));
    }
}
