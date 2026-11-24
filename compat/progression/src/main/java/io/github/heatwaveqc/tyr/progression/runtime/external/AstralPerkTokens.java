package io.github.heatwaveqc.tyr.progression.runtime.external;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.ModList;

/**
 * Source-validated optional Astral Sorcery 1.21.1 native API only.
 * Never mutate PlayerPerkData, set perk XP, or raise the native level cap.
 * Provider is queried on the logical server and its save/sync-capable
 * ResearchHelper.givePerkPointToken does the actual grant.
 */
public final class AstralPerkTokens {
    private static final String RESEARCH = "hellfirepvp.astralsorcery.common.research.";
    private AstralPerkTokens() {}

    public record Snapshot(boolean ready, Set<ResourceLocation> tokens,
                           int nativeLevel, int configuredCap) {
        static Snapshot unavailable() { return new Snapshot(false, Set.of(), -1, -1); }
        public boolean mastered() {
            return ExternalRewardPolicy.nativeMastery(ready, nativeLevel, configuredCap);
        }
        public boolean has(String id) {
            ResourceLocation key=ResourceLocation.tryParse(id);
            return key!=null && tokens.contains(key);
        }
    }

    private static boolean present() {
        return ModList.get().getModContainerById("astralsorcery")
                .filter(mod -> mod.getModInfo().getVersion().toString().startsWith("2.0.3"))
                .isPresent();
    }

    /**
     * A distinct native attunement is the actual perk-tree unlock prerequisite.
     * Blank progress after a research wipe is deliberately NOT ready.
     */
    public static Snapshot snapshot(ServerPlayer player) {
        if (!present()) return Snapshot.unavailable();
        try {
            Class<?> manager = Class.forName(RESEARCH + "ResearchManager");
            Method getProgress = manager.getMethod("getProgress",
                    net.minecraft.world.entity.player.Player.class, LogicalSide.class);
            Object progress = getProgress.invoke(null, player, LogicalSide.SERVER);
            if (progress == null || !Boolean.TRUE.equals(progress.getClass()
                    .getMethod("isValid").invoke(progress))) return Snapshot.unavailable();
            Object attunement = progress.getClass().getMethod("getAttunedConstellation").invoke(progress);
            if (!(attunement instanceof Optional<?> optional) || optional.isEmpty())
                return Snapshot.unavailable();
            Object perkData = progress.getClass().getMethod("getPerkData").invoke(progress);
            Object result = perkData.getClass().getMethod("getPointTokens").invoke(perkData);
            if (!(result instanceof Set<?> raw)) return Snapshot.unavailable();
            var tokens = new java.util.HashSet<ResourceLocation>();
            for (Object token : raw) {
                if (!(token instanceof ResourceLocation key)) return Snapshot.unavailable();
                tokens.add(key);
            }
            // A changed/missing level ABI must lock NEW purchases, not
            // invalidate the independent attunement/token restoration contract.
            int level = -1, cap = -1;
            try {
                Object nativeLevel = perkData.getClass().getMethod("getPerkLevel",
                        net.minecraft.world.entity.player.Player.class, LogicalSide.class)
                        .invoke(perkData, player, LogicalSide.SERVER);
                Class<?> configType = Class.forName(
                        "hellfirepvp.astralsorcery.common.config.server.PerkConfig");
                Object config = configType.getField("CONFIG").get(null);
                Object capValue = configType.getField("perkLevelCap").get(config);
                if (nativeLevel instanceof Integer nativeInt
                        && capValue instanceof net.neoforged.neoforge.common.ModConfigSpec.IntValue nativeCap) {
                    level = nativeInt;
                    cap = nativeCap.getAsInt();
                }
            } catch (ReflectiveOperationException | LinkageError | RuntimeException bad) {
                // Fail closed on unknown or unloaded native config/level.
                level = -1;
                cap = -1;
            }
            return new Snapshot(true, Set.copyOf(tokens), level, cap);
        } catch (ReflectiveOperationException | LinkageError | RuntimeException bad) {
            // Never accept an unknown ABI or empty/uninitialized research.
            return Snapshot.unavailable();
        }
    }

    public enum Result { GRANTED, ALREADY_PRESENT, NOT_READY, ERROR }

    public static Result grant(ServerPlayer player, String tokenId) {
        Snapshot snapshot = snapshot(player);
        if (!snapshot.ready()) return Result.NOT_READY;
        if (snapshot.has(tokenId)) return Result.ALREADY_PRESENT;
        try {
            Class<?> helper = Class.forName(RESEARCH + "ResearchHelper");
            var key = ResourceLocation.parse(tokenId);
            Object result = helper.getMethod("givePerkPointToken",
                    ServerPlayer.class, ResourceLocation.class).invoke(null, player, key);
            if (Boolean.TRUE.equals(result)) return Result.GRANTED;
            // If a native callback denied the token, we have no committed
            // grant evidence; do not pretend the purchase completed.
            return snapshot(player).has(tokenId) ? Result.ALREADY_PRESENT : Result.NOT_READY;
        } catch (ReflectiveOperationException | LinkageError | RuntimeException problem) {
            ProgressionMod.LOGGER.error("Astral perk point native API failed", problem);
            return Result.ERROR;
        }
    }

    /** Optional rollback used only if a committed local sale cannot be persisted. */
    public static boolean revoke(ServerPlayer player, String tokenId) {
        if (!snapshot(player).has(tokenId)) return true;
        try {
            Class<?> helper = Class.forName(RESEARCH + "ResearchHelper");
            Object result = helper.getMethod("removePerkPointToken",
                    ServerPlayer.class, ResourceLocation.class)
                    .invoke(null, player, ResourceLocation.parse(tokenId));
            return Boolean.TRUE.equals(result);
        } catch (ReflectiveOperationException | LinkageError | RuntimeException problem) {
            ProgressionMod.LOGGER.error("Failed to revoke uncommitted Astral token {}",tokenId,problem);
            return false;
        }
    }
}
