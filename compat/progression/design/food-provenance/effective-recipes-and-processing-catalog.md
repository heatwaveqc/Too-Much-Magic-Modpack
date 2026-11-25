# Effective recipes and processing catalog — #444 coding tasks 3 + 4

Source-only Progression slice built on #614 -> #608 -> #605 -> #600 and earlier
food provenance PRs. No inventory pass or world boot required to write the
runtime path; the *food census* remains an optional administrator QA source.

## 1. Evidence is reviewed input, not live truth

`FoodRecipeEvidence.complete: true` is not an authority to override food
nutrition. Server verification now runs against the **effective, current**
Minecraft `RecipeManager`, after datapack loading has completed. For every
item with a sidecar recipe, validation requires:

- Every sidecar recipe ID still exists and produces that exact output item.
- **Every currently active recipe** with that same output item appears in
  the sidecar. Another mod / KubeJS recipe may not silently add an alternative.
- Output count, method and all resolved item-ingredient alternatives match.
  Each repeated ingredient slot is counted; duplicate ingredient multiset
  equivalents may be normalized, but distinct receipt IDs are not dropped.
- A method/serializer/ingredient that cannot safely be inspected, or whose
  output preview is empty, fails closed instead of guessing.

An invalid/changed output is **blocked**, not merely removed from the recipe
map: even if a similarly named source/root record exists, its prior nutrition
must not be used for the blocked item. The existing recursive evaluator
propagates an invalid child to any crafted food depending on it, returning
native food properties as the safe fallback. A missing record likewise never
grants an invented nutrition override.

A custom serializer with no discoverable output is tracked by receipt, but
may have production methods invisible to `getResultItem`; those additional
dynamic production paths must be reviewed in native QA rather than described
as proven by the snapshot. The fallback is intentionally conservative.

## 2. Reload-scoped performance / consistency

FoodPolicyRegistry's new `RuntimeSnapshot` is identified by server,
recipe-manager identity and its atomic food-policy generation. The first
server-side projection/use after reload builds **one** `FoodServerRecipeSnapshot`,
validates all declared recipe outputs and shares an immutable
`FoodDecisionResolver`. Per-profile/item `FoodRecipeEvaluator` outcomes
are memoized in a cache belonging only to this snapshot. A food-use tick
**does not copy the entire source/route map or rescan recipes**.

Datapack `/reload` atomically publishes new policy, source, route, strict
activation and processing config. This increments the generation and clears
the runtime snapshot; the next player projection uses the new current
RecipeManager and tag membership. The same validated resolver serves both
server decision and client S2C nutritional projection. The server-stopped
listener releases the old snapshot. No client can supply its own recipe
catalog or validation status.

## 3. Shared configurable processing rules

The data file
`data/tyr_progression/tyr_progression/food_processing/default.json` is
shipped in the Progression JAR and read by both Java and the Python compiler.
It defines known recipe types and method (HEAT/NONTHERMAL/CHAR), thermal-tool
tag tokens, neutral item IDs/tag prefixes, ingredient-only material families,
reviewed output target/exclusion tags and conservative thermal-name
uncertainty warnings. Unknown type/ingredient shape is held; no processing
type is inferred merely from an item name. The schema is validated, including
type IDs, repeated selectors and unknown fields.

The compiler accepts
`--processing-config <reviewed JSON>` for a pinned alternate catalog.
Adding a new known stove, mixer, juicer, cold processing recipe type or
neutral tool is **data-only** once its actual native recipe signature has
been reviewed; it no longer requires altering Python sets. Java still
rejects recipes whose *live* input/options differ from compiled evidence.
The pure tests exercise a new custom heat station and new thermal tool token.

**IMPORTANT correction to the old compiler:** it no longer collapses distinct
recipe IDs with equivalent nutrition nor replaces tag-member alternatives
with one representative when their source budgets coincide. That old
compression would defeat full active-recipe validation after /reload.

## 4. Explicit non-goals / integration handoff

- This PR does **not** promote the ~57 generated candidate recipes from #590,
  resolve its held outputs, or claim entire installed food coverage. Any
  candidate data promoted later must be regenerated with the same source/pin
  recipe catalog and pass live validation.
- No KubeJS, Packwiz, pinned native JAR, or shared integration branch changes.
- No Gilded canonical-tag migration, permission-only beverage nutrition,
  JAWR cuisine hook reconciliation or custom-use Magical food repair; those
  are the separately documented compatibility-hardening slice.
- Source-only Java/JUnit and Python regression/CI are useful but **not** evidence
  of in-world recipe behavior. An authorized QA build should test recipe
  replacement, extra KubeJS alternatives, /reload, client preview and
  two-player diet isolation before gameplay acceptance.
