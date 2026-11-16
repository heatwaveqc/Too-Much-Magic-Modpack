# Target: shared mob affinities and Hunter-unlocked damage enchantments

**Status:** design target / initial disposition map; **documentation only**, no gameplay code, tags, build inputs, or integration manifests changed.  
**Date:** 2026-11-15. **Target:** Minecraft 1.21.1 / NeoForge 21.1.x with the pack-pinned Apothic Enchanting build.  
**Working map:** [AFFINITY-POLICY-MAP-v0.csv](AFFINITY-POLICY-MAP-v0.csv). **Not a claim of implemented or player-tested faction AI.**

## Decisions recorded

1. Continue the existing creature taxonomy rather than starting a new classification. Its authoritative research lives in [Hunter PR #97](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/97): 477 candidate creature records, 1,393 registered entity IDs from the integrated test server, 424 present matched candidates, 44 prospective Alex's Caves candidates, five absent Malum dev-only entities, and unresolved lineages explicitly identified. **Research inventory is not a complete shipped entity-tag registry**; filter nonliving, passive, owner-bound, and unavailable entities before generating gameplay rules.
2. Reuse the standalone progression chassis in [PR #127](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/127). Its `Selectors.entity`, per-player purchased/auto skill rank ledger, effect registry, target-change hook, and prototype `faction_neutrality` already supply most of the relevant primitives. Older [mob-alliance prototype](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/feature/mob-alliance-neutrality/docs/MOB-ALLIANCES-AUDIT.md) on `feature/mob-alliance-neutrality` supplies additional behavior to port, **not a dependency to retain**: the old `compat/transformations` bridge is excluded during TeamLapen's *Vampirism/Werewolves* retirement. **Just Another Witchery Remake (JAWR) is retained**, including its native player Vampire and Lycanthropy paths; do not confuse the removed TeamLapen mods with those independent player states.
3. **Discovery-only enchantment gate**: a Hunter who purchased the matching specialization/discovery skill can roll its associated damage enchantment through an enchanting table (including eligible books), and can distribute the resulting enchanted item. **Anyone may equip, use, trade, repair, combine, and benefit from that item**, even without Hunter enrollment or after Hunter respec. Do not add runtime Hunter/use restrictions to item damage effects.
4. **One damage enchantment per item by default.** All new family-specific damage enchantments must be mutually incompatible with one another and with the vanilla damage family (Sharpness, Smite, Bane of Arthropods; on 1.21 the vanilla damage exclusion tag also contains Impaling, Density, and Breach). Prefer Minecraft's data-driven `#minecraft:exclusive_set/damage` plus the new enchantment definitions' `exclusive_set`, not bespoke stacking suppression. Allow an explicitly configured Apothic/Apotheosis compatibility-bypass path **if the pinned mod actually supports it**; investigate its precise mechanism before implementation, never assume an existing option. Default is exclusive.
5. Affinity is **non-aggression**, not taming, team membership, healing, damage immunity, loot protection, or forced friendship. Existing natural targeting behavior remains the default when there is no eligible affinity rule. An NPC that is normally passive must **not** be made aggressive by the absence of a rule.
6. **Keep three separate datasets/contracts**: creature membership (what an entity is), affinity policy (who initiates combat against whom), and hunt reward eligibility (what counts for XP/milestones). A passive familiar may be a Beast without becoming legal XP prey; a boss may be Undead without becoming pacified.
7. Multi-category creature membership is intentional, e.g. Ender Dragon = Dragon + Void, Drowned = Undead + Aquatic. Exclusivity solves **enchantment-vs-enchantment** stacking; Hunter skill damage bonuses may still stack with the one selected enchantment and need a separate balance audit. Enchanted bypass stacking is an intentional compatibility exception, not the default.

## Initial disposition map

The [CSV matrix](AFFINITY-POLICY-MAP-v0.csv) has one row per currently approved Hunter creature family and columns for ordinary human/Hunter/Monk, Vampire, Lich, Werewolf, Forest Dragon, Cave Dragon, and **Sea Dragon** player forms. **All three ordinary human/Hunter/Monk columns retain native AI by default**; enrollment in Monk or Hunter never automatically makes a monster neutral. The one existing Monk exception is per-creeper Creeper Kiss, not a species-wide Plant affinity. It is a **design seed**, not 16 sets of actively installed factions.

Legend:
- **DEFAULT**: leave vanilla/modded AI alone (potentially hostile, neutral, or passive depending on mob).
- **NEUTRAL_PROPOSED**: suppress natural/unprovoked acquisition only, subject to all higher-priority exceptions and available species-state adapters.
- **INDIVIDUAL_SKILL_ONLY**: do not pacify a whole creature family; a specific mob may be calmed by an explicit purchased/unlocked skill.
- **NOT_INSTALLED**: no relevant player form or adapter in the current pack; preserve the target without inventing a replacement species.

Approved or previously established source intentions, **pending runtime integration**:
| Attacker group | Protected target/form | Default intended disposition | Provenance and implementation gap |
| --- | --- | --- | --- |
| Ordinary **Undead** (including valid modded undead) | JAWR Vampire, supported Lich identity | **Neutral unless provoked** | Old mob-alliance prototype checks both, but its TeamLapen-era bridge is removed. Reimplement through the retained JAWR native vampire identity and supported Goety/JAWR lich identity without requiring TeamLapen's Vampirism. Source-only until integrated and tested. |
| Ordinary **Beasts** | JAWR Lycanthrope/Werewolf | **Neutral unless provoked** | Hunter taxonomy proposes this. TeamLapen Werewolves is removed, but JAWR retains lycanthropy; validate its exact native state adapter before enabling. Social/owned beasts and bosses are exemptions. |
| Ordinary **Fae** ↔ Forest Dragons | Forest Dragon mob/form ↔ Fae mob/form | **Reciprocal neutrality, if present** | Reserved in the old prototype; forest/cave dragon tag bridge did not implement player dragon states. Validate player forms when a provider exists. |
| Ordinary **Demons** ↔ Cave Dragons | Cave Dragon mob/form ↔ Demon mob/form | **Reciprocal neutrality, if present** | Reserved in the old prototype; no blind dragon/demon inference by dimensions. |
| Ordinary **Aquatic** ↔ Sea Dragons | Sea Dragon mob/form ↔ Aquatic mob/form | **Reciprocal neutrality, if present** | **New owner decision.** Apply to confirmed Sea Dragon subtype/form and verified Aquatic entity-type tag, never all dragons or every entity in an ocean biome. Exempt bosses, owner/commanded defenders and provoked mobs. Dragon Survival's actual player-form hook must be validated against the exact installed test build. |
| Explicitly pacified **Creeper** | Monk who applied Creeper Kiss | **Individual creature only** | Existing Monk behavior; never infer that all Plants should be friendly to Monks. |
| All other categories and forms | Everyone else, **including ordinary Monks and Hunters** | **DEFAULT** | No innate class-based neutrality. Hunter specializations alter damage/discovery, not the quarry's targeting. Monk's explicit Creeper Kiss affects only its pacified individual mob; no other automatic Monk friendliness. |

### Priority and exception contract

Apply in this precedence; no blanket tag-only pacification should override a higher-priority rule:

1. **Boss/story/forced encounter explicit exemption** and hardcoded/registry exclusions (including relevant modded bosses) prevent automatic neutrality. Merely adding an entity to a family tag does not make it peaceable. Keep a configurable exemption list/tag; don't use a Boss Hunter class.
2. **Owner/commanded summon defense, scripted hostility, rider/mount social loyalty, protected NPC responsibilities** retain their native targeting when relevant. Source prototype used name-pattern servant heuristics; replace with exact owner/context checks where mod APIs permit.
3. **Retaliation after a direct or attributed projectile attack**: the old prototype and progression pilot both used a 600-tick (30-second) window. Match the *actual* aggressor UUID and target mob and respect summons/projectile ownership where possible; no unconditional target drop during this window. Existing 30-second rule is a starting policy, not yet cross-mod battle-tested.
4. **Eligible affinity**: evaluate actual attacker `EntityType` tag(s) and actual protected player state or mob tag/form, then cancel unprovoked target acquisition.
5. **No matching affinity**: keep normal AI; do not synthesize hostility or friendship. Check stale targets periodically when a player gains a form or purchases/refunds an affinity skill. Preserve compatibility with non-`LivingChangeTargetEvent` attacks via targeted adapters as discovered in QA.

### Player identity and progression boundary

- Identity/form checks must be **authoritative and explicit** (player class/trait/verified external provider), not implied merely by wearing equipment, class enrollment, or target `EntityType`.
- `faction_neutrality` is already a recognized progression effect type; make it reusable for purchase-gated affiliations such as a future Fae Affinity skill. It may be conditioned on skill rank, required milestone and player identity independently.
- Reconcile/port old alliance semantics into the standalone progression layer or a tiny optional shared policy module, **without restoring archived transformation dependencies**. Separate mob-vs-player and reciprocal mob-vs-mob relationships; the current progression target-change callback primarily handles player targets. Add a **Sea Dragon subtype ↔ Aquatic** reciprocal affinity; distinguish verified aquatic species/ecology from simply living in or near water.
- Maintain server-owned state, proper `/reload` data behavior, multiplayer isolation, persistence/respec correction, and fail-closed handling of absent optional mods.

## Hunter specialist enchantment discovery contract

### Catalog and names

One **candidate** damage enchantment for each approved creature-family specialization, subject to final per-family naming and balancing: Undead (existing Smite—do not duplicate it by default), Illager, Dragon, Void, Piglin, Sculk, Aquatic, Construct, Beast, Arthropod (existing Bane of Arthropods—do not duplicate by default), Plant, Elemental, Ooze, Demon, Fae, and Aberration. Reuse vanilla Smite and Bane where their actual target-tag behavior meets the agreed taxonomy; expand compatibility tags rather than introduce redundant competing variants. Do not add candidate-only Wilden/Goety Cult/Hobgoblin/etc until separately approved.

**Discovery policy** (for each new enchantment):
- A named Hunter skill/rank is a **required prerequisite for that player's enchanting-table candidate pool**, not a requirement to use or receive the enchantment. Skill purchase and respec affect **future offers only**, not existing items.
- Restrict *ordinary alternative discovery channels* for Hunter-only enchantments: no default librarian books, random chest enchantments, random mob/spawn equipment, default non-Hunter enchanting, or other automated enchantment-roll sources unless explicitly granted a pack exception. Admin/creative test tooling remains available. Reconcile with Apothic affix/loot sources instead of assuming vanilla tags cover every mod.
- A server-side gate must filter the **actual player-specific roll candidates before generating an offer** and validate on final enchant application (including book rolls), to prevent stale or client-spoofed offers. Global `#minecraft:in_enchanting_table` is not player-specific and cannot alone encode this gate. Check the actual pinned Apothic Enchanting roll path, table stats, offer sync/recalculation, and relevant mod APIs/mixins before choosing the exact interception point.
- **Equip/use are unrestricted**, as are anvil application/repair/trade/transfer. Other players can benefit from Hunter-made equipment. A skill respec does not erase or deactivate stored enchantments.
- Preserve normal enchantment weights/levels/cost ranges and prevent new categories from swamping the table's global pool. Exact values are later balance decisions; don't blindly assume Smite +2.5 damage/level is fair for every family.

### Compatibility and exclusivity

Use vanilla 1.21 enchantment data `exclusive_set` and `#minecraft:exclusive_set/damage` to ensure newly defined family damage enchantments are **mutually exclusive** and mutually exclusive with Sharpness, Smite, Bane, Impaling, Density, and Breach by default. Validate compatibility on anvil application and all actual enchanting workflows, including Apothic upgrades. **Configurable exception**: if the exact pinned Apothic/Apotheosis system intentionally allows normally incompatible enchantments, do not block its supported override. Check whether it offers an appropriate config/API; if not, design an explicit TMM compatibility toggle and implement it only after source-level verification. This is **not** a promise that a native Apothic toggle already exists.

The 1.21 `exclusive_set` tag expresses incompatibility globally. If a mod's bypass allows combining incompatible enchantments, both damage effects may apply unless the mod defines another rule. Document/testing must cover that intentional bypass and separately audit damage scaling against multi-tag bosses. **No new mandatory runtime hard-cap on enchantment stacking** while the default mutual exclusion already provides the one-damage-enchantment rule.

### Technical work packets (not started)

1. **Taxonomy handoff:** import reviewed exact registry IDs from PR #97 into shared reloadable entity-category tags; preserve overlap, nonliving exclusions, and 11 unresolved lineages. Keep the Hunter's pilot `tmm_progression:undead`/raider tags distinct until a deliberate migration.
2. **Affinity engine consolidation:** migrate old undead/vampire, fae/forest-dragon and demon/cave-dragon policy semantics into standalone shared progression hooks; add proposed Beast/werewolf policy only behind a real provider; robust owner, boss, retaliation and target-clearing guards.
3. **Data-driven policies:** implement a reloadable table mapping attacker entity tag + target player identity/form/skill or target mob tag to non-aggression; per-rule exemptions; use the CSV as planning input only, not direct runtime JSON. Include the **Sea Dragon ↔ Aquatic** reciprocal pair and keep default Monk/Hunter AI unchanged.
4. **Enchantment registry:** define approved specialist damage enchantments and baseline `exclusive_set/damage` membership; reuse vanilla Smite/Bane where appropriate; verify mod-specific available-damage enchantments do not introduce conflicts.
5. **Discovery gate:** implement player-aware offer filtering and server-side confirmation, with Apothic compatibility investigation before code implementation. Add JSON mapping Hunter skill IDs to enchantment ID/tag and optional rank threshold; book rolls covered.
6. **Acquisition policy:** exclude unintended random, trade, loot and automated sources; maintain valid Hunter-enchanted item transfers/anvils; verify all patched/modded roll paths.
7. **QA:** compile/boot, vanilla and Apothic enchanting-table offers, two-player Hunter vs non-Hunter side-by-side, skill purchase/respec/relog, enchanted books/anvils/trading, no unauthorized offers, default incompatibility and optional bypass, 30-second retaliation, boss/owner protection, multi-category targets, absent former vampire/werewolf mods, server/client parity, registry reload, and modded AI exceptions.

## Acceptance criteria

- [ ] Reviewed runtime IDs/tags represent approved species/factions accurately; overlap and exclusions documented; no passive/owned mob XP leak.
- [ ] Ordinary Hunter without qualifying skill never sees Hunter-only enchants; qualifying Hunter can see/roll the correct one at an enchanting table, including books; both client and server agree.
- [ ] Any player can wield, trade and anvil-apply a Hunter-enchanted weapon with its effect intact; respec does not retroactively disable the item.
- [ ] By default an item can carry **only one** mutually exclusive vanilla/new damage enchantment through normal enchanting and anvils. If enabled and supported, the Apothic override is exercised and validated separately.
- [ ] Undead→JAWR Vampire/Lich, Beast→JAWR Werewolf, Fae↔Forest Dragon, Demon↔Cave Dragon and **Aquatic↔Sea Dragon** affinities stop only unprovoked target acquisition *when their form providers are verified*, while 30-second retaliation, modded boss encounters, summons, and protected entities remain correct.
- [ ] Ordinary **Monks and Hunters keep unmodified native hostility**; Monk Creeper Kiss remains individual-target only. Confirm species/subtype tags do not accidentally pacify all Dragons or all Plants.
- [ ] No retired TeamLapen/Vampirism/Werewolves dependency reintroduced.
- [ ] All results identified as **source-tested**, **dedicated-server tested**, or **player-tested**; no green-build claim of complete gameplay functionality.

## Open question: remaining vampire NPCs after TeamLapen retirement

The test integration roster retains **Just Another Witchery Remake 0.5.39** and its native vampire progression, while removing the separate TeamLapen **Vampirism** module. The exact-pack Hunter census registered **`witchery:vampire`** (`MobCategory.MONSTER`, guidebook `Vampire Children`); this does **not** prove wild or natural spawning. The guide describes *creating a vampire child from a villager*, its hunting and Blood Crucible return. The original Witchery mob was sired and not naturally spawned, but **do not extrapolate that rule blindly to this rewritten 0.5.39 JAR**. A version-pinned spawn registration, structure-spawn audit, or world test must settle whether independent vampire NPCs appear without player actions. Also verify its owner/faction behavior and whether `witchery:vampire` should count as Undead (the census has `witchery:supernatural_prey`, not `minecraft:undead`).

## Evidence / design references

- [Hunter taxonomy, verified census and boundary decisions — PR #97](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/97)
- [Shared progression core, target-change hook and purchased skills — PR #127](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/127)
- [Prototype mob-alliance implementation](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/feature/mob-alliance-neutrality/docs/MOB-ALLIANCES-AUDIT.md)
- [Minecraft Java 1.21 enchantment data and tag design](https://www.minecraft.net/en-us/article/minecraft-java-edition-1-21)
- [Apothic Enchanting project](https://modrinth.com/project/apothic-enchanting)

**Handoff rule:** This document is planning-only. No merges into `test/full-integration` or activation of new features while the next test build is being assembled.