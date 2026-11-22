# Iron's 3.16.3 — ENDER school spell source defaults

Exact [v1.21.1-3.16.3 registry](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/api/registry/SpellRegistry.java) and linked source classes; defaults before TMM spell configuration, rarity rolls, mana multipliers and player attributes. Mana cost is `base + perLevel × (level−1)` before config. Cooldown is seconds, cast time ticks. No assumption every spell is craftable at Scroll Forge.

| Spell ID | Class | Min rarity | Max level | Mana base +/level | CD s | Cast ticks |
| --- | --- | --- | ---: | --- | ---: | ---: |
| `irons_spellbooks:counterspell` | [`CounterspellSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ender/CounterspellSpell.java) | RARE | 1 | 50 + 1 | 10 | 0 |
| `irons_spellbooks:dragon_breath` | [`DragonBreathSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ender/DragonBreathSpell.java) | COMMON | 10 | 5 + 1 | 12 | 100 |
| `irons_spellbooks:evasion` | [`EvasionSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ender/EvasionSpell.java) | EPIC | 5 | 40 + 20 | 180 | 0 |
| `irons_spellbooks:magic_arrow` | [`MagicArrowSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ender/MagicArrowSpell.java) | RARE | 10 | 40 + 5 | 8 | 30 |
| `irons_spellbooks:magic_missile` | [`MagicMissileSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ender/MagicMissileSpell.java) | COMMON | 10 | 10 + 2 | 1 | 0 |
| `irons_spellbooks:starfall` | [`StarfallSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ender/StarfallSpell.java) | UNCOMMON | 10 | 5 + 1 | 16 | 160 |
| `irons_spellbooks:teleport` | [`TeleportSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ender/TeleportSpell.java) | UNCOMMON | 5 | 20 + 5 | 6 | 0 |
| `irons_spellbooks:summon_ender_chest` | [`SummonEnderChestSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ender/SummonEnderChestSpell.java) | RARE | 1 | 25 + 1 | 5 | 0 |
| `irons_spellbooks:recall` | [`RecallSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ender/RecallSpell.java) | UNCOMMON | 1 | 100 + 1 | 300 | 80 |
| `irons_spellbooks:portal` | [`PortalSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ender/PortalSpell.java) | UNCOMMON | 3 | 200 + 10 | 180 | 0 |
| `irons_spellbooks:echoing_strikes` | [`EchoingStrikesSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ender/EchoingStrikesSpell.java) | RARE | 5 | 50 + 10 | 60 | 0 |
| `irons_spellbooks:black_hole` | [`BlackHoleSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ender/BlackHoleSpell.java) | LEGENDARY | 6 | 300 + 100 | 120 | 100 |
| `irons_spellbooks:summon_swords` | [`SummonSwordsSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ender/SummonSwordsSpell.java) | RARE | 5 | 80 + 15 | 150 | 20 |
| `irons_spellbooks:shadow_slash` | [`ShadowSlashSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ender/ShadowSlashSpell.java) | COMMON | 5 | 30 + 15 | 15 | 0 |
| `irons_spellbooks:arcane_shackle` | [`ArcaneShackleSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ender/ArcaneShackleSpell.java) | RARE | 8 | 40 + 8 | 45 | 10 |
| `irons_spellbooks:gravity_fissure` | [`GravityFissureSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/ender/GravityFissureSpell.java) | EPIC | 5 | 150 + 25 | 45 | 15 |

**Source verification:** 16 active fields, 16 class sources, 16 actual spell resource IDs. — means unparsed/unspecified, not zero.
