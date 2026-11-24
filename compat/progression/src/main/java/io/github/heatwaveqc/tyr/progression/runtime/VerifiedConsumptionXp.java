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
        var awards=new java.util.ArrayList<io.github.heatwaveqc.tyr.progression.data.XpSourceRouting.Candidate>();
        var discoveredPaths=new java.util.HashSet<String>();
        for(var entry:PathRegistry.trainingXp().entrySet()){
            String path=entry.getKey();var policy=entry.getValue();
            if(!TrainingRewards.eligible(player,path))continue;
            // NeoForge's conventional classification, not a custom second tag.
            // Also accept the vanilla/third-party drink use animation.
            boolean drink=drank||consumed.is(net.neoforged.neoforge.common.Tags.Items.DRINKS)
                ||ElixirRuntime.candidateOrBound(player,path,consumed);
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
                awards.add(TrainingRewards.candidate(policy,"first_consumable",
                    policy.discovery(),false,first));
                // Discovery is Path-local; emit the milestone once for each newly discovering Path.
                discoveredPaths.add(path);
            }
            if(drink&&ElixirRuntime.finished(player,path,policy,consumed,awards))continue;
            if(tagged(consumed,policy.meditativeTag())){
                awards.add(TrainingRewards.candidate(policy,drink?"meditative_drink":"meditative_food",
                    drink?policy.meditativeDrink():policy.meditativeFood(),true,null));
            }
        }
        ProgressionApi.awardSourceBatch(player,"training:"+UUID.randomUUID(),awards);
        if(!discoveredPaths.isEmpty())
            ProgressionApi.recordFiltered(player,Selectors.itemAction("food_discovered",consumed),
                    path->discoveredPaths.contains(path.id()));
    }
    public static void forget(ServerPlayer player){LAST.remove(player.getUUID());}
}
