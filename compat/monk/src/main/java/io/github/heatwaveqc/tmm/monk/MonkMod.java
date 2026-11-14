package io.github.heatwaveqc.tmm.monk;

import io.github.heatwaveqc.tmm.monk.config.MonkStartupConfig;
import io.github.heatwaveqc.tmm.monk.command.MonkAttributeInventoryCommand;
import io.github.heatwaveqc.tmm.monk.command.MonkMasteryCommand;
import io.github.heatwaveqc.tmm.monk.faction.MonkFaction;
import io.github.heatwaveqc.tmm.monk.faction.MonkPlayerData;
import io.github.heatwaveqc.tmm.monk.gameplay.SunsetInitiation;
import io.github.heatwaveqc.tmm.monk.gameplay.StepAssistEffects;
import io.github.heatwaveqc.tmm.monk.gameplay.MonkTechniqueEffects;
import io.github.heatwaveqc.tmm.monk.progression.MonkMasteryEvents;
import de.teamlapen.vampirism.api.entity.player.skills.SkillPointProviders;
import io.github.heatwaveqc.tmm.monk.faction.IMonkPlayer;
import net.minecraft.resources.ResourceLocation;
import io.github.heatwaveqc.tmm.monk.skills.MonkSkills;
import io.github.heatwaveqc.tmm.monk.skills.MonkAttributes;
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
        MonkAttributes.ATTRIBUTES.register(modBus);
        modBus.addListener(MonkAttributes::addToPlayer);
        // One native faction SP for each earned cultivation rank AFTER initiation.
        // Tyz still owns its own MXP/SP wallet; this is only for Vampirism skills.
        SkillPointProviders.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, "faction_rank"),
                player -> player instanceof IMonkPlayer ? Math.max(0, player.getLevel() - 1) : 0);
        // Pattern from Werewolves 2.0.x: register after Vampirism's constructor
        // but before Vampirism closes faction registrations at IMC processing.
        modBus.addListener(this::registerFaction);
        NeoForge.EVENT_BUS.addListener(MonkAttributeInventoryCommand::register);
        NeoForge.EVENT_BUS.addListener(MonkMasteryCommand::register);
        NeoForge.EVENT_BUS.addListener(SunsetInitiation::onPlayerTick);
        NeoForge.EVENT_BUS.addListener(SunsetInitiation::onFactionChangePre);
        NeoForge.EVENT_BUS.addListener(SunsetInitiation::onLogout);
        NeoForge.EVENT_BUS.addListener(SunsetInitiation::onClone);
        NeoForge.EVENT_BUS.addListener(SunsetInitiation::onDimensionChange);
        NeoForge.EVENT_BUS.addListener(StepAssistEffects::onPlayerTick);
        NeoForge.EVENT_BUS.addListener(StepAssistEffects::onFactionChanged);
        NeoForge.EVENT_BUS.addListener(StepAssistEffects::onLogout);
        NeoForge.EVENT_BUS.addListener(MonkMasteryEvents::onBlockDrops);
        NeoForge.EVENT_BUS.addListener(MonkMasteryEvents::onHostileDeath);
        NeoForge.EVENT_BUS.addListener(MonkMasteryEvents::onFoodFinished);
        NeoForge.EVENT_BUS.addListener(MonkTechniqueEffects::onTick);
        NeoForge.EVENT_BUS.addListener(MonkTechniqueEffects::onLogout);
        LOGGER.info("Monk First Sunset, native ranks 1-4, initial skill tree and Step Assist registered (external physiology bridge pending).");
    }

    private void registerFaction(RegisterEvent event) {
        MonkFaction.registerOnce();
    }
}
