package io.github.heatwaveqc.tyr.progression.client;
import io.github.heatwaveqc.tyr.progression.runtime.Selectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import java.lang.ref.WeakReference;
import java.util.*;

/** Server lease belongs to a particular local player, not a world-global client flag. */
public final class ClientDiet {
 private ClientDiet(){}
 private static WeakReference<LocalPlayer> owner=new WeakReference<>(null);
 private static List<String> selectors=List.of();
 private static int received;
 public static void accept(String payload){
    var player=Minecraft.getInstance().player;
    owner=new WeakReference<>(player);received=player==null?0:player.tickCount;
    var values=payload.isEmpty()?List.<String>of():Arrays.asList(payload.split("\\n"));
    selectors=values.size()>48||values.stream().anyMatch(id->id.length()>128||!id.matches("#?[a-z0-9_.-]+:[a-z0-9_./-]+"))?List.of():List.copyOf(values);
 }
 public static boolean allows(LivingEntity entity,ItemStack stack){
    var player=Minecraft.getInstance().player;
    return player!=null&&player==entity&&owner.get()==player&&player.isAlive()&&player.tickCount>=received&&
        player.tickCount-received<=40&&stack.getUseAnimation()==UseAnim.DRINK&&
        selectors.stream().anyMatch(selector->Selectors.item(selector,stack));
 }
}
