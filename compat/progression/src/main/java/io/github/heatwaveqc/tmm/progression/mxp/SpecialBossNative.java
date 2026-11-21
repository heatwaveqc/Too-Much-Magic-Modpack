package io.github.heatwaveqc.tmm.progression.mxp;

import io.github.heatwaveqc.tmm.progression.foundation.BossRewardPolicy;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpReflect;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpPlayerPolicy;
import io.github.heatwaveqc.tmm.progression.runtime.HunterExperience;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.util.FakePlayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Exact pinned provider receipts; optional integrations fail closed. */
public final class SpecialBossNative {
    private SpecialBossNative(){}
    public static final String NEO="com.breakinblocks.neovitae.common.entity.mob.DaemoniumDolorisEntity";
    public static final String NULL="tamaized.voidscape.entity.NullServantEntity";
    public static final String KNIGHT="twilightforest.entity.boss.KnightPhantom";
    public static final String CHAOS="com.brandon3055.draconicevolution.entity.guardian.DraconicGuardianEntity";
    public static boolean pinned(String mod,String version){
        return ModList.get().getModContainerById(mod).map(c->c.getModInfo().getVersion().toString().equals(version)).orElse(false);
    }
    private static boolean exact(Object obj,String name){return obj!=null&&obj.getClass().getName().equals(name);}
    public static String variantKey(LivingEntity victim,String id){
        if("neovitae:daemonium_doloris".equals(id)&&pinned("neovitae","1.1.53")&&exact(victim,NEO))
            return BossRewardPolicy.foremanKey(Boolean.TRUE.equals(MxpReflect.call(victim,"isForeman")));
        if("voidscape:null_servant".equals(id)&&pinned("voidscape","1.21.1-1.9.588")&&exact(victim,NULL)
            &&MxpReflect.call(victim,"getAugment") instanceof Integer augment)
            return BossRewardPolicy.nullServantKey(augment);
        return null;
    }
    public static boolean knight(LivingEntity victim,String id){
        return "twilightforest:knight_phantom".equals(id)&&pinned("twilightforest","4.8.3345")&&exact(victim,KNIGHT);
    }
    public static boolean chaos(LivingEntity victim,String id){
        return "draconicevolution:draconic_guardian".equals(id)&&pinned("draconicevolution","3.1.4.633")&&exact(victim,CHAOS);
    }
    public static List<LivingEntity> nearbyKnights(LivingEntity victim){
        Object result=MxpReflect.call(victim,"getNearbyKnights");
        if(!(result instanceof Collection<?> c))return List.of();
        var peers=new ArrayList<LivingEntity>();
        for(Object item:c)if(item instanceof LivingEntity peer&&exact(peer,KNIGHT))peers.add(peer);
        return List.copyOf(peers);
    }

    // Native White arena success invocation is synchronous. Capture the exact native rewarded players,
    // then settle once after the successful EndEvent returns. No proximity/last-crystal guessing.
    private record Arena(ServerLevel level,String tx,double maxHealth,Map<UUID,ServerPlayer> winners){}
    private static final ThreadLocal<Arena> WHITE=new ThreadLocal<>();
    private static double whiteHealth(){
        try{
            Object hp=MxpReflect.field(Class.forName("com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs"),"cosmicCrystalHealth");
            return hp instanceof Integer i&&i>0?6.0*i:0;
        }catch(ClassNotFoundException|RuntimeException|LinkageError e){return 0;}
    }
    public static void whiteVictory(ServerLevel level,Object controller,boolean won,Runnable nativeEnd){
        // Refuse reentrant or inactive events, preserve every native invocation.
        if(!won||WHITE.get()!=null||!MxpMod.active()||!pinned("the_bumblezone","7.17.3+1.21.1-neoforge")
            ||!level.getServer().isSameThread()||!(MxpReflect.call(controller,"getEventTimer") instanceof Number timer)
            ||timer.longValue()<=0){nativeEnd.run();return;}
        var arena=new Arena(level,"boss:cosmic_crystal:"+UUID.randomUUID(),whiteHealth(),new LinkedHashMap<>());
        WHITE.set(arena);
        try{
            nativeEnd.run();
            // Native EndEvent resets timer after awarding; never settle incomplete/reset-failed calls.
            if(MxpReflect.call(controller,"getEventTimer") instanceof Number end&&end.longValue()==0){
                for(ServerPlayer player:arena.winners().values()){
                    if(!eligible(player,level))continue;
                    try{
                        var receipt=MxpService.standardBossVictory(player,"the_bumblezone:cosmic_crystal_entity",arena.tx());
                        if(receipt.accepted())player.sendSystemMessage(Component.literal("MXP: "+String.format("%.2f",receipt.effectiveXP())+
                            (receipt.first()?" (first victory)":" (repeat victory)")));
                        if(arena.maxHealth()>0)HunterExperience.cosmicCrystalArenaVictory(player,arena.maxHealth(),arena.tx());
                    }catch(RuntimeException e){MxpMod.LOGGER.error("White arena reward failed ({})",player.getUUID(),e);}
                }
            }
        }finally{WHITE.remove();}
    }
    private static boolean eligible(ServerPlayer p,ServerLevel level){
        return p!=null&&p.server==level.getServer()&&!(p instanceof FakePlayer)&&MxpPlayerPolicy.eligible(p);
    }
    public static void whiteWinner(ServerPlayer player){
        var arena=WHITE.get();
        if(arena!=null&&eligible(player,arena.level()))arena.winners().putIfAbsent(player.getUUID(),player);
    }
    public static void clear(){WHITE.remove();}
}
