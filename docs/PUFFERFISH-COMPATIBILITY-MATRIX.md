# Pufferfish Skills × Too Much Magic — magic roster compatibility matrix

**Snapshot:** 2026-11-13; the complete **43 magic-related entries** from the approved B5 manifest, including Enigmatic Legacy+. The manifest has 23 active core magic projects (plus other magic categories and addons); Pufferfish Skills and Pufferfish Attributes are separately approved but **not installed** on the planning branch (PR #10).

**Important evidentiary limits:**
- **Confirmed** = a direct, **source-level** player modifiable stat, command, API or effect path was established in earlier audits. This does **not** mean in-game Pufferfish wiring, NeoForge/packwiz runtime proof, or completion of full mod behavior. Some confirmed paths still require custom scripting.
- **Compatible with work** = source-backed, concrete bridge design identified, but the bridge and/or runtime test remains incomplete.
- **Needs investigation** = there is insufficient evidence on personal stat hooks, and no compatibility or incompatibility conclusion should be inferred. Addons might inherit their parent's attributes, but that still needs checking.
- **Proven incompatible** = a tested or source-demonstrated impossibility under the pack's version constraints. **No member currently meets this criterion.** No Native Pufferfish attribute is not proof of incompatibility.
- **Nothing separate to enhance** = no justified *independent mod-specific player stat branch* identified, or explicitly exempt from the custom tree. Universal attributes, item/ritual crafting perks and reopened investigations remain possible.

## State summary

- Confirmed — source-backed compatibility: **9**
- Compatible with work — implementation path identified, not yet proven: **3**
- Needs investigation — not enough evidence to decide: **25**
- Nothing separate to enhance — currently no mod-specific skill track: **6**
- Proven incompatible: **0**

Count **9 + 3 + 25 + 0 + 6 = 43**. The 43 deliberately exclude standard dependencies, generic performance/QoL, late technology, bosses, and ProjectE; the total is an audit scope definition, not a claim that the pack has only 43 mods.

## Confirmed — source-backed compatibility (9)

| Mod | Current evidence / rationale | Next action |
|---|---|---|
| **Ars Nouveau** (`ars-nouveau`) | Native spell-related attributes are source-verified; central Ars casting path is a clear Pufferfish integration target. | Map skill rewards to actual attribute IDs and test with Ars spells. |
| **Iron's Spells 'n Spellbooks** (`irons-spells`) | Native caster/spell attributes are source-verified. | Check attribute values in game and test spell power, cooldown, mana. |
| **Mahou Tsukai** (`mahou-tsukai`) | Player Mahou maximum can be changed with documented mod commands (e.g. addmaxmahou); prior source/command audit found a controllable personal stat. | Wire skill-level deltas to commands; test refunds and persistence. Not zero-code. |
| **Neo Vitae** (`neovitae`) | Prior audit found accessible player magic stat/API integration points. | Map exact registered API and attributes into the final tree; runtime test. |
| **Malum** (`malum`) | The mod exposes player-adjustable Malum attributes in its public 1.21.1 code. | Connect attributes and test resulting spell/spirits effects. |
| **Eidolon: Repraised** (`eidolon-repraised`) | EidolonAttributes in the 1.21.1 repository establish native stat hooks. | Wire Pufferfish modifiers; test. |
| **Forbidden & Arcanus** (`forbidden-arcanus`) | FAAttributes in the 1.21.1 repository provide native stat hooks. | Wire Pufferfish modifiers; test. |
| **Goety — The Dark Arts** (`goety`) | 27 registered spell/school attributes, plus owned servant mechanics, inspected in Goety-3 1.21.1 source. | Test school/global precedence, cooldown edge cases, servant melee/ranged/spells. |
| **Botania Revived** (`botania-revived`) | Prior audit identified player-relevant, source-verified enhancement mechanisms (including PixieHandler). | Validate cross-loader and port-specific behavior; avoid claiming Pufferfish runtime proof. |

## Compatible with work — implementation path identified, not yet proven (3)

| Mod | Current evidence / rationale | Next action |
|---|---|---|
| **Occultism** (`occultism`) | Ordinary spirits extend TamableAnimal; generic pet attributes should apply, but special familiars use custom PathfinderMob ownership. | Test spirits; build reusable owner adapter for familiars if needed. |
| **Roots Classic** (`roots-classic`) | KubeJS pre-pickup Crystal Staff imprinting and server/client Terra attachment mirroring implemented as opt-in PR #8; not gameplay-tested. | Test staff components and 40→60 Terra HUD across login, death and dimensions. |
| **Primal Magick** (`primal-magick`) | A single datapack damage tag can bridge primalmagick:is_sorcery to Pufferfish magic attributes. Attunement has public setters; pixies/golems use custom ownership. | Test tag; add reusable companion owner bridge; defer skill→attunement if unnecessary. |

## Needs investigation — not enough evidence to decide (25)

| Mod | Current evidence / rationale | Next action |
|---|---|---|
| **Psi** (`psi`) | Programmable spell-casting resource and spell effects have not been audited for native player attributes or item-stat hooks. | Inspect CAD spell cost, infusion, spell power, cooldown and relevant commands. |
| **HexCast NEOFORGED** (`hexcast-neoforged`) | NeoForge port; personal media and cast mechanics not yet inspected. | Look for media capacity/recovery, efficiency, spell power and entity attributes. |
| **Scriptor Magicae** (`scriptor-magicae`) | Language-based spellcrafting; no player attribute/API review yet. | Inspect player proficiency, mana, spell attributes and crafted spell persistence. |
| **Just Another Witchery Remake** (`witchery-remake`) | JAWR 0.5.39 has native vampirism/lycanthropy and ritual progression; accessible public repository does not expose core Java implementation. | Inspect released JAR, player capability, spell/ritual hooks and transformation progression. |
| **Enchanted: Witchcraft** (`enchanted-witchcraft`) | Rites, poppets and magical progression have not been examined for player-level stat hooks. | Determine whether there are personal magic skills or just item/ritual effects. |
| **Pastel** (`pastel`) | Color-resonance/research magic has not been mapped to player attributes. | Inspect player facets, spell potency and crafting traits. |
| **Totemic** (`totemic`) | Ceremony/totem magic source-level player mechanics not audited. | Inspect ceremonial skill, musician performance bonuses and buff scaling. |
| **Ars Additions** (`ars-additions`) | Ars addon; possible inheritance of Ars Nouveau casting attributes, but no separate addon API audit. | Check new spells/effects for base Ars attribute handling or distinct stats. |
| **Ars Controle** (`ars-controle`) | Ars addon; likely inherits core casting but not proven. | Check distinct casting controls and player modifiers. |
| **Ars Elemancy** (`ars-elemancy`) | Ars addon; distinct systems possible. | Test element-specific spell damage and specialization under core Ars attributes. |
| **Ars Elemental** (`ars-elemental`) | Ars elemental spell system may introduce separate school-specific mechanics. | Check elemental focus/effects and whether core Ars attributes apply. |
| **Ars 'n Spells** (`ars-n-spells`) | Ars addon; inherits parent is a hypothesis, not verified. | Check each new casting pathway and unique modifiers. |
| **Ars Ocultas** (`ars-ocultas`) | Cross-mod Ars/Occultism integration; possible distinct familiar hooks. | Check whether its familiars use vanilla tameable or custom owner API. |
| **Ars Unification** (`ars-unification`) | Ars cross-mod integration; attribute inheritance unverified. | Check its integration logic and distinct player stats. |
| **Extra Botany Revived** (`extra-botany-revived`) | Botania addon may add artifacts and mana/spell effects; direct player hooks not yet audited. | Check whether Botania Revived player/pixie mechanisms carry over. |
| **Gaze (Malum addon)** (`gaze-malum`) | Malum addon may inherit Malum attributes; no independent audit. | Review Gaze features and registered enhancements. |
| **Goety Cataclysm** (`goety-cataclysm`) | Goety + Cataclysm addon; spell and servant modifications may inherit Goety attributes. | Test Goety school stats and owned damage for new summons. |
| **Psi: Tweaks and Additions** (`psi-tweaks`) | Psi addon likely modifies casting/recipes, but direct stat hooks unverified. | Inspect whether it adds independent programmable spell resource stats. |
| **JAWR: Forbidden Magic** (`forbidden-magic`) | JAWR: Forbidden Magic ten-level lich system has been researched conceptually, not for exact player attribute/API hooks. | Investigate lich attributes, phylactery, necromancy summoning and vampire hybrid mechanics. |
| **Aspect Alchemy** (`aspect-alchemy`) | Brewing/alchemy plugin has not been checked for player crafting perks. | Check yield, potion potency, time, item stats and recipe API. |
| **Embers Re-Ignited** (`embers-re-ignited`) | Magical industry/resource system not audited for player-specific upgrades. | Check whether player-based casting or skill-scalable crafting exists. |
| **Feywild** (`feywild`) | Fey summon/quest system; pet ownership interface unexamined. | Check summon entities, owner tracking, pact mechanics and generic pet buffs. |
| **Vampirism — Become a Vampire!** (`vampirism`) | Its built-in vampire/hunter levels and skill tree have not been audited for caps and attributes. | Check native player attributes, caps, custom resource, double-dipping and cross-mod transformation. |
| **Werewolves — Become a Beast!** (`werewolves`) | Vampirism addon with forms and faction skills; no player attribute audit yet. | Inspect form-specific attributes, caps and interactions with Vampirism and JAWR. |
| **Enigmatic Legacy+** (`enigmatic-legacy-plus`) | Artifact-driven personal powers and possible progression; no stat API audit yet. | Determine whether relevant benefits are player attributes or purely equipment-based. |

## Nothing separate to enhance — currently no mod-specific skill track (6)

| Mod | Current evidence / rationale | Next action |
|---|---|---|
| **Astral Sorcery** (`astral-sorcery`) | Deliberate exemption: Astral Sorcery already has its own sophisticated personal perk tree. | Preserve its internal perk tree; generic damage attributes may still affect combat. |
| **Theurgy** (`theurgy`) | Prior audit found no native player-specific adjustable magic stats; primarily world/item-based systems. | No separate Pufferfish branch until a distinct personal mechanic is identified. |
| **EvilCraft** (`evilcraft`) | Prior audit found no personal adjustable magic stat; machinery, items and blood resources dominate. | Generic magic/crafting perks can still apply; no dedicated tree currently justified. |
| **Hexerei** (`hexerei`) | Prior audit found no native player-specific attribute system; mostly witchcraft items/rituals and brews. | Reopen if broom/brewing perks or personal effects merit enhancement. |
| **Reliquary Reincarnations** (`reliquary-reincarnations`) | Primarily magical tools and artifacts; no known standalone character progression stat. | Use universal gear/perk bonuses; do not invent a separate Reliquary skill. |
| **Summoning Rituals** (`summoning-rituals`) | A KubeJS ritual framework for cross-mod recipes rather than its own player magic progression. | Use it as integration infrastructure and recipe gating, not a standalone skill tree. |

## Proven incompatible (0)

No demonstrated incompatibilities.

## Recommended investigation sequence

1. **Personal-casting systems**: Psi → HexCast NEOFORGED → Scriptor Magicae. They are most likely to expose reusable resource/casting traits.
2. **Native transformation skill trees**: Vampirism → Werewolves → JAWR → JAWR: Forbidden Magic. Document intrinsic caps, suppression, and double-dipping before introducing extra perks.
3. **Distinct alternate magic**: Totemic → Pastel → Enchanted: Witchcraft → Aspect Alchemy → Feywild → Embers Re-Ignited → Enigmatic Legacy+.
4. **Addon inheritance quick-pass**: all seven Ars addons, Extra Botany Revived, Gaze, Goety Cataclysm, and Psi: Tweaks and Additions. Determine whether each is simply covered by its parent or has a separate player mechanic.
5. **Implementation checks already designed**: test Occultism ownership, Roots PR #8 staff imprint/Terra HUD, and Primal Magick magic-damage tags/companion ownership. Promote to source-confirmed only once the designated compatibility mechanism is verified.

## Existing source and design references

- Live manifest on planning branch: [`manifest/mods.json`](../manifest/mods.json).
- Roots work: [draft PR #8](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/8), test documentation `docs/ROOTS-SKILLS-KUBEJS-PROTOTYPE.md` on its feature branch.
- Goety: [native attribute registry](https://github.com/Vivideru/Goety-3/blob/main/src/main/java/com/Polarice3/Goety/init/ModAttributes.java).
- Primal Magick: [AttunementManager](https://github.com/daedalus4096/PrimalMagick/blob/MC1.21.1/common/src/main/java/com/verdantartifice/primalmagick/common/attunements/AttunementManager.java), [sorcery tag generation](https://github.com/daedalus4096/PrimalMagick/blob/MC1.21.1/neoforge/src/main/java/com/verdantartifice/primalmagick/datagen/tags/DamageTypeTagsProviderPMNeoforge.java), [companion ownership](https://github.com/daedalus4096/PrimalMagick/blob/MC1.21.1/common/src/main/java/com/verdantartifice/primalmagick/common/entities/companions/AbstractCompanionEntity.java).
- Occultism: [SpiritEntity](https://github.com/klikli-dev/occultism/blob/version/1.21.1/src/main/java/com/klikli_dev/occultism/common/entity/spirit/SpiritEntity.java), [FamiliarEntity](https://github.com/klikli-dev/occultism/blob/version/1.21.1/src/main/java/com/klikli_dev/occultism/common/entity/familiar/FamiliarEntity.java).
- Pufferfish generic pet attributes: [LivingEntityMixin](https://github.com/pufmat/attributesmod/blob/1.21/Common/src/main/java/net/puffish/attributesmod/mixin/LivingEntityMixin.java).
- Source-level references for prior confirmed audits: [MalumAttributes](https://github.com/SammySemicolon/Malum-Mod/blob/1.21.1/src/main/java/com/sammy/malum/registry/common/MalumAttributes.java), [EidolonAttributes](https://github.com/Alexthw46/Eidolon-Repraised/blob/1.21.1/src/main/java/alexthw/eidolon_repraised/registries/EidolonAttributes.java), [FAAttributes](https://github.com/stal111/Forbidden-Arcanus/blob/1.21.1/neoforge/src/main/java/com/stal111/forbidden_arcanus/common/entity/attribute/FAAttributes.java), [Ars Nouveau PerkAttributes](https://github.com/baileyholl/Ars-Nouveau/blob/main/src/main/java/com/hollingsworth/arsnouveau/api/perk/PerkAttributes.java), [Neo Vitae API reference](https://github.com/breakinblocks/NeoVitae/wiki/API-Reference), [Botania Revived PixieHandler](https://github.com/Velron-Devs/Botania-Revived/blob/Neo/1.21.1/src/main/java/velrondevs/botania/common/handler/PixieHandler.java).
- No roster entry has been labelled **runtime-integrated** merely because it appears in this document.
