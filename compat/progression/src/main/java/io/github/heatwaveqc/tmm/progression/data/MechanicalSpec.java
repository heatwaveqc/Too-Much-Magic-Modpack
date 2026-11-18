package io.github.heatwaveqc.tmm.progression.data;

import io.github.heatwaveqc.tmm.progression.model.Definition;
import java.util.Map;
import java.util.Set;

/** Strict, pure schema for shared mechanical effects. No registry or client classes here. */
public final class MechanicalSpec {
    private MechanicalSpec(){}
    public static final Set<String> TYPES=Set.of("magnet","status_pulse","status_on_hit","status_on_hurt","heal_on_kill",
        "heal_on_hit","critical_hit","damage_at_low_health","enhanced_night_vision","lava_vision","food_efficiency","vanilla_xp_bonus","status_immunity",
        "crop_bonus","ordinary_drop_bonus","virtual_fortune","virtual_looting","magic_cost_discount","diet_permission","delayed_regeneration","hunger_efficiency","jump_bonus","fall_reduction");
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
        var p=fx.params();double a=fx.amountPerRank();String type=fx.type();
        if(type.equals("attribute")&&p.containsKey("max_contribution"))number(p,"max_contribution",0,0,1000000);
        if(!known(type))return;
        if(!fx.weapon().isEmpty()&&!Set.of("status_on_hit","heal_on_hit","heal_on_kill","critical_hit","damage_at_low_health","ordinary_drop_bonus","crop_bonus","virtual_fortune","virtual_looting").contains(type))
            throw new IllegalArgumentException("Weapon selector not supported for "+type);
        switch(type){
            case "food_efficiency","vanilla_xp_bonus" -> bounded(a,0,2);
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
                bounded(a,0,8);
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
                if(!Set.of("self","hostile","allied").contains(p.getOrDefault("scope","self")))
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
    private static void proc(Map<String,String> p){
        number(p,"chance",1,0,1);integer(p,"cooldown_ticks",0,0,72000);
        bool(p,"persistent_cooldown",false);
        delivery(p);
    }
    private static void delivery(Map<String,String> p){
        if(!Set.of("melee","projectile","any").contains(p.getOrDefault("delivery","any")))
            throw new IllegalArgumentException("Invalid delivery");
    }
    public static double scaled(double amount,int rank,double cap){
        return Math.min(cap,Math.max(0,amount*Math.max(0,rank)));
    }
    public static double heal(double perRank,int rank,double basis,double cap){
        return Math.min(cap,scaled(perRank,rank,1)*Math.max(0,basis));
    }
}
