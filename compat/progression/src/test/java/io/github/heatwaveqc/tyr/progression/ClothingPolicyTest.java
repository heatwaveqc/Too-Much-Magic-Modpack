package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.foundation.ClothingPolicy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClothingPolicyTest {
    @Test void onlyExplicitlyAllowedUnmodifiedGarmentsQualify(){
        assertTrue(ClothingPolicy.allows(true,false,false,false,false,false));
        assertFalse(ClothingPolicy.allows(false,false,false,false,false,false));
        assertFalse(ClothingPolicy.allows(true,true,false,false,false,false));
        assertFalse(ClothingPolicy.allows(true,false,true,false,false,false));
        assertFalse(ClothingPolicy.allows(true,false,false,true,false,false));
        assertFalse(ClothingPolicy.allows(true,false,false,false,true,false));
        assertFalse(ClothingPolicy.allows(true,false,false,false,false,true));
    }
    @Test void rejectsNativeAugmentationComponentsWithoutOptionalDependency(){
        for(String key:new String[]{"affixes","rarity","affix_name","sockets","socketed_gems","durability_bonus","malice_marker","touched_by_malice"})
            assertTrue(ClothingPolicy.augmentationComponent("apotheosis:"+key));
        assertFalse(ClothingPolicy.augmentationComponent("minecraft:custom_name"));
        assertFalse(ClothingPolicy.augmentationComponent("tyr_progression:tartan_palette"));
    }
}
