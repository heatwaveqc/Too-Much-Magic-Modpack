package io.github.heatwaveqc.tmm.economy;

import io.github.heatwaveqc.tmm.economy.converter.ManaConverters;
import io.github.heatwaveqc.tmm.economy.converter.SourceConverters;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

@Mod("tmm_economy")
public final class EconomyMod {
    public EconomyMod(IEventBus bus) {
        if (ModList.get().isLoaded("botania")) ManaConverters.register(bus);
        if (ModList.get().isLoaded("ars_nouveau")) SourceConverters.register(bus);
    }
}
