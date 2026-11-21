package io.github.heatwaveqc.tmm.progression.runtime.forms;

import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import io.github.heatwaveqc.tmm.progression.foundation.LichSkillMath;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpReflect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import java.util.function.BooleanSupplier;

/** Player-aware scope around native Arca cap reads. Never writes the global config or portable Totem. */
public final class LichSoulCapacity {
    private static final ThreadLocal<Scope> ACTIVE=new ThreadLocal<>();
    private static final String KEY="tmm_progression:lich_arca_capacity";
    private record Scope(ServerPlayer p,Object capability,int base,int maximum){}
    private LichSoulCapacity(){}
    private static int base(){
        try{
            Object cfg=MxpReflect.field(Class.forName("com.Polarice3.Goety.config.MainConfig"),"MaxArcaSouls");
            Object n=MxpReflect.call(cfg,"get");return n instanceof Integer i&&i>0?i:-1;
        }catch(ClassNotFoundException|RuntimeException|LinkageError ex){return -1;}
    }
    private static Scope quote(Player player){
        if(!(player instanceof ServerPlayer p)||!LichEffects.eligible(p)||!LichEffects.arca(p))return null;
        int base=base();if(base<=0)return null;
        int max=LichSkillMath.capacity(base,LichEffects.rate(p,"lich_soul_capacity"));
        Object capability=MxpReflect.staticCall(LichEffects.SE,"getCapability",p);
        return capability==null||max<=base?null:new Scope(p,capability,base,max);
    }
    public static boolean gain(Player player,int amount,BooleanSupplier original){
        Scope s=amount>0?quote(player):null;
        Scope old=ACTIVE.get();
        if(s==null||old!=null)return original.getAsBoolean();
        ACTIVE.set(s);
        try{
            boolean result=original.getAsBoolean();
            if(result&&LichEffects.souls(s.p)>s.base)remember(s);
            return result;
        }finally{ACTIVE.remove();}
    }
    private static void remember(Scope s){
        String marker=Integer.toString(s.maximum);
        if(!marker.equals(ProgressionApi.mode(s.p,KEY,"0")))ProgressionApi.setMode(s.p,KEY,marker);
    }
    public static Object capRead(Object nativeValue,Object capability){
        Scope s=ACTIVE.get();
        return s!=null&&s.capability==capability&&nativeValue instanceof Integer n&&n==s.base?s.maximum:nativeValue;
    }
    public static Object assignmentCap(Object nativeValue,Player player){
        Scope s=quote(player);
        if(s==null||!(nativeValue instanceof Integer n)||n!=s.base)return nativeValue;
        // This is the native setSoulsAmount cap branch; preserve all other native semantics.
        remember(s);return s.maximum;
    }
    public static void tick(ServerPlayer p){
        if(p.tickCount%20!=0||!p.isAlive()||!LichEffects.pinned("goety","3.2.2")||p.getServer()==null||!p.getServer().isSameThread())return;
        int previous;
        try{previous=Integer.parseInt(ProgressionApi.mode(p,KEY,"0"));}catch(NumberFormatException e){return;}
        if(previous<=0||!LichEffects.arca(p))return;
        int base=base(),current=LichEffects.souls(p);
        if(base<=0||current<0||current>previous)return; // foreign/unknown balance: no destructive clamp
        int max=LichSkillMath.capacity(base,LichEffects.rate(p,"lich_soul_capacity"));
        if(current>max){
            MxpReflect.staticCall(LichEffects.SE,"setSESouls",p,max);
            if(LichEffects.souls(p)!=max)return;
            MxpReflect.staticCall(LichEffects.SE,"sendSEUpdatePacket",p);
        }
        String marker=max>base&&current>base?Integer.toString(max):"0";
        if(!marker.equals(Integer.toString(previous)))ProgressionApi.setMode(p,KEY,marker);
    }
}
