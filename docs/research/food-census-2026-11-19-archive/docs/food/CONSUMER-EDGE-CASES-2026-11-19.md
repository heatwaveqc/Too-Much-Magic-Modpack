# Cross-consumer food edge cases — 2026-11-19

**Status:** first source-only review, 41 offline regression tests green in [run 44634610019](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44634610019). Current evidence is from frozen #348 installed census (23,681 registered items), complete exact-ID first-pass overlay (2,151 review entries), authored Dragon Survival diet map, explicit Gilded and charred tags, and historic ingredient equivalence report. These are **not** gameplay confirmations. All results and concrete selectors are preserved in [consumer-edge-case-audit-v1.json](consumer-edge-case-audit-v1.json). Zero runtime, recipe, loot, permission, packwiz or Progression changes.

## Highest priority before food live acceptance

1. **Dragon magical food namespace mismatch (real authored defect):** 9 selectors (three per species) still use `spectrum:` while the installed registry contains `pastel:enchanted_star_candy` and `pastel:enchanted_golden_carrot`. `pastel:magnificent_star_candy` **does not exist in the frozen installed registry**. Do not invent it, treat the ordinary candy as equivalent or rewrite nutrition; reconcile against exact pinned Pastel and Dragon Survival code, then hand off to dragon diet owner. The legacy static dragon verifier currently asserts those stale selectors and must be updated together with any reviewed change.
2. **JAWR special blood variants:** `witchery:bottle_of_blood` carries native special-component markers (including vampire/chicken blood) under the **same item registry ID**. An item-only diet or ordinary blood tag would launder special blood. Existing first-party component-sensitive interop is designed to preserve them; do not bypass it with generated food tags. Validate ordinary/special drink and cauldron paths under the transformation owner.
3. **Forest Dragon regex selectors:** seven expressions match 45, 5, 3, 2, 91, 12 and 47 concrete registered IDs respectively in a Python source survey; no nonfood candidate was identified by this classification screen, but this is **not proof of in-game matching or safe consumption**. Verify the pinned Dragon Survival matcher and resulting resolved membership. Do not treat patterns as bad item IDs.
4. **Two likely stale Aquaculture selectors:** `aquaculture:frog` and `aquaculture:frog_legs_raw` appear in Forest diet data but are absent in the frozen registry despite Aquaculture being installed. Check current version and exact content before removal or substitution.

**Not a cleanup request:** 219 other selectors refer to optional/uninstalled mod namespaces. Their presence alone does not establish a defect or justify deletion; test data-map loading with the intended module set.

## Other consumer holds

| Consumer | Observed evidence | Action |
|---|---|---|
| Kitchen/ingredients | Frozen partial recipe slots: tomato 65, onion 70, cabbage 84, rice 25, wheat dough 127, raw pasta 28. Existing additive patches cover some **tag-aware** recipes; direct-ID recipes do not auto-change. | Check real recipe ingredients and yields before broader substitutions; do not conflate a historical slot count with remaining incompatibilities after current patches. |
| Milk & raw fish fillets | Milk 51 and raw fillet 8 partial recipe slots. Different containers and fish cuts have non-equivalent output quantities and remainders. | Preserve original containers/yields and test both native appliances and Cooking for Blockheads. |
| Cave / Sea / Forest | Four native charred outputs correctly in Cave; Sea raw-fillet and rice-free sushi selectors present; Forest includes raw terrestrial meat by native design. | No generic raw/cooked bypass or mixed-meal inheritance. Confirm actual diet and recipe stage on client. |
| Reusable magical food | `evilcraft:flesh_rejuvenated` and `botania:infinite_fruit` have zero supplemental diet nutrition with `retain_effects` true in authored map. | Static map looks correct; confirm native blood/mana drain, durability/reuse, cooldowns, effects exactly once. Eternal Steak is **not** the Cave-only Everlasting Beef exception. |
| Gilded | Four explicit golden IDs exist and canonical tag is exact. Nether-origin food is a separate policy; My Nether's Delight is disabled in historical manifest. | Progression owner to reconcile Gilded runtime consumer tag and initiate/bonus gates without recursive recipe tracing. Do not auto-include generic gold-containing equipment. |
| Alcohol | 58 alcohol candidates, 10 intentionally uncertain drinks. The keg also makes nonalcoholic foods; root beer is not proof of alcohol. | Inspect exact native drinking/effects, including Beachparty, and preserve keg/manual actions. No global alcohol buff tag yet. |
| Mutagens / special-use | `tmm_progression:bio_mutagen` and `tmm_progression:mutagenic_brew` need native use and component gates. | Progression owns executable mutation effects; no food-registry XP or advancement trigger. |
| Liches and hybrids | Consumption/energy mechanics are provider- and state-specific; source registry does not capture them. | Test human, JAWR vampire/werewolf, both liches, all dragons and hybrid precedence independently; preserve dracolich restrictions. |

## Safe checkpoint and live-test strategy

- Keep the food registry **read-only** until the native food component / custom-use survey. The present corpus is not an authoritative edible list.
- Implement confirmed low-risk source fixes on the appropriate owning branch after checking installed version and regression tests, especially the Pastel selectors, then reconcile with integration writer.
- In a disposable game build, use one representative item per risk category (two-stage Cave charring, Sea sushi, Forest produce/meat, JAWR special blood, magical refill/cooldown, Gilded four explicit foods, milk containers, keg output, mutagen drink).
- No live-ready declaration is implied by this report. Server smoke, assembly, Prism package and gameplay acceptance remain owner-authorized work.
