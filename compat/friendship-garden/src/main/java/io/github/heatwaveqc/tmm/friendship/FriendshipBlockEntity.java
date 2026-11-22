package io.github.heatwaveqc.tmm.friendship;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/** One persisted lower-block identity; presentation sync is server event-driven. */
public final class FriendshipBlockEntity extends BlockEntity {
    private FriendshipFlower flower;
    private FriendshipFlower.Appearance appearance = FriendshipFlower.Appearance.BUDDING;

    public FriendshipBlockEntity(BlockPos pos, BlockState state) {
        super(FriendshipGardenMod.FLOWER_ENTITY.get(), pos, state);
    }

    public FriendshipFlower flower() { return flower; }
    public FriendshipFlower.Appearance appearance() { return appearance; }

    public void bind(FriendshipFlower flower) {
        if (this.flower != null || flower == null) return;
        this.flower = flower;
        if (level instanceof ServerLevel server) {
            FriendshipRuntime.track(this);
            updateFromServer(server, false);
        }
        setChanged();
    }

    public void updateFromServer(ServerLevel server, boolean petals) {
        if (flower == null) return;
        var player = server.getServer().getPlayerList().getPlayer(flower.crafterId());
        long now = server.getServer().overworld().getGameTime();
        var next = flower.appearance(player != null, now);
        boolean changed = appearance != next;
        appearance = next;
        if (changed) {
            setChanged();
            server.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
        if (petals) {
            server.sendParticles(net.minecraft.core.particles.ParticleTypes.CHERRY_LEAVES,
                    worldPosition.getX() + 0.5, worldPosition.getY() + 0.7,
                    worldPosition.getZ() + 0.5, 12, .28, .35, .28, .04);
        }
    }

    public void onCrafterDeath(ServerLevel server, long now, boolean permanent) {
        if (flower == null) return;
        flower = flower.onCrafterDeath(now, permanent);
        updateFromServer(server, true);
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (flower != null) {
            CompoundTag bound = new CompoundTag();
            FriendshipData.encode(bound, flower);
            tag.put("flower", bound);
        }
        tag.putString("appearance", appearance.name());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        flower = FriendshipData.decode(tag.getCompound("flower"));
        try {
            appearance = FriendshipFlower.Appearance.valueOf(tag.getString("appearance"));
        } catch (IllegalArgumentException ex) {
            appearance = FriendshipFlower.Appearance.BUDDING;
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level instanceof ServerLevel server) {
            FriendshipRuntime.track(this);
            updateFromServer(server, false);
        }
    }

    @Override
    public void setRemoved() {
        FriendshipRuntime.untrack(this);
        super.setRemoved();
    }

    @Override
    public void onChunkUnloaded() {
        FriendshipRuntime.untrack(this);
        super.onChunkUnloaded();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
