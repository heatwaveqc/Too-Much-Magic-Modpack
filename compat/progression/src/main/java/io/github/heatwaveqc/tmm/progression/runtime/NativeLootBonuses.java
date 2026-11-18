package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

/**
 * Provides virtual vanilla loot-enchantment levels at the actual formula site.
 * Does not alter held ItemStacks, generate entities, or run on player/entity ticks.
 */
public final class NativeLootBonuses {
    private NativeLootBonuses() {}

    public static int monkFortune(ServerPlayer player, LootContext context) {
        if (player.isCreative() || player.isSpectator()
                || context.getParamOrNull(LootContextParams.THIS_ENTITY) != player)
            return 0;

        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        BlockState block = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        // Only the player's actual empty fist; no doubling Fortune on equipped tools.
        if (tool == null || !tool.isEmpty() || block == null
                || !player.getMainHandItem().isEmpty())
            return 0;
        if (!ClassAbilities.unlocked(player, "treasure_hand")
                || !HarvestAbilities.canHarvestBarehand(player, block))
            return 0;

        return LootBonusMath.monkFortune(1, TyzMonkBridge.fortuneBonus(player),
                "fortune".equals(ProgressionApi.mode(player,
                        "tmm_progression:treasure_mode", "fortune")), true);
    }

    public static int hunterLooting(ServerPlayer player, LootContext context) {
        if (player.isCreative() || player.isSpectator()) return 0;
        if (!(context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof LivingEntity victim))
            return 0;
        DamageSource source = context.getParamOrNull(LootContextParams.DAMAGE_SOURCE);
        // The killer must really be this player. Projectile kills qualify;
        // spawners, simulated loot, other players and unrelated damage do not.
        if (source == null || source.getEntity() != player) return 0;
        return HunterExperience.favoredLootingBonus(player, victim);
    }
    /** Only the original loot formula is extended; no synthetic reroll. */
    public static int configuredFortune(ServerPlayer player,LootContext context){
        if(player.isCreative()||player.isSpectator()||NewbieMode.isNewbie(player)||
            context.getParamOrNull(LootContextParams.THIS_ENTITY)!=player)return 0;
        var tool=context.getParamOrNull(LootContextParams.TOOL);
        var block=context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if(tool==null||block==null||context.getParamOrNull(LootContextParams.BLOCK_ENTITY)!=null)return 0;
        // A fist cannot become a higher-tier harvester through Fortune alone.
        if(tool.isEmpty()&&!HarvestAbilities.canHarvestBarehand(player,block))return 0;
        double total=0;
        for(var a:EffectRuntime.active(player)){
            var fx=a.effect();
            if(fx.type().equals("virtual_fortune")&&(!fx.emptyHand()||tool.isEmpty())&&
                Selectors.block(fx.selector(),block)&&Selectors.item(fx.weapon(),tool))total+=fx.amountPerRank()*a.rank();
        }
        return io.github.heatwaveqc.tmm.progression.foundation.UtilityMath.virtualLevel(total);
    }
    public static int configuredLooting(ServerPlayer player,LootContext context){
        if(player.isCreative()||player.isSpectator()||NewbieMode.isNewbie(player)||
            !(context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof LivingEntity victim))return 0;
        var source=context.getParamOrNull(LootContextParams.DAMAGE_SOURCE);
        if(source==null||source.getEntity()!=player||victim instanceof net.minecraft.world.entity.player.Player)return 0;
        boolean direct=source.getDirectEntity()==player;
        var tool=direct?player.getMainHandItem():source.getDirectEntity() instanceof net.minecraft.world.entity.projectile.AbstractArrow arrow?arrow.getWeaponItem():null;
        if(tool==null)return 0;
        double total=0;
        for(var a:EffectRuntime.active(player)){
            var fx=a.effect();
            if(fx.type().equals("virtual_looting")&&(!fx.emptyHand()||direct&&tool.isEmpty())&&
                Selectors.entity(fx.selector(),victim)&&Selectors.item(fx.weapon(),tool)&&EffectRuntime.sourceMatches(fx.damageTag(),source))
                total+=fx.amountPerRank()*a.rank();
        }
        return io.github.heatwaveqc.tmm.progression.foundation.UtilityMath.virtualLevel(total);
    }
}
