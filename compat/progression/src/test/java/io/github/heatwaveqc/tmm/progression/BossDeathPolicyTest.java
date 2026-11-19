package io.github.heatwaveqc.tmm.progression;

import io.github.heatwaveqc.tmm.progression.foundation.BossDeathPolicy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BossDeathPolicyTest {
    @Test void encounterSpecificAndDerivativeIdsNeverSettleGenericDeath() {
        for (String id : new String[] {"botania:pink_wither", "neovitae:daemonium_doloris",
                "the_bumblezone:cosmic_crystal_entity", "twilightforest:knight_phantom",
                "voidscape:null_servant", "goety:vizier_clone", "twilightforest:plateau_boss"}) {
            assertFalse(BossDeathPolicy.permitsGenericDeath(id), id);
        }
        assertTrue(BossDeathPolicy.permitsGenericDeath("minecraft:wither"));
        assertTrue(BossDeathPolicy.permitsGenericDeath("minecraft:ender_dragon"));
    }
}
