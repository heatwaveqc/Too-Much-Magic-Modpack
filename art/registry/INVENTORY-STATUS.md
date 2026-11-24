# Static inventory baseline — 2026-11-16
Historical archive-scan baseline. The current [classification layer and review state](CLASSIFICATION-STATUS.md) supersede this document's pilot counts: 17 review files, zero approved/active pack PNGs, and 707 exact art exclusions. Archive/source path facts below are preserved; final runtime closure remains incomplete.


Frozen integration: `1413154722a92cdeefa88e2dc40c12c09f8baf80`.
Every indexed packwiz metadata file was checked against the pinned index hash.
All **199 indexed mod archives plus the vanilla 1.21.1 client** were downloaded
from the frozen official provider URLs and verified against their lock hashes.
The additional SHA-256 digest identifies every inspected archive.

| Scope | Count | State |
|---|---:|---|
| Provider-verified top-level archives | 200 | All scanned |
| Nested JARs | 87 | Recursively scanned with independent SHA-256 and parent identity |
| First-party source-resource projection | 1 | Pinned Git resource files, not a compiled JAR |
| Distinct base texture paths | 42,542 | Exact paths, dimensions, hashes and metadata hashes |
| Namespaces containing those paths | 152 | See namespace-counts.csv |
| Overlay texture records | 184 | Separate activation/layout review required |
| Other PNG and non-PNG texture resources | 790 | Applicability review required |
| JSON renderer/atlas/animation/font/particle contracts | 66,107 | Evidence collected; not all renderers resolved |
| Shared texture keys | 47 | 44 identical-byte collisions, 3 differing-byte collisions |
| Archives with no direct PNG | 84 | Not assumed asset-free; nested/generated resources remain relevant |
| Selected pilot PNGs | 16 | Drafted; zero owner-approved or game-tested |
| Desired future entries without an active exact pin | 12 | Planned scope, not silently omitted |

This establishes **complete path enumeration for the inspected frozen archives**,
including nested JARs. It does **not** establish a final required-coverage
denominator or whole-runtime closure. Do not divide 16 by 42,542 and label that
the pack's final coverage percentage: overlay applicability, unused working files,
generated assets, future mods and exception decisions still affect that total.

## Exact evidence

- `source-lock.json`: immutable provider URLs, lock digests, side and metadata hashes.
- `archive-audit.json`: successful acquisition and SHA-256/byte counts for all 200.
- `inventory.json`: catalog index, archive/nested identity, unresolved issues and
  the 16 draft decisions. Catalog shards contain compressed JSON facts only;
  **no upstream image, JAR or cache bytes**. Each shard is SHA-256 checked.
- `namespace-counts.csv` and `source-counts.csv`: exact readable scope tables.
- `collisions.json`: every duplicate path with all source digests, no guessed
  load-order choice.
- The source-resource projection enumerates the first-party asset paths pinned
  in Git. Its archive is ephemeral; it is not a built runtime artifact.

## Remaining closure work

1. Confirm actual loaded precedence for the three differing assets:
   `apotheosis:textures/items/gems/lightning.png` (Apotheosis / Iron's),
   `minecraft:textures/entity/shulker/shulker.png` (vanilla / BetterEnd), and
   `projecte:textures/curios/empty_klein_star.png` (ProjectE / Project Expansion).
   Identical duplicates remain recorded too, including bundled libraries.
2. Ars Nouveau's official JAR contains duplicate ZIP members. Exact member offsets
   and digests are recorded in its source entry; do not assume a unique archive
   directory implies unique members.
3. Resolve 184 overlay records, 670 PNGs outside canonical texture locations and
   120 non-PNG texture working files/resources. No automatic exclusions approved.
4. Check loader-injected resources, generated textures and hardcoded/custom
   renderers with the exact approved client. The 66,107 collected JSON contracts
   are evidence, not a claim that source-code renderer inspection is complete.
5. Reconcile built first-party JAR contents with the pinned source projection when
   an authorized runtime artifact exists. No code compilation or assembly was
   needed or run for this art task.
6. The 12 unpinned future entries are Aether, Comforts, Deep Aether, Lava Fishing,
   Liroth Revamped, Magical Satchel, My Nether's Delight, Nether Depths Upgrade,
   the unresolved roguelike provider, the unresolved Starlight/Stalwart candidate,
   YUNG's Better End Island and Prefab. Their existing decisions remain in the
   roster. Art work does not select or install new versions for gameplay.

## Reproduce

Run acquisition against `source-lock.json` into a scratch cache outside Git using
`acquire_sources.py`. Run `collect_inventory.py --lock ... --cache ... --out ...`
from the repository root; it rechecks provider digests and reads first-party files
only at the frozen Git SHA. Run `catalog_registry.py --scan ... --registry
art/registry` to reconcile facts while preserving decisions with unchanged source
contracts. The collector needs that exact integration Git object locally.
`validate.py` reads and checks all catalog shards. No tool launches Minecraft,
assembles the gameplay pack or makes a ZIP.
