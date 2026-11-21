# First-party MIT license policy — 2026-11-18

Owner approved MIT licensing for every custom first-party mod. The four current manifest-selected modules (Progression, MXP, Transformations, Trader's Vengence) each carry a complete MIT LICENSE, THIRD_PARTY_NOTICES.md, mod_license=MIT, and Gradle JAR inclusion. The existing NeoForge template expands mod_license into loader metadata.

Scope is original first-party code/documentation, not the whole modpack or independently licensed dependencies/assets. Copyright line follows the existing mod author credit, "Too Much Magic contributors" (2026); individual contributor rights and historical attribution must be verified for public distribution. Do not change external dependency licenses. No donor source has been imported in this PR. Future first-party modules must carry their own full MIT license and any actual upstream notices; use immutable provenance and preserve copyright and permission text. The unified Progression migration must retain these files, including the MXP notices.

The first-party build/paths/smoke verifier checks exact packaged LICENSE and THIRD_PARTY_NOTICES.md against module files and loader metadata, and the offline synthetic ZIP tests exercise valid and invalid cases. The manifest-selected quick workflow is the build verification path; no game boot is required. Reconcile the exact first-party licensing changes to dev/progression via its designated writer; do not replay an old module snapshot.

Create lava-fog donor research is recorded in the existing separate unified engine design PR #340; this change is licensing only.

## Current integration reconciliation — 2026-11-19

The newly integrated fifth module, Economy, also ships MIT first-party code; preserve the original META-INF placement and verify its separate asset credits and Botania license, not MIT-relicense that art. This source merge adds a scoped third-party notice and verifies its exact contents. Current Trader source at #341 is MIT; pending armed Trader draft #351 has a distinct owner-approved GPL-3.0-only policy for that future revision and its own module-specific verification. Do not import that unmerged gameplay/art source or treat this older MIT decision as superseding the later approved GPL decision. The Progression writer still needs to reconcile notices into the active unified source.
