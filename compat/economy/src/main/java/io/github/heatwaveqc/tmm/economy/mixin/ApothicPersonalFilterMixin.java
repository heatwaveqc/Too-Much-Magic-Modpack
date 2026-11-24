package io.github.heatwaveqc.tmm.economy.mixin;

import dev.shadowsoffire.apothic_enchanting.table.ApothEnchantmentMenu;
import dev.shadowsoffire.apothic_enchanting.table.EnchantmentTableStats;
import io.github.heatwaveqc.tmm.economy.runtime.PersonalEnchantFilterService;
import io.github.heatwaveqc.tmm.economy.runtime.enchanting.ApothicPersonalFilter;
import io.github.heatwaveqc.tmm.economy.runtime.enchanting.PersonalEnchantFilter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Exact Apothic 1.6.4 ABI: both normal and Raven use the private superclass
 * getEnchantmentList during preview and commit. Pass a local stats record to
 * the native selection helper, never mutate physical shelf stats.
 *
 * Deliberately NOT listed in the active mixin manifest until the native
 * ability/menu, optional-mod load and preview/commit integration are verified.
 */
@Pseudo
@Mixin(targets = "dev.shadowsoffire.apothic_enchanting.table.ApothEnchantmentMenu", remap = false)
public abstract class ApothicPersonalFilterMixin {
    @Shadow @Final protected Player player;
    @Unique private PersonalEnchantFilter tmm$personalPreview;

    @ModifyArg(method = "getEnchantmentList(Lnet/minecraft/world/item/ItemStack;II)Ljava/util/List;",
        at = @At(value = "INVOKE", target = "Ldev/shadowsoffire/apothic_enchanting/table/ApothEnchantmentHelper;selectEnchantment(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/item/ItemStack;ILdev/shadowsoffire/apothic_enchanting/table/EnchantmentTableStats;Lnet/minecraft/core/HolderLookup$RegistryLookup;)Ljava/util/List;"),
        index = 3, require = 1, remap = false)
    private EnchantmentTableStats tmm$personalBlacklist(EnchantmentTableStats stats) {
        return player instanceof ServerPlayer owner ? ApothicPersonalFilter.forRandomRoll(owner, stats) : stats;
    }

    @Inject(method = "slotsChanged(Lnet/minecraft/world/Container;)V", at = @At("RETURN"), require = 1, remap = false)
    private void tmm$rememberPersonalPreview(Container changed, CallbackInfo ci) {
        if (player instanceof ServerPlayer owner) tmm$personalPreview = PersonalEnchantFilterService.snapshot(owner);
    }

    @Inject(method = "clickMenuButton(Lnet/minecraft/world/entity/player/Player;I)Z",
        at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private void tmm$rejectStalePersonalPreview(Player clicker, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!(player instanceof ServerPlayer owner) || clicker != owner || button < 0 || button > 2) return;
        PersonalEnchantFilter now = PersonalEnchantFilterService.snapshot(owner);
        PersonalEnchantFilter previous = tmm$personalPreview == null ? PersonalEnchantFilter.locked() : tmm$personalPreview;
        // Leave the native path untouched if neither preview nor commit uses the ability.
        if (!now.unlocked() && !previous.unlocked()) return;
        if (!now.equals(previous)) {
            ApothEnchantmentMenu menu = (ApothEnchantmentMenu) (Object) this;
            menu.slotsChanged(menu.getSlot(0).container);
            menu.broadcastChanges();
            cir.setReturnValue(false);
        }
    }
}
