package io.github.heatwaveqc.tmm.tradersvengence.mixin;

import io.github.heatwaveqc.tmm.tradersvengence.runtime.VendettaEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Listen to vanilla's existing village reputation updates instead of
 * re-interpreting hits on villagers in a separate damage pipeline.
 * Other vanilla gossip and trade pricing remain untouched.
 */
@Mixin(Villager.class)
public abstract class VillagerReputationMixin {
    @Inject(method = "onReputationEventFrom", at = @At("TAIL"))
    private void tradersVengence$recordCrime(ReputationEventType eventType,
                                            Entity culprit, CallbackInfo ci) {
        if (eventType != ReputationEventType.VILLAGER_HURT
            && eventType != ReputationEventType.VILLAGER_KILLED) {
            return;
        }
        Villager villager = (Villager) (Object) this;
        if (villager.level() instanceof ServerLevel level
            && culprit instanceof ServerPlayer player) {
            VendettaEvents.accuse(level, player,
                eventType == ReputationEventType.VILLAGER_HURT
                    ? "hurt a villager (vanilla reputation)"
                    : "killed a villager (vanilla reputation)");
        }
    }
}
