# Archlich Ascendancy — an additional 100 points INSIDE the shared Lich tree

**Too Much Magic · Owner correction 2026-11-22 · DESIGN ONLY / NOT LIVE**

## Binding owner decision

**Regular Lich mastery costs 100 individually purchased one-point ranks. A real Archlich may spend ANOTHER 100 individually purchased one-point ranks in a locked second section of the EXACT SAME Lich tree.** The regular 21 named skill nodes, their effects/costs, and native Lich eligibility remain unchanged. One legal Archlich can ultimately own **41 paid nodes and 200 spent points** (100 ordinary +100 ascendant). A legal Goety-only Lich, JAWR-only Lich, Dracolich with one legal Lichdom, Piglich or other single-provider hybrid can only buy the first 100. No second skill tree, duplicate universal Core, new Path wallet or points for merely transforming.

**Archlich eligibility:** Read BOTH authoritative server-native form observations for THE SAME PLAYER: genuine Goety Lichdom **AND** genuine JAWR/Forbidden Magic Lichdom. Both must be affirmatively TRUE and legal under the current transformation compatibility policy. UNKNOWN, absent, stale or contradictory provider observations fail closed. No amount of native soul energy, player name, undead tag, historical advancement, two filled anchors or Path-level points substitutes for actually possessing both Lichdoms. If one source is later cured/lost, immediately deactivate ALL Archlich purchased powers; purchases remain owned only according to standard dormant/rank-refund policy. Original single-provider Lich ranks remain active while any legal Lichdom persists.

**Full ordinary mastery is NOT a silently invented third gate.** The owner only specified both Lichdoms for the advanced tier. This draft connects each of four ascendant branches to one existing ordinary Lich root purchase (one normal rank) so the new section visually lives in the same tree. It does not require purchasing the entire 100-point ordinary tier before ascendant ranks. Should the owner separately prefer 'complete the first 100 before entering Archlich', that can be a later explicit decision.

**All 20 Archlich skill names/effects/percentages below are REVIEW PROPOSALS, not owner-approved.** The user approved only the 100+100 budget, same-tree position, and verified dual-form gate. In particular, lifting native summon caps, adding chain damage and redirecting damage require source-specific APIs and careful explicit owner sign-off. Never install an unverified inert paid skill.

## Native boundaries (unchanged even for the most powerful Archlich)

- Goety's personal Soul Energy, the Goety Arca, JAWR phylactery, and JAWR Soul in a Bottle remain four distinct native concepts. No synthetic shared 'arch-soul' balance or conversion, duplication, passive death-charge generation or cross-payment for the other source.
- The source-authoritative death event permits **one** successful prevention/return per fatal damage. Valid Archlich JAWR phylactery rescue is attempted FIRST; only on failure does the Goety Arca fallback resolve. Neither gets double-debited, and a paid Archlich skill does not invent a new rescue. Normal post-rescue improvements trigger only from the final single confirmed return receipt.
- Dracolich's native Dragon Survival anti-death takes precedence over its one legal Lich backend. Dracolich is not suddenly permitted to combine both Lichdoms as an Archlich or buy Archlich ranks merely because it possesses Dragon and Lich.
- Gilded Piglich / Arch-Piglich and other legal combos retain **one** Lich tree; source truth determines whether they have one or two genuine Lichdoms. A compatible form label is not automatic dual ownership.
- No extra hotbar buttons. Every proposed effect is a passive attribute, a condition on real native spells/summons, a verified combat event, or one passive after-resurrection response. The engine must avoid visible synthetic potion status effects for progression powers, except real native harmful effects delivered by supported spells.
- Hard caps and ownership apply to hostile targets, summon count, chunk/entity workload, passive multiplication of source magic, and claim/PvP protections. Boss phases are NEVER bypassed merely because the Path is Archlich. Native vulnerabilities (including Smite, fire/sunlight where applicable) remain authoritative.

## Complete purchase budgets

| Same Lich tree section | Nodes | One-point ranks | Gating |
|---|---:|---:|---|
| Ordinary Lich mastery (existing) | 21 | **100** | Any compatible verified Goety OR JAWR/Forbidden Lich |
| Ascendant: Dual Soul Convergence | 5 | 25 | BOTH native lichdoms + connected ordinary root |
| Ascendant: Undead Dominion | 5 | 25 | BOTH native lichdoms + connected ordinary root |
| Ascendant: Black Apotheosis | 5 | 25 | BOTH native lichdoms + connected ordinary root |
| Ascendant: Deathless Majesty | 5 | 25 | BOTH native lichdoms + connected ordinary root |
| **Archlich full mastery** | **41** | **200** | Genuine dual Lichdom, same tree |

Each rank costs exactly **one** advancement point from the appropriately configured shared engine economy (#475). Archlich conversion is a **gate**, not a grant of 100 points or a forced refund. Same persistent purchases cannot be bought in two Lich trees.

## Additional 20 proposed Archlich skills

### Dual Soul Convergence — 25 additional one-point ranks

| Name and stable ID | Ranks | Proposed effect | Requires | Implementation and owner hold |
|---|---:|---|---|---|
| **Twin Soul Synthesis** (`tyr_progression:archlich_twin_soul_synthesis`) | 6 | +4% personally paid, source-confirmed Goety necromantic spell potency per rank (up to +24%) while both real Goety and JAWR Lichdoms are active; not a free Soul Energy cast and never a second Soul Energy pool. | `tyr_progression:lich_soul_conservation` + **both native Lichdoms** | Exact native Goety personal paid necromancy receipt, dual-form snapshot and legitimate damage/effect order. |
| **Dual Anchor Bulwark** (`tyr_progression:archlich_dual_anchor_bulwark`) | 5 | -2% ordinary eligible direct physical damage per rank (up to -10%) ONLY when both genuine native death anchors are independently valid and charged; neither anchor is spent, cloned, recharged or invoked by this bonus. | `tyr_progression:archlich_twin_soul_synthesis` + **both native Lichdoms** | Read-only source-true Arca and soul-filled phylactery validity without changing rescue priority, costs or source-native weaknesses. |
| **Soul Tithe** (`tyr_progression:archlich_soul_tithe`) | 5 | Once per 25 seconds, an actual hostile killed directly by an authorized personally owned native undead may restore +0.75% of the player's real personal Goety Soul Energy capacity per purchased rank (up to +3.75%); no reanimated minions, fake kills or phantom soul income. | `tyr_progression:archlich_twin_soul_synthesis` + **both native Lichdoms** | Real owner/kill attribution, personal cap, actual source recovery hook; exclude farm/dummy, Arca storage, death anchor. |
| **Phylactery Confluence** (`tyr_progression:archlich_phylactery_confluence`) | 5 | When a genuine paid Goety necromancy cast and a legitimate JAWR Soul-in-a-Bottle heal have both completed within 20 seconds, gain +3% personally cast necromancy output per rank (up to +15%) for the next 10 seconds; consume this qualifying pair once, 30-second cooldown. | `tyr_progression:archlich_dual_anchor_bulwark` + **both native Lichdoms** | JAWR authentic soul-bottle receipt and paid Goety spell transaction; neither is fabricated, refunded or substituted with phylactery death charge. |
| **Crown of Two Souls** (`tyr_progression:archlich_crown_of_two_souls`) | 4 | While both Lichdoms and both legitimate charged native anchors are present, +3% personal necromantic spell damage and +2% authorized owned-undead damage per rank (up to +12%/+8%); revert immediately when any native condition becomes false. | `tyr_progression:archlich_phylactery_confluence` + **both native Lichdoms** | Native snapshots on relog/cure/charge, valid paid damage sources, summon ownership and bounded stacking. |

### Undead Dominion — 25 additional one-point ranks

| Name and stable ID | Ranks | Proposed effect | Requires | Implementation and owner hold |
|---|---:|---|---|---|
| **Grave Legion** (`tyr_progression:archlich_grave_legion`) | 6 | +5% direct attack damage per rank (up to +30%) from genuinely owned Goety/JAWR native undead minions while the Archlich is valid; never wild mobs, other players' pets or all undead everywhere. | `tyr_progression:lich_bone_captain` + **both native Lichdoms** | Source-verified summoned minion ownership, legal damage origin and capped multipliers. |
| **Ossuary Host** (`tyr_progression:archlich_ossuary_host`) | 5 | +5% max health per rank (up to +25%) for real personally owned summoned undead while legally active; one modifier per actual minion, clean removal on logout/owner loss, no free healing when reapplied. | `tyr_progression:archlich_grave_legion` + **both native Lichdoms** | Server native minion lifecycle and owner UUID, real max-HP restoration rules. |
| **Throne of Bones** (`tyr_progression:archlich_throne_of_bones`) | 5 | PROPOSED major Archlich-only gate: +1 additional *legally active* personally summoned undead slot per rank (up to +5) ONLY for native providers that expose a safe dynamic cap; every added summon still requires its real source spell/material/Soul Energy cost, and no autonomous spawn is granted. | `tyr_progression:archlich_ossuary_host` + **both native Lichdoms** | OWNER/SOURCE HOLD: exact Goety summon cap/JAWR support, world/AI performance, claim safety and potential entity caps; disabled if cannot raise source cap safely. |
| **Bonewall Intercession** (`tyr_progression:archlich_bonewall_intercession`) | 5 | Once per two seconds, when legitimate physical damage would land on Archlich, redirect 3% of that damage per rank (up to 15%) onto one nearby, physically present, genuinely owned undead within six blocks; real minion takes actual damage. No minion means no reduction; no death/true damage bypass. | `tyr_progression:archlich_throne_of_bones` + **both native Lichdoms** | Generic trusted damage redirection and recursion guard, summon range, minion death, owner/PvP permission; no damage duplication. |
| **King of the Host** (`tyr_progression:archlich_king_of_the_host`) | 4 | After a real *paid, successful* personally owned native undead summon, all other already-owned qualifying undead within 16 blocks gain +4% melee movement and +4% damage per rank (max +16% each) for eight seconds; refresh at most once per 20 seconds, never spawn extra servants. | `tyr_progression:archlich_bonewall_intercession` + **both native Lichdoms** | Actual source summon success/payment and owner-bound minion effects; no fake summons, chunk-load, AI control bypass. |

### Black Apotheosis — 25 additional one-point ranks

| Name and stable ID | Ranks | Proposed effect | Requires | Implementation and owner hold |
|---|---:|---|---|---|
| **Soulstorm** (`tyr_progression:archlich_soulstorm`) | 6 | On one genuine personally paid Goety necromancy hit against a hostile, once every ten seconds, a second eligible hostile within four blocks takes one additional nonrecursive source-tagged death-magic hit of +2 real damage per rank (up to 12 HP); payment is an additional verified Soul Energy debit, no damage without paid source action. | `tyr_progression:lich_sanguine_attunement` + **both native Lichdoms** | OWNER/SOURCE HOLD: valid second damage target, cost debit and no recursive spells/kill loop, protection, bosses and PvP. |
| **Plague Crown** (`tyr_progression:archlich_plague_crown`) | 5 | +0.5 seconds per rank (up to +2.5s) to genuine personal necromancy-spell harmful status duration on legal hostile victims, clamped to provider-supported max; never spread unrelated poison, permanent curses or buffs. | `tyr_progression:archlich_soulstorm` + **both native Lichdoms** | Verified source spell effect and caster, duration stacking ceilings and harmless cancellation. |
| **Crypt Surge** (`tyr_progression:archlich_crypt_surge`) | 5 | With at least three actually alive, personally owned summoned undead within 12 blocks, +3% personal necromantic spell potency per rank (up to +15%); no synthetic minions or free command/follow range. | `tyr_progression:archlich_plague_crown` + **both native Lichdoms** | Owner minion proximity, source spell attribution, server-safe bounds, no passive farm credit. |
| **Grave Cascade** (`tyr_progression:archlich_grave_cascade`) | 5 | Once every 30 seconds following a genuine *personally cast and paid necromancy spell kill*, send one nonrecursive death-wave to at most one additional eligible nearby hostile per rank (max five within six blocks), dealing up to two actual death-magic damage per rank (max ten HP) to each; no repeated waves from wave kills. | `tyr_progression:archlich_crypt_surge` + **both native Lichdoms** | OWNER/SOURCE HOLD: event dedup, spread damage and one-wave-only chain guard, target caps, protections, loot/XP exploit safety. |
| **Eclipse of Undeath** (`tyr_progression:archlich_eclipse_of_undeath`) | 4 | After an actual successful paid personal necromancy cast, the NEXT accepted personally paid necromancy cast within eight seconds has +6% actual necromantic effect potency per rank (up to +24%) but its verified normal resource cost is 25% higher; consume the charge once, 25-second cooldown. | `tyr_progression:archlich_grave_cascade` + **both native Lichdoms** | Dual paid source cast receipts, nonrefundable increased cost, source rounding and per-player charge/cooldown; no free spell. |

### Deathless Majesty — 25 additional one-point ranks

| Name and stable ID | Ranks | Proposed effect | Requires | Implementation and owner hold |
|---|---:|---|---|---|
| **Deathless Tyrant** (`tyr_progression:archlich_deathless_tyrant`) | 6 | +3 maximum HP per rank (up to +18 HP) while genuinely both Lichdoms; remove/adjust correctly on cure, loss, respec, death or relog, with no bonus health restoration exploit. | `tyr_progression:lich_cold_persistence` + **both native Lichdoms** | Server-native dual-lich eligibility and reversible MAX_HEALTH attribute, no immutable mutation. |
| **Shroud of Ages** (`tyr_progression:archlich_shroud_of_ages`) | 5 | -3% direct ordinary physical damage taken per rank (up to -15%) while dual lich; does not mitigate Smite-specific weakness, fire/sun vulnerability, void, kill-plane or unrelated true damage, and cannot confer generic immortality. | `tyr_progression:archlich_deathless_tyrant` + **both native Lichdoms** | Source native damage types and mitigation stacking with ordinary Lich Ossuary Skin. |
| **Twinned Requiem** (`tyr_progression:archlich_twinned_requiem`) | 5 | Only AFTER one confirmed native rescue event completes (phylactery first or Arca fallback), restore +2 actual HP per rank (up to +10 HP), capped at current max. This modifies post-rescue recovery ONLY: it never recharges an anchor, decides the rescue, or grants a second save. | `tyr_progression:archlich_shroud_of_ages` + **both native Lichdoms** | Exactly one native post-rescue receipt from #540 provenance, provider priority and total bonus after regular Final Passage. |
| **Grave Return** (`tyr_progression:archlich_grave_return`) | 5 | For ten seconds after a real native death rescue, +5% personal grounded movement speed and +3% actual necromantic spell damage per rank (max +25%/+15%); effects occur after the one real rescue and never stop the next legitimate lethal hit. | `tyr_progression:archlich_twinned_requiem` + **both native Lichdoms** | Actual post-rescue callback, one trigger per death, no fake invulnerability, no potion-effect UI. |
| **Sovereign Eclipse** (`tyr_progression:archlich_sovereign_eclipse`) | 4 | For fifteen seconds after that one valid native resurrection, up to ten currently existing personally owned summoned undead within 12 blocks gain +8% actual melee damage per rank (up to +32%); no new summons, no extra Totem, Arca use, paid resurrection or automatic chunk-load. | `tyr_progression:archlich_grave_return` + **both native Lichdoms** | Native sole-rescue receipt, genuine owned undead count, explicit minion cap and duration cleanup. |

## Why these four branches belong above, not inside, ordinary Lich

**Dual Soul Convergence:** only a true Archlich has both provider states, real Goety spellcasting, and a JAWR phylactery/Soul-in-a-Bottle route. Advanced talents respond to the genuine coexistence of two independent Lichdoms without combining death anchors or creating a new shared Soul resource. High-level Cross-Provider Resonance must consume actual qualifying native action receipts, not a made-up soul charge.

**Undead Dominion:** the base Lich can field a competent source-native undead host. The proposed advanced version can command a much larger and stronger army, transfer small amounts of legitimate harm to living minions, and coordinate minion responses. Throne of Bones (+one authorized active servant slot per rank) is intentionally bold and **explicitly contingent on source capabilities and owner approval**. No source support means disabled/not sold, rather than free summoned duplicates.

**Black Apotheosis:** real native necromancy may cascade to tightly bounded additional legal hostile targets, perform stronger spell combinations, and leverage a legitimate existing minion host. Extra hits and waves must never recursively launch more waves, bypass protection, create unearned drops, fire without spending actual Goety Soul Energy, or passively execute a magic system by owning a skill.

**Deathless Majesty:** the Archlich has high-end health and ordinary damage resilience, plus dramatic but conditional power *after* an actual authorized death rescue. All these aftereffects happen **after** the one phylactery→Arca decision, not before and not as a substitute for source death prevention. The Archlich is difficult to kill, not invulnerable; the rescue anchors may genuinely be unavailable and bosses must still be fought.

## One authoritative tree and entitlement flow

1. Shared Progression sees true native Lich flag Goety OR JAWR and admits an eligible legal player to the same Lich tree. Ordinary 21 skills are available subject to their own prerequisites and native event providers.
2. On **every** Archlich purchase, refund, login, load and effect evaluation, the shared native snapshot verifies **Goety TRUE AND JAWR TRUE**, same player, legal combination. A cosmetic Archlich label or former transformation history is not enough. The graph may visually show a locked second region, but the server—not the UI—authorizes.
3. Four Archlich group entry nodes attach to ordinary `lich_soul_conservation`, `lich_bone_captain`, `lich_sanguine_attunement` and `lich_cold_persistence` respectively. The primary gate remains dual Lichdom, not a hidden requirement to max all ordinary 100 ranks. Further Archlich purchases require their single listed predecessor (normal one-point-rank prerequisite semantics).
4. Archlich becoming single-provider later immediately disables the 20 Archlich buffs, source chained attacks, max-HP bonuses and minion improvements, including their active timed remnants. Don't double refund; determine whether owner expects automatic refund on irreversible cure or manual respec. The 100 ordinary slots remain where a legitimate native Lich is still present.
5. If the second native provider returns via legal progression, previously bought Archlich ranks can become effective again under persistence policy; no free new points or duplicate skill ranks are created.
6. Universal Core remains independently purchasable and owned. Newbie earns no MXP. In unified mode normal+Archlich skills buy from the existing MXP/points economy; hybrid/separate modes obey the configured Path-mode contract, never a new separate Archlich currency.

## Engine/source implementation crosswalk

| Component | Source authority and necessary reusable handler |
|---|---|
| Dual-form gate | `NativePhysiologyObservations`/reconciled Goety 3.2.3 source checks from PR #540, JAWR/Forbidden Magic 0.5.39/0.5.37 native flags, shared snapshot fail-closed and source-legal transition policy |
| Unified graph | Same Lich Path skill graph, 20 additional paid nodes, one-point costs, one default wallet, ordered graph prerequisites, per-node dual-provider entitlement, server-side invalidation |
| Two real resources and anchors | Goety personal paid Soul Energy cast/recovery, JAWR healed Soul in Bottle, Arca validity and phylactery validity, no anchor debits outside actual source-owned rescues |
| Summon army | Real Goety/JAWR native summon API, authentic owner UUID, actual paid summon and permitted max cap, no cross-owner minion controls, claim/AI/entity budget & despawn |
| Damage redirects and waves | Trusted bounded effect chain and source damage tags, exact one additional target/group, nonrecursive damage, no double kill/XP/loot credit, no PvP/claim bypass |
| Native resurrection receipt | Phylactery FIRST then Arca fallback (PR #540) as one authoritative death controller; post-rescue effects only, no second revival, no duplicated resurrection event listeners |
| Passive state | All status/attribute buffs conditionally applied and revoked based on BOTH native flags, actual anchors, owned-minion presence and confirmed source actions, without visible synthetic potion effects |

Any Java, mixin, API, AI/cap or chained-effect code belongs to separate shared `feature/progression-*` and `compat/progression`. This is a **design-only** file plus design-ledger/summary changes. The previous [Lich #526](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/526) documented only 100 *ordinary* ranks; its old claim that 100 was the entire possible Archlich tree is now superseded by this owner decision. The other 21 regular IDs and their numerical candidates are intentionally unchanged.

## Validation/acceptance requirements — NOT YET GAME TESTED

1. Original 21 regular purchased IDs, all prerequisites, effects and 100 unit rank costs **byte-equivalent JSON objects** across before and after change.
2. Exactly 20 additional unique `tyr_progression:archlich_*` paid IDs and exactly 100 new one-point ranks, 25 in each ascendant lane; 41/200 full eligible Archlich and no >100 single-provider purchases.
3. Source Lich combinations: Goety-only 100, JAWR/Forbidden-only 100, permitted Piglich 100, Dracolich 100, both-provider legal Archlich 200; Arch-Piglich if genuine both 200; invalid/UNKNOWN snapshots fail-closed.
4. Both acquisition orders; losing/restoring either lich source; relog/cure/respawn/respec and player IDs. No access to Arch ranks on merely fake form tag, old advancement or filled death anchor.
5. Complete existing ordinary Lich does NOT automatically grant Archlich nodes or 100 additional points; Archlich-only player who acquired both source lichdoms can still invest in an ordinary node and unlocked ascendant branch without unwritten 100/100 maxing requirement.
6. One final native lethal rescue (JAWR phylactery first, Goety Arca fallback), never both. Dracolich's Dragon Survival rescue first. All proposed Twinned Requiem, Grave Return and Sovereign Eclipse effects only respond AFTER one genuine rescue; native failure means no fake buff.
7. Goety spell cast and JAWR Soul-in-Bottle actions are real, paid, separately tagged transactions; no phantom resource, Soul-to-blood conversion, personal energy from invalid kills or free phylactery refills.
8. Actual summon ownership/limits: no unlimited minion slots, chunk-loading, pathfinding overload, duplicates, item/farm mobs or player servant hijacking; Throne of Bones is disabled if source cap cannot be safely raised.
9. Source Necromancy actual paid hit/cast, Soulstorm and Grave Cascade bounded target number/area, no recursion, actual pay, no boss bypass, no double-drop/XP from callback duplication, PvP/claim protection.
10. Damage redirection and high-end defense preserve true damage, void, Smite and actual native weaknesses; may be fatal when minion absent or anchor is depleted. No extra life-saver, totem or independent immortality effect.
11. All skill currencies, Core, Hunter/existing 100-rank tree budgets remain untouched. Additional 20 Archlich options bring combined visible catalog from 261 to **281**, without creating a new separate top-level Path category.
12. Confirm source/owner review before activating novel 100-tier effects and any new generic handler. Static design validation is not module CI, in-world test, authorized comprehensive QA or release inclusion.

## Owner review checklist

**Owner-locked now:** regular 100 + Archlich additional 100, both genuinely native lichdoms as mandatory gate, one same tree, ordinary skills remain accessible to Dracolich and other legal single-provider Liches, Archlich uniquely receives the most extreme powers.

**Still proposed:** four ascendant lanes and all 20 named skills, exact numeric magnitude and stage prerequisites, whether a 100-regular-maxing gate should ALSO be required, increased summon caps, personal Soul-Energy returns, area death cascades, damage splitting, dual-cast resonances, and death-recovery buffs. Do not infer tacit owner permission to implement any of these numbers or override native guardrails.

Source: [Lich original draft #526](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/526); [current native Goety/Archlich source PR #540](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/540); [unified XP modes #475](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/475).
