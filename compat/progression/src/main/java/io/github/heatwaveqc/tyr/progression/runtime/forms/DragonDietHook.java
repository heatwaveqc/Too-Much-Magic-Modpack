package io.github.heatwaveqc.tyr.progression.runtime.forms;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

/** Compatibility with DS 2.0.74 native food properties: restore only a permitted, real
 * food-backed beverage's base component. Non-food potions and provider-specific beverages
 * without a component remain native behavior. No external code is copied.
 */
public final class DragonDietHook {
 private DragonDietHook(){}
 private static final ThreadLocal<Boolean> REENTRANT=ThreadLocal.withInitial(()->false);
 public static FoodProperties restore(ItemStack stack,LivingEntity entity,FoodProperties nativeResult){
    if(entity==null||stack.isEmpty()||!ModList.get().getModContainerById("dragonsurvival")
        .filter(mod->mod.getModInfo().getVersion().toString().startsWith("2.0.74")).isPresent())return nativeResult;
    FoodProperties base=stack.get(DataComponents.FOOD);
    if(base==null||REENTRANT.get())return nativeResult;
    REENTRANT.set(true);
    try{
    boolean allow=entity instanceof ServerPlayer player?DietPermissions.dragonDrink(player,stack):
        entity.level().isClientSide&&io.github.heatwaveqc.tyr.progression.client.ClientDiet.allows(entity,stack);
    return allow?base:nativeResult;
    }finally{REENTRANT.remove();}
 }
}
