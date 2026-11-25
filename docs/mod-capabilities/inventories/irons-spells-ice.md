# Iron's 3.16.3 — ICE school spell source defaults

Exact [v1.21.1-3.16.3 registry](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/api/registry/SpellRegistry.java) and linked source classes; defaults before TMM spell configuration, rarity rolls, mana multipliers and player attributes. Mana cost is `base + perLevel × (level−1)` before config. Cooldown is seconds, cast time ticks. No assumption every spell is craftable at Scroll Forge.

| Spell ID | Class | Min rarity | Max level | Mana base +/level | CD s | Cast ticks |
| --- | --- | --- | ---: | --- | ---: | ---: |
| `irons_spellbooks:cone_of_cold` | [`ConeOfColdSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ice/ConeOfColdSpell.java) | COMMON | 10 | 5 + 1 | 12 | 100 |
| `irons_spellbooks:frost_step` | [`FrostStepSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ice/FrostStepSpell.java) | RARE | 8 | 15 + 5 | 12 | 0 |
| `irons_spellbooks:ice_block` | [`IceBlockSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ice/IceBlockSpell.java) | RARE | 6 | 40 + 10 | 15 | 25 |
| `irons_spellbooks:icicle` | [`IcicleSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ice/IcicleSpell.java) | COMMON | 10 | 10 + 2 | 1 | 0 |
| `irons_spellbooks:summon_polar_bear` | [`SummonPolarBearSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ice/SummonPolarBearSpell.java) | RARE | 10 | 50 + 10 | 180 | 20 |
| `irons_spellbooks:ray_of_frost` | [`RayOfFrostSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ice/RayOfFrostSpell.java) | COMMON | 5 | 25 + 15 | 15 | 0 |
| `irons_spellbooks:frostwave` | [`FrostwaveSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ice/FrostwaveSpell.java) | COMMON | 8 | 50 + 5 | 45 | 20 |
| `irons_spellbooks:ice_spikes` | [`IceSpikesSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ice/IceSpikesSpell.java) | COMMON | 10 | 30 + 10 | 15 | 0 |
| `irons_spellbooks:ice_tomb` | [`IceTombSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ice/IceTombSpell.java) | UNCOMMON | 8 | 30 + 15 | 30 | 0 |
| `irons_spellbooks:snowball` | [`SnowballSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ice/SnowballSpell.java) | UNCOMMON | 5 | 40 + 2 | 12 | 20 |
| `irons_spellbooks:frostbite` | [`FrostbiteSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ice/FrostbiteSpell.java) | EPIC | 5 | 80 + 10 | 60 | 0 |
| `irons_spellbooks:blizzard` | [`BlizzardSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ice/BlizzardSpell.java) | RARE | 8 | 40 + 10 | 22 | 25 |

**Source verification:** 12 active fields, 12 class sources, 12 actual spell resource IDs. — means unparsed/unspecified, not zero.
