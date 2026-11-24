# Iron's 3.16.3 — LIGHTNING school spell source defaults

Exact [v1.21.1-3.16.3 registry](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/api/registry/SpellRegistry.java) and linked source classes; defaults before TMM spell configuration, rarity rolls, mana multipliers and player attributes. Mana cost is `base + perLevel × (level−1)` before config. Cooldown is seconds, cast time ticks. No assumption every spell is craftable at Scroll Forge.

| Spell ID | Class | Min rarity | Max level | Mana base +/level | CD s | Cast ticks |
| --- | --- | --- | ---: | --- | ---: | ---: |
| `irons_spellbooks:ascension` | [`AscensionSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/lightning/AscensionSpell.java) | RARE | 10 | 50 + 1 | 15 | 0 |
| `irons_spellbooks:chain_lightning` | [`ChainLightningSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/lightning/ChainLightningSpell.java) | UNCOMMON | 10 | 25 + 7 | 20 | 0 |
| `irons_spellbooks:charge` | [`ChargeSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/lightning/ChargeSpell.java) | RARE | 3 | 50 + 25 | 40 | 0 |
| `irons_spellbooks:electrocute` | [`ElectrocuteSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/lightning/ElectrocuteSpell.java) | COMMON | 10 | 3 + 1 | 12 | 100 |
| `irons_spellbooks:lightning_bolt` | [`LightningBoltSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/lightning/LightningBoltSpell.java) | EPIC | 10 | 75 + 15 | 25 | 0 |
| `irons_spellbooks:lightning_lance` | [`LightningLanceSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/lightning/LightningLanceSpell.java) | UNCOMMON | 10 | 50 + 10 | 8 | 40 |
| `irons_spellbooks:shockwave` | [`ShockwaveSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/lightning/ShockwaveSpell.java) | COMMON | 8 | 70 + 5 | 30 | 16 |
| `irons_spellbooks:thunderstorm` | [`ThunderstormSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/lightning/ThunderstormSpell.java) | RARE | 8 | 70 + 10 | 120 | 40 |
| `irons_spellbooks:ball_lightning` | [`BallLightningSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/lightning/BallLightningSpell.java) | COMMON | 10 | 20 + 4 | 1 | 0 |
| `irons_spellbooks:volt_strike` | [`VoltStrikeSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/lightning/VoltStrikeSpell.java) | COMMON | 10 | 30 + 5 | 10 | 0 |

**Source verification:** 10 active fields, 10 class sources, 10 actual spell resource IDs. — means unparsed/unspecified, not zero.
