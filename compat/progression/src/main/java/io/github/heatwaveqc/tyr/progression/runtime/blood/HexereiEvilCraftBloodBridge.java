package io.github.heatwaveqc.tyr.progression.runtime.blood;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

/**
 * Exact two-mod right-click compatibility for #508. Hexerei stores native
 * hexerei:blood_fluid, while EvilCraft's Blood Extractor accepts only
 * evilcraft:blood. Native generic FluidUtil therefore cannot fill the extractor
 * from that cauldron, even though the TMM bucket-emptying recipe works.
 *
 * Hook before Hexerei's useItemOn and its generic item-handler fallback.
 * A strict item+block+source-fluid guard avoids any global fluid rewrites.
 * The item handler is opened on a COPY: a failed transaction never replaces
 * the real held item. Server-side source draining happens only after both
 * handlers accept the proposed operation in SIMULATE.
 *
 * Avoid class linkage to either optional mod: both are looked up by registry ID
 * and vanilla/NeoForge public fluid handler APIs.
 */
public final class HexereiEvilCraftBloodBridge {
    private static final ResourceLocation HEXEREI_CAULDRON =
            ResourceLocation.fromNamespaceAndPath("hexerei", "mixing_cauldron");
    private static final ResourceLocation EVILCRAFT_EXTRACTOR =
            ResourceLocation.fromNamespaceAndPath("evilcraft", "blood_extractor");
    private static final ResourceLocation HEXEREI_BLOOD =
            ResourceLocation.fromNamespaceAndPath("hexerei", "blood_fluid");
    private static final ResourceLocation EVILCRAFT_BLOOD =
            ResourceLocation.fromNamespaceAndPath("evilcraft", "blood");

    private HexereiEvilCraftBloodBridge() {}

    public static void register() {
        if (ModList.get().isLoaded("hexerei") && ModList.get().isLoaded("evilcraft")) {
            NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, HexereiEvilCraftBloodBridge::rightClick);
        }
    }

    private static void rightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCanceled()) return;
        Player player = event.getEntity();
        if (player.isSpectator() || player.getAbilities().instabuild) return;
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        if (!HEXEREI_CAULDRON.equals(BuiltInRegistries.BLOCK.getKey(level.getBlockState(pos).getBlock())))
            return;

        ItemStack held = event.getItemStack();
        // The native extractor is unstackable; never process cloned stacks,
        // another EvilCraft container, or a special data-bearing blood bottle.
        if (held.isEmpty() || held.getCount() != 1
                || !EVILCRAFT_EXTRACTOR.equals(BuiltInRegistries.ITEM.getKey(held.getItem())))
            return;

        BlockEntity blockEntity = level.getBlockEntity(pos);
        // Pinned Hexerei MixingCauldronTile directly implements IFluidHandler.
        if (!(blockEntity instanceof IFluidHandler cauldron) || cauldron.getTanks() != 1)
            return;
        FluidStack source = cauldron.getFluidInTank(0);
        if (source.isEmpty() || !HEXEREI_BLOOD.equals(BuiltInRegistries.FLUID.getKey(source.getFluid())))
            return;
        // Do not silently strip native/custom fluid components.
        if (!FluidStack.isSameFluidSameComponents(source, new FluidStack(source.getFluid(), 1)))
            return;

        Fluid evilBlood = BuiltInRegistries.FLUID.getOptional(EVILCRAFT_BLOOD).orElse(null);
        if (evilBlood == null) return;
        IFluidHandlerItem extractor = FluidUtil.getFluidHandler(held.copy()).orElse(null);
        if (extractor == null || extractor.getTanks() != 1) return;

        FluidStack requested = new FluidStack(evilBlood, source.getAmount());
        int accepts = extractor.fill(requested, IFluidHandler.FluidAction.SIMULATE);
        int move = BloodCauldronTransferPolicy.transferable(source.getAmount(), accepts);
        if (move == 0) return;

        // Double-check the source against the precise amount before claiming
        // the interaction. This also prevents refilling from a phantom tank.
        FluidStack probe = cauldron.drain(new FluidStack(source.getFluid(), move),
                IFluidHandler.FluidAction.SIMULATE);
        if (probe.getAmount() != move || !FluidStack.isSameFluidSameComponents(probe, source))
            return;

        // The client only predicts a successful click; it never changes the
        // cauldron or player inventory. Server-owned state is authoritative.
        if (!level.isClientSide()) {
            FluidStack drained = cauldron.drain(new FluidStack(source.getFluid(), move),
                    IFluidHandler.FluidAction.EXECUTE);
            if (drained.getAmount() != move || !FluidStack.isSameFluidSameComponents(drained, source)) {
                if (!drained.isEmpty()) restore(cauldron, drained, "source execution differs from simulation");
                return;
            }
            int filled = extractor.fill(new FluidStack(evilBlood, move),
                    IFluidHandler.FluidAction.EXECUTE);
            if (filled != move) {
                // The copy is discarded, not applied to the held extractor.
                // Restore all of the source even if the destination misbehaved.
                restore(cauldron, drained, "destination execution differs from simulation");
                return;
            }
            player.setItemInHand(event.getHand(), extractor.getContainer());
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 0.7f, 1.0f);
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    private static void restore(IFluidHandler cauldron, FluidStack amount, String reason) {
        int restored = cauldron.fill(amount, IFluidHandler.FluidAction.EXECUTE);
        if (restored != amount.getAmount()) {
            ProgressionMod.LOGGER.error(
                "Hexerei/EvilCraft blood transaction rollback FAILED after {}: expected {} mB, restored {} mB",
                reason, amount.getAmount(), restored);
        } else {
            ProgressionMod.LOGGER.warn("Hexerei/EvilCraft blood transaction rolled back: {}", reason);
        }
    }
}
