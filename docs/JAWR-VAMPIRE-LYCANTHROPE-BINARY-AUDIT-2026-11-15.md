# JAWR 0.5.39 vampire + lycanthrope coexistence: pinned-JAR audit (2026-11-15)

**Status: binary research completed; no JAWR dual-form gameplay demonstrated.**
Target: Minecraft 1.21.1 NeoForge; `witchery-0.5.39+mc1.21.1.jar`, from the pack's checked-in Modrinth pin, SHA512 `a3d50e65bb494b4d9b709163a295ddcecf53c498314df473d6de55633a4a5ebe810b9a0b5672f97f41786c03af01ef3ac91f0262216e074887e75d6afc35141d`.

**Reproducible reports:**
- [First successful pinned-binary run](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43688537765).
- [Second successful run, with core affliction and data-state inspection](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43688703448), [text report artifact](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43688703448/artifacts/14151088136).
- Auditor: `scripts/audit_jawr_hybrids.py`. It temporarily downloads and hash-checks the exact upstream jar and uses `javap -c -p` plus packaged JSON/localization inspection. It never commits or redistributes upstream binaries.

## Findings directly grounded in 0.5.39 bytecode

| Question | Evidence | Implication |
| --- | --- | --- |
| Are vampire and lycanthrope independent player states? | `VampirePlayerAttachment$Data.isVampire()` and `WerewolfPlayerAttachment$Data.isWerewolf()` **each return their own level > 0**; there are distinct attachment readers/writers and distinct `VampireLeveling`/`WerewolfLeveling` implementations. | Native data layout can represent both levels simultaneously; neither boolean encodes exclusive species membership. |
| Does JAWR reject an already-vampire player becoming a werewolf? | `WerewolfLeveling.infectPlayer(ServerPlayer)` tests the **werewolf level only**, then grants werewolf/1 criterion and increases level. No vampire gate in this method. `WerewolfLeveling.setLevel` manipulates its own attachment. | No refusal at this central infection method, but other acquisition/quest entry points and live client behavior still need verification. |
| Does becoming/clearing vampire overwrite lycanthropy? | `VampireLeveling.setLevel` writes vampire attachment and blood-specific data, and `WerewolfLeveling.setLevel` writes wolf attachment. Neither selected setter clears the other **affliction**; setting a level to zero clears the *active transformation form* and selected abilities. | Progression records can coexist, but cures/level resets can disrupt the currently displayed form/UI. |
| Are bat/wolf/wolfman separate simultaneous models? | `TransformationHandler` uses a *single* `TransformationPlayerAttachment$TransformationType` with `NONE`, `BAT`, `WOLF`, and `WEREWOLF`; its setter methods replace that active form. `removeForm` re-applies vampire attributes and re-evaluates werewolf modifiers. | One active JAWR form at a time. This is a useful built-in arbiter, **not** an additional hybrid renderer. |
| Are both afflictions recognized by shared systems? | Six classes reference both attachments, including `WitcheryNeoEvents` and `TransformationHandler`. `WitcheryNeoEvents.onPotionEffectApplied` separately evaluates vampire and werewolf status-effect blacklists. | Some runtime systems already tolerate both state flags; no explicit 'abomination' class or hybrid-specific feature was found by name/constant search. |
| Do the survival/damage engines continue operating? | `VampireEvents.tick` gates on `isVampire()` and invokes `handleSunExposure`, `handleBloodHunger`, `handleBloodHealing`, and `applyActiveEffects`. `WerewolfEvents.tick` separately gates on wolf level, moon phase, and form; `WerewolfEvents` provides silver-damage checks. | Expect **both** native hazard systems in a dual-afflicted player. Test resource ownership and status consequences; do not infer a complete, balanced original-Witchery hybrid. |
| Can we intercept forced wolf transformations? | `WerewolfEvents.tryForceTransform` and `tryForceRevert` dispatch cancellable `WerewolfAfflictionEvent.ForceTransform` / `ForceRevert` events before changing forms. Moon Charm suppresses the automatic paths. | Promising non-mixin hooks to arbitrate a TeamLapen beast form and JAWR wolf/bat form. |
| What does the JAWR Remove Curse ritual do? | `RemoveCurseRitualEvent.cureAfflictions` calls `AfflictionEvents.cureAfflictions(ServerPlayer)`, which iterates **all registered affliction levelers with positive levels**, calls `setLevel(player, 0)` for each, then posts `CureAfflictions`. | A single *JAWR* Remove Curse may clear both JAWR vampire **and** werewolf states (plus other registered afflictions), rather than cure one chosen branch. Verify exact ritual flags/targeting and protect wider pack state. |

**Interpretation:** 0.5.39 appears to be *structurally permissive* of a JAWR vampire + JAWR lycanthrope. It does **not** provide a separately identified Abomination class or special hybrid progression in the bytecode-name/constant search. Neither the old Witchery hybrid blood-cap penalty nor special wolf-form vampire-power suppression has been established as present in JAWR; do not carry those historical rules into our pack by assumption.

## Design implication for Too Much Magic

1. Treat Werewolves: Become a Beast as an accessible entry path. The JAWR altar/bear-trap infection is a **later earnable lycanthropy milestone**, analogous to JAWR vampire ascension after Vampirism. Never silently award the advanced JAWR tree just because a player becomes a base werewolf.
2. Prototype *native JAWR dual affliction* before inventing custom hybrid shape, survival, or buff engines. Retain both JAWR progression tracks if native dual acquisition survives gameplay testing.
3. Do not mistake JAWR dual attachment capability for TeamLapen faction coexistence. Vampirism/Werewolves still have a **single active faction** and exact-faction ability guards, requiring a compatibility adapter/custom faction or supported substitute for a playable four-tree Abomination.
4. Keep our existing rule: Abomination is vampire + werewolf, **no lich prerequisite**; normal cross-infection is not allowed to bypass explicit hybrid progression. Untested lich stacking is denied.
5. Abomination survival and damage need explicit owner selection (canonical Vampirism blood; no competing hunger/cooldown/appearance; sunlight, silver, garlic, wolfsbane) and should preserve the original mods' earned abilities. No state mutation is implemented by this research.

## Mandatory disposable-world checks before hybrid code

- Level 1 JAWR vampire -> JAWR werewolf via altar/trap, then reverse order. Record *both* attachment levels and ability trees after relog/restart.
- JAWR vampire bat form -> wolf -> wolfman -> return to humanoid; test forced full-moon transformation, Moon Charm, dimensions, and PvP.
- Blood at zero/partial/full and ordinary food/raw meats while in each form. Make sure our existing vampire chassis does not double-spend blood or hunger.
- Silver, sunlight, garlic, fire, werewolf protective equipment, and both forms' attributes; ensure no duplicate buffs/hazards.
- JAWR Remove Curse, JAWR Lilith garlic vampire cure, and Werewolves-side cure; verify independently or atomically intended consequences and restoration.
- JAWR dual status *plus* TeamLapen single-faction constraints; test independent skills, model rendering, network/relog persistence, multiplayer isolation and actual death.

## Verification boundary

The two audit CI runs **passed checksum, syntax, bytecode inspection and report upload**. No full Prism integration pack, NeoForge dedicated-server **hybrid world**, or in-client four-tree gameplay test was run in these research jobs. No Python listener is inserted into live Minecraft; a read-only Java/NeoForge state probe and event trace can be added to the next disposable gameplay build if static findings prove insufficient. Do not claim production compatibility from this report.
