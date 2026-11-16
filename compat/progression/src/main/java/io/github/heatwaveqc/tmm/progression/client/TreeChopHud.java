package io.github.heatwaveqc.tmm.progression.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Small client-only fallback progress bar for partially chopped HT TreeChop
 * logs. TreeChop's crosshair glyph can disappear after a log changes to its
 * chopped-log block. Only render on the chopped state and honor its native
 * chopping-indicator visibility toggle. No TreeChop jar is hard-linked.
 */
public final class TreeChopHud {
    private TreeChopHud() {}

    private static boolean checked;
    private static Object cache;
    private static Method getTree;
    private static Method indicatorVisible;

    private static boolean initialize() {
        if (checked) return cache != null;
        checked = true;
        if (!ModList.get().isLoaded("treechop")) return false;
        try {
            Class<?> client = Class.forName("ht.treechop.client.Client");
            Field field = client.getField("treeCache");
            cache = field.get(null);
            getTree = cache.getClass().getMethod("getTree",
                    net.minecraft.world.level.Level.class, BlockPos.class);
            indicatorVisible = client.getMethod("isChoppingIndicatorEnabled");
            return true;
        } catch (ReflectiveOperationException | LinkageError ignored) {
            cache = null;
            return false;
        }
    }

    public static void render(RenderGuiEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null ||
                minecraft.screen != null || minecraft.options.hideGui ||
                !initialize() || !(minecraft.hitResult instanceof BlockHitResult target)
                || minecraft.hitResult.getType() != HitResult.Type.BLOCK) return;

        BlockPos pos = target.getBlockPos();
        if (!BuiltInRegistries.BLOCK.getKey(minecraft.level.getBlockState(pos).getBlock())
                .toString().equals("treechop:chopped_log")) return;
        try {
            if (!Boolean.TRUE.equals(indicatorVisible.invoke(null))) return;
            Object tree = getTree.invoke(cache, minecraft.level, pos);
            if (tree == null) return;
            int chopped = (int) tree.getClass().getMethod("getChops").invoke(tree);
            int total = (int) tree.getClass().getMethod("numChopsNeededToFell").invoke(tree);
            if (total < 2 || chopped <= 0 || chopped >= total) return;
            GuiGraphics gui = event.getGuiGraphics();
            int center = gui.guiWidth() / 2;
            int y = gui.guiHeight() / 2 + 24;
            int w = 74;
            int x = center - w / 2;
            int fill = Math.min(w - 2, Math.max(0,
                    (int) Math.round((double) chopped / total * (w - 2))));
            gui.fill(x - 2, y - 3, x + w + 2, y + 20, 0xB8101418);
            gui.fill(x, y, x + w, y + 6, 0xFF3D4449);
            gui.fill(x + 1, y + 1, x + 1 + fill, y + 5, 0xFF93D0A5);
            String message = chopped + " / " + total + " chops";
            gui.drawString(minecraft.font, message,
                    center - minecraft.font.width(message) / 2, y + 9,
                    0xFFE5EEE9, true);
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            // Cosmetic overlay is optional. No gameplay/XP impact on failure.
        }
    }
}
