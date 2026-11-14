# Too Much Magic — B6 local client smoke-test instructions

**Status:** Import archive built and structurally verified. Server boot passed; **graphical client NOT yet tested**.

## Download and import into Prism

1. [Download the GitHub Actions artifact](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43555071188/artifacts/14097951209). GitHub may require you to be logged in; the artifact expires **2026-12-14**.
2. Extract the downloaded outer artifact ZIP. Inside is **`Too-Much-Magic-B6-Prism-Client-Test.zip`**. Do not extract the inner ZIP.
3. In [Prism Launcher](https://prismlauncher.org/), choose **Add Instance → Import → Browse** and select the **inner ZIP**.
4. Select **Java 21** under the instance Java settings. The generated instance uses **8 GiB maximum RAM** (2 GiB minimum); adjust it if your computer has less available RAM.
5. Launch. A lightweight MIT-licensed packwiz bootstrap is embedded. It downloads the exact files directly from Modrinth/CurseForge as required, verifying packwiz hashes. **The ZIP does not include mod JARs.** If CurseForge asks for a manual file, follow the official download prompt and **do not substitute versions**.

The instance's packwiz source is pinned to a permanently identifiable, successfully boot-tested commit:
`ecddc4342c439a2b973ad16acff112b7d0a4bb60`

## Checks during your local boot

- Verify that the main menu appears, inspect the Mod List/JEI/Jade and examine the client log for mixin/render errors.
- Create a **new disposable world**; never use an existing valuable save for this experiment. Check Tectonic landforms and structure generation.
- Inspect the **Pufferfish Skills** menu; the **custom cross-mod skill tree does not exist yet**, so do not interpret a blank tree as a failure by itself.
- Check that **Improvable Skills 3** loads separately, including its skill-book interface and XP upgrades.
- Check Pam's gardens, fruit trees, Farmer's Delight crops/cooking, Aquaculture fish, **Harvest with Ease right-click harvesting**, and basic cooking/food tags.
- Look upward at the **Astral Sorcery sky**, especially alongside Embeddium; review Botania visual effects and Draconic Evolution HUD/energy/armor effects.
- If launching with shaders, do a second pass without shaders before declaring a crash or visual incompatibility.

If the game crashes or fails to enter a new world, share **Prism Launcher console output** and `logs/latest.log` (or `crash-reports/`), including the first **Caused by** section. Please report the exact screen or action that failed. Server evidence alone does not verify client stability.

## Important gameplay limitations

- **ProjectE and Project Expansion are not gated behind Chaos Guardian yet.**
- Replication, tech ore, and late miner restrictions remain to be implemented.
- Food-balance, transformation compatibility, Pufferfish tree, and quest guidance are not complete.
- This is a client compatibility prototype, **not** a production-balanced survival release.

Source branch: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/tree/build/b6-food-pufferfish-skills-client-handoff

Verified source test: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43554705678

Prism package build: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43555071188
