package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.AnsThriftQuotes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

/** Transform the new immutable quote once, before the native Plan captures it. */
@Pseudo
@Mixin(targets="com.otectus.arsnspells.casting.ArsCastPayments",remap=false)
public abstract class AnsArsThriftMixin {
    @Coerce
    @ModifyExpressionValue(method="quoteEvent",at=@At(value="INVOKE",target="Lcom/otectus/arsnspells/casting/QuoteService;quote(Lnet/minecraft/world/entity/player/Player;Lcom/otectus/arsnspells/contract/ResourceUnit;DLcom/otectus/arsnspells/contract/CostRules;Lcom/otectus/arsnspells/contract/CarrierPolicy;)Lcom/otectus/arsnspells/contract/CostQuote;"),require=0,allow=1,remap=false)
    private static Object tmm$arsQuote(@Coerce Object quote,@Coerce Object event){
        return AnsThriftQuotes.ars(quote,event);
    }
}
