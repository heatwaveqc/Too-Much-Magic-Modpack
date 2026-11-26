package io.github.heatwaveqc.tyr.progression.foundation;

import java.util.Objects;

/** Synchronous per-block sight context, separate from native loot attribution. */
public final class ScopedFellActor<A,L,P> {
    private record Frame<A,L,P>(A actor,L level,P pos){}
    private final ThreadLocal<Frame<A,L,P>> current=new ThreadLocal<>();
    public void run(A actor,L level,P pos,Runnable original){
        var prior=current.get();
        current.set(new Frame<>(Objects.requireNonNull(actor),Objects.requireNonNull(level),Objects.requireNonNull(pos)));
        try{original.run();}
        finally{if(prior==null)current.remove();else current.set(prior);}
    }
    public A actor(A nativeActor,L level,P pos){
        if(nativeActor!=null)return nativeActor;
        var frame=current.get();
        return frame!=null&&frame.level()==level&&frame.pos().equals(pos)?frame.actor():null;
    }
}
