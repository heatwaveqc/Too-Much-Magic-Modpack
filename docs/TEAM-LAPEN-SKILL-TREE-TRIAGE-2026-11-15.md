# TeamLapen skill-tree triage → JAWR-owned Vampire/Werewolf Tyz trees (2026-11-15)

> **Design audit, not new active skill JSON.** Owner decision: remove Vampirism and its Werewolves addon, **do not bring over their structures, villages, raids, minions or pets**, and transfer only valuable nonduplicate *player abilities* to the existing `tmm_vampire` and `tmm_werewolf` Tyz plans. Hunter and Monk implementation is explicitly out of scope and handled in other chats.

## Source quality and method

- Current pack pins: **Vampirism `1.10.13`** (`Vampirism-1.21-1.10.13.jar`) and **Werewolves `2.0.3.4`** (`Werewolves-1.21-2.0.3.4.jar`).
- Inspected actual registered skills and **which skill nodes are attached to the generated active trees**, not merely translation names:
  - [Vampirism `VampireSkills.java`](https://github.com/TeamLapen/Vampirism/blob/version/1.21/latest/src/main/java/de/teamlapen/vampirism/entity/player/vampire/skills/VampireSkills.java), [tree provider](https://github.com/TeamLapen/Vampirism/blob/version/1.21/latest/src/main/java/de/teamlapen/vampirism/data/provider/SkillTreeProvider.java), [English names](https://github.com/TeamLapen/Vampirism/blob/version/1.21/latest/src/main/resources/assets/vampirism/lang/en_us.json).
  - [Werewolves `ModSkills.java`](https://github.com/TeamLapen/Werewolves/blob/1.21/src/main/java/de/teamlapen/werewolves/core/ModSkills.java), [tree provider](https://github.com/TeamLapen/Werewolves/blob/1.21/src/main/java/de/teamlapen/werewolves/data/SkillTreeProvider.java), [English descriptions](https://github.com/TeamLapen/Werewolves/blob/1.21/src/main/resources/assets/werewolves/lang/en_us.json).
  - Older version-scoped [Vampirism 1.10 skill wiki](https://wiki.vampirism.dev/docs/1.10%20Forge/wiki/factions/vampire/skills). **Do not substitute the newly published 2.x/Dracula/FactionAPI wiki tree** for this pinned 1.10.x pack.
- **Version caveat:** upstream Vampirism `version/1.21/latest` currently reports **1.10.14** in `gradle.properties`, one patch later than the pack's pinned **1.10.13**, whereas the observed Werewolves 1.21 source declares **2.0.3.4**. This is a source-tree design census, **not a byte-for-byte audit of the pinned JAR**. Validate exact IDs and runtime abilities from locked binaries before coding. Upstream branches can change after this audit.
- The upstream sources prove *what is offered* in those trees; they do **not** prove that JAWR 0.5.39 exposes identical hooks, item tags, form models, costs, or cures. All transferred mechanics need JAWR-specific attribution and validation.

**Disposition legend:** **KEEP → Tyz** = make a single gated skill/ability candidate in the existing class tree; **NATIVE/COMPARE** = prefer JAWR's innate physiology/progression/action, and add no duplicate unlock; **CONDITIONAL** = useful only if verified JAWR mechanics permit it and the other magic mods do not already provide it; **DROP** = no transfer. These are design decisions, not approved final numeric balancing.

## Vampire level tree — source-backed 1.10.x census

| Original skill / ID fragment | Disposition | Destination / specific behavior |
| --- | --- | --- |
| Becoming a Vampire, `vampire` (level root); Become Vampire Lord, `vampire_lord` | NATIVE/COMPARE | JAWR transformation and level track, not purchasable in Tyz |
| Night Vision, `night_vision` | NATIVE/COMPARE | JAWR base vision if present; existing **Nocturnal Vision** is a limited *enhancement*, not a second form unlock |
| Regeneration, `vampire_regeneration` | KEEP → Tyz | **Crimson Renewal**: blood-*debited* healing, bounded and cooldown-limited. If JAWR already provides healing action, improve efficiency rather than adding an identical button |
| Fledgling, `fledgling` (bat form + infect) | NATIVE/COMPARE | JAWR infection and bat form remain innate/native milestones. **Bat Mastery** may improve verified bat mode, never buy the transformation again |
| Vampire Rage, `vampire_rage` | KEEP → Tyz | **Blood Regent / predatory stance**: late burst physical damage and speed with actual JAWR blood debit; avoid generic Potency stacking |
| Advanced Biter, `advanced_biter` | KEEP → Tyz | **Efficient Feeding**: bonus yield on real JAWR bites/feeding only; capped, not free blood from misses |
| Finisher, `sword_finisher` | DROP | Weapon-specific execution risks bypassing boss balance; do not import item dependencies or instant-kill design |
| Dark Blood Projectile, `dark_blood_projectile` | DROP | Redundant ranged combat spell: Ars/Iron's and other magic mods already own ranged spellcasting |
| Blood Charge, `blood_charge` | CONDITIONAL | Consider a **portable blood-refill utility** only if JAWR has a meaningful legitimate blood container/charge mechanic; validate exact resource transfer |
| Freeze, `freeze` | DROP | Generic AoE crowd control duplicated by magic schools |
| Sunscreen, `sunscreen` | CONDITIONAL | **Sunshroud**: short, blood-costing partial mitigation, preserving actual JAWR sun vulnerability and equipment value; never blanket immunity |
| Attack Speed Boost, `vampire_attack_speed` | KEEP → Tyz | Vampire-specific bounded physical speed enhancer, preferably integrated into **Predation** rather than duplicate a generic Core skill |
| Speed Boost, `vampire_speed` | KEEP → Tyz | **Nightstalker mobility**: bounded form-dependent travel speed, not a second universal movement stat purchase |
| Blood Vision, `blood_vision` | CONDITIONAL | Upgrade existing **Shadow Stalker / predatory senses** with nearby living-target awareness; do not add unlimited through-wall x-ray |
| Garlic Blood Vision, `blood_vision_garlic` | CONDITIONAL | An optional later sense upgrade only if JAWR has source-verified toxic/garlic blood types; otherwise drop |
| Attack Damage Boost, `vampire_attack_damage` | KEEP → Tyz | **Fang Training / Blood Duelist** limited to vampire bite or qualified melee; avoid buffing every spell and weapon indiscriminately |
| Jump Boost, `vampire_jump` | CONDITIONAL | Small vampire agility upgrade, but avoid duplicate generic mobility/flight powers |
| Fast Recovery, `neonatal_decrease` | DROP | Shortens *Vampirism-specific* neonatal phase; JAWR has no proven matching state |
| Swift Resurrection, `dbno_duration` | DROP | Shortens *Vampirism-specific* DBNO/resurrection timer; don't transfer into JAWR or Tombstone death arbitration |
| Teleport, `teleport` | CONDITIONAL | Possible high-level vampire **short blood-funded blink**, only if it adds distinct value beyond Ars/Iron's spells and is safe with claims |
| Summon Bats, `summon_bats` | DROP | Summoned minion/pet-equivalent and no required feature |
| Tough Skin (sun), `less_sundamage` | CONDITIONAL | Modest JAWR sunlight-damage resistance if mod-specific damage attribution is available; do not cancel weakness |
| Water Resistance, `water_resistance` | CONDITIONAL | Only if JAWR has a real water-based weakness; no meaningless skill |
| Frugal Vampire, `less_blood_thirst` | **KEEP → Tyz** | **Blood Thrift**: reduce verified blood costs/hunger, minimum cost and no double-counting with Core Thrift |
| Disguise, `vampire_disguise` | DROP | Mod-specific disguise/faction detection and likely overlap with magic stealth; not worth its own compatibility pipeline |
| Damage Limiter, `half_invulnerable` | KEEP → Tyz | **Pale Resilience/Elder Presence**: short blood-paid emergency damage mitigation, with cap and cooldown, never invulnerability |
| Invisibility, `vampire_invisibility` | DROP | Existing magic schools handle invisibility; vampire tree can improve stealth without another invisibility spell |
| Hissing, `hissing` | CONDITIONAL | Bounded predatory **disorientation/fear** effect, if JAWR lacks equivalent; avoid knock-on mob/NPC faction or pet mechanics |

**Vampire Lord tree:** `vampire_minion_stats_increase`, `vampire_minion_collect` and shared `lord_speed`, `lord_attack_speed`, `minion_recovery` are **all DROP**. Their owner is the retired TeamLapen faction lord/minion system. Do not recreate minions, raid leadership, group-faction lords or a new lord skill wallet. All remaining player bonuses are subject to *JAWR progression* gates, not Vampirism lord levels.

### Vampire recommended amendments to the current nine-tree blueprint

1. **Keep** its existing Bloodcraft, Nightstalker, Predation and Undead Constitution lanes, rather than spawning a separate copied TeamLapen category.
2. Add explicit **Blood Thrift** and bounded **blood-funded predatory stance** as candidates. Blood Thrift must act on an actual JAWR cost and never reduce it to zero unless the source cast is free by design.
3. **Efficient Feeding**, **Crimson Renewal**, **Fang Training**, **Shadow Stalker**, **Bat Mastery**, **Sunshroud**, and **Elder Presence/Pale Resilience** already cover the strongest original concepts. Do not count the same ability twice in a skill list.
4. Change all requirements saying **dual native vampire**, **two native progression tracks**, **canonical dual blood pool**, **Vampirism lord** to **JAWR-only**. A vampire's JAWR state and progression are the authority. If JAWR lacks a named TeamLapen action, a Tyz node may unlock an original pack-owned handler; Tyz alone doesn't implement a blood debit or active effect.
5. Drop all nonplayer world/faction/minion content.

## Werewolf level tree — 2.0.3.4 source-backed census

Active branches in `ModSkills.Nodes.createSkillNodes`: **Beast** (offense), **Survivalist** (mobility/evasion), and **utility/Human** (control, survival, claw mining). The paths are not necessarily mutually exclusive; the exact tree has multi-choice nodes. The pack **will not recreate three incompatible physiology owners**: JAWR owns the actual wolf form(s).

| Original skill / ID fragment | Disposition | Destination / specific behavior |
| --- | --- | --- |
| Becoming a Werewolf `werewolf` and Werewolf Lord `werewolf_lord` | NATIVE/COMPARE | JAWR lycanthropy and native milestones; no TeamLapen lord state |
| Partial Turn `human_form`, Beast Form `beast_form`, Survivalist Form `survival_form` | NATIVE/COMPARE | JAWR-native form selection only; do not buy TeamLapen form types or invent additional bodies |
| Night Vision `night_vision` | NATIVE/COMPARE | Innate wolf sensory trait where JAWR supports it; avoid second toggled vision stack |
| Sense `sense` | CONDITIONAL | **Predatory Senses**: short-range/cooldown enemy awareness, not unrestricted wallhacks |
| Rage `rage` and Beast Rage `beast_rage` | KEEP → Tyz / CONDITIONAL | Merge into **Primal Fury**, bounded action with resource/fatigue and form gate. Beast Rage's original *daytime penalty bypass* only applies if JAWR has an equivalent penalty |
| Thick Fur `thick_fur` and Improved Damage `damage` | KEEP → Tyz | Bounded natural defense and **Rending Claws**; only legal JAWR wolf-form damage, not every magical attack |
| Stun Bite `stun_bite`, Bleeding Bite `bleeding_bite` | KEEP → Tyz | **Bite Techniques**: choose one specialized status at a time, true wolf bite event/attribution, bounded stun/bleed, boss exclusions |
| Bloodlust `health_after_kill` | **KEEP → Tyz** | **Bloodlust Recovery**: heal on **actual player-credited hostile kill** (cooldown, no pet/DoT kill duplication and no farmable rapid infinite heal) |
| Frightening `fear` | CONDITIONAL | Late short-radius **Feral Intimidation**; no permanent creature pacification or raid/faction mechanics |
| Improved Speed `speed`, Jump `jump` | KEEP → Tyz | **Predator's Stride** and bounded jump mobility (no generic passive flight) |
| Leap `leap` | **KEEP → Tyz** | **Pouncing Leap**: short aimed jump, collision/landing safety, cooldown and claim checks |
| Wolf Pawn `wolf_pawn` | KEEP → Tyz | **Surefooted Predator**: reduced block environmental/fall damage; narrow list and no blanket hazard immunity |
| Climber `climber` | CONDITIONAL | Native-like step assist while in verified wolf movement mode; avoid redundant standalone Monk passive |
| Efficient Diet `efficient_diet` | CONDITIONAL | Reduction in real JAWR lycan hunger/saturation costs only; do not invent a second diet owner |
| Arrow Awareness `arrow_awareness` | **KEEP → Tyz** | **Arrow Awareness**: capped moving-projectile evasion with cooldown; no perpetual immunity to crossbows or bosses |
| Movement Tactics `movement_tactics` | **KEEP → Tyz** | **Evasive Sprint**: bounded sprint dodge with diminishing returns/shared avoidance ceiling with Arrow Awareness |
| Howling `howling` | CONDITIONAL | A **player-only** temporary self/group haste effect is possible; does not summon or command pets. Defer unless group role is valuable |
| Wolf Pack `wolf_pack` | DROP | Original summons wolves on howl; owner explicitly excludes pets/minions |
| Digger `digger`, Enhanced Digger `enhanced_digger` | **KEEP → Tyz** | **Feral Excavation**: claw break speed and finite permitted harvest tier, not arbitrary protected ore bypass. Deduplicate from universal mining ranks |
| Not Meat? `not_meat` | NATIVE/COMPARE | JAWR feeding/food rules; dietary access should not be purchasable as an optional stat rank |
| Improved Regeneration `health_reg` | **KEEP → Tyz** | **Regenerative Flesh**: especially cost-effective werewolf *health* regeneration; not magic regeneration; budget with Bloodlust |
| Free Will `free_will` | NATIVE/COMPARE | JAWR owns forced full-moon transformation; decide as a JAWR-native perk or pack-level cure rule, not a purchased workaround that silently breaks curse mechanics |
| Silver Blooded `silver_blooded` | **KEEP → Tyz** | **Tempered Against Silver**: partial silver resistance only, preserving meaningful vulnerability; exact damage source verified |
| Wear Armor `wear_armor` | NATIVE/COMPARE | JAWR's equipment/shape permissions; no TeamLapen form-based equipment rule |
| Water Lover `water_lover` | CONDITIONAL | Mild underwater environmental perk only if real JAWR debuffs exist; not a new underwater immunity package |
| Hide Name `hide_name` | DROP | Cosmetic/player-detection gimmick with PvP exploit risk, not core lycan skills |

**Werewolf Lord tree:** `werewolf_minion_stats_increase`, `werewolf_minion_collect` and shared `lord_speed`, `lord_attack_speed`, `minion_recovery` are **all DROP** (faction minions and lord mechanics).

**Registration-vs-tree distinction:** upstream Werewolves 2.0.3.4 source registers `resistance` and `sixth_sense` but does not include them in the generated active tree's `createSkillNodes` mapping. `throat_seeker` appears in the language file but is **not registered as a live skill** in observed `ModSkills.java`. Thus neither **Improved Resistance**, **Sixth Sense** nor **Throat Seeker** can be claimed as successfully purchasable active skills in this source revision. Their ideas can be independently considered: natural defense already covered by **Thick Fur**; danger warning is a **CONDITIONAL** hunter/wolf sensory feature; **Throat Seeker** instant-kill finisher is **DROP** for boss balance.

### Werewolf recommended amendments to the current nine-tree blueprint

1. Existing **Lupine Vitality** already covers extra hearts and regeneration, **Feral Might** covers melee and rage, **Moonrunner** covers speed/leap, and **Wild Communion** covers nature magic. **Do not replace the identity budget** with a wholesale copied Werewolves tree.
2. Add as distinct high-value candidates not fully covered: **Bloodlust Recovery**, **Bite Techniques (stun/bleed choice)**, **Arrow Awareness + Evasive Sprint** (shared survival cap), **Feral Excavation** (finite claws/harvest tier) and **Tempered Against Silver** (partial vulnerability mitigation).
3. Incorporate **Surefooted Predator** in Moonrunner or defense. Add limited Predatory Senses only if not an existing JAWR feature.
4. Keep **very large maximum hearts and strong HP regeneration per SP**, as previously approved. Do not grant werewolf-only magic regeneration; nature magic is specialized and personal.
5. Remove the phrase **unified werewolf owner remains unselected** from the draft: **JAWR is selected**, pending verified actual state APIs. Remove Werewolves-native skill/level gates, lord trees, howling-to-wolf-summon, duplicate forms and pet dependencies.

## Recommended one-wallet shortlist (net new or clarified)

| Priority | Tyz category / concept | Expected owner and implementation boundary |
| --- | --- | --- |
| P0 | Vampire **Blood Thrift** and **Efficient Feeding** | Tyz purchase; JAWR actual blood-spend/bite handler; verify no zero-cost or duplicate production |
| P0 | Vampire **Crimson Renewal** and **Blood Regent** | Tyz unlock/amplifier; original active handler only if JAWR's healing/rage does not already satisfy it |
| P0 | Werewolf **Feral Vigor**, **Regenerative Flesh**, **Bloodlust Recovery** | Tyz ranks; authoritative JAWR state; bounded max-health, timer and credited-kill effects |
| P0 | Werewolf **Rending Claws**, **Bite Techniques** | Tyz licenses; original JAWR-form melee/bite attribution; no generic magic bonus or boss execute |
| P1 | Werewolf **Feral Excavation** | Tyz ranks plus real harvest tier/break events; claim/tool permission and loot correctness |
| P1 | Werewolf **Arrow Awareness + Evasive Sprint** | One combined evasion budget, mob/player projectile and armor tests |
| P1 | Werewolf **Pouncing Leap**, **Predator's Stride**, **Surefooted Predator** | Form/movement restrictions, landing collision, bounded environmental protection |
| P1 | Werewolf **Tempered Against Silver** | Preserve silver weakness; verify JAWR damage events and effects |
| P1 | Vampire **Nocturnal Vision/Shadow Stalker** and bounded **Sunshroud** | Compare JAWR innate senses/sun effects; no free full-daylight immunity |
| P2 | Vampire **blood-funded teleport/control**, Werewolf **senses/fear**, group **Howling** | Only if distinct from existing magic/JAWR and not minion mechanics |

**Not in scope:** Hunter skills audit/implementation and Monk implementation; their independent workstreams own those decisions. No dropped-mod worldgen, raids, pets, villages, outposts, structures, minions, or equipment/assets are being preserved in this audit.

## Integration checklist for the Tyz tree author

- Apply [new retirement target](TEAM-LAPEN-RETIREMENT-TARGET-2026-11-15.md) to [nine-tree blueprint PR #83](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/83). Rebase any in-progress design so it no longer asserts dual-provider vampires or uncertain Werewolves physiology.
- Keep stable Tyz category IDs `tmm_vampire`, `tmm_werewolf`; keep Core and six other custom category IDs unchanged.
- Create no stat duplicated in both Core and class-specific categories at equal efficiency. Source-classified werewolf *health* regen is not personal mana regen.
- Set external gates at the Tyz **node** level (native prereqs alone check ownership, not prior rank 99); gate every effect on the live JAWR server-side state, not merely a visible tab. Cure suspends purchased skills; refund rules and grant reconciliation are separately checked.
- Preserve original pack power ceilings: no creative flight, protected-block harvesting bypass, boss-execution instant kills, infinite evasion, costless blood, or unbounded health regeneration.
- Need exact-JAR skill/JAWR API validation and client/survival tests before `active:true`; **no runtime change has been made by this document**.
