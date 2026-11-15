# Hunter pinned-artifact and runtime verification — 15 November 2026

## Outcome

**Validated successfully:** 22/22 targeted mods were downloaded from their **packwiz-pinned sources** and independently SHA1/SHA512 checked; 90/95 upstream source-registration IDs were present as exact registered IDs in the full tested server. **All five missing IDs were Malum cultists from a newer upstream branch, not the pinned Malum 1.8.2 JAR.**

**Full dedicated-server test:** fresh NeoForge 1.21.1 integrated build booted using the repo's existing `scripts/smoke_server.sh` and a temporary, read-only first-party `ServerStartedEvent` census listener inserted into a disposable CI workspace. The server registered **1,393 unique entity types** including MISC projectiles/vehicles/FX. That 1,393 figure is **not** a count of living mobs or natural enemies.

**Evidence:** [22/22 verified hash run 43754945690](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43754945690); [full-server census run 43755119592](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43755119592); [archived 1,393 ID census commit](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43756456415).

**Durable outputs:**
- [1,393-ID exact-pack census](runtime-entity-census.csv): all registered IDs, namespace, MobCategory, full entity-type tags. `living_verified=unverified` and `hunt_eligible=no` are deliberately safe defaults pending behavior tests.
- [477-row candidate inventory](creature-inventory.csv): taxonomy, faction-neutrality candidate, XP policy and source evidence.
- [477-row candidate-to-registry crosswalk](candidate-runtime-crosswalk.csv): **424 present**, **44 Alex's Caves prospective/unpinned**, **five upstream-only Malum cultists absent**, **four former references with no distinct registered entity**.
- [95-source-ID evidence ledger](registration-evidence.csv): 90 now marked `runtime-registered-pinned-jar-string-matched`; five Malum IDs `not-in-tested-runtime`.
- [Creature taxonomy and neutrality policy](CREATURE-TAXONOMY.md).

## Material taxonomy fixes

### Newly resolved from the exact integrated server

| Entity | Registry evidence | Decision |
| --- | --- | --- |
| `minecraft:ghast` | `minecraft:undead` | **Undead**, not Netherborn/Spirit |
| `eidolon_repraised:necromancer` | `minecraft:undead` | **Undead**, despite role as caster |
| `irons_spellbooks:necromancer` | `minecraft:undead` | **Undead**, despite role as caster |
| `cataclysm:ancient_remnant` | `minecraft:undead` | **Undead + Prehistoric** |
| `cataclysm:modern_remnant` | `minecraft:undead` | **Undead + Prehistoric**; neutral/contract limitations still apply |
| `cataclysm:koboleton`, `cataclysm:wadjet`, `cataclysm:kobolediator`, `cataclysm:ignited_berserker` | `minecraft:undead` | **Undead**, not desert/fire/weapon-type categories |
| `cataclysm:clawdian` | `minecraft:aquatic` + `minecraft:arthropod` | **Aquatic + Arthropod** |
| `goety:watchling`, `goety:blastling`, `goety:snareling` | `goety:enderlings` | **Void lineage** (not general End mobs) |
| `goety:crusher`, `goety:sorcerer`, `goety:tormentor` | `minecraft:illager` | **Illager** |
| `goety:frayed`, `goety:mossy_necromancer` | `minecraft:undead` | **Undead** |
| `goety:wartling` | `minecraft:arthropod` | **Arthropod**, even if visually plantlike |
| `witchery:imp`, `witchery:demon`, `witchery:mirror_demon` | `witchery:demonic_entity` | **Demon** |
| `cataclysm:the_watcher` | separate lore/guide source: EMP-sensitive robot | **Construct**; likewise Goety Cataclysm summon |
| Vanilla Vex | Evoker summon and Illager battle ecosystem | **Illager ecosystem** as a faction relation, not as undead anatomy; no Spirit tree |

Other verified registry name corrections: `irons_spellbooks:citadel_keeper` is the **Ancient Knight**, not a second creature; `botania:doppleganger` is the **Gaia Guardian**; `draconicevolution:draconic_guardian` is the Chaos Guardian's registered type. Primal Magick's Guardian Pixies are registered as `primalmagick:guardian_pixie_basic/grand/majestic`, not display-name slugs.

**Important nuance:** native tags are pack-dependent compatibility evidence, not infallible lore. Cataclysm's `cataclysm:elemental` applies to different kinds of monsters (including undead), so Ignis should not become Elemental merely because that mod uses the tag. A `c:spirit` label likewise does not automatically imply a dead soul.

### Malum version drift (real finding, not guesswork)

The 1.21.1 development branch shows five monster-registered Cultists: Altar, Believer, Cherub, Cardinal and Evangelist. **The pinned Malum 1.8.2 binary has none of their expected registration strings, and the actual 1,393-ID integrated server has no such IDs**. Retain their historic research rows with `source_scope=upstream-only-runtime-absent`, `hunt_reward_policy=no`, `candidate_hunter_categories=none`. There is **no active Malum Cult specialist tree** justified by this pin.

### Current roster expansion

108 more candidate entity rows were added directly from exact runtime registrations: **32 Goety Cataclysm**, **56 Occultism** (possessed hosts, wild forms and familiar variants), and **20 Witchery Remake**. This grows the research inventory to **477 candidate records / 22 source groups**. Many new IDs have `hunt_reward_policy=review` or `no`: existing registration alone does not prove a legal natural kill or non-player ownership.

The complete runtime sidecar includes many other entities not yet given an individually designed Hunter tree: e.g., Goety has **192 non-MISC** registrations in total versus 76 manually described candidates; Vampirism, Werewolves, Aquaculture and other mods also contribute mobs. The full census closes the *registration discovery* gap, not the *gameplay categorization and eligibility* gap.

## Remaining true taxonomy ambiguities (11)

These are deliberately **Unresolved** and should not be shoehorned into combat-style, dimension or boss trees:

1. **Minecraft Shulker:** its shell and teleport ability do not by themselves prove Void lineage or Construct nature; current types lack a decisive Hunter family.
2. **Cataclysm Ignis:** mod `elemental` tag is broad, including undead; fire attacks do not prove elemental species. Supernatural burning knight lineage needs stronger evidence.
3. **Iron's Echo of Tyros, First Flamebearer:** source identifies `irons_spellbooks:fire_boss`, but an 'echo' and fire magic do not establish Undead/Elemental.
4. **Alex's Caves Watcher:** prospective/unpinned darkness entity; do not confuse with Cataclysm's mechanical Watcher.
5. **Goety Heresiarch:** distinctive hostile miniboss, but illager/cult/undead membership is not source-established. Author wiki explicitly says some illager-looking mobs are *not* Illagers.
6. **Botania Revived Gaia Guardian:** `botania:doppleganger` confirmed, but the nature of a nature guardian doesn't automatically prove Fae, Undead or Construct in this port.
7. **Occultism Shub Niggurath Spawn:** registered living-type entity; exact supernatural species/lineage and owner provenance need source/behavior verification.
8. **Witchery Elle:** entity exists; supernatural lineage not confirmed in pinned release.
9. **Witchery Horned Huntsman:** old Witchery lycanthropy lore is not automatically equivalent to JAWR's registered implementation.
10. **Witchery Insanity:** nightmare/psychic entity name is not a lineage.
11. **Witchery Nightmare:** Dreamworld context alone is not a species category.

**Non-specialist is a valid result, not necessarily a missing category.** Player-owned familiars, human cultists without a distinct faction, social NPCs and nonliving visuals may correctly have `none` and `hunt_reward_policy=no`. This audit reduced the earlier 51 unclassified display names to the **11 genuine uncertain taxonomy cases above**, while adding 108 newly discovered candidate rows.

## Verification scope and remaining runtime tests

1. **Not done:** construct an entity *instance* of every ID and verify class inheritance/ `LivingEntity` status (the census logs `MobCategory` and tag membership, not Java class of spawned instances).
2. **Not done:** verify natural spawn behavior, ritual spawning, AI hostility, player ownership and complex summon chains. `MONSTER` category is not proof of unprovoked aggression, and a friendly `CREATURE` may still defend itself.
3. **Not done:** client/world acceptance of a Hunter mod (it is not implemented), no profession XP behavior or neutrality AI behavior was changed or tested by this research PR.
4. **Needed:** a safe Hunter XP filter: require genuinely eligible hostile encounter and valid kill credit, exclude tames/summons/traders/nonliving effects/player self-created mobs, prevent repetitive summoning exploits; ensure special boss flags affect XP but not create a Boss damage category.
5. **Needed:** vampire–undead and werewolf–beast neutrality integration tests including retaliation, owners, faction override and exceptional bosses. `EntityTypeTags.UNDEAD` and pack-specific tags help identify most candidates, but should not automatically override native mob behavior.
6. **Needed:** specializations' damage stacking caps for legitimate double memberships, especially Ender Dragon (Dragon+Void) and Drowned (Undead+Aquatic).
7. **Future:** Alex's Caves remains unpinned and will require a selected NeoForge port, a new server census and precise mob compatibility testing before claims of activation.

**Do not merge these data tables as working gameplay logic without those tests. This PR remains research and evidence only.**
