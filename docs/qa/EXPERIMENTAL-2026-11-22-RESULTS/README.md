# Experimental A/B investigation — November 22, 2026

Owner requested immediate investigation of ghosts and a BetterEnd advancement, autonomous A/B testing/tooling, and repository publication. The already-open disposable world was inspected and rejoined; no Minecraft process, new world, full server smoke, assembly or Prism package was started. No enrollment, XP grant, graduation, teleport, summon, combat, death fixture, recipe craft or game-rule change was performed. Three save/rejoin cycles loaded successive temporary QA probe revisions. **World left paused; screen access ended; temporary probe, enable flag and reduced build-manifest copy removed.** This does not roll back ordinary ticks, natural mob behavior or autosaves during the authorized session.

## Exact scope

| Field | Evidence |
| --- | --- |
| Installed E source | `c56fb18ffc3b9f12f27317e7fb37f15c32576a8c` |
| Integration baseline | `74165da57a97228559562e016e1f7b796f60e652` |
| Four-update candidate | `b604cb3c483c0004e481d3fcf71eaf855f895438` |
| Existing server smoke reference | `45048210980`, recorded in installed BUILD-MANIFEST; not independently repeated |
| Installed inventory | **226/226 exact expected JAR filenames/hashes**, including four first-party modules |
| Source roster / index | **222 active pins / 584 indexed files** |
| Independent module result | **244 tests, 0 failures, 0 errors, 0 skipped** |
| In-game census | **26,569 loaded recipes**, zero recipe read errors; actual command-root list captured |
| U control | Source metadata only, baseline above. No U client installed or run in this turn. |

`pin-delta.json` verifies exactly the intended four pin differences versus the baseline: Ars Elemental 0.7.10.4 → 0.7.11.3 beta, EvilCraft 1.2.100 → 1.2.102, Goety 3.2.2 → 3.2.3, Neo Vitae 1.1.53 → 1.1.54. Neither source checks nor launch success approve promotion of the Ars Elemental beta.

## Immediate findings

### Ghosts: Lost Soul restriction has not reached the installed pack

The saved player snapshot is in `minecraft:overworld`; its Adventuring Time criterion records `minecraft:snowy_beach`. The observed minimap also displayed Snowy Beach. Region/entity NBT was read without editing the save. In the saved 128-block neighborhood there are **six `forbidden_arcanus:lost_soul`**, all roughly at surface height (−1.1 to +9.3 relative to the saved player), nearest about 44 blocks. There are also 14 Eidolon wraiths and two Goety wraiths, but those saved entities are underground; they must not be conflated with the visible surface ghosts. See `saved-world-evidence.json` for distances/height offsets and read status, without player UUID/name/world coordinates.

The installed `forbidden_arcanus-2.6.1.jar` native resource `data/forbidden_arcanus/neoforge/biome_modifier/add_lost_soul_overworld.json` targets **`#minecraft:is_overworld`**, weight 35, group 1–3. There is no matching installed KubeJS Lost Soul override. Thus ordinary surface Overworld eligibility is explained by the installed native rule; this is not evidence that the four updated mods changed spawning. The saved census and native rule strongly support Lost Souls as the reported surface ghosts, but no individual on-screen entity was targeted to prove each visual identity or its exact spawn origin/biome.

The owner-approved restriction exists in **open, unmerged PR #343**, head `e63a1d4ced011972087540cee3ad6941074c595f`, not this runtime. Its latest handoff includes Deep Dark, Soul Sand Valley and Twilight Forest work plus a broader approved spooky-dimension roster whose remaining biome IDs/filtering still need engineering evidence. The PR explicitly remains draft/not declared live-test-ready. Record this as **approved source work not installed**, rather than assuming a shipped restriction regressed. Do not merge or invent biome IDs from this report.

### BetterEnd advancement: native root is not End-gated

The actual saved award is **`betterend:root`**, completed at first join, 10:30:21 America/Phoenix. Installed `BetterEnd-21.0.39.jar` contains `data/betterend/advancement/root.json` with `minecraft:location`, an empty `location: {}` predicate and **no dimension restriction**; its sole requirement is `welcome`. Therefore the root can be earned in the Overworld. This award does not show that BetterEnd took over Overworld generation or that an End-specific exploration milestone fired. No native advancement override was made. Delaying/hiding that welcome root until the End would be a separate owner-approved presentation change, not an established compatibility repair.

### Confirmed TMM script defect: parchment fishing does not load

The real KubeJS server log reports:

```text
parchment_fishing.js#43: SyntaxError: invalid object initializer
originals.push({entry, weight, quality})
Loaded 12/13 KubeJS server scripts ... with 1 errors
```

The 13th script in that count is the temporary QA helper. With that helper absent, this represents one failed shipped script among the 12 installed pack scripts. Parsing all installed server/client/startup scripts using the **actual Rhino 2101.2.7-build.85 parser**, without evaluating them, checked **21 files including the temporary helper: 20 passed, only `kubejs/server_scripts/parchment_fishing.js` failed**. See `rhino-syntax.json` and reproducible `scripts/audit_kubejs_syntax.py`.

This object-shorthand initializer is accepted by Node but rejected by the pinned Rhino parser. The entire authored fishing script fails during parse; do not claim the TMM parchment weighted-treasure change is active merely because its file exists or Node accepts it. Source introduction is `170e959a919e043d41f1bad52e7ea94d2d05bcf9` (Nov 21, weighted fishing change for #318), integrated through `5e10a15d`. No gameplay script was patched in the instance or this reporting branch. A targeted owner-branch correction should use supported syntax and pass the pinned parser before real fishing acceptance.

### Other observed client noise

An initial log scan counted **at least 4,639 `65539: Invalid key -1` messages**. This continues the earlier unbound-key polling problem; no individual provider is attributed without further evidence. It did not prevent this client from entering/rejoining the world. Project Expansion's missing access-transformer resource warning, native Pam's legacy asset path warnings and upstream guidebook references also occur; they are not promoted to independent native-mod test obligations here. No broad native feature tour was performed.

## A results

`a-checks.json` records exact commands, exits and relevant output. All **A01–A12** final source/fixture checks pass against the installed frozen source. A03 ran in the exact detached Git checkout after fetching frozen donor refs; canonical content reruns used a clean archive of the same commit with `core.eol=lf`.

Initial A01/A04/A09 failures were local test-input problems: checkout/archive CRLF conversion and ignored retired `config/tyzs_skills` residue from switching older checkouts. Git object bytes matched pinned values. A clean LF archive removed the residue and restored the exact committed bytes, after which all three passed. Expected hashes were not rewritten and validators were not weakened. Initial failures are preserved separately in JSON, not misreported as runtime defects.

| Module | Independent compile/JAR task | Tests |
| --- | --- | ---: |
| `tyr_progression` | PASS | 178 |
| `tmm_transformations` | PASS | 38 |
| `traders_vengence` | PASS | 11 |
| `tmm_economy` | PASS | 17 |

Modules were built/tested in the isolated frozen-source archive with Gradle 9.2.1 / Java 21 and Economy's supplied installed mod classpath, after inventory verification. Only `test`/`jar` tasks ran; **no Gradle client/server run task**. This is not a claim of byte-for-byte reproducible JARs. Installed bundled modules and native pins separately match their expected hashes. The repaired installed Economy/Jade dependency-closure check passes against the pinned Jade JAR's effective qualified version. Offline packaging fixture A12 is not a new Prism package. A14 Astral design-input perk census was not run; it is not relevant to either reported symptom. No U runtime/differential gameplay result is asserted.

## B and passive live evidence: deliberately bounded

Direct automated chat keystrokes did not open Minecraft chat, although pause/menu controls worked. Configured vanilla chat/command bindings were present on disk. No settings were changed to force chat input. Instead, a temporary OP-only `PlayerEvents.loggedIn` probe attempted the same read-only QA APIs after rejoining the existing world.

Captured successfully: **actual registered command roots and 26,569 effective recipe IDs/types/default outputs/counts**, with zero recipe read errors. The census includes `tyrprogress`, `tyrmxp`, `tmmnewbie`, `tmmtransform` and the installed structure exporter. The old checklist's `/tmmprogress` and `/tmmmxp` roots are absent; future player commands must use source-verified current syntax. The compressed `live-command-recipe-census.json.gz` publishes that derived registry evidence only.

**Not captured / no B PASS:** player ledger/wallet snapshots, actual crafting-match verdicts, full baseline and live biome/entity census. QA adapter failures were: forbidden parent-directory JsonIO access, blocked FML `ModList` class, ambiguous Java String construction, KubeJS-renamed `getUUID`, and an entity-type description accessor mismatch. They are QA-tool failures, not pack gameplay failures. The per-stage completion log marker did not mean all captures passed. Initial helper code was written before a live-world opportunity and its mocked tests did not cover those Java bridge contracts.

Source corrections now stage an in-directory reduced build manifest, avoid the filtered FML API, use KubeJS UUID/type properties, retain partial observation fields, use Guava hashing without ambiguous constructors, recognize Tyr's `LedgerCodec`/`MutationLedger`, resolve the current Bio-Mutagen recipe ID and report explicit per-capture statuses. **Final adapter corrections are source-only, not rerun live.** Fourteen Node contract tests pass; do not convert that into a native-bridge PASS. Three save/rejoin cycles completed without a crash, but there was no valid nonzero player-state before/after capture, so mode/XP/skill persistence remains PARTIAL/unproven.

The last world state was left at the pause menu. Temporary probe and its two local configuration files were removed; local exports remain available. Already-registered read-only callbacks stay in that server session until a script reload/world exit, but the one-shot login callback requires the now-removed flag and will not run again. No client helper/key capture was installed or run this turn.

## Handoff inventories

### Implementation-ready / engineering follow-through

- [x] Publish exact E A results, ghost/advancement findings, parser defect and sanitized census — implemented/source-ready in this report.
- [x] Add read-only NBT/entity and pinned-Rhino syntax tools; revise passive QA adapters and explicit failure reporting — source-ready. Native final-adapter acceptance still pending.
- [ ] Fishing owner (#318/#319): repair the unsupported initializer (and any subsequent runtime bridge issues), rerun pinned-Rhino checks, then stage through normal ownership/integration. Native fish/grind tests are unnecessary; assertion is on TMM parchment weighting.
- [ ] Lost Soul workstream (#343): finish registry-backed approved biome filters and integrate through its designated writer when ready. Current runtime lacks the restriction; avoid an unrelated blanket ghost/wraith removal.
- [ ] QA/integration owners: include actual Rhino parsing of authored runtime scripts in lightweight A validation. Node syntax alone missed a real startup failure. This can run without Minecraft.
- [ ] QA helper owner: validate revised native adapters in the next authorized existing-world session; keep failed/unsupported captures visible rather than reporting a blanket completion marker.

### Explicit design blockers

No new design decision is needed to diagnose or repair the parser defect. Lost Soul roster intent is already recorded in #343; missing registry IDs are engineering inputs. A change to BetterEnd's native welcome advancement timing is **not approved** by this report and needs owner direction only if desired. No new mod inclusion, balance, crafting or spawn policy was invented.

### Remaining B/C queue for owner's return

- [ ] **T02 / B-C PARTIAL:** verify actual nonzero Tyr ledger/wallet persistence across rejoin once snapshot adapters work; game rejoin alone passed, full state assertion did not.
- [ ] **T03/T04/T06 / B-C ready for authorized QA:** source-verified Tyr status, legitimate path/skill/XP behavior; no synthetic grants used in this turn.
- [ ] **T17/T18/T20 / C ready for authorized E QA:** changed EvilCraft canonical blood boundary, Neo Vitae/Mahou dust bridge and one beta Ars Elemental spell through the TMM XP hook. No native mod functionality tour; U control only if E differs.
- [ ] **T11 fishing / C failed-follow-up:** first repair/include the parser-compatible authored script, then measure TMM weighted treasure behavior. Do not ask the player to grind while the script is unloaded.
- [ ] **Ghost restriction / C awaiting #343 inclusion:** verify selected approved spooky biomes and a snowy-beach negative control with exact effective biome-spawn data. Saved surface entities are supporting evidence, not a natural-spawn event trace.
- [ ] **BetterEnd root:** native trigger explained; no mandatory repair/retest unless owner changes the desired welcome presentation.
- [ ] **UI / C:** unbound-key error flood attribution, visual rendering/HUD and actual interactions still require targeted observation. Screen capture and tools must be ended after each bounded UI task.

References: PR #476's dual-channel scope/check IDs (its documented package SHAs predate this repaired installed build), #343 Lost Soul source, #318/#319 parchment, and the installed manifest above. U/E scope is TMM-authored boundaries and reported bugs; unrelated native behavior was not recertified. No raw logs, saves, region files, screenshots, JARs, player names/UUIDs/world coordinates or private key bindings are committed.
