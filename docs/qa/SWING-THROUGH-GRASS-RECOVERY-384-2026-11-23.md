# Swing Through Grass — recovered approved next-QA pin (#384 / #385)

**Source integration only, 2026-11-23.** Base `test/full-integration@5177aa881fa0265621b57b80f57b66fba6a27bad`. An approved 2026-11-19 QoL candidate survived on draft #385, while the other two approved changes from that branch (fireproof dropped `minecraft:blaze_rod` and `minecraft:mushroom_stew` maximum stack size 16) are already byte-identical to the checked-in `kubejs/startup_scripts/vanilla_item_qol.js`; do not import that old branch's source snapshot again.

## Exact pin evidence

- Publisher [CurseForge file 9063127](https://www.curseforge.com/minecraft/mc-mods/swing-through-grass/files/9063127) identifies NeoForge **Minecraft 1.21.1** release **1.1.0**, exact filename `swingthroughgrass-neoforge-1.21.1-1.1.0.jar`, project **1477279**.
- Independent lock metadata for [Create & Dungeons 1.21.1](https://modpacks.ch/modpacks/1502385/create-dungeons) lists **that identical filename and CurseForge file 9063127** with SHA-1 `15f48ed3ab02b9e8763284279487a9d95b206337`. This is external cross-check provenance, **not a locally re-downloaded publisher binary**.
- Packwiz selected `metadata:curseforge` official provider mode; `side = "both"` intentionally selects server and the singleplayer client. Architectury 13.0.11 is already indexed; pinned native dependency range, provider download resolution and client/server startup still require subsequent independently authorized verification.
- New selected manifest/QA entry; Packwiz SHA-256 metafile/index chain regenerated. Preserved existing 248 pinned mod rows, Q Shop, food, quests, Lootr, Progression and shared scripts.

## Source checks vs in-world acceptance

- **Type A:** `python3 -m unittest discover -s tests -p 'test_swing_through_grass_intake.py' -v`; `python scripts/validate.py`; `python scripts/validate_integration_sources.py`. Protected repository quick CI and Packwiz read-only validation before merge.
- **Type B/C next owner-authorized test:** attacks pass through collisionless grass/ferns/flowers without breaking them; solid blocks/walls still block hits; verify range is unchanged, with modded reach and no creative reach exploit. Confirm dropped blaze rods survive lava/fire and burn/smelt normal fuel behavior. Check consuming 1 and 16 stacked vanilla mushroom stews, including full inventory, consumes just one and returns exactly one empty bowl without duplication/loss. Test ordinary nutrition unchanged.
- **No** Integration Assembly, full smoke, Prism package, game launch, or gameplay PASS from this source integration.
