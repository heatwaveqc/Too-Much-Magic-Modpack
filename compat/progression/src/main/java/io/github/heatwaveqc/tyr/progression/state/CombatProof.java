package io.github.heatwaveqc.tyr.progression.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Persisted, non-removable full-health challenge evidence on a living victim.
 * Only the server's post-damage handler may advance it.
 */
public record CombatProof(boolean seen,boolean qualified,String owner,long firstTick,
                          double baselineMaxHp,boolean lastFist,boolean settled) {
    public static final Codec<CombatProof> CODEC=RecordCodecBuilder.create(i->i.group(
        Codec.BOOL.fieldOf("seen").forGetter(CombatProof::seen),
        Codec.BOOL.fieldOf("qualified").forGetter(CombatProof::qualified),
        Codec.STRING.fieldOf("owner").forGetter(CombatProof::owner),
        Codec.LONG.fieldOf("first_tick").forGetter(CombatProof::firstTick),
        Codec.DOUBLE.fieldOf("baseline_max_hp").forGetter(CombatProof::baselineMaxHp),
        Codec.BOOL.fieldOf("last_fist").forGetter(CombatProof::lastFist),
        Codec.BOOL.fieldOf("settled").forGetter(CombatProof::settled)
    ).apply(i,CombatProof::new));
    public static CombatProof empty(){
        return new CombatProof(false,false,"",0,0,false,false);
    }
    public CombatProof hit(boolean startedAtFull,boolean validFist,String attacker,
                           long tick,double observedMax,int window){
        if(settled)return this;
        if(!Double.isFinite(observedMax)||observedMax<=0)return this;
        if(!seen)
            return new CombatProof(true,startedAtFull&&validFist,
                validFist?attacker:"",tick,observedMax,validFist,false);
        boolean stillValid=qualified&&validFist&&owner.equals(attacker)
            &&tick>=firstTick&&tick-firstTick<=window;
        return new CombatProof(true,stillValid,owner,firstTick,
            baselineMaxHp,validFist&&stillValid,false);
    }
    public boolean strict(String attacker,long tick,int window){
        return seen&&!settled&&qualified&&lastFist&&owner.equals(attacker)
            &&tick>=firstTick&&tick-firstTick<=window;
    }
    public CombatProof finish(){
        return new CombatProof(seen,false,owner,firstTick,baselineMaxHp,false,true);
    }
}
