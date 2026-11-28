package io.github.heatwaveqc.tmm.economy.compat;

import com.Polarice3.Goety.common.blocks.entities.ModBlockEntities;
import com.Polarice3.Goety.common.blocks.entities.SoulAbsorberBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;

/** Goety 3.2.3 exposes a native sided container but no item-handler capability. */
public final class GoetySoulAbsorberCapability {
    private GoetySoulAbsorberCapability() {}

    public static void register(IEventBus bus) {
        bus.addListener(GoetySoulAbsorberCapability::registerCapabilities);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.SOUL_ABSORBER.get(),
                Handler::new);
    }

    private static final class Handler implements IItemHandler {
        private final SoulAbsorberBlockEntity absorber;
        private final Direction side;

        private Handler(SoulAbsorberBlockEntity absorber, Direction side) {
            this.absorber = absorber;
            this.side = side;
        }

        // A cached capability must not write into an unloaded or replaced block entity.
        private boolean live() {
            Level level = absorber.getLevel();
            return level != null && !level.isClientSide && !absorber.isRemoved()
                    && level.hasChunkAt(absorber.getBlockPos())
                    && level.getBlockEntity(absorber.getBlockPos()) == absorber;
        }

        @Override
        public int getSlots() { return 1; }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return slot == 0 && live() ? absorber.getItem(0).copy() : ItemStack.EMPTY;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (stack.isEmpty()) return ItemStack.EMPTY;
            int accepted = SingleItemInsertion.accepted(slot, stack.getCount(), simulate,
                    () -> live() && absorber.canPlaceItemThroughFace(0, stack, side),
                    () -> absorber.getRecipes(stack)
                            .map(recipe -> absorber.placeItem(stack.copyWithCount(1), recipe.getCookingTime()))
                            .orElse(false));
            if (accepted == 0) return stack;
            return stack.getCount() == 1 ? ItemStack.EMPTY : stack.copyWithCount(stack.getCount() - 1);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            // Native WorldlyContainer forbids extraction on every face, including unsided access.
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) { return slot == 0 ? 1 : 0; }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            // Recipe validity is independent of transient occupancy/owner availability.
            return slot == 0 && !stack.isEmpty() && live() && absorber.getRecipes(stack).isPresent();
        }
    }
}
