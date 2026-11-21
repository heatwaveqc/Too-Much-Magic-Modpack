# Too Much Magic — Potion and Alchemy Interoperability Matrix

**Status:** Design/research v0.2, 2026-11-20. Owner-approved preservation and anti-progression-bypass goals; no executable gameplay changes authorized by this document. Owner decision issue: [#337](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/337). Target: Minecraft 1.21.1 NeoForge; inspect installed build before claiming PASS.

## Why this is not just a potion-container chart

Potion interoperability crosses five independent boundaries:

1. **Production:** base potions, alchemical recipes, custom flasks, magical brews, Wixie automation, alternative cauldrons.
2. **Transport/storage:** potion bottles, PotionContents, potion fluids, multi-dose flasks, jars, item-specific components, containers and side payloads.
3. **Modification:** potency/duration upgrades, mixing, combining, enhancing, infusion, copying, charm conversion, supernatural familiar application.
4. **Application/harvesting:** drinking, splashing/diffusion, charms, entity effects, Purifier rebottling, attribute-based effects and transformation handlers.
5. **Progression/rewards:** Hunter XP, mutagenic brews and future effect-enhancing Hunter skills; native progression-locked alchemy, species/diet rules, resource and dose economics.

**Evidence labels:** **S** = behavior verified in reviewed upstream source; **P** = owner-stated intention, not runtime implementation; **I** = inference/possible incompatibility requiring exact binary and gameplay testing; **U** = not audited; **QA** = needs a captured exact-build test. Source S does **not** mean pinned JAR bytecode equivalence or live PASS. The external mod roster below is selected in the current integration manifest, not a claim that every listed first-party feature is enabled in the player's build.

### Representation vocabulary / transaction record

| Class | State carried | Semantic constraints |
|---|---|---|
| **PC** — standard PotionContents | Optional potion registry holder, custom color, custom MobEffectInstance collection (amplifier, duration, flags) | An item may also carry additional meaningful components that PC cannot express |
| **NVF** — Neo Vitae Alchemy Flask | Primary FlaskEffects custom component with per-effect modifiers; a mirrored PC for compatibility; damage / remaining uses; eight-use flask | PC mirror alone is **not** an authoritative snapshot of flask, charge count, recipe ancestry or original item |
| **ARF** — Ars Nouveau Flask | Ars-specific MultiPotionContents with stored doses and possible derived effect modification | Do not equate with one vanilla bottle or a generic PC component |
| **IRF** — Iron's Potion Fluid | 250 mB per potion dose, PC plus bottle-type component in a FluidStack | A fluid transformation may preserve effects yet irreversibly replace or destroy an unusual original carrier |
| **BMB** — Botania brew | Brew registry/key and native dose-count semantics | Not automatically representable as PC |
| **GCB** — Goety brew | PC-like conventional effects plus CustomBrewEffects in custom item data for special behavior | Dropping custom data changes the item/recipe semantics |
| **PST** — Pastel and other custom carriers | PC and/or extra specialized metadata, duration flags or recipe state | Must verify complete schema; merely finding PC is insufficient |
| **HMB** — Hunter Mutagenic Brew | Authorized vanilla brewing-stand base PotionContents on a special consumable; server-side mutation accrual | It is not a generic interchangeable potion; converting it can bypass prerequisites or corrupt progress |
| **ACTIVE** — current mob effects | MobEffectInstance duration/amplifier/flags on an entity; no guaranteed source provenance | EvilCraft can extract these but the original bottle, recipe, ritual, and dose identity are generally lost |

A transaction test MUST record input item ID/components, effect registry IDs, potion holder, effect level and duration, source vs current duration, extra payloads, stack/doses/durability, fluid amount, player ownership, resources/XP, outputs/remainders, and action timing. **No silent effect loss, extra charge, zero-cost resource creation, or unauthorized transformation bypass.**

## A. Capability and machine inventory

| ID | Producer / transformer | Native input → output | Reviewed status and boundaries | Test priority |
|---|---|---|---|---|
| P01 | Minecraft brewing stand | Base potions + reagents → standard potions (PC) | Reference baseline; Hunter mutagen conversion explicitly depends on approved ordinary brewing-stand base types. | High |
| P02 | Iron's Spells Alchemist Cauldron 3.16.3 | Potion bottle / PC item → potion fluid (IRF), up to four doses; brew reagent + fluid → transformed/multiple fluids; fluid + glass bottle → PC potion; custom elixirs, inks/scroll recycling | **S:** has dynamic PC fill/empty and data-driven brew recipes. **I:** generic PC fallback accepts non-bottle items including potentially Neo Vitae flask; returns glass bottle, losing original carrier identity/remaining uses. Must source-verify exact pinned JAR. | Critical |
| P03 | Ars Nouveau Wixie Cauldron 5.13.5 | Potions from nearby jars + potion recipe ingredients → target potion jar contents, 300 jar units per completed batch | **S:** recognizes potion brewing cases and uses PotionCraftingManager; output requires accepting Potion Jar. Test whether modded potion mixing recipes are recognized and whether batch cost/refill is conserved. | High |
| P04 | Ars Nouveau Potion Jar | Vanilla potion → 100 units PC; glass bottle / arrows extract 100 / 10 | **S:** hand insert accepts Items.POTION, stores PC, reconstructs vanilla potion/arrow; custom carrier item metadata is lost if forced through an adapter. | High |
| P05 | Ars Nouveau Potion Flask | Ars multi-dose representation ↔ Potion Jar | **S:** independent MultiPotionContents charge storage; one dose exchange at a time with jar by native interaction. Not equivalent to NVF. | High |
| P06 | Ars Nouveau Potion Melder / Potion Diffuser | PC mixture/combinations and area effect delivery | **S** known native targets, full per-recipe and multi-effect acceptance still to verify. Test duplicated effect types, durations, number of uses, player owner and special effects. | High |
| P07 | Ars Nouveau Alchemical Sourcelink | Potion effect doses → Source | **S:** effect count/duration/amplifier drive production; verify overflow, empty buffer, and consumed dose/Source reconciliation. Never assume every extreme high-duration potion is safe. | High |
| P08 | Neo Vitae Alchemy Flask and Athanor 1.1.53 | Customized flask effects, potency/length, fill, transformation → reusable NVF | **S:** 1.21.1 source stores primary FlaskEffects, mirrors PC, 8 uses, data-driven flask_effect/flask_length/flask_potency/flask_fill and transforms. Directly supported by Neo recipes; other mods must preserve primary component + charges. | Critical |
| P09 | Neo Vitae Alchemy Table / Tabula Vitae / related arrays | Bespoke ingredient / catalyst / flask recipe families | **S:** recipe registry is data-driven. Inventory exact recipe output types, minimum tiers and resource consumption before generic cross-recipe conversion. | High |
| P10 | Occultism Demonic Husband / Wife 1.227.0 | Any held item with PC effects → effects applied directly to owner; consumes one item; gives bottle or crafting remainder | **S:** non-instant effect duration ×5, instant effect amplifier +2; code tests for PC rather than vanilla item ID; accepts multiple effects. **I:** NVF PC mirror may cause entire charged flask to be consumed as one generic potion and return glass bottle. Also handles suspicious stew separately at ×50 duration. | Critical |
| P11 | EvilCraft Purifier 1.2.100 | Living entity ACTIVE effect + glass bottle + full blood → custom-effect vanilla PC potion | **S:** captures non-ambient effect after processing; default extractable duration cap 5 min (remaining duration retained as active effect); removes effect and resets blood. Re-extraction / partner ×5 loop needs resource and duration audit. | Critical |
| P12 | Apotheosis Potion Charm 8.9.0 | Registered single non-instant base potion → renewable effect charm | **S:** crafting currently rebuilds fresh PC from potion holder, dropping extra customEffects/color; native charm polls effect duration and consumes charm durability. Wider custom-effect eligibility is **not yet approved**; source constraints/blacklist preserved. | High |
| P13 | Reliquary Reincarnations 2.0.85 | Potion vial with PC effects and Reliquary carrier/remainder | **S:** carrier uses PC; direct Ars jar hand-in fails vanilla-item identity check. Potential lossless one-dose adapter with exact remainder and cost. | Medium |
| P14 | Goety 3.2.2 | Brew recipes with special effects, custom payloads and rituals | **S** from prior #337 source review: CustomBrewEffects in CUSTOM_DATA may encode world/summon/nonstandard behavior. **Never** strip payload on conversion; preserve magical gating. | Critical |
| P15 | Botania Revived / Extra Botany | Magical brew/doses and effect delivery | **S** prior #337: brew key/doses are native representation. Audit any conversion to PC separately. | Medium |
| P16 | Pastel 1.1.5.5 | Pigment potion and other custom-crafted effect carriers | **S** prior #337: custom components may exceed PC schema. Need exact effect, extra flags and duration verification. | High |
| P17 | Hexerei cauldron / potion candles 0.5.0.3 | Magical mixing, potions and status-effect-related candles | **U:** trace actual recipes, fluid transfer and ambient effect semantics; don't assume vanilla-container equivalence. | Medium |
| P18 | Enchanted: Witchcraft / JAWR cauldron / Forbidden Magic | Ritual/transformational brews with specialist costs and gates | **U:** obtain exact list of special states, effects and world-side actions; **never** generic-export or replicate a transformation effect without validated progression. | Critical |
| P19 | Primal Magick, Corail Tombstone, Malum, Theurgy and other sources | Effects / alchemical ingredients, duration modifiers, ritual tools | **U:** inventory actual potion-effect alteration vs non-potion magical buffs; Corail Alchemist duration stacking with Purifier is a known historical interaction worth checking. | Medium |
| P20 | HerbalBrews, Brewin' and Chewin', Cocktails Delight and food/drink mods | Consumable buffs, sometimes food effects rather than PC | **U:** preserve nutrition, intoxication, special item/food carrier, diet restrictions, and container returns; not every consumable is a potion. | Medium |
| H01 | Hunter potion-drinking XP | Completed drinking of registered minecraft:potion with an approved base potion ID → Hunter XP | **S in current repo**: rejects customEffects; difficulty-based XP; first discovery bonus; persistent per-potion-type cooldown ≥ longest base duration; no source-agnostic XP for arbitrary bottled effects. | Critical |
| H02 | Hunter Bio-Mutagen + Mutagenic Brew | Approved ordinary brewing-stand potion + Bio-Mutagen → HMB, then drinking → cumulative permanent mutation progress | **S in current repo**: guards base-potion origin/type; credits original effect duration; separate exhaust-based cost; not a conversion for exotic effects/ritual payloads. | Critical |
| H03 | Hunter potion duration/potency/improvement skills | Purchases improve eligible potion effects, preserving meaningful class specialization | **P:** owner-described goals, not confirmed as deployed in current 20-level hunter.json pilot; need explicit effect timing/eligibility and anti-recursion semantics in shared Progression engine. | Critical |

## B. Pairwise compatibility / transaction matrix

**State column:** N = native source logic provides path, X = source-confirmed rejection, R = high-risk source implication, ? = binary/runtime unverified. Even N requires exact-JAR and survival QA for TMM.

| Test | From → to | State | Expected / acceptance boundary |
|---|---|---|---|
| C01 | Vanilla PC bottle → Iron Cauldron → PC bottle | N | A 250 mB dose round-trips same holder, effects, splash/linger type and one container; no loss/extra portion |
| C02 | Iron cauldron base potion + recipe reagent → custom elixir | N | Original recipe, ingredients, per-fluid amount and byproducts preserved; Wixie only if recipe actually supported |
| C03 | **Neo Vitae NVF → Iron Cauldron** | **R** | Generic PC detection could consume one entire eight-use flask as only one bottle; prevent carrier/dose destruction or implement explicit one-dose transfer |
| C04 | Vanilla PC bottle → Ars Potion Jar → PC bottle | N | 100 units = one bottle; original PC effects/color preserved |
| C05 | Ars Potion Flask ↔ Ars Potion Jar | N | One stored charge corresponds to exactly one 100-unit jar dose; native vial state retained |
| C06 | **Neo Vitae NVF → Ars Potion Jar** | X | No generic hand insertion; an adapter must handle primary effects and actual remaining flask charges, never raw PC-only transformation |
| C07 | Reliquary potion vial → Ars Potion Jar | X | If adapted, consume exactly one dose and return Reliquary's expected empty container, not synthetic duplicate |
| C08 | Wixie + Ars Potion Jar potion ingredients → output Potion Jar | N | Need source potion/ingredients and **300 jar units available**; completion must not duplicate potion ingredients or jar contents |
| C09 | Vanilla PC bottle → Occultism partner | N | 5× non-instant application, instant +2 amplifier, consume one item and return remainder; legitimate source |
| C10 | **Neo Vitae NVF → Occultism partner** | **R** | PC mirror recognized even though flask is 8-use primary-component carrier; implement one real use or explicit refusal, never consume entire flask unexpectedly |
| C11 | Ars Potion Flask → Occultism partner | ? | Ars charge component may not expose PC directly; verify no accidental whole-flask consume |
| C12 | PC / ACTIVE → EvilCraft Purifier → PC | N | Requires non-ambient active effect, blood and bottle; max 5-minute captured segment by default, remaining effect stays on entity |
| C13 | Occultism ×5 effect → EvilCraft Purifier → Occultism ×5 | **R** | Renewable enhancement loop can boost duration with costs; log input/captured/remaining ticks and blood/item cost; preserve intended native power until design review |
| C14 | Purifier custom-only PC bottle → Hunter potion-drinking XP | X | Current Hunter XP needs an eligible **registered base potion** and no customEffects; zero XP expected |
| C15 | Wixie-produced normal registered PC potion → Hunter XP on drink | ? | Should earn the same legitimate native Hunter credit as a brewing-stand potion; source/result type proof needed |
| C16 | Ordinary registered brewing-stand potion → Hunter Mutagenic Brew | N | Only eligible base potion may convert through authorized Bio-Mutagen recipe; progression based on original effect |
| C17 | Potions from Iron Cauldron / Wixie / Purifier → Bio-Mutagen | ? | Exact source/brew origin policy matters: current mutation recipe validates registered base (not immutable recipe provenance); test for bypass/inconsistency |
| C18 | Hunter future boosted ACTIVE effect → EvilCraft Purifier | **R** | If effect modifications are applied after drinking, captured enhanced duration/potency could become a permanent item; avoid reward recursion and test costs |
| C19 | Registered PC + custom effects → Apotheosis Potion Charm | **R** | Native charm recipe silently discards custom payload; refuse before consumption or implement owner-approved exact-preservation rules |
| C20 | Custom-only PC → Apotheosis Charm | X | Native recipe rejects without valid base holder; expanded safe single-effect charms remain a separate pending owner choice |
| C21 | Goety CustomBrewEffects / JAWR transformation → generic PC jar, charm or fluid | **R** | Do not discard specialized payload or create gate-bypassing renewable transformation |
| C22 | Botania brew → Ars Jar / Iron Cauldron / Occultism partner | ? | Native brewKey and dose semantics require explicit adapter, not only matching status-effect names |
| C23 | PC source → Ars Alchemical Sourcelink | N | Test actual Source output vs duration, amplifier, effect count and source buffer; overflow/zero-cost debit are QA blockers |
| C24 | Custom-carrier potion or therapeutic drink → dragon / JAWR diet | ? | Correct consumed item, underlying food/potion effect, source-specific feeding rules, and drink exception ordering; never generic bypass |

## C. Invariants and potentially recursive loops

**I1 — One conversion action, one accountable input.** Count a potion dose and the vessel separately. A flask containing eight doses is not eight free single-use bottles; conversely feeding one flask to a machine must not silently destroy seven additional charges. Native recycling of valuable vessels must be preserved.

**I2 — Exact effect semantics.** A successful compatible transfer preserves effect ID, amplifier, duration, ambient/visible flags, source requirement, specialized payloads and status. Do not imply that a vanilla bottle can reproduce an effect whose gameplay relies on an alternate carrier/ritual/ongoing block.

**I3 — Hunter XP awards only on earned consumption.** Current server code rewards normal registered base potions on completed drinking with a persistent anti-repeat timer. Applying an effect from Occultism, a charm, diffuser, or extracted bottle does not mint an independent Hunter XP claim. Retain the source-visible reward boundary unless owner explicitly redesigns it.

**I4 — Hunter mutagen progress is not general magic duplication.** Any new duration/potency skill needs explicit order: compute one authoritative original consumed-brew credit, then apply player improvements for active status if intended, rather than credit the same enhanced effect again through Purifier/rebottle. No passive award on auto-refresh.

**I5 — Preserve native power, test feedback loops.** Occultism ×5 and EvilCraft segment extraction are real mechanics; don't cap them globally just because combinations may be strong. Capture input/output and detect value amplification/zero-cost cycle first. Owner selects balance intervention if necessary.

**I6 — Effect application is not identical to potion crafting.** Direct applications (Demonic Partner, charms, rituals, area delivery) may skip ordinary vanilla item-use events; test Hunter, diet, and transformation hooks separately from ItemStack transformations.

**I7 — Reject unsupported carriers BEFORE resource debit.** Any bridge must validate complete payload and charge/container behavior before mutating item, fluid, blood, Source or XP state. Failure explains incompatible data rather than replacing it with a weaker generic potion.

**I8 — Advancement/quest gates cannot be laundered.** An eligible standard MobEffectInstance doesn't automatically authorize an alternate ritual, Witchery curse/ascension, Goety lich, or Hunter mutation acquisition. Validate registered recipe/authoritative progression as appropriate.

## C.5. Balance-sensitive effect capabilities and laundering loops

**Owner design direction (2026-11-20):** One-dose/carrier conservation **is necessary but insufficient**. **A Hunter must not acquire unrestricted or permanent creative flight through a Neo Vitae → EvilCraft → Occultism Demonic Partner chain**, or by adding future Hunter duration/potency/mutation skills to it. Preserve the deliberately supported native magical flight paths ([#330](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/330)) and other ordinary potion effects. No gameplay edits authorized merely by this documentation.

### Source-confirmed Neo Vitae flight vector

- [Neo Vitae 1.21.1 FlightEffect](https://github.com/breakinblocks/NeoVitae/blob/1.21.1/src/main/java/com/breakinblocks/neovitae/common/effect/FlightEffect.java) grants **NeoForge CREATIVE_FLIGHT** by an attribute modifier of +1.0 while active, sets flying speed by amplifier, and clears fall distance.
- [Neo Vitae flask recipe](https://github.com/breakinblocks/NeoVitae/blob/1.21.1/src/generated/resources/data/neovitae/recipe/flask/suspended_to_flight.json) synthesizes \`neovitae:flight\` from \`neovitae:suspended\` plus \`minecraft:levitation\` at **3,600 ticks**, with a Combinational Catalyst, 1,000 syphon, 200 processing ticks, \`upgradeLevel: 1\`. Its separate flight lengthening/potency recipes adjust this native effect. This is intended native temporary flight and is **not** to be removed globally.
- [EvilCraft Purifier](https://github.com/CyclopsMC/EvilCraft/blob/master/src/main/java/org/cyclops/evilcraft/blockentity/tickaction/purifier/CollectPotionPurifyAction.java) extracts any **non-ambient** active effect into a fresh standard vanilla potion with custom-only PotionContents, limited to **6,000 ticks** (five minutes) by default **per extraction** and preserving the entity's remaining portion. It checks no creative-flight capability; it could make a generic bottle from native Neo Vitae flight.
- [Occultism DemonicPartner](https://github.com/klikli-dev/occultism/blob/master/src/main/java/com/klikli_dev/occultism/common/entity/spirit/demonicpartner/DemonicPartner.java) reads that potion's PotionContents and applies non-instant effects at **5× supplied duration**. Therefore a 6,000-tick captured flight segment could become 30,000 ticks (25 min), given the source path and its costs. The exact infinite/repeated-cycle economics have **not** been measured; do not label it confirmed infinite or a runtime-executed exploit without pinned-binary QA.

### Required compatibility policy (owner intent) and engineering proposal

| Capability / source class | Normal consumption | EvilCraft active-effect extraction | Demonic Partner duration / amplifier magnification | Generic jars/fluids / Apotheosis charm | Hunter XP, mutagen or generic potion-enhancement skills |
|---|---|---|---|---|---|
| Ordinary status effects (e.g. Speed, Night Vision) | Preserve native behavior | Permit native eligible effect with blood/container costs | Permit native spouse behavior and dose-safe input | Permit lossless eligible conversion; native charm policy separately decided | Native XP/mutation source rules; improvement skills only as owner-approved |
| **Creative-flight-granting MobEffects** (first known: \`neovitae:flight\`) | **Keep correctly gated native temporary flight** | **Propose deny/skip**, leaving effect active and not consuming blood/bottle for it | **Propose reject enhanced application**, without swallowing dose/vessel | **Propose no generic conversion/renewable charm** if privilege would be laundered; machine-native Neo Vitae recipes remain usable | **Exclude from permanent mutations, progression XP and generic Hunter duration/potency enhancements**; no bypass via splash/arrow/area carrier |
| Other exceptionally privileged effects (invulnerability, teleport/progression bypass, transformation, dimension effects, world/resource generation; only where they actually use potion effects) | Preserve existing native progression | Require per-effect review | Require per-effect review | Require per-effect review | Fail closed unless explicitly allowlisted for Hunter |
| Ordinary no-potency / special effect types | Preserve native behavior | Only with proven faithful semantics | Apply native type rules, not numeric modifier blindly | Lossless-only | Follow meaningful Hunter effect and source rules |

**Important distinction:** This is **not a ban on flight or a retroactive nerf to Neo Vitae**; native Neo Vitae temporary flight, rituals and related resource costs remain available through their intended routes. The restrictions are on *exporting a privileged buff to otherwise generic effect carriers*, *cross-mod multiplication*, and *permanent Hunter acquisition* without an explicit class/boss/progression gate. Require owner sign-off before choosing a particular implementation (e.g. Purifier skip vs deny; Demonic Partner decline whole interaction vs apply unamplified).

**Design mechanics (implementation not approved yet):**

1. Introduce an explicit **data-driven privileged-effect registry/tag** in the existing compatibility/Progression subsystem rather than a global hardcoded potion blacklist. Seed \`neovitae:flight\` and audit all current effects that add \`neoforge:creative_flight\` or grant mayfly through callbacks; IDs alone are not exhaustive. Scope permissions by operation: \`allow_native_use\`, \`allow_generic_bottling\`, \`allow_crossmod_duration_multiplier\`, \`allow_reusable_charm\`, \`allow_hunter_mutation\`, \`allow_hunter_duration_skill\`, etc. Do not silently infer every mod effect is special, but review unknown privilege-bearing effects before allowing generically.
2. Enforce a capability filter at **both endpoints**, especially on any reconstituted vanilla potion. EvilCraft and Iron's/Ars conversion may lose source-specific provenance, so a consumer must check *effect identities/capabilities*, not trust the incoming item ID or whether it looks vanilla. Mixed-effect bundles containing one restricted effect are denied as an atomic transaction, not partially converted into an apparently weaker potion.
3. If a generic machine cannot represent a source's charges or effect privilege exactly, **refuse before debiting**; don't destroy the original flask, consume whole eight-use item, remove player effect, or spend blood/XP. Distinguish *safe regular enhancement* from *unearned capability activation*.
4. Hunter **currently** has a whitelist-like brewing **source** gate but a blacklist-based **effect** policy: \`HunterMutationPolicy.eligible\` tests only non-instant and \`effect_blacklist\`, and \`hunter.json\` presently does **not** list \`neovitae:flight\`. Today the item/source gate prevents this particular flask/Purifier chain from directly becoming a mutagen, but a future registered brewing stand result or new adapter could accidentally admit it. Add **defense in depth**: deny known privileged effect IDs/capabilities in Hunter's mutation eligibility even when the brewing source is otherwise valid; ideally migrate to explicit permit-list/privilege tiers for permanent effects, preserving the existing owner-approved normal eligible potion roster.
5. Hunter's periodic mutation application creates **ambient=true** MobEffectInstance; EvilCraft currently ignores ambient effects. Preserve this useful safety invariant, but do **not** use ambient as the only gate: Hunter's planned duration/potency skills and other mods may apply ordinary **non-ambient** effects that need independent privilege checks.
6. Test effect-loop **economics** as well as a Boolean PASS/FAIL: measure source recipe tier, consumed inputs, EV, blood, time, full/remaining effect durations, refundable doses, number of Purifier iterations, partner item remainder, stacking/replacement, effect amplifier, and whether the destination bypasses a native skill/boss gate. A 5× duration result is not automatically an exploit for ordinary buffs; what matters is repeatability, privileges, and progression.

### Dedicated QA vectors to add

| Test | Pipeline | Must prove |
|---|---|---|
| B01 | Neo Vitae native Flight Flask → player | Native resource-gated temporary creative-style flight works; expiry removes flight cleanly |
| B02 | Flight ACTIVE → EvilCraft Purifier | Privileged effect is not converted to a transferable generic bottle by the approved policy; no blood/bottle/effect consumed when refused |
| B03 | Flight PC bottle (commands, alternate origin) → Demonic Partner | Cannot receive unrestricted fivefold flight; no silent item/dose loss on refusal |
| B04 | Neo Vitae flask directly to Demonic Partner / Iron's cauldron | No whole-flask consumption; privilege + charge rules checked independently |
| B05 | Flight potion or extracted effect → Apotheosis Charm / Ars Jar / Iron potion fluid | No route makes flight infinitely replenishable, duplicates charges or launders gate via vanilla potion |
| B06 | Flight effect via registered stand recipe, edited PC or new potion mod → Hunter Bio-Mutagen | Denied by **effect eligibility** even if source detection succeeds; no mutagen progress |
| B07 | Native Hunter mutation refresh → Purifier | Ambient state remains protected; no free re-bottle/XP |
| B08 | Future Hunter potion-enhancement skills + Flight | Privileged duration/amplifier excluded; ordinary Speed/Strength still improve normally |
| B09 | Two players, one with native flight progression and one without | No transfer of privileged unlock via rebottled or shared fluids; vanilla potions and native flight remain usable |
| B10 | Native attribute flight plus temporary potion, and expiration/grounding | No stuck \`mayfly\` permission, forced fall on removal of one of two legitimate sources, or ability-state desynchronization |

**The specific cross-mod block list and actual runtime patches remain to be reviewed against pinned JARs.** This is an owner-directed design guardrail and a source-backed high-risk QA plan, not an accepted implementation or an in-game exploit proof.


## D. Proposed investigation and test sequence

1. **Type A source + pinned-JAR registry pass:** freeze exact packwiz SHAs, inspect registered potion holders, custom effects, carrier components, material recipes, block handlers, and the named pinned versions. Enumerate all potion-bearing items with actual components rather than relying on names. Confirm exact method signatures and recipe counts for 1.21.1.
2. **Conservation tests:** native C01/C04/C05/C08/C09/C12, then high-risk **C03/C10/C19** with before/after NBT/data-component/durability snapshots and restored items. Record initial/final uses, not just player buff.
3. **Cross-mod pipeline tests:** ordinary brew → Iron Cauldron → vanilla bottle → Ars Jar → Wixie output → Occultism → EvilCraft extraction → charm eligibility. Stop each time special payload would be discarded. Record alchemy resources, fluid amounts, blood and Source.
4. **Hunter XP/mutation security tests:** two players, base registered vs custom-only, double consumption, fresh world time reset, direct application without drinking, every new potion-producing machine, duration/amp enhancement, held/unheld extra charges, re-log and persisted cooldown. Test explicit potion mutation gate separately.
5. **Balance review:** only after exact evidence, decide whether to support one-charge Neo Vitae Flask use in Occultism and Iron's Cauldron; whether custom single-effect charms are allowed; whether source duration/potency enhancements should be harvestable by EvilCraft. These are **design choices**, not an automatic instruction to nerf existing magic.

## E. Current source provenance (upstream source, not binary validation)

- [Neo Vitae 1.21.1 Alchemy Flask](https://github.com/breakinblocks/NeoVitae/blob/1.21.1/src/main/java/com/breakinblocks/neovitae/common/item/potion/ItemAlchemyFlask.java) / [Flask Recipe](https://github.com/breakinblocks/NeoVitae/blob/1.21.1/src/main/java/com/breakinblocks/neovitae/common/recipe/flask/FlaskRecipe.java) / [Flask Length](https://github.com/breakinblocks/NeoVitae/blob/1.21.1/src/main/java/com/breakinblocks/neovitae/common/recipe/flask/FlaskLengthRecipe.java).
- [Occultism DemonicPartner](https://github.com/klikli-dev/occultism/blob/master/src/main/java/com/klikli_dev/occultism/common/entity/spirit/demonicpartner/DemonicPartner.java): multiplies duration ×5, boosts instant effect by +2, consumes one held stack and gives glass bottle/crafting remainder.
- [EvilCraft CollectPotionPurifyAction](https://github.com/CyclopsMC/EvilCraft/blob/master/src/main/java/org/cyclops/evilcraft/blockentity/tickaction/purifier/CollectPotionPurifyAction.java) / [Purifier config](https://github.com/CyclopsMC/EvilCraft/blob/master/src/main/java/org/cyclops/evilcraft/block/BlockPurifierConfig.java).
- [Iron's AlchemistCauldronTile](https://github.com/Iron431/Irons-Spells-n-Spellbooks/blob/master/src/main/java/io/redspace/ironsspellbooks/block/alchemist_cauldron/AlchemistCauldronTile.java) / [PotionFluid](https://github.com/Iron431/Irons-Spells-n-Spellbooks/blob/master/src/main/java/io/redspace/ironsspellbooks/fluids/PotionFluid.java).
- [Ars WixieCauldronTile](https://github.com/baileyholl/Ars-Nouveau/blob/1.21.1/src/main/java/com/hollingsworth/arsnouveau/common/block/tile/WixieCauldronTile.java) / [PotionCraftingManager](https://github.com/baileyholl/Ars-Nouveau/blob/1.21.1/src/main/java/com/hollingsworth/arsnouveau/api/recipe/PotionCraftingManager.java) / [PotionJar](https://github.com/baileyholl/Ars-Nouveau/blob/1.21.1/src/main/java/com/hollingsworth/arsnouveau/common/block/PotionJar.java) / [PotionFlask](https://github.com/baileyholl/Ars-Nouveau/blob/1.21.1/src/main/java/com/hollingsworth/arsnouveau/common/items/PotionFlask.java).
- [Apotheosis PotionCharmRecipe](https://github.com/Shadows-of-Fire/Apotheosis/blob/1.21/src/main/java/dev/shadowsoffire/apotheosis/recipe/PotionCharmRecipe.java) / [PotionCharmItem](https://github.com/Shadows-of-Fire/Apotheosis/blob/1.21/src/main/java/dev/shadowsoffire/apotheosis/item/PotionCharmItem.java).
- TMM current engine: [HunterExperience](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/compat/progression/src/main/java/io/github/heatwaveqc/tmm/progression/runtime/HunterExperience.java), [HunterBrewing](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/compat/progression/src/main/java/io/github/heatwaveqc/tmm/progression/runtime/HunterBrewing.java), [HunterMutations](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/compat/progression/src/main/java/io/github/heatwaveqc/tmm/progression/runtime/HunterMutations.java). Per AGENTS.md, executable Hunter integration belongs to shared Progression engine; this **research-only** document does not authorize edits to that code.
