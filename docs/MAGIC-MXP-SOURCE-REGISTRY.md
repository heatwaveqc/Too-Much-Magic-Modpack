# Universal Magic XP (MXP) — 42-mod source and hook registry

**First-pass research / implementation planning**, 2026-11-14. **Not an implementation or pinned-JAR validation claim.** See [progression contract](MAGIC-MXP-PROGRESSION.md). Each entry is one magic-mod skill audit entry; excludes **Aspect Alchemy** by explicit decision and includes **Enigmatic Legacy+**. This MXP-source inventory is **distinct** from the earlier universal-*skill compatibility* matrix (17 confirmed / 12 work / 0 investigation / 13 no separate enhancement). A mod can merit MXP for rituals even if it deserves no dedicated skills.

## Legend and evidence levels

- **L** = likely low effort: documented mod-native event or generic reliable server event, plus ledger wiring. **M** = medium: requires source-specific callback, transaction/crafter association, or modest Java adapter. **H** = high: payment path can span multiple systems, private internals, item storage, or automation; substantial targeted Java bridge and tests anticipated. **I** = inherits another adapter; little extra code unless a bypass is found. **N** = no distinct integration needed/desired.
- **Hook candidates are not all confirmed.** The strongest *specific* hooks established from source include Totemic's ceremonyStartupSuccess, Embers' EmberRemoveEvent, Neo Vitae's AnimaEvent.PostSyphon, Psi's built-in preSpellCast/spellCast KubeJS events, Iron's SpellOnCastEvent, and Ars Nouveau's SpellCostCalcEvent.Post. Botania Revived centralizes player mana requests but lacks a verified general post-consumption event. See the [focused five-mod audit](MAGIC-MXP-FIVE-MOD-HOOK-REASSESSMENT.md). NeoForge generic boss defeat interception is plausible; bosses may implement special death phases. The KubeJS Iron's Spells **external addon is not currently approved/pinned**; mentioning its PlayerEvents.changeMana API does not authorize installing it.
- All baselines marked TBD require first-access verification in the pinned 1.21.1 world. Initial pool differs from late-game max. World machines are excluded from personal mana normalization.
- MXP categories: **Use** = actual paid personal activated magic, nominal pre-efficiency cost; **Ritual** = player-attributed successful completion; **Craft** = successfully completed significant manual magical craft; **Boss** = shared, registered defeat tracker. First ritual/boss completions grant *fixed* XP and repeat rituals/bosses are divided unless registered as fixed-repeat exceptions.

## Complete inventory

| # | Mod | MXP candidate | Base pool/reference | Candidate hook | Effort | Main uncertainty |
|---:|---|---|---|---|:---:|---|
| 1 | Ars Nouveau | Personal mana use | Shared Ars/Iron's pool (one B) | SpellResolver SpellCostCalcEvent.Post at actual cost; SpellCastEvent may predate payment | M | Filter turret/nonplayer use and Ars 'n Spells sharing; nominal cost vs applied discount |
| 2 | Iron's Spells 'n Spellbooks | Personal mana use | Same B as Ars, no second pool | SpellOnCastEvent.getOriginalManaCost/getManaCost followed by committed native/ANS payment | M | Event precedes debit; channel pulses, recasts, scrolls, cross-casts |
| 3 | Mahou Tsukai | Personal Mahou use | Initial personal Mahou capacity | Actual spent-Mahou operation | H | Ritual circles, passive drain and regen must not fake player casts |
| 4 | Primal Magick | Player spellcasting; magical crafts | Verify starting casting reserve | Inspect wand/spell debit and active crafting completion | H | Do not assume a vanilla-style mana bar |
| 5 | Roots Classic | Personal Terra spells, crystal staffs | Initial Terra capacity | Terra spend on accepted cast; specific staff activations | M | Login/dimension updates; item costs vary |
| 6 | Psi | CAD/player Psi casting | First accessible CAD/player Psi reference | Built-in KubeJS PsiEvents.preSpellCast + PsiEvents.spellCast; PlayerData.deductPsi | L | Computed cost already discounted by CAD; recover compiled nominal base; zero-cost/automation checks |
| 7 | HexCast NEOFORGED | Media spent on active Hex casting | Canonical first accessible media budget TBD | Caster + media payment (multi-item/caster types) | H | Consumables, held media, automated minds, failed Hexes |
| 8 | Scriptor Magicae | Manual spell creation/casting milestone | Not applicable (cooldowns only) | Spellbook writing / manual cast success; NOT autonomous lecterns | M | Cost represents cooldown complexity, not a mana pool |
| 9 | Occultism | Summoning rituals; significant crafts | Not applicable | Ritual success/crafted result, player initiator | M | Ritual circle ownership and demon/automatic mining |
| 10 | Neo Vitae | Personal Anima/EV sigils and bound tools; rituals | First-tier bound Orb/Anima capacity TBD | Native AnimaEvent.PostSyphon with owner UUID, ticket nominal EV, actual drained EV | L | 1.1.53 fires PostSyphon; PreSyphon declared but not posted by Anima.syphon; separate nonpersonal drains |
| 11 | Just Another Witchery Remake (JAWR) | Personal infusion charges; Symbology; rites | Starting personal infusion reserve TBD | Infusion payment/Symbology cast; separate rite success | H | Source shows charge, not a verified interception event |
| 12 | EvilCraft | Blood-fueled actively used items; magical crafting | First usable portable Blood store TBD | Successful item ability and real blood debit | M | Blood extractor transfers/blood farming are not spell usage |
| 13 | Goety | Personal Soul Energy spell use; summons | First usable totem/arca capacity TBD | Spell cast+Soul Energy cost, credited once | H | Servant-fueled income, auto devices and lich healing excluded |
| 14 | Malum | Spirit-based active magic; runework/crafts | No universal player pool established | Active tool effect or completed spirit-craft | M | Do not count ordinary spirit collection or passive equipment |
| 15 | Eidolon: Repraised | Chanting/ritual progression; magical crafting | Not applicable unless personal pool verified | Ritual/craft completion with initiating UUID | M | Confirm meaningful completed rite and repeatable recipes |
| 16 | Forbidden & Arcanus | Player-active relics; arcane crafting/rituals | Verify whether eligible personal charge exists | Item action or arcane crafting completion | H | Aureal/other resource paths need 1.21.1 audit |
| 17 | Hexerei | Cauldron crafts, witchcraft items | Not applicable | Finished cauldron craft, explicit player action | M | Brooms/passive effects should not generate free repeat XP |
| 18 | Enchanted: Witchcraft | Circle rites and Kettle recipes | Not applicable (altar-world power) | RiteType completion (owner UUID exists), Kettle output | M | No bespoke Pufferfish buffs; altar recharging is not personal mana |
| 19 | Astral Sorcery | Altar crafts, attunement/research milestones | Not applicable | Player-performed altar completion | M | Native perk XP stays separate; no starlight transfer farming |
| 20 | Botania Revived | Player-used mana tools; significant mana crafts | First available portable mana container TBD | Central ManaItemHandlerImpl requestMana/requestManaExact/*ForTool with remove=true | M | One narrow Java interception; no general public debit event; skip simulation and energy transfers |
| 21 | Pastel | Ink-powered equipment; personal staffs; potion craft | First accessible Ink storage capacity TBD | InkPowered payment path and successful item use | M | Multi-color ink, Curios/palette, partial spends |
| 22 | Theurgy | Alchemical/ritual recipes | Not applicable | Completed recipe/ritual with real initiator | M | Automated processing and copying outputs do not give free XP |
| 23 | Totemic | Successful ceremonies; ceremony-summoned bosses | Not applicable | Confirmed KubeJS ceremonyStartupSuccess event | M | Participant attribution to players unverified; no minigame buffs |
| 24 | Ars Additions | Parent Ars casts/abilities | Shared Ars baseline | Inherit Ars cast ledger | I | Check unique cost paths only |
| 25 | Ars Controle | Parent Ars spells/control | Shared Ars baseline | Inherit Ars cast ledger | I | No duplicate awards on redirected effects |
| 26 | Ars Elemancy | Parent Ars casting | Shared Ars baseline | Inherit Ars cast ledger | I | Inspect only for bypass of parent spend |
| 27 | Ars Elemental | Parent Ars casting | Shared Ars baseline | Inherit Ars cast ledger | I | Do not award twice for elemental augments |
| 28 | Ars 'n Spells | Shared Ars/Iron's mana bridge | Single shared baseline | Single canonical payment observer at native/ANS committed debit | M | ANS intercepts Iron's setMana; cross-casts, conversions, channels and exceptions need transaction dedup |
| 29 | Ars Ocultas | Ars + Occultism bridge | Use parent for casts; rituals separate | Delegate to Ars or Occultism based on true action | I | Do not double-count cross-mod casting and ritual rewards |
| 30 | Ars Unification | Ars integration | Shared Ars baseline | Inherit Ars adapter | I | Only add if genuinely independent resource use |
| 31 | Extra Botany Revived | Botania mana tool extras | Inherit portable Botania baseline | Inherit Botania adapter; audit new items | I | Item-specific unusual power draws may need extension |
| 32 | Gaze | Malum addon content | Inherit Malum where applicable | Inherit Malum/boss-global adapter | I | No independent resource assumed |
| 33 | Goety Cataclysm | Goety spells and Cataclysm summons/bosses | Goety Soul Energy baseline | Inherit Goety; eligible boss kills through global tracker | I | No award for summoning alone |
| 34 | Psi: Tweaks and Additions | Psi casting and FE tricks | Psi baseline | Inherit Psi active cast adapter | I | Passive/generated FE is not a separate MXP source |
| 35 | JAWR: Forbidden Magic | Native lich progression, rituals/quests | No separate MXP pool | Ritual completion only if player-performed; boss-global otherwise | N | Lich leveling/death owned by Transformation Compat |
| 36 | Embers Re-Ignited | Ember-consuming player weapons/tools | First accessible portable Ember store TBD | **Confirmed** EmberRemoveEvent for Player debit | L | Use pre-reduction amount; cannot credit machine Ember removal |
| 37 | Feywild | Fey Altar crafts, active companion summoning, bosses | Not applicable | Completed manual scroll craft; boss-global tracker | M | Fey Altar does not natively record crafter UUID |
| 38 | Reliquary Reincarnations | Player-actuated fuel-consuming artifacts | Canonical first active fuel item TBD | Item-specific activation + consumed reagent | H | Not every passive artifact effect deserves MXP |
| 39 | Summoning Rituals | Player-triggered summoning rituals | Not applicable | KubeJS ritual success/output; verify event surface | M | No XP for automated repeaters or idle summoning circles |
| 40 | Vampirism | No bespoke MXP for vampire/hunter form trees | Not applicable | Global boss source only, if registered | N | Transformation Compat retains all state/death mechanics |
| 41 | Werewolves | No bespoke MXP for werewolf form trees | Not applicable | Global boss source only, if registered | N | Transformation Compat retains all state/death mechanics |
| 42 | Enigmatic Legacy+ | No dedicated MXP source for artifact perks | Not applicable | Global boss source only, if registered | N | Keep spellstones and death risks native |

## Global boss-victory overlay (not counted among the 42 entries)

| Source | Proposed award source | Hook and validation |
|---|---|---|
| Vanilla Wither / Ender Dragon | First **1,000 MXP fixed**, repeat divided | NeoForge server-side entity defeat; verify dragon death sequence |
| L_Ender's Cataclysm bosses | First **1,000 MXP fixed**, repeat divided | Enumerate actual entity IDs, death and phase handling |
| Draconic Evolution Chaos Guardian | **First 1,000 MXP fixed, subsequent 100 MXP fixed** (repeat proposed) | Verify exact entity, genuinely completed death, end-island repeat progression, damage contributors |
| Iron's Spells Dead King / Tyros | First **1,000 MXP fixed**, repeat divided | Dead King respawn/phylactery and ominous variant require policy |
| Goety / Goety Cataclysm | First fixed, repeat divided | Summoned bosses and boss-class entities; casting-only summons not an award |
| Totemic Baykok and other eligible ceremony bosses | First fixed, repeat divided | Ceremony success is different from boss defeat: both can reward if legitimately completed |
| Feywild seasonal bosses | First fixed, repeat divided | Determine boss identity, quest mechanics and entity death behavior |
| Other magic/adventure mods | Opt-in only | Verify qualifying boss list and user-facing difficulty |

**Do not** award for every elite or ordinary mob merely because it has a boss health bar. Record specific entity type IDs, boss ID aliases/variants, stable IDs for achievements, and allowed contribution semantics from test world registry output before writing configuration. The existing Chaos Guardian gate for ProjectE/Project Expansion remains unchanged.

## First implementation slices

1. **Core MXP ledger and global boss tracker:** server-only UUID ledger, first-time registry, simple fixed / divided award dispatcher, Tyz's Skills XP grant (MXP-only), respec persistence, percent progress display, first/repeat generic boss smoke. No magic-mod source integration needed for initial simulator.
2. **Totemic ritual pilot:** use documented 1.21.1 KubeJS **TotemicEvents.ceremonyStartupSuccess** to detect a successful ceremony. Confirm callback payload, whether firing happens before another handler can cancel, and how actual instrument performers can be credited. First completion fixed; repeat divided; *do not alter music/minigame*.
3. **Embers player resource pilot:** Java subscriber to **EmberRemoveEvent**, where owner and pre-discount amount exist. Validate item-paid Cinder Staff/Blazing Ray; no award for refueling, pumping or stationary machine consumption. This is the strongest ready-to-prototype expenditure path.
4. **Ars Nouveau + Iron's shared pool:** first verify actual Ars 'n Spells ISS Primary config and real debit sequence for both spell families. Build **one** canonical payment observer and pair it with accepted caster action/nominal cost. Tests must disprove duplicate awards and preflight/failure awards before enabling.
5. **Neo Vitae + Psi simple pilots:** AnimaEvent.PostSyphon and Psi's KubeJS preSpellCast/spellCast against exact pinned releases; verify nominal costs, player actor, and debit. Roots Terra stays a separate medium-effort pilot.
6. **Pastel and Botania tools:** personally activated Ink/mana consumption. Audit each relevant item path, partial cost, portable container, owner UUID, and multicolor Ink rather than counting general inventory energy transfers.
7. **Remaining caster bridges:** Mahou, HexCast, Goety, JAWR infusion, Primal Magick, EvilCraft, Forbidden & Arcanus and other unusual active items. Treat bytecode/source inspection of pinned releases as the gate for a reliable award hook.
8. **Ritual/craft schools:** Occultism, Theurgy, Enchanted, Eidolon, Hexerei, Astral Sorcery, Malum, Summoning Rituals, Feywild and Scriptor manual creation; attribute the initiating person and completed output, not the creator of unattended equipment.

## Adapter review checklist

Per resource source record: (1) actual pinned mod/version; (2) native internal resource name and whether player/item/world; (3) measured **first accessible** baseline B; (4) original pre-discount cost method; (5) real successful debit method/event; (6) completed action signal; (7) actor/owner UUID; (8) cancellation, automation and multiple charge-path tests; (9) direct event vs mixin vs KubeJS integration, and confidence; (10) test world evidence and final award ID.

Per ritual/craft: recipe/ceremony stable ID, first completion criteria, repeat interval, success semantics, participant attribution, and whether station automation can bypass active player work. Per boss: exact registry ID, phase semantics, damage attribution, first/normal-repeat/fixed-repeat MXP and per-entity deduplication.

## Source anchors for the current confidence calls

- [Tyz's Skills public XP API and custom skill data](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/docs/kubejs/kubejs_methods.md)
- [Totemic 1.21.1 KubeJS ceremony success event](https://kubejs.com/wiki/addons/totemic)
- [Embers user-project source audit and event paths](EMBERS-SKILLS-AND-ECONOMY-AUDIT-2026-11-13.md)
- [JAWR personal infusion audit](JAWR-0.5.39-INFUSION-SYMBOLOGY-PUFFERFISH-AUDIT-2026-11-14.md)
- [Scriptor source and no-mana lectern audit](SCRIPTOR-LECTERN-SKILLS-AUDIT-2026-11-13.md)
- [Enchanted source audit, RiteType owner](ENCHANTED-WITCHCRAFT-4.2.7-DEEP-DIVE-2026-11-13.md)
- [Feywild source audit, missing altar crafter ownership](FEYWILD-5.6.4-INITIAL-SKILLS-AUDIT-2026-11-14.md)
- [Ars 'n Spells shared mana modes](https://www.curseforge.com/minecraft/mc-mods/ars-n-spells)
- [Neo Vitae Anima/EV versus altar machines](https://modrinth.com/mod/neovitae)
- [Astral Sorcery 1.21.1 attunement/perk progression](https://github.com/HellFirePvP/AstralSorcery/blob/1.21.1/src/main/resources/assets/astralsorcery/lang/en_us.json)
