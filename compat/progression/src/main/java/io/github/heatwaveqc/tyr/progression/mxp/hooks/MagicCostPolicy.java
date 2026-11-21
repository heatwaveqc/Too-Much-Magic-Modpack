package io.github.heatwaveqc.tyr.progression.mxp.hooks;

import io.github.heatwaveqc.tyr.progression.runtime.EffectRuntime;
import io.github.heatwaveqc.tyr.progression.runtime.NewbieMode;
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
        // Managed ANS quotes alone own shared pricing; never discount again at native debit.
        if(!source.equals("psi:cad_cast")&&ModList.get().isLoaded("ars_n_spells"))return price;
        return discounted(price,rate(player,source));
    }

    /** Evaluates one originating school; no other school is added for shared mana. */
    public static double rate(ServerPlayer player,String source){
        if(!MxpPlayerPolicy.eligible(player)||NewbieMode.isNewbie(player))return 0;
        double rate=0;
        for(var active:EffectRuntime.active(player))
            if(active.effect().type().equals("magic_cost_discount")&&active.effect().selector().equals(source))
                rate+=active.effect().amountPerRank()*active.rank();
        return Double.isFinite(rate)?Math.max(0,Math.min(.95,rate)):0;
    }
}
