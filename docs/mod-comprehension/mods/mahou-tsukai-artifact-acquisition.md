# Mahou Tsukai — pinned Clarent, Emrys and Replica acquisition

Companion to [main profile](mahou-tsukai.md), [catalyst crosswalk](mahou-tsukai-pinned-onboarding.md), [mana](mahou-tsukai-mana-circuits.md) and [Caliburn/Morgan](mahou-tsukai-caliburn-morgan.md). Exact inspected artifact `mahoutsukai-1.21.1-v1.36.31.jar`, SHA-512 `0c4f3bc823d681ad9cb00895d348d2b414201c4d397ca3aa6427b45b8d2b369e0bae8c1f354a9541e4205dadb58c49203d3570e2433d772c3eef1227bc8bfa5a`; [pin](../../../mods/mahou-tsukai.pw.toml), research2026-11-16. **Acquisition handlers traced; full combat/teleport behavior and client acceptance remain incomplete.** Settings below are native defaults.

## Shared prerequisite: actual Strengthening state

Strengthening is the cloth-required two Powdered Diamond + one Powdered Iron recipe. Native scroll cost50, stacking cap50. `setStrengthened` adds5 uses up to cap, applies an Unbreakable component and remembers whether it existed before strengthening. When uses run out the strengthening component is removed and prior unbreakable state restored. A glowing/enchant-looking item alone is not the gate: artifact handlers check `getStrengthened(stack)>0`.

The target-selection helper chooses a nonempty offhand first; otherwise it scans hotbar candidates excluding SpellScroll. Full Strengthening scroll dispatch and use-consumption hooks remain a separate detailed audit; use its Compendium **Strengthening** page and ensure the intended item has positive uses before beginning an artifact event. Do not waste the remaining uses on unrelated attacks/block interactions before conversion.

## Clarent: strengthened sword meets Dragon-owned breath cloud

Native subscribed `RightClickItem` calls `ClarentEffect.clarentOnRightClick` on the server. It checks:

- **Main-hand `SwordItem`**, positive Strengthening uses, allowed by Clarent's item list.
- An alive `AreaEffectCloud` within the player's bounding box inflated2 blocks whose owner is an **EnderDragon**. It is not simply any purple cloud, lingering potion, Dragon's Breath bottle or End dimension location.
- Native blacklist mode true excludes `mahoutsukai:caliburn` and `mahoutsukai:morgan`. Other exact/namespace matcher behavior follows shared item-blacklist helper; no arbitrary mod-sword compatibility is claimed.

On success it selects the first matching cloud, reduces its radius by0.5, creates fresh Clarent with stored attack0, applies Unbreakable when native `CLARENT_UNBREAKABLE=true`, replaces the selected inventory slot or drops output if insertion fails, and triggers native CLARENT. No extra mana drain occurs in this conversion handler. Source sword components/name/enchantments are not copied. A valuable sword is sacrificed, so use an expendable allowed sword.

**How-to:** Prepare Strengthening and an ordinary sword before the Ender Dragon encounter, verify strengthened state in main hand, reach an actual Dragon-owned lingering breath cloud and right-click an item while the sword remains main-hand. The event handler does not require a sword-specific right-click success result. Actual cloud availability, survival damage and competing interaction hooks need a disposable-world test before an authored quest prescribes the maneuver.

Clarent is a blocking sword, not a Caliburn upgrade. Its incoming-damage storage adds half event amount up to500000 default when blocking with it, requests durability damage1 and has release-time stored-energy decay0.02 per held-use tick. Its attack handler derives a base from the target's better held weapon, attempts mana-funded stored damage at0.02 mana/damage, clears stored attack and applies Wounded600ticks. It uses a truncated affordable mana cost; zero-cost edge behavior, target-weapon attribute arithmetic, Wounded delegates and cancellations need combat tests. Do not teach permanent additive weapon growth or guaranteed damage reflection from the storage field.

## Emrys: strengthened offhand Stick and a lightning-strike event

Subscribed `EntityStruckByLightningEvent` calls `StaffEmrys.emrysStruckByLightning`. Native `DISABLE_EMRYS=false`. It requires a server-side Player with **exact `minecraft:stick` in offhand** and positive Strengthening uses. `turnStickIntoItem` repeats those checks and replaces offhand slot0 with fresh Staff of Emrys, then triggers EMRYS. No separate mana payment appears in the conversion handler. It replaces the whole offhand stack, so carry one Stick rather than a valuable/full stack.

This is not main-hand Stick, Lightning Rod item crafting, ordinary electric/magic damage, or a generic thunderstorm-presence test. A real strike event must reach the subscribed handler. How to produce a strike reliably in this pack, event cancellation, lightning survival and loss of old stack components remain acceptance questions; do not promise natural weather timing or an unverified spell lightning source as a reliable recipe.

Payoff: staff has held focused lightning, offhand zap support and a handler setting lightning-bolt damage to0 while held in either hand. Those hooks do not prove immunity to all “electric” mod damage. Configured focused/passive costs200/80 and damage/range require their actual tick/delegate audit before reporting a measured rate. Full chain targeting, fire, friendly filtering and phantom Mandatory Fun objective remain open.

## Replica: compound defensive progression, not just a strengthened shield

`Replica.replicaDamageSource` is reached from ordinary incoming-damage handler, before that method later applies Damage Exchange. Native `REPLICA_DISABLED=false`. It requires a blocking ServerPlayer, a ShieldItem as active-use item, and **all** these state checks:

| Mandatory check | Native-default consequence |
| --- | --- |
| Main-hand item exactly `minecraft:shield` | Other modded ShieldItem/main-hand substitutes fail the exact equality, even though the active-use test accepts ShieldItem. |
| Positive Strengthening on main-hand shield | Offhand strengthened shield only produces a wrong-hand hint; no conversion. |
| Damage Exchange uses strictly greater than90% of configured cap | Native cap5 means all5 uses required (`>4.5`), not4 or simply effect presence. |
| Death Collection uses-left ≥ `min(100, configured maximum)` | Native maximum10000000 means100 stored souls. |
| Immunity Exchange effect active | A real active buff, not carrying its scroll. |
| `getArmorValue()>15` | At least16 returned armor points; toughness alone is insufficient. |

It removes the selected shield, creates fresh Replica, sets Unbreakable when native default true, inserts/replaces or drops result and triggers REPLICA. It does not debit mana/souls/uses inside that conversion block. Other handlers in the same event can still alter those resources; do not promise the event preserves every prerequisite. A fresh item does not carry old shield names/banner/enchantments through this handler.

### Build the prerequisite states

1. **Damage Exchange:** cloth-required2 Emerald powder +1 Iron powder. Native scroll cost40, adds5 uses capped5. Its later hurt handler skips void/generic-kill, grants20×floor(event amount) personal mana up to maximum, changes incoming amount to1 and consumes one use. Do this late in setup so intervening hits do not deplete the5-use condition. The field is not an ordinary potion effect.
2. **Mystic Eyes of Death Collection:** cloth-required2 Powdered Eye +1 Iron, scroll cost400, effect duration600 ticks. The highest-priority death handler notifies nearby alive players in radius10 cube who have the effect and are looking at the dying entity. Mob death adds0.25 stored uses; player death1, capped by configured maximum. Killer ownership is not required by that collector loop. The source method must see the entity not removed.100 souls therefore corresponds to400 counted ordinary mob deaths if there are no other gains/losses; this is arithmetic, not a practical farm rate.
3. Stored souls incur regeneration debt (native1 mana per stored use per periodic opportunity), and excessive negative regeneration can reduce souls. Player death-prevention also spends1 stored use, revives to12 health capped to maximum and cancels ordinary death except void/generic-kill. That prevention check does not require Eyes still active, whereas collecting nearby deaths does. Record balance while preparing Replica; stockpiling100 is not “activate scroll once.”
4. **Immunity Exchange:** native scroll cost400, duration1200 ticks. It targets a selected nearby living entity when one is found; otherwise it buffs the user. Ensure the effect actually reaches the intended player. Its identity memory compares damage `getMsgId`: first type records normally; same next type sets amount0/cancels; a different next type doubles incoming amount and updates memory. It is not universal invulnerability and can increase danger.
5. Equip sufficient armor, strengthen an exact vanilla Shield, put it in main hand, and arrange a controlled incoming-damage event while actively blocking. Keep prerequisite balances visible; do not use lethal experimentation in a survival world.

**Event-order boundary:** The highest-priority handler runs Immunity Exchange before the ordinary handler containing Replica acquisition. A repeated matching damage type can cancel before the ordinary handler is delivered; a changed type can be doubled. “Block any hit” is therefore not a reliable unconditional walkthrough instruction. Event bus cancellation delivery, actual shield-block ordering, Strengthening hooks and source type must be verified together. This research does not claim the compound route works in the assembled pack merely because all predicates exist.

Native JEI record classes exist for Clarent, Emrys and Replica with input/display/output fields; viewer registration, displayed conditions and construction hints remain client untested. All three native trigger emitters are source-reachable at acquisition; actual advancement declarations/task detector compatibility must be reconciled before authoring quests.

## Quest and acceptance handoff

| Branch lesson | Action and guide/JEI reference | Truthful completion and useful reward direction |
| --- | --- | --- |
| Strengthening prep | Native Strengthening recipe/page; prepare intended expendable item and verify uses. | Item task alone does not prove component state. Manual until component-aware detector exists; XP+ordinary materials. |
| Clarent | Native Clarent/Strengthening references; main-hand strengthened sword, Dragon-owned breath cloud, right-click. | Actual Clarent possession + native advancement candidate; XP+End survival supplies, not the next artifact. |
| Emrys | Native Staff of Emrys/Strengthening references; one strengthened Stick in offhand, receive real lightning-strike event. | Staff item + native advancement candidate; XP+food/lighting. Reliable strike setup must be tested before quest release. |
| Replica prerequisites | Native Damage Exchange, Death Collection and Immunity Exchange pages; stock actual counters, armor and correct shield. | Counters/effect/armor need actual state detection or honest manual checks. XP+survival consumables, never fake completion from scroll possession. |
| Replica conversion | Maintain all gates, actively block controlled eligible damage and obtain Replica. | Replica item/native advancement candidate; no promise normal shield-hit event always reaches handler. |

Minimum positive/boundary tests (none run):

- Clarent: genuine Dragon-owned cloud vs ordinary lingering cloud; Strengthening1/0; allowed sword vs banned Caliburn/Morgan; main/offhand; source slot/output/cloud radius changes.
- Emrys: offhand exact Stick1 strengthened vs main-hand/unstrengthened; genuine strike vs generic lightning-like damage; one-item vs multi-item offhand replacement and event cancellation.
- Replica:5/4 Damage Exchange uses,100/99.75 souls, armor16/15, main/offhand exact Shield and modded shield; record original shield, resulting item and counters before/after one controlled event.
- Repeat/change Immunity Exchange damage IDs and cancellation; compare collector active/inactive, looking/not looking, range and soul debt. Never report stack of400 deaths as a measured rate.

Evidence: exact `effects/projection/{ClarentEffect,StrengtheningSpellEffect}`, `item/{clarent/Clarent,emrys/StaffEmrys,replica/Replica}`, `effects/exchange/{DamageExchangeSpellEffect,ImmunityExchangeSpellEffect}`, `potion/{DeathCollectionEyesPotion,ImmunityExchangePotion}`, corresponding scroll classes, `handlers/ServerHandler` event order, `MTConfig$Server`, native recipe registrar and JEI record classes. Combat, teleport/cover/shockwave, weapon durability, full Strengthening event lifecycle and guide-page completeness remain open; main profile stays researching.
