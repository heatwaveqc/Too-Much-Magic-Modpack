package io.github.heatwaveqc.tmm.progression.data;

import io.github.heatwaveqc.tmm.progression.model.Definition;
import java.util.Set;

/** Explicit source and form constraints are opt-in data; unknown context is never a bonus. */
public final class FormEffectSpec {
    private FormEffectSpec(){}
    private static final Set<String> OFFENSE=Set.of("melee_bonus","damage_bonus","status_on_hit","heal_on_hit",
        "heal_on_kill","critical_hit","hit_streak","damage_at_low_health","backstab");
    public static void validate(Definition.Effect fx){
        String attack=fx.params().get("native_attack"), mining=fx.params().get("native_mining");
        if(attack!=null){
            if(!OFFENSE.contains(fx.type())||!Set.of("dragon_form_melee","jawr_wolf_melee","dragon_breath","jawr_bite").contains(attack))
                throw new IllegalArgumentException("Unknown/unsupported native attack context");
            boolean melee=attack.endsWith("_melee");
            if(!fx.weapon().isEmpty()&&!fx.weapon().equals("*"))throw new IllegalArgumentException("Native attack cannot target an ordinary weapon");
            if(melee){
                if(!fx.damageTag().isEmpty()&&!fx.damageTag().equals("minecraft:player_attack"))
                    throw new IllegalArgumentException("Form melee uses direct native player_attack only");
            }else{
                String prefix=attack.equals("dragon_breath")?"dragonsurvival:":"witchery:";
                // Exact provider damage type; do not accept wildcard or a broad vanilla tag.
                if(!fx.damageTag().startsWith(prefix)||!fx.damageTag().matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
                    throw new IllegalArgumentException("Native bite/breath requires an exact provider damage ID");
                if(fx.type().equals("melee_bonus")||fx.type().equals("backstab"))
                    throw new IllegalArgumentException("Native bite/breath must use a source-aware effect");
                if(fx.params().containsKey("delivery")&&!fx.params().get("delivery").equals("any"))
                    throw new IllegalArgumentException("Native bite/breath requires delivery any");
            }
        }
        if(mining!=null&&(!fx.type().equals("break_speed")||!Set.of("dragon_claw","jawr_wolf_claw").contains(mining)))
            throw new IllegalArgumentException("Native mining is legal speed only");
    }
}
