package io.github.heatwaveqc.audit.mixin;
import io.github.heatwaveqc.audit.Placement;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureStart.class)
public abstract class StructureStartMixin {
    @Inject(method="placeInChunk", at=@At("HEAD"))
    private void tmm$begin(WorldGenLevel level, StructureManager manager, ChunkGenerator generator,
                           RandomSource random, BoundingBox box, ChunkPos target, CallbackInfo ci) {
        StructureStart self=(StructureStart)(Object)this;
        if (!(level instanceof WorldGenRegion region) || !region.getLevel().dimension().equals(Level.OVERWORLD)) return;
        var id=level.registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(self.getStructure());
        if(id==null) throw new IllegalStateException("Unregistered audit structure");
        Placement.begin(id.toString(),self.getChunkPos(),target,region,generator);
    }
    @Inject(method="placeInChunk", at=@At("RETURN"))
    private void tmm$end(WorldGenLevel level, StructureManager manager, ChunkGenerator generator,
                         RandomSource random, BoundingBox box, ChunkPos target, CallbackInfo ci) {
        if (level instanceof WorldGenRegion region && region.getLevel().dimension().equals(Level.OVERWORLD)) Placement.end();
    }
}
