package io.github.heatwaveqc.tmm.progression.mxp.hooks;

import io.github.heatwaveqc.tmm.progression.mxp.MxpMod;
import io.github.heatwaveqc.tmm.progression.mxp.core.OccultRitualProof;
import io.github.heatwaveqc.tmm.progression.mxp.core.RitualRewards;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import java.util.UUID;
import net.neoforged.fml.ModList;

/** One synchronous native virtual Ritual.finish; never award from the earlier native advancement. */
public final class OccultRitualContext {
    private OccultRitualContext(){}
    private static final ThreadLocal<Scope> ACTIVE=new ThreadLocal<>();
    private static final class Scope{
        final Object ritual;
        final ServerLevel level;
        final ServerPlayer actor;
        final RitualRewards.Receipt receipt;
        final OccultRitualProof.Kind kind;
        final ItemStack expected;
        boolean actual,valid=true;
        Scope(Object ritual,ServerLevel level,ServerPlayer actor,RitualRewards.Receipt receipt,OccultRitualProof.Kind kind,ItemStack expected){
            this.ritual=ritual;this.level=level;this.actor=actor;this.receipt=receipt;this.kind=kind;this.expected=expected;
        }
    }
    public static boolean supported(){
        return ModList.get().getModContainerById("occultism").map(c->"1.227.0".equals(c.getModInfo().getVersion().toString())).orElse(false);
    }
    private static String itemId(ItemStack item){
        return item==null||item.isEmpty()?"":BuiltInRegistries.ITEM.getKey(item.getItem()).toString();
    }
    public static boolean eligibleRecipe(RecipeHolder<?> recipe,ItemStack activation){
        if(recipe==null)return false;
        String id=recipe.id().toString();
        if(OccultRitualProof.kind(id)==OccultRitualProof.Kind.HELD||!OccultRitualProof.matchesBook(id,itemId(activation)))return false;
        if(!(MxpReflect.call(recipe.value(),"getActivationItem") instanceof Ingredient nativeIngredient))return false;
        ItemStack[] choices=nativeIngredient.getItems();
        return choices.length==1&&OccultRitualProof.matchesBook(id,itemId(choices[0]));
    }
    private static Scope prepare(Object ritual,Object bowl,Level world,BlockPos pos,ServerPlayer actor,ItemStack activation,UUID operation){
        if(operation==null||!supported()||!MxpMod.active()||!(world instanceof ServerLevel level)||
            !MxpPlayerPolicy.eligible(actor)||world.getBlockEntity(pos)!=bowl||
            MxpReflect.field(bowl,"castingPlayer")!=actor||!actor.getUUID().equals(MxpReflect.field(bowl,"castingPlayerId"))||
            !Boolean.TRUE.equals(MxpReflect.field(bowl,"ritualActive"))||
            !(MxpReflect.call(bowl,"getCurrentRitualRecipe") instanceof RecipeHolder<?> recipe)||
            MxpReflect.call(recipe.value(),"getRitual")!=ritual||!eligibleRecipe(recipe,activation))return null;
        String id=recipe.id().toString();
        var kind=OccultRitualProof.kind(id);
        ItemStack expected=kind==OccultRitualProof.Kind.ITEM?recipe.value().getResultItem(level.registryAccess()).copy():ItemStack.EMPTY;
        if(kind==OccultRitualProof.Kind.ITEM&&expected.isEmpty())return null;
        return new Scope(ritual,level,actor,new RitualRewards.Receipt(id,operation,actor.getUUID(),true,false),kind,expected);
    }
    public static void finish(Object ritual,Object bowl,Level world,BlockPos pos,ServerPlayer actor,ItemStack activation,UUID operation,Runnable nativeFinish){
        Scope outer=ACTIVE.get();
        if(outer!=null){
            outer.valid=false;
            ACTIVE.remove();
            try{nativeFinish.run();}finally{ACTIVE.set(outer);}
            return;
        }
        Scope scope;
        try{scope=prepare(ritual,bowl,world,pos,actor,activation,operation);}
        catch(RuntimeException e){scope=null;}
        if(scope==null){nativeFinish.run();return;}
        ACTIVE.set(scope);
        try{
            nativeFinish.run();
            if(scope.valid&&scope.actual)RitualSettlement.settle(scope.actor,scope.receipt);
        }finally{ACTIVE.remove();}
    }
    private static Scope itemScope(Object ritual,ItemStack stack){
        Scope s=ACTIVE.get();
        return s!=null&&s.valid&&s.ritual==ritual&&s.kind==OccultRitualProof.Kind.ITEM&&
            stack!=null&&!stack.isEmpty()&&stack.getItem()==s.expected.getItem()&&stack.getCount()==s.expected.getCount()?s:null;
    }
    public static boolean watchingItem(Object ritual,ItemStack stack){return itemScope(ritual,stack)!=null;}
    public static void placedItem(Object ritual,ItemStack expected,ItemStack installed,boolean emptyBefore){
        Scope s=itemScope(ritual,expected);
        if(s!=null&&emptyBefore&&ForgeRitualCompletion.sameStack(expected,installed))s.actual=true;
    }
    public static void insertedItem(Object ritual,Level level,Entity entity,boolean accepted){
        if(accepted&&entity instanceof ItemEntity item){
            Scope s=itemScope(ritual,item.getItem());
            if(s!=null&&s.level==level)s.actual=true;
        }
    }
    public static void insertedSummon(Object ritual,Level level,Entity entity,boolean accepted){
        Scope s=ACTIVE.get();
        if(s!=null&&s.valid&&s.ritual==ritual&&s.kind==OccultRitualProof.Kind.SUMMON&&s.level==level&&
            accepted&&entity instanceof LivingEntity&&!(entity instanceof Player))s.actual=true;
    }
}
