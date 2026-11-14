# Monk trait candidates — source scan (2026-11-14)

**Status:** source-backed candidate inventory; **not** a live player registry
dump, not yet purchasable, and not a claim that every candidate is usable
by a player. The pinned modpack is Minecraft 1.21.1 NeoForge.

Inventory: `compat/monk/profiles/attribute-candidates-1.21.1.json`
and `non-attribute-trait-candidates.json`.

## 142 source-based candidate attribute IDs

| Origin | Count | Evidence notes |
| --- | ---: | --- |
| `minecraft` | 30 | minecraft 1.21.1 registry no runtime |
| `apothic_attributes` | 22 | upstream 1.21 branch pinned 2.11.0 unverified binary |
| `irons_spellbooks` | 26 | upstream 1.21 branch gradle 3.16.3 exact |
| `ars_nouveau` | 9 | upstream main not pinned 5.13.5 |
| `malum` | 15 | upstream 1.21.1 branch current 1.9.0 not pinned 1.8.2 |
| `goety` | 27 | upstream main gradle 3.2.2 exact |
| `eidolon_repraised` | 4 | upstream 1.21.1 branch gradle 0.5.0.8 exact |
| `forbidden_arcanus` | 2 | upstream 1.21.1 branch pinned 2.6.1 unverified |
| `neovitae` | 7 | published api reference not pinned binary 1.1.53 |
| **Total** | **142** | May include mob/equipment-only entries and omit other registered attributes |

- Apothic: 22 custom attributes, including boolean Elytra Flight and
  capped Cooldown Reduction (0.95). These are not all uncapped.
- Iron's: 8 global caster stats plus 9 spell-school power and 9 school
  resistance attributes, 26 total. Exact source branch declares pinned
  3.16.3.
- Ars: 9 stats in PerkAttributes (mana, mana regen, damage, warding,
  fall, magical familiars and satiety). Source on `main` may differ
  from pinned 5.13.5; especially check raw names containing dots.
- Malum: 15 source attributes (ward, Geas, charge, soul effects).
  **1.21.1 source branch currently says 1.9.0, while TMM pins 1.8.2**;
  runtime reconcile before mapping. Not every stat is appropriate for
  unarmed or non-ritual use.
- Goety: 27 source attributes: 9 global effects and potency+discount
  for nine schools. Source branch identifies version 3.2.2.
- Eidolon: four source attributes, including one deprecated; branch
  identifies pinned 0.5.0.8.
- Forbidden & Arcanus: two source stats, Aureal regen and magic
  protection.
- Neo Vitae: seven published API player attributes (module wiki,
  not the pinned binary) including self-sacrifice, EV efficiency,
  Spiritus, Sigil costs, Blood Siphon and Blood Shield.
- Vanilla: 30 IDs in 1.21.1 registry after excluding the zombie-only
  ID; not all are attached or meaningful on Player.

## Coverage boundaries

This does **not** yet include the complete installed custom attribute
registry: active attributes added by other mods/addons, dynamic data
attributes and mod-specific non-Attribute progression are possible.
A registry scan against the complete pinned client+server is required.

The addon now provides an *operator-only diagnostic*:
`/tmmmonk attributes`. Run it as a player with OP level 2 on an
integrated or dedicated test world; it exports the actual registered
attributes with instances on that player (ID, default, current, bounds)
to `<world>/tmm-monk-player-attributes.json`. This does NOT alter
the player's attributes, rank or inventory. Compare its rows against
the static candidate JSON. Until run, no runtime coverage percentage
should be reported.

### Important gating choices

- `minecraft:generic.attack_speed`: bounded by effective combat tick
  cadence; cannot simply be endlessly improved without effect.
- `apothic_attributes:elytra_flight`: binary permission to glide, **not**
  momentum thrust or creative flight.
- `apothic_attributes:cooldown_reduction`: 0.95 native hard maximum.
- Goety casting discount and resource discounts: effective 100%
  discount or faster-than-tick casting need guards even if native
  registry tolerates larger values.
- Warding, magic damage and mana stats often permit thousands of
  native points, but high performance and effect overlap require testing.
- Ranged/projectile equipment stats need Monk-created projectile
  actions; they should not become free unused purchases.
- Malum scythe proficiency needs unarmed equivalent or disabled
  purchase until an explicit analogue exists.
- Distinguish `MAX_MANA`, Mahou personal mana, Ars mana, Soul Energy
  and Neo Vitae EV; they are **different resources** and may not share
  a modifier.

## Sources / provenance

- `vanilla`: https://raw.githubusercontent.com/PixiGeko/Minecraft-generated-data/master/1.21/releases/1.21.1/custom-generated/registries/attribute.txt
- `apothic`: https://raw.githubusercontent.com/Shadows-of-Fire/Apothic-Attributes/1.21/src/main/java/dev/shadowsoffire/apothic_attributes/api/ALObjects.java
- `iron`: https://raw.githubusercontent.com/iron431/irons-spells-n-spellbooks/1.21/src/main/java/io/redspace/ironsspellbooks/api/registry/AttributeRegistry.java
- `ars`: https://raw.githubusercontent.com/baileyholl/Ars-Nouveau/main/src/main/java/com/hollingsworth/arsnouveau/api/perk/PerkAttributes.java
- `malum`: https://raw.githubusercontent.com/SammySemicolon/Malum-Mod/1.21.1/src/main/java/com/sammy/malum/registry/common/MalumAttributes.java
- `goety`: https://raw.githubusercontent.com/Vivideru/Goety-3/main/src/main/java/com/Polarice3/Goety/init/ModAttributes.java
- `eidolon`: https://raw.githubusercontent.com/Alexthw46/Eidolon-Repraised/1.21.1/src/main/java/alexthw/eidolon_repraised/registries/EidolonAttributes.java
- `forbidden`: https://raw.githubusercontent.com/stal111/Forbidden-Arcanus/1.21.1/neoforge/src/main/java/com/stal111/forbidden_arcanus/common/entity/attribute/FAAttributes.java
- Neo Vitae API: https://github-wiki-see.page/m/breakinblocks/NeoVitae/wiki/API-Reference

**Status rule:** source inventory ≠ game-attached attributes ≠ usable
unarmed abilities ≠ implemented Monk skills. The full affix inventory
(99 entries) is tracked separately and every affix still requires a
Monk effect adapter.

## Original Monk 1.4 abilities — 25/25 crosswalk

The original RWTema Monk mod registers **25 configurable ability types**,
not just the powers chosen by its default rank progression. All 25 have
an explicit classification in
`compat/monk/profiles/original-monk-abilities-1.4.json`.

**Already represented by candidate numeric attributes (7):** armor, armor
toughness, increased max health, jump power, movement speed ("swift"),
step height and attack damage ("strength"). Note original jump behavior
also modified movement/fall handling, so numeric overlap alone does not
prove full parity.

**Requires event or special-gameplay handling (16):** Blink, catching
arrows, Creeper Kiss, explosion resistance, feather fall, Glass Touch
(silk-like glass harvesting), potion cleansing/immunity, hunger
efficiency, **split harvest tier/speed** (mining), fire resistance, lava
protection, animal taming/friendship, water walking, partial underwater
breathing, full underwater breathing and Withering Stare.

**Historical but not a positive TMM unlock (2):** temporary imposed
blindness was part of the default Monk trial; Creative Flight was the
original rank-20 award, but is **removed from the TMM skill tree** and
left to an allowed Draconic Evolution Curio module.

The factory registers four abilities **not selected in the original
default progression**: Armor Toughness, Health, Glass Touch, and Full
Water Breathing. All four are included, with clear defaults vs optional
historical markers. The source also contains a FastDraw class that is
not registered in the factory; it is not counted among the 25.

These 25 are **non-additive to the 142 numeric source candidates**:
some overlap by design. Their inventory status is "mapped, not
implemented," not "available for player purchase." Test coverage
guards against silent omission.

Sources:
- https://github.com/rwtema/Monk-Mod/blob/master/src/main/java/com/rwtema/monkmod/factory/Factory.java
- https://github.com/rwtema/Monk-Mod/blob/master/src/main/java/com/rwtema/monkmod/config/MonkConfiguration.java

### Mining clarity

`mining` in original Monk already contained **both**
`harvest_level` and `speed_increase`. In TMM these become **two
independent investments**: a finite progression of valid barehand
harvest tiers and open-ended (effective engine limits respected)
barehand mining speed. Compare Dragon Survival's similarly separate
`harvest_bonus` and `break_speed_multiplier` source mechanics.
