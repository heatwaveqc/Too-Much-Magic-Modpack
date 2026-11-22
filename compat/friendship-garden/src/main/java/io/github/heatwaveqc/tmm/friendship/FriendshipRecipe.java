package io.github.heatwaveqc.tmm.friendship;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/** 1 bone meal + 1 rotten flesh + any eligible decorative flower, shapeless. */
public final class FriendshipRecipe extends CustomRecipe {
    public FriendshipRecipe(CraftingBookCategory category) { super(category); }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return findOrigin(input) != null;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        Block flower = findOrigin(input);
        if (flower == null) return ItemStack.EMPTY;
        boolean tall = flower instanceof DoublePlantBlock
                || flower.defaultBlockState().hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF);
        ItemStack result = new ItemStack(tall ? FriendshipGardenMod.TALL_ITEM.get()
                                            : FriendshipGardenMod.SHORT_ITEM.get());
        FriendshipData.setOrigin(result, BuiltInRegistries.BLOCK.getKey(flower), tall);
        // A recipe without a real player (automation) is unbound and cannot be placed.
        // Only Item#onCraftedBy attributes the actual crafting player.
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FriendshipGardenMod.FLOWER_RECIPE.get();
    }

    private static Block findOrigin(CraftingInput input) {
        int bone = 0, flesh = 0;
        Block flower = null;
        int nonEmpty = 0;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            nonEmpty++;
            if (stack.is(Items.BONE_MEAL)) bone++;
            else if (stack.is(Items.ROTTEN_FLESH)) flesh++;
            else if (stack.getItem() instanceof BlockItem blockItem
                    && eligible(blockItem.getBlock())) {
                if (flower != null) return null;
                flower = blockItem.getBlock();
            } else return null;
        }
        return nonEmpty == 3 && bone == 1 && flesh == 1 ? flower : null;
    }

    private static boolean eligible(Block flower) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(flower);
        String namespace = id.getNamespace();
        // Functional flowers must keep their original magic behavior.
        if (namespace.equals("botania") || namespace.equals("mythicbotany")
                || namespace.equals("extra_botany")) return false;
        return flower.defaultBlockState().is(BlockTags.FLOWERS)
                && !flower.defaultBlockState().is(FriendshipGardenMod.EXCLUDED_FLOWERS)
                && !id.toString().equals("minecraft:wither_rose");
    }
}
