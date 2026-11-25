package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.runtime.RecentBlockCredits;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class RecentBlockCreditsTest {
    @Test void overlappingProducerReceiptsCannotReplayAnOlderTick(){
        UUID player=UUID.randomUUID();
        try{
            assertTrue(RecentBlockCredits.first(player,"test:level",1,100));
            assertFalse(RecentBlockCredits.first(player,"test:level",1,100));
            assertTrue(RecentBlockCredits.first(player,"test:level",1,101));
            assertFalse(RecentBlockCredits.first(player,"test:level",1,100));
            assertFalse(RecentBlockCredits.first(player,"test:level",1,101));
            assertTrue(RecentBlockCredits.first(player,"test:other",1,101));
            assertTrue(RecentBlockCredits.first(player,"test:level",2,101));
        }finally{RecentBlockCredits.forget(player);}
    }
}
