package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.core.ProgressionMath;
import io.github.heatwaveqc.tyr.progression.core.Signal;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;

/** Reusable native material predicates. No numerical tier or hardcoded path identity. */
public final class NativeHarvest {
    private NativeHarvest(){}
    private record Scope(ServerPlayer player,ServerLevel level,BlockPos pos){}
    private record Trial(String path,String tool){}
    private static final ThreadLocal<Scope> SCOPE=new ThreadLocal<>();

    static ItemStack representative(String id){
        var item=BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
        var tool=new ItemStack(item);
        return !tool.isEmpty()&&tool.has(DataComponents.TOOL)?tool:ItemStack.EMPTY;
    }
    static boolean correct(String id,BlockState state){
        var tool=representative(id);
        return !tool.isEmpty()&&tool.isCorrectToolForDrops(state);
    }
    /** Read-only discovery API for a path-author-selected native item ID/tag and
     * block family. Call after server registry tags have been bound, not every tick.
     * Default-state fingerprints intentionally exclude item-specific behavior. */
    public static List<io.github.heatwaveqc.tyr.progression.foundation.HarvestCapabilityGraph.Group>
            discover(String itemSelector,String blockSelector){
        if(itemSelector==null||!itemSelector.matches("#?[a-z0-9_.-]+:[a-z0-9_./-]+")
            ||blockSelector==null||!blockSelector.matches("#?[a-z0-9_.-]+:[a-z0-9_./-]+"))
            throw new IllegalArgumentException("Discovery needs explicit item and block IDs/tags");
        var tools=new java.util.TreeMap<String,ItemStack>();
        for(var item:BuiltInRegistries.ITEM){
            var stack=new ItemStack(item);
            if(!stack.isEmpty()&&stack.has(DataComponents.TOOL)&&Selectors.item(itemSelector,stack)){
                tools.put(BuiltInRegistries.ITEM.getKey(item).toString(),stack);
                if(tools.size()>256)throw new IllegalArgumentException("Harvest discovery item selector too broad");
            }
        }
        var blocks=new java.util.TreeMap<String,BlockState>();
        for(var block:BuiltInRegistries.BLOCK){
            var state=block.defaultBlockState();
            if(state.requiresCorrectToolForDrops()&&Selectors.block(blockSelector,state)){
                blocks.put(BuiltInRegistries.BLOCK.getKey(block).toString(),state);
                if(blocks.size()>32768)throw new IllegalArgumentException("Harvest discovery block selector too broad");
            }
        }
        if((long)tools.size()*blocks.size()>2_000_000)
            throw new IllegalArgumentException("Harvest discovery comparison budget exceeded");
        var fingerprints=new java.util.TreeMap<String,Set<String>>();
        tools.forEach((id,stack)->{
            var matches=new java.util.TreeSet<String>();
            blocks.forEach((block,state)->{if(stack.isCorrectToolForDrops(state))matches.add(block);});
            fingerprints.put(id,matches);
        });
        return io.github.heatwaveqc.tyr.progression.foundation.HarvestCapabilityGraph.plan(fingerprints);
    }
    static boolean canHarvest(ServerPlayer p,BlockState state){
        if(!ToolForms.usable(p))return false;
        for(var active:EffectRuntime.active(p)){
            var effect=active.effect();
            if(effect.type().equals("native_harvest")&&Selectors.block(effect.selector(),state)
                &&correct(effect.params().get("tool"),state))return true;
        }
        return !ToolForms.shearTool(p,state).isEmpty();
    }

    /** A configured material trial is derived from the representative's actual
     * correct-for-drops predicate, not the name or numerical rank of an ore. */
    private static List<Trial> eligibleTrials(ServerPlayer p,BlockState state){
        if(!state.requiresCorrectToolForDrops()||canHarvest(p,state))return List.of();
        var trials=new ArrayList<Trial>();
        for(var path:PathRegistry.all().values()){
            if(!PathRegistry.eligible(p,path.id()))continue;
            var track=ProgressionApi.track(p,path.id());
            if(!track.joined())continue;
            for(var milestone:path.milestones()){
                if(track.completed(milestone.id())||!milestone.strictOrder()
                    ||!ProgressionMath.canCollectMilestone(milestone,track))continue;
                for(var criterion:milestone.criteria()){
                    if(criterion.event().equals("material_harvest_trial")&&correct(criterion.selector(),state)){
                        var trial=new Trial(path.id(),criterion.selector());
                        if(!trials.contains(trial))trials.add(trial);
                    }
                }
            }
        }
        return List.copyOf(trials);
    }

    /** Only a real, verified native player removal can count toward a trial. */
    public static boolean nativeBreak(ServerPlayer p,BlockPos pos,BooleanSupplier original){
        if(SCOPE.get()!=null||!ToolForms.usable(p))return original.getAsBoolean();
        var level=p.serverLevel();
        if(!level.hasChunkAt(pos)||SpellMiningScope.captures(level,pos))return original.getAsBoolean();
        var state=level.getBlockState(pos);
        if(state.isAir()||state.getDestroySpeed(level,pos)<0||level.getBlockEntity(pos)!=null
            ||!level.mayInteract(p,pos))return original.getAsBoolean();
        var trials=eligibleTrials(p,state);
        SCOPE.set(new Scope(p,level,pos.immutable()));
        boolean result;
        try{result=original.getAsBoolean();}
        finally{SCOPE.remove();}
        if(result&&p.serverLevel()==level&&level.hasChunkAt(pos)
            &&VerifiedMiningXp.removed(state,level.getBlockState(pos))){
            for(var trial:trials)ProgressionApi.recordFiltered(p,
                new Signal("material_harvest_trial",trial.tool(),Set.of()),
                path->path.id().equals(trial.path()));
        }
        return result;
    }

    /** Substitute shears only inside the original native loot roll for this exact
     * empty-hand break. The actual BlockDropsEvent tool remains the real fist. */
    public static ItemStack lootTool(BlockState state,ServerLevel level,BlockPos pos,
            net.minecraft.world.entity.Entity entity,ItemStack real){
        var scope=SCOPE.get();
        if(scope==null||scope.player()!=entity||scope.level()!=level||!scope.pos().equals(pos)
            ||!real.isEmpty()||level.getBlockEntity(pos)!=null)return real;
        var player=scope.player();
        // The existing legacy Silk mode owns its own loot replacement.
        if(ClassAbilities.unlocked(player,"treasure_hand")
            &&"silk".equals(ProgressionApi.mode(player,"tyr_progression:treasure_mode","fortune")))return real;
        var shears=ToolForms.shearTool(player,state);
        return shears.isEmpty()?real:shears;
    }
}
