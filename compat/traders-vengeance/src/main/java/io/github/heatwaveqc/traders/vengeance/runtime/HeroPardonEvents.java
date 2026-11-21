package io.github.heatwaveqc.traders.vengeance.runtime;

import io.github.heatwaveqc.traders.vengeance.TradersVengeance;
import io.github.heatwaveqc.traders.vengeance.state.TraderBlacklist;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

/**
 * A true village hero is forgiven. Pardon follows award/renewal of the status
 * effect, not its duration; ending the effect does not restore a blacklist.
 */
public final class HeroPardonEvents {
    private HeroPardonEvents() {}

    public static void onHeroAwarded(MobEffectEvent.Added event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
            || !(player.level() instanceof ServerLevel level)
            || !event.getEffectInstance().getEffect().equals(MobEffects.HERO_OF_THE_VILLAGE)) {
            return;
        }
        if (TraderBlacklist.get(level).pardon(player.getUUID())) {
            player.sendSystemMessage(Component.translatable("message.traders_vengeance.hero_pardon"));
            TradersVengeance.LOGGER.info("Player {} pardoned after earning Hero of the Village",
                player.getGameProfile().getName());
        }
    }
}
