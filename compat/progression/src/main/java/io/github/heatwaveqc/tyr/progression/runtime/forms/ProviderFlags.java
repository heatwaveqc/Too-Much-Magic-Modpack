package io.github.heatwaveqc.tyr.progression.runtime.forms;

import io.github.heatwaveqc.tyr.progression.data.Eligibility;
import io.github.heatwaveqc.tyr.progression.foundation.Condition.Result;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Optional, read-only adapter to the existing Transformations ProbeRegistry.
 * No dependency or new transformation state. Unknown is distinct from false.
 */
public final class ProviderFlags {
    private ProviderFlags() {}
    private static final String MOD="tmm_transformations";
    private static final String ROOT="io.github.heatwaveqc.tmm.transformations.";
    private record Access(Method observe, Method observations) {}
    private record Cached(Object server,long tick,Map<String,Result> flags) {}
    private static volatile Access access;
    private static final Map<UUID,Cached> cache=new HashMap<>();

    public static void register() {
        register("jawr_vampire","JAWR_VAMPIRE");
        register("jawr_werewolf","JAWR_LYCANTHROPE");
        register("jawr_lich","FORBIDDEN_MAGIC_LICH");
        register("goety_lich","GOETY_LICH");
        register("dragon","DRAGON_SURVIVAL_DRAGON");
        Eligibility.registerFlag("tyr_progression:lich",p->{
            var obs=observations(p);
            return io.github.heatwaveqc.tyr.progression.foundation.FormCombination.lich(
                obs.getOrDefault("GOETY_LICH",Result.UNAVAILABLE),obs.getOrDefault("FORBIDDEN_MAGIC_LICH",Result.UNAVAILABLE));
        });
        Eligibility.registerFlag("tyr_progression:archlich",p->{
            var obs=observations(p);
            return io.github.heatwaveqc.tyr.progression.foundation.FormCombination.archlich(nativeDragon(p),
                obs.getOrDefault("GOETY_LICH",Result.UNAVAILABLE),obs.getOrDefault("FORBIDDEN_MAGIC_LICH",Result.UNAVAILABLE));
        });
        Eligibility.registerFlag("tyr_progression:dracolich",p->{
            var obs=observations(p);
            return io.github.heatwaveqc.tyr.progression.foundation.FormCombination.dracolich(nativeDragon(p),
                obs.getOrDefault("GOETY_LICH",Result.UNAVAILABLE),obs.getOrDefault("FORBIDDEN_MAGIC_LICH",Result.UNAVAILABLE));
        });
        for(String name:java.util.List.of("cave","forest","sea"))
            Eligibility.registerFlag("tyr_progression:"+name+"_dragon",
                player->dragonSpecies(player,"dragonsurvival:"+name+"_dragon"));
    }
    private static void register(String name,String nativeId) {
        Eligibility.registerFlag("tyr_progression:"+name,
                player -> observations(player).getOrDefault(nativeId,Result.UNAVAILABLE));
    }
    public static Result nativeDragon(ServerPlayer player){
        if(!ModList.get().getModContainerById("dragonsurvival")
            .filter(mod->mod.getModInfo().getVersion().toString().startsWith("2.0.74")).isPresent()||
            player.getServer()==null||!player.getServer().isSameThread())return Result.UNAVAILABLE;
        try{
            Class<?> provider=Class.forName("by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider");
            Object handler=provider.getMethod("getData",net.minecraft.world.entity.player.Player.class).invoke(null,player);
            Object value=handler.getClass().getMethod("isDragon").invoke(handler);
            return value instanceof Boolean b?(b?Result.PASS:Result.FAIL):Result.UNAVAILABLE;
        }catch(ReflectiveOperationException|LinkageError|RuntimeException unavailable){return Result.UNAVAILABLE;}
    }
    /** Exact 2.0.74 native read-only species ID; absent provider is not a human observation. */
    public static Result dragonSpecies(ServerPlayer player,String expected){
        if(!ModList.get().getModContainerById("dragonsurvival")
            .filter(mod->mod.getModInfo().getVersion().toString().startsWith("2.0.74")).isPresent()||
            player.getServer()==null||!player.getServer().isSameThread())return Result.UNAVAILABLE;
        try{
            Class<?> provider=Class.forName("by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider");
            Object handler=provider.getMethod("getData",net.minecraft.world.entity.player.Player.class).invoke(null,player);
            Object isDragon=handler.getClass().getMethod("isDragon").invoke(handler);
            if(!(isDragon instanceof Boolean dragon))return Result.UNAVAILABLE;
            if(!dragon)return Result.FAIL;
            Object id=handler.getClass().getMethod("speciesId").invoke(handler);
            if(!(id instanceof net.minecraft.resources.ResourceLocation location))return Result.UNAVAILABLE;
            return location.toString().equals(expected)?Result.PASS:Result.FAIL;
        }catch(ReflectiveOperationException|LinkageError|RuntimeException unavailable){return Result.UNAVAILABLE;}
    }
    private static Map<String,Result> observations(ServerPlayer player) {
        if(!ModList.get().isLoaded(MOD) || player.getServer()==null || !player.getServer().isSameThread())
            return Map.of();
        long tick=player.getServer().overworld().getGameTime();
        Cached old=cache.get(player.getUUID());
        if(old!=null && old.server()==player.getServer() && old.tick()==tick) return old.flags();
        try {
            Access a=access;
            if(a==null) {
                Class<?> registry=Class.forName(ROOT+"adapters.ProbeRegistry");
                Method observe=registry.getMethod("observe",ServerPlayer.class);
                a=new Access(observe,observe.getReturnType().getMethod("observations"));
                access=a;
            }
            Object snapshot=a.observe().invoke(null,player);
            Object raw=a.observations().invoke(snapshot);
            if(!(raw instanceof Map<?,?> values)) return Map.of();
            var flags=new HashMap<String,Result>();
            for(var entry:values.entrySet()) {
                if(!(entry.getKey() instanceof Enum<?> key) || !(entry.getValue() instanceof Enum<?> value)) continue;
                flags.put(key.name(),switch(value.name()) {
                    case "PRESENT" -> Result.PASS;
                    case "ABSENT" -> Result.FAIL;
                    default -> Result.UNAVAILABLE;
                });
            }
            Map<String,Result> result=Map.copyOf(flags);
            cache.put(player.getUUID(),new Cached(player.getServer(),tick,result));
            return result;
        } catch(ReflectiveOperationException | LinkageError | RuntimeException failure) {
            return Map.of();
        }
    }
    public static void forget(UUID player) { cache.remove(player); }
}
