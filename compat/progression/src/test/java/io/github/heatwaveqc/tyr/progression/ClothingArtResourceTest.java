package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.foundation.MartialGarmentStyle;
import java.awt.image.BufferedImage;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Native-size PNGs, valid transparent masks and complete generated model references. */
class ClothingArtResourceTest {
    private static final String ROOT = "/assets/tyr_progression/";
    private BufferedImage read(String path) throws Exception {
        try (var in = getClass().getResourceAsStream(ROOT + path)) {
            assertNotNull(in, path);
            var image = ImageIO.read(in);
            assertNotNull(image, path);
            assertEquals(64, image.getWidth(), path);
            assertEquals(64, image.getHeight(), path);
            assertTrue(image.getColorModel().hasAlpha(), path);
            return image;
        }
    }
    private void masks(String prefix, int channels) throws Exception {
        boolean[] occupied = new boolean[4096];
        for (int layer=0; layer<=channels; layer++) {
            var image = read(prefix + (channels==0 ? "" : "_" + layer) + ".png");
            int count = 0;
            for (int y=0;y<64;y++) for (int x=0;x<64;x++) {
                int pixel=image.getRGB(x,y), alpha=pixel>>>24;
                if (alpha==0) continue;
                assertEquals(255, alpha, prefix);
                assertFalse(occupied[y*64+x], prefix+" overlapping masks");
                occupied[y*64+x]=true;
                count++;
                if (layer<channels) {
                    assertEquals((pixel>>>16)&255,(pixel>>>8)&255,prefix+" non-neutral mask");
                    assertEquals((pixel>>>8)&255,pixel&255,prefix+" non-neutral mask");
                }
            }
            assertTrue(count>0, prefix+" empty layer "+layer);
        }
    }
    private void garment(String item, String material, int armorLayer, int channels) throws Exception {
        masks("textures/item/"+item, channels);
        masks("textures/models/armor/"+material+"_layer_"+armorLayer, channels);
        try (var in = getClass().getResourceAsStream(ROOT+"models/item/"+item+".json")) {
            assertNotNull(in,item);
            var model=JsonParser.parseReader(new InputStreamReader(in,StandardCharsets.UTF_8)).getAsJsonObject();
            assertEquals("minecraft:item/generated",model.get("parent").getAsString());
            var textures=model.getAsJsonObject("textures");
            assertEquals(channels+1,textures.size());
            for(int i=0;i<=channels;i++)
                assertEquals("tyr_progression:item/"+item+(channels==0?"":"_"+i),textures.get("layer"+i).getAsString());
        }
    }
    @Test void allFifteenGarmentsHaveComplete64PixelArtwork() throws Exception {
        garment("white_cap","white_cap",1,0);
        garment("loincloth","loincloth",2,0);
        garment("tartan_kilt","tartan_kilt",2,4);
        for(var style: MartialGarmentStyle.values()) {
            garment(style.upperId(),style.id(),1,3);
            garment(style.lowerId(),style.id(),2,3);
        }
    }
}
