# Consolidated Progression skill-tree designs — 2026-11-24

Design evidence only; not loadable Path data or balance approval. [v15 catalog](REMAINING-PATHS-100PT-FIRST-PASS.json) / [summary](REMAINING-PATHS-100PT-FIRST-PASS.md); [Monk blueprint](MONK-FIVE-LANE-1000PT-DRAFT.json) / [summary](MONK-FIVE-LANE-1000PT-DRAFT.md). Earlier baseline drafts and pilots are unchanged. Per-Path specifications remain beside these files.

## Locked constraints and separate decisions

- Unit cost per purchased rank; specialist ordinary mastery target 100, Monk 1,000. Core is independent of Path and MXP is unavailable as Newbie; Core 100-point allocation remains tentative. Native free abilities remain free; one shared engine, config-only Paths, passive/contextual preference.
- Lich 100 ordinary points plus another 100 in same tree only for genuine dual-Lich. #611 approved Otherworld Sight capability (no barehand Iesnium); later #526 proposed one-point placement, Stride 4→3 to preserve budget; placement not newly approved. Engine #629/runtime acceptance separate.
- Hunter shared 62 + one training 8 + one prey 30 is a draft; confirm exclusivity/respec and Fae taxonomy. Sea Dragon extreme Luck 10 ranks. Monk five-lane exact allocation and numbers proposed; Focus port must preserve max +25% movement. No inert ranks.
- All numerical effects, new candidate skills, stage gates and exact lane allocations remain subject to source and owner review as appropriate. Archlich summon caps, chained hits, damage redirect, death-wave and rescue bonuses need distinct review. No second rescue or resource wallet. Bare Zerker approved Fury/Too Angry to Die behavior takes precedence. Species, diet and native provider checks are engineering tasks, not owner votes.
- Sapient Slime, Sexy Demon, Dryad and Spawn remain concept shells. Consult live #446/#452/#468 and later Lilith offering work before implementation. No blanket approval.

## Validated design budgets

- core: 27 paid options; 100 raw unit ranks.
- cave_dragon: 25 paid options; 100 raw unit ranks.
- sea_dragon: 22 paid options; 100 raw unit ranks.
- forest_dragon: 26 paid options; 100 raw unit ranks.
- vampire: 19 paid options; 100 raw unit ranks.
- werewolf: 20 paid options; 100 raw unit ranks.
- lich: 42 paid options; 200 raw unit ranks (100 ordinary + 100 gated).
- hunter: 57 paid options; 498 raw unit ranks (one legal build 100).
- gilded: 22 paid options; 100 raw unit ranks.
- bare_zerker: 22 paid options; 100 raw unit ranks.
- Monk: 38 paid nodes, 1,000 unit ranks.
- JSON parse, unique IDs per tree, unit prices, rank array lengths, ordinary and Archlich budgets, Hunter legal alternatives, Monk total checked. Not native source/QA acceptance.
- Next writer: designated Progression integrator for docs-only acceptance; owner decisions in existing #431 and linked issues; executable missing adapters in shared Progression; later per-Path data PRs. Preserve pending B/C.

## Exact donors

- [#495](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/495): [6e21fc417357a324f987733b7519a5d5b027e7f4](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/commit/6e21fc417357a324f987733b7519a5d5b027e7f4)
- [#514](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/514): [a04a09844f1c5fa79b68d1da41a2ecfa80dac133](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/commit/a04a09844f1c5fa79b68d1da41a2ecfa80dac133)
- [#518](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/518): [dcdcc584e9e48b388c1b4d64f4c6c1835df830df](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/commit/dcdcc584e9e48b388c1b4d64f4c6c1835df830df)
- [#520](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/520): [644fb2c04816ee7027e62ec978411fb69d2b8e86](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/commit/644fb2c04816ee7027e62ec978411fb69d2b8e86)
- [#526](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/526): [5ac6e15d42cec646d3f60059549ab5477e3c49ff](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/commit/5ac6e15d42cec646d3f60059549ab5477e3c49ff)
- [#530](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/530): [b3d573023e496b3ae64fe9571f943e1df0d8341f](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/commit/b3d573023e496b3ae64fe9571f943e1df0d8341f)
- [#536](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/536): [0e11a9b7ba282ec4c18fef7013ec4379413680fb](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/commit/0e11a9b7ba282ec4c18fef7013ec4379413680fb)
- [#547](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/547): [1fc42ed969848747456927f65ffb98a9f694ca60](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/commit/1fc42ed969848747456927f65ffb98a9f694ca60)
- [#553](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/553): [fdbe7fcee8f33e52ee3c281418e2cf4e40dbf8bb](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/commit/fdbe7fcee8f33e52ee3c281418e2cf4e40dbf8bb)
- [#554](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/554): [dc78f67c8c79e0b0bcaa19d1b35b4060ab02e218](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/commit/dc78f67c8c79e0b0bcaa19d1b35b4060ab02e218)
- [#558](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/558): [52bcd8841df3aa7e2c0329fc4007d6ac398a8228](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/commit/52bcd8841df3aa7e2c0329fc4007d6ac398a8228)
- [#478](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/478): [03c7f2000f2573a1644434f2d642a1fdc7e70f9d](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/commit/03c7f2000f2573a1644434f2d642a1fdc7e70f9d)

Tip #558 carries the cumulative stack except two late #526 commits; merged their ordinary Lich correction with the independent Archlich extension. Eight per-Path docs and Monk pair are exact source blobs. New isolated consolidation PR because old stack diverged from dev/progression; no old engine history or live config imported. Close donors only after exact preservation and dependent-base verification; no branch deletion.
