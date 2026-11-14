# B1 — first actual mods, pinned from Modrinth

This candidate contains four downloaded mods as packwiz metadata: KubeJS 2101.7.2-build.381, Rhino 2101.2.7-build.85, Better Advanced Tooltips 2101.1.0-build.5, and ModernFix 5.27.27+mc1.21.1.

Source: reproducible packwiz resolution in GitHub Actions run 43546027388, metadata job 132297437117. Each .pw.toml includes exact Modrinth version ID and SHA-512 content hash. The unexpected Better Advanced Tooltips dependency was automatically selected by packwiz and has been recorded rather than hidden.

**Status:** modded dedicated-server **smoke passed** in GitHub Actions run 43546287284; SHA-512 verification for all four mods and NeoForge `Done` with exit 0. Graphical client **NOT TESTED**. Baseline NeoForge loader-only smoke passed in run 43545677885. The next required test is fetching JARs and starting a fresh **modded** dedicated server, then a client. No gameplay locks enabled.
