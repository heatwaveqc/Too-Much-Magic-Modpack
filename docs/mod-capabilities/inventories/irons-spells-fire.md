# Iron's 3.16.3 — FIRE school spell source defaults

Exact [v1.21.1-3.16.3 registry](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/api/registry/SpellRegistry.java) and linked source classes; defaults before TMM spell configuration, rarity rolls, mana multipliers and player attributes. Mana cost is `base + perLevel × (level−1)` before config. Cooldown is seconds, cast time ticks. No assumption every spell is craftable at Scroll Forge.

| Spell ID | Class | Min rarity | Max level | Mana base +/level | CD s | Cast ticks |
| --- | --- | --- | ---: | --- | ---: | ---: |
| `irons_spellbooks:blaze_storm` | [`BlazeStormSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/fire/BlazeStormSpell.java) | COMMON | 10 | 5 + 1 | 20 | 60 - 5 |
| `irons_spellbooks:burning_dash` | [`BurningDashSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/fire/BurningDashSpell.java) | COMMON | 10 | 20 + 2 | 10 | 0 |
| `irons_spellbooks:fireball` | [`FireballSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/fire/FireballSpell.java) | RARE | 5 | 60 + 15 | 25 | 40 |
| `irons_spellbooks:firebolt` | [`FireboltSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/fire/FireboltSpell.java) | COMMON | 10 | 10 + 2 | 1 | 0 |
| `irons_spellbooks:fire_breath` | [`FireBreathSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/fire/FireBreathSpell.java) | COMMON | 10 | 5 + 1 | 12 | 100 |
| `irons_spellbooks:magma_bomb` | [`MagmaBombSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/fire/MagmaBombSpell.java) | UNCOMMON | 8 | 30 + 5 | 12 | 20 |
| `irons_spellbooks:wall_of_fire` | [`WallOfFireSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/fire/WallOfFireSpell.java) | COMMON | 5 | 30 + 5 | 30 | 0 |
| `irons_spellbooks:heat_surge` | [`HeatSurgeSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/fire/HeatSurgeSpell.java) | COMMON | 6 | 50 + 10 | 45 | 20 |
| `irons_spellbooks:flaming_strike` | [`FlamingStrikeSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/fire/FlamingStrikeSpell.java) | COMMON | 5 | 30 + 15 | 15 | 10 |
| `irons_spellbooks:scorch` | [`ScorchSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/fire/ScorchSpell.java) | UNCOMMON | 10 | 50 + 5 | 12 | 20 |
| `irons_spellbooks:flaming_barrage` | [`FlamingBarrageSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/fire/FlamingBarrageSpell.java) | RARE | 5 | 80 + 5 | 15 | 0 |
| `irons_spellbooks:fire_arrow` | [`FireArrowSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/fire/FireArrowSpell.java) | RARE | 10 | 40 + 5 | 8 | 20 |
| `irons_spellbooks:raise_hell` | [`RaiseHellSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/fire/RaiseHellSpell.java) | LEGENDARY | 5 | 90 + 45 | 25 | 16 |

**Source verification:** 13 active fields, 13 class sources, 13 actual spell resource IDs. — means unparsed/unspecified, not zero.
