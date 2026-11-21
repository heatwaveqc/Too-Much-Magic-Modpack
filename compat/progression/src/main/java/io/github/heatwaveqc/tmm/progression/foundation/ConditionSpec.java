package io.github.heatwaveqc.tmm.progression.foundation;

/** Pure validation and evaluation helpers for contextual predicates. */
public final class ConditionSpec {
    private ConditionSpec(){}
    public static double threshold(double value,double min,double max){
        if(!Double.isFinite(value)||value<min||value>max)throw new IllegalArgumentException("Invalid condition value");
        return value;
    }
    public record TimeWindow(int start,int end){
        public TimeWindow{if(start<0||start>=24000||end<0||end>=24000)throw new IllegalArgumentException("Invalid time window");}
        public boolean contains(long time){
            long t=Math.floorMod(time,24000L);
            return start<=end?t>=start&&t<=end:t>=start||t<=end;
        }
    }
}
