# A Sufficiently Advanced Magic — TMM source-backed plan

**Owner decision: 2026-11-15.** Remove original Bigger Reactors and Bigger Reactors Rework; their uranium reactor, battery, turbine and exchanger lines are retired. This document replaces the previous nuclear build guide. The fifth Progression chapter retains its stable FTB chapter/quest IDs and now uses the file `a_sufficiently_advanced_magic.snbt` and title **A Sufficiently Advanced Magic**. No additional general FE transport/logistics mods are approved. Future explicitly approved technology mods belong in this chapter, while Botania's Mana Fluxfield remains part of its own magic chapter.

## Progression

1. **Early — Draconic Evolution Generator.** Burns normal furnace fuel; version-aligned 1.21 source defines five modes, from 5 to 300 OP/FE per tick with different fuel efficiency. Exact pinned-JAR and pack-config behavior still need in-world verification.
2. **Midgame — Botania Revived Mana Fluxfield.** Source declaring Botania Revived 2.0.2 converts mana at **10 FE per mana**, and offers up to **1,600 FE/t** attempted transfer to adjacent energy consumers. Actual throughput requires sufficient sustained mana and a receiving FE endpoint. Teach the Botania flower/mana generation setup in its **own chapter**, crosslink from FE progression.
3. **Other magical alternatives.** Psi and Hex Casting may provide additional FE paths. Confirm *actual energy capabilities, conversion costs, stage and implementation* in their pinned versions before claiming production values or writing tutorials.
4. **Storage and routing — Draconic Energy Core and crystals.** The Core is a battery, not a generator. Use its tier-specific Build Guide, four stabilizers, input/output pylons, and Draconic crystal network; test transfer and storage separately.
5. **Endgame — Draconic Reactor.** Extremely powerful but dangerous. Use dedicated reactor stabilizers, injector, separate containment power and guarded extraction; never conflate the Reactor with the Energy Core. Test in an expendable world and preserve Chaos/endgame material gates.

## Verification before final quest writing

- Can an ordinary Draconic Generator handle the first meaningful FE requirements with sustainable fuel?
- Can a reasonably achievable Mana Fluxfield farm handle the largest *pre-Chaos* Draconic Fusion Crafting input at practical speed?
- Do Draconic pylons, crystals and Energy Core accept Botania FE reliably in the assembled pack?
- What actual FE options do Psi and Hex Casting expose?
- Are removed BiggerReactors IDs gone from packwiz, quest manifests, runtime quest tasks, scripts and any future-QA staged work?

## Source references

- [Draconic Evolution 1.21 Generator](https://github.com/Draconic-Inc/Draconic-Evolution/blob/1.21/src/main/java/com/brandon3055/draconicevolution/blocks/tileentity/TileGenerator.java)
- [Botania Revived 2.0.2 Mana Fluxfield](https://github.com/Velron-Devs/Botania-Revived/blob/5f803227d484598da1f1093a91d5d7cf5cf9600d/src/main/java/velrondevs/botania/common/block/block_entity/mana/PowerGeneratorBlockEntity.java)
- [Next-QA draft PR #56](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/56) — retain planned Alex's Caves + CodxLib, **do not import reactor uranium bridge**.

**World migration warning:** removing the currently pinned Rework may remove its blocks, items and uranium from existing worlds. Back up test saves and perform new-world QA. Source inspection alone is not a client/pack test.
