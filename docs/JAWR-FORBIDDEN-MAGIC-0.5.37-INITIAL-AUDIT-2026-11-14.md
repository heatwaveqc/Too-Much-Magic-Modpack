# JAWR: Forbidden Magic — 0.5.37 initial abilities and skills investigation

**Date:** 2026-11-14 (America/Phoenix). **Pinned mod:** `witchery_forbidden_magic-0.5.37+mc1.21.1`, NeoForge 1.21.1. **Pinned parent:** `witchery-0.5.39+mc1.21.1` (Just Another Witchery Remake). **Status:** **Needs investigation — released feature descriptions verified, exact jar internals NOT inspected.** Do not extrapolate detailed attribute hooks, phylactery event priorities, death-state serialization, or minion AI from promotional text.

## Exact release facts and source limits

- [Forbidden Magic 0.5.37 CurseForge file, id 9426760](https://www.curseforge.com/minecraft/mc-mods/jawr-forbidden-magic/files/9426760), uploaded 2026-11-03, NeoForge 1.21.1, size 881 KB, version matches pack.
- [Modrinth official mod description](https://modrinth.com/mod/jawr-forbidden-magic) documents **ten-level lichdom**, quest/ability progression, **phylactery**, ritual of **petrification**, **soul severance** ritual, **Imp soul trades** and **necromancy infusion** (restores deceased villagers and zombies as fighters).
- [Base JAWR parent 0.5.39, id 9525978](https://www.curseforge.com/minecraft/mc-mods/just-another-witchery-remake/files/9525978) uploaded 2026-11-09, NeoForge 1.21.1, adds Lilith vampirism cure option and various fixes. Parent 0.5.37 changelog includes **"More syncing for lich, vamp, werewolf attributes"**; this establishes *some* player-state/attribute sync exists but **does not prove a conventional NeoForge attribute key, Pufferfish target, or public bridge**.
- Public GitHub repository searches for `witchery_forbidden_magic`, `JAWR Forbidden Magic`, `mrsterner Witchery` found a small third-party patch and wiki, **not the authoritative 0.5.37 source**. The addon is marked **All Rights Reserved**. Attempt to retrieve official compiled JAR in this environment did not succeed; no bytecode, assets/registry, or in-world run audited.
- [Independent registry catalogue](https://blocksitems.com/mods/jawr-forbidden-magic) lists `phylactery`, `soul_cage`, `soulstone_pendant`, `soul_in_a_bottle`, `necromantic_soulbind`, `brew_of_soul_severance`, `ancient_tablet`, `lifeblood_plant`. This is **secondary inventory data**; may reflect a different/latest version, so confirm IDs against pinned jar before quest registration.

## Player gameplay model, high-confidence

Unlike Enchanted/Enigmatic artifact systems, Forbidden Magic has an **actual named ten-level player lich progression**. The player embarks on soul-related quests and abilities and works toward phylactery-enabled lichdom. The addon also supplies unique witchery ritual recipes, imp soul commerce, and **necromancy infusion that can return dead villagers/zombies as fighting servants**.

**Exact unknowns that cannot be asserted from public description alone:**
- Entry rite, tier requirements, ability names/levels and each numerical bonus; whether max 10 has a branch/option system.
- How player lich level is stored: JAWR attachment, data component, persistent NBT, native `Attribute`, capabilities, scoreboard; whether stat data is client/server synchronized and survives clone/respawn.
- How the **phylactery** works when lethal damage is received: event priority / canceled death / true death+respawn, block vs held item, location anchor, soul consumption, chunk/loaded state, whether two competing phylacteries trigger.
- Whether undead combat servants expose vanilla owner, `TamableAnimal`, a custom `owner UUID`, a UUID stored on an AI brain, or none; player attribution and XP/loot.
- Any personal resource/cooldown and direct cast abilities; **do not automatically assume** "no spellcasting" merely because addon is mostly rites/lich progression.
- Soul collection/consumption, mob/animal restrictions, automated soul farm economy, villagers trade retention, Soul Cage behavior and cross-mod permissions.
- Ability compatibility with player vampire/lycan traits. Base 0.5.37 sync patch is evidence of ongoing work, not gameplay tested compatibility.

## Pufferfish design implications

1. **Strong default recommendation: preserve the native ten-level lich track, no duplicate standalone Pufferfish lich tree.** Pufferfish skill branching for a second ten-tier progression would likely be redundant. This remains a **recommendation for user review, not locked**, until exact abilities are known.
2. **Generic summoning skills could apply** to reanimated villagers/zombies only if owner attribution is verified. Reuse the cross-mod owner-adapter work if needed; avoid writing a Forbidden-Magic-specific expensive bridge for small benefits unless its API demands it.
3. **Generic combat or magic attributes may apply** to the *player* where powers use standard modifiers; but the actual attribute IDs and amount of overlap with innate lich buffs are **unknown**. Do not classify as native Confirmed or make a Pufferfish datapack key from guesswork.
4. **Do not merge** hypothetical lich soul energy into Ars+Iron's unified mana, Hex media, Psi or Goety souls/Arca merely because both are necromantic. Establish exact resources and shared logic before making any decision.
5. No arbitrary weakening of necromancy, Imp trades, summoned combatants or soul automation. Test **demonstrated** economy/progression issues first.

## Our transformation target and compatibility implications

Later user-approved design target (unimplemented): **two independent lichdoms that can stack into an Archlich**, unified vampire and werewolf base states with two progression tracks, and a deliberately constrained special abomination/hunter/dragon model. Therefore **JAWR lich + Goety lich is a required test pair**, not something to ban simply because an older B3 `TRANSFORMATION-COMPATIBILITY.md` proposal said exclusive physiology by default. Do not change existing game logic without explicit compatibility analysis.

Important test combinations:
- **JAWR Forbidden Magic lich alone**: entry, levels 1/10, state persistence, phylactery survival, item/chunk damage and actual death.
- **Goety lich alone**, then **JAWR→Goety** and **Goety→JAWR** on fresh disposable players. Verify both leveling trees, spellcasting, phylacteries, death/respawn, soul power/recharge, feeding, entity model, flight and enchantment.
- **JAWR base Witchery vampire + Forbidden lich** (plus unified Vampirism paths) only as permitted by final design; test whether conflicting diet and death code remains sane. Do not infer parent compatibility from changelog sync notes alone.
- **Enigmatic Legacy+**: Creation Heart, Cube, Void Pearl, Cosmic Scroll, Cursed Ring Soul Crystal vs Forbidden phylactery. Ensure only one **effective** death resolution, no duplicated drops, no infinite resurrection, correct charge/location ownership.
- **Enchanted poppet, vanilla Totem, Tombstone**: distinguish fatal-hit prevention, `LivingDeathEvent` cancellation, true death and grave placement.
- **Undead servant ownership**: owner online/offline, logout/login, multi-dimension, source tags, Pufferfish generic pet buffs, attacks/loot/XP/targeting, dismissal or death, grave/teleport from dead player.
- **Ritual/quest economy**: soul source, soul trade cost, Imp rewards, cage capacity, rituals, reanimated villager profession/trading and mob farms; inspect custom recipes and restrictions. Preserve powerful magic absent a demonstrated gate bypass.

## Immediate next inspection path

1. Obtain the **exact pinned** 0.5.37 JAR (official CurseForge file 9426760 / Modrinth version) and matching 0.5.39 JAWR base JAR; list class names/resources and detect registry identifiers, data attachments, spell/ability handlers, phylactery and death listeners.
2. In a backed-up test profile, record 0→10 lich advancement steps / abilities; inspect Curios/Modonomicon, localized strings and actual server configs.
3. Add diagnostic gameplay logging for incoming fatal hit, attempted `LivingDeathEvent`, canceled event, `PlayerEvent.Clone`, respawn, death drops and claimed grave; then test all cross-mod paths.
4. Confirm summon owner + native attribute access and only then decide whether any Pufferfish shared pet/necromancy node is warranted; do **not** create a bespoke tree until a meaningful benefit is identified.
5. Validate Forge/CurseForge parent `0.5.39` with addon `0.5.37`, both pinned in current manifest; shared version prefix is not proof or disproof of compatibility.

**No bytecode decompiled, no source-owned implementation reviewed, no gameplay test, no skill tree added.** Initial documented feature/economy/death integration planning only.
