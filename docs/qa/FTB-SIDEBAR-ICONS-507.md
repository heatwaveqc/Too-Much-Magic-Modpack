# FTB inventory sidebar: offline image provenance probe (#507)

The latest user-visible failure is **black square buttons** in the inventory's top-left FTB Library sidebar. The native FTB Library sidebar owns positioning, click actions and icons. TMM's current `config/ftblibrary-client.snbt` sets only `sidebar.position: top_left`; the TMM KubeJS assets do not currently provide an FTB-namespaced icon override. This does **not** establish which layer renders black squares.

## Type A — pinned offline audit

Run from the repository root:

```sh
python3 -m unittest discover -s tests -p 'test_ftb_sidebar_assets.py'
python3 scripts/audit_ftb_sidebar_assets.py --output /tmp/tmm-ftb-sidebar-source.json
python3 scripts/audit_ftb_sidebar_assets.py --mods-dir /path/to/installed-profile/mods --output /tmp/tmm-ftb-sidebar-installed.json
```

The source-only mode verifies the Packwiz SHA lock for FTB Library, Quests, Teams, Chunks and Essentials and enumerates pack-owned FTB asset overrides. The optional installed-JAR mode (never downloads JARs) verifies each FTB JAR's exact pinned digest, enumerates every sidebar JSON in installed JARs, checks literal `namespace:textures/*.png` references against the **combined** installed assets, flags duplicate candidate icon assets, and separately reports symbolic `ftblibrary:icons/...` IDs. Never misclassify symbolic icon lookups as missing PNG files.

A missing literal PNG or malformed sidebar JSON fails the installed audit. A successful audit is **not** proof that the icon renders. It cannot establish the active client atlas, GUI shader/render state, font provider, resource-pack stacking, cached player sidebar layout, or screen click/tooltip behavior. Do not directly overwrite a native atlas, remove/disable sidebar buttons, change mod pins, or assume a recent FTB Library upgrade repairs the issue.

## Code-ready / blockers / gameplay

- [x] **Source-ready:** Read-only exact pinned asset/definition auditor and synthetic unit coverage. No shared integration paths, mod pins, or UI behavior changed.
- [ ] **Engineering evidence:** Run installed-JAR audit on the failed installed client; compare resource-pack selection, clean profile, exact pinned runtime and UI scale, then diagnose direct-texture vs symbolic icon vs rendering failure. Investigate source-defined sidebar icon rendering (not generic quest tile textures).
- **Design blockers:** None. A corrective UI change remains **engineering-diagnosis blocked**, not approved as an arbitrary replacement of original FTB icons.
- [ ] **Type C, next authorized client:** all expected FTB buttons render distinguishable icons with correct tooltip and click target; compare a fresh disposable profile with an existing QA profile. Do not claim this from Type A.

Issue: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/507

## Exact owner-uploaded JAR audit — November 22, 2026

The owner supplied seven FTB 1.21.1 NeoForge JARs for static examination, all matching the current `test/full-integration` Packwiz SHA-1 pin values (source JARs **not committed or redistributed**):

| Exact JAR | SHA-1 | Sidebar definitions |
| --- | --- | ---: |
| `ftb-chunks-neoforge-2101.1.24.jar` | `f0d48143cc80918cf632a4c14c3ca3e974ed0457` | 2 |
| `ftb-essentials-neoforge-2101.1.10.jar` | `fa7b3d1b06c0285dcbe8e53a27d44cd7f8d15726` | 1 |
| `ftb-filter-system-neoforge-21.1.4.jar` | `a48e54edeacc59aaba29a1b627799c052112ee0b` | 0 |
| `ftb-library-neoforge-2101.1.39.jar` | `ff852d99ade215fb892369687d0634ecf0bca6ae` | 5 |
| `ftb-quests-neoforge-2101.1.38.jar` | `389fb269d338ed4c7f14f945a07759b6d444af40` | 1 |
| `ftb-teams-neoforge-2101.1.12.jar` | `9c831fbbbccf2178827c3ccf278b2f2a0ce57306` | 2 |
| `ftb-xmod-compat-neoforge-21.1.14.jar` | `8dc3db7b8aa93eabac54237c5d53bfea15f92ba5` | 0 |

**Findings:** All seven ZIP archives open, all 367 texture PNGs across them pass Pillow image decoding/verification, and the five representative sidebar icons (FTB Library map/bin/settings, Teams image, Quests book) contain nontransparent pixels. There are 11 sidebar JSON definitions: five literal PNG paths with images present, and six atlas-sprite expressions. No missing literal sidebar PNGs were observed.

The four FTB Library atlas-sprite uses (`ftblibrary:icons/map` twice, `ftblibrary:icons/bin`, `ftblibrary:icons/settings`) have **both** real PNG files and the supplied FTB Library `assets/minecraft/atlases/blocks.json` directory source `{"type":"directory","source":"icons","prefix":"icons/"}`. Binary inspection of `Icon.getIcon0` confirms non-`.png` resource IDs become `AtlasSpriteIcon`; `AtlasSpriteIcon.draw` fetches the sprite from `InventoryMenu.BLOCK_ATLAS`. Claim Chunks' `"; tint=..."` syntax is stripped by `Icon.getIcon` before this selection. The other two atlas IDs are `ftbquests:item/book` (the book PNG exists in Quests but its atlas inclusion needs vanilla's atlas source) and `minecraft:item/diamond_sword` (vanilla texture not included in uploads). They must **not** be mislabeled missing from these mod archives.

**Interpretation:** An absent/black image inside these uploaded JARs is not the demonstrated cause of the owner's black buttons. A missing stitched sprite in the *effective full client's* combined block atlas, a different loaded resource pack, a GUI shader or render-state issue, or GUI profile differences remain possible. Source-level asset presence does not establish runtime stitching or rendering. FTB Library 2101.1.40 exists upstream for 1.21.1, but its available changelog does **not** specifically attribute/fix this symptom. No update is approved by this report.

**Next focused Type C/engineering test:** On an otherwise identical disposable client, compare a **direct PNG** button (FTB Teams' My Team, `ftbteams:textures/teams.png`) with an **atlas-backed** one (FTB Chunks map, `ftblibrary:icons/map`). If only atlas-backed icons fail, inspect the effective `minecraft:blocks` texture atlas and sprite-registration/resource-pack precedence. If both fail, investigate common FTB Library GUI drawing/shader state, overlays and client resource packs before editing any icon JSON. Verify buttons still click and tooltips appear. Do not preemptively disable the sidebar.

The installed-JAR audit script now reports packaged atlas source evidence, but does not conflate it with a final atlas-resolution pass. With only these seven JARs, do not infer other installed mods cannot override the same texture/atlas paths.
