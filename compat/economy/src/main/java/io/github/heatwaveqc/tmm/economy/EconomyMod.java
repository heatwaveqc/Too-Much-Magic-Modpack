package io.github.heatwaveqc.tmm.economy;

import io.github.heatwaveqc.tmm.economy.audit.EmcCoverageCommands;
import io.github.heatwaveqc.tmm.economy.converter.ManaConverters;
import io.github.heatwaveqc.tmm.economy.converter.SourceConverters;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

@Mod("tmm_economy")
public final class EconomyMod {
    public EconomyMod(IEventBus bus) {
        // Native-only optional compatibility: no IS3 classes loaded when absent.
        if (io.github.heatwaveqc.tmm.economy.runtime.enchanting.EnchantingFeatureGate.enabled())
            io.github.heatwaveqc.tmm.economy.runtime.enchanting.ArcaneDiscriminationRegistration.register(bus);
        if (ModList.get().isLoaded("projecte")) EmcCoverageCommands.registerIfProjectEInstalled();
        if (ModList.get().isLoaded("botania")) ManaConverters.register(bus);
        if (ModList.get().isLoaded("ars_nouveau")) SourceConverters.register(bus);
        if (ModList.get().isLoaded("goety"))
            io.github.heatwaveqc.tmm.economy.compat.GoetySoulAbsorberCapability.register(bus);
    }
}
