# Monk ranked-skill backend evaluation

**2026-11-14 — source-backed research, not an implementation decision.**
Pack: MC 1.21.1 NeoForge; Pufferfish Skills 0.19.2 and Improvable
Skills 3 21.0.2 are already installed. No new mod is approved here.

## Actual storage vs how players see levels

| Option | Rank storage and cost | Monk permission gate | Fundamental limitation |
|---|---|---|---|
| **Improvable Skills 3 (existing)** | **Per-skill numeric levels**, XP cost formula, skill book and custom Java `PlayerSkillBase` registry | Subclass `canUpgrade(PlayerSkillData)`, called by server upgrade packet | **Java `short` storage**, normal highest purchasable rank **32766**; XP cost returns `int`; separate book screen; scroll bypass |
| **Pufferfish Skills (existing)** | **Category** XP level and points, but **skill node** stores only unlocked/not-unlocked ID | Category lock/unlock and programmatic node unlock; per-node Monk gate unproven | Repeat purchases of the *same node* cannot be modeled without custom rank store/UI |
| **Tyz's Skills 6.4.2 (new)** | Per-skill ranks, JSON skill definitions, configurable SP costs, bulk purchase/refund | Cancellable `SkillActionEvent.PurchasePre`, including bulk path | Hard **99-rank** constant in 1.21.1 source, plus new overlapping character skill system |
| **SkillLib (new)** | JSON `max_level`, int-valued rank store, attribute effects | Skill prerequisites and external unlock | Normal skill level **purchase/XP UI not present** in inspected client screen; we'd build it |
| **Reskillable Reimagined 4.1.x (new)** | Individual XP skill levels, documented API can veto leveling | Yes | Only **8 extra custom skills** on 1.21.1; too narrow |
| **PlayerEx 5.0.0 (new)** | Spend attribute points on 6 major stats / magic schools | External integration needed | Whole equipment/character RPG overhaul, not 98 affix-specific upgrades |
| **Project MMO (new)** | Separate activity-earned skill XP levels | External bridge | *Train by doing*, not choose and purchase next rank; PMMO/Pufferfish bridge grants Pufferfish points |
| **Player Abilities (new)** | Leveled active/passive abilities, integrates Pufferfish node grants | Supported ability requirements | Pufferfish grants still one-time nodes; no repeatable purchase UI by itself |

## Improvable Skills: exact developer findings

Source: `dragon-forge/ImprovableSkills` branch `1.21`;
**verify source vs exact 21.0.2 JAR before coding**.

- `api/registry/PlayerSkillBase.java`: constructor takes max
  level, has `canUpgrade`, XP formula, `onUpgrade`,
  `onDowngrade`, tick and NeoForge event hooks.
- `api/PlayerSkillData.java`: `Map<ResourceLocation, Short>`
  and NBT `putShort("Lvl", ...)` are the actual rank store.
- `net/PacketLvlUpSkill.java`: server invokes `canUpgrade`
  before incrementing the rank; also checks
  `lvl < Short.MAX_VALUE - 1` (**32766** upper endpoint).
- `api/SkillCostConfig.java`: customizable expression using
  `%lvl%` and `%xpv%`, but returns `int`. Validate large ranks
  against overflow and negative cost. Default currency is **vanilla XP**,
  not Monastic Mastery XP or Pufferfish points.
- **Security trap:** `custom/items/ItemSkillScroll.java`
  directly calls `setSkillLevel` on scroll consumption and automatic
  inventory-use, **bypassing `canUpgrade`**. Custom Monk skills
  MUST NOT enable or generate scrolls; also audit all commands and
  data mutation paths. GUI hiding alone is not a permission gate.
- The mod's built-in Mining skill requires the proper tool and
  held-item destroy speed: it is **not a Monk barehand adapter**.
  Likewise existing buffs may overlap Pufferfish shared modifiers.
  Use **one canonical owner** per stat and test actual tool/empty-hand
  events before applying more modifiers.

**Short storage is a real technical cap**, not a design-target hard cap.
Several hundred Mahou *ranks* could provide hundreds of thousands of
Mahou capacity if the installed Mahou maximum permits it; don't confuse
stat ceiling with purchased rank count. Nonetheless, no claims of
literally infinite ranks are appropriate.

## Tyz's Skills: reason it doesn't win outright

Latest 1.21.1 release 6.4.2 offers JSON-configurable individual
levels, a well-defined public API, cancellation of purchases before
SP debit, and bulk rank purchases. The public 1.21.1 source
`Constants.SKILL_MAX_LEVEL=99`, enforced in
`SkillManager.setSkillLevel`. This is a worse default for the
open-ended Monk than Improvable's 32766-rank store despite cleaner
faction gating. It adds another progression mod and UI.

## Recommended experiment — do not add a new mod

1. Leave **native Monk faction** as owner of singular unlocks,
   identity and permission to use advanced disciplines.
2. Leave **Pufferfish** as the existing spatial skill-tree/universal
   one-time unlock layer. Its category levels earn points; nodes
   are not repeatably purchased.
3. Prove **one addon-created Improvable Skills rank** gated by a
   Monk-native license (`canUpgrade`) without skill scrolls, and
   one truly **universal** attribute upgrade available to every
   player. Verify the Monk never pays twice for a common rank.
4. Test an effective finite cap (Cooldown Reduction), one high-rank
   trait (barehand Mining Speed or cross-mod Magic Capacity), server
   purchase and refund, logout/death, faction respec, scroll/command
   bypass, and both client GUIs. Test level **300+** with rising
   costs, not just level 2.
5. Decide whether the **second book UI** and **32766 limit** are
   acceptable. If yes, Improvable Skills saves a large amount of
   new code; if not, the next candidate is a **small custom
   per-trait rank store/purchase interface** connected to Pufferfish's
   existing tree—not another whole RPG progression mod.
6. Decide rank currency separately: vanilla XP (Improvable default),
   Pufferfish points, or Monastic Mastery XP. Keep ordinary traits
   available to non-Monks as the user intends.

No integration implementation or in-game proof exists yet.
Do **not** change the manifest or remove Pufferfish/Improvable.

## Source links

- Pufferfish [skill ID storage](https://puffish.net/skillsmod/docs/creators/configuration/skill);
  [category level/XP](https://puffish.net/skillsmod/docs/creators/configuration/files/experience)
- [Improvable Skills 21.0.2 release](https://modrinth.com/mod/improvable-skills/version/21.0.2)
- [Improvable skill API](https://github.com/dragon-forge/ImprovableSkills/blob/1.21/src/main/java/org/zeith/improvableskills/api/registry/PlayerSkillBase.java),
  [rank store](https://github.com/dragon-forge/ImprovableSkills/blob/1.21/src/main/java/org/zeith/improvableskills/api/PlayerSkillData.java),
  [server purchase](https://github.com/dragon-forge/ImprovableSkills/blob/1.21/src/main/java/org/zeith/improvableskills/net/PacketLvlUpSkill.java),
  [scroll bypass](https://github.com/dragon-forge/ImprovableSkills/blob/1.21/src/main/java/org/zeith/improvableskills/custom/items/ItemSkillScroll.java),
  [cost](https://github.com/dragon-forge/ImprovableSkills/blob/1.21/src/main/java/org/zeith/improvableskills/api/SkillCostConfig.java)
- [Tyz's Skills](https://github.com/Tyzbrand/TyzsSkills),
  [Reskillable Reimagined](https://wiki.pixeldreamstudios.net/mods/reskillable-reimagined),
  [SkillLib](https://github.com/Yvliux/SkiLLlib),
  [PlayerEx](https://github.com/BareMinimumStudios/playerex),
  [Project MMO](https://modrinth.com/mod/project-mmo),
  [Player Abilities](https://modrinth.com/mod/player-abilities)
