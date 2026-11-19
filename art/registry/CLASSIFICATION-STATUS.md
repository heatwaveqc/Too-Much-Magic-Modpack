# Texture categorization — v0.2 first pass

All **42,542** inventoried base texture keys now have a category-schema record: primary category (or explicit unresolved state), family ID, deliverables, technical flags, scope, evidence and review state. Category assignment is not visual approval or runtime verification.

| Evidence level | Paths |
|---|---:|
| Direct model/atlas/font/exact renderer evidence | 34,137 |
| Dynamic renderer-pattern proposal; confirmation required | 1,810 |
| Native category unresolved | 6,595 |
| Total | 42,542 |

**35,947 have a primary category proposal; 8,405 still need stronger renderer/category evidence** when pattern-only assignments are included. Family IDs derived from material-face suffixes are explicitly provisional; singleton family IDs do not invent a relationship between unrelated images.

| Primary category | All paths | Eligible known paths |
|---|---:|---:|
| armor_and_wearables | 770 | 770 |
| complex_blocks | 7,286 | 7,284 |
| connected_blocks | 600 | 0 |
| directional_blocks | 2,597 | 2,534 |
| environmental_textures | 10 | 10 |
| fluids | 65 | 65 |
| gui_elements | 5,082 | 5,082 |
| layered_items | 993 | 993 |
| micro_icons_and_fonts | 562 | 562 |
| mobs | 1,111 | 1,111 |
| particles_and_vfx | 2,286 | 2,286 |
| projectiles_and_rendered_objects | 765 | 765 |
| pseudoblocks | 1,557 | 1,557 |
| shaped_blocks | 18 | 18 |
| simple_blocks | 2,417 | 2,417 |
| simple_items | 9,828 | 9,826 |
| unresolved | 6,595 | 6,555 |

## Scope and evidence

- Exact source provenance excludes **25 Crystalix** and **682 Rechiseled** texture keys, 707 total, from original production. Those records load as `excluded-approved`; shared vanilla/included-mod resources stay eligible. There are **41,835 eligible known base texture paths**, not a final whole-runtime coverage denominator.
- Read `classification-index.json` and its hash-checked `classification/` JSON shards. `catalog_registry.load_registry` joins classifications and approved exclusions to the immutable inventory by exact key. It rejects changed source signatures, corrupt shards, missing/duplicate keys and unresolved evidence IDs.
- Evidence inspection covered **62,639 model contracts**, **15,194 blockstates**, **39 font definitions**, **2,183 texture metadata documents** and **99,814 class constant pools**, plus the original atlas/particle contracts. No class parser errors. Official Mojang mappings were provider-hash verified to interpret vanilla class inheritance; see `renderer-source-lock.json`.
- Model texture variables and inherited geometry are resolved before classification. Sprite layers, tint indices, animation metadata and native connectivity declarations are composable flags. Renderer classifications use inherited native types plus actual texture literals; dynamic path recipes are marked provisional. Folder names alone never establish a primary category.
- Reused stone/wood surfaces keep one material-family asset and all detected usage roles; stairs/slabs using a master do not create duplicate repaint work. Block inventory model previews and coupled wearable icon/UV deliverables are explicitly required.
- Vanilla ore records name their required host master and product palette, with mixed/all-ore proofs required. Mana Diamond/Pearl links name their base master; the 16 native Botania runes share a substrate-template requirement. These are dependency plans, not approved or generated derivatives.
- **278 model texture resource references** are absent from the frozen base PNG catalog. They are stored separately, with model evidence, for generated/overlay/missing-resource review; they were not fabricated into inventory PNG rows.

## Remaining review queue

The unresolved records distinguish missing native usage edges from unresolved model parents/custom loaders. Dynamic pattern records remain a separate review state. Next work should resolve those native contexts and unused/generated/overlay applicability, not fill missing categories with path-name guesses. The original archive/overlay/first-party/future-mod closure gaps in [INVENTORY-STATUS.md](INVENTORY-STATUS.md) remain.

Frozen gameplay source remains `1413154722a92cdeefa88e2dc40c12c09f8baf80`. No roster upgrade, mod removal, gameplay or build change. The current pilot has 17 review candidates (10 revised, 6 carried, 1 new paired icon), zero category approvals and zero game tests.
