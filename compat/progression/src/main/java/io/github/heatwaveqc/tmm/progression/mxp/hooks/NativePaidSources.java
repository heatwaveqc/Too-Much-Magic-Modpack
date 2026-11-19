package io.github.heatwaveqc.tmm.progression.mxp.hooks;

import io.github.heatwaveqc.tmm.progression.mxp.*;
import io.github.heatwaveqc.tmm.progression.mxp.core.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import java.util.UUID;

/** Independent receipt scopes; native operations remain authoritative. No resource transfers. */
public final class NativePaidSources {
    private NativePaidSources(){}
    public static boolean eligible(ServerPlayer p){return MxpMod.active()&&MxpPlayerPolicy.eligible(p);}
    public static void credit(ServerPlayer p,String source,double nominal,double pool,double actual){
        if(!eligible(p)||!PaymentReceiptPolicy.positive(nominal)||!PaymentReceiptPolicy.positive(pool)||!PaymentReceiptPolicy.positive(actual))return;
        try{MxpService.resource(p,source,source+":"+UUID.randomUUID(),nominal,pool,actual);}
        catch(RuntimeException e){MxpMod.LOGGER.warn("{} MXP receipt rejected: {}",source,e.toString());}
    }
    public static final class Hex {
        private static final ThreadLocal<Scope> CURRENT=new ThreadLocal<>();
        private static final class Scope {
            final ServerPlayer player; final Object environment; final long nominal;
            long price; double paid; boolean valid=true;
            Scope(ServerPlayer p,Object e,long nominal){this.player=p;environment=e;this.nominal=nominal;}
        }
        public static long cast(Object env,long cost,java.util.function.LongSupplier run){
            Object caster=MxpReflect.call(env,"getCaster");
            if(!(caster instanceof ServerPlayer p)||!eligible(p)||cost<=0||MxpSourceConfig.HEX_MEDIA_POOL.get()<=0||
                !isPersonal(env))return run.getAsLong();
            Scope outer=CURRENT.get();
            if(outer!=null){outer.valid=false;return run.getAsLong();}
            Scope s=new Scope(p,env,cost);CURRENT.set(s);
            try{
                long remaining=run.getAsLong();
                if(s.valid&&remaining<=0&&ObservedPayment.funded(s.paid,s.price))
                    credit(p,"hexcasting:media",s.nominal,MxpSourceConfig.HEX_MEDIA_POOL.get(),s.paid);
                return remaining;
            }finally{CURRENT.remove();}
        }
        private static boolean isPersonal(Object env){
            // Known native inventory/equipped carrier paths; no owner-associated circles or unknown addons.
            String name=env.getClass().getName();
            return name.equals("at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv")
                ||name.equals("at.petrak.hexcasting.api.casting.eval.env.PackagedItemCastEnv");
        }
        public static void price(Object env,long price){
            Scope s=CURRENT.get();
            if(s!=null&&s.environment==env)s.price=price;
        }
        public static double before(Object holder){
            Scope s=CURRENT.get();if(s==null)return Double.NaN;
            return MxpReflect.number(MxpReflect.call(holder,"withdrawMedia",-1L,true));
        }
        public static void removed(Object holder,long request,long result,double before,boolean simulated){
            Scope s=CURRENT.get();if(s==null||simulated||request<=0)return;
            // An infinite or unobservable holder cannot establish a genuine finite debit.
            double after=MxpReflect.number(MxpReflect.call(holder,"withdrawMedia",-1L,true));
            if(!Double.isFinite(before)||!Double.isFinite(after)||before<0||after<0){s.valid=false;return;}
            if(result>0){
                double paid=ObservedPayment.proven(before,after,result,request);
                if(paid+1e-6<Math.min(result,request)){s.valid=false;return;}
                s.paid+=paid;
            }
        }
    }

    public static final class Roots {
        private static final ThreadLocal<Scope> CURRENT=new ThreadLocal<>();
        private static final class Scope{
            final ServerPlayer player;double debit,nominal;boolean valid=true;
            Scope(ServerPlayer p){player=p;}
        }
        public static void release(net.minecraft.world.entity.LivingEntity caster,Runnable run){
            if(!(caster instanceof ServerPlayer p)||!eligible(p)||MxpSourceConfig.ROOTS_TERRA_POOL.get()<=0){run.run();return;}
            Scope outer=CURRENT.get();if(outer!=null){outer.valid=false;run.run();return;}
            Scope s=new Scope(p);CURRENT.set(s);
            try{run.run();if(s.valid)credit(p,"rootsclassic:terra",s.nominal,MxpSourceConfig.ROOTS_TERRA_POOL.get(),s.debit);}
            finally{CURRENT.remove();}
        }
        public static double before(Object mana){return CURRENT.get()==null?Double.NaN:MxpReflect.number(MxpReflect.call(mana,"getMana"));}
        public static void removed(Object mana,float target,double before){
            Scope s=CURRENT.get();if(s==null)return;
            double after=MxpReflect.number(MxpReflect.call(mana,"getMana"));
            if(!Double.isFinite(before)||!Double.isFinite(target)||!Double.isFinite(after)||target<0||
                Math.abs(after-target)>Math.max(1e-5,Math.ulp(target)*2)||s.debit>0){s.valid=false;return;}
            s.debit=ObservedPayment.delta(before,after);
        }
        public static void effect(Object component,net.minecraft.world.entity.Entity caster){
            Scope s=CURRENT.get();if(s==null||s.player!=caster||s.debit<=0)return;
            if(s.nominal>0){s.valid=false;return;}
            s.nominal=MxpReflect.number(MxpReflect.call(component,"getManaCost"));
            if(!PaymentReceiptPolicy.positive(s.nominal)||s.debit>s.nominal+1e-5)s.valid=false;
        }
    }

    public static final class Embers{
        private static final ThreadLocal<Scope> CURRENT=new ThreadLocal<>();
        private static final class Scope{
            final ServerPlayer player;final double nominal;double price,paid;boolean valid=true;
            Scope(ServerPlayer p,double nominal){player=p;this.nominal=nominal;}
        }
        private static final java.util.Set<String> CALLERS=java.util.Set.of(
            "item.CinderStaffItem","item.BlazingRayItem","item.DawnstoneToolUtil","item.ClockworkToolItem",
            "augment.CinderJetAugment","augment.FlameBarrierAugment","augment.SuperheaterAugment",
            "augment.IntelligentApparatusAugment","augment.BlastingCoreAugment","augment.ResonatingBellAugment",
            "util.SuperHeaterLootModifier","network.message.MessageCasterOrb");
        private static boolean personalCaller(){
            String prefix="com.rekindled.embers.";
            for(StackTraceElement frame:Thread.currentThread().getStackTrace()){
                String name=frame.getClassName();
                if(name.startsWith(prefix)&&CALLERS.contains(name.substring(prefix.length()).split("\\$")[0]))return true;
            }
            return false;
        }
        public static void spend(Player player,double nominal,Runnable run){
            if(!(player instanceof ServerPlayer p)||!eligible(p)||!PaymentReceiptPolicy.positive(nominal)||
                MxpSourceConfig.EMBERS_PORTABLE_POOL.get()<=0||!personalCaller()){run.run();return;}
            Scope outer=CURRENT.get();if(outer!=null){outer.valid=false;run.run();return;}
            Scope s=new Scope(p,nominal);CURRENT.set(s);
            try{run.run();if(s.valid&&ObservedPayment.funded(s.paid,s.price))
                credit(p,"embers:personal",s.nominal,MxpSourceConfig.EMBERS_PORTABLE_POOL.get(),s.paid);}
            finally{CURRENT.remove();}
        }
        public static void price(double amount){Scope s=CURRENT.get();if(s!=null)s.price=amount;}
        public static double before(Object cap){return CURRENT.get()==null?Double.NaN:MxpReflect.number(MxpReflect.call(cap,"getEmber"));}
        public static void removed(Object cap,double request,boolean remove,double result,double before){
            Scope s=CURRENT.get();if(s==null||!remove||request<=0)return;
            double after=MxpReflect.number(MxpReflect.call(cap,"getEmber"));
            if(!Double.isFinite(before)||!Double.isFinite(after)||before<0||after<0||!Double.isFinite(result)){s.valid=false;return;}
            if(result>0){
                double paid=ObservedPayment.proven(before,after,result,request);
                if(paid+1e-6<Math.min(result,request)){s.valid=false;return;}
                s.paid+=paid;
            }
        }
    }

    public static final class Goety{
        private static final String SE="com.Polarice3.Goety.utils.SEHelper";
        private static final String TOTEM="com.Polarice3.Goety.utils.TotemFinder";
        private static final String ENERGY="com.Polarice3.Goety.api.items.magic.ITotem";
        private static final ThreadLocal<Scope> CURRENT=new ThreadLocal<>();
        private static final class Scope{
            final ServerPlayer player;double nominal,paid;int expected=-1;boolean valid=true,debitting,finished;Wallet paymentWallet;
            Scope(ServerPlayer p){player=p;}
        }
        private record Wallet(boolean arca,Object identity,double amount){}
        private static Wallet wallet(Player p){
            Object active=MxpReflect.staticCall(SE,"getSEActive",p);
            if(Boolean.TRUE.equals(active))return new Wallet(true,p,MxpReflect.number(MxpReflect.staticCall(SE,"getSESouls",p)));
            if(!Boolean.FALSE.equals(active))return null;
            Object totem=MxpReflect.staticCall(TOTEM,"FindTotem",p);
            if(!(totem instanceof ItemStack stack)||stack.isEmpty())return null;
            return new Wallet(false,stack,MxpReflect.number(MxpReflect.staticCall(ENERGY,"currentSouls",stack)));
        }
        public static void cast(net.minecraft.world.entity.LivingEntity caster,Runnable run){
            if(!(caster instanceof ServerPlayer p)||!MxpPlayerPolicy.eligible(p)){run.run();return;}
            Scope outer=CURRENT.get();if(outer!=null){outer.valid=false;run.run();return;}
            Scope s=new Scope(p);CURRENT.set(s);
            try{
                run.run();
                if(s.valid&&s.finished&&ObservedPayment.funded(s.paid,s.expected)){
                    credit(p,"goety:soul_energy",s.nominal,MxpSourceConfig.GOETY_SOUL_POOL.get(),s.paid);
                    rebate(s);
                }
            }finally{CURRENT.remove();}
        }
        public static Object before(Player p){
            Scope s=CURRENT.get();if(s==null||s.player!=p)return null;
            if(s.debitting||s.paymentWallet!=null){s.valid=false;return null;}
            s.debitting=true;return wallet(p);
        }
        public static void price(Player p,int price){
            Scope s=CURRENT.get();if(s==null||s.player!=p||!s.debitting)return;
            if(s.expected!=-1||price<0)s.valid=false;
            s.expected=price;
        }
        public static void effect(Runnable effect){
            Scope s=CURRENT.get();effect.run();if(s!=null)s.finished=true;
        }
        private static void rebate(Scope s){
            if(s.paymentWallet==null||s.paid<=1)return;
            var p=s.player;var current=wallet(p);
            if(current==null||current.arca!=s.paymentWallet.arca||current.identity!=s.paymentWallet.identity)return;
            int amount=io.github.heatwaveqc.tmm.progression.foundation.LichSkillMath.rebate((int)s.paid,
                io.github.heatwaveqc.tmm.progression.runtime.forms.LichEffects.rate(p,"lich_soul_treasury"));
            if(amount<=0)return;
            // Native capped refill, bypass the earned-gain event. Same wallet only.
            if(current.arca){
                MxpReflect.staticCall(SE,"increaseSESouls",p,amount);
                MxpReflect.staticCall(SE,"sendSEUpdatePacket",p);
            }else MxpReflect.staticCall(ENERGY,"increaseSouls",current.identity,amount);
        }
        public static void removed(Player p,int cost,Object raw){
            Scope s=CURRENT.get();if(s==null||s.player!=p)return;
            s.debitting=false;
            if(cost<=0||!(raw instanceof Wallet before)){s.valid=false;return;}
            Wallet after=wallet(p);
            if(after==null||before.arca!=after.arca||before.identity!=after.identity||s.paid>0){s.valid=false;return;}
            double paid=ObservedPayment.delta(before.amount,after.amount);
            if(paid>cost+1e-6){s.valid=false;return;}
            s.nominal=cost;s.paid=paid;s.paymentWallet=after;
        }
    }
}
