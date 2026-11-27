# Ars 'n Spells3.3.5 — pinned carriers, teaching resources and advancement credit

Exact `ars_n_spells-3.3.5.jar`,2325760bytes, fullSHA512 `21f27911378853800129de4123b406a1b7dff2993a986f3c944761f059becac55509925f77b8f8ce49502dfc6747b397407454c85b9a339f6c003891d13400e2` matched packwiz. Complete JAR decompiled with CFR0.152 including inner classes, externally under `Temp/tmm-magic-research-20261116/ans-fresh`. [Parent addon profile](ars-n-spells.md) remains researching: this closes carrier/task evidence, not the entire mana/damage/cooldown/recipient bridge audit. No gameplay test.

## Guide resource correction

The binary **does ship six Patchouli resources** under `assets/ars_nouveau/patchouli_books/worn_notebook/en_us`: category `ars_n_spells` plus entries `loom`, `transcription`, `binding`, `unbind`, `rules`. Loom/transcription/binding carry `mod:irons_spellbooks` flags; rules/unbind do not. Recipe pages name `ars_n_spells:spell_loom`, `ars_n_spells:apparatus/spell_transcription`, `.../spellbook_binding`, `.../spell_uninscription`.

This corrects the old profile's blanket no-guide-resource claim. It **does not prove those Patchouli pages appear in Ars5.13.5's native Java Notebook screen**. A whole-addon decompiled-class search found no `Documentation`, `DocEntry`, Patchouli or native entry-registration references; absence of such strings is only that search result, not proof all runtime compatibility is impossible. Obtain the parent Notebook (Book+lapis-gem-tag item, right-click; [native guide audit](ars-nouveau-pinned-guide-glyphs.md)), use its Getting Started/ritual information and JEI apparatus recipes, and supply the concrete steps below in future quest text. Do not leave a mandatory step dependent on an unconfirmed book page. Effective visibility/search/rendering is an acceptance case.

## Recipes, inputs and actual Loom transaction

Exact seven data recipes: Loom grid ` G /LBL/OOO` with Gold Ingot,2Lapis,Book,3Obsidian; Blank Scroll shapeless Blank Parchment+Source Gem. Five Apparatus recipes create ritual tablets, separate from performing a ritual:

| Tablet | Reagent | Unit pedestal ingredients | Crafting Source |
| --- | --- | --- | ---: |
| spell_transcription | Novice Spell Book | Iron's spell-book tag,Archwood-log tag,Source Gem Block |2000 |
| spellbook_binding | Novice Spell Book | Iron's spell-book tag,Iron's Scroll,Source Gem Block,Archwood-log tag |2500 |
| spell_uninscription | Blank Parchment | Water Bucket,Source Gem,Archwood-log tag |500 |
| mana_infusion | Blank Parchment | Source Gem Block,Source Gem,Arcane Essence,Archwood-log tag |1500 |
| mana_well | Blank Parchment | Source Gem Block,Arcane Essence,Water Bucket,Archwood-log tag |2000 |

All five set keepNbtOfReagentfalse; all except Uninscription have Iron's-loaded conditions. Reagents/pedestals are apparatus recipe inputs, not reusable spell sources. Follow [Ars Apparatus layout/payment](ars-nouveau-pinned-crafting.md). These Source amounts buy tablets, not a declaration of per-cast or per-tick mana cost.

`LoomInscription.plan/apply`: slot0source,slot1target,slot2output. Output must be empty, Iron's loaded, target an Iron's Scroll or addon Blank Scroll, source extractable as an Ars spell, and glyphs not blacklisted. Normal inscription refuses a filled/nonempty target; conversion uses a blanked single copy of that target for planning. `InscriptionPlanner` sets one transformed target, source cost0for reusable kind or1otherwise. Apply validates outcome and creates a real Iron's carrier **before** extracting source/target; failed creation preserves them. Then it extracts source if required, extracts one target, writes output and marks changed. No XP/Source/mana debit in this transaction; later casting still pays.

`LoomRequestHandler.execute` requires a valid live nonspectator/nonsleeping sender, actual valid Loom menu, admitted request and matching menu/session/inventory revision/school-map digest. It rejects stale or overbudget names; trims name and canonicalizes cosmetic choices. Actions1/2 apply inscription/conversion; only `ok` grants transcription advancement to that sender. A mere preview, output possession or raw direct invocation is not that request success path.

## Binding and cast credit

`SpellbookBindingRitual.onEnd` is server-only and checks enabled config, then alive/nonempty dropped item entities in the **Brazier block AABB inflated3**, not a spherical3block distance. It requires exactly one carrier entity and one Iron's book entity, rejects extras and asks the shared binder to add the entry. Duplicate/full/disabled/invalid/uncastable/failed binding returns before item consumption. ADDED writes the changed book, shrinks1scroll, plays effects, reports success and grants `bind_spell` if a server recipient resolves. Book remains; stack size and entity count are distinct.

`AnsRitual` stores initiatorUUID on start and serializes it. `RitualFeedback.resolveRecipient` prefers the online initiator via server player list **without a local distance/dimension check in this method**, otherwise nearest player whose bounds intersect block AABB inflated16. Ritual item ownership is not used here. Therefore ritual credit is not proof the credited player threw every input or stayed near it; no team/claim policy is established.

Iron's Table alternative: `IronsTableBindHandler.handle` only intercepts addon carrier kind, posts cancellable `InscribeSpellEvent`, then shared binder. On ADDED it removes1scroll, changes/broadcasts book slot and grants interacting server player `bind_spell`; ordinary scrolls pass through. Command binding also grants this advancement after successful bind and1scroll debit. It is not exclusive evidence of the Brazier method.

| Native advancement / pilot | Declared JSON trigger and exact binary award | What it proves / does not |
| --- | --- | --- |
| craft_spell_loom /43 | inventory_changed for Loom | Possession, not crafting or UI use.45XP+3Blank Scrolls in pilot. |
| transcribe_spell /44 | impossible; `LoomRequestHandler` only after `apply` returnsok | Completed Loom transaction for sender, **not generic Transcription ritual** (no award call in that class).60XP+8Arcane Essence. |
| bind_spell /45 | impossible; successful ritual recipient, Iron's Table handler, or command | Shared successful bind, not one exclusive method or caster ownership.75XP+4Source Berry Rolls. |
| first_cross_cast /46 | impossible; `CrossCastingHandler.serverHandleCast` on castOk, or `ArsCrossProxySpell.onCast` on delegated success | Server cast acceptance; not damage dealt, target kill, every pulse paid or desired final world effect.125XP+Enchanted Golden Apple. |

`AdvancementUtil.grant` resolves namespaced advancement, skips null/offline/missing/done and awards every remaining criterion; it does not check completion of the displayed parent advancement. JSON parent order Loom→transcribe→bind→cast organizes the tab, not a prerequisite enforcement graph. Operator commands/manual advancement grants are also separate from ordinary survival operation evidence. Stable quest IDs and current files: [49-node crosswalk](ars-nouveau-pilot-crosswalk.md).

Generic `SpellTranscriptionRitual` is a separate operation: at end it classifies one source entity and one blank target entity, refuses already-inscribed/ambiguous inputs and blacklisted/missing sources, plans one transformed target, and writes cross-mod spell component. If target stack has a remainder, it attempts to spawn the inscribed copy before reducing target/source; failed spawn returns without debit. It then consumes the source only if required and reports success. It does **not** award `transcribe_spell`, so using this alternate route cannot be assumed to complete pilot44. Use mainhand ordinary inventory/right-click carrier casting; sneak-right-click cycles. Server rejects offhand, open-container, dead/spectator/sleeping, mismatched held-reference, future schema and Iron's spellbook direct-carrier requests. Cycling returns before cast advancement.

For Ars carrier casting, helper validates payload and glyph integrity, constructs native SpellResolver, checks canCast then uses the boolean returned by onCast. Bound Iron's wheel proxy has base advertised mana0, but delegates to the same Ars path/payment and grants only on returned success. **Proxy zero base mana does not establish free casting**. Exact unified payment/cooldown/rollback and Iron's counterpart methods still need a full two-sided pinned audit.

## Lifecycle and test boundaries

Transcription/Binding/Uninscription/ManaInfusion extend `AnsRitual`: its tick increments progress and finishes at60. ManaWell instead extends native AbstractRitual directly, overrides tick to scan world players in inflated AABB and add configured mana, with empty onEnd; it doesn't inherit that60tick timer. Parent Brazier tryTick/lifecycle/Source and actual stop/removal must be traced separately before claiming duration or sustainable economy.

Later acceptance cases: Loom preview/occupied-output/stale request preserve inputs; valid single inscription credits sender; generic ritual does not earn Loom advancement; bind rejects duplicates/fullbook/extras without scroll debit; compare initiator online far away/offline-nearest credit; Table event cancellation preserves scroll; failed/short-mana/invalid cast doesn't earn cast advancement; accepted harmless cast can earn it without damage; cycle/offhand/open-menu do not. Inspect effective guide pages and both native spell payment pools, plus world save/reload. None run here; no runtime/quest/config changes.
