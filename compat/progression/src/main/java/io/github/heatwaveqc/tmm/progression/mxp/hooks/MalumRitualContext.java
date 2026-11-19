package io.github.heatwaveqc.tmm.progression.mxp.hooks;

import io.github.heatwaveqc.tmm.progression.mxp.core.MalumRitualCatalog;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.ModList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.Set;
import java.util.stream.Collectors;

/** Pinned optional mod interface; no gameplay changes or registry mutation. */
public final class MalumRitualContext {
    private MalumRitualContext(){}
    public static boolean supported(){
        return ModList.get().getModContainerById("malum").map(c->"1.8.2".equals(c.getModInfo().getVersion().toString())).orElse(false)&&
            ModList.get().getModContainerById("gaze").map(c->"1.1.7.1".equals(c.getModInfo().getVersion().toString())).orElse(true);
    }
    public static String state(Object tile){Object s=MxpReflect.field(tile,"state");return s instanceof Enum<?> e?e.name():"";}
    public static boolean installed(Object tile,ServerLevel world){
        return tile instanceof BlockEntity be&&be.getLevel()==world&&world.getServer().isSameThread()&&world.getBlockEntity(be.getBlockPos())==be;
    }
    public static String id(Object value){Object key=MxpReflect.call(value,"getRegistryName");return key instanceof ResourceLocation rl?rl.toString():null;}
    public static String result(Object brazier){return id(MxpReflect.field(MxpReflect.field(brazier,"recipe"),"result"));}
    private static int count(Object o){Object v=MxpReflect.call(o,"count");return v instanceof Integer i?i:-1;}
    private static List<Integer> counts(Object o){return o instanceof List<?> l?l.stream().map(MalumRitualContext::count).toList():List.of(-1);}
    public static String eligibleEffect(Object brazier,ServerLevel world){
        try{
            Object recipe=MxpReflect.field(brazier,"recipe");
            if(recipe==null||!recipe.getClass().getName().equals("com.sammy.malum.common.recipe.SoulBindingRecipe"))return null;
            String id=result(brazier);
            var e=MalumRitualCatalog.lookupGeas(id);
            if(e==null||!supported()||(id.startsWith("gaze:")&&!ModList.get().isLoaded("gaze")))return null;
            var h=world.getRecipeManager().byKey(ResourceLocation.parse(e.recipe())).orElse(null);
            return h!=null&&h.value()==recipe&&e.matches(h.id().toString(),count(MxpReflect.field(recipe,"input")),counts(MxpReflect.field(recipe,"extraInputs")),counts(MxpReflect.field(recipe,"spirits")))?id:null;
        }catch(RuntimeException|LinkageError ignored){return null;}
    }
    public static Set<UUID> sacrifices(Object tile){
        Object raw=MxpReflect.field(tile,"sacrificedTargets");
        return raw instanceof Collection<?> l?l.stream().filter(UUID.class::isInstance).map(UUID.class::cast).collect(Collectors.toUnmodifiableSet()):Set.of();
    }
}
