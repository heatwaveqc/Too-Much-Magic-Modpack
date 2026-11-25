package io.github.heatwaveqc.tyr.progression.foundation;

import static io.github.heatwaveqc.tyr.progression.foundation.Condition.Result.*;
/** No independent state, no new acquisition or resurrection. Three-valued native observation. */
public final class FormCombination {
    private FormCombination(){}
    public static Condition.Result lich(Condition.Result goety,Condition.Result jawr){
        if(goety==PASS||jawr==PASS)return PASS;
        return goety==FAIL&&jawr==FAIL?FAIL:UNAVAILABLE;
    }
    public static Condition.Result archlich(Condition.Result dragon,Condition.Result goety,Condition.Result jawr){
        if(dragon==PASS||goety==FAIL||jawr==FAIL)return FAIL;
        return dragon==FAIL&&goety==PASS&&jawr==PASS?PASS:UNAVAILABLE;
    }
    public static Condition.Result dracolich(Condition.Result dragon,Condition.Result goety,Condition.Result jawr){
        if(dragon==FAIL||goety==PASS&&jawr==PASS||goety==FAIL&&jawr==FAIL)return FAIL;
        return dragon==PASS&&(goety==PASS&&jawr==FAIL||goety==FAIL&&jawr==PASS)?PASS:UNAVAILABLE;
    }
}
