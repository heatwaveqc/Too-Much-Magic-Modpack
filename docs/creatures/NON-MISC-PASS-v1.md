# Non-MISC taxonomy pass v1 — owner review

Source: immutable v0 reconciliation 7a9b2b19c7f6c91f1e1c88a36001ad2825e10b03. The v1 CSV is a review overlay, not a runtime tag file. It covers all 899 non-MISC registrations and all 130 vanilla IDs (including 51 vanilla MISC IDs), for 950 unique records.

- Vanilla: all 82 mob identities accounted for; 48 other entities (including player and armor stand), explicitly distinguished. 78 mobs have proposed existing Hunter-family membership. Villager, wandering trader, and witch are sapient humanoids with no existing specialist proposed; Shulker is a shell-bearing End creature with specialty unresolved (avoid dimension-only Void classification). Iron/snow golems and villager are actual MISC mobs. Allay Fae, strider Beast, tadpole Aquatic, turtle Aquatic are proposals, not owner-approved changes.
- Non-MISC: 420 explicit first-pass rows, 315 carried historical/source membership rows, 164 identity-review rows. Of the 481 originally unmatched non-MISC registrations, 341 modded and all 22 vanilla received explicit proposals; the 164 review queue also contains older candidates with unresolved membership. Explicit proposals can include an intentional empty family or an open family decision; counts are not verified creature/eligible-prey totals.
- Largest remaining identity-review groups: Goety 35, Occultism 20, Eternal Starlight 17, Twilight Forest 16, Ars Elemental 12, Iron's Spells 9, Warden Tools 9. Preserve unknowns rather than classify by dimension or generic monster spawn category. Prior Hunter memberships and reward policies remain separate verbatim columns. Unapproved historical family values remain review-only. Not all carried proposals were verified in this pass.
- Alex's Caves prior prospective work reused where identities match; Confection/Mutant remain descriptors, not automatically approved specialties. Source guides are evidence leads, not exact-port binary verification. The v1 modded proposals are based on identifiable registered names, known species/lineage and prior research; exact source/class evidence, natural/owned status and multi-family exceptions are outstanding.
- No player-summon reward changes, blood capacities, spirit/essence tables, AI or consumer tags generated. Modded MISC remains separate. V1 is not an approval to activate gameplay.

A: exact IDs checked against v0, duplicate rule IDs rejected, full vanilla coverage asserted, CSV round trip and committed readback (950 data rows). Earlier validation caught an invalid Goety name and an omitted small fireball before any write; both corrected. B/C: no tests run, no build or deployment authorized. Next: resolve identity queue, confirm named proposals against exact source and reconcile current resolved tags before consumer policy proposals.

## Owner rulings — 2026-11-18 (supersede earlier open decisions)

| Entity | Hunter family | Non-Hunter category |
| --- | --- | --- |
| Witch | Illager | — |
| Shulker | Void | — |
| Villager | — | Villager |
| Wandering Trader | — | Villager |
| Iron Golem | Construct | Villager |

Illager includes witches on raid-faction grounds; this does not assert native biological Illager status. Shulker is not a vanilla arthropod (https://minecraft.wiki/w/Arthropod and historical captured tags). Villager is a distinct non-Hunter category in the separate CSV column non_hunter_categories. Iron Golem retains its existing hunt exclusion. The five changed rows have owner-approved memberships only, no reward/ownership/mechanics changes. Other village-associated mobs remain unchanged pending review. All 82 vanilla mobs now have at least one proposed or approved category; 80 have Hunter-family membership, two are Villager-only. Other proposals remain review-only.

## Owner rulings — Humanoid consolidation and Iron's Spells 'n Spellbooks

Supersedes the Villager category in the previous ruling. Humanoid is a non-Hunter category, not a specialty tree. The Villager taxonomy category is retired; no replacement with Humanoid for Iron Golem, which remains Construct. Village faction/AI allegiance remains an independent relationship, and native village tags and mechanics are untouched. Villager and Wandering Trader move to Humanoid. Priest, Apothecarist, Cryomancer, Pyromancer and Cultist join Humanoid. Spectral Steed is Undead (owner correction), Summoned Vex is Illager, and Debug Wizard is excluded. Echo of Tyros has owner-endorsed provisional Undead membership; exact lore/lineage audit is still pending. The nine previously unresolved Iron's entities now have a category or explicit exclusion, with one provisional lore case. Prior Iron's membership proposals remain unchanged. Existing reward policies, summon/ownership safeguards and technical exclusions remain independent. Humanoid is not inferred for every biped or every existing creature in this pass; broader coverage is deferred to the next review. No deployed tags or mechanics changed.

Validation: exactly 12 intended records revised, 950 unique data rows retained, no remaining Villager entries in the non-Hunter category column. The former decision is kept above for provenance only.

## Dive Into the Abyss (wardentools) — source-supported review proposals

Installed `wardentools-1.1.4_neoforge_1.21.1.jar` (survey #348; SHA-256 ffe5eb5da73b480e22361834b14109e8f380a91eeff56a33d6334ee0090b16a1). Not the unrelated Warden Tools mod. All nine non-MISC registrations are now proposed. Developer's [mob wiki](https://github.com/LuciferC-137/MC-Mod_ABYSS/wiki/Mobs) and upstream [NeoForge 1.21.1 source](https://github.com/LuciferC-137/MC-Mod_ABYSS/tree/neoforge_1.21.1), whose gradle.properties reports 1.1.4, are evidence. Source read used moving branch, observed recursive tree d2bfa6cfb7aa47d9121311934580e0e799413b79; not a byte-for-byte match to installed JAR. Per-row links and reasoning retained.

| Creature | Proposed family | Rationale |
| --- | --- | --- |
| Deep Lurker | Beast | Passive forest animal |
| Pale Wanderer | Beast | Passive forest animal |
| Noctilure | Beast | Tameable flying bird |
| Crystal Golem | Construct | Mechanical temple guardian |
| Protector | Construct | AbstractGolem; heart-invoked radiant guardian |
| Temper | Fae | Battle Allay |
| Shadow | Undead | Corruption-death mimic; soul-spawner alternative |
| Parasyte | Aberration | CorruptionMonster boss add; no proof of arthropod anatomy |
| Contagion Incarnation | Aberration | Ritual-created corruption boss; wind lore 14–15 |

Sculk, corruption and Void are related in lore but not assumed identical. Shadow has Warden-like vibration behavior; Protector can originate from purified Warden Heart; Incarnation lore mentions Void worship and creation from corruption. Optional secondary memberships require review; no dimension-only shortcut. Proposed summon/owned/pet/technical exclusions, repeated boss-add and corrosion-death-loop safeguards are **review notes only**, not reward or drop rules. No blanket assumption about blood capacities, harvesting or boss neutrality. Boss identity is separate from species.

Five remaining MISC IDs reviewed as non-creature candidates without adding rows to the current non-MISC overlay: abyss_lightning and crystal_laser (effects), mod_boat and mod_chest_boat (vehicles), contagion_incarnation_corpse (post-defeat display/loot entity; upstream class extends LivingEntity, but should not receive a second kill or harvest credit; exact runtime behavior to verify). These remain in v0 and will be handled in MISC pass. All 14 namespace IDs accounted for conceptually, 9 classified in v1.

Validation A: nine exact registry IDs, 950 unique overlay rows and CSV round trip; committed readback separately checked. Source and documentation audit only; owner review and runtime tests pending.

## Owner-approved capture policy and Abyss lock (2026-11-18)

All nine Abyss taxonomy proposals above approved, without speculative secondary Sculk membership. Canonical capture rule: preserve the effective native Apotheosis Capturing blacklist and extend it to every boss and entity type whose intended acquisition is a specific ritual or exclusive special-summon/encounter mechanism. Apply the **same denied entity set** to Capturing, Reliquary charms and spawn eggs (including economy eligibility); exact pinned consumer adapters/registry IDs and native constraints must be reconciled with the economy and integration writers. Do not invent a native tag, independently maintain divergent lists, remove native exclusions, price forbidden variants, or disable ordinary mobs simply because a normal type can also be summoned. Existing natural varieties require independently reviewed provenance policy, not a blanket type denial. Technical/debug/unavailable entities are not capture targets. Document optional additional progression safeguards for review. Capture eligibility does not determine Hunter XP, normal loot, summoning rituals, natural spawn or spirit harvesting. No runtime blacklist activated in this research PR.

Two new columns in v1: capture_policy_target / capture_policy_reason. Values: blacklist_target (required by approved rule or explicit existing exclusion, exact source reconciliation pending); proposed_blacklist (additional safeguard needing owner review); review_hold (uncertain; do not generate allow override); native_policy_pending (no confirmed added reason, not approval to bypass existing native blacklist); unreviewed. Abyss targets: Contagion Incarnation, Protector, Parasyte, Temper. Shadow review hold; other four preserve native policy pending. Retro target entries for vanilla dragon/wither/Warden and six Iron's boss/summon/debug IDs; not a claim those namespaces are fully audited.

## Twilight Forest — 59 non-MISC registry IDs

Installed twilightforest-1.21.1-4.8.3345-universal.jar, SHA-256 493da16d10210f9f53a3c33b3d8fcb4ae9b14016ee420d753c894562bd9aba35. Source review: upstream 1.21.x registration and boss tag (moving branch, not installed binary equivalence). https://github.com/TeamTwilight/twilightforest/blob/1.21.x/src/generated/resources/data/twilightforest/tags/entity_type/bosses.json. Exactly nine native boss entries: Naga, Lich, Minoshroom, Hydra, Knight Phantom, Ur-Ghast, Alpha Yeti, Snow Queen, Plateau Boss placeholder. All BLACKLIST targets, including placeholder. Separate desired final-boss addon #323 not installed in survey; require its actual registry ID and capture denial at intake. No new ID fabricated.

Sixteen previously unresolved identities proposed/excluded: Adherent, Armored Giant, Giant Miner, Kobold and Troll = non-specialty Humanoid; Block and Chain Goblin, Lower/Upper Goblin Knight, Redcap and Redcap Sapper = Fae + Humanoid (folkloric proposals); Carminite Ghastguard, Carminite Ghastling, Ur-Ghast = Undead under Ghast precedent (first two extend Ghast lineage); Snow Guardian = provisional Elemental (extends BaseIceMob, floating armor); Snow Queen = provisional Elemental + Humanoid; Plateau Boss = unfinished technical boss placeholder/exclude. Additional proposals Hydra Beast+Dragon; Minotaur and Minoshroom Beast+Humanoid. Other earlier proposals preserved. No new Hunter family; no change to reward mechanics.

Capture: 12 targets (9 native bosses, Lich Minion, Loyal Zombie, Roving Cube [noSummon/noLootTable]); six review holds (Ice Crystal temporary Queen summon, Giant Miner, Armored Giant, Adherent, Harbinger Cube, Rising Zombie); one proposed additional blacklist (Questing Ram, unique quest reward); remaining ordinary creatures preserve native policy pending. Ghastling has a normal spawn predicate plus summoned variant; no type denial merely for boss summoning. Exact spawn/loot routes and native Capturing tag expansion pending. All 21 MISC registrations remain in v0 for later technical review. Source registration https://github.com/TeamTwilight/twilightforest/blob/1.21.x/src/main/java/twilightforest/init/TFEntities.java and relevant classes support proposals; owner review needed.

A: 59 TF rows, 16 formerly unresolved, 9 Abyss locked, 9 retro capture target annotations; 950 unique rows and CSV parse/round-trip/readback. No deployable tags, price or build changes. B/C not run. Handoff to economy owner: one canonical capture denial list and exact consumer tests after source validation, not competing independent lists.
