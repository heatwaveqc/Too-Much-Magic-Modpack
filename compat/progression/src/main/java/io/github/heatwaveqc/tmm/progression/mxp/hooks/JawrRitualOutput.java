package io.github.heatwaveqc.tmm.progression.mxp.hooks;

import io.github.heatwaveqc.tmm.progression.mxp.core.E6bRitualCatalog;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import java.util.List;

/** Exact synchronous JAWR event dispatch: only accepted native spawn of expected output counts. */
public final class JawrRitualOutput {
    private JawrRitualOutput(){}
    public interface Observer { void tmm$markJawrOutput(); }
    private record Scope(Observer altar,Object event,ServerLevel level,E6bRitualCatalog.JawrOutput proof){}
    private static final ThreadLocal<Scope> ACTIVE=new ThreadLocal<>();
    public static void tick(Observer altar,Object event,ServerLevel level,E6bRitualCatalog.JawrOutput proof,Runnable action){
        Scope outer=ACTIVE.get();
        if(outer!=null){ACTIVE.remove();try{action.run();}finally{ACTIVE.set(outer);}return;}
        if(altar==null||event==null||level==null||proof==null){action.run();return;}
        Scope scope=new Scope(altar,event,level,proof);
        long time=level.getDayTime();boolean rain=level.getLevelData().isRaining(),thunder=level.getLevelData().isThundering();
        ACTIVE.set(scope);
        try{
            action.run();
            if(ACTIVE.get()==scope&&proof.output().equals(event.getClass().getName())&&Boolean.TRUE.equals(MxpReflect.call(event,"isFinished"))){
                if(proof.kind()==E6bRitualCatalog.Kind.TIME&&level.getDayTime()!=time)altar.tmm$markJawrOutput();
                if(proof.kind()==E6bRitualCatalog.Kind.WEATHER&&(rain||thunder)&&!level.getLevelData().isRaining()&&!level.getLevelData().isThundering())altar.tmm$markJawrOutput();
            }
        }finally{ACTIVE.remove();}
    }
    /** Only two exact pinned vanilla lightning commands; not general command credit. */
    public static boolean wantsLightningCommand(ServerLevel level,String resolved){
        Scope s=ACTIVE.get();
        return s!=null&&s.level()==level&&s.proof().kind()==E6bRitualCatalog.Kind.COMMAND
            &&s.event().getClass().getName().equals("dev.sterner.witchery.content.ritual_event.RunCommandRitualEvent")
            &&s.proof().output().equals(MxpReflect.call(s.event(),"getCommand"))
            &&resolved!=null&&resolved.startsWith("summon lightning_bolt ");
    }
    public static void lightningCompleted(ServerLevel level,String resolved){
        if(wantsLightningCommand(level,resolved))ACTIVE.get().altar().tmm$markJawrOutput();
    }
    public static void spawned(Object event,ServerLevel level,Entity entity,boolean accepted){
        Scope s=ACTIVE.get();
        if(!accepted||s==null||s.event()!=event||s.level()!=level||entity==null)return;
        var proof=s.proof();
        if(proof.kind()==E6bRitualCatalog.Kind.ITEM&&entity instanceof ItemEntity item){
            ItemStack stack=item.getItem();
            if(stack.isEmpty()||!proof.output().equals(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString()))return;
            if(!(MxpReflect.call(event,"getItems") instanceof List<?> offered))return;
            for(Object expected:offered)if(expected instanceof ItemStack e&&stack.getCount()>=e.getCount()&&ItemStack.isSameItemSameComponents(stack,e)){
                s.altar().tmm$markJawrOutput();return;
            }
        }else if(proof.kind()==E6bRitualCatalog.Kind.ENTITY&&entity instanceof LivingEntity&&!(entity instanceof Player)
            &&proof.output().equals(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()))s.altar().tmm$markJawrOutput();
    }
}
