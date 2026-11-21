/*
 * Adapted from Creators-of-Create/Create, revision 37ea4afcfe5bf85875d4b0749798a45330070a1f.
 * src/main/java/com/simibubi/create/foundation/events/ClientEvents.java
 * Original license: MIT; see third_party_licenses/Create.txt in source and JAR.
 * Modifications: Too Much Magic contributors, 2026.
 */
package io.github.heatwaveqc.tyr.progression.foundation.client;

import io.github.heatwaveqc.tyr.progression.foundation.VisionMath;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.event.ViewportEvent;

/** Client-only unregistered hook. No subscriber, no automatic vision grants.
 * Integrator must call only with current authoritative ability state.
 */
public final class LavaFogHook {
    private LavaFogHook() {}

    public static void apply(ViewportEvent.RenderFog event, boolean eligible, float range) {
        if (!eligible) return;
        Camera camera = event.getCamera();
        Level level = Minecraft.getInstance().level;
        if (level == null || camera.getEntity() == null || camera.getEntity().isSpectator()) return;
        BlockPos blockPos = camera.getBlockPosition();
        FluidState fluidState = level.getFluidState(blockPos);
        if (!fluidState.is(FluidTags.LAVA) ||
            camera.getPosition().y >= blockPos.getY() + fluidState.getHeight(level, blockPos)) return;
        VisionMath.Fog fog = VisionMath.lavaFog(range);
        event.setNearPlaneDistance(fog.near());
        event.setFarPlaneDistance(fog.far());
        event.setCanceled(true);
    }
}
