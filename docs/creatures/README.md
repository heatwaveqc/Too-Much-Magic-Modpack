# Canonical creature taxonomy — shared integration registry

**Current working registry:** [registry-v1.json](registry-v1.json), see [REGISTRY.md](REGISTRY.md) for schema, evidence and projection contract. The reconciliation v0 material below is historical input, not a competing editable registry.

Issue #324. Data and research only. No gameplay changes, generated runtime tags, mod pins, economy settings, or build dispatch. Successor working inventory of the existing Hunter research, not a replacement for historical sources or the Progression engine.

## Frozen inputs
- Installed registry: PR #348, `fa869c43a685241d4d9b48e7d2b89d019c321856`, `docs/qa/surveys-2026-11-18/entities.json` (installed integration `f7aeb56a0f7b9c5957f922f5a397037f25207c76`).
- Hunter crosswalk, prior runtime census and source tag declarations: PR #347, `39c3631fa2cf6d86c82a16eb9c17cc40f6b3ea3b`, `docs/hunter/` and `compat/progression/src/main/resources/data/tmm_progression/tags/entity_type/`. This is a source snapshot of advancing Progression, not evidence those tags are installed. Direct membership declarations were transcribed from the 18 inspected tag files; nested tag references not evaluated.

## Exact-ID join
| Metric | Count |
| --- | ---: |
| total | 1763 |
| oldRegistry | 1348 |
| added | 415 |
| crosswalk | 424 |
| directMembership | 42 |
| directPolicy | 3 |
| neither | 1336 |
| miscUnresolved | 855 |
| nonMiscUnresolved | 481 |
| conflict | 0 |
| categoryChange | 0 |
| Prior registry IDs absent from current registry | 45 |
| Previously matched Hunter IDs absent from current registry | 0 |
| Explicit source tag IDs absent from current registry | 0 |

`reconciliation-v0.csv` has exactly one row per current registry ID, including projectiles and technical entities. Old names/memberships/reward policies remain proposals, preserved as distinct values, not collapsed into approved assignments. Direct source memberships and direct reward exemptions are separate. Multiple candidate classifications or reward policies are flagged even where compatible. Current native category is registration metadata, not proof of biology, hostility, spawn provenance, boss status, or eligibility. No fresh instance/class verification; historical tags remain historical. Nested tag references are not flattened without a current resolved tag census.

Nested source references:
```json
{
  "void": [
    "#goety:enderlings"
  ],
  "demon": [
    "#witchery:demonic_entity"
  ],
  "hunt_excluded": [
    "#c:player_summons"
  ]
}
```

Largest unresolved non-MISC namespaces: goety (116), twilightforest (59), alexscaves (44), eternal_starlight (33), aquaculture (32), endermanoverhaul (23), minecraft (22), ars_elemental (20), creeperoverhaul (16), crittersandcompanions (16), theaurorian (14), ars_nouveau (10), golemoverhaul (9), wardentools (9), betterend (6), dragonsurvival (6), feywild (6), extrabotany (5), pastel (5), the_bumblezone (5).
MISC requires source/instance evidence; don't automatically exclude living MISC or assume non-MISC is eligible prey.

Absent prior IDs (retain frozen evidence, not active output):
```
vampirism:advanced_hunter
vampirism:advanced_hunter_imob
vampirism:advanced_vampire
vampirism:advanced_vampire_imob
vampirism:blinding_bat
vampirism:converted_camel
vampirism:converted_cow
vampirism:converted_creature
vampirism:converted_creature_imob
vampirism:converted_donkey
vampirism:converted_fox
vampirism:converted_goat
vampirism:converted_horse
vampirism:converted_mule
vampirism:converted_sheep
vampirism:crossbow_arrow
vampirism:dark_blood_projectile
vampirism:dummy_creature
vampirism:dummy_sit_entity
vampirism:ghost
vampirism:hunter
vampirism:hunter_imob
vampirism:hunter_minion
vampirism:hunter_trainer
vampirism:hunter_trainer_dummy
vampirism:particle_cloud
vampirism:remains_defender
vampirism:soul_orb
vampirism:task_master_hunter
vampirism:task_master_vampire
vampirism:throwable_item
vampirism:vampire
vampirism:vampire_baron
vampirism:vampire_imob
vampirism:vampire_minion
vampirism:villager_angry
vampirism:villager_converted
vampirism:vulnerable_remains_dummy
werewolves:alpha_werewolf
werewolves:human_werewolf
werewolves:task_master_werewolf
werewolves:werewolf_beast
werewolves:werewolf_minion
werewolves:werewolf_survivalist
werewolves:wolf
```

Absent previously matched Hunter candidate IDs: none.
Absent explicit Progression tag IDs: none.

## Policy boundaries and next pass
- Preserve locked Hunter families; species/affinities separate from reward, ownership, summon provenance, boss exceptions. Do not invent families or infer XP eligibility from registry categories.
- Living status, blood capacity, boss/miniboss flags, Malum native spirit drops and Primal native affinities are independent evidence fields. Audit exact source and installed binary. No implied blood from living status or affinity from namespace; preserve native special cases.
- Reliquary/ProjectE use separately owned approved capture blacklist policy. Theurgy needs separate item/drop-source ledger retaining ordinary material vs gear vs magical-harvest distinction and boss gates. No economy changes.
- Next: resolve historical candidate conflicts and additions, verify ordinary/technical creature identity with exact-pin sources, obtain current resolved tags/living-class evidence through existing QA survey. Propose consumer mappings for owner review. Generator later as reviewed offline tooling; progression adapters via shared engine owner. No runtime tag generation or deployment until approved.
- Reconcile next assembled candidate before activation; integration writer takes reviewed exact paths. Neither shared branch edited.

## Validation
A: parsed all three frozen datasets, expected counts (1763 current, 1393 old, 477 candidate), checked unique current IDs, field lengths, output CSV parse and count invariants. No game execution. Quick CI pending. B/C deferred to consumer-specific acceptance after approved implementation; no commands or fresh build authorized.

Owned paths: `docs/creatures/README.md`, `docs/creatures/reconciliation-v0.csv`. No gameplay files.
