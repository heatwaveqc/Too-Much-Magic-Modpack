# Research pickup — local JAR continuation

## Current workstream

- Branch: `research/mod-comprehension-local-jars-20261116`; base `cb8e2dcab59cc625500b3ca0329df1511823610b`.
- User requests completion of major magic-mod research Markdown and regular recovery commits. Research only; preserve QA edits in the separate `qa-source` checkout.
- Start with `AGENTS.md`, `MOD-COMPREHENSION.md`, `WORK-CHAT-INSTRUCTIONS.md`, `INDEX.md`, templates and the quest walkthrough standard. Keep partial profiles `researching`; artifact inspection is not gameplay testing.
- [Artifact audit](LOCAL-ARTIFACT-AUDIT-2026-11-16.md): 156/159 current baseline pins match local files, all 159 metafiles match the index. Missing baseline JARs are Default Options, FTB Filter System and Keysets; all selected Goety research artifacts match. The prior QA checkout had 161/161 matches; those numbers describe different snapshots.

## Active slice and exact next steps

1. Completed this slice: Goety Cataclysm Fabricator and parent training/altar bytecode audit, 61-recipe appendix, 11 conversion routes, all 47 Cataclysm recipe item/block IDs matched registration literals, exact scaffold IDs, and targeted boundary tests. Key corrections: charged ritual advances are 20 game ticks apart; nominal cost is rate × duration; obstructed Fabricator attempts spend a queue entry. Profile remains researching because focus effects, research/structure gates, servant care and full acquisition paths are incomplete.
2. Next: map all focus cast effects/costs, parent research unlock/Forge setup, servant care/death recovery and boss-drop acquisition. Recipe inventory and exact current scaffold reconciliation are done; do not repeat them. The appendix is factual data coverage, not proof of complete progression.
3. Update index and this note, commit each coherent finding, then push and open a reviewable research PR to `test/full-integration`. Never merge into the shared integration branch yourself.
4. Next priorities: base Goety remaining ritual/focus/structure paths; Ars/Occultism exact artifact gaps; Mahou Tsukai exact artifact mechanics. Read existing profiles before duplicating work.

## Inspection method and local-only scratch

The local artifact directory is documented in the audit. `javap.exe` is available in Prism's `java/java-runtime-delta/bin/`. CFR 0.152 from its official site was used only for selected local classes; transient output lives under the Windows user's `AppData/Local/Temp/tmm-magic-research-20261116`. Do not stage decompiled Java, classes, JARs, assets or caches. Recover every claim by inspecting exact hash-matched artifacts; scratch paths are convenience only.

No full smoke, assembly, client launch, gameplay or new quest/config/runtime implementation is authorized by this research task. Workflow gates must be inspected before pushing; only quick CI is appropriate.

## Validation and continuation receipt

- Recovery commits: `9425b041` (local access audit/pickup) and `8efd4af1` (addon recipe/operating audit); subsequent commit corrects base-Goety ritual totals and cleans historical blocker text.
- `python scripts/validate.py`: PASS after canonical LF text normalization in the isolated research checkout (159 active pins, 294 indexed files, 181 desired selections).
- `python scripts/validate_integration_sources.py`: read-only PASS at `8efd4af1`, after fetching the 13 missing frozen donor commits. No assembly or smoke ran. Rerun on the final clean research head.
- Windows Git initially materialized CRLF text outside explicit `eol=lf` patterns. Only isolated-worktree text bytes were normalized and the unchanged index refreshed; no runtime/config changes entered the research commits. The unrelated `qa-source` edits were preserved.
- Local Markdown check found one pre-existing template placeholder in INDEX (`mods/<manifest-id>.md`); exclude that deliberate placeholder from literal link validation. All 61 recipe keys occur exactly once in the appendix.
- Base Goety Forbidden Fragment/Piece acquisition was already expanded on the current integration base. Do not repeat that work or keep the older claim that no sourcing is mapped. Its runtime loot acceptance remains open.
