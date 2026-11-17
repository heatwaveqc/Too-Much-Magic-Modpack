# JAWR-only Vampire/Werewolf Tyz capability-retention build (2026-11-15)

> **Status: 37 skill definitions + isolated source module + four server-side pilots, not installed and not client-tested.** No original Vampirism/Werewolves worldgen, raids, minions or pets. Hunter/Monk belong to other workstreams. This follows the owner-approved [retirement target](../../../docs/TEAM-LAPEN-RETIREMENT-TARGET-2026-11-15.md) and [skill triage](../../../docs/TEAM-LAPEN-SKILL-TREE-TRIAGE-2026-11-15.md).

## Concrete source deliverables

- `tyzs-retention/catalog.json` is the **authoritative 37-node catalog**: 16 Vampire skills, 21 Werewolf skills; exact stable IDs, five/three/one provisional ranks, positive SP prices, four Vampire and five Werewolf lanes, prerequisites, incompatible bite styles, late milestones, backend ownership and initial values.
- `tyzs-retention/build_preview.py` validates schema, all edges, no cycles, and creates **Tyz 6.4.2 IMMUTABLE JSON previews**. Every generated node is `active:false`, even for source-level pilots. It merges category JSON with the *real* existing categories and refuses collisions. It does not alter packwiz, existing `compat/magic/tyzs-test-overlay`, or `test/full-integration` skills.
- `tyzs-retention/test_build_preview.py` tests counts, DAG, gating schema, category preservation, no forbidden world systems, and inert previews.
- Independent `compat/forms` **experimental NeoForge addon**: JAWR read-only state observers, SkillPrefab registration from this one catalog, form-purchase denials, prerequisite-safe refunds, and proof-of-shape for four server mechanics. All 37 `SkillPrefab` defaults inactive.
- The standalone module is intentionally **not added to the integration manifest**, not compiled in the current integration pipeline and **not represented as a playable feature**. No build or runtime test has been run.

To validate static definitions:
```bash
python compat/forms/tyzs-retention/build_preview.py --check
python -m unittest discover -s compat/forms/tyzs-retention -p 'test_*.py'
```

To generate read-only/inactive Tyz files, supply the **current staged Tyz** categories; do not silently use the historical five-tab example once the nine-tree work advances:
```bash
python compat/forms/tyzs-retention/build_preview.py \
  --out /tmp/tmm-form-skills-preview \
  --base-categories compat/magic/tyzs-test-overlay/config/tyzs_skills/custom/data/categories.json
```
That base categories file currently lives in the Tyz runtime feature branch, **not** in the baseline `test/full-integration`. Copy/merge only via the approved integration toolchain. Generated custom IMMUTABLE overrides **are not independently loadable** unless a matching `SkillPrefab` is registered by the addon. That is why this branch contains the API registration module.

## The two trees

### Vampire — 16 nodes (JAWR vampire authority)

| Lane | Skills, in intended sequence | Code dependency |
| --- | --- | --- |
| **Bloodcraft** | Efficient Feeding → Blood Thrift; Deep Reservoir; Crimson Renewal | JAWR actual blood gain/debit, fraction/rounding/capacity, heal transaction |
| **Nightstalker** | Nocturnal Vision → Shadow Stalker; Blood Sight; Bat Mastery | Verified JAWR senses, darkness/light, bat action and form state |
| **Predation** | Fang Training → Blood Duelist; Night Rush; Blood Regent | Native bite attribution, low-blood threshold, timed blood-funded stance |
| **Undead Constitution** | Pale Resilience → Sunshroud; Damage Limiter; Elder Presence | JAWR sun hazards, defensive damage hook and partial mitigation |

The vampire should feel like an undead predator, not a generic caster. **No** duplicate JAWR bat/infection unlock, vampire-specific immortal/death timers, free invisibility spells, invented blood bars, summoned bats, or minions.

### Werewolf — 21 nodes (JAWR lycanthropy authority)

| Lane | Skills, in intended sequence | Code dependency |
| --- | --- | --- |
| **Lupine Vitality** | Feral Vigor → Regenerative Flesh; Bloodlust Recovery → Unbreakable Beast → Moon Apex | Conditional max HP, regeneration, credited kills, cooldown/reload/cure |
| **Feral Might** | Rending Claws → Bleeding Bite **or** Stunning Bite; Primal Fury | Native wolf-hit tagging, status effects, fatigue/Rage, bosses |
| **Moonrunner** | Predator's Stride → Pouncing Leap; Arrow Awareness; Evasive Sprint; Surefooted Predator | Movement and jump, shared bounded dodge, hazard tags |
| **Wild Communion** | Wild Attunement → Rootsong → Grove Howl | Source-verified **personal** nature casts and player-only temporary buffs |
| **Claws & Survival** | Feral Excavation → Enhanced Claws; Tempered Against Silver → Predatory Senses | Actual barehand mining speed, legal tool tier/loot, silver damage, bounded senses |

**Balance targets (provisional only):** five levels of Feral Vigor provide +10/+20/+30/+40/+50 max HP (5–25 extra hearts); Unbreakable Beast adds up to +30 more HP; Regenerative Flesh heals 0.2–1.0 HP/sec, after a damage-free interval; Bloodlust Recovery heals 2–10 HP on a valid personally credited hostile kill, with a minimum three-second cooldown; Feral Excavation improves bareclaw mining speed by 20–100%, but does not grant tool tiers yet. Final prices/caps must be playtested with armor and nature magic.

## What actually needs additional Tyz or pack-owned code?

**Tyz upstream does not need a fork.** Its API already provides `TyzsSkillsAPI.skills().getSkillLevel(ServerPlayer, id)`, `TyzsSkillsCommonSetupEvent` and `SkillPrefab` with `SkillBehavior`, plus `SkillActionEvent.PurchasePre`/`RefundPre`. We add a pack-owned **Tyz companion** that reads skill ranks and applies bounded effects to JAWR states. In particular Tyz's own `BloodlustEffect` uses `LivingDeathEvent` and an uncapped % of defeated hostile max health. Our copy should not simply run that global effect on every player.

| Priority | Required adapter | Why JSON / Tyz stock is insufficient | Current status |
| --- | --- | --- | --- |
| P0 | **Form/rank eligibility & lifecycle** | Tyz categories are not per-player hidden or form gated; CUSTOM attribute modifiers would persist after cure | Read-only 0.5.39 JAWR state probe; purchase checks deny all unverified stages; effect-time checks in pilots. Full milestones/cure hook not yet implemented |
| P0 | **Max HP + HP regeneration** | Max-health modifiers must vanish after cure, respawn and unknown JAWR data; health regen is an event/tick effect | Source pilot: conditional extra hearts, 20-tick reconciliation, damage-cooldown regen; **needs prompt cure interception and tests** |
| P0 | **Kill-triggered healing** | Tyz Bloodlust has no verified wolf gate, cooldown, or pack-specific safe cap | Source pilot: actual direct player `LivingDeathEvent`, hostile mob only, 2–10 HP heal, 3-second cooldown; no gameplay QA |
| P0 | **Claw mining speed** | Must work with empty hands and verified JAWR wolf identity; client/server break speed | Source pilot using `PlayerEvent.BreakSpeed` server-side. Client feedback and checks remain |
| P0 | **Claw harvest tier and drops** | `BreakSpeed` does not establish pickaxe/tool tier or correct-tool loot predicates | **Unimplemented:** validate `HarvestCheck`, virtual tool/loot context, claim protection, stone/iron/diamond/netherite block tiers, no duplicate drops or MXP |
| P0 | **JAWR blood transaction bridge** | Efficient Feeding, Blood Thrift, Reservoir, Renewal, Blood Regent and Sunshroud must affect real *one-source* blood transactions | **Unimplemented:** exact JAWR 0.5.39 `BloodPoolHandler` hooks; cost before spending, supply after legitimate feed, rounding/fractional carry, positive-cost floor |
| P1 | **JAWR bite and melee classifier** | Generic `LivingIncomingDamageEvent` doesn't tell us whether an attack was a native vampire/wolf bite | **Unimplemented:** source-proven bite hook, damage source and claim/PvP/boss tags |
| P1 | **Bleed and stun status effects** | Timed effects require real attack events and immunity checking; one active bite technique | **Unimplemented:** debuff storage, 5-second bleed, short stun, one-per-target cooldown, boss/PvP caps |
| P1 | **Evasion and partial silver resistance** | Must identify projectiles and actual JAWR silver damage; two avoidance nodes must share a cap | **Unimplemented:** before-final-damage events; one roll per hit, no blanket immunity, appropriate projectile/claims/PvP |
| P1 | **Mobility/leap/senses and safe state transitions** | Actual leap, vision, darkness stealth, and nearest mobs cannot be done via JSON only | **Unimplemented:** keybind or ability action, network checks, target filters, bounded scans and cooldowns |
| P1 | **JAWR native milestone prerequisites** | Tyz JSON only checks another skill's existence, not real JAWR vampire/wolf level/quest progress | **Unimplemented:** hash-verified level getter and source-specific entry/mid/late/end thresholds; deny unknowns, keep acquired ranks dormant after cure |
| P2 | **Nature-magic personal attribution** | Botania/Roots world machines should not get stronger just because an owner is a wolf | **Unimplemented:** personally cast spell tags, mod-specific effect support and deduplication with Core Potency |
| P2 | **Late active buffs and defensive stances** | Activation networking, paid blood/fatigue, cooldowns, HUD and anti-exploit rules | **Unimplemented:** no free creative flight, unkillable mitigation, permanent fear, raids, pets or mob ownership |

### Activation gates

1. Merge/reconcile the nine-tree **conceptual PR #83** first; preserve existing IDs and the single MXP wallet. The current PR #83 still describes superseded TeamLapen providers.
2. Verify exact pinned **JAWR 0.5.39** signatures, especially Werewolf attachment getter, native level thresholds, bite, blood, sun, silver, wolf transformation and monthly moon behavior.
3. Build this independent mod with pinned Tyz 6.4.2 and JAWR installed; confirm `TyzsSkillsCommonSetupEvent` registrations and inactive overrides parse with zero errors. Source-only Java is not a compile test.
4. Turn on **one safe pilot at a time** in a disposable client/server world after gating is implemented: Feral Vigor → kill healing → claw speed → verified legal harvest.
5. Require effective client prediction for mining, no lingering health modifier after immediate JAWR cure, no rank transfer to other forms, no negative blood/free action, no double cast/XP/kill rewards, and good multiplayer/logout/reset behavior.
6. Only after all effects are implemented, use a controlled stager to activate form JSON and add the addon JAR to packwiz integration. No blanket `active:true` flip of the 37 nodes.

## Explicit non-goals

No standalone Hunter/Monk changes, additional wallets, new magic schools, worldgen, vampire forests, faction villages, hunts/raids, pets/minions/lord progression, third-party art transfer, or extra bat/werewolf morph frameworks.

References: [Tyz 6.4+ custom skill documentation](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/docs/json-tweaks/create_skill.md), [Tyz API](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/api/TyzsSkillsAPI.java), [native Bloodlust listener](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/impl/server/effects/skillEffects/BloodlustEffect.java), [JAWR pinned binary audit PR #70](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/70).
