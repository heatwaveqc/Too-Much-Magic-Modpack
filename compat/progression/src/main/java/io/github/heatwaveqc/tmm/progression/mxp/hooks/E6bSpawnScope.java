package io.github.heatwaveqc.tmm.progression.mxp.hooks;

import io.github.heatwaveqc.tmm.progression.mxp.core.E6bSpawnCatalog;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import java.util.Optional;

/** Scope the exact native dispatch, never observe arbitrary nearby item/entity spawns. */
public final class E6bSpawnScope {
    private E6bSpawnScope(){}
    public interface EnchantedChalk { io.github.heatwaveqc.tmm.progression.mxp.core.RitualRewards.Receipt tmm$takeEnchanted(Object rite,ServerLevel level); }
    public interface Observer {
        void tmm$spawned(Object ritual,ServerLevel level,Entity entity);
        default void tmm$timeChanged(Object ritual,ServerLevel level){}
        default void tmm$effect(Object ritual,ServerLevel level){}
    }
    public static void timeChanged(Object ritual,ServerLevel level,long before){
        Observer observer=ACTIVE.get();
        if(observer!=null&&level.getDayTime()!=before)observer.tmm$timeChanged(ritual,level);
    }
    public static boolean observing(){return ACTIVE.get()!=null;}
    public static void effect(Object ritual,net.minecraft.world.level.Level world){
        Observer observer=ACTIVE.get();
        if(observer!=null&&world instanceof ServerLevel level)observer.tmm$effect(ritual,level);
    }
    public static boolean eidolonSpecial(E6bSpawnCatalog.Proof p){
        return p!=null&&java.util.Set.of("AbsorptionRitual","AllureRitual","RepellingRitual","DeceitRitual","CrystalRitual","PurifyRitual","LocationRitual")
            .contains(p.nativeClass().substring(p.nativeClass().lastIndexOf('.')+1));
    }
    public static boolean timeProof(E6bSpawnCatalog.Proof proof){
        return proof!=null&&(proof.nativeClass().endsWith(".DaylightRitual")||proof.nativeClass().endsWith(".MoonlightRitual"));
    }
    private static boolean classMatches(E6bSpawnCatalog.Proof proof,Object ritual){
        if(proof==null||ritual==null)return false;
        String name=ritual.getClass().getName();
        return proof.nativeClass().equals(name)||("eidolon_repraised:sanguine_amulet".equals(proof.output())&&name.equals("alexthw.eidolon_repraised.common.ritual.CraftingRitual$SanguineRitual"));
    }
    private static final ThreadLocal<Observer> ACTIVE=new ThreadLocal<>();
    public static void run(Observer observer,Runnable nativeCall){
        Observer previous=ACTIVE.get();
        if(previous!=null){ACTIVE.remove();try{nativeCall.run();}finally{ACTIVE.set(previous);}return;}
        if(observer==null){nativeCall.run();return;}
        ACTIVE.set(observer);try{nativeCall.run();}finally{ACTIVE.remove();}
    }
    public static void spawned(Object ritual,net.minecraft.world.level.Level world,Entity entity,boolean accepted){
        Observer observer=ACTIVE.get();
        if(accepted&&observer!=null&&world instanceof ServerLevel level)observer.tmm$spawned(ritual,level,entity);
    }
    public static int units(E6bSpawnCatalog.Proof proof,Object ritual,Entity entity){
        if(entity==null||!classMatches(proof,ritual)||timeProof(proof))return 0;
        if("eidolon_repraised:purify".equals(proof.output()))return entity instanceof LivingEntity&&! (entity instanceof Player)&&java.util.Set.of("minecraft:piglin","minecraft:hoglin").contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString())?1:0;
        if(proof.item()&&entity instanceof ItemEntity item){
            ItemStack stack=item.getItem();
            if("enchanted:circle_talisman".equals(proof.output())&&!boundTalisman(stack))return 0;
            if("minecraft:filled_map".equals(proof.output())&&(!stack.has(net.minecraft.core.component.DataComponents.MAP_ID)||!stack.has(net.minecraft.core.component.DataComponents.MAP_DECORATIONS)))return 0;
            return !stack.isEmpty()&&proof.output().equals(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString())?Math.min(proof.count(),stack.getCount()):0;
        }
        return !proof.item()&&entity instanceof LivingEntity&&!(entity instanceof Player)&&proof.output().equals(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString())?1:0;
    }
    private static boolean boundTalisman(ItemStack stack){
        try{
            Object supplier=MxpReflect.field(Class.forName("net.favouriteless.enchanted.common.items.component.EDataComponents"),"CIRCLE_MAGIC_SHAPE_MAP");
            if(!(supplier instanceof java.util.function.Supplier<?> s)||!(s.get() instanceof net.minecraft.core.component.DataComponentType<?> component))return false;
            return stack.get(component) instanceof java.util.Map<?,?> shapes&&!shapes.isEmpty();
        }catch(ClassNotFoundException|LinkageError|RuntimeException ignored){return false;}
    }
    @SuppressWarnings("unchecked")
    public static String enchantedId(ServerLevel level,Object type){
        if(level==null||type==null)return null;
        try {
            Object key=MxpReflect.field(Class.forName("net.favouriteless.enchanted.common.init.EData"),"RITE_TYPES_REGISTRY");
            if(!(key instanceof ResourceKey<?> resource))return null;
            Registry<Object> registry=level.registryAccess().registryOrThrow((ResourceKey<? extends Registry<Object>>)(ResourceKey<?>)resource);
            ResourceLocation id=registry.getKey(type);
            return id==null?null:id.toString();
        }catch(ClassNotFoundException|RuntimeException|LinkageError ignored){return null;}
    }
    public static String eidolonId(ServerLevel level,Object recipe){
        if(level==null||recipe==null)return null;
        for(String id:E6bSpawnCatalog.eidolonIds()){
            Object result=MxpReflect.call(level.getRecipeManager(),"byKey",ResourceLocation.parse(id));
            if(result instanceof Optional<?> opt&&opt.orElse(null) instanceof RecipeHolder<?> h&&h.value()==recipe)return id;
        }
        return null;
    }
    public static boolean eidolonNativeMatches(E6bSpawnCatalog.Proof proof,Object ritual){
        if(!classMatches(proof,ritual))return false;
        if(timeProof(proof))return proof.output().equals(String.valueOf(MxpReflect.call(ritual,"getRegistryName")));
        if(eidolonSpecial(proof)){
            if("minecraft:filled_map".equals(proof.output())){
                Object tag=MxpReflect.field(ritual,"structureTagKey");
                return tag instanceof net.minecraft.tags.TagKey<?> t&&"eidolon_repraised:catacombs".equals(t.location().toString());
            }
            String expected="eidolon_repraised:soul_shard".equals(proof.output())?"eidolon_repraised:crystal":proof.output();
            return expected.equals(String.valueOf(MxpReflect.call(ritual,"getRegistryName")));
        }
        if(proof.item()){
            Object result=MxpReflect.call(ritual,"getResult");
            return result instanceof ItemStack s&&!s.isEmpty()&&proof.output().equals(BuiltInRegistries.ITEM.getKey(s.getItem()).toString())&&s.getCount()==proof.count();
        }
        Object type=MxpReflect.call(ritual,"getEntityType"),count=MxpReflect.call(ritual,"getCount");
        return type instanceof net.minecraft.world.entity.EntityType<?> t&&proof.output().equals(BuiltInRegistries.ENTITY_TYPE.getKey(t).toString())&&count instanceof Integer n&&n==proof.count();
    }
}
