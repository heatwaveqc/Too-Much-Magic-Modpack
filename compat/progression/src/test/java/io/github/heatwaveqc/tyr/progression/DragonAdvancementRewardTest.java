package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.core.ChannelEngine;
import io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy;
import io.github.heatwaveqc.tyr.progression.foundation.BossRewardPolicy;
import io.github.heatwaveqc.tyr.progression.model.XpChannel;
import io.github.heatwaveqc.tyr.progression.state.Ledger;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class DragonAdvancementRewardTest {
    private static final String KILL="minecraft:end/kill_dragon", RESPAWN="minecraft:end/respawn_dragon";
    private static final XpChannel MXP=XpChannel.mxp(1.02);
    private static final String SOURCE="tyr_progression:default";

    private static Ledger seed(String xp, Set<String> completions) {
        var persisted=new ExperienceEconomy.Ledger(
            Map.of(MXP.id(),new BigDecimal(xp)),List.of(),Set.of(),completions,Set.of());
        return Ledger.empty().withEconomy(persisted);
    }
    private static Ledger claim(Ledger ledger,String id,String tx){
        String key=BossRewardPolicy.dragonAdvancementKey(id);
        return key==null?ledger:ChannelEngine.magic(ledger,MXP,tx,key,1000,0,true,false);
    }

    @Test void exactlyTwoFixedAwardsEvenAtHighCurrentLevel(){
        var initial=seed("100125",Set.of());
        var first=claim(initial,KILL,"advancement:"+KILL);
        assertEquals(101,first.wallet(MXP.id()).magicLevels());
        assertEquals(125,first.wallet(MXP.id()).magicProgress(),1e-8);
        assertEquals(101,first.wallet(MXP.id()).points());
        assertSame(first,claim(first,KILL,"different:tx"));
        var second=claim(first,RESPAWN,"advancement:"+RESPAWN);
        assertEquals(102,second.wallet(MXP.id()).magicLevels());
        assertEquals(125,second.wallet(MXP.id()).magicProgress(),1e-8);
        assertEquals(102,second.wallet(MXP.id()).points());
        assertSame(second,claim(second,RESPAWN,"different:respawn"));
        assertSame(second,claim(second,KILL,"different:kill"));
        assertEquals(Set.of("boss:minecraft:ender_dragon","advancement:"+RESPAWN),
            Set.copyOf(second.wallet(MXP.id()).firstCompletions()));
    }

    @Test void unrelatedAdvancementsDoNotPayAndClaimReceiptCannotReplay(){
        assertNull(BossRewardPolicy.dragonAdvancementKey("minecraft:end/dragon_breath"));
        assertNull(BossRewardPolicy.dragonAdvancementKey("other:end/kill_dragon"));
        var initial=seed("1000",Set.of(MXP.id()+"|"+SOURCE+"|boss:minecraft:ender_dragon"));
        assertSame(initial,claim(initial,KILL,"advancement:"+KILL));
        assertSame(initial,claim(initial,"minecraft:end/dragon_breath","unrelated"));
        assertEquals(2,claim(initial,RESPAWN,"advancement:"+RESPAWN).wallet(MXP.id()).magicLevels());
    }
}
