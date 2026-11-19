package io.github.heatwaveqc.tmm.progression.mxp.hooks;

import io.github.heatwaveqc.tmm.progression.mxp.MxpSourceConfig;
import io.github.heatwaveqc.tmm.progression.mxp.core.InkPaymentReceipt;
import io.github.heatwaveqc.tmm.progression.mxp.core.PastelInkValuation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.LongSupplier;

public final class PastelInkPayments {
    private static final ThreadLocal<Scope> ACTIVE=new ThreadLocal<>();
    private PastelInkPayments(){}
    private record Scope(ServerPlayer player,Object color,double pool,InkPaymentReceipt receipt){}
    private static double reference(Object color){
        Object rawId=MxpReflect.call(color,"getID");
        Object rawDye=MxpReflect.call(color,"getDyeColor");
        if(!(rawId instanceof ResourceLocation id)||!id.getNamespace().equals("pastel")||
            !(rawDye instanceof Optional<?> optional)||!(optional.orElse(null) instanceof DyeColor dye)||
            !id.getPath().equals(dye.getName()))return 0;
        var cfg=MxpSourceConfig.PASTEL_DYE_EMC.get(id.getPath());
        return cfg==null?0:PastelInkValuation.reference(id.getPath(),cfg.get());
    }
    private static long balance(ItemStack stack,Object color){
        if(stack==null||stack.isEmpty())return -1;
        Object storage=MxpReflect.call(stack.getItem(),"getEnergyStorage",stack);
        Object energy=MxpReflect.call(storage,"getEnergy",color);
        return energy instanceof Long value&&value>=0?value:-1;
    }
    public static boolean pay(Player player,Object color,long price,BooleanSupplier nativeCall){
        Scope outer=ACTIVE.get();
        if(outer!=null){outer.receipt.invalidate();return nativeCall.getAsBoolean();}
        if(!(player instanceof ServerPlayer p)||!NativePaidSources.eligible(p)||price<=0||
            price>9_007_199_254_740_991L)return nativeCall.getAsBoolean();
        double pool=reference(color);
        if(pool<=0)return nativeCall.getAsBoolean();
        Scope scope=new Scope(p,color,pool,new InkPaymentReceipt(price));ACTIVE.set(scope);
        try{
            boolean success=nativeCall.getAsBoolean();
            if(scope.receipt.complete(success))
                NativePaidSources.credit(p,"pastel:ink",price,pool,scope.receipt.paid());
            return success;
        }finally{ACTIVE.remove();}
    }
    public static long drain(ItemStack stack,Object color,long requested,Player player,LongSupplier nativeCall){
        Scope scope=ACTIVE.get();
        if(scope==null)return nativeCall.getAsLong();
        if(scope.player!=player||scope.color!=color){scope.receipt.invalidate();return nativeCall.getAsLong();}
        long before=balance(stack,color);
        long result=nativeCall.getAsLong();
        long after=balance(stack,color);
        scope.receipt.observe(requested,result,before,after);
        return result;
    }
}
