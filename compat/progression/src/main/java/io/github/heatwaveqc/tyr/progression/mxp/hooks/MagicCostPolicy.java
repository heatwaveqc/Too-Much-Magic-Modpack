package io.github.heatwaveqc.tyr.progression.mxp.hooks;

import io.github.heatwaveqc.tyr.progression.runtime.MagicSkillRuntime;
import io.github.heatwaveqc.tyr.progression.foundation.MagicSkillMath;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

/** Discount a positive, already quoted expenditure; keep original nominal price for MXP. */
public final class MagicCostPolicy {
    private MagicCostPolicy(){}
    public static int discounted(int original,double fraction){
        return (int)MagicSkillMath.reduce(original,fraction,.95);
    }
    public static int quote(ServerPlayer player,String source,int price){
        // Managed ANS quotes alone own shared pricing; never discount again at native debit.
        if(!source.equals("psi:cad_cast")&&ModList.get().isLoaded("ars_n_spells"))return price;
        return discounted(price,rate(player,source));
    }

    /** Evaluates one originating school; no other school is added for shared mana. */
    public static double rate(ServerPlayer player,String source){
        return MagicSkillRuntime.contribution(player,"magic_cost_discount",source,.95);
    }
}
