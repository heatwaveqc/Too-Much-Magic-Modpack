# Sea Dragon v2 — owner-approved design

**2026-11-27 · design enumeration only; not runtime configuration.** Owner approved the v2 proposal with one correction: a fully developed adult Dragon with maxed Dragon Survival and rank 5 Draconic Vitality must have **500 total HP**, not +20 HP. All three Dragons share the 50-point Draconic Foundation. This supersedes the Sea-specific November 22 proposal in [#514](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/514); other species-specific draft content is not modified. Historical [#495](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/495) remains provenance. Monk design principles: [#721](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/721).

## Purchased ranks

Exactly 1 advancement point per rank, 100 total points, 14 skills. Shared foundation 50; species specialization 50. No additional purchase currencies. Default MXP architecture per #475; preserve native advancement independently.

### Draconic Foundation — 50 points

| Skill | Ranks | Per rank | Full investment target |
|---|---:|---|---|
| Draconic Reservoir | 10 | +100% of base personal mana capacity | +1,000% (11× base) |
| Draconic Potency | 10 | +20% base external personal spell potency | +200% (3× base) |
| Ancient Power | 10 | +20% base native Dragon Survival offensive ability damage | +200% (3× base) |
| Draconic Vitality | 5 | (500 − H_native) / 5 HP; calibrated against fully developed native adult | 500 total intrinsic HP (250 hearts) at max native adult + rank 5 |
| Draconic Scales | 5 | +2 armor | +10 armor |
| Draconic Claws | 5 | +2 natural melee damage | +10 damage |
| Draconic Talons | 5 | +2 native specialty harvesting speed | +10 speed |

### Oceanic Physiology — 20 points

| Skill | Ranks | Per rank | Full investment target |
|---|---:|---|---|
| Sleek Form | 5 | +0.5 swim-speed attribute | +2.5 |
| Slipstream | 5 | +2 percentage points aquatic evasion | 10% |
| Brine Scales | 5 | +5 percentage points submerged eligible physical damage reduction | 25% |
| Deepwater Refuge | 5 | +0.4 HP/sec submerged health regeneration | +2 HP/sec |

### Tidal & Storm Sorcery — 20 points

| Skill | Ranks | Per rank | Full investment target |
|---|---:|---|---|
| Tidal Attunement | 10 | +10% base external water/ice spell potency | +100% |
| Stormcalling | 10 | +10% base external lightning/storm spell potency | +100% |

### Abyssal Fortune — 10 points

| Skill | Ranks | Per rank | Full investment target |
|---|---:|---|---|
| Abyssal Fortune | 10 | +10 Luck | +100 Luck |

## Numerical interpretation and boundaries

- Vitality: H_native means the effective intrinsic max HP of the fully developed adult in the corresponding native species, with native health progression maxed and without temporary effects, equipment, Core or other Paths. Calibrate a fixed additive rank contribution against that verified reference, not +500 HP, not a forced current-health assignment or a hard cap on all sources. For rank r, the reference target is H_native + r × (500 − H_native)/5. The exact native baseline, attribute cap/AttributeFix and rounding are research/engine handoff items; no fabricated per-rank HP. Minimum level/growth purchase restrictions intentionally deferred by owner. Whether any cap changes are required is an engineering question, not authorization to change them in this design. No automatic healing on purchase assumed.
- Mana and potency percentage increments additive against a defined provider base, not compounded. Exclude machines/world resources. External potency and Ancient Power apply to their respective distinct personal/native actions, never both recursively. External general potency +200% and relevant elemental +100% yield 4× base before Core/gear; a dual-tagged spell receives the elemental bonus once. Provider-specific magnitudes, supported resource/element classifications and caps require source verification.
- **No extra magic cooldown reduction, cost reduction, or magic recovery** in Dragon Path, including native cooldown reduction. Core and existing native Dragon Survival mechanics remain unchanged. No new MXP multiplier.
- Claws apply to natural melee; Talons to native shovel/pickaxe/axe specialty only, no harvest-tier entitlement.
- Slipstream is aquatic evasion; Brine Scales reduces eligible remaining physical damage after ordinary armor while submerged; Deepwater Refuge is health (not magic) recovery and operates in combat.
- Luck is flat +100 at ten ranks, subject to actual attribute/loot consumer verification, not unconditional loot duplication or boss/quest bypass.
- Actual species gating and compatible Dracolich pairing follow #452/#468 and existing Path policy. No duplicate species benefits.

## Zero-point capabilities

Candidate challenge-earned entitlements accepted as targets; exact unlock deeds, gates and mechanics remain to be specified: Aquatic Communion (appropriate aquatic creatures neutral); Ocean Leap (powerful water-to-air breach); Undertow (natural aquatic attacks slow enemies); Wakebreaker (native aquatic Spin gains offensive component only if not already present). No extra hotkey or duplicate native ability presumed.

Native Dragon Survival abilities remain native, not bought again in Progression: Storm Breath, Ball Lightning, Sea Vision, Ore Glow, Soul Revelation, Sea Magic, Sea Athletics, Hydration, Spectral Impact, Electric Immunity, Claws and Teeth, Amphibious, Diver, Sea Wings, Sea Spin. Preserve innate effective Aquamirae Depths Fury V and Resilience V only in Sea form against relevant native-tagged combatants per #404; no PvP adapter/global player tag. Food policy #444 unchanged.

## Provenance and handoff

Native source audit: [Dragon Survival 2.0.74 exact upstream revision](https://github.com/DragonSurvivalTeam/DragonSurvival/tree/658c63608adc0194c9c9516804ecd66c1fd10112), generated species ability registry and definitions; installed binary/effective overlays not independently verified. This file records the approved numerical design, not live code or a claim that provider handlers already exist. Shared engine writer owns any necessary generic handlers. Next: engine/provider audit for numerical targets and config-only implementation after owner-directed implementation scope; challenge details and minimum level restrictions later. Acceptance of this documentation: 14 skills, ranks 50+20+20+10=100, 1 point/rank, 500 intrinsic adult HP endpoint, no additional magic efficiency. Runtime acceptance not scheduled by this document. No assembly, server boot, Prism or gameplay tests authorized or run. Canonical ledger #431.
