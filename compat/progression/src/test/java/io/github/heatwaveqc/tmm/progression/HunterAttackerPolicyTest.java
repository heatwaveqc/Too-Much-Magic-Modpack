package io.github.heatwaveqc.tmm.progression;

import io.github.heatwaveqc.tmm.progression.foundation.HunterAttackerPolicy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HunterAttackerPolicyTest {
    private enum Actor { PLAYER, OTHER_PLAYER, BLADE, ARROW, WOLF, SERVANT, FAMILIAR, FAKE_PLAYER, OFFLINE, CREATIVE, SPECTATOR }
    private static boolean eligible(Actor actor){return actor==Actor.PLAYER||actor==Actor.OTHER_PLAYER;}
    private static Actor resolve(Actor causing,Actor direct){
        return HunterAttackerPolicy.resolve(causing,direct,HunterAttackerPolicyTest::eligible,
            ()->{fail("Native entity must not fall back to a magical scope");return null;});
    }
    @Test void nativePlayerDamageIncludesWeaponsAndSpells(){
        assertSame(Actor.PLAYER,resolve(Actor.PLAYER,Actor.PLAYER));
        assertSame(Actor.PLAYER,resolve(Actor.PLAYER,Actor.ARROW));
        assertSame(Actor.PLAYER,resolve(Actor.PLAYER,Actor.BLADE));
        assertSame(Actor.PLAYER,resolve(Actor.PLAYER,null));
        assertSame(Actor.PLAYER,resolve(null,Actor.PLAYER));
        assertSame(Actor.PLAYER,resolve(Actor.PLAYER,Actor.OTHER_PLAYER));
    }
    @Test void independentCreatureAttacksNeverFollowOwnership(){
        for(Actor creature:new Actor[]{Actor.WOLF,Actor.SERVANT,Actor.FAMILIAR}){
            assertNull(resolve(creature,creature));
            assertNull(resolve(creature,Actor.ARROW));
            assertNull(resolve(creature,Actor.PLAYER));
            assertNull(resolve(null,creature));
        }
        assertNull(resolve(null,Actor.BLADE)); // no native owner; needs separate exact evidence
    }
    @Test void invalidNativePlayersNeverFallBack(){
        for(Actor invalid:new Actor[]{Actor.FAKE_PLAYER,Actor.OFFLINE,Actor.CREATIVE,Actor.SPECTATOR}){
            assertNull(resolve(invalid,Actor.ARROW));
            assertNull(resolve(invalid,Actor.PLAYER));
            assertNull(resolve(null,invalid));
        }
    }
    @Test void exactScopeOnlyForOwnerlessDamage(){
        assertSame(Actor.PLAYER,HunterAttackerPolicy.resolve(null,null,HunterAttackerPolicyTest::eligible,()->Actor.PLAYER));
        assertNull(HunterAttackerPolicy.resolve(null,null,HunterAttackerPolicyTest::eligible,()->null));
        assertNull(HunterAttackerPolicy.resolve(null,null,HunterAttackerPolicyTest::eligible,()->Actor.FAKE_PLAYER));
        assertNull(HunterAttackerPolicy.resolve(null,null,HunterAttackerPolicyTest::eligible,()->Actor.OFFLINE));
    }
}
