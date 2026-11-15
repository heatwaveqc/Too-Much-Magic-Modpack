# Monk — full native faction tree skeleton (owner revision, 2026-11-15)

**State: conceptual tree and source-only preview, not 42 playable new abilities.** The existing **6 live pilot entries**, including the free root, are unchanged in the installed Vampirism tree while the next player test checks native skill-point purchases and hand-harvesting. The revised **48-node** blueprint is the authority for subsequent implementations.

- [Machine-readable canonical blueprint](native-full-tree-skeleton-v1.json)
- [Staged Vampirism configured-tree-shaped preview](preview/monk_level.configured_tree.PREVIEW.json) (NOT installed)
- [Node ID / Java-skill registry index](preview/skill-node-index.PREVIEW.json) (NOT installed)
- [Monk-only Tyz supplement](primary-tyz-monk-only-skeleton-v1.json) — 7 Monk-licensed ranks and 40 bounded affix families, all inactive
- [Reserved fist slot implementation contract](fist-slot-lock-contract-v1.json) — first hotbar slot (`1` key), free passive, not yet wired
- [No-Armor Lock trait contract](armor-lockout-trait-v1.json) — free, default-ON toggle preventing automatic armor equips, not yet wired

## Important owner changes

### No Spirit Projectile

**Monks fight at range with their existing spell systems**, particularly **Ars Nouveau** and **Iron's Spellbooks**, and may use other legitimate magic sources. No native Monk Spirit Projectile, Spirit Arrows or alternate projectile currency should ever be generated from the old source inventory. The original `tmm_monk:martial/spirit_projectile` record is kept only as an explicitly rejected historical audit candidate; it is **not** a live or planned skill.

Source-backed ranged Apotheosis one-time techniques (Magical, Acidic, Deathbound) remain in the arcane branch as *conditional spell-hit adapters*: use proven player-cast Ars/Iron's damage provenance, normal target immunity rules, and actual source semantics. They **do not** create attacks or duplicate universal Potency/Magic stats. Withering Stare stays an independent martial technique, not a projectile upgrade. No native purchase may be required merely to cast an Ars or Iron's spell. Legal spell attacks must retain their intended progression against Draconic Guardian crystals/shields; investigate boss interaction through the real spell damage source, not a made-up native bolt.

### Treasure Hand: exclusive Fortune / Silk Touch

Former **Delicate Hands** and **Fortunate Hands License** are **one** proposed native ability, `tmm_monk:harvest_touch`, purchasable once after the Iron Fist path. When enabled, a deliberate server-authoritative toggle chooses exactly **Fortune** or **Silk Touch**.

- **Fortune mode:** Initial **Fortune I** baseline; extra effective Fortune is purchased only in the primary Tyz/MXP `Fortunate Hands` ranking, with source-validated limits. A rank is an increment to this technique, not a second native unlock.
- **Silk Touch mode:** Effective Silk Touch I from the same native technique. Fortune must be absent from this break's loot calculation, regardless of saved Tyz ranks.
- **Both modes:** Only valid **player-attributed barehand mining**, respecting real harvest tool tiers and loot tables. Ordinary held tools, protected/canceled blocks, automatic farms and ownerless magical mining must not qualify. Later compatibility work can expand to a specific spell only when its actual loot context can safely accept the mode.
- **Switching:** A configurable keybind/action, validated on the server and persisted per Monk. Default mode Fortune. Display current mode via actionbar and in the fist-slot HUD; never toggle automatically because the player sneaks, changes target block or picks up loot.
- **Compatibility gate:** Cannot implement correctly merely by modifying drops after they are created. Verify the actual block loot context and tool enchantment semantics so Silk/Fortune apply once and no normal drops or XP duplicate.

Glass Touch remains a distinct early glass-only harvesting interaction; it is **not** another Silk Touch purchase.

### Permanent reserved fist hotbar position

The Monk gains a **free passive fist slot** upon joining the faction — not a branch node or another native SP purchase.

The default is **hotbar slot 1, inventory index 0** — the far-left position, selected with the **numeral 1 key**. When the Monk selects it, the main hand stays genuinely empty, suitable for punching/mining, while the other eight hotbar positions can hold Ars spellbooks, Iron's spellbooks, magic staffs or food. A small translucent fist overlay may identify the otherwise-empty slot but **must not be an actual inventory item**. The slot remains protected even if conventional equipment temporarily suppresses a Monk technique.

Real inventory lockout is a server-authoritative **inventory feature** and must cover pickup routing, GUI click/drag/shift-click, number-key swaps, /give, creative and modded menu injections, plus client sync and logout/death. Items already in the position at initiation must be transferred *atomically* if they fit; **if there is no room, initiation must not destroy or drop them or silently continue**. Any mod bypass must be recovered without loss/dupes. If all other inventory slots are full, picked-up items must remain on the ground.

**The contract is staged but not implemented in the current test build.** A periodic tick moving items after the fact would *not* satisfy a hard lockout. Read [the full acceptance matrix](fist-slot-lock-contract-v1.json) before hooking menus/inventory code.

### Optional No-Armor Lock (ON by default)

**No-Armor Lock** is a second **free, non-purchasable Monk faction trait**, independent of the permanently reserved fist slot. It is a protective control for graveyard/gravestone recovery and mods that auto-equip gear.

- **ON (default):** The four vanilla armor/equipment slots (head, chest, legs, feet) cannot accept *any* item. The player may still carry armor in ordinary inventory, hold legal magic focuses, and use **Curios** (including legal Draconic flight gear); an Elytra in the vanilla chest slot is blocked. Equipment restored by a graveyard mod must go to cargo inventory or remain safely recoverable at its source, not silently get equipped.
- **OFF:** Armor can be equipped manually or by ordinary mod restore behavior. This is **not** permission to wear conventional armor while keeping Monk bonuses: current `MonkEligibility` still disables qualifying Monk abilities whenever armor is actually worn, whether lock is ON or OFF. Removing the armor re-enables eligibility.
- **Toggling ON with armor worn:** Only enable after every equipped item can be legally and atomically moved intact into permitted inventory space. Full inventory, Curse of Binding, or an unequip restriction must produce a clear refusal without item drops, deletion, duplication, or bypassing the restriction.
- **State and interface:** Server-authoritative, per-character persisted ON/OFF with a configurable keybind or settings toggle and actionbar/HUD status; do not use a skill point, item or GUI-only illusion. Lock protects against GUI clicks, shift-click, equipment actions, direct inventory writes, modded grave restoration, and dispensers; Curios are explicitly unaffected.

The [armor lock contract](armor-lockout-trait-v1.json) records the complete pre-equipment interception, post-mutation fallback and no-loss acceptance requirements. NeoForge's `LivingEquipmentChangeEvent` fires **after** an equipment change, so listening to it alone cannot prevent a brief armor equip and Monk power shutdown. The protective feature is **not yet active**; its pure Java rules and unit tests do not constitute actual equip interception. The near-term six-skill pilot is unchanged. When implemented, inventory lock and fist lock remain independent: disabling Armor Lock must never release hotbar slot 1.

## Architecture and world progression

The **native Vampirism faction** owns one-off abilities, the two-stage Breath Meditation, finite hand-harvest tiers, explicit milestone permissions, and *licenses* for Monk-only Tyz effects. **One global Tyz/MXP backend** owns stat ranks, Skill Points and the universal magical specialties; no second numeric Monk tree or duplicate spell-power purchase is allowed. The installed Monk pilot currently awards one native faction point per rank (20 by default), so this 48-node plan requires meaningful choice, specialization and review of capstone costs rather than promising everything is purchasable by one character.

Rank numbers in the following tables are **suggested earliest rank bands**, not yet enforced. Before any node is enabled, add a verified server-side rank, skill and world milestone gate. Momentum Gliding is still **after verified Ender Dragon defeat**, and the two proposed rank-20 capstones still require **verified Chaos Guardian participation**. The Monk never gains Creative Flight as a native skill; the permitted Draconic Curio provides external flight. Apotheosis Unbound remains excluded.

## Intended rank roadmap

| Rank window | Direction |
| --- | --- |
| 1–4 | First Sunset; Open Palm, Unarmored Poise or Stone Fist; early footwork, guarded combat and breath training |
| 5–8 | Better hand mining, animal empathy, Catch Arrows, early water mobility; ranged magic already works from Ars/Iron's |
| 9–12 | Fortune/Silk Touch toggle, advanced hand harvesting, Blink, source-gated spell-affix unlocks |
| 13–16 | Higher useful harvest tiers, increased environmental resilience, verified post-Dragon momentum glide |
| 17–20 | Late source-affix specialties and **proposed** post-Chaos capstones — no innate Creative Flight |

## Five branches

### Martial Discipline

Legal empty-hand combat, parries, counters, staring techniques, and source-faithful unarmed affixes. Ranged combat comes from Ars/Iron's.

| Rank guide | Node | Prerequisite | State |
| ---: | --- | --- | --- |
| 1 | Open Palm | monk root | **Live pilot** |
| 4 | Guarding Palm | open palm | Planned |
| 6 | Catch Arrows | guarding palm | Planned |
| 9 | Looting Palm License | open palm | Planned (Tyz permission) |
| 11 | Galvanizing | guarding palm | Planned |
| 12 | Withering Stare | guarding palm | Planned |
| 14 | Retreating | galvanizing | Planned |
| 16 | Omniscient | withering stare | Planned |
| 20 | Empty-Hand Paragon | omniscient | Planned; verified chaos guardian milestone |

### Body & Mobility

Safe traversal, footwork, water movement, teleportation and post-Dragon momentum glide.

| Rank guide | Node | Prerequisite | State |
| ---: | --- | --- | --- |
| 2 | Fleet Footwork | open palm | **Live pilot** |
| 3 | Step Assist | fleet footwork | **Live pilot** |
| 7 | Steadfast | step assist | Planned |
| 8 | Water Walking | step assist | Planned |
| 10 | Surefooted | steadfast | Planned |
| 11 | Blink | water walking | Planned |
| 14 | Unhurried | surefooted | Planned |
| 15 | Momentum Gliding | blink | Planned; personal or credited ender dragon defeat verified server side |

### Contemplation & Resilience

Meditation, animal empathy, breathing and non-absolute environmental protection.

| Rank guide | Node | Prerequisite | State |
| ---: | --- | --- | --- |
| 1 | Unarmored Poise | monk root | **Live pilot** |
| 4 | Breath Meditation I | unarmored poise | Planned |
| 6 | Animal Friendship | breath meditation 1 | Planned |
| 6 | Hunger Discipline License | breath meditation 1 | Planned (Tyz permission) |
| 7 | Fire Resilience License | unarmored poise | Planned (Tyz permission) |
| 9 | Explosion Resilience License | unarmored poise | Planned (Tyz permission) |
| 10 | Breath Meditation II | breath meditation 1 | Planned |
| 12 | Creeper Kiss | animal friendship | Planned |
| 12 | Purifying Meditation | breath meditation 2 | Planned |
| 13 | Lava Survival | fire resilience license | Planned |
| 20 | Stillness Paragon | purifying meditation | Planned; verified chaos guardian milestone |

### Empty-Hand Harvest

Finite effective harvest tiers, one exclusive Fortune/Silk Touch toggle and tool-free gathering; repeatable mining speed remains in Tyz.

| Rank guide | Node | Prerequisite | State |
| ---: | --- | --- | --- |
| 1 | Stone Fist | monk root | **Live pilot** |
| 5 | Iron Fist | stone fist | Planned |
| 7 | Gentle Shearing | stone fist | Planned |
| 7 | Glass Touch | iron fist | Planned |
| 9 | Diamond Fist | iron fist | Planned |
| 9 | Gardening | gentle shearing | Planned |
| 9 | Sandforming | iron fist | Planned |
| 10 | Treasure Hand — Fortune / Silk Touch | iron fist | Planned (Fortune/Silk toggle and Tyz Fortune license); verified hand loot context adapter |
| 13 | Leafforming | gardening | Planned |
| 13 | Stoneforming | diamond fist | Planned |
| 14 | Netherite Fist | diamond fist | Planned |
| 18 | Exotic Fist | netherite fist | Planned; discover actual pack tool tiers and effective drops |
| 18 | Supermassive | netherite fist | Planned |

### Arcane Mastery & Licenses

Monk-only progress licenses and source-faithful spell-affix adaptations for Ars Nouveau and Iron's; not a second generic mana/spell-stat wallet.

| Rank guide | Node | Prerequisite | State |
| ---: | --- | --- | --- |
| 5 | Monastic Mastery Learning License | monk root | Planned (Tyz permission) |
| 10 | Focused Advancement License | monastic learning license | Planned (Tyz permission) |
| 11 | Magical Spell Affix | monastic learning license | Planned; verified ars or irons spell hit source |
| 12 | Telepathic | monastic learning license | Planned |
| 14 | Acidic Spell Affix | magical casting | Planned; verified player spell damage attribution |
| 18 | Deathbound Spell Affix | magical casting | Planned; verified player spell damage attribution |

## The separate primary Tyz portion

The [47-entry Monk-specific primary Tyz supplement](primary-tyz-monk-only-skeleton-v1.json) preserves **7** native-licensed Tyz ranks and **40** source-rarity-bounded special-affix traits; all are `active:false`. The `Fortunate Hands` Tyz family is now licensed by **Treasure Hand**, not an independent native node. Its effective Fortune bonus must be suppressed while Treasure Hand is set to Silk Touch. The general and universal Tyz skills, their independent magic-mod adapters and **43** attribute-equivalent affix aliases are NOT registered here again. The **15 one-time affix adaptations** are shown in the native tree; the 40 bounded source-affix families do not become 40 new native SP gates.

## Activation order

1. Run the current initial test for First Sunset, the native Monk tree/purchases, Stone Fist, TreeChop and verified magic-break credit. None of the 42 blueprint-only nodes are in the runtime datapack.
2. Implement hard-reserved fist slot across inventory/menu entry points with no item loss/dupes, client display and multiplayer tests; this is a **free Monk faction utility**, not a paywall.
3. For Treasure Hand, implement a reliable hand-break loot context and server-side mode toggle/persistence and test Fortune vs Silk, permission limits, protected blocks, XP, save/load, other mods and a real Tyz rank.
4. For each later ability, build/compile the Java effect and adapters, register a unique skill ID, add language/GUI labels, and enforce rank/prerequisite/world gates on the server before changing the installed resource tree.
5. For Ars/Iron's affix conversions, use source-proven cast/hit/loot attribution; no forged attacks, no generic blanket damage buff, and no duplication of primary Tyz Potency.
6. Refresh the blueprint's `implementation` state and CI allowances **only after** a disposable client/server smoke. Keep all new player saves disposable until state migration is validated.

## Historical sources and current source of truth

- [Original Monk 1.4 audit](../../../docs/MONK-ORIGINAL-AUDIT.md): source of physical deeds and all 25 original registered abilities, not binding design for the TMM spellcasting Monk.
- [No-Pufferfish skill placement v2](../profiles/skill-placement-no-pufferfish-v2.json): historical complete 270-record audit. **Explicit owner overrides in the current blueprint supersede** Spirit Projectile and the separate Fortune/Silk purchases without deleting the source audit.
- [Milestone and flight targets](../profiles/world-tier-unlocks.json): the Ender Dragon / Chaos Guardian progression contract.
- [Next live Monk QA](../NEXT-QA-2026-11-15.md): installed pilot state. Verify it first.
