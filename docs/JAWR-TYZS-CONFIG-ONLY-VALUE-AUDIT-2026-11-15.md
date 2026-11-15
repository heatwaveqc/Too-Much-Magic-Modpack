# Configuration-only JAWR + Tyz capability-retention audit (2026-11-15)

> **External-gate clarification (2026-11-15):** This audit assumes **no new scripts as well as no new Java module**. Tyz 6.4.2 **does** expose KubeJS-native server-side `skill_purchase_pre` for gating any skill or whole category by a verified advancement, player stage, or tag **without a compiled compat mod**. See [exact source and non-installed pilot](TYZS-EXTERNAL-ADVANCEMENT-KUBEJS-GATES-2026-11-15.md). These rules do not automatically hide category tabs or suspend unconditional attributes after a cure. The "no hard species gates" statement is about *unmodified JSON config alone*, not Tyz's extensibility.\n\n> **Value-added audit, NOT an owner decision to replace the approved target and NOT runtime implementation.** Alternative evaluated: JAWR 0.5.39 and Tyz's Skills 6.4.2 using only their documented, shipped configs / Tyz skill JSON overrides, **no custom NeoForge add-on, no KubeJS hooks, no fabricated resource attributes**. Compares against the 37-node pilot under [draft PR #94](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/94). TeamLapen Vampirism and Werewolves world content and native skills remain excluded as already decided. Hunter/Monk are separate.

## Executive conclusion

**We keep native transformation gameplay, feeding/blood economy, the JAWR vampire/lycan progression, curses/weaknesses and a surprising amount of ordinary combat/survival progression.** We primarily sacrifice **class-conditional purchase/effect enforcement** and selected bespoke actions (blood-powered per-rank abilities, body-part-specific combat, passive lycan healing if JAWR doesn't already do it, bite statuses, projectile dodging and true claw harvest tiers). The ability to customize a shared combat/survival progression does **not** imply the ability to attach purchased ranks to a particular species.

There is no compelling reason to build a custom **JAWR blood-cost integration** merely to retain the existence of blood feeding, blood healing or a blood pool: JAWR already owns all of those. Blood-efficiency, additional reservoir, blood-scaling and purchased blood-funded active skills would be *new class-specific luxuries*, not retained essential JAWR functionality.

## What is demonstrably possible with existing config only?

**Tyz 6.4.2**:
- Override native **stock skill JSON** under `config/tyzs_skills/custom/skills/<existing_id>.json` to change rank counts, prices, `customValues`, icon, category, name and prerequisites. Existing Java `SkillBehavior` stays attached by the ID. See [upstream `SkillPresets.java`](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/impl/server/skills/SkillPresets.java) and [`SkillLoader.java`](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/impl/server/skills/SkillLoader.java).
- Add **new `CUSTOM` Tyz skills** that modify existing registered player attributes: `minecraft:generic.max_health`, `minecraft:generic.attack_damage`, `minecraft:generic.movement_speed`, `minecraft:player.block_break_speed`, `minecraft:generic.step_height`, `neoforge:swim_speed`, and mod-provided attributes *only where confirmed*. Custom JSON supports plain attributes, pricing and prerequisites. A category is an organizational tab, **not a species eligibility predicate**.
- Existing native `bloodlust` is an `IMMUTABLE` skill with `health_percentage` values and a compiled `BloodlustEffect`. It is not the same as a new `CUSTOM` JSON skill. The existing effect heals the direct player killer of a hostile `Enemy` for a percentage of **victim max health**, **not** the player's maximum health. Native values (3/7/11/15%) can be reduced through configuration, **but no cooldown, absolute HP cap, or wolf-only condition exists** in that stock logic. It may be extremely strong on bosses.
- Existing native `excavation` is a `GENERIC` modifier of `minecraft:player.block_break_speed` (+20–200% across stock ten ranks), usable for faster digging, including unarmed break speed where the vanilla attribute applies. It is **not** a virtual pickaxe, tool tier or harvest-check override; it never fixes tool-restricted ore drops.
- Other native stock Tyz behavior candidates: `resistance`, `resilience`, `rage`, `adrenaline`, `backstab`, `stealth`, `venomous_attack`, `critical_hit`, `damage_deal`. A config override **moves/tunes these exact behavior IDs**; a brand-new `IMMUTABLE` ID is not accepted as an arbitrary custom file. `SkillLoader.preLoadSkill` only permits creation of new `CUSTOM` IDs without a registered `SkillPrefab`.
- `PurchasePre` and native rank retrieval are Java APIs, **not configuration-based options** for checking `isVampire()` or `isWerewolf()`. Setting `category: "tmm_werewolf"` does not guard purchases or remove permanent attribute modifiers on cure.

**JAWR 0.5.39**:
- Its independent Vampire and Werewolf attachments and native leveling represent the two supernatural states; native action/form selection, blood feeding, vampire blood-hunger/blood-healing ticks, sunlight, wolf moon-force events and silver checks belong to JAWR. The [pinned binary study in PR #70](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/70) confirms these code paths; it does not establish every form's health/sense/movement strengths through in-world play.
- The author's [official issue / changelog repository](https://github.com/mrsterner/witchery-issues) records config for vampire immortality and sunlight handling in certain dimensions (introduced 0.5.13), native Remove Curse for vampirism/lycanthropy, blood drinking from Neo Vitae's network through native orb/altar integration, and other vampire changes. Those are useful native levers, **not proof** that individual feeding efficiency, blood capacity, lycan passive regeneration, wolf claw mining, or silver-defense percentages are exposed as configurable settings. Do not invent TOML keys.
- JAWR's datapack ritual, altar and infusion JSON recipes affect **JAWR's own spells/rituals and world effects**, not Tyz rank conditions; data-driven ritual recipes are outside this no-new-mechanics configuration test.

## Full 37-node comparison

**Column meaning:** first cell after trait = existing functionality available without the pack-specific module; second = unique specialization or rule that configuration alone doesn't recreate.

### Vampire (16)

| Proposed trait | What survives configuration-only | What is not the same / is lost |
| --- | --- | --- |
| **Efficient Feeding** | JAWR feeding remains | Extra blood yield per feed requires a real JAWR transaction adapter or verified native config |
| **Blood Thrift** | JAWR blood costs remain | Tyz generic magic Thrift does not automatically discount JAWR blood |
| **Deep Reservoir** | JAWR blood pool remains | No verified JAWR configuration for a Tyz-rank-sensitive pool capacity |
| **Crimson Renewal** | JAWR's native blood-healing loop remains | A new on-demand, rank-scaled blood-to-health exchange needs code unless JAWR already provides an equivalent native action |
| **Nocturnal Vision** | JAWR native vampire perception is retained | Specific extra vision stage not established as a 0.5.39 configurable Tyz or JAWR trait |
| **Shadow Stalker** | Tyz stock `stealth` can lower hostile visibility radius | Not tied to darkness or vampire state |
| **Blood Sight** | No verified stock analogue | Rank-scaled, bounded prey scan requires code; native JAWR senses should be checked in game before declaring functionality missing |
| **Bat Mastery** | JAWR native bat transformation remains | Tyz cannot conditionally alter bat endurance or movement |
| **Fang Training** | Tyz `attack_damage` and combat skills modify general combat | Cannot restrict bonus to verified vampire bite damage |
| **Blood Duelist** | Tyz `rage` is low-HP based | Cannot use JAWR blood threshold as condition |
| **Night Rush** | Tyz `speed_boost` grants speed | Cannot make it vampire/night-only |
| **Blood Regent** | Tyz `rage` is a partial low-HP alternative | No blood-funded activated vampire stance |
| **Pale Resilience** | Tyz `resistance`/`resilience` protect against general damage/negative effects | Not vampire-specific; source tags still determine exemptions |
| **Sunshroud** | JAWR has native sunlight rules and some config (dimension protection) | No verified ranked, short blood-paid sunlight mitigation; keep real daylight drawback |
| **Elder Presence** | No clear stock equivalent | No class-only fear/disorientation aura |
| **Damage Limiter** | Tyz `resistance` reduces eligible damage globally | No blood-costing, temporary emergency shield |

### Werewolf (21)

| Proposed trait | What survives configuration-only | What is not the same / is lost |
| --- | --- | --- |
| **Feral Vigor** | Tyz `health_boost` or new `CUSTOM` max-health JSON | Bonus remains on any purchaser, including a cured player; native JAWR werewolf health bonuses are not fully audited |
| **Regenerative Flesh** | JAWR's native werewolf physiology remains | No stock Tyz passive regeneration skill; whether 0.5.39 already grants sufficient wolf-form regen needs direct validation |
| **Bloodlust Recovery** | Tyz `bloodlust` already heals on hostile kills | No species restriction, fixed HP cap or per-player cooldown; vanilla behavior heals percentage of *victim's* max HP |
| **Unbreakable Beast** | Same global Tyz health/defense knobs | No wolf-exclusive high-level extra hearts or combat-healing stage |
| **Rending Claws** | Tyz `attack_damage`, `damage_deal`, `critical_hit` | No native-claw-only hit classification |
| **Bleeding Bite** | Tyz `venomous_attack` adds poison to regular combat | Poison is not bleeding; no JAWR werewolf bite-only application |
| **Stunning Bite** | No exact stock match | No short wolf-bite immobilization |
| **Primal Fury** | Tyz `rage` when low on HP | No selectable activated feral rage with cost/cooldown |
| **Predator's Stride** | Tyz `speed_boost` and `step_height` | No JAWR-wolf-only speed purchase |
| **Pouncing Leap** | JAWR native forms/movement remain; Tyz `step_height` is not a leap | No custom aimed leap unless JAWR natively has one |
| **Arrow Awareness** | Tyz `resistance` can lower some projectile damage | No chance-based moving-projectile dodge |
| **Evasive Sprint** | Tyz defensive stats can reduce damage | No sprint-only evasion and shared dodge budget |
| **Surefooted Predator** | Tyz `resistance`, `step_height` approximate survivability/mobility | No per-hazard/fall block damage immunity from simple JSON |
| **Wild Attunement** | Existing Core Magic Potency (where supported) and JAWR/Roots native spell progressions remain | No wolf-only nature-tagged personal spell multiplier; cannot assume Botania/Roots hooks exist |
| **Rootsong** | Global magic/healing boosts only when a source mod exposes an attribute | No wolf-only Roots/Botania healing specialization |
| **Grove Howl** | JAWR native wolf progression remains | No Tyz-created player-only howl/buff; intentionally no wolf summons/pets |
| **Feral Excavation** | Tyz stock `excavation` uses `minecraft:player.block_break_speed` | Works as general break-speed improvement, not wolf-only, not claw-specific and does not grant tool tier |
| **Enhanced Claws** | No replacement needed under owner's relaxed requirement | Barehand stone/iron/diamond harvest tiers and tool-qualified loot remain unavailable |
| **Tempered Against Silver** | JAWR's native silver vulnerability remains; generic `resistance` may absorb eligible damage | No silver-only ranked resistance unless JAWR's own config provides it; generic bypass-type exclusions apply |
| **Predatory Senses** | JAWR native wolf perception should be tested | No rank-scaled bounded enemy scan in Tyz stock config |
| **Moon Apex** | Global Tyz health, speed, resistance and rage can approximate its stats | No activated endgame lycan stance, native milestone gate or extra form-only benefits |

## Biggest actual losses, ranked by incremental gameplay value

1. **Wolf-exclusive purchasable HP/regen and kill-heal:** TYZ `health_boost` and `bloodlust` get the basic gameplay effect; without code neither stops working after JAWR cure. Regenerative Flesh is only a *definite* loss if JAWR's pinned native wolf progression does not already grant satisfactory regeneration (requires live test).
2. **Physical bite specialization:** true bleed, short stun, wolf-only damage and bite attribution are not achievable just by rewriting the JSON for `venomous_attack` (which applies **poison** to generic attacks).
3. **Evasion:** moving projectile dodge and sprint dodge are not native Tyz skills; `resistance` approximates damage reduction, not chance to avoid a hit.
4. **Vampire paid special abilities:** feeding efficiency, extra blood reservoir, blood-cost rebates, bat tuning and predatory emergency stances are not stock Tyz toggles; however JAWR's core feeding, bat form and native blood-healing remain functional. These were optional enhancements rather than dependencies.
5. **Native-specialty sensors/silver/sun:** bounded prey vision, per-rank silver toughness, paid temporary sunlight protection and JAWR-specific native-level gates are not available through stock cross-mod configuration. Generic night vision, armor, magic and native JAWR progression may make these minor.
6. **True claw harvesting:** only speed remains through `excavation`; tool-harvest class and correct-tool ore drops do not. **Owner has indicated willingness to omit this**, so it is not a reason to insist on a companion mod.
7. **Wolf-only nature magic synergy and custom capstones:** no JAWR-form-aware Roots/Botania multiplier or timed Moon Apex effect, though universal Core Skills and native nature magic remain.

## Cost/risk comparison

| Alternative | New executable source | Purchasable form-specific abilities | Examples | Engineering / risks |
| --- | --- | --- | --- | --- |
| **A. Pure config** | None | **No hard species gates** | Tyz global Health, Bloodlust, Excavation, Resistance, Speed, Stealth; native JAWR forms and healing | Low build/maintenance risk; preserve ordinary gameplay; balance global Bloodlust and larger stats; names/categories must not imply actual species checks |
| **B. Config + JAWR-only native progression** | None | Only JAWR's *own* native unlocks | Exact original native forms, curses, blood, moon/silver and advancement | Same zero pack-specific code; intentionally drop 37 promised class-specific Tyz nodes |
| **C. Minimal state bridge** | Small Java addon | **Yes**, if implemented correctly | Conditional HP/regen/Bloodlust and cure-safe attributes; without full blood/bite/magic hooks | Material maintenance surface; must install and test a bridge. This is **not** a pure config solution |
| **D. Full PR #94 concept** | Broad NeoForge addon | Yes, with full custom actions | The 37 class-skills as unique behaviors | Highest code/testing cost; most skills have no finished handlers; not warranted if incremental features aren't valued |

## Recommended no-code target if adopted

- **Use JAWR alone for supernatural identity and innate progression.** Do not present recreated TeamLapen skill-tree unlocks as if they already exist.
- **Keep one global Tyz wallet** fed by the separately planned MXP dispatcher (the dispatcher is still a separate pack requirement and is not implemented by this audit). Keep Core/ordinary survival and combat bonuses universal: `health_boost`, `bloodlust` (rebalance against boss target HP), `excavation`, `resistance`, `rage`, `stealth`, `speed_boost`, `attack_damage`, `backstab`, `resilience`, `adrenaline`.
- **Do not place large unconditional health/attack bonuses in a "Werewolf only" Tyz category** unless willing for any species to purchase them and keep them after cure. A cosmetic tab is not enforcement. Prefer universal Core Skills with quests or guidebook pointers to JAWR's native transformation/progression.
- Avoid creating custom IMMUTABLE behavior IDs in config: the loader will reject them without registration. **If the owner still requires real Vampire/Werewolf Tyz-only skill purchases, pure config is insufficient.** No existing JAWR config changes Tyz purchase rules.
- Preserve the no-worldgen/no-raids/no-pets decision and Hunter/Monk ownership boundaries.
- **Do not close or merge draft #94 solely on this audit.** Once the owner chooses config-only, the 37-node module can be parked/closed as superseded without removing the valid research and static tests.

## Remaining evidence gaps

- Inspect **actual generated `witchery` TOML** from the pinned 0.5.39 game, not 0.5.13 changelog alone. Determine whether wolf health/regen, native sense/vision, silver reduction, blood-efficiency and native moon mobility are configurable. No authoritative complete config listing was obtained in this audit.
- Disposable world: record baseline no-Tyz JAWR vampire/wolf stats and active actions at each native rank, including wolf hunger, blood-healing, silver, moon force, day/rain sun behavior and forced-form effects.
- Configure Tyz native `excavation` and native `bloodlust` in a disposable test world; measure actual hand break speed, boss kill healing and multiplayer; also test a cure after purchase to demonstrate that Tyz perks stay universal.
- Do not confuse JAWR's optional native Neo Vitae integration with a new Tyz blood hook. The JAWR/Neo Vitae integration is an *existing feature* and should be used rather than implemented again.
