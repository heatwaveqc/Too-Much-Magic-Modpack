# Meta / QoL integration smoke

NeoForge 21.1.265, Minecraft 1.21.1, Java 21 (GitHub Actions Linux).
**Dedicated modded-server smoke: PASS**.
Test used a disposable world and verified all server JAR hashes.
30 mod artifacts pinned: 23 installed on the dedicated server, 7 client-only. Zero requested project IDs failed to resolve.

**Graphical client: NOT TESTED**. Modded game logic and recipes: not tested.
CI workflow logs and artifacts are evidence for this run.

## Unresolved requested slugs
- None from automated Modrinth and official CurseForge project-ID resolution.

**Evidence:** [GitHub Actions run 43548230954](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43548230954), full server logs retained as workflow artifacts. Official FTB Quests, Chunks, Library, Teams and XMod Compat are included. The automatic Modrinth fuzzy matches to unrelated projects were rejected.
