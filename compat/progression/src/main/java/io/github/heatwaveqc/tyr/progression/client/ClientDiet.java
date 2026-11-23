package io.github.heatwaveqc.tyr.progression.client;

import io.github.heatwaveqc.tyr.progression.network.FoodPreviewWire;
import io.github.heatwaveqc.tyr.progression.runtime.Selectors;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

/**
 * Client-local dietary preview only. Every nutritional value comes from the
 * server's per-profile evaluation; the client does not recalculate food policy.
 */
public final class ClientDiet {
    private ClientDiet() {}

    // Existing short-lived beverage selector lease stays independent.
    private static WeakReference<LocalPlayer> beverageOwner = new WeakReference<>(null);
    private static List<String> selectors = List.of();
    private static int received;

    public static void accept(String payload) {
        var player = Minecraft.getInstance().player;
        beverageOwner = new WeakReference<>(player);
        received = player == null ? 0 : player.tickCount;
        var values = payload.isEmpty() ? List.<String>of() : Arrays.asList(payload.split("\n"));
        selectors = values.size() > 48 || values.stream().anyMatch(
            id -> id.length() > 128 || !id.matches("#?[a-z0-9_.-]+:[a-z0-9_./-]+"))
                ? List.of() : List.copyOf(values);
    }

    public static boolean allows(LivingEntity entity, ItemStack stack) {
        var player = Minecraft.getInstance().player;
        return player != null && player == entity && beverageOwner.get() == player
            && player.isAlive() && player.tickCount >= received
            && player.tickCount - received <= 40
            && stack.getUseAnimation() == UseAnim.DRINK
            && selectors.stream().anyMatch(selector -> Selectors.item(selector, stack));
    }

    // A new snapshot is unpublished until the entire transfer validates.
    private static WeakReference<LocalPlayer> previewOwner = new WeakReference<>(null);
    private static Map<String, FoodPreviewWire.Entry> values = Map.of();
    private static int pendingTransfer;
    private static int pendingCount;
    private static int nextPart;
    private static StringBuilder pendingRows;

    private static void clearPreview() {
        values = Map.of();
        pendingRows = null;
        pendingCount = 0;
        nextPart = 0;
    }

    public static void acceptPreviewChunk(int transfer, int part, int total, String rows) {
        var player = Minecraft.getInstance().player;
        if (player == null) return;
        if (part == 0) {
            previewOwner = new WeakReference<>(player);
            clearPreview(); // Fail closed during a policy/species transition.
            if (total < 1 || total > FoodPreviewWire.MAX_CHUNKS) return;
            pendingTransfer = transfer;
            pendingCount = total;
            pendingRows = new StringBuilder();
        }
        if (previewOwner.get() != player || pendingRows == null
                || transfer != pendingTransfer || total != pendingCount
                || part != nextPart || rows.length() > FoodPreviewWire.CHUNK_CHARS) {
            clearPreview();
            return;
        }
        pendingRows.append(rows);
        nextPart++;
        if (nextPart == pendingCount) {
            try {
                values = FoodPreviewWire.parse(pendingRows.toString());
            } catch (IllegalArgumentException invalid) {
                values = Map.of();
            } finally {
                pendingRows = null;
            }
        }
    }

    /**
     * Only the local player may use the projection, and only when the actual
     * stack has a native FOOD component. Preserve the item's effects, return
     * container, eating duration and all item-use callbacks.
     */
    public static FoodProperties preview(LivingEntity entity, ItemStack stack) {
        var player = Minecraft.getInstance().player;
        if (player == null || player != entity || !player.isAlive()
                || previewOwner.get() != player || stack.isEmpty()) return null;
        var entry = values.get(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        FoodProperties original = stack.get(DataComponents.FOOD);
        if (entry == null || original == null) return null;
        if (entry.nativePermission()) return original;
        return new FoodProperties(entry.hunger(), entry.saturationModifier(),
            original.canAlwaysEat(), original.eatSeconds(),
            original.usingConvertsTo(), original.effects());
    }
}
