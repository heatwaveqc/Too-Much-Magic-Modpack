# Shared food policy: tag-driven and configurable (#444)

Source-only first slice, 2026-11-22. Not a live Dragon Survival diet replacement.

## Architecture

Progression is the single server-authoritative food handler. The recipe/loot provenance classifier (#463) contributes shared item tags; data packs may add or remove tag members normally. The dietary engine reads those tags at lookup time rather than caching a hand-maintained registry of foods. No food is hardcoded by item ID in the policy.

A data pack can override:
data/tyr_progression/tyr_progression/food_policies/default.json

Schema 1 supports preferred_raw, stage_multipliers, mixed.off_diet_contribution, family_tags, profiles, preparation_tags and magical_tags. Selectors are tags only. Arbitrary physiology profiles can declare their preferred source families without new Java. Source mods own their native food and special-use effects.

## Owner-locked defaults

- Raw, cooked and charred are the only preparation stages. Default remaining nourishment is 1, 0.5, 0.25, and both hunger and theoretical saturation *points* scale by the same factor.
- Preferred eligible whole raw food: 6 nutrition, 2.0 saturation modifier = 24 theoretical saturation points.
- The default off-diet source contribution is 0.5. A recipe containing two raw ingredients, one preferred and one off-diet, has the weighted benefit of 1.5 preferred source units. A wholly off-diet, source-classified raw item receives a half-strength baseline, but unclassified items keep native behavior.
- Multi-ingredient composition must be source-verified; do not guess ingredient percentages from tag overlap, names or a broad ingredient tag. A half raw fish portion passes 0.5 source units, minced beef passes 2/3 source units per output, and two fruits processed into one juice pass two source units. Preserve native output counts and recipe-aggregate conservation.
- Hunger points are integers. Raw 6 => cooked 3 => charred 2 (round to nearest); theoretical saturation 24 => 12 => 6. Charred therefore needs saturation modifier 1.5 so saturation is actually quartered rather than quartered twice. A recipe-aware generator must allocate fractional remainders across multi-output recipes and avoid reversible synthesis loops.
- Magical-tagged food is universally eligible but receives no fabricated nutrition or extra custom-use credit. The native item retains effects, blood/mana debits, hunger restrictions, durability, relic charges and cooldowns. Golden is not automatically Magical.
- No new Eternal food counterparts in #444. Existing special infinite/rejuvenating foods remain independently compatible.

## Code contract

FoodPolicyRules is a pure validator and nutrition calculator. FoodPolicyRegistry is a reload listener with atomic validated publication. Its evaluate(profile, stack, rawSourceUnits, preferredShare) method can consume provenance evidence; absent or ambiguous evidence returns empty and must preserve the native provider result. A source family without a verified preparation tag also falls back. A stronger heat stage tag wins over raw family tags, so processing does not reset history.

This first PR intentionally does not intercept food use or mutate DataComponents.FOOD, native Dragon Survival maps, shared Packwiz hashes, Gilded/JAWR food mechanics or client display. It installs the *policy contract*, not a premature runtime override.

## Integration sequence / QA

1. Finish the pinned-JAR edible registry and recipe/loot/provenance census in the separate classifier lane. Resolve actual tag membership, ingredient variants, tools and output count, including Nether raw-fish exceptions, fungi, sea plants, fish slices, minced beef, stock and prepared dishes. Unknown IDs stay in the audit queue.
2. Feed that source evidence to the Progression resolver and write one provider-aware runtime adapter. Keep native side effects and other player/class food values unchanged. Introduce a coherent client preview and reload lifecycle.
3. Ensure generated shared tags merge without replacing external contributions. Only the designated integration writer may atomically promote revised Dragon Survival maps, indexed Packwiz metadata or runtime manifests.
4. Type-A regression includes fractions, stages, mixed-source weighting, source portion yields, universal magical exemption and failed classification. B/C gameplay requires separately authorized full integration: JAWR/Gilded/Dragon/other Paths, foods and recipes, reload, login, multiplayer, dynamic datapack tags, effects and food component isolation.

No full smoke, Prism build, common integration branch mutation or gameplay PASS implied.
