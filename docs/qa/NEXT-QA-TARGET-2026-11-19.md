# Next QA target — 2026-11-19

Owner requests source preparation and Quick CI only. Never assemble, boot or publish from this change.

The independent `manifest/next-qa-target.json` is the intended exact mod-ID scope. Keep all 207 presently pinned mods, including restored Comforts and Colorful Hearts, plus 14 outstanding approved candidates. New dependencies must be inspected and explicitly added; no guessing hashes, silently dropping candidates, or labeling an incomplete scope comprehensive. Three pin restorations come from #371 and the previous immutable published snapshot f7aeb56a0f7b9c5957f922f5a397037f25207c76.

- #347 merged to Progression foundation; #344 combined source CI and subsequent promotion still required. Legacy Tyz / MXP cannot be shipped alongside their replacement merely because their pins are present: revise runtime selection, assembly and tests together before build.
- #351 armed Trader conflict: reconcile exact module and license-policy/test changes against integration; do not copy stale shared runtime scripts.
- #366 Bountiful: candidate compiler source only, requires reviewed generated pools and safe default handling before gameplay.
- #368 kitchen: additive completed tags and registry evidence can be separately reviewed; skillet adapter and Occult Cooking dependency closure not yet ready.
- Dimension prior holds: Deep Aether exact metadata has reported exclusive Minecraft upper bound; Aether and Liroth beta prerequisite chain needs verification. Reopened by owner for this next candidate, not unconditional loader compatibility.
- Candidate audits should download only approved exact files, compute hashes and inspect native metadata. Never commit binary payloads.
