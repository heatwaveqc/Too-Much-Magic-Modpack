# Persistent player transformation compatibility — design proposal (2026-11-13)

**Status: proposed architecture / research and acceptance gates, not an implemented feature or a decision to include Dragon Survival.** The current installed B3 pack does not include Dragon Survival, Vampirism, Werewolves, Goety or the other new candidate systems, except that **JAWR and JAWR: Forbidden Magic are already installed**. Do not claim this document makes transformations exclusive.

## The failure mode

Several independently authored mods alter the player's persistent biology, hunger/food, blood, body model, movement, immunities, death/respawn and cure mechanics. A player might be a dragon, vampire and lich simultaneously. **Stacked statuses can leave no sane method of feeding or undoing a transformation**, even when there is no startup crash.

## Identified persistent transformation providers

| Provider | Primary persistent transformation | Relevant food/authority issue |
| --- | --- | --- |
| Dragon Survival | Dragon (multiple species) | Diet restrictions and food property/consumption hooks; custom diet can be disabled |
| Vampirism | Vampire | Replaces food with blood consumption; hunger behaves differently |
| Werewolves (Vampirism addon) | Werewolf faction | Uses Vampirism's faction infrastructure |
| JAWR / Just Another Witchery Remake | Vampire; lycanthrope/werewolf | Independent vampire blood and food rules, not the same Vampirism faction |
| JAWR: Forbidden Magic | Lich (10-level path and phylactery) | Separate lich progression, death, soul/phylactery handling |
| Goety — The Dark Arts | Lich | Separate Arca/soul-energy system; no ordinary hunger bar |

**Other approved mods must be screened before installation for further permanent player states.** Temporary spells, removable short-duration shapechanging, and normal magical skill trees do not automatically count as primary physiology; evaluate based on actual sustained hunger, health, death, attribute, inventory or rendering rewrites.

Sources:
- Dragon Survival 1.21.1 config and food code: https://github.com/DragonSurvivalTeam/DragonSurvival/blob/1.21.1/src/main/java/by/dragonsurvivalteam/dragonsurvival/common/handlers/DragonFoodHandler.java and https://github.com/DragonSurvivalTeam/DragonSurvival/blob/1.21.1/src/main/java/by/dragonsurvivalteam/dragonsurvival/config/ServerConfig.java
- Vampirism blood rules: https://wiki.vampirism.dev/docs/wiki/factions/vampire/vampire-player
- JAWR 1.21.1 vampire/lycan fixes: https://www.curseforge.com/minecraft/mc-mods/just-another-witchery-remake/files/9089145
- JAWR 1.21.1 0.5.17 adds *some* lich+vampire handling, but does **not** establish universal safe stacking: https://www.curseforge.com/minecraft/mc-mods/just-another-witchery-remake/files/9166145
- JAWR Forbidden Magic: https://modrinth.com/mod/jawr-forbidden-magic
- Goety lich feeding/irreversibility: https://goety.wiki.gg/wiki/Lichdom

## Proposed policy — default exclusive primary physiology, explicit exceptions only

1. Each player has **zero or one primary persistent physiology**. Starting state is human/untransformed. Identifiers are by **provider and state**, e.g., `dragonsurvival:dragon`, `vampirism:vampire`, `werewolves:werewolf`, `witchery:vampire`, `witchery:lycanthrope`, `forbiddenmagic:lich` (placeholder identifiers; verify actual registry IDs), `goety:lich`.
2. **No combination allowed by default**, even two similarly named vampire or lich variants from separate mods. Existing magical abilities, crafting, rituals and items remain available to all unless those explicitly require a body transformation.
3. Acquiring a transformation requires checking all **actual mod state providers**. An in-pack owner registry is useful for diagnostics but cannot be the sole source of truth. Allow upgrades to the already-owned physiology. Block *new incompatible* state before it applies, not merely remove it on a later game tick.
4. A player may change physiology only through a **verified cure/reversal/transition** that leaves no stale diet, blood, flight, model, death, phylactery, soul-energy or faction state. Goety's standard lichdom cannot be reversed normally, so changing away from it should require explicit controlled administrative or custom ritual support, not silently stripping flags.
5. Exception pairs (e.g. **dragon+lich**, **witchery vampire+witchery lich**) may be **explicitly whitelisted later**. Each exception must define *exactly one owner* for hunger/blood/food, and test sunlight, immunities, equipment, flight, rendering, death/respawn and cure. JAWR's basic lich+vamp handler alone is not such a proof.
6. Keep hunter *profession/faction* distinct from vampire *physiology* in the policy. The existing bridge below blocks any Vampirism faction from dragon altars, potentially including human hunters: decide whether this conservatism is acceptable or whether to narrow it.
7. No automatic de-transformation, silent data wipe or ticking cure. For preexisting illegal combinations: flag for admin review, back up data and offer a controlled resolution.

### Mechanism: a small NeoForge cross-mod bridge

- **Existing narrowly scoped helper:** [Dragons and Vampires 1.1](https://modrinth.com/mod/dragons-and-vampires/version/1.1) (NeoForge 1.21.1) mutually blocks Dragon Survival altar transformations and entering Vampirism factions (including Werewolves). This is a *candidate*, not an approved installed mod; inspect its exact hooks and test both transition directions, hunters, infection and initial dragon selection.
- **Our required extension:** A server-authoritative, small compatibility addon that detects player state in Dragon Survival, Vampirism/Werewolves, JAWR, Forbidden Magic and Goety; blocks the actual transition routes at pre-event or targeted interaction/ritual/potion hooks; and records diagnostics. **The helper does not cover JAWR or Goety lichdom.**
- KubeJS can define integration recipes, prevent unsupported acquisitions and offer messaging, but **recipe denial by itself is insufficient**: other entry paths include infection, bites, rituals, commands, potion consumption, world creation and death/respawn. Use mod-specific NeoForge hooks/integration for transitions that KubeJS cannot reliably intercept.
- When upstream does not expose a cancellable transformation event, add a narrowly scoped compatibility hook or disable that acquisition route until protected. Prefer not to mutate players reactively each tick.

### Conservative Dragon Survival configuration while testing

Official 1.21.1 source includes these server-side options (verify config file format/actual generated paths before writing config):
- `start_with_dragon_choice=false` — remove the initial join-time choice; ensures the player cannot bypass progression/exclusivity at first login.
- `allow_dragon_choice_from_inventory=false` — remove alternative inventory-based dragon choice while bridge is being built.
- `no_humans_allowed=false` — humans remain valid.
- `disable_dragon_food_handling=true` — optional temporary simplification while checking compatibility. This disables the dragon-specific food modifier; its long-term gameplay tradeoffs should be assessed rather than assumed optimal.
- Do not claim `transform_altar=false` disables already existing altars: the documented setting controls *creation* of dragon altars, not necessarily using existing ones.

Source: https://github.com/DragonSurvivalTeam/DragonSurvival/blob/1.21.1/src/main/java/by/dragonsurvivalteam/dragonsurvival/config/ServerConfig.java and https://github.com/DragonSurvivalTeam/DragonSurvival/blob/1.21.1/src/main/java/by/dragonsurvivalteam/dragonsurvival/common/handlers/DragonFoodHandler.java

## Validation / release gate

1. Enumerate actual API identifiers and persisted player-state structures for all six mods. Test regular and operator entry, temporary states versus permanent transformation, join-time dragon selection, cure/reversal, respawn and world reload.
2. For every pair of primary states test both orders (**A -> B** and **B -> A**), including same-mod Vampirism vampire vs Werewolves faction and JAWR independent vampire/lycan. Both must reject the second transition without partial application, lost inventory, lost skill points or stale metadata.
3. Verify that permitted states have a real functional sustenance path. Test normal foods, modded foods, blood, food overlays (AppleSkin), exhaustion, starvation, thirst/energy, recovery, and cure after login/death.
4. Stress unusual resurrection: Goety Arca death, JAWR phylactery, Dragon Survival soul/altar, Vampirism faction changes. Verify exclusive state persists properly across logout, death, dimension travel and teleport.
5. If any exception combination is proposed, test its full **hunger, health, model, flight, abilities, sun, inventory, cure and respawn** matrix; document one owner per conflicting subsystem before allowing it.
6. If automated denial is not implementable for every route, **do not ship Dragon Survival as enabled**, and temporarily close any unprotected transformation routes among already included systems. Add a gate to the modpack's gameplay integration checklist.
7. Record exact runtime test logs and workarounds. A dedicated-server boot is not a transformation-compatibility test.

## Outstanding user decisions

- Adopt *exclusive primary persistent physiology by default* as the formal rule?
- Treat Vampirism's **hunter faction** as compatible with dragon physiology or disallow all factions as the existing helper does?
- Allow any **explicit exceptions** (such as dragon+lich / vampire+lich) after empirical testing, or keep exclusive permanently?
- Retain or disable Dragon Survival's unique diet once there is reliable exclusivity?

**No changes to the installed pack, no decision to include Dragon Survival and no working bridge are implied by this document.**

## Food suite integration and state-combination tree (2026-11-13)
- The user approved the complete Pam's HarvestCraft 2 family, Farmer's Delight, Aquaculture 2, plus **Harvest with Ease** and its Cobweb prerequisite to ensure right-click gentle harvest (Pam's built-in support was removed).
- [FOOD-AND-SPECIES-DESIGN.md](FOOD-AND-SPECIES-DESIGN.md) provides the **provisional 7-provider-state / 21-pair matrix**, grouped combination tree and suggested gameplay identities for human, dragons, independent vampire/lycan branches and independent lich paths.
- Extra kitchen integrations ([Aquaculture Delight](https://modrinth.com/mod/aquaculture-delight), [Pantrywork](https://modrinth.com/mod/pantrywork), [Vampire's Delight](https://modrinth.com/mod/vampires-delight)) are **discovered research candidates** but not automatically approved. Vampire's Delight 1.21.1 is beta-tagged; do not bypass the modpack's beta-selection gate.
- **Diet/food compatibility is a user-experience requirement, not just a crash test.** Each enabled physiology needs sustainable feeding. Whether vampires and liches should be able to enjoy prepared meals without an ordinary hunger bar is an open design decision. Do not convert a player to a conflicting state while this remains unimplemented.

## B6 historical-context note
The original text describes the B3 installed set when written. The current B6 candidate **includes** Vampirism, Werewolves, Goety and other listed magic systems, but **Dragon Survival remains excluded/deferred**. No physiology exclusivity or hybrid-support rules have been implemented. The 125-mod combined dedicated server has been boot-tested; the 21 cross-state interactions and food/feeding safety require actual gameplay analysis.

## Additional actual death handlers: Enigmatic Legacy+ 1.1.2 (2026-11-14)
- [Exact pinned-version code audit](ENIGMATIC-LEGACY-PLUS-1.1.2-SKILLS-DEATH-AUDIT-2026-11-14.md): `CreationHeart` cancels death with high priority even when held in inventory (barring technical death), `TheCube` revives on cooldown and grants buffs, `VoidPearl` gives chance-based cancellation, `CosmicScroll` provides another death interception. The Cube may teleport on revival. **These are not standard Totem consumption and require separate event-order tests**.
- `CursedRing` tracks probable vs confirmed death, has a soul-loss meter (-10% max health per unrecovered Soul Crystal), and manipulates `LivingDropsEvent` with `SoulArchive`, `EscapeScroll`, `AscensionAmulet`/Storage Crystal, `EldritchAmulet` inventory preservation. Default maxSoulCrystalLoss **9** disables its explicit ≥10 permanent-death screen, **not** its normal soul-loss consequences. Also test `keepInventory` changes to lost-soul eligibility.
- Add Enchanted player-bound poppets (inventory/shelf) and vanilla Totems to the **prelethal phase**; test that no extra soul loss/duplicated graves/Curios inventory restoration occurs if a death is canceled. Enigmatic has event handlers at **HIGHEST**, **LOW**, and **LOWEST receiveCanceled** and requires actual compiled-B6 gameplay testing with death handlers from JAWR, Goety, Vampirism, Tombstone and the custom transformation compat addon.
- `LostEngine` extra `ItemCooldowns.tick()` every third server tick may accelerate unrelated mod cooldowns; test with existing spell systems and any death rescue cooldown rather than assuming independent timers.
- No change to the transformation combinations policy or installed configs; this is a test obligation only.

## Current approved target supersedes historical B3 exclusive-physiology proposal (2026-11-14 note)
**Important:** The start of this document is an earlier design proposal, **not current locked combination policy**. Subsequent user decisions established **two independently leveled lichdoms that can stack to form an Archlich**, unified vampire and werewolf base states with separate progression tracks, special abomination combinations, hunter exclusion, and limited dragon+lich exceptions (dragons cannot be Archlich). This is a **target specification, not implemented runtime compatibility**. Do not enforce the old blanket exclusivity text against intended combinations.

[Forbidden Magic first pass](JAWR-FORBIDDEN-MAGIC-0.5.37-INITIAL-AUDIT-2026-11-14.md): source of JAWR lich path, bound to 0.5.37 and parent JAWR 0.5.39. Need exact JAR inspection to test phylactery and parallel Goety lich revival, damage/respawn event sequences, survival resources and transformation/attribute sync. Tests must include both Goety→JAWR and JAWR→Goety with persistence checks, as well as Enigmatic, Enchanted, Totem and Tombstone combinations. No code integration implemented in this pass.

## Explicit cross-project ownership (2026-11-14 user decision)
The user is developing a **separate Transformation Compat mod** that owns all transformation states, stacking, and **death effects** across JAWR Forbidden Magic (lich), Goety (lich), Vampirism, Werewolves, dragons, abominations and hunters, and any additional resurrection/death-cancel systems. Pufferfish Skills does **not** own these investigations. This document remains a **compatibility research/handoff record**; it does not represent implemented bridge behavior. The later locked transformation coexistence design supersedes old blanket-exclusivity assumptions. **Keep Pufferfish scope limited** to ordinary player casting/resources/skill buffs and reusable universal pet/combat modifiers. Do not escalate missing death-handler/physiology source to a Pufferfish matrix blocker.
