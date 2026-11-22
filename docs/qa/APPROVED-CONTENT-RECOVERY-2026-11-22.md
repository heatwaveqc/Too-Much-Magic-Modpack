# Approved Content Recovery — source-only integration, November 22, 2026

**Pull request:** [#524](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/524) into `test/full-integration`. **Owner:** live 2026-11-22 explicit request to integrate approved missing content, exclude Weeping Angels and other selected holds, verify dependencies and run lightweight CI. **Not** authorization for Integration Assembly, full server boot, Prism package, client/world test or release.

## Baseline and changes retained

The approved Experimental frozen source was `c56fb18ffc3b9f12f27317e7fb37f15c32576a8c`. Its owner-accepted four upgraded mod pins were previously promoted through [#511](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/511), and the chosen Unstable base `2e922405a01ded8893aef08cb06ae0278b9d96ef` contains **byte-for-byte identical** `mods/ars-elemental.pw.toml`, `mods/evilcraft.pw.toml`, `mods/goety.pw.toml` and `mods/neovitae.pw.toml`. The Unstable base additionally owns Theurgy quest PNGs, the feature/ore ledgers, integration lock, and other Unstable-only code/data. **No wholesale source reset, older directory restore, nor test snapshot overwrite was performed.**

## 19 newly indexed verified mod pins

| Slug | Intended gameplay content | Origin and dependencies |
|---|---|---|
| `mowzies-mobs` | Mowzie's Mobs | #399; existing GeckoLib |
| `aquamirae` | Aquamirae | #399; `fragmentum-neoforge`, existing GeckoLib |
| `fragmentum-neoforge` | Fragmentum | #399, Aquamirae's dependency |
| `sons-of-sins` | Sons of Sins | #399 |
| `the-graveyard` | The Graveyard | #399; `villagerstradingplus` |
| `villagerstradingplus` | VillagersTradingPlus | #399, Graveyard dependency |
| `aura-cascade-reimagined` | Aura Cascade Reimagined 0.2.1 beta | #425; existing Patchouli; beta caveat |
| `lets-do-herbalbrews` | HerbalBrews 1.1.4 | #398; CF CDN blocked but official Modrinth JAR verified with matching SHA1 |
| `aquaculture-delight` | Aquaculture Delight | #403; existing Aquaculture / Farmer's Delight |
| `born-in-chaos` | Born in Chaos | #403; owner lifted original hold |
| `dungeon-crawl` | Dungeon Crawl | #403; replaces false unresolved `roguelike-dungeons` catalog identity |
| `just-enough-professions` | Just Enough Professions | #403; **client** only; existing JEI |
| `ldlib2` | LDLib2 | #403; needed by Prefab Custom Addon (at least client) |
| `lets-do-brewery` | Brewery 2.1.13 | #403; Farm & Charm and **Foundation** |
| `lets-do-farm-and-charm` | Farm & Charm 1.1.29 | #403; newly discovered **Foundation** |
| `prefab-custom-addon` | Prefab Custom Addon 2.7.1 | #403; existing Prefab and LDLib2 |
| `yungs-better-end-island` | YUNG's Better End Island 3.1.2 | #403; existing YUNG's API 5.1.9; must test BetterEnd New Dawn overlap |
| `lets-do-foundation` | Foundation 1.1.0 | Official bytes CF project 1733606 / file 9735873; required by Brewery/Farm & Charm |
| `ars-conflux` | Ars Conflux 0.1.56 | Official bytes CF project 1737308 / file 9505585; preinstalled Ars Nouveau 5.13.5 is in declared [5.13.1,6) range |

Exactly 19 added Packwiz metafiles are indexed in `index.toml` with individually computed SHA256 content hashes; `pack.toml` index SHA256 was regenerated. `manifest/mods.json` enabled roster and `manifest/next-qa-target.json` required IDs reconciled from 222 to **241** selected active pins, with the unresolved `roguelike-dungeons` placeholder removed. The original 222 selected mod IDs and existing integration files were retained. **Source indexed ≠ assembled, server-booted, Prism-packaged, launched or gameplay accepted.**

## Evidence and CI

The [targeted JAR audit](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/workflows/approved-recovery-artifact-audit.yml) fetches exactly these publisher releases, verifies each pinned hash against downloaded bytes, checks ZIP integrity/native metadata and required deps, and does not assemble or boot Minecraft. Initial 17-JAR audit caught Foundation absent and HerbalBrews CF HTTP 403. After adding Foundation and Ars Conflux and using the publisher's official Modrinth HerbalBrews release, 19/19 selected hash/metadata audit passed ([Actions run 45072823157](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/45072823157)). Follow-up checks also examine Foundation duplication risk from existing Let's Do JARs.

On preliminary full indexed source HEAD `6f1f334dac6fe9fc067b5c65da5234afece6ce57`, all nine triggered **lightweight** PR workflows passed: Verify bootstrap, recovery exact-JAR audit, next QA target, new-player module/static, economy module, Bountiful static, branch lifecycle report, direct client links and CC Tweaked B14 static. The B14 workflow has a confusing name; its **server smoke step was explicitly SKIPPED**, verified from job step conclusions. Exact successor head must pass required quick CI again before merger. No new full Minecraft builds or runtime tests were performed.

## Required blocked/deferred intake, not silently omitted

- **Weeping Angels — OWNER-EXCLUDED** from this tranche. Source draft #392 remains incomplete for actual full dimension-ID blacklist, native teleport destinations, structure-only encounter traps, physical damage and FTB Chunks claim-aware griefing protection. No Angel JAR/config/biome override imported.
- **Aquamirae & Nouveau 0.1.3 — BLOCKED, not deselected.** Owner-approved #404/#406. Official NeoForge CurseForge file 9470029 returned HTTP403 from both publisher download endpoints in exact-file probe; no verified byte digest. Related **Ars Conflux is installed as an indexed dependency** for future activation. Retry via another **official** publisher source only; no fabricated hashes.
- **Cathobb Unified 64x v40 — separately approved, not included in this mod-only tranche.** #294 client resource-pack legal distribution, hash, Packwiz metadata and new-profile-only default enabling require exact validation. Do not misreport as installed.
- **Deep Aether, Liroth Revamped, Nuit and other explicitly held/unapproved mods — excluded.** Arcane Archives: Reborn and Twilight Forest Final Boss remake already indexed before this tranche and were not duplicated.

## Next owner-authorized gameplay queue (B/C)

- [ ] B: exact 241 selected mod binaries, dependency closure, config registration and no duplicate mod IDs; client and server agree on side filtering. **Requires separately authorized build**.
- [ ] C: Aquamirae + Fragmentum frozen ocean, worldgen/dungeon and combat; Graveyard structures/mobs and claim safety; Mowzie's bosses, Sons of Sins powers, Aura beta / magic book parity, Born in Chaos encounter balance, Dungeon Crawl dungeons, and Hunter/MXP/Monk/Dragon attribution and EMC abuses.
- [ ] C: Brewery/Farm & Charm/Foundation native brew/food functionality and interaction with Farmer's Delight/Pam's; HerbalBrews native tea/coffee; Aquaculture Delight food/recipes; JEP UI on client only.
- [ ] C: YUNG's Better End Island versus BetterEnd New Dawn fresh End and Dragon respawn behavior; Prefab Custom Addon / LDLib2 actual deployment of custom tower/houses, worldgen/permissions and preview.
- [ ] C: existing Experimental owner-reported passes only where exactly evidenced, do not generalize to all mods or all mechanics.

**Workflow ownership:** designated integration writer reviews/merges one coherent scope; donor PRs #399/#425/#398/#403/#406 retain their recorded history and should be linked as superseded only after the content is verifiably reachable. Full build and gameplay QA remain independently owner-authorized.

## Additional required upgrade: Furniture 1.1.11 (replaces one existing pin)

Exact publisher-JAR audit found the existing Furniture 1.1.10 package contains nested `META-INF/jars/foundation-neoforge-1.0.7.jar`. With standalone Foundation 1.1.0 (mandatory for newly approved Brewery/Farm & Charm), the old version poses a duplicate-mod/API hazard. Therefore PR #524 upgrades that **one existing pin** to native NeoForge Furniture 1.1.11 from official Modrinth project `3JQDJrYW` version `vVtpdidG`, SHA512 `acb631153c701957662662436820441d65e048e2038eb8c39e434827785fa766f7e433dc5c9245007fc1253680520f3533493daab08f2fc577b8dfe332e0da7e`. Exact downloaded JAR integrity, declared native `furniture` mod ID and required `foundation [1.1.0,)` were verified; **no nested jars**. The side remains both, no gameplay redesign. Existing Beachparty 2.1.5 official JAR has no nested Foundation. [Audit action](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/45073468430) traced the risk and the owner-reviewed source correction. Game compatibility still requires separately authorized gameplay QA.
