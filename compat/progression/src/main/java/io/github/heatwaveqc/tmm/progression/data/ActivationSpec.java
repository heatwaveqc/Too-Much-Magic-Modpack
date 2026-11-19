package io.github.heatwaveqc.tmm.progression.data;
import com.google.gson.JsonObject;
import java.util.Set;

/** Optional activation for an owned skill, not a separate detachable power. */
public record ActivationSpec(String mode,int durationTicks,int cooldownTicks,int jawrBloodCost,double jawrHealOnActivation) {
    public ActivationSpec(String mode,int durationTicks,int cooldownTicks,int jawrBloodCost){this(mode,durationTicks,cooldownTicks,jawrBloodCost,0);}
    public ActivationSpec(String mode,int durationTicks,int cooldownTicks){this(mode,durationTicks,cooldownTicks,0);}
    public ActivationSpec {
        if(!Set.of("toggle","timed").contains(mode)||durationTicks<0||durationTicks>72000||cooldownTicks<0||cooldownTicks>72000
            ||mode.equals("timed")&&durationTicks<1||mode.equals("toggle")&&durationTicks!=0
            ||jawrBloodCost<0||jawrBloodCost>1_000_000||jawrBloodCost>0&&!mode.equals("timed")
            ||!Double.isFinite(jawrHealOnActivation)||jawrHealOnActivation<0||jawrHealOnActivation>100||jawrHealOnActivation>0&&jawrBloodCost<=0)
            throw new IllegalArgumentException("Invalid activation spec");
    }
    public static ActivationSpec read(JsonObject skill){
        if(!skill.has("activation"))return null;
        var j=skill.getAsJsonObject("activation");
        for(var key:j.keySet())if(!Set.of("mode","duration_ticks","cooldown_ticks","jawr_blood_cost","jawr_heal_on_activation").contains(key))
            throw new IllegalArgumentException("Unsupported activation field "+key);
        return new ActivationSpec(j.get("mode").getAsString(),j.has("duration_ticks")?j.get("duration_ticks").getAsInt():0,
            j.has("cooldown_ticks")?j.get("cooldown_ticks").getAsInt():0,
            j.has("jawr_blood_cost")?j.get("jawr_blood_cost").getAsBigDecimal().intValueExact():0,
            j.has("jawr_heal_on_activation")?j.get("jawr_heal_on_activation").getAsDouble():0);
    }
    public record State(long until,long ready){
        public State {if(until<0||ready<0)throw new IllegalArgumentException("Invalid activation state");}
        public static State empty(){return new State(0,0);}
        public boolean active(long now){return until==Long.MAX_VALUE||until>now;}
        public State stop(){return new State(0,ready);}
        public State request(ActivationSpec spec,long now){
            if(active(now))return spec.mode.equals("toggle")?stop():this;
            if(now<ready)return this;
            long end=spec.mode.equals("toggle")?Long.MAX_VALUE:Math.addExact(now,spec.durationTicks);
            return new State(end,Math.addExact(spec.mode.equals("timed")?end:now,spec.cooldownTicks));
        }
        public String encode(){return until+"/"+ready;}
        public static State decode(String raw){
            try{var parts=raw.split("/",-1);if(parts.length!=2)return empty();
                return new State(Long.parseLong(parts[0]),Long.parseLong(parts[1]));}
            catch(RuntimeException bad){return empty();}
        }
    }
}
