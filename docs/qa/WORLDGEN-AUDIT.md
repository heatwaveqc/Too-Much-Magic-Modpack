# World-generation audit — #746 (2026-11-25)

## Baseline and evidence

Use the known owner-tested Thanksgiving snapshot `721ea058294f838b9d7f145af427f5db228d248d` (MC 1.21.1, NeoForge 21.1.265, Java 21). Run [45550879036](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/45550879036); artifacts 14994161378 (logs and audit diagnostics, 428337 bytes) and 14993876779 (Prism package, 3337266 bytes) were confirmed unexpired. #708 records 255 installed JAR hashes, 251 third-party indexed pins and four first-party modules. Neither artifact retains the full installed dedicated server or region files. A more recent Prism dispatch for integration aeee4fc6155691a2a6fcf1e98712ed783ab5a5d3 was accepted, but success/frozen revision was not verified here. Do not substitute an advancing source head for the tested baseline.

First-pass roster triage (candidate contributors only, NOT confirmed structures or counts):

- Overworld magical landmarks and encounters: Ars Nouveau/Additions, Iron's Spells, Astral Sorcery, Goety, Hexerei, EvilCraft, Eidolon, Forbidden & Arcanus, Primal Magick, Malum, Feywild, Occultism, Theurgy, Witchery, Botania and Pastel.
- Adventure and settlement candidates: Dungeon Crawl, Graveyard, Cataclysm, Mowzie's Mobs, Aquamirae, Born in Chaos, Sons of Sins, Ribbits, SWEM, YUNG's Bridges and Extras. Vanilla replacements from YUNG's temples, dungeons, mineshafts, monuments, strongholds, fortresses, huts and End island must be examined as replacements, not blindly counted as additions.
- Separate dimensions: Aether, BetterEnd, Deeper and Darker, Eternal Starlight, Aurorian, Bumblezone, Twilight Forest, Voidscape. Check cross-dimensional eligibility rather than inferring from theme.
- Alex's Caves has separate indexed biome-generation configs. Exact baseline Forlorn Hollows and Magnetic Caves set distance-from-spawn 650 and 400 respectively, rarity offsets 4 and 0; not ordinary random-spread spacing and not counts. Inspect native placement.
- Tectonic is terrain context, not evidence of added structures. Include vanilla resources and both structure-like features and custom placement code in coverage review.

Exact initial pins verified in snapshot: Ars Nouveau 5.13.5, Iron's Spells 3.16.3, Goety 3.2.3, Dungeon Crawl 2.3.17, Graveyard 4.0.1. Actual archive content not inspected in this pass.

## Offline scanner

`python scripts/audit_worldgen_resources.py --pack-root <frozen-source> --mods-dir <installed-server/mods> --extra-jar <vanilla-data-jar> --data-root <frozen-source/kubejs> --snapshot 721ea058294f838b9d7f145af427f5db228d248d --output <new-report.json>`

The scanner reads raw data resources, nested JAR candidates, templates and feature IDs, verifies indexed mod pins against actual archives and records all duplicates. It never overwrites a report. Pass first-party archives and datapacks as needed; verify the source SHA and data pack load order separately. Raw counts are not effective registries or worldgen starts. Report missing pins, malformed resources, nested archive guards, custom code and pack overrides; no implicit completeness claim.

Synthetic fixture: `python -m unittest discover -s tests -p test_audit_worldgen_resources.py -v`.

The frozen pack already includes `kubejs/server_scripts/tmm_structure_registry_export.js`: its declared `tmm_structure_registry` command logs structure IDs using Registry.of. This is a useful reconciliation candidate, NOT a confirmed runtime command or structure-set export; verify exact KubeJS execution and permissions on the disposable audit server.

## Dedicated pilot — not dispatched

Owner explicitly authorized one audit build if necessary in live chat 2026-11-25. Do not reuse that for an unrelated full smoke/package/release. Current command console #291 supports only smoke and Prism, neither retains the world nor starts pregeneration. Do not bypass it with push triggers or a fake smoke. Dedicated manually authorized runner needs reviewed workflow/dispatch support. Prefer reconstructing the exact tested snapshot and pinning audit-only Chunk Pregenerator NeoForge 1.21 4.5.4 (CurseForge file 8748909); verify downloaded hash, native command tree, world preset and completion semantics before execution. Upstream page: https://www.curseforge.com/minecraft/mc-mods/chunkpregenerator/files/8748909. Published examples of modern pregen syntax are not exact-version runtime verification.

Proposed pilot: one fresh disposable seed, 1024x1024 measured blocks plus edge buffer, bounded time/disk/heap, explicit full-generation mode. Confirm Tectonic/vanilla preset, capture registry IDs and actual config hashes, save/stop cleanly, analyze actual full chunks and start records (not references), record measured coverage. Upload derived JSON/CSV and logs, not JARs or user worlds. Report failure/partial coverage; do not silently retry or publish a candidate. Expand to five 4096x4096 measured areas only after pilot rate/coverage and separate approval for further runs; nearby-structure proxy is not true visibility. No production installation or tuning.

Next implementation: review scanner fixtures, obtain real exact archive inventory, then region analyzer and reviewed audit-specific launcher. No measured density, server launch or completed pregeneration reported at this checkpoint.
