package io.github.heatwaveqc.tyr.progression.mxp.hooks;

import io.github.heatwaveqc.tyr.progression.mxp.MxpMod;
import io.github.heatwaveqc.tyr.progression.mxp.core.GoetyRitualCatalog;
import io.github.heatwaveqc.tyr.progression.mxp.core.GoetyRitualCatalog.Kind;
import io.github.heatwaveqc.tyr.progression.mxp.core.GoetyRitualProof;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.items.IItemHandler;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/** Exact synchronous native terminal dispatch. Unrelated or nested calls never supply evidence. */
public final class GoetyRitualContext {
    private GoetyRitualContext(){}
    private static final String PREFIX="com.Polarice3.Goety.common.";
    private static final ThreadLocal<Scope> ACTIVE=new ThreadLocal<>();
    private static final class Scope {
        final Object ritual,altar; final ServerLevel world; final ServerPlayer actor;
        final GoetyRitualCatalog.Entry entry; final IItemHandler handler;
        final ItemStack activation; final Entity convertSource;
        Entity created; boolean observed,valid=true;
        Scope(Object ritual,Object altar,ServerLevel world,ServerPlayer actor,GoetyRitualCatalog.Entry entry,IItemHandler handler,ItemStack activation){
            this.ritual=ritual;this.altar=altar;this.world=world;this.actor=actor;this.entry=entry;this.handler=handler;this.activation=activation;
            this.convertSource=MxpReflect.field(altar,"getConvertEntity") instanceof Entity e?e:null;
        }
    }
    public static boolean supported(String recipe){
        if(recipe==null)return false;
        boolean base=ModList.get().getModContainerById("goety").map(c->"3.2.2".equals(c.getModInfo().getVersion().toString())).orElse(false);
        return base&&(!recipe.startsWith("goety_cataclysm:")||ModList.get().getModContainerById("goety_cataclysm").map(c->"1.21.1-1.8.4".equals(c.getModInfo().getVersion().toString())).orElse(false));
    }
    public static String currentId(Object altar){Object raw=MxpReflect.field(altar,"currentRitualRecipeId");return raw==null?null:raw.toString();}
    public static UUID owner(Object altar){return MxpReflect.field(altar,"castingPlayerId") instanceof UUID u?u:null;}
    public static IItemHandler handler(Object altar){return MxpReflect.field(altar,"itemStackHandler") instanceof IItemHandler h?h:null;}
    private static String item(ItemStack s){return s==null||s.isEmpty()?"":BuiltInRegistries.ITEM.getKey(s.getItem()).toString();}
    private static String type(EntityType<?> e){return e==null?"":BuiltInRegistries.ENTITY_TYPE.getKey(e).toString();}
    private static String tag(Object v){return v instanceof TagKey<?> k?k.location().toString():"";}
    private static String value(Object o){return o==null?"":o.toString();}
    private static int integer(Object o){return o instanceof Integer n?n:-1;}
    private static String enchant(Object recipe){Object v=MxpReflect.call(recipe,"getEnchantmentHolder");return v instanceof Holder<?> h?h.unwrapKey().map(k->k.location().toString()).orElse(""):"";}
    @SuppressWarnings("unchecked")
    private static Holder<Enchantment> enchantment(Object recipe){
        Object raw=MxpReflect.call(recipe,"getEnchantmentHolder");
        return raw instanceof Holder<?> h&&h.value() instanceof Enchantment?(Holder<Enchantment>)h:null;
    }
    private static String className(Kind k){
        return PREFIX+"ritual."+switch(k){case ITEM->"CraftItemRitual";case ENCHANT->"EnchantItemRitual";case LOCATE->"LocateRitual";case TELEPORT->"TeleportRitual";case CONVERT->"ConvertRitual";case SUMMON->"SummonRitual";};
    }
    private static RecipeHolder<?> holder(Object altar,ServerLevel world){
        Object id=MxpReflect.field(altar,"currentRitualRecipeId");
        if(!(id instanceof net.minecraft.resources.ResourceLocation key))return null;
        RecipeHolder<?> h=world.getRecipeManager().byKey(key).orElse(null);
        if(h==null||h.value()!=MxpReflect.call(altar,"getCurrentRitualRecipe")||!h.value().getClass().getName().equals(PREFIX+"crafting.RitualRecipe"))return null;
        return h;
    }
    private static GoetyRitualCatalog.Entry entry(RecipeHolder<?> h,ServerLevel world){
        if(h==null||!supported(h.id().toString()))return null;
        var e=GoetyRitualCatalog.lookup(h.id().toString());
        if(e==null)return null;
        Object recipe=h.value(),ritual=MxpReflect.call(recipe,"getRitual"),ingredients=MxpReflect.call(recipe,"getIngredients");
        if(ritual==null||!ritual.getClass().getName().equals(className(e.kind()))||!(ingredients instanceof Collection<?> list))return null;
        String out;
        int count=1;
        switch(e.kind()){
            case ITEM->{ItemStack s=h.value().getResultItem(world.registryAccess());out=item(s);count=s.getCount();}
            case ENCHANT->out=enchant(recipe);
            case LOCATE->out=tag(MxpReflect.call(recipe,"getStructureTag"));
            case TELEPORT->out=e.output();
            case CONVERT->{Object t=MxpReflect.call(recipe,"getEntityToConvertInto");out=t instanceof EntityType<?> v?type(v):"";}
            case SUMMON->{Object t=MxpReflect.call(recipe,"getEntityToSummon");out=t instanceof EntityType<?> v?type(v):"";}
            default->{return null;}
        }
        return e.matches(value(MxpReflect.call(recipe,"getRitualType")),out,count,value(MxpReflect.call(recipe,"getCraftType")),
            integer(MxpReflect.call(recipe,"getSoulCost")),integer(MxpReflect.call(recipe,"getDuration")),list.size(),
            value(MxpReflect.call(recipe,"getResearch")),tag(MxpReflect.call(recipe,"getEntityToSacrifice")),
            tag(MxpReflect.call(recipe,"getEntityToConvert")),integer(MxpReflect.call(recipe,"getXPLevelCost")))?e:null;
    }
    public static boolean eligible(Object altar,ServerLevel world){
        try{return entry(holder(altar,world),world)!=null;}catch(RuntimeException ignored){return false;}
    }
    public record Terminal(boolean successful,Entity delayed){}
    private static final Terminal FAILED=new Terminal(false,null);
    public static Terminal finish(Object ritual,Object altar,Level level,BlockPos pos,Player player,ItemStack activation,GoetyRitualProof op,Runnable original){
        Scope outer=ACTIVE.get();
        if(outer!=null){outer.valid=false;ACTIVE.remove();try{original.run();}finally{ACTIVE.set(outer);}return FAILED;}
        Scope s=null;Holder<Enchantment> enchant=null;int beforeLevel=0;GlobalPos destination=null;double x=0,y=0,z=0;
        try{
            if(op!=null&&MxpMod.active()&&level instanceof ServerLevel w&&w.getBlockEntity(pos)==altar&&
                player instanceof ServerPlayer p&&MxpPlayerPolicy.eligible(p)&&op.actor().equals(p.getUUID())&&op.matches(currentId(altar),owner(altar))&&MxpReflect.field(altar,"castingPlayer")==p){
                var h=holder(altar,w);var e=entry(h,w);var handler=handler(altar);
                Object remaining=MxpReflect.field(altar,"remainingAdditionalIngredients");
                if(e!=null&&handler!=null&&MxpReflect.call(h.value(),"getRitual")==ritual&&handler.getStackInSlot(0)==activation&&!activation.isEmpty()&&
                    integer(MxpReflect.field(altar,"currentTime"))>=e.duration()&&remaining instanceof Collection<?> rem&&rem.isEmpty()&&
                    (e.sacrifice().isEmpty()||Boolean.TRUE.equals(MxpReflect.field(altar,"sacrificeProvided")))){
                    s=new Scope(ritual,altar,w,p,e,handler,activation);
                    if(e.kind()==Kind.ENCHANT){
                        enchant=enchantment(h.value());
                        if(enchant==null)s=null;
                        else beforeLevel=EnchantmentHelper.getEnchantmentsForCrafting(activation).getLevel(enchant);
                    }else if(e.kind()==Kind.LOCATE&&!(MxpReflect.field(altar,"findStructurePos") instanceof BlockPos))s=null;
                    else if(e.kind()==Kind.TELEPORT){
                        Object target=MxpReflect.staticCall(PREFIX+"items.WaystoneItem","getPosition",activation);
                        if(!activation.is(BuiltInRegistries.ITEM.get(net.minecraft.resources.ResourceLocation.parse(e.output())))||!(target instanceof GlobalPos gp)||!gp.dimension().equals(p.level().dimension()))s=null;
                        else{destination=gp;x=p.getX();y=p.getY();z=p.getZ();}
                    }else if(e.kind()==Kind.CONVERT&&s.convertSource==null)s=null;
                }
            }
        }catch(RuntimeException ignored){s=null;}
        if(s==null){original.run();return FAILED;}
        ACTIVE.set(s);
        try{
            original.run();
            if(!s.valid)return FAILED;
            ItemStack after=s.handler.getStackInSlot(0);
            boolean success=switch(s.entry.kind()){
                case ITEM,LOCATE->s.observed&&!after.isEmpty();
                case SUMMON,CONVERT->s.observed&&after.isEmpty();
                case ENCHANT->!after.isEmpty()&&enchant!=null&&EnchantmentHelper.getEnchantmentsForCrafting(after).getLevel(enchant)>beforeLevel;
                case TELEPORT->destination!=null&&after.isEmpty()&&s.actor.level().dimension().equals(destination.dimension())&&
                    s.actor.distanceToSqr(destination.pos().getX(),destination.pos().getY()+1.0,destination.pos().getZ())<0.0001&&
                    s.actor.distanceToSqr(x,y,z)>0.0001;
            };
            if(!success)return FAILED;
            if("goety:summon_storm".equals(op.recipeId()))return s.created instanceof GoetyStormReceipt?new Terminal(true,s.created):FAILED;
            return new Terminal(true,null);
        }finally{ACTIVE.remove();}
    }
    /** Exact accepted slot insertion, including native inherited item components and map target. */
    public static void inserted(Object ritual,IItemHandler handler,int slot,ItemStack stack,boolean simulated,ItemStack remaining){
        Scope s=ACTIVE.get();
        if(s==null||!s.valid||s.ritual!=ritual||s.handler!=handler||slot!=0||simulated||stack.isEmpty()||!remaining.isEmpty())return;
        ItemStack after=handler.getStackInSlot(0);
        if(!ItemStack.matches(stack,after))return;
        if(s.entry.kind()==Kind.ITEM)s.observed=s.entry.output().equals(item(stack))&&s.entry.count()==stack.getCount();
        if(s.entry.kind()==Kind.LOCATE&&stack.is(Items.FILLED_MAP)&&stack.getCount()==1&&stack.has(DataComponents.MAP_ID)){
            Object decorations=stack.get(DataComponents.MAP_DECORATIONS);
            Object map=MxpReflect.call(decorations,"decorations");
            s.observed=map instanceof Map<?,?> m&&m.containsKey("+");
        }
    }
    /** Observe the final native creation, including the variant selected by the native ritual. */
    public static void created(Object ritual,Entity entity,EntityType<?> requested,Level world,BlockPos pos,Object altar,Player actor){
        Scope s=ACTIVE.get();
        if(s==null||!s.valid||s.entry.kind()!=Kind.SUMMON||s.ritual!=ritual||s.altar!=altar||world!=s.world||actor!=s.actor||s.world.getBlockEntity(pos)!=altar)return;
        if(entity==null||entity.getType()!=requested){s.created=null;return;}
        if(s.created==null&&!s.entry.output().equals(type(requested)))return;
        // The second exact native creation in finish is the optional variant.
        if(s.created!=null&&s.entry.type().endsWith("no_variant")&&!s.entry.output().equals(type(requested))){s.valid=false;return;}
        s.created=entity;
    }
    public static void spawned(Object ritual,Level world,Entity entity,boolean accepted){
        Scope s=ACTIVE.get();
        if(s!=null&&s.valid&&s.entry.kind()==Kind.SUMMON&&s.ritual==ritual&&world==s.world&&s.created==entity&&accepted&&s.world.getEntity(entity.getUUID())==entity)
            s.observed=true;
    }
    public static void converted(Object ritual,Entity source,EntityType<?> target,Entity result){
        Scope s=ACTIVE.get();
        if(s!=null&&s.valid&&s.entry.kind()==Kind.CONVERT&&s.ritual==ritual&&source==s.convertSource&&source!=null&&result!=null&&result!=source&&
            s.entry.output().equals(type(target))&&result.getType()==target&&source.isRemoved()&&result.level()==s.world&&s.world.getEntity(result.getUUID())==result)s.observed=true;
    }
}
