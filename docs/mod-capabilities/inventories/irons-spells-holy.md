# Iron's 3.16.3 — HOLY school spell source defaults

Exact [v1.21.1-3.16.3 registry](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/api/registry/SpellRegistry.java) and linked source classes; defaults before TMM spell configuration, rarity rolls, mana multipliers and player attributes. Mana cost is `base + perLevel × (level−1)` before config. Cooldown is seconds, cast time ticks. No assumption every spell is craftable at Scroll Forge.

| Spell ID | Class | Min rarity | Max level | Mana base +/level | CD s | Cast ticks |
| --- | --- | --- | ---: | --- | ---: | ---: |
| `irons_spellbooks:angel_wing` | [`AngelWingsSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/holy/AngelWingsSpell.java) | LEGENDARY | 5 | 80 + 20 | 120 | 0 |
| `irons_spellbooks:blessing_of_life` | [`BlessingOfLifeSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/holy/BlessingOfLifeSpell.java) | COMMON | 10 | 10 + 5 | 10 | 30 |
| `irons_spellbooks:cloud_of_regeneration` | [`CloudOfRegenerationSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/holy/CloudOfRegenerationSpell.java) | COMMON | 5 | 10 + 3 | 35 | 200 |
| `irons_spellbooks:fortify` | [`FortifySpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/holy/FortifySpell.java) | COMMON | 10 | 80 + 10 | 180 | 60 |
| `irons_spellbooks:greater_heal` | [`GreaterHealSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/holy/GreaterHealSpell.java) | RARE | 1 | 100 + 0 | 45 | 120 |
| `irons_spellbooks:guiding_bolt` | [`GuidingBoltSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/holy/GuidingBoltSpell.java) | COMMON | 10 | 20 + 5 | 8 | 0 |
| `irons_spellbooks:healing_circle` | [`HealingCircleSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/holy/HealingCircleSpell.java) | COMMON | 10 | 40 + 10 | 25 | 20 |
| `irons_spellbooks:heal` | [`HealSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/holy/HealSpell.java) | UNCOMMON | 8 | 30 + 15 | 30 | 0 |
| `irons_spellbooks:sunbeam` | [`SunbeamSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/holy/SunbeamSpell.java) | UNCOMMON | 10 | 40 + 10 | 20 | 0 |
| `irons_spellbooks:wisp` | [`WispSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/holy/WispSpell.java) | COMMON | 10 | 15 + 2 | 3 | 20 |
| `irons_spellbooks:divine_smite` | [`DivineSmiteSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/holy/DivineSmiteSpell.java) | COMMON | 5 | 30 + 15 | 15 | 16 |
| `irons_spellbooks:haste` | [`HasteSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/holy/HasteSpell.java) | EPIC | 4 | 50 + 10 | 80 | 30 |
| `irons_spellbooks:cleanse` | [`CleanseSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/holy/CleanseSpell.java) | EPIC | 1 | 100 + 0 | 60 | 60 |

**Source verification:** 13 active fields, 13 class sources, 13 actual spell resource IDs. — means unparsed/unspecified, not zero.
