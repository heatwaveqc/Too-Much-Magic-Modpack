# Magic mod survey — selection decisions (2026-11-13)

This document records the **user's current intent**, not successful integration or verification of release claims in the earlier survey. Minecraft target: **1.21.1 NeoForge**. `manifest/mods.json` is the canonical machine-readable roster for **approved** or **explicitly excluded** choices. The current installed state is controlled separately by packwiz metadata (`mods/*.pw.toml` and `index.toml`). **No new JARs, pins, or progression rules were added by this survey.**

## A. Approved — user believed these were already included

The following are **approved to include**, even though most were missing from the B3 manifest and none should be described as installed until pinned and tested:

- Goety — The Dark Arts
- Malum
- Eidolon: Repraised (confirm intended Eidolon project/port before pinning)
- Forbidden & Arcanus
- Hexerei
- Primal Magick
- **JAWR: Forbidden Magic — already included, installed, and B3 server-smoked**, alongside Just Another Witchery Remake
- Reliquary Reincarnations
- Enigmatic Legacy+ (specific port; verify its provenance)
- Botania Revived — user explicitly approves evaluating this unofficial port for 1.21.1; do not silently substitute original Botania or another fork
- Extra Botany Revived — planned addon for Botania Revived

### All addons / cross-mod integrations from the preceding survey — approved

The phrase *all addons and cross-mod integrations* is interpreted as **the ten specifically enumerated in the preceding assistant survey**. Any other addons discovered later are not automatically approved.

1. Ars Elemental
2. Ars Ocultas
3. Ars Additions
4. Ars Controle
5. Ars Unification
6. Ars Elemancy
7. Ars 'n Spells
8. Goety Cataclysm
9. Gaze (Malum addon)
10. Psi: Tweaks and Additions

Review each addon's precise supported versions of **both** its parent mod(s) and Minecraft/NeoForge, not just the filename or a project's most recent release. Avoid introducing a second unapproved version/port of a dependency.

## B. Newly and definitely approved

- **Psi** — programmable magic
- **HexCast NEOFORGED** — Hex Casting-style programmable magic through the identified port; source and maintenance quality pending confirmation
- **Embers Re-Ignited** — magical/industrial processing; review energy and logistics against the magic-first design
- **Totemic** — ceremonial/totem magic (identify the correct 1.21.1 NeoForge project, e.g. the Totemic candidate from the survey, before pinning)

These are approved selections, **not guaranteed viable releases**. Put candidates in B6 and proceed through the normal dependency and combined-boot procedure before enabling.

## C. Evaluate maturity *before* approving an install

- **Manaweave and Runes** — assess implemented spell designer, mana networks, rituals, progression loops, unfinished/placeholder features, known issues and release stability.
- **Thavma** — examine Thaumcraft-like research completeness, supported progression, alpha quality and issues.
- **Primeva** — investigate research, scanning, infusion, alchemy and golem systems; assess the extent of playable implemented content.
- **Mana and Artifice** — user particularly likes the original. Seek **actual** 1.21.1 NeoForge publication or credible active port first; older Forge releases do not count. If none exists, record unavailable rather than quietly using a different mod.

Decision required after actual repo/release examination. **No implied approval and no install** yet.

## D. Probably exclude due to overlapping gameplay — *soft exclusions*

These are **not** hard bans. User is leaning against them unless an actual distinctive mechanic justifies reconsideration:

- Vampirism
- Werewolves
- Howling Werewolf
- Toil and Trouble — check whether the relevant potion/cauldron mechanics are genuinely duplicated by Iron's Spells before claiming this overlap.
- Spells & Shields
- Wizards (RPG Series)
- Paladins & Priests (RPG Series)

## E. Explicitly excluded — *hard selections*

Do **not** install or advocate for inclusion without the user's changed decision:

- ElementalCraft — user dislikes it
- Nature's Aura — user dislikes it
- ForceCraft — user dislikes it
- Mystical Agriculture, Mystical Agradditions and their related resource-crop ecosystem — user states this was already a design exclusion

The specific two Mystical Agriculture entries and the three disliked mods have been added to the machine-readable `excluded` list. The ecosystem-level restriction applies to related additions even when each derivative project has not been assigned an ID.

## F. Tell me more — research queue, neither approved nor rejected

- Feywild
- Pastel
- Enchanted: Witchcraft
- Witchcraft and Curses
- Pagan's Blessing
- Scriptor Magicae
- Runology
- Summoning Rituals
- Aspect Alchemy
- Apothecary — investigate overlap with existing potion mechanics
- Magitech — Arcane Engineering
- Relics (RPG Series) — distinguish it from the **different Relics** mod already in the pack roster
- Jewelry (RPG Series)
- Runic Curiosities

Next discussion: compare distinct mechanics, active development and integration overhead before selecting any. None has been implicitly approved just by being on this list.

## G. Deferred / revisit later

- **Dragon Survival** — interesting playable-dragon system; user reports unusual food-compatibility behavior. Defer until food compatibility is understood. This is **not a hard exclusion**.

## Reconciliation with the current repository

- **B3 current state:** 64 pinned mods and a successful combined dedicated-server smoke test; client GUI, world entry, recipes, progression and worldgen behavior are not thereby verified. Do not confuse published support, approved selection and packwiz installation.
- Of the **eleven** main/addon items in section A preceding the detailed ten-addons list, only **JAWR: Forbidden Magic** was already in the B3 register and active. The ten remaining items were missing and have been entered as planned.
- The ten cross-mod addons were also missing, and all ten are now entered as planned.
- The four definite additions in section B were missing, and all four are now entered as planned.
- Original Botania remains a historical nonselected row; **Botania Revived** is the user-approved specific port candidate. Extra Botany Revived is a separate planned addon. Do not infer a successful release/artifact check from the selection.
- The three disliked projects and Mystical Agriculture family were **not actually listed** in B3's `excluded` machine roster; that discrepancy is now corrected.
- This pass makes **selection/roster changes only**, not assertions of compatible published releases; `release_status: unverified`, `version: null`, and `enabled_in_pack: false` are deliberate for all newly recorded approvals.

## Follow-up order

1. Review roster discrepancies and any cases where a previously stated exclusion might conceal distinct mechanics, **without overriding user decisions**.
2. Investigate the four maturity-test candidates and the fourteen *tell me more* candidates; verify exact 1.21.1 NeoForge artifacts and source repositories.
3. In a later build batch, resolve full dependency closures and license terms, pin approved viable mods with packwiz, perform combined server and client testing and record blockers.
