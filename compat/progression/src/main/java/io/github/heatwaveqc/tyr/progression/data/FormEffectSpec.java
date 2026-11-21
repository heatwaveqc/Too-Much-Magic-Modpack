package io.github.heatwaveqc.tyr.progression.data;

import io.github.heatwaveqc.tyr.progression.model.Definition;
import java.util.Set;

/** Explicit source/form constraints are opt-in data; unknown context is never a bonus. */
public final class FormEffectSpec {
    private FormEffectSpec(){}
    private static final Set<String> OFFENSE=Set.of("melee_bonus","damage_bonus","status_on_hit","heal_on_hit",
        "heal_on_kill","critical_hit","hit_streak","damage_at_low_health","backstab");
    private static final Set<String> MELEE=Set.of("dragon_form_melee","dragon_claw_weapon_melee","jawr_wolf_melee");
    private static final Set<String> BITES=Set.of("jawr_bite","jawr_wolf_bite","jawr_vampire_bite");
    private static final Set<String> BREATH_DAMAGE=Set.of("dragonsurvival:fire_breath","dragonsurvival:forest_breath","dragonsurvival:lightning_breath");
    public static boolean breathDamage(String id){return BREATH_DAMAGE.contains(id);}
    public static void validate(Definition.Effect fx){
        String attack=fx.params().get("native_attack"), mining=fx.params().get("native_mining");
        if(attack!=null){
            boolean melee=MELEE.contains(attack),bite=BITES.contains(attack),breath=attack.equals("dragon_breath");
            if(!OFFENSE.contains(fx.type())||!(melee||bite||breath))
                throw new IllegalArgumentException("Unknown/unsupported native attack context");
            if(!fx.weapon().isEmpty()&&!fx.weapon().equals("*"))throw new IllegalArgumentException("Native attack cannot target an ordinary weapon");
            if(melee){
                if(!fx.damageTag().isEmpty()&&!fx.damageTag().equals("minecraft:player_attack"))
                    throw new IllegalArgumentException("Form melee uses direct native player_attack only");
            }else{
                // JAWR native hurt calls supply vanilla player_attack, not a Witchery damage ID.
                if(bite&&!fx.damageTag().equals("minecraft:player_attack"))
                    throw new IllegalArgumentException("Native bite requires scoped player_attack");
                if(breath&&!breathDamage(fx.damageTag()))
                    throw new IllegalArgumentException("Native breath requires verified direct breath damage ID");
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
