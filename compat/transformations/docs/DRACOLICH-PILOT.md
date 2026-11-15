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

**Resolved target ordering (pre-alpha implementation):** Dragon Survival's
native `OnDeath.trigger` is registered at NORMAL priority (source:
`DragonSurvival.java` line 82). For a genuine Dracolich, our HIGHEST-priority
handler calls the **native** dragon death passive once before either lich
provider's default death listener. If the dragon heals enough to cancel
death, **neither Arca nor phylactery is spent**. Otherwise the selected
lich's original handler runs as fallback. A narrow mixin suppresses a
second NORMAL-priority invocation for that same event. Test client-world
ordering and exactly-once ability costs before declaring runtime success.

**Hard exclusivity rule:** A Dracolich cannot acquire the other kind
of lichdom. Goety's native `LichdomHelper.setLich(Player, true)` is vetoed
if the dragon already has Forbidden Magic lichdom; Forbidden Magic's
native `LichdomLeveling.setLevel(ServerPlayer, positive)` is vetoed if the
dragon already has Goety lichdom. Native state is checked before mutation;
unknown state fails closed for dragon candidates. Same-provider upgrades
and cures are untouched. The classifier still rejects Dragon + both liches.

Dragon Survival also has `DragonFoodHandler` and `HungerEffect` abilities
that can add hunger/saturation/exhaustion. We must verify their interaction
with the chosen lich's fixed hunger value. Do not add a third food tick writer.

## Implemented first slice

- `DracolichPolicy`: pure classification and acquisition veto for the two
  legitimate one-lich Dracolich variants, a pure dragon, and the conflict.
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

No automatic acquisition transformation is introduced. A separate experimental
[Cave/Center visual pilot](DRACOLICH-CAVE-CENTER-PILOT.md) now implements
conditional skeletal rendering, skin/glow and cosmetic state synchronization.
Native dragon-first death and second-lich acquisition guards are active
in the pre-alpha transform module, but require server and client acceptance.
No new hunger, soul currency, resurrection destination or Dragon Survival
diet changes were introduced.

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
5. Test with/without configured dragon `OnDeath` passive: it must execute
   before the selected lich anchor, **exactly once**. When it rescues the
   player, spend 0 JAWR souls / 0 Goety Soul Energy; otherwise verify
   exactly one native lich anchor and no duplicate dragon passive cost.
   Test both mod load orders and an unrelated normal dragon control.
6. Test both soul resources empty, anchor missing, cross-dimensional
   anchors, and rejoin persistence; no invalid dragon-state removal.
7. Test normal dragon foods, forbidden foods and magical foods for each
   species; liches should not require ordinary meals just to survive.
8. Attempt both forbidden acquisition orders:
   **Cave/Forest/Sea + Goety -> Forbidden Magic** and
   **Cave/Forest/Sea + Forbidden Magic -> Goety**. Each must be vetoed
   before native state changes; verify ritual item/cost side effects
   separately and ensure a refused level does not show progress credit.
   Same-track levelling and curing must still work.
9. Verify non-lich dragon diet rules and standalone lich abilities
   remain unchanged. Creative/operator writes that bypass canonical
   levelling methods need separate negative testing.

## Skeletal appearance target (separate cosmetics workstream)

The user-approved next visual goal is an unmistakably **skeletal dragon**
for all three Dragon Survival species, without replacing species-specific
models, growth, hitboxes, breath abilities, wing animations or armor.
Design as an undead skin / render-layer transformation controlled by the
*actual* Dracolich state, never a new lich item or always-on Dragon Survival
model replacement. Verify juveniles and adult sizes, first/third person,
flight, multiplayer clients and cure to original appearance. This is
**not implemented** in the resurrection/acquisition guard patch.

## Release gate

The integration is compiled in `tmm_transformations`, but does **not**
establish that the two native transformation rituals accept a dragon,
that a direct native capability/NBT mutation cannot bypass our canonical
entrypoint guards, or that the prioritization works in every event-order
configuration. Those are client acceptance tests before alpha.
The Cave/Center skeletal appearance is a separate implemented visual pilot,
with in-world client acceptance still required; other species/bodies remain planned.
