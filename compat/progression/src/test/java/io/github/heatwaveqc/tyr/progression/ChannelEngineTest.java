package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.core.ChannelEngine;
import io.github.heatwaveqc.tyr.progression.data.ChannelConfig;
import io.github.heatwaveqc.tyr.progression.data.PathJson;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.model.XpChannel;
import io.github.heatwaveqc.tyr.progression.state.Ledger;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
class ChannelEngineTest {
    private static JsonObject json(String s){return JsonParser.parseString(s).getAsJsonObject();}
    private static Map<String,XpChannel> systems(){
        var curve=json("{\"schema\":1,\"rank_xp\":[0,20,55],\"points_per_rank\":1,\"sources\":[]}");
        return ChannelConfig.definitions(Map.of("test:a",curve,"test:b",curve,"test:c",curve,"test:d",curve),1.02);
    }
    @Test void independentRegistryAndMixedCurrencies(){
        var systems=systems();assertEquals(5,systems.size());
        var raw=json("""
         {"schema":1,"xp_channel":"tyr_progression:mxp","skills":[
          {"id":"test:magic","base_cost":1,"effects":[]},
          {"id":"test:native","xp_channel":"test:a","level_system":"test:b","required_rank":2,"base_cost":1,"effects":[]}]}
         """);
        var other=json("""
         {"schema":1,"xp_channel":"tyr_progression:mxp","skills":[{"id":"test:other","base_cost":1,"effects":[]}]}
         """);
        var a=PathJson.read("test:tree",ChannelConfig.treeInput(raw,systems));
        var b=PathJson.read("test:second",ChannelConfig.treeInput(other,systems));
        var config=ChannelConfig.bind(systems,Map.of(a.id(),a,b.id(),b),Map.of(a.id(),raw,b.id(),other));
        var mxp=systems.get(XpChannel.MXP);var ledger=ChannelEngine.join(ChannelEngine.join(Ledger.empty(),a,mxp),b,mxp);
        ledger=ChannelEngine.grant(ledger,systems.get("test:a"),20,0);
        ledger=ChannelEngine.grant(ledger,systems.get("test:b"),1,0);
        assertSame(ledger,ChannelEngine.buy(ledger,a,config,"test:native"));
        ledger=ChannelEngine.grant(ledger,systems.get("test:b"),19,0);
        ledger=ChannelEngine.magic(ledger,mxp,"tx:1","boss:first",1000,100,true,false);
        ledger=ChannelEngine.buy(ledger,a,config,"test:magic");
        assertEquals(0,ChannelEngine.view(ledger,b,config).points());
        assertSame(ledger,ChannelEngine.buy(ledger,b,config,"test:other"));
        ledger=ChannelEngine.buy(ledger,a,config,"test:native");
        assertEquals(1,ledger.wallet("test:a").points());
        assertEquals(2,ledger.wallet("test:b").points());
        assertEquals(1,ledger.get(a.id()).skillRank("test:native"));
        ledger=ChannelEngine.refund(ledger,a);
        assertEquals(1,ledger.wallet(mxp.id()).points());
        assertEquals(2,ledger.wallet("test:a").points());
        assertEquals(0,ledger.get(a.id()).skillRank("test:native"));
        ledger=ChannelEngine.refund(ledger,a);
        assertEquals(1,ledger.wallet(mxp.id()).points());
    }
    @Test void magicFractionsAndDedup(){
        var mxp=XpChannel.mxp(1.02);var ledger=Ledger.empty();
        ledger=ChannelEngine.magic(ledger,mxp,"tx:1",null,1,999.5,false,false);
        ledger=ChannelEngine.magic(ledger,mxp,"tx:2",null,1,1,false,false);
        assertEquals(1,ledger.wallet(mxp.id()).magicLevels());
        assertEquals(.5/1.02,ledger.wallet(mxp.id()).magicProgress(),1e-8);
        assertSame(ledger,ChannelEngine.magic(ledger,mxp,"tx:2",null,1,1,false,false));
        ledger=ChannelEngine.magic(ledger,mxp,"tx:3","boss:first",1000,100,true,false);
        assertEquals(2,ledger.wallet(mxp.id()).points());
        ledger=ChannelEngine.magic(ledger,mxp,"tx:4","boss:first",1000,100,true,false);
        assertEquals(2,ledger.wallet(mxp.id()).magicLevels());
    }
    @Test void unknownCurrencyFailsClosed(){
        var systems=systems();
        assertThrows(IllegalArgumentException.class,()->ChannelConfig.treeInput(json("{\"schema\":1,\"xp_channel\":\"test:unknown\"}"),systems));
        assertThrows(IllegalArgumentException.class,()->ChannelConfig.definitions(Map.of(XpChannel.MXP,json("{}")),1.02));
    }
}
