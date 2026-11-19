package io.github.heatwaveqc.tmm.progression.mxp;

import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import io.github.heatwaveqc.tmm.progression.core.MxpMath;
import io.github.heatwaveqc.tmm.progression.model.XpChannel;
import io.github.heatwaveqc.tmm.progression.state.Wallet;
import io.github.heatwaveqc.tmm.progression.state.ProgressionAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import java.util.HashMap;
import java.util.Map;

/** First-party MXP source contract, now backed by the shared authoritative attachment. */
public final class MxpService {
    private MxpService(){}
    public record AwardStatus(boolean accepted,boolean duplicate,boolean first,double effectiveXP,
             float deliveredToTyz,long lifetimeLevels,double progress,double pendingXP,String note){}
    public static final double FIRST_BOSS_MXP=1000;
    public record BossRule(double repeat,boolean repeatFixed){
        public BossRule {if(!Double.isFinite(repeat)||repeat<=0)throw new IllegalArgumentException("Invalid boss reward");}
    }
    private static final Map<String,BossRule> RULES=new HashMap<>();
    public static void registerBossRule(String entityId,double repeat,boolean repeatFixed){
        ResourceLocation id=ResourceLocation.tryParse(entityId);
        if(id==null||!id.toString().equals(entityId))throw new IllegalArgumentException("Invalid boss ID");
        RULES.put(entityId,new BossRule(repeat,repeatFixed));
    }
    public static BossRule bossRule(String entityId){return RULES.get(entityId);}
    public static void clearBossRules(){RULES.clear();}
    public static Wallet inspect(ServerPlayer player){
        var w=player.getData(ProgressionAttachments.LEDGER.get()).wallet(XpChannel.MXP);
        return w==null?Wallet.initial(XpChannel.mxp(1.02)):w;
    }
    private static AwardStatus ignored(ServerPlayer player,String note){
        Wallet w=inspect(player);return new AwardStatus(false,false,false,0,0,w.magicLevels(),w.magicProgress(),0,note);
    }
    private static AwardStatus award(ServerPlayer player,String tx,String completion,double first,double repeat,
                                     boolean firstFixed,boolean repeatFixed){
        if(!MxpMod.active())return ignored(player,"Legacy MXP installed");
        Wallet before=inspect(player);
        if(before.recentTransactions().contains(tx))return new AwardStatus(false,true,false,0,0,before.magicLevels(),before.magicProgress(),0,"Duplicate committed action");
        boolean accepted=completion==null?
            ProgressionApi.awardMagicOrdinary(player,XpChannel.MXP,tx,repeat):
            ProgressionApi.awardMagicCompletion(player,XpChannel.MXP,tx,completion,first,repeat,firstFixed,repeatFixed);
        Wallet after=inspect(player);
        double effective=(after.magicLevels()-before.magicLevels())*1000.0+after.magicProgress()-before.magicProgress();
        return new AwardStatus(accepted,false,accepted&&completion!=null&&!before.firstCompletions().contains(completion),
            effective,0,after.magicLevels(),after.magicProgress(),0,accepted?"Recorded":"Rejected");
    }
    public static AwardStatus resource(ServerPlayer player,String source,String tx,double nominal,double pool,double actual){
        if(!Double.isFinite(actual)||actual<=0||!Double.isFinite(nominal)||nominal<=0||!Double.isFinite(pool)||pool<=0)
            return ignored(player,"No verified positive debit");
        double base=MxpMath.normalizedBase(nominal,pool);
        return award(player,tx,null,base,base,false,false);
    }
    public static AwardStatus ritual(ServerPlayer player,String id,String tx,double first,double repeat){
        return award(player,tx,"ritual:"+id,first,repeat,true,false);
    }
    public static AwardStatus craft(ServerPlayer player,String id,String tx,double first,double repeat){
        return award(player,tx,"craft:"+id,first,repeat,false,false);
    }
    public static AwardStatus boss(ServerPlayer player,String id,String tx){
        BossRule rule=bossRule(id);
        return rule==null?ignored(player,"Unregistered boss"):award(player,tx,"boss:"+id,FIRST_BOSS_MXP,rule.repeat(),true,rule.repeatFixed());
    }
    /** Registry-authorized standard encounter, retaining canonical persisted boss completion key. */
    public static AwardStatus standardBossVictory(ServerPlayer player,String id,String tx){
        BossRule configured=bossRule(id);
        double repeat=configured==null?MxpProgressionConfig.BOSS_REPEAT.get():configured.repeat();
        return award(player,tx,"boss:"+id,FIRST_BOSS_MXP,repeat,true,configured!=null&&configured.repeatFixed());
    }
    /** Native dragon advancement only; persistent first key, explicitly zero repeat. */
    public static AwardStatus dragonAdvancement(ServerPlayer player,String advancementId){
        String key=io.github.heatwaveqc.tmm.progression.foundation.BossRewardPolicy.dragonAdvancementKey(advancementId);
        if(key==null||!MxpMod.active()||player instanceof net.neoforged.neoforge.common.util.FakePlayer
            ||player.getServer()==null||!player.getServer().isSameThread()
            ||player.getServer().getPlayerList().getPlayer(player.getUUID())!=player)
            return ignored(player,"No eligible native dragon advancement");
        return award(player,"advancement:"+advancementId,key,FIRST_BOSS_MXP,0,true,false);
    }
    public static AwardStatus minibossVictory(ServerPlayer player,String id,String tx){
        return award(player,tx,"miniboss:"+id,250,MxpProgressionConfig.MINIBOSS_REPEAT.get(),true,false);
    }
    /** No completion key: an empowered ordinary mob never consumes a first-boss discovery. */
    public static AwardStatus eliteVictory(ServerPlayer player,String tx,double base){
        if(!Double.isFinite(base)||base<=0)return ignored(player,"No verified elite reward");
        return award(player,tx,null,base,base,false,false);
    }
    public static AwardStatus jawrCommittedBloodDebit(ServerPlayer player,String tx,double original,double actual){
        if(!MxpMod.active())return ignored(player,"Legacy MXP installed");
        if(!player.server.isSameThread()||player.isCreative()||player.isSpectator()||
            !(io.github.heatwaveqc.tmm.progression.runtime.forms.JawrFormState.present(player,"vampire")||
              io.github.heatwaveqc.tmm.progression.runtime.forms.JawrFormState.present(player,"werewolf")))
            return ignored(player,"No verified eligible native JAWR form");
        return resource(player,"witchery:player_blood",tx,original,MxpSourceConfig.JAWR_INITIAL_BLOOD_POOL.get(),actual);
    }
    public static AwardStatus fixedDiagnostic(ServerPlayer player,String tx,double amount){
        return ritual(player,"diagnostic:"+tx,tx,amount,amount);
    }
}
