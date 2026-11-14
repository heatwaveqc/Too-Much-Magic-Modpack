# Pufferfish Skills × Too Much Magic — 42-mod compatibility matrix

Updated 2026-11-13. Scope: the 42 retained magic-related entries from the original 43-mod B5 survey, including Enigmatic Legacy+. Aspect Alchemy is excluded as of B6 and is not part of this matrix. Pufferfish Skills and Attributes are both pinned and dedicated-server-smoked in B6; the custom skill tree is not yet authored or gameplay-tested. **Psi and Psi: Tweaks and Additions have now been source-audited; native hooks do not imply gameplay-tested integration.**

**How to read these states:** Confirmed means source-verified player stats/hooks OR demonstrably inherited parent spell-casting hooks, **not runtime-tested skill tree integration**. Compatible with work means a concrete bridge, configuration or implementation test remains. Needs investigation means evidence is insufficient and incompatibility cannot be inferred. Proven incompatible requires demonstrable impossibility (none so far). Nothing separate to enhance means there is no justified *independent mod-specific player progression track*, not that universal Pufferfish attributes cannot apply.

**Clarified design:** unify *character skill progression* and compatible caster bonuses across schools. **Ars Nouveau + Iron's Spells use Ars 'n Spells' shared mana bar; Psi is not part of this shared pool** and keeps its player Psi/CAD battery. Hex Casting media stays native. Avoid double-applying spell multipliers. PsiTweaks-specific balance choices are provisional. Psi-generated FE is favored and could replace BiggerReactors after measured throughput and a later decision. The ten-addons pass is documented in [PUFFERFISH-ADDON-AUDIT-2026-11-13.md](PUFFERFISH-ADDON-AUDIT-2026-11-13.md), including upstream Java references, version caveats, and the Ars 'n Spells mana-unification design issue.

## Summary

| State | Count |
|---|---:|
| Confirmed (source-backed and parent-inherited) | **15** |
| Compatible with work | **9** |
| Needs investigation | **10** |
| Proven incompatible | **0** |
| Nothing separate to enhance | **8** |
| **Total** | **42** |

## Confirmed — source-backed or parent-inherited Pufferfish compatibility (15)

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
| **Psi** (`psi`) | Psi 110 defines synchronized player attributes `psi:total_psi` (default 5000) and `psi:regen` (default 25/t), a complete NeoForge `PsiEvents` KubeJS API including `preSpellCast`, `regenPsi`, and `cadStat`, and datapack-defined CAD stats and piece unlocks. | Bind Pufferfish capacity/regeneration directly; test event-based cost reductions and separate item-based CAD crafting perks. CAD stats are item properties, and compile-time limits are checked before pre-cast cost modifiers. Review PsiTweaks penalty defaults and resource interoperability. |
| **Psi: Tweaks and Additions** (`psi-tweaks`) | Pinned 0.10.10-hotfix source registers the player attribute `psitweaks:spell_damage_factor` and applies it in multiple damaging tricks; addon equipment also buffs Psi max/regeneration and spell damage, confirming parent integration. | Apply Pufferfish damage rewards to that attribute for supported addon spells, verify coverage/double stacking, and audit Ideaspace Storage, FE Supply, Mass Block Break, Time Accelerate and configurable Psi penalty overrides before enabling skill boosts. |

## Compatible with work — concrete integration path identified (9)

| Mod | Current evidence / rationale | Next action |
|---|---|---|
| **Ars 'n Spells** (`ars-n-spells`) | Bridges Ars and Iron's mana/spell damage; NeoForge SpellDamageEvent.Pre handler adds Iron's power to Ars damage without explicitly recounting Ars damage perk. | **Shared Ars/Iron's mana is desired**. Test the built-in `iss_primary` default (Iron's authoritative) against pinned 3.3.5; verify client display, casting costs and Pufferfish spell-power stacking. Psi is explicitly excluded from this shared mana. |
| **Extra Botany Revived** (`extra-botany-revived`) | Botania addon with custom DamageHandler, damage types, gear/Curios attributes, and custom-owned entities that do not implement OwnableEntity. | Review magic-damage tags and direct-health attacks; test universal attributes and item enhancements. |
| **Goety Cataclysm** (`goety-cataclysm`) | Cataclysm spells/servants architecturally extend Goety Spell and Summoned in public 1.20.1 source, but the pinned 1.21.1 code is not matched. | Inspect exact 1.21.1 jar/source and test school potency, soul discount and owned-servant damage. |
| **HexCast NEOFORGED** (`hexcast-neoforged`) | Exact `0.11.1-19+neoforge.1.21.1` public port sources provide `CastingEnvironment.addCreateEventListener`, `ExtractMedia` / `IsVecInRange` components and user/circle environments. Media is item/circle based, with no native personal media capacity/regen attribute discovered. | An **opt-in 10%/20% media-efficiency NeoForge bridge pilot** now passes 65 Java assertions, builds against exact pinned HexCast + NeoForge libraries and **boots in the full B6 dedicated server**; player casting and real media spending remain untested. It is **not** installed in normal packwiz. Keep Hex media separate from Ars/Iron's shared mana and Psi; test native charging, overcast, packaged casts and circles. No native KubeJS skill event verified. Test circles, packaged casts, server config denylist and lore/great-spell gates. [Audit](HEXCAST-SKILLS-DEEP-DIVE-2026-11-13.md). |
| **Gaze (Malum addon)** (`gaze-malum`) | Gaze 1.1.7 release notes say *some* projectiles benefit from Malum Arcane Resonance, a registered/synced player attribute in Malum 1.21.1. The pinned 1.1.7.1 retains Malum 1.8 support; Gaze bytecode not inspected. | Test which projectiles respond to Pufferfish `malum:arcane_resonance`, remaining custom weapon/Geas paths, Staff Charge recovery and trial Iron's compatibility. No separate Gaze tree unless a genuinely independent player stat is found. |
| **Occultism** (`occultism`) | Ordinary spirits extend TamableAnimal; generic pet attributes should apply, but special familiars use custom PathfinderMob ownership. | Test spirits; build reusable owner adapter for familiars if needed. |
| **Primal Magick** (`primal-magick`) | A single datapack damage tag can bridge primalmagick:is_sorcery to Pufferfish magic attributes. Attunement has public setters; pixies/golems use custom ownership. | Test tag; add reusable companion owner bridge; defer skill→attunement if unnecessary. |
| **Scriptor Magicae** (`scriptor-magicae`) | Source-backed cooldown in `EntityCastCooldownExtension` (private data, no Minecraft attribute), explicit book/cast cost and lectern behavior; `Inflame` smelts furnace recipes, `Break` mines existing ore, `Bring` transports drops, `Place` generates magic blocks (not ore). Lectern caster inventory has a likely null getter bug. | **No Mana Mode.** Personal cooldown/crafted-item-quality bridge candidates; **keep lecterns powerful** pending actual 2.0.14 gameplay tests of ores, smelting and collection. [Audit](SCRIPTOR-LECTERN-SKILLS-AUDIT-2026-11-13.md). |
| **Roots Classic** (`roots-classic`) | Opt-in KubeJS first-pickup Crystal Staff imprinting and server/client Terra attachment mirroring prototyped in PR #8 and carried into the B6 reconciliation branch; not gameplay-tested. | Test script loading, staff components and 40→60 Terra HUD across login, death and dimensions. |

## Needs investigation — insufficient source evidence yet (10)

| Mod | Current evidence / rationale | Next action |
|---|---|---|
| **Embers Re-Ignited** (`embers-re-ignited`) | Magical industry/resource system not audited for player-specific upgrades. | Check whether player-based casting or skill-scalable crafting exists. |
| **Enchanted: Witchcraft** (`enchanted-witchcraft`) | Rites, poppets and magical progression have not been examined for player-level stat hooks. | Determine whether there are personal magic skills or just item/ritual effects. |
| **Enigmatic Legacy+** (`enigmatic-legacy-plus`) | Artifact-driven personal powers and possible progression; no stat API audit yet. | Determine whether relevant benefits are player attributes or purely equipment-based. |
| **Feywild** (`feywild`) | Fey summon/quest system; pet ownership interface unexamined. | Check summon entities, owner tracking, pact mechanics and generic pet buffs. |
| **JAWR: Forbidden Magic** (`forbidden-magic`) | JAWR: Forbidden Magic ten-level lich system has been researched conceptually, not for exact player attribute/API hooks. | Investigate lich attributes, phylactery, necromancy summoning and vampire hybrid mechanics. |
| **Just Another Witchery Remake** (`witchery-remake`) | JAWR 0.5.39 has native vampirism/lycanthropy and ritual progression; accessible public repository does not expose core Java implementation. | Inspect released JAR, player capability, spell/ritual hooks and transformation progression. |
| **Pastel** (`pastel`) | Color-resonance/research magic has not been mapped to player attributes. | Inspect player facets, spell potency and crafting traits. |
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
- **Addon follow-up tests:** Ars 'n Spells separate-pool mode and damage stacking; Extra Botany's custom magic damage; 1.21.1 Goety Cataclysm inheritance; Gaze's remaining projectile paths/bytecode. These stay separate from the broader investigation queue.
- **Reviewed with parent:** Psi and Psi: Tweaks and Additions have source-backed player attribute and KubeJS hooks. **Psi does not join Ars/Iron's shared mana**; evaluate provisional penalty/storage/automation tuning and Psi-generated FE as a candidate alternative to BiggerReactors. See [focused audit](PSI-SKILLS-DEEP-DIVE-2026-11-13.md).
- **Broader unexplored systems:** JAWR, Vampirism and Werewolves, Totemic, Pastel, Enchanted: Witchcraft, Feywild, Embers, Enigmatic Legacy+.
- **Previously designed work:** Occultism ownership adapter, Roots draft PR #8 first-pickup/Terra synchronization, Primal Magick sorcery-tag bridge.
- **Gaze:** [focused audit and test plan](GAZE-SKILLS-AUDIT-2026-11-13.md) covers the partial Malum attribute bridge and remaining uncertainty.
- **Psi + PsiTweaks:** [source-backed deep dive](PSI-SKILLS-DEEP-DIVE-2026-11-13.md), including Psi 110 KubeJS events, player/equipment attribute separation, addon research and balance blockers.

## Evidence locations

- Upstream Java references and caveats for all ten addons: [addon audit](PUFFERFISH-ADDON-AUDIT-2026-11-13.md).
- Locked pack roster: [manifest/mods.json](../manifest/mods.json).
- Roots prototype: [local experimental integration](ROOTS-SKILLS-KUBEJS-PROTOTYPE.md), originally developed in https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/8.
- Goety: https://github.com/Vivideru/Goety-3/blob/main/src/main/java/com/Polarice3/Goety/init/ModAttributes.java.
- Primal Magick: https://github.com/daedalus4096/PrimalMagick/blob/MC1.21.1/common/src/main/java/com/verdantartifice/primalmagick/common/attunements/AttunementManager.java.
- Pufferfish pet/damage attribute implementation: https://github.com/pufmat/attributesmod/blob/1.21/Common/src/main/java/net/puffish/attributesmod/mixin/LivingEntityMixin.java.

**No game boot or skill-tree runtime test was performed by this documentation update.**

## B6 resolution update
The originally planned Pufferfish Skills/Attributes pair is now installed for explicit user-approved **experimental client testing**. Skills **0.19.2 beta**, Attributes **0.8.3 stable**, both NeoForge Minecraft 1.21.1, hash-pinned. The **125-mod combined dedicated server reached Done** in [run 43554705678](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43554705678). Improvable Skills 3 with HammerLib is also installed as an independent upgrade system. **A custom Pufferfish skill tree has not yet been created**. Client UI, dynamic-attribute overlaps and game-play feature registration remain unverified. Do not add any default skill-tree mods without an explicit decision.
- **HexCast NEOFORGED:** [port-specific source audit](HEXCAST-SKILLS-DEEP-DIVE-2026-11-13.md) confirms extension seam but no native personal stat; classify as Compatible with work, not complete runtime support.
