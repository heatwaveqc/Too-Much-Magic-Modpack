# Locked design decisions — implementation deferred

These are design invariants, not claims about live game behavior.

1. **ProjectE and Project Expansion:** all usable systems post-Chaos Guardian (Draconic Evolution). Lock all acquisition routes and functionality, not just crafting-table recipes. Check loot, villagers, quests, advancements, item duplication/EMC, starter items, commands and integrations for leaks; admin commands may remain a test bypass.
2. **Replication:** its Deepslate Replica Ore must be available through the highest-tier Occultism miner (Eldritch Ancient Miner) instead of ordinary worldgen. Confirm the exact mod, recipe/item IDs and whether any generation path can leak before coding this.
3. **Eldritch Ancient Miner:** gate by advanced Cataclysm materials, provisionally from the Harbinger and Ignis. Reduce the previously assumed eight-Marid input to a balance-target range of **two to four**. Consider very late Botania materials as another pathway or extra requirement; compatibility and balancing still pending.
4. **Tech materials:** uranium and draconic dust belong in dangerous deep exploration or Occultism mining, not routine overworld ore farm. Final sources and generation changes pending.
5. **Ring of the Seven Deadly Sins:** no normal acquisition at all, including crafting, world loot, trades, quest rewards, mob drops or generated containers. Audit similarly overpowered one-shot gear.
6. **Renewability:** every item/resource eventually needs a reproducible route. Boss loot can remain challenging; alternative routes must not bypass the intended boss/technology gates.
7. **Quests:** FTB Quests will communicate the pathways after core mechanics stabilize, not substitute for locks.

Do not add placeholder broken KubeJS recipes using speculative registry names.

## Psi-powered FE candidate (provisional)
- Prefer trying PsiTweaks' `trick_supply_fe` as an intentionally magical FE source. Review sustainable FE/t under player regeneration, CAD efficiency, automated casts, chunk loading, Draconic energy demand and any interaction with late-game gates.
- **BiggerReactors Rework remains installed for now.** It is a potential removal **only after** Psi can reliably handle the pack's entire generation needs, with the user's explicit subsequent approval. FE transport remains Draconic's responsibility; avoid generic energy networks.
- **Ars Nouveau + Iron's Spells intentionally share one mana pool** through Ars 'n Spells (provisional `iss_primary` setting pending exact config check). Psi stays outside this pool with its own player/CAD Psi; Hex Casting media and other native resource systems remain separate. The character skill tree is unified, not every resource meter.
- PsiTweaks penalty overrides, Ideaspace Storage, time acceleration, mass mining and similar tuning are **provisional** pending balance/economy tests; do not implement restrictions merely because an audit flagged risks.

## B4 compatibility proviso
- BiggerReactors Rework 0.1.6 adds a separate battery and industrial processor along with reactor energy generation. Review the energy/storage features against our limited-tech policy before progression work; it was explicitly approved as the newer fork for testing.
- Botania Revived may expose optional Ars Nouveau integration; balance mana/source conversions and remove any forbidden storage acquisition path.
- Experimental Astral Sorcery and Draconic Evolution are boot-tested, **not yet gameplay-accepted**; Draconic Chaos Guardian remains the ProjectE gate.

## Scriptor automation policy
- Scriptor remains **cooldown-based** (no Mana Mode). Strong, repeated casting by lecterns is intended, not a blanket balance defect. Smelting, mob farming, ground-item relocation, buff stations and ordinary existing-ore mining can be features.
- **No automatic ore conjuration** is present in stock `Place`; it creates colored magic blocks absent an existing block item. The user wants knowledge of what lecterns *actually* do before deciding any restrictions. Respect already locked Chaos Guardian, boss and hard-to-mine ore gates; don't invent new gates or nerfs without approval.
- Pursue *quality imprints* on crafted/written items and lecterns; personal cooldown bonuses must not give unattended machines perpetual live-owner skills. See [source audit](SCRIPTOR-LECTERN-SKILLS-AUDIT-2026-11-13.md).

## Embers Re-Ignited source-audit economy notes (2026-11-13)
- Embers is primarily magical machinery and item-powered player attacks. Retain its native item/machine **Ember** resource. Don't merge into Ars+Iron's mana, Psi reserves or other school resources.
- Ember Energy Converter gives **100 FE / Ember, maximum 1,000 FE/t per block** with a 20,000 FE internal buffer; the **sustained** FE supply and end-to-end power economics remain unmeasured. Evaluate with PsiTweaks FE generation and Draconic loads before a decision to remove BiggerReactors.
- `crystal_seed.enable_dynamic_metal_seeds` defaults **true**. A dynamic seed needs **three tagged ingots** and alchemy, then produces tagged nuggets from Ember; it **does not bypass first acquisition outright** but can make endgame metals renewable. `uranium` is a *static* seed excluded from dynamic discovery; its normal craft recipe was not found in inspected upstream tree. Audit pack-specific merged tags and recipes (particularly Draconic, uranium, replication-adjacent materials) before taking action. Datapack seed blacklists can be narrow if demonstrated necessary. **No gate, blacklist or configuration changed.**
- See [Embers source audit](EMBERS-SKILLS-AND-ECONOMY-AUDIT-2026-11-13.md) for exact event hooks, gear and validation.

## Enchanted ritual progression (source audit, not implementation)
- Keep Enchanted's six-block environmental altar and separate `RiteType` circle magic as a distinct **structure-powered** school. Unlike Ars/Iron's spells and Psi, it has **no native player mana bar**. Cross-school Pufferfish can enhance **caster-attributed ritual cost or crafted poppet quality** only through specific opt-in bridge work; do not globally boost all shared altars based on the creator's current perks.
- Stock **Transpose Iron** consumes `minecraft:iron_ores` in the world and brings drops to the circle; not generic ore creation or a bypass to late draconium/uranium gates. **Duplicate Waystone** is item-specific, not an arbitrary resource dupe. Stock rituals are powerful (weather, teleport, protection dome, curses); preserve them unless a *demonstrated* existing gate violation is found.
- Poppets trigger before lethal harm and may interact with multiple mods' death interception. Add event-priority, uncharged shelf/offline and depleted-doll tests to the transformation/death compatibility plan. Use the [full Enchanted deep dive](ENCHANTED-WITCHCRAFT-4.2.7-DEEP-DIVE-2026-11-13.md) for implementation candidates.

## Enchanted and Enigmatic Pufferfish decisions (2026-11-14)
- **Enchanted: Witchcraft — no dedicated skill integration** per user decision. Preserve crafting, altars, rituals and poppets; do not implement an extra bridge for trivial bonuses. Keep it in FTB Quest chapters and the death-system regression matrix.
- **Enigmatic Legacy+** provides artifact and one-equipped-spellstone powers, with three native player attributes. Recommend **no independent skill branch**, but these may be referenced by broadly shared Pufferfish combat/defense perks after numerical tests. Keep its Ring of Seven Curses and Soul Crystal risk/reward economy native, and do not extend their max-loss/permadeath effects without approval.
- Verify **Lost Engine's acceleration of global ItemCooldowns** and the spellstone death-cancel/Curios grave interactions before implementation. See [Enigmatic Legacy+ audit](ENIGMATIC-LEGACY-PLUS-1.1.2-SKILLS-DEATH-AUDIT-2026-11-14.md).
