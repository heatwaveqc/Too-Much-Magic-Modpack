package io.github.heatwaveqc.tmm.progression.runtime;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * Server-only, opt-in temporary Bountiful decree sealing.
 *
 * The shapeless recipe produces an unsigned Bountiful decree carrying a
 * custom-data application ID. That is NOT a signed, useable specialist decree.
 * A player with the matching native form or joined discipline can right-click
 * to attach Bountiful's actual decree_data component. Machines, vanilla
 * crafting and traders cannot turn an application into a specialized decree.
 *
 * Progression owns the gameplay logic. Native transformation probes are
 * queried read-only via the optional tmm_transformations module and fail closed
 * if incomplete or unavailable. No player-supplied state or NBT form flag.
 */
public final class BountifulDecreeSeals {
    private BountifulDecreeSeals() {}

    private static final String MARKER = "tmm_bountiful_decree_application";
    private static final ResourceLocation DECREE_ID = ResourceLocation.parse("bountiful:decree");
    private static final ResourceLocation DEED_COMPONENT_ID = ResourceLocation.parse("bountiful:decree_data");

    public static void register() {
        if (ModList.get().isLoaded("bountiful")) {
            NeoForge.EVENT_BUS.addListener(BountifulDecreeSeals::onUse);
        }
    }

    public static void onUse(PlayerInteractEvent.RightClickItem event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ItemStack held = event.getItemStack();
        if (held.isEmpty() || held.getItem() != BuiltInRegistries.ITEM.get(DECREE_ID)) return;
        CustomData custom = held.get(DataComponents.CUSTOM_DATA);
        if (custom == null) return;
        String document = custom.copyTag().getString(MARKER);
        if (!document.startsWith("tmm_")) return;
        String family = document.substring(4);
        if (!BountifulDecreeSealPolicy.declared(family) || !allowed(player, family)) return;
        // Never override an already signed decree. Keep the application if
        // the upstream component ID or codec cannot be resolved.
        DataComponentType<?> component = BuiltInRegistries.DATA_COMPONENT_TYPE.getOptional(DEED_COMPONENT_ID).orElse(null);
        if (component == null || component.codec() == null || held.has(component)) return;
        JsonObject encoded = new JsonObject();
        JsonArray ids = new JsonArray();
        ids.add(document);
        encoded.add("ids", ids);
        encoded.addProperty("rank", 1);
        Object payload = component.codec().parse(JsonOps.INSTANCE, encoded).result().orElse(null);
        if (payload == null) return;
        attach(held, component, payload);
        held.remove(DataComponents.CUSTOM_DATA);
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void attach(ItemStack held, DataComponentType component, Object value) {
        held.set(component, value);
    }

    private static boolean allowed(ServerPlayer player, String family) {
        String path = BountifulDecreeSealPolicy.classPath(family);
        if (path != null) {
            return ProgressionApi.track(player, path).joined();
        }
        Set<String> required = BountifulDecreeSealPolicy.nativeStates(family);
        return required != null && allNativeStatesPresent(player, required);
    }

    /**
     * Use the existing authoritative five-provider snapshot, not a player tag.
     * Reflection avoids a hard compile/runtime dependency on Transformations.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static boolean allNativeStatesPresent(ServerPlayer player, Set<String> required) {
        if (!ModList.get().isLoaded("tmm_transformations")) return false;
        try {
            Class<?> probes = Class.forName(
                "io.github.heatwaveqc.tmm.transformations.adapters.ProbeRegistry");
            Object snapshot = probes.getMethod("observe", ServerPlayer.class).invoke(null, player);
            if (snapshot == null ||
                !Boolean.TRUE.equals(snapshot.getClass().getMethod("isComplete").invoke(snapshot))) {
                return false;
            }
            Class<?> provider = Class.forName(
                "io.github.heatwaveqc.tmm.transformations.core.ProviderState");
            Method present = snapshot.getClass().getMethod("present", provider);
            for (String name : required) {
                Object enumValue = Enum.valueOf((Class<? extends Enum>) provider.asSubclass(Enum.class), name);
                if (!Boolean.TRUE.equals(present.invoke(snapshot, enumValue))) return false;
            }
            return true;
        } catch (ReflectiveOperationException | LinkageError | IllegalArgumentException ex) {
            return false;
        }
    }
}
