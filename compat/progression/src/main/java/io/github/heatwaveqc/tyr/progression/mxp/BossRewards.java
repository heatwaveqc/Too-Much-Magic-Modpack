package io.github.heatwaveqc.tyr.progression.mxp;

import io.github.heatwaveqc.tyr.progression.foundation.BossRewardPolicy;
import io.github.heatwaveqc.tyr.progression.foundation.BossRewardPolicy.Tier;
import io.github.heatwaveqc.tyr.progression.runtime.HunterNativePreyOwnership;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

/** One shared native death settlement. Separate from Hunter XP and native loot. */
public final class BossRewards {
    private BossRewards() {}
    private static TagKey<EntityType<?>> tag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE,ResourceLocation.parse("tyr_progression:"+name));
    }
    private static final TagKey<EntityType<?>> STANDARD=tag("bosses"), MINI=tag("minibosses"), BLOCKED=tag("boss_generic_death_excluded");
    // Entity lifetime bounds memory; no persistent participant ledger or world scans.
    private static final Map<LivingEntity, Map<UUID,Long>> HITS=new WeakHashMap<>();
    private record Pending(LivingDeathEvent event, String id, Tier tier, double elite, Set<UUID> recipients) {}
    private static final List<Pending> PENDING=new ArrayList<>();
    private static final Map<LivingEntity, Set<UUID>> CHAOS_FINAL=new WeakHashMap<>();

    private static ServerLevel serverLevel(LivingEntity e) {
        return e.level() instanceof ServerLevel level&&level.getServer().isSameThread()?level:null;
    }
    private static boolean eligible(ServerPlayer p,ServerLevel level,LivingEntity victim) {
        return p!=null&&p!=victim&&!(p instanceof FakePlayer)&&!p.isCreative()&&!p.isSpectator()
            &&level.getServer().getPlayerList().getPlayer(p.getUUID())==p;
    }
    private static UUID playerId(Entity attacker,ServerLevel level,LivingEntity victim) {
        return attacker instanceof ServerPlayer p&&eligible(p,level,victim)?p.getUUID():null;
    }
    private static boolean invalidVictim(LivingEntity victim) {
        if(victim instanceof Player || victim instanceof TamableAnimal animal&&animal.isTame())return true;
        if(victim instanceof OwnableEntity own&&own.getOwnerUUID()!=null) {
            LivingEntity owner=own.getOwner();
            if(owner==null||owner instanceof Player)return true;
        }
        return HunterNativePreyOwnership.excluded(victim);
    }
    /** Native 8.9.0 source markers; a string-valued pending elite is not an initialized elite. */
    private static double eliteMultiplier(LivingEntity victim) {
        if(!(victim instanceof Mob)||!ModList.get().getModContainerById("apotheosis")
            .filter(c->c.getModInfo().getVersion().toString().startsWith("8.9.0")).isPresent())return 0;
        var data=victim.getPersistentData();
        return BossRewardPolicy.eliteMultiplier(data.getBoolean("apoth.boss"),
            data.getBoolean("apoth.miniboss"),data.getString("apoth.boss.rarity"));
    }
    private static Tier tier(LivingEntity victim,double elite) {
        var type=victim.getType();
        return BossRewardPolicy.tier(type.is(BLOCKED),type.is(STANDARD),type.is(MINI),elite);
    }
    private static String typeId(LivingEntity victim) {
        ResourceLocation id=BuiltInRegistries.ENTITY_TYPE.getKey(victim.getType());
        return id==null?"":id.toString();
    }
    private static Set<UUID> recentRecipients(LivingEntity victim,ServerLevel level,UUID finalBlow) {
        return BossRewardPolicy.recent(HITS.getOrDefault(victim,Map.of()),level.getServer().overworld().getGameTime(),
            MxpProgressionConfig.BOSS_CREDIT_TICKS.get(),finalBlow);
    }
    private static void recordHit(LivingEntity victim,UUID player,long now,int window) {
        Map<UUID,Long> recent=HITS.computeIfAbsent(victim,e->new HashMap<>());
        recent.entrySet().removeIf(e->e.getValue()>now||now-e.getValue()>window);
        recent.put(player,now);
    }
    public static void onDamage(LivingDamageEvent.Post event) {
        LivingEntity victim=event.getEntity(); ServerLevel level=serverLevel(victim);
        if(level==null||event.getNewDamage()<=0)return;
        String type=typeId(victim);
        boolean knight=SpecialBossNative.knight(victim,type);
        boolean chaos=SpecialBossNative.chaos(victim,type);
        if(!knight&&!chaos&&SpecialBossNative.variantKey(victim,type)==null&&tier(victim,eliteMultiplier(victim))==Tier.NONE)return;
        UUID player=playerId(event.getSource().getEntity(),level,victim);
        if(player==null)return;
        long now=level.getServer().overworld().getGameTime();
        int window=MxpProgressionConfig.BOSS_CREDIT_TICKS.get();
        recordHit(victim,player,now,window);
        // Native encounter membership, not a new radius/structure heuristic.
        if(knight)for(LivingEntity peer:SpecialBossNative.nearbyKnights(victim))
            if(peer.level()==level&&peer.isAlive())recordHit(peer,player,now,window);
    }
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity victim=event.getEntity(); ServerLevel level=serverLevel(victim);
        if(level==null||event.isCanceled())return;
        double elite=eliteMultiplier(victim);
        String key=SpecialBossNative.variantKey(victim,typeId(victim));
        Tier tier=key==null?tier(victim,elite):Tier.STANDARD;
        if(tier==Tier.NONE||invalidVictim(victim))return;
        long now=level.getServer().overworld().getGameTime();
        Set<UUID> players=BossRewardPolicy.recent(HITS.getOrDefault(victim,Map.of()),now,
            MxpProgressionConfig.BOSS_CREDIT_TICKS.get(),playerId(event.getSource().getEntity(),level,victim));
        if(players.isEmpty())return;
        ResourceLocation type=BuiltInRegistries.ENTITY_TYPE.getKey(victim.getType());
        if(type!=null)PENDING.add(new Pending(event,key==null?type.toString():key,tier,elite,players));
    }
    /** Recheck cancellation and death after all ordinary listeners/native rescue logic have unwound. */
    public static void finishTick() {
        if(PENDING.isEmpty())return;
        var work=List.copyOf(PENDING); PENDING.clear();
        for(Pending pending:work) {
            LivingDeathEvent event=pending.event(); LivingEntity victim=event.getEntity(); ServerLevel level=serverLevel(victim);
            if(level==null||event.isCanceled()||!victim.isDeadOrDying())continue;
            HITS.remove(victim);
            String tx="boss:"+victim.getUUID();
            for(UUID id:pending.recipients()) {
                ServerPlayer player=level.getServer().getPlayerList().getPlayer(id);
                if(!eligible(player,level,victim))continue;
                try {
                    var receipt=switch(pending.tier()) {
                        case STANDARD -> MxpService.standardBossVictory(player,pending.id(),tx);
                        case MINIBOSS -> MxpService.minibossVictory(player,pending.id(),tx);
                        case ELITE -> MxpService.eliteVictory(player,tx,MxpProgressionConfig.ELITE_BASE.get()*pending.elite());
                        case NONE -> null;
                    };
                    if(receipt!=null&&receipt.accepted())player.sendSystemMessage(Component.literal("MXP: "+String.format("%.2f",receipt.effectiveXP())+
                        (pending.tier()==Tier.ELITE?" (empowered foe)":receipt.first()?" (first victory)":" (repeat victory)")));
                } catch(RuntimeException e) { MxpMod.LOGGER.error("MXP boss award failed ({} / {} / {})",pending.id(),tx,id,e); }
            }
        }
    }
    /** Native last-phantom postmortem transition only; one boss receipt, no per-knight MXP. */
    public static void knightCompleted(LivingEntity victim,net.minecraft.world.damagesource.DamageSource cause) {
        ServerLevel level=serverLevel(victim);
        if(!MxpMod.active()||level==null||!victim.isDeadOrDying()||!SpecialBossNative.knight(victim,typeId(victim)))return;
        var players=recentRecipients(victim,level,playerId(cause.getEntity(),level,victim));
        HITS.remove(victim);
        settleNative(level,victim,players,"twilightforest:knight_phantom","boss:"+victim.getUUID(),false);
    }
    /** Capture the last real, positive native health attack before guardian entering its dying phase. */
    public static void chaosLethalHit(LivingEntity victim,net.minecraft.world.damagesource.DamageSource cause) {
        ServerLevel level=serverLevel(victim);
        if(!MxpMod.active()||level==null||!victim.isDeadOrDying()||!SpecialBossNative.chaos(victim,typeId(victim)))return;
        var players=recentRecipients(victim,level,playerId(cause.getEntity(),level,victim));
        if(!players.isEmpty())CHAOS_FINAL.put(victim,players);
    }
    /** ONLY called from tickDeath's native manager invocation, never from administrative kill(). */
    public static void chaosCompleted(LivingEntity victim) {
        ServerLevel level=serverLevel(victim);
        if(!MxpMod.active()||level==null||!SpecialBossNative.chaos(victim,typeId(victim)))return;
        Set<UUID> players=CHAOS_FINAL.remove(victim);
        HITS.remove(victim);
        if(players!=null)settleNative(level,victim,players,"draconicevolution:draconic_guardian","boss:"+victim.getUUID(),true);
    }
    private static void settleNative(ServerLevel level,LivingEntity victim,Set<UUID> players,String id,String tx,boolean chaos){
        for(UUID uuid:players){
            ServerPlayer player=level.getServer().getPlayerList().getPlayer(uuid);
            if(!eligible(player,level,victim))continue;
            try{
                var receipt=chaos?MxpService.chaosVictory(player,tx):MxpService.standardBossVictory(player,id,tx);
                if(receipt.accepted())player.sendSystemMessage(Component.literal("MXP: "+String.format("%.2f",receipt.effectiveXP())+
                    (receipt.first()?" (first victory)":" (repeat victory)")));
            }catch(RuntimeException e){MxpMod.LOGGER.error("Native boss award failed ({}, {})",id,uuid,e);}
        }
    }
    public static void clear(){HITS.clear();PENDING.clear();CHAOS_FINAL.clear();SpecialBossNative.clear();}
}
