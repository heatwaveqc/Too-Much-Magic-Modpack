# Monk integration into the PRIMARY Tyz/MXP skill design

**Date:** 2026-11-14. **Status: cross-branch reconciliation, design-only. Do not implement a second tree.**

## Single source of truth

The active **primary skill design** lives on branch `design/universal-mxp-progression-registry`:

- [Tyz core magic + five form/Monk categories](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/design/universal-mxp-progression-registry/docs/TYZS-CORE-MAGIC-AND-FORM-TREES.md)
- [MXP-only Tyz XP and external gates](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/design/universal-mxp-progression-registry/docs/TYZS-MXP-EXTERNAL-GATES.md)
- [MXP earned from use, rituals, bosses, first discoveries](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/design/universal-mxp-progression-registry/docs/MAGIC-MXP-PROGRESSION.md)
- [42-mod magic hook registry](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/design/universal-mxp-progression-registry/docs/MAGIC-MXP-SOURCE-REGISTRY.md)

**Any contradictory older Monk design text is superseded.** In particular:

1. **MXP is the ONLY Tyz XP source.** No parallel Monk mastery currency, direct Tyz activity XP, or separately purchased 'shared Monk magic' rank. One global Tyz XP/level/SP wallet; vanilla XP stays relevant only to existing independent Improvable Skills content. Faction progression is its own native mechanic, not an alternate purchase wallet for universal ranks.
2. **One owned rank of the previous tier** satisfies normal Tyz `skillPrerequisites`, not 99/99. Optional external progression locks on advanced tiers still apply **server-side**, using the primary design's PurchasePre/RefundPre/bulk/direct-grant protections. Up to 99 ranks per stage and up to five x10 Monk magnitude stages remain *possible*; stage effect/cost curves require MXP economy simulation before approval.
3. **One shared compatibility addon** owns cross-mod stat attributes, mana/EV adapters, MXP award observations and per-transaction de-duplication. Monk does not create its own separate mana bridge, 'universal' Tyz custom category, per-mod spell tiers, or player-owned XP accumulator.
4. **One `tmm_magic_core` Tyz category** owns cross-mod magic numerics: Reservoir, Wellspring, Thrift, Potency, Quickening, Projection, Persistence, Conjuration, Warding. Five other Tyz category IDs are already reserved: `tmm_vampire`, `tmm_werewolf`, `tmm_monk`, `tmm_dragon`, `tmm_lich`.
5. **Monk-specific scope:** faction identity/cultivation milestones, unique original Monk techniques, unarmored/unarmed legality adapters, eligibility and effect suspension, and candidate Apotheosis technique adaptations (where not already equivalent to shared purchases). These integrate with the existing Tyz `tmm_monk` category and **do not spawn an independent skill tree or point system**.
6. Transformation Compat owns physiological compatibility, faction co-presence and resurrection. Monk must use its authoritative player eligibility state. Native Vampirism faction skills still own their own powers where appropriate.
7. **Mahou efficiency** is **not** a blocker: the generic Thrift family may serve other mods; omit the Mahou-specific payment adapter until a supported direct cost interception is proved. **Neo Vitae Ritual Mastery** is not a blocker: omit the optional Monk altar upgrade. The shared MXP dispatcher may *still* use Neo Vitae `AnimaEvent.PostSyphon` for actual personal EV spends; that's **not** the same thing as modifying ritual efficiency.
8. Elastic is fully represented by the **single, native Step Assist**. No separate Elastic rank, no stacked 3-step rarity adaptation.

## Reconciliation of the older 24 'shared Tyz skills'

The original Monk catalog's **24 unification groups are research-source buckets**, **not 24 new mandatory Tyz nodes**. Map to the canonical primary-category families and existing base Tyz tabs before constructing any JSON. Some are merely candidates and must not be silently generated as extra purchases.

| Prior shared candidate ID | Canonical primary assignment |
| --- | --- |
| `magic_capacity` | **Core Magic / Reservoir** |
| `magic_regeneration` | **Core Magic / Wellspring** |
| `spell_power` | **Core Magic / Potency** |
| `spell_cast_speed` | **Core Magic / Quickening** |
| `spell_efficiency` | **Core Magic / Thrift** |
| `magic_resistance` | **Core Magic / Warding** |
| `summoner_potency` | **Core Magic / Conjuration**, for eligible player-owned summons |
| `spell_range` | **Core Magic / Projection** |
| `spell_area` | **Expansion is a separately proposed candidate**, not an automatic Projection duplicate |
| `spell_cooldown` | **Review separately**: lowering cooldowns is not automatically faster casting/Quickening |
| `projectile_velocity` | Existing/base combat category or supported projectile-only option; not automatically magic Projection |
| `projectile_damage` | Existing/base combat, or specific Monk legal projectile adapter; do not double charge |
| `mining_speed` | Existing/base Nature or utility category, plus Monk empty-hand permission |
| `fall_protection`, `swim_mastery`, `movement_speed` | Existing/base movement/utility category |
| `natural_armor`, `max_health`, `attack_power`, `attack_speed`, `critical_chance`, `critical_damage` | Existing/base combat categories, with Monk-specific unarmed/unarmored legality rules |
| `luck`, `vanilla_xp_gain` | Existing/base miscellaneous category (vanilla-XP gain **does not award Tyz XP**) |

**Persistence** is the primary catalog's duration family; prior Monk Goety-specific `spell_duration` and any equivalent sources should be checked against it rather than auto-implemented as another shared rank. Magic and combat nodes each have a **single owner of purchased effects**. If the original catalog source differs in semantics, preserve it as a research alias and **do not generate a purchasable node** without review.

**Important candidate correction:** Primal Magick Sorcery `expertise` is research progression earned from spells, not spell power. The old `primal/spell_proficiency -> shared spell_power` *proposal is invalid*; do not wire that candidate to Potency. Keep Primal induced-attunement research separate from universal spell-power bonuses.

## Prior work to reuse, not replicate

- The 270-row Monk audit and 98 Apotheosis candidate adaptations are **source inventories**, not a second implementation manifest. Their 204 preliminary canonical endpoints were counted *before* primary-engine deduplication and **must not be treated as 204 independently purchasable nodes**.
- Source-backed Primal/Roots/Occultism integration research in [Monk API audit](MONK-MOD-API-VERIFICATION-2026-11-14.md) should be passed to the primary shared compatibility addon owner. Its one canonical adapter should cover both MXP observations and stat effects where useful.
- The Monk faction Java scaffold is an identity/permission prototype. It currently **does not contain** the new Tyz skill tree implementation. Reuse its identity state, but let the primary skill design implement Tyz configuration and global points.
- The old Tyz 99/99 purchase gate, Tyz-native XP assumptions, and 'generate five x10 stages for all skills' do **not** meet the primary contract. Historical JSON prototypes remain inactive.

## Mahou configurable ceiling (separate from skill integration)

Official [Mahou mana/config reference](https://mahoutsukai.stepsword.com/guides/mana-and-leylines/) documents `mahoutsukai-server.toml` under `[mana]`:
- `MAX_MANA_CAP` documented **default 200,000**, accepted **0–100,000,000**. User wants possible maximum well above 100,000. Changing the server TOML value suffices to raise the *configured* ceiling; it does **not** gift mana or create progression ranks.
- `MANA_INCREASE=1` and `MANA_UP_COUNTER=100` mean natural cap advancement can be very slow if the cap is massively raised; the primary **Reservoir** shared attribute adapter must respect the configured cap and grant only intended safe per-player capacity.
- Raising max mana can affect cast/run throughput and max-mana-scaled effects such as leylines; avoid simultaneously increasing `MANA_REGEN_PER_TICK` or changing native costs by accident.
- No exact *pack target maximum* has been fixed by user. **Proposal:** 10,000,000 as endgame headroom (still 1/10 of config's supported maximum) rather than an artificial 100k ceiling, to be reviewed before next full-build config pin. A 1,000,000 cap is also valid. Verify exact pinned `mahoutsukai-1.21.1-v1.36.31.jar` server config/ABI and world `serverconfig` behavior; the repository has not yet boot-tested this modification.

## Scope guard

Do NOT write a new Monk Tyz tree from this branch or activate its inactive Strength stage JSON. The next implementation belongs to the **primary skill-design / Magic Compatibility integration branch**. Monk PR #31 should be reviewed as **research and Monk-only supplementary behavior** only, with explicit references to the canonical primary skill design.
