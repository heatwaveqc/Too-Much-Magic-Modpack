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

- Howling Werewolf
- Toil and Trouble — **overlap confirmed, not exact duplication**: Iron's Spells already provides an Alchemist Cauldron with fluid-based Brew/Fill/Empty recipes, ink and elixir production; Toil and Trouble adds conventional Brewing Stand potion recipes to vanilla cauldrons (and dipped tipped arrows). Keep soft-excluded, as the value appears incremental. Sources: https://iron.wiki/changelog/ and https://modrinth.com/mod/toil-and-trouble .
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

## F. Tell Me More — selection resolved (2026-11-13 follow-up)

### Approved for inclusion — five projects
These **are approved choices** from the earlier 'Recommended Inclusion' and 'Strongly Investigate' groups, not yet installed or validated in the assembled pack:

- **Summoning Rituals** — KubeJS-backed customizable cross-mod ritual framework.
- **Scriptor Magicae** — discover and compose spells in a generated magical language.
- **Pastel** — color-resonance magic, research and exploration; review any logistics against the pack's restrictions.
- **Feywild** — distinct fey summons, characters and quests; review world generation alongside Tectonic.
- **Enchanted: Witchcraft** — larger witchcraft system with rites and poppets; compare interactions with JAWR and Hexerei.

All five are in `manifest/mods.json` as desired but inactive. Exact target NeoForge artifact and dependency closure remain unverified.

### Omit from this pack — nine projects
The user said to leave all remaining Tell Me More candidates out; Aspect Alchemy was separately withdrawn after a detailed source-level review. These are **current selection exclusions**, not claims they lack merit, and do not affect similarly named distinct mods:

- **Witchcraft and Curses** — small overlapping ritual features and unwanted extra biome/tree content.
- **Pagan's Blessing** — another partially overlapping ritual system.
- **Runology** — limited demonstrated content / maturity questions.
- **Apothecary** — avoid adding another potion/effect layer that might conflict with the many alchemy systems already selected.
- **Aspect Alchemy** — **removed by explicit user decision** after detailed code review. Does not justify hard-coded aspect integration, a known Jade 15.10.6 incompatibility, and extra maintenance given Neo Vitae, Ars Nouveau and Botania's deeper existing potion systems. No need to override any vanilla or other mod recipes.
- **Magitech — Arcane Engineering** — an additional magical-tech and resource/worldgen ecosystem.
- **Relics (RPG Series)** — avoid pulling in the RPG Series' Spell Engine-related dependencies; **not** the separately tracked base `relics` mod.
- **Jewelry (RPG Series)** — avoid new RPG Series spellcasting/attribute infrastructure.
- **Runic Curiosities** — underdeveloped/power-balance risk relative to our existing artifacts.

These nine are recorded in the manifest's `excluded` list, with the reason that they were omitted in the user-directed survey selection. The four distinct earlier *maturity test* candidates (Manaweave and Runes, Thavma, Primeva, Mana and Artifice) remain unresolved research, not excluded.

## G. Deferred / revisit later

- **Dragon Survival** — interesting playable-dragon system, **deferred, not rejected** because of unusual food compatibility. Verified published NeoForge 1.21.1 release line (CurseForge lists v2.0.74 dated 2026-11-10), and its 1.21.1 build permits disabling dragon-specific food handling (`disable dragon food handling` option added in 2.0.36). Earlier 1.21.1 changelogs include AppleSkin food-bar handling fixes. Its separate Dragon Survival Compatibility addon is for older MC versions, not required/available for 1.21.1; authors say future compat lives in the base mod. Audit custom diet enabled vs disabled, AppleSkin, modded edible items/placeable food, dragon form/equipment/flight and gameplay/client rendering before any approval. Sources: https://www.curseforge.com/minecraft/mc-mods/dragons-survival/files/all and https://www.curseforge.com/minecraft/mc-mods/dragons-survival/files/7036245 and https://www.curseforge.com/minecraft/mc-mods/dragon-survival-compatibility .

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
2. Investigate the four maturity-test candidates and verify exact 1.21.1 NeoForge artifacts and source repositories for the five remaining approved *Tell Me More* selections. The nine omitted mods are no longer in the research-to-include queue.
3. In a later build batch, resolve full dependency closures and license terms, pin approved viable mods with packwiz, perform combined server and client testing and record blockers.

## H. Newly approved supernatural factions (2026-11-13 follow-up)

- **Vampirism — Become a Vampire!** — explicitly approved. Release evidence: [CurseForge](https://www.curseforge.com/minecraft/mc-mods/vampirism-become-a-vampire/files/all?page=1&version=1.21.1), `Vampirism-1.21-1.10.13`, NeoForge 1.21.1 release dated 2026-09-05. Adds vampire/hunter factions, territorial mechanics, a Vampire Forest biome and powers.
- **Werewolves — Become a Beast!** — explicitly approved as the *Vampirism addon*, not Howling Werewolf. Release evidence: [CurseForge](https://www.curseforge.com/minecraft/mc-mods/werewolves-become-a-beast), `Werewolves-1.21-2.0.3.3`, NeoForge 1.21.1 release dated 2026-03-25. Requires Vampirism; new faction/forms and Werewolf Heaven biome.
- **Defer** skill-tree point limits, caps, levels and inter-mod balance to a dedicated later pass. Exact dependency closure and version pairing still require validation before packwiz installation.
- This approval does **not** approve every additional Vampirism addon. The pre-existing ten named addon approvals remain separate.


## I. Aspect Alchemy withdrawn after detailed review (2026-11-13 follow-up)

- User explicitly removed **Aspect Alchemy** from approved selections. Its separate brewing implementation is not valuable enough to justify integration when Neo Vitae, Ars Nouveau, and Botania already supply deeper and longer-lasting potion capabilities.
- Source review found hard-coded ingredient/aspect tables and no built-in datapack API for modded aspects; supporting other mods would require custom integration. An open Jade 15.10.6 compatibility issue further increases maintenance cost.
- Moved the machine roster entry from `mods` and B6 to `excluded`. It was **never installed** in the pinned pack. Do not revisit unless user explicitly changes the decision.
