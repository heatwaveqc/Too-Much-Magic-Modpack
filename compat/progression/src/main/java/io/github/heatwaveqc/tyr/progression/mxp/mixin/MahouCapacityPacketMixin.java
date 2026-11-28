package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MahouResourceSkills;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.Unique;

/** Server snapshots only the two resource fields; no protocol extension or learned-max mutation. */
@Pseudo
@Mixin(targets="stepsword.mahoutsukai.networking.PlayerMahouPacket",remap=false)
public abstract class MahouCapacityPacketMixin implements MahouResourceSkills.CapacityPacket {
    @Unique private boolean tyr$hasCapacitySnapshot;
    @Unique private int tyr$storedSnapshot;
    @Unique private int tyr$maximumSnapshot;
    @Override public void tyr$capacitySnapshot(int stored,int max){
        tyr$storedSnapshot=stored;tyr$maximumSnapshot=max;tyr$hasCapacitySnapshot=true;
    }
    @WrapOperation(method="encode(Lio/netty/buffer/ByteBuf;)V",at=@At(value="INVOKE",target="Lstepsword/mahoutsukai/dataattachments/mahou/IMahou;getStoredMana()I"),require=0,remap=false)
    private int tyr$stored(@Coerce Object holder,Operation<Integer> original){
        return tyr$hasCapacitySnapshot?tyr$storedSnapshot:original.call(holder);
    }
    @WrapOperation(method="encode(Lio/netty/buffer/ByteBuf;)V",at=@At(value="INVOKE",target="Lstepsword/mahoutsukai/dataattachments/mahou/IMahou;getMaxMana()I"),require=0,remap=false)
    private int tyr$maximum(@Coerce Object holder,Operation<Integer> original){
        return tyr$hasCapacitySnapshot?tyr$maximumSnapshot:original.call(holder);
    }
}
