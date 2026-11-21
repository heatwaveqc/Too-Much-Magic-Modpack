package io.github.heatwaveqc.tmm.progression.mxp.hooks;

import io.github.heatwaveqc.tmm.progression.mxp.core.ForgeRitualProof;
import io.github.heatwaveqc.tmm.progression.mxp.core.RitualRewards;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.ModList;

/** A pending native FINISH receipt, consumed only after the forge's own slot-commit callback. */
public record ForgeRitualCompletion(RitualRewards.Receipt receipt,ItemStack expected,int beforeTier) {
    public interface Provider { ForgeRitualCompletion tmm$takeForgeReceipt(); }
    public static boolean supported(){
        return ModList.get().getModContainerById("forbidden_arcanus").map(c->"2.6.1".equals(c.getModInfo().getVersion().toString())).orElse(false);
    }
    public static int tier(Object manager){
        Object n=MxpReflect.field(manager,"forgeTier");
        return n instanceof Integer i?i:-1;
    }
    public static boolean sameStack(ItemStack a,ItemStack b) {
        return a!=null&&b!=null&&a.getCount()==b.getCount()&&
            ((a.isEmpty()&&b.isEmpty())||(!a.isEmpty()&&!b.isEmpty()&&ItemStack.isSameItemSameComponents(a,b)));
    }
    public void installed(Object forge,Object manager,ItemStack nativeResult){
        if(!supported()||!(forge instanceof BlockEntity block)||!(block.getLevel() instanceof ServerLevel level)
            ||!level.getServer().isSameThread()||level.getBlockEntity(block.getBlockPos())!=block)return;
        Object slot=MxpReflect.field(forge.getClass(),"MAIN_SLOT");
        if(!(slot instanceof Integer number)||!(MxpReflect.call(forge,"getStack",number) instanceof ItemStack installed)
            ||!sameStack(expected,nativeResult)||!sameStack(expected,installed))return;
        String id=receipt.recipeId();
        boolean actual=ForgeRitualProof.upgradeTarget(id)>=0?
            ForgeRitualProof.actualUpgrade(id,beforeTier,tier(manager)):
            ForgeRitualProof.itemResult(id,BuiltInRegistries.ITEM.getKey(installed.getItem()).toString(),installed.getCount());
        if(actual)RitualSettlement.settle(level.getServer().getPlayerList().getPlayer(receipt.actor()),receipt);
    }
}
