# Hunter creature taxonomy and faction-affinity contract

**Updated:** 2026-11-15 — exact-pin verification pass. **Platform:** Minecraft 1.21.1 / NeoForge 21.1.265. **Status:** researched and runtime-registered; **no Hunter ability, AI, faction, skill, XP or quest implementation** has been changed by this PR.

## Authoritative inventory and evidence

- [creature-inventory.csv](creature-inventory.csv): **477** named candidate records across **22 source groups**; includes friendly NPCs, player summons, legacy/unavailable entries and 44 prospective Alex's Caves creatures. Not 477 huntable entities.
- [runtime-entity-census.csv](runtime-entity-census.csv): **1,393 actual namespaced entity IDs** (including projectiles, boats, visual effects and other nonliving entities) from a disposable dedicated-server run of the exact integrated pack; records active tags and `MobCategory`.
- [candidate-runtime-crosswalk.csv](candidate-runtime-crosswalk.csv): 477 named candidates linked against that server's registry. **424 runtime-present**, **44 prospective Alex's Caves**, **five Malum cultists absent from the pinned build**, **four source-only names without distinct runtime entity IDs**.
- [registration-evidence.csv](registration-evidence.csv): **95 upstream source-registration candidates** across seven mods. **90 matching entity-registration strings** found in SHA-verified JARs; the five mismatches are those absent Malum cultists.
- [PINNED-RUNTIME-VERIFICATION.md](PINNED-RUNTIME-VERIFICATION.md): actual test results, caveats and unresolved taxonomy.
- [Full JAR and server CI](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43755119592) and [hash-verification CI](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43754945690): **22/22 targeted pinned JARs SHA-verified**; server started, census ran. We have **NOT** demonstrated all natural spawns, attacker AI, hostility, ownership handling or Hunter XP eligibility in client/world tests.

**Caution:** a runtime `EntityType` registration is real evidence of existence **in the tested pack**, but does not prove that it is living, normally hostile, naturally spawning, or legal to hunt. `MobCategory.MONSTER` alone cannot prove unprovoked attacks. Source tags such as `minecraft:undead` establish a useful default classification, but unusual modded mobs may be untagged and need explicit compatibility additions.

| Source | Candidate rows | Evidence status |
|---|---:|---|
| Vanilla Minecraft | 56 | live registry and tags |
| Cataclysm | 39 | live registry and tags; some nature from lore |
| Eidolon: Repraised | 7 | six non-MISC mobs plus one historical/noncombat case |
| Iron's Spells | 20 | runtime registrations; duplicate Ancient Knight/Citadel Keeper merged |
| Alex's Caves | 44 | **prospective only; no selected or pinned port** |
| Draconic Evolution | 1 | Chaos Guardian linked to active `draconicevolution:draconic_guardian` alias |
| Goety | 76 | live registry + author classifications; remaining servants in full census |
| Goety Cataclysm | 32 | added from runtime, many summons; XP eligibility unverified |
| Deeper and Darker | 11 | v1.4.1 tagged source + live registry |
| Feywild | 13 | live IDs matched or reviewed |
| Occultism | 63 | 62 registered mob IDs + one historical `Wild Spirit` name not independently registered |
| EvilCraft | 6 | five IDs + Werewolvian Villager is not a distinct registered entity |
| Witchery Remake | 27 | 26 mob registrations + one unregistered `Spirit Villager` candidate |
| Neo Vitae | 16 | 15 mob registrations + no registered `Sanguine Ward` entity |
| Ars Nouveau | 13 | live IDs matched |
| Botania Revived | 2 | Gaia Guardian's registry ID is `botania:doppleganger` |
| Malum | 6 | **five upstream-only cultists absent**; `natural_spirit` is a nonliving resource |
| Primal Magick | 36 | pinned JAR and live source IDs (Pixies are Fae) |
| Hexerei | 2 | crow/ owl; passive companions, not ordinary prey |
| Enchanted: Witchcraft | 2 | familiar cat and hostile Mandrake; source Ent registration commented out |
| Roots Classic | 1 | Phantom Skeleton; targets other monsters, not unprovoked players |
| Forbidden & Arcanus | 4 | three non-aggressive Lost Souls, Dark Trader; not normal XP prey |

## Owner-locked design rules

1. Hunter specialization follows **species, supernatural ancestry, coherent faction or coherent ecological group**, never simply a dimension, biome, weapon, tactic or difficulty.
2. **Do not create** Mage Slayer, Explosive Threat, Endborn, Netherborn, Spirit or Boss Hunter trees. **Spirit is merged into Undead** for actual dead/ghostly beings; a summoned outsider merely called a spirit is not necessarily undead.
3. Vanilla **Creeper = Plant** by pack design, including variants with real botanical ancestry. Exploding is not a category.
4. **Void** includes Enderman, Endermite, confirmed variants, Ender Dragon and Draconic Evolution Chaos Guardian. It can also include Goety's `goety:enderlings` Watchlings/Blastlings/Snarelings. **Do not classify every End mob as Void**; Shulker remains unresolved.
5. **Piglin** includes Piglin, Brute, Hoglin and coherent faction/ecosystem variants; **zombified Piglins and Zoglins are Undead**, not Piglin specialists.
6. **Aquatic** is an intentional ecology/physiology group across any dimension; animals, Deeplings, Cataclysm ocean mobs and other genuinely adapted enemies can count.
7. **Dinosaurs and prehistoric fauna are Beasts**, rather than a separate Prehistoric specialty. Actual aquatic arthropods (e.g., Trilocaris) remain Aquatic + Arthropod, since these describe their biology and combat ecology. Ancient Remnant/Modern Remnant are Beast + Undead in this pack; boss neutrality exclusions still apply. Multi-tag creatures are legitimate: Ender Dragon = Dragon + Void; Drowned = Undead + Aquatic; Clawdian = Aquatic + Arthropod. **Cap or diminish damage stacking** so obtaining several specialist branches does not trivialize multi-tag bosses.
8. `boss`, `summoned`, `owner`, `hostile`, `flying`, `spellcasting`, `explosion`, `armor` and `teleportation` are **combat/encounter attributes**, not specialist branches.
9. A data-driven registry must permit third-party mods to declare creature categories, tags, and individual exclusions. Neither the standalone progression Core nor standalone Monk should hardcode this taxonomy.

### Working specialization families

Base families: **Undead, Illager, Dragon, Void, Piglin, Sculk, Aquatic, Construct, Beast, Arthropod, Plant, Elemental, Ooze, Demon, Fae, Aberration**. **Dinosaurs are Beasts; there is no Prehistoric specialization.** Species traits like ancient/prehistoric may be retained as descriptive metadata, never as a Hunter damage category.

Additional **candidate families, not yet owner-approved as skill trees**:
- **Wilden** — Ars Nouveau Stalker/Hunter/Guardian/Chimera creature lineage.
- **Goety Cult** — Goety Heretic, Warlock, Maverick, Crone, Apostle; this is a coherent social faction and not generic magical combat. [Goety's cultist listing](https://goety.wiki.gg/wiki/Category:Cultist).
- **Hobgoblin** — JAWR's Hobgoblin community and patrons Mog/Gulg, pending decisions on whether patrons share species bonuses or only faction-counter abilities.
- **Mutant**, **Confection**, and perhaps **Darkness-born** — especially useful if Alex's Caves is added; that mod remains unpinned.
- **Malum Cult is NOT currently viable**: five tempting cultist registrations occur in a later upstream branch but **none exists in the pinned 1.8.2 runtime**. This candidate is suspended, not a supported pack tree.

### Corrected examples from exact runtime or lore

| Creature | Classification | Evidence / gameplay implication |
|---|---|---|
| Minecraft Ghast | **Undead** | Actual `minecraft:undead` entity-type tag; no Netherborn |
| Eidolon Necromancer | **Undead** | Actual `minecraft:undead` tag, unlike earlier assumptions |
| Iron's Necromancer | **Undead** | Actual `minecraft:undead` tag, unlike earlier assumptions |
| Cataclysm Ancient Remnant | **Undead + Beast** | Actual `minecraft:undead` tag on extinct dinosaur |
| Cataclysm The Watcher | **Construct** | EMP-sensitive Ancient Factory robotic sentry; same for Goety's summoned Watcher |
| Goety Blastling / Snareling / Watchling | **Void** | `goety:enderlings` lineage, not merely found in the End |
| Witchery Imp / Demon / Mirror Demon | **Demon** | Actual `witchery:demonic_entity` tags |
| Vanilla Vex | **Illager ecosystem** (not biologically illager) | Evoker-summoned raider ally; no standalone Spirit bonus |
| Goety Wartling | **Arthropod** | Actual `minecraft:arthropod` tag; not Plant merely from its name |
| Malum `natural_spirit` | **Not prey** | Nonliving `MISC` spirit-item entity, not Undead |
| Primal Magick pixies | **Fae** | Void/Infernal/Sea refer to magical affinities, not species |
| Enchanted Familiar Cat / Hexerei Owl | **Beast, not legal prey** | Registered companions; XP excluded |

## Faction neutrality — related but independently configurable

`creature_memberships` governs **what Hunters study and fight**. `affinity_rules` governs **who naturally targets whom**. `hunt_reward_policy` independently governs **what earns XP, progression milestones or contracts**. These must not be one table with inseparable meaning.

Proposed default affinity:
- Most ordinary Undead do **not** acquire an unprovoked vampire/lich target. This includes modded undead validated by runtime tags (e.g. Ghasts, certain necromancers), *subject to explicit story/boss exceptions*.
- Most ordinary Beasts do **not** acquire an unprovoked werewolf target. This does not grant taming, protect from damage or override social enemies (e.g. Illager-ridden Ravager).
- Preserve owner/commanded defense targets, retaliation, PvP rules, and specifically configured boss/story exceptions before applying broad neutral affinities. Exceptions are AI flags, **not a Boss specialization**.
- Hunters remain mutually exclusive with incompatible supernatural species; their transformation immunity requires dedicated provider hooks, not merely a status-effect cure.

**Current behavior is not implemented by this research PR.** The separate `feature/mob-alliance-neutrality` prototype already contains vampire/lich-to-undead targeting hooks, with owner and retaliation exemptions, but has no werewolf-to-Beast rule and is not proven active here. Do not claim neutral AI from a server census alone.

## Remaining acceptance tests

1. Verify entity *instance* classes (`LivingEntity`/Mob) as opposed to registry `MobCategory`; exclude projectiles, boats, FX, decorative models and non-living MISC types.
2. Test naturally-spawning versus ritual/summoned acquisition, owner chains, boss flags, faction affiliations, triggered-spawn cases and despawns.
3. Test no XP for passive livestock, player-owned familiars/summons, merchants, collectible souls, repeated self-created minions or duplicate kill-credit.
4. Test two or more simultaneous categories (Dragon+Void, Undead+Aquatic) for sensible stacking and neutrality precedence.
5. Verify faction neutrality against actual native AI targeting (vampire→Undead, werewolf→Beast), provocation, social faction override and hostile bosses.
6. Resolve [the intentionally narrow remaining taxonomy list](PINNED-RUNTIME-VERIFICATION.md), only with confirmed lineage, not guesses.

**This PR changes research documentation and data only. It does not rewrite the pack's runtime, install Alex's Caves or ship Hunter/Monk abilities.**
