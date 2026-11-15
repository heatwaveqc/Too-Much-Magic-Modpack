# Hunter Mutagenic Brew — Permanent Mutation Design and QA

**2026-11-15. Draft source work only.** PR #148 is stacked above PR #143, #141, #136 and #127. Do not integrate the compiled JAR or modify quests until this has gameplay QA.

## Potion-to-mutation loop

1. Craft one Bio-Mutagen from **two moss blocks, two spider eyes and one rotten flesh** in a shaped recipe (center row: Spider Eye, Rotten Flesh, Spider Eye; moss above and below).
2. Brew the Bio-Mutagen with an ordinary potion or compatible modded PotionItem in the Brewing Stand. The dynamic NeoForge brewing recipe copies the original standard PotionContents, including registered modded potion effects and custom effects, into a new drinkable Mutagenic Brew.
3. After Hunter initiation, drinking this special brew accumulates the source's **original positive duration** for **each** eligible effect, without granting the temporary potion effects. Ordinary potion drinking and Hunter potion XP are still separate mechanics. Mutagenic Brews grant mutation progress **instead** of ordinary potion XP.
4. Each effect and amplifier (zero-based) tracks its own duration. The target is **720,000 ticks = 36,000 seconds = 10 hours** per potency tier. There is **no minimum duration filter**: 1 tick contributes 1 tick. Effect duration of zero is not time; instantaneous effects such as Instant Health are not eligible for this passive mutation model.
5. A completed threshold unlocks a persistent effect. Higher amplifier tiers supersede lower tiers of the **same** effect and are not counted as extra mutations for metabolism. Unlimited distinct effects may be unlocked. The effect is periodically refreshed by the server and saved in a copy-on-death player attachment.
6. The ongoing cost is **0.0005 food exhaustion per tick per unlocked distinct mutation** by default. At 20 TPS, that is 0.01 exhaustion/s per mutation; 4 exhaustion drains a saturation/food point, approximately one point every 400 seconds or 1.5 points per 10 minutes **per mutation**, in addition to normal activity. No aggregate mutation count ceiling.

For scale: a three-minute potion requires **200** contributing brews to reach ten hours; an eight-minute potion requires **75**. This might be expensive but follows the requested endgame economy.

## Server-reloadable Hunter policy

The Hunter path JSON has a hunter_mutations object with target_duration_ticks (720000), exhaustion_per_mutation_tick (0.0005), maximum_amplifier (3; potency limit only, not mutation-count limit), and effect_blacklist (exact namespaced effect IDs). The initial blacklist includes Absorption, Saturation, instantaneous healing/harming, raid/omen reward effects, death-triggered spawn effects, and Tombstone Ghostly Shape. It can be edited by pack datapacks. Modded effects not blacklisted are accepted by default when they use standard PotionContents and have meaningful duration.

The brewing handler currently accepts PotionItem subclasses that expose standard POTION_CONTENTS; modded items with entirely independent potion metadata require adapters. We preserve the recognized base potion and custom effects rather than inventing guessed mod IDs. A brew with no eligible effects cannot be created. Custom multi-effect mixtures credit all eligible effects' original duration. Potency above maximum_amplifier is clamped to the configured cap.

**Important:** A potion-like instant heal has no meaningful permanent timed status and is excluded for now. It can get a deliberately designed alternative passive ability later, rather than infinite bursts of healing every tick.

## Everlasting Abilities and Pylons comparison

[Everlasting Abilities](https://modrinth.com/project/everlasting-abilities) has a NeoForge 1.21.1 release, requires Cyclops Core, and supplies persistent abilities via Ability Bottles / Totems, XP-based transfer and ranks. It has datapack controls, GUI and possible PvP ability loss. This is a useful reference for rank display and long-term persistence, but a poor direct replacement for an unlimited-effect ten-hour potion-absorption economy. We should **not automatically add it as a required mod**, and should check that standalone Everlasting players cannot stack duplicate power with Hunter mutations unchecked.

[Pylons](https://modrinth.com/mod/pylons) uses a Potion Filter to extract active effects, generally requiring 60 seconds of minimum active duration and an hour of total duration. This feature instead directly consumes the special potion's verified component data; the player never receives the source effect first.

## Important unverified release gates

- Compile/JUnit and standalone dedicated-server boot, plus real client brewing and drinking.
- Verify original potion effects never apply, inventory/bottle transaction correctness, data component preservation and recipe availability.
- Save/reload/respawn/death, amplifier upgrades, one-tick increments, ten-hour unlock, hunger per mutation.
- Test Pylons/Everlasting interactions and modded potions with custom item classes; fail closed on unsuitable effects.
- Confirm no hunger bypass via permanent Saturation, infinite food from modded effects, other mods' hunger-elimination mechanisms, or unauthorized data.
- Add a purge/cure or voluntary permanent mutation removal procedure before shipping, since unfavorable modded effects could otherwise make a player character unplayable.
- Decide whether future blacklisting of an already unlocked mutation should also remove metabolic upkeep (the current implementation suppresses the effect; state remains saved).
- Hunter still lacks a dedicated mutation GUI; /tmmprogress mutations currently gives a command summary.
