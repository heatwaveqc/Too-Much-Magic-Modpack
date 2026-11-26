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
    private static final class Scope {
        final ServerPlayer player;
        final ServerLevel level;
        final BlockPos pos;
        ItemStack virtual=ItemStack.EMPTY;
        boolean silk;
        Scope(ServerPlayer player,ServerLevel level,BlockPos pos){this.player=player;this.level=level;this.pos=pos;}
    }
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
        return io.github.heatwaveqc.tyr.progression.foundation.HarvestCapabilityGraph.plan(fingerprints(itemSelector,blockSelector));
    }
    public static java.util.Map<String,Set<String>> fingerprints(String itemSelector,String blockSelector){
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
        return java.util.Map.copyOf(fingerprints);
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
        if(!state.requiresCorrectToolForDrops())return List.of();
        String block=BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
        boolean alreadyHarvestable=canHarvest(p,state);
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
                        var delta=PathRegistry.generatedTrainingBlocks(path.id(),criterion.selector());
                        // Generated parallel branches train relative to their own
                        // prerequisites, not unrelated forms/paths. This avoids a
                        // dead end when another incomparable branch covers a block.
                        if(delta==null?alreadyHarvestable:!delta.contains(block))continue;
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
        if(!ToolForms.usable(p))return original.getAsBoolean();
        Scope parent=SCOPE.get();
        if(parent!=null&&(parent.player!=p||parent.pos.equals(pos)))return original.getAsBoolean();
        var level=p.serverLevel();
        if(!level.hasChunkAt(pos)||SpellMiningScope.captures(level,pos))return original.getAsBoolean();
        var state=level.getBlockState(pos);
        if(state.isAir()||state.getDestroySpeed(level,pos)<0||level.getBlockEntity(pos)!=null
            ||!level.mayInteract(p,pos))return original.getAsBoolean();
        // Secondary native breaks may use loot context but do not add trial credit.
        var trials=parent==null?eligibleTrials(p,state):List.<Trial>of();
        // Snapshot the real actor and pre-break harvest permission; a new tier
        // unlocked by this break must not retroactively qualify the same block.
        boolean submergedHarvest=parent==null&&p.isEyeInFluid(net.minecraft.tags.FluidTags.WATER)
            &&java.util.stream.StreamSupport.stream(p.getArmorSlots().spliterator(),false).allMatch(ItemStack::isEmpty)
            &&HarvestAbilities.canHarvestBarehand(p,state);
        var submergedSignal=submergedHarvest?Selectors.blockBreak(state):null;
        SCOPE.set(new Scope(p,level,pos.immutable()));
        boolean result;
        try{result=original.getAsBoolean();}
        finally{if(parent==null)SCOPE.remove();else SCOPE.set(parent);}
        if(result&&p.serverLevel()==level&&level.hasChunkAt(pos)
            &&VerifiedMiningXp.removed(state,level.getBlockState(pos))){
            for(var trial:trials)ProgressionApi.recordFiltered(p,
                new Signal("material_harvest_trial",trial.tool(),Set.of()),
                path->path.id().equals(trial.path()));
            if(submergedSignal!=null)ProgressionApi.record(p,new Signal("submerged_barehand_harvest",
                submergedSignal.target(),submergedSignal.tags()));
        }
        return result;
    }

    static boolean silkSelected(ServerPlayer p,BlockState state){
        return ToolForms.usable(p)&&EffectRuntime.active(p).stream().anyMatch(a->
            a.effect().type().equals("virtual_silk_touch")&&Selectors.block(a.effect().selector(),state));
    }
    private static boolean exactScope(Scope scope,ServerPlayer player,ServerLevel level,BlockPos pos){
        return scope!=null&&scope.player==player&&scope.level==level&&scope.pos.equals(pos);
    }
    /** True only for the exact temporary loot context, not a held or simulated tool. */
    static boolean virtualContext(ServerPlayer player,ItemStack tool){
        var scope=SCOPE.get();
        return scope!=null&&scope.player==player&&!scope.virtual.isEmpty()&&scope.virtual==tool;
    }
    static boolean silkContext(ServerPlayer player,ItemStack tool){
        return virtualContext(player,tool)&&SCOPE.get().silk;
    }
    /** Preserve the normal empty-hand event tool, including training and claim listeners. */
    static boolean silkDrops(ServerPlayer p,net.neoforged.neoforge.event.level.BlockDropsEvent event){
        var scope=SCOPE.get();
        return exactScope(scope,p,event.getLevel(),event.getPos())&&scope.silk&&event.getTool().isEmpty();
    }
    private static ItemStack materialTool(ServerPlayer p,BlockState state){
        // Use only an earned representative whose native rule actually permits this block.
        // Deterministic choice independent of registry iteration; never invoke its item actions.
        return EffectRuntime.active(p).stream().filter(a->a.effect().type().equals("native_harvest")
            &&Selectors.block(a.effect().selector(),state))
            .map(a->a.effect().params().get("tool")).distinct().sorted()
            .filter(id->correct(id,state)).findFirst().map(NativeHarvest::representative).orElse(ItemStack.EMPTY);
    }
    /** Substitute only within the original native loot computation for the exact break. */
    public static ItemStack lootTool(BlockState state,ServerLevel level,BlockPos pos,
            net.minecraft.world.entity.Entity entity,ItemStack real){
        var scope=SCOPE.get();
        if(!(entity instanceof ServerPlayer player)||!exactScope(scope,player,level,pos)
                ||!real.isEmpty()||level.getBlockEntity(pos)!=null)return real;
        boolean silk=silkSelected(player,state)&&HarvestAbilities.canHarvestBarehand(player,state);
        // Keep old paths' legacy Silk reroll behavior unchanged unless they explicitly adopt the new mode.
        if(!silk&&ClassAbilities.unlocked(player,"treasure_hand")
            &&"silk".equals(ProgressionApi.mode(player,"tyr_progression:treasure_mode","fortune")))return real;
        var shears=ToolForms.shearTool(player,state);
        var material=materialTool(player,state);
        if(silk){
            // Silk has precedence over the shear form; prefer an earned correct tool.
            // No fallback can grant a missing harvest permission.
            var tool=material.isEmpty()?new ItemStack(net.minecraft.world.item.Items.WOODEN_PICKAXE):material;
            var enchants=level.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT);
            tool.enchant(enchants.getOrThrow(net.minecraft.world.item.enchantment.Enchantments.SILK_TOUCH),1);
            scope.virtual=tool;scope.silk=true;
            return tool;
        }
        var tool=shears.isEmpty()?material:shears;
        if(tool.isEmpty())return real;
        scope.virtual=tool;scope.silk=false;
        return tool;
    }
}
