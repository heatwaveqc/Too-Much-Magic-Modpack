package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.foundation.BossRewardPolicy;
import io.github.heatwaveqc.tyr.progression.foundation.BossRewardPolicy.Tier;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class BossRewardPolicyTest {
    @Test void independentCreditWindowIncludesExactlyRecentPlayersAndFinalBlow(){
        UUID a=UUID.randomUUID(),b=UUID.randomUUID(),stale=UUID.randomUUID(),future=UUID.randomUUID(),finish=UUID.randomUUID();
        assertEquals(Set.of(a,b,finish),BossRewardPolicy.recent(Map.of(a,100L,b,200L,stale,99L,future,201L),200,100,finish));
        assertEquals(Set.of(a),BossRewardPolicy.recent(Map.of(a,200L),200,100,a));
    }
    @Test void nativeInvadersAndElitesOnly(){
        assertEquals(0.0,BossRewardPolicy.eliteMultiplier(false,false,"apotheosis:mythic"));
        assertEquals(1.0,BossRewardPolicy.eliteMultiplier(false,true,""));
        assertEquals(1.0,BossRewardPolicy.eliteMultiplier(true,false,"apotheosis:uncommon"));
        assertEquals(2.0,BossRewardPolicy.eliteMultiplier(true,false,"apotheosis:rare"));
        assertEquals(3.0,BossRewardPolicy.eliteMultiplier(true,false,"apotheosis:epic"));
        assertEquals(4.0,BossRewardPolicy.eliteMultiplier(true,false,"apotheosis:mythic"));
        assertEquals(0.0,BossRewardPolicy.eliteMultiplier(true,true,"unrecognized"));
    }
    @Test void tierPrecedenceAndBlockedBossesNeverFallThrough(){
        assertEquals(Tier.NONE,BossRewardPolicy.tier(true,true,true,4));
        assertEquals(Tier.STANDARD,BossRewardPolicy.tier(false,true,true,4));
        assertEquals(Tier.MINIBOSS,BossRewardPolicy.tier(false,false,true,4));
        assertEquals(Tier.ELITE,BossRewardPolicy.tier(false,false,false,4));
        assertEquals(Tier.NONE,BossRewardPolicy.tier(false,false,false,0));
    }
}
