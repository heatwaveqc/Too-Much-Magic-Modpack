#!/usr/bin/env python3
"""Apply a read-only registry log listener to a disposable CI build, not to repo commits."""
from pathlib import Path
root=Path(__file__).resolve().parents[1]
base=root/"compat/transformations/src/main/java/io/github/heatwaveqc/tmm/transformations"
mod=base/"TransformationsMod.java"
src=mod.read_text()
needle="        VampireFactionEvents.register();"
assert src.count(needle)==1, "Unexpected transformations bootstrap; refuse patch"
src=src.replace(needle,needle+"\n        NeoForge.EVENT_BUS.addListener(HunterCensusEvents::onStarted);")
mod.write_text(src)
target=base/"HunterCensusEvents.java"
assert not target.exists()
target.write_text("""package io.github.heatwaveqc.tmm.transformations;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

/** CI ONLY: enumerate actual server-registered entity types and tags, no spawning or mutation. */
public final class HunterCensusEvents {
    private HunterCensusEvents() {}

    public static void onStarted(ServerStartedEvent event) {
        int count = 0;
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(type);
            String tags = type.builtInRegistryHolder().tags()
                    .map(t -> t.location().toString()).sorted()
                    .reduce((a,b) -> a + "|" + b).orElse("");
            TransformationsMod.LOGGER.info("HUNTER_RUNTIME_ENTITY id={} category={} tags={}",
                    id, type.getCategory(), tags);
            count++;
        }
        TransformationsMod.LOGGER.info("HUNTER_RUNTIME_ENTITY_COUNT total={}", count);
    }
}
""")
print("CI-ONLY patch added census listener; no committed gameplay change.")
