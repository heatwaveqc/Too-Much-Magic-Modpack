package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonObject;
import io.github.heatwaveqc.tyr.progression.model.XpChannel;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

/**
 * Common verified physical-training rates. All non-fixed amounts are NOMINAL
 * shared XP, with lifetime growth resolved separately for each committed award.
 */
public record TrainingXpSpec(String system,BigDecimal hardness,BigDecimal sprint,
                             BigDecimal ordinaryKill,BigDecimal strictKill,int strictWindow,
                             BigDecimal discovery,BigDecimal meditativeFood,
                             BigDecimal meditativeDrink,String meditativeTag,String drinkTag) {
    private static BigDecimal rate(JsonObject j,String key) {
        BigDecimal n=j.has(key)?j.get(key).getAsBigDecimal():BigDecimal.ZERO;
        if(n.signum()<0 || n.compareTo(new BigDecimal("1000000"))>0)
            throw new IllegalArgumentException("Invalid training MXP rate "+key);
        return n;
    }
    private static String tag(JsonObject j,String key,String fallback){
        String v=j.has(key)?j.get(key).getAsString():fallback;
        if(!v.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
            throw new IllegalArgumentException("Invalid training item tag "+key);
        return v;
    }
    public static TrainingXpSpec parse(JsonObject root, Map<String,XpChannel> systems){
        if(!root.has("training_mxp"))return null;
        JsonObject j=root.getAsJsonObject("training_mxp");
        Set<String> fields=Set.of("system","hardness","sprint","ordinary_kill_per_hp",
            "strict_fist_kill_per_hp","strict_window_ticks","first_consumable",
            "meditative_food_fixed","meditative_drink_fixed","meditative_tag","drink_tag");
        if(!fields.containsAll(j.keySet()))throw new IllegalArgumentException("Unknown training_mxp field");
        String id=j.get("system").getAsString();
        if(!systems.containsKey(id)||!systems.get(id).magic())
            throw new IllegalArgumentException("Training XP must use a configured shared system");
        int window=j.has("strict_window_ticks")?j.get("strict_window_ticks").getAsInt():6000;
        if(window<20||window>72000)throw new IllegalArgumentException("Invalid strict fist window");
        return new TrainingXpSpec(id,rate(j,"hardness"),rate(j,"sprint"),
            rate(j,"ordinary_kill_per_hp"),rate(j,"strict_fist_kill_per_hp"),window,
            rate(j,"first_consumable"),rate(j,"meditative_food_fixed"),
            rate(j,"meditative_drink_fixed"),
            tag(j,"meditative_tag","tyr_progression:meditative"),
            tag(j,"drink_tag","tyr_progression:drinks"));
    }
    public BigDecimal mining(double originalHardness){
        if(!Double.isFinite(originalHardness)||originalHardness<=0)return BigDecimal.ZERO;
        return hardness.multiply(BigDecimal.valueOf(originalHardness));
    }
    public BigDecimal sprint(double distance){
        if(!Double.isFinite(distance)||distance<=0)return BigDecimal.ZERO;
        return sprint.multiply(BigDecimal.valueOf(distance));
    }
    public BigDecimal kill(double legitimateMaxHp,boolean strict){
        if(!Double.isFinite(legitimateMaxHp)||legitimateMaxHp<=0)return BigDecimal.ZERO;
        return (strict?strictKill:ordinaryKill).multiply(BigDecimal.valueOf(legitimateMaxHp));
    }
}
