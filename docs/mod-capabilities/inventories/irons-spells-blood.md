# Iron's 3.16.3 — BLOOD school spell source defaults

Exact [v1.21.1-3.16.3 SpellRegistry](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/api/registry/SpellRegistry.java) and linked class sources; values **before datapack/per-spell configuration and player multipliers**. Mana `base + perLevel × (level-1)` before server multiplier. Cooldown in seconds; cast time in ticks; rarity is minimum. No claim that every active spell is Scroll Forge craftable.

| Exact spell ID | Class | Minimum rarity | Max lvl | Mana base +/lvl | CD (s) | Cast ticks |
| --- | --- | --- | ---: | --- | ---: | ---: |
| `irons_spellbooks:acupuncture` | [`AcupunctureSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/blood/AcupunctureSpell.java) | RARE | 10 | 25 + 5 | 20 | 0 |
| `irons_spellbooks:blood_needles` | [`BloodNeedlesSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/blood/BloodNeedlesSpell.java) | UNCOMMON | 10 | 25 + 5 | 10 | 0 |
| `irons_spellbooks:blood_slash` | [`BloodSlashSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/blood/BloodSlashSpell.java) | RARE | 5 | 25 + 5 | 10 | 0 |
| `irons_spellbooks:blood_step` | [`BloodStepSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/blood/BloodStepSpell.java) | UNCOMMON | 5 | 30 + 10 | 12 | 0 |
| `irons_spellbooks:devour` | [`DevourSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/blood/DevourSpell.java) | UNCOMMON | 10 | 25 + 4 | 20 | 0 |
| `irons_spellbooks:heartstop` | [`HeartstopSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/blood/HeartstopSpell.java) | RARE | 5 | 100 + 10 | 120 | 0 |
| `irons_spellbooks:raise_dead` | [`RaiseDeadSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/blood/RaiseDeadSpell.java) | UNCOMMON | 6 | 50 + 10 | 150 | 30 |
| `irons_spellbooks:ray_of_siphoning` | [`RayOfSiphoningSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/blood/RayOfSiphoningSpell.java) | COMMON | 10 | 8 + 1 | 15 | 100 |
| `irons_spellbooks:wither_skull` | [`WitherSkullSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/blood/WitherSkullSpell.java) | UNCOMMON | 10 | 20 + 2 | 1 | 0 |
| `irons_spellbooks:sacrifice` | [`SacrificeSpell`](https://github.com/iron431/irons-spells-n-spellbooks/blob/v1.21.1-3.16.3/src/main/java/io/redspace/ironsspellbooks/spells/blood/SacrificeSpell.java) | RARE | 5 | 25 + 5 | 1 | 0 |

**Readback:** 10 registry fields, 10 source classes, 10 IDs. Absent fields are —, not zero. Version-pinned source facts, not live game acceptance.
