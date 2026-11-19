package io.github.heatwaveqc.tmm.progression.foundation;

import java.util.function.Supplier;

/** Synchronous exact identity receipt, never a tick-long flag or persistent player state. */
public final class NativeAttackScope {
    private record Frame(String kind,Object owner,Object target,Object source){}
    private final ThreadLocal<Frame> current=new ThreadLocal<>();
    public <T> T around(String kind,Object owner,Object target,Object source,Supplier<T> action){
        Frame previous=current.get();
        current.set(new Frame(kind,owner,target,source));
        try{return action.get();}
        finally{if(previous==null)current.remove();else current.set(previous);}
    }
    /** Resolve the top frame only. Never search an outer frame during nested damage. */
    public Object owner(String kind,Object target,Object source){
        Frame f=current.get();
        return f!=null&&f.kind().equals(kind)&&f.target()==target&&f.source()==source?f.owner():null;
    }
    public String kind(Object owner,Object target,Object source){
        Frame f=current.get();
        return f!=null&&f.owner()==owner&&f.target()==target&&f.source()==source?f.kind():null;
    }
}
