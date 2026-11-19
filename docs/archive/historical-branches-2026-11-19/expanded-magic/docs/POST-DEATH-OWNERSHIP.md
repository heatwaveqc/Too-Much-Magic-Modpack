# Actual player death: inventory, XP, soul penalties and resurrection ownership

**STATUS: LOCKED POLICY by user on 2026-11-13; IMPLEMENTATION and game-world verification NOT COMPLETE.** Minecraft 1.21.1 / NeoForge, 2026-11-13. Complements the locked player-species target in [TRANSFORMATION-TARGET.md](TRANSFORMATION-TARGET.md) and the protection survey in [DEATH-MECHANICS-AUDIT.md](DEATH-MECHANICS-AUDIT.md). This focuses on what happens **only after an actual player death**, rather than arbitrating every Totem-like save.

## Core distinction

A cancelled/prevented fatal hit is **not an actual death**: no grave, no dropped inventory, no death-related soul penalties, no replayed respawn inventory logic, no death credit. Many mods can offer totem-like protections; **do not build a complicated priority manager just because multiple savers exist**. Test paired saves for double spending, but patch only confirmed conflicts.

If *all* protection/DBNO/lich interception fails and the game commits a real player death, then the pack needs exactly **one authoritative inventory-and-XP recovery path**. **LOCKED: Corail Tombstone is the sole authoritative recoverable-inventory and XP grave owner, subject to explicit soulbound/always-keep rules.**

The *resurrection* of a lich/Archlich/Dracolich may mean a **death was cancelled before being committed** (no grave) or a **true death and subsequent spawn relocation** (grave at death location). Do not guess which without inspecting JAWR and 1.21.1 Goety implementations. Set one finalization policy after measuring these events. Never invoke an anchor rescue *after* a grave was created unless the design explicitly treats that as a genuine death and validates inventory.

## Enigmatic Legacy+ — actual 1.21.1 source audit (major overlap!)

Authoritative target project is [Auviotre/Enigmatic-Legacy-Plus](https://github.com/Auviotre/Enigmatic-Legacy-Plus), its `main` 1.21.1 source. NeoForge 1.21.1 **v1.1.2** is [published as CurseForge file 9141458](https://www.curseforge.com/minecraft/mc-mods/enigmatic-legacy-plus/files/9141458), a release from October 12 2026. Published source availability is not pack installation or proof every release JAR class matches head; confirm exact pin before coding.

### Death prevention — normally non-inventory features; keep eligible

- `CosmicScroll`: `LivingDeathEvent` cancellation at HIGHEST priority with revive cooldown, health restoration and potion buffs.
- `TheCube`: special survival condition HIGHEST, plus a LOW-priority last-chance death cancellation/revive if off cooldown.
- `CreationHeart`: HIGHEST + `receiveCanceled=true`, forces surviving at 1 HP for eligible damage; monitor interference with other cancellation handlers.
- `VoidPearl`: probabilistic death cancellation.
- `DesolationRing`: sacrifices nearby mob to save wearer on death if applicable; can clear drops from mobs killed by ring's wearer (separate effect).
- `TotemOfMalice`: mixin to Minecraft `checkTotemDeathProtection`, like an actual totem, charging its own durability.
- These remain **eligible protective abilities**. Check same-hit multiple consumption and validity of damage-source exclusions; **they do not become grave owners**.
- `RevivalLeaf` is healing/damage resistance rather than a proven player-death replacement; don't falsely count it as one.

### Competing actual-death item handlers — LOCKED handling policy

| Feature | Exact source behavior | Recommendation | Test |
| --- | --- | --- | --- |
| `EldritchAmulet.keepInventory` | `onConfirmedDeath` persists entire main/armor/offhand inventory, empties live slots; `PlayerEvent.Clone` restores saved inventory | **REQUIRED false** when enabled alongside Corail Tombstone | Amulet worn, `keepInventory` true/false, multiple death/clone |
| `StorageCrystal.enable` (Ascension Amulet death storage) | `LivingDropsEvent` copies and **clears all item drops**, preserves XP and spawns recoverable Storage Crystal, possibly at respawn anchor | **Set false** while Corail Tombstone owns inventory/XP | Amulet + mixed Curios, location change, XP conservation, death drops |
| `EscapeScroll` | Moves death item entities to respawn location, then clears original drops | **REQUIRED** suppress its competing item-drop relocation when Tombstone owns the grave. Preserve/replace the item's use only if a safe grave-key/location adapter is later approved | Escape Scroll equipped, overworld/Nether/End, respawn anchor; verify scroll still consumed only on real death |
| `CursedRing.soulCrystalsMode` | Defaults `NEED_CURSE_RING`, spawning **separate Soul Crystal** on true death when curse ring worn and `keepInventory=false` | **RETAIN** as unique, non-inventory *death penalty* | Soul Crystal spawns separate from grave, can be retrieved, HP penalty updates once |
| `CursedRing.maxSoulCrystalLoss` | Defaults **9**; each lost soul crystal reduces max health by 10%; permanent-death check only when >=10 losses AND threshold configured >=10 | **RETAIN default 9** as locked pack policy; 10-soul permadeath is not authorized | Nine lost crystals, subsequent deaths, soul redemption, different dimensions |
| `CursedXpScroll` | `LivingDeathEvent` LOWEST clears the scroll's stored XP | Keep as a **specific item penalty**, not a second XP inventory owner, once tests confirm no clearing on cancelled deaths | Scroll with saved XP, cancelled fatality vs true death |
| Soulbound `CursedRing` | Curio returns `ALWAYS_KEEP`, may have special destroy-on-death mechanics | Keep as **explicitly exempt persistent curio**, not a second full-inventory save | Curios/slot retention, grave retrieval, ring destruction routes |

**Do not claim options are already applied**: verify generated mod config location, exact names and the latest published jar. The options above are read directly from 1.21.1 source:
- [CursedRing.java](https://github.com/Auviotre/Enigmatic-Legacy-Plus/blob/main/src/main/java/auviotre/enigmatic/legacy/contents/item/rings/CursedRing.java)
- [SoulCrystal.java](https://github.com/Auviotre/Enigmatic-Legacy-Plus/blob/main/src/main/java/auviotre/enigmatic/legacy/contents/item/misc/SoulCrystal.java)
- [StorageCrystal.java](https://github.com/Auviotre/Enigmatic-Legacy-Plus/blob/main/src/main/java/auviotre/enigmatic/legacy/contents/item/misc/StorageCrystal.java)
- [EldritchAmulet.java](https://github.com/Auviotre/Enigmatic-Legacy-Plus/blob/main/src/main/java/auviotre/enigmatic/legacy/contents/item/amulets/EldritchAmulet.java)
- [EscapeScroll.java](https://github.com/Auviotre/Enigmatic-Legacy-Plus/blob/main/src/main/java/auviotre/enigmatic/legacy/contents/item/scrolls/EscapeScroll.java)
- [EnigmaticEventHandler.java](https://github.com/Auviotre/Enigmatic-Legacy-Plus/blob/main/src/main/java/auviotre/enigmatic/legacy/handlers/EnigmaticEventHandler.java)
- [CosmicScroll.java](https://github.com/Auviotre/Enigmatic-Legacy-Plus/blob/main/src/main/java/auviotre/enigmatic/legacy/contents/item/scrolls/CosmicScroll.java), [TheCube.java](https://github.com/Auviotre/Enigmatic-Legacy-Plus/blob/main/src/main/java/auviotre/enigmatic/legacy/contents/item/spellstones/TheCube.java), [CreationHeart.java](https://github.com/Auviotre/Enigmatic-Legacy-Plus/blob/main/src/main/java/auviotre/enigmatic/legacy/contents/item/spellstones/CreationHeart.java), [VoidPearl.java](https://github.com/Auviotre/Enigmatic-Legacy-Plus/blob/main/src/main/java/auviotre/enigmatic/legacy/contents/item/spellstones/VoidPearl.java), [DesolationRing.java](https://github.com/Auviotre/Enigmatic-Legacy-Plus/blob/main/src/main/java/auviotre/enigmatic/legacy/contents/item/rings/DesolationRing.java), [MixinLivingEntity.java](https://github.com/Auviotre/Enigmatic-Legacy-Plus/blob/main/src/main/java/auviotre/enigmatic/legacy/mixin/MixinLivingEntity.java)

### Evidence of Tombstone–Enigmatic overlap

[Corail Tombstone issue #298](https://github.com/Corail31/tombstone_lite/issues/298) is a **Minecraft 1.20.1 Forge** report, not our 1.21.1 pair. User reported Corail stored items while Enigmatic Ring's Soul Crystal still spawned and its inventory Vessel did not. Corail's maintainer explained this was expected because Tombstone handled the drops and declined adding dedicated XP integration. This is evidence of **architecture overlap and a possible coexistence model**, **not proof** it works on the current versions.

Corail official 1.21.1 feature list notes compatibility upgrades in v9.3.1 with **Dragon Survival auto-equip**, and v9.3.3 with **Ars Nouveau/Occultism familiar handling**. These are positive signs, not a guarantee that a current combined pack supports all Curios and death rituals. Source: https://www.curseforge.com/minecraft/mc-mods/corail-tombstone/files/6708353

## Required successful true-death transaction (locked outcomes, runtime ordering TBD)

1. **Fatality confirmed** after vanilla and modded totems, species interception and other death avoidance. Guarantee the event is not cancelled; only now begin post-death side effects. No late death saver is permitted to fire *after* inventory is committed.
2. **Capture identity and exact death location** (dimension, coordinates, cause, source-player UUID), with an idempotent `death_event_id` if a custom bridge is needed. Do not create duplicate records on player clone/respawn, dimension change, or multiple death-event listeners.
3. **Resolve explicit always-kept/soulbound items and Curios once**. Everything else, including unusual armor/Curios, is routed to **Corail Tombstone inventory grave**. The grave is at actual death position (or Corail's documented safe substitute location if the block is invalid); do not have a second mod teleport the dropped items.
4. **XP handled once by Corail**, using its configurable loss/recovery policy. Enigmatic Storage Crystal XP storage is off; preserve relevant item-specific `CursedXpScroll` stored-XP penalty separately.
5. **Death consequences run once** only after confirmed death: curse Ring Soul Crystal loss/physical separate soul recovery, optional other magic soul effects, normal death statistics. Non-inventory cursed soul shards remain separate recoverables if tested. No reward for a kill that was prevented by a death saver unless that mechanic specifically requires a near-death trigger.
6. **Respawn** via vanilla/Corail logic or documented species-specific true-death location; recreate and sync equivalent vampire/wolf flags, the four Abomination progression tracks, both lich histories for Archlich, dragon growth, and Hunter protection appropriately. Avoid double-write with `PlayerEvent.Clone` and native persistent attachment restoration.
7. **Recovery**: the player retrieves their grave; the curse soul crystal remains its own meaningful object. Verify the retrieved inventory contents/XP with before-and-after snapshot, not simply that a grave block appeared.

### Remaining implementation choices (not reopening locked ownership)

- Does a **failed Arca/phylactery revival** lead to vanilla *real* death, or some mod-specific alternate outcome? Probe exact Goety port and JAWR code; if actual death is committed, Corail handles gear normally.
- Should any *successful* lich anchor return count as a true death for quests/scoreboards? **Recommend no if the engine death is canceled**, and keep one consistent definition.
- Should Enigmatic's **Escape Scroll** retain an adapted effect to redirect the **grave location/key** rather than physical items, or simply be a nonfunctional death utility while Corail is present? **Requires explicit gameplay decision**.
- Corail `keepInventory`, gamerule `keepInventory`, soulbound enchantments, other inventory curios, dimensional spaces and XP settings require a matrix. Never rely on a stale consumer version's config names.
- Corail Tombstone's grave system may use custom handling not visible in public source. Inspect the actual pinned 9.5.6 jar and API where available; avoid inferring the order of inventory handlers from mod names.

### Acceptance tests

| Case | Expected |
| --- | --- |
| True death, no curios | Exactly one grave, correct inventory and XP |
| True death, Enigmatic Eldritch Amulet | Single grave, no extra restored inventory after respawn |
| True death, Ascension Amulet/Storage Crystal | Single grave, no separate item/XP Vessel |
| True death, Escape Scroll | No missing/double items; behavior explicit (blocked or adapted) |
| True death, Cursed Ring | One Corail grave and **one** recoverable Soul Crystal; exactly one soul-loss increment |
| True death, Cursed XP Scroll | Stored XP penalty once; other XP handled by Corail |
| Cancelled fatality by 2–3 charms | Zero graves, zero cursed soul penalties, one or zero spent savers depending on active precedence |
| Successful Arca/phylactery rescue | No extra grave if actual death cancelled; correct anchor and gear |
| Failed lich rescue (actual death) | One grave, normal drops/respawn, persistent progression correct |
| `keepInventory=true` plus Soulbound Curios | No duplication, obey chosen policy |
| Death in void/lava/other dimension | Safe grave fallback, correct death location/key and recovery |
| Two deaths before recovering first | Separate graves; no overwritten inventory or loss of soul shards |

**Decision locked; no config was applied, and no game-world or integration test was run in this audit.** See [DEATH-COMPAT-TEST-PLAN.md](DEATH-COMPAT-TEST-PLAN.md) for the test-case register.
