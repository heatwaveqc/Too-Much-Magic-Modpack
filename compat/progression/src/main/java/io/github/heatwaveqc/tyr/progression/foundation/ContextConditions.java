package io.github.heatwaveqc.tyr.progression.foundation;

import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.runtime.Selectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import java.util.List;
import java.util.Set;
import static io.github.heatwaveqc.tyr.progression.foundation.Condition.Result.*;

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
    public static Condition<ServerPlayer> moon(int phase){
        if(phase<0||phase>7)throw new IllegalArgumentException("Invalid moon phase");
        return p->p.level().dimensionType().natural()?truth(p.level().getMoonPhase()==phase):UNAVAILABLE;
    }
    /** Per-predicate short cache; unloaded chunks never scanned or forced. */
    public static Condition<ServerPlayer> nearbyBlock(String target,int radius,int minimum){
        if(!target.matches("#?[a-z0-9_.-]+:[a-z0-9_./-]+")||radius<1||radius>4||minimum<1||minimum>128)
            throw new IllegalArgumentException("Unsafe nearby-block condition");
        boolean tag=target.startsWith("#");
        var id=ResourceLocation.parse(tag?target.substring(1):target);
        record Sample(net.minecraft.world.level.Level level,net.minecraft.core.BlockPos pos,long tick,Condition.Result result){}
        var cache=new java.util.WeakHashMap<ServerPlayer,Sample>();
        return p->{
            if(tag?BuiltInRegistries.BLOCK.getTag(TagKey.create(Registries.BLOCK,id)).isEmpty():!BuiltInRegistries.BLOCK.containsKey(id))return UNAVAILABLE;
            var level=p.serverLevel();var center=p.blockPosition();long now=level.getGameTime();
            var old=cache.get(p);
            if(old!=null&&old.level()==level&&old.pos().equals(center)&&now>=old.tick()&&now-old.tick()<20)return old.result();
            int count=0;boolean incomplete=false;
            for(var pos:net.minecraft.core.BlockPos.betweenClosed(center.offset(-radius,-radius,-radius),center.offset(radius,radius,radius))){
                if(center.distSqr(pos)>radius*radius||level.isOutsideBuildHeight(pos))continue;
                if(!level.hasChunkAt(pos)){incomplete=true;continue;}
                if(Selectors.block(target,level.getBlockState(pos))&&++count>=minimum){cache.put(p,new Sample(level,center,now,PASS));return PASS;}
            }
            var result=incomplete?UNAVAILABLE:FAIL;cache.put(p,new Sample(level,center,now,result));return result;
        };
    }
    public static Condition<ServerPlayer> weather(String value){
        if(!Set.of("clear","rain","thunder").contains(value))throw new IllegalArgumentException("Unknown weather");
        return p->{
            boolean rain=p.level().isRainingAt(p.blockPosition());
            return truth(switch(value){case "clear"->!rain;case "rain"->rain;default->rain&&p.level().isThundering();});
        };
    }
    public static Condition<ServerPlayer> state(String name,boolean expected){
        if(!Set.of("sneaking","sprinting","underwater","in_lava","on_ground","sky_visible","empty_hand","empty_hands","curios_empty","unarmored").contains(name))
            throw new IllegalArgumentException("Unknown movement/environment state");
        return p->truth(expected==switch(name){
            case "sneaking"->p.isCrouching();
            case "sprinting"->p.isSprinting();
            case "underwater"->p.isUnderWater();
            case "in_lava"->p.isInLava();
            case "on_ground"->p.onGround();
            case "sky_visible"->p.level().canSeeSky(p.blockPosition().above());
            case "empty_hand"->p.getMainHandItem().isEmpty();
            case "empty_hands"->p.getMainHandItem().isEmpty()&&p.getOffhandItem().isEmpty();
            case "curios_empty"->emptyCurios(p);
            default->{boolean empty=true;for(var stack:p.getArmorSlots())if(!stack.isEmpty())empty=false;yield empty;}
        });
    }
    /** Player-owned equipment predicate; never requires the retired Tyz bridge. */
    private static boolean emptyCurios(ServerPlayer player) {
        if (!net.neoforged.fml.ModList.get().isLoaded("curios")) return true;
        try {
            Class<?> api = Class.forName("top.theillusivec4.curios.api.CuriosApi");
            Object raw = api.getMethod("getCuriosInventory",
                    net.minecraft.world.entity.LivingEntity.class).invoke(null, player);
            if (!(raw instanceof java.util.Optional<?> optional) || optional.isEmpty())
                return false;
            Object inventory = optional.get();
            Object rawMap = inventory.getClass().getMethod("getCurios").invoke(inventory);
            if (!(rawMap instanceof java.util.Map<?, ?> entries)) return false;
            for (Object slotHandler : entries.values()) {
                Object stacks = slotHandler.getClass().getMethod("getStacks").invoke(slotHandler);
                int count = ((Number) stacks.getClass().getMethod("getSlots").invoke(stacks)).intValue();
                for (int slot = 0; slot < count; slot++) {
                    Object stack = stacks.getClass().getMethod("getStackInSlot", int.class).invoke(stacks, slot);
                    if (stack instanceof net.minecraft.world.item.ItemStack item && !item.isEmpty())
                        return false;
                }
            }
            return true;
        } catch (ReflectiveOperationException | RuntimeException failure) {
            return false; // Never claim empty inventory after an unknown provider ABI.
        }
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
