# First-party MIT license policy — 2026-11-18

Owner approved MIT licensing for every custom first-party mod. The four current manifest-selected modules (Progression, MXP, Transformations, Trader's Vengence) each carry a complete MIT LICENSE, THIRD_PARTY_NOTICES.md, mod_license=MIT, and Gradle JAR inclusion. The existing NeoForge template expands mod_license into loader metadata.

Scope is original first-party code/documentation, not the whole modpack or independently licensed dependencies/assets. Copyright line follows the existing mod author credit, "Too Much Magic contributors" (2026); individual contributor rights and historical attribution must be verified for public distribution. Do not change external dependency licenses. No donor source has been imported in this PR. Future first-party modules must carry their own full MIT license and any actual upstream notices; use immutable provenance and preserve copyright and permission text. The unified Progression migration must retain these files, including the MXP notices.

The first-party build/paths/smoke verifier checks exact packaged LICENSE and THIRD_PARTY_NOTICES.md against module files and loader metadata, and the offline synthetic ZIP tests exercise valid and invalid cases. The manifest-selected quick workflow is the build verification path; no game boot is required. Reconcile the exact first-party licensing changes to dev/progression via its designated writer; do not replay an old module snapshot.

Create lava-fog donor research is recorded in the existing separate unified engine design PR #340; this change is licensing only.
