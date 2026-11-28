package io.github.heatwaveqc.tmm.economy.mixin;

import dev.ftb.mods.ftblibrary.ui.Panel;
import io.github.heatwaveqc.tmm.economy.client.AdventurersShopButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Add an ordinary FTB tab button before the native toolbar layout pass. */
@Pseudo
@Mixin(targets = "dev.ftb.mods.ftbquests.client.gui.quests.OtherButtonsPanelTop", remap = false)
public abstract class QuestShopToolbarMixin {
    @Inject(method = "addWidgets()V", at = @At("TAIL"), remap = false)
    private void tmm$addAdventurersShop(CallbackInfo ci) {
        Panel panel = (Panel) (Object) this;
        panel.add(new AdventurersShopButton(panel));
    }
}
