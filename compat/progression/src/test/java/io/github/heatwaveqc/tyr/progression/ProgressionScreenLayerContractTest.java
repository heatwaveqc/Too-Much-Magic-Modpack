package io.github.heatwaveqc.tyr.progression;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Source-level regression for #506. Minecraft 1.21.1's Screen.render() invokes
 * renderBackground() immediately before its widgets. Calling super.render()
 * AFTER drawing our nodes must never apply vanilla blur a second time.
 *
 * This test intentionally needs no Minecraft client or shader context. Actual
 * client visual acceptance remains a separate Type C check.
 */
class ProgressionScreenLayerContractTest {
    private static final Path SOURCE = Path.of(
            "src/main/java/io/github/heatwaveqc/tyr/progression/client/ProgressionScreen.java");

    private static String readSource() throws IOException {
        return Files.readString(SOURCE);
    }

    @Test void inheritedBackgroundCannotBlurOrCoverAlreadyDrawnSkills() throws IOException {
        String source = readSource();
        int override = source.indexOf("@Override public void renderBackground(");
        int render = source.indexOf("@Override public void render(GuiGraphics", override);
        assertTrue(override >= 0 && render > override, "custom Screen background override required");
        String customBackground = source.substring(override, render);
        assertFalse(customBackground.contains("super.renderBackground("),
                "superclass background would blur the painted skill tree");
        assertFalse(customBackground.contains("g.fill("),
                "background must not paint over the tree when Screen.render invokes it");
        assertFalse(source.substring(render).contains("renderBackground(g,mx,my,partial)"),
                "do not call background separately before Screen.render");
    }

    @Test void opaquePanelPrecedesSingleStandardWidgetPass() throws IOException {
        String source = readSource();
        int render = source.indexOf("@Override public void render(GuiGraphics");
        int firstOpaque = source.indexOf("g.fill(0,0,width,height,0xFF13101D)", render);
        int panel = source.indexOf("g.fill(4,4,width-4,height-4,0xFF1A1725)", render);
        int widgets = source.indexOf("super.render(g,mx,my,partial)", render);
        assertTrue(render >= 0 && firstOpaque > render && panel > firstOpaque && widgets > panel,
                "opaque Progression canvas must be drawn before normal buttons");
        assertEquals(widgets, source.lastIndexOf("super.render(g,mx,my,partial)"),
                "widgets should be painted exactly once");
        assertFalse(source.contains("renderBlurredBackground("),
                "progression must not introduce a direct shader blur");
    }

    @Test void userInputsAndServerDataRemainOwnedByExistingCode() throws IOException {
        String source = readSource();
        assertTrue(source.contains("ProgressionMenuNetwork.Request(pathId)"));
        assertTrue(source.contains("ProgressionMenuNetwork.Mutate(pathId,\"buy\",selection)"));
        assertTrue(source.contains("super.mouseClicked(mx,my,button)"));
        assertTrue(source.contains("g.enableScissor(viewportLeft(),viewportTop(),viewportRight(),viewportBottom())"));
        assertTrue(source.contains("g.disableScissor()"));
    }
}
