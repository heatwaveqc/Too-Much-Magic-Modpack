package io.github.heatwaveqc.tmm.progression.mxp.hooks;

import io.github.heatwaveqc.tmm.progression.mxp.MxpMod;
import io.github.heatwaveqc.tmm.progression.mxp.core.AnsThriftMath;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Version-gated adapter over Ars 'n Spells 3.3.5's immutable native quote.
 * Called only at the three managed construction sites, never at a debit/preview mutation.
 * No dependency on upstream classes at compile time; a failed reflective construction
 * returns the original quote intact, including its native payer and rules generation.
 */
public final class AnsThriftQuotes {
    private static final String CONTRACT="com.otectus.arsnspells.contract.";
    private static final String IRON="irons_spellbooks:spell", ARS="ars_nouveau:spell";
    private static final AtomicBoolean WARNED=new AtomicBoolean();
    private AnsThriftQuotes(){}

    private static boolean supported(){
        return ModList.get().getModContainerById("ars_n_spells")
            .map(c->"3.3.5".equals(c.getModInfo().getVersion().toString())).orElse(false);
    }

    public static Object ars(Object quote,Object event){
        if(!supported())return quote;
        Object context=MxpReflect.field(event,"context");
        Object caster=MxpReflect.call(context,"getUnwrappedCaster");
        return caster instanceof ServerPlayer p?initial(quote,p,ARS,"ARS_MANA"):quote;
    }

    public static Object ironInitial(Object quote,Player player){
        return player instanceof ServerPlayer p&&supported()?initial(quote,p,IRON,"IRONS_MANA"):quote;
    }

    private static Object initial(Object quote,ServerPlayer player,String selector,String origin){
        if(!MxpPlayerPolicy.eligible(player))return quote;
        try{
            double factor=AnsThriftMath.factor(MagicCostPolicy.rate(player,selector));
            return transform(quote,origin,factor);
        }catch(RuntimeException|LinkageError failed){warn(failed);return quote;}
    }

    /**
     * The native plan latches the rate across windup/channel. Repricing from another
     * cost event uses the rate on the initial plan, not a second live skill read.
     * Unmarked original plans mean zero Thrift, including ABI failure/legacy skills.
     */
    public static Object ironReprice(Object quote,Player player){
        if(!supported()||!(player instanceof ServerPlayer p)||!MxpPlayerPolicy.eligible(p))return quote;
        try{
            Class<?> owner=Class.forName("com.otectus.arsnspells.casting.IronsCastPayments");
            Object raw=MxpReflect.field(owner,"PLANS");
            Object data=MxpReflect.staticCall("io.redspace.ironsspellbooks.api.magic.MagicData","getPlayerMagicData",p);
            if(!(raw instanceof Map<?,?> plans)||data==null)return quote;
            Object prior=MxpReflect.field(plans.get(data),"quote");
            if(prior==null)return quote;
            // A plan with no marker deliberately has the native price.
            double frozen=marker(prior);
            return AnsThriftMath.validFactor(frozen)?transform(quote,"IRONS_MANA",frozen):quote;
        }catch(ReflectiveOperationException|RuntimeException|LinkageError failed){warn(failed);return quote;}
    }

    static double marker(Object quote){
        Object steps=MxpReflect.call(quote,"breakdown");
        if(!(steps instanceof List<?> list))return Double.NaN;
        double marker=1;
        boolean found=false;
        for(Object step:list){
            if(AnsThriftMath.MARKER.equals(MxpReflect.call(step,"id"))){
                if(found||!Boolean.TRUE.equals(MxpReflect.call(step,"multiplicative")))return Double.NaN;
                marker=MxpReflect.number(MxpReflect.call(step,"factorOrDelta"));
                if(!AnsThriftMath.validFactor(marker))return Double.NaN;
                found=true;
            }
        }
        return marker;
    }

    /**
     * Rebuild solely the native quote's legs and append provenance. All-or-nothing;
     * no writes to native objects/maps. Original origin remains available for MXP.
     */
    static Object transform(Object quote,String originUnit,double factor){
        if(quote==null||!AnsThriftMath.validFactor(factor)||factor==1)return quote;
        try{
            Class<?> quoteType=quote.getClass();
            if(!quoteType.getName().equals(CONTRACT+"CostQuote"))return quote;
            // Never transform a quote already carrying our marker (even a malformed marker).
            double existing=marker(quote);
            if(existing!=1)return quote;
            Object origin=MxpReflect.call(quote,"origin");
            Object rawSteps=MxpReflect.call(quote,"breakdown");
            Object rawLegs=MxpReflect.call(quote,"legs");
            Object generation=MxpReflect.call(quote,"rulesGeneration");
            if(origin==null||!originUnit.equals(String.valueOf(MxpReflect.call(origin,"unit")))||
                !(rawSteps instanceof List<?> steps)||!(rawLegs instanceof List<?> legs)||
                !(generation instanceof Integer gen))return quote;
            double nominal=MxpReflect.number(MxpReflect.call(origin,"amount"));
            if(!Double.isFinite(nominal)||nominal<0)return quote;
            Class<?> amountType=origin.getClass();
            if(!amountType.getName().equals(CONTRACT+"ResourceAmount"))return quote;
            ClassLoader loader=quoteType.getClassLoader();
            Class<?> unitType=Class.forName(CONTRACT+"ResourceUnit",false,loader);
            Class<?> modifierType=Class.forName(CONTRACT+"QuoteModifier",false,loader);
            var input=new ArrayList<AnsThriftMath.Leg>(legs.size());
            var units=new ArrayList<Object>(legs.size());
            for(Object leg:legs){
                if(leg==null||leg.getClass()!=amountType)return quote;
                Object unit=MxpReflect.call(leg,"unit");
                if(!unitType.isInstance(unit))return quote;
                units.add(unit);
                input.add(new AnsThriftMath.Leg(String.valueOf(unit),MxpReflect.number(MxpReflect.call(leg,"amount"))));
            }
            var discounted=AnsThriftMath.discount(input,factor);
            if(discounted==null)return quote;
            var amountConstructor=amountType.getConstructor(unitType,double.class);
            var adjusted=new ArrayList<Object>(legs.size());
            for(int i=0;i<discounted.size();i++)adjusted.add(amountConstructor.newInstance(units.get(i),discounted.get(i).amount()));
            var breakdown=new ArrayList<Object>(steps);
            breakdown.add(modifierType.getMethod("factor",String.class,String.class,double.class)
                .invoke(null,AnsThriftMath.MARKER,"TMM Thrift (ceil, min 1 per positive mana leg)",factor));
            Object result=quoteType.getConstructor(amountType,List.class,List.class,int.class)
                .newInstance(origin,breakdown,adjusted,gen);
            return result;
        }catch(ReflectiveOperationException|RuntimeException|LinkageError failed){warn(failed);return quote;}
    }

    private static void warn(Throwable failure){
        if(WARNED.compareAndSet(false,true))MxpMod.LOGGER.warn("Ars 'n Spells Thrift quote unavailable; retaining native price: {}",failure.toString());
    }
}
