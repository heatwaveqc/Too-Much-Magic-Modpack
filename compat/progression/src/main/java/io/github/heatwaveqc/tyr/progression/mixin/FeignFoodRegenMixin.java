package io.github.heatwaveqc.tyr.progression.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.heatwaveqc.tyr.progression.runtime.FeignDeath;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Stop hunger-driven regeneration before it spends food; starvation still runs. */
@Mixin(FoodData.class)
public abstract class FeignFoodRegenMixin {
    @WrapOperation(method="tick",at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/player/Player;isHurt()Z"))
    private boolean tmm$noRegen(Player player,Operation<Boolean> original){
        return !(player instanceof ServerPlayer p&&FeignDeath.active(p))&&original.call(player);
    }
}
