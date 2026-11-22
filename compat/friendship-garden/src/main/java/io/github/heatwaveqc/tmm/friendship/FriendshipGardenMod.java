package io.github.heatwaveqc.tmm.friendship;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

@Mod(FriendshipGardenMod.MODID)
public final class FriendshipGardenMod {
    public static final String MODID = "tmm_friendship_garden";
    public static final TagKey<Block> EXCLUDED_FLOWERS = TagKey.create(Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(MODID, "excluded_flowers"));
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredBlock<FriendshipBlock> SHORT_BLOCK = BLOCKS.register("short_flower",
            () -> new FriendshipBlock(false));
    public static final DeferredBlock<FriendshipBlock> TALL_BLOCK = BLOCKS.register("tall_flower",
            () -> new FriendshipBlock(true));
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredItem<FriendshipFlowerItem> SHORT_ITEM = ITEMS.register("short_flower",
            () -> new FriendshipFlowerItem(SHORT_BLOCK.get(), new net.minecraft.world.item.Item.Properties().stacksTo(1)));
    public static final DeferredItem<FriendshipFlowerItem> TALL_ITEM = ITEMS.register("tall_flower",
            () -> new FriendshipFlowerItem(TALL_BLOCK.get(), new net.minecraft.world.item.Item.Properties().stacksTo(1)));
    public static final DeferredRegister<BlockEntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final Supplier<BlockEntityType<FriendshipBlockEntity>> FLOWER_ENTITY = ENTITIES.register(
            "friendship_flower", () -> BlockEntityType.Builder.of(FriendshipBlockEntity::new,
                    SHORT_BLOCK.get(), TALL_BLOCK.get()).build(null));
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, MODID);
    public static final Supplier<RecipeSerializer<FriendshipRecipe>> FLOWER_RECIPE =
            RECIPES.register("friendship_flower", () -> new SimpleCraftingRecipeSerializer<>(FriendshipRecipe::new));

    public FriendshipGardenMod(IEventBus bus, ModContainer container) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        ENTITIES.register(bus);
        RECIPES.register(bus);
        container.registerConfig(net.neoforged.fml.config.ModConfig.Type.SERVER,
                FriendshipConfig.SPEC, "tmm_friendship_garden-server.toml");
        NeoForge.EVENT_BUS.register(new FriendshipRuntime());
    }
}
