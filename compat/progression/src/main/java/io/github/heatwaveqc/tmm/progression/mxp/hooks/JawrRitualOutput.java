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
    public interface Observer { void tmm$markJawrOutput(); java.util.UUID tmm$jawrActor(); }
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
    private static boolean isCommand(Scope s,E6bRitualCatalog.Kind kind,String template){
        return s!=null&&s.proof().kind()==kind&&s.event().getClass().getName().equals("dev.sterner.witchery.content.ritual_event.RunCommandRitualEvent")
            &&template.equals(MxpReflect.call(s.event(),"getCommand"));
    }
    public static boolean curseTarget(Player target,net.minecraft.resources.ResourceLocation id){
        Scope s=ACTIVE.get();
        return target!=null&&id!=null&&isCommand(s,E6bRitualCatalog.Kind.CURSE,"witchery curse apply {taglockPlayer} "+id+" {witchPower}")
            &&s.proof().output().equals(id.toString())&&target.level() instanceof ServerLevel level&&level.getServer()==s.level().getServer()
            &&target.getUUID().equals(MxpReflect.call(s.altar(),"getTargetPlayer"));
    }
    public static java.util.List<String> curseState(Player target,net.minecraft.resources.ResourceLocation id){
        Object data=MxpReflect.staticCall("dev.sterner.witchery.feature.curse.CursePlayerAttachment","getData",target);
        if(!(MxpReflect.call(data,"getPlayerCurseList") instanceof java.util.List<?> curses))return null;
        var snapshot=new java.util.ArrayList<String>();
        for(Object curse:curses)if(id.equals(MxpReflect.call(curse,"getCurseId"))){
            Object duration=MxpReflect.call(curse,"getDuration"),power=MxpReflect.call(curse,"getWitchPower"),cat=MxpReflect.call(curse,"getCatBoosted");
            if(!(duration instanceof Integer)||!(power instanceof Integer)||!(cat instanceof Boolean))return null;
            snapshot.add(duration+":"+power+":"+cat);
        }
        java.util.Collections.sort(snapshot);return java.util.List.copyOf(snapshot);
    }
    public static void curseChanged(Player target,net.minecraft.resources.ResourceLocation id,java.util.List<String> before,boolean success){
        if(!success||before==null||!curseTarget(target,id))return;
        var after=curseState(target,id);
        if(after!=null&&!after.isEmpty()&&!after.equals(before))ACTIVE.get().altar().tmm$markJawrOutput();
    }
    public static String infusionType(Player target){
        Object data=MxpReflect.staticCall("dev.sterner.witchery.feature.infusion.InfusionPlayerAttachment","getData",target);
        Object type=MxpReflect.call(data,"getType"),name=MxpReflect.call(type,"getSerializedName");
        if(!(name instanceof String n))return null;
        return n.contains(":")?n:"witchery:"+n;
    }
    public static String infusionExpected(Player target){
        Scope s=ACTIVE.get();
        if(s==null||target==null||!target.getUUID().equals(s.altar().tmm$jawrActor())||!(target.level() instanceof ServerLevel level)||level.getServer()!=s.level().getServer())return null;
        String id=s.proof().output();return isCommand(s,E6bRitualCatalog.Kind.INFUSION,"witchery infusion set {owner} "+id)?id:null;
    }
    public static void infusionChanged(Player target,String expected,String before){
        if(expected!=null&&before!=null&&!expected.equals(before)&&expected.equals(infusionExpected(target))&&expected.equals(infusionType(target)))
            ACTIVE.get().altar().tmm$markJawrOutput();
    }
    public static Boolean manifestationBefore(Player target){
        Scope s=ACTIVE.get();
        if(s==null||target==null||!target.getUUID().equals(s.altar().tmm$jawrActor())||!isCommand(s,E6bRitualCatalog.Kind.MANIFESTATION,"witchery manifestation set {owner} true")
            ||!(target.level() instanceof ServerLevel level)||level.getServer()!=s.level().getServer())return null;
        Object data=MxpReflect.staticCall("dev.sterner.witchery.feature.spirit_world.ManifestationPlayerAttachment","getData",target);
        return MxpReflect.call(data,"getHasRiteOfManifestation") instanceof Boolean value?value:null;
    }
    public static void manifestationChanged(Player target,Boolean before){
        if(Boolean.FALSE.equals(before)&&Boolean.TRUE.equals(manifestationBefore(target)))ACTIVE.get().altar().tmm$markJawrOutput();
    }
    public static void removal(Object event,boolean changed){
        Scope s=ACTIVE.get();
        if(changed&&s!=null&&s.event()==event&&s.proof().kind()==E6bRitualCatalog.Kind.REMOVE_CURSE&&s.proof().output().equals(event.getClass().getName()))
            s.altar().tmm$markJawrOutput();
    }
    public static void spawned(Object event,ServerLevel level,Entity entity,boolean accepted){
        Scope s=ACTIVE.get();
        if(!accepted||s==null||s.event()!=event||s.level()!=level||entity==null)return;
        var proof=s.proof();
        if(proof.kind()==E6bRitualCatalog.Kind.OWNER_ITEM&&entity instanceof ItemEntity item){
            ItemStack stack=item.getItem();
            if(stack.isEmpty()||!proof.output().equals(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString())
                ||!"dev.sterner.witchery.content.ritual_event.SummonItemWithOwnerRitualEvent".equals(event.getClass().getName())
                ||!"minecraft:profile".equals(String.valueOf(MxpReflect.call(event,"getOwnerComponentId"))))return;
            Object profile=stack.get(net.minecraft.core.component.DataComponents.PROFILE);
            Object id=MxpReflect.call(profile,"id");
            if(id instanceof java.util.Optional<?> opt)id=opt.orElse(null);
            if(id==null)id=MxpReflect.call(MxpReflect.call(profile,"gameProfile"),"getId");
            if(!java.util.Objects.equals(s.altar().tmm$jawrActor(),id))return;
            ItemStack base=stack.copy();base.remove(net.minecraft.core.component.DataComponents.PROFILE);
            if(MxpReflect.call(event,"getItems") instanceof List<?> offered)for(Object expected:offered)
                if(expected instanceof ItemStack e&&base.getCount()>=e.getCount()&&ItemStack.isSameItemSameComponents(base,e)){s.altar().tmm$markJawrOutput();return;}
        }else if(proof.kind()==E6bRitualCatalog.Kind.ITEM&&entity instanceof ItemEntity item){
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
