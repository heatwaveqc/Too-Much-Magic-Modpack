package io.github.heatwaveqc.tmm.progression.foundation;

import io.github.heatwaveqc.tmm.progression.data.PathRegistry;
import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import io.github.heatwaveqc.tmm.progression.runtime.Selectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import java.util.List;
import java.util.Set;
import static io.github.heatwaveqc.tmm.progression.foundation.Condition.Result.*;

/** Read-only predicates for enrollment, sources, purchase and effect eligibility. */
public final class ContextConditions {
    private ContextConditions(){}
    private static Condition.Result truth(boolean value){return value?PASS:FAIL;}
    public static Condition<ServerPlayer> meter(String type,Comparison comparison,double value){
        double threshold=ConditionSpec.threshold(value,0,type.equals("food_level")?20:1);
        return player->{
            double current=switch(type){
                case "health_fraction"->player.getHealth()/Math.max(1,player.getMaxHealth());
                case "food_level"->player.getFoodData().getFoodLevel();
                default->throw new IllegalArgumentException("Unknown meter "+type);
            };
            return truth(comparison.test(current,threshold));
        };
    }
    public static Condition<ServerPlayer> time(int start,int end){
        var window=new ConditionSpec.TimeWindow(start,end);
        return p->truth(window.contains(p.level().getDayTime()));
    }
    public static Condition<ServerPlayer> weather(String value){
        if(!Set.of("clear","rain","thunder").contains(value))throw new IllegalArgumentException("Unknown weather");
        return p->{
            boolean rain=p.level().isRainingAt(p.blockPosition());
            return truth(switch(value){case "clear"->!rain;case "rain"->rain;default->rain&&p.level().isThundering();});
        };
    }
    public static Condition<ServerPlayer> state(String name,boolean expected){
        if(!Set.of("sneaking","sprinting","underwater","in_lava","on_ground","sky_visible","empty_hand","unarmored").contains(name))
            throw new IllegalArgumentException("Unknown movement/environment state");
        return p->truth(expected==switch(name){
            case "sneaking"->p.isCrouching();
            case "sprinting"->p.isSprinting();
            case "underwater"->p.isUnderWater();
            case "in_lava"->p.isInLava();
            case "on_ground"->p.onGround();
            case "sky_visible"->p.level().canSeeSky(p.blockPosition().above());
            case "empty_hand"->p.getMainHandItem().isEmpty();
            default->{boolean empty=true;for(var stack:p.getArmorSlots())if(!stack.isEmpty())empty=false;yield empty;}
        });
    }
    public static Condition<ServerPlayer> equipped(String slot,String target){
        List<EquipmentSlot> slots=switch(slot){
            case "mainhand"->List.of(EquipmentSlot.MAINHAND);
            case "offhand"->List.of(EquipmentSlot.OFFHAND);
            case "head"->List.of(EquipmentSlot.HEAD);
            case "chest"->List.of(EquipmentSlot.CHEST);
            case "legs"->List.of(EquipmentSlot.LEGS);
            case "feet"->List.of(EquipmentSlot.FEET);
            case "armor"->List.of(EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.LEGS,EquipmentSlot.FEET);
            case "any"->List.of(EquipmentSlot.MAINHAND,EquipmentSlot.OFFHAND,EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.LEGS,EquipmentSlot.FEET);
            default->throw new IllegalArgumentException("Unknown equipment slot");
        };
        boolean wildcard=target.equals("*"), tag=target.startsWith("#");
        if(!wildcard&&!target.matches("#?[a-z0-9_.-]+:[a-z0-9_./-]+"))throw new IllegalArgumentException("Invalid item selector");
        ResourceLocation id=wildcard?null:ResourceLocation.parse(tag?target.substring(1):target);
        return p->{
            if(!wildcard&&(tag?BuiltInRegistries.ITEM.getTag(TagKey.create(Registries.ITEM,id)).isEmpty():!BuiltInRegistries.ITEM.containsKey(id)))return UNAVAILABLE;
            return truth(slots.stream().map(p::getItemBySlot).anyMatch(stack->!stack.isEmpty()&&Selectors.item(target,stack)));
        };
    }
    public static Condition<ServerPlayer> skill(String path,String id,int minimum){
        if(minimum<1||minimum>100)throw new IllegalArgumentException("Invalid skill rank");
        return p->{
            var definition=PathRegistry.get(path);
            if(definition==null||definition.skill(id)==null)return UNAVAILABLE;
            return truth(ProgressionApi.track(p,path).skillRank(id)>=minimum);
        };
    }
    public static Condition<ServerPlayer> milestone(String path,String id){
        return p->{
            var definition=PathRegistry.get(path);
            if(definition==null||definition.milestones().stream().noneMatch(m->m.id().equals(id)))return UNAVAILABLE;
            return truth(ProgressionApi.track(p,path).completed(id));
        };
    }
}
