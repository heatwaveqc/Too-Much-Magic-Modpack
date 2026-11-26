# Roots Classic 1.5.10 — capability evaluation

2026-11-25. Atlas #410, owner-requested addition to the witchcraft comparison (#767). Separate tradition, not a combined score. [29-axis factual dossier](roots-classic.md), [quest map](roots-classic-quest-map.md), [exact spells and items](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/a86c90befd8dd5dca11c7883590ea49858553e15/docs/mod-comprehension/mods/roots-classic-pinned-spells-items.md), [46 exact rituals](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/a86c90befd8dd5dca11c7883590ea49858553e15/docs/mod-comprehension/mods/roots-classic-pinned-rituals.md). Active pin verified: MC 1.21.1 NeoForge, RootsClassic-1.21.1-1.5.10.jar, SHA1 7d02ec46e4d276ebf556cdd529a75108b4724efd, CF 295780/8860609; earlier hash-matched JAR and [source db9aa883](https://github.com/Lothrazar/RootsClassic/tree/db9aa883b7a54a1558dbc710546be1f42544be72) evidence reused. Not Roots 3/4. No new JAR inspection or gameplay test.

| Domain | Score | Evidence and rating boundary |
| --- | ---: | --- |
| Combat | 4 | 22 component spells with potency/efficiency/size modifiers, lightning, projectile/area effects, life drain, Engraved Blade, temporary Phantom Skeleton (not proven owner-bound). |
| Protection | 3 | Native armor, Shadowstep damage evasion and defensive crowd control; no comprehensive ward system. |
| Survival | 4 | Self-repairing living tools, Wildwood regeneration, Peony and Pink Tulip healing, Apple effect clearance, persistent Healer stone. |
| Gathering | 3 | Azure Bluet block breaking, limited common-block Blue Orchid duplication, living tools, 12 entity summon rituals; no general ore generation or autonomous mining. |
| Industry | 2 | Mortar/Imbuer, 24 crafting rituals, Oxeye Daisy acceleration of entity/block-entity ticking; strong auxiliary acceleration, no independent industrial/refining system. |
| Construction | 2 | Azure Bluet excavation, Blue Orchid common-block spikes, Lily Pad water-state placement, terrain/growth effects; no broad building system. |
| Transportation | 3 | Chorus look-direction teleport, Dandelion motion, Accelerator stone, Dragon's Eye; no persistent gateway network or native sustained flight demonstrated. |
| Logistics & storage | 1 | Vacuum/Repulsor affect dropped ItemEntities only, no routing/inventory insertion or general remote storage. |
| Life & agriculture | 5 | Growth Powder, Lilac spell, large-area Grow ritual, unattended Grower stone, mass breeding, animal summons, rare plant mutation and renewable bark/crop ingredients. |
| Death & recovery | 0 | Healing and damage effects do not establish lethal interception, resurrection or inventory recovery. |
| Advancement | 3 | Four mixing-base tiers, altar levels 0–2, four-slot Crystal Staff, alternative armor sets and blade modifiers; no permanent player tree or Terra tier. |

**30/55, 7/11 independent, 1 transformative, Substantial Magic.** Scale 0 absent, 1 some assistance, 2 significant assistance, 3 independent, 4 superior, 5 transformative. Same rubric as witchcraft comparison; native-only evidence, no borrowed capabilities or custom TMM Progression hooks. Editorial judgment, not power benchmarking.

## Mechanic-specific boundaries

- All 22 spell components and 46 rituals previously audited (24 crafting, 12 summons and ten other individual ritual effects). Three ritual structure levels, not character levels. Four powder mixing bases permit 0/1/2/3 modifier slots; personal Terra maximum 40 and nominal 1 per five ticks, not the unused advertised 15-tick regen setting. Ordinary staff uses 15+15 per efficiency level; use decremented before Terra affordability. Crystal Staff holds up to four spells, uses personal Terra but no finite staff-use counter. Level-2 ritual creates staff; separate level-1 rite imbues existing staff. No ground restriction or health fallback found in actual handler.
- Healer stone applies Regeneration II for 500 ticks every 20 ticks to LivingEntities within ±10 XYZ, including hostile mobs; Accelerator applies Speed II with same bounds and cadence. Grower shuffles 9x9 positions every 100 ticks with 3% per position growth attempt; level-0 Grow checks 35x35x9 region with 1/12 independent chance for eligible blocks. Mass Breeding sets actual animal love state subject to native age/cooldown/other checks, no per-animal food payment in inspected method. Do not promise instant maturity or universally compatible crops.
- Oxeye Daisy base Terra 14, creates temporary target accelerator; entity helper calls tick and baseTick P+2 extra times, block-entity helper calls its ticker P+2 times; lifetime 200+200*size ticks. This is powerful complementary industry, not its own production network. Effective modded device acceleration, idle/block replacement handling and performance remain separate integration tests.
- Engraved Blade Shadowstep held-item chance to cancel incoming damage 12.5% per level; maximum four bark modifiers total. No Forceful effect consumer identified. Runic Focus/Charged Focus likewise no verified active consumer, not a power network. Sylvan full set +1 spell potency, Wildwood full set 2% chance each eligible tick to heal 1 HP; self-repairing equipment 1/80 chance per relevant tick. Neither increases Terra regeneration. The factual Atlas corrects earlier spell appendix's Sylvan ritual tier typo: level 2, not level 1.
- Chorus base Terra 12, moves along look by 8+8*potency, no explicit safe-landing scan. Azure removal and Blue Orchid placement need claim checks. Life Drain healing budget uses requested damage rather than observed health loss. Sacrifice subtracts health from first queried entity, potentially a player; not safe animal-only ritual. Prior documented source hazards remain, not newly authorized fixes.
- Roots Classic provides a strong standalone druidic role: agriculture, regional passive magical benefits, combat/healing and exploration. No general-purpose storage, high-throughput industry, death save or broad construction. No added quests, recipes, config, pin, builds or code.

## Batch handoff

Add this fifth independent evaluation to #767, retaining the original four unchanged. JAWR 46 (Major), Hexerei 35, EvilCraft 35, Enchanted 33, Roots Classic 30 (Substantial). Preserve source dossiers and coordinate shared index later. Next programmed magic batch Psi, Hexcasting, Scriptor Magicae.
