package io.github.heatwaveqc.tmm.monk;

import io.github.heatwaveqc.tmm.monk.config.MonkStartupConfig;
import io.github.heatwaveqc.tmm.monk.faction.MonkFaction;
import io.github.heatwaveqc.tmm.monk.faction.MonkPlayerData;
import io.github.heatwaveqc.tmm.monk.skills.MonkSkills;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Monk addon entrypoint for the pinned Vampirism 1.10.x API.
 *
 * No initiation, powers, experience boosts, equipment restrictions or
 * cross-mod exclusions are active in this bootstrap.
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
        LOGGER.warn("Monk development scaffold loaded: no powers or discipline restrictions are implemented.");
    }

    private void registerFaction(RegisterEvent event) {
        MonkFaction.registerOnce();
    }
}
