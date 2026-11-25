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
     * Presence is independent of FoodProperties: a DENY projects a deliberately
     * null food result, whereas an unknown ID must preserve the native result.
     */
    public record Projection(boolean present, FoodProperties food) {
        private static final Projection NONE = new Projection(false, null);
    }

    /** Read-only client projection for the current local player. */
    public static Projection preview(LivingEntity entity, ItemStack stack) {
        var player = Minecraft.getInstance().player;
        if (player == null || player != entity || !player.isAlive()
                || previewOwner.get() != player || stack.isEmpty()) return Projection.NONE;
        var entry = values.get(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        FoodProperties original = stack.get(DataComponents.FOOD);
        if (entry == null || original == null) return Projection.NONE;
        if (entry.denied()) return new Projection(true, null);
        if (entry.beveragePermission()) return new Projection(true,
            new FoodProperties(0,0f,true,original.eatSeconds(),
                original.usingConvertsTo(),original.effects()));
        if (entry.lavaConditional()) {
            var data = stack.get(DataComponents.CUSTOM_DATA);
            boolean caught = data != null && data.copyTag().getBoolean("tyr_progression:lava_caught");
            if (!caught) return new Projection(true, null);
            if (!entry.lavaHasNutrition()) return new Projection(true, original);
        }
        if (entry.nativePermission()) return new Projection(true, original);
        return new Projection(true, new FoodProperties(
            entry.hunger(), entry.saturationModifier(),
            original.canAlwaysEat(), original.eatSeconds(),
            original.usingConvertsTo(), original.effects()));
    }
}
