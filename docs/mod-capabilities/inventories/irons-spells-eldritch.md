# Iron's 3.16.3 — ELDRITCH school (SpellRegistry group VOID) source defaults

Exact [v1.21.1-3.16.3 registry](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/api/registry/SpellRegistry.java) and linked source classes; defaults before TMM spell configuration, rarity rolls, mana multipliers and player attributes. Mana cost is `base + perLevel × (level−1)` before config. Cooldown is seconds, cast time ticks. **SpellRegistry's comment label VOID is not the actual ninth school ID; SchoolRegistry uses `irons_spellbooks:eldritch`.** No assumption every spell is craftable at Scroll Forge.

| Spell ID | Class | Min rarity | Max level | Mana base +/level | CD s | Cast ticks |
| --- | --- | --- | ---: | --- | ---: | ---: |
| `irons_spellbooks:abyssal_shroud` | [`AbyssalShroudSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/eldritch/AbyssalShroudSpell.java) | LEGENDARY | 3 | 300 + 20 | 200 | 0 |
| `irons_spellbooks:sculk_tentacles` | [`SculkTentaclesSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/eldritch/SculkTentaclesSpell.java) | LEGENDARY | 4 | 150 + 50 | 30 | 20 |
| `irons_spellbooks:sonic_boom` | [`SonicBoomSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/eldritch/SonicBoomSpell.java) | LEGENDARY | 3 | 110 + 50 | 25 | 30 |
| `irons_spellbooks:planar_sight` | [`PlanarSightSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/eldritch/PlanarSightSpell.java) | LEGENDARY | 3 | 150 + 50 | 200 | 0 |
| `irons_spellbooks:telekinesis` | [`TelekinesisSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/eldritch/TelekinesisSpell.java) | LEGENDARY | 5 | 25 + 0 | 35 | 140 |
| `irons_spellbooks:eldritch_blast` | [`EldritchBlastSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/eldritch/EldritchBlastSpell.java) | LEGENDARY | 5 | 90 + 15 | 15 | 0 |
| `irons_spellbooks:pocket_dimension` | [`PocketDimensionSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/eldritch/PocketDimensionSpell.java) | LEGENDARY | 1 | 300 + 0 | 60 | 40 |

**Source verification:** 7 active fields, 7 class sources, 7 actual spell resource IDs. — means unparsed/unspecified, not zero.
