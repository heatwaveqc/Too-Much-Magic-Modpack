package io.github.heatwaveqc.tyr.progression.mixin;

import io.github.heatwaveqc.tyr.progression.runtime.blood.HexereiCauldronBloodTransfer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Exact Hexerei 0.5.0.3 1.21.1 class target; no hard Hexerei binary dependency.
 *
 * Two *distinct* native entry points:
 *  1. Pipes/capabilities call IFluidHandler.fill directly.
 *  2. Held item fluid handlers call interactWithFluid, whose native implementation
 *     bypasses fill and assigns this.fluidStack from the source ItemStack.
 *
 * Do not turn this into a global NeoForge blood capability override.
 * Missing pinned-version method targets do not crash optional mod setups;
 * source ABI and actual bytecode injections require explicit Type A/B review.
 */
@Pseudo
@Mixin(targets = "net.joefoxe.hexerei.tileentity.MixingCauldronTile", remap = false)
public abstract class HexereiOrdinaryBloodInputMixin {
    @ModifyVariable(
            method = "fill(Lnet/neoforged/neoforge/fluids/FluidStack;Lnet/neoforged/neoforge/fluids/capability/IFluidHandler$FluidAction;)I",
            at = @At("HEAD"), argsOnly = true, ordinal = 0,
            require = 0, remap = false)
    private FluidStack tmm$normalizeBloodFromPipes(FluidStack source) {
        return HexereiCauldronBloodTransfer.forCauldronFill(source);
    }

    @Inject(
            method = "interactWithFluid(Lnet/neoforged/neoforge/fluids/capability/IFluidHandlerItem;)Z",
            at = @At("HEAD"), cancellable = true, require = 0, remap = false)
    private void tmm$normalizeBloodFromHeldItem(
            IFluidHandlerItem item, CallbackInfoReturnable<Boolean> cir) {
        if (item.getTanks() != 1) return;
        FluidStack source = item.getFluidInTank(0);
        if (!HexereiCauldronBloodTransfer.isConvertible(source)) return;
        BlockEntity tile = (BlockEntity) (Object) this;
        if (tile.getLevel() == null) return;
        IFluidHandler cauldron = (IFluidHandler) (Object) this;
        // Always consume the native method for *recognized* ordinary blood:
        // on transfer failure its original path could store raw evilcraft:blood.
        cir.setReturnValue(HexereiCauldronBloodTransfer.fromHeldContainer(
                cauldron, item, !tile.getLevel().isClientSide()));
    }
}
