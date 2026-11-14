# Feywild 5.6.4 — initial skill, summoning and companion audit

**Date:** 2026-11-14, Phoenix local. **Pack pin:** `feywild-1.21.1-5.6.4`, NeoForge 1.21.1. **Source anchor:** [SaphieNyako/Feywild `project/neo/1.21` at `2a339f9`](https://github.com/SaphieNyako/Feywild/tree/2a339f98a5a2ea0d5620801538095a7b013b6de2), dated **2026-10-25**, same day as [published 5.6.4 release](https://www.curseforge.com/minecraft/mc-mods/feywild/files/9321431); `gradle.properties` has `mod_version=5.6.4`. **Initial code-source audit, not pinned-JAR decompilation or multiplayer in-world test**. No code, recipes, mod pins, configs, or skills were altered.

## Product model and beginner experience

**Not conventional player spellcasting.** Feywild Reboot 5.x is a magical companion, adventure, seasonal courts and NPC story/quest system. The mod's player mainly **obtains summoning scrolls, summons owned fey, controls their own autonomous abilities, battles/visits seasonal bosses and pursues four seasonal questlines**. It also has a Fey Altar recipe station, Curios wing tiaras and fey-world vegetation.

The [author's 5.x getting-started guide](https://github.com/SaphieNyako/Feywild/wiki/How-to-get-started!) explains:
- Get fey dust from feeding wild pixies **cookies**, mining Fey Gem Ore and smelting the gems, or defeating pixies. Craft **Feywild Lexicon** from a book and Fey Dust.
- Make Mandrake root (Fey Dust + poisoned potato), grow and harvest Mandrake; use with ink sac/bottle to obtain **Fey Ink**; ink + paper + feather creates a blank **Summoning Scroll**.
- Craft a Fey Altar using Fey Gem and smooth blocks, prepare the seasonal ingredients, then craft a specific filled summoning scroll.
- Use a scroll to instantiate and **assign owner** to a Fey. **Pixie Orb** opens the owner's fey interaction menu (toggle ability, follow/stay, dismiss back to scroll).
- Feywild's four seasonal pixies have **native Quest Giver** questlines; an FTB Quests chapter should *introduce and guide through* native quests rather than clone rewards/completion conditions. Pack manifest already enables `quest-giver-1.21.1-1.5.3`, but marks its dependency verification **unverified**. Confirm actual Quest Giver 1.5.3 loading in client and dedicated server.
- Compatibility: Patchouli and Quest Giver; optional Curios 5.6.0+ and Supplementaries 5.6.3+; do **not** add optional mods without approval.

## Verified source behavior and personal effects

| Fey system | Verified mechanics | Skill integration impact |
|---|---|---|
| [`SummoningScrollItem`](https://github.com/SaphieNyako/Feywild/blob/2a339f98a5a2ea0d5620801538095a7b013b6de2/src/main/java/com/saphienyako/feywild/item/SummoningScrollItem.java) | On block use instantiates associated FeyBase, stores summon position and calls `IOwnable.setOwner(player)`; **consumes one scroll** unless creative | Summon strength not tied to player's native magic power. Item does store owner identity on **entity**, not creator item quality |
| [`FeyBase`](https://github.com/SaphieNyako/Feywild/blob/2a339f98a5a2ea0d5620801538095a7b013b6de2/src/main/java/com/saphienyako/feywild/entity/base/FeyBase.java) | Extends `PathfinderMob`, implements **custom `IOwnable`** (UUID), `ISummonable`; owner/follow/toggle/voice/anchor persisted in **entity NBT**. Does **NOT** subclass vanilla `TamableAnimal`; owner resolution uses current level `getPlayerByUUID`. | Generic Pufferfish **pet** ownership detection may miss Fey even though ordinary entity attributes exist; do not claim it works until tested. Shared owner adapter used for Occultism-like special familiars is potential work |
| **Spring Pixie** / `BreedAbilityGoal` | With owner's activation and online owner, breeds nearby suitable adults. | Utility farm ability, not damage-based; simple pet combat stat buffs won't improve breeding throughput |
| **Summer Pixie** / `CropGrowAbilityGoal` | Grows nearby `CropBlock` by applying the standard crop growth method in its AI goal. | Plant-growing world mechanic; no player caster attribute. Check Pam's/Delight crops if they inherit `CropBlock` |
| **Autumn Pixie** / `GatherCropsAbilityGoal` | Checks mature `CropBlock`, resets plant to age 0 and drops a cloned crop item (special-case wheat/beetroot); no general Farmer's Delight harvest API implied. | Specialized farm harvest; test crop yields, replanting and storage collection; not evidence of broad modded crop support |
| **Winter Pixie** / `GatherMobItemsGoal` | Selects nearby non-baby, untamed `Animal`; reads **animal's death loot table and spawns its drops** despite animal still being alive, then applies attack. This can generate renewable loot by harvesting repeatedly surviving animals. No player kill attribution/looting was seen in `dropFromLootTable`; no arbitrary mob drops (target filter `Animal`). | Important **magical automation** / drops economy. **Do not automatically nerf**. Test standard/magic animals, timing, loot tables and death, mob health sustainability, missing loot context and mixed ownership |
| **Tree Ents / Bee Knight** | Rideable/combat servants with special effects; TreeEntBase default max health **120**, source config default **12 attack**, companion protection effects can push mobs/projectiles away. | Vanilla `MAX_HEALTH`/`ATTACK_DAMAGE` attributes could be raised on entity; reliable owner-to-skill adapter needs runtime check. See [TreeEntBase](https://github.com/SaphieNyako/Feywild/blob/2a339f98a5a2ea0d5620801538095a7b013b6de2/src/main/java/com/saphienyako/feywild/entity/base/TreeEntBase.java) |
| **Fey Wing Tiaras** | Curios/offhand equipment with player flying magic; `FeyFlyingEffect` controls ability. | Player effect, but not independent mage stat/progression; no standalone Feywild skill bar required |
| **Fey Altar** | A physical **5-input, 1-output** recipe station (`FeyAltarBlockEntity`) with a default 40-tick cycle; output crafted without recording any crafter UUID, normal item component inheritance not guaranteed (explicitly creates a new ItemStack). | A **player-specific crafting quality skill** would require attribution and item imprint; no justification until a meaningful effect is selected |

**Offline-owner limitation:** [`CropGrowAbilityGoal.canUse`](https://github.com/SaphieNyako/Feywild/blob/2a339f98a5a2ea0d5620801538095a7b013b6de2/src/main/java/com/saphienyako/feywild/entity/goals/pixie_goals/CropGrowAbilityGoal.java) and other pixie-goal implementations require `getOwningPlayer() instanceof ServerPlayer` and `getAbilityActive()`. **Force-loading a chunk alone will not run these pixie farm abilities while owner is offline.** Test in integrated multiplayer world.

## Skill decision boundary

**Matrix status: Compatible with work (source-backed ownership, runtime untested)**. The custom `IOwnable` interface and persistent UUID offer a feasible **single shared pet-owner adapter**, especially if that adapter is already needed for Occultism familiars and Goety summons. But the player **has not approved** a Feywild-specific skill tree or a new bridge. Existing universal pet buffs should only apply if they recognize these `PathfinderMob` entities and their non-vanilla owner interface. Their strongest features (pixie farm goals, tree-ent shields and mounts) will **not** improve from generic damage/health boosts, so don't promise a full enchanted-companion progression without focused work.

Source has **no new registered player personal mana**, no spell-power cost API and no natural player-cast attack spells. Exempting Feywild from unique Pufferfish progression may be reasonable later, but avoid prematurely locking a skill decision before the user weighs the universal pet adapter value.

## Important security and gameplay checks in a multiplayer pack

- **Owner permission on UI packets:** [`ToggleAbilityMessage.handle`](https://github.com/SaphieNyako/Feywild/blob/2a339f98a5a2ea0d5620801538095a7b013b6de2/src/main/java/com/saphienyako/feywild/network/ToggleAbilityMessage.java) and [`DismissEntityMessage.handle`](https://github.com/SaphieNyako/Feywild/blob/2a339f98a5a2ea0d5620801538095a7b013b6de2/src/main/java/com/saphienyako/feywild/network/DismissEntityMessage.java) resolve an arbitrary entity by networked ID and mutate it. The inspected handlers **do not check `entity.getOwner().equals(player.getUUID())`** before changing ability or dismissing it (the normal Pixie Orb UI does check ownership before opening). **Potential crafted-packet multiplayer permission issue; test with Client A and Client B and verify server validation.** Don't claim an exploit proven from a code read alone; if reproduced, report upstream/bridge a server-side ownership check. Also inspect follow and voice packets.
- **Owner online:** test force-loaded chunks with owner logged out; expected farm AI dormant.
- **Drops economy:** repeatedly harvest animal loot with Winter Pixie; test modded livestock and death loot (entity is attacked after drops spawned), see whether farm can yield unbounded items; user generally supports powerful magic automation. Avoid arbitrary bans.
- **Quest Giver vs FTB Quests:** show tutorial path, then let native Pixie NPCs run their established branch quests. Don't inadvertently pay duplicate endgame quest rewards for the same completion; ensure FTB quest completion remains independently earned, respecting user’s XP+item reward policy.
- **Owned creature buffs:** summon a pixie and Ent, test Pufferfish pet bonuses before/after summon, item ownership vs UUID owner, logout/login, dimension transitions, resummoning via scroll, performance at scale.
- **Ritual world/chunk and combat:** test wing flight, Ent mount and shield, targeting and raids; test winter loot and boss loot handling. Do not conflate seasonal Feywild "bosses" with Cataclysm endgame progression gates.
- **Dependencies:** verify exact `quest-giver` pin and Patchouli + Curios compatibility in B6; main manifest currently lists Quest Giver as `release_status: unverified`.

## No implementation

This is an **initial source assessment**, not a runtime compatibility proof. No Feywild skills, tags, KubeJS scripts, pet adapters, datapacks, server security fixes, recipes or configuration were changed.

## Feywild skill integration decision (2026-11-14)
**User agreed** to test **existing universal Pufferfish pet bonuses first**, then evaluate a **shared** owner adapter (possibly usable for Occultism and other custom owner interfaces) **only if existing ownership detection fails**. No automatic dedicated Feywild skills, custom AI patch or bridge authorized. Preserve the four pixie farm abilities and native questlines; the initial packet-permission and Quest Giver tests remain separately necessary.
