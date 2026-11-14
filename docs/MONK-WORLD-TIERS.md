# Monk world-tier progression and flight gates

**Design commitments, not gameplay code** — 2026-11-14.

## Flight order

1. **Before the Ender Dragon:** allow existing legal mobility and
   Curios, limited jumps/blinks/water movement and spells. No
   Monk innate powered Elytra and no Monk Creative Flight.
2. **After real Ender Dragon progression, before Chaos Guardian:**
   unlock `tmm_monk:momentum_gliding`: an innate Elytra-style
   capability with a *small sustained acceleration while gliding*.
   Gentle momentum compensates drag; pitch and dive still matter.
   Server-authoritative thrust is applied only during actual
   fall-flying, with maximum safe horizontal/vertical velocity and
   chunk-loading checks. This does NOT enable hovering.
   No new energy logistics mod is needed.
3. **After real Chaos Guardian progression:** unlock
   `tmm_monk:creative_flight`: full hovering Creative-style
   flight, gated separately from Elytra or Draconic Curio flight.
   Sync/change permission without overwriting other providers.

**Likely reference:** Draconic Evolution itself has an adjustable
`elytra_boost` FlightEntity and `Elytra Boost` control (Forward +
Sprint during flight). Another older standalone mod called
`Powered Elytra` exists for Minecraft 1.12.2; we cannot prove
which mod the owner remembers. Reimplement the **described behavior**,
do not add an unrelated tech stack and do not copy code/assets.

Source: https://github.com/Draconic-Inc/Draconic-Evolution/blob/1.21/src/main/java/com/brandon3055/draconicevolution/api/modules/entities/FlightEntity.java

## Actual tier passage by a weaponless, armorless Monk

| Gate | Main risk if forbidden tools/armor | Required Monk-accessible path |
| --- | --- | --- |
| Nether and End access | Obsidian collection, portal repairs, Eyes of Ender | Natural valid harvest tier, Ars/other legal magic, trade or structure traversal; no creative/admin shortcut |
| Ender Dragon kill | Boss mechanics and survival, melee range | Legal spells, innate unarmed techniques and/or summons; secure legitimate boss-credit record |
| Cataclysm Harbinger / Ignis | Extreme damage; drops required for Occultism Eldritch Ancient Miner | Scalable unarmored defense, high magic, reliable unarmed/ranged damage, appropriate kill credit and collected boss materials |
| Eldritch Ancient Miner | Crafting, magical miner tiers and late resource acquisition | Can use stations, summon miners and harvest required materials without wielding conventional pickaxes |
| Chaos Guardian | Crystal shields, **large shield before health phase**, extreme aerial combat | Before-Guardian glide thrust; non-weapon projectile/spell that can genuinely damage crystals and shields; adequate durability/sustain |
| Post-Guardian ProjectE | User-imposed late-game ban on acquisition/use | Reuse existing global post-Guardian gate; never unlock via Monk XP, Curio, item trade or FTB reward |

**Major test blocker:** the 1.21 Draconic Evolution source explicitly
defines Guardian crystal shields and the Guardian's own shield.
Merely having high spell DPS does not prove compatibility. Run an
actual disposable-world combat test with a restricted Monk and verify
damage types, shield interaction, aerial movement, any phase-specific
mechanics, rewards and server-side credit. If spells cannot satisfy
a phase, implement a legal Monk ability that can, without trivializing
the boss.

## Character vs world progression attribution

Save stable evidence based on the *real upstream event/advancement*
(Dragon, Cataclysm bosses, Chaos Guardian). Distinguish:
- **World tier opened:** global gate may be satisfied by a group.
- **Monk mastery deed:** player or team participation proof still
  needs a policy and must survive death/logouts.
- Do not require the Monk to hold a weapon, wear armor or equip a
  specific boss item to receive the milestone.
- Never promote progression by merely claiming FTB Quests reward.

Unresolved: personal-kill vs team-assist criteria, and whether previously
defeated Dragon/Guardian milestones can be credited retroactively.
Until decided and tested, no enforcement is implemented.

Machine-readable proposal: `compat/monk/profiles/world-tier-unlocks.json`.
