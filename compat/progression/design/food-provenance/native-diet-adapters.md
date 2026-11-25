# Native dietary adapters — coding slice 2 for issue #444

Source-only follow-up to the shared resolver (#608) and client bridge (#605).
No food inventory, food census, indexed Dragon Survival diet map, global FOOD
component mutation, module migration or world boot is required.

## Unified authoritative pipeline

1. `NativeDietAdapters.activeProfiles(ServerPlayer)` reads a *verified* native
   Dragon Survival 2.0.74 species and the set of independently activated strict
   policies from the same immutable food-registry snapshot.
2. Each strict policy has a typed server-side `when` eligibility predicate.
   For example the Dryad rule references `path_active` with
   `tyr_progression:dryad`; no matching eligible joined Path means no Dryad
   restriction. Policies without `when` are **dormant**. Future Path rules
   use the same generic condition engine; no class-specific Java branch.
3. The shared `FoodDecisionResolver` combines strict veto, nutrition and
   magical-native permissions. `NativeDietPolicy.intersect` adds owned
   Gilded-style food restrictions. **One deny beats nutrition.** A positive
   allow from one profile never bypasses another.
4. `NativeDietAdapters.foodStart` and `foodTick` enforce the same decision
   on the server, replacing Gilded's separate event registrations. Dragon
   Survival's IItemExtension hook also calls this same decision. Native item
   consumption, potion effects, return containers and resource debits remain
   upstream-owned.
5. `DietPreviewSync` projects active profiles and active food-related effects
   into the existing bounded, one-way S2C packets. Changes in Path eligibility,
   native Dragon species, purchased/toggled food skills, `/reload`, respawn
   and dimension change invalidate the preview. The packet contains explicit
   denial, permission-only and nutritional entries, never recipes or blood
   meters.

## Gilded parity

The original `food_restriction` owned effect is evaluated using its original
selector, Golden recipe closure, lava-caught `minecraft:custom_data` and
recreational drink permissions. No universal whitelist of cooked fish/chicken
is inferred from a lava-caught stack. The approved Gilded baseline alcohol
option is a **declarative effect parameter** `baseline_alcohol=true`, checked
only against the explicit `#c:food/alcohol` tag and DRINK animation. The Gilded
Path author must add that parameter to the owned effect; absence preserves
existing installed behavior. Magic retains its native use/effects without an
accidental Golden bonus, blood or mana grant.

A native item may obtain food properties without a static default FOOD
component. The server gate still uses the existing Gilded food test for such
items. The generic policy and default-stack client projection intentionally
do not invent nutrition for them.

**Stack-specific client limitation:** an owned effect with `lava_caught=true`
could allow a flagged variant of *any* food item. Its unflagged item-ID-wide
client DENY is omitted to avoid incorrectly suppressing that valid flagged
stack. The server always checks the actual custom data on use. This can be
extended later with an explicit per-stack preview predicate without adding
a second food policy engine.

## JAWR 0.5.39 native Vampire penalty

Pinned disassembly: `VampireEvents.onItemUseFinish` calls
`isSolidFood(ItemStack)`, which applies ordinary-food Hunger and Nausea on
success. A separate JAWR werewolf food-penalty path was NOT established.
The optional exact-0.5.39 mixin intercepts only this one predicate at finish.
Its pure `NativeDietPolicy.suppressVampireFoodPenalty` result requires:

- A real JAWR vampire, server-side component-backed food and no applicable
  strict/Gilded veto
- One of: already approved universal magical nourishment; explicitly tagged
  `#tyr_progression:jawr_vampire_safe_foods`; or a tagged
  `#c:food/recreational` DRINK item with an earned diet permission

It never changes `LivingEntityUseItemEvent.Finish`, consumes an extra item,
clears non-diet potion effects, adds blood, overrides the JAWR blood pool or
extends a Werewolf diet. The broad `#c:food/bloody` tag does not automatically
make a food safe nor confer blood-meter credit. Exact blood feeding for native
JAWR/Hexerei/Iron's bottles remains in `JAWRBottleInterop` and native JAWR
handlers. New Blood Wine/Sausage/Pie Slice under issue #450 must supply
separate source-verified bottle economics and explicit safe-food membership
before claiming complete cuisine support.

## Verification, boundaries and follow-up

Pure JUnit tests cover native restriction intersect, no speculative JAWR
werewolf penalty, earned recreational permission vs ordinary solid food,
strict policy `when` activation/dormancy, and existing hybrid resolver and
client wire cases. Module CI compiles the mixin and source classes but cannot
verify bytecode injection success, JAWR event ordering or gameplay outcomes.
The optional mixin is enabled only with JAWR 0.5.39 via the existing native
mixins plugin and must be checked against that exact pinned artifact in
owner-authorized runtime QA before gameplay acceptance.

**Not part of this PR:** item inventory/census; recipe graph coverage;
#450 blood-food item/recipe registrations; new Dryad Path implementation;
`baseline_alcohol` Path data migration; full integration or Prism build.
