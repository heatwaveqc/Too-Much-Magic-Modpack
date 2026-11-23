package io.github.heatwaveqc.tyr.progression.runtime.cuisine;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.runtime.forms.JawrFormState;
import java.lang.reflect.Method;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

/**
 * Strict optional integration with the EXACT pinned JAWR 0.5.39 and
 * Brewin' and Chewin' 4.5.0 sources. All accepted blood vessels are
 * native filled items with a verified 500-blood consumption value.
 *
 * JAWR ordinary/chicken/vampire bottles are the SAME registry item; never
 * use an item tag or translated tooltip to distinguish their subtypes.
 */
public final class BloodCuisineNative {
    public static final String JAWR = "witchery:bottle_of_blood";
    public static final String IRONS = "irons_spellbooks:blood_vial";
    public static final String HEXEREI = "hexerei:blood_bottle";
    public static final String RED_WINE = "brewinandchewin:red_wine";

    private static DataComponentType<?> chickenMarker;
    private static DataComponentType<?> vampireMarker;
    private static Method bloodGain;

    private BloodCuisineNative() {}

    public static boolean pinned() {
        return pinnedVersion("witchery", "0.5.39")
                && pinnedVersion("brewinandchewin", "4.5.0");
    }

    private static boolean pinnedVersion(String mod, String version) {
        return ModList.get().getModContainerById(mod)
                .filter(container -> container.getModInfo().getVersion().toString().startsWith(version))
                .isPresent();
    }

    private static synchronized boolean resolve() {
        if (chickenMarker != null && vampireMarker != null && bloodGain != null) return true;
        if (!pinned()) return false;
        try {
            Class<?> registry = Class.forName("dev.sterner.witchery.core.registry.WitcheryDataComponents");
            Object singleton = registry.getField("INSTANCE").get(null);
            DataComponentType<?> chicken = resolveMarker(registry, singleton, "getCHICKEN_BLOOD");
            DataComponentType<?> vampire = resolveMarker(registry, singleton, "getVAMPIRE_BLOOD");
            Method increase = Class.forName("dev.sterner.witchery.feature.blood_pool.BloodPoolHandler")
                    .getMethod("increaseBlood", LivingEntity.class, int.class);
            chickenMarker = chicken;
            vampireMarker = vampire;
            bloodGain = increase;
            return true;
        } catch (ReflectiveOperationException | LinkageError | RuntimeException failure) {
            // Unknown native component layout is never treated as ordinary.
            return false;
        }
    }

    private static DataComponentType<?> resolveMarker(Class<?> registry, Object singleton, String getter)
            throws ReflectiveOperationException {
        Object holder = registry.getMethod(getter).invoke(singleton);
        Object component = holder.getClass().getMethod("get").invoke(holder);
        if (!(component instanceof DataComponentType<?> result))
            throw new ReflectiveOperationException(getter + " was not a component");
        return result;
    }

    public static int verifiedBottleBlood(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !resolve()) return 0;
        // These are the native ordinary bottle IDs. No potion or custom effect
        // may masquerade as a blood resource.
        if (stack.has(DataComponents.POTION_CONTENTS)) return 0;
        String id = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
        if (IRONS.equals(id) && ModList.get().isLoaded("irons_spellbooks"))
            return BloodCuisinePolicy.VERIFIED_FILLED_BOTTLE_BLOOD;
        if (HEXEREI.equals(id) && ModList.get().isLoaded("hexerei"))
            return BloodCuisinePolicy.VERIFIED_FILLED_BOTTLE_BLOOD;
        if (JAWR.equals(id) && !stack.has(chickenMarker) && !stack.has(vampireMarker))
            return BloodCuisinePolicy.VERIFIED_FILLED_BOTTLE_BLOOD;
        return 0;
    }

    public static boolean isVampire(ServerPlayer player) {
        return resolve() && JawrFormState.present(player, "vampire");
    }

    public static boolean canCreditNative() {
        return resolve() && bloodGain != null;
    }

    public static boolean credit(ServerPlayer player, int amount) {
        if (player.getAbilities().instabuild || amount <= 0 || !isVampire(player))
            return false;
        try {
            bloodGain.invoke(null, player, amount);
            return true;
        } catch (ReflectiveOperationException | RuntimeException failure) {
            ProgressionMod.LOGGER.error("JAWR cuisine blood credit failed ({} units)", amount, failure);
            return false;
        }
    }

    public static synchronized void stopped(ServerStoppedEvent event) {
        chickenMarker = null;
        vampireMarker = null;
        bloodGain = null;
    }
}
