# Ars Nouveau — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | `ars-nouveau` / `ars_nouveau` |
| Category / parent or addons | Primary magic system; see [Ars Elemental](ars-elemental.md), [Ars 'n Spells](ars-n-spells.md), [Ars Ocultas](ars-ocultas.md), [Ars Unification](ars-unification.md) when profiles exist |
| MC / loader / pack target | 1.21.1 / NeoForge / TMM `test/full-integration` at base `385c69889fccf281f94957ba4063488e1646a2e0` |
| Pinned version / artifact / hash | `mods/ars-nouveau.pw.toml`; `ars_nouveau-1.21.1-5.13.5.jar`; CurseForge project 401955, file 9521184; SHA-1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3` |
| Profile status | source-complete; exact pinned JAR not downloaded for local inspection; not verified in TMM gameplay |
| Evidence reached | Exact-version upstream source tree (`mod_version=5.13.5`), pinned packwiz metadata, upstream release notes; secondary gameplay guide explicitly targets 5.11 |
| Reviewed | 2026-11-15 |
| Quest ownership | Proposed Magic chapter “Ars Nouveau”; addons grouped under it. See [quest chapter inventory](../../QUEST-CHAPTER-INVENTORY.md). Quest plan only; no authored quest content verified. |

## Executive gameplay map

**What the player actually does:** Ars has two related but distinct economies. The player builds custom spells by combining a cast form, effects and augments into a spell stored in a spellbook or compatible casting item; casting spends the player's mana. Separately, Sourcelinks create **Source** from supported events or inputs and Source Jars store it for nearby devices and rituals. Source powers item processing (Imbuement Chamber, Enchanting Apparatus, Scribes Table), ritual effects and magical automation. The mod's summoned and befriended creatures extend that automation: Starbuncles move filtered items, Whirlisprigs reward diverse plant growth with Source, Wixies automate supported potion/item recipes, and Drygmys turn nearby living entities into repeatable item and experience-gem output. Exploration supplies Wilden parts, magical plants, creatures, and other progression materials. This is both a flexible combat/utility spell system and a small creature-run base-automation system, not only a spellbook.

**Why it matters in TMM:** Its distinctive contribution is player-authored spell composition plus Source-powered magical processing and creature automation. That complements Iron's authored spellbook/school progression, Occultism's spirit labor and storage, and Neo Vitae's altar economy. These are possible comparison points, not evidence of a working integration. Ars currently has four separately pinned addon records in the pack; addon-specific mechanics are not covered by this base profile.

**Quick start:** Craft the Novice Spell Book (the 5.11 secondary guide documents book plus ordinary iron tools), read the Worn Notebook, and use the Scribes Table to make glyphs. Build a Sourcelink and a Source Jar as the first Source loop; choose the link whose input matches what is already available (crop/tree growth, qualifying potion jars, food, fuel, or entity events). Keep Source Jars close to Source-consuming blocks. Exact early recipes and the current TMM recipe state still need a direct recipe/artifact check.

## Systems and full mechanics inventory

| Subsystem | How accessed | Inputs + costs | Effects / outputs | Limits, failure conditions and source |
| --- | --- | --- | --- | --- |
| Spell composition and casting | Worn Notebook, spellbook, Scribes Table; compatible items include caster tools | Scribes Table recipes take item ingredients and award glyph output plus recipe XP field; spells combine cast forms, effects and augments. Mana is the player's casting resource. | Reusable player-authored spells: attacks, movement, harvesting, utility, defense and summons depending on unlocked glyphs. | A syntactically assembled spell can still be invalid due to glyph order/slot rules, target requirements or missing glyph unlocks. Exact per-spell costs, spellbook tier limits and unlock costs were not established in this pass. [Glyph recipe implementation](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/main/java/com/hollingsworth/arsnouveau/common/crafting/recipes/GlyphRecipe.java); [5.11 guide](https://www.minecraft-guides.com/mod/ars-nouveau/wiki/spellcasting/). |
| Source storage and Sourcelinks | Place Sourcelink with Source Jar(s) nearby | **Source is an internal energy capability, not a fluid.** Each Sourcelink supports a specific event/input; see the input/output table below. | Generates and stores Source, then passes Source to supported nearby Source consumers. | Base Source link code has an internal 20,000 capacity; source transfer checks run on a 100-tick interval and special-event link collection is queued. These are code defaults, not a pack-specific rate guarantee. A link with the wrong trigger/input produces nothing. [Base link implementation](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/main/java/com/hollingsworth/arsnouveau/common/block/tile/SourcelinkTile.java). |
| Imbuement Chamber | Item-processing block | Source plus item recipes; exact accepted recipe schema must be inspected for the target item | Converts recipe inputs into magical materials, e.g. source gems from lapis or amethyst according to the item's upstream tooltip. | Recipes define the accepted items and cost; do not infer arbitrary item conversion. Recipe visibility can be queried through JEI. Exact recipe costs not audited here. [Generated item tooltip](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/generated/resources/assets/ars_nouveau/lang/en_us.json). |
| Enchanting Apparatus and Arcane Core | Apparatus, required Arcane Core in the specified aligned position | Recipe ingredients placed on pedestals plus Source drawn from nearby jars; recipes can craft trinkets, armor and magical items. | Recipe-defined item output, often persistent equipment. | Core must be next to the apparatus base and correctly aligned; missing ingredients, Source or core prevent crafting. Exact recipe costs and all recipe inputs remain unevaluated. [Upstream alert strings](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/generated/resources/assets/ars_nouveau/lang/en_us.json); [5.11 guide](https://www.minecraft-guides.com/mod/ars-nouveau/wiki/enchanting-apparatus/). |
| Scribes Table | Crafting / glyph interface | Recipe-defined item ingredients; recipe includes XP value | Glyph item used to expand spell vocabulary | Recipe list and XP costs need exact artifact/JEI audit. Source inspection confirms a custom recipe serializer with output, item ingredients and XP field; it does not by itself prove exact GUI consume behavior. [GlyphRecipe.java](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/main/java/com/hollingsworth/arsnouveau/common/crafting/recipes/GlyphRecipe.java). |
| Ritual Brazier | Place the ritual tablet and required items; activate with empty hand | Ritual tablet is consumed on activation. Some rituals use pedestal items; some draw Source from Source Jars within six blocks. Inputs may include tablets, item ingredients and nearby world/entity conditions. | Ritual-specific effects: area changes, mobility/utility effects, mob summoning, familiar binding, farming or other world changes. | Ritual setup and requirements differ by tablet. The Summon Wilden implementation takes Wilden drops for the Chimera path and warns that the Chimera destroys blocks in an area. No evidence that the base ritual framework accepts fluids, FE or XP as general inputs. [Notebook ritual text in generated language](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/generated/resources/assets/ars_nouveau/lang/en_us.json); [Wilden ritual implementation](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/main/java/com/hollingsworth/arsnouveau/common/ritual/RitualWildenSummoning.java). |
| Magical creatures, familiars and automation | Find creatures in world, trade or use mod mechanics; charms configure their work; Ritual of Binding turns eligible creature into familiar | Depends on creature: Source, target entities, items, crops/trees, chests, potion recipes or valid habitat. | Drygmy Henge harvests nearby entity-associated drops and experience gems into adjacent storage; Starbuncle moves filtered items; Whirlisprig rewards suitable plant growth; Wixie handles supported potion/item recipes and combat potion use; Bookwyrm magnetizes nearby drops and XP orbs when used as a familiar. | Creature-specific radius, happiness/XP, storage, filters and habitat matter. Drygmy requires Source to recharge after output; it is not an unconstrained mob-to-item conversion. Wixie stops crafting when its pedestal is powered (upstream Notebook text). Treat automated drops as exploit-sensitive in a pack with boss gates. [Notebook automation and familiar descriptions](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/generated/resources/assets/ars_nouveau/lang/en_us.json); [5.11 creature guides](https://www.minecraft-guides.com/mod/ars-nouveau/wiki/creatures/). |
| Magical crops and worldgen | Explore Ars structures/biomes and cultivate crops | Seeds, farmland/growth conditions and recipe ingredients | Magebloom and other plants provide material inputs; archwood and special plants participate in recipes/Sourcelinks. | Full worldgen, biome, seed and growth conditions were not audited. Avoid assuming a crop or structure appears in TMM until tested. |
| Worn Notebook / advancements | Starting guidebook, recipe and advancement discovery | Information and player actions | In-game reference plus milestone clues for acquiring key objects (Source Jar, Imbuement Chamber, Apparatus, familiar, Wilden Tribute and others). | UI and advancement trigger behavior are not client-tested in TMM. Language strings establish the named milestones but not actual pack presentation. |

### Sourcelink input/output contracts

These details were read from upstream source files in a source tree whose `gradle.properties` declares `mod_version=5.13.5` and Minecraft 1.21.1. The exact CurseForge JAR was not unpacked here.

| Sourcelink | Confirmed input / trigger | Output and consumption | Evidence / caution |
| --- | --- | --- | --- |
| Agronomic | Successful crop growth creates an event: 20 Source, or 45 for blocks tagged as magic plants. Successful tree growth: 50, or 100 for magic saplings. | Source only; crop/tree is not consumed by the link. | [AgronomicSourcelinkTile.java](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/main/java/com/hollingsworth/arsnouveau/common/block/tile/AgronomicSourcelinkTile.java). Do not assume which TMM crops carry Ars's tag. |
| Vitalic | Eligible breeding event: 600 Source. Eligible non-summoned/non-dispellable mob death: 200 Source. It also advances baby age near the link. | Source; no item/fluid consumed. | [VitalicSourcelinkTile.java](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/main/java/com/hollingsworth/arsnouveau/common/block/tile/VitalicSourcelinkTile.java). Entity blacklist/tag and event-range conditions apply. |
| Alchemical | Potion Jar with at least 100 units beside/below link; base 75, plus duration/amplifier/effect contributions, multiplied for multiple distinct effects. | Removes 100 potion units and adds calculated Source. | [AlchemicalSourcelinkTile.java](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/main/java/com/hollingsworth/arsnouveau/common/block/tile/AlchemicalSourcelinkTile.java). Formula is version-sensitive; unusual high-effect potions may be balance-sensitive. |
| Mycelial | Food item entity within one block or food in surrounding Arcane Pedestal; Source value scales with nutrition and saturation; magic-food/magic-plant tags add a bonus. | Consumes one food item and preserves its crafting remainder/container; produces Source. Progress can spread mycelium/mushrooms. | [MycelialSourcelinkTile.java](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/main/java/com/hollingsworth/arsnouveau/common/block/tile/MycelialSourcelinkTile.java). No rate/yield-per-item comparison done. |
| Volcanic | Burnable item or fuel-tagged recipe value; logs have bonuses, and Fire Essence has a fixed high value. | Consumes an item when the value fits available storage (or link is empty), outputs Source; progress can convert nearby stone/magma/lava blocks. | [VolcanicSourcelinkTile.java](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/main/java/com/hollingsworth/arsnouveau/common/block/tile/VolcanicSourcelinkTile.java). Exact fuel values depend on Minecraft/mod tags and must be checked in the actual registry. |

## Material / capability input-output contracts

| Producer / system | Accepted inputs | Output / world effect | Consumed? | Automation / scriptability |
| --- | --- | --- | --- | --- |
| Spell casting | Player mana; spell glyph sequence; cast target/world state | Damage, effects, blocks, movement or summon based on glyphs | Mana per cast | Spell effects are native; exact per-glyph costs and script hooks unreviewed |
| Sourcelinks | Source-generating game event; eligible item/potion for item links; target entities/blocks depending on type | Source points in Source storage | Events generally not; Mycelial, Alchemical and Volcanic consume input as above | Native event behavior; source API exposes event and provider contracts; no generic fluid support shown |
| Imbuement / Apparatus / glyph recipes | Recipe-defined items, Source; some crafting XP | Recipe-defined items, glyphs | Recipe-defined; XP field exists on glyph recipe | Custom datapack recipe types are present upstream; exact KubeJS recipe support not confirmed |
| Rituals | Ritual tablet; ritual-specific items/entities/world conditions; Source for rituals that require it | Item, entity, player-effect or world/block change | Tablet consumed at start; each ritual defines the rest | Native rituals and serializers; custom rituals require a documented addon/API, not assumed scriptable |
| Drygmy Henge | Nearby living entities, Source, charm/happiness/work progress | Entity-associated drops and experience gems into adjacent chests | Source per cycle; entity is not stated to be killed by the henge | Native automation; yield depends on entity list/state and happiness |
| Wixie | Supported recipes/items, Source, cauldron/pedestal/connected storage arrangement | Supported potion/item outputs | Recipe inputs and Source | Native automation; 5.11 wiki says a linked chest is required on 1.21.1; verify this and exact recipe behavior against 5.13.5 |
| Mob Jar | Entity capture / jar; selected entities continue a limited simulation | Passive entity behaviors such as egg laying/shearing/milking or light; Drygmy may regard the jarred mob as nearby | Entity is contained, not consumed | Native block simulation; not a general-purpose entity item transformer |

**Explicitly established:** Ars uses Source and player mana as separate resources; Source Jar is not a fluid. Rituals and spells can affect entities/world state, and magical automation can output items and experience gems. This pass did not establish a generic input for fluids, Forge Energy, raw player XP, arbitrary entities, commands or external item/fluid networks. Absence from this evidence is not a proof that no addon/API can add one.

## Acquisition, renewability and output economics

- Core Source is renewable when its chosen trigger/input is renewable. Agronomic and Vitalic links are event-driven; Alchemical, Mycelial and Volcanic links convert consumed potion/food/fuel inputs.
- Source is stored/handled as Ars capability energy in Source Jars and machines. It is not interchangeable with FE, mana, blood or a fluid based on naming.
- Drygmy output is a conversion of eligible nearby mob drop tables/experience value into item output and experience gems; it spends Source and depends on the mob's happiness and experience. This has high potential to bypass gated drops if eligible boss or rare mobs are included; audit exact entity validity before proposing a pack farm.
- Apparatus/Imbuement outputs are recipe-defined. No universal duplicator claim is supported.
- Ars adds unique Wilden drops and a repeatable summoning route. The Wilden Chimera is a distinct boss and its summoning ritual can damage terrain. It is an obvious progression gate candidate, but any TMM recipe/lock remains unimplemented unless separately confirmed.

## Progression map

| Milestone | Entry and acquisition | Unlock / payoff | Gate status |
| --- | --- | --- | --- |
| Entry | Find/craft Tattered Tome and Novice Spell Book; follow Worn Notebook | First spell composition and core concepts | Upstream guidebook/recipes; no TMM client test |
| Basic utility | Learn glyph recipes, make Source Jar and an appropriate Sourcelink | Useful combat/mining/movement spells; power for basic magical crafting | Recipe and glyph unlock details need JEI / exact artifact check |
| Processing | Imbuement Chamber, Arcane Pedestals, Enchanting Apparatus and Arcane Core | Magical ingredient processing and wearables | Native structure/alignment and Source conditions; no TMM gate is asserted |
| Creature labor | Obtain charms, find target creature, set appropriate work area and storage; bind familiar separately | Item transport, crop care, potion/item automation, Drygmy drops and passive familiar perks | Native creature rules; rates and supported entity outputs need target-world verification |
| Wilden progression | Dens or Summon Wilden tablet; collect parts | Equipment/ritual crafting, Chimera and Wilden Tribute | Base-source ritual can summon; the TMM ProjectE center recipe is separately designed to accept Wilden Tribute, but this profile does not claim that gate is implemented |
| Advanced | Magebloom, advanced glyphs, rituals, additional equipment and addons | Broad utility/combat/automation options | Exact tier sequence and current addon recipes remain to be researched |

## Internal combinations and overlooked mechanics

| Combination | Full chain | Benefit | Limitation / risk |
| --- | --- | --- | --- |
| Crop-powered workshop | Magic-tagged crop growth → Agronomic Sourcelink → Source Jar → nearby Ars machines | Passive Source while a farm grows; no fuel item is consumed by the link | TMM crops may not use Ars magic-plant tag; growth frequency and collection rate need measurement |
| Food-to-Source + fungi | Food input → Mycelial Sourcelink → Source; accumulated progress changes nearby mycelium/mushrooms | Converts otherwise surplus food into magical machine power and can develop a small fungi patch | Input item consumed; output amount varies with food values/tags; no self-sustaining rate demonstrated |
| Mob habitat to research materials | Eligible entity + Drygmy Charm/Henge + nearby Source and chest → mob-associated drops/experience gems | Converts a deliberately built habitat into renewable item/XP-gem output | Source upkeep, happiness, entity validity and drop gating; test each target mob; avoid rare/boss bypass |
| Potion production to Source | Supported potion recipe → Potion Jar → Alchemical Sourcelink → Source | Turns a potion pipeline into Source generation | Potion input is consumed in 100-unit units; effect-rich potions may have steep returns; no cross-mod potion support is assumed |

## Low-hanging setups useful in ordinary survival

### 1. Crop-adjacent Source starter — early, passive
- **Goal and payoff:** Create a steady trickle of Source while ordinary crops or trees grow.
- **Minimum entry:** Agronomic Sourcelink, one Source Jar, a small farm/tree plot. Exact crafting burden is not stated here.
- **Build/use:** Place Sourcelink beside the farm; place Source Jar next to it; use Source-consuming Ars stations close to the Jar. Prefer several growing crops rather than relying on an unverified modded crop tag.
- **Sustainability and limits:** No crop is consumed by the link; it only reacts to successful growth. Output depends on growth events and correct source/tick conditions.
- **Evidence / pack test:** Upstream source confirms 20 Source per normal crop growth and 50 per normal successful tree growth; not tested in TMM.

### 2. Mycelial food recycler — early, small item sink
- **Goal and payoff:** Convert surplus food into Source; the link also uses stored progress to make nearby mushrooms/mycelium.
- **Minimum entry:** Mycelial Sourcelink, Source Jar, surplus edible item; optional Arcane Pedestal for controlled feed.
- **Build/use:** Feed one item at a time into the link or its surrounding pedestal; connect Jar storage and monitor total Source. Use a safe collection area if allowing mushroom/mycelium growth.
- **Sustainability and limits:** Food is consumed, with crafting remainder preserved; this does not create free food. Better food and magic-food tags raise Source; exact best value should be measured, and modded food tag behavior is unconfirmed.
- **Evidence / pack test:** Conversion formula and consumption path inspected in upstream code; no rate test.

### 3. Drygmy drop-harvesting habitat — midgame, renewable drops
- **Goal and payoff:** Generate item drops and experience gems from eligible nearby living entities without killing them manually.
- **Minimum entry:** Drygmy Charm, Drygmy, Henge, Source Jar, nearby chest, a safely contained eligible mob and an appropriate habitat. Exact Charm recipe and setup count not audited.
- **Build/use:** Place Henge near the contained mob(s), attach a chest and Source Jar, create a safe habitat, then check progress, happiness and output. Confirm one target at a time before scaling.
- **Sustainability and limits:** Source is consumed to recharge each output cycle. Drop and gem amounts are tied to entity happiness/experience; tested entity eligibility and boss/rare drop inclusion are unresolved. Never treat it as approved bypass for TMM's boss-gated resources.
- **Evidence / pack test:** Current upstream Notebook text states generated items and experience gems are placed in adjacent chests and Source is required to recharge. No TMM test performed.

## API, integration and pack-config surfaces

| Surface | Evidence / capability | Current TMM override |
| --- | --- | --- |
| Datapack recipes | Upstream custom recipe serializers include glyph, summoning, scrying and machine recipes; recipe schemas differ by type | Pack pins KubeJS, but KubeJS support for each Ars recipe type is unknown; no profile-time custom recipe added |
| Source API | Base link uses `AbstractSourceMachine`, Source storage, `ISpecialSourceProvider`, queued Source events, tick and source-range checks | No custom override inspected |
| Ritual API | `AbstractRitual`, `RitualRegistry`, ritual context/event queue and ritual-specific classes | No custom ritual or altar edit made |
| Tags | Source link logic reads tags for magic crops, saplings, food, fuel and Wilden drops | Exact current registry tags and pack-added membership need artifact/registry dump |
| Advancement/guidebook | Upstream generated language includes milestones for core blocks/items and familiar / boss actions | No client observation of advancement triggers or book UI |
| Addons | Pinned companion manifests include Ars Elemental, Ars 'n Spells, Ars Ocultas and Ars Unification | Addon interactions are not established by shared theme or installed pins; separate profiles and two-sided evidence needed |

## Compatibility leads

| Other mod | Candidate link | Status |
| --- | --- | --- |
| Ars Ocultas + Occultism | Ars Ocultas describes a bridge involving Occultism spirits and Ars containment/processing. Exact recipes, entity behavior, and whether current pinned builds agree must be confirmed in both profiles and an in-pack test. | Candidate only; no interaction report yet |
| Ars 'n Spells + Iron's Spells 'n Spellbooks | Addon explicitly positions itself as integration between the two spell systems. Need inspect both exact artifact sides, addon-supported conversion/casting and progression settings. | Candidate only |
| Ars Elemental + Ars Nouveau | Addon extends glyphs, foci, equipment and creatures. Core profile does not absorb its subsystem details. | Separate profile queued |
| Ars Unification + multiple content mods | Name implies recipe/tag harmonization; actual covered materials and compatibility rules unknown. | Separate profile queued |

## Quest and teaching opportunities (research, not actual quests)

| Suggested stage | Meaningful action / demonstrable milestone | Possible detection | Possible safe reward | Caveat |
| --- | --- | --- | --- | --- |
| Early magic | Read the Worn Notebook and craft first Novice Spell Book | Item/advancement if exact trigger is confirmed; otherwise a manual check | Useful spell component or food themed as “wizard supplies” | Verify chapter and trigger in target client; don't reward/gate past an access requirement |
| Source basics | Produce Source in a Source Jar with a Sourcelink | Acquire Source Jar / block milestone plus explained use | Low-value source-related ingredient | A jar alone does not prove Source generation; consider task chaining |
| Spellmaking | Craft and cast a player-designed spell with a newly learned glyph | Glyph recipe / spell item; cast detection may need advancement or manual task | Matching glyph ingredient | Don't fabricate a cast criterion without checking FTB task support |
| Creature utility | Set up filtered Starbuncle transport or a Whirlisprig crop area | Charm acquisition and visible setup; likely checkable by item/task, not behavior from item alone | Small tool or thematic seed | Explain actual setup, not just charm crafting |
| Automation | Demonstrate a Drygmy output cycle with a safe common mob | Henge/charm/output item; manual observation if no criterion | Useful storage/automation material | Keep rare/boss loot out of early rewards and test whether this mob yields its drops |

These are research recommendations only. Chapter organization, task wording, rewards, localization and gate enforcement remain future quest work under the existing quest contract.

## Conflicts, limitations and version drift

- The only local TMM evidence is the exact packwiz metadata; no mod JAR, client world, server test of mechanics or current registry dump was available in the empty workspace. The upstream source tree's Gradle property matches version 5.13.5, but source-tree equality is not a binary/JAR checksum equivalence.
- The explanatory community wiki used here targets Ars Nouveau 5.11 for NeoForge 1.21.1, not the pinned 5.13.5. It informs the gameplay map only where supported by current upstream code/book strings; exact 5.11 differences may exist.
- Latest 5.13.5 release notes are mostly visual/particle and rendering fixes (including Prestidigitation behavior on living entities wearing/holding affected gear); they do not establish TMM runtime behavior.
- Source link acceptance tags are data driven. A crop, mob, or food being in the pack does not establish a matching Ars tag or supported automation path.
- Drygmy output and Ars summoning are potential loopholes in any drop-gated economy. Audit exact valid mob entities, outputs and Source costs in a disposable world before allowing use on protected boss drops.
- No Ars-Nouveau-only interaction with external fluid, FE, raw XP, storage or item transport networks was verified. Ars addon or third-party recipe integration may add such capabilities, but requires separate evidence.

## Evidence ledger

| Claim | Source and kind | What it proves / limits |
| --- | --- | --- |
| P-001 | [TMM packwiz pin](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/mods/ars-nouveau.pw.toml), pinned metadata | Exact filename, CurseForge project/file IDs and SHA-1; not runtime activation/test |
| P-002 | [TMM manifest row](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/manifest/mods.json), selection record | Approved selection and declared version; not installation proof |
| P-003 | [CurseForge file 9521184](https://www.curseforge.com/minecraft/mc-mods/ars-nouveau/files/9521184), official file page | Release version, MC 1.21.1/NeoForge, upload date and version-specific release notes |
| P-004 | [Upstream `gradle.properties`](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/gradle.properties), source-tree evidence | Current upstream source declares MC 1.21.1 and `mod_version=5.13.5`; branch is mutable and isn't an artifact hash |
| P-005 | [Upstream changelog](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/changelog.md), source/release note | Current release changes; not a full feature guide |
| P-006 | [Sourcelink base class](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/main/java/com/hollingsworth/arsnouveau/common/block/tile/SourcelinkTile.java), source inspected | Source capacity defaults, event transfer and provider calls; no in-game rates |
| P-007 | [Agronomic Sourcelink](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/main/java/com/hollingsworth/arsnouveau/common/block/tile/AgronomicSourcelinkTile.java), source inspected | Crop/tree growth event values and tag bonuses |
| P-008 | [Vitalic Sourcelink](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/main/java/com/hollingsworth/arsnouveau/common/block/tile/VitalicSourcelinkTile.java), source inspected | Breeding/death and baby-growth behavior; entity exclusions still require tags |
| P-009 | [Alchemical Sourcelink](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/main/java/com/hollingsworth/arsnouveau/common/block/tile/AlchemicalSourcelinkTile.java), source inspected | Potion jar consumption and source formula |
| P-010 | [Mycelial Sourcelink](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/main/java/com/hollingsworth/arsnouveau/common/block/tile/MycelialSourcelinkTile.java), source inspected | Food conversion, remainder handling, magic-food bonus and block progress |
| P-011 | [Volcanic Sourcelink](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/main/java/com/hollingsworth/arsnouveau/common/block/tile/VolcanicSourcelinkTile.java), source inspected | Fuel-value conversion and nearby magma/stone world changes |
| P-012 | [Wilden summoning ritual](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/main/java/com/hollingsworth/arsnouveau/common/ritual/RitualWildenSummoning.java), source inspected | Summoned mob stages, three Wilden parts for Chimera, nearby terrain destruction |
| P-013 | [Glyph recipe](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/main/java/com/hollingsworth/arsnouveau/common/crafting/recipes/GlyphRecipe.java), source inspected | Inputs, output and XP fields in the custom recipe serializer |
| P-014 | [Generated English strings and Notebook content](https://github.com/baileyholl/Ars-Nouveau/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/src/generated/resources/assets/ars_nouveau/lang/en_us.json), source data | Named milestones, item tooltips, familiar, ritual and automation descriptions; presentation not client-tested |
| P-015 | [Ars Nouveau 1.21.1 wiki](https://www.minecraft-guides.com/mod/ars-nouveau/wiki/), secondary, explicitly 5.11 | Explanatory guide only; not authoritative for 5.13.5 or TMM overrides |
| P-016 | [1.21.1 addon guide](https://github.com/Ars-Nouveau/Ars-Auxilia/blob/4d323fba9200c3293a4f4848cf1e0fe70beb7098/apps/ars.guide/src/content/docs/1.21.1/ars-addons/guide.mdx), upstream project guide | Addon identity leads; not proof of current addon feature details/compatibility |

## Disposable-world test plan

| Test | Steps | Expected observable result | Performed |
| --- | --- | --- | --- |
| Source distinction and consumers | In fresh TMM world, craft a Source Jar, use an Agronomic link, then inspect any nearby Ars machine and test a fluid container against the Jar | Crop/tree events change Source storage; Jar is not a fluid tank | Not run: no local game/client |
| Sourcelink inputs and tags | Compare ordinary and each installed magic crop; breed, grow and kill eligible/blacklisted entities near Vitalic; feed common foods and fuels to Mycelial/Volcanic; use representative potion jars with Alchemical | Only expected events/tags produce Source; consumption and remainder behavior match source | Not run |
| Processing recipes | Use JEI and apparatus with valid/invalid Arcane Core alignment; compare Source costs before/after craft | Exact recipes and failure messages recorded; no item loss on failed setup | Not run |
| Drygmy economy | Run common passive mob, neutral mob, boss/rare mob and a contained mob one at a time; record outputs, Source drain, happiness, distance and chunk unload/reload behavior | Per-entity output eligibility/gating and persistence understood | Not run; priority because of progression risk |
| Spell UX | Learn representative glyphs; validate several cast/effect/augment sequences; test mana/resource bars and spellbook tiers | GUI rejects invalid order; correct casts, costs and server replication documented | Not run |
| Server/client split | Repeat key automation and spell tests on a dedicated server with client GUI; unload/reload chunks and relog | No duplicated/lost Source, tasks or entity state; authority and persistence documented | Not run |
| Addon boundaries | Test Ars Ocultas, Ars 'n Spells, Ars Elemental and Ars Unification only after their individual profiles establish exact inputs/outputs | Each claimed bridge has both-side expected behavior and observable pass/fail | Not run |

No implicit test credit from parsing, compilation or dedicated-server startup.

## Unknowns and next research

- [ ] Inspect the exact CurseForge JAR or source artifact by its SHA-1; compare recipes, tags, guidebook, registry and config with the matching upstream tree.
- [ ] Audit all 5.13.5 custom recipe JSON types and recipe costs; confirm which are exposed to KubeJS and JEI in the pinned KubeJS stack.
- [ ] Establish exact spell-book tier/unlock progression, glyph recipe XP costs and representative spell mana costs.
- [ ] Inspect all source link input formulas, Sourcelink event range, Source Jar transfer/transport and machine pull radius across pinned code; validate measured rates.
- [ ] Establish creature acquisition, required charm recipes, complete Drygmy supported entity handling and Wixie linked-inventory rules in 5.13.5.
- [ ] Compare installed addons one by one, then select the first two-sided interaction report: Ars Ocultas × Occultism or Ars 'n Spells × Iron's.
- [ ] Confirm FTB task types that can detect a valid spell cast or functioning Source link without relying on a crafted item alone.
- [ ] Run disposable client and dedicated-server checks; upgrade profile to `verified-in-pack` only with reproducible evidence.

## Revision notes

- 2026-11-15 — First source-backed profile. Exact pack pin and upstream source version match; source details and version-lag limits recorded. Gameplay not tested.
