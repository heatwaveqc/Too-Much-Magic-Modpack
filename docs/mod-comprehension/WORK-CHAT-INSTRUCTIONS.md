# Work operating instructions — Mod Comprehension Project

## Invocation and objective

The user can say **"Work on the mod comprehension project."** Treat this as standing permission to **research, write or improve the knowledge-base Markdown, maintain its index and open a reviewable GitHub PR**. Do not ask which mod to pick unless every reasonable route is blocked. This does **not** authorize modifying the live pack, upgrading mods, implementing compatibility changes, or authoring/publishing quests.

**Outcome requirement:** A completed gameplay profile must explain **the full mod progression sufficiently to author a rewarded, start-to-end quest walkthrough**. Quests, JEI and the mod's in-game guide together must be everything a new player needs; do not leave essential steps dependent on an external wiki. Read [QUEST-WALKTHROUGH-STANDARD](../QUEST-WALKTHROUGH-STANDARD.md) before any mod profile or quest handoff.

Project repository: `heatwaveqc/Too-Much-Magic-Modpack`. The current working integration stream is `test/full-integration`, not the minimal `main` bootstrap branch. Check the current repo/branch state each session rather than relying on this snapshot. Follow the root `AGENTS.md`, including Git hygiene, copyright and explicit owner decisions.

## Session startup — do this every time

1. Read `AGENTS.md`, `MOD-COMPREHENSION.md`, this file, `README.md` and `INDEX.md` in this directory. Consult `docs/DESIGN.md`, `docs/QUEST-ARCHITECTURE.md`, `docs/QUEST-DESIGN-RULES.md`, **`docs/QUEST-WALKTHROUGH-STANDARD.md`**, and current relevant compatibility/progression documents.
2. Fetch `manifest/mods.json` and **reconcile** it with `mods/*.pw.toml`, `index.toml` and the integration source registry. The manifest records selection intent; pins and assembled runtime establish what's actually installed. If the live branch has changed, record the relevant commit/versions. Do not research old releases as if active.
3. Inspect `mods/`, `interactions/` and `handoffs/` before starting new files; the index may be stale. Take the first **highest-value unfinished** profile or cross-mod relationship, avoiding duplicate/conflicting work in open PRs.
4. Use an isolated `research/mod-comprehension-<topic>` branch from the latest active integration base, with a descriptive PR back to that branch. Never force-push or overwrite another agent's work. If an existing research branch belongs to this task and is safe to resume, continue it instead of duplicating a PR.
5. Finish a coherent slice of work per session (normally one deep profile, or two smaller profiles, or one to three well-supported interaction reports). No "TODO" padding to inflate completion counts. If time or access runs out, write a truthful partial profile with explicit open questions and a next action, then commit it.

## Picking work independently

Prioritize **gameplay-impactful content** over alphabetical order: (a) immediate utility and accessible setup; (b) parent magic systems and addons being quested; (c) transformation/faction overlaps; (d) broad shared resources, energy, rituals, storage and logistics; (e) late-game gates and boss systems; then (f) lower-impact QoL, passive equipment and libraries. See the live high-priority list in `INDEX.md`.

Suggested initial cluster: **Ars Nouveau + addons, Occultism, Neo Vitae, Vampirism, Werewolves, Just Another Witchery Remake/JAWR Forbidden Magic, Summoning Rituals, Botania Revived, EvilCraft, Roots Classic, Iron's, Scriptor Magicae, Goety and related addons**. This is priority guidance, not an assertion they have been investigated. A pure library gets concise interface/API documentation rather than imaginary survival progression. An inactive or explicitly excluded mod should not consume a full profile unless the user asks for a decision audit.

One substantial outcome per run is preferable to ten skim-level summaries. Mark `researching` and `blocked` honestly.

## Pass 1 — single-mod comprehension

For each selected mod:

1. Copy `templates/MOD-PROFILE.md` to `mods/<manifest-id>.md`. Use the **manifest ID as filename**, while separately recording actual `modid`, artifact basename and registry namespaces. Group an addon under its own profile **and** link it from the parent where it extends a progression loop; never silently treat addons as identical to their parent.
2. **Establish the exact version.** Read the manifest row, packwiz metafile, download hash/ID and loader/MC constraints. Where safely accessible, inspect the exact mod artifact locally for its `META-INF` metadata, data/recipe JSON, tags, loot tables, advancements, config defaults, command names and registry values. Read source code/bytecode for mechanics that docs fail to answer. Download artifacts only for transient inspection; **do not commit JARs, source copies, decompiled code dumps, caches or copyrighted assets.**
3. Research official mod wiki, developer docs, release changelog, authoritative source repository/tag, issue discussions, and community guides. Prefer exact-version material; older-version explanations must be marked as such. Verify claims about mods/ports with similar names. Log source URLs, source path/ref and inspected version in the evidence ledger.
4. Map the **complete, step-by-step player progression**: acquire the in-game guidebook (or exact UI/keybind to open it) → starting materials → first functioning setup → every meaningful unlock/tier → all major machines, altars, rituals and optional branches → automation → bosses/advanced systems → endpoint. Name actual crafting/activation/build steps, conditions, costs, limitations and failure signals, not just 'early/mid/late'. For each nontrivial structure or operation, record the **specific guidebook section/page or JEI use/recipe category** a quest must mention. A required step with no confirmed path is a documented blocker, not source-complete.
5. Map **typed inputs → transformations → typed outputs**: exact input kind (item stack vs dropped entity, fluid vs non-fluid Source, XP orb vs XP item, FE/mana/blood, entity, state, event), registry/tag/trigger, quantity/formula, consumed/remainder, output kind/location, tick/event cadence, range/capacity and automation API. Explicitly mark unestablished channels. Label renewable loops by trigger: event, consumed item, consumed fluid, timer, entity state or world interaction. Distinguish code-derived amounts from rates **measured in an identified client/world**. Never infer arbitrary ritual inputs from a mod description.
6. Find **internal synergies** and "why use this?" scenarios: a machine feeding another, cheap starter builds, useful automation, renewable boss/resource paths and overlooked utility. Begin with a compact player-value panel: *five standout capabilities*, *three early practical unlocks* (if available), *three promising integration leads*. Each low-hanging setup needs actual steps, prerequisites, estimated stage/material burden, payoff, limitations and evidence; **do not label midgame infrastructure 'easy early-game'**. Include a throughput/anti-exploit check for farms with gated drops.
7. Identify configuration/API hooks and current pack overrides (KubeJS, data maps, datapacks, configs, addons), especially where content is constrained or functionality conflicts. Distinguish a **documented default** from the **current pack behavior** and a **proposed customization**.
8. Produce a **progression-to-quest coverage matrix**, from first guidebook access to the terminal capability, with a proposed how-to quest/teaching reference for every required stage. For each quest record: intended learning; actual actionable steps; where the guidebook or JEI shows construction/recipes; what FTB can truthfully detect (actual operation versus item proxy versus manual fallback); and a safe XP/thematic reward direction. Account for existing quest chapters/pilots and all addons without pretending their features are present in base-mod code. **Do not edit quests or runtime content** during research.
9. Finish with subsystem coverage status, open questions and a test plan separating **code proves possibility** from **client confirms behavior**. A full-content mod is `source-complete` only when **major subsystems AND full progression AND guidebook access/build references are source-backed, with no unacknowledged mandatory path gaps**; exact pinned JAR inspection or a clearly labeled missing-artifact caveat is required. A substantial but incomplete survey stays `researching` with its completed subsections acknowledged. `verified-in-pack` requires reproducible client/world tests, not server smoke.

**Evidence standard:** Use compact per-claim tags where useful: `[pin]` (packwiz/manifest identity), `[source: SHA]` (immutable source file), `[docs: version]` (documentation at a stated version), `[pack-test: build/world]` (reproducible client/world observation), `[inference]` (unverified hypothesis). Link the evidence **beside the claim** and expand in the evidence ledger with source version and version gap. A source commit declaring the same version as the JAR is **version-aligned**, not proof it generated that binary. Note missing artifact comparison explicitly. A registry/tag/item ID is confirmed only when seen in the pinned artifact/registry; do not invent negative claims.

**Version drift:** Record pinned artifact filename and hash, upstream tag/SHA and version of each guide. When a packwiz artifact hash changes, mark the profile `stale` pending at least a targeted impact review; version strings alone are insufficient. Do not implement an automatic index rewriter unless separately authorized.

**Boundary testing:** For important compatibility, exploitation, progression gates, resource conversion or behavior-detection claims, specify a **positive case and a negative/boundary case**, with the observable change, quantities and setup. Routine harmless features need only a realistic positive test. Item possession is not evidence that an altar, automation system or spell actually operated.

**Scope management:** Profile addons independently, record every installed addon in the parent coverage inventory and inspect both ends of a cross-mod bridge. Do not substitute a large narrative for the concise **subsystem status** and **progression-to-quest** matrices. Research completeness is not the same as authored, working or client-accepted quests.

## Pass 2 — cross-mod interactions

Start once two or more useful profiles expose compatible inputs, outputs, entities, energy systems, recipes, or events. A relationship can be found earlier, but must be marked `candidate` if it lacks two-sided evidence.

1. Clone `templates/CROSS-MOD-INTERACTION.md` to `interactions/<id-a>--<id-b>--<purpose>.md` (alphabetical IDs; add further participants in metadata). Link both source profiles, plus pinned version/source evidence.
2. Trace the **complete conversion path**, not an impression of thematic similarity. Example shape: `Mod A output (actual ID/type/quantity) → bridge/interface (native vs script vs addon) → Mod B input/consumer → payoff`. State whether both systems share tags/registries, whether pipes/automation are required, and where ownership/permissions or server/client state matters.
3. Classify every idea as: **native/in-pack already**, **native upstream but unverified in TMM**, **possible using a documented KubeJS/datapack/config hook**, **requires custom Java or feature request**, or **unsupported/unknown**. No "works" claims without TMM tests.
4. Compare upside (early utility, uniqueness, reduction of grind, fun/quest teaching opportunity) with cost (resources, complexity, performance, balance, escape from locked progression). Be explicit about no-Create, no generic storage/transport, faction exclusivity, hunger/diet exceptions and ProjectE/late-boss gates. Record conflict/feedback loops and possible exploits.
5. Write **reproducible disposable-world tests** with minimum setup and observable pass/fail, including positive plus boundary/negative cases for consequential compatibility, exploit and progression claims. Use current source formulas and separately measure actual in-pack throughput where it matters; include dedicated-server checks where appropriate. If currently blocked, record the missing piece rather than constructing imaginary mechanics.
6. Put strong candidate quest/compat recommendations into `handoffs/` using the handoff template. A research report is not an implementation approval. Cross-link interaction reports from each participant profile.

## Coverage and completion

- `INDEX.md` must remain an honest **coverage tracker**: each active mod is `queued`, `researching`, `source-complete`, `verified-in-pack` or `blocked`; inactive mods are `deferred`. Use `stale` when major pinned-version changes invalidate prior findings. Keep the manifest ID and the file link accurate.
- For each substantive profile, answer: **What does this mod do? How does a player start and get the guide? What exact steps complete its full tier progression? Where does JEI/guidebook explain structures? What does each system consume and produce? What is it best at? What are three useful setups? How do its addons connect? What remains unknown?** If a question doesn't apply, explain why. Check each required progression step against the quest coverage matrix.
- Add or refine interaction files when evidence supports them. Keep a quick count/links in the index's interaction section, including ideas not yet validated.
- Do not fill an "unknown" with generic filler or a blanket "all tiers supported." Deeper, verified partial work is acceptable and traceable.
- If source and live test disagree, describe both explicitly, prefer the observed result for current pack behavior, and mark the proposed cause as hypothesis pending inspection.
- Maintain original writing and short quotations; link sources instead of copying wiki paragraphs, README files or source code wholesale.

## Git, QA and handoff

Before submitting: review Markdown links/path names; ensure all IDs correspond to the current roster; make sure the status and evidence match; ensure no JARs/binaries or generated worlds are staged; check no files under `config/`, `kubejs/`, `manifest/` or gameplay/quest directories were changed. Existing unrelated modifications must be preserved. If the exact code/JAR cannot be accessed, continue with documented research and flag the limitation prominently.

Commit only the research files, open a PR with **mods covered, versions, findings, proven vs proposed interactions, unresolved questions, evidence/verification performed, and next recommended work**. Never call file inspection "gameplay tested." Leave a short end-of-session report linking the PR, new profiles and the next priority. If PR creation is unavailable, commit to the branch and provide its exact URL; do not claim it is merged.

## Working assumptions to reject

- A wiki page covering 1.20.1 proves a mechanic in the pinned NeoForge 1.21.1 fork.
- An item being tagged like a fluid or reagent proves a ritual framework accepts fluids or entities.
- A server boots, therefore quest detection, UI state or mod interoperability works.
- A documented interaction in an addon not pinned by this pack is active here.
- A low-level material can be sold, traded, transmuted or rewarded without harming later gates.
- A quest completes a locked progression gate; the game must enforce the gate independently.
