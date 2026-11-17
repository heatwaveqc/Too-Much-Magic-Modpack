package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import io.github.heatwaveqc.tmm.tradersvengence.TradersVengence;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.chunk.LevelChunk;

/** Check only existing loaded chunks: never force-load chunks from an entity-join event. */
public final class TraderSpawnRules {
    public static final TagKey<Block> VALID_GROUND = TagKey.create(
        Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(
            TradersVengence.MOD_ID, "valid_trader_ground"));

    private TraderSpawnRules() {}

    public static boolean allowed(ServerLevel level, BlockPos feet) {
        if (!level.hasChunkAt(feet) || !level.hasChunkAt(feet.below())) {
            return false;
        }
        if (!VengeanceRules.nearSeaLevel(feet.getY(), level.getSeaLevel())) {
            return false;
        }
        if (!level.canSeeSky(feet)) {
            return false;
        }
        if (!level.getBlockState(feet.below()).is(VALID_GROUND)) {
            return false;
        }
        return !nearNoSolicitingSign(level, feet);
    }

    private static boolean nearNoSolicitingSign(ServerLevel level, BlockPos pos) {
        int r = VengeanceRules.SIGN_RADIUS;
        for (int cx = (pos.getX() - r) >> 4; cx <= (pos.getX() + r) >> 4; cx++) {
            for (int cz = (pos.getZ() - r) >> 4; cz <= (pos.getZ() + r) >> 4; cz++) {
                LevelChunk chunk = level.getChunkSource().getChunkNow(cx, cz);
                if (chunk == null) {
                    continue;
                }
                for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                    if (blockEntity instanceof SignBlockEntity sign
                        && sign.getBlockPos().distSqr(pos) <= (double) r * r
                        && (hasMessage(sign.getFrontText()) || hasMessage(sign.getBackText()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean hasMessage(SignText text) {
        for (int i = 0; i < 4; i++) {
            if (VengeanceRules.markedSign(text.getMessage(i, false).getString())) {
                return true;
            }
        }
        return false;
    }
}
