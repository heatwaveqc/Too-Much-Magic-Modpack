package io.github.heatwaveqc.tmm.progression.mixin;
import io.github.heatwaveqc.tmm.progression.runtime.GildedMechanics;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/** Port of first-party GildedPiglinMixin; effect ownership, not path name. */
@Mixin(PiglinAi.class)
public abstract class OwnedPiglinMixin {
    @Inject(method="angerNearbyPiglins",at=@At("HEAD"),cancellable=true)
    private static void tmm$ignoreAlarm(Player player,boolean requiresSight,CallbackInfo ci){
        if(player instanceof ServerPlayer p&&GildedMechanics.piglinKin(p,true))ci.cancel();
    }
    @Inject(method="isWearingGold",at=@At("HEAD"),cancellable=true)
    private static void tmm$kinship(LivingEntity target,CallbackInfoReturnable<Boolean> cir){
        if(target instanceof ServerPlayer p&&GildedMechanics.piglinKin(p,false))cir.setReturnValue(true);
    }
}
