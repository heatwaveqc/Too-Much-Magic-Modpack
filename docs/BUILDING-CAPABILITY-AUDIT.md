# B13 building-functionality audit and decoration/beach additions

**Date:** 2026-11-15  
**Target:** Minecraft 1.21.1, NeoForge 21.1.265  
**Scope:** Current full-integration baseline, plus owner-approved B13 candidates. This is a **source-and-design audit**, not a declaration of in-world functionality or a client test.

## Decision (2026-11-15)

**Selected gameplay additions (6):**
- **Crystalix 3.0.3** — configurable transparent, lit, ethereal/mob-passable and actually Wither-proof glass.
- **[Let's Do] Beachparty 2.1.5** — seaside furniture, palmwood, bar/coconut drinks and loungers.
- **Rechiseled 1.2.5** — chiseled decorative palettes/stairs/slabs/connected textures; no simultaneous Chipped install.
- **Brewin' and Chewin' 4.5.0** — Farmer's Delight keg/fermentation, not another basic cooking mod.
- **[Let's Do] Furniture 1.1.10** — general furniture, compatibility for Beachparty palm furniture.
- **Cocktails Delight 1.1.12** — mixed drinks including Piña Colada; contains new combat/XP effects requiring balance audit.

**Necessary support (3):** SuperMartijn642 Core Lib 1.1.20, SuperMartijn642 Config Lib 1.1.8, Fusion connected textures 1.3.16 (client-only). Do not mark installed until hash-pinned and dedicated-server smoke; no client test claimed.

**Do NOT add:** FramedBlocks, Effortless Building, Building Gadgets 2 / Construction Gadgets, independent Formation Wand, Chipped, or duplicate cooking/fishing frameworks. An unrelated block-shape mod may be revisited only if a verified player need survives this audit. Owner explicitly said **no Building Gadgets**.

**Source of truth:** [manifest/mods.json](../manifest/mods.json) is the selection roster; [index.toml](../index.toml), [pack.toml](../pack.toml), and mods/*.pw.toml determine installed bytes. The presence of a selected row is NOT installation.

## Hard audit: capabilities actually available in current major mods

Evidence levels: **source-confirmed** = upstream target-version source/guide establishes mechanics; **historical/conditional** = mechanic exists in another version or port, requires confirmation on our 1.21.1 fork; **unverified** = candidate possibility only. Installed baseline is source-pinned but client/world behavior needs testing.

| Need | Existing mod and exact tool | Coverage / limits | Evidence |
| --- | --- | --- | --- |
| Rapid straight-line placement | **Astral Sorcery Formation Wand** | **Source-confirmed:** line toward player, line from player; consumes inventory blocks, previews shape | [Astral 1.21.1 localization/tome source](https://github.com/HellFirePvP/AstralSorcery/blob/1.21.1/src/main/resources/assets/astralsorcery/lang/en_us.json) |
| Large planes, floors and vertical walls | **Astral Formation Wand** | **Source-confirmed:** horizontal plane and vertical plane; lumen improves reach; can randomize several selected block types | [Astral 1.21.1](https://github.com/HellFirePvP/AstralSorcery/blob/1.21.1/src/main/resources/assets/astralsorcery/lang/en_us.json) |
| Solid/hollow spheres, domelike shells | **Astral Formation Wand** | **Source-confirmed:** solid sphere and hollow sphere. Trimming a sphere into a dome is manual/other tool | [Astral 1.21.1](https://github.com/HellFirePvP/AstralSorcery/blob/1.21.1/src/main/resources/assets/astralsorcery/lang/en_us.json) |
| Connected-surface replacement | **Astral Sorcery Conversion Wand** | **Source-confirmed:** replaces connected exposed blocks of one type, consumes target blocks from inventory; old blocks properly broken/returned, refuses unbreakable and very hard blocks; adjustable area from 5x5 to 11x11 shown in lang source. Not unrestricted 3-D flood-fill | [Astral 1.21.1](https://github.com/HellFirePvP/AstralSorcery/blob/1.21.1/src/main/resources/assets/astralsorcery/lang/en_us.json) |
| Directed/adaptive placement | **Ars Nouveau Place Block glyph** | **Source-confirmed:** uses caster inventory/hotbar, can augment AoE and Sensitive orientation. Can use Touch or Projectile casting forms; not a blueprint copier | [Ars 1.21.1 glyph catalog](https://ars.guide/1.21.1/docs/spell_theory/glyphs/) |
| Surface/area demolition | **Ars Break glyph** | **Source-confirmed:** AoE/power augments, resource cost, harvest restrictions. Not automatic repair or undo | [Ars 1.21.1](https://ars.guide/1.21.1/docs/spell_theory/glyphs/) |
| Exchange area without losing originals | **Ars Exchange glyph** | **Source-confirmed:** swaps blocks with inventory/hotbar targets, Silk Touch-type return; AoE and Amplify for harder blocks | [Ars 1.21.1](https://ars.guide/1.21.1/docs/spell_theory/glyphs/) |
| Temporary openings/passages | **Ars Intangible glyph** | **Source-confirmed:** blocks temporarily become air; combine AoE/duration; **not** an actual long-term construction editor | [Ars 1.21.1](https://ars.guide/1.21.1/docs/spell_theory/glyphs/) |
| Remote/automated block handling | **Ars Spell Turrets and spellbooks** | **Source-supported concept:** repeat supported spells on targets; exact autonomous multi-block behavior and protection checks need in-pack testing | [Ars profile](mod-comprehension/mods/ars-nouveau.md) |
| Surface remodeling | **Botania Revived — Rod of the Shifting Crust** | **Current port source-confirmed:** actively exchanges selected block surfaces using inventory replacement blocks and mana (source has 3-block-range base and larger proficiency range), with item/block-entity restrictions. **Exact pinned 2.0.2 behavior remains to be world-tested**; Astral/Ars overlap | [Botania Revived ShiftingCrustRodItem.java](https://github.com/Velron-Devs/Botania-Revived/blob/main/src/main/java/velrondevs/botania/common/item/rod/ShiftingCrustRodItem.java) |
| Terrain leveling and generation | **Botania Revived — Rod of the Terra Firma, Rod of the Lands, Rod of the Depths** | **Current port source-confirmed:** Terra Firma Rod processes tagged terraformable surface terrain, removes land above player's level and fills suitable gaps with dirt below. Range 16 blocks, 22 with proficiency; checks sea level; charges 55 mana per change. Lands Rod creates dirt (75 mana), Depths Rod cobblestone (150 mana). **No arbitrary heightmap/bedrock reshaping.** Exact pinned 2.0.2 must still be tested in-world | [TerraFirmaRodItem.java](https://github.com/Velron-Devs/Botania-Revived/blob/main/src/main/java/velrondevs/botania/common/item/rod/TerraFirmaRodItem.java); [LandsRodItem.java](https://github.com/Velron-Devs/Botania-Revived/blob/main/src/main/java/velrondevs/botania/common/item/rod/LandsRodItem.java); [DepthsRodItem.java](https://github.com/Velron-Devs/Botania-Revived/blob/main/src/main/java/velrondevs/botania/common/item/rod/DepthsRodItem.java) |
| Decorative light placement | **Astral Illumination Wand and Cave Illuminator; Ars conjured light; new Crystalix** | Astral 1.21.1 source names dedicated Illumination Wand / Cave Illuminator; light behavior must be world-tested. Crystalix is a block feature, not mass building tool | [Astral 1.21.1](https://github.com/HellFirePvP/AstralSorcery/blob/1.21.1/src/main/resources/assets/astralsorcery/lang/en_us.json) |
| Building materials and variety | **Astral marble, infused wood; Botania and other core magic palettes; Rechiseled and Furniture** | Decorative blocks are distinct from **construction methods**. Rechiseled adds block variants and chiseling, not geometry automation | [Astral 1.21.1](https://github.com/HellFirePvP/AstralSorcery/blob/1.21.1/src/main/resources/assets/astralsorcery/lang/en_us.json); [Rechiseled 1.2.5](https://modrinth.com/mod/rechiseled/version/S5FnErRw) |
| Programmable line placement/breaking | **Psi — Trick: Place in Sequence and Break in Sequence** | **Current 1.21-line source-confirmed:** geometric line of positions through vector ray and maximum block count; consuming source blocks from player inventory for placement, and applying harvest logic for breaks. Costs/potency scale with length. **Not a blueprint editor or general geometric template.** Client/world verification needed for pinned 1.21.1-110 | [PlaceInSequence.java](https://github.com/VazkiiMods/Psi/blob/main/Xplat/src/main/java/vazkii/psi/common/spell/trick/block/PieceTrickPlaceInSequence.java); [BreakInSequence.java](https://github.com/VazkiiMods/Psi/blob/main/Xplat/src/main/java/vazkii/psi/common/spell/trick/block/PieceTrickBreakInSequence.java) |
| Programmable movement of existing blocks | **Psi — Trick: Move Block, Move Block Sequence** | **Current source-confirmed:** shifts selected blocks by unit vectors; sequence filters immovable entities and block entity positions, respects permissions and harvestability. Has an **upstream anti-duplication TODO (#740)**; verify no live dupes and FTB claim interaction before treating as safe for server construction | [MoveBlockSequence.java](https://github.com/VazkiiMods/Psi/blob/main/Xplat/src/main/java/vazkii/psi/common/spell/trick/block/PieceTrickMoveBlockSequence.java) |
| Programmable patterns / arbitrary blueprint replication | **HexCast NEOFORGED and Psi** | **Not established as robust in this exact pack.** Psi vector math and loops may enable creative patterns, but no tested saved arbitrary structure import/export, transform/undo transaction, or template workflow was shown. HexCast needs a separate source audit | [active roster](../manifest/mods.json) |
| EMC-powered construction and retexturing | **ProjectE — Mercurial Eye** | **Current ProjectE source-confirmed:** Creation (single block), Extension and Extension Classic (surface growth), Transmutation and Transmutation Classic (surface replacement), and Pillar (3×3 columns, up to 15 blocks by charge). Uses a Klein Star and a target block with positive EMC. **Late-stage ProjectE unlock; do not advertise as early-game tool or count on unimplemented boss gates.** Pin is PE1.1.0; world verify before claiming exact parity | [ProjectE MercurialEye.java](https://github.com/sinkillerj/ProjectE/blob/main/src/main/java/moze_intel/projecte/gameObjs/items/MercurialEye.java); [pack progression](../docs/DESIGN.md) |

### Direct comparison with construction add-ons

- **Simple placement wands:** redundant. Astral's own Formation Wand is already explicitly named and its source supports multiple advanced shapes. The standalone `formation-wand` manifest candidate is discontinued; no new standalone wand.
- **Bulk replace wands:** redundant. Astral Conversion Wand and Ars Exchange cover their common purpose.
- **Building Gadgets / Construction Gadgets:** no; this is an owner decision regardless of whether a specific blueprint/undo feature would be unique.
- **Effortless Building:** omitted per owner instruction. Mirroring and transforms are unconfirmed gaps, but not enough to override omission.
- **FramedBlocks:** omitted per owner instruction. Its slopes/panels are a *decoration geometry* distinction, not a missing multi-placement method.

## Genuinely missing or unproven, after removing duplicated functionality

**The following capabilities have no verified general-purpose implementation in our current pack; this is a negative evidence boundary, not proof they are impossible in every spell system.**

1. **Arbitrary blueprint capture, save, import/export, copy/paste and repeated building placement** — no verified magic tool for exact large multi-block structure templates. Astral's reference previews are shapes, not recorded arbitrary structures.
2. **Undo/redo transaction history with automatic refund and rollback** — no verified native support in Astral or Ars; manually changing blocks back is not equivalent, especially in survival multiplayer.
3. **Mirroring/rotation of entire placed structures and orientation-aware directional blocks** — not supported by the documented Astral line/plane/sphere modes; not demonstrated in Ars. Rotating a single block or shape preview would not establish full structure transforms.
4. **General customizable curves, arbitrary spline/path tools and non-spherical arches** — Astral supplies spheres, walls and planes, but not verified free-form curves. Rechiseled adds decorative stairs/slabs, not free-form geometry.
5. **Arbitrary terrain heightmaps, below-sea-level grading and multi-material fill/drain of irregular volumes** — **basic flattening/terraforming is already present in Botania Revived Terra Firma** (source-confirmed) but it is limited to terraformable tags, range, sea level and mana. No unconstrained imported heightmap or user-selected geometry is verified.
6. **Cross-chunk unattended blueprint builds and scheduled construction with inventory sourcing** — Ars turrets support automation but no general-purpose survival template planner verified.
7. **Direction-preserving multi-block state transforms** — stairs, slabs, chests, glazed terracotta and multi-part modded blocks need live client tests before recommending any such feature.

**Recommendation:** add *no* further dedicated construction mod now. The confirmed core covers lines, floors, walls, spheres, surface exchange, AoE placement/demolition, Psi programmable sequences, Botania terrain grading, and late-stage ProjectE EMC building via the Mercurial Eye. If repeated arbitrary blueprints become important, evaluate a tightly scoped design instead of contradicting the no-Building-Gadgets decision.

## Player teaching requirements

- Early Ars chapter: teach `Place Block`, `Break`, `Exchange`, `AoE`, `Sensitive`, `Amplify` with inventory and mana constraints. **Do not** teach `not_enough_glyphs:glyph_propagate_plane` merely because a web glyph aggregator lists it: that addon is not in the approved roster.
- Astral chapter: explicitly introduce crafting/learning the **Formation Wand**, its inventory block select/clear, line-to-player, line-from-player, plane, wall, solid sphere and hollow sphere, preview and lumen extension. Then introduce **Conversion Wand** and adjustable contiguous surface range. Mention the **Astral Tome** guidebook.
- Add the six decorative/beverage mods as optional discovery branches: Glass types and Wither-proof caveats; Rechiseled texture conversion; Beachparty furnishings/lounge; Furniture palmwood integration; Brewin' fermentation advancements; Cocktails Delight JEI/wiki.
- If a construction job can be done with Ars or Astral, direct the player to that magic progression before presenting it as a generic QoL task.

## Combined build, client and balance test matrix

1. **Pin/boot:** verify every artifact is correct **NeoForge 1.21.1**, has hash and needed libraries, and boot combined with full baseline. Fusion is client-only. A successful server boot does not prove functional crafting or graphics.
2. **Crystalix:** test Wither spawning blast, direct/blue skulls, block-breaking AI and explosives separately; clear+light+collision combinations and client visibility under Embeddium. Assess accessible Wither-proof arenas before intended boss progression.
3. **Rechiseled + Fusion:** test chisel menu, JEI recipes, CT model rendering, atlas cost, compatibility with Embeddium and ModernFix, no ghost blocks.
4. **Beachparty + Furniture:** test sit/use, bar, palmwood furniture recipes, mirrors and reflections, fish tanks (Aquaculture fish if supported), beach worldgen with Tectonic and existing world saves. Disable/limit unwanted Palm/sand structures only after concrete config verification.
5. **Brewin + Cocktails:** version 4.5 requires Farmer's Delight >=1.3.2 (current pinned version 1.3.4). Verify keg liquids/ferment time, Curios/JEI recipe routing, Pam's recipe compat and Piña Colada in-game.
6. **Balance:** audit Cocktails' `Wisdom` double-XP, arrow Looting and Overdraw, combat buffs; avoid accidental skill XP escalation/stacking with Tyz/other systems. Furniture cash-register, bin junk loot, Beachparty drink buffs must not bypass currency/progression.
7. **Protection:** test Astral and Ars placing/breaking/replacing inside own and other teams' FTB Chunks claims, inventory return, rollback by manual correction; no unbounded cross-claim griefing or dupes.
8. **Magic parity:** create a 7x7 floor, vertical wall, sphere, hollow sphere, mixed-color wall, surface retexture, area mine and remote build using Ars and Astral; then test Botania Terra Firma leveling and Shifting Crust replacement, Psi Place/Break/Move Sequence, and ProjectE Mercurial Eye Creation/Extension/Pillar/Transmutation (after its intended progression gate). Document what actually fails, not what a mod description claims.

### Release evidence (selected pins)

| Mod | Exact provider ID | Source |
| --- | --- | --- |
| Crystalix 3.0.3 | CurseForge 1187033 / 9251153 | https://www.curseforge.com/minecraft/mc-mods/crystalix/files/9251153 |
| Beachparty 2.1.5 | Modrinth GyKzAh3l / HN4rzKRl | https://modrinth.com/mod/lets-do-beachparty/version/HN4rzKRl |
| Rechiseled 1.2.5 | Modrinth B0g2vT6l / S5FnErRw | https://modrinth.com/mod/rechiseled/version/S5FnErRw |
| Brewin' 4.5.0 | Modrinth hIu9KJTT / MbcR48Ou | https://modrinth.com/mod/brewin-and-chewin/version/MbcR48Ou |
| Furniture 1.1.10 | Modrinth 3JQDJrYW / FxbGZtS6 | https://modrinth.com/mod/lets-do-furniture/version/FxbGZtS6 |
| Cocktails Delight 1.1.12 | CurseForge 1198178 / 9482953 | https://www.curseforge.com/minecraft/mc-mods/cocktails-delight/files/9482953 |
| Core Lib 1.1.20 | Modrinth rOUBggPv / MQOzQxLt | https://modrinth.com/mod/supermartijn642s-core-lib/version/MQOzQxLt |
| Config Lib 1.1.8 | Modrinth LN9BxssP / qKL9jM75 | https://modrinth.com/mod/supermartijn642s-config-lib/version/qKL9jM75 |
| Fusion 1.3.16 | Modrinth p19vrgc2 / AYDaLPK8 | https://modrinth.com/mod/fusion-connected-textures/version/AYDaLPK8 |

**Audit completeness boundary:** This source pass covers confirmed construction functionality from **Astral Sorcery, Ars Nouveau, Botania Revived, Psi, and ProjectE**, including genuine terraforming, programmable sequences, and EMC-powered surface/pillar work. These are upstream current-port sources, not binary equality verification against the pinned jars or player-tested mechanics. **HexCast** and some lesser-known magic addons still require individual source profiles; no evidence establishes an arbitrary blueprint editor. The no-duplicate-wand decision is unaffected.
