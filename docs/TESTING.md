# Test ledger

**2026-11-13 bootstrap environment**
- GitHub repository created by user and first README commit verified: **PASS**.
- Java 21 in local container (`openjdk 21.0.12.1`): **PASS**.
- Local container `packwiz` binary: **NOT INSTALLED**.
- Local container network DNS for Minecraft/Modrinth/GitHub: **BLOCKED** (`Could not resolve host`).
- Packwiz TOML hash chain, manifest exclusions and CLI read: **PASS** GitHub Actions run 43545677885, job 132296314018.
- NeoForge 21.1.265 installer download and clean, loader-only dedicated-server startup: **PASS** GitHub Actions run 43545677885, job 132296313700; reached `Done` with script exit 0. This is a clean loader only, **not a modded-server boot**.
- B1 actual modded dedicated-server startup: **PASS** GitHub Actions run 43546287284, job 132298268556 (four SHA-512 pinned mod JARs fetched and verified, NeoForge 21.1.265 reached `Done`, clean exit status 0). No larger magic mods installed.
- Client startup / menu / fresh world: **NOT RUN** (no graphics-capable launcher).
- KubeJS script registration and recipe event validation: **NOT TESTED**; KubeJS JAR present in the B1 successful modded server, but the scripts are deliberately inert placeholders.
- B1 dependency resolution and download: **PASS** for exactly KubeJS, Rhino, Better Advanced Tooltips, ModernFix; Modrinth artifacts SHA-512-verified in CI run 43546287284. Other entries not resolved.
- Meta/QoL **modded-server runtime compatibility demonstrated** for the 23 server-loaded JARs only; core magic and graphical client are untested.

## Required subsequent test matrix
1. **Static**: `python3 scripts/validate.py`; packwiz index and hashes; machine manifest uniqueness and restrictions.
2. **Loader baseline dedicated server**: fetch exact 21.1.265 NeoForge installer; install on Java 21 into disposable dir; accept EULA for CI only; launch `nogui`, capture exit code and `Done` log line, then terminate cleanly.
3. **Core mods server**: pin exact 1.21.1 NeoForge mod files+all required libraries. Download via supported packwiz installer, start server, assert no missing dependencies, registry errors or data pack issues. Save logs.
4. **Client**: on suitable workstation import packwiz pack, launch main menu, create disposable world, check mod screens, shader/sky render, KubeJS events and absence of crashes.
5. **Gameplay integration**: validate each gate in survival and via negative bypass tests; confirm future renewable routes; ensure banned items have no acquisition paths.

Never reclassify step 1 as steps 2–5.

## B1 integration result — 2026-11-13 (local America/Phoenix date)
- Run: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43546287284
- Metadata job: **PASS**, offline SHA-256 chain and actual packwiz CLI.
- Modded-server job: **PASS**; downloaded `better-advanced-tooltips-2101.1.0-build.5.jar`, `kubejs-neoforge-2101.7.2-build.381.jar`, `modernfix-neoforge-5.27.27+mc1.21.1.jar`, `rhino-2101.2.7-build.85.jar`; verified SHA-512 each, reached dedicated server `Done`, shut down with status 0.
- Client GUI/menu: **NOT RUN**. KubeJS actual recipe execution: **NOT TESTED**. Multiplayer connectivity and custom quest APIs: **NOT RUN**.
- Baseline and modded stages are distinct; nothing in this run proves future Occultism/Ars/Iron's/Neo Vitae compatibility.

## B2 metadata/QoL integration result — 2026-11-13
- Workflow: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43548230954
- Automated resolution: **PASS**, 30 exact packwiz artifact pins, 0 requested slug failures. FTB pulled from exact official CurseForge IDs. Fuzzy-name false positives detected and rejected.
- Hash verification and dedicated-server startup: **PASS**, all 23 server-side JARs verified using source hashes (Modrinth and CurseForge), server reached `Done`, clean stop exit status 0.
- Seven client-only mods: **NOT CLIENT BOOT-TESTED**; no graphical session attempted.
- Quest content, KubeJS script events (other than empty placeholders), user joins and chunk claiming: **NOT TESTED**.
- Full resolved filenames, side and project IDs: [META-QOL-RESOLUTION.json](META-QOL-RESOLUTION.json).

## B3 stable gameplay + Tectonic (2026-11-13 local)
- Initial 55-server-JAR boot was **BLOCKED** by missing Iron's Lib and playerAnimator in [run 43549472015](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43549472015). Both were added with exact immutable artifact metadata.
- Repaired full **57-JAR dedicated-server** boot was **PASS** in [run 43549822627](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43549822627), reached `Done`, graceful stop exit 0.
- **Final release-JEI full-server regression PASS** in [run 43550199289](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43550199289): replaced beta-tagged JEI 19.70.0.469 with stable 19.64.0.455, all 57 JARs hash-verified, reached `Done`, exit 0. 64 total mods; seven client-only.
- Beta exception: playerAnimator 2.0.4, mandatory prerequisite of release-channel Iron's Spells; no beta gameplay mods. No Astral Sorcery, Botania, Draconic Evolution or beta Relics.
- **Client menu, new-world entry, Tectonic worldgen, spellcasting, scripts, recipes, quest UI, server connection: NOT TESTED**. ProjectE/Expansion Chaos Guardian gates **NOT IMPLEMENTED**; the current pack is a startup test build, not a balanced survival release.
- [Detailed B3 evidence](B3-TESTING.md), [dependency exception](B3-DEPENDENCY-EXCEPTIONS.json), [exact release metadata](B3-RESOLUTION.json).

## B4 reactor and experimental magic candidate — 2026-11-13 local
- [Staged CI run 43551344358](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43551344358): **PASS all four stages**, each from a disposable new dedicated server. Sequence: Reactor Rework 0.1.6; Astral 2.0.3 alpha + ObserverLib 1.10.3 alpha; Botania Revived 2.0.2 release; Draconic Evolution 3.1.4.633 beta + Brandon's Core 3.2.1.309 + auto-required CodeChicken Lib 4.6.1.529.
- 71 distinct packwiz mod pins in final working configuration. Hash verification, NeoForge 21.1.265 `Done` ready line and graceful exit succeeded for each stage.
- Nonblocking recurring log messages: Project Expansion claims missing `accesstransformer.cfg`; a mixin referring to an absent Quark GoldToolsHaveFortuneModule emits warnings. Neither prevented server readiness, but both warrant investigation before a production release.
- Graphical client / shaders / Astral sky effects / player-world entry / Botania mechanics / reactor energy balances / Draconic Chaos Guardian **NOT TESTED**. Endgame gates remain unimplemented; the pack is not production-ready for survival.
- [Detailed stage evidence](B4-TESTING.md) and [pinned artifacts](B4-RESOLUTION.json).

## B5 expanded user-approved magic roster (2026-11-13 local)
- 32 user-approved survey selections reconciled with working B4 branch; **31 new approved systems** plus **12 required libraries** were added (Botania Revived was already installed): final total **114 mod pins**.
- [Attempt 1, run 43552770652](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43552770652): all 102 selected server JARs hash-verified, startup blocked by missing **Lodestone** required by Malum. Added Lodestone 1.8.2 plus Ars Elemental and Gaze from their verified upstream projects.
- [Attempt 2, run 43553107811](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43553107811): all 105 server JARs hash-verified; loader reported missing **Caelus API** and **PAUCAL** for HexCast/Enigmatic Legacy+ and incompatible **Ars Elemancy 1.17** for Ars 'n Spells 3.3.5.
- [Attempt 3, run 43553444492](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43553444492): upgraded Ars Elemancy to 1.18.3 and installed Caelus 7.0.1 and PAUCAL 0.7.0. **PASS** 107/107 JAR content hashes, NeoForge 21.1.265 reached `Done`, clean shutdown exit 0.
- One new alpha-tagged required library **PAUCAL 0.7.0** and one beta-tagged optional-power addon **Psi: Tweaks and Additions** are boot-test candidates, not validated gameplay mechanics. Previously approved B4 alphas/betas remain.
- No hard-excluded or unapproved maturity-test systems installed. No graphical client, Tectonic worldgen gameplay, multiplayer connection, FTB Quests, KubeJS gameplay scripts, creative/survival balance or Chaos Guardian/ProjectE progression restrictions were verified.
- [B5 test ledger](B5-TESTING.md), [exact resolution report](B5-RESOLUTION.json), [survey approvals](MAGIC-MOD-SURVEY-DECISIONS.md).

## B6 food + dual skill systems + Pufferfish — 2026-11-13 local
- [**Full dedicated-server PASS run 43554705678**](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43554705678): **125 exact packwiz pins** (118 server/both-side, 7 client-only), 118 server JARs downloaded and hash-verified, Minecraft 1.21.1 / NeoForge 21.1.265 / Java 21 reached `Done` and shut down gracefully exit 0.
- **12 newly pinned**: four Pam's HarvestCraft 2 modules, Farmer's Delight, Aquaculture 2, Harvest with Ease, Cobweb, Pufferfish Skills (0.19.2 beta) and Attributes (0.8.3 stable), Improvable Skills 3 (21.0.2), HammerLib (21.0.17). **Aspect Alchemy removed**, because subsequently excluded.
- [**Prism Client Test ZIP produced and verified**](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43555071188), artifact [14097951209](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43555071188/artifacts/14097951209). Format contains `mmc-pack.json`, `instance.cfg`, MIT installer bootstrap and help text, but **no third-party Minecraft mod JARs**. Prism import and graphical launch not yet performed.
- Remaining: client boot/menu/world entry, Pufferfish UI/config, Improvable Skills Book of Skills, Pam's crop harvest, fruit tree/fish worldgen, Farmer's Delight recipes, Tectonic, Astral graphics with Embeddium, KubeJS gates/quests and server multiplayer. **ProjectE/Expansion Chaos Guardian locks not implemented**.

## B7 sorting-only addition / same dedicated-server stack
[Run 43558589888](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43558589888): **PASS**, exact 125-to-126 mod metadata pin update (new ReFoxed client-only), 118 server-side JARs fetched and hash-verified, NeoForge 1.21.1 / 21.1.265 reached `Done` and exited 0. ReFoxed exact `invtweaks-1.21.1-1.3.2.jar`, CurseForge project 976858 file 6407807, SHA1 verified in packwiz; Mouse Tweaks retained. The mod is client-only and not in server JAR count, so **middle-click chest sorting remains a client acceptance test**. [B7 Prism ZIP](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43558589888/artifacts/14098959586), immutable install snapshot `316d11a0626e5793ab7cc5d4b463891f80aa0fe6`. [Audit of other ATM10 QoL mods](ATM10-QOL-AUDIT.md); no other QoL candidates added.

## B8 QoL, Nature's Compass and seed Market — 2026-11-13 local
- **Dedicated-server PASS:** [Action 43560055987](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43560055987) — 140 packwiz pins, 127 server-side JARs downloaded and hash-verified, Minecraft 1.21.1 / NeoForge 21.1.265 / Java 21 reached ready `Done` and quit exit 0.
- 14 additional exact mod JAR pins: 11 selected features plus Balm/Bookshelf/Prickle; old B7 baseline remains untouched.
- **Client delivery metadata corrected**: beta-tagged Simple Backups was moved from `side = "server"` to `side = "both"` for Prism singleplayer, without changing the JAR, version, hash or server composition. [Final Prism ZIP build](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43560531515) passed packwiz hash validation and ZIP structure checks. [Import artifact](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43560531515/artifacts/14099663054) pins immutable revision `0c214d24793f2c9d912b4540e609bd04cb7b5e9a`.
- **Not tested:** actual GUI/JEI interactions, Market seed/sapling trades (especially magical), crop and Pam's integration, compass biome search, inventory shortcuts, backup creation/retention/restore, profiler and overlay interactions. Use a disposable world. In particular, **do not trust Simple Backups as sole data protection** while beta and unverified.

## B8 corrective removal candidate (2026-11-14)

- **Scope:** remove Re:Avaritia and Extended Crafting as explicitly excluded by owner. Reduce 140 pinned mods to **138** (previous 127 server/both-side -> 125 server/both-side, 13 client-only unchanged). Two packwiz metafiles removed; manifest and SHA-256 hash chain updated.
- **Status when authored:** metadata validation and fresh combined NeoForge dedicated-server test **pending** on `design/five-category-quest-chapter-inventory`. Do not mistake the earlier 140-mod B8 test for verification of this changed candidate.
- **Client:** Prism launch, existing-world migration, all recipes and endgame compatibility still pending. Back up worlds; removing mods from a world that has their items/blocks can lose contents. Test on a disposable world first.

## Live dual-vampire client tests (2026-11-14)

- **PASS (user-reported): Vampirism -> JAWR.** After Fangs/Sanguinare Vampiris, both states PRESENT once experimental config was enabled. Initial failure was caused by default-disabled settings.
- **PASS (user-reported): JAWR -> Vampirism.** Drinking a creative-spawned Lilith blood item initialized both native providers; the legitimate ritual is not tested.
- **PASS (user-reported): Vampirism feeding.** Suck Blood worked on a cow, increasing unified blood; chickens are not a valid native Vampirism blood source (zero blood).
- **PASS (user-reported): JAWR feeding.** JAWR Drink Blood via active ability bar/right-click and JAWR drink item increased shared blood. Owner also observed blood reduction after damage.
- **OPEN:** unified **spending**/double-debit, near-empty casts, rapid retries, fractional carries, native leveling/capacity, save/relogin, hunger, sun/garlic/fire, cure, death, multiplayer. [Focused next test instructions](VAMPIRE-CHASSIS-V1-TEST-PLAN.md).
- **Next-build desired-only client mod:** Colorful Hearts 10.5.9 for NeoForge MC 1.21.1, confirmed publication but *not yet installed*; must client-test high health and compatibility with JAWR/Vampirism/AppleSkin.

These results are first-person client test reports, **not** independently captured diagnostic readouts or automatically reproduced CI results. Do not promote the vampire bridge to release-ready.
# Dracolich model build — 2026-11-15

Linux container, Python 3 (Pillow/NumPy/Matplotlib/SciPy), OpenJDK 21.0.13;
Dragon Survival source `1.21.1`, version 2.0.74, immutable snapshot
`658c63608adc0194c9c9516804ecd66c1fd10112`. No pack/loader/mod upgrades.

Executed from repository root (source checkout and generated complete rig
are private sibling directories, not repository content):

```bash
python3 scripts/build_dracolich_mesh.py
python3 scripts/assemble_dracolich_model.py ../dragon-upstream --output ../dracolich-local-build
python3 scripts/preview_dracolich_model.py ../dracolich-local-build/dracolich.geo.json --output compat/transformations/art/dracolich
python3 scripts/check_dracolich_model.py ../dragon-upstream ../dracolich-local-build/dracolich.geo.json
```

PASS: 428 new cubes/54 attachments; 272 native bone fields/hierarchy preserved
exactly; 0 original cubes retained; UV bounds; all 527 animations across Center,
East, North, South, West have no new missing bone tracks. Changed rig and
unknown joint are rejected. Static review GEO vertices match the locally
assembled original mesh within 0.000006 model units. GLB is self-contained
and has correct buffers/triangle counts. Four-view actual mesh PNG inspected;
thin leg joint endpoints and native wing-edge hinge were corrected and rebuilt.

Native GeckoLib check, using Java 21 and existing GeckoLib 4.9.3 plus installed
Minecraft/NeoForge runtime library JARs on the classpath:

```bash
java -cp "$gecko_jar:$minecraft_runtime_classpath" scripts/CheckDracolichGeo.java ../dracolich-local-build/dracolich.geo.json 272 428
java -cp "$gecko_jar:$minecraft_runtime_classpath" scripts/CheckDracolichGeo.java compat/transformations/art/dracolich/dracolich-neutral-review.geo.json 42 386
```

Both PASS: actual library JSON deserialization, GeometryTree hierarchy and
BakedModelFactory cube baking, rather than Python JSON parsing alone.
The smaller static preview omits alternate small-wing/flipper geometry and
does not contain the upstream rig. Evidence: model directory `validation.json`.

NOT RUN: Minecraft client render, in-game animation/Molang, growth, armor,
items/rider, wing toggles, multiplayer, transformation/cure restoration, or a
new dedicated-server/full-pack boot. Model-only upload adds no runtime hook;
no claim of client integration. No world was opened or modified.
## Cave/Center runtime and skin implementation — 2026-11-15

Linux, Minecraft 1.21.1, NeoForge 21.1.265, Gradle 9.2.1, full Temurin
21.0.13+9 JDK, official CF Dragon Survival 420799/9539454 v2.0.74 and the
already-installed GeckoLib 4.9.3. No Minecraft/mod/loader upgrade. The minimal
container JRE lacked compiler release data; local compilation used a complete
JDK at the same Java 21.0.13 version. Development downloads/caches stayed private.

Executed from repository root:

```bash
gradle --no-daemon -p compat/transformations build
python3 scripts/build_dracolich_cave_skin.py
python3 scripts/verify_dracolich_cave_assets.py --jar compat/transformations/build/libs/tmm_transformations-0.1.0-dev.jar
java -cp "$check_classpath" scripts/CheckDracolichCave.java /official/DragonSurvival-1.21.1-v2.0.74-09.11.2026-all.jar /repository /private/cave_center.geo.json
python3 scripts/preview_dracolich_cave.py /private/cave_center.geo.json
```

PASS: compilation and **43 JUnit tests / 0 failures / 0 errors / 0 skipped**;
407 original cubes, 2,442 separate nonoverlapping UV rectangles, matching
1024x1024 RGBA diffuse/emissive atlas CRCs and packaged runtime files with no
extracted upstream assets. The actual runtime ResourceManager loader assembled
the official JAR's 272-bone rig and GeckoLib baked 407 cubes. Native rig fields
and attachment controls stayed intact. A changed native rig returned a harmless
marker rejected by selection. Native armor pose-copy checks passed for rotation,
position, scale, bone hiding and child visibility. Offline actual-atlas preview
and textured GLB were produced and visually inspected.

PASS, graphical **resource smoke only**: `gradle runClient` in an isolated
authenticated TCP Xvfb display with software OpenGL, minimal Dragon Survival +
GeckoLib + TMM development client. The new version-gated mixins loaded without
injection failures; the real client ResourceManager logged successful Cave/Center
assembly, then created its GUI and other texture atlases. The test harness
deliberately stopped the process after resource readiness. No world was entered.
Initial display attempts were blocked by unavailable UNIX sockets; the final
same-process TCP display completed the resource-loading smoke.

Observed baseline warnings: native `mass_buff` and `flapping_wings_standing`
animation parsing errors, unavailable headless audio and offline Mojang
authentication lookups. These did not block the completed resource reload and
are not counted as successful animation/skin display tests.

Dedicated-server startup evidence and exact module hash are in the Cave/Center
validation report. Remaining manual in-world gates: genuine acquisition/cure,
actual rendering/animation, growth, armor/items/riders, multiplayer tracking,
native resurrection and restoration of the original custom skin. No full
integration launcher ZIP or survival release was built by this work.
