package io.github.heatwaveqc.tyr.progression.runtime.clothing;

import io.github.heatwaveqc.tyr.progression.foundation.TrainingWeight;
import io.github.heatwaveqc.tyr.progression.runtime.ClothingApi;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/** One martial garment and one tagged lead unit, shapeless. Only upward
 * upgrades; no downgrade/refund or rewriting palette/custom-name data. */
public final class WeightGarmentRecipe extends CustomRecipe {
    private static final TagKey<Item> NUGGET=lead("nuggets/lead"), INGOT=lead("ingots/lead"), BLOCK=lead("storage_blocks/lead");
    private static TagKey<Item> lead(String id){return TagKey.create(Registries.ITEM,ResourceLocation.fromNamespaceAndPath("c",id));}
    public WeightGarmentRecipe(CraftingBookCategory category){super(category);}
    private record Match(ItemStack garment,TrainingWeight tier){}
    private static Match match(CraftingInput input){
        ItemStack garment=ItemStack.EMPTY;
        TrainingWeight tier=TrainingWeight.NORMAL;
        for(int i=0;i<input.size();i++){
            ItemStack stack=input.getItem(i);
            if(stack.isEmpty())continue;
            if(ClothingItems.martialStyle(stack).isPresent()){
                if(!garment.isEmpty()||!ClothingApi.isNonProtective(stack))return null;
                garment=stack;
            }else{
                if(tier!=TrainingWeight.NORMAL)return null;
                int count=(stack.is(NUGGET)?1:0)+(stack.is(INGOT)?1:0)+(stack.is(BLOCK)?1:0);
                if(count!=1)return null;
                tier=stack.is(BLOCK)?TrainingWeight.EXTREME:stack.is(INGOT)?TrainingWeight.HEAVY:TrainingWeight.LIGHT;
            }
        }
        if(garment.isEmpty()||!WeightedClothing.tier(garment).canUpgradeTo(tier))return null;
        return new Match(garment,tier);
    }
    @Override public boolean matches(CraftingInput input,Level level){return match(input)!=null;}
    @Override public boolean canCraftInDimensions(int w,int h){return w*h>=2;}
    @Override public ItemStack assemble(CraftingInput input,HolderLookup.Provider provider){
        Match match=match(input); if(match==null)return ItemStack.EMPTY;
        ItemStack result=match.garment().copyWithCount(1);
        result.set(ClothingComponents.TRAINING_WEIGHT.get(),match.tier());
        return result;
    }
    @Override public RecipeSerializer<?> getSerializer(){return ClothingRecipeSerializers.GARMENT_WEIGHT.get();}
}
