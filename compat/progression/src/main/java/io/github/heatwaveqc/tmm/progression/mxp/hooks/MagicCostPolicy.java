package io.github.heatwaveqc.tmm.progression.mxp.hooks;

import io.github.heatwaveqc.tmm.progression.mxp.MxpMod;
import io.github.heatwaveqc.tmm.progression.runtime.EffectRuntime;
import io.github.heatwaveqc.tmm.progression.runtime.NewbieMode;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

/** Discount a positive, already quoted expenditure; keep original nominal price for MXP. */
public final class MagicCostPolicy {
    private MagicCostPolicy(){}
    public static int discounted(int original,double fraction){
        if(original<=0||!Double.isFinite(fraction)||fraction<=0)return original;
        return Math.max(1,(int)Math.ceil(original*(1-Math.min(.95,fraction))));
    }
    public static int quote(ServerPlayer player,String source,int price){
        // Existing legacy Thrift and MXP remain authoritative until cutover.
        if(price<=0||!MxpMod.active()||ModList.get().isLoaded("tyzs_skills")||player.isCreative()||player.isSpectator()||
            NewbieMode.isNewbie(player))return price;
        // ANS reserves funds earlier and may settle several legs. Native getter changes would risk duplicate discounts.
        if(!source.equals("psi:cad_cast")&&ModList.get().isLoaded("ars_n_spells"))return price;
        double rate=0;
        for(var active:EffectRuntime.active(player))
            if(active.effect().type().equals("magic_cost_discount")&&active.effect().selector().equals(source))
                rate+=active.effect().amountPerRank()*active.rank();
        return discounted(price,rate);
    }
}
