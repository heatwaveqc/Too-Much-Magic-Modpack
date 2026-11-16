package io.github.heatwaveqc.tmm.tradersvengence.mixin;

import io.github.heatwaveqc.tmm.tradersvengence.runtime.MagicMissileBridge;
import io.github.heatwaveqc.tmm.tradersvengence.state.TraderBlacklist;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;

/** Make ordinary villagers eligible for the built-in RangedAttackGoal. */
@Mixin(Villager.class)
public abstract class VillagerRangedAttackMixin implements RangedAttackMob {
    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        Villager villager = (Villager) (Object) this;
        if (villager.level() instanceof ServerLevel level
            && target instanceof ServerPlayer player
            && TraderBlacklist.get(level).contains(player.getUUID())) {
            MagicMissileBridge.fire(villager, player);
        }
    }
}
