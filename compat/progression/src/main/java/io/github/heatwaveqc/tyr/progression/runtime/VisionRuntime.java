package io.github.heatwaveqc.tyr.progression.runtime;
import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.data.MechanicalSpec;
import io.github.heatwaveqc.tyr.progression.network.VisionNetwork;
import net.minecraft.server.level.ServerPlayer;
import java.util.*;

/** Ranked capabilities with persistent player preferences, independent of potion effects. */
public final class VisionRuntime {
    private VisionRuntime(){}
    public static final String NIGHT="enhanced_night_vision",LAVA="lava_vision";
    private record Grant(boolean night,int lava){}
    private static final Map<UUID,Grant> LAST=new HashMap<>();
    private static String key(String type){return "tyr_progression:"+type;}
    private static boolean enabled(ServerPlayer p,EffectRuntime.Active active){
        String fallback=MechanicalSpec.bool(active.effect().params(),"default_enabled",true)?"on":"off";
        return ProgressionApi.mode(p,key(active.effect().type()),fallback).equals("on");
    }
    private static Grant grant(ServerPlayer player){
        if(!player.isAlive()||player.isCreative()||player.isSpectator()||NewbieMode.isNewbie(player))return new Grant(false,0);
        boolean night=false;int lava=0;
        for(var a:EffectRuntime.active(player)){
            var type=a.effect().type();
            if(!type.equals(NIGHT)&&!type.equals(LAVA))continue;
            if(!enabled(player,a))continue;
            if(type.equals(NIGHT))night=true;
            else lava=Math.max(lava,Math.max(1,(int)Math.ceil(MechanicalSpec.scaled(a.effect().amountPerRank(),a.rank(),128))));
        }
        return new Grant(night,lava);
    }
    public static boolean toggle(ServerPlayer p,String type){
        if(!type.equals(NIGHT)&&!type.equals(LAVA))return false;
        var matches=EffectRuntime.active(p).stream().filter(a->a.effect().type().equals(type)).toList();
        if(matches.isEmpty()||p.isCreative()||p.isSpectator()||NewbieMode.isNewbie(p))return false;
        boolean wasOn=matches.stream().anyMatch(a->enabled(p,a));
        if(!ProgressionApi.setMode(p,key(type),wasOn?"off":"on"))return false;
        reconcile(p,true);return true;
    }
    public static String mode(ServerPlayer p,String type){
        return EffectRuntime.active(p).stream().anyMatch(a->a.effect().type().equals(type)&&enabled(p,a))?"on":"off";
    }
    public static void reconcile(ServerPlayer p,boolean force){
        Grant next=grant(p),prior=LAST.put(p.getUUID(),next);
        if(force||!next.equals(prior)||p.tickCount%20==0&&(next.night||next.lava>0))
            VisionNetwork.send(p,next.night,next.lava);
    }
    public static void forget(ServerPlayer p){LAST.remove(p.getUUID());}
}
