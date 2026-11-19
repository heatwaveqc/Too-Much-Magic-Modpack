# Too Much Magic — Quest Authoring Standard v2.0
**Status:** authoring standard updated with owner-approved *funny and useful tangible rewards* (2026-11-14). Other progression/ID/migration changes still require review. It does **not** by itself authorize a migration of player claim data or an unreviewed change to an existing reward.  
**Minecraft:** 1.21.1 / NeoForge; verify actual pinned mod versions using the current **integration lock**, not a historical B8/B11 roster.  
**Related documents:** [funny and useful reward palette](QUEST-REWARD-PALETTE-V1.md), [critical audit](QUEST-CRITICAL-AUDIT-2026-11-14.md), [Work execution handoff](QUEST-WORK-HANDOFF.md), [45-chapter approved architecture](QUEST-ARCHITECTURE.md), [shop economy](QUEST-SHOP-ECONOMY.md).

## 0. Goal and hierarchy

**The quest book is a player-centered field guide to a magic-first sandbox, not a compulsory completion checklist.**

A player should be able to answer, for any modded system: **What does this do? How can I get started? What is the next meaningful capability? What actually blocks it? Where do I go when I'm stuck?**

All authoring decisions must satisfy these priorities, in order:

1. **Truth and safety** — what works in the pinned game today; don't claim that a planned bridge/gate/recipe is implemented.
2. **Player agency** — many independent schools and builds; no artificial mandatory unrelated school, reactor or vanilla item detour.
3. **Actual learning** — teach unfamiliar mechanics and diagnose likely errors; don't restate JEI or vanilla achievements.
4. **Honest verification** — a quest completes only on the action/state it says is being measured.
5. **Delightful compensation** — every claimable achievement should feel like a present: an amusing odd food, a useful cheap tool or an early related derivative. A coin is the fallback. Humor, not junk, is the objective.
6. **Navigability** — coherent chapter ownership, optional branches, small readable diagrams and searchable names.
7. **Maintenance** — traceable sources, stable IDs, versionable authored schema, automatic checks and multiplayer-safe rewards.

### Existing decisions that are binding

- Exactly **five** categories, presently **45** approved chapter slots: Introduction, Progression, Magic, Exploration & Combat, Endgame. Keep major primary magic mods in their own chapters; addons belong with their parent unless truly independent.
- Starter-path choice is **one native choice per player**, optional and non-exclusive, immediately after the crafting-table milestone. It has passed an initial user UI test. No permanent class gate.
- No quest may reward **\`minecraft:experience_bottle\`** as an item. XP points via an FTB XP reward remain permissible. Maintain this blacklist recursively, including choice/reward tables.
- Checkmarks should be limited to **genuinely non-observable** actions, aside from the initial welcome/reference onboarding and the intentional starter choice. Adding a checkmark requires a written reason.
- Preserve actual game gates: ProjectE and Project Expansion **after Chaos Guardian**, enforced in mechanics (not just a claimed quest). Do not add Re:Avaritia, Extended Crafting, Ring of the Seven Deadly Sins, or forbidden/unverified boss-gated resources as rewards.
- Keep the working shop button and **team-shared coin economy**. The twelve resource sales intentionally consume items and credit one coin; do not rewrite rates/repeatability without separate economy approval.
- First-entry personal starter kit (Akashic Tome, two ordinary cooked steaks and Improvable Skills book access) and first-log Trinkets Pouch are separate **once-per-player** grants. Do not double-award them via quests or repeat on relog/death.
- Tyz's Skills and the unified MXP plan supersede **future Pufferfish tree design**. Pufferfish removal and live Tyz ranks must not be represented as shipped until the actual runtime does so.

### Owner-approved physical reward policy (2026-11-14)

Keep the **direct XP plus an enjoyable tangible reward** principle for claimable quests. The problem was not giving rewards; it was giving **paper, cookies, bowls, compasses and arbitrary filler**. Prefer a quirky food, modest useful tool, relevant ingredient, or an early head start on a related mechanic; give one shared Quest Shop coin only if no good physical gift is suitable. **One explicit empty-bowl exception:** the Farmer's Delight Cooking Pot quest awards a bowl as a deliberate joke. **Never reward XP bottles as items.** No blank paper/cookies/compasses/empty bowls as generic placeholders. Consult [the reward palette](QUEST-REWARD-PALETTE-V1.md) and record why each grant is interesting. Pure *non-claimable* reference pages are allowed only where the actual UI semantics support them; don't convert previously accepted claimable rewards to no-reward without owner review.

## 1. Four player-facing node types (plus exchange)

| Kind | Exists because… | Objective & UI | Reward | Typical place |
| --- | --- | --- | --- | --- |
| **Reference / navigation** | The player needs knowledge or a link | Genuine non-claimable page if supported; if made claimable, it must be a purposeful optional tip, not a fake task | Claimable tips: direct XP plus a delightful **small** useful/odd gift (or a shared coin fallback); no arbitrary paper | Chapter index, controls, safety, crosslinks |
| **Milestone** | Something useful was actually acquired, crafted, visited, or achieved | Native auto-detect criterion whose **semantics match the title** | Direct XP plus a modest, deliberate physical gift: next-stage component, cheap useful tool or funny consumable; a coin if necessary | First spellbook, first ritual component, first usable machine |
| **Challenge / mastery** | Optional skillful or costly achievement that advances real play | Boss kill, structure, advancement, verified interaction, sustained mechanic or explicit justified checkmark | Relevant earned supplies/utility, not a boss bypass or an item from the next gate | Specialization, powerful rituals, boss or optional feat |
| **One-time choice** | The player makes a meaningful but nonexclusive opening selection | Native FTB choice reward, no Claim All autoselection; separate personal claim state | One reviewed starter gift per player, no permanent class lock | Choose Your First Path |
| **Economy exchange** | Player chooses to trade resources for anti-grind currency | **Consuming** item task with a clearly stated one-time/repeat contract | Team-shared coins **exactly once** per valid contract | Copper/emerald/diamond sale line |

Every authored quest must declare a **kind**, reason for existence, **success condition**, player/team claim scope, prerequisite rationale, and the actual validation mechanism. If the node only repeats a generic fact from the tooltip or JEI and offers no choice, decision or useful guidance, **delete or merge it**.

## 2. Content and prose

### Text budget (editorial targets, not artificial pass/fail quotas)

| Node | Target | What it must contain |
| --- | --- | --- |
| Reference/index | 20–70 words; longer only for safety/critical links | Purpose, how to navigate, one or two relevant links or caveats |
| Ordinary milestone | 35–85 words | **Why**, **how to begin**, **what counts**, **what it enables** |
| New modded mechanic / ritual | 60–120 words | Starting materials and interface, actual sequence, one common failure, next step |
| Dangerous endgame machine / boss | 80–160 words in readable short paragraphs | Real prerequisites, hazards, configuration or failure conditions, safe test boundary, next step |

**Useful explanation, not filler, is the goal.** Six words can suffice for a trivial vanilla item, but then ask why that quest exists. Avoid padded generic claims such as "This is useful for many magical recipes"; say *which relevant system* uses it or link to that system.

Rules for every description:

- **Actionable:** Name the relevant object, ritual, GUI, progression resource or location. Explain what the player does next and where to get stuck.
- **Technically honest:** If the task checks possession, say **obtain/bring/acquire**, not **craft/catch/cast/build/use**. When crafting-only detection is actually configured and tested, the stronger verb is allowed.
- **Mod-specific:** Describe the mechanic that distinguishes this mod from the 24 other schools. Avoid “make the book; use JEI; continue” repeated 25 times.
- **Brief alternatives:** A different viable magic route can be mentioned and cross-linked; don't make it an artificial prerequisite.
- **No creator diary:** Never tell players in game that "our pinned build still needs QA", "the upstream repository says", or "the planned bridge will eventually work". Keep these as contributor notes. In active test builds, show a clear short "Experimental" label only for a feature genuinely present and playable but not accepted.
- **No unsupported absolutes:** Safety, fuel, mana, drop rates, compatibility, chunk protection, rarity and soft caps need version-specific evidence.
- **Readable names:** Short, specific and mechanically descriptive. Prefer **Acquire a Novice Spellbook** over **Master the Arcane Arts** for an item-possession task.
- **Localization:** Escape literal \`&\` through FTB's formatter, then SNBT; test all descriptions, not just category labels. Validate quote/newline/unicode formatting, the \`undefined\` string and truncated text. Don't copy quest prose/diagrams wholesale from other packs.

### Bad/good examples grounded in the current book

| Current wording / structure | Why it fails | Better design |
| --- | --- | --- |
| **Catch a Cod** + \`minecraft:cod\` item check | Possession does not prove catching it | **Bring Home a Cod**, or a separately verified fishing-stat/custom detection if the actual catch matters |
| **Build a Research Lectern** + plain lectern item | Does not measure placement, use or the separate Scriptor Casting Lectern | **Obtain a Lectern** as optional library item; move actual Scriptor fuel-free magic to its own Magic chapter |
| **Try a First Spell or Ritual**, blocked behind iron pickaxe → barrel → lectern | Generic magic initiation tied to unrelated survival equipment | Make the first-magic guide directly accessible after starter choice; each school's real spell/ritual requirements live in that chapter |
| "Pufferfish's Skills will support…" | Deprecated architectural expectation disguised as current guidance | Status-aware guide about **currently shipped** Improvable Skills; later replace with tested Tyz/MXP instructions |
| **Tips: use JEI**, checkmark, paper reward | Reading an explanation is not progress and paper is rubbish | Make a genuinely useful optional tip with a strange small edible/tool reward; or a true non-claimable reference, if verified and appropriate |
| **Make a Crafting Table**, reward two bowls | Functioning loot, no purpose | Reward a useful trinket or bizarre snack. Reserve the **single bowl** joke for Farmer's Delight Cooking Pot |

## 3. Structure, dependencies and chapter composition

### Global structure

- Preserve the 45 slots, but **don't publish an empty-looking quest branch as if it is complete**. Work should replace the 42 temporary overview checkmarks with polished **reference/navigation** nodes, then progressively add actual content.
- **Introduction:** short optional survival essentials + earliest branch into magic, fishing, exploration, shop and QoL. No required iron, lectern, reactor, boss or second school to open a first magical activity.
- **Progression:** only truly **shared** milestones, constraints, resource bridges, major gates and cross-mod integration. Don't replicate every other chapter's items or impose a singular technology arc.
- **Magic:** one substantial, independently browseable chapter per primary tradition. Own the detailed starter-to-capstone path and addons/crosslinks.
- **Exploration & Combat:** places, encounters, factions, bosses and rare equipment, with suitable optional branches and correct death/loot notes.
- **Endgame:** definitive difficult systems and boss-gated rewards. Roadmap in Progression only **points toward** definitive tasks; it does not pay duplicate boss rewards.

### Recommended template for each substantial magic chapter

1. **One concise reference entry:** fantasy, actual resource/mana system, prerequisites, what makes this mod fun.
2. **First viable entry:** how to obtain the first real item/ritual and begin doing magic. The beginning must be reachable independently except genuine recipe/dependency gates.
3. **First useful effect:** produce a real spell, summon, ritual, flower, infusion or equivalent.
4. **Resource loop:** where its energy/ingredients come from, how they replenish, and what limits progress.
5. **A meaningful fork:** two or more useful play styles when the mod actually supports alternatives (combat, utility, automation, exploration).
6. **Midgame system:** a key advancement in capability with verified ingredients and risks.
7. **Signature feature:** something unique enough to justify playing this school.
8. **Capstone / optional mastery:** challenging, rewarding, mechanically real; cross-link related mods and true gates.

Small specialist mods may require **4–8** substantive nodes; broad traditions might reasonably require **12–25**. **No fixed quotas.** Merge trivial crafting steps, split dense multistep mechanics, and don't create a chapter of 25 checkmarks to hit an arbitrary target.

### Prerequisite edges

- Require a dependency **only** if it mirrors real mechanics, makes learning much clearer, or represents an intentional player-chosen challenge.
- Keep independent systems independent. Item possession through loot, the shop or a starter reward may intentionally satisfy early item tasks; don't label those actions as crafting.
- Optional branches should be visibly separate and reconverge only at a genuinely shared milestone.
- Crosslink chapters in text or supported hyperlinks; **avoid copying another chapter's objective and reward**.
- Do not treat quest completion as a recipe or transformation lock. Implement actual locks in their owning mod/KubeJS/compat first and test them.

### Layout

Readable top-to-bottom progression, consistent horizontal lane spacing, few crossing connectors, icons matching the mechanic, branch clusters with clear labels, not one long vertical line or hundreds of icons on one plane. The player should immediately find **start**, **optional branches**, **next capability**, and **long-term goals**. Verify pan/zoom and text in Prism after generation.

## 4. Objective selection: measure what the title promises

Use the native FTB Quests 2101.x types actually present in its source where the pinned version supports them:

| Claimed action | Preferred detection | Notes |
| --- | --- | --- |
| Acquire / bring an item | \`item\` task | Non-consumed possession unless explicitly a shop sale |
| **Craft** an item | Item task with FTB's **\`only_from_crafting\`** option, if shown compatible in runtime | Plain item-possession task does **not** prove crafting. Test selected recipes, including modded crafting tables. |
| Interact with an entity/block | \`interaction\` task | Check exact interaction event and block/entity ID |
| Look at / identify a world block/entity | \`observation\` task | Only claim what observation actually detects; sky or terrain puzzles need trial |
| Enter a dimension | \`dimension\` task | More honest than “bring a blaze rod” if entry is the goal |
| Visit a structure | \`structure\` / \`location\` task | Confirm dimension and target registry; entering one may not demonstrate clearing it |
| Defeat an entity or boss | \`kill\` task or specific advancement | Need ID validation and team attribution; don't use possession of boss loot as proof when a kill is required |
| Complete known vanilla/mod advancement | \`advancement\` task | Verify trigger semantics and installed ID |
| Perform action with an available stat | \`stat\` task | Verify stat is incremented by that exact mod mechanic |
| Prove a mod-specific event absent from native types | Small server-side advancement/event bridge, or a justified manual checkmark | New custom code requires maintainers' review, tests, and multiplayer-safe lifecycle |
| Provide a voluntary material sale | **Consuming** item task | Shop coins shared by FTB Team, correct repeat cooldown; never use non-consumed possession |

Also available upstream: biome, energy/FE, fluid, XP, stage, custom tasks. **Availability does not prove a given mechanic can be instrumented**. Work must use the actual pinned API/registry and test its semantics before adopting an unfamiliar type. Do not silently substitute an item task for an unobservable action.

**Manual checkmark exception policy:** Each surviving manual objective records \`manual_reason\`, e.g. "ritual performed by one of seven incompatible casting APIs; no shared event exists". Initial welcome/reference/choice are special cases. A checkmark for **craft X**, **collect Y**, **enter Z**, or **kill boss B** is a lint failure unless a documented technical blocker is proved.

## 5. Rewards and economy

### Reward policy

- **No experience bottles as quest-item grants.** Direct XP points are fine. No routine paper, cookies, compasses or empty bowls; one exception is the Farmer's Delight Cooking Pot quest rewarding one empty bowl.
- **Reference content:** avoid unnecessary claim buttons; genuinely claimable optional tips should give direct XP plus a small funny/useful present, never trash. Do not rewrite existing IDs or claimed rewards without a migration review.
- **Simple milestones:** direct XP **plus a deliberate item**. Examples: a relevant next-ingredient, a compact reusable tool, a weird edible treat, or a one-coin fallback if a physical reward really does not fit.
- **Significant milestones and mastery:** meaningful resource grants (e.g. first EvilCraft Dark Gem → 1–2 Crushed Dark Gems), utility or cosmetic items, possibly a verified choice; no guaranteed boss drops or next-tier unlock bypasses.
- **Starting rewards:** one personal starter choice, one separate personal starter kit, one first-log pouch. Do not multiply these grants in FTB, advancements and KubeJS.
- **Trade contracts:** team-owned coin balance, consuming tasks, exact 12-approved sale semantics. No repeatable XP/currency loops through partial claiming, resets or team swaps.

### Reward quality review

Before approving any physical reward, answer **all five**:

1. *Why this item here?* Can it be used soon in the path the player just advanced?
2. *Would this reward be enjoyable or useful?* If not, don't add it just to satisfy schema.
3. *Is it obtainable/balanced at this point?* Avoid singular loot, quest-only items, unverified tags/IDs and later-tier bypasses.
4. *Does this item fulfill a different authored goal?* If so, is that a documented intended shortcut, with stack quantities and team claim scope checked?
5. *Would a second claim, teammate or repeated quest break the economy?* One-time, repeatable, per-player and team must be explicit.

Static lint should **warn** if a chapter pays the same item more than twice or the book does so more than four times; a maintainer can approve a thematic exception. **Humor and intentional utility matter more than an arbitrary uniqueness count.** **Hard fail** if the reward is forbidden, unregistered, wrongly team-shared, can be duplicated unintentionally or breaks a real progression gate. All reward IDs, counts and click/Claim All behavior require eventual client QA.

Use **relative XP tiers** (orientation, introductory milestone, middle system, capstone) pending an XP balance review; do not inflate payouts to compensate for empty reference entries. The existing 5–100 point rewards are a baseline, **not** a calibrated target.

### Shop integration

Quest Shop already adds a working coin button inside FTB Quests; don't add another shop or require the command to open it. \`/hqs shop\` remains a valid fallback. Any proposal for a one-coin purchase or sale-rate redesign is a **separate economy decision**. Confirm that purchases cannot buy forbidden bosses, bypass Chaos Guardian, or multiply team currency.

## 6. Evidence standard: source, implemented, tested

Every non-vanilla quest needs a compact **author-only evidence record**:

- **Pinned mod ID/version:** authoritative integration lock or resolved packwiz pin, not a stale design deck.
- **Registry evidence:** exact block/item/entity/advancement/dimension ID, shown to exist in pinned binary or verified live registry; cite repository path/commit where helpful.
- **Mechanic evidence:** source/official documentation, actual configuration or tested JEI recipe.
- **Pack customization:** relevant KubeJS/compat/config source and its active build status.
- **Task semantics:** what event is detected; why chosen task matches the title; whether the task consumes resources.
- **Prerequisite and gating evidence:** true in-game preconditions vs book-only informational links.
- **Claim semantics:** per player/team; auto/manual/choice; first-claim, repeat and restart behavior.
- **Test state:** \`draft\` → \`source-verified\` → \`server-smoked\` → \`client-verified\` → \`multiplayer-verified\` where applicable.

**Never treat source-verified as playable.** If a fact is uncertain, keep a flagged candidate in the authoring backlog and omit the unverified claim from live player text until resolved.

## 7. Source model, IDs and CI

**This is a blocker to Work scale today.** Existing \`scripts/rebuild_quest_ui.py\` only rewrites existing SNBT quest blocks, contains hand-listed coordinates, and expects exactly 180/138 entries. Existing \`scripts/validate_quests.py\` rejects extra content in placeholder-only chapters, and the old v1 document contains contradictory reward rules.

Before **runtime insertion of bulk new chapters**, implement a **versioned, declarative compiler**. However, **drafting source-verified mod questlines can and should proceed concurrently** in isolated content-only branches; the compiler is not a blocker to researching, writing or reviewing them:

- One canonical authored quest record per stable logical ID; ID should **not depend on title**. Store canonical chapter, kind, description, task specification, reward specification, dependencies by stable quest ID, optional/hidden/repeat flags, claim scope, manual exception reason and evidence status.
- Regenerate **FTB SNBT, localization and index/hash** from that canonical source; never hand-maintain three diverging copies of the same quest. Preserve IDs for existing completed quests; new IDs allocated deterministically with collision checks.
- Move node positions into data or an algorithm with manual overrides; validate non-overlap, reachable branches, clear connector routing and stable layout.
- Dynamically count authored/placeholder quests; remove hard-coded counts and “only one quest in unpopulated chapter” assumption. Protect exactly five categories and the approved chapter inventory.
- Nested reward lint: no experience bottles, no off-limits items, valid registry IDs, sensible counts and claim scopes. Include starter choices, command rewards, advancements and shared currencies.
- Task lint: verify allowed types/fields, target registry and team scopes; enforce documented manual exception, check title-versus-detection semantics; no duplicate criterion without written reason.
- Dependency lint: no dangling references, cycles, self-gates or unintended cross-chapter lock; maintain migration compatibility with existing FTB IDs and team progress.
- Content lint: no \`undefined\`, broken \`&\`, obsolete Pufferfish narrative, author-only "coming soon" statements in player text, duplicate generic description templates or banned items.
- Run **roundtrip/idempotence tests** (build twice → identical output) and preserve the immutable integration snapshot before any new public test build.

Do not remove or rewrite established FTB reward IDs unless migration has a plan to protect existing saves/claims. Never replace stable test release or user world files. Maintain separate stable and all-active-streams experimental builds.

## 8. Acceptance rubric and release gates

Score each quest 0 / 1 / 2 for six dimensions:

| Dimension | 0 — Reject | 1 — Revise | 2 — Accept |
| --- | --- | --- | --- |
| Purpose | Pure filler / duplicate | Somewhat useful | Teaches, proves, challenges or offers meaningful choice |
| Accuracy | Unsupported or false | Source suggests, needs direct runtime check | Correct for pinned build, caveats recorded |
| Task semantics | Task detects wrong thing | Proxy task explicitly described | Objective faithfully measures displayed goal |
| Player guidance | Generic or confusing | Partial direction | Actionable mechanic, next step, relevant constraint |
| Progression graph | Arbitrary gate / duplicate track | Mostly coherent | Independent where possible, real necessary dependencies |
| Reward/scope | Filler, prohibited, exploit | Plausible but unbalanced | Relevant, appropriately sized, once/repeat/team correct |

A **zero in accuracy, task semantics, or reward safety is an automatic blocker**. Require at least **10/12** and no zeros for routine publication candidates; later player QA can still reject a source-verified entry.

### Gates per Work batch

1. **Source review** — mod/recipe/runtime status and registry evidence. No item/event invented.
2. **Compiler + lint** — source schema, IDs, localization, reward blacklist, graph, count and regeneration idempotence pass.
3. **Dedicated-server smoke** — full integration, all active working streams, both first-party modules installed; no silent package omissions.
4. **Client acceptance** — categories/quests/choice/claims/icons/task triggers, absence of broken/unnamed formatting and actual player usability.
5. **Multiplayer + persistence** for rewards and gates — team/individual scope, Claim All, death, relog, server restart, partial inventories and repeating contracts.
6. **Owner content review** — first pilot, then batched acceptance. A green smoke never upgrades a claim from "candidate" to "client-proven".

Do not publish to the stable branch or rewrite old claim data without explicit approval. Static tests, meaningful prose and real gameplay tests are **separate quality requirements**, not substitutes.

## 9. Delegation and stop conditions

Work may autonomously research pinned content, draft quests, build a declarative compiler in an isolated branch, lint, add tests, create a **two-chapter pilot** and report a proposed content/reward diff. It should **not** autonomously change endgame unlocks, add/remove mods, bypass transformation incompatibility, replace skill-engine architecture, set a new currency policy, or convert existing claimed quests to new reward semantics without separate approval.

If Work cannot verify an item ID, mechanic, or pack-specific config, it must label that entry **unverified**, omit it from the runnable pack and continue with other verified entries rather than guessing.

Read [QUEST-WORK-HANDOFF.md](QUEST-WORK-HANDOFF.md) for the exact run order, starting refs, deliverables and approval gates.
