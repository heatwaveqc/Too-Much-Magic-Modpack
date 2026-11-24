# Iron's 3.16.3 — NATURE school spell source defaults

Exact [v1.21.1-3.16.3 registry](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/api/registry/SpellRegistry.java) and linked source classes; defaults before TMM spell configuration, rarity rolls, mana multipliers and player attributes. Mana cost is `base + perLevel × (level−1)` before config. Cooldown is seconds, cast time ticks. No assumption every spell is craftable at Scroll Forge.

| Spell ID | Class | Min rarity | Max level | Mana base +/level | CD s | Cast ticks |
| --- | --- | --- | ---: | --- | ---: | ---: |
| `irons_spellbooks:acid_orb` | [`AcidOrbSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/nature/AcidOrbSpell.java) | COMMON | 8 | 40 + 10 | 15 | 15 |
| `irons_spellbooks:blight` | [`BlightSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/nature/BlightSpell.java) | RARE | 8 | 60 + 20 | 90 | 50 |
| `irons_spellbooks:poison_arrow` | [`PoisonArrowSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/nature/PoisonArrowSpell.java) | COMMON | 10 | 40 + 5 | 15 | 20 |
| `irons_spellbooks:poison_breath` | [`PoisonBreathSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/nature/PoisonBreathSpell.java) | COMMON | 10 | 5 + 1 | 12 | 100 |
| `irons_spellbooks:poison_splash` | [`PoisonSplashSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/nature/PoisonSplashSpell.java) | UNCOMMON | 10 | 40 + 10 | 20 | 15 |
| `irons_spellbooks:root` | [`RootSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/nature/RootSpell.java) | UNCOMMON | 10 | 45 + 3 | 35 | 50 |
| `irons_spellbooks:spider_aspect` | [`SpiderAspectSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/nature/SpiderAspectSpell.java) | RARE | 8 | 35 + 5 | 90 | 0 |
| `irons_spellbooks:firefly_swarm` | [`FireflySwarmSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/nature/FireflySwarmSpell.java) | UNCOMMON | 10 | 40 + 10 | 20 | 30 |
| `irons_spellbooks:oakskin` | [`OakskinSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/nature/OakskinSpell.java) | COMMON | 8 | 25 + 10 | 90 | 0 |
| `irons_spellbooks:earthquake` | [`EarthquakeSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/nature/EarthquakeSpell.java) | UNCOMMON | 10 | 50 + 10 | 16 | 40 |
| `irons_spellbooks:stomp` | [`StompSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/nature/StompSpell.java) | UNCOMMON | 5 | 50 + 10 | 16 | 10 |
| `irons_spellbooks:gluttony` | [`GluttonySpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/nature/GluttonySpell.java) | COMMON | 5 | 35 + 0 | 90 | 0 |
| `irons_spellbooks:touch_dig` | [`TouchDigSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/nature/TouchDigSpell.java) | RARE | 3 | 15 + 0 | 0.5 | 0 |

**Source verification:** 13 active fields, 13 class sources, 13 actual spell resource IDs. — means unparsed/unspecified, not zero.
