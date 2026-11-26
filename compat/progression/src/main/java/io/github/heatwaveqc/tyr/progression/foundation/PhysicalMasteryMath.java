package io.github.heatwaveqc.tyr.progression.foundation;

public final class PhysicalMasteryMath {
    private PhysicalMasteryMath(){}
    public static float protection(float nativePoints,double earned){
        if(!Float.isFinite(nativePoints)||!Double.isFinite(earned)||earned<=0)return nativePoints;
        // Preserve native values above the cap for other readers; the native
        // CombatRules damage layer clamps the combined total.
        return (float)Math.min(Float.MAX_VALUE,nativePoints+earned);
    }
    public static double damageAfterProtection(double damage,double points){
        return damage*(1-Math.max(0,Math.min(20,points))/25);
    }
    public static int soulLevel(double amount){
        return Double.isFinite(amount)?(int)Math.max(0,Math.min(3,Math.floor(amount))):0;
    }
    public static double soulSpeed(int level){
        return level<=0?0:0.0405+0.0105*(Math.min(3,level)-1);
    }
    public static boolean retainSpeed(boolean eligible,boolean soulTerrain,boolean airborne,boolean previous){
        return eligible&&(soulTerrain||airborne&&previous);
    }
}
