package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.Signal;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Set;
import java.util.stream.Collectors;

/** Common exact-ID/tag predicates used by both rewards and gameplay effects. */
public final class Selectors {
    private Selectors() {}
    private static ResourceLocation location(String value) {
        return ResourceLocation.parse(value.startsWith("#") ? value.substring(1) : value);
    }
    public static boolean block(String selector, BlockState state) {
        if (selector.isEmpty() || selector.equals("*")) return true;
        return selector.startsWith("#") ?
            state.is(TagKey.create(Registries.BLOCK,location(selector))) :
            BuiltInRegistries.BLOCK.getKey(state.getBlock()).equals(location(selector));
    }
    public static boolean entity(String selector, LivingEntity target) {
        if (selector.isEmpty() || selector.equals("*")) return true;
        // JAWR lycanthropy belongs to the player attachment, never all players.
        if (selector.equals("#tyr_progression:lycanthropes")
                && target instanceof net.minecraft.world.entity.player.Player)
            return jawrLycanthrope(target);
        return selector.startsWith("#") ?
            target.getType().is(TagKey.create(Registries.ENTITY_TYPE,location(selector))) :
            BuiltInRegistries.ENTITY_TYPE.getKey(target.getType()).equals(location(selector));
    }
    private static boolean jawrLycanthrope(LivingEntity target) {
        if (!net.neoforged.fml.ModList.get().isLoaded("witchery")) return false;
        try {
            Class<?> attachment = Class.forName(
                "dev.sterner.witchery.feature.affliction.werewolf.WerewolfPlayerAttachment");
            Object data = attachment.getMethod("getData",
                net.minecraft.world.entity.player.Player.class).invoke(null,target);
            return data != null && Boolean.TRUE.equals(
                data.getClass().getMethod("isWerewolf").invoke(data));
        } catch (ReflectiveOperationException | LinkageError ex) {
            return false;
        }
    }
    public static boolean item(String selector, ItemStack stack) {
        if (selector.isEmpty() || selector.equals("*")) return true;
        return selector.startsWith("#") ?
            stack.is(TagKey.create(Registries.ITEM,location(selector))) :
            BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(location(selector));
    }
    public static Signal blockBreak(BlockState state) {
        Set<String> tags = state.getTags().map(tag -> tag.location().toString())
            .collect(Collectors.toSet());
        return new Signal("block_break",
            BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString(), tags);
    }
    /** Credited post-break physical practice, excluding tools and pre-break attempts. */
    public static Signal barehandBlockBreak(BlockState state) {
        Signal original = blockBreak(state);
        return new Signal("barehand_block_break",original.target(),original.tags());
    }
    /** Credited direct empty-hand melee kill. */
    public static Signal unarmedKill(LivingEntity target) {
        Signal original = kill(target);
        return new Signal("unarmed_kill",original.target(),original.tags());
    }
    public static Signal itemAction(String event, ItemStack stack) {
        Set<String> tags = stack.getTags().map(tag -> tag.location().toString())
            .collect(Collectors.toSet());
        return new Signal(event,BuiltInRegistries.ITEM.getKey(stack.getItem()).toString(),tags);
    }
    public static Signal entityAction(String event, LivingEntity entity) {
        Set<String> tags = entity.getType().getTags().map(tag -> tag.location().toString())
            .collect(Collectors.toSet());
        return new Signal(event,BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString(),tags);
    }
    public static Signal kill(LivingEntity target) {
        Set<String> tags = target.getType().getTags().map(tag -> tag.location().toString())
            .collect(Collectors.toSet());
        return new Signal("kill",
            BuiltInRegistries.ENTITY_TYPE.getKey(target.getType()).toString(), tags);
    }
}
