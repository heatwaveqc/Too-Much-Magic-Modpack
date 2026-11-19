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
    private VampireEffects(){}
    private record Quote(ServerPlayer player,int nominal,int price){}
    public record Balance(int current,int max){}
    public static boolean eligible(ServerPlayer p){
        return MxpPlayerPolicy.eligible(p)&&p.isAlive()&&!NewbieMode.isNewbie(p)&&
            ModList.get().getModContainerById("witchery").filter(c->c.getModInfo().getVersion().toString().startsWith("0.5.39")).isPresent()&&JawrFormState.present(p,"vampire");
    }
    public static Balance read(ServerPlayer p){
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
    /** Legitimate bite feeding only: actual positive recipient gain, capped by capacity.
     * Never amplify a vampire donor, conversion, orb transfer, regeneration, or a failed bite. */
    public static void feed(ServerPlayer p,LivingEntity victim,int amount,IntConsumer original){
        if(!eligible(p)||victim==p||victim==null||victim instanceof net.minecraft.world.entity.player.Player||amount<=0){original.accept(amount);return;}
        Balance before=read(p);
        if(before==null){original.accept(amount);return;}
        double rate=rate(p,"jawr_feeding_bonus");
        original.accept(amount);
        Balance after=read(p);
        if(after==null||after.max!=before.max)return;
        int gained=after.current-before.current;
        if(gained<=0||gained>amount)return;
        int bonus=BloodSkillMath.feedingBonus(gained,after.max-after.current,rate);
        if(bonus>0)original.accept(bonus);
    }
    public static float sun(ServerPlayer p,float amount){
        return BloodSkillMath.sunDamage(amount,rate(p,"jawr_sun_resistance"));
    }
}
