# Lich — complete 100-point regular tier in one 200-point Archlich-eligible tree

**Too Much Magic · 2026-11-22 · DESIGN ONLY / NOT LIVE / NUMERICAL BALANCE CANDIDATES**

Current authoritative continuation: `REMAINING-PATHS-100PT-FIRST-PASS.json` schema **v14** on `feature/archlich-ascendancy-100pt-extension-20261122` (stacked on Hunter #554, following original Lich #526). **The 21 nodes in this document remain the unchanged 100-point REGULAR tier** of the single Lich tree. The new **Archlich-only extra 100-point tier** is documented in [ARCHLICH-ASCENDANCY-ADDITIONAL-100PT-2026-11-22.md](ARCHLICH-ASCENDANCY-ADDITIONAL-100PT-2026-11-22.md). The original #526 100-point maximum claim for Archlich is now superseded by the owner's 2026-11-22 clarification. Design only, no runtime files, engine code or game tests.

## Role and native boundaries

The Lich is a **personally funded necromancer** with a durable undead host, optional blood-magic specialization with a real nature-magic tradeoff, moderate deathless combat mitigation, and mastery support after a legitimate resurrection. Lich relies on resource discipline and followers rather than Werewolf's bodily regeneration or Vampire's feeding. All abilities are passive, event-triggered or modifiers to actual source abilities; there are **zero new active hotbar buttons**.

- **ONE shared purchased Lich tree with TWO gated tiers.** These 21 regular nodes / 100 points are available to any legal Goety OR JAWR/Forbidden Magic Lich, including Dracolich's one Lichdom alongside its Dragon tree and legal Piglich hybrids. The **same tree** has 20 new Archlich-only nodes / 100 ADDITIONAL points, unlocked **only with BOTH genuine native Goety and JAWR/Forbidden Magic Lichdoms** on the same player. A complete Archlich may thus spend 200 points, without duplicating either native form or buying the original 100 twice. Dracolich cannot acquire the second native Lichdom under the current compatibility policy. Gilded→Piglich→Arch-Piglich may invest in both tiers only after genuinely acquiring both providers.
- **Goety Soul Energy**, Goety **Arca** death anchor, JAWR/Forbidden Magic **soul-filled phylactery**, and legitimate **Soul in a Bottle** remain distinct native objects/values. There is no unified soul wallet, new soul bar, synthetic resurrection, independent shadow cost or second native progression.
- Source owns its natural lichdom rite, level/ritual gates, summons, resource debits, soul capture, cures, death/return rules, and actual damage/environment vulnerabilities. Source-native paid levels or innate abilities are not repurchased.
- **One rescue per fatal event**. Archlich death priority: a valid JAWR phylactery rescue FIRST, Goety Arca only as fallback; never debit both. For Dracolich, Dragon Survival's own anti-death result resolves before the selected Lich backend. Skills may see a confirmed successful rescue receipt afterward but never decide or repeat it.
- There is no general second set of Core ranks for Lich. MXP is global and Path-agnostic after Newbie. A later cure/respec rule must preserve Core and avoid double refunds or free cross-form rank persistence; exact Lich cure ownership should be tested against current Progression policy.

## Owner-approved 100+100 correction, same tree

**Do not treat this file's 100-point regular Lich design as the entire Archlich tree.** The owner has now locked 100 regular + **100 MORE Archlich-only** ranks; Archlich's section is in the SAME tree and requires both confirmed source Lichdoms, while Dracolich and other legal single-provider hybrids have full access to all 100 REGULAR ranks. The extra tier's **20 proposed** high-end skill effects are documented separately at [Archlich Ascendancy](ARCHLICH-ASCENDANCY-ADDITIONAL-100PT-2026-11-22.md) and require owner/source review; the added-point count and native gate are not proposals.

## Exact rank budget

| Branch | Paid skills | One-point ranks |
|---|---:|---:|
| Soul Economy | 5 | 25 |
| Sanguine Necromancy | 4 | 20 |
| Undead Host | 5 | 25 |
| Deathless Form | 4 | 20 |
| Soul Preservation | 3 | 10 |
| **Total** | **21** | **100** |

Every rank costs exactly **1 advancement point**. The existing **21 regular purchased nodes / 100 one-point ranks** are the maximum for a legal **single-provider** Lich, not the complete Archlich ceiling. The gated extension contributes **20 additional nodes / 100 ranks**, so a genuine dual-provider Archlich may master **41 skills / 200 purchased ranks IN THIS ONE TREE**. Native conversion grants eligibility, never free points. The old 12-skill proportional placeholder is superseded; effects still require source review.

## All proposed skills and effects

A **source/provider hold is real**: if a feature is not supported for an eligible native type, do not advertise or charge for an inert rank. Either implement a strictly sourced alternative or revise the tree with the owner. Numeric deltas are candidate test targets only.

### Soul Economy — 25 points

| Skill | Ranks | Candidate full effect | Stage | Hard blocker / test |
|---|---:|---|---|---|
| **Soul Conservation** (`tyr_progression:lich_soul_conservation`) | 6 | -3% per rank (maximum -18%) of eligible personally paid Goety Soul Energy necromancy cast costs; JAWR uses only an actually verified paid native necromantic cost, never phylactery, ritual or resurrection expense. | entry | Native cost receipt; JAWR equivalent is a source-audit HOLD; no fake resource pool |
| **Soul Reserve** (`tyr_progression:lich_soul_reserve`) | 5 | Goety: +4% per rank (maximum +20%) to verified personal Soul Energy storage, explicitly excluding Arca. JAWR: +4% per rank (maximum +20%) to duration of genuine personally cast native necromantic self-buffs, if such a source effect exists. | entry | Provider-specific non-equivalent benefits; no fabricated JAWR mana/soul bank |
| **Soul Recovery** (`tyr_progression:lich_soul_recovery`) | 5 | Goety: +4% per rank of legitimate credited native Soul Energy recovery; JAWR: +4% per rank of verified actual Soul-in-a-Bottle healing. Only amplify an existing successful provider transaction. | advanced | No synthetic soul creation, no phylactery recharge, no resource-to-resource conversion |
| **Soul Treasury** (`tyr_progression:lich_soul_treasury`) | 5 | +3% per rank (maximum +15%) to eligible personally cast necromancy buff duration after a real, accepted source cast and paid costs; never machinery, rituals, passive auras or resurrections. | advanced | Require real spell ID, caster and payment; do not buff a refunded/cancelled spell |
| **Death's Dividend** (`tyr_progression:lich_deaths_dividend`) | 4 | One real hostile killed directly by an eligible personal necromancy spell may grant Goety 0.5% maximum native Soul Energy per rank or JAWR 0.5 HP per rank; 20-second player cooldown, no player/owned minion/farm-credit triggers. | mastery | Provider-credited direct kills only; not an independent soul or XP source; disabled if unverified |

### Sanguine Necromancy — 20 points

| Skill | Ranks | Candidate full effect | Stage | Hard blocker / test |
|---|---:|---|---|---|
| **Sanguine Attunement** (`tyr_progression:lich_sanguine_attunement`) | 6 | +3% per rank (maximum +18%) personally cast verified blood-magic damage; in exchange -2% per rank (maximum -12%) personally cast Roots/Botania nature-magic potency. No other player's spells or machine effects. | entry | Dual-sided optional specialization; real school mapping and exact personal caster receipts |
| **Crimson Channel** (`tyr_progression:lich_crimson_channel`) | 5 | +3% per rank (maximum +15%) personal necromantic spell potency, and -2% per rank (maximum -10%) personal nature-magic healing. Does not multiply all generic magic or ally casts. | advanced | Personal Goety/JAWR necromancy attributions must be source-verified |
| **Verdant Severance** (`tyr_progression:lich_verdant_severance`) | 4 | +4% per rank (maximum +16%) personal blood/necromantic spell damage while imposing -4% per rank (maximum -16%) personal nature spell potency; cumulative self-only tradeoff, clamped against zero. | mastery | Opt-in severe blood-vs-nature specialization, not an innate species-wide nature ban |
| **Grave Sorcery** (`tyr_progression:lich_grave_sorcery`) | 5 | +3% per rank (maximum +15%) to qualifying harmful necromantic status durations that this player personally inflicts with genuine accepted spells; excludes instant damage and friendly effects. | advanced | Needs a specific eligible effect allowlist, caster attribution and duration cap |

### Undead Host — 25 points

| Skill | Ranks | Candidate full effect | Stage | Hard blocker / test |
|---|---:|---|---|---|
| **Bone Captain** (`tyr_progression:lich_bone_captain`) | 6 | +3% per rank (maximum +18%) damage from personally owned, verified summoned undead servants; never naturally spawned hostile undead or another player's units. | entry | Native ownership attribution and owner-scoped damage only |
| **Grave Regiment** (`tyr_progression:lich_grave_regiment`) | 5 | +4% per rank (maximum +20%) to supported native follow/leash range for already-authorized personally owned undead; never raises summon cap, chunk-loads or overrides native command controls. | advanced | Requires per-provider summoned-minion AI/owner API; unsupported entities receive no effect |
| **Death Host** (`tyr_progression:lich_death_host`) | 5 | After a verified personal necromantic cast with real cost, owned eligible undead gain +2% damage per rank (maximum +10%) for eight seconds; refresh at most once per 15 seconds. | mastery | Passive cast-triggered buff, no extra button, no unlimited aura/no costless loop |
| **Grave Intercession** (`tyr_progression:lich_grave_intercession`) | 5 | When an actual hostile attack damages the Lich, one nearby eligible owned undead may legitimately retaliate with +2% attack speed per rank for five seconds; 15-second cooldown, no PvP/claims bypass. | advanced | Needs source-legal AI retaliation; does not tame, summon or mind-control mobs |
| **Grave Vigil** (`tyr_progression:lich_grave_vigil`) | 4 | +4% per rank (maximum +16%) max health for player-owned eligible undead summons; apply once on native creation/update, remove cleanly on owner loss; no mob cap increase. | entry | No additive duplicate on reload, portal transfer or re-summon |

### Deathless Form — 20 points

| Skill | Ranks | Candidate full effect | Stage | Hard blocker / test |
|---|---:|---|---|---|
| **Cold Persistence** (`tyr_progression:lich_cold_persistence`) | 6 | -3% per rank (maximum -18%) duration of verified harmful necrotic/wither-like status effects not already negated by native Lich immunity. Preserve Smite, daylight, fire and other actual source weaknesses. | entry | Condition allowlist audit required; do not duplicate source-native full immunity |
| **Ossuary Skin** (`tyr_progression:lich_ossuary_skin`) | 5 | 1.5% per rank (maximum 7.5%) reduction to ordinary incoming physical hits in an authenticated Lich state, after eligible mitigation; exclude true damage, void, native Smite weakness and fatal-event decisions. | entry | Small bounded direct defense, not armor or a generic magic-immunity layer |
| **Grave Resilience** (`tyr_progression:lich_grave_resilience`) | 5 | After a direct, credited hostile kill by the player or an eligible owned undead, heal this Lich 0.5 HP per rank (maximum 2.5 HP) at most once every 12 seconds. | advanced | No friendly/player/self-kill farming; resource-free health recovery capped and attributed |
| **Unquiet Stride** (`tyr_progression:lich_unquiet_stride`) | 4 | +2% per rank (maximum +8%) grounded movement speed in natural darkness or below open sky; no speed in flight, no new flight permission, no night-vision duplication. | entry | Need server-side darkness/grounded predicates; environmental audit |

### Soul Preservation — 10 points

| Skill | Ranks | Candidate full effect | Stage | Hard blocker / test |
|---|---:|---|---|---|
| **Anchor Attunement** (`tyr_progression:lich_anchor_attunement`) | 4 | +2% knockback resistance per rank (maximum +8%) only while native provider confirms a charged, currently valid Lich anchor (JAWR phylactery or Goety Arca); never creates, charges or substitutes an anchor. | advanced | Provider-backed read-only anchor readiness, no death-handler modification |
| **Final Passage** (`tyr_progression:lich_final_passage`) | 3 | Only after the native unified death controller actually records one successful Lich rescue, restore +2 HP per rank (maximum +6 HP), capped at max health, once for that event. | mastery | No second resurrection and no extra life; native rescue must have already succeeded |
| **Second Witness** (`tyr_progression:lich_second_witness`) | 3 | Only after the same single native rescue receipt, grant +3 seconds per rank (maximum nine seconds) of Resistance I to cover the dangerous return; respect actual native spawn/world rules. | mastery | No second Totem visual, invulnerability or repeat trigger |

## Gate, purchase and source crosswalk

**Entry:** a read-only authoritative native-probe confirms exactly an eligible Goety and/or JAWR/Forbidden Magic lichdom; neither player-declared Path labels nor an item alone masquerades as native form. Never charge for native initiation. **Advanced:** separately verify source-native progression evidence (Goety ritual/resource state, JAWR actual lich rank/milestone); no fabricated numerical Goety level. **Mastery:** verified native late stage plus 1 rank in the preceding branch skill where applicable. Paid dependencies are design gates, not source power unlocks. The previous baseline ladder is deliberately not retained as dogma.

| Area | Reusable engine candidate | Missing/verifiable boundary |
|---|---|---|
| Conditional defense, movement, combat heals | Generic attribute, damage reduction, heal-on-kill, environment/condition engine | Authenticated Lich state, exclude Smite/source weaknesses, owner-attributed kill + cooldown |
| Goety/JAWR resource economy | Existing Progression native-cost / resource adapters where actually present | Strict personal debit/recovery receipts, separate Arca/Phylactery, Soul in a Bottle healing transaction; never charge rescue |
| Blood vs nature personal casting | Source-scoped spell-stat and magic cost hooks | Verify exact Neo Vitae/EvilCraft/Botania/Roots/Goety/JAWR IDs and personal casting; source-tag effects, not machine/ritual |
| Undead summons and AI | Existing owner-scoped entity stats where actually present | Goety/JAWR entity ownership IDs, true spawned owner, lifecycle cleanup, AI follow/retaliation without friendly-fire overrides or added summons |
| Lich resurrection after-effects | Unifying Progression-owned death receipt/event bridge | Exactly one successful native rescue from authoritative controller, provider priority and no repeated last-stand/cure events |

All missing generic handlers belong to `feature/progression-*` and the shared `compat/progression` engine; **this `feature/lich-*` branch is configuration/design only**. Do not add a separate Lich Java runtime or copy the retired Tyz tree.

## Tests required before any executable purchase

1. **Source probe/gates:** JAWR-only and Goety-only have ordinary 100. A real Archlich (either acquisition order) gains access to extra Archlich 100 in same tree after BOTH positive native Lichdoms; Gilded Piglich and Dracolich keep their legal provider limits; mundane/unknown fail closed.
2. **Rank/accounting:** this regular section retains exactly 21 unique skill IDs and 100 one-point ranks unchanged. The SAME tree holds another 20 Archlich-gated skills /100 one-point ranks; all 41 IDs are unique and only genuinely dual-source Archlich may spend 200. Server-side dual-form snapshot and prerequisites must fail closed.
3. **Resource identity:** source-verified Soul Energy spends/recovery and JAWR bottle healing only; Arca and phylactery expenses unchanged and never affected by spell-cost discounts; canceled casts no effect.
4. **Purchased utility is real:** test each benefit under both JAWR-only and Goety-only conditions; mark unsupported distinct backend choices as blocked rather than exposing zero-effect UI.
5. **Blood/nature tradeoff:** source-identified personal casts only; verify penalties and bonuses stack arithmetically with caps, do not affect others, passive machines, ritual infrastructure or unrelated magic.
6. **Summon ownership:** test multiple casters, logout/rejoin, summoned undead vs wild mobs, minion death, portal/claim changes, no ownerless buffs or recursive entity creation, no summon-cap creep.
7. **Damage/defense:** normal melee/projectile, Smite, sunlight/fire, fall/void, harmful effects; keep native liabilities, caps and correct mitigation order.
8. **Single rescue:** Archlich phylactery then Arca fallback, never both; Dracolich Dragon Survival anti-death before one selected Lich; single receipt triggers recovery once; deliberate true death still possible.
9. **Persistence/respec:** exactly one invested Lich tree per player, 100 regular +100 conditional Archlich max. Losing one provider makes Archlich powers immediately dormant, without double refund or Core loss; ordinary Lich ranks remain while one legal provider exists. No resurrection perk retained after full native cure unless explicitly approved.
10. **Authorized game tests:** only after integration writer reconciles versioned binary APIs, content/handlers and separately authorized test build; CI validation is not gameplay acceptance.

## Historical IDs preserved

Original named skills retained, repriced/rebalanced rather than mechanically copied: Soul Conservation, Soul Reserve, Soul Treasury, Sanguine Attunement, Crimson Channel, Verdant Severance, Bone Captain, Grave Regiment, Death Host, Cold Persistence, Anchor Attunement, Final Passage. Nine additional skills supply distinct mechanics without artificially inflating old rank counts. Original archived `tmm_lich_*` sources are provenance, not today’s executable contracts.

## Open owner/source decisions

**Most consequential:** confirm the intended severity of the blood/nature price and whether each provider has sufficient verified resource transactions for Soul Conservation/Reserve/Recovery; otherwise those skill effects must be revised before sale. Verify exact stage gates and whether Lich-specific curing returns all points, analogous to Vampire/Werewolf, in the shared engine. Stage/percent figures in this draft are **review proposals**, never an undocumented change to core physics or native resurrection.
