package io.github.heatwaveqc.tyr.progression.mxp.core;

import java.util.IdentityHashMap;
import java.util.Map;

/** Immutable-identity source accounting for one accepted finite-resource cast. */
public final class MultiSourcePayment {
    private final Map<Object,Integer> expected=new IdentityHashMap<>();
    private final Map<Object,Integer> paid=new IdentityHashMap<>();
    private boolean valid=true;
    public void expect(Object source,int nominal){
        if(source==null||nominal<0||expected.containsKey(source)){valid=false;return;}
        expected.put(source,nominal);
    }
    public void observe(Object source,int price,int leftover,double measured){
        if(!expected.containsKey(source)||expected.get(source)<=0||price<=0||
            leftover!=0||!ObservedPayment.funded(measured,price)||paid.putIfAbsent(source,price)!=null)valid=false;
    }
    public boolean complete(){
        if(!valid||paid.isEmpty())return false;
        for(var entry:expected.entrySet())
            if(entry.getValue()>0&&!paid.containsKey(entry.getKey()))return false;
        return true;
    }
    public double nominal(){
        return complete()?expected.values().stream().mapToLong(Integer::longValue).sum():0;
    }
    public double actual(){
        return complete()?paid.values().stream().mapToLong(Integer::longValue).sum():0;
    }
    public void invalidate(){valid=false;}
}
