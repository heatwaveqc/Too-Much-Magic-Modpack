package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MahouPracticePayments;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/** Exact descriptors from SHA-256 verified Mahou 1.36.31 ABI audit 44452615910. */
@Pseudo
@Mixin(targets="stepsword.mahoutsukai.mana.PlayerManaManager",remap=false)
public abstract class MahouPracticePaymentMixin {
    private static final String DRAIN="drainMana(Lnet/minecraft/world/entity/player/Player;IZZZZZ)I";
    private static final String CHARGE="chargeCircuit(Lnet/minecraft/world/entity/player/Player;I)I";
    private static final String GEMS="drainManaFromGems(Ljava/util/ArrayList;I)I";
    private static final String BATTERIES="drainManaFromNearbyBatteries(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/Level;Ljava/util/Set;ILjava/util/UUID;)I";

    // Delegate overloads are intentionally not wrapped, so the deepest call counts once.
    @WrapMethod(method=DRAIN,require=0,remap=false)
    private static int tmm$mana(Player player,int amount,boolean a,boolean b,boolean c,boolean d,boolean e,Operation<Integer> original){
        return MahouPracticePayments.practice(player,amount,()->original.call(player,amount,a,b,c,d,e));
    }
    @WrapMethod(method=CHARGE,require=0,remap=false)
    private static int tmm$charge(Player player,int amount,Operation<Integer> original){
        return MahouPracticePayments.practice(player,amount,()->original.call(player,amount));
    }
    @WrapOperation(method={DRAIN,CHARGE},at=@At(value="INVOKE",target="Lstepsword/mahoutsukai/dataattachments/mahou/IMahou;setStoredMana(I)V"),require=0,remap=false)
    private static void tmm$personalDebit(@Coerce Object holder,int value,Operation<Void> original){
        MahouPracticePayments.storedMana(holder,()->original.call(holder,value));
    }
    @WrapMethod(method=GEMS,require=0,remap=false)
    private static int tmm$gemDebit(ArrayList<ItemStack> gems,int amount,Operation<Integer> original){
        return MahouPracticePayments.gems(gems,()->original.call(gems,amount));
    }
    @WrapMethod(method=BATTERIES,require=0,remap=false)
    private static int tmm$circuitScope(BlockPos pos,Level level,Set<BlockEntity> batteries,int amount,UUID caller,Operation<Integer> original){
        return MahouPracticePayments.batteries(caller,()->original.call(pos,level,batteries,amount,caller));
    }
    @WrapOperation(method=BATTERIES,at=@At(value="INVOKE",target="Lstepsword/mahoutsukai/tile/circuits/ManaCircuitTileEntity;setMana(I)V"),require=0,remap=false)
    private static void tmm$circuitDebit(@Coerce Object tile,int value,Operation<Void> original){
        MahouPracticePayments.circuit(tile,()->original.call(tile,value));
    }
}
