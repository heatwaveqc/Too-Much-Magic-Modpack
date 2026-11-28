package io.github.heatwaveqc.tyr.progression.runtime.clothing;

import io.github.heatwaveqc.tyr.progression.foundation.TrainingWeight;
import io.github.heatwaveqc.tyr.progression.runtime.ClothingApi;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;

/** Equipment-derived state; never stored as a player bonus or applied to item attributes. */
public final class WeightedClothing {
    private WeightedClothing(){}
    private static final ResourceLocation SPEED = ResourceLocation.fromNamespaceAndPath("tyr_progression","training_weight");
    public static TrainingWeight tier(ItemStack stack){
        if(!ClothingItems.isTrainingGarment(stack)||!ClothingApi.isNonProtective(stack))
            return TrainingWeight.NORMAL;
        return stack.getOrDefault(ClothingComponents.TRAINING_WEIGHT.get(),TrainingWeight.NORMAL);
    }
    private static TrainingWeight slot(ServerPlayer player,EquipmentSlot slot){
        var stack=player.getItemBySlot(slot);
        // Commands or modded equip adapters cannot obtain a bonus from
        // garments placed into a nonmatching standard equipment slot.
        if(!(stack.getItem() instanceof ArmorItem armor)||armor.getEquipmentSlot()!=slot)
            return TrainingWeight.NORMAL;
        return tier(stack);
    }
    public static TrainingWeight.Loadout equipped(ServerPlayer player){
        return TrainingWeight.loadout(slot(player,EquipmentSlot.HEAD),slot(player,EquipmentSlot.CHEST),
                slot(player,EquipmentSlot.LEGS),slot(player,EquipmentSlot.FEET));
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
