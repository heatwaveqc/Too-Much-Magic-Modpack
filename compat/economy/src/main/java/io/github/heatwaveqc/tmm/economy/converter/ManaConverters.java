package io.github.heatwaveqc.tmm.economy.converter;

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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

public final class ManaConverters {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, "tmm_economy");
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, "tmm_economy");
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "tmm_economy");
    public static final DeferredHolder<Block, CatalystBlock> CATALYST = BLOCKS.register("emc_catalyst",
            () -> new CatalystBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE)
                    .strength(4.0F, 12.0F).requiresCorrectToolForDrops().pushReaction(PushReaction.BLOCK)));
    public static final DeferredHolder<Item, BlockItem> ITEM = ITEMS.register("emc_catalyst",
            () -> new BlockItem(CATALYST.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CatalystTile>> TILE =
            TILES.register("emc_catalyst", () -> BlockEntityType.Builder.of(CatalystTile::new, CATALYST.get()).build(null));
    private ManaConverters() {}

    public static void register(IEventBus bus) {
        BLOCKS.register(bus); ITEMS.register(bus); TILES.register(bus);
    }

    public static final class CatalystBlock extends Block implements EntityBlock {
        CatalystBlock(Properties properties) { super(properties); }
        @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new CatalystTile(pos, state);
        }
        @Override public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
            super.setPlacedBy(level, pos, state, placer, stack);
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof CatalystTile tile)
                tile.assignOwner(placer instanceof ServerPlayer player ? player.getUUID() : null);
        }
        @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
            return level.isClientSide || type != TILE.get() ? null : (l, p, s, be) -> {
                if (be instanceof CatalystTile tile) tile.tick();
            };
        }
    }

    public static final class CatalystTile extends BlockEntity {
        private UUID owner;
        CatalystTile(BlockPos pos, BlockState state) { super(TILE.get(), pos, state); }
        void assignOwner(UUID owner) { this.owner = owner; setChanged(); }
        @Override protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
            super.saveAdditional(tag, registries);
            if (owner != null) tag.putUUID("tmm_owner", owner);
        }
        @Override protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
            super.loadAdditional(tag, registries);
            owner = tag.hasUUID("tmm_owner") ? tag.getUUID("tmm_owner") : null;
        }
        void tick() {
            if (!(level instanceof ServerLevel server) || isRemoved()
                    || server.getBlockEntity(worldPosition) != this || owner == null
                    || server.hasNeighborSignal(worldPosition)) return;
            BlockPos above = worldPosition.above();
            if (!server.isLoaded(above) || !(server.getBlockEntity(above) instanceof ManaPoolBlockEntity pool)
                    || pool.isRemoved()) return;
            AccountRefill.tick(server, owner, pool.getMaxMana(), pool::getCurrentMana, pool::receiveMana, 20, 1);
        }
    }
}
