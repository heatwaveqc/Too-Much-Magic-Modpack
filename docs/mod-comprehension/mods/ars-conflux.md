# Ars Conflux — library behavior and optional player branches

**Status: source-complete for0.1.56library interfaces, native behavior changes and current installed Ars-family bridges.** Six exact-binary companions below cover the content/inputs/outputs, operation and failure contracts. No effective-world config, transformed-mixin order, external consumer or gameplay acceptance is implied. Other mods remain independently researching; the whole research phase is incomplete.

| Field | Evidence |
| --- | --- |
| Manifest / mod ID | `ars-conflux` / `ars_conflux`; library with real native behavior overlays |
| Version | `ars-conflux-0.1.56-mc1.21.1-neoforge.jar`,552,542bytes; [official file9505585](https://www.curseforge.com/minecraft/mc-mods/ars-conflux/files/9505585) |
| Exact inspected hash | SHA1 `97f3c31c624df19209db648e4fe6dd2e67e08d80`; ZIP/resources/full externalCFR0.152, selectedjavap |
| Source snapshot | integration `c11a95fc14955b3e646885d37e33636b9e37ddf1`; pin originally added17a564d8/PR524. Research checkout pin tree older; current remote pin inspected read-only. |
| Dependencies | MC[1.21.1], NeoForge[21.1.250,21.2), Ars[5.13.1,6); optionalElemental/HexLink. No jarjar. MITcode/LGPL3optional translations. |
| Family | [Ars Nouveau](ars-nouveau.md), [Elemental](ars-elemental.md), [Controle](ars-controle.md), [Additions](ars-additions.md), [nSpells](ars-n-spells.md), [sharedSauce74.113](ars-family-pinned-sauce-74113.md) |

## Why it matters and how a player starts

Five useful capabilities: percentage native mana-discount rings; managed NPC mana pools; correct independent Random operands in Controle binary filters; current-source Retaliate targeting; shared execution/targeting/carrier/geometry/school interfaces for consumers. Optional Nearby Allies/Phalanx acquisition support is additional configured content, not guaranteed default unlock.

Conflux has **no separate survival guide, machine ladder, boss or endgame material** in inspected artifact. Start with native Ars: craft Worn Notebook(Book+Lapis-gem tag), open Getting Started, obtain native Spell Book/glyph/workshop tiers through [parent walkthrough](ars-nouveau.md). Use native Glyph Index/Scribes Table/Curios and relevant addon guide entries. No extra Conflux book, guide gift or introductory crafting currency is needed. It is normal for a library to lack three independent early-game unlocks; do not manufacture them.

Three practical setups, with honest stage:

1. **Existing mana workshop:** after native ring materials/access, craft Lesser/Greater Discount Ring using JEI, equip intended Curios slot and compare a known spell. Default Conflux percentages10%/20%replace flat reduction, with rounding/other-equipment/event caveats. It is native equipment, not a free first-day item.
2. **Midgame control spell:** learn Controle binary/Random glyphs via existing Scribes recipes, compose first Random with different augments from second and use a safe observable effect. Conflux snapshots distinct probabilities; no deterministic RNG outcome or extra recipe.
3. **Tier3defensive option:** learn Additions Retaliate through native glyph route, test safe supported reactive/manual use. Current living source attacker replaces stale kill credit while inside hurt; environmental damage can fail. Do not require risky damage for a compulsory quest or promise success from glyph possession.

Three source-supported integration leads: Controle Random/Warp identity, Additions Retaliate/Codex exclusions, Elemental Phalanx/NPC BubbleShield. Their actual recipients and boundary cases are linked below. Source possibility is not pack-tested compatibility.

## Default versus configured behavior

| Area | Artifact default / exact contract | Teaching and scope boundary |
| --- | --- | --- |
| Native rings | percentage mode true, lesser.10/greater.20, summed rate capped1, positive residual roundedup | [Mana contract](ars-conflux-pinned-mana.md); Source is not personalmana; Sauce/Conflux final injection order unverified. |
| NPC mana | Nonplayer living server ManagedCap provider, attachment/profile/native bonuses/20tickregen; default profile100max/5regen | Capability eligibility changes, not guaranteed350cost shield usefulness or visible NPC UI. Ceiling1M/100kdoes not grant that amount. |
| Native dispatch billing | server ars_trigger_billing.enabled false | Optional true mode groups native effects, upfront share then per-dispatch reservation→action→debit; failure/refund boundaries explicit. |
| Nearby Allies | startup enable_nearby_allies false | If enabled, Tier3/base30/radiusclamp8+3AOE1..32, excludes executor/caster; native owner/team/provider alliance, not SauceBooklist. |
| Phalanx support | startup enable_elemental_phalanx false gatesConfluxrecipe | Current Elemental0.7.11.3item registered unconditionally; recipe gate doesn't exclude AdditionsAncientCodex candidate. |
| NEG propagators | startuptrue but actualNEGabsent | No installed extra propagator from switch alone. Zero/Omega/Hex/Adams targets absent; do not add them. |
| Preparation/cooldowns | Native wrappers/providerAPI; no own provider/store registration caller found | No extra default chant inferred. Other eightfamilyJARs lack directConfluxclassreferences; outsidepack consumers/reflection unverified. |
| Geometry/schools/abilities | Incomingvectorcapture; explicit data/APIdeclaredweights/tags; powerdefault0/schoolattributesempty; targetdamage_rules false | No bundled school/ability data, no automatic native membership inference/new vulnerability table. Operator diagnostics are not player progression. |

Startup/server/client files are named ars_conflux-startup/server/client.toml. Bounded current tracked config/defaultconfigs/kubejs searches found no corresponding overrides; generated saves/assembled donors/effective data remain acceptance evidence to collect. Research changes none.

## Optional acquisition and endpoint

[Content companion](ars-conflux-pinned-content.md) lists all4conditional recipes, each160XP points plus exact ingredients. Enabled Nearby Allies uses Conjuration+Manipulation+SourceGem; enabled Phalanx uses Homingglyph+Manipulation+Conjuration+Amethyst. Native Tier3learning/casting prerequisites remain. Two propagator recipes require actual registered NEG form, absent current target. Ingredients/XP are consumed through native Scribes route; no separate Sourcefee in those glyph recipes.

No linear Conflux capstone. Endpoint is understanding effective native overlays and using whichever supported optional branch/provider the pack actually enables. Defaultfalse content is not a mandatory completion blocker or permission to toggle config. Additions Codex grants learned capability, not physical glyph item; its default exclusion bridge is two-sided in companion and full Codex acquisition belongs to Additions profile.

## Complete subsystem evidence map

| System | Exact-binary report / status |
| --- | --- |
| Four recipes, registered optional content, alliance, Additions learning exclusion | [Content](ars-conflux-pinned-content.md), source-backed; effective config/tag/liveunlock unrun |
| Ring quote, native trigger debit, resource reservations, NPC pool, nSpells quote cleanup | [Mana](ars-conflux-pinned-mana.md), source-backed; final injection order/runtime accounting unrun |
| Eleven carrier profiles, onepayload validator, managed conversion/reflection/owner-account | [Carriers](ars-conflux-pinned-carriers.md), interface-backed; no own ProjectileFormimplementation/defaultreflector invented |
| Chant/provider/pendingguard/acknowledgement/persistentdeadline/clone | [Active casts](ars-conflux-pinned-active-casts.md), interface-backed; actual external providers unknown |
| Incomingvector, declaredweights, power/rules/datareload/operator diagnostics | [Geometry/schools](ars-conflux-pinned-geometry-schools.md), source-backed; no bundledschooldata |
| Installed Controle/Additions/nSpells, abilitytags, generic session/budget lifecycle | [Installed bridges](ars-conflux-pinned-installed-bridges.md), both ends/signatures checked; runtime application unrun |

Known compatibility boundary: nSpells3.3.5CrossCastingHandler lacks CastTargetHint/InteractionOrigin signature, so plugin skips3OptionalAns hooks. This is guarded optional skip, not a hard blocker, required upgrade or whole-loader PASS. Generic native wrappers and reflective ArsCastPayments.finish still apply. Managed reflection API has only optional absentZeroShield direct caller inside this JAR; it doesn't fix Elemental native Mirror Shield/prism automatically.

## Walkthrough-to-quest coverage

| Stage / choice | Action and reference | What task can truthfully prove / safe reward direction |
| --- | --- | --- |
| Guide/onboarding | Native Notebook acquisition/open; parent Getting Started | Existing native guide item is proxy. Reuse parent orientation; don't create duplicate library guide gift. |
| Understand changed mana | Native ringcraft/equip/knownspell; Curios/JEI | Ring possession/equip only if supported, manual actualdebit. PositiveXP+ordinarymaterials, no repeated spend payout. |
| Control/defense options | Existing Controle binary/Random and AdditionsRetaliate pages | Glyph possession is proxy; manual safe effect/target observation. PositiveXP/thematicordinarysupplies. |
| Optional enabled targeting | Tier3book/Scribes exact160XP recipe, beneficialeffect/trueally | Acquisition not castsuccess; manual ally/exclusion check. No config-change objective/default-disabled mandatorygate. |
| Optional enabled projectile | Source-confirmed Phalanx route, orbit/effect; separate recipe versus Codex | Learned state/item proxy distinguished from operation. Ordinary positive reward, no admin glyphgift bypass. |
| Advanced consumer/library endpoint | Identify actual installed payload/provider, then its guide and linked contract | No fictitious SDKcraft or tagitem task. Manual/test handoff only until real consumer route established. |

Current integration canonical pilot manifest still49rows, no Conflux mention; canonical quest_content and generated ars_nouveau chapter also contain no Conflux strings in targeted read. Existing49-node [pilotcrosswalk](ars-nouveau-pilot-crosswalk.md) retains stableIDs/criteria/rewards. There is **no current Conflux walkthrough** implied by those nodes. Ring changes may need text correction on native quests; no authoring/regeneration performed. Completion of this research is not completion of quest implementation.

## Remaining acceptance and research boundaries

No unacknowledged mandatory survival stage remains for this library scope. Exact six reports provide positive/negative tests for costs, failures, cancellation, ownership, RNG, capability, transfer and data. Effective config/data, final transformed mixin order, unrelated binary consumers and actual client UI/claims/physical effects remain **unrun acceptance**, not claimed verified-in-pack. Native parent and other addons retain separate source gaps.

Do not treat declarations as installed optional targets, repeatedAPIquote as measured throughput, logical reservation as worldrollback, or output possession as action detector. No runtime/config/quest/pin changes, server/client/gameplay tests, integration assembly or merge were performed. Project source coverage becomes10/43(32researching,1stale); research continues with Elemental consolidation, Elemancy/Additions and other majors.
