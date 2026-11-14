package io.github.heatwaveqc.tmm.transformations.vampire;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import java.util.Optional;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

/** Non-mutating exact-version binary readout. No inferred state from a meter. */
public final class VampireDiagnostics {
    private VampireDiagnostics() { }

    public record Vitals(int level, int blood, int maxBlood) {
        public Vitals {
            if (level < 0 || blood < 0 || maxBlood < 0) {
                throw new IllegalArgumentException("Upstream values must be nonnegative");
            }
        }
    }

    public static Optional<Vitals> vampirism(ServerPlayer player) {
        if (!ModList.get().isLoaded("vampirism")) return Optional.empty();
        try {
            Class<?> api = Class.forName("de.teamlapen.vampirism.api.VampirismAPI");
            Object playerData = api.getMethod("vampirePlayer", Player.class).invoke(null, player);
            if (playerData == null) return Optional.empty();
            int level = ((Number) playerData.getClass().getMethod("getLevel").invoke(playerData)).intValue();
            int blood = ((Number) playerData.getClass().getMethod("getBloodLevel").invoke(playerData)).intValue();
            Object stats = playerData.getClass().getMethod("getBloodStats").invoke(playerData);
            int max = ((Number) stats.getClass().getMethod("getMaxBlood").invoke(stats)).intValue();
            return Optional.of(new Vitals(level, blood, max));
        } catch (ReflectiveOperationException | LinkageError | SecurityException | IllegalArgumentException e) {
            TransformationsMod.LOGGER.warn("Vampirism vampire meters unavailable", e);
            return Optional.empty();
        }
    }

    public static Optional<Vitals> jawr(ServerPlayer player) {
        if (!ModList.get().isLoaded("witchery")) return Optional.empty();
        try {
            Class<?> attachment = Class.forName(
                    "dev.sterner.witchery.feature.affliction.vampire.VampirePlayerAttachment");
            Object data = attachment.getMethod("getData", Player.class).invoke(null, player);
            if (data == null) return Optional.empty();
            int level = ((Number) data.getClass().getMethod("getLevel").invoke(data)).intValue();

            Class<?> bloodAttachment = Class.forName(
                    "dev.sterner.witchery.feature.blood_pool.BloodPoolAttachment");
            Object blood = bloodAttachment.getMethod("getData", LivingEntity.class).invoke(null, player);
            if (blood == null) return Optional.empty();
            int current = ((Number) blood.getClass().getMethod("getBloodPool").invoke(blood)).intValue();
            int max = ((Number) blood.getClass().getMethod("getMaxBlood").invoke(blood)).intValue();
            return Optional.of(new Vitals(level, current, max));
        } catch (ReflectiveOperationException | LinkageError | SecurityException | IllegalArgumentException e) {
            TransformationsMod.LOGGER.warn("JAWR vampire meters unavailable", e);
            return Optional.empty();
        }
    }
}
