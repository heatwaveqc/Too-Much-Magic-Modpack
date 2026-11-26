package io.github.heatwaveqc.tyr.progression.runtime;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

/** Only modifies the native harvest-state input, not the world or the generated loot. */
public final class OtherworldHarvest {
    private OtherworldHarvest(){}
    /** Only the natural log classes already covered by the native Occultism adapter. */
    public static BlockState revealIndirectLog(BlockState state,net.minecraft.world.entity.Entity breaker,ItemStack tool){
        if(!(breaker instanceof Player player))return state;
        String type=state.getBlock().getClass().getName();
        if(!type.equals("com.klikli_dev.occultism.common.block.otherworld.OtherworldLogNaturalBlock")
            &&!type.equals("com.klikli_dev.occultism.common.block.otherworld.OtherworldStrippedLogNaturalBlock"))return state;
        return revealForHarvest(state,player,tool);
    }
    public static BlockState revealForHarvest(BlockState state,Player player,ItemStack tool){
        if(!(player instanceof ServerPlayer owner)||owner.isCreative()||owner.isSpectator()
            ||owner.getServer()==null||owner.getServer().getPlayerList().getPlayer(owner.getUUID())!=owner)
            return state;
        int sight=VisionRuntime.otherworldTier(owner);
        if(sight<1)return state;
        // This adapter is invoked only on the native Otherworld classes listed in the optional mixin.
        boolean ore=Selectors.block("occultism:iesnium_ore_natural",state);
        if(ore&&(sight<2||!tool.isEmpty()||!owner.getMainHandItem().isEmpty()
            ||!HarvestAbilities.canHarvestBarehand(owner,state)
            ||EffectRuntime.active(owner).stream().noneMatch(a->a.effect().type().equals("otherworld_harvest")
                &&a.effect().tier()==2&&Selectors.block(a.effect().selector(),state))))return state;
        for(var property:state.getProperties())
            if(property instanceof BooleanProperty flag&&flag.getName().equals("uncovered"))
                return state.setValue(flag,true);
        return state;
    }
}
