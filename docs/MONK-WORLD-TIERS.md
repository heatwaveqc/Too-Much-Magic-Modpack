# Monk world-tier progression, harvest capability and gliding

**Design commitment, not gameplay code** — 2026-11-14.

## Flight (revised by owner)

1. **Before the Ender Dragon:** legal normal movement, jumps, blink,
   water traversal, spells and permitted Curios. The Monk has no
   innate powered Elytra.
2. **After actual Ender Dragon progression:** offer
   `tmm_monk:momentum_gliding`, an **innate, non-hovering** Elytra-style
   glide with small continuous acceleration that offsets vanilla drag
   while fall-flying. Preserve pitch/dive/speed skill; server-authoritative
   thrust with speed/safety checks. This may need an innate fall-flying
   grant or an allowed external glide source, independently tested.
3. **Creative Flight is NOT a Monk skill, at any rank.** An eligible Monk
   may use its allowed Draconic Evolution chestpiece **as a Curio** and
   obtain Creative Flight from a working Draconic/Chaotic Flight module
   according to the normal Draconic Evolution progression. Do not add
   `tmm_monk:creative_flight`, revoke flight supplied by that gear, or
   accidentally create a second provider. The user's earlier post-Chaos
   flight preference is now a separate **pack-wide acquisition question**,
   not a Monk skill or a Monk-imposed flight gate.

Draconic source: https://github.com/Draconic-Inc/Draconic-Evolution/blob/1.21/src/main/java/com/brandon3055/draconicevolution/init/DEModules.java
("Wyvern" = Elytra only; Draconic/Chaotic = Elytra + Creative).

## Separate harvesting tiers from harvesting speed

**Existing reference:** the Dragon Survival 1.21.1 source provides separate
`harvest_bonus` and `break_speed_multiplier` values using a data-driven
`HarvestBonus` codec and harvest checks in `HarvestBonuses.canHarvest`.
It demonstrates precisely the division the Monk should reproduce:
- **Barehand harvest tier:** finite unlock track, calculated against the
  *actual tool/block requirements in the final pack*. Tier only determines
  whether a bare hand can correctly harvest a block and receive expected
  drops. At maximum, it matches the best obtainable tool capability;
  further investment is disallowed. No speculative "tier 99" or bypass
  of protected/locked ores. Player must use legal empty-hand action.
- **Barehand harvest speed:** independent **repeatable numeric upgrade**,
  with escalating cost and no arbitrary node/rank cap, constrained only by
  meaningful native speed/math/performance limits. Potential modifiers:
  `minecraft:player.block_break_speed`,
  `minecraft:player.mining_efficiency` and/or
  `apothic_attributes:mining_speed`; verify which combination is effective
  and avoid counting one investment twice. A Monk with mining speed 1000
  must still fail to collect an ore requiring a higher *harvest tier*.
- **Tool type / drop correctness:** a harvest-level bonus alone may not
  satisfy every block's required tool, especially modded ores. Mirror
  proven checks for block tags, tool actions and loot contexts, but do
  not pretend the player's empty hand contains a forbidden pickaxe.
  Model an explicit Monk-compatible virtual tool context if necessary;
  no fake held weapon or item may affect enchanting or affix slots.

Dragon Survival source:
- https://github.com/DragonSurvivalTeam/DragonSurvival/blob/1.21.1/src/main/java/by/dragonsurvivalteam/dragonsurvival/common/codecs/HarvestBonus.java
- https://github.com/DragonSurvivalTeam/DragonSurvival/blob/1.21.1/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/attachments/HarvestBonuses.java

**Integration caveat:** Dragon Survival is now **approved and present in `manifest/mods.json` as desired**, pinned to the verified 1.21.1 NeoForge candidate file (v2.0.74), but its `enabled_in_pack` is **false** pending packwiz installation/boot. Its API is not yet available as a pack runtime dependency. Verify the
final pack composition and exact release before assuming its events or
classes are present. The Monk must not depend on dragon form, since the
factions are mutually exclusive.

## Achieving every world tier without equipment violations

| World milestone | Required Monk path | Monk ability progression |
| --- | --- | --- |
| Nether and End access | Legitimate mining/tool equivalence or legal magic, access to required resources and portals | Barehand tier milestones stop at real tool capability |
| Ender Dragon | Legal unarmed or magic DPS, survivability, spell/summon kill attribution | Innate momentum-gliding technique |
| Cataclysm Harbinger / Ignis | Scalable natural defense, damage and loot; magic/mining route to miner components | Higher-tier resilience, mining and spells |
| Eldritch Ancient Miner | Legitimate advanced Occultism miners and boss materials | Continued mining/utility capability |
| Chaos Guardian | Pre-Guardian glide and **actual tested** ranged spell/unarmed projectile interactions with crystals and shields | Capstone techniques **excluding innate Creative Flight** |
| Post-Guardian ProjectE | Obey globally enforced ProjectE unlock regardless of Monk skills | Continued endgame investments |

A Monk must be capable of **completing** each fight and collecting all
relevant rewards without wielding conventional weapons or wearing armor.
Guardian shields/crystals require disposable-world integration testing:
high raw spell DPS alone may not satisfy the boss implementation.
Allow Draconic chestpiece in Curios; confirm module behavior there.

## Personal achievements vs team/world unlocks

Track real world milestone evidence from server events, not ownership of
loot or mere FTB Quests checkboxes. Distinguish world-tier access from
personal Monk achievement; decide how team participation, retroactive
defeats and boss assist credit work before enforcing a gate.

Proposed data: `compat/monk/profiles/world-tier-unlocks.json`.

**State:** no mining, flight or boss unlock handlers implemented yet.
