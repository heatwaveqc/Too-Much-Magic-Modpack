package io.github.heatwaveqc.tmm.friendship;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.network.chat.Component;

/** Decorative, non-colliding, optional two-block-tall BE-rendered plant. */
public final class FriendshipBlock extends Block implements EntityBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    private static final VoxelShape SHORT_SHAPE = Block.box(4, 0, 4, 12, 12, 12);
    private static final VoxelShape TALL_SHAPE = Block.box(4, 0, 4, 12, 16, 12);
    private final boolean tall;

    public FriendshipBlock(boolean tall) {
        super(BlockBehaviour.Properties.of().noCollission().noOcclusion().instabreak()
                .sound(SoundType.GRASS).noLootTable());
        this.tall = tall;
        registerDefaultState(stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER));
    }

    public boolean tall() { return tall; }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        if (tall && (!context.getLevel().getBlockState(pos.above()).canBeReplaced(context)
                || pos.getY() >= context.getLevel().getMaxBuildHeight() - 1)) return null;
        return defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (tall && state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockState below = level.getBlockState(pos.below());
            return below.is(this) && below.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState adjacent,
                                  LevelAccessor level, BlockPos pos, BlockPos adjacentPos) {
        return canSurvive(state, level, pos) ? state
                : Blocks.AIR.defaultBlockState();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return tall && state.getValue(HALF) == DoubleBlockHalf.UPPER
                ? null : new FriendshipBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                            LivingEntity placer, ItemStack stack) {
        FriendshipFlower flower = FriendshipData.bound(stack);
        if (flower == null) return;
        if (level.getBlockEntity(pos) instanceof FriendshipBlockEntity entity) {
            entity.bind(flower);
        }
        if (tall && state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            level.setBlock(pos.above(), defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), 3);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos,
                         BlockState replacement, boolean moving) {
        if (!state.is(replacement.getBlock())
                && (!tall || state.getValue(HALF) == DoubleBlockHalf.LOWER)
                && !level.isClientSide()
                && level.getBlockEntity(pos) instanceof FriendshipBlockEntity entity
                && entity.flower() != null) {
            ItemStack drop = new ItemStack(tall ? FriendshipGardenMod.TALL_ITEM.get()
                                              : FriendshipGardenMod.SHORT_ITEM.get());
            FriendshipData.applyBound(drop, entity.flower());
            popResource(level, pos, drop);
        }
        super.onRemove(state, level, pos, replacement, moving);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                            Player player, BlockHitResult hit) {
        BlockPos source = tall && state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
        if (level.getBlockEntity(source) instanceof FriendshipBlockEntity entity
                && entity.flower() != null) {
            if (!level.isClientSide()) {
                FriendshipFlower flower = entity.flower();
                player.displayClientMessage(Component.translatable("message.tmm_friendship_garden.inspect",
                        flower.crafterDisplayName(), flower.originalFlowerId()), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        return InteractionResult.PASS;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) { return RenderShape.INVISIBLE; }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level,
                               BlockPos pos, CollisionContext context) {
        return tall ? TALL_SHAPE : SHORT_SHAPE;
    }
}
