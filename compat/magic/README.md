# Core Magic first-wave — Tyz test overlay

Status: **stageable source, not yet in packwiz, not game-tested**. Target MC 1.21.1 NeoForge, Tyz's Skills 6.4.2, KubeJS 2101. This branch builds on the Monk Tyz design; the MXP source registry and deeper contract are in [PR #27](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/27).

## Implemented five-rank base skills (20% of previous 99-rank ceiling per rank; one SP per rank)

| Skill | Native attributes | Additional KubeJS cost hooks | Initial/rank-5 example |
|---|---|---|---|
| Reservoir I | Ars Nouveau max mana; Iron's max mana; Psi total Psi | none | Ars +20/+99 mana; Iron +20/+99 max mana; Psi +990/+4950 Psi |
| Wellspring I | Ars mana regen; Iron's mana regen; Psi regen; Forbidden & Arcanus Aureal regen | none | Ars +0.495/+2.475 regen; Iron +0.198/+0.99; Psi +4.95/+24.75 |
| Thrift I | Goety soul discount; Neo Vitae sigil cost reduction | Ars Nouveau; Iron's Spells; Psi | 5.94 to 29.7 in the Neo Vitae percentage-scale attribute; Goety 0.0594 to 0.297 in native units, positive payment never reduced below 1 |
| Potency I | Ars spell damage; Iron's spell power; Goety spell potency; Eidolon magic power | none | Ars +0.99/+4.95 flat spell damage; Iron +0.198/+0.99; Goety +20/+99; Eidolon +0.198/+0.99 |

Seven independent magic mods are directly addressed: Ars Nouveau, Iron's Spells, Psi, Forbidden & Arcanus, Goety, Neo Vitae, Eidolon Repraised. Ars 'n Spells is a common mana payment bridge, **not** a second pool to buff or credit independently. These are *source-verified targets*, not tested performance claims.

Native attributes are backed by source API; the KubeJS script uses NativeEvents.onEvent for Ars SpellCostCalcEvent.Post and Iron's SpellOnCastEvent, and built-in PsiEvents.preSpellCast for CAD cost. Only real server players are eligible. The hooks only discount an existing positive cost. The MXP dispatcher must award based on the original pre-skill nominal cost after a committed payment, not on a preview or transfer; the separate MXP module is **not yet implemented here**.

## Staging into the next test build

Overlay path: compat/magic/tyzs-test-overlay. It contains four active CUSTOM skill JSONs with exactly five price/attribute values per skill, five categories (the four Tyz defaults plus Core Magic), a fallback level pool of 1000 XP => 1 SP, a common TOML disabling native Tyz activity XP, and the Thrift KubeJS server script.

Run from the repository:

    python3 compat/magic/stage_preview.py --check
    python3 compat/magic/stage_preview.py --pack-root PATH_TO_DISPOSABLE_PACKWIZ_CHECKOUT --install

The staging script merges existing categories rather than deleting third-party category additions. It avoids overwriting existing files unless explicitly authorized. The preview intentionally does not edit index.toml or install Tyz; the integrated build owner must pin Tyz 6.4.2, run packwiz refresh, validate the resulting hashes, address Pufferfish migration, and boot a clean server + client. Preserve B6/B8 rollback snapshots. Do not stage into a live survival instance.

The current nine-tree blueprint reserves Core Magic, three dragons, Vampire, Werewolf, Lich, Hunter and Monk. The next step is to relocate and verify all 32 stock Tyz skills before removing their old category tabs; this first-wave overlay keeps those tabs as transitional compatibility.

The five ranks represent 20%, 40%, 60%, 80%, 100% of the PREVIOUS rank-99 maximum value, not necessarily a literal +20% for every underlying attribute. See [full stock inventory and migration plan](../../docs/TYZS-STOCK-SKILL-MIGRATION-2026-11-15.md). Existing snapshots with 6-99 purchased ranks require a save migration/refund plan before activation outside disposable QA worlds.

## What to test in a disposable world

1. Load four skills, Core Magic category, and all native attributes with no KubeJS errors. Test /skills reload.
2. Give **administrative test XP**, e.g. /skills xp PLAYER add 1000. Confirm exactly one Tyz level + one SP. Ordinary mining/food/crafting/exploration/vanilla kills should award zero Tyz XP. This is a test mechanism, not a planned survival source.
3. Buy one rank of each skill; compare server player attributes and actual spell/resource behavior before/after. Refund and repeat; verify persistence and no duplicate modifiers.
4. Compare paid Ars and Iron's spell costs at Thrift ranks 0/1/3/5 with Ars 'n Spells shared mana. Test standard, channeled, cross-cast, free/scroll and enchanted equipment cases, including Botania's optional Ars source bridge. Do not award MXP twice.
5. Test Psi CAD spells with varying CAD efficiency and Psi reserve/regen; confirm Psi regen rounding at low levels, proper cost floor and no FE-generation double reward.
6. Test Goety potency/cost; Neo Vitae actual sigil payments; Eidolon output; and Forbidden Aureal regeneration. The latter two require game verification, not just attribute registration.
7. Run headless server boot and graphical client smoke separately. No claim of either test passing is made by the source overlay.

## Deliberate additional adapter work

- Roots Classic personal mana/Terra capacity and consumption use a nonstandard resource path; the searched GitHub default source is from an older Forge generation and must not be applied verbatim to pinned NeoForge.
- Mahou Tsukai player data and real cast/payment hooks need pinned binary inspection.
- Botania Revived uses item-mediated mana through ManaItemHandlerImpl; a short Java consumption bridge is needed to distinguish active use, automatic self-repair and mana transfers.
- Neo Vitae Anima max and regeneration are not the existing sigil-cost attribute; PostSyphon is a useful MXP transaction hook, not a generic capacity stat.
- Pastel Ink, Hex media, Primal Magick spells and Malum charges need distinct, source-verified adapters; avoid inventing universal attributes where there are none.

## Upstream source references

- [Tyz custom skills](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/docs/json-tweaks/create_skill.md)
- [Ars ManaUtil](https://github.com/baileyholl/Ars-Nouveau/blob/main/src/main/java/com/hollingsworth/arsnouveau/api/util/ManaUtil.java)
- [Iron's SpellOnCastEvent](https://github.com/iron431/irons-spells-n-spellbooks/blob/1.21/src/main/java/io/redspace/ironsspellbooks/api/events/SpellOnCastEvent.java)
- [Psi player attributes](https://github.com/VazkiiMods/Psi/blob/master/Xplat/src/main/java/vazkii/psi/common/attribute/base/ModAttributes.java)
- [Goety attributes](https://github.com/Vivideru/Goety-3/blob/main/src/main/java/com/Polarice3/Goety/init/ModAttributes.java)
- [Neo Vitae sigils](https://github.com/breakinblocks/NeoVitae/blob/1.21.1/src/main/java/com/breakinblocks/neovitae/common/item/sigil/SigilItem.java)
