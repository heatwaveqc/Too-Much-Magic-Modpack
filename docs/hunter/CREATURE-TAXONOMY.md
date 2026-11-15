# Hunter creature census and faction-affinity research — active inventory

**State:** research only; candidate classifications, not runtime tags or implemented skills.  
**Date:** 2026-11-15. **Target:** Minecraft 1.21.1 / NeoForge 21.1.265.  
**Working baseline:** `test/full-integration` manifest, read at commit `f6f1c5894ffd1ebcdcd4fd5d0989375fcc4d8f78`.  
**Machine-readable working inventory:** [creature-inventory.csv](creature-inventory.csv).

## Coverage

The working CSV now contains **325 named candidate entries** from **16 sources**. It is not a list of 325 independently verified living, hostile entity IDs. It deliberately includes noncombat, neutral, passive, summoned and prospective cases so the registry can exclude inappropriate hunt rewards.

| Mod | Entries | Evidence status |
| --- | ---: | --- |
| [Vanilla Minecraft 1.21.1](https://minecraft.wiki/w/Mob) | 56 | baseline |
| [L_Ender's Cataclysm](https://lendercataclysm.wiki.gg/wiki/Mobs) | 39 | pinned 3.33; wiki roster candidates |
| [Eidolon: Repraised](https://wiki.9minecraft.net/java/mod/eidolon-repraised/) | 7 | pinned 0.5.0.8; wiki candidates |
| [Iron's Spells 'n Spellbooks](https://iron.wiki/) | 21 | pinned 3.16.3; catalog candidates |
| [Alex's Caves](https://alexscaves.wiki.gg/wiki/Mobs) | 44 | prospective; **not approved/pinned** |
| [Draconic Evolution](https://github.com/Draconic-Inc/PI-Docs) | 1 | pinned 3.1.4.633; historical guardian reference |
| [Goety](https://goety.wiki.gg/wiki/Mobs) | 76 | pinned 3.2.2; wiki candidates |
| [Deeper and Darker](https://www.minecraft-guides.com/mod/deeper-and-darker/wiki/) | 11 | pinned 1.4.1; 1.4.1 guide |
| [Feywild](https://modrinth.com/mod/feywild) | 13 | pinned 5.6.4; project description |
| [Occultism](https://wiki.9minecraft.net/java/mod/occultism/guides/occultism-mobs/) | 7 | pinned 1.227.0; entity-guide candidates |
| [EvilCraft](https://github.com/CyclopsMC/EvilCraft/blob/master-26-lts/CHANGELOG-1.21.1.md) | 6 | pinned 1.2.100; matching changelog |
| [Just Another Witchery Remake](https://github.com/mrsterner/witchery-issues) | 7 | pinned 0.5.39; recent changelog + legacy candidates |
| [Neo Vitae](https://github-wiki-see.page/m/breakinblocks/NeoVitae/wiki/Daemonium-Bestiary) | 16 | pinned 1.1.53; developer bestiary |
| [Ars Nouveau](https://github.com/baileyholl/Ars-Nouveau/blob/main/src/generated/resources/assets/ars_nouveau/lang/en_us.json) | 13 | pinned 5.13.5; source text + creature guide |
| [Botania Revived](https://wiki.9minecraft.net/java/mod/botania/guides/botania-bosses-the-gaia-guardian/) | 2 | pinned 2.0.2; upstream behavior not confirmed in port |
| [Malum](https://github.com/SammySemicolon/Malum-Mod/blob/1.21.1/src/main/java/com/sammy/malum/registry/common/entity/MalumCultistEntityTypes.java) | 6 | pinned 1.8.2; current 1.21.1 branch source; **pin comparison pending** |

### Evidence and limits

- The CSV records **display names**, not fabricated namespaced entity IDs. `entity_id`, native entity tags, creature attributes and actual runtime membership are **not yet verified** against the pinned JARs or a live registry.
- Wiki pages may describe newer or older mod versions than the target; this inventory is a source-backed **candidate**, not version-matched completion. Some catalog pages list decorative or scripted entities as 'mobs', so the CSV explicitly distinguishes summoned, corpse, noncombat and uncertain cases.
- Alex's Caves has multiple distinct unofficial NeoForge 1.21.1 ports, including [Alex's Caves Neo](https://modrinth.com/mod/alexscavesneo), [another unofficial port](https://modrinth.com/project/alexs-caves-%28unofficial-port%29), and [Alex's Caves Continued](https://modrinth.com/mod/alexs-caves-continued). No port has been approved/pinned for this pack, so **do not install or assume port-specific parity** from this research.
- The earlier 168-entry starter has been expanded, but it does **not** claim complete modpack hostile entity coverage. A registered entity dump is still the acceptance criterion.

## Taxonomy policy — owner decisions

1. Categories represent **species/lineage, supernatural origin, or coherent faction/ecosystem**; they are not attack mechanics or dimensions.
2. **No** Mage Slayer, Explosive Threat, Endborn, Netherborn, Spirit or Boss specialization. **Spirit has been merged into Undead**: wraiths, ghosts, revenants and other postmortem/incorporeal dead count as Undead where supported; spectral movement is a trait, not a skill tree. Do not automatically treat non-dead beings called spirits as undead.
3. **Creepers are Plants** by pack design, notwithstanding uncertain in-game biology. Preserve the special case for ordinary Creepers; for mechanical or modified Creeper analogues review whether the plant ancestry still applies.
4. **Void** includes Endermen, Endermites and their confirmed variants, Ender Dragon, and Draconic Evolution's Chaos Guardian (inventoried as a seed); **not** every mob in the End. The latter requires a Draconic Evolution entry in a future wave.
5. **Piglin** includes Piglins, Brutes, Hoglins, and coherent related fauna/faction members; **not** zombified Piglins or Zoglins, which are Undead.
6. A creature may have multiple *genuine* memberships; e.g., Ender Dragon = Dragon + Void; Drowned = Undead + Aquatic. Tactical traits (flight, casting, summoning, explosion, armor) remain separate non-skill-tree metadata.
7. Boss/elite flags may affect **balance, XP, and neutrality exceptions**, but never grant a Boss damage specialization.
8. Passive wildlife, friendly traders, and player-owned summons should not become routine farmable Hunter XP or contracts. The CSV distinguishes `yes`, `defensive-only`, `no`, and `review` policies.
9. Registration should be data-driven and overridable in a standalone Hunter mod; the progression core should not hardcode mod names, monsters, or Minecraft lore.

### Working families

Existing: Undead (including spectral dead), Illager, Dragon, Void, Piglin, Sculk, Aquatic, Construct, Beast, Arthropod, Plant, Elemental, Ooze, Demon, Fae, Aberration.

**Candidate additions exposed by Alex's Caves (not yet approved skill trees):**
- **Prehistoric**: dinosaurs and ancient fauna across several mods; do not reduce to 'anything from a cave'.
- **Mutant**: creatures actually transformed by radiation/biological mutation, potentially including Brainiac/Gammaroach/Tremorzilla; distinguish hazardous attack effects from mutated physiology.
- **Confection**: magically living candy organisms. May warrant a narrow standalone tree if there are sufficient meaningful prey and abilities, otherwise fold into a broader lineage with a data tag.
- **Darkness-born**: the Watcher/Forsaken and any confirmed shared supernatural ancestry; research needed before making a skill tree. A mole cultist's worship does not by itself establish Darkness-born biology.

**Candidate families exposed by this expanded pass (not approved yet):**
- **Wilden**: coherent Ars Nouveau creature lineage (Stalker, Pack Hunter, Defender, Chimera), rather than a category for magical attacks or forests.
- **Malum Cult**: five related monster-category cultist registrations in the 1.21.1 source branch; exact 1.8.2 pinned binary parity unverified. A specific social faction, not generic Mage Slayer or all cultists.

**Explicit review cases:** Shulker, Ghast, Cataclysm Ignis/Wadjet/Clawdian/The Watcher, Iron's Ancient Knight/Citadel Keeper/Echo of Tyros, Eidolon Wraith and Necromancer, and Alex's Caves Nucleeper/Gumbeeper/Caniac. Do not force assignments based on spellcasting, names, locations, or model appearance alone.

## Faction neutrality — separate from Hunter damage taxonomy

**Design requirement:** vampires are normally neutral to most Undead; werewolves to most Beasts. Hunter remains a mutually exclusive non-supernatural identity. Neutrality is **no unprovoked target acquisition**, not taming, invulnerability, permanent pacification, or a ban on retaliation.

Two separate layers are necessary:
- `creature_memberships`: what a mob *is* and which Hunter specialization affects it;
- `affinity_rules`: which player species or creature factions it naturally treats as neutral, including per-mob exceptions and provocation conditions.

A third `hunt_reward_policy` prevents abuse by farming passive fauna, traders, repeated player summons or inert corpse/progression entities. A neutral mob may still be classified as a Hunter prey type without offering a kill contract.

### Proposed precedence

1. **Explicit owner/commander/defense target and damage retaliation** take priority; neither vampire nor werewolf affinity defeats owner-directed minions.
2. **Named boss/story encounter exemptions** can override neutrality; this is a behavior flag, **not** a Boss specialization.
3. **Specific social factions** can override broad lineage affinity. An Illager-aligned Ravager should not abandon its patrol because the player is a werewolf; Piglin/Hoglin relations require faction precedence.
4. **Baseline affinity:** non-exempt ordinary Undead do not initiate attacks against confirmed vampires/liches; non-exempt Beast mobs do not initiate attacks against confirmed werewolves. Include optional species providers only when their state can be verified.
5. **Native AI/default behavior** applies otherwise.

### Spirit-to-Undead fold: cross-mod caveats

- Eidolon Wraith and Goety Wraith are Undead; death-derived spectral enemies inherit Undead when verified. No separate Spirit specialization or duplicate damage stacking.
- **Occultism Foliot, Djinni, Afrit and Marid are supernatural outsiders labeled spirits by Occultism, not automatically dead souls.** They provisionally belong to Demon, and being player-owned summons prohibits Hunter XP farming.
- **Malum spirit items and spirit-extraction entities are resources, not undead mobs.** A `natural_spirit` noncombat `SpiritItemEntity` was found in Malum's source; it must not become an Undead prey entry.
- The Alex's Caves Watcher remains Unresolved; its spectral appearance does not prove it is a dead soul.

### Current audit priorities

- **Exact registry audit:** extract IDs, class, MobCategory, spawn egg, living status, owner metadata and tag memberships from the pinned JAR or booted registry. An upstream name/ID is evidence for a candidate only; do not assume it matches a pin.
- **Werewolf neutrality:** implement/tag a *separate* `Beast` no-first-aggro relationship with explicit owner/faction/boss exceptions; Goety summons and ravagers must not be pacified just for being beasts.
- **Name reconciliation:** source references use different names for Ars Wilden Guardian vs Defender, Hunter vs Pack Hunter; merge only after exact registry comparison.
- **Pending installed mob sources:** Enchanted: Witchcraft, Forbidden & Arcanus, Hexerei, Gaze/Malum addon, Goety Cataclysm, Primal Magick, Roots Classic, remaining Ars addons, and other registered entity providers. Some add no huntable mobs; prove absence from registry rather than guessing.
- **Port check:** Alex's Caves is not in the selected pack, and any future NeoForge port requires approval, provenance and version comparison before its rows become enabled.
 Damage and kill-credit rules remain native unless separately designed.

Do not automatically make every `Undead` row vampire-friendly: e.g., Wither, Maledictus, Dead King, summoned servants may need exclusions. Likewise, predatory or giant beasts (Tremorsaurus/Tremorzilla) and rival factions should be deliberate exceptions if needed. Hunter combat tags should not encode these social exceptions.

### Existing compatibility prototype

The repository has a **separate, unmerged-to-integration** `feature/mob-alliance-neutrality` branch containing:
- `compat/transformations/.../mob/MobAllianceEvents.java`: target-change cancellation and periodic target clearing; 600-tick retaliation memory; vanilla + extra Undead tagging; vampire/lich verification; fae/forest-dragon and demon/cave-dragon relationships; optional summoned and boss exemptions; `TMM_MOB_CENSUS` server log.
- `compat/transformations/.../mob/MobAlliancePolicy.java` and corresponding test.

**Gap:** no werewolf–Beast rule, no unified Hunter taxonomy registration, and no evidence that either behavior is functional in the current integration build. **Do not mistake this design-pass note for a runtime update.**

**Census concern:** that prototype skips every `MobCategory.MISC` entity. Some modded bosses, spawned summons, or scripted living entities may use unconventional categories, so a reliable entity census should inspect actual living/mob registrants and filter out projectiles separately; log `minecraft` entries too.

## Next census stages

1. **Exact-version registry census:** get `namespace:path` from pinned JAR entity registrations or a clean test-server dump; record entity class, living flag, natural behavior, owner/summon type, and vanilla/mod tags. Do not infer IDs from display names. Include boss and MISC edge cases.
2. **Expand active-mod coverage:** Deeper and Darker; the remainder of Draconic Evolution; Goety + Goety Cataclysm; JAWR and its addons; Feywild; Enchanted: Witchcraft; Malum; Occultism; EvilCraft; Ars Nouveau + additions; Neo Vitae; Hexerei; Botania Revived; Forbidden & Arcanus; Primal Magick; Roots Classic; other packs creating living entities. Reconcile each against actual `mods/*.pw.toml` and version.
3. **Neutrality matrix:** vampire, lich, werewolf, human Hunter, and dragon/fey/demon species states × tags/individual exceptions; test no-aggro, retaliation, commanded summons, PvP and boss encounters independently.
4. **Coverage gates:** no unknown natural hostile entity silently assigned to a default specialist; no neutral/trader automatically treated as a legal contract; no summon-farming bypass; no broad multi-tag boss damage multiplication.
5. **Review proposed new trees** after the full installed-mod census to determine if Prehistoric, Mutant, Confection, or Darkness-born have sufficient shared identity and gameplay to justify standalone specializations.

This pass changes **documentation only**. It does not alter packwiz, gameplay classes, skills, mob AI, loot or faction assignment.
