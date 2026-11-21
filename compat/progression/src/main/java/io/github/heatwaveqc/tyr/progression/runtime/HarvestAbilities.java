package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import java.util.List;

/** Post-break loot replacement for explicitly unlocked, barehand-only harvesting techniques. */
public final class HarvestAbilities {
    private HarvestAbilities() {}
    private static boolean has(ServerPlayer p,String type,BlockState state) {
        for (var a : EffectRuntime.active(p))
            if (a.effect().type().equals(type) && Selectors.block(a.effect().selector(),state)) return true;
        return false;
    }

    // Shared eligibility for the actual Fortune loot formula and Silk Touch reroll.
    static boolean canHarvestBarehand(ServerPlayer player, BlockState state) {
        int required = EffectRuntime.requiredTier(state);
        if (required < 0) return false;
        for (var a : EffectRuntime.active(player)) {
            var fx = a.effect();
            if (fx.type().equals("harvest_tier") && Selectors.block(fx.selector(),state)
                    && Math.min(fx.tier(),a.rank()) >= required) return true;
        }
        // Blocks requiring no special tool can still be hand-harvested.
        return required == 0;
    }

    public static void blockDrops(ServerPlayer player,BlockDropsEvent event) {
        if (player.isCreative() || player.isSpectator() || event.isCanceled()
                || !player.getMainHandItem().isEmpty() || !event.getTool().isEmpty()
                || event.getBlockEntity() != null) return;
        BlockState state = event.getState();
        BlockPos pos = event.getPos();
        if (state.getDestroySpeed(event.getLevel(),pos) < 0
                || !canHarvestBarehand(player,state)) return;
        // Fortune is now an effective enchantment level in the original loot
        // computation (MonkFortuneMixin), not a second drop roll here.
        // Silk Touch changes the selected loot table branch and is still
        // rerolled using a synthetic tool until a separate context hook exists.
        if (has(player,"treasure_hand",state)
                && "silk".equals(ProgressionApi.mode(player,
                    "tyr_progression:treasure_mode","fortune"))) {
            ItemStack contextTool = new ItemStack(Items.NETHERITE_PICKAXE);
            var enchants = event.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            contextTool.enchant(enchants.getOrThrow(Enchantments.SILK_TOUCH), 1);
            List<ItemStack> rolled = Block.getDrops(state,event.getLevel(),pos,
                    null,player,contextTool);
            // Intentional replacement ONLY for Silk mode. Never append to native drops.
            event.getDrops().clear();
            for (ItemStack stack : rolled) if (!stack.isEmpty())
                event.getDrops().add(new ItemEntity(event.getLevel(),
                    pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,stack));
            return;
        }
        if (has(player,"glass_touch",state) &&
                (state.is(Blocks.GLASS) || state.is(Blocks.GLASS_PANE)
                    || state.getBlock() instanceof StainedGlassBlock
                    || state.getBlock() instanceof StainedGlassPaneBlock)) {
                event.getDrops().clear();
                event.getDrops().add(new ItemEntity(event.getLevel(),
                    pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,
                    new ItemStack(state.getBlock())));
            }
    }
}
