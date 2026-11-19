package io.github.heatwaveqc.tmm.progression.mxp.hooks;

import io.github.heatwaveqc.tmm.progression.mxp.MxpMod;
import io.github.heatwaveqc.tmm.progression.mxp.MxpService;
import io.github.heatwaveqc.tmm.progression.mxp.MxpSourceConfig;
import io.github.heatwaveqc.tmm.progression.mxp.core.CommittedDebit;
import io.github.heatwaveqc.tmm.progression.mxp.core.PaymentReceiptPolicy;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import java.util.UUID;

/** Independent compatibility adapter. Psi 110 ItemCAD.cast reads a posted PreSpellCastEvent,
 * calls PlayerData.deductPsi, executes the accepted cast, then constructs SpellCastEvent.
 * Observe both actual debit legs only at the deduct call, deliver once on matching completion.
 * Standard and native loopcast pulses share the payment contract. Crafting and health-paid overflow are excluded.
 */
public final class PsiCastHooks {
    private PsiCastHooks(){}
    private record Pending(ServerPlayer player,Object context,Object data,ItemStack cad,double nominal,int price,String tx,long tick,double debit){}
    private static final ThreadLocal<Pending> ACTIVE=new ThreadLocal<>();
    private record Loop(ServerPlayer player,Object data,ItemStack cad,ItemStack bullet,double nominal,int price,
                        long tick,String tx,double debit){}
    private static final ThreadLocal<Loop> LOOP=new ThreadLocal<>();
    private static boolean supported(){
        return MxpMod.active()&&ModList.get().getModContainerById("psi")
            .map(c->{String v=c.getModInfo().getVersion().toString();return v.equals("110")||v.endsWith("-110")||v.startsWith("110+");}).orElse(false);
    }
    private static double cad(ItemStack stack){
        if(stack==null||stack.isEmpty())return Double.NaN;
        return MxpReflect.number(MxpReflect.call(stack.getItem(),"getStoredPsi",stack));
    }
    private static double personal(Object data){return MxpReflect.number(MxpReflect.call(data,"getAvailablePsi"));}
    public static int price(Object event,int quoted){
        ACTIVE.remove();
        if(!supported()||quoted<=0)return quoted;
        Object raw=MxpReflect.call(event,"getPlayer");
        if(!(raw instanceof ServerPlayer player)||!MxpPlayerPolicy.eligible(player))return quoted;
        Object ctx=MxpReflect.call(event,"getContext"),data=MxpReflect.call(event,"getPlayerData");
        Object rawCad=MxpReflect.call(event,"getCad");
        if(!(rawCad instanceof ItemStack stack)||ctx==null||data==null||MxpReflect.call(data,"getCAD")!=stack)return quoted;
        double reserve=cad(stack);
        if(!Double.isFinite(reserve)||reserve<0)return quoted;
        Object stat;
        try{
            @SuppressWarnings({"rawtypes","unchecked"}) Class<? extends Enum> stats=(Class<? extends Enum>)Class.forName("vazkii.psi.api.spell.EnumSpellStat");
            @SuppressWarnings("unchecked") Object cost=Enum.valueOf(stats,"COST");stat=cost;
        }catch(ReflectiveOperationException|LinkageError|RuntimeException absent){return quoted;}
        double nominal=MxpReflect.number(MxpReflect.call(MxpReflect.field(MxpReflect.field(ctx,"cspell"),"metadata"),"getStat",stat));
        if(!Double.isFinite(nominal)||nominal<=0)return quoted;
        int price=MagicCostPolicy.quote(player,"psi:cad_cast",quoted);
        ACTIVE.set(new Pending(player,ctx,data,stack,nominal,price,"psi:"+UUID.randomUUID(),player.server.overworld().getGameTime(),0));
        return price;
    }
    public record Before(double personal,double reserve){}
    public static Before before(Object data,int debit){
        var p=ACTIVE.get();if(p==null||p.data!=data||p.price!=debit||p.debit>0||MxpReflect.call(data,"getCAD")!=p.cad)return null;
        return new Before(personal(data),cad(p.cad));
    }
    public static void after(Object data,int debit,Before before){
        var p=ACTIVE.get();if(p==null||before==null||p.data!=data||p.price!=debit)return;
        double measured=MxpReflect.call(data,"getCAD")==p.cad&&!Boolean.TRUE.equals(MxpReflect.call(data,"isOverflowed"))?
            CommittedDebit.measured(before.personal,personal(data),before.reserve,cad(p.cad),debit):0;
        ACTIVE.set(new Pending(p.player,p.context,p.data,p.cad,p.nominal,p.price,p.tx,p.tick,measured));
    }
    public static void complete(Object event){
        var p=ACTIVE.get();if(p==null)return;
        if(MxpReflect.field(event,"context")!=p.context||MxpReflect.field(event,"player")!=p.player||
            MxpReflect.field(event,"playerData")!=p.data||MxpReflect.field(event,"cad")!=p.cad)return;
        ACTIVE.remove();
        if(!supported()||!MxpPlayerPolicy.eligible(p.player)||p.tick!=p.player.server.overworld().getGameTime()||!PaymentReceiptPolicy.fullyFunded(p.debit,p.price))return;
        double pool=MxpSourceConfig.PSI_INITIAL_POOL.get();
        if(pool>0)MxpService.resource(p.player,"psi:cad_cast",p.tx,p.nominal,pool,p.debit);
    }
    /** Only invoked inside the native tick's realCost call, not ordinary CAD previews. */
    public static int loopPrice(Object data,ItemStack stack,ItemStack bullet,int nominal,int quote) {
        LOOP.remove();
        if(!supported()||nominal<=0||quote<=0||stack.isEmpty()||bullet.isEmpty()||
            MxpReflect.call(data,"getCAD")!=stack) return quote;
        Object wr=MxpReflect.field(data,"playerWR");
        if(!(wr instanceof java.lang.ref.Reference<?> reference)||
            !(reference.get() instanceof ServerPlayer p)||!MxpPlayerPolicy.eligible(p))return quote;
        double reserve=cad(stack);
        if(!Double.isFinite(reserve)||reserve<0)return quote;
        int price=MagicCostPolicy.quote(p,"psi:cad_cast",quote);
        LOOP.set(new Loop(p,data,stack,bullet,nominal,price,p.server.overworld().getGameTime(),
                "psi:loop:"+UUID.randomUUID(),0));
        return price;
    }
    public static Before beforeLoop(Object data,int price) {
        Loop p=LOOP.get();
        if(p==null||p.data!=data||p.price!=price||p.debit>0||MxpReflect.call(data,"getCAD")!=p.cad)return null;
        return new Before(personal(data),cad(p.cad));
    }
    public static void afterLoop(Object data,int price,Before before) {
        Loop p=LOOP.get();
        if(p==null||before==null||p.data!=data||p.price!=price)return;
        double measured=MxpReflect.call(data,"getCAD")==p.cad&&!Boolean.TRUE.equals(MxpReflect.call(data,"isOverflowed"))?
            CommittedDebit.measured(before.personal,personal(data),before.reserve,cad(p.cad),price):0;
        LOOP.set(new Loop(p.player,p.data,p.cad,p.bullet,p.nominal,p.price,p.tick,p.tx,measured));
    }
    public static void loopReturned(Object data,Object ctx,boolean result) {
        Loop p=LOOP.get();LOOP.remove();
        if(!result||p==null||p.data!=data||MxpReflect.field(ctx,"caster")!=p.player||
            !(MxpReflect.number(MxpReflect.field(ctx,"loopcastIndex"))>0)||
            !supported()||!MxpPlayerPolicy.eligible(p.player)||p.tick!=p.player.server.overworld().getGameTime()||
            !PaymentReceiptPolicy.fullyFunded(p.debit,p.price))return;
        double pool=MxpSourceConfig.PSI_INITIAL_POOL.get();
        if(pool>0){
            try{MxpService.resource(p.player,"psi:cad_cast",p.tx,p.nominal,pool,p.debit);}
            catch(RuntimeException failure){MxpMod.LOGGER.warn("Psi loopcast MXP receipt rejected: {}",failure.toString());}
        }
    }
    public static void clearLoop(){LOOP.remove();}
    public static void clear(){ACTIVE.remove();LOOP.remove();}
}
