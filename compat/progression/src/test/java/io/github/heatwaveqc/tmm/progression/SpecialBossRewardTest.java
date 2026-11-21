package io.github.heatwaveqc.tmm.progression;

import io.github.heatwaveqc.tmm.progression.foundation.BossRewardPolicy;
import io.github.heatwaveqc.tmm.progression.core.ChannelEngine;
import io.github.heatwaveqc.tmm.progression.model.XpChannel;
import io.github.heatwaveqc.tmm.progression.state.Ledger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SpecialBossRewardTest {
    @Test void exactVariantsNeverAuthorizeOrdinaryOrUnknownTypes(){
        assertNull(BossRewardPolicy.foremanKey(false));
        assertEquals("neovitae:foreman",BossRewardPolicy.foremanKey(true));
        assertNull(BossRewardPolicy.nullServantKey(0));
        assertNull(BossRewardPolicy.nullServantKey(-1));
        assertNull(BossRewardPolicy.nullServantKey(4));
        assertEquals("voidscape:null_servant/titanite",BossRewardPolicy.nullServantKey(1));
        assertEquals("voidscape:null_servant/ichor",BossRewardPolicy.nullServantKey(2));
        assertEquals("voidscape:null_servant/astral",BossRewardPolicy.nullServantKey(3));
    }
    @Test void variantFirstsSeparateAndChaosRepeatFixed(){
        var channel=XpChannel.mxp(1.02);var state=Ledger.empty();
        for(int i=1;i<=3;i++)state=ChannelEngine.magic(state,channel,"test:variant/"+i,"boss:"+BossRewardPolicy.nullServantKey(i),1000,1000,true,false);
        assertEquals(3,state.wallet(channel.id()).magicLevels());
        state=ChannelEngine.magic(state,channel,"test:chaos/1","boss:draconicevolution:draconic_guardian",1000,100,true,true);
        assertEquals(4,state.wallet(channel.id()).magicLevels());
        state=ChannelEngine.magic(state,channel,"test:chaos/2","boss:draconicevolution:draconic_guardian",1000,100,true,true);
        assertEquals(100,state.wallet(channel.id()).magicProgress(),1e-8);
        assertSame(state,ChannelEngine.magic(state,channel,"test:chaos/2","boss:draconicevolution:draconic_guardian",1000,100,true,true));
    }
}
