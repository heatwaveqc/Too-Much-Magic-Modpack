package io.github.heatwaveqc.tyr.progression.mxp.hooks;
import io.github.heatwaveqc.tyr.progression.mxp.MxpMod;
import io.github.heatwaveqc.tyr.progression.mxp.core.RootsRitualProof;
import io.github.heatwaveqc.tyr.progression.mxp.core.RootsRitualProof.Kind;
import io.github.heatwaveqc.tyr.progression.mxp.core.RitualRewards;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.items.ItemStackHandler;

/** One actual native altar dispatch: pending receipt is consumed only after altar clearing. */
public final class RootsRitualContext {
    private RootsRitualContext(){}
    public interface Altar {
        RootsRitualProof.Operation tmm$takeRootsOperation();
        void tmm$rootsPending(RitualRewards.Receipt receipt);
        RitualRewards.Receipt tmm$takeRootsPending();
    }
    private static final ThreadLocal<Scope> ACTIVE=new ThreadLocal<>();
    private static final class Scope {
        final ServerLevel level; final Object effect; final RootsRitualProof.Rule rule;
        boolean valid=true,actual;
        Scope(ServerLevel level,Object effect,RootsRitualProof.Rule rule){this.level=level;this.effect=effect;this.rule=rule;}
    }
    public static boolean supported(){
        return ModList.get().getModContainerById("rootsclassic").map(c->{
            String v=c.getModInfo().getVersion().toString();
            return "1.5.10".equals(v)||"1.21.1-1.5.10".equals(v);
        }).orElse(false);
    }
    public static int progress(Object altar){return MxpReflect.call(altar,"getProgress") instanceof Integer i?i:-1;}
    public static RecipeHolder<?> current(Object altar){return MxpReflect.call(altar,"getCurrentRitual") instanceof RecipeHolder<?> recipe?recipe:null;}
    public static boolean currentId(Object altar,String id){var c=current(altar);return c!=null&&c.id().toString().equals(id);}
    private static String itemId(ItemStack stack){return stack==null||stack.isEmpty()?"":BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();}
    public static RootsRitualProof.Rule eligibleRecipe(RecipeHolder<?> holder,ServerLevel level){
        if(holder==null||level==null)return null;
        var rule=RootsRitualProof.rule(holder.id().toString());
        if(rule==null)return null;
        Object recipe=holder.value(),effect=MxpReflect.field(recipe,"effect"),nativeId=MxpReflect.field(recipe,"effectId"),nativeLevel=MxpReflect.field(recipe,"level");
        if(effect==null||nativeId==null||!(nativeLevel instanceof Integer n)||!rule.matches(n,nativeId.toString(),effect.getClass().getName()))return null;
        if(rule.kind()==Kind.ITEM||rule.kind()==Kind.SUMMON){
            if(!(MxpReflect.field(recipe,"effectConfig") instanceof CompoundTag config))return null;
            if(rule.kind()==Kind.ITEM){
                ItemStack expected=ItemStack.parseOptional(level.registryAccess(),config.getCompound("result"));
                if(!rule.resultMatches(itemId(expected),expected.getCount()))return null;
            }else if(!rule.resultMatches(config.getString("entity"),1))return null;
        }
        return rule;
    }
    public static RitualRewards.Receipt finish(Object altar,Object recipe,Level world,BlockPos pos,RootsRitualProof.Operation operation,Runnable nativeFinish){
        Scope outer=ACTIVE.get();
        if(outer!=null){outer.valid=false;ACTIVE.remove();try{nativeFinish.run();}finally{ACTIVE.set(outer);}return null;}
        Scope scope=null;
        try{
            var holder=current(altar);
            if(operation!=null&&supported()&&MxpMod.active()&&world instanceof ServerLevel level&&level.getServer().isSameThread()&&
                world.getBlockEntity(pos)==altar&&progress(altar)==0&&holder!=null&&holder.value()==recipe&&holder.id().toString().equals(operation.recipeId())&&
                MxpPlayerPolicy.eligible(level.getServer().getPlayerList().getPlayer(operation.actor()))){
                var rule=eligibleRecipe(holder,level);
                if(rule!=null)scope=new Scope(level,MxpReflect.field(recipe,"effect"),rule);
            }
        }catch(RuntimeException ignored){scope=null;}
        if(scope==null){nativeFinish.run();return null;}
        boolean rainBefore=scope.level.getLevelData().isRaining();long timeBefore=scope.level.getDayTime();
        ACTIVE.set(scope);
        try{
            nativeFinish.run();
            Kind kind=scope.rule.kind();
            if(kind==Kind.RAIN_ON||kind==Kind.RAIN_OFF)scope.actual=RootsRitualProof.weatherChanged(kind,rainBefore,scope.level.getLevelData().isRaining());
            if(kind==Kind.TIME)scope.actual=RootsRitualProof.timeAdvanced(timeBefore,scope.level.getDayTime());
            return scope.valid&&scope.actual?operation.receipt():null;
        }finally{ACTIVE.remove();}
    }
    public static boolean watching(Object effect,Kind kind,Level level){
        Scope s=ACTIVE.get();return s!=null&&s.valid&&s.effect==effect&&s.rule.kind()==kind&&s.level==level;
    }
    public static void changed(Object effect,Kind kind,Level level,boolean actual){
        if(actual&&watching(effect,kind,level))ACTIVE.get().actual=true;
    }
    public static void spawned(Object effect,Level level,Entity entity,boolean accepted){
        Scope s=ACTIVE.get();
        if(!accepted||s==null||!s.valid||s.effect!=effect||s.level!=level)return;
        if(s.rule.kind()==Kind.ITEM&&entity instanceof ItemEntity item&&s.rule.resultMatches(itemId(item.getItem()),item.getItem().getCount()))s.actual=true;
        else if(s.rule.kind()==Kind.SUMMON&&entity instanceof LivingEntity&&!(entity instanceof Player)&&s.rule.resultMatches(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString(),1))s.actual=true;
    }
    public static void settled(Object altar,RitualRewards.Receipt receipt){
        if(receipt==null||!supported()||!(altar instanceof BlockEntity block)||!(block.getLevel() instanceof ServerLevel world)||
            !world.getServer().isSameThread()||world.getBlockEntity(block.getBlockPos())!=block||progress(altar)!=0||current(altar)!=null||
            !(MxpReflect.field(altar,"inventory") instanceof ItemStackHandler inventory))return;
        for(int slot=0;slot<inventory.getSlots();slot++)if(!inventory.getStackInSlot(slot).isEmpty())return;
        RitualSettlement.settle(world.getServer().getPlayerList().getPlayer(receipt.actor()),receipt);
    }
}
