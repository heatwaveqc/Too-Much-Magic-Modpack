# MXP ritual settlement — accepted reward bands and remaining eligibility audit

Owner decisions 2026-11-18, PR #347. Supersedes the *proposal* values and 1.2 growth default in historical #48 at ad9c519029e2aa3653f885ea788e84ed0df5a76d. Default growth is 1.02 (world-server configurable). This is an authoritative design lock for the amounts below; **no live recipe reward registration, integration activation, or gameplay hook is implemented by this record**. Recipe assignments and other providers' numbers still require classification/approval.

## Accepted bands (first fixed effective MXP / repeat base MXP)

| Provider | Tier | First | Repeat |
|---|---|---:|---:|
| Occultism | Foliot | 1000 | 200 |
| Occultism | Djinni | 1750 | 350 |
| Occultism | Afrit | 3000 | 600 |
| Occultism | Marid | 5000 | 1000 |
| Forbidden & Arcanus | Required forge tier 1 | 1000 | 200 |
| Forbidden & Arcanus | Required forge tier 2 | 1600 | 320 |
| Forbidden & Arcanus | Required forge tier 3 | 2400 | 480 |
| Forbidden & Arcanus | Required forge tier 4 | 3500 | 700 |
| Forbidden & Arcanus | Required forge tier 5 | 5000 | 1000 |
| Enchanted | Basic | 1000 | 200 |
| Enchanted | Major | 2000 | 400 |
| Enchanted | Exceptional | 4000 | 800 |
| JAWR | Basic | 1000 | 200 |
| JAWR | Major | 2000 | 400 |
| JAWR | Exceptional | 4000 | 800 |
| Eidolon | Basic | 1000 | 200 |
| Eidolon | Advanced | 2000 | 400 |
| Eidolon | Major | 3500 | 700 |
| Totemic | Introductory | 1200 | 240 |
| Totemic | Advanced | 2200 | 440 |
| Totemic | Exceptional | 3500 | 700 |
| Astral Sorcery | First personal constellation attunement (once per player, not one per constellation) | 3000 | 0 |

First only genuinely distinct successful ceremonial action per player and canonical activity family; repeat ordinary divided, no fixed repeat. Astral attunement is one fixed milestone; reattuning is 0 (skip service call for zero repeat rather than feed invalid zero to existing reward code). Shared actual ritual transaction/recipe ID and verified actor, not spectators or altar vicinity. Record actual terminal successful world effect/result, not startup, progress, advancement that precedes actual output, failure, cancellation, item retrieval, automated maintenance. No double awards for same ritual/output/advancement/paid-resource settlement. Boss victory retains independent global boss award; avoid turning simple summon/retry into an extra XP faucet. Ordinary magic crafting does not receive fixed first reward; no blanket divided crafting XP either; separately authorized significant crafting/milestone awards only. Ceremony producing an item is not automatically routine crafting—judge the actual ritual performance, setup, cost, distinct outcome, and ownership. Conversely expensive or long item processing alone is not ceremony. Retain native recipes; this is reward classification, not balance changes to magic school content.

## Remaining provider scope

**Owner exclusion: Theurgy.** No MXP source (including alchemy, transmutation, vats, processing, crafted gear, milestones) and no dedicated MXP skills integration. Native mechanics retained. Generic independently eligible global boss/combat rules remain generic, not a Theurgy provider reward. Remove Theurgy from prior suggested completion adapters.

| Provider | Proposed eligibility | Ordinary work / exclusions | Evidence and next issue |
|---|---|---|---|
| Roots Classic | Real altar rituals (including mechanically ceremonial item-output rites); consider 0–2 native recipe levels as tier signal, not automatic full reward for every recipe. | Staff spells pay Terra; ordinary crafts, decorative variants, routine imbuer copies or repeating no-effect weather excluded or held. | Exact pinned report #265, roots-classic-pinned-rituals.md: 46 recipes, fixed progress200, incense and altar slots consumed; no reliable caster ownership across save/reload proven; certain effects have unchecked/faulty results. Require manual initiator capture, actual outcome, dedup, safe failure; don't grant for unsafe sacrifice until resolved. |
| Neo Vitae | Real Diviner/rune ceremonies: discrete successful world effects, dungeon breach/armor rites, meaningful first commissioned ongoing rite. | Ara Vitae crafting, Athanor/Hellfire Forge, routine Alchemy Array item recipes; no ongoing ritual ticks, passive Well of Suffering, duplicated Anima EV spend receipt. | #214 neovitae.md, native 45 ritual guide entries (not necessarily 45 unique recipes). Need terminal/funded activation and persistent ownership; explicitly distinguish ongoing versus one-shot rite; only deliberate verified successful fresh execution, no trivial toggle/restart farming. Exceptional array world effects need separate review. |
| Goety (+ Cataclysm) | Genuine Dark Altar summoning/conversion/binding or ceremonial crafting with structure/environment/research requirements. | Ordinary item recipes, summoned servant upkeep/combat and focus casting already in paid path. | #265 goety-ritual-gates.md: 15 structure families, distinct environmental gates, actual soul payment and failed partial inputs; need recipe/actor/success data, group variants by meaningful effects. Do not count soul debit plus same altar ritual. Boss summons distinguish from kill. |
| Primal Magick | True Ritual Altar with wand activation, salt-connected props, offerings, stability and mishaps; meaningful personal attunement may be milestone if separately approved. | Arcane Workbench, mundane spell inscription/recipe, Mana Font/relay/magitech, ordinary research and spellcasts already paid. | #207 primal-magick.md at 8aaac9a6f1ea24c0e89f38c7d53aaf2a0b70d30a, 6.0.7 immutable release 5977468bda968b994e54066236ec5719295ac9c3; ritual outcome registry, initiator persistence and failure paths remain. |
| Malum (+ Gaze) | Real totem rite activation/effect, potentially personal Soul Binding/Geas if true performed pact rather than station craft. | Spirit Altar infusion, Spirit Crucible, runework, repair, Weeping Well routine processing, passive totem pulses or spirit drops. | #172 malum.md, #197 records source pin mismatch: guide/source-family only, no exact binary proof. Need rite registry, actor, actual effect, ongoing lifecycle, anti-restart, soul-binding mechanics. Do not promise fixed reward for every pact recipe. |
| Botania Revived (+ Extra Botany) | Hold rare actual player rituals for review; Gaia summoning is preparation for global boss victory, recommend boss-only for this event. | Runic Altar, Terrestrial Agglomeration Plate, Mana Pool infusion, Elven Trade, automatic production: magical crafting/processing. Portable spell expenditure stays existing source. | Compare exact 2.0.2 pin and addon rites; no new blanket craft award. |
| Pastel | Hold Fusion Shrine for exact recipe/lifecycle audit; elaborate structure or environmental gate alone not proof of ritual. | Pedestal, Spirit Instiller, Cinderhearth, Enchanter, Crystallarieum, Titration Barrel and normal Fusion item recipes are processing/crafting by default. Portable paid ink candidate separate. | #173 pastel.md: source-family and secondary exact-version guide, branch-to-JAR not established; specify if any exceptional actual ceremony. |
| Hexerei | No verified genuine ritual in pinned selection; no provider-specific award recommended. | Mixing Cauldron, candle infusion, drying, priestly-looking Book of Shadows altar (display), brooms, blood collection and companion mechanics. | #171 hexerei.md source metadata near-release mismatch 0.5.0.1 vs pinned 0.5.0.3, exact pin check required. |
| Feywild | No genuine ritual established; optional story or first summon only if owner wants ordinary milestone award. | Five-input Fey Altar produces summoning scroll; no fixed ritual reward per craft, no summon/dismiss loop, pixie tasks. | #184 feywild.md; altar lacks player UUID in earlier audit; separate genuine summon from ingredient processing. |
| Scriptor Magicae | No genuine ritual; optional unique complexity milestone only if separately approved; recommend no routine source. | Spell writing/copying, cooldown-only cast and casting lectern automation not resource-paid or ritual. | #48 and pinned Scriptor profile; no attributable significant ritual proof. |
| Summoning Rituals | Only pack-authored genuine manually performed ritual definitions, per recipe explicit tier. | Framework installation, basic cheap recipe, automatic output no credit. | #265 exact 3.14.2 profile: no built-in recipes; no authored recipes in audited baseline (check new merged config). Need actual actor and outcome callback, no start sacrifice as completion. |

Additional tier assignments and numbers for these unapproved providers remain open. Theurgy exclusion is locked; other exclusions/holds above are review recommendations, not new owner decisions.

### High-volume canonicalization

Occultism pinned audit #265 has 158 native recipe records (not 158 guaranteed independent first awards). Preserve genuinely different binding/job/miner tier/major progression rituals, including real ritual-produced items. Collapse light/dark cosmetic forms of identical effects and equivalent repair/recopy/rebinding identities; consider routine repair/upgrade spam and no-effect recipes separately. Use required bound book or actual recipe's documented ritual tier, not held highest book or altar hardware; do not treat all JSON recipe IDs as automatically eligible. F&A required recipe tier, not physical forge tier. Enchanted/JAWR and Eidolon need actual rite tier classification and equivalent targeting aliases. Totemic exact 14 ceremony IDs with music requirements available in #265; suggested classification only: intro war_dance, depths, fertility, zaphkiel_waltz, animal_growth, buffalo_dance; advanced rain, drought, flute_infusion, eagle_dance; exceptional cleansing, sun_dance, danse_macabre, baykok_summon. Final success/result and actual participating players required. Baykok summon effect delayed until tick79 of 80; not an award at startup-success, and no second boss reward before defeat. Rain/drought no-op and missing conversions/no actual meaningful result must not pay. No new gameplay code or runtime eligibility assignments made.

### Handoff
Existing MxpService.ritual/craft and shared per-player first-completion ledger remain architecture. Next: build explicit recipe-to-canonical-family/tier catalog (no inferred blanket namespace awards), validate native terminal receipts and actor capture, define applicable new tier values and assignments; test first/repeat/duplicate/no-effect/failure/co-op/autonomous/permission/reload and overlapping paid actions. Source CI for previous normalization b7e978e was green (Verify bootstrap, Standalone progression core, Progression JAWR forms plus MXP contract). No full smoke/assembly/build authorized.
