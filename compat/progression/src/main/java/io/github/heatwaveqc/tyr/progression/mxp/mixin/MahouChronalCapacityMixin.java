package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MahouResourceSkills;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
@Pseudo
@Mixin(targets="stepsword.mahoutsukai.tile.exchange.ChronalExchangeMahoujinTileEntity",remap=false)
public abstract class MahouChronalCapacityMixin {
    @WrapOperation(method="doManaGenOrLoss()V",at=@At(value="INVOKE",target="Lstepsword/mahoutsukai/dataattachments/mahou/IMahou;getMaxMana()I"),require=0,remap=false)
    private int tyr$cap(@Coerce Object holder,Operation<Integer> original){
        return MahouResourceSkills.ownerCapacity(this,original.call(holder));
    }
}
