package io.github.heatwaveqc.tyr.progression.foundation;

/** A stun is followed by an unconditional target-wide recovery window. */
public final class StunWindow {
    private StunWindow(){}
    public static final int MAX_DURATION=60,MAX_RECOVERY=1200;
    public record Window(long start,long until,long ready){
        public Window{
            if(start<0||until<start||ready<until||until-start>MAX_DURATION||ready-until>MAX_RECOVERY)
                throw new IllegalArgumentException("Invalid stun window");
        }
        public boolean stunned(long now){return now>=start&&now<until;}
        public boolean canApply(long now){return now>=ready;}
    }
    public static Window begin(long now,int duration,int recovery){
        if(duration<1||duration>MAX_DURATION||recovery<20||recovery>MAX_RECOVERY)
            throw new IllegalArgumentException("Invalid stun duration/recovery");
        return new Window(now,Math.addExact(now,duration),Math.addExact(Math.addExact(now,duration),recovery));
    }
}
