# November 15 integration QA repair backlog

**Source of truth:** [Nov 14–15 player QA report (PR #80)](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/80). Tested **integration SHA 3d3dd9f6c12f52500da5130d14978e657f3b94dc**, not today's rolling branch. This is a prioritized owner-request/repair handoff; **not a claim that later work passed a new gameplay test**. PR #80 is still a separate QA-documentation PR. The baseline report, raw local logs, exact binaries, and player results must not be conflated with CI-only source checks.

## Acceptance / release policy

- P0 = blocks a coherent core play path; P1 = must resolve before alpha test signoff; P2 = supporting experience or diagnostic work. Reprioritize on evidence.
- Proposed/compiled != integrated. Each acceptance case below needs a **fresh disposable client world on the exact new, hashed integration ZIP**. No candidate/stable promotion from CI alone.
- Record item IDs from the actual loaded registry, not intuitive names; retain both positive and negative cases. Preserve the approved exclusions, original progression design, and no automatic form overlaps.
- Until CI's live registry gate passes, **do not publish the new full-integration Prism QA artifact**. Existing missing quest IDs should fail that gate rather than be silently allowlisted.

## P0 — repair before interpreting further gameplay

| Area | What remains | Required acceptance |
| --- | --- | --- |
| Monk native skill tab | Rank 2 had three native points but Vampirism displayed its **no-tabs** message "You Cannot Learn Skills." [PR #71](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/71) has a **compiled, untested** repair for plural skill-tree/node resource paths and faction unlock predicate. | Fresh First Sunset rank-1 character opens the native tree; can buy each first-tier choice; rank/point costs and purchases persist across relog and death. Confirm no premature Step Assist. Do not treat Tyz as Monk's native skill UI. |
| Ascended vampire blood state | Lilith transition left both reported pools at zero and neither feeding nor blood setter appeared to restore them until relog; bottles worked after relog. Root cause could be ownership, initialization or sync; do not assume all feeds always fail. | Instrument authoritative JAWR blood, Vampirism mirror/bridge state, packet/UI values and feeding/debit events **immediately before/after Lilith** and **before/after relog**. Test bottle, native feeding, ability cost, healing, recharge, world save and dual cleanup. |
| Ascended blood HUD | No blood bar when empty **or full**; Iron's mana bar appeared to move down. Not a proven absence-of-duplicate-bars success. | Show one correctly sourced JAWR canonical blood bar across zero/partial/full, with correct armor/mana/Colorful Hearts layout and no erroneous vanilla bar. |
| Quest runtime item registry | Client log reports unknown item IDs `cookingforblockheads:oven` and `biggerreactorsrework:cyanite_reprocessor`, leaving FTB missing-item placeholders. Quest structural checks did not catch them. | Find exact item or correct quest design through same-build registry/JEI; fix **both icons and objectives/rewards** without guessing IDs; live CI registry gate passes on all 45 chapters. |

## P1 — next playable feature/regression pass

| Area | Required action and target check |
| --- | --- |
| Monk rank-one viability | Verify native Open Palm (+1 barehand damage), Unarmored Poise (+2 armor), Stone Fist (+4 mining efficiency and stone-tier hand harvest) from PR #71. Confirm equipment suppression and restore, no higher harvest tiers by accident; improve combat/more mastery actions only with explicit new balance values. |
| Monk TreeChop accounting | Four entire trees gave four mastery in prior build. New listener is intended to credit **actually removed logs**. Test seven-log tree = exactly seven mastery, partial chop = zero, lone log = one, leaves = zero; block placed-log XP farming/duplicate events. |
| Monk spell harvesting | Ars Nouveau, Iron's and Roots player-attributed actual block removal should credit mastery even while holding appropriate casting implements. Conventional tools, canceled breaks, attempts and ownerless synthetic destruction must not yield credit. Probe multi-block events; add source-specific adapters if the generic hooks miss a spell. |
| Monk transformation exclusion | Existing Monk eligibility omits external dragon/physiology exclusion; test and enforce inability to join/remain Monk while a dragon, lich/archlich, vampire/JAWR, werewolf, abomination or dracolich, with safe transition behavior. Do not mistake an unrelated successful transformation for exclusivity proof. |
| Ritual-only dragon acquisition | `config/dragonsurvival-server.toml` under `[general]`: set `start_with_dragon_choice=false`, `allow_dragon_choice_from_inventory=false`; keep `transform_altar=true`, `no_humans_allowed=false`. Verify from genuinely new players; old worlds are disposable. |
| Dragon diets and rendering | Exact sourceberry, enchanted golden carrot and sushi IDs before modifying rules. Respect Sea grain restrictions and approved magical-food exceptions. Forest had missing sourceberry/carrot permissions; sushi lacked the Sea tag; Cave native charring worked. Reproduce dragon-form/inventory visual glitches separately and do not assume a cause. |
| First-login trinkets and bag | Move the two Enigmatic trinkets from automatic grant to once-per-player **Getting Started checkmark rewards**, using exact registered IDs; move Ars Elemental bag to a claimable quest reward and create/verify dedicated **bundle Curios slot**. No duplicate grants on relog, quest teams or repeat claims; bag keeps contents. |
| Akashic Tome | Initial Tome is empty while Astral/Primal/Ars manuals are separate. Audit exact-version supported book registration and prepopulation/combination behavior; verify book switching and persistence, rather than granting an empty Tome as a fake pass. |
| Progression gates and quest registry | Inspect item tasks, reward items and icon references against the loaded **full build**. Revalidate authored quests and progress after regenerated files, starter reward changes, new Ars chapter and progression content. Confirm Chaos Guardian / ProjectE safeguards separately; don't infer from displayed quest text. |

## P2 — repairs, source audits, or deferred acceptance

| Area | Work / evidence boundary |
| --- | --- |
| ProjectE × Goety | Reproduce mapping errors for `goety:totem_of_roots` and recipes `goety:savage_tooth_craft`, `goety:empty_focus_craft2`. World did not crash; map completeness and unintended EMC loops still unknown. |
| Rendering and asset diagnostics | Reproduce and attribute animation errors (Dragon Survival mass_buff/flapping_wings_standing; Theurgy calcination; Ars Elemental spike; Ars Nouveau Wilden Chimera). Investigate missing Botania barrel models and declared ProjectExpansion access transformer. Avoid mass-disable changes without a proven symptom. |
| Biomes/book content | Patchouli errors mentioning Vampire Forest/Werewolf Heaven are **not** proof the biomes do not exist; player saw Vampire Forest shelter. Check exact-book entries and stale `apotheosis:long_sundering`, `hexerei:belladonna_flower`, and optional icons. |
| Mob alliances | Census: 613 non-misc modded entities, 129 classified, 484 unclassified. Add and verify undead neutrality toward vampire/lich, tagged fae/cave-demon relationships for forest/cave dragons, retaliation and boss exclusions; test actual AI, not just static tags. |
| Vampire sunlight | Early JAWR guide indicates instant-lethal sunlight may be native. Compare level, damage source and protections before filing as a bridge defect; Vampire Forest shelter itself passed. |
| Deferred functional testing | JAWR-native cleansing rite, Mahou/Catalyst Exchange intended compatibility, Comforts sleeping, full dragon negative diets, exact Tyz refunds/scaling, complete 47-offer shop and coin loops, relic persistence, form death/restart/dimension transitions, all-world performance, multiplayer. Do not mark omitted tests passed. |

## Required new registry-validation gate

- `scripts/quest_registry_gate.py prepare` scans **all staged** `config/ftbquests/quests/chapters/*.snbt` for every item task/reward and every icon. Unsupported syntax is a hard error, not silently skipped. It writes a temporary manifest (with per-file locations and SHA-256) and a temporary KubeJS server-load probe.
- The full-integration workflow copies the generated probe **only** into its disposable NeoForge server. At `ServerEvents.loaded`, KubeJS asks `BuiltInRegistries.ITEM` whether each exact resource ID is registered. The probe logs missing IDs and a checksum-bound summary.
- `scripts/quest_registry_gate.py verify` requires **exactly one** live summary, checks digest/count and the complete missing-ID log, and **fails the workflow before snapshot tagging or Prism upload** on any unknown item ID, absent probe, mismatch or inconclusive result. No mod binaries, world data, or QA probe are committed to the final pack.
- `.github/workflows/verify.yml` executes the probe's fail-closed unit tests. Structural quest checks still run separately; successful startup/registry existence still does **not** prove item obtainable, correct icon aesthetics, FTB task credit, recipe validity, or unlock balance.
- **Expected initial outcome:** the two known bad IDs may turn the next integration build red until their real replacements are verified. This is intentional; it must not be bypassed by allowing missing IDs or skipping the live step. If the KubeJS bridge itself needs an API repair, the missing valid summary likewise fails rather than granting an unearned pass.

## Suggested next QA sequence

1. Resolve quest IDs to unblock the exact-version registry gate, then execute the full combined server smoke.
2. Disposable-world Monk: rank-1 purchase → native skills → seven-log TreeChop → Ars/Roots/Iron's actual block breaks → gear exclusions and persistence.
3. Disposable-world vampire: ordinary infection → Lilith ascension → before/after relog blood state, HUD, cost, feeding, healing, both cures → sunlight context.
4. Disposable-world dragon/start: ritual-only entry, Sea/Forest/Cave food matrix, Enigmatic trinket and bundle claim, Akashic books, quest task credit.
5. Follow-on system audit: ProjectE/Goety, modded mobs, animation/resource failures and deferred capabilities.

**Status on creation:** registry gate code and CI wiring committed to this PR as an unrun runtime change; PR #71 is a compiled but not yet client-accepted Monk fix; PR #80 is player-QA documentation. Do not update any PASS labels without new evidence.
