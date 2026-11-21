package io.github.heatwaveqc.traders.vengeance;

import io.github.heatwaveqc.traders.vengeance.command.VendettaCommands;
import io.github.heatwaveqc.traders.vengeance.runtime.VendettaEvents;
import io.github.heatwaveqc.traders.vengeance.runtime.NpcPistolSounds;
import io.github.heatwaveqc.traders.vengeance.runtime.GolemMinigunEvents;
import io.github.heatwaveqc.traders.vengeance.network.GolemVisualPayload;
import io.github.heatwaveqc.traders.vengeance.runtime.HeroPardonEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Isolated server-authoritative mod; no changes to packwiz or the active test build. */
@Mod(TradersVengeance.MOD_ID)
public final class TradersVengeance {
    public static final String MOD_ID = "traders_vengeance";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public TradersVengeance(IEventBus modBus) {
        NpcPistolSounds.register(modBus);
        NeoForge.EVENT_BUS.addListener(VendettaEvents::onDeath);
        NeoForge.EVENT_BUS.addListener(VendettaEvents::onDefenderHurt);
        NeoForge.EVENT_BUS.addListener(VendettaEvents::onPriestAggro);
        NeoForge.EVENT_BUS.addListener(HeroPardonEvents::onHeroAwarded);
        NeoForge.EVENT_BUS.addListener(VendettaEvents::onJoin);
        NeoForge.EVENT_BUS.addListener(GolemMinigunEvents::onTick);
        modBus.addListener(GolemVisualPayload::register);
        NeoForge.EVENT_BUS.addListener(VendettaCommands::register);
    }
}
