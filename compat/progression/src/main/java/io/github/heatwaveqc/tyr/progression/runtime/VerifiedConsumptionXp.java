package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.state.PathDiscoveries;
import io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Settled use-completion only. Distinct IDs never depend on item NBT. */
public final class VerifiedConsumptionXp {
    private VerifiedConsumptionXp() {}
    private static final Map<UUID,Map<String,Long>> LAST=new HashMap<>();
    private static boolean tagged(ItemStack stack,String id){
        return stack.is(TagKey.create(Registries.ITEM,ResourceLocation.parse(id)));
    }
    public static void finished(ServerPlayer player,ItemStack consumed) {
        if(consumed.isEmpty())return;
        boolean drank=consumed.getUseAnimation()==UseAnim.DRINK;
        boolean food=consumed.getFoodProperties(player)!=null;
        boolean anything=false;
        for(var entry:PathRegistry.trainingXp().entrySet()){
            String path=entry.getKey();var policy=entry.getValue();
            if(!TrainingRewards.eligible(player,path))continue;
            boolean drink=drank||tagged(consumed,policy.drinkTag());
            if(!drink&&!food)continue;
            String item=BuiltInRegistries.ITEM.getKey(consumed.getItem()).toString();
            String key=path+"/"+item;
            long tick=player.serverLevel().getGameTime();
            var seen=LAST.computeIfAbsent(player.getUUID(),ignored->new HashMap<>());
            if(seen.getOrDefault(key,Long.MIN_VALUE)==tick)continue;
            if(seen.size()>4096)seen.clear();
            seen.put(key,tick);
            PathDiscoveries progress=player.getData(ProgressionAttachments.PATH_DISCOVERIES.get());
            if(!progress.contains(path,item)){
                PathDiscoveries updated=progress.discover(path,item);
                if(updated!=progress)player.setData(ProgressionAttachments.PATH_DISCOVERIES.get(),updated);
                String first=policy.discoveryReplay()?null:"first:"+UUID.nameUUIDFromBytes(
                    (path+"|"+item).getBytes(StandardCharsets.UTF_8));
                TrainingRewards.award(player,path,policy,"first_consumable",
                    policy.discovery(),false,first);
                // Discovery is Path-local; an earlier Normie snack never pre-marks it.
                ProgressionApi.record(player,Selectors.itemAction("food_discovered",consumed));
            }
            if(tagged(consumed,policy.meditativeTag())){
                TrainingRewards.award(player,path,policy,drink?"meditative_drink":"meditative_food",
                    drink?policy.meditativeDrink():policy.meditativeFood(),true,null);
            }
            anything=true;
        }
    }
    public static void forget(ServerPlayer player){LAST.remove(player.getUUID());}
}
