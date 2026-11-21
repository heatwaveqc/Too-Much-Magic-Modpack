package io.github.heatwaveqc.tyr.progression.runtime.forms;

import io.github.heatwaveqc.tyr.progression.foundation.LichSkillMath;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpPlayerPolicy;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpReflect;
import io.github.heatwaveqc.tyr.progression.runtime.EffectRuntime;
import io.github.heatwaveqc.tyr.progression.runtime.NewbieMode;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.fml.ModList;

public final class LichEffects {
    public static final String SE="com.Polarice3.Goety.utils.SEHelper";
    private static final String FM="dev.sterner.witchery_forbidden_magic.feature.affliction.lich.";
    private LichEffects(){}
    static boolean pinned(String mod,String version){
        return ModList.get().getModContainerById(mod).filter(c->c.getModInfo().getVersion().toString().startsWith(version)).isPresent();
    }
    public static boolean goety(Player p){
        return pinned("goety","3.2.2")&&Boolean.TRUE.equals(MxpReflect.staticCall("com.Polarice3.Goety.utils.LichdomHelper","isLich",p));
    }
    public static boolean forbidden(Player p){
        return pinned("witchery_forbidden_magic","0.5.37")&&Boolean.TRUE.equals(MxpReflect.call(MxpReflect.staticCall(FM+"LichPlayerAttachment","getData",p),"isLich"));
    }
    public static boolean eligible(ServerPlayer p){
        return MxpPlayerPolicy.eligible(p)&&!NewbieMode.isNewbie(p)&&(goety(p)||forbidden(p));
    }
    public static double rate(ServerPlayer p,String type){
        if(!eligible(p))return 0;
        double result=0;
        for(var a:EffectRuntime.active(p))if(a.effect().type().equals(type))result+=a.effect().amountPerRank()*a.rank();
        return Double.isFinite(result)?Math.max(0,result):0;
    }
    public static boolean arca(Player p){
        return pinned("goety","3.2.2")&&Boolean.TRUE.equals(MxpReflect.staticCall(SE,"getSEActive",p))
            &&MxpReflect.staticCall(SE,"getArcaBlock",p) instanceof net.minecraft.core.BlockPos;
    }
    public static int souls(Player p){
        Object n=MxpReflect.staticCall(SE,"getSESouls",p);
        return n instanceof Integer i&&i>=0?i:-1;
    }
    /** Only the native final noncancelled kill-gain price, not transfers or rebates. */
    public static int earned(Player player,int amount,int nominal){
        if(amount<=0||nominal<=0||!(player instanceof ServerPlayer p)||!p.isAlive()||!eligible(p))return amount;
        boolean kill=StackWalker.getInstance().walk(frames->frames.anyMatch(f->f.getClassName().equals(SE)&&f.getMethodName().equals("rawHandleKill")));
        return kill?LichSkillMath.gain(amount,rate(p,"lich_soul_recovery")):amount;
    }
    public static void rescued(ServerPlayer p){
        if(!p.isAlive()||!eligible(p))return;
        double heal=rate(p,"lich_rescue_heal");
        if(heal>0)p.heal((float)Math.min(20,Math.min(heal,Math.max(0,p.getMaxHealth()-p.getHealth()))));
        int duration=0;
        for(var a:EffectRuntime.active(p)){
            if(!a.effect().type().equals("lich_rescue_resistance")||a.effect().amountPerRank()*a.rank()<=0)continue;
            try{duration=Math.max(duration,Integer.parseInt(a.effect().params().getOrDefault("duration_ticks","100")));}
            catch(NumberFormatException ignored){}
        }
        // Resistance I only; no extra death save or removal of native rescue debuffs.
        if(duration>0)p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,Math.min(1200,duration),0));
    }
}
