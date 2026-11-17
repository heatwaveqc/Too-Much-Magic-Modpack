# Mahou Tsukai — pinned William and Kodoku mana routes

Companion to [main profile](mahou-tsukai.md), [mana/circuit rules](mahou-tsukai-mana-circuits.md), [exchange economy](mahou-tsukai-exchange-life-drain.md) and [artifact acquisition](mahou-tsukai-artifact-acquisition.md). Exact inspected `mahoutsukai-1.21.1-v1.36.31.jar`, SHA-512 `0c4f3bc823d681ad9cb00895d348d2b414201c4d397ca3aa6427b45b8d2b369e0bae8c1f354a9541e4205dadb58c49203d3570e2433d772c3eef1227bc8bfa5a`; [pin](../../../mods/mahou-tsukai.pw.toml). Research2026-11-16, **partial native artifact/economy audit, no client/world tests**. Values below are native defaults.

## William: authorship before generation

1. Write and sign a nonempty vanilla Written Book, then give that item positive **Strengthening** uses (see artifact appendix). Prepare an empty vanilla Lectern. Compendium William/R'lyeh pages and JEI are the intended in-game references; their full client instructions remain unverified.
2. Right-click the empty Lectern with the strengthened Written Book. `William.createWilliam(RightClickBlock)` starts configured dictionary loaders and records Lectern position→player UUID. It relies on ordinary interaction to put the book on the Lectern; recording a request alone does not create the artifact.
3. `createWilliamCheck`, called from the player's native10-tick handler, waits until the dictionary map is nonempty and all entries complete. For that player's queued position it checks Lectern/HAS_BOOK, exact Written Book and nonempty text, then attempts fiction creation. Once processed it removes the request; a failed payment or missing book is not an endlessly retried reservation.
4. `MahouSavedData.addPlayerFictionIfDifferent` evaluates new text, charges `max(0, int(style×500))` native mana and creates the William item with author UUID, style and **fame0**. It preserves written-book content in the Lectern replacement. Negative style selects the R'lyeh appearance/behavior while still using the William item class; negative calculated creation cost clamps to0. It is not a separate universally crafted R'lyeh item.
5. The saved fiction registry remembers exact text and owner. Same-author exact-text recreation uses existing style/fame and still charges creation cost. An exact text already attributed to someone else is rejected. New creations per caster are limited to10 records by removal of that caster's earliest record when adding another; evaluate effects on old fame references before making a large library.

### Dictionary and text-scoring limits

Native dictionary selection is English. `DictionaryDownloader` reads `mahoutsukai/<configured name>.txt` when present, otherwise starts an HTTP download and then loads UTF-8 into its searcher. The built-in remote path points to a moving upstream language-list branch; its contents are **not pinned by this mod JAR hash**. This audit inspected loader code, not that remote dictionary or a local effective cache. No download was executed by this research.

Download/read failure logs an error and leaves the entry incomplete in the inspected path; queued creation waits for all dictionaries. A cache can support offline use, but offline success is not established without identifying and testing actual cache contents. Do not promise an always-offline immediate Lectern conversion or fixed style from a quoted sample story.

`WrittenEvaluation` analyzes character, word, punctuation and spacing scores against configured dictionaries, selects the best weighted score, and applies a similarity/plagiarism adjustment against prior fiction before flooring to integer style. Full subscore configuration, text limits and all edge cases remain open. A high page count or unique-looking text does not prove positive/high style. The precise formula depends on dictionary data and prior world fiction; no guaranteed optimized story or mana yield is supplied here.

### Fame and copying

Fresh fame0 gives **zero generation**, even with positive style. `TurnedPagePacket` tracks opening a WrittenBookItem, then increments fame for sequential next-page messages with wall-clock gaps **strictly over500ms and under120000ms**. It excludes the recorded author or recognized alternate account through `AltChecker`. This is not a measured reading-rate guarantee; actual client page notifications, cache/profile lookup and anti-alt mappings need testing. Repeated opening alone is not the inspected increment condition.

TradeWithVillager hooks also add fame when written books appear in offers: input costs increment matching registered text; output trade passes buyer UUID for author/alternate exclusion. It does not establish an obtainable matching villager offer in this pack. `AltChecker` reads optional `mahoutsukai/alts.json` plus profile-cache identity; it is not a universal account-relationship service.

`WilliamCloningRecipe` accepts one William and one or more Writable Books. It returns **ordinary Written Books**, subject to written-content copy generation, and returns the original William as a crafting remainder. This spreads readable text; it does not duplicate the mana-generating artifact/component. The custom recipe requires at least3×3 crafting dimensions. Full generation-limit and post-copy reading acceptance remain untested.

### Carried vs deployed generation

| Form | Exact inspected output contract |
| --- | --- |
| Carried William/R'lyeh | Inventory scan chooses highest positive and most negative `max(0,fame)×style`, sums those two, not every copy. Personal regeneration adds integer truncation of that result×0.01 on its normal100-tick opportunity. Soul debt/other terms can offset it. |
| Deployed positive-style book | Sneak-use main-hand creates WilliamEntity at ray-hit adjacent position and consumes held item after unchecked spawn-add. Every100 level ticks it computes integer truncation of `style×abs(max(0,fame)×0.01)` and distributes among shuffled non-full **same-owner** circuits in strict horizontal range30, no Y filter. It passes onOnly=false: inactive circuits can receive this generation. Shared flag alone does not grant a differently owned circuit this route. |
| Deployed negative-style book | At that same cadence, negative amount is divided by count of nearby Player entities in a radius30 coordinate cube. Eligible ServerPlayers with positive personal mana and without Mana Taken cooldown lose that amount, clamped at0, then get100-tick cooldown. No same-owner/contract exclusion appears here. It is not positive circuit generation or a safe shared-base decoration. |

Fame is refreshed on book use through the saved registry before deployment. Full update/propagation of fame to already-deployed copies remains open. Positive generation has no input fuel debit in `plethora`, but fiction creation, fame and setup are real prerequisites; stable entity persistence/performance, theater behavior and safe storage remain untested. Unowned circuit UUID null handling deserves a boundary test because this helper invokes owner `.equals` directly.

Main-hand entity interaction by the sneaking owner toggles theater. The other branch queues dropping the stored book and discarding the entity without an owner-equality gate; another player's retrieval is not excluded by this method. Spawn/drop rejection can lose the item because add booleans are ignored. William theater, movement/actor control and lifetime/persistence are not complete in this economy slice.

## Kodoku: consumed worm, accumulated death state and fire payout

Exact shapeless starter recipe: one each **Dirt, Carrot, Potato, Poisonous Potato, Rotten Flesh, Nether Wart** → `mahoutsukai:kodoku`. This is accessible only after those materials, including Nether Wart; not a first-day zero-gate mana source. The item starts with Kodoku component0.

- Right-click a non-worm living target with an uncharged worm (value≤0), and no Kodoku passenger already present. The item spawns a worm, starts it riding target and consumes one held item. No tame-owner check is present in this handler; pack interaction/claim protection must be tested.
- Living-killer death handling accumulates target Kodoku + killer Kodoku +1 + configured health/armor bonuses. Native health factor0.1, armor factor0, with separate Mob-killer settings currently equal. The code uses target **current health at death handling**, not maximum health, so do not calculate a bonus from advertised max HP. Attribution requires a LivingEntity damage-source entity; environmental kills do not establish the same accumulation path.
- If the dying target has a Kodoku passenger, the handler copies that target's stored Kodoku into the first worm and sets transfer-to-killer to0; it does not simply give the worm the full killer-transfer formula. It then clears target Kodoku. Timing and preventing loss of the mounted worm need live observation.
- A mounted worm attacks its living mount when health<5, requesting1 damage on native counter multiples of20. It does not automatically kill a full-health host. Other death/farming input is required.
- Interact with the worm entity to spawn a Kodoku item carrying its stored value and discard the entity. This method has no owner restriction and ignores spawn-add success; collect promptly and test interaction protection. A charged item can be placed on a block face to spawn a free worm carrying its value, consuming the held item. Mounting behavior only accepts the uncharged branch.

On a worm's death from ON_FIRE, IN_FIRE or LAVA, payout is `worm value × (8 + nearby ley score)` in a valid ley dimension, or value×8 when invalid. That death handler calls nearby active circuit search at native range10, then `chargeAnyNearbyBatteries`. **The charging helper checks space, not owner/shared UUID**. Non-owned/unshared active circuits can receive Kodoku payout; this is not identical to player draw permission. Excess beyond available capacity is returned as remainder by helper, but this caller does not store or retry it. No circuit/space means lost payout, not a charged item refund.

The helper fills circuits in set iteration order, so nearest-first or owner-priority is not promised. Same strict horizontal/no-Y search applies. Fire payout consumes the dying worm; it is a discrete accumulated-value conversion, not passive mana per tick. Full loot penalties, misfortune/status effects, tool damage, value serialization and integer-limit behavior remain separate Kodoku branches; do not approve an automatic mob farm or all rare-drop yields from this slice.

## Walkthrough and boundary tests (none run)

| Lesson | Actual steps / meaningful completion | Positive vs boundary observation |
| --- | --- | --- |
| Create original fiction | Native book references; sign original text, strengthen, place in empty Lectern, wait dictionaries, fund calculated cost and retrieve artifact. | Positive output/component vs no cache/network failure, insufficient funds, duplicate other-author text and full registry. XP+paper/ink, not a pre-famed artifact. |
| Establish fame | Distribute ordinary readable copies and have a distinct player read sequential pages; use real supported trade only if present. | Fame changes vs own/alternate reader, same page, too-fast/too-late turn; verify actual client event. Item possession is not proof of fame. |
| Choose deployment | Carry for personal regen or deploy near same-owner circuits; measure balances. | Positive book/circuit vs fame0, negative style, differently owned, inactive and unowned circuits. Record actual output and theft/retrieval/protection. |
| Collect Kodoku | Craft worm, attach to expendable test host, generate/observe host value, recover charged worm. | Value-bearing item vs environmental death, full-health mount, preexisting worm and unrelated owner's target. No guessed max-HP bonus. |
| Redeem Kodoku | Place charged worm by safe active circuits and use controlled fire death; measure formula payout and capacity loss. | Owned vs other-owner circuit, active/off, valid/invalid ley dimension and insufficient space. XP+mundane survival supplies; worm item alone proves no energy conversion. |

Exact evidence: `item/william/{William,WilliamCloningRecipe,DictionaryDownloader,WrittenEvaluation}`, `networking/TurnedPagePacket`, `util/AltChecker`, `dataattachments/worldsave/MahouSavedData`, `entity/WilliamEntity`, `effects/Kodoku`, `item/kodoku/KodokuItem`, `entity/kodoku/KodokuEntity`, `mana/PlayerManaManager`, subscribed `ServerHandler`, native config builder and `data/mahoutsukai/recipe/{kodoku,william_book_cloning}.json`. No dictionaries, source dumps, JARs or assets committed. Main Mahou remains researching.
