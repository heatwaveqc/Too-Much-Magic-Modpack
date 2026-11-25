package io.github.heatwaveqc.tyr.progression.foundation;

/** Reconcile a single native reservoir only when its maximum is native or previously owned by us. */
public final class BloodCapacityPolicy {
    private BloodCapacityPolicy(){}
    public record Plan(int maximum,int current,boolean owned){}
    public static Plan reconcile(int nativeMax,int maximum,int current,int previousBase,int previousMax,double rate){
        if(nativeMax<=0||maximum<=0||current<0||current>maximum||!Double.isFinite(rate)||rate<0)return null;
        boolean previouslyOwned=previousBase>0&&previousMax>previousBase;
        boolean ourMaximum=previouslyOwned&&maximum==previousMax;
        if(maximum!=nativeMax&&!ourMaximum)return null; // other mod/admin owns this cap
        int target=(int)Math.min(Integer.MAX_VALUE,Math.floor(nativeMax*(1+Math.min(rate,1))));
        if(target==maximum)return new Plan(maximum,current,target>nativeMax);
        // An upstream native level/relogin reset must never repeatedly refill the bonus.
        boolean nativeReset=previouslyOwned&&!ourMaximum;
        int adjusted=nativeReset?Math.min(current,target):(int)Math.min(target,((long)current*target)/maximum);
        return new Plan(target,adjusted,target>nativeMax);
    }
}
