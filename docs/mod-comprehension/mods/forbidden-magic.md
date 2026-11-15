# JAWR: Forbidden Magic — mechanics and progression profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | `forbidden-magic` / `witchery_forbidden_magic` |
| Category / parent or addons | JAWR addon; extends the `witchery` registry, ritual engine, Symbology, cauldron and Guidebook |
| MC / loader / pack target | 1.21.1 / NeoForge / B8 full-integration snapshot |
| Pinned version / artifact / hash | `witchery_forbidden_magic-0.5.37+mc1.21.1.jar`; Modrinth version `YgPMe2M7`; SHA-512 `f6604accc4bb7c65131a3f74a80aa3c0d1ae4daa6399f1bbb04a001ac98a8c47e88c55040fcfecd3e8832aaf4a89e1d1eda772f16407ab51c814ea3aa80ceead` (`mods/jawr-forbidden-magic.pw.toml`) |
| Source snapshot | Official [Modrinth project](https://modrinth.com/mod/pitZv4pV), version `YgPMe2M7`; matching artifact SHA-512 verified. Source commit/binary equivalence not identified. |
| Guide/documentation version gap | Addon adds data to JAWR's Modonomicon Guidebook. Exact pinned JAR pages and localized text inspected; no matching source/datagen snapshot identified. |
| Profile status | researching |
| Evidence reached | Exact pinned JAR contents, recipes, advancement gates, localizations, dependency metadata and parent JAWR guide extensions inspected. No client/world test. |
| Reviewed | 2026-11-15 |
| Quest ownership | Future JAWR/lich chapter only; no quest file changed. |

## Executive gameplay map

**What the player does:** Forbidden Magic adds the JAWR Soul Magic branch and native lich progression. The player opens the addon’s Soul Magic category from the JAWR Guidebook, learns how their own soul is used as a resource, advances four hidden necromancy ranks through code-granted advancements, and uses rites and cauldron recipes to separate souls from bodies, manipulate/capture them, and create or bind a phylactery. The highest rank unlocks soul-in-a-bottle harvesting for a small health restoration. The addon also contributes Lifeblood Berries, Soulstone Pendant, binding and petrification rites, Necromantic Infusion, chalk and a Petrify glyph contract traded by an Imp. [source: exact pinned JAR `data/witchery/modonomicon`, `data/witchery_forbidden_magic/recipe`, `assets/witchery_forbidden_magic/lang/en_us.json`, advancement data]

**Why it matters in TMM:** This is the source-backed native foundation for a future lich/Archlich path. The pack design says an Archlich should establish a phylactery first, then progress through Arca; that owner target remains a separate design requirement, not a claim that this addon implements Arca. See [progression design](../../PROGRESSION.md) and the [TeamLapen retirement target](../../TEAM-LAPEN-RETIREMENT-TARGET-2026-11-15.md). Forbidden Magic adds a real phylactery and necromancy progression but does not by itself settle the pack's Archlich stages, Tyz skill-tree behavior, or desired species/faction architecture.

**Progression shape in the locked JAR:** JAWR's `witchery:soul` advancement gates the addon category. Necromancy advancements `witchery:necro/1` through `/4` form a strict parent chain and are `minecraft:impossible`, so addon code must grant them. Guide entries `carving_1`, `_2`, `_3` are successively gated on ranks 2, 3 and 4, while `phylactery` is gated at rank 4. The fourth rank text says death becomes survivable through a phylactery, requiring three deaths and returns in one night. The data and text establish intended ordering, but not exact code predicates for gaining each rank. [source: pinned JAR advancement and Guidebook JSON/localization]

### Player-value snapshot

| Five standout capabilities | Three early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Soul separation and capture; phylactery-based death protection; soul-resource progression; necromantic undead raising; temporary petrification glyph | Soul severance brew/rite; Lifeblood Berries; Symbology Petrify via Imp contract | JAWR's core ritual/Guidebook systems; future lich skills; possible pack Arca/Archlich design, which is not in this artifact |

### Subsystem coverage at a glance

| Subsystem / branch | Mechanics evidence | Progression completeness | Guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Soul Magic guide/category | Exact JAR category and entry gates | Strong structural map | JAWR Guidebook → Soul Magic; unlocked by `witchery:soul` advancement | not run | Confirm when/how the parent mod grants `witchery:soul` |
| Necromancy ranks 1–4 | Hidden impossible-trigger advancement chain and staged guide text | Rank ordering known; grant predicates incomplete | Soul Magic → Your Soul as a Resource, carvings 1–3 | not run | Inspect matching implementation or observe rank triggers in game |
| Severance, soul capture and return | Seven ritual recipes and guide entries describe soul/body separation and reintegration | Recipe layout/requirements inventoried; entity edge behavior open | Soul Severance, Soulbind, Binding and Phylactery pages | not run | Test target classes, ownership, corpse decay and failure paths |
| Phylactery | Exact two craft rituals, bind rite and recipe data | Material and power inputs known; revival behavior partial | Soul Magic → The Phylactery | not run | Measure charges, death state, recovery and soul consumption |
| Brewing, infusion and healing | Two brewing recipes and two infusion recipes in addon | Named results known; full ingredient/target matrix not mapped | Brewing and Ritual categories; JEI | not run | Audit exact brewing steps, effects and resource changes |
| Symbology / Imp contract | Guide says Petrify is taught by a contract traded by an Imp | Acquisition known at concept level; trade table not extracted | Symbology → Petrify; JEI | not run | Confirm exact Imp trade and glyph target/duration |
| Lifeblood and Soulstone Pendant | Guide text gives berry setup and pendant output; crafting recipe inspected | Partial | Soul Magic entries; JEI | not run | Verify build pattern, harvesting, healing and extra soul slot behavior |

## Guidebook access and contents

- The addon does not define a separate book item. It extends the JAWR `witchery:guidebook` with a `Soul Magic` category using the `ancient_tablet` icon; its category unlock condition is the parent advancement `witchery:soul`. The JAWR base profile covers how the Guidebook itself is crafted and opened. [source: pinned addon JAR `data/witchery/modonomicon/books/guidebook/categories/soul_magic.json`; see [JAWR](witchery-remake.md)]
- The category contains 19 Soul Magic entries. Important titles include Your Soul as a Resource; The Binding of Souls; The Severed Soul; The Phylactery's Embrace; The Phylactery; Soul Severance; Soulbind; Soul in a Bottle; Lifeblood Berries; Necromantic Infusion; Rite of Petrification; chalk variants; and Soulstone Pendant. Entry unlocks depend on parent-JAWR category progression and the four necromancy ranks.
- The addon also augments Brewing with Necromantic Soulbind and a potion effect entry, Rituals with Necromantic Infusion, and Symbology with Petrify. The guide describes important goals but several pages are cryptic and do not provide full ingredient lists or operational failure feedback. JEI and the exact recipes are needed to complete a player walkthrough.
- Future quest guidance should first direct players to obtain and open the JAWR Guidebook. A lich route should explicitly teach soul separation, soul capture, phylactery creation and binding, and the phylactery's death-return behavior as separate operations. Do not imply that acquiring the item alone completes the binding or validates resurrection.

## Core mechanics and inputs / outputs

| System | Inputs / operation | Outputs / effect | Limits, failure and evidence |
| --- | --- | --- | --- |
| Soul severance rite | 11×11 Otherwhere Chalk pattern with Golden Chalk center; 5,000 altar power; consumes Taglock, Ender Dew, Tormented Twine and Necromantic Stone; moon must be waning/new | `witchery:soul_severance` effect separates the taglocked entity's soul from its body | Parent JAWR rite-event engine executes the custom event. Guide says humanoids may be collected in Soul Cage, and only humans can re-enter their own bodies; exact target filters, timers and death outcomes need tests. |
| Soulbind rite | 11×11 Infernal Chalk pattern; 5,000 altar power; waxing/full moon; consumes Taglock, Rotten Flesh, Golden Apple and Spectral Dust | `witchery:soulbind` event returns a soul-form to flesh | Exact identity/eligibility and cross-player edge behavior untested. |
| Binding rite | 11×11 Otherwhere Chalk pattern; 3,000 altar power; consumes Taglock, Chain, Lead and Waystone | Applies an area/entity binding effect | Scope, duration and escape conditions unknown. |
| Bind phylactery | 7×7 Binding Chalk ring with Golden Chalk center; 500 altar power; consumes Necromantic Stone and Tormented Twine, then runs a delayed command increasing Hunter chance | `witchery:bind_phylactery` binds an eligible nearby soul into a phylactery | Recipe declares no soul item among consumed inputs; likely target/state is obtained by event context. Exact range, accepted soul forms, ownership and charges must be verified. |
| Phylactery creation | Two same 15×15 Binding Chalk patterns, Golden Chalk center; each costs 10,000 altar power; consumes Necromantic Soulbind, Focused Will, Eternal Catalyst, a Witchery Cage and either Gold Ingot (gold variant) or Lapis Lazuli (soul variant); sets midnight | Summons `witchery_forbidden_magic:phylactery` with component variant `gold` or `soul` | Two visually identical patterns and costly altar requirements. Need confirm item variants have different capacities/effects; ingredient consumption and ritual failure safety untested. |
| Infuse Necromancy | Ritual pattern costs 4,000 altar power and consumes Necromantic Soulbind | Runs `witchery infusion setAndKill {owner} witchery_forbidden_magic:necro`; guide warns it kills the user | Treat death as a required cost, not a cosmetic effect. Any prevention/revival route is unresolved; the user must have a safe recovery path before attempting. |
| Petrification rite | 11×11 ritual pattern; 1,000 altar power; consumes Stone, Condensed Fear and Mutandis Extremis | Petrifies nearby entities after a delay | Guide says area entities; valid targets, duration, boss immunity and restoration behavior unknown. |
| Brew of Soul Severance | Recipe-defined JAWR cauldron brew | Detaches the drinker's soul; the soul can wander or possess dead hosts | Guide warns the body will rot if not returned in time. The exact recipe/order and timer/effect behavior need a matching game test. |
| Necromantic Soulbind brew | Recipe-defined cauldron brew | Essence used in rituals or other soul operations | Inputs/output are in exact recipe JSON; brewing heat, order and effect application require client verification. |
| Necromantic soulbind infusion | 2,500 altar power; applies Binding Curse to an armor item tagged `minecraft:enchantable/armor`, with Necromantic Soulbind as brew input | Enchants eligible armor with Binding Curse | This is a Cauldron Infusion recipe, distinct from the ritual called Necromantic Infusion. Do not conflate similar names. |
| Soulstone Pendant | Shapeless recipe: Lifeblood, Wool of Bat, Impregnated Fabric and Echo Shard | Soulstone Pendant; guide says it grants an extra soul slot | Exact slot behavior/compatibility with phylactery or death recovery is unknown. |
| Lifeblood | Guide instructs combining four Chorus, four glowing berry vines and a Villager Soul to build a contraption, then applying a Mutating Sprig to its center cobweb | Lifeblood Berry; consuming Lifeblood grants temporary regenerating hearts while charges remain | Guide text does not fully specify structure layout, harvest cycle or berry depletion. Client guide images/recipe data need inspection. |
| Soul in a Bottle | Fully leveled Lich uses Life Drain to kill a humanoid while holding a glass bottle offhand | Soul in a Bottle; Lich consumes to restore two hearts | The guide describes a combat trigger; eligible humanoids, bottle consumption and experience/rank conditions require testing. |
| Petrify glyph contract | Trade a Petrification Contract from an Imp, then right-click to learn | Unlocks the forbidden `Petrify` Symbology glyph | Trade offer and exact glyph targeting/cost/range not yet inspected. |

## Progression walkthrough blueprint

This is the source-supported sequence, not a completed player tutorial. Some requirements have prose or recipe data only; implementation-specific advancement predicates must be verified.

| Stage | Player action | Observable completion | Evidence / remaining gap |
| --- | --- | --- | --- |
| 0. Enter JAWR | Obtain/open the parent `witchery:guidebook`; complete JAWR's Soul-category trigger | Soul Magic appears in the Guidebook | Addon category condition is explicit; parent event granting `witchery:soul` remains open |
| 1. Start Soul Magic | Read the first Soul Magic entry and locate its instructions in JEI | First necromancy advancement (`witchery:necro/1`) is granted | Advancement is code-only; exact action for rank 1 unresolved |
| 2. Learn soul severance | Make Necromantic Stone and required ritual materials; perform soul severance on an allowed target | Soul separates; eligible soul can be placed in a Soul Cage or returned | Recipe inputs and moon/power requirements known; exact valid entity cases and rank credit open |
| 3. Progress through carvings | Follow the rank 2 and 3 carvings: capture/control undead souls, demonstrate necromantic command, defeat golems; later drain five innocent creatures, use soul sustenance, possess a risen shell and defeat the three-souled Nether creature | Corresponding `/2` then `/3` advancement and next Guidebook page unlock | Narrative goals source-backed; exact code predicates, counters and eligible entities unverified |
| 4. Create phylactery | At rank 4, craft gold or soul variant using the associated 10,000-power ritual | Phylactery item appears with its variant component | Both exact ingredient lists are known; client placement/UI and power operation untested |
| 5. Bind a soul | Run Bind Phylactery rite with a suitable soul/source nearby | Phylactery reports a bound soul/charge, or a death test demonstrates protection | Guide gives broad requirement; exact event rules and capacity unknown |
| 6. Complete lich embrace | With phylactery bound, die and return three times in one night | Lich status/advancement is present; death is intercepted as described | Guide prose confirms intent only; timer, charge use, and exact revival state require tests |
| 7. Use late lich abilities | Learn Life Drain and test bottle harvest for Soul in a Bottle; test Lifeblood and Soulstone Pendant separately | Correct item/health/soul-slot change occurs | Exact cooldowns, caps, eligible entities, and interaction with JAWR blood/other skill systems unknown |
| 8. Optional necromantic branches | Obtain Imp contract; learn Petrify; test binding, area petrification and Necromantic Infusion on disposable characters | Glyph/rite effect is observable with valid targets | Acquisitions and side effects need in-world validation; Infusion deliberately kills its caster |

## Compatibility and design notes

- **JAWR base dependency:** pinned addon metadata requires `witchery` version `>=0.5.37+mc1.21.1`, ordered after it. TMM's JAWR 0.5.39 pin satisfies the declared floor. This establishes metadata compatibility, not successful player-world behavior. [source: exact JAR `META-INF/neoforge.mods.toml`]
- **Ritual cost caveat:** the addon ritual recipes explicitly declare altar-power costs and the ritual consumer path is described in the JAWR base profile. JAWR's separate reported bug concerns `cauldron_crafting`; do not assume that report covers these ritual recipes. Test ritual power at the pinned pair.
- **Design alignment:** the user's target says “Phylactery first, then Arca” for Archlich progression. Forbidden Magic's current path has a phylactery but no Arca registry/data in the addon JAR. Treat Arca as a distinct follow-up stage to research/design; do not collapse the two or infer that a phylactery automatically implements the Archlich advancement.
- **Faction/skill migration:** broader pack target assigns supernatural physiology and progression to JAWR and selected personal skills to Tyz. No transfer into Tyz, no removal of the current mod, and no quest/runtime changes are part of this knowledge profile.
- **No startup conflict established:** JAWR, Forbidden Magic and dependencies are included in the B8 integration roster whose server boot passed. It does not prove client UI, guide rendering, rituals, or phylactery resurrection.

## Disposable-world verification plan

| Test | Positive and boundary cases | Expected observable | Performed? |
| --- | --- | --- | --- |
| Guide category | Trigger `witchery:soul`; compare no-trigger state; read entries in rank order | Category and each entry unlock only under declared conditions | no |
| Rank predicates | Complete each carving action with eligible and ineligible targets; count actions; relog between stages | Exact code-granted advancement and repeatability | no |
| Soul severance and return | Villager, player, zombie villager and other humanoid; cage vs no cage; return before/after body decay | Correct soul/entity outcomes; no unintended permanent character loss | no |
| Phylactery recipes | Gold and soul variants, exact power, missing ingredient, wrong chalk, failed ritual | Correct variant and safe, understood failure behavior | no |
| Binding and death protection | Bind each accepted soul type; test one, three and depleted charges; deaths inside/outside intended period | Phylactery preserves intended owner and consumes correct soul/charge | no |
| Infusion death | Run `infuse_necromancy` with and without an established revival method | Death and necromancy state match guide; no irrecoverable state loss | no |
| Healing / glyph branches | Consume Lifeblood with 0/1/multiple charges; use Soul in a Bottle at full/low health; petrify diverse mob types | Correct regeneration, two-heart restoration, valid temporary petrification | no |

## Evidence ledger

| Claim | Exact source | What it establishes / limit |
| --- | --- | --- |
| Artifact identity and hash | `mods/jawr-forbidden-magic.pw.toml`; downloaded pinned JAR SHA-512 | Exact version/file identity; local digest matches lock |
| Parent dependency and runtime range | `META-INF/neoforge.mods.toml` in pinned JAR | JAWR minimum, load ordering, NeoForge and MC range |
| Guide categories, entries and conditions | `data/witchery/modonomicon/books/guidebook/**` in pinned JAR | Native guide extensions and unlock predicates; not rendered UI behavior |
| Recipe patterns and ingredient/event data | `data/witchery_forbidden_magic/recipe/**`, plus JAWR namespace recipe data in pinned JAR | Declared inputs, altar costs, patterns and ordered ritual events; code event semantics still need tests |
| Rank chain and page localizations | `data/witchery/advancement/necro/*.json`, `assets/witchery_forbidden_magic/lang/en_us.json` | Rank parent order, guide-stage narrative and intended effects; advancement triggers are impossible/code-granted |
| Project/version description | [Modrinth project](https://modrinth.com/mod/pitZv4pV), exact version `YgPMe2M7` | Public release identity/description; not a substitute for pinned-data inspection |
| Cross-system decisions | `docs/PROGRESSION.md`, `docs/TEAM-LAPEN-RETIREMENT-TARGET-2026-11-15.md` | Current pack design target; not a live implementation claim |

## Unknowns and next research

- [ ] Identify matching source or inspect pinned bytecode to map how each impossible necromancy advancement is granted and how Lich status is represented.
- [ ] Extract complete Witchery event implementations for `soul_severance`, `soulbind`, `bind_phylactery`, `phylactery_1/2` and `petrify_entities`; verify valid targets, owner matching and charge semantics.
- [ ] Compare 19 guide entry JSON files with all parent JAWR prerequisites; capture hidden conditions and chapter navigation in a complete guide map.
- [ ] Inspect recipe-generated book page images/layouts and verify Lifeblood's physical build recipe, not just its localized prose.
- [ ] Audit Imp trade pool and Petrify spell registry for exact item/glyph IDs and operation costs.
- [ ] Test all documented positive and failure cases in a disposable client world before marking source-complete or verified-in-pack.
- [ ] Research the distinct Arca stage required by the owner-approved Archlich design when its exact mod/mechanic target is available.

## Revision notes

- 2026-11-15 — Added profile from the hash-matched 0.5.37 addon JAR. Mapped the four-rank code-granted journal, soul rituals, phylactery craft/bind sequence, and auxiliary branches. Marked runtime, rank predicates and phylactery behavior open. No quest or runtime files changed.
