# Issue #47 — exact-world EMC coverage census

**Status:** source implementation for a one-shot, administrator-triggered
Minecraft export, plus offline validation. This is NOT a real full-pack
census or gameplay acceptance. Do not assign missing values blindly.

## Why this exists

The existing `scripts/audit_emc_coverage.py` needs two authoritative
inputs: the **same running NeoForge 1.21.1 server's item registry** and
ProjectE's **post-mapping EMC values**. A hand-maintained list of JAR
asset IDs, mod manifests, defaults, or ProjectE custom_emc.json cannot
substitute for actual values. This command obtains the two inputs in one
server-thread invocation, after ProjectE reports a loaded EMC map.

## Operator action after including this code in the integrated test build

1. Check the exact integration commit SHA and freeze that source revision.
   Verify the installed packwiz manifest and mod hashes separately; the
   command takes the SHA as an **operator-provided label, not attestation**.
2. In an expendable test world with ProjectE fully mapped, run as permission
   level 3+ (server admin):

   ```
   /tmm_economy audit_emc <exact_40_character_integration_commit_sha>
   ```

3. The server writes one completed directory beneath
   `<world>/tmm-economy/emc-audits/source-<12sha>-<timestamp>/`:
   - `items.json`: sorted **all registered item IDs**, not inferred source
     assets, from `BuiltInRegistries.ITEM` (the registered `air` sentinel
     remains present for explicit coverage review).
   - `emc.json`: mapped base values via `EMCMappingHandler.hasEmcValue`
     and `getStoredEmcValue`. A missing entry is **absent**, not silently
     assigned a zero price. Mapped-zero values remain explicit zero.
   - `status.json`: explicit mapped-positive, mapped-zero and unmapped
     counts and IDs.
   - `metadata.json`: operator-supplied source SHA, UTC time, ProjectE
     mapping generation, raw registry and mapper counts and disclaimer
     that SHA is not programmatically attested.

   A failed/incomplete write remains in `incomplete-*` and is rejected
   by the analyzer rather than counted as successfully sampled.
   The exporter is on-demand, not a tick/update listener.
4. Copy the **entire** completed snapshot out of the disposable world.
   Audit it with:

   ```sh
   python3 scripts/audit_emc_snapshot.py \
     "/path/to/completed/source-.../" \
     --expected-source-sha <exact_same_SHA> \
     --output /path/to/emc-coverage-report.json
   ```

   The analyzer checks all four JSON files agree on total, unmapped and
   mapped-zero values. It records a SHA-256 digest of each input file, a
   per-namespace missing report and the approved exclusions (glass-pane
   family and two Bountiful signed document types). It can be rerun
   with `--enforce` to fail when coverage remains incomplete.

## Important boundaries

- **No world modification other than writing the audit files.**
  No item/EMC/player mutation, no per-tick scans, no automatic full game
  boot, no full Integration Assembly, no Prism build.
- A base item registry value is **not** proof that component-bearing
  stacks are priced safely. Follow-up tests separately examine filled
  storages, books, tools, charged items, NBT and Project Expansion
  BigInteger cases.
- This is **not** an automatic root price generator. Once the real
  report exists, separate true roots from missing recipe mappers,
  gated items, tags, rootless SCCs and intentional zero policies as
  specified by #326 and #322. Preserve owner-approved accounting and
  deliberate productive paths. No generic fallback `1 EMC`.
- Source CI can verify Java compilation/fixtures and the offline
  snapshot validator; **only a post-integration test world** can produce
  a real census and test runtime semantics. Never make unrun gameplay
  acceptance a prerequisite to integrating reviewed source code.

## Engineering and QA ledger

- [x] **Source staged**: permission-3 on-demand command, deterministic
  registry/map encoder, explicit absent-vs-zero states and atomic
  complete-directory publication when supported.
- [x] **Source staged**: offline same-snapshot validator with
  negative fixtures and approved Bountiful-document exceptions.
- [ ] **Type A**: exact pinned Economy module Java compilation/JUnit
  and snapshot fixture CI (check head before integration).
- [ ] **Integration writer**: reconcile this isolated source against
  current `test/full-integration`, source manifests/Packwiz if relevant;
  do not replay obsolete Economy paths.
- [ ] **Type B after integration**: invoke command in disposable
  server world after ProjectE maps, validate registry count,
  load generation, permissions, output completeness and no performance
  spike, then run offline report.
- [ ] **Type C after integration**: test same save/reload mapping,
  actual transmutation/condensers, ingredient loop exploit cases,
  two-player parity and component-bearing items.
- [ ] **Owner pricing / #326**: produce per-mod root/recipe gap
  classification and assign missing *approved* anchor values until
  complete against the frozen build.
