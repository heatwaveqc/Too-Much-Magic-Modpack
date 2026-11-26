package io.github.heatwaveqc.tyr.progression.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.heatwaveqc.tyr.progression.runtime.NativeHarvest;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;

/** Original loot computation only; no second loot roll or inventory mutation. */
@Mixin(Block.class)
public abstract class VirtualToolLootMixin {
    @WrapMethod(method="getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;")
    private static List<ItemStack> tmm$virtualTool(BlockState state,ServerLevel level,BlockPos pos,
            BlockEntity blockEntity,Entity breaker,ItemStack tool,Operation<List<ItemStack>> original){
        var lootState=io.github.heatwaveqc.tyr.progression.runtime.OtherworldHarvest.revealIndirectLog(state,breaker,tool);
        return original.call(lootState,level,pos,blockEntity,breaker,
            blockEntity==null?NativeHarvest.lootTool(state,level,pos,breaker,tool):tool);
    }
}
