package io.github.heatwaveqc.tyr.progression.data;
import com.google.gson.JsonObject;

/** Reusable environmental contemplation ritual. Optional sunrise window shares the original receipt. */
public record TimedEnrollment(String dimension,int startTime,int endTime,int ticks,
        boolean clearSky,boolean dry,boolean emptyHand,boolean unarmored,
        double maxLookX,double maxAbsLookY,String completionEvent,String label,
        int sunriseStartTime,int sunriseEndTime,double sunriseMinLookX) {
    public TimedEnrollment(String dimension,int startTime,int endTime,int ticks,boolean clearSky,boolean dry,
            boolean emptyHand,boolean unarmored,double maxLookX,double maxAbsLookY,String completionEvent,String label){
        this(dimension,startTime,endTime,ticks,clearSky,dry,emptyHand,unarmored,maxLookX,maxAbsLookY,completionEvent,label,-1,-1,0);
    }
    public static TimedEnrollment parse(JsonObject path){
        if(!path.has("enrollment")||!"sunset".equals(path.get("enrollment").getAsString()))return null;
        var j=path.getAsJsonObject("timed_enrollment");
        if(j==null)throw new IllegalArgumentException("Missing timed_enrollment");
        boolean sunrise=j.has("sunrise_start_time");
        if(sunrise!=j.has("sunrise_end_time")||sunrise!=j.has("sunrise_min_look_x"))
            throw new IllegalArgumentException("Sunrise time and direction must be specified together");
        var d=new TimedEnrollment(j.get("dimension").getAsString(),j.get("start_time").getAsInt(),j.get("end_time").getAsInt(),
            j.get("ticks").getAsInt(),j.get("clear_sky").getAsBoolean(),j.get("dry").getAsBoolean(),
            j.get("empty_hand").getAsBoolean(),j.get("unarmored").getAsBoolean(),
            j.get("max_look_x").getAsDouble(),j.get("max_abs_look_y").getAsDouble(),
            j.get("completion_event").getAsString(),j.get("label").getAsString(),
            sunrise?j.get("sunrise_start_time").getAsBigDecimal().intValueExact():-1,
            sunrise?j.get("sunrise_end_time").getAsBigDecimal().intValueExact():-1,
            sunrise?j.get("sunrise_min_look_x").getAsDouble():0);
        if(!d.dimension.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")||!d.completionEvent.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")||
           d.startTime<0||d.endTime>=24000||d.startTime>d.endTime||d.ticks<1||d.ticks>72000||
           !Double.isFinite(d.maxLookX)||d.maxLookX < -1||d.maxLookX>1||!Double.isFinite(d.maxAbsLookY)||
           d.maxAbsLookY<0||d.maxAbsLookY>1||d.label.isBlank()||d.label.length()>100)
            throw new IllegalArgumentException("Invalid timed enrollment");
        if(sunrise){
            if(d.sunriseStartTime<0||d.sunriseStartTime>=24000||d.sunriseEndTime<0||d.sunriseEndTime>=24000||
                !Double.isFinite(d.sunriseMinLookX)||d.sunriseMinLookX<0||d.sunriseMinLookX>=1||d.maxLookX>=0)
                throw new IllegalArgumentException("Invalid sunrise enrollment window or direction");
            for(int time=d.startTime;time<=d.endTime;time++)
                if(d.acceptsSunrise(time))throw new IllegalArgumentException("Sunrise and sunset windows overlap");
        }
        return d;
    }
    public boolean acceptsTime(long time){return time>=startTime&&time<=endTime;}
    public boolean acceptsSunrise(long time){
        return sunriseStartTime>=0&&time>=0&&time<24000&&(sunriseStartTime<=sunriseEndTime
            ?time>=sunriseStartTime&&time<=sunriseEndTime:time>=sunriseStartTime||time<=sunriseEndTime);
    }
    /** Positive X faces east (sunrise); negative X faces west (sunset). */
    public int window(long time,double lookX){
        if(!Double.isFinite(lookX))return 0;
        if(acceptsTime(time)&&lookX<maxLookX)return 1;
        if(acceptsSunrise(time)&&lookX>sunriseMinLookX)return 2;
        return 0;
    }
    public static int nextTicks(int previousWindow,int previousTicks,int window,boolean valid){
        return !valid||window==0?0:previousWindow==window?previousTicks+1:1;
    }
}
