# Weeping Angels — dimension exclusion audit and integration handoff

**Scope:** source/config-only checkpoint for #387 / draft PR #392. No game assembly, server boot, Prism package, in-world acceptance, or proof of official Weeping Angels 1.0.5 binary parity. **Do not merge/declare a strict six-destination policy until the full runtime level census is present.**

## Locked rules and source status

Native teleport only, both local and interdimensional, 50% native chance, 400-block local displacement, no custom weighting, destination selector, fallback or return mechanic. Exactly six destinations are authorized: `minecraft:overworld`, `aether:the_aether`, `the_bumblezone:the_bumblezone`, `twilightforest:twilight_forest`, `theaurorian:the_aurorian`, `eternal_starlight:starlight`. The candidate TOML excludes seven source-confirmed non-target levels; **it does not yet include JAWR Spirit World** because no exact registry ID was verified, nor can it prove there are no more levels.

`angel_theft=false` is permanent; `block_breaking=true` is a conditional unclaimed-world QA target, not claim-safety proof. `config/weeping-angels-spawns.toml` disables Snow Angels; the `neoforge:none` same-ID biome modifier suppresses regular biome spawns. These files are intended for authored statue traps only; they do not create traps or implement scoped damage-source handling.

## Complete the negative list (integration writer)

1. Freeze the candidate **exact indexed modpack / datapacks** and verify official 1.21.1 NeoForge Weeping Angels 1.0.5 JAR bytes/hash, loader metadata/dependencies and generated config keys. Candidate: CurseForge project 291249, file 8962702, `forge-weeping_angels-mc1.21-v1.0.5-forge.jar`; the published third-party SHA-1 `2f5843d18e0fe3905d3569cc19022d4f2f7e7570` is **not an independently verified pin**. Indexing, packwiz hash and manifest edits are integration-writer owned.
2. **Read all actual loaded `ResourceKey<Level>` IDs** from the complete candidate runtime registry. Include data pack and programmatically created dimensions, JAWR Spirit World (pinned Witchery 0.5.39), possible additional JAWR realms, Feywild, Pastel and any new/modded worlds. A scan of `data/*/dimension/*.json` inside JARs is useful preflight evidence, not a substitute for the loaded-level registry.
3. Save the complete **witnessed** registry as JSON, with the exact current integration commit and *actual* resource IDs, e.g.:

```json
{
  "schema": 1,
  "source": "runtime_loaded_level_registry",
  "integration_commit": "0000000000000000000000000000000000000000",
  "dimensions": ["minecraft:overworld", "minecraft:the_nether", "minecraft:the_end"]
}
```

The example is **incomplete dummy data and MUST NOT be used as evidence**. Do not insert guessed IDs into this file or the native TOML.

4. Run `python scripts/audit_weeping_angels_dimensions.py --census path/to/observed.json --require-census`. The audit checks the native config and computes **actual loaded dimensions minus (six authorized + native blacklist)**; **any** unlisted loaded realm FAILS, as does a missing approved realm. Add every observed excluded ID to `banned_dimensions`, recheck and record source SHA, exact IDs, witness and date. Without a runtime census, `python scripts/audit_weeping_angels_dimensions.py` checks only the source candidate and prints `CENSUS BLOCKED`. `--require-census` exits nonzero without evidence. Optional `--jar-dir` only surveys dimension JSON files from already-available local JARs; it never downloads files or asserts a loaded dimension.
5. Verify native failure fallback and local vs interdimensional destination behavior during separately authorized B/C QA. Adding a seventh eligible destination, changing the native selector, or granting automatic Newbie travel exceptions is **not** implied by successful config validation.

## Other independent work, not config acceptance

- **Source implementation:** dedicated damage-source adapter for pickaxes, genuine fists/Monk hits, Dragon Survival bites/claws; reject sword/projectile/ordinary spell false positives while preserving generator and admin/void exceptions. No `ANYTHING` blanket override. Owner/design blocker: none; pinned native attribution/compat engineering is unresolved.
- **Structure authoring:** two verifiable dormant statue trap templates, calibrated footstep sculk chamber and hidden tripwire corridor, with correct block entities, redstone persistence and repeat/stealth/chunk tests.
- **B/C gameplay after authorized build:** no natural or Snow Angel spawns, no theft, six native teleport targets and five naked return routes; Newbie gating; claimed/unclaimed FTB Chunks block breaking, lights, candles and levers; damage-source attribution, mob/structure integration and save/reload.

A successful offline audit is **source policy verification**, not gameplay PASS, claim safety, actual JAR pin verification or permission to run Integration Assembly/Smoke/Prism. Keep PR #392 **draft** while these integration prerequisites remain unresolved.
