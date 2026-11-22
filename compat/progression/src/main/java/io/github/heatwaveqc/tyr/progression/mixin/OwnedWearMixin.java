package io.github.heatwaveqc.tyr.progression.mixin;
import io.github.heatwaveqc.tyr.progression.runtime.OwnedWear;
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
    // Mixin's @ModifyVariable captures the modified value, then the ORIGINAL target arguments.
    // Even though the original damage is not otherwise needed, its int slot is required
    // before ServerLevel. Without this slot, Mixin aborts during ItemStack transformation.
    private int tmm$ownedUnbreaking(int damage,int originalDamage,ServerLevel level,ServerPlayer player,Consumer<Item> onBreak){
        return OwnedWear.filter(player,level,(ItemStack)(Object)this,damage);
    }
}
