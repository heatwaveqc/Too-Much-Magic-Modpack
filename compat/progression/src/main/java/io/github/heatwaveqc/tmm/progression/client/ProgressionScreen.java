package io.github.heatwaveqc.tmm.progression.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.heatwaveqc.tmm.progression.network.ProgressionMenuNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Native visual browser for the server's Monk and Hunter skill graphs.
 * No guesswork from local rank/XP: purchases, costs, nodes and prerequisites
 * are projections of the actual server-loaded JSON and player ledger.
 */
public final class ProgressionScreen extends Screen {
    private static final String MONK = "tmm_progression:monk";
    private static final String HUNTER = "tmm_progression:hunter";
    private static final int CARD_H = 44;
    private static final int CARD_GAP = 8;
    private String pathId;
    private JsonObject data;
    private int scroll;
    private boolean confirmRespec;
    private Button respecButton;

    public ProgressionScreen(String pathId) {
        super(Component.literal("Disciplines"));
        this.pathId = pathId;
    }

    private void request() {
        PacketDistributor.sendToServer(new ProgressionMenuNetwork.Request(pathId));
    }

    public static void receive(String json) {
        try {
            JsonObject parsed = JsonParser.parseString(json).getAsJsonObject();
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.screen instanceof ProgressionScreen screen
                    && screen.pathId.equals(parsed.get("path").getAsString()))
                screen.data = parsed;
        } catch (RuntimeException ignored) {
            // Malformed/outdated server snapshot must not crash the client UI.
        }
    }

    @Override
    protected void init() {
        int left = Math.max(12, (width - 505) / 2);
        addRenderableWidget(Button.builder(Component.literal("Monk"), b -> switchPath(MONK))
                .bounds(left + 10, 36, 94, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Hunter"), b -> switchPath(HUNTER))
                .bounds(left + 109, 36, 94, 20).build());
        respecButton = addRenderableWidget(Button.builder(Component.literal("Respec..."),
                b -> {
                    if (data == null || !data.get("joined").getAsBoolean()) return;
                    if (!confirmRespec) {
                        confirmRespec = true;
                        respecButton.setMessage(Component.literal("Confirm respec"));
                    } else {
                        confirmRespec = false;
                        respecButton.setMessage(Component.literal("Respec..."));
                        PacketDistributor.sendToServer(new ProgressionMenuNetwork.Mutate(
                                pathId, "respec", ""));
                    }
                }).bounds(left + 374, height - 31, 119, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose())
                .bounds(left + 264, height - 31, 105, 20).build());
        request();
    }

    private void switchPath(String id) {
        if (pathId.equals(id)) return;
        pathId = id;
        data = null;
        scroll = 0;
        confirmRespec = false;
        if (respecButton != null)
            respecButton.setMessage(Component.literal("Respec..."));
        request();
    }

    private static String pretty(String id) {
        String shortId = id.substring(id.lastIndexOf(':') + 1).replace('_', ' ');
        String[] words = shortId.split(" ");
        StringBuilder out = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                if (!out.isEmpty()) out.append(' ');
                out.append(word.substring(0, 1).toUpperCase(Locale.ROOT))
                        .append(word.substring(1));
            }
        }
        return out.toString();
    }

    private static int color(JsonObject skill) {
        if (skill.get("owned").getAsInt() > 0) return 0xFF264D3D;
        if (skill.get("auto").getAsBoolean()) return 0xFF25364C;
        if (skill.get("buyable").getAsBoolean()) return 0xFF745B29;
        return 0xFF313139;
    }

    private int cols() { return width >= 640 ? 4 : 3; }
    private int left() { return Math.max(12, (width - 505) / 2); }
    private int startY() { return 108; }
    private int bottomY() { return height - 43; }
    private int cardWidth() { return (485 - (cols() - 1) * CARD_GAP) / cols(); }
    private int visibleHeight() { return Math.max(30, bottomY() - startY()); }

    private JsonArray skills() {
        return data == null ? new JsonArray() : data.getAsJsonArray("skills");
    }

    private int maxScroll() {
        int rows = (skills().size() + cols() - 1) / cols();
        int contentHeight = rows * (CARD_H + CARD_GAP);
        return Math.max(0, (contentHeight - visibleHeight() + CARD_H + CARD_GAP - 1)
                / (CARD_H + CARD_GAP));
    }

    private int cardX(int index) { return left() + 10 + index % cols() * (cardWidth() + CARD_GAP); }
    private int cardY(int index) { return startY() + (index / cols() - scroll) * (CARD_H + CARD_GAP); }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        renderBackground(gui, mouseX, mouseY, partialTick);
        int left = left();
        gui.fill(left, 10, left + 505, height - 8, 0xDF141821);
        gui.drawString(font, "DISCIPLINE PROGRESSION", left + 13, 17, 0xFFEEEEEE, false);
        gui.drawString(font, pathId.equals(MONK) ? "MONK CULTIVATION" : "HUNTER TRAINING",
                left + 215, 42, 0xFFA6DCD0, false);

        if (data == null) {
            gui.drawString(font, "Requesting skill data from server...", left + 20,
                    startY() + 15, 0xFFAAAAAA, false);
            super.render(gui, mouseX, mouseY, partialTick);
            return;
        }
        boolean joined = data.get("joined").getAsBoolean();
        int rank = data.get("rank").getAsInt();
        int maxRank = data.get("max_rank").getAsInt();
        int points = data.get("points").getAsInt();
        long xp = data.get("xp").getAsLong();
        long nextXp = data.get("next_xp").getAsLong();
        long rankStart = data.get("rank_start_xp").getAsLong();
        gui.drawString(font, joined ? "Rank " + rank + "/" + maxRank : "Not enrolled",
                left + 14, 69, joined ? 0xFFA6DCD0 : 0xFFFFB6A0, false);
        gui.drawString(font, "Native skill points: " + points,
                left + 150, 69, 0xFFF1D99B, false);
        gui.drawString(font, xp + " / " + nextXp + " cultivation XP",
                left + 314, 69, 0xFFD0D0D0, false);
        gui.fill(left + 13, 84, left + 491, 91, 0xFF353941);
        double fraction = rank >= maxRank ? 1.0
                : nextXp <= rankStart ? 0.0
                : Math.max(0, Math.min(1.0,
                        (double) (xp - rankStart) / (nextXp - rankStart)));
        gui.fill(left + 13, 84, left + 13 + (int) (478 * fraction), 91, 0xFF529E84);
        gui.drawString(font, "Automatic gifts: blue | Learned: green | Purchasable: gold",
                left + 13, 96, 0xFFAFBAC6, false);

        JsonArray nodes = skills();
        scroll = Math.max(0, Math.min(scroll, maxScroll()));
        gui.enableScissor(left + 8, startY(), left + 496, bottomY());
        String hover = null;
        for (int i = 0; i < nodes.size(); i++) {
            JsonObject node = nodes.get(i).getAsJsonObject();
            int x = cardX(i), y = cardY(i), w = cardWidth();
            if (y >= bottomY() || y + CARD_H < startY()) continue;
            gui.fill(x, y, x + w, y + CARD_H, color(node));
            gui.fill(x, y, x + w, y + 2, node.get("buyable").getAsBoolean()
                    ? 0xFFE5C66A : 0xFF678A83);
            String name = font.plainSubstrByWidth(pretty(node.get("id").getAsString()), w - 10);
            gui.drawString(font, name, x + 5, y + 7, 0xFFF7F7F7, false);
            int owned = node.get("owned").getAsInt();
            int maximum = node.get("maximum").getAsInt();
            String info = node.get("auto").getAsBoolean()
                    ? "Auto  " + owned + "/" + maximum
                    : owned + "/" + maximum + "  -  " + node.get("price").getAsInt() + " SP";
            gui.drawString(font, info, x + 5, y + 26, 0xFFD7DDE3, false);
            if (mouseX >= x && mouseX < x+w && mouseY >= y && mouseY < y+CARD_H) {
                String effects = "";
                for (JsonElement effect : node.getAsJsonArray("effects")) {
                    if (!effects.isEmpty()) effects += ", ";
                    effects += effect.getAsString().replace('_', ' ');
                }
                hover = pretty(node.get("id").getAsString()) + " | requires rank " +
                        node.get("required_rank").getAsInt() + " | " + effects;
                if (!node.get("choice_group").getAsString().isEmpty())
                    hover += " | Exclusive: " + node.get("choice_group").getAsString();
            }
        }
        gui.disableScissor();
        if (hover != null)
            gui.renderTooltip(font, Component.literal(hover), mouseX, mouseY);
        super.render(gui, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) return true;
        if (button != 0 || data == null || !data.get("joined").getAsBoolean()) return false;
        if (mouseY < startY() || mouseY >= bottomY()) return false;
        JsonArray nodes = skills();
        for (int i = 0; i < nodes.size(); i++) {
            int x = cardX(i), y = cardY(i);
            if (mouseX < x || mouseX >= x + cardWidth() ||
                    mouseY < y || mouseY >= y + CARD_H) continue;
            JsonObject node = nodes.get(i).getAsJsonObject();
            if (node.get("buyable").getAsBoolean()) {
                PacketDistributor.sendToServer(new ProgressionMenuNetwork.Mutate(
                        pathId, "buy", node.get("id").getAsString()));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (mouseY >= startY() && mouseY <= bottomY()) {
            scroll = Math.max(0, Math.min(maxScroll(), scroll - (int) Math.signum(scrollY)));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override public boolean isPauseScreen() { return false; }
}
