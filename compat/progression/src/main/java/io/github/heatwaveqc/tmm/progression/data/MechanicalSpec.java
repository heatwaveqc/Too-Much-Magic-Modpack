package io.github.heatwaveqc.tmm.progression.data;

import io.github.heatwaveqc.tmm.progression.model.Definition;
import java.util.Map;
import java.util.Set;

/** Strict, pure schema for shared mechanical effects. No registry or client classes here. */
public final class MechanicalSpec {
    private MechanicalSpec(){}
    public static final Set<String> TYPES=Set.of("magnet","status_pulse","status_on_hit","status_on_hurt","heal_on_kill",
        "heal_on_hit","critical_hit","damage_at_low_health","enhanced_night_vision","lava_vision","food_efficiency","vanilla_xp_bonus","status_immunity",
        "crop_bonus","ordinary_drop_bonus","virtual_fortune","virtual_looting","magic_cost_discount","diet_permission","delayed_regeneration","hunger_efficiency","jump_bonus","fall_reduction","experience_gain_bonus","glide_control_bonus","backstab","counter_attack","breeding_cooldown","baby_growth","bonemeal_growth","projectile_deflection","hit_streak","health_threshold_status","melee_reflection","quarry_mark","area_pull","landing_shockwave","detection_range","status_duration_reduction","virtual_unbreaking","piglin_kinship","fire_immunity","held_harvest_tier","food_restriction","ore_practice","lava_fishing_practice","vein_mining","ore_sense","villager_attraction","ammo_conservation");
    public static boolean known(String type){return TYPES.contains(type);}
    public static double number(Map<String,String> p,String key,double fallback,double min,double max){
        double n=Double.parseDouble(p.getOrDefault(key,Double.toString(fallback)));
        if(!Double.isFinite(n)||n<min||n>max)throw new IllegalArgumentException("Invalid "+key);
        return n;
    }
    public static int integer(Map<String,String> p,String key,int fallback,int min,int max){
        int n=Integer.parseInt(p.getOrDefault(key,Integer.toString(fallback)));
        if(n<min||n>max)throw new IllegalArgumentException("Invalid "+key);return n;
    }
    public static boolean bool(Map<String,String> p,String key,boolean fallback){
        String s=p.getOrDefault(key,Boolean.toString(fallback));
        if(!s.equals("true")&&!s.equals("false"))throw new IllegalArgumentException("Invalid "+key);
        return Boolean.parseBoolean(s);
    }
    private static void bounded(double a,double min,double max){
        if(!Double.isFinite(a)||a<min||a>max)throw new IllegalArgumentException("Invalid per_rank");
    }
    private static void status(Map<String,String> p){
        if(!p.getOrDefault("effect","").matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
            throw new IllegalArgumentException("Status effect requires a registry ID");
        integer(p,"duration_ticks",60,1,12000);
        integer(p,"amplifier",0,0,9);
    }
    public static void validate(Definition.Effect fx){
        FormEffectSpec.validate(fx);
        var p=fx.params();double a=fx.amountPerRank();String type=fx.type();
        if(type.equals("attribute")&&p.containsKey("max_contribution"))number(p,"max_contribution",0,0,1000000);
        if(!known(type))return;
        if(!fx.weapon().isEmpty()&&!Set.of("status_on_hit","heal_on_hit","heal_on_kill","critical_hit","damage_at_low_health","ordinary_drop_bonus","crop_bonus","virtual_fortune","virtual_looting","backstab","hit_streak","held_harvest_tier","vein_mining","ammo_conservation").contains(type))
            throw new IllegalArgumentException("Weapon selector not supported for "+type);
        switch(type){
            case "food_efficiency" -> {bounded(a,0,2);bool(p,"gold_recipes",false);}
            case "vein_mining" -> {
                bounded(a,0,16);scoped(fx);if(fx.weapon().isEmpty()&&!fx.emptyHand()||fx.weapon().equals("*"))throw new IllegalArgumentException("Vein mining needs a scoped tool or empty hand");
                integer(p,"radius",6,1,8);integer(p,"max_blocks",16,1,32);integer(p,"cooldown_ticks",20,5,200);bool(p,"sneak_only",true);
            }
            case "ore_sense" -> {bounded(a,0,12);scoped(fx);integer(p,"interval_ticks",60,40,200);integer(p,"max_targets",8,1,16);}
            case "villager_attraction" -> {bounded(a,0,12);scoped(fx);number(p,"speed",1,0.1,1.5);integer(p,"max_targets",4,1,8);}
            case "ammo_conservation" -> {
                bounded(a,0,0.5);
                if(!fx.weapon().equals("minecraft:crossbow")||!fx.selector().equals("minecraft:arrow"))
                    throw new IllegalArgumentException("Native ammo conservation currently supports vanilla crossbow and plain arrow only");
            }
            case "vanilla_xp_bonus" -> bounded(a,0,2);
            case "piglin_kinship" -> {bounded(a,0,1);bool(p,"alarm_protection",true);}
            case "fire_immunity" -> bounded(a,0,1);
            case "held_harvest_tier" -> {
                bounded(a,0,1);scoped(fx);
                if(fx.weapon().isEmpty()||fx.weapon().equals("*")||fx.tier()<1||fx.tier()>3)
                    throw new IllegalArgumentException("Held harvest permission needs scoped weapon and vanilla tier");
            }
            case "food_restriction" -> {bounded(a,0,1);scoped(fx);bool(p,"gold_recipes",false);bool(p,"lava_caught",false);bool(p,"drink_permission",false);}
            case "ore_practice","lava_fishing_practice" -> {
                bounded(a,0,1);if(type.equals("ore_practice"))scoped(fx);
                if(!p.getOrDefault("event","").matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))throw new IllegalArgumentException("Explicit practice event ID required");
            }
            case "experience_gain_bonus" -> {
                bounded(a,0,2);
                if(!fx.selector().matches("[a-z0-9_.-]+:[a-z0-9_./-]+")||fx.selector().equals("tmm_progression:mxp"))
                    throw new IllegalArgumentException("Native XP bonus requires explicit non-MXP system");
            }
            case "breeding_cooldown","baby_growth" -> {bounded(a,0,4); scoped(fx);}
            case "bonemeal_growth" -> {bounded(a,0,1); scoped(fx); integer(p,"extra_stages",1,1,3);}
            case "projectile_deflection" -> {bounded(a,0,1);proc(p);number(p,"min_front_cosine",0.5,0,1);}
            case "hit_streak" -> {bounded(a,0,2);scoped(fx);integer(p,"max_stacks",5,1,20);integer(p,"expiry_ticks",100,1,1200);bool(p,"same_target",true);delivery(p);}
            case "health_threshold_status" -> {status(p);bounded(a,0,1);number(p,"threshold",0.3,0.01,0.99);proc(p);}
            case "melee_reflection" -> {bounded(a,0,1);scoped(fx);proc(p);number(p,"reflect_fraction",1,0,1);number(p,"max_damage",20,0.01,100);}
            case "quarry_mark" -> {bounded(a,0,32);scoped(fx);integer(p,"interval_ticks",20,10,200);integer(p,"duration_ticks",80,1,1200);number(p,"min_look_cosine",0.985,0.5,1);}
            case "area_pull","landing_shockwave" -> {bounded(a,0,2);scoped(fx);number(p,"radius",4,0.5,16);integer(p,"max_targets",8,1,16);integer(p,"interval_ticks",20,10,200);number(p,"min_fall_distance",3,1,64);proc(p);}
            case "detection_range" -> {bounded(a,0,0.95);scoped(fx);number(p,"reference_range",16,1,64);}
            case "status_duration_reduction" -> {bounded(a,0,0.95);scoped(fx);if(!fx.selector().matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))throw new IllegalArgumentException("Exact status ID required");}
            case "virtual_unbreaking" -> {bounded(a,0,32);scoped(fx);if(!Set.of("mainhand","offhand","armor","equipped").contains(p.getOrDefault("slot","equipped")))throw new IllegalArgumentException("Invalid wear slot");integer(p,"max_total",32,1,64);}
            case "glide_control_bonus" -> bounded(a,0,0.1);
            case "backstab" -> {bounded(a,0,2);number(p,"min_rear_cosine",0.5,0,1);}
            case "counter_attack" -> {
                bounded(a,0,20);proc(p);integer(p,"cooldown_ticks",40,1,72000);
                number(p,"max_damage",20,0.01,100);
            }
            case "delayed_regeneration" -> {
                bounded(a,0,20);integer(p,"interval_ticks",20,20,200);
                integer(p,"after_damage_ticks",80,0,72000);
            }
            case "hunger_efficiency" -> bounded(a,0,0.95);
            case "jump_bonus" -> bounded(a,0,2);
            case "fall_reduction" -> bounded(a,0,64);
            case "status_immunity" -> {
                if(!p.getOrDefault("effect","").matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
                    throw new IllegalArgumentException("Immunity requires an exact effect ID");
                bool(p,"harmful_only",true);
            }
            case "crop_bonus","ordinary_drop_bonus" -> {
                bounded(a,0,1);number(p,"chance",1,0,1);
                if(fx.selector().isEmpty()||fx.selector().equals("*"))
                    throw new IllegalArgumentException("Drop bonus requires scoped target");
            }
            case "virtual_fortune","virtual_looting" -> {
                bounded(a,0,32);integer(p,"max_total",8,1,32);
                if(fx.selector().isEmpty()||fx.selector().equals("*"))
                    throw new IllegalArgumentException("Virtual enchantment requires a scoped target");
                if(fx.weapon().isEmpty()&&!fx.emptyHand())
                    throw new IllegalArgumentException("Virtual enchantment requires weapon or empty hand condition");
            }
            case "magic_cost_discount" -> {
                bounded(a,0,0.95);
                if(!Set.of("ars_nouveau:spell","irons_spellbooks:spell","psi:cad_cast").contains(fx.selector()))
                    throw new IllegalArgumentException("Unknown committed cost adapter");
            }
            case "diet_permission" -> {
                bounded(a,0,1);
                if(fx.selector().isEmpty()||fx.selector().equals("*"))throw new IllegalArgumentException("Diet permission requires explicit item ID/tag");
                if(!Set.of("recreational_drink","alcohol").contains(p.getOrDefault("category","")))
                    throw new IllegalArgumentException("Invalid diet permission category");
            }
            case "magnet" -> {
                bounded(a,0.1,32);integer(p,"interval_ticks",5,5,200);integer(p,"max_targets",16,1,32);
                bool(p,"move_xp",false);
            }
            case "status_pulse" -> {
                status(p);bounded(a,0,32);integer(p,"interval_ticks",20,10,200);
                integer(p,"max_targets",8,1,32);
                bool(p,"allow_allied_players",false);
                if(p.getOrDefault("scope","self").equals("quarry")){scoped(fx);if(!p.get("effect").equals("minecraft:glowing"))throw new IllegalArgumentException("Quarry pulse is highlighting only");}
                if(!Set.of("self","hostile","allied","quarry").contains(p.getOrDefault("scope","self")))
                    throw new IllegalArgumentException("Invalid pulse scope");
                // Leased self/ally effects expire naturally. No arbitrary PvP or instant effect farming.
            }
            case "status_on_hit" -> {status(p);bounded(a,0,1);proc(p);}
            case "status_on_hurt" -> {
                status(p);bounded(a,0,1);proc(p);
                if(!Set.of("self","attacker").contains(p.getOrDefault("recipient","self")))
                    throw new IllegalArgumentException("Invalid reactive status recipient");
            }
            case "heal_on_kill","heal_on_hit" -> {
                bounded(a,0,p.getOrDefault("basis","").equals("flat")?100:1);proc(p);number(p,"max_heal",20,0.01,100);
                if(!Set.of("flat","target_health","damage").contains(p.getOrDefault("basis",type.equals("heal_on_kill")?"target_health":"damage")))
                    throw new IllegalArgumentException("Invalid healing basis");
                if(type.equals("heal_on_kill")&&p.getOrDefault("basis","target_health").equals("damage"))
                    throw new IllegalArgumentException("Kill event has no final damage amount");
            }
            case "critical_hit" -> {bounded(a,0,1);proc(p);number(p,"multiplier",2,1,5);}
            case "damage_at_low_health" -> {bounded(a,0,5);number(p,"threshold",0.3,0.01,1);delivery(p);}
            case "enhanced_night_vision" -> {bounded(a,0,1);bool(p,"default_enabled",true);}
            case "lava_vision" -> {bounded(a,0.1,128);bool(p,"default_enabled",true);}
            default -> throw new IllegalArgumentException("Unhandled mechanical effect");
        }
    }
    private static void scoped(Definition.Effect fx){
        if(fx.selector().isEmpty()||fx.selector().equals("*"))throw new IllegalArgumentException("Effect requires explicit target");
    }
    /** A positive speed multiplier reduces a fixed timer, never below one tick. */
    public static int fasterTimer(int ticks,double rate){
        if(ticks<=0)return 0;
        return Math.max(1,(int)Math.ceil(ticks/(1+Math.min(4,Math.max(0,rate)))));
    }
    private static void proc(Map<String,String> p){
        number(p,"chance",1,0,1);integer(p,"cooldown_ticks",0,0,72000);
        bool(p,"persistent_cooldown",false);
        delivery(p);
    }
    private static void delivery(Map<String,String> p){
        if(!Set.of("melee","projectile","any").contains(p.getOrDefault("delivery","any")))
            throw new IllegalArgumentException("Invalid delivery");
    }
    public static boolean crossesBelow(double before,double after,double threshold){return before>threshold&&after<=threshold;}
    public static int nextStreak(int prior,int cap,boolean sameSequence){return Math.min(cap,sameSequence?Math.max(0,prior)+1:1);}
    public static int reducedDuration(int ticks,double fraction){
        if(ticks<=0)return ticks;
        return Math.max(1,(int)Math.ceil(ticks*(1-Math.min(0.95,Math.max(0,fraction)))));
    }
    public static double unbreakingPrefilter(int actual,int virtual,boolean armor){
        if(actual<0||virtual<=0)return 1;
        double before=armor?0.6+0.4/(actual+1.0):1.0/(actual+1.0);
        double after=armor?0.6+0.4/(actual+virtual+1.0):1.0/(actual+virtual+1.0);
        return Math.min(1,after/before);
    }
    public static double scaled(double amount,int rank,double cap){
        return Math.min(cap,Math.max(0,amount*Math.max(0,rank)));
    }
    public static double heal(double perRank,int rank,double basis,double cap){
        return Math.min(cap,scaled(perRank,rank,1)*Math.max(0,basis));
    }
}
