package io.github.heatwaveqc.tmm.economy.runtime;

import java.lang.reflect.Method;
import java.util.Optional;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

/** Native Improvable Skills 21.0.2 owns the unlock and persisted player data.
 * No Economy skill wallet or separate attachment. The original ability ID
 * remains reserved until the coordinated owner cutover; no registration here.
 */
public final class PersonalEnchantFilterAbilityGate {
    private PersonalEnchantFilterAbilityGate() {}
    public static final ResourceLocation ABILITY_ID =
        ResourceLocation.fromNamespaceAndPath("tyr_progression", "arcane_discrimination");

    public static boolean unlocked(ServerPlayer player) {
        return unlockedData(player).isPresent();
    }

    // Package private: only the server transaction service can mutate the native compound.
    static Optional<CompoundTag> unlockedData(ServerPlayer player) {
        if (player == null || !ModList.get().isLoaded("improvableskills")) return Optional.empty();
        try {
            Class<?> mod = Class.forName("org.zeith.improvableskills.ImprovableSkills");
            Object registry = mod.getField("ABILITIES").get(null);
            if (!(registry instanceof Registry<?> abilities)) return Optional.empty();
            Object ability = abilities.get(ABILITY_ID);
            if (ability == null) return Optional.empty();
            Class<?> dataManager = Class.forName("org.zeith.improvableskills.data.PlayerDataManager");
            Object data = dataManager.getMethod("getDataFor", Player.class).invoke(null, player);
            if (data == null) return Optional.empty();
            Class<?> nativeAbility = Class.forName("org.zeith.improvableskills.api.registry.PlayerAbilityBase");
            Method hasAbility = data.getClass().getMethod("hasAbility", nativeAbility);
            if (!Boolean.TRUE.equals(hasAbility.invoke(data, ability))) return Optional.empty();
            Object persisted = data.getClass().getField("persistedData").get(data);
            return persisted instanceof CompoundTag tag ? Optional.of(tag) : Optional.empty();
        } catch (ReflectiveOperationException | LinkageError | RuntimeException ignored) {
            return Optional.empty();
        }
    }
}
