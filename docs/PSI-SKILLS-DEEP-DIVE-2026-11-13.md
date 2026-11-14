# Psi 110 + Psi: Tweaks and Additions 0.10.10-hotfix — initial Pufferfish deep dive

**Research date:** 2026-11-13. Source-backed initial design assessment, **not** a successful Pufferfish gameplay test or verified exact-release bytecode inspection.

## Scope and evidence

- Installed B6: Psi `Psi-neoforge-1.21.1-110` (CurseForge file 8786571; release) and PsiTweaks `psitweaks-1.21.1-0.10.10-hotfix` (CurseForge file 9266947, beta).
- Compared public Psi **master** (multi-loader `gradle.properties` sets `mod_version=110`) and PsiTweaks **`1.21.1-0.10.10-hotfix` branch**. The older `VazkiiMods/Psi:1.21` source layout is obsolete for 110: don't use it for current KubeJS support. Exact published jar behavior still needs an in-world test.
- Classification under this project's matrix definition: **both Confirmed (source hooks exist)**; this does *not* mean the full skill tree or all addon spells are operational.
- Psi license is custom Psi License; no third-party source or jar is copied into this repo. PsiTweaks is MIT-licensed.

## Core conclusion: player statistics are already attributes

Psi 110 declares two synchronized vanilla-style player attributes in `ModAttributes`:
- `psi:total_psi`: **5000** default, maximum internal player Psi; excludes CAD battery.
- `psi:regen`: **25 Psi/tick** default, measured as regeneration before cooldown and CAD recharge interactions.

`PlayerData.getTotalPsi()` and `getRegenPerTick()` directly read the player's attribute values. The player's actual available Psi is stateful and capped when ticking, so **don't model changes by incrementally changing current Psi every tick**. Pufferfish skill purchases can give stable attribute modifiers; refunds remove the modifiers and the max clamps on tick. Verify sync, respec and bonus stacking in-world.

Psi has a **second reservoir on the CAD**: Battery component `overflow`. CAD storage and wearer reserves interact through regeneration, casting, and damage behavior; don't conflate `psi:total_psi` with the equipped CAD battery.

## PsiTweaks also exposes a player spell damage attribute

- Its pinned-release branch registers synced `psitweaks:spell_damage_factor` (default **1.0**, range 0–100) to Player.
- Source for damaging addon tricks (e.g. Aqua Cutter, Blaze Ball, Dry Meteor, Guillotine, Active Air Mine, Flare/Ice Circle, Phonon Maser) multiplies spell damage by the player's `SPELL_DAMAGE_FACTOR` alongside global/per-spell config multipliers. Not all special pieces or damage methods are proven to use it.
- The Sorcery Booster Curio adds **+0.3** (only when worn in the relevant slot); Psi/Moval suit armor also adds damage and Psi regeneration/max attributes. A Reactive CAD Battery grants **+10 Psi regen** to the wearer and Quantum battery **+1500 max Psi**, checked per 10 ticks.
- **Cross-school design:** prefer mapping shared 'magic damage' progression to existing `psitweaks:spell_damage_factor` and relevant Pufferfish generic magic attribute only where testing proves no double-hit; do not assume Psi's *base* tricks also use this PsiTweaks-only stat.

## Casting and CAD architecture

Psi spells have compile-time metadata: complexity, potency, projection and bandwidth (plus cost), checked against the CAD. Components and stats belong to equipment:

| CAD part | Configurable stats |
|---|---|
| Assembly | `efficiency`, `potency` |
| Core | `complexity`, `projection` |
| Socket | `bandwidth`, `sockets`, `saved_vectors` |
| Battery | `overflow` |

Psi 110 reads stats from the datapack registry `psi:cad_component`, with live stat lookup on already-assembled items when the world is reloaded. `CADStatEvent` can change queried values, but **the event has no player**, only the CAD, component and stat. Use datapacks for global CAD rebalancing; for *player-dependent equipment craftsmanship*, prototype a once-per-assembly/retrieval imprint using `PsiEvents.cadTake`/`assembleCad`, storing permanent custom data on the CAD and reading it from `cadStat` (must verify mutation/lifecycle). This should work like Roots' first-pickup item benefits, not like casting-time rewards. Alternative item-property redesign requires approval.

**Cost:** `ItemCAD.getRealCost` roughly scales with `100 / efficiency`, after bullet cost modifiers; a base 1,000-cost spell at CAD efficiency 125 is about 800 Psi before integer rounding and other conditions. Psi's `PsiEvents.preSpellCast` provides `getCost/setCost`, cooldown, player, CAD and bullet; it fires **after** `compiled.metadata.evaluateAgainst(cad)`. Therefore a player skill can reduce a *successfully compiled cast's* cost, but it **cannot automatically bypass insufficient CAD potency, complexity, projection or bandwidth**. Need correct client/server symmetric behavior where casting is predicted.

## Psi 110's first-party KubeJS support

Psi's NeoForge `docs/kubejs.md` explicitly supports `PsiEvents`: `cadStat`, `assembleCad`, `cadTake`, `preSpellCast`, `spellCast`, `regenPsi`, `pieceExecuted`, `pieceKnowledge`, `psiArmor`, `programmerPopulate` and more. The KubeJS class filter exposes `vazkii.psi.api.*`, **not** `vazkii.psi.common.*`. Script-created pieces with mandatory `complexity`, `potency`, `cost`, `projection`, `bandwidth` can be registered in startup scripts; they execute server-side and lack client prediction.

Use a small central KubeJS adapter to read actual Pufferfish skill levels and apply cost/regen rewards in the **native Psi events**, rather than running a generic per-tick command to overwrite Psi resources. Validate the runtime scripts before implementing a production tree.

## Progression controls are much stronger than previously assumed

Psi 110 supports datapack `psi:spell_piece_group` and `psi:spell_piece_settings` registries:
- `psi:advancement` groups are **enforced**: hidden in programmer, rejected at compile, blocked on clipboard imports; player's client needs a visible advancement.
- `psi:execute_main` groups describe tutorial progression but **do not enforce** unlocks. Do not mistake them for hard locks.
- `enabled:false` settings disable pieces even for Creative mode and invalidate previously programmed casts without deleting their data.
- PsiTweaks has **another independent spell-unlock system**, `SpellUnlockHandler`, including a program-research item path and server commands. Confirm conflicts before layering Pufferfish skill-gates on top of both.

This gives us a source-backed way to gate dangerous automation/generation and late-game spells **without banning the entire addon**. Requires regression tests to ensure previously-encoded bullets do not continue to work.

## Balance and modpack-contract risks — high priority

1. **Default penalty overrides:** PsiTweaks `PsitweaksConfig` defaults `disableDamagePsiDeduction=true` and `disableRegenCooldown=true`. Base Psi normally drains Psi on taking damage and can impose cooldown. Actual configured behavior in B6 is not runtime-verified; audit and decide whether to retain or change these before increasing regeneration.
2. **Ideaspace Storage** is a player-owned saved-data storage for items, fluids, chemicals, and **FE**, with remote access and connected logistics. Source defaults include **256 distinct item types**, up to **16,384 stacks per type**, and `Long.MAX_VALUE` FE per player. Source indicates its core exists outside Mekanism-specific classes. This potentially supersedes Occultism storage and conflicts with the generic logistics/storage exclusion; it is not automatically okay just because the UI is magical.
3. **`psitweaks:trick_supply_fe`** directly injects FE into a target block. Its source gives **4000 FE per 200 base Psi** at CAD efficiency 100 (**20 FE/Psi**, before other costs). That may serve as an alternative magical power source, but can prematurely bypass the exceptional late-tech reactor/Draconic economy. Evaluate tier/availability.
4. **`psitweaks:trick_mass_block_break`** has area mining behavior; it is not the banned FTB Ultimine mod, but can undermine deep-exploration/Occultism ore gates if worldgen or target conditions are not secured.
5. **`psitweaks:trick_time_accelerate`** accelerates random ticks/block entities; test farming and machine loops, especially any replication/resource-production exploits.
6. The addon has several aggressive damage tricks, including unusual direct-effect mechanics. Test which respect `psitweaks:spell_damage_factor`, general Pufferfish magic damage, damage immunity and PvP settings.
7. Mekanism-specific machines (including a player-Psi-powered FE generator and MekaSuit) are optional and should **not** be installed indirectly. Source README says the addon can load without Mekanism and offers alternative recipes. The **non-Mekanism** Ideaspace and FE-supply spell require separate scrutiny.

The above identifies **risks**, not a new owner-approved ban, removal, or change to the selected mod roster.

## Unified magic design choice

The shared Pufferfish skill tree can safely offer **one progression vocabulary**, mapping maximum resources, regeneration, and damage to Psi-native player attributes. A **single shared mana pool** across Psi, Ars and Iron's is a separate technical problem. Psi uses integer player Psi plus a CAD-battery overflow reservoir, its own casting validation, and separate client/server cost paths. Do not replace Psi's meter with Iron's/Ars mana without explicitly designing conversion, battery behavior, casting rollback, and no double-spend. Investigate a reversible conversion layer after the native attribute path is tested.

**Recommendation:** classify both parent and addon as source-backed Confirmed, defer live player↔CAD stat craftsmanship and cross-pool interoperability to tests, and resolve the storage/FE/automation policy before publishing the pack.

## Minimal staged test plan

1. **Version/boot:** B6 with unchanged pins, clean server and client startup (particularly Psi110 KubeJS plugin with KubeJS 2101.7.2-build.381); disposable world only.
2. **Native stats:** inspect player attributes; add/remove temporary attribute modifiers on `psi:total_psi`, `psi:regen` and `psitweaks:spell_damage_factor`; compare capacity, regen and an addon damaging spell. Validate client HUD.
3. **Cost KubeJS:** opt-in `PsiEvents.preSpellCast` sample reducing ordinary cast cost, with a failing compile-time CAD potency test and side parity; no permanent global skill buff.
4. **CAD stat:** temporary datapack changes to a basic core/assembly; verify existing and new CAD stat reads. Optional once-per-assembly imprint test, with no duplicate or refund.
5. **Costs/penalties:** test Psi loss on damage, regeneration cooldown, CAD battery overflow, loopcasts, fake player/automatic casts, and addon default config.
6. **Research and gating:** lock a test Psi piece with `psi:advancement` and another with `enabled:false`; test clipboard and already-programmed bullets. Cross-check PsiTweaks' separate `spell_unlocks` rules.
7. **Exploit audit:** Ideaspace storage access/capacity, FE supply, mass mining, Time Accelerate, special attacks, ProjectE/Replication gates and resource generation.
8. **Unification:** only after above, test universal spell damage, mana cost and possible safe cross-pool conversion with Ars/Iron's. Avoid modifying actual player progression until design approval.

## Source references

- Psi 110 release: https://www.curseforge.com/minecraft/mc-mods/psi/files/8786571
- Psi 110 sources: https://github.com/VazkiiMods/Psi/blob/master/Xplat/src/main/java/vazkii/psi/common/attribute/base/ModAttributes.java ; https://github.com/VazkiiMods/Psi/blob/master/Xplat/src/main/java/vazkii/psi/common/core/handler/PlayerData.java ; https://github.com/VazkiiMods/Psi/blob/master/Xplat/src/main/java/vazkii/psi/common/item/ItemCAD.java
- Psi official KubeJS integration: https://github.com/VazkiiMods/Psi/blob/master/docs/kubejs.md
- Psi official CAD datapack definitions: https://github.com/VazkiiMods/Psi/blob/master/docs/cad-components.md
- Psi official piece gating: https://github.com/VazkiiMods/Psi/blob/master/docs/spell-pieces.md
- PsiTweaks pinned source branch: https://github.com/moratan251/psitweaks/tree/1.21.1-0.10.10-hotfix
- PsiTweaks player attributes: https://github.com/moratan251/psitweaks/blob/1.21.1-0.10.10-hotfix/src/main/java/com/moratan251/psitweaks/common/attributes/PsitweaksAttributes.java
- PsiTweaks player gear buffs: https://github.com/moratan251/psitweaks/blob/1.21.1-0.10.10-hotfix/src/main/java/com/moratan251/psitweaks/common/items/component/CadBatteryAttributeHandler.java
- PsiTweaks spell unlock handler: https://github.com/moratan251/psitweaks/blob/1.21.1-0.10.10-hotfix/src/main/java/com/moratan251/psitweaks/common/handler/SpellUnlockHandler.java
- PsiTweaks defaults and Ideaspace settings: https://github.com/moratan251/psitweaks/blob/1.21.1-0.10.10-hotfix/src/main/java/com/moratan251/psitweaks/common/config/PsitweaksConfig.java
- PsiTweaks FE spell: https://github.com/moratan251/psitweaks/blob/1.21.1-0.10.10-hotfix/src/main/java/com/moratan251/psitweaks/common/spells/spellpiece/trick/PieceTrickSupplyFE.java
- PsiTweaks public overview: https://modrinth.com/mod/psitweaks

**No live tests, skill-tree implementation, config changes or bans were made as part of this research.**
