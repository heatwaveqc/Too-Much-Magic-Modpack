package io.github.heatwaveqc.tmm.progression.runtime.forms;

import io.github.heatwaveqc.tmm.progression.runtime.EffectRuntime;
import io.github.heatwaveqc.tmm.progression.runtime.Selectors;
import io.github.heatwaveqc.tmm.progression.runtime.NewbieMode;
import io.github.heatwaveqc.tmm.progression.network.DietNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import java.util.*;

/** Permission is narrow and additive, never a replacement for a provider's diet restrictions. */
public final class DietPermissions {
    private DietPermissions(){}
    private static final Map<UUID,String> LAST=new HashMap<>();
    private static boolean active(ServerPlayer player){
        return player.isAlive()&&!player.isCreative()&&!player.isSpectator()&&!NewbieMode.isNewbie(player);
    }
    public static boolean permits(ServerPlayer player,ItemStack stack,String category){
        if(!active(player)||stack.isEmpty()||!Set.of("recreational_drink","alcohol").contains(category))return false;
        for(var a:EffectRuntime.active(player)){
            var fx=a.effect();
            if(fx.type().equals("diet_permission")&&category.equals(fx.params().get("category"))&&
                Selectors.item(fx.selector(),stack))return true;
        }
        return false;
    }
    public static boolean dragonDrink(ServerPlayer player,ItemStack stack){
        return ProviderFlags.nativeDragon(player)==io.github.heatwaveqc.tmm.progression.foundation.Condition.Result.PASS&&
            !stack.isEmpty()&&stack.getUseAnimation()==UseAnim.DRINK&&
            (permits(player,stack,"recreational_drink")||permits(player,stack,"alcohol"));
    }
    private static String selectors(ServerPlayer player){
        if(!active(player)||ProviderFlags.nativeDragon(player)!=io.github.heatwaveqc.tmm.progression.foundation.Condition.Result.PASS)return "";
        var selectors=new TreeSet<String>();
        for(var a:EffectRuntime.active(player)){
            var fx=a.effect();if(fx.type().equals("diet_permission")&&fx.selector().length()<=128&&
                Set.of("recreational_drink","alcohol").contains(fx.params().get("category")))selectors.add(fx.selector());
        }
        // Refuse oversize configuration for client preview rather than sending unbounded data.
        return selectors.size()>48?"":String.join("\n",selectors);
    }
    public static void sync(ServerPlayer player,boolean force){
        String next=selectors(player),prior=LAST.put(player.getUUID(),next);
        if(force||!Objects.equals(prior,next)||!next.isEmpty()&&player.tickCount%20==0)DietNetwork.send(player,next);
    }
    public static void forget(ServerPlayer player){LAST.remove(player.getUUID());}
}
