# Pufferfish Skills × Too Much Magic — 43-mod compatibility matrix

Updated 2026-11-13. Scope: all 43 approved magic-related entries in the B5 planning manifest, including Enigmatic Legacy+. Pufferfish Skills and Attributes remain approved but uninstalled, pending their custom tree. **Psi: Tweaks and Additions stays in Needs Investigation until the main Psi audit.**

**How to read these states:** Confirmed means source-verified player stats/hooks OR demonstrably inherited parent spell-casting hooks, **not runtime-tested skill tree integration**. Compatible with work means a concrete bridge, configuration or implementation test remains. Needs investigation means evidence is insufficient and incompatibility cannot be inferred. Proven incompatible requires demonstrable impossibility (none so far). Nothing separate to enhance means there is no justified *independent mod-specific player progression track*, not that universal Pufferfish attributes cannot apply.

The ten-addons pass is documented in [PUFFERFISH-ADDON-AUDIT-2026-11-13.md](PUFFERFISH-ADDON-AUDIT-2026-11-13.md), including upstream Java references, version caveats, and the Ars 'n Spells mana-unification design issue.

## Summary

| State | Count |
|---|---:|
| Confirmed (source-backed and parent-inherited) | **13** |
| Compatible with work | **6** |
| Needs investigation | **16** |
| Proven incompatible | **0** |
| Nothing separate to enhance | **8** |
| **Total** | **43** |

## Confirmed — source-backed or parent-inherited Pufferfish compatibility (13)

| Mod | Current evidence / rationale | Next action |
|---|---|---|
| **Ars Additions** (`ars-additions`) | Extra glyphs extend Ars AbstractEffect and reuse its spell pipeline; ReachPerk adds vanilla interaction-range attributes. | No independent tree needed. Test new glyph stats and perk/attribute stacking. |
| **Ars Controle** (`ars-controle`) | Precise Delay extends Ars AbstractEffect and uses the existing SpellStats/SpellContext pipeline; other features are spell-control utilities. | Verify Ars caster perks apply to newly added glyphs. No dedicated Controle player stat. |
| **Ars Elemancy** (`ars-elemancy`) | Focus inherits Ars Elemental GreaterElementalFocus and modifies SpellStats; armor extends Ars gear and uses PerkAttributes/IManaDiscountEquipment. | Test focus amplification and armor/regen stacking with Pufferfish buffs. |
| **Ars Elemental** (`ars-elemental`) | Damage glyphs extend Ars AbstractEffect and use SpellStats/SpellResolver; hence normal Ars spell perks are inherited. | Test custom damage types, elemental summons and ownership separately; no automatic generic-pet claim. |
| **Ars Nouveau** (`ars-nouveau`) | Native spell-related attributes are source-verified; central Ars casting path is a clear Pufferfish integration target. | Map skill rewards to actual attribute IDs and test with Ars spells. |
| **Botania Revived** (`botania-revived`) | Prior audit identified player-relevant, source-verified enhancement mechanisms (including PixieHandler). | Validate cross-loader and port-specific behavior; avoid claiming Pufferfish runtime proof. |
| **Eidolon: Repraised** (`eidolon-repraised`) | EidolonAttributes in the 1.21.1 repository establish native stat hooks. | Wire Pufferfish modifiers; test. |
| **Forbidden & Arcanus** (`forbidden-arcanus`) | FAAttributes in the 1.21.1 repository provide native stat hooks. | Wire Pufferfish modifiers; test. |
| **Goety — The Dark Arts** (`goety`) | 27 registered spell/school attributes, plus owned servant mechanics, inspected in Goety-3 1.21.1 source. | Test school/global precedence, cooldown edge cases, servant melee/ranged/spells. |
| **Iron's Spells 'n Spellbooks** (`irons-spells`) | Native caster/spell attributes are source-verified. | Check attribute values in game and test spell power, cooldown, mana. |
| **Mahou Tsukai** (`mahou-tsukai`) | Player Mahou maximum can be changed with documented mod commands (e.g. addmaxmahou); prior source/command audit found a controllable personal stat. | Wire skill-level deltas to commands; test refunds and persistence. Not zero-code. |
| **Malum** (`malum`) | The mod exposes player-adjustable Malum attributes in its public 1.21.1 code. | Connect attributes and test resulting spell/spirits effects. |
| **Neo Vitae** (`neovitae`) | Prior audit found accessible player magic stat/API integration points. | Map exact registered API and attributes into the final tree; runtime test. |

## Compatible with work — concrete integration path identified (6)

| Mod | Current evidence / rationale | Next action |
|---|---|---|
| **Ars 'n Spells** (`ars-n-spells`) | Bridges Ars and Iron's mana/spell damage; NeoForge SpellDamageEvent.Pre handler adds Iron's power to Ars damage without explicitly recounting Ars damage perk. | Choose mana-unification mode and test possible Pufferfish cross-system spell-power stacking (3.3.5 pinned vs newer source). |
| **Extra Botany Revived** (`extra-botany-revived`) | Botania addon with custom DamageHandler, damage types, gear/Curios attributes, and custom-owned entities that do not implement OwnableEntity. | Review magic-damage tags and direct-health attacks; test universal attributes and item enhancements. |
| **Goety Cataclysm** (`goety-cataclysm`) | Cataclysm spells/servants architecturally extend Goety Spell and Summoned in public 1.20.1 source, but the pinned 1.21.1 code is not matched. | Inspect exact 1.21.1 jar/source and test school potency, soul discount and owned-servant damage. |
| **Occultism** (`occultism`) | Ordinary spirits extend TamableAnimal; generic pet attributes should apply, but special familiars use custom PathfinderMob ownership. | Test spirits; build reusable owner adapter for familiars if needed. |
| **Primal Magick** (`primal-magick`) | A single datapack damage tag can bridge primalmagick:is_sorcery to Pufferfish magic attributes. Attunement has public setters; pixies/golems use custom ownership. | Test tag; add reusable companion owner bridge; defer skill→attunement if unnecessary. |
| **Roots Classic** (`roots-classic`) | KubeJS pre-pickup Crystal Staff imprinting and server/client Terra attachment mirroring implemented as opt-in PR #8; not gameplay-tested. | Test staff components and 40→60 Terra HUD across login, death and dimensions. |

## Needs investigation — insufficient source evidence yet (16)

| Mod | Current evidence / rationale | Next action |
|---|---|---|
| **Aspect Alchemy** (`aspect-alchemy`) | Brewing/alchemy plugin has not been checked for player crafting perks. | Check yield, potion potency, time, item stats and recipe API. |
| **Embers Re-Ignited** (`embers-re-ignited`) | Magical industry/resource system not audited for player-specific upgrades. | Check whether player-based casting or skill-scalable crafting exists. |
| **Enchanted: Witchcraft** (`enchanted-witchcraft`) | Rites, poppets and magical progression have not been examined for player-level stat hooks. | Determine whether there are personal magic skills or just item/ritual effects. |
| **Enigmatic Legacy+** (`enigmatic-legacy-plus`) | Artifact-driven personal powers and possible progression; no stat API audit yet. | Determine whether relevant benefits are player attributes or purely equipment-based. |
| **Feywild** (`feywild`) | Fey summon/quest system; pet ownership interface unexamined. | Check summon entities, owner tracking, pact mechanics and generic pet buffs. |
| **Gaze (Malum addon)** (`gaze-malum`) | 1.21.1 published addon has distinct evolving progression, but the audit did not find matching public source/API evidence. | Inspect Gaze 1.1.7.1 jar/docs for Malum attr inheritance, soul progression and custom stats. |
| **HexCast NEOFORGED** (`hexcast-neoforged`) | NeoForge port; personal media and cast mechanics not yet inspected. | Look for media capacity/recovery, efficiency, spell power and entity attributes. |
| **JAWR: Forbidden Magic** (`forbidden-magic`) | JAWR: Forbidden Magic ten-level lich system has been researched conceptually, not for exact player attribute/API hooks. | Investigate lich attributes, phylactery, necromancy summoning and vampire hybrid mechanics. |
| **Just Another Witchery Remake** (`witchery-remake`) | JAWR 0.5.39 has native vampirism/lycanthropy and ritual progression; accessible public repository does not expose core Java implementation. | Inspect released JAR, player capability, spell/ritual hooks and transformation progression. |
| **Pastel** (`pastel`) | Color-resonance/research magic has not been mapped to player attributes. | Inspect player facets, spell potency and crafting traits. |
| **Psi** (`psi`) | Programmable spell-casting resource and spell effects have not been audited for native player attributes or item-stat hooks. | Inspect CAD spell cost, infusion, spell power, cooldown and relevant commands. |
| **Psi: Tweaks and Additions** (`psi-tweaks`) | Psi addon likely modifies casting/recipes, but direct stat hooks unverified. | Inspect whether it adds independent programmable spell resource stats. |
| **Scriptor Magicae** (`scriptor-magicae`) | Language-based spellcrafting; no player attribute/API review yet. | Inspect player proficiency, mana, spell attributes and crafted spell persistence. |
| **Totemic** (`totemic`) | Ceremony/totem magic source-level player mechanics not audited. | Inspect ceremonial skill, musician performance bonuses and buff scaling. |
| **Vampirism — Become a Vampire!** (`vampirism`) | Its built-in vampire/hunter levels and skill tree have not been audited for caps and attributes. | Check native player attributes, caps, custom resource, double-dipping and cross-mod transformation. |
| **Werewolves — Become a Beast!** (`werewolves`) | Vampirism addon with forms and faction skills; no player attribute audit yet. | Inspect form-specific attributes, caps and interactions with Vampirism and JAWR. |

## Proven incompatible (0)

None demonstrated.

## Nothing separate to enhance — no distinct player skill track (8)

| Mod | Current evidence / rationale | Next action |
|---|---|---|
| **Ars Ocultas** (`ars-ocultas`) | Adds Occultism spirits to Ars Containment Jars and recipes; inspected registerGlyphs is empty. No independent player spell resource. | Retain Ars and Occultism personal skill paths; check only processing/recipe economy. |
| **Ars Unification** (`ars-unification`) | Reuses existing Ars Crush/Cut glyphs for other mods' processing recipes, including Occultism Crusher Spirits. | No dedicated Pufferfish branch; review resource/recipe loops only. |
| **Astral Sorcery** (`astral-sorcery`) | Deliberate exemption: Astral Sorcery already has its own sophisticated personal perk tree. | Preserve its internal perk tree; generic damage attributes may still affect combat. |
| **EvilCraft** (`evilcraft`) | Prior audit found no personal adjustable magic stat; machinery, items and blood resources dominate. | Generic magic/crafting perks can still apply; no dedicated tree currently justified. |
| **Hexerei** (`hexerei`) | Prior audit found no native player-specific attribute system; mostly witchcraft items/rituals and brews. | Reopen if broom/brewing perks or personal effects merit enhancement. |
| **Reliquary Reincarnations** (`reliquary-reincarnations`) | Primarily magical tools and artifacts; no known standalone character progression stat. | Use universal gear/perk bonuses; do not invent a separate Reliquary skill. |
| **Summoning Rituals** (`summoning-rituals`) | A KubeJS ritual framework for cross-mod recipes rather than its own player magic progression. | Use it as integration infrastructure and recipe gating, not a standalone skill tree. |
| **Theurgy** (`theurgy`) | Prior audit found no native player-specific adjustable magic stats; primarily world/item-based systems. | No separate Pufferfish branch until a distinct personal mechanic is identified. |

## Targeted next steps

- **No repeat full audit needed** for Ars Additions, Ars Controle, Ars Elemancy or Ars Elemental. Parent mechanics are source-backed; record only focused in-game regression tests for new glyphs, armor and summoned pets.
- **No independent skill nodes** for Ars Ocultas and Ars Unification; their block/recipe behavior is complementary to the parent systems.
- **Addon follow-up tests:** Ars 'n Spells mana mode and damage stacking; Extra Botany's custom magic damage; 1.21.1 Goety Cataclysm inheritance; Gaze's source/binary. These stay separate from the broader investigation queue.
- **Deferred with parent:** Psi: Tweaks and Additions remains unexamined.
- **Broader unexplored systems:** Psi, Hex Casting, Scriptor Magicae, JAWR, Vampirism and Werewolves, Totemic, Pastel, Enchanted: Witchcraft, Aspect Alchemy, Feywild, Embers, Enigmatic Legacy+.
- **Previously designed work:** Occultism ownership adapter, Roots draft PR #8 first-pickup/Terra synchronization, Primal Magick sorcery-tag bridge.

## Evidence locations

- Upstream Java references and caveats for all ten addons: [addon audit](PUFFERFISH-ADDON-AUDIT-2026-11-13.md).
- Locked pack roster: [manifest/mods.json](../manifest/mods.json).
- Roots prototype: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/8.
- Goety: https://github.com/Vivideru/Goety-3/blob/main/src/main/java/com/Polarice3/Goety/init/ModAttributes.java.
- Primal Magick: https://github.com/daedalus4096/PrimalMagick/blob/MC1.21.1/common/src/main/java/com/verdantartifice/primalmagick/common/attunements/AttunementManager.java.
- Pufferfish pet/damage attribute implementation: https://github.com/pufmat/attributesmod/blob/1.21/Common/src/main/java/net/puffish/attributesmod/mixin/LivingEntityMixin.java.

**No game boot or skill-tree runtime test was performed by this documentation update.**

## B6 resolution update
The originally planned Pufferfish Skills/Attributes pair is now installed for explicit user-approved **experimental client testing**. Skills **0.19.2 beta**, Attributes **0.8.3 stable**, both NeoForge Minecraft 1.21.1, hash-pinned. The **125-mod combined dedicated server reached Done** in [run 43554705678](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43554705678). Improvable Skills 3 with HammerLib is also installed as an independent upgrade system. **A custom Pufferfish skill tree has not yet been created**. Client UI, dynamic-attribute overlaps and game-play feature registration remain unverified. Do not add any default skill-tree mods without an explicit decision.
