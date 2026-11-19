package io.github.heatwaveqc.tmm.progression.runtime.forms;

import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import io.github.heatwaveqc.tmm.progression.foundation.BloodCapacityPolicy;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpReflect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModList;

/** Single native max/current attachment. Persistent ownership marker is not a second reservoir. */
public final class JawrBloodCapacity {
    private static final String KEY="tmm_progression:jawr_capacity";
    private static final String BLOOD="dev.sterner.witchery.feature.blood_pool.BloodPoolAttachment";
    private static final String VAMP="dev.sterner.witchery.feature.affliction.vampire.";
    private JawrBloodCapacity(){}
    public static void tick(ServerPlayer p){
        if(p.tickCount%20!=0||!p.isAlive()||p.getServer()==null||!p.getServer().isSameThread()||
           !ModList.get().getModContainerById("witchery").filter(c->c.getModInfo().getVersion().toString().startsWith("0.5.39")).isPresent())return;
        String marker=ProgressionApi.mode(p,KEY,"0/0");
        boolean vampire=JawrFormState.present(p,"vampire");
        if(!vampire){
            // Native cure owns the blood reset; never write a human blood pool.
            if(!marker.equals("0/0"))ProgressionApi.setMode(p,KEY,"0/0");
            return;
        }
        double rate=VampireEffects.rate(p,"jawr_blood_capacity");
        if(rate<=0&&marker.equals("0/0"))return;
        try{
            int oldBase=0,oldMax=0;
            String[] parts=marker.split("/",-1);
            if(parts.length==2){oldBase=Integer.parseInt(parts[0]);oldMax=Integer.parseInt(parts[1]);}
            Object v=MxpReflect.staticCall(VAMP+"VampirePlayerAttachment","getData",p);
            if(!(MxpReflect.call(v,"getLevel") instanceof Integer level)||level<=0)return;
            Class<?> leveling=Class.forName(VAMP+"VampireLeveling");
            var method=leveling.getDeclaredMethod("bloodForLevel",int.class);
            if(!method.trySetAccessible())return;
            Object owner=java.lang.reflect.Modifier.isStatic(method.getModifiers())?null:MxpReflect.field(leveling,"INSTANCE");
            if(owner==null&&!java.lang.reflect.Modifier.isStatic(method.getModifiers()))return;
            if(!(method.invoke(owner,level) instanceof Integer nativeMaximum)||nativeMaximum<=0)return;
            Class<?> attachment=Class.forName(BLOOD);
            Object data=MxpReflect.staticCall(BLOOD,"getData",p);
            if(data==null||!(MxpReflect.call(data,"getMaxBlood") instanceof Integer maximum)||
                !(MxpReflect.call(data,"getBloodPool") instanceof Integer current))return;
            var plan=BloodCapacityPolicy.reconcile(nativeMaximum,maximum,current,oldBase,oldMax,rate);
            if(plan==null)return;
            String next=plan.owned()?nativeMaximum+"/"+plan.maximum():"0/0";
            if(plan.maximum()==maximum&&plan.current()==current){
                if(!next.equals(marker))ProgressionApi.setMode(p,KEY,next);
                return;
            }
            Object newData=data.getClass().getMethod("copy",int.class,int.class).invoke(data,plan.maximum(),plan.current());
            // Verify constructor/order before committing to the authoritative native writer.
            if(!Integer.valueOf(plan.maximum()).equals(MxpReflect.call(newData,"getMaxBlood"))||
               !Integer.valueOf(plan.current()).equals(MxpReflect.call(newData,"getBloodPool")))return;
            attachment.getMethod("setData",LivingEntity.class,data.getClass()).invoke(null,p,newData);
            Object seen=MxpReflect.staticCall(BLOOD,"getData",p);
            if(Integer.valueOf(plan.maximum()).equals(MxpReflect.call(seen,"getMaxBlood"))&&
                Integer.valueOf(plan.current()).equals(MxpReflect.call(seen,"getBloodPool"))&&!next.equals(marker))
                ProgressionApi.setMode(p,KEY,next);
        }catch(ReflectiveOperationException|RuntimeException|LinkageError ignored){
            // No guessed base or fallback second pool. Exact installed lifecycle acceptance required.
        }
    }
}
