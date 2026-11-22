# Friendship Garden — next authorized integration build checklist (issue #428)

**Selected first-party custom mod:** `tmm_friendship_garden`, source `compat/friendship-garden` added to `manifest/first-party-runtime.json`; package in next owner-authorized assembly with other selected first-party JARs. **Not** a Packwiz third-party JAR. Original policy donor [#453](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/453); runtime PR [#537](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/537). Gameplay code/asset status should follow exact source, not draft policy notes.

## A / source readiness (CI)

- [ ] Exact-head NeoForge 21.1.265 Java compile, bundled native `tmm_friendship_garden` metadata, MIT LICENSE and third-party notices verified.
- [ ] Original policy JUnit cases pass, plus packaging-inventory count and frozen source manifest validation.
- [ ] New module included by the first-party runtime manifest and smoke module JAR inventory; no duplicate `mods/*.pw.toml` or third-party asset redistribution.
- [ ] Optional schematic/static tests for recipe data JSON and blockstates, client/server physical-dist event handler separation.

## B / commands and reproducible survival actions (on next *authorized* build)

- [ ] `/friendshipgarden harmful enable|disable` recipient choice (default off) persists suitably, no unauthorized other-player toggle.
- [ ] Two players craft with bonemeal + rotten flesh + different vanilla/modded short/tall decorative flowers; owner UUID/name always **actual crafter** on normal and shift-click, no pickup/placer rebinding.
- [ ] Automated crafting output is unbound and cannot be placed; excluded functional plants do not craft.
- [ ] Flower trade/gift, silk-like break/drop and replant preserve original species, name/UUID, short/tall and current mourning.
- [ ] Breaking *upper or lower* of tall flower removes both halves, returns exactly one bound flower, never duplicates or leaves orphan.
- [ ] Log in/out and cross-dimension state: original flower model when crafter online, generic bud when offline, persisted across save/reload/chunk unload. Original modded renderer and shading work.
- [ ] Owner death sends bounded leaf/petal particles, five-minute wilt remains through immediate respawn/relog, then correct new state; hardcore permanent wilt. No spam from many flowers.
- [ ] Splash a vanilla beneficial potion near flower (not direct block collision), both nearby and across dimensions: correct full effect; two owners near same splash each receive it; repeated flowers of one owner do not duplicate.
- [ ] Harmful potion: off by default even with PvP enabled, on only for recipient's own explicit opt-in **and** server PvP; blocklisted potion/effect IDs suppress; modded special brews excluded by default; offline/dead/mourning recipients excluded.
- [ ] No accidental conventional potion suppression or extra refunds.

## C / visual and multiplayer quality

- [ ] Original flower species appearance (including tall modded flowers) and four generic short/tall alternate appearances readable and free of missing models / purple-black placeholders.
- [ ] Impact radius respects splash coverage in edges/walls; messages/tooltips identify correct bound player without revealing UUID outside advanced tooltip.
- [ ] TPS/CPU cost with hundreds of flowers, chunk unload/reload and multi-player login storms; no force-loaded chunks, no stale event registry, no all-flower polling.

**Test level currently:** SOURCE CHANGES IN REVIEW; **no full Minecraft boot or new package authorized**. If any Type A fails, leave PR draft and feature out of the next-build manifest until resolved.
