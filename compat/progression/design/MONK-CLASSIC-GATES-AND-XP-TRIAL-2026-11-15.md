# Monk: full classic progression gate inventory and functional cultivation XP trial

**Date:** 2026-11-15. **State:** experimental standalone core branch only; NOT merged into full integration.  
**Code/source:** [Monk Mod 1.4 original configuration](https://github.com/rwtema/Monk-Mod/blob/master/src/main/java/com/rwtema/monkmod/config/MonkConfiguration.java) and [requirement implementations](https://github.com/rwtema/Monk-Mod/tree/master/src/main/java/com/rwtema/monkmod/advancements/criteria).  
**Contracts:** [exact 21-stage source audit](monk-original-21-gate-inventory-v1.json), [functional XP trial profile](monk-xp-functional-test-profile-v1.json), [real Monk path](../src/main/resources/data/tmm_progression/tmm_progression/paths/monk.json). [Full 25-ability design #100](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/100).

## 1. Original source: **21 stages, 0 through 20**, each a mandatory gate

The original mod was **not XP-based**. The next stage was awarded exclusively by performing its current task. Importantly, **stage 0 actually required breaking one wood block by hand**; its first sun-looking ordeal was at stage 2. Our new **First Sunset** is an intentional redesign of initiation, not a claim about the original.

| Stage | Original requirement | New XP milestone status | One-time bonus |
| ---: | --- | --- | ---: |
| 0 | 1 wood block by hand | Intentional replacement | — |
| 1 | 10 wood blocks by hand | Live optional deed | +35 |
| 2 | Look directly at horizon sun, 200 ticks | Pending detector | +70 |
| 3 | Underwater with low air, 280 ticks plus RNG | Pending detector | +70 |
| 4 | 5 stone blocks by hand | Live optional deed | +50 |
| 5 | Pet 20 DISTINCT animals | Partial evidence | +80 |
| 6 | Sprint 200 blocks | Pending detector | +90 |
| 7 | Kill 10 undead unarmed | Live optional deed | +120 |
| 8 | Dodge 5 skeleton arrows without impact | Pending detector | +100 |
| 9 | Break 4 iron blocks by hand | Live optional deed | +100 |
| 10 | Gaze at moon from boat, 200 ticks | Pending detector | +130 |
| 11 | Calm Enderman proximity, 600 ticks | Pending detector | +140 |
| 12 | Walk 10 blocks in active fire | Pending detector | +140 |
| 13 | Break obsidian by hand | Live optional deed | +175 |
| 14 | Pacify creeper with empty-hand interaction | Live optional deed | +175 |
| 15 | 200-tick Wither Skeleton gaze in Nether Fortress | Pending detector | +190 |
| 16 | 40-block fall onto hay | Pending detector | +210 |
| 17 | Sleep and wake on paired bedrock blocks | Pending detector | +230 |
| 18 | Kill 5 hostiles while blinded | Live optional deed | +270 |
| 19 | Deal >25% of Wither HP unarmored/nonprojectile | Partial evidence | +550 |
| 20 | 15 intercepted lethal void falls | Intentional replacement | +500 |

**What the audit verified:** 7 source-inspired deeds have working server event mappings; 2 are intentionally replaced (one-log initiation → First Sunset, repeated void deaths → Dragon-gated Momentum Gliding); 2 are partial (distinct animal proof and Wither contribution); 10 need dedicated detectors and are **not** disguised as valid live milestones.

Distinct source details matter. Animal petting counted **20 different animals per player**, not 20 repeated clicks. The skeleton trial tracked arrows fired by skeletons targeting the Monk and reset upon impact. The Wither trial required **over 25% max-HP damage personally dealt**, unarmored and without projectile damage; a merely player-credited Wither death is a weaker trial. The last original stage canceled **15 lethal deep-void falls**, restored health and granted Creative Flight. That dangerous loop is deliberately *not* carried forward.

The original Sunrise trial required **200 consecutive ticks aimed at the horizon sun**. The squid trial required roughly **280 underwater ticks with low air**, then a random eligible tick. The boat trial aimed at the moon for 200 ticks; the Enderman trial lasted approximately 600 ticks near a calm Enderman. The hay fall required a **40+ block fall onto hay**, not just any survived drop. Those precise semantics are in the machine audit with links to original requirement classes.

## 2. New progression rule: earn XP by practice, earn additional XP by optional deeds

**First Sunset** remains the only mandatory join ritual. Once joined, any legitimate activity advances **custom Monk cultivation XP** regardless of which classic deed the player completed. At rank thresholds, all classic baseline abilities are granted by `auto_ranks`, never Tyz, vanilla experience, or paid native skill points.

**One intentional rank-ability exception:** Momentum Gliding needs **both** the native rank-20 unlock and the Dragon milestone. The old Void Death ritual is completely retired. Future Chaos Guardian specialization is separate from this *pre-Tyz classic ability* baseline.

### Live, testable training activity economy

| Server event | Cultivation XP | Notes |
| --- | ---: | --- |
| `barehand_block_break` `#minecraft:logs` | 2 | Each real hand-broken log; no cooldown so genuine TreeChop removed logs can count individually |
| `barehand_block_break` `#minecraft:mineable/pickaxe` | 1 | Each eligible hand-broken stone/ore-category block; verified post-break player signal |
| `kill` `#tmm_progression:monk_training_hostiles` | 8 | Any credited enemy kill of curated hostile/neutral combat species; spell kills may qualify when player attribution works |
| `unarmed_kill` `#tmm_progression:monk_training_hostiles` | 4 | Additional bonus ONLY for direct empty-main-hand melee; not arrows/spell projectiles |
| `food_discovered` `*` | 10 | Once per distinct successfully consumed edible registry ID across saves/death |
| `advancement` `*` | 25 | New advancement earned after enrollment; technical `recipes/` unlocks excluded |
| `animal_petted` `*` | 2 | Only successful animal-affection server interactions; 20 unique animal requirement still incomplete |

The implementation adds **two first-class trusted event types** to the generic path engine: `barehand_block_break` and `unarmed_kill`. The parser understands both. The former is sent from a non-canceled player-owned `BlockDropsEvent` with empty actual tool and empty main hand, **not** from the premature `BlockEvent.BreakEvent`. The latter is emitted on an actual attributed direct melee kill, not an arrow/spell. These signals retain real block/entity tags and do not let clients submit custom XP numbers.

The vanilla pilot tag `#tmm_progression:monk_training_hostiles` combines existing undead/raider groups and extra vanilla combat mobs, not passive livestock; pack-wide modded mobs must be added through verified tag extension later.

### One-time classic-inspired milestones

The path contains **15 one-time XP milestone definitions**: First Sunset; 10 and 100 barehand logs; five stone; 10 empty-fist undead; 20 accepted animal interactions; four iron blocks; one obsidian; Creeper Kiss; five blind-hostile defeats; player-credited Wither; Dragon; 20 hostile defeats; 10 and 30 earned advancements. These persist on the native path ledger and cannot pay repeatedly.

- `trial_creeper_kiss` uses a newly emitted **trusted server external event** on each valid Monk creeper interaction; **the milestone pays only once per player**, even if a different Monk already pacified that creeper.
- `blind_ordeal_victory` now requires **five** server-credited **direct-fist** hostile kills while the player has the opt-in Blindness ordeal active. Automatic rank 18 does not falsely complete the ordeal; the player can abort and resume, and no permanent blindness is enforced.
- Original pet20 is **not yet distinct-by-animal**. The current partial milestone awards once after 20 successful interactions; a unique-entity ledger is required for exact parity.
- Original Wither damage-contribution conditions are **not yet enforced**. The present player-credited kill milestone is a provisional optional XP award, not classic parity certification.
- The remaining 10 original environment and movement gates are **design-only** until proper server detectors exist. No fake `external` stub milestone is loaded in the runtime.

## 3. Experimental rank curve

The prior 20-rank path reached rank 20 at **28,000 XP**. For this initial functional test, keep the early **0 / 20 / 55 / 105** thresholds for ranks 1–4, but make the late curve less punishing. The current profile reaches rank 20 at **14,300 XP**. This is **not final balance** and isn't a claim about real hours needed.

| Rank | Total Monk XP | Rank | Total Monk XP |
| ---: | ---: | ---: | ---: |
| 1 | 0 | 11 | 1,650 |
| 2 | 20 | 12 | 2,200 |
| 3 | 55 | 13 | 2,900 |
| 4 | 105 | 14 | 3,750 |
| 5 | 180 | 15 | 4,800 |
| 6 | 280 | 16 | 6,100 |
| 7 | 420 | 17 | 7,700 |
| 8 | 600 | 18 | 9,600 |
| 9 | 850 | 19 | 11,800 |
| 10 | 1,200 | 20 | 14,300 |

Illustrative arithmetic checks using the actual JSON and `ProgressionMath.grantXp`:

| Mixed activity sketch | Example XP | Resulting rank |
| --- | ---: | ---: |
| First-hour novice (illustrative) | 259 | 5 |
| Mixed day of training | 1,730 | 11 |
| High-activity endgame | 15,340 | 20 |

These are **simulated reward budgets**, not estimates of elapsed playtime. Ordinary ranks can be gained with no optional deed completed. Original deeds only help accelerate XP, not block the classical Monk ability suite.

## 4. Disposable-world test walkthrough

Run a dedicated NeoForge server with the new `tmm_progression` JAR + datapack from this branch. The full-integration manifest is **unchanged**. Enter as a survival player, watch sunset while empty-handed and unarmored for 200 ticks, then check:

```text
/tmmprogress status tmm_progression:monk
```

Test ten real barehand logs first (**+2 each; tenth additionally +35**). Mine five stone by hand (**+1 each; fifth additionally +50**). Try the same breaks with real tools—those must not earn barehand XP. For 10 direct-fist undead kills, expect **8 hostile XP plus 4 fist XP per kill**, with an additional **120 one-time milestone XP** upon the tenth. Projectile or spell kills should not earn the fist bonus.

Eat several distinct edible items, then repeat the same food; **only new registry IDs** yield +10. Earn a real visible advancement (+25), then unlock a recipe-only advancement (should be **zero**). After 100 legit logs, confirm the one-time 100-log reward and any rank-ups; relog and make sure no milestone pays twice.

At the appropriate ranks, try `/tmmprogress power ordeal` and make **five direct-fist blinded hostile kills**, checking that rank 18 alone does not close the challenge. Try `/tmmprogress power treasure`, `/tmmprogress power armor`, and Creeper Kiss as additional server event checks. Ender Dragon advancement must be in the native completed-milestones set before gliding is legal even at rank 20.

To bypass First Sunset strictly for testing, OP can use `/tmmprogress admin join tmm_progression:monk`. That is an operator cheat and not player enrollment. No general XP-grant command is needed for normal testing.

## 5. Known real gaps that this configuration **does not hide**

1. **Placed-block XP farming:** the generic engine does not yet distinguish naturally generated from player-placed blocks. A repeated place/break loop can grant hand mining XP. We leave zero per-log cooldown so a verified TreeChop multi-log action can eventually credit *every actual removed log*. Before shipping, add provenance and claim-safe final-credit deduplication.
2. **TreeChop and magic attribution:** post-commit all-log signals and spell-owner mining signals need adapter QA. A path JSON alone cannot guarantee those events exist.
3. **Modded hostiles:** the training target tag covers a vanilla-only pilot roster, not the full creature taxonomy being assembled in the Hunter thread.
4. **Source-flavored missing deeds:** moonboat, sunset gaze, drowning, sprint distance, skeleton dodge, nonaggressive Enderman meditation, firewalk, Wither Skeleton gaze, 40-block hay landing and bedrock sleep need verified server-world detectors.
5. **Boss credit and history:** Dragon advancement earned *before* Monk enrollment will not automatically backfill; the current advancement may also include nearby participants rather than a strictly credited personal defeat. Wither 25% gearless contribution is not yet tracked.
6. **Harmful status lifecycle:** Blindness Ordeal works only through server verified kills. Death/relog/abort must clear lingering forced blindness; progress counts persist unless explicitly reset.
7. **Advancement abuse:** recipe IDs are filtered, but hidden modded technical advancements also need vetting before a broad pack release.
8. **Balance after actual play:** use survival XP-per-hour logs across three progression styles before treating 14,300 as a fixed cap.

**Engineering verification:** [MonkClassicXpTrialTest](../src/test/java/io/github/heatwaveqc/tmm/progression/MonkClassicXpTrialTest.java) parses the *live* Monk JSON and all 21 source gates, validates authorized signals and 15 milestone definitions, checks free classic automatic unlocks, and simulates three XP curves. A green JUnit/server-boot check is not an in-world XP rate audit.
