# Magical nourishment exceptions — next food compatibility pass (2026-11-14)

**Status:** Dragon Survival diet data map, Werewolves addon tags, and static guard tests **implemented** on feature and `test/full-integration` branches. **In-world runtime QA and special-item consumption verification remain pending; not stable-approved.**
**Branches:** `feature/dragon-diets-food-compat` and `test/full-integration` (unstable source). Stable/main remains unchanged. New builds from integration pick up config; an already-exported Prism instance does not update itself.
**Dependencies:** Dragon Survival 2.0.74 (NeoForge 1.21.1), installed magical-food providers, and the two werewolf/lich systems.

## Governing policy

Some specific magical foods are exceptions to ordinary species diets **for forms with meaningful, variable hunger and saturation**. Explicit inclusion must never remove the item's native replenishment cost, cooldown, reuse behavior, healing, effects, or resource accounting.

This is an explicit **magical nourishment allowlist**, not a blanket exemption for all modded food, all items with potion effects, or all enchanted/rare dishes.

The pre-existing species food rules continue unchanged:
- Cave: species-native foods and native second-stage charred meat/seafood, no generic cooked meals and no raw-plus-coal charring shortcut.
- Forest: fruits, vegetables, mushrooms, sugar and eligible plant preparations, never grains/ordinary cooked meals.
- Sea: raw fish, seafood, sea plants and compatible grain-free preparations; cooked fish and rice sushi excluded.
- All dragons: no ordinary grain/cooked-food bypass. Distinct Dragon Survival specialty foods remain as already configured.

## Explicit treatment

| Item | Human | All three dragons | Werewolves (both player systems) | Liches (both systems) | Notes |
| --- | --- | --- | --- | --- | --- |
| EvilCraft Rejuvenated Flesh (`evilcraft:flesh_rejuvenated`) | Normal native behavior | Magical exception | Magical exception | Exclude pending specific hunger check | Preserve blood-recharge durability and any native blood cost. |
| Botania Fruit of Grisaia (`botania:infinite_fruit`) | Normal native behavior | Magical exception | Magical exception | Exclude pending specific hunger check | Preserve mana cost, activation, and reusable behavior. |
| Botania Mana Cookie (`botania:mana_cookie`) | Normal native behavior | Magical exception | Magical exception | Exclude pending specific hunger check | Preserve original magic/hunger/saturation behavior. |
| Artifacts Everlasting Beef (`artifacts:everlasting_beef`) | Native behavior | **Cave only** (raw meat) | Allowed in both | No extra permission | Not universal magical nourishment; preserve original item functionality. |
| Artifacts Eternal Steak (confirm registry ID) | Native behavior | **No** dragon exception | Native werewolf rules only | No extra permission | Cooked: deliberately excluded as an exception. |
| Pastel Enchanted Star Candy (`spectrum:enchanted_star_candy`) | Native behavior | **Proposed** magical exception | **Proposed** | Exclude pending check | Clears all harmful effects, has 8 base nutrition; it does **not** fully heal or fill hunger. |
| Pastel Enchanted Golden Carrot (`spectrum:enchanted_golden_carrot`) | Native behavior | **Proposed** magical exception | **Proposed** | Exclude pending check | Verify original effects and installed ID. |
| Pastel Magnificent Star Candy (`spectrum:magnificent_star_candy`) | Native behavior | Magical exception | Magical exception | Exclude pending check | **This** is the full-heal/full-hunger candy; preserve its one-time custom on-use behavior. |
| Vanilla Enchanted Golden Apple | Native behavior | **Candidate** for universal exception | Candidate / native | Exclude pending check | Do not silently promote this candidate; Forest already accepts this item. |
| Ordinary Golden Apple | Native behavior | Existing species rules | Native rules | Native rules | Still fundamentally fruit; **not** a universal exception. |
| Pastel ordinary Star Candy | Native behavior | Existing species rules | Native rules | Native rules | A potion effect by itself does not make it universal. |

Vampires are a separate follow-up: regular Vampirism uses blood / blood saturation, and ascended JAWR vampires have additional custom blood logic. Do not substitute ordinary hunger restoration for blood or accept a mere eating animation as working nourishment. Keep resource/hunger authority with the actual provider and test explicitly before granting magical-food exceptions.

Goety liches have special nonstandard hunger; JAWR Forbidden Magic lich hunger remains to be checked on the pinned JAR / in-world. Neither should be automatically targeted merely because their game objects support an eating interaction.

## Existing-map discrepancy to fix in the next pass

At `test/full-integration` commit `4354001fd7da05528d3fae816c10287d7ea58f4a`, `kubejs/data/dragonsurvival/data_maps/dragonsurvival/dragon_species/diet_entries.json` contains:
- **Forest Dragon**: `artifacts:everlasting_beef` with nutrition 0 / saturation 0, plus `minecraft:rotten_flesh` and `born_in_chaos_v1:monster_flesh`. The latter two also warrant a diet-policy audit.
- **Cave Dragon**: no `artifacts:everlasting_beef` selector.
- **No species** has explicit EvilCraft Rejuvenated Flesh, Botania Mana Cookie / Fruit of Grisaia, or Pastel enchanted-food selectors.
- `minecraft:enchanted_golden_apple` is present only under Forest.

Correct Everlasting Beef to Cave only, remove the unintended Forest treatment, and test that its original reusable raw-meat behavior survives. Never infer food success from a nutrition=0/saturation=0 entry.

## Implementation/validation gates

1. Inspect **installed pinned JARs** and actual item registry IDs, food properties, item-use handlers and any cooldown/blood/mana costs before writing selectors. The canonical Dragon Survival override replaces the source data map; update it carefully without dropping existing native entries.
2. First try explicit Dragon Survival diet entries with `retain_effects` where applicable. **Do not assume** a data-map entry preserves custom `Item#finishUsingItem`, reusable return/stack behavior, or blood/mana resource consumption. If it doesn't, use the narrowest targeted compatibility hook; do not run costly per-item per-tick scans.
3. Preserve the intended normal food restrictions and no Cave charring fallback. Keep global player food properties, `c:` tags, and original recipe mechanics unmodified.
4. Test Human, Cave/Forest/Sea, both werewolf systems (JAWR human/wolf and Werewolves addon with/without Not Meat?), Goety lich, JAWR Forbidden Magic lich and vampire cases **separately** in a disposable client world. Check depleted hunger/saturation, whether eating completes, net gains, item reuse/durability, blood/mana deltas, native effects, cooldowns, rejoin, and server/client consistency.
5. Verify exact **one-time** nutrition/effect application and no resource duplication, double-healing, extra item consumption, or cost-free repeat use. Test negative foods on all three dragons after world reload.
6. Extend `scripts/verify_dragon_diets.py` with selector and exclusion assertions, and add gameplay acceptance cases to the existing next-full QA checklist when implemented. Static QA isn't sufficient for live KubeJS or custom item handlers.
7. Promote to unstable combined test **only after** an isolated compatibility implementation passes relevant static checks; stable promotion remains gated on practical QA.

Existing context: `docs/DRAGON-DIETS-2026-11-14.md`, `docs/FOOD-AND-SPECIES-DESIGN.md`, and (on `test/full-integration`) `docs/JAWR-FOOD-TEST-PLAN-2026-11-14.md`.

## Deployed source changes (2026-11-14)
- Dragon Survival explicit map: `kubejs/data/dragonsurvival/data_maps/dragonsurvival/dragon_species/diet_entries.json`, all three species include the six approved magical foods. The two custom-use non-FoodProperties items (`evilcraft:flesh_rejuvenated`, `botania:infinite_fruit`) receive **0 nutrition / 0 saturation** in the diet map to permit dragon recognition without inventing free food; their native handlers must deliver the actual nutrition/resource cost.
- `artifacts:everlasting_beef` transferred from Forest to Cave; `minecraft:rotten_flesh` and `born_in_chaos_v1:monster_flesh` removed from Forest. No new Eternal Steak exception.
- Forest `ars_nouveau:sourceberry_bush` is enabled **using original FoodProperties and retain_effects**, preserving Mana Regeneration; despite its registry name, this block-item is the consumable Sourceberry.
- Werewolves addon: `kubejs/data/werewolves/tags/item/werewolf_food.json` and `raw_food.json` append the approved foods without replacing upstream tags. The addon checks `FoodProperties != null` for its meat categorization, so the two custom-use items still need explicit live QA; a source tag alone does **not** prove the Werewolves addon accepts them when the Not Meat skill is absent.
- Static guardrails: `scripts/verify_dragon_diets.py`. Direct map/tag inspection passed; **no actual Java client or server consumed food during this check**.
- JAWR werewolf behavior is still unverified because it does not use the Werewolves addon tag authority; JAWR and Goety lich changes are expressly outside this pass.

## Runtime acceptance
1. On every dragon test special items with/without depleted hunger and drained mana/blood; no free repeat use or duplicated effects.
2. Werewolves addon test with and without Not Meat; JAWR lycan in human/wolf forms separately. Verify magic food restores hunger/effects under native conditions, not just animation.
3. Confirm sourceberry Mana Regeneration, Pastel Enchanted candy harmful-effect removal, Magnificent candy full restoration, enchanted carrot effect durations, Rejuvenated Flesh blood drain, Fruit of Grisaia mana drain, and reusable-beef cooldown.
4. No stable promotion without successful dedicated-server + client runtime testing. An existing playtest export is not changed retroactively.

