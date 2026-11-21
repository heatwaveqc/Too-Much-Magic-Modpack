package io.github.heatwaveqc.tyr.progression.mxp.core;

import java.util.Map;

/** Frozen ordinary dye EMC anchors captured in #348, not a live ProjectE dependency. */
public final class PastelInkValuation {
    public static final double INK_PER_DYE=5.0;
    public static final Map<String,Long> DYE_EMC=Map.ofEntries(
        Map.entry("white",1L),
        Map.entry("orange",1L),
        Map.entry("magenta",1L),
        Map.entry("light_blue",1L),
        Map.entry("yellow",1L),
        Map.entry("lime",1L),
        Map.entry("pink",1L),
        Map.entry("gray",1L),
        Map.entry("light_gray",1L),
        Map.entry("cyan",1L),
        Map.entry("purple",1L),
        Map.entry("blue",1L),
        Map.entry("brown",1L),
        Map.entry("green",1L),
        Map.entry("red",1L),
        Map.entry("black",1L)
    );
    private PastelInkValuation(){}
    public static double reference(String color,double dyeEmc){
        if(color==null||!DYE_EMC.containsKey(color)||!Double.isFinite(dyeEmc)||dyeEmc<=0)return 0;
        double pool=1000.0*INK_PER_DYE/dyeEmc;
        return Double.isFinite(pool)&&pool>0?pool:0;
    }
}
