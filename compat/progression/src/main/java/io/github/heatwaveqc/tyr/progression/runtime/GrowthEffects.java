package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.data.MechanicalSpec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.entity.player.BonemealEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import java.util.*;

/** Observe actual completion before granting a bounded bonus. No new items or offspring. */
public final class GrowthEffects {
    private GrowthEffects(){}
    private static final int MAX_PENDING=256;
    private record CropKey(ServerLevel level,BlockPos pos){}
    private record CropReceipt(ServerPlayer player,BonemealEvent event,int count,int age){}
    private record BreedReceipt(ServerPlayer player,BabyEntitySpawnEvent event,long tick){}
    private static final Map<CropKey,CropReceipt> CROPS=new HashMap<>();
    private static final Set<CropKey> AMBIGUOUS=new HashSet<>();
    private static final List<BreedReceipt> BREEDS=new ArrayList<>();
    private static boolean usable(ServerPlayer p){
        return p.isAlive()&&!p.isCreative()&&!p.isSpectator()&&!NewbieMode.isNewbie(p);
    }
    public static void breed(BabyEntitySpawnEvent event){
        if(event.isCanceled()||!(event.getCausedByPlayer() instanceof ServerPlayer p)
            ||!usable(p)||!(event.getParentA() instanceof Animal)||!(event.getParentB() instanceof Animal)
            ||event.getChild()==null||BREEDS.size()>=MAX_PENDING)return;
        if(EffectRuntime.active(p).stream().noneMatch(a->Set.of("breeding_cooldown","baby_growth").contains(a.effect().type())))return;
        BREEDS.add(new BreedReceipt(p,event,p.serverLevel().getGameTime()));
    }
    public static void bonemeal(BonemealEvent event){
        if(event.isCanceled()||!(event.getLevel() instanceof ServerLevel level))return;
        var key=new CropKey(level,event.getPos().immutable());
        // Even another player's or dispenser's second use makes attribution ambiguous.
        if(CROPS.containsKey(key)){CROPS.remove(key);if(AMBIGUOUS.size()<MAX_PENDING)AMBIGUOUS.add(key);return;}
        if(AMBIGUOUS.contains(key)||CROPS.size()>=MAX_PENDING||AMBIGUOUS.size()>=MAX_PENDING)return;
        if(!(event.getPlayer() instanceof ServerPlayer p)||!usable(p)||!event.isValidBonemealTarget()
            ||!event.getStack().is(Items.BONE_MEAL)||event.getStack().isEmpty()
            ||!(event.getState().getBlock() instanceof CropBlock crop)||!ordinary(crop))return;
        if(EffectRuntime.active(p).stream().noneMatch(a->a.effect().type().equals("bonemeal_growth")&&Selectors.block(a.effect().selector(),event.getState())))return;
        CROPS.put(key,new CropReceipt(p,event,event.getStack().getCount(),crop.getAge(event.getState())));
    }
    private static boolean ordinary(CropBlock crop){
        return crop==Blocks.WHEAT||crop==Blocks.CARROTS||crop==Blocks.POTATOES||crop==Blocks.BEETROOTS;
    }
    public static void finish(ServerTickEvent.Post event){
        var crops=new HashMap<>(CROPS);CROPS.clear();AMBIGUOUS.clear();
        for(var e:crops.entrySet())finishCrop(event,e.getKey(),e.getValue());
        var breeds=new ArrayList<>(BREEDS);BREEDS.clear();
        Set<UUID> done=new HashSet<>();
        for(var b:breeds)finishBreed(event,b,done);
    }
    private static void finishCrop(ServerTickEvent.Post tick,CropKey key,CropReceipt r){
        var p=r.player();var e=r.event();var level=key.level();var pos=key.pos();
        if(p.server!=tick.getServer()||p.serverLevel()!=level||!usable(p)||e.isCanceled()
            ||e.getStack().getCount()!=r.count()-1||!level.hasChunkAt(pos)
            ||!level.mayInteract(p,pos)||p.distanceToSqr(pos.getX()+.5,pos.getY()+.5,pos.getZ()+.5)>64)return;
        var current=level.getBlockState(pos);
        if(current.getBlock()!=e.getState().getBlock()||!(current.getBlock() instanceof CropBlock crop))return;
        int age=crop.getAge(current);
        if(age<=r.age()||age>=crop.getMaxAge())return;
        double chance=0;int extra=0;
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();
            if(!fx.type().equals("bonemeal_growth")||!Selectors.block(fx.selector(),current))continue;
            chance+=MechanicalSpec.scaled(fx.amountPerRank(),a.rank(),1);
            extra=Math.max(extra,MechanicalSpec.integer(fx.params(),"extra_stages",1,1,3));
        }
        if(extra>0&&p.getRandom().nextDouble()<Math.min(1,chance))
            level.setBlock(pos,crop.getStateForAge(Math.min(crop.getMaxAge(),age+extra)),2);
    }
    private static void finishBreed(ServerTickEvent.Post tick,BreedReceipt r,Set<UUID> done){
        var p=r.player();var e=r.event();var level=p.serverLevel();
        if(p.server!=tick.getServer()||!usable(p)||e.isCanceled()
            ||level.getGameTime()-r.tick()<0||level.getGameTime()-r.tick()>1
            ||!(e.getParentA() instanceof Animal first)||!(e.getParentB() instanceof Animal second)
            ||first==second||first.level()!=level||second.level()!=level||first.getAge()<=0||second.getAge()<=0
            ||!(e.getChild() instanceof Animal child)||child.level()!=level||child.getAge()>=0
            ||level.getEntity(child.getUUID())!=child||!done.add(child.getUUID()))return;
        double cooldown=0,growth=0;
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();
            if(fx.type().equals("breeding_cooldown")&&Selectors.entity(fx.selector(),first)&&Selectors.entity(fx.selector(),second))
                cooldown+=MechanicalSpec.scaled(fx.amountPerRank(),a.rank(),4);
            if(fx.type().equals("baby_growth")&&Selectors.entity(fx.selector(),child))
                growth+=MechanicalSpec.scaled(fx.amountPerRank(),a.rank(),4);
        }
        if(cooldown>0){first.setAge(MechanicalSpec.fasterTimer(first.getAge(),cooldown));second.setAge(MechanicalSpec.fasterTimer(second.getAge(),cooldown));}
        if(growth>0)child.setAge(-MechanicalSpec.fasterTimer(-child.getAge(),growth));
    }
}
