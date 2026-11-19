package io.github.heatwaveqc.tmm.economy.converter;

import com.hollingsworth.arsnouveau.common.block.SourceJar;
import com.hollingsworth.arsnouveau.common.block.tile.SourceJarTile;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class SourceConverters {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "tmm_economy");
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, "tmm_economy");
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "tmm_economy");
    public static final DeferredHolder<Block, EmcJarBlock> JAR = BLOCKS.register("emc_source_jar",
            () -> new EmcJarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE)
                    .strength(3.0F, 8.0F).noOcclusion().pushReaction(PushReaction.BLOCK)));
    public static final DeferredHolder<Item, BlockItem> ITEM = ITEMS.register("emc_source_jar",
            () -> new BlockItem(JAR.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EmcJarTile>> TILE =
            TILES.register("emc_source_jar", () -> BlockEntityType.Builder.of(EmcJarTile::new, JAR.get()).build(null));
    private SourceConverters() {}

    public static void register(IEventBus bus) {
        BLOCKS.register(bus); ITEMS.register(bus); TILES.register(bus);
        bus.addListener(SourceConverters::registerCapability);
    }
    private static void registerCapability(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(CapabilityRegistry.SOURCE_CAPABILITY, TILE.get(), (tile, side) -> tile.getSourceStorage());
    }

    public static final class EmcJarBlock extends SourceJar {
        EmcJarBlock(Properties properties) { super(properties, "emc_source_jar"); }
        @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new EmcJarTile(pos, state);
        }
        @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
            // Avoid SourceJar's explicit scheduling of the *ordinary* jar block.
            return defaultBlockState().setValue(BlockStateProperties.WATERLOGGED,
                    context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER)
                    .setValue(SourceJar.fill, 0);
        }
        @Override public PushReaction getPistonPushReaction(BlockState state) { return PushReaction.BLOCK; }
        @Override public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
            super.setPlacedBy(level, pos, state, placer, stack);
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof EmcJarTile tile) {
                // A newly placed block always starts empty and belongs only to
                // its actual placer, never to item/NBT-supplied ownership.
                tile.assignOwner(placer instanceof ServerPlayer player ? player.getUUID() : null);
                tile.setSource(0);
            }
        }
        @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
            return level.isClientSide || type != TILE.get() ? null : (l, p, s, be) -> {
                if (be instanceof EmcJarTile tile) tile.refill();
            };
        }
    }

    /** Native SourceJarTile subclass keeps SourceUtil instanceof discovery,
     * relay behavior, 10,000 capacity, comparator and Source capability. */
    public static final class EmcJarTile extends SourceJarTile {
        private UUID owner;
        EmcJarTile(BlockPos pos, BlockState state) { super(TILE.get(), pos, state); }
        void assignOwner(UUID owner) { this.owner = owner; setChanged(); }
        @Override protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
            super.saveAdditional(tag, registries);
            if (owner != null) tag.putUUID("tmm_owner", owner);
        }
        @Override protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
            super.loadAdditional(tag, registries);
            owner = tag.hasUUID("tmm_owner") ? tag.getUUID("tmm_owner") : null;
        }
        void refill() {
            if (!(level instanceof ServerLevel server) || isRemoved()
                    || server.getBlockEntity(worldPosition) != this || owner == null
                    || server.hasNeighborSignal(worldPosition)) return;
            AccountRefill.tick(server, owner, getMaxSource(), this::getSource,
                    amount -> addSource(amount, false), 4, 3);
        }
    }
}
