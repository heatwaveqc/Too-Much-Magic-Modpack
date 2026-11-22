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
