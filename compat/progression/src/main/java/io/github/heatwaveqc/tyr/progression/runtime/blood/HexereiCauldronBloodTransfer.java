package io.github.heatwaveqc.tyr.progression.runtime.blood;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

/**
 * Source-boundary-only ordinary blood normalization for Hexerei (#557).
 *
 * External liquids remain canonical evilcraft:blood (or their own upstream
 * physical blood) while the mixing cauldron's ONLY internal ordinary blood
 * identity is hexerei:blood_fluid. This intentionally never modifies native
 * EV/Anima, JAWR blood components or Forbidden & Arcanus typed essence.
 *
 * All native game objects are accessed through public NeoForge APIs. The
 * optional Hexerei mixin contains the only reference to its class by name.
 */
public final class HexereiCauldronBloodTransfer {
    private static final ResourceLocation HEX_BLOOD =
            ResourceLocation.fromNamespaceAndPath("hexerei", "blood_fluid");

    private HexereiCauldronBloodTransfer() {}

    private static String fluidId(FluidStack fluid) {
        return BuiltInRegistries.FLUID.getKey(fluid.getFluid()).toString();
    }

    /** A nonempty, component-free, explicitly allowed ordinary input fluid. */
    public static boolean isConvertible(FluidStack fluid) {
        return !fluid.isEmpty()
                && HexereiBloodInputPolicy.needsInternalConversion(fluidId(fluid))
                && FluidStack.isSameFluidSameComponents(
                        fluid, new FluidStack(fluid.getFluid(), fluid.getAmount()));
    }

    /** Used at the public cauldron IFluidHandler.fill boundary (pipes/pumps). */
    public static FluidStack forCauldronFill(FluidStack source) {
        if (!isConvertible(source)) return source;
        return BuiltInRegistries.FLUID.getOptional(HEX_BLOOD)
                .map(fluid -> new FluidStack(fluid, source.getAmount()))
                .orElse(source);
    }

    /**
     * Called only for a held IFluidHandlerItem containing recognized ordinary
     * blood. Hexerei's native interactWithFluid directly assigns its private
     * FluidStack on this route, so changing fill(...) alone is insufficient.
     *
     * Hexerei's caller gives us a COPY of the held item handler. A failed
     * transaction therefore must never publish the altered item container.
     * The client only simulates; the authoritative server modifies fluids.
     *
     * False means do not claim a successful transfer, and the mixin cancels
     * the native implementation to prevent a raw alternate-blood tank write.
     */
    public static boolean fromHeldContainer(
            IFluidHandler cauldron, IFluidHandlerItem item, boolean execute) {
        if (cauldron.getTanks() != 1 || item.getTanks() != 1) return false;
        FluidStack source = item.getFluidInTank(0);
        if (!isConvertible(source)) return false;

        FluidStack stored = cauldron.getFluidInTank(0);
        if (!HexereiBloodInputPolicy.mayMix(
                stored.isEmpty() ? "" : fluidId(stored), stored.getAmount())) return false;
        int requested = HexereiBloodInputPolicy.availableTransfer(
                source.getAmount(), stored.getAmount(), cauldron.getTankCapacity(0));
        if (requested <= 0) return false;

        // Never drain an unknown item through drain(int): the typed request
        // protects exact source identity and associated components.
        FluidStack request = source.copyWithAmount(requested);
        FluidStack simulatedDrain = item.drain(request, IFluidHandler.FluidAction.SIMULATE);
        if (simulatedDrain.isEmpty()
                || !FluidStack.isSameFluidSameComponents(simulatedDrain, source)
                || simulatedDrain.getAmount() > requested) return false;
        int candidate = simulatedDrain.getAmount();

        FluidStack hexBlood = forCauldronFill(source.copyWithAmount(candidate));
        if (fluidId(hexBlood).equals(fluidId(source))) return false; // registry missing
        int acceptable = cauldron.fill(hexBlood, IFluidHandler.FluidAction.SIMULATE);
        int amount = Math.min(candidate, acceptable);
        if (amount <= 0) return false;
        FluidStack matchingSource = source.copyWithAmount(amount);
        FluidStack probe = item.drain(matchingSource, IFluidHandler.FluidAction.SIMULATE);
        if (probe.getAmount() != amount
                || !FluidStack.isSameFluidSameComponents(probe, source)) return false;
        FluidStack matchingHex = hexBlood.copyWithAmount(amount);
        if (cauldron.fill(matchingHex, IFluidHandler.FluidAction.SIMULATE) != amount)
            return false;

        if (!execute) return true;

        // Execute the item side first; its stack is a private copy made by
        // the Hexerei block interaction, and will only be published on SUCCESS.
        FluidStack actuallyDrained = item.drain(
                matchingSource, IFluidHandler.FluidAction.EXECUTE);
        if (actuallyDrained.getAmount() != amount
                || !FluidStack.isSameFluidSameComponents(actuallyDrained, source)) {
            ProgressionMod.LOGGER.warn(
                    "Hexerei blood import: item execute disagreed with simulate ({} vs {} mB); item copy will not be committed",
                    actuallyDrained.getAmount(), amount);
            return false;
        }

        int filled = cauldron.fill(matchingHex, IFluidHandler.FluidAction.EXECUTE);
        if (filled != amount) {
            // Source item was only a copy. Roll back any fluid inserted so
            // the real held item remains unchanged without duplicating blood.
            if (filled > 0) {
                FluidStack recovered = cauldron.drain(
                        matchingHex.copyWithAmount(filled), IFluidHandler.FluidAction.EXECUTE);
                if (recovered.getAmount() != filled ||
                        !FluidStack.isSameFluidSameComponents(recovered, matchingHex)) {
                    ProgressionMod.LOGGER.error(
                            "Hexerei blood import ROLLBACK FAILED: inserted {} mB, recovered {} mB",
                            filled, recovered.getAmount());
                }
            }
            ProgressionMod.LOGGER.warn(
                    "Hexerei blood import: cauldron execute disagreed with simulate ({} vs {} mB); item copy will not be committed",
                    filled, amount);
            return false;
        }
        return true;
    }
}
