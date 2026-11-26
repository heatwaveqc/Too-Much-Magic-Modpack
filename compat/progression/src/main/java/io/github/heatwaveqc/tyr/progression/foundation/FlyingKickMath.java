package io.github.heatwaveqc.tyr.progression.foundation;

/** Pure geometry and bounded rank scaling shared by the aerial strike handler and tests. */
public final class FlyingKickMath {
    private FlyingKickMath(){}
    public static double radius(double base,double perRank,int rank){
        if(rank<1||!Double.isFinite(base)||!Double.isFinite(perRank)||base<0||perRank<0)return 0;
        return Math.min(16,base+perRank*rank);
    }
    public static boolean eligible(boolean vanillaCritical,boolean actualCritical,double verticalVelocity,boolean clearBelow){
        return vanillaCritical&&actualCritical&&Double.isFinite(verticalVelocity)&&verticalVelocity<0&&clearBelow;
    }
    public static boolean inRadius(double distanceSquared,double radius){
        return Double.isFinite(distanceSquared)&&distanceSquared>=0&&radius>0&&distanceSquared<=radius*radius;
    }
}
