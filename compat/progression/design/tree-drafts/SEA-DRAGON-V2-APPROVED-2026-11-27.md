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
| Draconic Vitality | 5 | +92 HP; growth-gated per rank | 500 total intrinsic HP (250 hearts) at max native adult + rank 5 |
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

- Vitality: owner-approved fixed +92 additive HP per rank (total +460 HP), based on native max-growth adult intrinsic 40 HP = vanilla 20 + native 20. Exact source: [adult stage JSON](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/658c63608adc0194c9c9516804ecd66c1fd10112/src/generated/resources/data/dragonsurvival/dragonsurvival/dragon_stage/adult.json) and [DSModifiers](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/658c63608adc0194c9c9516804ecd66c1fd10112/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/DSModifiers.java): growth 60 minus stage min 40 gives level 20, linear 10.5 + 19 × 0.5 = +20. No species/body native max-health modifier identified. Installed binary verification remains outstanding. 500 intrinsic max HP before gear, other paths or temporary effects; not a forced current-health assignment or global health cap. Attribute cap verification remains engine work. No automatic healing on purchase assumed. Growth gates below supersede deferred Vitality restrictions.
- Mana and potency percentage increments additive against a defined provider base, not compounded. Exclude machines/world resources. External potency and Ancient Power apply to their respective distinct personal/native actions, never both recursively. External general potency +200% and relevant elemental +100% yield 4× base before Core/gear; a dual-tagged spell receives the elemental bonus once. Provider-specific magnitudes, supported resource/element classifications and caps require source verification.
- **No extra magic cooldown reduction, cost reduction, or magic recovery** in Dragon Path, including native cooldown reduction. Core and existing native Dragon Survival mechanics remain unchanged. No new MXP multiplier.
- Claws apply to natural melee; Talons to native shovel/pickaxe/axe specialty only, no harvest-tier entitlement.
- Slipstream is aquatic evasion; Brine Scales reduces eligible remaining physical damage after ordinary armor while submerged; Deepwater Refuge is health (not magic) recovery and operates in combat.
- Luck is flat +100 at ten ranks, subject to actual attribute/loot consumer verification, not unconditional loot duplication or boss/quest bypass.
- Actual species gating and compatible Dracolich pairing follow #452/#468 and existing Path policy. No duplicate species benefits.

## Draconic Vitality growth gates — 2026-11-27 owner direction

Use five purchase unlocks indexed 0–4, one paid rank each. Native DS 2.0.74 defines three stages (newborn 10–25, young 25–40, adult 40–60), not five. Native Claws and Teeth has four growth-based ranks at 0, 25, 40, 60. Map the owner's five-rank intent to actual growth boundaries plus one proposed intermediate adult threshold, without changing native growth:

| Growth tier | Minimum native growth | Unlocks purchase of rank | Cumulative HP bonus |
|---:|---:|---:|---:|
| 0 | 10 | 1 | +92 |
| 1 | 25 | 2 | +184 |
| 2 | 40 | 3 | +276 |
| 3 | 50 | 4 | +368 |
| 4 | 60 | 5 | +460 |

Growth 50 is a design-selected intermediate threshold (not a native named stage). Verify current authoritative species and actual growth (not visual or desired size) server-side at purchase; earlier ranks and one point per rank still required. Growth grants eligibility, not free ranks. Shrinking after purchase: recommended effective-rank clamp to current growth eligibility without erasing paid ranks or refunding points; confirm with owner/engine writer before implementation. This is a generic provider-backed rank prerequisite requirement, not permission to implement engine code here. Full growth rank 5 must yield 500 intrinsic max HP; no other skills changed. Other minimum-level restrictions remain deferred.

## Zero-point capabilities

Candidate challenge-earned entitlements accepted as targets; exact unlock deeds, gates and mechanics remain to be specified: Aquatic Communion (appropriate aquatic creatures neutral); Ocean Leap (powerful water-to-air breach); Undertow (natural aquatic attacks slow enemies); Wakebreaker (native aquatic Spin gains offensive component only if not already present). No extra hotkey or duplicate native ability presumed.

Native Dragon Survival abilities remain native, not bought again in Progression: Storm Breath, Ball Lightning, Sea Vision, Ore Glow, Soul Revelation, Sea Magic, Sea Athletics, Hydration, Spectral Impact, Electric Immunity, Claws and Teeth, Amphibious, Diver, Sea Wings, Sea Spin. Preserve innate effective Aquamirae Depths Fury V and Resilience V only in Sea form against relevant native-tagged combatants per #404; no PvP adapter/global player tag. Food policy #444 unchanged.

## Provenance and handoff

Native source audit: [Dragon Survival 2.0.74 exact upstream revision](https://github.com/DragonSurvivalTeam/DragonSurvival/tree/658c63608adc0194c9c9516804ecd66c1fd10112), generated species ability registry and definitions; installed binary/effective overlays not independently verified. This file records the approved numerical design, not live code or a claim that provider handlers already exist. Shared engine writer owns any necessary generic handlers. Next: engine/provider audit for numerical targets and config-only implementation after owner-directed implementation scope; challenge details and other minimum level restrictions later. Acceptance of this documentation: 14 skills, ranks 50+20+20+10=100, 1 point/rank, 500 intrinsic adult HP endpoint, no additional magic efficiency. Runtime acceptance not scheduled by this document. No assembly, server boot, Prism or gameplay tests authorized or run. Canonical ledger #431.
