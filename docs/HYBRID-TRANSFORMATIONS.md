# Hybrid supernatural physiology — Abomination, Dracolich, dual Lich (2026-11-13)

**Decision levels:** Vampire's Delight has been approved separately and registered as a planned beta. **Dracolich is a user-required future playable outcome**, subject to implementing and testing a safe combination. **Abomination and dual-lich mechanics are research/design proposals**, not user-confirmed implementations. Dragon Survival remains deferred pending compatibility bridge. No new code, mods, or acceptance tests were completed by this design entry.

Target: Minecraft **1.21.1 NeoForge**. Do not confuse current 1.21.x Vampirism/Werewolves APIs with the *later, incompatible* Vampirism 2.0/FactionApi announced for Minecraft 26.1.2. Our pack targets 1.21.1 and must use its embedded faction system.

## 1. Technical findings

- Vampirism 1.21.x uses **one active faction** in `FactionPlayerHandler.currentFaction`. It exposes **`PlayerFactionEvent.FactionLevelChangePre`** (cancellable), **`CanJoinFaction`**, and **`FactionRegistry.createPlayableFaction`**, providing an extension route to a new custom faction. Existing vampire and werewolf abilities will **not** automatically work for a custom faction.
- The 1.21.x implementation of Vampirism `Helper.isVampire` checks equality with `VReference.VAMPIRE_FACTION`. Werewolves `Helper.isWerewolf` checks equality with `WReference.WEREWOLF_FACTION`. `WerewolfPlayer.get(entity)` also rejects players failing that check; Werewolf skill handler is bound to the original Werewolf faction. Therefore *simply creating an Abomination faction* gives neither complete vampire blood rules nor complete wolf abilities without adapters or targeted patches. Forcibly granting **both** vanilla factions would contradict the one-current-faction design.
- Werewolves updates its own hunger stats, wolf-specific meat checks, player models, transformations, inventories and skills. Vampirism's `VampirePlayer` owns blood statistics, feeding, level modifiers and skill actions. Both have independent serialized attachments even though one faction is active.
- JAWR has separate vampirism and lycanthropy; it is **not** the same subsystem as Vampirism/Werewolves. A JAWR-vampire/JAWR-wolf Abomination is a **different, as-yet-unanalyzed integration**, not a free extension of the TeamLapen hybrid.
- Goety lichdom normally returns a dying player to their **Arca** and applies its own soul-energy/cost logic, with no ordinary hunger bar and optional lich form; it cannot ordinarily be cured without commands. The original Goety 1.20 death handler **cancels the death event** after a rescue; the 1.21.1 NeoForge port's exact handler and priority need inspection/reproduction.
- JAWR Forbidden Magic independently offers ten levels of lich progression and a **phylactery**. Its 1.21.1 code is not evidenced to share a death/revival arbiter with Goety. JAWR 0.5.17 changelog adds a *simple Lich+Vampire handler*, which **does not** establish Goety+JAWR dual-lich compatibility.

Primary source references:
- https://github.com/TeamLapen/Vampirism/blob/version/1.21/latest/src/api/java/de/teamlapen/vampirism/api/event/PlayerFactionEvent.java
- https://github.com/TeamLapen/Vampirism/blob/version/1.21/latest/src/main/java/de/teamlapen/vampirism/entity/factions/FactionPlayerHandler.java
- https://github.com/TeamLapen/Vampirism/blob/version/1.21/latest/src/main/java/de/teamlapen/vampirism/entity/factions/FactionRegistry.java
- https://github.com/TeamLapen/Vampirism/blob/version/1.21/latest/src/main/java/de/teamlapen/vampirism/util/Helper.java
- https://github.com/TeamLapen/Werewolves/blob/1.21/src/main/java/de/teamlapen/werewolves/util/Helper.java
- https://github.com/TeamLapen/Werewolves/blob/1.21/src/main/java/de/teamlapen/werewolves/entities/player/werewolf/WerewolfPlayer.java
- https://github.com/TeamLapen/Werewolves/blob/1.21/src/main/java/de/teamlapen/werewolves/WerewolvesMod.java
- https://goety.wiki.gg/wiki/Lichdom
- https://github.com/Polarice3/Goety-2/blob/1.20/src/main/java/com/Polarice3/Goety/common/events/SoulEnergyEvents.java (older 1.20 logic, **not verified as identical** in 1.21.1)
- https://modrinth.com/mod/jawr-forbidden-magic
- https://www.curseforge.com/minecraft/mc-mods/just-another-witchery-remake/files/9166145

## 2. Abomination: deliberate vampire/werewolf hybrid

**Research recommendation:** One *new hybrid physiology* with **one authoritative Vampirism faction**, rather than forcing two incompatible factions active. Core magic schools remain unaffected.

Proposed implementation:
1. Prototype a standalone NeoForge mod depending on the exact pinned Vampirism 1.21.x and Werewolves 1.21.x jars. Register a playable `tmm:abomination` faction (registry name *provisional*) with its own player attachment, serialization, HUD and transition ritual. Restrict upgrade to an existing vampire or werewolf after a late-game ritual; preserve history, migrate skill points deliberately and maintain cure rollback.
2. Integrate blood feeding via Vampirism's `VampirePlayer` data/API or a targeted adapter; wolf body/form toggles via Werewolves' form data/API. Since vanilla helper methods check exact original factions, patch or adapt dependent calls for approved Abomination behaviors (skills, actions, bites, daylight, model, equipment, food). **Do not blindly set two faction flags** or overwrite serialized data on each tick.
3. Use a **single survival resource authority**: vampire blood is required for normal ongoing sustenance. Wolf-form hunting, special raw meals and Vampire's Delight recipes can potentially replenish blood through a documented translation; ordinary hunger is not a second lethal meter. Distinctive downsides: sunlight, silver, wolfsbane, garlic, loss of some ordinary food utility.
4. Give the hybrid a **single integrated skill allocation/limit**, not the sum of every vampire and werewolf maximum. Retain signature abilities (blood feeding, moon-driven wolf form, claws/strength, selected vampire powers); special hybrid milestones rather than unrestricted stacking.
5. Wolf-form rendering and equipment are owned by Werewolves when in beast form; humanoid vampire form uses its normal player renderer. Test daylight effects during shapeshift, transformations while dead, respawn and dimensions.
6. Add a single reversible or explicitly irreversible transformation/cure policy. Document whether the player retains earlier unlocked skills and lord progression, and whether cross-faction villagers treat the hybrid as an enemy.

**Effort class:** *medium-to-large custom mod*, not a minor KubeJS recipe. A prototype checking pre/post faction transitions and showing a new faction is reasonably approachable; completing power inheritance, form rendering, skill trees, food, death and network sync is the expensive part. Upstream 1.21.1 changes can require patches. Start with a vertical slice: vampire -> Abomination -> beast form -> blood feeding -> death/respawn -> persistence, before designing the full skill tree.

Potential simpler variant: retain the *Vampirism vampire faction* as the authoritative identity and grant a **small custom lycanthrope form** with bespoke powers, rather than reuse all of Werewolves' APIs. This sacrifices some feature reuse but may be significantly more stable. Evaluate both approaches in a small test mod before committing.

## 3. Dracolich — required hybrid gameplay outcome

User explicitly wants playable **Dracoliches**. Treat this as a design requirement, not permission to stack underlying flags unmanaged.

- Required prerequisite: Dragon Survival must first be proven safe enough to install, then integrate it via our own NeoForge compatibility bridge. Its three dragon species (Cave/Forest/Sea) remain relevant.
- Offer two **ritual branches** for dracolich creation, if feasible:
  - Dragon + **JAWR Forbidden Magic lich**: body, flight, growth from Dragon Survival; phylactery/lich levels and soul system from JAWR.
  - Dragon + **Goety lich**: body from Dragon Survival; Arca, soul energy, undead immunities, and resurrection from Goety.
- Each player gets **one effective undead metabolism and one resurrection arbiter**. Lich rules govern whether hunger is necessary; dragon diet may remain as optional buffs or be managed per player. **The server-global Dragon Survival disable-food config cannot selectively turn off dragon diet for dracoliches while retaining it for normal dragons.** Implement per-player handling if we want both.
- Visual direction: species-specific dracolich anatomy, undead skeletal/necromantic features and one authoritative body scale/model system. Do not assume the two mods' renders compose automatically.
- Gate transformation by having a dragon complete the chosen lich pathway through a **special dracolich ritual**; block ordinary lich potion/ritual on dragons unless transformed through supported composite procedure.
- Keep the two lich pathways mechanically distinct until user chooses otherwise; dragon+both lich systems is **not automatically authorized**.

**Effort class:** high if proper custom undead dragon visuals/food/death integration are required. A less ambitious functional first release can preserve normal Dragon Survival appearance with a lich aura and one chosen resurrection backend.

## 4. Can Goety and JAWR lichdom coexist?

**Possibly at the data/state level; not safely by default at the player-survival level.** They are different independent player state systems, so both flags could theoretically exist. This does **not** imply death/respawn, immunities, diet, appearance or progression will cooperate.

Specific consequences/risk areas:
- **Competing resurrection anchors**: Goety Arca attempts to rescue and teleport a dying lich; JAWR has phylactery-based lich progression. If both respond to a fatal hit, event order may determine destination, charged resource, side effects and whether the other system runs. If one cancels the event, the other might never run; if both act, conflicting teleports/status persistence may occur. Exact 1.21.1 behavior is **unverified**, not a claimed observed bug.
- **Redundant immortality**: if both independent anchors work, a player may get two effectively independent extra lives, possibly without sacrificing either's full utility. This is major balance power; it could be desirable as a very late-game *Archlich* feature, but should be an explicit unlock rather than an accidental loophole.
- **Overlapping food/health/effect rules**: Goety disables ordinary hunger; JAWR may modify undead effects and survival. Whichever system applies later could control the HUD or healing. Avoid starvation/phantom hunger, double damage immunity or permanent invulnerability.
- **Two progression trees**: each could reasonably remain learnable, but merely possessing both full lich statuses might grant stacked immunities and no associated costs. Preserve *spell/ritual mastery*, separate it from *physical deathless state*.
- **Cures/loss of anchor**: removing one lich status could leave the other intact; death/respawn, login and commands must reconcile and never leave stale invulnerability or broken anchor references.

**Recommendation:** Both lich *magical progressions* may be learned while the player uses **one active lich physiology/primary resurrection**. If a dual-lich apex state is desired, implement a custom **Archlich / Twin-Anchored Lich** state:
- one configurable/deterministic **primary rescue arbiter** and primary anchor;
- one secondary anchor with explicitly priced, limited **failover**, triggered only when the primary is destroyed/unavailable;
- never two independent unbounded death cancellation paths on the same fatal event;
- explicit cost, cooldown, recovery, severing and irreversible-loss rules. Endgame balance audit required.
**This is a concept pending the user's choice**, not a declaration that dual lich is approved.

## 5. Hybrid matrix addendum — non-transitive exceptions

Previous matrix in [FOOD-AND-SPECIES-DESIGN.md](FOOD-AND-SPECIES-DESIGN.md) used one active physiology by default and listed 3 possible exceptions. Extend the **research candidates** as follows:

| Pair | Intent | Status |
| --- | --- | --- |
| Dragon + JAWR Lich | Dracolich based on phylactery | **Required playable design goal; not yet implemented** |
| Dragon + Goety Lich | Dracolich based on Arca | **Required playable design goal; not yet implemented** |
| Vampirism Vampire + Werewolves Werewolf | Abomination | **Engineering proposal / pending player design approval** |
| JAWR Vampire + JAWR Lich | Recognized by upstream partial handler | Research-only existing mod behavior; no formal pack compatibility yet |
| JAWR Lich + Goety Lich | Twin-anchored Archlich | **Investigate, not yet approved** |

That is **5 exception candidates out of 21 unordered provider-pairs**, leaving 16 default-blocked pairs for this initial proposal. The two dracolich paths are *required goal concepts*; exact mechanics remain unapproved. All other pairings are blocked unless explicitly designed later.

**Nontransitive rule:** Supporting `Dragon + Goety Lich` and `Goety Lich + JAWR Lich` does **not** permit `Dragon + Goety + JAWR Lich` automatically. Likewise, Abomination is not vampire+wolf+lich, and different vampire/lycan providers are not interchangeable. Every 3+-way hybrid needs its own named design and complete validation.

## 6. Suggested validation gates

1. Pin exact 1.21.1 NeoForge versions and launch baseline Vampirism + Werewolves + Vampire's Delight with Farmer's Delight and AppleSkin; log interfaces/events.
2. Prototype new faction and *cancellable* transition without any other modded transformations. Prove no data loss during vampire->hybrid or werewolf->hybrid; verify save/rejoin.
3. Prototype feeding, sun, wolfsbane, lunar phases, render and at least one compatible signature power. Verify normal and transformed eating and blood effects on both client and server.
4. For each dracolich branch, test dragon-only, lich-only, and both with finite death/respawn scenarios and custom bridge. Verify exactly one resolver fires and death consequences are predictable.
5. For dual lich, if explored, reproduce 1.21.1 fatal events with both progression flags using a disposable world to see what actually happens. Capture behavior before choosing event priorities/failover; validate inventory, teleport, death statistics, cure/removal, dimension changes and logout.
6. Keep default exclusive-physiology policy in force except for *named, tested composite states*. All hybrid mods remain **uninstalled and undesigned at the implementation level** until their particular scope has been accepted.

## 7. Open decisions

- **Abomination:** preserve the full Werewolves shapeshifting system (higher integration cost) or grant a smaller custom cursed form atop the Vampirism vampire faction (potentially simpler)?
- **Nutrition:** blood as the only required sustenance, with meat/culinary recipes as an alternate way to obtain blood or a nonrequired buff? If both hunger and blood exist, avoid duplicate starvation loops.
- **Dracolich:** offer both independent Goety- and JAWR-based variants as planned, or choose one initial supported variant and add the second after testing?
- **Twin-lich:** allow it as a named endgame Archlich with an explicitly limited backup anchor, or require that each player choose exactly one active immortality source?

No changes to the installed modpack or permanent user decisions beyond the **Dracolich required-goal** statement.
