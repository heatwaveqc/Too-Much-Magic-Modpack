package io.github.heatwaveqc.tmm.progression.runtime.forms;

import io.github.heatwaveqc.tmm.progression.foundation.Comparison;
import io.github.heatwaveqc.tmm.progression.foundation.Condition;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpReflect;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;
import java.util.Set;
import static io.github.heatwaveqc.tmm.progression.foundation.Condition.Result.*;

/** Live read-only native conditions, never an additional form or physiology owner. */
public final class NativeFormConditions {
    private static final String JAWR="dev.sterner.witchery.feature.";
    private static final String FM="dev.sterner.witchery_forbidden_magic.feature.affliction.lich.LichPlayerAttachment";
    private NativeFormConditions(){}
    private static boolean pin(String mod,String version){
        return ModList.get().getModContainerById(mod).filter(c->c.getModInfo().getVersion().toString().startsWith(version)).isPresent();
    }
    private static boolean server(ServerPlayer p){return p.getServer()!=null&&p.getServer().isSameThread();}
    public static Condition.Result jawrShape(ServerPlayer p,String shape){
        if(!server(p)||!pin("witchery","0.5.39"))return UNAVAILABLE;
        Object data=MxpReflect.staticCall(JAWR+"transformation.TransformationPlayerAttachment","getData",p);
        Object value=MxpReflect.call(data,"getTransformationType");
        if(!(value instanceof Enum<?> form)||!Set.of("NONE","BAT","WOLF","WEREWOLF").contains(form.name()))return UNAVAILABLE;
        if(!shape.equals(form.name()))return FAIL;
        if(shape.equals("BAT")&&!JawrFormState.present(p,"vampire"))return FAIL;
        if((shape.equals("WOLF")||shape.equals("WEREWOLF"))&&!JawrFormState.present(p,"werewolf"))return FAIL;
        return PASS;
    }
    public static Condition<ServerPlayer> shape(String name){
        if(!Set.of("NONE","BAT","WOLF","WEREWOLF").contains(name))throw new IllegalArgumentException("Unknown JAWR active shape");
        return p->jawrShape(p,name);
    }
    public static Condition<ServerPlayer> level(String form,Comparison comparison,int level){
        if(!Set.of("jawr_vampire","jawr_werewolf","jawr_lich").contains(form)||level<0||level>100)
            throw new IllegalArgumentException("Unknown native form level or threshold");
        return p->{
            if(!server(p))return UNAVAILABLE;
            String cls;
            if(form.equals("jawr_lich")){
                if(!pin("witchery_forbidden_magic","0.5.37"))return UNAVAILABLE;
                cls=FM;
            }else{
                if(!pin("witchery","0.5.39"))return UNAVAILABLE;
                cls=JAWR+"affliction."+(form.equals("jawr_vampire")?"vampire.VampirePlayerAttachment":"werewolf.WerewolfPlayerAttachment");
            }
            Object data=MxpReflect.staticCall(cls,"getData",p);
            Object value=MxpReflect.call(data,"getLevel");
            return value instanceof Integer n&&n>=0?(comparison.test(n,level)?PASS:FAIL):UNAVAILABLE;
        };
    }
    public static Condition<ServerPlayer> blood(Comparison comparison,double threshold){
        if(!Double.isFinite(threshold)||threshold<0||threshold>1)throw new IllegalArgumentException("Blood fraction must be 0..1");
        return p->{
            if(!server(p)||!pin("witchery","0.5.39"))return UNAVAILABLE;
            if(!JawrFormState.present(p,"vampire"))return FAIL;
            Object data=MxpReflect.staticCall(JAWR+"blood_pool.BloodPoolAttachment","getData",p);
            Object current=MxpReflect.call(data,"getBloodPool"),max=MxpReflect.call(data,"getMaxBlood");
            if(!(current instanceof Integer c)||!(max instanceof Integer m)||m<=0||c<0||c>m)return UNAVAILABLE;
            return comparison.test((double)c/m,threshold)?PASS:FAIL;
        };
    }
    public static Condition<ServerPlayer> growth(Comparison comparison,double threshold){
        if(!Double.isFinite(threshold)||threshold<0||threshold>1000000)throw new IllegalArgumentException("Invalid native growth threshold");
        return p->{
            var dragon=ProviderFlags.nativeDragon(p);
            if(dragon!=PASS)return dragon;
            Object data=MxpReflect.staticCall("by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider","getData",p);
            Object value=MxpReflect.call(data,"getGrowth");
            if(!(value instanceof Number n)||!Double.isFinite(n.doubleValue())||n.doubleValue()<0)return UNAVAILABLE;
            return comparison.test(n.doubleValue(),threshold)?PASS:FAIL;
        };
    }
}
