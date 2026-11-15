# Dracolich pre-alpha integration pilot — Minecraft 1.21.1

**Target:** Dragon Survival 2.0.74 Cave, Forest or Sea dragon **plus exactly one**
lich provider: Goety 3.2.2 (Arca) **or** JAWR Forbidden Magic 0.5.37
(phylactery). Dragon + both lich sources remains **CONFLICT**, not an Archlich.

## Ownership (mechanical target)

| System | Native owner | Pilot action |
| --- | --- | --- |
| Species, model, size, growth, breath, flight, dragon mana | Dragon Survival | Preserve native dragon state and abilities |
| Lich progression and soul resources | The one chosen lich mod | Preserve native progression/costs |
| Hunger and saturation | Chosen lich | Goety keeps 17; Forbidden Magic 12/0 from pinned code; no third TMM hunger writer |
| Revival on fatal damage | Chosen lich | Goety Arca **or** JAWR phylactery, not both |
| Food limitations | Dragon Survival data-map diet | Retain existing three-species food rules for food items; lich hunger renders nutrition largely unnecessary; magic-food exception for liches remains pending |
| Cures | Individual upstream providers for now | Do not auto-remove dragonhood on lich cure or fake an atomic cure |
| Undead affiliation | The chosen lich state | Verify mob alliances on native flags, not on a made-up dragon entity |
| Appearance | Dragon Survival | Skeletal/undead skin treatment is a separate art/design target, not implied by the current renderer |

**Why this can be simpler than Archlich:** A dragon with *one* lich provider
normally has a single lich death anchor. There is no Goety-vs-JAWR
resurrection race unless a second lich state has been acquired, which the
current form classifier rejects.

**Discovered conflict to investigate:** Dragon Survival defines an
`OnDeath` ability trigger that may heal a player and cancel the
`LivingDeathEvent`. Its `DragonFoodHandler` changes food properties, and
its `HungerEffect` abilities can add hunger/saturation/exhaustion. We must
measure trigger order and whether any active dragon ability overwrites the
fixed lich hunger value. Do **not** globally disable dragon abilities
or impose a third 20-tick food reset without evidence.

## Implemented first slice

- `DracolichPolicy`: pure classification for the two legitimate one-lich
  Dracolich variants, a pure dragon, and the two-lich conflict.
- `/tmmtransform dracolich`: *read-only* native probe using the pinned
  upstream public methods:
  `DragonStateProvider.isDragon(Entity)` and `getData(Player).speciesId()`;
  `Goety LichdomHelper.isLich(Player)`; and
  `LichPlayerAttachment.getData(Player).isLich()/getLevel()`.
  Prints actual species, lich flags, JAWR level, current food and saturation,
  and native resurrection owner. Unknown API or mismatched version fails
  closed to a diagnostic message.
- `DracolichPolicyTest`: positive Goety/JAWR cases, pure states, and
  forbidden dual-lich dragon conflict.

No automatic transformation or forced visual overlay has been implemented.
No new hunger, soul debit, resurrection or Dragon Survival diet logic was
introduced in this first slice.

## Disposable-world QA matrix

1. Create a separate test player/world for each Cave/Forest/Sea species
   with Goety lichdom, and each species with Forbidden Magic lichdom
   (**six** variants), plus one plain-dragon control and two plain-lich
   controls.
2. Run `/tmmtransform dracolich` before and after lich acquisition,
   re-login, dimensional travel, natural/canceled death and cure.
   A valid combination should name the native **Arca** or **phylactery**.
3. Verify species, body, growth stage, skills, breath and wings remain
   intact across all lich events and after resurrection.
4. Verify chosen lich fixed food level (Goety 17, JAWR 12) is stable
   during standing still, sprinting, exertion and dragon spells,
   especially abilities using `HungerEffect`; check saturation too.
5. Test with/without configured dragon `OnDeath` passive:
   note whether it first cancels death through healing; if it cannot
   save the character, verify the single chosen lich anchor acts once.
   No double soul cost, duplicate healing, death-loop or teleport race.
6. Test both soul resources empty, anchor missing, cross-dimensional
   anchors, and rejoin persistence; no invalid dragon-state removal.
7. Test normal dragon foods, forbidden foods and magical foods for each
   species; liches should not require ordinary meals just to survive.
8. Verify non-lich dragon diet rules and standalone lich abilities
   remain unchanged. Never enable two lich sources for a dragon unless
   testing the explicitly rejected CONFLICT state.

## Release gate

The first slice can compile and be packaged in the existing
`tmm_transformations` module. It does **not** establish that dual
dragon/lich acquisition works, that a Dragon Survival player can satisfy
both mods' rituals, that the intended death-event ordering runs, or that
a skeletal skin is available. Those are client acceptance tests before
the pack can approach alpha.
