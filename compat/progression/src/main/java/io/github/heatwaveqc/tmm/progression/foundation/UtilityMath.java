package io.github.heatwaveqc.tmm.progression.foundation;
/** Pure rounding and caps for utility effects; no entitlement or reward side effects. */
public final class UtilityMath {
    private UtilityMath(){}
    public static int bonus(int base,double rate,double roll,int cap){
        if(base<=0||!Double.isFinite(rate)||rate<=0||!Double.isFinite(roll)||roll<0||roll>=1||cap<=0)return 0;
        double raw=Math.min(cap,base*Math.min(2,rate));
        int whole=(int)Math.floor(raw);
        return Math.min(cap,whole+(roll<raw-whole?1:0));
    }
    public static int virtualLevel(double total){return virtualLevel(total,8);}
    public static int virtualLevel(double total,int cap){return (int)Math.min(Math.min(32,Math.max(0,cap)),Math.max(0,Math.floor(total)));}
}
