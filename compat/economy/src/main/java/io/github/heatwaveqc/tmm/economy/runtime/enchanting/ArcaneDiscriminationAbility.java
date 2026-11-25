package io.github.heatwaveqc.tmm.economy.runtime.enchanting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import org.zeith.improvableskills.api.registry.PlayerAbilityBase;

/** Native IS3 unlock and ability-book entry; no separate progression currency. */
public final class ArcaneDiscriminationAbility extends PlayerAbilityBase {
    public ArcaneDiscriminationAbility() {
        setColor(0x23889b);
        // Reference existing installed native art; no copied third-party texture.
        tex.texNorm = ResourceLocation.fromNamespaceAndPath("improvableskills", "textures/abilities/template_normal.png");
        tex.texHov = ResourceLocation.fromNamespaceAndPath("improvableskills", "textures/abilities/template_hovered.png");
    }

    @Override
    public void onClickClient(Player player, int button) {
        if (button == 0 && player.level().isClientSide)
            PacketDistributor.sendToServer(new ArcaneDiscriminationRegistration.Open());
    }
}
