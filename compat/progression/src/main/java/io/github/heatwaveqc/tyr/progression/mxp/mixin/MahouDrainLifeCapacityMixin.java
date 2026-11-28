package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MahouResourceSkills;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import net.minecraft.world.entity.player.Player;
@Pseudo
@Mixin(targets="stepsword.mahoutsukai.tile.boundary.DrainLifeBoundaryMahoujinTileEntity",remap=false)
public abstract class MahouDrainLifeCapacityMixin {
    @WrapOperation(method="giveLife(Lnet/minecraft/world/entity/player/Player;)V",at=@At(value="INVOKE",target="Lstepsword/mahoutsukai/dataattachments/mahou/IMahou;getMaxMana()I"),require=0,remap=false)
    private int tyr$cap(@Coerce Object holder,Operation<Integer> original,Player player){
        return MahouResourceSkills.capacity(player,original.call(holder));
    }
}
