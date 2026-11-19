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
 * No Psi code or assets redistributed. Loopcasting, crafting and health-paid overflow are excluded.
 */
public final class PsiCastHooks {
    private PsiCastHooks(){}
    private record Pending(ServerPlayer player,Object context,Object data,ItemStack cad,double nominal,int price,String tx,long tick,double debit){}
    private static final ThreadLocal<Pending> ACTIVE=new ThreadLocal<>();
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
        if(!(rawCad instanceof ItemStack stack)||ctx==null||data==null)return quoted;
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
        var p=ACTIVE.get();if(p==null||p.data!=data||p.price!=debit||p.debit>0)return null;
        return new Before(personal(data),cad(p.cad));
    }
    public static void after(Object data,int debit,Before before){
        var p=ACTIVE.get();if(p==null||before==null||p.data!=data||p.price!=debit)return;
        double measured=CommittedDebit.measured(before.personal,personal(data),before.reserve,cad(p.cad),debit);
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
    public static void clear(){ACTIVE.remove();}
}
