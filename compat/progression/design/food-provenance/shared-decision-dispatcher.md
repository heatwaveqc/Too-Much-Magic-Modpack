# Shared dietary decision dispatcher — #444, coding slice 1

This Progression source slice adds one **pure, reusable policy decision** atop the
existing food-source and recipe nutrition evaluator. It is stacked after the
client preview bridge (#605), but it **never calls the operator food census**.

## Data-driven types

The existing `food_policies/default.json` defines affinity families, species
profiles, preferred raw nutrition and preparation/mixed-food rules. Affinity
profiles affect nourishment but do **not** reject off-diet or mixed food.

Additional datapacks can supply strict policies in
`data/<namespace>/tyr_progression/food_constraints/*.json`:

```json
{
  "schema": 1,
  "profile": "dryad",
  "type": "strict",
  "default": "deny",
  "allow_any": ["#tmm:foods/drinks", "#c:food/recreational"],
  "deny_any": []
}
```

All policy fields are validated on server resource reload and published
atomically alongside recipe/source evidence. Duplicate/overlapping strict and
affinity profile IDs invalidate reload; inferred source tags apply locally
without rewriting Minecraft's global item tags. The bundled Dryad rule is
**dormant until an authoritative Path/form adapter supplies the `dryad`
profile**. Its drink selectors are intentionally data-driven; the food
classification inventory must populate the intended tags. Native potions
without `DataComponents.FOOD` are outside the food interception path and
retain native use.

## One common result, no second set of food rules

`FoodDecisionResolver.decide(activeProfiles, itemID)` returns exactly one of:

- `NATIVE`: no veto or verified nutrition; leave the original provider alone
- `DENY`: a strict profile vetoes this food; never override it via affinity
- `PERMIT_NATIVE`: an explicitly magical food remains eligible with **native**
  nourishment, effects, charges, blood/mana or other costs
- `NUTRITION`: verified source/recipe evidence provides profile-specific
  nutrition/saturation; retain native item effect and use metadata

Precedence: known active profile → universal magical exception → any strict veto
→ lowest evidenced affinity nourishment → native fallback. Allow tags from one
strict profile merely lift **that profile's veto**; they do not make an item
edible through another policy or bypass native use. Unrecognized/unclassified
profiles do not grant permissions. Strict denies work without source inventory.

The existing Dragon Survival hook now uses this single resolver for its
supported exact-version player species. Separate Gilded/JAWR native handlers
are **not yet rewritten**: attaching their profile authorities and native
consumption gates is coding slice 2. In particular, no Dryad/JAWR/Gilded Path
is silently activated from an item tag or client hint.

## Client protocol

The server's policy generation and supported diet profiles produce a single
food preview. `FoodPreviewWire` now carries a distinct `D` (explicit deny)
beside `P` (permit with native properties) and numeric nutrition. Missing rows
mean **native fallback**, not denial. `ClientDiet.Projection` distinguishes a
present denial with intentionally null FoodProperties from a missing preview
entry, so the client-side DS return-value bridge can honor a veto without
re-enabling a denied beverage. This capability does not let clients request
dietary profiles; the per-player form/path adapter remains server owned.

## Next coding slice and non-goals

Next implement **native policy adapters** for actual active strict profiles,
JAWR food/penalty and special blood resources, and Gilded's stack-specific food
restriction/bonus. Supply the complete authoritative set of active profiles to
`FoodPolicyRegistry.decide` and to `clientPreview(Set<String>)`. The current
`DietPreviewSync` continues to supply only the native dragon species until
that adapter is completed.

This slice does not implement actual Dryad initiation, alter ordinary humans,
global FoodProperties, recipes or food values, replace Dragon Survival's indexed
legacy map, or claim any client/gameplay QA. Pure tests cover strict veto over
Forest, magical hybrid exception without invented nutrition, all-strict
intersection, malformed/duplicate policy records and explicit client denial.
