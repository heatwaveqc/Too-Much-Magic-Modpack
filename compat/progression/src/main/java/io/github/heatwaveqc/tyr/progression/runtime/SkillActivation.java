package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.data.ActivationSpec;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import net.minecraft.server.level.ServerPlayer;
import java.util.UUID;
import java.nio.charset.StandardCharsets;

/** Server-authorized timed/toggle gate over effects of ordinary owned skill nodes. */
public final class SkillActivation {
    private SkillActivation(){}
    private static final ThreadLocal<Boolean> REQUESTING=ThreadLocal.withInitial(()->false);
    private static String key(String path,String skill){
        return "tyr_progression:activation/"+UUID.nameUUIDFromBytes((path+"|"+skill).getBytes(StandardCharsets.UTF_8));
    }
    private static long now(ServerPlayer p){return p.server.overworld().getGameTime();}
    private static ActivationSpec.State get(ServerPlayer p,String path,String skill){
        return ActivationSpec.State.decode(ProgressionApi.mode(p,key(path,skill),"0/0"));
    }
    private static boolean owned(ServerPlayer p,String path,String skill){
        return p.isAlive()&&!p.isCreative()&&!p.isSpectator()&&!NewbieMode.isNewbie(p)
            &&PathRegistry.skillEligible(p,path,skill)&&ProgressionApi.track(p,path).skillRank(skill)>0;
    }
    public static boolean active(ServerPlayer p,String path,String skill){
        return PathRegistry.activation(path,skill)!=null&&owned(p,path,skill)&&get(p,path,skill).active(now(p));
    }
    public static boolean canRequest(ServerPlayer p,String path,String skill){
        var spec=PathRegistry.activation(path,skill);
        if(spec==null||!owned(p,path,skill)||spec.jawrHealOnActivation()>0&&p.getHealth()>=p.getMaxHealth())return false;
        var before=get(p,path,skill);var next=before.request(spec,now(p));
        return !next.equals(before)&&(spec.jawrBloodCost()==0||before.active(now(p))||
            io.github.heatwaveqc.tyr.progression.runtime.forms.NativeBloodCost.affordable(p,spec.jawrBloodCost()));
    }
    public static long remaining(ServerPlayer p,String path,String skill){
        var state=get(p,path,skill);return Math.max(0,state.ready()-now(p));
    }
    public static boolean request(ServerPlayer p,String path,String skill){
        if(p.getServer()==null||!p.getServer().isSameThread()||REQUESTING.get()||!canRequest(p,path,skill))return false;
        REQUESTING.set(true);
        try{
            var spec=PathRegistry.activation(path,skill);
            var before=get(p,path,skill);long at=now(p);var next=before.request(spec,at);
            if(next.equals(before))return false;
            // Paid timed activation only. No spell credit here: the existing native
            // BloodPoolHandler receipt is the sole source of eligible MXP.
            if(spec.jawrBloodCost()>0&&!before.active(at)&&
                !io.github.heatwaveqc.tyr.progression.runtime.forms.NativeBloodCost.pay(p,spec.jawrBloodCost()))return false;
            if(!owned(p,path,skill))return false;
            if(!ProgressionApi.setMode(p,key(path,skill),next.encode()))return false;
            if(spec.jawrHealOnActivation()>0&&!before.active(at)&&p.isAlive())
                p.heal((float)Math.min(spec.jawrHealOnActivation(),Math.max(0,p.getMaxHealth()-p.getHealth())));
            return true;
        }finally{REQUESTING.remove();}
    }
    public static void stopPath(ServerPlayer p,String path){
        var tree=PathRegistry.get(path);if(tree==null)return;
        for(var skill:tree.skills())if(PathRegistry.activation(path,skill.id())!=null)stop(p,path,skill.id());
    }
    private static void stop(ServerPlayer p,String path,String skill){
        var before=get(p,path,skill);
        if(before.until()!=0)ProgressionApi.setMode(p,key(path,skill),before.stop().encode());
    }
    public static void reconcile(ServerPlayer p){
        for(var tree:PathRegistry.all().values())for(var skill:tree.skills()){
            if(PathRegistry.activation(tree.id(),skill.id())==null)continue;
            var state=get(p,tree.id(),skill.id());
            if(state.until()!=0&&(!state.active(now(p))||!owned(p,tree.id(),skill.id())))stop(p,tree.id(),skill.id());
        }
    }
    public static void stopAll(ServerPlayer p){for(var path:PathRegistry.all().keySet())stopPath(p,path);}
}
