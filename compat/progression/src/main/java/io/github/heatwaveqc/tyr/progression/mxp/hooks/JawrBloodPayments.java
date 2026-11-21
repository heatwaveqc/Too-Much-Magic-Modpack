package io.github.heatwaveqc.tyr.progression.mxp.hooks;

import io.github.heatwaveqc.tyr.progression.mxp.MxpSourceConfig;
import io.github.heatwaveqc.tyr.progression.runtime.forms.JawrFormState;
import io.github.heatwaveqc.tyr.progression.mxp.core.ObservedPayment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

/** Observe the native vampire blood ledger, never mirror food or change physiology. */
public final class JawrBloodPayments {
    private static final String ATTACHMENT="dev.sterner.witchery.feature.blood_pool.BloodPoolAttachment";
    private static final ThreadLocal<Scope> ACTIVE=new ThreadLocal<>();
    private record Blood(int current,int maximum){}
    private static final class Scope { final ServerPlayer player; boolean valid=true; Scope(ServerPlayer p){player=p;} }
    private JawrBloodPayments(){}
    private static Blood read(ServerPlayer player){
        Object data=MxpReflect.staticCall(ATTACHMENT,"getData",player);
        Object current=MxpReflect.call(data,"getBloodPool");
        Object max=MxpReflect.call(data,"getMaxBlood");
        if(!(current instanceof Integer c)||!(max instanceof Integer m)||m<=0||c<0||c>m)return null;
        return new Blood(c,m);
    }
    public static void decrease(LivingEntity entity,int amount,Runnable original){
        Scope outer=ACTIVE.get();
        if(outer!=null){outer.valid=false;original.run();return;}
        if(!(entity instanceof ServerPlayer p)||amount<=0||!NativePaidSources.eligible(p)||
            MxpSourceConfig.JAWR_INITIAL_BLOOD_POOL.get()<=0||!JawrFormState.present(p,"vampire")||
            io.github.heatwaveqc.tyr.progression.runtime.forms.VampireEffects.externalExtraction()){
            original.run();return;
        }
        Blood before=read(p);
        if(before==null){original.run();return;}
        Scope s=new Scope(p);ACTIVE.set(s);
        try{
            original.run();
            Blood after=read(p);
            if(!s.valid||after==null||before.maximum!=after.maximum||!JawrFormState.present(p,"vampire"))return;
            double debit=ObservedPayment.delta(before.current,after.current);
            if(debit>0){
                // Only a fully paid discounted self-cost can claim undiscounted nominal practice.
                double nominal=debit==amount?io.github.heatwaveqc.tyr.progression.runtime.forms.VampireEffects.nominal(p,amount):debit;
                NativePaidSources.credit(p,"witchery:player_blood",nominal,MxpSourceConfig.JAWR_INITIAL_BLOOD_POOL.get(),debit);
            }
        }finally{ACTIVE.remove();}
    }
}
