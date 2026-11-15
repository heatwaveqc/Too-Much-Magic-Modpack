# Monk Armor Lock / reserved fist — targeted QA gate

**Target:** NeoForge 1.21.1, standalone `tmm_progression`, Corail Tombstone `tombstone-neoforge-1.21.1-9.5.6.jar`. This is a **test plan, not evidence of a client playtest**.

## Locked policy

- A Monk wearing **any conventional vanilla armor** loses all cultivation powers, independent of the Armor Lock toggle. The `armor_lock` and `reserved_fist` control effects remain available even while wearing armor.
- Armor Lock defaults **ON**. ON drops removable equipped armor as *one entity per equipped piece*, including with a full inventory, without an inventory insertion requirement. OFF does not remove armor; a Monk wearing armor still loses their powers.
- Curse of Binding armor cannot be forcibly unequipped. When ON and damageable, it takes **one ordinary item-durability-wear attempt per 20 server ticks**. Unbreaking and Life Mending still apply. OFF stops wear. Undamageable bound gear stays equipped and suppresses cultivation.
- Hotbar inventory index `0` can contain items **if the player deliberately puts them there** by mouse, drag, number-key swap or offhand swap. The slot must not be selected for **automatic** `Inventory#getFreeSlot`, `getSlotWithRemainingSpace`, or `Inventory#add(0,...)` insertion. No tick-based forced emptying or item deletion.
- Never silently turn on lost-item deletion, no-drop logic, or hidden replacement stacks to maintain a reserved slot.

## Disposable-world acceptance matrix

| ID | Test | Required evidence |
| --- | --- | --- |
| A01 | Monk ON + equip ordinary armor, empty inventory | Armor item drops into world once; vanilla armor slot empties; no second drop next tick |
| A02 | Monk ON + equip ordinary armor, inventory completely full | Drop still appears; armor slot empties; inventory contents unchanged |
| A03 | Monk OFF + manually equip armor | Item stays worn indefinitely; Iron Palm, Blink, Step Assist, mining and defensive Monk buffs disabled |
| A04 | Wearing armor, switch Armor Lock OFF→ON | Existing removable armor drops once; power returns once unarmored |
| A05 | Monk ON + Curse of Binding, damageable + Unbreaking | Cannot be ejected; normal wear attempts every 20 ticks with Unbreaking behavior; no repeated extra wear on equipment-change callbacks |
| A06 | Monk ON + Curse of Binding + Life Mending | Wear and enchantment repair interact ordinarily; OFF stops wear, no direct health debit by Armor Lock |
| A07 | Bound undamageable armor, toggle ON | Remains worn, no durability attempt, Monk buffs stay suppressed |
| A08 | Manual mouse-click, shift swap, F/offhand swap, selected slot 1 | Deliberate index-0 item remains present across subsequent server ticks; combat requires empty main hand as before |
| A09 | Item entity pickup with empty index 0 and ordinary free slots | Auto inserts into other slots, never index 0; pickup stats/advancements still behave normally |
| A10 | Pickup matching stack already **manually** in slot 0 | Automatic stack merger skips index 0; merges elsewhere or leaves item on ground safely |
| A11 | Pickup when only index 0 has capacity | Original item stays on ground (including a partial stack); no loss, duplicate, or hidden slot |
| A12 | Normal scripted `Inventory#add(ItemStack)` and explicit `Inventory#add(0,ItemStack)` | Both avoid index 0 when Monk is enrolled, without changing non-Monk behavior |
| A13 | Corail Tombstone 9.5.6 grave recovery when fist slot was empty at death | Recovered item goes to other slots, or is left safely in grave/on ground; **never** auto-fills index 0 |
| A14 | Tombstone recovery when protected item was manually in slot 0 at death | Record whether mod restores exact-slot placement versus generic auto-insertion; ensure no item loss or duplicate |
| A15 | Login, respawn, dimension change, multiplayer and /reload | Correct no-armor effect suppression and restored toggle state; no ghost modifiers |
| A16 | Player with no Monk path | All normal auto inventory insertion and armor rules remain unchanged |

**Unverified external behavior:** Corail Tombstone may restore saved items by calling `Inventory#setItem(0,...)` or changing the backing list, not vanilla automatic insertion. The targeted Mixin intentionally does **not** block direct slot writes because these are also used by manual movement. If A13 or A14 fails, trace the pinned Tombstone 9.5.6 exact restore path and implement a narrow optional recovery adapter; do not put a universal `setItem` ban back in.

**Release status:** compilation/JUnit and vanilla standalone server smoke do not certify A01–A16. Mark each case passed only from actual multiplayer/pack-integrated test evidence.
