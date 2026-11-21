package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.NativePaidSources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Pseudo
@Mixin(targets="at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv",remap=false)
public abstract class HexInventoryPaymentMixin {
    @WrapOperation(method="extractMediaFromInventory",at=@At(value="INVOKE",target="Lat/petrak/hexcasting/api/utils/MediaHelper;extractMedia(Lat/petrak/hexcasting/api/addldata/ADMediaHolder;JZZ)J"),require=0,remap=false)
    private long tmm$paid(@Coerce Object holder,long cost,boolean batteries,boolean simulate,Operation<Long> original){
        double before=NativePaidSources.Hex.before(holder);
        long result=original.call(holder,cost,batteries,simulate);
        NativePaidSources.Hex.removed(holder,cost,result,before,simulate);
        return result;
    }
}
