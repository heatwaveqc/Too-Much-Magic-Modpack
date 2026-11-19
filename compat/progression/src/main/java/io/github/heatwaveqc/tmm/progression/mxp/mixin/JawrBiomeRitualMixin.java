package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.brigadier.context.CommandContext;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.JawrRitualOutput;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import java.util.BitSet;
@Pseudo
@Mixin(targets="dev.sterner.witchery.core.registry.WitcheryCommands",remap=false)
public abstract class JawrBiomeRitualMixin {
    @WrapOperation(method="executeChangeBiome(Lcom/mojang/brigadier/context/CommandContext;)I",at=@At(value="INVOKE",target="Lnet/minecraft/world/level/chunk/ChunkAccess;fillBiomesFromNoise(Lnet/minecraft/world/level/biome/BiomeResolver;Lnet/minecraft/world/level/biome/Climate$Sampler;)V"),require=0,remap=false)
    private void tmm$biome(ChunkAccess chunk,BiomeResolver resolver,Climate.Sampler sampler,Operation<Void> original,CommandContext<CommandSourceStack> context){
        ServerLevel level=context.getSource().getLevel();
        ResourceKey<?> target=JawrRitualOutput.biomeExpected(level);
        if(target==null){original.call(chunk,resolver,sampler);return;}
        int x0=QuartPos.fromBlock(chunk.getPos().getMinBlockX()),z0=QuartPos.fromBlock(chunk.getPos().getMinBlockZ());
        int y0=QuartPos.fromBlock(chunk.getMinBuildHeight()),end=QuartPos.fromBlock(chunk.getMaxBuildHeight()-1)+1;
        BitSet different=new BitSet();
        int i=0;
        for(int y=y0;y<end;y++)for(int x=x0;x<x0+4;x++)for(int z=z0;z<z0+4;z++,i++)
            if(chunk.getNoiseBiome(x,y,z).unwrapKey().filter(target::equals).isEmpty())different.set(i);
        original.call(chunk,resolver,sampler);
        i=0;
        for(int y=y0;y<end;y++)for(int x=x0;x<x0+4;x++)for(int z=z0;z<z0+4;z++,i++)
            if(different.get(i)&&chunk.getNoiseBiome(x,y,z).unwrapKey().filter(target::equals).isPresent()){
                JawrRitualOutput.biomeChanged(level,target,true);return;
            }
    }
}
