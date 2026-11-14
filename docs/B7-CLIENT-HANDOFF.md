# Too Much Magic B7 — Prism sorting test

## Install
1. [Download the GitHub Actions artifact ZIP](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43558589888/artifacts/14098959586). Sign into GitHub if it requests authentication.
2. Extract the **outer** GitHub artifact ZIP. Do not extract the inner `Too-Much-Magic-B7-Inventory-Sorting-Prism.zip`.
3. In **Prism Launcher**, choose **Add Instance → Import**, and select the **inner ZIP**. Use **Java 21**, the same as your working B6 instance.
4. Launch. The bootstrap downloads pinned files from CurseForge and Modrinth as needed; no copyrighted mod jars were included in the ZIP. This version pins [packwiz commit `316d11a0626e5793ab7cc5d4b463891f80aa0fe6`](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/tree/316d11a0626e5793ab7cc5d4b463891f80aa0fe6).
5. In a **new test world**, open an ordinary chest, put items out of order, and **middle-click** inside its slots to sort. Repeat in your player inventory.
6. Move items using your existing **Mouse Tweaks** drag/scroll shortcuts, and check whether **tool auto-refill** works when a held tool breaks. Inspect controls if middle-click does something else.
7. If the client does not launch or sorting fails, share the Prism launcher console, `logs/latest.log`, and the relevant keybind settings screenshot.

## State and rollback
B7 has **126 pinned mods**; only Inventory Tweaks — ReFoxed 1.3.2 is new. The **118-mod dedicated-server stack passed its boot** in [run 43558589888](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43558589888); client-only behavior remains untested. Your earlier **B6 125-mod build remains the working rollback**. We have not added Crafting Tweaks, Akashic Tome or any other proposed QoL systems without permission. Quests, actual Pufferfish custom skill trees, and ProjectE gating still need implementation.
