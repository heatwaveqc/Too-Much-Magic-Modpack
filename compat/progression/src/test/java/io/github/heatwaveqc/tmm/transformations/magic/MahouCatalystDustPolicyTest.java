package io.github.heatwaveqc.tmm.transformations.magic;

import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MahouCatalystDustPolicyTest {
    @Test void onlyFiveExactNeoVitaeDusts() {
        assertEquals(Set.of("neovitae:iron_dust","neovitae:gold_dust","neovitae:diamond_dust","neovitae:emerald_dust","neovitae:quartz_dust"), MahouCatalystDustPolicy.acceptedInputs());
        for (String material : Set.of("iron","gold","diamond","emerald","quartz")) {
            assertEquals("mahoutsukai:powdered_" + material, MahouCatalystDustPolicy.nativePowder("neovitae:" + material + "_dust"));
        }
    }
    @Test void unrelatedAndNativeInputsRemainUntouched() {
        for (String id : Set.of("mahoutsukai:powdered_iron","mahoutsukai:powdered_ender","mahoutsukai:powdered_eye","minecraft:iron_ingot","neovitae:copper_dust","other:iron_dust","minecraft:air")) assertNull(MahouCatalystDustPolicy.nativePowder(id));
    }
}
