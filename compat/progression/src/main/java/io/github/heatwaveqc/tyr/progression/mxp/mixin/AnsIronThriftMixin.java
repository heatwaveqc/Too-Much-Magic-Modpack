package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.AnsThriftQuotes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

/** Initial and native final-event repricing, never payment or effect hooks. */
@Pseudo
@Mixin(targets="com.otectus.arsnspells.casting.IronsCastPayments",remap=false)
public abstract class AnsIronThriftMixin {
    private static final String QUOTE="Lcom/otectus/arsnspells/contract/StandardQuotePolicy;quote(Lcom/otectus/arsnspells/contract/ResourceAmount;Lcom/otectus/arsnspells/contract/CostRules;Lcom/otectus/arsnspells/contract/CarrierPolicy;DD)Lcom/otectus/arsnspells/contract/CostQuote;";

    @Coerce
    @ModifyExpressionValue(method="quote",at=@At(value="INVOKE",target=QUOTE),require=0,allow=1,remap=false)
    private static Object tmm$ironQuote(@Coerce Object quote,Player player){
        return AnsThriftQuotes.ironInitial(quote,player);
    }
    @Coerce
    @ModifyExpressionValue(method="price",at=@At(value="INVOKE",target=QUOTE),require=0,allow=1,remap=false)
    private static Object tmm$ironReprice(@Coerce Object quote,Player player){
        return AnsThriftQuotes.ironReprice(quote,player);
    }
}
