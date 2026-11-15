# Werewolves food tags — Relics compatibility patch (2026-11-14)

**Scope:** next full-integration/unstable QA, not Stable. No changes to the dragon food rules, the Werewolves skills, or either lich hunger hook.

## Source diagnosis

In the full-integration run #72 server log, Minecraft's tag loader failed to load
`werewolves:werewolf_food` because **Relics 0.12.5** supplied a required reference
to the unavailable item `relics:infinity_ham`. This exact error has been
reported upstream: [Octo-Studios/relics #286](https://github.com/Octo-Studios/relics/issues/286).
The issue was closed after the maintainers stated it was fixed for a later
update, but the currently pinned JAR still produced the error.

The patch intentionally does NOT create a fake `relics:infinity_ham` item.

## Patch

- `kubejs/data/werewolves/tags/item/werewolf_food.json` is a **replace:true**
  resource override of the broken combined tag. It preserves the six original
  native Werewolves entries (raw food, cooked fish, vanilla meat, wolf berries,
  sweet berries, glow berries) and includes the Relics ham **only if its item is
  really registered** via `{"id":"relics:infinity_ham","required":false}`.
- The optional `#werewolves:cooked_food` inclusion keeps modded cooked meats
  available without removing the addon’s native foods.
- Four **additive (replace:false)** tag fragments link the contemporary
  `c:foods/raw_fish`, `c:foods/cooked_fish`, `c:foods/raw_meat`, and
  `c:foods/cooked_meat` categories into their Werewolves counterparts,
  which should cover Aquaculture 2 and tagged Farmer's Delight meats.
  All tag references are optional so an absent source tag does not break
  the destination.
- The override is deliberately narrow but could supersede third-party
  direct additions to `werewolves:werewolf_food`. Future additions should
  be reintroduced through a preserved category or tested against this
  replacement. Remove the override if a future Relics version fixes the
  underlying reference and clean tag merging works.

## Runtime test

1. Build the full pack with KubeJS's datapack enabled, not a bare mod-only server.
2. Confirm **no** `Couldn't load tag werewolves:werewolf_food` and **no**
   `relics:infinity_ham (from mod/relics)` on first load or `/reload`.
   A successful server boot **alone** is not sufficient.
3. As a basic Werewolves player without `Not Meat?`, consume vanilla raw beef,
   cooked steak, Farmer's Delight raw bacon, cooked bacon, Aquaculture raw
   fillet and cooked fillet. Check survival hunger restoration and any raw-meat
   bonuses.
4. Test Aquaculture's individual raw fish, not just `aquaculture:fish_fillet_raw`.
   Also try one cooked Aquaculture fish/fillet and a Vampire's Delight werewolf
   food item.
5. Confirm bread remains restricted before acquiring `Not Meat?`, while
   `werewolves:wolf_berries`, sweet berries and glow berries remain edible.
   After unlocking `Not Meat?`, recheck bread.
6. Retest after a logout/rejoin and when both vampire mods are present.
7. Run `python3 scripts/verify_werewolf_food_tags.py` for the static
   contract; it is not a substitute for world testing.

## Lich note — separate proposal only

Forbidden Magic's pinned 0.5.37 code fixes lich hunger to **12** and saturation
to **0** (with a vampire exception), while Goety documents a constant hunger
value of **17**, allowing eating but avoiding normal >=18 food-based natural
healing. The two fixed values will compete for a player holding **both**
lich states.

A future *Archlich* implementation could align the effective hunger to **17**
and saturation to **0**, with Goety owning undead healing via Soul Energy and
Forbidden Magic's phylactery/soul mechanics preserved. This would remove the
simple fixed-value oscillation, but **not** by itself reconcile vanilla hunger
tick cancellation, hunger HUD, exhaustion, eating, or two resurrection
implementations. Do not add a global food-level tick script or patch either
mod's magic/respawn logic without testing. This is not implemented in this PR.

**Verification:** static authored-tag check before promotion; next combined
server log and real client gameplay required.
