# Research pickup — local JAR continuation

## Current workstream

- Branch: `research/mod-comprehension-local-jars-20261116`; base `cb8e2dcab59cc625500b3ca0329df1511823610b`.
- User requests completion of major magic-mod research Markdown and regular recovery commits. Research only; preserve QA edits in the separate `qa-source` checkout.
- Start with `AGENTS.md`, `MOD-COMPREHENSION.md`, `WORK-CHAT-INSTRUCTIONS.md`, `INDEX.md`, templates and the quest walkthrough standard. Keep partial profiles `researching`; artifact inspection is not gameplay testing.
- [Artifact audit](LOCAL-ARTIFACT-AUDIT-2026-11-16.md): 156/159 current baseline pins match local files, all 159 metafiles match the index. Missing baseline JARs are Default Options, FTB Filter System and Keysets; all selected Goety research artifacts match. The prior QA checkout had 161/161 matches; those numbers describe different snapshots.

## Active slice and exact next steps

1. Re-audit Goety Cataclysm Fabricator through the hash-pinned addon and Goety 3.2.2 bytecode. Locally confirmed `FabricatorBlockEntity` extends `TrainingBlockEntity`; produces Watcher servants from Mechanized Iron Blocks; inherits queue, controls, timer and spawn-failure debit behavior. Record evidence beside claims and add positive/boundary disposable-world tests.
2. Expand addon recipe/servant coverage, verify Cataclysm inputs from its matching JAR, and reconcile the current Goety chapter/authoring manifest. Keep any unresolved focus/research/owner paths explicit.
3. Update index and this note, commit each coherent finding, then push and open a reviewable research PR to `test/full-integration`. Never merge into the shared integration branch yourself.
4. Next priorities: base Goety remaining research/ritual paths; Ars/Occultism exact artifact gaps; Mahou Tsukai exact artifact mechanics. Read existing profiles before duplicating work.

## Inspection method and local-only scratch

The local artifact directory is documented in the audit. `javap.exe` is available in Prism's `java/java-runtime-delta/bin/`. CFR 0.152 from its official site was used only for selected local classes; transient output lives under the Windows user's `AppData/Local/Temp/tmm-magic-research-20261116`. Do not stage decompiled Java, classes, JARs, assets or caches. Recover every claim by inspecting exact hash-matched artifacts; scratch paths are convenience only.

No full smoke, assembly, client launch, gameplay or new quest/config/runtime implementation is authorized by this research task. Workflow gates must be inspected before pushing; only quick CI is appropriate.
