# First Monk progression — TWO interfaces, ONE Tyz wallet

**Branch:** `feature/monk-early-mastery-two-trees`, based on the user-tested First Sunset slice. **Status:** implemented as early-rank server code and staged Tyz configuration, pending dedicated-server/client integration verification. The backend and numerical progress are intentionally limited to a four-rank test.

## 1. Native Vampirism Monk progression (runnable when compiled JAR is installed)

- **Enrollment:** The existing First Sunset initiation, root Step Assist and faction membership persist. This code does not change successful sunset tests.
- **Separate purpose:** `Monk mastery XP` is *only* cultivation/faction progression; **never grants Tyz XP, levels or SP**. Vampirism owns native rank and its native skill tree. The master skill backend stays Tyz+MXP.
- **Player attachment:** `MonkPlayer` stores cumulative mastery points and a set of discovered food item IDs as NBT, using the existing serialized `copyOnDeath` attachment. It is not a second global magic skill wallet.
- **Initial sources:** +1 mastery for completed bare-handed training-block breaks (`tmm_monk:training_blocks`), +4 for directly player-credited deaths of hostile `Enemy` entities, and +8 once per new food registry ID after completed consumption (not repeated variants). Source values are **pilot numbers**. Eligibility currently requires active Monk faction and compliant conventional equipment.
- **Cumulative thresholds:** rank 1: 0; rank 2: 20; rank 3: 55; rank 4: 105 mastery. Rank 4 is a **test stop**, not a permanent Monk level cap; registration still defaults to 20 and will accept more when full progression is built. Advancement calls native `IFactionPlayerHandler.setFactionLevel` with proper event/skill sync.
- **Native skill budget:** source registers one extra Monk-only native Vampirism skill point per rank *after* joining (level -1) with `SkillPointProviders`, separate from Tyz. Validate Vampirism's other providers in a client to ensure no unintended stacking.
- **Native faction tree:** original root `Step Assist` (free) branches to `Open Palm` (1 native point; +1 attack with empty main hand), `Unarmored Poise` (1 point; +2 armor in compliant equipment, requires Open Palm), and `Fleet Footwork` (1 point; +5% base movement speed while compliant, directly from root). Their modifiers are transient/reversible on gear swaps, respec, logout and faction departure. No duplicate spell or generic Tyz skill purchased.

## 2. Tyz Monk rank skills (staged, not yet active)

The authoritative common Tyz implementation belongs to the **primary** `design/universal-mxp-progression-registry` branch. We must not create another Tyz XP grant dispatcher here.

`compat/monk/tyzs-prototypes/config/tyzs_skills/custom/data/categories.json` contains **all four default Tyz tabs** plus **all six reserved pack tabs** (Core Magic, Vampire, Werewolf, Monk, Dragon, Lich). Tyz's categories file overrides the whole category map, so losing the default tabs is not acceptable.

Two actual Tyz 6.4+-schema `CUSTOM` skill JSON files are present in the `tyzs-prototypes` folder:
- **Iron Palm Training**, 10 ranks, 0.25 unarmed attack damage per rank, adds to **inert Monk-owned** `tmm_monk:unarmed_training` attribute.
- **Barehand Harvest Training**, 10 ranks, 0.5 mining efficiency per rank (not harvest tier), adds to **inert Monk-owned** `tmm_monk:hand_harvest_training` attribute.

The Monk Java addon registers both attributes and conditionally translates values into real attack/mining bonuses while in the Monk faction and observing the no-conventional-equipment rule. When not a Monk, those custom attribute ranks have no effect (but stay purchased in Tyz once the latter exists). No raw universal `attack_damage` or `mining_efficiency` modifiers are applied directly by Tyz, avoiding uncontrolled cross-form benefits.

**CRITICAL release hold:** both Tyz skill JSONs are `active: false` and live only under **staged prototypes**, because Tyz 6.4.2 and the central MXP-only XP dispatcher / purchase-pre faction gate **are not installed, tested and integrated** on this feature branch. The primary shared-skills integration branch must install Tyz, ensure XP only comes from MXP (disable all native Tyz activity XP), merge this category layout into its one canonical `config/tyzs_skills/custom/data/categories.json`, and enable these nodes with verified server-side purchase/refund gates and no direct-admin bypass for progression. Do not simply copy the files and turn on `active: true` in a public pack. A staged tree file does **not** mean a working Tyz UI.

The native Monk progression is independent of a working Tyz tree. Do not route native mastery XP into Tyz or make normal mining/kills/food award native Tyz XP as a shortcut.

Use operator command `/tmmmonk mastery` to inspect the player’s current rank, mastery, food count and native faction skill points without altering them.

## Smoke tests for next combined test build

1. Enter an existing Monk character from the successful sunset test. Make sure Step Assist remains granted and native skill-tree button still opens.
2. Break barehand training blocks (+1), kill real hostile mobs (+4), and consume new foods (+8 only once each). Verify actionbar XP and **level 2 at 20**, **level 3 at 55**, **level 4 at 105**. XP above 105 must not advance beyond rank 4 in this pilot.
3. Check that **one native Vampirism skill point per new rank** is available. Buy Open Palm, Unarmored Poise and Fleet Footwork; each must show in the native tree and produce its documented stat effect only when eligible.
4. Equip ordinary armor, a sword, bow or shield; bonuses and Step Assist should stop. Remove them; purchased native skills should resume without duplicate modifiers. Confirm Curios/spellcasting implements aren't accidentally disallowed by the vanilla item tag.
5. Verify vanilla mining and combat and food sources don't accrue Monk XP for non-Monks. Repeated foods must award no additional discovery points.
6. Log out, restart, die and change dimension. Verify all Monk state, rank, purchased skills and consumed-food history persist, and that no additional ranks are minted.
7. Test protected blocks, player-placed block loops, mobs whose deaths are canceled by resurrection systems, summon-owned kills, modded food component behavior, and multiple multiplayer characters; these require expanded anti-farm/detection logic for final release.
8. **Only after the primary Tyz/MXP owner enables the staged category**: Verify both Tyz nodes appear under one `tmm_monk` tab, only real MXP grants Tyz XP, purchases require Monk faction, refund and bulk buying obey gates, benefits are suppressed when faction/equipment invalid, and no other category is overwritten.

## Known gaps / blockers

- The pilot does not yet implement permanent suppression of player-placed training block loops; rank 4 stop limits impact, but this is **not survival-release antifarming**.
- The early numbers are deliberately not the final 20-rank balance.
- External transformation checks (dragon, lich, etc.) still depend on Transformation Compat.
- Native SP provider stacking and skill tree ordering require an actual client check.
- No active Tyz install or universal MXP dispatcher in this feature branch, so second UI **is prepared but not yet visible**.
- No current claim of in-game validation for new progression or skills. First Sunset / Step Assist were player-tested on the earlier slice.
- Original-style bottom-right radial progress HUD remains a full-release target; new mastery awards use text for test builds.
