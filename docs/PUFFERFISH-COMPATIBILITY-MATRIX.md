# Pufferfish Skills × Too Much Magic — 42-mod compatibility matrix

Updated 2026-11-13. Scope: the 42 retained magic-related entries from the original 43-mod B5 survey, including Enigmatic Legacy+. Aspect Alchemy is excluded as of B6 and is not part of this matrix. Pufferfish Skills and Attributes are both pinned and dedicated-server-smoked in B6; the custom skill tree is not yet authored or gameplay-tested. **Psi and Psi: Tweaks and Additions have now been source-audited; native hooks do not imply gameplay-tested integration.**

**How to read these states:** Confirmed means source-verified player stats/hooks OR demonstrably inherited parent spell-casting hooks, **not runtime-tested skill tree integration**. Compatible with work means a concrete bridge, configuration or implementation test remains. Needs investigation means evidence is insufficient and incompatibility cannot be inferred. Proven incompatible requires demonstrable impossibility (none so far). Nothing separate to enhance means there is no justified *independent mod-specific player progression track*, not that universal Pufferfish attributes cannot apply.

**Clarified design:** unify *character skill progression* and compatible caster bonuses across schools. **Ars Nouveau + Iron's Spells use Ars 'n Spells' shared mana bar; Psi is not part of this shared pool** and keeps its player Psi/CAD battery. Hex Casting media stays native. Avoid double-applying spell multipliers. PsiTweaks-specific balance choices are provisional. Psi-generated FE is favored and could replace BiggerReactors after measured throughput and a later decision. The ten-addons pass is documented in [PUFFERFISH-ADDON-AUDIT-2026-11-13.md](PUFFERFISH-ADDON-AUDIT-2026-11-13.md), including upstream Java references, version caveats, and the Ars 'n Spells mana-unification design issue.

**Scope boundary, approved 2026-11-14:** **Transformation Compat** owns every persistent physiology/faction and death-system interaction: two lichdoms and Archlich, vampires, werewolves, dragons, abominations, hunters, phylacteries, resurrections, soul loss, graves and death-cancel priorities. Those are **not Pufferfish discovery blockers**. A mod stays in Needs Investigation **only if** a separate meaningful personal-casting/skill integration question remains. **Goety remains Confirmed because it has independent spellcasting/soul/servant attribute hooks, not because of its lich transformation.** JAWR base remains under investigation only for non-transformation witchcraft/ritual casting. Dragon Survival is not part of the installed 42-entry survey.

## Summary

| State | Count |
|---|---:|
| Confirmed (source-backed and parent-inherited) | **17** |
| Compatible with work | **10** |
| Needs investigation | **3** |
| Proven incompatible | **0** |
| Nothing separate to enhance | **12** |
| **Total** | **42** |

## Confirmed — source-backed or parent-inherited Pufferfish compatibility (17)

| Mod | Current evidence / rationale | Next action |
|---|---|---|
| **Ars Additions** (`ars-additions`) | Extra glyphs extend Ars AbstractEffect and reuse its spell pipeline; ReachPerk adds vanilla interaction-range attributes. | No independent tree needed. Test new glyph stats and perk/attribute stacking. |
| **Ars Controle** (`ars-controle`) | Precise Delay extends Ars AbstractEffect and uses the existing SpellStats/SpellContext pipeline; other features are spell-control utilities. | Verify Ars caster perks apply to newly added glyphs. No dedicated Controle player stat. |
| **Ars Elemancy** (`ars-elemancy`) | Focus inherits Ars Elemental GreaterElementalFocus and modifies SpellStats; armor extends Ars gear and uses PerkAttributes/IManaDiscountEquipment. | Test focus amplification and armor/regen stacking with Pufferfish buffs. |
| **Ars Elemental** (`ars-elemental`) | Damage glyphs extend Ars AbstractEffect and use SpellStats/SpellResolver; hence normal Ars spell perks are inherited. | Test custom damage types, elemental summons and ownership separately; no automatic generic-pet claim. |
| **Ars Nouveau** (`ars-nouveau`) | Native spell-related attributes are source-verified; central Ars casting path is a clear Pufferfish integration target. | Map skill rewards to actual attribute IDs and test with Ars spells. |
| **Botania Revived** (`botania-revived`) | Prior audit identified player-relevant, source-verified enhancement mechanisms (including PixieHandler). | Validate cross-loader and port-specific behavior; avoid claiming Pufferfish runtime proof. |
| **Embers Re-Ignited** (`embers-re-ignited`) | Exact 1.21.1-1.5.9 upstream source exposes `EmberRemoveEvent` (player item-based Ember expense) and `EmberProjectileEvent` (Cinder Staff and Blazing Ray); `AugmentUtil` manipulates persistent equipment augments and heat. Most features are machines and item effects, not a native player mana or magic attribute. | Source-backed hooks **Confirmed**, gameplay/Pufferfish bridge not implemented: trial bounded per-player expenditure discount, projectile scaling and optional at-craft gear quality; guard Curios discount stacking and pre-discount weapon reserve checks. Separate Ember-to-FE dynamo at theoretical 1,000 FE/t; audit dynamic metal seed renewability and static uranium exception. [Audit](EMBERS-SKILLS-AND-ECONOMY-AUDIT-2026-11-13.md). |
| **Enigmatic Legacy+** (`enigmatic-legacy-plus`) | Exact pinned 1.21.1-1.1.2 source registers three real living-entity attributes: `enigmaticlegacyplus:etherium_shield`, `enigmaticlegacyplus:projectile_deflect`, `enigmaticlegacyplus:lifesteal` and explicitly adds them to every living type. Its spellstones offer passive and keyed active abilities using **shared item cooldown**, not mana. | **Confirmed native player attributes** (gameplay untested); **User-approved: no separate Enigmatic skill tree**. Universal Pufferfish defensive/offensive nodes *could* use attributes without a custom bridge after balance checks. **Death-handler, Soul Crystal, Tombstone and Lost Engine cross-mod cooldown tests are tracked outside the Pufferfish skills investigation** (Transformation Compat owns death/state concerns). [Audit](ENIGMATIC-LEGACY-PLUS-1.1.2-SKILLS-DEATH-AUDIT-2026-11-14.md). |
| **Eidolon: Repraised** (`eidolon-repraised`) | EidolonAttributes in the 1.21.1 repository establish native stat hooks. | Wire Pufferfish modifiers; test. |
| **Forbidden & Arcanus** (`forbidden-arcanus`) | FAAttributes in the 1.21.1 repository provide native stat hooks. | Wire Pufferfish modifiers; test. |
| **Goety — The Dark Arts** (`goety`) | 27 registered spell/school attributes, plus owned servant mechanics, inspected in Goety-3 1.21.1 source. | Test school/global precedence, cooldown edge cases, servant melee/ranged/spells. |
| **Iron's Spells 'n Spellbooks** (`irons-spells`) | Native caster/spell attributes are source-verified. | Check attribute values in game and test spell power, cooldown, mana. |
| **Mahou Tsukai** (`mahou-tsukai`) | Player Mahou maximum can be changed with documented mod commands (e.g. addmaxmahou); prior source/command audit found a controllable personal stat. | Wire skill-level deltas to commands; test refunds and persistence. Not zero-code. |
| **Malum** (`malum`) | The mod exposes player-adjustable Malum attributes in its public 1.21.1 code. | Connect attributes and test resulting spell/spirits effects. |
| **Neo Vitae** (`neovitae`) | Prior audit found accessible player magic stat/API integration points. | Map exact registered API and attributes into the final tree; runtime test. |
| **Psi** (`psi`) | Psi 110 defines synchronized player attributes `psi:total_psi` (default 5000) and `psi:regen` (default 25/t), a complete NeoForge `PsiEvents` KubeJS API including `preSpellCast`, `regenPsi`, and `cadStat`, and datapack-defined CAD stats and piece unlocks. | Bind Pufferfish capacity/regeneration directly; test event-based cost reductions and separate item-based CAD crafting perks. CAD stats are item properties, and compile-time limits are checked before pre-cast cost modifiers. Review PsiTweaks penalty defaults and resource interoperability. |
| **Psi: Tweaks and Additions** (`psi-tweaks`) | Pinned 0.10.10-hotfix source registers the player attribute `psitweaks:spell_damage_factor` and applies it in multiple damaging tricks; addon equipment also buffs Psi max/regeneration and spell damage, confirming parent integration. | Apply Pufferfish damage rewards to that attribute for supported addon spells, verify coverage/double stacking, and audit Ideaspace Storage, FE Supply, Mass Block Break, Time Accelerate and configurable Psi penalty overrides before enabling skill boosts. |

## Compatible with work — concrete integration path identified (10)

| Mod | Current evidence / rationale | Next action |
|---|---|---|
| **Ars 'n Spells** (`ars-n-spells`) | Bridges Ars and Iron's mana/spell damage; NeoForge SpellDamageEvent.Pre handler adds Iron's power to Ars damage without explicitly recounting Ars damage perk. | **Shared Ars/Iron's mana is desired**. Test the built-in `iss_primary` default (Iron's authoritative) against pinned 3.3.5; verify client display, casting costs and Pufferfish spell-power stacking. Psi is explicitly excluded from this shared mana. |
| **Feywild** (`feywild`) | 1.21.1-5.6.4 source confirms scroll-summoned `FeyBase extends PathfinderMob implements IOwnable` with persistent UUID, follow/stay/ability toggle, seasonal pixies doing farm work, and combat/mount Ents. **Custom owner interface, not vanilla TamableAnimal**; no player mana or spell-power attribute. | **Compatible with work (initial source pass):** generic pet buffs may require shared owner adapter with Occultism/Goety; practical utility bonuses need separate goal patches and may not justify work. **Player has not decided Feywild skill integration.** High-priority test: source packet handlers for ability/dismiss lack explicit owner check. Winter Pixie harvests live animal loot; preserve automation until assessed. [Audit](FEYWILD-5.6.4-INITIAL-SKILLS-AUDIT-2026-11-14.md). |
| **Extra Botany Revived** (`extra-botany-revived`) | Botania addon with custom DamageHandler, damage types, gear/Curios attributes, and custom-owned entities that do not implement OwnableEntity. | Review magic-damage tags and direct-health attacks; test universal attributes and item enhancements. |
| **Goety Cataclysm** (`goety-cataclysm`) | Cataclysm spells/servants architecturally extend Goety Spell and Summoned in public 1.20.1 source, but the pinned 1.21.1 code is not matched. | Inspect exact 1.21.1 jar/source and test school potency, soul discount and owned-servant damage. |
| **HexCast NEOFORGED** (`hexcast-neoforged`) | Exact `0.11.1-19+neoforge.1.21.1` public port sources provide `CastingEnvironment.addCreateEventListener`, `ExtractMedia` / `IsVecInRange` components and user/circle environments. Media is item/circle based, with no native personal media capacity/regen attribute discovered. | An **opt-in 10%/20% media-efficiency NeoForge bridge pilot** now passes 65 Java assertions, builds against exact pinned HexCast + NeoForge libraries and **boots in the full B6 dedicated server**; player casting and real media spending remain untested. It is **not** installed in normal packwiz. Keep Hex media separate from Ars/Iron's shared mana and Psi; test native charging, overcast, packaged casts and circles. No native KubeJS skill event verified. Test circles, packaged casts, server config denylist and lore/great-spell gates. [Audit](HEXCAST-SKILLS-DEEP-DIVE-2026-11-13.md). |
| **Gaze (Malum addon)** (`gaze-malum`) | Gaze 1.1.7 release notes say *some* projectiles benefit from Malum Arcane Resonance, a registered/synced player attribute in Malum 1.21.1. The pinned 1.1.7.1 retains Malum 1.8 support; Gaze bytecode not inspected. | Test which projectiles respond to Pufferfish `malum:arcane_resonance`, remaining custom weapon/Geas paths, Staff Charge recovery and trial Iron's compatibility. No separate Gaze tree unless a genuinely independent player stat is found. |
| **Occultism** (`occultism`) | Ordinary spirits extend TamableAnimal; generic pet attributes should apply, but special familiars use custom PathfinderMob ownership. | Test spirits; build reusable owner adapter for familiars if needed. |
| **Primal Magick** (`primal-magick`) | A single datapack damage tag can bridge primalmagick:is_sorcery to Pufferfish magic attributes. Attunement has public setters; pixies/golems use custom ownership. | Test tag; add reusable companion owner bridge; defer skill→attunement if unnecessary. |
| **Scriptor Magicae** (`scriptor-magicae`) | Source-backed cooldown in `EntityCastCooldownExtension` (private data, no Minecraft attribute), explicit book/cast cost and lectern behavior; `Inflame` smelts furnace recipes, `Break` mines existing ore, `Bring` transports drops, `Place` generates magic blocks (not ore). Lectern caster inventory has a likely null getter bug. | **No Mana Mode.** Personal cooldown/crafted-item-quality bridge candidates; **keep lecterns powerful** pending actual 2.0.14 gameplay tests of ores, smelting and collection. [Audit](SCRIPTOR-LECTERN-SKILLS-AUDIT-2026-11-13.md). |
| **Roots Classic** (`roots-classic`) | Opt-in KubeJS first-pickup Crystal Staff imprinting and server/client Terra attachment mirroring prototyped in PR #8 and carried into the B6 reconciliation branch; not gameplay-tested. | Test script loading, staff components and 40→60 Terra HUD across login, death and dimensions. |

## Needs investigation — insufficient source evidence yet (3)

| Mod | Current evidence / rationale | Next action |
|---|---|---|
| **Just Another Witchery Remake** (`witchery-remake`) | Native witchcraft, brewing and rituals may have player-cast abilities distinct from its vampire/lycanthrope progression. Public source insufficient for exact 0.5.39 spell/ritual hooks. | **Investigate non-transformation casting only:** are there player attributes or meaningful spell/ritual buffs that can share existing Pufferfish skills? Vampire, werewolf, death and stacking mechanics belong exclusively to Transformation Compat. |
| **Pastel** (`pastel`) | Color-resonance/research magic has not been mapped to player attributes. | Inspect player facets, spell potency and crafting traits. |
| **Totemic** (`totemic`) | Ceremony/totem magic source-level player mechanics not audited. | Inspect ceremonial skill, musician performance bonuses and buff scaling. |

## Proven incompatible (0)

None demonstrated.

## Nothing separate to enhance — no distinct player skill track (12)

| Mod | Current evidence / rationale | Next action |
|---|---|---|
| **Ars Ocultas** (`ars-ocultas`) | Adds Occultism spirits to Ars Containment Jars and recipes; inspected registerGlyphs is empty. No independent player spell resource. | Retain Ars and Occultism personal skill paths; check only processing/recipe economy. |
| **Ars Unification** (`ars-unification`) | Reuses existing Ars Crush/Cut glyphs for other mods' processing recipes, including Occultism Crusher Spirits. | No dedicated Pufferfish branch; review resource/recipe loops only. |
| **Astral Sorcery** (`astral-sorcery`) | Deliberate exemption: Astral Sorcery already has its own sophisticated personal perk tree. | Preserve its internal perk tree; generic damage attributes may still affect combat. |
| **EvilCraft** (`evilcraft`) | Prior audit found no personal adjustable magic stat; machinery, items and blood resources dominate. | Generic magic/crafting perks can still apply; no dedicated tree currently justified. |
| **Enchanted: Witchcraft** (`enchanted-witchcraft`) | Pinned 4.2.7 uses structure-owned altar power, physical circle rites, Kettle, ingredient workshop, player-bound poppets and a cat familiar, but has no player caster/mana attribute. A modest personal upgrade would require new code and attribution/persistence rules. | **User decision: no dedicated Pufferfish skills and no custom bridge planned**; preserve Enchanted's own gameplay. Still test poppets vs Totems/Tombstone/transformation death handling. [Deep dive](ENCHANTED-WITCHCRAFT-4.2.7-DEEP-DIVE-2026-11-13.md). |
| **JAWR: Forbidden Magic** (`forbidden-magic`) | Already has a ten-level lich ability/progression track; necromancy, phylactery and undead-state mechanics are not a reason to invent a second Pufferfish lich tree. No sufficiently valuable *distinct* Pufferfish enhancement has been identified. | **User decision: closed for dedicated Pufferfish integration.** Preserve native lich advancement. **Transformation Compat** owns dual-lich Archlich and all death/phylactery/state interactions; universal combat/pet attributes can still apply without reopening this audit. [Initial research](JAWR-FORBIDDEN-MAGIC-0.5.37-INITIAL-AUDIT-2026-11-14.md). |
| **Vampirism — Become a Vampire!** (`vampirism`) | Vampire/hunter faction and skill progression are native to the mod; player transformation, feeding, blood, faction state and death handling do not require a duplicate Pufferfish school. | **No dedicated Pufferfish vampire/hunter tree planned.** Transformation Compat owns faction/body/food/death interactions. Shared generic Pufferfish skills remain available as applicable. |
| **Werewolves — Become a Beast!** (`werewolves`) | Wolf-state, forms and faction progression are native to the Vampirism addon; no independent Pufferfish casting school is justified. | **No dedicated Pufferfish werewolf tree planned.** Transformation Compat owns form/state, hunger, mixed states and death rules. Generic universal Pufferfish attributes may still apply. |
| **Hexerei** (`hexerei`) | Prior audit found no native player-specific attribute system; mostly witchcraft items/rituals and brews. | Reopen if broom/brewing perks or personal effects merit enhancement. |
| **Reliquary Reincarnations** (`reliquary-reincarnations`) | Primarily magical tools and artifacts; no known standalone character progression stat. | Use universal gear/perk bonuses; do not invent a separate Reliquary skill. |
| **Summoning Rituals** (`summoning-rituals`) | A KubeJS ritual framework for cross-mod recipes rather than its own player magic progression. | Use it as integration infrastructure and recipe gating, not a standalone skill tree. |
| **Theurgy** (`theurgy`) | Prior audit found no native player-specific adjustable magic stats; primarily world/item-based systems. | No separate Pufferfish branch until a distinct personal mechanic is identified. |

## Scoped closure note (2026-11-14)
- **Forbidden Magic, Vampirism and Werewolves:** closed to *dedicated* Pufferfish skill development because their relevant character progression is native. Source/JAR details can be investigated by **Transformation Compat**, without reopening the Pufferfish matrix. Universal player or pet bonuses do not constitute a mod-specific new tree.
- **Goety:** keep native caster/servant integration status; do not transfer ordinary Goety spellcasting to Transformation Compat. Only Goety's lich physiology/death-state interactions transfer.
- **JAWR base:** keep in Needs Investigation strictly for ordinary witchcraft casting, ritual abilities or any genuinely distinct player skills, not the vampire/werewolf implementation.

## Targeted next steps

- **No repeat full audit needed** for Ars Additions, Ars Controle, Ars Elemancy or Ars Elemental. Parent mechanics are source-backed; record only focused in-game regression tests for new glyphs, armor and summoned pets.
- **No independent skill nodes** for Ars Ocultas and Ars Unification; their block/recipe behavior is complementary to the parent systems.
- **Addon follow-up tests:** Ars 'n Spells separate-pool mode and damage stacking; Extra Botany's custom magic damage; 1.21.1 Goety Cataclysm inheritance; Gaze's remaining projectile paths/bytecode. These stay separate from the broader investigation queue.
- **Reviewed with parent:** Psi and Psi: Tweaks and Additions have source-backed player attribute and KubeJS hooks. **Psi does not join Ars/Iron's shared mana**; evaluate provisional penalty/storage/automation tuning and Psi-generated FE as a candidate alternative to BiggerReactors. See [focused audit](PSI-SKILLS-DEEP-DIVE-2026-11-13.md).
- **Broader unexplored Pufferfish systems:** JAWR's non-transformation casting, Totemic ceremonies and Pastel magic.
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
