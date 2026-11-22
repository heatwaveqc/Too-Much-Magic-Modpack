# TMM Quest Presentation and Visual Authoring Standard v3

**Status:** owner-approved design direction, 2026-11-22; documentation-only authoring contract, not a claim that current chapters or generators comply. This visual companion is normative for **new or substantially rewritten FTB Quests chapters** after adoption. It supplements [QUEST-DESIGN-RULES.md](QUEST-DESIGN-RULES.md) and [QUEST-WALKTHROUGH-STANDARD.md](QUEST-WALKTHROUGH-STANDARD.md). Neither graphical polish nor ATM10 inspiration supersedes verified native mechanics, safe rewards, stable IDs, or the latest owner decisions.

**Intent:** A TMM chapter is a beautiful, readable, actionable guide. A player should understand what to do next from the spatial graph, item-specific icons, section illustrations, labels and prose—without making visual art a progression gate. The Mod Capability Atlas is the primary detailed factual research source; mod-comprehension profiles and current pinned runtime data reconcile it.

## 1. What the ATM10 reference actually establishes

The owner-provided 2026-10-30 ATM10 quest-configuration ZIP contains 67 chapter SNBT files. **65 of 67** contain a chapter-level **images** array with positioned visual elements; neither a required full-screen image nor a literal chapter **background** field was present in these files. Across the archive the images arrays contain **1,318 image references**, including **932 references to ATM-owned questpic paths** in 59 chapters, alongside native Minecraft and mod resources. Many are banners, title art, illustrative machine renders, ornaments, section panels, and small pictograms. Quest nodes also use distinct shapes and sizes; chapter graphs usually use dependencies and visible connector lines. Some images have layer/order, rotation, tint, or optional click/hover fields.

**Crucial limitation:** the ZIP contains SNBT references, *not the ATM10 resource-pack textures themselves*. A referenced title or background image is evidence of a visual placement, **not** proof that its underlying pixels are available in the ZIP or licensed for reuse. Never copy ATM10 text, art, loot tables, distinctive graphic assets, exact layouts or IDs. Reuse only the organizational idea; produce original TMM art or reference assets legitimately supplied by our installed mods/Minecraft.

The operative FTB Quests feature is a **chapter's positioned images collection**, which can produce a background-like visual composition. Do not invent a universal SNBT background key.

## 2. Chapter archetypes and readable graph grammar

Choose and record **one primary archetype** before positioning quests. A chapter may contain well-separated subgraphs of another kind when the actual progression warrants it.

- **Left-to-right branching (default for substantive mod chapters):** early guide access on the left, required native stages through the center, capstone on the right. Place optional machinery, spells, rituals, pets, tier-specific addons and alternative paths in visually separate lanes; reconnect only where a real prerequisite exists. This is the default even if source material suggested a top-to-bottom draft.
- **Center-out (open-ended subjects):** central orientation, spokes for independent activities. Prefer for Getting Started, Tips, food/farming/fishing, broad QoL, and other areas without one factual tier ladder. Do not require all spokes for completion unless mechanics actually require them.
- **Convergent/network (cross-mod routes):** distinct validated choices flow toward a common capability or major milestone. Use for shared Progression, multi-school alternatives, boss-route preparations, and endgame roadmaps; multiple alternatives must not be converted into an AND requirement accidentally.
- **Hybrid:** where a single chapter genuinely combines ranked tiers and unrelated utilities, use a clearly labeled left-to-right core with visually offset optional clusters. Do not invent dependency lines merely to force an attractive pattern.

Layout rules:

1. Follow actual prerequisite direction. Let separate branches be browsable; avoid a single compulsory chain for optional content. Cross-chapter informational links are not substitute game locks.
2. Dependencies should be visible by default, readable and reasonably short. Minimize crossings, overlapping nodes/lines and confusing backtracking. Hiding dependency lines needs a documented clarity reason and a navigable alternative.
3. Use consistent grid units, negative space, and aligned lanes. Use measured node bounds—including larger milestones—not just identical center positions to avoid collisions. No absolute spacing number is declared safe until rendered on the pinned FTB client at normal zoom and reduced zoom.
4. Group related mechanics with section labels/visual panels. Do not overlay decorative imagery over node icons, important arrows, rewards, or quest text.
5. Preserve a clear visible entry, reachable next steps and meaningful end state. A required-path orphan, visually ambiguous branch or disconnected mandatory node fails review.

## 3. Quest icon is mandatory and semantically meaningful

**Every authored quest must specify a distinct, intentionally chosen visual referent.** No generic enchanted-book, plain paper, placeholder chest or other catchall icon simply because the generator lacks metadata. The icon must tell a player what the quest asks them to acquire, craft, do, fight or unlock. Icon selection is independent of the reward: showing the objective is not promising it as a reward.

Selection priority:

| Objective | First-choice icon | Appropriate fallback |
| --- | --- | --- |
| Obtain/craft item | Exact requested item stack/registry ID | Visually unambiguous output of the same recipe |
| Build machine, altar or multiblock | Defining machine/altar block item | Unique center block or meaningful construction tool |
| Perform ritual/process | Recognizable ritual sigil/tablet/catalyst or result | Relevant unique workstation; custom original emblem if needed |
| Spell/glyph/research | Exact registered glyph, scroll, spell item, knowledge token | Original icon of that capability; distinct illustrative relevant item |
| Boss/creature encounter | Unique trophy/drop/head or verified native mob depiction | Relevant native spawn egg **only if** it communicates the correct mob; original TMM emblem |
| Tier upgrade/unlock | Defining tier orb, chalk, core, book, gear, badge or component | Original tier-specific emblem |
| Informational tip | Specific relevant tool/UI-access item | Original semantic pictogram, never a generic blank placeholder |
| Cross-mod milestone | Most informative objective result or capability emblem | Original combined emblem if a single native item would mislead |

An item-detection quest normally uses the *task's* item—not its cost materials. For an operational quest, prefer the object that signifies the operation or achieved capability, not some incidental consumable. A book icon is fine for a quest **about obtaining/opening that specific book**. Avoid using one icon dozens of times merely because the same system is involved.

**Technical rules:** resolve and validate every item/resource namespace against the current pinned registry or actual resource pack. Prefer a native item stack icon (preserves in-game rendering) when available. Custom art must resolve to a packaged original texture, with a deliberate missing-asset fallback that stays specific to the quest. If no usable icon exists, mark that quest **icon-blocked for review**, not complete; do not silently substitute enchanted books. Keep icon choices stable and auditable across regenerations. Unique item IDs per icon are *not* required—two quests legitimately about one apparatus may share it—but excessive sameness triggers a visual review.

## 4. Chapter artwork and background-like composition

**Every substantial new chapter needs a deliberate visual identity**, normally combining:

1. **Header/title artwork**: a relevant original chapter banner or recognizable native mod icon at the entry region. It should identify the school/system without copying the mod's proprietary promotional art or ATM10 title graphics.
2. **One or more large thematic illustrations or soft backdrop panels**: altars, circuitry, magic circles, constellations, gardens, crafting scenes or natural environment motifs relevant to the lesson area. Use FTB chapter **images** records. Asymmetric compositions are welcome; the picture should frame the graph rather than fill the entire canvas.
3. **Section-level cues**: modest separators, small native-item illustrations, emblems and captions to indicate distinct tiers, mechanics or branches. A packed chapter can have more; a short reference chapter can have less.
4. **Quest-node hierarchy**: use compatible node shapes and scale for orientation, routine actions, branch choices, achievements and capstones.

Do not enforce one wallpaper image behind every chapter. If a whole-page background is chosen, make it low-contrast, noninteractive, and clearly subordinate to node lines and icons. Prefer negative space over busy full-screen illustrations. Background and title art must not imply a mechanic or an obtainable item that is absent from the chapter.

Resource approach: ship **original TMM PNG resources** inside the actual client-distributed resource pack/module (proposed namespace and path family: assets/tmm/textures/questpics/<chapter>/...). In SNBT, reference the verified resource location—e.g., **tmm:textures/questpics/<chapter>/title.png**—only after its actual packaging path is confirmed. Existing vanilla/mod texture paths are acceptable when real and version-verified. Design at the pack's intended 64x visual direction when creating new art, but test scaling and UI clarity; a 64x art target is **not** a claim that all available item sprites are 64x.

FTB images support positioned coordinates, width/height, rotation, and some examples show order, color, click/hover. Treat those as **candidate verified capabilities from the ATM10 SNBT**, not universal permissions for arbitrary UI behavior. For new TMM images, author source location, intended bounds, layering, opacity/tint when supported, and interaction policy. Default decorative art to no click action. External links/actions need a separately approved, safe, useful purpose. Ensure image elements do not obstruct selection/hover of quests.

**Do not assume** the current TMM generator supports authored decorations. At the 2026-11-22 inspection, the integrated **config/ftbquests/quests/chapters/*.snbt** examined had empty images arrays, and **scripts/rebuild_quest_ui.py** regenerates quest positions/localization and can overwrite direct edits. Before runtime artwork authoring, give the generator a declarative presentation input and tested serialization step; never hand-edit only the generated SNBT and expect persistence.

## 5. Node hierarchy and treatment

Use recognizable, consistent visual meaning rather than arbitrary prettiness:

| Role | Suggested treatment (client-verified mapping required) |
| --- | --- |
| Chapter orientation/guidebook | A clear start node; modest enlargement and distinctive shape |
| Core action or regular craft | Normal-sized, repeated base shape |
| Meaningful fork or independent specialization | Distinct compatible shape or small size difference; label branch |
| Major tier / boss / significant ritual | Enlarged node and stronger section identity |
| Final capstone | Most prominent node, uniquely relevant icon and tasteful framing |
| Optional side learning | Normal size in separate lane; explicit optionality in quest data and prose |

ATM10 uses diamonds, squares/rounded squares, hexagons, circles, gears and other shapes. **These are examples, not an imposed semantic mapping.** Choose a small TMM-specific set and verify each shape really renders in the pinned version before fixing exact names, borders, and sizes. Larger does not mean more XP or more important mechanical gating. Reserve attention-grabbing embellishment for genuinely meaningful milestones.

Do not encode the **only** meaning via color: rely on shape, location, icon, text and real dependency semantics. Use legible contrast and account for zoom, small screens and color-vision variation. Visual section names may be localized; avoid baked-in tiny English text where localization or readability would suffer.

## 6. Atlas -> quest -> presentation data contract

Maintain separately versioned, reviewable logical and visual sources:

- **Research (Atlas + mod comprehension):** exact pinned mechanic, objective, first obtainable point, real prerequisites, optional/alternative status, guidebook/JEI reference, current integration overlay, evidence and unknowns. Atlas research labels are NOT FTB quest IDs.
- **Quest logic:** stable chapter and quest IDs, task and detection truth, prerequisite graph, mandatory/optional status, owner-approved rewards and localization. Never infer a new hard gate from a line drawn for readability.
- **Presentation:** archetype, node positions, chosen icon ID and reason, shape/size intent, section grouping, image assets/placement/layer, source/rights information, and verification state.
- **Generated runtime:** deterministic localization and FTB SNBT produced from the preceding sources, with packwiz/index hashes updated by the recognized build process. Existing IDs and claim state survive cosmetic regeneration.

Recommended source fields (conceptual schema, **not already implemented as parser input**):

- At chapter level: **archetype**, **visual_theme**, **header_art**, **background_panels**, **decorations**, **sections** and a **presentation_schema_version**.
- Per quest, keyed by persistent FTB ID or a stable authoring key: **icon_item_or_texture**, **icon_represents**, **icon_source**, **icon_verified**, **position**, **node_role**, **shape**, **size**, **section_id**, **art_review_status**.
- Per image: **texture_resource**, **asset_origin/licence**, **x**, **y**, **width**, **height**, **rotation**, **layer_order**, **interaction** (default none), and **visibility/contrast QA state**.

Do not encode art-only attributes into gameplay prerequisites or rewards. Generator runs must be idempotent and preserve quest, task, reward, chapter and claim identifiers. Review any change to position/shape/icon/images as **cosmetic** only if no logic or reward changed.

## 7. Quality gates

**A. Offline/source validation (required for new/updated chapter presentations):**

- [ ] Source pin and Atlas/guide evidence identified for every substantive required stage; no unacknowledged coverage gap or stale mechanic.
- [ ] All quest IDs, task IDs, reward IDs, dependencies and localized descriptions stay stable/valid.
- [ ] Graph is acyclic and reaches intended endpoints; alternatives remain alternatives and optional lanes remain optional.
- [ ] Every quest icon is explicitly chosen and documented, not a placeholder; referenced item/texture exists in the actual pinned registry/assets.
- [ ] Every custom image path resolves inside **distributed TMM assets**, with original/safely reused art provenance. No ATM10 artwork or unbundled external resource reference.
- [ ] Node bounding boxes and imagery avoid obvious collisions and unreasonably long/crossing arrows by layout checks.
- [ ] Regeneration twice produces identical output/hashes (other than known generated metadata) and does not destroy art. Source-controlled presentation is authoritative.

**B. Client/UI acceptance (required before declaring chapter presentation accepted):**

- [ ] Open from a fresh and progressed world; title, illustrations and every quest icon render—not purple/black missing textures.
- [ ] At normal and reduced zoom, primary route and optional branches are visually apparent. All nodes, line directions and section labels remain understandable.
- [ ] Inspect dense hubs, top/left/right extents, large capstones, hover/click hitboxes and image layering; nothing obscures quest controls or localized text.
- [ ] Verify icon meaning, actual task credit, item textures/3D renders and dependency visibility with a player unfamiliar with the mod.
- [ ] Distinguish code/source QA from client rendering; a successful offline build, server boot, or snapshot is **not** a visual PASS.

For source tests use the repository's **A** offline class; use the existing **B** verified in-game commands and **C** manual client observation as appropriate. No full smoke/Prism build without the separate owner authorization already required by **AGENTS.md**.

## 8. First-chapter production checklist and boundaries

1. Select a **single** chapter whose Atlas has a source-backed quest map, pinned registry evidence and a recognizable guidebook/core progression. Reconcile existing FTB entries and preserve their stable IDs.
2. Turn research lessons into a **complete but not bloated** walkthrough with documented branch choices; not every Atlas row must be its own node.
3. Choose archetype and draw the dependency graph **before** placing art.
4. Assign meaningful icons to **100% of quests**; draft header art, at least one relevant major illustration/panel and section cues appropriate to chapter density.
5. Create original/approved visual assets, add their real packaged paths to presentation data, generate SNBT and run static checks.
6. In an **authorized** Prism QA build, verify navigation, images, zoom, icons, detection, localization and stable claims; repair based on screenshots/playthrough evidence.
7. Only then use the chapter as the archetype template for subsequent Atlas-derived chapters.

This document defines the **goal** and review contract. It **does not** create a new FTB image pipeline, imply that all ATM assets are available, import external IP, change any gameplay mechanic, authorize a build, or claim existing TMM quests have passed visual QA.
