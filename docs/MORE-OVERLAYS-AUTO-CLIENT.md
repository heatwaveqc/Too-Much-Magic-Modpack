# More Overlays Updated — automatic client installation

**User QA issue, 2026-11-14:** More Overlays was repeatedly added by hand to the Prism test instance. The mod **was already in** the official 140-mod `manifest/mods.json`, with `side = "client"`, exact NeoForge 1.21.1 version `1.24.2`, and a valid `index.toml` reference. It was therefore not missing from the modlist. The weak link is likely the **CurseForge metadata/manual-download path**, since the original entry relied on `mode = "metadata:curseforge"`.

The approved binary is `moreoverlays-1.24.2-mc1.21.1-neoforge.jar`, pinned with SHA1 `21e8bd61c5e146a9574266015d3f64621c2d9e18` from CurseForge file `6981252`. The exact same release exists as an [official Modrinth version](https://modrinth.com/mod/more-overlays-updated/version/Kq8xaqKi) (project `Thy5Pqut`).

`scripts/resolve_more_overlays.py` checks the Modrinth API for project, exact release, filename, MC 1.21.1, NeoForge and **SHA1 byte identity with the old pin**. It **refuses** any changed bytes or mismatched release. When confirmed, it installs the official Modrinth **direct CDN URL + SHA512** into the existing `.pw.toml` and refreshes `index.toml` and `pack.toml`. The resolver runs under `.github/workflows/more-overlays-autoinstall.yml`, and the action commits only the verified pin.

The updated Prism bootstrap explicitly uses **`-s client`**, so it installs client and both-side mod entries. The generated Prism ZIP remains mod-JAR-free; the installer downloads the official file on launch.

**Client acceptance test:** Import the `Too-Much-Magic-More-Overlays-Auto-Client-Prism` artifact and run it. Verify Prism **Edit Instance → Mods** lists More Overlays Updated, or inspect `minecraft/mods/moreoverlays-1.24.2-mc1.21.1-neoforge.jar`. F7 should display light levels and F9 should display chunk borders (resolve key conflicts if necessary). If the file is missing, capture packwiz installer logs and manual-download prompts. An automated metadata pass cannot prove actual GUI behavior.

**Release:** The official release will use this hash-pinned client entry and packwiz-based auto-download, avoiding manual installation of More Overlays. Other CurseForge-only mods may still require separate manual download; those are a distinct issue. Do not treat a successful server-only smoke as verification of a client-only mod.
