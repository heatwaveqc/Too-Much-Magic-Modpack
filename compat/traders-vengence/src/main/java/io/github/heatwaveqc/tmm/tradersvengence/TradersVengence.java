package io.github.heatwaveqc.tmm.tradersvengence;

import io.github.heatwaveqc.tmm.tradersvengence.command.VendettaCommands;
import io.github.heatwaveqc.tmm.tradersvengence.runtime.VendettaEvents;
import io.github.heatwaveqc.tmm.tradersvengence.runtime.NpcPistolSounds;
import io.github.heatwaveqc.tmm.tradersvengence.runtime.GolemMinigunEvents;
import io.github.heatwaveqc.tmm.tradersvengence.network.GolemVisualPayload;
import io.github.heatwaveqc.tmm.tradersvengence.runtime.HeroPardonEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Isolated server-authoritative mod; no changes to packwiz or the active test build. */
@Mod(TradersVengence.MOD_ID)
public final class TradersVengence {
    public static final String MOD_ID = "traders_vengence";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public TradersVengence(IEventBus modBus) {
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
