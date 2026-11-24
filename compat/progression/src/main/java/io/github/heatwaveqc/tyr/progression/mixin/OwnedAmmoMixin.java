package io.github.heatwaveqc.tyr.progression.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.heatwaveqc.tyr.progression.runtime.EffectRuntime;
import io.github.heatwaveqc.tyr.progression.runtime.NewbieMode;
import io.github.heatwaveqc.tyr.progression.data.MechanicalSpec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Native ammo debit (crossbows load before firing). Never refunds an item or creates a pickup. */
@Mixin(ProjectileWeaponItem.class)
public abstract class OwnedAmmoMixin {
    @ModifyExpressionValue(method="useAmmo",at=@At(value="INVOKE",
        target="Lnet/minecraft/world/item/enchantment/EnchantmentHelper;processAmmoUse(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)I"))
    private static int tmm$conserve(int amount,@Local(argsOnly=true,ordinal=0) ItemStack weapon,
        @Local(argsOnly=true,ordinal=1) ItemStack ammo,@Local(argsOnly=true) LivingEntity shooter){
        if(amount!=1||!(shooter instanceof ServerPlayer p)||!p.isAlive()||p.isCreative()||p.isSpectator()||NewbieMode.isNewbie(p)
            ||!weapon.is(Items.CROSSBOW)||!ammo.is(Items.ARROW)||ammo.isEmpty()
            ||!ItemStack.isSameItemSameComponents(ammo,new ItemStack(Items.ARROW))
            ||(p.getMainHandItem()!=weapon&&p.getOffhandItem()!=weapon))return amount;
        double chance=0;
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();
            if(fx.type().equals("ammo_conservation"))chance+=MechanicalSpec.scaled(fx.amountPerRank(),a.rank(),.5);
        }
        // A zero native debit uses vanilla's non-recoverable/intangible ammunition path.
        return chance>0&&p.getRandom().nextDouble()<Math.min(.5,chance)?0:amount;
    }
}
