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
