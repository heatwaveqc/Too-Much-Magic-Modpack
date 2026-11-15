# Mod-comprehension methodology improvements collected

This is a proposal log, not a change to the operating contract. It records process refinements that surfaced while profiling Ars Nouveau 1.21.1-5.13.5. Adopt or fold them into the instructions only after review.

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

## Recommended small addition to the instructions

A compact reusable evidence syntax could prevent repeated caveats while preserving readability:

| Tag | Meaning |
| --- | --- |
| `[pin]` | TMM manifest/packwiz version and artifact identity |
| `[source: SHA]` | Exact source file inspected at immutable commit |
| `[docs: version]` | Upstream documentation or guide and covered version |
| `[pack-test: build/world]` | Reproducible in-pack observation |
| `[inference]` | Plausible conclusion awaiting direct confirmation |

Use tags only where they clarify provenance; the existing evidence ledger remains the canonical full citation list.

## Revision notes

- 2026-11-15 — Initial recommendations from the Ars Nouveau 5.13.5 profile. No operating rules changed.
