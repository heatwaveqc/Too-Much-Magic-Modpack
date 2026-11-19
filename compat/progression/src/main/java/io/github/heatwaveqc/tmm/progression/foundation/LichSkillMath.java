package io.github.heatwaveqc.tmm.progression.foundation;

/** Engineering ceilings, not authored skill ranks or power goals. */
public final class LichSkillMath {
    private LichSkillMath(){}
    private static double cap(double rate,double max){return !Double.isFinite(rate)?0:Math.max(0,Math.min(max,rate));}
    public static int capacity(int base,double rate){
        if(base<=0||base>=Integer.MAX_VALUE/2||cap(rate,1)==0)return base;
        return (int)Math.min(Integer.MAX_VALUE/2L,Math.min((long)Integer.MAX_VALUE,(long)Math.floor(base*(1+cap(rate,1)))));
    }
    public static int gain(int nativeGain,double rate){
        if(nativeGain<=0||nativeGain>=Integer.MAX_VALUE/2||cap(rate,1)==0)return nativeGain;
        return (int)Math.min(Integer.MAX_VALUE/2L,Math.floor(nativeGain*(1+cap(rate,1))));
    }
    /** Add a bounded flat limit without reducing zero/negative/large native caps. */
    public static int servantLimit(int nativeLimit,double extra){
        if(nativeLimit<=0||nativeLimit>=64||!Double.isFinite(extra)||extra<=0)return nativeLimit;
        return (int)Math.min(64,nativeLimit+Math.min(16,Math.floor(extra)));
    }
    public static int rebate(int paid,double rate){
        if(paid<=1)return 0;
        return (int)Math.min(paid-1,Math.floor(paid*cap(rate,.25)));
    }
    public static float damage(float amount,double attack,double protection){
        if(!Float.isFinite(amount)||amount<=0||amount>=Float.MAX_VALUE/2)return amount;
        return (float)Math.min(1_000_000,amount*(1+cap(attack,1))*(1-cap(protection,.5)));
    }
    public static boolean rescue(boolean alreadyCanceled,boolean canceled,boolean nativeRescue,boolean paid,float health){
        return !alreadyCanceled&&canceled&&nativeRescue&&paid&&Float.isFinite(health)&&health>0;
    }
}
