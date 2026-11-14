package io.github.heatwaveqc.tmm.monk;

import io.github.heatwaveqc.tmm.monk.config.MonkStartupConfig;
import io.github.heatwaveqc.tmm.monk.command.MonkAttributeInventoryCommand;
import io.github.heatwaveqc.tmm.monk.faction.MonkFaction;
import io.github.heatwaveqc.tmm.monk.faction.MonkPlayerData;
import io.github.heatwaveqc.tmm.monk.gameplay.SunsetInitiation;
import io.github.heatwaveqc.tmm.monk.gameplay.StepAssistEffects;
import io.github.heatwaveqc.tmm.monk.skills.MonkSkills;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Monk addon entrypoint for the pinned Vampirism 1.10.x API.
 *
 * First Sunset initiation and the single reversible Step Assist root power
 * are active. Universal Tyz/MXP and other Monk techniques live elsewhere.
 */
@Mod(MonkMod.MOD_ID)
public final class MonkMod {
    public static final String MOD_ID = "tmm_monk";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public MonkMod(IEventBus modBus, ModContainer container) {
        // Faction registration happens in RegisterEvent, before COMMON/SERVER configs.
        // STARTUP values are available as soon as they are registered; clients and
        // dedicated servers must carry matching settings for this registry field.
        container.registerConfig(ModConfig.Type.STARTUP, MonkStartupConfig.SPEC);
        MonkPlayerData.ATTACHMENTS.register(modBus);
        MonkSkills.SKILLS.register(modBus);
        // Pattern from Werewolves 2.0.x: register after Vampirism's constructor
        // but before Vampirism closes faction registrations at IMC processing.
        modBus.addListener(this::registerFaction);
        NeoForge.EVENT_BUS.addListener(MonkAttributeInventoryCommand::register);
        NeoForge.EVENT_BUS.addListener(SunsetInitiation::onPlayerTick);
        NeoForge.EVENT_BUS.addListener(SunsetInitiation::onFactionChangePre);
        NeoForge.EVENT_BUS.addListener(SunsetInitiation::onLogout);
        NeoForge.EVENT_BUS.addListener(SunsetInitiation::onDimensionChange);
        NeoForge.EVENT_BUS.addListener(StepAssistEffects::onPlayerTick);
        NeoForge.EVENT_BUS.addListener(StepAssistEffects::onFactionChanged);
        NeoForge.EVENT_BUS.addListener(StepAssistEffects::onLogout);
        LOGGER.info("Monk First Sunset and Step Assist prototype registered (external physiology bridge pending).");
    }

    private void registerFaction(RegisterEvent event) {
        MonkFaction.registerOnce();
    }
}
