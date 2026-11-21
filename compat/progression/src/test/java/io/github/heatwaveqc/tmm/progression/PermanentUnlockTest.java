package io.github.heatwaveqc.tmm.progression;
import io.github.heatwaveqc.tmm.progression.core.ChannelEngine;
import io.github.heatwaveqc.tmm.progression.data.ChannelConfig;
import io.github.heatwaveqc.tmm.progression.data.PathJson;
import io.github.heatwaveqc.tmm.progression.model.XpChannel;
import io.github.heatwaveqc.tmm.progression.state.Ledger;
import com.google.gson.JsonParser;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PermanentUnlockTest{
 @Test void purchaseCreditsNativeOnceAndSurvivesRespec(){
   var nativeDef=JsonParser.parseString("{\"schema\":1,\"rank_xp\":[0,20,30],\"points_per_rank\":1}").getAsJsonObject();
   var systems=ChannelConfig.definitions(Map.of("test:native",nativeDef),1.02);
   var raw=JsonParser.parseString("""
      {"schema":1,"xp_channel":"test:native","skills":[
       {"id":"test:focus","max_rank":2,"xp_channel":"tmm_progression:mxp","level_system":"test:native",
        "rank_costs":[1,1],"rank_level_requirements":[1,3],"refundable":false,
        "rank_point_award":{"system":"test:native","points":1}},
       {"id":"test:normal","base_cost":1}]}
   """).getAsJsonObject();
   var path=PathJson.read("test:path",ChannelConfig.treeInput(raw,systems));
   var config=ChannelConfig.bind(systems,Map.of(path.id(),path),Map.of(path.id(),raw));
   var mxp=systems.get(XpChannel.MXP);var nativeChannel=systems.get("test:native");
   var l=ChannelEngine.join(Ledger.empty(),path,nativeChannel);
   l=ChannelEngine.magic(l,mxp,"test:1","test:boss",2000,1,true,false);
   l=ChannelEngine.buy(l,path,config,"test:focus");assertEquals(2,l.wallet("test:native").points());
   assertSame(l,ChannelEngine.buy(l,path,config,"test:focus"));
   l=ChannelEngine.buy(l,path,config,"test:normal");
   l=ChannelEngine.refund(l,path);assertEquals(1,l.wallet(mxp.id()).points());
   assertEquals(1,l.get(path.id()).skillRank("test:focus"));assertEquals(0,l.get(path.id()).skillRank("test:normal"));
   assertEquals(2,l.wallet("test:native").points());
   l=ChannelEngine.grant(l,nativeChannel,30,0);
   l=ChannelEngine.buy(l,path,config,"test:focus");assertEquals(2,l.get(path.id()).skillRank("test:focus"));assertEquals(5,l.wallet("test:native").points());
   l=ChannelEngine.refund(l,path);assertEquals(0,l.wallet(mxp.id()).points());assertEquals(2,l.get(path.id()).skillRank("test:focus"));
   assertSame(l,ChannelEngine.buy(l,path,config,"test:focus"));
 }
}
