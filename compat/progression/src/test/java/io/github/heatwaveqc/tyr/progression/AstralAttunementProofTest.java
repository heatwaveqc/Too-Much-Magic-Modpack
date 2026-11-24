package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.mxp.core.AstralAttunementProof;
import io.github.heatwaveqc.tyr.progression.mxp.core.RitualRewards;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class AstralAttunementProofTest {
    @Test void singleFirstOnlyPersonalFamily(){
        UUID a=UUID.randomUUID(),b=UUID.randomUUID();
        var receipt=AstralAttunementProof.receipt(a,true,true);
        assertEquals(3000,receipt.rule().band().first());
        assertEquals(0,receipt.rule().band().repeat());
        assertEquals(AstralAttunementProof.ID,receipt.rule().family());
        assertEquals(receipt.transaction(),AstralAttunementProof.receipt(a,true,true).transaction());
        assertNotEquals(receipt.transaction(),AstralAttunementProof.receipt(b,true,true).transaction());
        assertNull(AstralAttunementProof.receipt(a,false,true));
        assertNull(AstralAttunementProof.receipt(a,true,false));
        assertNull(AstralAttunementProof.receipt(null,true,true));
        assertEquals(1,RitualRewards.rules().keySet().stream().filter(id->id.startsWith("astralsorcery:")).count());
    }
}
