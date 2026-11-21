package io.github.heatwaveqc.tmm.progression.data;
import com.google.gson.JsonObject;

/** Reusable environmental contemplation ritual. Timing, posture, direction and consequences are data. */
public record TimedEnrollment(String dimension,int startTime,int endTime,int ticks,
        boolean clearSky,boolean dry,boolean emptyHand,boolean unarmored,
        double maxLookX,double maxAbsLookY,String completionEvent,String label) {
    public static TimedEnrollment parse(JsonObject path){
        if(!path.has("enrollment")||!"sunset".equals(path.get("enrollment").getAsString()))return null;
        var j=path.getAsJsonObject("timed_enrollment");
        if(j==null)throw new IllegalArgumentException("Missing timed_enrollment");
        var d=new TimedEnrollment(j.get("dimension").getAsString(),j.get("start_time").getAsInt(),j.get("end_time").getAsInt(),
            j.get("ticks").getAsInt(),j.get("clear_sky").getAsBoolean(),j.get("dry").getAsBoolean(),
            j.get("empty_hand").getAsBoolean(),j.get("unarmored").getAsBoolean(),
            j.get("max_look_x").getAsDouble(),j.get("max_abs_look_y").getAsDouble(),
            j.get("completion_event").getAsString(),j.get("label").getAsString());
        if(!d.dimension.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")||!d.completionEvent.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")||
           d.startTime<0||d.endTime>=24000||d.startTime>d.endTime||d.ticks<1||d.ticks>72000||
           !Double.isFinite(d.maxLookX)||d.maxLookX < -1||d.maxLookX>1||!Double.isFinite(d.maxAbsLookY)||
           d.maxAbsLookY<0||d.maxAbsLookY>1||d.label.isBlank()||d.label.length()>100)
            throw new IllegalArgumentException("Invalid timed enrollment");
        return d;
    }
    public boolean acceptsTime(long time){return time>=startTime&&time<=endTime;}
}
