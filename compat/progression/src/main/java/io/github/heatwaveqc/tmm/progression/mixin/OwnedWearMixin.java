package io.github.heatwaveqc.tmm.progression.mixin;
import io.github.heatwaveqc.tmm.progression.runtime.OwnedWear;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import java.util.function.Consumer;
/** Generalized from first-party GildedDurabilityMixin; native wear still runs once. */
@Mixin(ItemStack.class)
public abstract class OwnedWearMixin {
    @ModifyVariable(method="hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V",at=@At("HEAD"),argsOnly=true,ordinal=0)
    private int tmm$ownedUnbreaking(int damage,ServerLevel level,ServerPlayer player,Consumer<Item> onBreak){
        return OwnedWear.filter(player,level,(ItemStack)(Object)this,damage);
    }
}
