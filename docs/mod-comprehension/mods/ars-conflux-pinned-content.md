# Ars Conflux 0.1.56 — optional glyphs, allies and random-learning exclusions

Exact Conflux552,542byte JAR SHA1 `97f3c31c624df19209db648e4fe6dd2e67e08d80`: [profile/metadata](ars-conflux.md). Direct CFR0.152: ArsConflux, all3condition classes, startup/client config, OptionalGlyphs, MethodNearbyAllies, Allies, NegFormGlyphs/PropagateMethod, GlyphAcquisition/GlyphAcquisitionPack, ArsAccess visual method. Two-sided recipient: exact Additions21.3.0 SHA512 pin from [artifact receipt](../COMPLETION-STATUS.md), CodexEntry/Lost/Ancient and empty packaged forgotten_knowledge tag; native Ars5.13.5 Config.isGlyphEnabled. Full Additions CFR external tree now available. No client/recipe-reload/random-loot/gameplay acceptance.

## Entry and effective scope

This is an execution/compatibility addon, not a separate altar tier or physical guidebook. No packaged Patchouli/Modonomicon/book resources were found; use ordinary **Worn Notebook → Glyph Index**, native **Learning Glyphs / Scribes Table**, and JEI's glyph recipe view for enabled registered glyphs. Native Notebook generation from registry supplies glyph descriptions; no dedicated Conflux chapter/guide acquisition was established. Future quest text must supply optional-config explanations rather than invent a new book.

Registers `ars_conflux-startup.toml` (STARTUP), `ars_conflux-server.toml` (SERVER), `ars_conflux-client.toml` (CLIENT). Startup false defaults for **enable_nearby_allies** and **enable_elemental_phalanx**; NEG propagators defaulttrue but require actual `not_enough_glyphs`. Current integration c11a95fc has no NEG pin and no matching Conflux override string in tracked config/defaultconfigs/kubejs. This is a bounded source snapshot, not proof of generated save config, all donor overlays or loaded runtime settings. Research changes none.

Nearby Allies is registered only if startup booltrue. Current Elemental0.7.11.3Phalanx registers unconditionally, as [javap-backed audit](ars-elemental-pinned-phalanx-worldgen.md) corrects; Conflux's bool still gates its own Phalanx recipe. No optional setting licenses installing another mod or distributing an enabled feature as a guaranteed current default.

## All four packaged glyph recipes

These are native `ars_nouveau:glyph` recipes, **160XP points** each; materials consumed through native Scribes Table, output1glyph. Conditions are independent of possessing recipe JSON in an archive.

| Recipe/output ID | Conditions and actual registration | Materials |
| --- | --- | --- |
| `ars_conflux:glyph_carian_phalanx` → `ars_elemental:glyph_carian_phalanx` | elemental_phalanx_enabled returns startup.elementalPhalanx AND neoforge:item_exists(Elemental glyph); default recipe conditionfalse | Elemental Homing Projectile glyph +Manipulation Essence +Conjuration Essence +Amethyst Shard |
| `ars_conflux:glyph_nearby_allies` | nearby_allies_enabled returns startup.nearbyAllies; optional registry uses same bool | Conjuration Essence +Manipulation Essence +native Source Gem |
| `ars_conflux:glyph_propagate_carian_phalanx` | glyph_registered condition queries actual GlyphRegistry; registration requires NEG loaded, negFormPropagatorstrue, Phalanx booltrue and Elemental class resource present | Elemental Phalanx glyph +Manipulation Essence +Source Gem |
| `ars_conflux:glyph_propagate_nearby_allies` | glyph_registered; registration requires NEG loaded, negFormPropagatorstrue, Nearby Allies booltrue | Conflux Nearby Allies glyph +Conjuration Essence +Source Gem |

Both propagators Tier3/base mana100/Manipulation; compatible augment Split for Phalanx or AoE for Nearby Allies. They extend AbstractEffect and implement NEG IPropagator only in optional nested Form. PropagateMethod resolves target method by registered ID; server checks target exists/enabled and living frame caster, sets executor to original caster, assigns resolver.castType and invokes target.onCast with EMPTY stack. That local path has no independent ordinary onCast admission call. NEG copyResolver/Conflux propagation mixins and payment still need combined audit before promising anchor position, free recast or final costs. Neither propagator is installed merely from default neg setting when actual NEG absent.

## Nearby Allies if enabled

Tier3/form, native base mana30; compatible AoE only. Radius **clamp(8+3×AoE,1,32)**. Server centers on executing living entity's position/AABB, excludes executor **and original frame caster**, dead and spectator targets, then applies true Euclidean position distance≤radius. Selects LivingEntities satisfying **Conflux Allies.areAllies(original caster,target)**; not every nearby friendly-looking creature. Sorts by center distance then entity ID for deterministic tie order.

For each target still alive/not removed, creates EntityHitResult at bounding-box center, clones resolver, binds target-scoped CastContext, runs branch.onResolveEffect then native Self-style visual/sound. Client returns success without doing server iteration; missing frame caster/executor and an empty candidate set also returnSUCCESS. No built-in requirement for a healing effect or guarantee that chosen remaining effect is beneficial. No ally stack/item consumption or Source Jar charge in form loop; actual personal mana budget/deferred billing must be traced in separate Conflux billing review.

Self/caster exclusion means this form is not a substitute for Self Heal. Cloned per-target context does not itself establish one-versus-per-target mana fee. Creative allies are not expressly excluded here, unlike current Homing Sensitive predicate; spectators are. No line-of-sight/raycast or team-only filter in this query. Actual health/effects/canceled resolves remain native/effect recipient behavior.

## Conflux alliance is different from Sauce Book of Friends

Allies.areAllies accepts: same entity; either direction of native isAlliedTo; intersecting ownable-owner UUID chains; an ownable's actual owner's scoreboard relation in either direction; or a registered provider true in either argument order. Ownership walker includes own UUID/ownerUUID, follows at most8levels with visited UUID guard. Shared ownership can recognize owner/pets and pets together even when owner entity reference unavailable but ownerUUID exists. It does not check every arbitrary ISummon implementation unless it participates in these native ownership/team/provider paths.

Provider registry permits unique ResourceLocation keys, rejects duplicates, returns AutoCloseable removal. **No Allies.register caller was found in this exact Conflux binary.** Sauce Book of Friends SavedData is not read by this class and no bridge registration was established. Therefore a Book-listed creature/player does not automatically become Conflux Nearby Allies recipient from these inspected bodies. Conversely native owner/team alliance may qualify without being added to Sauce's book. External installed providers remain an explicit combined-scope question, not a claim they exist.

## Dynamic Additions random-learning exclusion bridge

Conflux registers built-in SERVER_DATA pack `ars_conflux:random_glyph_exclusions`, selected by default at BOTTOM and not fixed. It dynamically exposes **`ars_additions:tags/item/forgotten_knowledge.json`**, with `replace=false`, values derived from registered glyphs whose namespace is in excluded set, skipping null/AIR glyph items, distinct/sorted. Byte snapshot is cached per opened resource instance; pack selection/reload order and final merged tag must be inspected in acceptance.

Default excluded namespaces: ars_conflux, irons_nouveau, goety_nouveau, bomd_nouveau, cataclysm_nouveau, aquamirae_nouveau, mowzies_nouveau, legendary_nouveau. This declaration does not install absent addons. API can add valid namespaces. **ars_elemental is not in default set.** The term forgotten_knowledge is an exclusion list on inspected recipient, not a list of allowed random rewards.

Exact Additions CodexEntry.getPossibleGlyphs enumerates GlyphRegistry, filters native Config.isGlyphEnabled, excludes tagged glyph items/default starting spells/already-known glyphs; then isValidGlyph matches codex tier. Server use picks one uniformly from eligible list, directly unlocks in player capability/syncs and consumes1codex. If no eligible glyph, awards XP then consumes1: basic55, Lost160, Ancient480, with tiers1/2/3respectively. No direct material glyph output or guarantee of a selected spell. Full codex acquisition/loot/tier walkthrough remains Additions-profile research.

**Current Elemental boundary:** Phalanx is now registered Tier3, default Conflux random-learning tag does not exclude ars_elemental, and Additions AncientCodex can consider enabled unknown Tier3glyphs without checking the Conflux Scribes recipe condition in the inspected filter. Thus **disabling Conflux's Phalanx recipe alone does not prove the glyph cannot be learned through Additions**. This is a two-sided source-supported alternative-learning candidate, conditional on effective enable/tag/starting/known/tier/codex access; no live unlock or full survival path proven. Do not teach blanket default Phalanx impossibility or approve a reward bypass. Native valid spellbook/tier casting rules remain separate after unlock.

This bridge gives a concrete current Additions follow-up: locate obtainable Ancient Codex routes and effective merged exclusion tag before claiming optional glyph acquisition gates. Learning capability state and item possession must not be conflated.

## Walkthrough / tests still unrun

| Optional learning | Action/reference | Truthful completion and safe reward direction |
| --- | --- | --- |
| Enabled Nearby Allies | Confirm recipe enabled, obtain native Tier3book/160XP/materials, Scribes recipe/learn, compose with harmless beneficial effect, put one true team/owned ally and original caster nearby. Notebook Glyph Index →Nearby Allies /Learning Glyphs. | Glyph possession is proxy; manual target/effect check. Positive XP +ordinary ingredients, no mandatory settings changes. |
| Ally distinctions | Contrast native team/owned creature versus Sauce Book-listed-only entity; test personal outgoing guard independently. | Manual acknowledgement; no fictitious team-wide cast detector. Ordinary small reward. |
| Optional Phalanx | Follow [current Elemental route](ars-elemental-pinned-phalanx-worldgen.md), separate recipe gate from actual registered/random-learning pool. | Item/capability-aware task only if supported; don't reward enabling config or claim acquisition proxy proves orbit. |
| Additions exclusion | Inspect final tag/codex pool on authorized test setup, compare excluded Conflux namespace and unexcluded Elemental. | Research/acceptance case, no repeatable random-unlock payout quest. |

Positive/boundary tests: configfalse/true+restart/recipe reload, NEG absent means propagator unregistered, radius clamp/edge/tie order, caster/executor/self excluded, dead/spectator/creative/line-of-sight, owned/team/Book-only/provider alliance. Observe mana admission/partial target failure separately. Compare dynamic pack selected/unselected and final tag, codex tier/known/all-unlocked XP fallback, exact recipe-disabled Phalanx candidate and actual valid-book casting. No edits, assembly or launches performed.
