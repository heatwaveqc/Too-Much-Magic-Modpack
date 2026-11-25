package io.github.heatwaveqc.tyr.progression.mixin;

import io.github.heatwaveqc.tyr.progression.runtime.OtherworldHarvest;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Optional Occultism 1.227.0 native playerDestroy adapter. Native loot table remains authoritative. */
@Pseudo
@Mixin(targets={
    "com.klikli_dev.occultism.common.block.otherworld.IesniumOreNaturalBlock",
    "com.klikli_dev.occultism.common.block.otherworld.OtherworldLogNaturalBlock",
    "com.klikli_dev.occultism.common.block.otherworld.OtherworldLeavesNaturalBlock",
    "com.klikli_dev.occultism.common.block.otherworld.OtherworldSaplingNaturalBlock",
    "com.klikli_dev.occultism.common.block.otherworld.OtherworldStrippedLogNaturalBlock"
},remap=false)
public abstract class OccultismOtherworldHarvestMixin {
    @ModifyVariable(method="playerDestroy",at=@At("HEAD"),argsOnly=true,ordinal=0,require=0,remap=false)
    private BlockState tmm$innateHarvest(BlockState value,Level level,Player player,BlockPos pos,
                                         BlockState state,BlockEntity entity,ItemStack tool){
        return level.isClientSide?value:OtherworldHarvest.revealForHarvest(value,player,tool);
    }
}
