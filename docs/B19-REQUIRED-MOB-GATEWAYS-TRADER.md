# B19 — Renewable progression-mob Gateways and wandering trader policy

**Status:** 14 source-verified Gateways JSONs + 14 native Gateways crafting recipes + en_us Gate Pearl names staged in an independent PR against test/full-integration. Gateways 1.21.1-5.1.0, Minecraft 1.21.1 NeoForge. **Static source work only; no in-game boot, entity registry, crafting, loot or wave test.**

## Scope

The user requests **at least one Gateway for every scarce/random-spawning mob required by a mod**: Ars Wilden monsters, Iron's necromancers and knights, and equivalent mobs in other mods. This PR implements the **first 14 encounters** with verified source entity IDs:
- Ars Nouveau: Wilden Hunter, Wilden Stalker, Wilden Guardian. These supply the normal Wilden Horn/Wing/Spike loop. **Do not replace** the normal Wilden Chimera summoning ritual with a farmable boss gate.
- Iron's Spells 'n Spellbooks: Necromancer, Pyromancer, Cryomancer, Archevoker, Magehunter Vindicator, Citadel Keeper (Ancient Knight), Apothecarist, Cultist, Cursed Armor Stand, Ice Spider and Catacombs Zombie.
- **Explicit exclusions:** Iron's Priest is a *neutral tradable support NPC*, not a natural hostile kill-farm target. Also exclude native bosses (Dead King, Fire Boss, Wilden Chimera), summoned minions, projectile effects and Wandering Trader.

This is **not yet exhaustive** for all magic mods: the machine-readable manual cohort in \`manifest/gateway_required_mobs.json\` should be expanded after checking source JAR loot tables, actual mob spawn paths, loot/recipe dependencies and the full loaded registry for Malum, Goety, Cataclysm, Forbidden & Arcanus, Occultism, Feywild, Neo Vitae, etc. Only add progression-required enemies; do not automatically add every mob or biome feature just because it is in the entity registry.

Sources: https://github.com/Shadows-of-Fire/GatewaysToEternity/tree/1.21/schema ; https://github.com/baileyholl/Ars-Nouveau/blob/1.21.x/src/main/java/com/hollingsworth/arsnouveau/common/lib/LibEntityNames.java ; https://github.com/iron431/irons-spells-n-spellbooks/blob/1.21/src/main/java/io/redspace/ironsspellbooks/registries/EntityRegistry.java . Verify against pinned Ars 5.13.5 and Iron's 3.16.3 registry, not only current upstream branches.

## Gameplay implementation

Each \`tmm:mob_<mod>_<entity>\` gateway is registered as \`data/tmm/gateways/mob_*.json\` and each Gate Pearl as \`data/tmm/recipe/gateways/mob_*.json\`. \`gateways:gate_recipe\` uses an **Ender Pearl**, **two Iron Ingots**, one magical focus (**Ars Source Gem** or **Iron's Arcane Essence**), and a **unique vanilla catalyst** associated with that target. All are native, statically indexed data files.

- Three timed waves; counts 1, 2 and 3 for common mobs; 1, 1 and 1 for the elite Citadel Keeper.
- No extra entity-loot rewards (use normal mob death drops, subject to in-world testing), no explosions or block-damage on failure; cleanup on failure; enforced separation between nearby gateways, and failures when no player remains.
- **Vanilla XP only** on completion (40 normal, 100 elite), not custom Monk/Hunter or magic progression XP.
- Gates support spells and player-summoned minions, so \`player_damage_only\` stays at default false. No spawn egg or treasure bypass for unique boss mobs.

Generator: \`python3 scripts/build_progression_gateways.py\` (source match audit), \`--write\` to regenerate after catalog changes, then packwiz refresh/update. Static validator: \`python3 scripts/validate_b19_gateways.py\`. Confirm all named gate pearls in JEI and original Gateways' gate recipe codec in actual installed mod.

## Wandering Trader — two actual choices

**Disable vanilla random trader spawning:** run \`/gamerule doTraderSpawning false\` as operator on Minecraft 1.21.1. This world-local game rule does not disable other mobs, remove pre-existing wandering traders, or block commands/eggs and other mods' custom spawning. To *completely* eliminate all traders, a separate server-side entity-join veto and existing-entity cleanup is required; it should not be applied merely to suppress vanilla natural spawns.

**Upgrade merchant (recommended for this magic-focused pack):** Iron's Spells already registers \`WandererTradesEvent\` at low event priority, adding random spell scrolls, ink buyback/sales, curios, rare Lost Knowledge Fragments and more when its \`ADDITIONAL_WANDERING_TRADER_TRADES\` config flag is true. That code confirms 5 generic + 1 rare trade slot at a time, so adding a large extra trade pool can make reliable access to needed products *worse*. Better solution is to guarantee **some** targeted trades via NeoForge custom event/merchant logic, preserve existing magical trades, and exclude boss trophies, Nether Stars, Chaos Shards, ProjectE stones and other progression gates. Treat rare biome and early renewable resource purchases as primary targets.

Sources: https://github.com/iron431/irons-spells-n-spellbooks/blob/1.21/src/main/java/io/redspace/ironsspellbooks/player/AdditionalWanderingTrades.java ; https://aldak.netlify.app/javadoc/1.21.1-21.1.x/net/neoforged/neoforge/event/village/WandererTradesEvent ; https://minecraft.wiki/w/Gamerule .

**Current decision:** leave the gamerule and trader trades unchanged while the user considers disable versus upgrade. Do not flip off the trader in the integration source tree just because disabling is technically possible.

## B19 QA gates

1. Dedicated-server boot and client startup with all generated Gateways/recipe/lang files; 14 gate pearls present in JEI/creative search; recipes craft exactly their expected gateway pearl and resolve correct entity.
2. Verify all 14 entity types exist in the *pinned* complete mod registry, reject missing IDs and fix names/catalysts, then test each combat/AI behavior. The source registry may evolve between versions.
3. Test Wilden Horn, Wing and Spike obtained by actually killing these enemies; verify Iron's mob loot/drop compatibility and XP, including any item requiring special spawn NBT, dimension or loot context.
4. Ensure no special boss, neutral priest, summon-only entity, or required worldgen discovery is shortcut by the gate.
5. Benchmark wave spawning and tick time with consecutive and concurrent gates on disposable test world; verify cleanup and no drops/XP dupes on aborts, despawn, chunk unload or failed gates.
6. Record remaining progression-required hostiles across *all other magic mods* and add at least one gated encounter per verified required random-spawn mob. Complete this sweep before calling the user's **all required mobs** design goal satisfied.
