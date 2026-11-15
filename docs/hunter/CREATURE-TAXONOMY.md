# Hunter creature census and faction-affinity research — Pass 1

**State:** research only; candidate classifications, not runtime tags or implemented skills.  
**Date:** 2026-11-15. **Target:** Minecraft 1.21.1 / NeoForge 21.1.265.  
**Working baseline:** `test/full-integration` manifest, read at commit `f6f1c5894ffd1ebcdcd4fd5d0989375fcc4d8f78`.  
**Machine-readable working inventory:** [creature-inventory.csv](creature-inventory.csv).

## Coverage

The first inventory has **168 named entries** (not 168 proven distinct, registered, living combat entities):

| Source | Entries | Current pack state | Evidence |
| --- | ---: | --- | --- |
| Vanilla 1.21.1 | 56 | baseline | [Minecraft mob reference](https://minecraft.wiki/w/Mob) |
| L_Ender's Cataclysm | 39 | pinned; manifest says 1.21.1-3.33 | [Cataclysm mob roster](https://lendercataclysm.wiki.gg/wiki/Mobs), [boss roster](https://lendercataclysm.wiki.gg/wiki/) |
| Eidolon: Repraised | 7 | pinned; manifest says 1.21.1-0.5.0.8 | [Eidolon entity listing](https://wiki.9minecraft.net/java/mod/eidolon-repraised/) (third party; includes a possible noncombat Chant Caster) |
| Iron's Spells 'n Spellbooks | 21 | pinned; manifest says 1.21.1-3.16.3 | [Official changelog](https://iron.wiki/changelog/), [official site](https://iron.wiki/), [creature guide](https://www.minecraft-guides.com/mod/irons-spells-and-spellbooks/wiki/) |
| Alex's Caves | 44 | **prospective, NOT in manifest or packwiz** | [Alex's Caves wiki](https://alexscaves.wiki.gg/wiki/Mobs) |
| Draconic Evolution | 1 | pinned; manifest says 1.21.1-3.1.4.633 | [Developer's Chaos Guardian lore](https://github.com/Draconic-Inc/PI-Docs/blob/master/ModDocs/draconicevolution/2.3.0/en_us/chaos_guardian_4c4fd.md) (older version; entity ID unverified) |

### Evidence and limits

- The CSV records **display names**, not fabricated namespaced entity IDs. `entity_id`, native entity tags, creature attributes and actual runtime membership are **not yet verified** against the pinned JARs or a live registry.
- Wiki pages may describe newer or older mod versions than the target; this inventory is a source-backed **candidate**, not version-matched completion. Some catalog pages list decorative or scripted entities as 'mobs', so the CSV explicitly distinguishes summoned, corpse, noncombat and uncertain cases.
- Alex's Caves has multiple distinct unofficial NeoForge 1.21.1 ports, including [Alex's Caves Neo](https://modrinth.com/mod/alexscavesneo), [another unofficial port](https://modrinth.com/project/alexs-caves-%28unofficial-port%29), and [Alex's Caves Continued](https://modrinth.com/mod/alexs-caves-continued). No port has been approved/pinned for this pack, so **do not install or assume port-specific parity** from this research.
- This is a starter inventory of five rosters plus a deliberate Chaos Guardian seed. It does **not** claim complete modpack hostile entity coverage.

## Taxonomy policy — owner decisions

1. Categories represent **species/lineage, supernatural origin, or coherent faction/ecosystem**; they are not attack mechanics or dimensions.
2. **No** Mage Slayer, Explosive Threat, Endborn, Netherborn or Boss specialization.
3. **Creepers are Plants** by pack design, notwithstanding uncertain in-game biology. Preserve the special case for ordinary Creepers; for mechanical or modified Creeper analogues review whether the plant ancestry still applies.
4. **Void** includes Endermen, Endermites and their confirmed variants, Ender Dragon, and Draconic Evolution's Chaos Guardian (inventoried as a seed); **not** every mob in the End. The latter requires a Draconic Evolution entry in a future wave.
5. **Piglin** includes Piglins, Brutes, Hoglins, and coherent related fauna/faction members; **not** zombified Piglins or Zoglins, which are Undead.
6. A creature may have multiple *genuine* memberships; e.g., Ender Dragon = Dragon + Void; Drowned = Undead + Aquatic. Tactical traits (flight, casting, summoning, explosion, armor) remain separate non-skill-tree metadata.
7. Boss/elite flags may affect **balance, XP, and neutrality exceptions**, but never grant a Boss damage specialization.
8. Passive wildlife, friendly traders, and player-owned summons should not become routine farmable Hunter XP or contracts. The CSV distinguishes `yes`, `defensive-only`, `no`, and `review` policies.
9. Registration should be data-driven and overridable in a standalone Hunter mod; the progression core should not hardcode mod names, monsters, or Minecraft lore.

### Working families

Existing: Undead, Spirit, Illager, Dragon, Void, Piglin, Sculk, Aquatic, Construct, Beast, Arthropod, Plant, Elemental, Ooze, Demon, Fae, Aberration.

**Candidate additions exposed by Alex's Caves (not yet approved skill trees):**
- **Prehistoric**: dinosaurs and ancient fauna across several mods; do not reduce to 'anything from a cave'.
- **Mutant**: creatures actually transformed by radiation/biological mutation, potentially including Brainiac/Gammaroach/Tremorzilla; distinguish hazardous attack effects from mutated physiology.
- **Confection**: magically living candy organisms. May warrant a narrow standalone tree if there are sufficient meaningful prey and abilities, otherwise fold into a broader lineage with a data tag.
- **Darkness-born**: the Watcher/Forsaken and any confirmed shared supernatural ancestry; research needed before making a skill tree. A mole cultist's worship does not by itself establish Darkness-born biology.

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
5. **Native AI/default behavior** applies otherwise. Damage and kill-credit rules remain native unless separately designed.

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
