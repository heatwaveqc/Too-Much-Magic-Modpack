# Next QA target — 2026-11-19

Owner requests source preparation and Quick CI only. Never assemble, boot or publish from this change.

The independent `manifest/next-qa-target.json` is the intended exact mod-ID scope. Keep all 207 presently pinned mods, including restored Comforts and Colorful Hearts, plus 14 outstanding approved candidates. New dependencies must be inspected and explicitly added; no guessing hashes, silently dropping candidates, or labeling an incomplete scope comprehensive. Three pin restorations come from #371 and the previous immutable published snapshot f7aeb56a0f7b9c5957f922f5a397037f25207c76.

- #347 merged to Progression foundation; #344 combined source CI and subsequent promotion still required. Legacy Tyz / MXP cannot be shipped alongside their replacement merely because their pins are present: revise runtime selection, assembly and tests together before build.
- #351 armed Trader conflict: reconcile exact module and license-policy/test changes against integration; do not copy stale shared runtime scripts.
- #366 Bountiful: candidate compiler source only, requires reviewed generated pools and safe default handling before gameplay.
- #368 kitchen: additive completed tags and registry evidence can be separately reviewed; skillet adapter and Occult Cooking dependency closure not yet ready.
- Dimension prior holds: Deep Aether exact metadata has reported exclusive Minecraft upper bound; Aether and Liroth beta prerequisite chain needs verification. Reopened by owner for this next candidate, not unconditional loader compatibility.
- Candidate audits should download only approved exact files, compute hashes and inspect native metadata. Never commit binary payloads.

## Exact artifact audit and pin update

[Run 44617434376](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44617434376) downloaded and inspected all 14 candidates. Eleven compatible source pins now selected (CF hash SHA-1, Prefab MR SHA-512). Required known dependency versions: NeoForge 21.1.265; SWEM 1.6.14, GeckoLib 4.9.3, playerAnimator 2.0.4; KubeJS 2101.7.2-build.381, Theurgy 1.79.0; Twilight Forest 4.8.3345; CFB 21.1.24, Occultism 1.227.0, Balm 21.0.67; Aquaculture 2.7.22, KFF 5.12.0, FD 1.3.4; Cloth Config 15.0.140 (MR required izKINKFg same version as current CF pin). Unicorn Academy's actual native SWEM range [1.6.13,) permits selected release, but ABI and gameplay unverified. All 11 on both sides. No dependency verification or gameplay PASS claim. Existing confirmed pins remain.

Remaining holds: Aether requires owo-lib exact beta; Liroth requires Nuit client >= beta.6 and currently installed BetterEnd library variants need native ID/API compatibility audit. Deep Aether 1.1.5.1 exact binary declares Minecraft [1.21,1.21.1), incompatible with target. Audit prior 1.1.5 separately; do not patch upstream JAR. Add required dependencies to target explicitly. New target 223, exact pins 218. No server boot or Prism package.

## Second exact audit

Job [135892920025](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44618334095/job/135892920025) established Aether, owo-lib and Nuit exact binaries. Pinned Aether 1.5.10 and owo-lib 0.12.15.6-beta.1 on both sides, Nuit beta.6 on client only. Now 221/223 third-party target pins. Deep Aether 1.1.5 and 1.1.5.1 both exclude Minecraft 1.21.1 by native range; held. Liroth still requires confirming BCLib/WorldWeaver/WunderLib native IDs and GeckoLib provider version; exact existing library versions requested in next read-only audit. No gameplay or full build. Unified Progression merged to dev/progression via #344; separate pack runtime migration pending. Trader source reconciled on candidate.

## Combined source CI regression repair

Previous candidate 734beb9: six checks passed, three failed (Newbie required Prefab disabled; Economy Gradle omitted the MIT property expected by license verification; Windows converted the exact Caliber Arms donor license text). Replace stale Prefab expectation with exact owner target pin verification; declare Economy's existing MIT license in Gradle; preserve LF bytes for audited Caliber Arms notice/provenance files. Do not relax artifact or license validation. New CI required.
