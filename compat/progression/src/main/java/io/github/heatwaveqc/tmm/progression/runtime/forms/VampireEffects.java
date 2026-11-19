package io.github.heatwaveqc.tmm.progression.runtime.forms;

import io.github.heatwaveqc.tmm.progression.foundation.BloodSkillMath;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpPlayerPolicy;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpReflect;
import io.github.heatwaveqc.tmm.progression.runtime.EffectRuntime;
import io.github.heatwaveqc.tmm.progression.runtime.NewbieMode;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModList;
import java.util.function.IntConsumer;

/** Only native, eligible vampires can obtain these optional effects. */
public final class VampireEffects {
    private static final String ATTACHMENT="dev.sterner.witchery.feature.blood_pool.BloodPoolAttachment";
    private static final ThreadLocal<Quote> QUOTE=new ThreadLocal<>();
    private static final ThreadLocal<Feeding> FEEDING=new ThreadLocal<>();
    private record Feeding(ServerPlayer p,LivingEntity victim,Balance donor){}
    private VampireEffects(){}
    private record Quote(ServerPlayer player,int nominal,int price){}
    public record Balance(int current,int max){}
    public static boolean eligible(ServerPlayer p){
        return MxpPlayerPolicy.eligible(p)&&p.isAlive()&&!NewbieMode.isNewbie(p)&&
            ModList.get().getModContainerById("witchery").filter(c->c.getModInfo().getVersion().toString().startsWith("0.5.39")).isPresent()&&JawrFormState.present(p,"vampire");
    }
    public static Balance read(LivingEntity p){
        Object d=MxpReflect.staticCall(ATTACHMENT,"getData",p);
        Object c=MxpReflect.call(d,"getBloodPool"),m=MxpReflect.call(d,"getMaxBlood");
        return c instanceof Integer current&&m instanceof Integer max&&max>0&&current>=0&&current<=max?new Balance(current,max):null;
    }
    public static double rate(ServerPlayer p,String type){
        if(!eligible(p))return 0;
        double n=0;
        for(var a:EffectRuntime.active(p))if(a.effect().type().equals(type))n+=a.effect().amountPerRank()*a.rank();
        return Double.isFinite(n)?Math.max(0,n):0;
    }
    public static int price(ServerPlayer p,int nominal){
        return BloodSkillMath.price(nominal,rate(p,"jawr_blood_thrift"));
    }
    /** Own the price once upstream of a native self-pay call; saved nominal is used only by E4 receipt. */
    public static void quoted(ServerPlayer p,int nominal,int price,IntConsumer nativeDebit){
        Quote old=QUOTE.get();
        if(old!=null){nativeDebit.accept(price);return;}
        QUOTE.set(new Quote(p,nominal,price));
        try{nativeDebit.accept(price);}finally{QUOTE.remove();}
    }
    public static void selfDebit(LivingEntity entity,int nominal,IntConsumer nativeDebit){
        if(!(entity instanceof ServerPlayer p)||!eligible(p)||nominal<=0){nativeDebit.accept(nominal);return;}
        quoted(p,nominal,price(p,nominal),nativeDebit);
    }
    public static int nominal(ServerPlayer p,int charged){
        Quote quote=QUOTE.get();
        return quote!=null&&quote.player==p&&quote.price==charged?quote.nominal:charged;
    }
    /** Known extraction/transfer sites are not practice for the drained player. */
    public static boolean externalExtraction(){
        return StackWalker.getInstance().walk(frames->frames.anyMatch(f->{
            String c=f.getClassName();
            return c.equals("dev.sterner.witchery.content.ability.DrinkBloodAbility")||
                   c.equals("dev.sterner.witchery.content.item.BoneNeedleItem")||
                   c.equals("dev.sterner.witchery.content.entity.VampireEntity")||
                   c.equals("dev.sterner.witchery.integration.neovitae.NeoVitaeCompat");
        }));
    }
    public static int feeding(ServerPlayer p,LivingEntity victim,java.util.function.IntSupplier original){
        Feeding prior=FEEDING.get();
        if(prior!=null){
            FEEDING.remove();
            try{return original.getAsInt();}finally{FEEDING.set(prior);}
        }
        if(!eligible(p)||victim==null||victim==p)return original.getAsInt();
        Balance donor=read(victim);if(donor==null||donor.current<=0)return original.getAsInt();
        FEEDING.set(new Feeding(p,victim,donor));
        try{return original.getAsInt();}finally{FEEDING.remove();}
    }
    /** Legitimate bite feeding, including player donors: measured donor loss and recipient gain.
     * Never amplify conversion, orb transfer, regeneration, or a failed bite. */
    public static void feed(ServerPlayer p,LivingEntity victim,int amount,IntConsumer original){
        if(!eligible(p)||victim==p||victim==null||amount<=0){original.accept(amount);return;}
        Feeding context=FEEDING.get();Balance donor=read(victim);
        Balance before=read(p);
        if(context==null||context.p!=p||context.victim!=victim||donor==null||context.donor.max!=donor.max||before==null){original.accept(amount);return;}
        int actualDonor=context.donor.current-donor.current;
        if(actualDonor<=0){original.accept(amount);return;}
        applyFeedingGain(p,before,amount,actualDonor,original);
    }
    private static final ThreadLocal<Boolean> DRINKING=new ThreadLocal<>();
    private static boolean consumptiveCaller(){
        return StackWalker.getInstance().walk(frames->{
            var stack=frames.toList();
            if(stack.stream().anyMatch(f->io.github.heatwaveqc.tmm.progression.foundation.FeedingSource.nativeDrink(f.getClassName(),f.getMethodName())))return true;
            return stack.stream().anyMatch(f->io.github.heatwaveqc.tmm.progression.foundation.FeedingSource.interopCredit(f.getClassName(),f.getMethodName()))&&
                stack.stream().anyMatch(f->io.github.heatwaveqc.tmm.progression.foundation.FeedingSource.interopFinish(f.getClassName(),f.getMethodName()));
        });
    }
    /** Shared exact native increaseBlood seam, but only verified drink/meal callers.
     * The three-bottle bridge already performs the ordinary-bottle safety/finish gate.
     * No duplicate credit, no refilling via Neo Vitae, no passive hunts/regeneration. */
    public static void drink(LivingEntity recipient,int amount,IntConsumer original){
        if(!(recipient instanceof ServerPlayer p)||amount<=0||!eligible(p)||DRINKING.get()!=null||FEEDING.get()!=null||
            !consumptiveCaller()){original.accept(amount);return;}
        Balance before=read(p);
        if(before==null){original.accept(amount);return;}
        DRINKING.set(true);
        try{applyFeedingGain(p,before,amount,amount,original);}
        finally{DRINKING.remove();}
    }
    private static void applyFeedingGain(ServerPlayer p,Balance before,int amount,int sourceDebit,IntConsumer original){
        double fraction=rate(p,"jawr_feeding_bonus");
        original.accept(amount);
        if(fraction<=0||!eligible(p))return;
        Balance after=read(p);
        if(after==null||after.max!=before.max)return;
        int gained=after.current-before.current;
        if(gained<=0||gained>amount)return;
        int bonus=BloodSkillMath.feedingBonus(Math.min(sourceDebit,gained),after.max-after.current,fraction);
        if(bonus>0)original.accept(bonus);
    }
    public static double batDuration(net.minecraft.world.entity.player.Player p,double nativeTicks){
        if(!(p instanceof ServerPlayer server)||NativeFormConditions.jawrShape(server,"BAT")!=io.github.heatwaveqc.tmm.progression.foundation.Condition.Result.PASS)return nativeTicks;
        return BloodSkillMath.batDuration(nativeTicks,rate(server,"jawr_bat_duration"));
    }
    public static float sun(ServerPlayer p,float amount){
        return BloodSkillMath.sunDamage(amount,rate(p,"jawr_sun_resistance"));
    }
}
