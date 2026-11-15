# Work operating instructions — Mod Comprehension Project

## Invocation and objective

The user can say **"Work on the mod comprehension project."** Treat this as standing permission to **research, write or improve the knowledge-base Markdown, maintain its index and open a reviewable GitHub PR**. Do not ask which mod to pick unless every reasonable route is blocked. This does **not** authorize modifying the live pack, upgrading mods, implementing compatibility changes, or authoring/publishing quests.

Project repository: `heatwaveqc/Too-Much-Magic-Modpack`. The current working integration stream is `test/full-integration`, not the minimal `main` bootstrap branch. Check the current repo/branch state each session rather than relying on this snapshot. Follow the root `AGENTS.md`, including Git hygiene, copyright and explicit owner decisions.

## Session startup — do this every time

1. Read `AGENTS.md`, `MOD-COMPREHENSION.md`, this file, `README.md` and `INDEX.md` in this directory. Consult `docs/DESIGN.md`, `docs/QUEST-ARCHITECTURE.md`, `docs/QUEST-DESIGN-RULES.md` and current relevant compatibility/progression documents.
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
4. Map the complete **player gameplay loop**: starting resources → discovery/unlock → basic capabilities → progression tiers → optional branches → bosses/endgame, including alternative routes, repeatable processes and renewable resources. Cover each major subsystem (not only its most famous block). State known costs, limits, prerequisites and resource drains; mark unavailable data.
5. Map **inputs → transformations → outputs**: accepted items/tags, fluids, gases, energy types, mana/blood/source, XP, entities, mobs, world blocks/biomes, status effects, player abilities, skills, events and recipes. Note catalysts consumed vs reused, storage capacities, automation, GUI/ritual behavior and whether external scripts/datapacks can express an operation. Do not infer arbitrary fluid/XP/entity support from "ritual mod" marketing.
6. Find **internal synergies** and "why use this?" scenarios: a machine that feeds another, quick self-sustaining loops, cheap starter builds, helpful long-term automation, renewable boss/resource paths and overlooked utility. Each "low-hanging" setup needs exact step-by-step prerequisites, estimated stage/material burden, payoff, limitations and evidence.
7. Identify configuration/API hooks and current pack overrides (KubeJS, data maps, datapacks, configs, addons), especially where content is constrained or functionality conflicts. Distinguish a **documented default** from the **current pack behavior** and a **proposed customization**.
8. Record discovery hooks suitable for quests (a real unique milestone, use/detection task, crafting item/advancement, repeatability), and compatibility opportunities, **without editing actual quests or game content**. Check current quest chapter ownership and reward/gate rules.
9. Finish with open questions and a test plan that can separate **code proves possibility** from **client confirms behavior**. Use `source-complete` only after the major subsystems have evidence, not because one wiki page was read. `verified-in-pack` requires reproducible client tests, not server smoke or file validation.

**Evidence standard:** Place evidence near each nontrivial claim (URLs, versioned source paths, recipe/file IDs, observed commands) and list it in the evidence ledger. Distinguish **documented upstream**, **inspected exact version**, **tested in pack**, and **inferred/unverified**. A tag name or item ID is only confirmed if actually observed in the pinned registry/artifact; avoid invented item IDs and "no such feature" claims without checking likely alternate systems.

## Pass 2 — cross-mod interactions

Start once two or more useful profiles expose compatible inputs, outputs, entities, energy systems, recipes, or events. A relationship can be found earlier, but must be marked `candidate` if it lacks two-sided evidence.

1. Clone `templates/CROSS-MOD-INTERACTION.md` to `interactions/<id-a>--<id-b>--<purpose>.md` (alphabetical IDs; add further participants in metadata). Link both source profiles, plus pinned version/source evidence.
2. Trace the **complete conversion path**, not an impression of thematic similarity. Example shape: `Mod A output (actual ID/type/quantity) → bridge/interface (native vs script vs addon) → Mod B input/consumer → payoff`. State whether both systems share tags/registries, whether pipes/automation are required, and where ownership/permissions or server/client state matters.
3. Classify every idea as: **native/in-pack already**, **native upstream but unverified in TMM**, **possible using a documented KubeJS/datapack/config hook**, **requires custom Java or feature request**, or **unsupported/unknown**. No "works" claims without TMM tests.
4. Compare upside (early utility, uniqueness, reduction of grind, fun/quest teaching opportunity) with cost (resources, complexity, performance, balance, escape from locked progression). Be explicit about no-Create, no generic storage/transport, faction exclusivity, hunger/diet exceptions and ProjectE/late-boss gates. Record conflict/feedback loops and possible exploits.
5. Write **reproducible disposable-world tests** with minimum setup, expected behavior and observable pass/fail, including singleplayer and server where appropriate. If currently blocked, record the missing piece rather than constructing imaginary mechanics.
6. Put strong candidate quest/compat recommendations into `handoffs/` using the handoff template. A research report is not an implementation approval. Cross-link interaction reports from each participant profile.

## Coverage and completion

- `INDEX.md` must remain an honest **coverage tracker**: each active mod is `queued`, `researching`, `source-complete`, `verified-in-pack` or `blocked`; inactive mods are `deferred`. Use `stale` when major pinned-version changes invalidate prior findings. Keep the manifest ID and the file link accurate.
- For each substantive profile, answer: **What does this mod do? How does a player start? What does it consume and produce? What is it best at? What are three genuinely useful setups/shortcuts? What is it compatible with? What remains unknown?** If a question does not apply, say why.
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
