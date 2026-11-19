# Death/respawn compatibility acceptance test matrix — locked target, unrun
Version: 2026-11-13. MC 1.21.1 / NeoForge 21.1.265 / Java 21. User approved [POST-DEATH-OWNERSHIP.md](POST-DEATH-OWNERSHIP.md) as binding gameplay contract.

## Laboratory requirements
- Use disposable worlds only. Snapshot player NBT/data attachments, inventory (main/armor/offhand), Curios, XP total/level, death counts, location/dimension, progression and items saved in nearby storage **before** every test; repeat after damage, respawn, server restart and recovery.
- Pin exact B3 mod versions first, then add approved mods in broad batches after resolving dependency closure. B3 installed Corail Tombstone is `tombstone-neoforge-1.21.1-9.5.6`; Enigmatic Legacy+ 1.1.2, Vampirism, Goety, Malum etc are **not installed** yet. Do not call these tests passed because files/configs exist.
- Add server-side diagnostics if needed: fatal hit event, amount/cause, `LivingDeathEvent` cancel state and event priority, `LivingDropsEvent` contents/clear operations, Curios drop rules, `PlayerEvent.Clone`, respawn, world/dimension, Arca/phylactery events, once-only player death token. Don't log secret/private user data.
- `keepInventory=false` is the default matrix; test `keepInventory=true` separately. Ensure no user-world writes.
- Preset saved items: multiple 64-stacks; non-stackable enchantments; soulbound equipment; a filled portable bag; Curios; XP; a storage item with internal contents; food and progression artifacts. Record slot-by-slot item and component hashes; verify **no dupes or losses**.

## Case register (planned; zero executed)

| ID | State / circumstance | Steps | Must pass |
| --- | --- | --- | --- |
| D01 | Ordinary human true death | Kill in overworld with inventory + XP, recover grave | **Exactly one Corail grave**, all recoverable gear and prescribed XP, one death statistic |
| D02 | Simultaneous general saves | Activate vanilla Totem, Occultism Guardian, Ars Totem Perk and Mahou soul protection, induce a fatal hit | Survives with **no grave**; at most one rescue resource spent, or log precise overlap as blocker |
| D03 | Enigmatic Legacy+ Cosmic Scroll / Cube / Totem of Malice | One at a time and paired with D02 protection | Cancelled death never deposits items, gives XP loss or spawns cursed soul; detect duplicate resource costs |
| D04 | Malum Wyrd Geas | Trigger with and without active cooldown / source exclusion | Eligible hit cancels death; ineligible lethal hit yields normal Corail grave |
| D05 | Enigmatic Eldritch Amulet | With proposed `keepInventory=false`, die with empty/full Curios | No amulet-side clone/restore or lost item; all eligible items in one Corail grave |
| D06 | Enigmatic Ascension Amulet/Storage Crystal | With `StorageCrystal.enable=false`, die with full inventory and 30 XP levels | No separate item/XP vessel; one Corail grave and one XP accounting |
| D07 | Enigmatic Escape Scroll | Die in overworld, lava, void and in other dimension wearing it | No duplication/teleport of loose items bypassing Corail; decide alternate scroll effect if blocked |
| D08 | Cursed Ring soul loss | Real death with ring and `soulCrystalsMode=NEED_CURSE_RING` `maxSoulCrystalLoss=9` | Exactly one separately recoverable Soul Crystal, one max-health soul-loss increment, **one Corail grave** |
| D09 | Cursed Ring recovery/threshold | Recover soul, then accumulate up to nine lost souls in disposable world | Max-health correctly restored on recovery; no permanent-death screen; no more than nine counted |
| D10 | Cursed XP Scroll and Escape Scroll | Real death with stored experience | Item-specific penalty once; Corail controls all remaining XP and inventory |
| D11 | Ordinary death with gamerule `keepInventory=true` | Kill with full inventory, Soulbound and Curios | No items copied to grave/duplicate slots; XP and curse soul behavior follows explicitly configured rules |
| D12 | Two genuine deaths before first grave recovered | Die twice in separate dimensions, then recover both | Each death creates separate, retrievable inventory owner; neither overwrites/copies the other |
| D13 | Vuln/void/technical deaths | Lava, falling, void, `/kill` and special damage that bypasses invulnerability | Consistent per-source allowed protection; otherwise true death routes to Corail safely; note broken-world edge cases |
| D14 | Vampirism unified vampire, downed state | Trigger DBNO; recover and fail recovery separately | Success: no grave/death penalty. Failure: **one true death** plus Corail grave, with vampiric flags/levels retained as designed |
| D15 | JAWR lich / Goety lich | Trigger successful and failed phylactery/Arca return separately | Precisely classify **pre-death cancellation vs post-death respawn**; no grave on prevented death, exactly one grave on committed death |
| D16 | Archlich dual anchor | Activate both lich paths and force primary success, primary failure/secondary success, and both failures | Exactly one resurrection resolver per event; no duplicated soul costs or corrupt binding; one grave only when real death |
| D17 | Dracolich | Dragon + one supported lich, kill with each source and fail anchor | Dragon species/growth/model persists; no access to second lich, no inventory loss or double grave |
| D18 | Abomination | Four source provider recognitions/trees and custom hybrid level, induce survival and true death | One canonical feeding/death engine; every independent progression retained, one Corail grave on actual death |
| D19 | Hunter | Involuntary bite vs deliberate ritual, then real death | Accidental conversion rejected, confirmed ritual replaces Hunter, respawn and inventory/skill state consistent |
| D20 | Player state persistence | Save/reload, logout, dimension travel, backup restore and death respawn for all species | No stale source flags, cures, skill points, anchor binds or crossmod duplication |
| D21 | Griefing/soulbound/recovery | Exempt enchanted equipment, bound Cursed Ring and Curios + grave retrieval by another player | Item exclusions respected, no second recovery owner, no duplication exploits |
| D22 | Drop/kill credit | Cancelled death by charm, lethal hit to mob and Malum/Occultism/EvilCraft on-death effects | Death-triggered rewards do not accidentally fire twice or on a cancelled death unless explicitly intended |
| D23 | Server/client network consistency | Dedicated server and GUI client for above cases | Correct HUD, gravestone/anchor display, no client ghost inventory, no kick/crash after respawn |
| D24 | Back-to-back logout/respawn race | Save during downed state, anchor return and cross-dimension respawn | Deterministic recovery after restart; one grave, no permanent softlock |

## Promotion and evidence
Each ID must record:
- precise tested mod/JAR file IDs, hashes and generated config values
- commands/steps, event traces and video/screenshot if UI matters
- exact before/after slot/Curios/XP/soul-count/skill progression/dimension/grave inventory
- PASS/FAIL/BLOCKED plus cause and any workaround; mark `not run` until actual execution.

**Gate A (minimum for Corail+Enigmatic enable):** D01, D05–D13, D21 and D23 pass.
**Gate B (preliminary multiple-save safety):** D02–D04, D22 pass or have explicitly accepted bounded behavior.
**Gate C (before transformation pack release):** D14–D20 and D24 pass on complete installed pack.
No ready-for-survival claim before Gates A–C are satisfied.

No test in this document has been executed.
