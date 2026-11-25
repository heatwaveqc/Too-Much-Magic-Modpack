package io.github.heatwaveqc.tyr.progression.runtime;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.github.heatwaveqc.tmm.transformations.adapters.ProbeRegistry;
import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/** Server-owned application sealing. Bountiful's own persistent component codec is authoritative. */
public final class BountifulDecreeSeals {
    private BountifulDecreeSeals() {}

    // These external recipe markers are intentionally stable across the Progression namespace migration.
    private static final String MARKER = "tmm_bountiful_decree_application";
    private static final ResourceLocation DECREE_ID = ResourceLocation.parse("bountiful:decree");
    private static final ResourceLocation DECREE_COMPONENT_ID = ResourceLocation.parse("bountiful:decree_data");

    public static void register() {
        if (ModList.get().isLoaded("bountiful"))
            NeoForge.EVENT_BUS.addListener(BountifulDecreeSeals::onUse);
    }

    public static void onUse(PlayerInteractEvent.RightClickItem event) {
        if (event.isCanceled() || !(event.getEntity() instanceof ServerPlayer player)
            || player instanceof FakePlayer || !player.isAlive() || player.isSpectator()
            || player.getServer() == null
            || player.getServer().getPlayerList().getPlayer(player.getUUID()) != player) return;
        ItemStack held = event.getItemStack();
        if (held.getCount() != 1 || !BuiltInRegistries.ITEM.getOptional(DECREE_ID)
            .map(held::is).orElse(false)) return;
        CustomData custom = held.get(DataComponents.CUSTOM_DATA);
        if (custom == null) return;
        var tag = custom.copyTag();
        String document = tag.getString(MARKER);
        if (!BountifulDecreeSealPolicy.allowed(document,
            path -> ProgressionApi.track(player, path).joined(),
            () -> ProbeRegistry.observe(player))) return;

        // An existing native component is never overwritten. A missing or
        // incompatible codec fails before any item mutation.
        DataComponentType<?> component = BuiltInRegistries.DATA_COMPONENT_TYPE
            .getOptional(DECREE_COMPONENT_ID).orElse(null);
        if (component == null || component.codec() == null || held.has(component)) return;
        JsonObject encoded = new JsonObject();
        JsonArray ids = new JsonArray();
        ids.add(document);
        encoded.add("ids", ids);
        encoded.addProperty("rank", 1);
        if (!attach(held, component, encoded)) return;

        tag.remove(MARKER);
        if (tag.isEmpty()) held.remove(DataComponents.CUSTOM_DATA);
        else held.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    private static <T> boolean attach(ItemStack held, DataComponentType<T> component, JsonObject encoded) {
        T payload;
        try {
            payload = component.codec().parse(JsonOps.INSTANCE, encoded).result().orElse(null);
        } catch (RuntimeException | LinkageError failure) {
            return false;
        }
        if (payload == null) return false;
        held.set(component, payload);
        return true;
    }
}
