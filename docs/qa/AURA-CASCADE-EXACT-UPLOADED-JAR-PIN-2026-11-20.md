# Aura Cascade Reimagined 0.2.1+1.21.1 — exact uploaded NeoForge JAR pin evidence

**Decision:** owner expressly approved Aura Cascade Reimagined with full native books/bookshelves, fairies, processing, rituals, enchanter, inventory and FE interoperability for next comprehensive unstable inclusion; see [#387](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/387) and source QA [draft #421](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/421). **This branch only stages an isolated, unindexed `mods/aura-cascade-reimagined.pw.toml`; the integration writer must reconcile shared `index.toml`, `manifest/mods.json`, source locks and QA register. No assembly/server/client/Prism QA run or authorized here.**

## Artifact checks, 2026-11-20

Exact user-uploaded file `aura-cascade-neoforge-0.2.1+1.21.1.jar`, **1,572,301 bytes**, 1448 ZIP entries, no zip integrity failure on `zipfile.testzip()`.

- **SHA-1 computed locally from actual bytes:** `4cfde7f28e7d691fa771d91c23c79e8f2297e6a5`.
- **SHA-256 computed locally from actual bytes:** `f2bc9d9c6b73e59004f5e582196f850f1e4bfc5bd0515a299a5201ead0605f0b`.
- Bundled `META-INF/neoforge.mods.toml` identifies JavaFML loader `[4,)`, **mod ID `aura`**, `version=0.2.1+1.21.1`, **MIT license**, required `neoforge [21.1.252,21.2)`, Minecraft `[1.21.1]`, required `patchouli [1.21.1-93-NEOFORGE,)` BOTH sides. `aura.mixins.json` and `data/aura/patchouli_books/encyclopedia_aura/book.json` exist. Pack's provisional target 21.1.265 and existing Patchouli 93 should satisfy requirements, pending actual integrated startup/loader checks.
- Metadata agrees with published **NeoForge** file [CurseForge project **1519395**, file **9005415**](https://www.curseforge.com/minecraft/mc-mods/aura-cascade-reimagined/files/9005415) by version+filename. Different **Fabric** file 8961227 and its upstream SHA-256 are inapplicable and MUST NOT be used.
- **Provenance caveat:** the bytes were supplied by the owner as a conversation attachment; a hash computed over them is direct integrity evidence and an exact pin for those bytes, but **not independent verification that CurseForge file 9005415 serves identical bytes**. At final integration attempt, compare provider-authoritative JAR SHA against this pin, and fail closed/record blockage if mismatch. Do not commit or redistribute the JAR.

## Next writer / QA actions

1. Pin only exactly this `mods/aura-cascade-reimagined.pw.toml` path from this branch, avoiding overlapping feature-source branches.
2. Integrator validates the CurseForge binary against the computed SHA, reconciles the canonical roster/index together with all ready approved mods and commits the immutable donor source. No silent omission; if provider artifact differs, mark BLOCKED with evidence.
3. Read-only normal validation after indexing: Packwiz metadata/project/version chain and repository quick CI/Integration Preflight. **No unattended full server boot, Prism package or comprehensive game build.**
4. Future **owner-authorized** gameplay: eight aura-color transfer conservation, enchantment and magic power processing, flux/FE bridges, fairies/juveniles and breeding, book/bookshelf/lectern storage and claims, reload persistence, item duplication, high throughput ticking, ProjectE/Replication conversions.
