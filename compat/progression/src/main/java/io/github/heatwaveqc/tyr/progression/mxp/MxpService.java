package io.github.heatwaveqc.tyr.progression.mxp;

import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.core.MxpMath;
import io.github.heatwaveqc.tyr.progression.model.XpChannel;
import io.github.heatwaveqc.tyr.progression.state.Wallet;
import io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;
import java.util.List;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.data.XpSourceRouting;
import io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy;

/** First-party MXP source contract, now backed by the shared authoritative attachment. */
public final class MxpService {
    private MxpService(){}
    public record AwardStatus(boolean accepted,boolean duplicate,boolean first,double effectiveXP,
             float deliveredToTyz,long lifetimeLevels,double progress,double pendingXP,String note,
             Map<String,XpSourceRouting.Settlement> destinations){
        public AwardStatus {
            destinations=java.util.Collections.unmodifiableMap(new java.util.LinkedHashMap<>(destinations));
        }
        public AwardStatus(boolean accepted,boolean duplicate,boolean first,double effectiveXP,
                float deliveredToTyz,long lifetimeLevels,double progress,double pendingXP,String note){
            this(accepted,duplicate,first,effectiveXP,deliveredToTyz,lifetimeLevels,progress,pendingXP,note,Map.of());
        }
        /** Do not label non-MXP currencies as MXP or mix first/repeat state. */
        public String rewardSummary(String firstLabel,String repeatLabel){
            return destinations.entrySet().stream().map(row->{
                String name=row.getKey().equals(XpChannel.MXP)?"MXP":row.getKey();
                var reward=row.getValue();
                return name+": "+String.format(java.util.Locale.ROOT,"%.2f",reward.effectiveXp())
                        +(reward.first()?firstLabel:repeatLabel);
            }).collect(java.util.stream.Collectors.joining("; "));
        }
    }
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
    private static AwardStatus award(ServerPlayer player,String source,String tx,String completion,double first,double repeat,
                                     boolean firstFixed,boolean repeatFixed){
        var before=player.getData(ProgressionAttachments.LEDGER.get()).economy();
        var destinations=PathRegistry.economyCatalog().routing().destinations(source,XpChannel.MXP);
        // The router must see the event even if MXP already settled: another
        // destination might have been gated or disabled during the first call.
        boolean accepted=completion==null?
            ProgressionApi.awardMagicOrdinary(player,XpChannel.MXP,source,tx,repeat):
            ProgressionApi.awardMagicCompletion(player,XpChannel.MXP,source,tx,completion,first,repeat,firstFixed,repeatFixed);
        return status(player,source,tx,completion,before,destinations,accepted);
    }

    private static AwardStatus status(ServerPlayer player,String source,String tx,String completion,
            ExperienceEconomy.Ledger before,List<XpSourceRouting.Destination> destinations,boolean accepted){
        var after=player.getData(ProgressionAttachments.LEDGER.get()).economy();
        var settled=XpSourceRouting.settled(destinations,source,tx,completion,before,after);
        var mxp=settled.get(XpChannel.MXP);
        Wallet display=inspect(player);
        boolean duplicate=!accepted&&XpSourceRouting.alreadySettled(destinations,source,tx,before);
        boolean first=settled.values().stream().anyMatch(XpSourceRouting.Settlement::first);
        return new AwardStatus(accepted,duplicate,first,
                mxp==null?0:mxp.effectiveXp().doubleValue(),0,display.magicLevels(),display.magicProgress(),0,
                accepted?"Recorded":duplicate?"Duplicate committed action":"Rejected",settled);
    }

    public static AwardStatus resource(ServerPlayer player,String source,String tx,double nominal,double pool,double actual){
        if(!Double.isFinite(actual)||actual<=0||!Double.isFinite(nominal)||nominal<=0||!Double.isFinite(pool)||pool<=0)
            return ignored(player,"No verified positive debit");
        double base=MxpMath.normalizedBase(nominal,pool);
        return award(player,source,tx,null,base,base,false,false);
    }
    public static AwardStatus ritual(ServerPlayer player,String id,String tx,double first,double repeat){
        return award(player,"tyr_progression:ritual",tx,"ritual:"+id,first,repeat,true,false);
    }
    public static AwardStatus craft(ServerPlayer player,String id,String tx,double first,double repeat){
        return award(player,"tyr_progression:craft",tx,"craft:"+id,first,repeat,false,false);
    }
    public static AwardStatus boss(ServerPlayer player,String id,String tx){
        BossRule rule=bossRule(id);
        return rule==null?ignored(player,"Unregistered boss"):award(player,"tyr_progression:boss",tx,"boss:"+id,FIRST_BOSS_MXP,rule.repeat(),true,rule.repeatFixed());
    }
    /** Registry-authorized standard encounter, retaining canonical persisted boss completion key. */
    public static AwardStatus standardBossVictory(ServerPlayer player,String id,String tx){
        BossRule configured=bossRule(id);
        double repeat=configured==null?MxpProgressionConfig.BOSS_REPEAT.get():configured.repeat();
        return award(player,"tyr_progression:boss",tx,"boss:"+id,FIRST_BOSS_MXP,repeat,true,configured!=null&&configured.repeatFixed());
    }
    /** Native fight-manager completion only; first fixed 1000, subsequent fixed 100. */
    public static AwardStatus chaosVictory(ServerPlayer player,String tx){
        return award(player,"tyr_progression:boss",tx,"boss:draconicevolution:draconic_guardian",FIRST_BOSS_MXP,100,true,true);
    }
    /** Native dragon advancement only; persistent first key, explicitly zero repeat. */
    public static AwardStatus dragonAdvancement(ServerPlayer player,String advancementId){
        String key=io.github.heatwaveqc.tyr.progression.foundation.BossRewardPolicy.dragonAdvancementKey(advancementId);
        if(key==null||player instanceof net.neoforged.neoforge.common.util.FakePlayer
            ||player.getServer()==null||!player.getServer().isSameThread()
            ||player.getServer().getPlayerList().getPlayer(player.getUUID())!=player)
            return ignored(player,"No eligible native dragon advancement");
        return award(player,"tyr_progression:advancement","advancement:"+advancementId,key,FIRST_BOSS_MXP,0,true,false);
    }
    public static AwardStatus minibossVictory(ServerPlayer player,String id,String tx){
        return award(player,"tyr_progression:miniboss",tx,"miniboss:"+id,250,MxpProgressionConfig.MINIBOSS_REPEAT.get(),true,false);
    }
    /** No completion key: an empowered ordinary mob never consumes a first-boss discovery. */
    public static AwardStatus eliteVictory(ServerPlayer player,String tx,double base){
        if(!Double.isFinite(base)||base<=0)return ignored(player,"No verified elite reward");
        return award(player,"tyr_progression:elite",tx,null,base,base,false,false);
    }
    public static AwardStatus jawrCommittedBloodDebit(ServerPlayer player,String tx,double original,double actual){
        if(!player.server.isSameThread()||player.isCreative()||player.isSpectator()||
            !(io.github.heatwaveqc.tyr.progression.runtime.forms.JawrFormState.present(player,"vampire")||
              io.github.heatwaveqc.tyr.progression.runtime.forms.JawrFormState.present(player,"werewolf")))
            return ignored(player,"No verified eligible native JAWR form");
        return resource(player,"witchery:player_blood",tx,original,MxpSourceConfig.JAWR_INITIAL_BLOOD_POOL.get(),actual);
    }
    /** Administrative test award: no fictitious ritual, debit or discovery receipt. */
    public static AwardStatus diagnostic(ServerPlayer player,String tx,double amount,boolean fixed){
        if(!Double.isFinite(amount)||amount<=0)return ignored(player,"Invalid diagnostic amount");
        String source="tyr_progression:diagnostic";
        var before=player.getData(ProgressionAttachments.LEDGER.get()).economy();
        boolean accepted=ProgressionApi.awardExplicitConfiguredXp(player,XpChannel.MXP,source,tx,
                null,BigDecimal.valueOf(amount),fixed);
        return status(player,source,tx,null,before,List.of(new XpSourceRouting.Destination(XpChannel.MXP,1)),accepted);
    }
    public static AwardStatus fixedDiagnostic(ServerPlayer player,String tx,double amount){
        return diagnostic(player,tx,amount,true);
    }
}
