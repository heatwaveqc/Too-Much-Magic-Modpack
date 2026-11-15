# EvilCraft × Dragon Survival — Rejuvenated Flesh

| Field | Value |
| --- | --- |
| Participant manifest IDs / versions | EvilCraft 1.2.100; Dragon Survival 2.0.74 (NeoForge 1.21.1) |
| Source profiles | [EvilCraft](../mods/evilcraft.md); Dragon Survival species-food selector is documented in [magical food policy](../../MAGICAL-FOOD-EXCEPTIONS-2026-11-14.md) |
| Pack integration base | `test/full-integration` at `b9e41333623817926dd165802be4c6e7985e8ef9` |
| Interaction status | source-supported |
| Mechanism class | EvilCraft custom item handler plus in-pack Dragon Survival species data-map selector |
| Recommended game stage | Late second-age EvilCraft utility; food's custom blood cost is essential |
| Reviewed | 2026-11-15 |

## Player-facing payoff

Rejuvenated Flesh is a reusable food item that keeps its own Blood cost. The current Dragon Survival map permits Cave, Forest and Sea Dragons to recognize it without injecting synthetic food: each species entry sets nutrition and saturation to zero and retains effects. If the Dragon Survival interaction reaches EvilCraft's native use handler, the bite can still require and consume Blood, then provide the mod's custom hunger restoration.

## End-to-end mechanism

`evilcraft:flesh_humanoid + evilcraft:garmonbozia → evilcraft:flesh_rejuvenated (Blood Infuser recipe) → EvilCraft fluid handler stores Blood → Dragon Survival allows special item use → EvilCraft finishUsingItem drains Blood and calls FoodData.eat`

| Step | Mod and system | Actual type / registry hook / conditions | Consumed, transferred or transformed? | Evidence |
| --- | --- | --- | --- | --- |
| 1 | EvilCraft creation | `evilcraft:flesh_humanoid` item + `evilcraft:garmonbozia` item; crafting recipe outputs `evilcraft:flesh_rejuvenated` | Inputs consumed; one custom food item crafted | Pinned JAR recipe, guide section Second Age → Tools → Rejuvenated Flesh |
| 2 | EvilCraft blood charge | `evilcraft:blood` fluid in the item's fluid capability; default container 10,000 mB | Blood fills the item's internal fluid container | Source `ItemRejuvenatedFleshConfig.java`; default capacity |
| 3 | Dragon Survival selector | `kubejs/data/dragonsurvival/data_maps/dragonsurvival/dragon_species/diet_entries.json` has the item under Cave, Forest and Sea; each sets `nutrition: 0`, `saturation: 0`, `eat_seconds: 1.6`, `retain_effects: true` | Data-map allowance, not food or Blood transfer | Current tracked mapping |
| 4 | EvilCraft use handler | `ItemRejuvenatedFlesh.use` requires at least configured bite Blood and `player.canEat(false)` | Begins 32-tick eating action; otherwise returns failure | Matching pinned EvilCraft source |
| 5 | EvilCraft completion | `finishUsingItem` drains 250 mB default then applies `FoodData.eat(3, 0.5F)`; same item is returned | -250 mB Blood; +3 food points and 0.5 saturation for a player; item is reusable | Matching pinned EvilCraft source |

The food amount and Blood drain are code-derived defaults, not a pairwise measured observation. The guide's “infinite source of food” means the item is reusable with Blood supply; it is not free food, does not have vanilla `FoodProperties`, and does not bypass the hunger gate.

## Existing support vs work required

- **Natively supported and present?** EvilCraft itself implements the item handler and blood cost. Dragon Survival has a species data-map allowlist, and the three current entries are present in tracked KubeJS data.
- **Pack implementation already exists?** Yes: the selectors are in `kubejs/data/dragonsurvival/data_maps/dragonsurvival/dragon_species/diet_entries.json`.
- **If not, smallest implementation:** Not applicable. Do not modify it during this knowledge pass.
- **What cannot currently be claimed?** The selector does not prove Dragon Survival calls EvilCraft's custom `use`/`finishUsingItem`, that its data-map handler accepts a non-`FoodProperties` item, or that the interaction works after species transition/relog. No client behavior has been observed.

## Minimum practical setup

1. Complete the tier-3 Infuser route for Garmonbozia and craft Rejuvenated Flesh as shown in JEI.
2. Fill it with EvilCraft Blood using a valid tank/Infuser path. Confirm the item's Blood tooltip or fluid display.
3. As each Dragon Survival species, reduce hunger below full and attempt one bite with at least 250 mB; repeat at 249 mB and while full.
4. Compare item persistence, Blood amount, hunger/saturation and any species-specific effects before/after. Repeat after rejoin.

**Cost and power comparison:** Source defaults are 250 mB per bite, +3 hunger and +0.5 saturation, and 10,000 mB capacity (40 possible bites from a full item absent changes). Those numbers must not be converted into a sustainability claim until Blood replenishment rate and actual Dragon Survival use are measured. Ordinary players use the same EvilCraft handler.

## Balance, safety and compatibility

- **Progression locks and item-gate leakage:** Its input is an EvilCraft late-stage item; eligibility cannot be earned by editing the species map alone, but map allowance may expose a custom-food path across species.
- **Resource loops or duplication:** Verify exactly one 250 mB drain and one `FoodData.eat(3, 0.5F)` per completed bite. Rejected use, insufficient fluid, full hunger and interrupted use must not provide either reward or free healing.
- **Entity/world performance:** No entity loop; per-use item handling only.
- **Multiplayer, teams, dimensions and persistence:** Verify blood data, dragon-specific use and food values remain synchronized and persist through rejoin/form change.
- **Exclusion risks:** No Blood Magic or JAWR Blood conversion is implied. Lich and Vampire rules remain separate. Other species providers such as the Werewolves addon may inspect ordinary food properties and require their own test.
- **Version changes:** A hash change in either pinned artifact or a modification to Dragon Survival's data-map schema invalidates this profile's pair claims.

## Quest / Tips & Tricks opportunity

Later food-compatibility teaching can show how to craft, charge and consume Rejuvenated Flesh, but only after a client test confirms per-species behavior and resource accounting. Teach the named page **Origins of Darkness → Second Age → Tools → Rejuvenated Flesh** and the JEI recipe. A craft task proves possession only; require a live one-bite QA/action criterion, or a manual confirmation until a truthful task exists. Never provide a prefilled Rejuvenated Flesh item as a reward because that hides its Blood cost.

## Verification plan

| Test ID | Positive or boundary case with disposable-world steps | Expected observable value/behavior; distinguish source formula from measured rate | Observed status |
| --- | --- | --- | --- |
| INT-01 positive | Rejuvenated Flesh with ≥250 mB; player hunger below full; one completed bite for human, Cave, Forest and Sea forms | One bite completes; item persists; Blood decreases 250 mB and hunger/saturation increase once per source code | not run |
| INT-01 boundary | Try at 249 mB; at full hunger; cancel before completion; repeat after form switch/relog | Use fails or cancels without free food or Blood; handler values stay synchronized; no double effect | not run |
| INT-02 alternate physiology | Test EvilCraft item separately with Werewolves addon, JAWR lycanthropy and Lich; inspect each provider's food gate | No assumption of cross-provider support from Dragon Survival allowlist | not run |

## Evidence and open questions

| Claim | Primary source URL / pinned artifact file + version | What verified / unknown |
| --- | --- | --- |
| EvilCraft item behavior | Source tag `1.21.1-1.2.100`, `item/ItemRejuvenatedFlesh.java`, `ItemRejuvenatedFleshConfig.java`; exact pinned JAR hash in [profile](../mods/evilcraft.md) | Custom Blood handler and use gate; not Dragon pair behavior |
| Dragon species selector | `kubejs/data/dragonsurvival/data_maps/dragonsurvival/dragon_species/diet_entries.json` | All three species map the item with 0/0 synthetic values and retained effects; no live test |
| Owner food policy | [`MAGICAL-FOOD-EXCEPTIONS-2026-11-14.md`](../../MAGICAL-FOOD-EXCEPTIONS-2026-11-14.md) | Explicit magical-food allowance while preserving native resource costs; implementation acceptance remains open |
| Guide page | [EvilCraft online Rejuvenated Flesh entry](https://evilcraft.rubensworks.net/book/second_age/tools/rejuvenated_flesh.html) | Public guide version 1.2.98; exact pinned InfoBook/JAR used for current profile |

- [ ] Confirm that Dragon Survival's selector permits a custom-use item without assigning its own food effect and that EvilCraft's `finishUsingItem` runs once.
- [ ] Check starvation/full-hunger and transformation transitions; test with drained, partially filled and full item states.
- [ ] Reconcile any failure with the pack's Dragon Survival compatibility issue record only after observation.

## Recommendation

**Disposition:** keep the existing selector and run pairwise client tests before describing it as usable.

**Why:** both sides expose the relevant mechanisms in source and the current map has the intended entry, but the food is not a normal food item and its custom handler is the only authority for Blood cost and hunger restoration.

## Revision notes

- 2026-11-15 — Added source-supported interaction report from EvilCraft handler/config and Dragon Survival data map. No runtime or quest change and no pairwise test.
