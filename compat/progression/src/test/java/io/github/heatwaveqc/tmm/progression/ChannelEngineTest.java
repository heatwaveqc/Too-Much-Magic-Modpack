package io.github.heatwaveqc.tmm.progression;

import io.github.heatwaveqc.tmm.progression.core.ChannelEngine;
import io.github.heatwaveqc.tmm.progression.core.ProgressionMath;
import io.github.heatwaveqc.tmm.progression.data.ChannelConfig;
import io.github.heatwaveqc.tmm.progression.data.Eligibility;
import io.github.heatwaveqc.tmm.progression.foundation.Condition;
import io.github.heatwaveqc.tmm.progression.model.Definition;
import io.github.heatwaveqc.tmm.progression.model.XpChannel;
import io.github.heatwaveqc.tmm.progression.state.Ledger;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ChannelEngineTest {
    private static Definition.Path path(String id) {
        return new Definition.Path(id,"",List.of(0L,20L,55L),1,List.of(),List.of(),List.of(
            new Definition.Skill("test:skill",3,1,1,0,List.of(),List.of(),List.of(),"",List.of(),List.of()),
            new Definition.Skill("test:auto",2,1,0,0,List.of(),List.of(),List.of(),"",List.of(1,2),List.of())));
    }
    @Test void differentTreesUseOneBalanceAndIndependentRanks() {
        var a=path("test:a"); var b=path("test:b"); var c=path("test:c");
        var shared=new XpChannel("test:shared",a,1); var other=new XpChannel("test:other",c,1);
        var ledger=ChannelEngine.join(Ledger.empty(),a,shared);
        ledger=ChannelEngine.join(ledger,b,shared);
        ledger=ChannelEngine.join(ledger,c,other);
        assertEquals(1,ChannelEngine.view(ledger,a,shared).points());
        assertEquals(1,ChannelEngine.view(ledger,b,shared).points());
        var v=ChannelEngine.view(ledger,a,shared);
        ledger=ChannelEngine.commit(ledger,a,shared,ProgressionMath.grantXp(a,v,20));
        assertEquals(2,ChannelEngine.view(ledger,b,shared).rank());
        assertEquals(2,ChannelEngine.view(ledger,b,shared).skillRank("test:auto"));
        v=ChannelEngine.view(ledger,a,shared);
        ledger=ChannelEngine.commit(ledger,a,shared,ProgressionMath.buy(a,v,"test:skill"));
        assertEquals(1,ChannelEngine.view(ledger,b,shared).points());
        assertEquals(0,ChannelEngine.view(ledger,b,shared).skillRank("test:skill"));
        v=ChannelEngine.view(ledger,b,shared);
        ledger=ChannelEngine.commit(ledger,b,shared,ProgressionMath.buy(b,v,"test:skill"));
        assertEquals(0,ChannelEngine.view(ledger,a,shared).points());
        assertEquals(1,ChannelEngine.view(ledger,c,other).points());
        v=ChannelEngine.view(ledger,a,shared);
        ledger=ChannelEngine.commit(ledger,a,shared,ProgressionMath.refundAll(a,v));
        assertEquals(1,ChannelEngine.view(ledger,b,shared).points());
        assertEquals(1,ChannelEngine.view(ledger,b,shared).skillRank("test:skill"));
        assertEquals(0,ledger.get(a.id()).xp());
    }
    @Test void mxpFractionsAndFixedFirstsAndSharedSpend() {
        var a=path("test:a"); var b=path("test:b");
        var magic=new XpChannel("test:magic",null,1.02);
        var ledger=ChannelEngine.join(ChannelEngine.join(Ledger.empty(),a,magic),b,magic);
        assertEquals(0,ChannelEngine.view(ledger,a,magic).points());
        ledger=ChannelEngine.magic(ledger,magic,"tx:1",null,1,999.5,false,false);
        assertEquals(999.5,ledger.wallet(magic.id()).magicProgress(),1e-8);
        ledger=ChannelEngine.magic(ledger,magic,"tx:2",null,1,1,false,false);
        assertEquals(1,ledger.wallet(magic.id()).magicLevels());
        assertEquals(.5/1.02,ledger.wallet(magic.id()).magicProgress(),1e-8);
        assertSame(ledger,ChannelEngine.magic(ledger,magic,"tx:2",null,1,1,false,false));
        ledger=ChannelEngine.magic(ledger,magic,"tx:3","boss:first",1000,100,true,false);
        assertEquals(2,ledger.wallet(magic.id()).points());
        var v=ChannelEngine.view(ledger,a,magic);
        ledger=ChannelEngine.commit(ledger,a,magic,ProgressionMath.buy(a,v,"test:skill"));
        assertEquals(1,ChannelEngine.view(ledger,b,magic).points());
        assertEquals(2,ledger.wallet(magic.id()).magicLevels());
        ledger=ChannelEngine.magic(ledger,magic,"tx:4","boss:first",1000,100,true,false);
        assertEquals(2,ledger.wallet(magic.id()).magicLevels());
    }
    private static JsonObject json(String value) { return JsonParser.parseString(value).getAsJsonObject(); }
    @Test void validatesThreeChannelLimitAndSharedSourceBinding() {
        var a=path("test:a"); var b=path("test:b");
        var paths=Map.of(a.id(),a,b.id(),b);
        var raw=Map.of(a.id(),json("{\"xp_channel\":\"test:m\"}"),b.id(),json("{\"xp_channel\":\"test:m\"}"));
        var ok=ChannelConfig.read(json("{\"schema\":1,\"channels\":{\"test:m\":{\"type\":\"mxp\"}}}"),paths,raw);
        assertSame(ok.paths().get(a.id()),ok.paths().get(b.id()));
        var tooMany=json("{\"schema\":1,\"channels\":{\"test:a\":{},\"test:b\":{},\"test:c\":{},\"test:d\":{}}}");
        assertThrows(IllegalArgumentException.class,()->ChannelConfig.read(tooMany,paths,raw));
        assertThrows(IllegalArgumentException.class,()->ChannelConfig.read(json("{\"schema\":1,\"channels\":{\"test:m\":{\"type\":\"path_curve\",\"source_path\":\"unknown:path\"}}}"),paths,raw));
    }
    @Test void missingFlagsCannotBeTurnedIntoPermissionByNegation() {
        var missing=Eligibility.parse(json("{\"type\":\"not\",\"condition\":{\"type\":\"flag\",\"id\":\"test:never_registered\"}}"));
        assertEquals(Condition.Result.UNAVAILABLE,missing.evaluate(null));
        assertFalse(missing.test(null));
        assertThrows(IllegalArgumentException.class,()->Eligibility.parse(json("{\"type\":\"bad\"}")));
    }
}
