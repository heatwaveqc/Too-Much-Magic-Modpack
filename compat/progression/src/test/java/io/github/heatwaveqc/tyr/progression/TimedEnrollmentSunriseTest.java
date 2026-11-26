package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.data.TimedEnrollment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TimedEnrollmentSunriseTest {
    private static final String OLD="""
      {"enrollment":"sunset","timed_enrollment":{"dimension":"minecraft:overworld",
      "start_time":12000,"end_time":13000,"ticks":200,"clear_sky":true,"dry":true,
      "empty_hand":true,"unarmored":true,"max_look_x":-0.7,"max_abs_look_y":0.45,
      "completion_event":"test:first_light","label":"First Light"}}
      """;
    private static TimedEnrollment parse(String json){return TimedEnrollment.parse(JsonParser.parseString(json).getAsJsonObject());}
    private static String sunrise(){return OLD.replace("\"label\":","\"sunrise_start_time\":23000,\"sunrise_end_time\":1000,\"sunrise_min_look_x\":0.7,\"label\":");}
    @Test void legacySunsetAndWrappedSunrise(){
        var old=parse(OLD);assertEquals(1,old.window(12500,-0.8));assertEquals(0,old.window(0,0.8));
        var dual=parse(sunrise());
        assertEquals(2,dual.window(23000,0.8));assertEquals(2,dual.window(23999,0.8));
        assertEquals(2,dual.window(0,0.8));assertEquals(2,dual.window(1000,0.8));
        assertEquals(0,dual.window(1001,0.8));assertEquals(0,dual.window(22999,0.8));
        assertEquals(0,dual.window(0,-0.8));assertEquals(0,dual.window(12500,0.8));
        assertEquals(1,dual.window(12500,-0.8));assertEquals(old.completionEvent(),dual.completionEvent());
        assertEquals(0,dual.window(6000,0.8));
    }
    @Test void interruptedOrDifferentWindowResetsCounter(){
        assertEquals(200,TimedEnrollment.nextTicks(2,199,2,true));
        assertEquals(0,TimedEnrollment.nextTicks(2,199,2,false));
        assertEquals(0,TimedEnrollment.nextTicks(2,199,0,true));
        assertEquals(1,TimedEnrollment.nextTicks(1,199,2,true));
    }
    @Test void rejectsPartialOverlapAndInvalidDirection(){
        assertThrows(IllegalArgumentException.class,()->parse(sunrise().replace("\"sunrise_min_look_x\":0.7,","")));
        assertThrows(IllegalArgumentException.class,()->parse(sunrise().replace("23000","12000").replace("1000,","13000,")));
        assertThrows(IllegalArgumentException.class,()->parse(sunrise().replace("0.7,\"label","-0.7,\"label")));
    }
}
