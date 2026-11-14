# B7 — ATM10 QoL / Inventory Tweaks — ReFoxed

## Dedicated-server regression: PASS
- [Full CI resolver and server regression](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43558589888)
- Minecraft 1.21.1, NeoForge 21.1.265, Java 21.
- Existing 125-mod B6 baseline retained; exactly one new **client-only** mod pinned.
- ReFoxed exact: invtweaks-1.21.1-1.3.2.jar
- ReFoxed CurseForge project 976858, file 6407807, hash format sha1.
- Mouse Tweaks remains installed; Inventory Tweaks handles sorting and auto-refill.
- No other proposed ATM10 quality-of-life mods approved or added.
- **The new client-only ReFoxed JAR is not loaded by a dedicated server.**
- A server regression PASS means the previous server mod stack still boots, **not** that middle-click sorting was tested.
- Client test required: import the B7 Prism zip, open chest, middle-click to sort,
  inspect personal inventory, auto-refill, and Mouse Tweaks gestures.

## Rollback
Prior known-good local client generation baseline: B6, 125 mods,
commit `ecddc4342c439a2b973ad16acff112b7d0a4bb60`.
Detailed ATM10 comparison: [ATM10-QOL-AUDIT.md](ATM10-QOL-AUDIT.md).

## Prism client ZIP delivered
- [**Download the B7 updated Prism instance**](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43558589888/artifacts/14098959586), produced by [successful Action](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43558589888). GitHub supplies an outer ZIP; extract it and import the **inner `Too-Much-Magic-B7-Inventory-Sorting-Prism.zip`** into Prism.
- The installer carries zero third-party mod JARs; packwiz downloads the pinned 126 mods (including client ReFoxed) from their providers. Immutable source revision: `316d11a0626e5793ab7cc5d4b463891f80aa0fe6`.
- Test middle-click sorting in vanilla chest and personal inventory, player hotbar auto-refill and Mouse Tweaks wheel/drag. **No graphical B7 result has been claimed.**
