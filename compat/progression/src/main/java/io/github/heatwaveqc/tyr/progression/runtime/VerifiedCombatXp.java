package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.state.CombatProof;
import io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Strict fist evidence is tied to actual health loss on the logical server.
 * No potion flags or client packets can create, clear or refresh a challenge.
 * The Wither deed listener may later consume a successful strict result.
 */
public final class VerifiedCombatXp {
    private VerifiedCombatXp() {}
    private record Before(float health,double max,boolean fist,String attacker,
                          DamageSource source,long tick) {}
    private static final Map<UUID,Before> PENDING=new HashMap<>();
    @FunctionalInterface public interface StrictVictory {
        void completed(ServerPlayer owner,LivingEntity victim);
    }
    private static final java.util.List<StrictVictory> VICTORY_LISTENERS=
        new java.util.concurrent.CopyOnWriteArrayList<>();
    /** Trusted server-only observers: boss-specific deeds and gliding stay external. */
    public static void registerStrictVictory(StrictVictory listener){
        VICTORY_LISTENERS.add(java.util.Objects.requireNonNull(listener));
    }

    private static boolean validVictim(LivingEntity victim){
        if(victim instanceof Player)return false;
        if(victim instanceof TamableAnimal tame && tame.isTame())return false;
        if(victim instanceof OwnableEntity owned && owned.getOwnerUUID()!=null)return false;
        return victim.level() instanceof ServerLevel
            && Double.isFinite(victim.getMaxHealth()) && victim.getMaxHealth()>0;
    }
    private static ServerPlayer attacker(LivingEntity victim,DamageSource source){
        if(source.getEntity() instanceof ServerPlayer player
                && source.getDirectEntity()!=null && TrainingRewards.eligible(player,"tyr_progression:monk"))
            return player;
        // Explicit native magic provenance only; no owner-chain guessing.
        var owner=OwnedMagicDamage.hunterOwner(victim,source);
        return owner!=null&&TrainingRewards.eligible(owner,"tyr_progression:monk")?owner:null;
    }
    private static String barehandOwner(LivingEntity victim,DamageSource source){
        if(!source.is(DamageTypes.PLAYER_ATTACK)
                ||!(source.getEntity() instanceof ServerPlayer player)
                ||source.getDirectEntity()!=player
                ||!player.getMainHandItem().isEmpty()
                ||!TrainingRewards.eligible(player,"tyr_progression:monk"))return "";
        return player.getUUID().toString();
    }
    public static void beforeDamage(LivingDamageEvent.Pre event){
        LivingEntity victim=event.getEntity();
        if(!validVictim(victim))return;
        String owner=barehandOwner(victim,event.getSource());
        PENDING.put(victim.getUUID(),new Before(victim.getHealth(),
            victim.getMaxHealth(),!owner.isEmpty(),owner,event.getSource(),
            victim.level().getGameTime()));
        if(PENDING.size()>20000)PENDING.clear();
    }
    private static int window(){
        return PathRegistry.trainingXp().values().stream()
            .mapToInt(x->x.strictWindow()).max().orElse(6000);
    }
    private static void apply(LivingEntity victim,Before first,DamageSource damage){
        CombatProof current=victim.getData(ProgressionAttachments.COMBAT_PROOF.get());
        String owner=barehandOwner(victim,damage);
        boolean full=first.health()>=first.max()-0.0001d;
        var next=current.hit(full,!owner.isEmpty()&&owner.equals(first.attacker()),
            owner,first.tick(),first.max(),window());
        if(!current.equals(next))victim.setData(ProgressionAttachments.COMBAT_PROOF.get(),next);
    }
    public static void afterDamage(LivingDamageEvent.Post event){
        LivingEntity victim=event.getEntity();
        Before previous=PENDING.remove(victim.getUUID());
        if(!validVictim(victim)||event.getNewDamage()<=0)return;
        if(previous!=null&&previous.source()==event.getSource()
                &&victim.getHealth()<previous.health()){
            apply(victim,previous,event.getSource());
        }else{
            // Damage lacking its matching pre-state may count toward an
            // ordinary kill, but can NEVER initiate a strict fist ordeal.
            CombatProof before=victim.getData(ProgressionAttachments.COMBAT_PROOF.get());
            var after=before.hit(false,false,"",victim.level().getGameTime(),
                victim.getMaxHealth(),window());
            victim.setData(ProgressionAttachments.COMBAT_PROOF.get(),after);
        }
    }
    /** Reuse first committed-damage evidence for Hunter. Lethal first hits can
     * be delivered before the post-damage callback; use the matching observed pre-state.
     */
    public static double baselineHealth(LivingEntity victim){
        var proof=victim.getData(ProgressionAttachments.COMBAT_PROOF.get());
        if(proof.baselineMaxHp()>0)return proof.baselineMaxHp();
        var pending=PENDING.get(victim.getUUID());
        if(pending!=null&&victim.getHealth()<pending.health())return pending.max();
        return victim.getMaxHealth();
    }
    public static void forget(LivingEntity victim){PENDING.remove(victim.getUUID());}
    public static void death(LivingDeathEvent event){
        if(event.isCanceled()||!validVictim(event.getEntity()))return;
        LivingEntity victim=event.getEntity();
        // Some vanilla/modded death paths notify death before post-damage.
        // A lethal observed pre-state may be completed only on real death.
        Before pending=PENDING.remove(victim.getUUID());
        if(pending!=null&&pending.source()==event.getSource()
                &&victim.getHealth()<pending.health())apply(victim,pending,event.getSource());

        CombatProof proof=victim.getData(ProgressionAttachments.COMBAT_PROOF.get());
        if(proof.settled())return;
        victim.setData(ProgressionAttachments.COMBAT_PROOF.get(),proof.finish());
        ServerPlayer credited=attacker(victim,event.getSource());
        if(credited==null)return;
        double baseline=proof.baselineMaxHp()>0?proof.baselineMaxHp():victim.getMaxHealth();
        boolean directFistFinisher=credited.getUUID().toString()
            .equals(barehandOwner(victim,event.getSource()));
        long now=victim.level().getGameTime();
        TrainingRewards.kill(credited,baseline,proof,directFistFinisher,now);
        if(directFistFinisher)for(var entry:PathRegistry.trainingXp().entrySet()){
            if(TrainingRewards.eligible(credited,entry.getKey())
                    &&proof.strict(credited.getUUID().toString(),
                        now,entry.getValue().strictWindow())){
                // This only emits a verified generic completion. It does not
                // automatically grant Wither deeds, gliding, or extra boss MXP.
                for(StrictVictory observer:VICTORY_LISTENERS) {
                    try{observer.completed(credited,victim);}
                    catch(RuntimeException failure){
                        io.github.heatwaveqc.tyr.progression.ProgressionMod.LOGGER.error(
                            "Strict-victory observer failed for {}",victim.getUUID(),failure);
                    }
                }
                break;
            }
        }
    }
}
