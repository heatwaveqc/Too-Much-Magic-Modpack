# Capability profile — <manifest-id>

**Status:** unreviewed / partial / source-backed / verified-in-pack  
**Pack selection:** unknown / pinned-active / approved-unpinned / deferred / excluded  
**Identity:** manifest ID `<manifest-id>`; modid `<modid>`; installed artifact `<filename>` and SHA/version `<hash/version>`  
**Source evidence:** exact immutable URL/tag/hash or versioned docs; jar inspection caveat if applicable  
**Existing deep comprehension:** `../../mod-comprehension/mods/<manifest-id>.md` (if available)  
**Other installed addons:** <IDs/none/unknown>

## Axis coverage

`unknown` = not yet established, **not absent**. `no_capability_confirmed` must cite a sufficiently complete axis-scoped search. `present` must have a record below. `not_applicable` needs rationale.

| Axis ID | Status | Capability record(s) / negative evidence |
| --- | --- | --- |
| `gear` | unknown | — |
| `movement` | unknown | — |
| `ore_processing` | unknown | — |
| `storage` | unknown | — |
| `crafting` | unknown | — |
| `logistics` | unknown | — |
| `buffs` | unknown | — |
| `external_compatibility` | unknown | — |
| `mob_farming` | unknown | — |
| `ore_generation` | unknown | — |
| `permanent_power` | unknown | — |
| `general_generation` | unknown | — |
| `direct_damage` | unknown | — |
| `block_breaking` | unknown | — |
| `permanent_pets` | unknown | — |
| `temporary_pets` | unknown | — |
| `defense` | unknown | — |
| `healing_recovery` | unknown | — |
| `debuff_control` | unknown | — |
| `energy` | unknown | — |
| `automation_control` | unknown | — |
| `agriculture` | unknown | — |
| `detection_information` | unknown | — |
| `equipment_augmentation` | unknown | — |
| `world_manipulation` | unknown | — |
| `world_content` | unknown | — |
| `death_respawn` | unknown | — |
| `trade_economy` | unknown | — |

## Capability records

Create one subsection or table row for **each independent action/effect**, not one per device. Multi-tag true multi-effect functionality; separate an item's stats from a spell it merely carries. Use stable identifiers `<manifest-id>::<slug>`.

| ID | Name | Primary axis (other axes) | Delivery | Trigger and typed input → output | Gate / cost / duration / throughput | Presence + evidence | In-pack test |
| --- | --- | --- | --- | --- | --- | --- | --- |
| <manifest-id>::<slug> | <specific capability> | <axis> (<optional axes>) | item / spell / ritual / block / entity / player-state / world rule | <actual invocation; item/fluid/power/entity consumed → effect / product> | <requirements, cooldown, capacity, uptime, persistence, owner rules> | native_upstream / present_in_pack_unverified / verified_in_pack / pack_customization_proposed / unsupported_unknown; URL+SHA | not run / positive+negative evidence |

## External bridges (only where evidenced)

| Bridge ID | Source capability/typed output | Actual bridge (native/installed addon/pack script/proposed) | Recipient typed input | State, versioned source evidence, boundary test |
| --- | --- | --- | --- | --- |
| — | — | — | — | — |

## Confidence, gaps and reconciliation

- **Identity/pins:** document manifest and exact binary provenance. Manifest selection alone does not prove installed/active.
- **Evidence ledger:** `[pin]`, `[jar: SHA]`, `[source: SHA]`, `[docs: version]`, `[pack-test: build/world]`, `[inference]` with direct URLs beside claims.
- **Unresolved axes:** name concrete missing inspections; never infer absence from silence.
- **Positive/boundary tests:** for consequential gates and interactions, explain a reproducible positive test and a failure/negative test.
- **Reconciling with existing mod comprehension:** link the original deep walkthrough and any interaction reports; do not copy/overwrite them.
- **Research-only:** no gameplay implementation authorization, and no server/client/pass claims without evidence.
