package io.github.heatwaveqc.tyr.progression.runtime;

import java.lang.reflect.Method;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

/**
 * Optional Improvable Skills integration. Until the dedicated native ability
 * is registered and earned, the six-book escrow and its exclusions are inert.
 * Reflective access avoids a mandatory hard dependency or cloned ability list.
 */
public final class PersonalEnchantFilterAbilityGate {
    private PersonalEnchantFilterAbilityGate() {}

    public static final ResourceLocation ABILITY_ID =
        ResourceLocation.fromNamespaceAndPath("tyr_progression", "arcane_discrimination");

    public static boolean unlocked(ServerPlayer player) {
        if (player == null || !ModList.get().isLoaded("improvableskills")) return false;
        try {
            Class<?> mod = Class.forName("org.zeith.improvableskills.ImprovableSkills");
            Object registry = mod.getField("ABILITIES").get(null);
            if (!(registry instanceof Registry<?> abilities)) return false;
            Object ability = abilities.get(ABILITY_ID);
            if (ability == null) return false;

            Class<?> dataManager =
                Class.forName("org.zeith.improvableskills.data.PlayerDataManager");
            Object data = dataManager.getMethod("getDataFor", Player.class)
                .invoke(null, player);
            if (data == null) return false;

            Class<?> nativeAbility =
                Class.forName("org.zeith.improvableskills.api.registry.PlayerAbilityBase");
            Method hasAbility = data.getClass().getMethod("hasAbility", nativeAbility);
            return Boolean.TRUE.equals(hasAbility.invoke(data, ability));
        } catch (ReflectiveOperationException | LinkageError | RuntimeException ignored) {
            return false;
        }
    }
}
