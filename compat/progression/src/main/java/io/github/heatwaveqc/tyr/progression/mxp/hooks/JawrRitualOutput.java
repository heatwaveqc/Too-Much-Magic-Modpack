package io.github.heatwaveqc.tyr.progression.mxp.hooks;

import io.github.heatwaveqc.tyr.progression.mxp.core.E6bRitualCatalog;
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
    private static final class Outputs{
        final java.util.IdentityHashMap<Entity,ItemStack> stacks=new java.util.IdentityHashMap<>();
        final java.util.Set<java.util.UUID> accepted=new java.util.HashSet<>();
        final java.util.Map<java.util.UUID,int[]> mirrors=new java.util.HashMap<>();
    }
    private record Scope(Observer altar,Object event,ServerLevel level,E6bRitualCatalog.JawrOutput proof,Outputs outputs){}
    private static final ThreadLocal<Scope> ACTIVE=new ThreadLocal<>();
    public static void tick(Observer altar,Object event,ServerLevel level,E6bRitualCatalog.JawrOutput proof,Runnable action){
        Scope outer=ACTIVE.get();
        if(outer!=null){ACTIVE.remove();try{action.run();}finally{ACTIVE.set(outer);}return;}
        if(altar==null||event==null||level==null||proof==null){action.run();return;}
        Scope scope=new Scope(altar,event,level,proof,new Outputs());
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
    public static boolean eventMatches(Object event,ServerLevel level){
        Scope s=ACTIVE.get();
        return s!=null&&s.event()==event&&s.level()==level&&(s.proof().kind()==E6bRitualCatalog.Kind.EVENT_MUTATION||s.proof().kind()==E6bRitualCatalog.Kind.EVENT_SPAWN)
            &&s.proof().output().equals(event.getClass().getName());
    }
    public static void changed(Object event,ServerLevel level,boolean changed){
        if(changed&&eventMatches(event,level))ACTIVE.get().altar().tmm$markJawrOutput();
    }
    public static boolean oreMatches(Object event,ServerLevel level,net.minecraft.world.level.block.state.BlockState before){
        Scope s=ACTIVE.get();
        if(s==null||s.event()!=event||s.level()!=level||s.proof().kind()!=E6bRitualCatalog.Kind.MINE
            ||!event.getClass().getName().equals("dev.sterner.witchery.content.ritual_event.MineBlocksBelowRitualEvent"))return false;
        String block=BuiltInRegistries.BLOCK.getKey(before.getBlock()).toString();
        return switch(s.proof().output()){
            case "iron" -> java.util.Set.of("minecraft:iron_ore","minecraft:deepslate_iron_ore").contains(block);
            case "gold" -> java.util.Set.of("minecraft:gold_ore","minecraft:deepslate_gold_ore").contains(block);
            case "copper" -> java.util.Set.of("minecraft:copper_ore","minecraft:deepslate_copper_ore").contains(block);
            case "zinc" -> java.util.Set.of("create:zinc_ore","create:deepslate_zinc_ore").contains(block);
            case "amber" -> block.equals("thaumaturge:ore_amber");
            case "cinnabar" -> block.equals("thaumaturge:ore_cinnabar");
            default -> false;
        };
    }
    public static void mined(Object event,ServerLevel level,net.minecraft.world.level.block.state.BlockState before,boolean changed){
        if(changed&&oreMatches(event,level,before))ACTIVE.get().altar().tmm$markJawrOutput();
    }
    public static boolean familiarOwner(Object event,ServerLevel level,java.util.UUID owner){
        return eventMatches(event,level)&&owner!=null&&owner.equals(ACTIVE.get().altar().tmm$jawrActor());
    }
    public static record TeleportWitness(Entity entity,ServerLevel beforeLevel,net.minecraft.world.phys.Vec3 before,net.minecraft.core.GlobalPos dest){}
    public static TeleportWitness teleportBefore(ServerLevel level,String resolved){
        Scope s=ACTIVE.get();
        if(s==null||s.level()!=level||s.proof().kind()!=E6bRitualCatalog.Kind.TELEPORT||resolved==null||!resolved.startsWith("tp ")
            ||!isCommand(s,E6bRitualCatalog.Kind.TELEPORT,s.proof().output()))return null;
        Object target=MxpReflect.call(s.altar(),"getTargetPos");
        if(!(target instanceof net.minecraft.core.GlobalPos dest))return null;
        Entity entity=null;
        if(s.proof().output().equals("tp {owner} {waystonePos}"))entity=level.getServer().getPlayerList().getPlayer(s.altar().tmm$jawrActor());
        else if(MxpReflect.call(s.altar(),"getTargetPlayer") instanceof java.util.UUID uuid)entity=level.getServer().getPlayerList().getPlayer(uuid);
        else if(MxpReflect.call(s.altar(),"getTargetEntity") instanceof Integer id)entity=level.getEntity(id);
        return entity!=null&&!entity.isRemoved()&&entity.level() instanceof ServerLevel prior?new TeleportWitness(entity,prior,entity.position(),dest):null;
    }
    public static void teleportAfter(TeleportWitness witness,boolean success){
        Scope s=ACTIVE.get();
        if(!success||witness==null||s==null||s.proof().kind()!=E6bRitualCatalog.Kind.TELEPORT)return;
        Entity e=witness.entity();
        if(!e.isRemoved()&&e.level().dimension().equals(witness.dest().dimension())&&
            (e.level()!=witness.beforeLevel()||e.position().distanceToSqr(witness.before())>1.0e-6)
            &&e.position().distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(witness.dest().pos()))<=4.0)
            s.altar().tmm$markJawrOutput();
    }
    public static net.minecraft.resources.ResourceKey<?> biomeExpected(ServerLevel level){
        Scope s=ACTIVE.get();
        if(s==null||s.level()!=level||!isCommand(s,E6bRitualCatalog.Kind.BIOME,s.proof().output()))return null;
        return MxpReflect.call(s.altar(),"getTargetBiome") instanceof net.minecraft.resources.ResourceKey<?> key?key:null;
    }
    public static void biomeChanged(ServerLevel level,net.minecraft.resources.ResourceKey<?> target,boolean changed){
        if(changed&&target!=null&&target.equals(biomeExpected(level)))ACTIVE.get().altar().tmm$markJawrOutput();
    }
    public static Integer wolfBefore(net.minecraft.server.level.ServerPlayer target){
        Scope s=ACTIVE.get();
        if(s==null||target==null||!isCommand(s,E6bRitualCatalog.Kind.WOLF_CURSE,s.proof().output())||
            !target.getUUID().equals(MxpReflect.call(s.altar(),"getTargetPlayer"))||
            target.getServer()!=s.level().getServer())return null;
        Object data=MxpReflect.staticCall("dev.sterner.witchery.feature.affliction.werewolf.WerewolfPlayerAttachment","getData",target);
        return MxpReflect.call(data,"getLevel") instanceof Integer n?n:null;
    }
    public static void wolfAfter(net.minecraft.server.level.ServerPlayer target,Integer before){
        Integer after=before==null?null:wolfBefore(target);
        if(after!=null&&after>before&&after>0)ACTIVE.get().altar().tmm$markJawrOutput();
    }
    /** Actual native floating item initialization. Never inspect unrelated dropped items. */
    public static void initializedItem(Object event,Object holder,ItemStack offered){
        Scope s=ACTIVE.get();
        if(s==null||s.event()!=event||!(holder instanceof Entity e)||offered==null||offered.isEmpty()
            ||!e.getClass().getName().equals("dev.sterner.witchery.content.entity.FloatingItemEntity"))return;
        s.outputs().stacks.put(e,offered.copy());
    }
    private static ItemStack actualStack(Scope s,Entity entity){
        ItemStack initialized=s.outputs().stacks.remove(entity);
        if(initialized!=null){
            Object nativeValue=MxpReflect.call(entity,"getItem");
            if(nativeValue instanceof ItemStack actual&&(!ItemStack.isSameItemSameComponents(actual,initialized)||actual.getCount()!=initialized.getCount()))return ItemStack.EMPTY;
            return initialized;
        }
        return entity instanceof ItemEntity item?item.getItem():ItemStack.EMPTY;
    }
    private static java.util.UUID pairId(ItemStack stack){
        try{
            Object companion=MxpReflect.field(Class.forName("dev.sterner.witchery.content.item.MirrorItem"),"Companion");
            return MxpReflect.call(companion,"getPairId",stack) instanceof java.util.UUID id?id:null;
        }catch(ClassNotFoundException|LinkageError ignored){return null;}
    }
    private static boolean contractOwner(ItemStack stack,java.util.UUID actor){
        try{
            Object registry=MxpReflect.field(Class.forName("dev.sterner.witchery.core.registry.WitcheryDataComponents"),"INSTANCE");
            Object holder=MxpReflect.call(registry,"getPLAYER_UUID_ORDERED_LIST"),raw=MxpReflect.call(holder,"get");
            if(!(raw instanceof net.minecraft.core.component.DataComponentType<?> component)||!(stack.get(component) instanceof List<?> entries))return false;
            return entries.stream().anyMatch(pair->actor.equals(MxpReflect.call(pair,"getFirst")));
        }catch(ClassNotFoundException|LinkageError ignored){return false;}
    }
    private static void mirrorAccepted(Scope scope,Entity entity,ItemStack stack){
        if(!scope.proof().output().equals(scope.event().getClass().getName())||stack.isEmpty()||!scope.outputs().accepted.add(entity.getUUID()))return;
        String id=BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
        if(!id.equals("witchery:mirror")&&!id.equals("witchery:demonic_contract"))return;
        var pair=pairId(stack);if(pair==null)return;
        int[] counts=scope.outputs().mirrors.computeIfAbsent(pair,k->new int[2]);
        if(id.equals("witchery:mirror"))counts[0]=Math.min(2,counts[0]+stack.getCount());
        else if(contractOwner(stack,scope.altar().tmm$jawrActor()))counts[1]=1;
        if(counts[0]>=2&&counts[1]==1)scope.altar().tmm$markJawrOutput();
    }
    /** Only the exact nested spectral binding child of the active native parallel event. */
    public static void child(Object parent,Object event,Object context,Runnable original){
        Scope s=ACTIVE.get();
        if(s==null||s.event()!=parent||s.proof().kind()!=E6bRitualCatalog.Kind.SPECTRAL||
            event==null||!s.proof().output().equals(event.getClass().getName())||
            !(MxpReflect.call(parent,"getEvents") instanceof List<?> children)||children.stream().noneMatch(e->e==event)||
            MxpReflect.call(context,"getEntity")!=s.altar()||MxpReflect.call(context,"getLevel")!=s.level()){
            original.run();return;
        }
        ACTIVE.set(new Scope(s.altar(),event,s.level(),s.proof(),s.outputs()));
        try{original.run();}finally{ACTIVE.set(s);}
    }
    public static void spectralChain(net.minecraft.world.level.Level world,Entity chain,boolean accepted){
        Scope s=ACTIVE.get();
        if(!accepted||s==null||s.level()!=world||s.proof().kind()!=E6bRitualCatalog.Kind.SPECTRAL||
            !s.proof().output().equals(s.event().getClass().getName())||chain==null||
            !chain.getClass().getName().equals("dev.sterner.witchery.content.entity.ChainEntity")||
            s.level().getEntity(chain.getUUID())!=chain)return;
        Object target=MxpReflect.call(chain,"getTargetEntity");
        Object raw=MxpReflect.field(s.event(),"entityToEffigyMap");
        if(!(target instanceof LivingEntity living)||living instanceof Player||!(raw instanceof java.util.Map<?,?> map)||
            !(map.get(living.getUUID()) instanceof net.minecraft.core.BlockPos pos))return;
        var effigy=s.level().getBlockEntity(pos);
        if(effigy!=null&&effigy.getClass().getName().equals("dev.sterner.witchery.content.block.effigy.EffigyBlockEntity"))s.altar().tmm$markJawrOutput();
    }
    public static void spawned(Object event,ServerLevel level,Entity entity,boolean accepted){
        Scope s=ACTIVE.get();
        if(!accepted||s==null||s.event()!=event||s.level()!=level||entity==null)return;
        var proof=s.proof();
        ItemStack stack=actualStack(s,entity);
        if(proof.kind()==E6bRitualCatalog.Kind.MIRROR_PAIR){mirrorAccepted(s,entity,stack);return;}
        if(proof.kind()==E6bRitualCatalog.Kind.EVENT_SPAWN&&entity instanceof net.minecraft.world.entity.animal.Animal&&eventMatches(event,level)){
            s.altar().tmm$markJawrOutput();
        }else if(proof.kind()==E6bRitualCatalog.Kind.EVENT_MUTATION&&eventMatches(event,level)&&event.getClass().getName().endsWith(".RotRitualEvent")
            &&(entity instanceof ItemEntity||entity instanceof LivingEntity&&!(entity instanceof Player))){
            s.altar().tmm$markJawrOutput();
        }else if(proof.kind()==E6bRitualCatalog.Kind.OWNER_ITEM&&!stack.isEmpty()){
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
        }else if(proof.kind()==E6bRitualCatalog.Kind.ITEM&&!stack.isEmpty()){
            if(stack.isEmpty()||!proof.output().equals(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString()))return;
            if(!(MxpReflect.call(event,"getItems") instanceof List<?> offered))return;
            for(Object expected:offered)if(expected instanceof ItemStack e&&stack.getCount()>=e.getCount()&&ItemStack.isSameItemSameComponents(stack,e)){
                s.altar().tmm$markJawrOutput();return;
            }
        }else if(proof.kind()==E6bRitualCatalog.Kind.ENTITY&&entity instanceof LivingEntity&&!(entity instanceof Player)
            &&proof.output().equals(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()))s.altar().tmm$markJawrOutput();
    }
}
