# Mod-comprehension methodology improvements collected

This log originated as **proposals** from the Ars Nouveau 1.21.1-5.13.5 first pass. **Decision 2026-11-15:** all eight are adopted into [Work instructions](WORK-CHAT-INSTRUCTIONS.md), [templates](templates/MOD-PROFILE.md) and the [quest walkthrough standard](../QUEST-WALKTHROUGH-STANDARD.md), with the scope refinements below. The numbered prose records the original suggestions; normative rules live in those linked documents.

### Adoption decisions and refinements

| # | Decision | Scope |
| --- | --- | --- |
| 1 | Adopted | Exact artifact hash + immutable source SHA; label source/JAR equivalence unknown until actually compared |
| 2 | Adopted | Compact per-claim evidence tags and subsystem coverage; retain source ledger |
| 3 | Adopted, foundational | Typed I/O with consumed/remainder, output position, triggers, cadence and API |
| 4 | Adopted selectively | Positive and boundary/negative tests **required for consequential** compatibility/economy/gates, not trivial low-risk behavior |
| 5 | Adopted | Trigger-aware renewability, code formulas separate from measured in-game throughput |
| 6 | Adopted | Evidence version/gap and hash-based stale review; automation is a future enhancement, not implemented |
| 7 | Adopted | Separate per-addon coverage, both-side evidence for any claimed bridge |
| 8 | Adopted | Teach actual operation, don't claim item ownership proves behavior; honest detectors/fallbacks |

**Additional approved requirements:** concise player-value summary (*five standout uses / three early unlocks / three integration leads* where applicable), a **subsystem coverage dashboard**, and a **complete start-to-finish progression/guidebook/quest coverage matrix**. A full mod profile is not source-complete with mandatory tier or build-guide gaps. Every mod's quest chapter is intended to function as a rewarded how-to guide in combination with JEI and the mod's own guidebook.

### Additional improvement from the Ars pilot reconciliation

The Ars source review exposed a recurring audit trap: counting quests or cataloging every recipe can hide the difference between a mandatory gate and a player-selected option. A chapter should teach how to choose and complete representative valid paths while making optional breadth discoverable in JEI/guide references. The pilot audit should also treat each quest's description, task detector, reward, and corresponding progression row as separate evidence fields; a matching item task cannot stand in for the operation described in prose.

| # | Decision | Scope |
| --- | --- | --- |
| 9 | Adopted | Track **mandatory gates vs optional choices**. Teach the progression rule and how to pursue a valid branch; do not make every optional recipe a quest milestone. Reconcile each quest's action claim against its task predicate and the profile's stage map. |
| 10 | Adopted | If a project lacks a release tag, inspect immutable history for the commit where its version metadata changes to the pinned version. Record that commit and its declared dependency versions; this can resolve addon source identity without pretending it proves the downloaded JAR or compatibility. |

## 1. Pin source snapshots as carefully as pack artifacts

The TMM packwiz pin identifies a CurseForge file and SHA-1, while the upstream Ars source's `main` branch at research time declares the same `mod_version=5.13.5`. That is a strong version match, but a `main` URL is mutable and does not prove the JAR was built from that exact commit.

**Suggested improvement:** record both the artifact identity and the upstream source commit SHA (or release tag) in every full profile. Use commit permalinks for code evidence. If the source commit or source JAR cannot be matched to the pinned binary, say “version-aligned source, artifact not inspected” and retain an artifact-level unknown. This prevents a version string from being mistaken for reproducibility.

## 2. Split evidence and confidence per claim, not only per profile

One profile can have pinned metadata, code-inspected source values, guidebook descriptions, a 5.11 community guide, and no in-pack test. A single status cannot express all of those distinctions; a broad “source-complete” can sound stronger than the weakest important subsystem.

**Suggested improvement:** keep the current overall coverage state, but classify each material claim or subsystem with a short evidence tag such as `[pin]`, `[source: commit]`, `[guide: version]`, `[pack-test: build/world]`, or `[inference]`. For high-risk claims (resource value, item consumption, boss drops, capability type), also state the unresolved verification step next to the claim. Prefer a small tag vocabulary over duplicating evidence in prose.

## 3. Standardize capability I/O as a typed contract

The key Ars finding was that “mana,” “Source,” and a Source Jar can be casually conflated even though player mana and machine Source serve different roles and Source is not a fluid. Similar ambiguity affects item entities vs inventory items, XP orbs vs experience gems, and ritual item offerings vs generic inputs.

**Suggested improvement:** give every subsystem the same compact I/O fields: input type and registry/tag/event, quantity or formula, consumed/remainder behavior, output type, output location, cadence/trigger, storage/range, and automation interface. Use an explicit value of “not established” for unverified channels. This makes later cross-mod comparisons mechanical instead of semantic.

## 4. Use boundary tests that can falsify a compatibility claim

A quest or item icon can establish that a player crafted a block, but not that a Sourcelink generated Source, a spell cast successfully, or a mob automation produced the expected reward. Likewise, two mods supporting recipes does not establish they share a fluid, energy, entity or item capability.

**Suggested improvement:** for each compatibility lead, write at least one minimal positive test and one boundary/negative test. Examples: confirm Ars Source changes while a known matching Sourcelink trigger occurs, then check that an unrelated trigger does not; confirm an Ars Source Jar is not accepted by a fluid handler; test Drygmy output on one ordinary and one gated entity. Record the observable value/UI/item change, not simply “works.”

## 5. Make passive and renewable loops trigger-aware

An event-driven producer can be renewable without being continuously productive. Crop growth, tree growth, breeding, mob death, item feeding and potion consumption have different cadence, costs and exploit potential. A source formula alone does not reveal output per minute or progression balance.

**Suggested improvement:** label every renewable route by trigger class (`event`, `consumed item`, `consumed fluid`, `timer`, `entity state`, or `world interaction`). When usefulness or balance depends on throughput, measure it in-game under declared conditions (world tick, chunk state, number of triggers, input recipe). Keep code-derived formula separate from measured rate. For entity-based outputs, record the exact entity IDs and drops in a whitelist/blacklist audit before suggesting progression use.

## 6. Track source age and version drift explicitly

The explanatory Ars guide available during this pass described 5.11, while TMM pins 5.13.5. Current source code and the generated Notebook can fill some gaps, but the mismatch is material and easy to miss when several versioned pages are cited.

**Suggested improvement:** add a short `Evidence version` field to each cited source, and a `Version gap` note where sources are older/newer than the pin. Consider an index check that marks profiles stale when their exact packwiz artifact hash changes. A version-only comparison misses same-version repacks; hash changes should prompt at least a targeted review.

## 7. Separate parent-system and addon scopes before claiming integration

Ars has several pinned addons, two of which are intentionally positioned as bridges (Ars Ocultas and Ars 'n Spells). The parent profile can enumerate them, but their presence alone does not establish the content, exact input/output mapping, configuration, or compatible version behavior.

**Suggested improvement:** retain separate profile status for each addon. In a cross-mod report, require two-sided evidence: inspect the addon's own recipe/code and the target mod's actual accepted input or event. Add the interaction report only after this evidence, or label it `candidate` with a concrete test instead of describing it as working.

## 8. Connect quest milestones to actual detectable behavior

Acquiring a Source Jar is not the same as generating Source. Crafting a glyph is not necessarily a successful cast. Automation setup items do not show that a farm is producing output. Those distinctions matter for quest design and avoid misleading completion criteria.

**Suggested improvement:** record, for every proposed quest milestone, (a) intended player learning, (b) the actual observable criterion, (c) whether FTB Quests can detect it natively, and (d) a manual/checklist fallback if not. Keep item ownership and system operation as separate milestone types. Don't write implementation-specific task IDs until the live FTB task schema/version is checked.

## Adopted evidence syntax

The compact evidence syntax below is now normative in the work instructions and profile template; use it to avoid repeated caveats while preserving readability:

| Tag | Meaning |
| --- | --- |
| `[pin]` | TMM manifest/packwiz version and artifact identity |
| `[source: SHA]` | Exact source file inspected at immutable commit |
| `[docs: version]` | Upstream documentation or guide and covered version |
| `[pack-test: build/world]` | Reproducible in-pack observation |
| `[inference]` | Plausible conclusion awaiting direct confirmation |

Use tags only where they clarify provenance; the existing evidence ledger remains the canonical full citation list.

## Revision notes

- 2026-11-15 — Initial recommendations from Ars Nouveau 5.13.5.
- 2026-11-15 — All eight adopted with scoped testing requirements; full progression, guidebook onboarding, player-value summaries and subsystem coverage added to the operating rules.
- 2026-11-15 — Added the choice-boundary rule from the Ars walkthrough audit: mark required capability gates separately from optional recipes/branches, teach discovery through JEI/guide references, and keep quest action, detector and stage mapping independently auditable.
- 2026-11-15 — Adopted the evidence syntax proposed above in the work instructions and profile template.
- 2026-11-15 — Added a release-source discovery step: use version-file/Gradle history to find immutable commits for pinned addon versions when tags are missing. This found Ars Additions 21.3.0 (`91f102a90dc058cf40e4eac5a67a881e48b856b4`) and Ars Elemental 0.7.10.4 (`2c433b6a4e6ff5c3b314b5f5442881e0ad2dd0c2`) source snapshots; both still declare older Ars dependencies than TMM.
