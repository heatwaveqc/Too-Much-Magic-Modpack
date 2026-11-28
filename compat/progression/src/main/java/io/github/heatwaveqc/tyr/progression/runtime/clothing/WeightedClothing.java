package io.github.heatwaveqc.tyr.progression.runtime.clothing;

import io.github.heatwaveqc.tyr.progression.foundation.TrainingWeight;
import io.github.heatwaveqc.tyr.progression.runtime.ClothingApi;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

/** Equipment-derived state; never stored as a player bonus or applied to item attributes. */
public final class WeightedClothing {
    private WeightedClothing(){}
    private static final ResourceLocation SPEED = ResourceLocation.fromNamespaceAndPath("tyr_progression","training_weight");
    public static TrainingWeight tier(ItemStack stack){
        if(ClothingItems.martialStyle(stack).isEmpty()||!ClothingApi.isNonProtective(stack))
            return TrainingWeight.NORMAL;
        return stack.getOrDefault(ClothingComponents.TRAINING_WEIGHT.get(),TrainingWeight.NORMAL);
    }
    public static TrainingWeight equipped(ServerPlayer player){
        TrainingWeight result=TrainingWeight.NORMAL;
        for(ItemStack stack:player.getArmorSlots()){
            result=TrainingWeight.highest(result,tier(stack));
            if(result==TrainingWeight.EXTREME)break;
        }
        return result;
    }
    public static void reconcile(ServerPlayer player){
        var instance=player.getAttribute(Attributes.MOVEMENT_SPEED);
        if(instance==null)return;
        double speed=player.isAlive()&&!player.isSpectator()?equipped(player).speed():0;
        var previous=instance.getModifier(SPEED);
        if(previous!=null&&(previous.amount()!=speed||previous.operation()!=AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL||speed==0)){
            instance.removeModifier(SPEED); previous=null;
        }
        if(previous==null&&speed!=0)
            instance.addTransientModifier(new AttributeModifier(SPEED,speed,AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }
    public static void clear(ServerPlayer player){
        var instance=player.getAttribute(Attributes.MOVEMENT_SPEED);
        if(instance!=null)instance.removeModifier(SPEED);
    }
}
