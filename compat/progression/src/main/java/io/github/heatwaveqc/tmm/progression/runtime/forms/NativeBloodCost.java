package io.github.heatwaveqc.tmm.progression.runtime.forms;

import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpPlayerPolicy;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpReflect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModList;

/** Use the existing JAWR debit method; never create, mirror, directly assign or refill blood. */
public final class NativeBloodCost {
    private static final String ROOT="dev.sterner.witchery.feature.blood_pool.";
    private NativeBloodCost(){}
    private record Balance(int current,int max){}
    private static Balance read(ServerPlayer p){
        try{
            if(!MxpPlayerPolicy.eligible(p)||!ModList.get().getModContainerById("witchery")
                    .filter(c->c.getModInfo().getVersion().toString().equals("0.5.39")).isPresent()
                ||!JawrFormState.present(p,"vampire"))return null;
            Object d=MxpReflect.staticCall(ROOT+"BloodPoolAttachment","getData",p);
            Object c=MxpReflect.call(d,"getBloodPool"),m=MxpReflect.call(d,"getMaxBlood");
            return c instanceof Integer current&&m instanceof Integer max&&max>0&&current>=0&&current<=max?
                new Balance(current,max):null;
        }catch(RuntimeException|LinkageError e){return null;}
    }
    public static boolean affordable(ServerPlayer p,int price){
        Balance b=read(p);return price>0&&b!=null&&b.current>=price;
    }
    public static boolean pay(ServerPlayer p,int price){
        Balance before=read(p);
        if(price<=0||before==null||before.current<price)return false;
        try{
            var nativeDebit=Class.forName(ROOT+"BloodPoolHandler")
                .getMethod("decreaseBlood",LivingEntity.class,int.class);
            nativeDebit.invoke(null,p,price);
            Balance after=read(p);
            return after!=null&&before.max==after.max&&before.current-after.current==price;
        }catch(ReflectiveOperationException|RuntimeException|LinkageError e){return false;}
    }
}
