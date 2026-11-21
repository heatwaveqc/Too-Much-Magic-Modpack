# Personal Astral Night Sight × More Darkness — implementation pilot

**Owner-locked design:** [#264](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/264), Nov 20, 2026.
**Development branch:** feature/progression-astral-darkness-vision-20261120 (isolated; PR to dev/progression).
**Target runtime:** Minecraft 1.21.1, NeoForge, Astral Sorcery 2.0.3.59 and More Darkness 2.0.1+1.21.1 (both independently optional at Java compile time). No new standalone mod or global light-source changes.

## Intended visual contract

1. Newbie (personal FTB opt-out mode): default/bright visibility, no More Darkness dark-sight distance crush; player's own brightness/gamma settings remain untouched.
2. Regular SHIMMER / no Astral: native More Darkness at night and in caves, with moon-phase lighting and adaptation.
3. Illumination: partly mitigated moonlight, still noticeably darker than vanilla.
4. Resonance: approximately vanilla nighttime view; full-moon pixel math equals vanilla, new moon remains a little dimmer. Remove 8-block artificial distance veil.
5. Luminance: natural skylight better than vanilla; distant silhouettes readable.
6. Radiance: much stronger natural night vision but **not** fullbright and **not** vanilla Night Vision; no additional actual light emitted.
7. Actual Night Vision (including Astral Liquid Starlight and Akasha lumen flask) retains native real effect semantics, including underground sight and More Darkness bypass; Conduit similarly preserved.

No Astral stage creates light where world skylight is zero. Underground still needs lighting, and player blindness/darkness effects suppress the supernatural projection. Newbies with pre-earned Astral research regain the correct tier immediately when graduating. A joined FTB team does not confer personal tiers.

**Calibration coefficients in code are provisional visual QA defaults**, not numeric owner design locks. Tests capture ordering/invariants, not player-perceived luminance.

## Engine implementation boundaries

- Server: optional reflection to Astral's public `ResearchManager.getProgress(player, LogicalSide.SERVER)` and personal `PlayerProgress.getTierReached()`, yielding enum SHIMMER / ILLUMINATION / RESONANCE / LUMINANCE / RADIANCE. Only server reads authoritative tier; no advancement OR recipe proxy, no client grant. If no mod or incompatibility, Regular defaults to native darkness.
- Already existing `VisionRuntime` reconciliation sends personal stage in revised one-way `VisionNetwork.State` packet; `ClientVision` retains weak local-player ownership and 40-tick expiry. Newbie graduation calls forced reconciliation.
- Client: optional `@Pseudo` mixins target public-source More Darkness `DarknessCalculator.darken(III)I` *after* original evaluation and `PerceptionEffects`' separate distance veil. This uses original vanilla pixel, vendor-modified pixel, lightmap skylight row, client moon brightness/time, and server-projected tier. No vendor file modification, no persistent gamma/config mutation, no global world-light adjustment, and no requirement that dedicated servers install More Darkness.
- Unlike global `minimumMoonBrightness`, this approach can enhance above vanilla at Luminance/Radiance without changing More Darkness for other players. It does not force all clients' render settings.
- There is intentionally no directly declared optional NeoForge mod dependency: a missing client mod leaves the existing vanilla lightmap and vision system alone. Mixin annotations/config are optional; exact release JAR method descriptor must be verified on the packaged 2.0.1 binary.

## Source evidence and known uncertainty

- More Darkness 1.21.1 public source at https://github.com/payangar-dev/more-darkness/blob/1.21.1/common/src/main/java/com/payangar/moredarkness/darkness/DarknessCalculator.java declares static `darken(int,int,int)` and post-transform native lightmap. `PerceptionEffects` separately applies the radius/depth crush. **The public branch's gradle.properties says 0.1.0 rather than the distributed 2.0.1; there is NO exact-binary parity proof yet.**
- Astral 1.21.1 source at https://github.com/HellFirePvP/AstralSorcery/blob/1.21.1/src/main/java/hellfirepvp/astralsorcery/common/research/ResearchManager.java and ResearchTier.java enumerates five personal tiers, server research manager and ordered thresholds. Its source is all-rights-reserved and used **only as an API reference**; no code/assets copied.
- More Darkness source is MIT; adapter code and tests are original, not a source transplant. Reinspect exact distributed binary and licenses during integration.

## QA / integration gate

1. **Module compile + pure policy unit tests** on current branch; main-only checks do not imply a client PASS.
2. Verify exact installed More Darkness 2.0.1 JAR binary method descriptors, mixin success/no missing-target class errors and Astral 2.0.3.59 reflection API before declaring runtime readiness. Must install/pin More Darkness+client YACL via integration writer; this branch deliberately does not touch packwiz/manifest/index.
3. One client, two players on same team but different personal Newbie/Regular and Astral tiers; check immediate graduation, tier progression, relog, respawn, different worlds/servers and dimension transfer. Verify server fail-closed if Astral or More Darkness absent.
4. Capture identical scene screenshots at new/full moon, sunrise/dusk and daytime for all stages; compare daytime unaffected. Test natural skylight 0 and 1, caves, surface, artificial torch light, sky/weather, blindness/darkness, real Night Vision from Liquid Starlight, Akasha flask and other providers; check no permanent potion or extra spawning light. Check high-tier distance veil suppression.
5. Measure shader/Embeddium/Astral renderer compatibility, FPS and More Darkness YACL config options; retain local player brightness settings. Record visual accept/re-tune stage coefficients only after real client testing.
6. Test compiled mod JAR on dedicated server **without More Darkness** and, if available, without Astral; no client-only classload, mod requirement or packet failure.
7. Rename PR #424 converts `tmm_progression`/Java package to `tyr_progression`: designated Progression writer must translate all new paths, mod id, packet identity and mixin config references during reconciliation. Housing PR #418 also edits NewbieMode; preserve both the Starter Home registration and immediate vision reconciliation. No direct write to shared dev/progression or test/full-integration.

**Status:** source pilot; no owner authorization for Integration Assembly, full server boot, Prism packaging, client smoke or gameplay PASS. The next authorized comprehensive test must not silently omit approved issue #264, but source release readiness depends on above parity and compile checks.
