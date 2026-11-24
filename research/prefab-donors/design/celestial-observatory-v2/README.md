# Celestial Observatory V2 — STATIC_QA_PREVIEW_ONLY
Supersedes V1 for QA; read ../celestial-observatory-v1/README.md for underlying layout and research. Owner approved native marble/infused wood/pillars and recessed altar/Lightwell, sky access. No donor assets or machines. Original V1 exact grid remains unchanged.

## Revision
Size 67×27×62; surface Y6 (placement offset -6 relative to selected floor elevation). 4600 nonair; 3542 Astral marble family, 533 infused-wood family. Replace all calcite/smooth stone/diorite/deepslate/dark oak stand-ins with native marble, sooty marble, infused wood, native stairs and engraved trim. Marble pillar bottom/middle/top states and arch lintels incorporated into the residence; native pillar lantern piers outside functional reservations. Retain blue dome and cyan glazing. No dark oak remains. Source registry and generated blockstate files at Astral tree 0ec3ff0fcb202d8035fc5aef409681fe402e1d0b verified; exact installed JAR not inspected. Infused wood planks are normally an illumination altar craft (4 output from 1 infused wood); explicit owner-approved decorative exception; audit recoverable material and EMC parity before release.

## Machine spaces
- Altar T2–T4: 19×10×19, origin Y6, one-block recessed terrace, full rectangle explicit air through Y26; static pass.
- Attunement: 23×7×23, origin Y7, level terrace, full rectangle explicit air through Y26; static pass.
- Infuser: 11×6×11, origin Y6, one-block recessed terrace, full rectangle explicit air through Y26; static pass.
- Gateway: 11×4×11, origin Y7, level terrace, full rectangle explicit air through Y26; static pass.
- Lightwell and crystal handling: 11×6×11, origin Y6, one-block recessed terrace, full rectangle explicit air through Y26; provisional size.
- Lumen and auxiliary machinery: 11×6×11, origin Y7, level terrace, full rectangle explicit air through Y26; provisional size.
Attunement floor uses sooty marble, not a finished altar. Infuser's two replaceable layers retained. Three recessed courts have three-wide marble stairs outside their machine envelopes. Lightwell sites X,Z (9,50), (15,50), (9,56), (15,56) at Y6; engraved caps Y5, clear service void Y3–4; remove caps for bottom extraction (source TileLightwell exposes inventory/fluid below, requires air immediately above or destroys catalyst). Vault access stair from west; exact collision and terrain sealing pending. No Lightwell placed, no fluid or catalyst supplied. All six roofs remain absent; live sky above template and starlight altitude must be checked.

## Rebuild
Run `node build_observatory.mjs` from this directory. Reads preserved V1 grid, applies deterministic revision, compares to V2 grid, writes gzip NBT and audit. Static revision and independent geometry assertions ran before commit; compressed NBT export/round-trip deferred to local Node run. See static-audit.json. No runtime deployment or game validation. Verify native block-state IDs against pin and terrain offset, light, sky, stairs, vault access, all multiblock builds and balance.
