> **Primary design precedence (2026-11-14):** This 270-source placement list is
> **research and Monk-only feature inventory**, NOT a second skill-tree
> implementation or 203 guaranteed unique purchases. The primary Tyz/MXP
> category design is authoritative for one global SP wallet, universal magic
> skills, one-rank prior-tier prerequisite, external gates, and mod adapters.
> See [reconciliation](MONK-INTEGRATION-WITH-PRIMARY-TYZS-TREE.md).

# Monk skill catalog v2 — Tyz + Improvable + Vampirism, no Pufferfish

**Design classification only (2026-11-14).** Machine-readable exhaustive crosswalk:
[`skill-placement-no-pufferfish-v2.json`](../compat/monk/profiles/skill-placement-no-pufferfish-v2.json). Source inventories are preserved in v1; nothing in this change installs Tyz, removes Pufferfish, changes an actual skill tree, or asserts a successful game test.

## Locked target design

- **Pufferfish's Skills is absent from the target design**: no category XP, layout, passive bonuses or dependency on node permissions. The currently distributed B6–B8 pack snapshots may still contain it; that is a *pack migration task*, not a hidden runtime change in this catalog.
- **Tyz's Skills 6.4.2** owns purchases, level/rank storage, SP spending/refunds, and configurable shared character attributes. It also sells *finite or multi-stage Monk-only traits* after a native faction permission unlock. Custom JSON only covers real player attributes; non-attribute resource state and triggered effects need Java adapters.
- **Improvable Skills 3 21.0.2** retains its *existing* universal vanilla-XP-powered perks, independent of Tyz SP. **No new records from the Monk candidate inventory are currently assigned to Improvable.** This is intentional, not exclusion of the installed mod. Avoid making Monk ranks consume vanilla experience. First audit its built-in perks and skill-scroll/upgrade bypass paths.
- **Vampirism's embedded faction/skill framework** owns Monk identity, initiation, unique one-time abilities, meaningful staged unlocks, licenses for Monk-specific Tyz traits, and Monk-only effect suspension/reapplication when faction state changes.
- Do not re-add Monk Creative Flight or the Apotheosis **Unbound** affix. The permitted Draconic Curio is the external flight source, and Momentum Gliding is a post-Ender-Dragon native Monk unlock. Don't count increased `flying_speed` as granting flight.
- Five chained 99-rank Tyz stages with x10 per-stage benefit and price remain a **conditional maximum**, never a default 495-level grant to a bounded stat. Native attribute bounds and real useful effects win. A sixth stage would need a separate justification.

## Audited candidate counts

| Group | Records | Placement |
| --- | ---: | --- |
| Core/mod attribute and ability candidates | **172** | v1's actual model distribution is **95 repeatable, 58 bounded, 17 once, 2 staged**. Earlier conversation summary said 59 bounded / 16 once; v1 source JSON is authoritative. |
| Apotheosis adaptations | **98** | Of 99 upstream affixes, Unbound stays excluded. |
| **Raw candidate/source rows** | **270** | All IDs preserved, exactly once, in v2 JSON. |
| Core rows absorbed into shared upgrades | **47** | Replaced provisionally by **24 canonical shared Tyz skills**. Includes six optional non-attribute aliases that need real semantics/API validation. |
| Apotheosis affixes represented by the same attributes | **43** | Derived from the corresponding canonical ranks. **Never sell the attribute modifier twice** as a second affix purchase. |
| Distinct event/behavior-based Apotheosis techniques | **55** | **40** bounded Tyz + Monk permission/trigger bridge; **15** once-only native Monk faction powers. |
| Provisional canonical endpoints | **204** | 172 − 47 + 24 + 98 − 43. |
| Automatic compatibility rule | **1** | Boss Shield Contact must not become a paid progression gate. |
| **Potential skill purchases or unlocks** | **203** | Provisional planning upper count; not 203 ready-to-ship skills, and not necessarily 203 UI nodes. |

The 24 shared groups derive from [the prior unification proposal](../compat/monk/profiles/skill-unification-proposal.json), **not a proven universal-stat API**. Each includes a list of its original IDs and source attributes for traceability. If two modules use different resource units, the canonical Tyz rank may apply separately calibrated improvements to *each player's own resource*; it must not merge their reservoirs or consume one resource as another.

### Detailed disposition

| Disposition in JSON | Count | Implementation owner |
| --- | ---: | --- |
| `independent_Tyz_ranks` | 96 | Tyz attributes, proposed general availability |
| `shared_alias_no_second_purchase` | 47 | The 24 shared Tyz purchases; source rows not separately sold |
| `native_Monk_faction_unlock` | 17 | Vampirism faction unlock/effect |
| `Monk_licensed_Tyz_ranks` | 7 | Tyz ranks, Vampirism permission and Java effect |
| `conditional_Tyz_mod_resource` | 4 | Tyz purchase + source-verified Java player-resource API |
| `automatic_compatibility_no_purchase` | 1 | Monk server compatibility behavior |
| `derived_attribute_affix_no_second_purchase` | 43 | Derived Monk adaptation of purchased canonical stat |
| `unique_bounded_Tyz_Monk_affix` | 40 | Tyz ranks + Vampirism permission + source-trigger adapter |
| `unique_one_time_Monk_affix` | 15 | Vampirism one-time ability + source-trigger adapter |

## Placement examples / invariants

1. **Strength:** `tmm_shared:attack_power` owns the purchase backing `minecraft:generic.attack_damage`. Monk Unarmed Strike derives from that attribute but must respect actual unarmed conditions. Native damage max **2048** means the illustrated million-damage Strength V cannot be offered through ordinary attribute modifiers.
2. **Magic:** `magic_capacity`, `magic_regeneration`, `spell_power`, `spell_cast_speed`, `spell_cooldown`, and `spell_efficiency` are shared Tyz candidate groups. Ars/Irons/Goety/Aureal/Eidolon/Mahou/Roots adapters cannot cross-fill unrelated pools or double count bridging mods.
3. **Barehand mining:** `tmm_monk:harvest/barehand_speed` is provisionally **an adapter to** `tmm_shared:mining_speed`, not another purchase on the same three attributes. The Monk faction unlock authorizes effective empty-hand mining, with finite *harvest tiers* handled separately. Harvest speed cannot grant invalid drops or violate a tool requirement.
4. **One-time powers:** Catch Arrows, Withering Stare, Creeper Kiss, Water Walking, Glass Touch, Step Assist, and Momentum Gliding remain faction abilities; Breath Meditation and Barehand Harvest Tier are finite staged faction progression. A source rarity affix that merely modifies Step Height must not be charged again.
5. **Affix attributes:** Adamantine / Ironforged armor, Murderous / Violent attack damage, and other shared-attribute affixes represent **different source milestones or presentation of one modifier**, not extra bought copies. The 43 source-affix records remain traceable to the purchased canonical ID; their rarity thresholds must be calibrated.
6. **Event affixes:** Spectral, Ensnaring, Telepathic, Psychic, Venomous, Catalyzing, and other unarmed/unarmored adaptations use native faction or Tyz purchases *only after* a valid server-trigger implementation exists. Tool, shield and weapon procs do not magically work without one.
7. **Boss Shield Contact:** if the Monk has a legitimate ranged/ki technique, it should interact with Guardian shields/crystals under ordinary rules, without a paid special perk.
8. **External flight control:** `flying_speed` is an optional speed modifier only while external Curio flight exists and actually consumes that attribute. It is **not** Monk Creative Flight.
9. **Experience:** Tyz SP, Improvable's vanilla XP, and Vampirism/Monk faction advancement stay separate until a deliberate economy design connects them. Avoid accidental multiplied reward payouts.
10. **Refund/faction changes:** Tyz stage N+1 requires N = 99 **on the server**; disallow invalid earlier-stage refunds. Retain universal ranks across faction changes; suspend Monk-only effects when Monk eligibility is lost. Public administrative level setters require reconciliation rather than trusting purchase events alone.

## Blockers before claiming working integration

**Hard technical blockers for implementing the whole catalog:**

- **Uninstalled Tyz backend:** packwiz pin, NeoForge 1.21.1 client and dedicated-server boots, genuine JSON skill registration and loaded built-in skill audit are still required.
- **API/semantics:** ten original non-attribute resource candidates were checked against upstream 1.21.1 sources and official Mahou documentation: **four source-backed extension paths, two Mahou candidates lacking verified Java hooks, two candidates needing redesign, and two blocked as proposed**. See the [dated audit](MONK-MOD-API-VERIFICATION-2026-11-14.md). The pinned JAR ABI and game runtime are still untested. Six sources were provisionally coalesced into shared upgrades; undo merges where mechanics differ. No fabricated Tyz player attribute for non-attribute mana.
- **Event bridges:** 55 independent Apotheosis techniques need source-faithful server behavior and faction eligibility. 43 attribute-affix rarity steps need mapping to existing paid rank values. **Elastic [armor] is resolved:** its only effect is bonus Step Height; one native Step Assist grants one-block traversal and Elastic gets no separate Monk purchase. See the [API and Elastic audit](MONK-MOD-API-VERIFICATION-2026-11-14.md).
- **Purchase safety and progression:** Tyz pre-purchase/pre-refund for singles and bulk, its administrative direct-level setter bypass, 99/99 chains, bound checks, 32-bit SP price arithmetic, client sync and effect suspension must be proved in a disposable test instance.
- **Improvable overlap:** inspect actual built-in perks versus Tyz defaults and the auto-consuming scroll path. Improvable upgrades remain vanilla-XP purchases and must not bypass Monk faction-only restrictions.
- **No-Pufferfish pack migration:** remove its skills and possibly attributes *only after* dependency inspection; update former shared one-time nodes, skill quests, configs, and reward references. Not part of this catalog-only change.
- **GUI and faction scale:** the Monk faction scaffold defaults to 20 faction levels. We must not dump 203 nodes into a tree designed for 20 levels. Group numeric purchases in Tyz's UI and let the faction tree show only licenses, milestones, and singular powers.
- **Integration smoke tests:** player attribute inventory, native caps, attack/projectile adapters, empty-hand harvesting, login/death/dimension transitions, faction swaps, damage registration and late-game boss behavior.

## Proposed build sequence

1. Validate the two installed/general skill engines and remove Pufferfish dependencies on a **separate** test branch; retain the last known-good Prism pack baseline.
2. Implement the 24 shared Tyz definitions selectively, starting with a safe attribute that has verified headroom. Do not generate five stages per source row or ship the inactive Strength prototypes as gameplay.
3. Verify Monk faction license and one complete bounded Tyz skill; test purchases, refunds, respec and source attribute caps.
4. Implement the original Monk one-time powers and harvest mechanics as server behavior, with explicit legal-attack adapters.
5. Implement the 55 event-affix adaptations in batches, and use source-affix rarity milestones as views or thresholds into the existing canonical attribute owner; retain the one-Step-Assist decision without creating a separate Elastic rank.
6. Run integrated quest/UI, Pufferfish-removal, multiplayer and dedicated-server/client regression tests before enabling any skills.

**Source of truth:** [v2 machine placement](../compat/monk/profiles/skill-placement-no-pufferfish-v2.json) contains an explicit disposition, owner, access rule, original rank type, canonical IDs, integration method, and flags **for every one of the 270 source records**. v1 snapshots remain intact for reproducibility.
