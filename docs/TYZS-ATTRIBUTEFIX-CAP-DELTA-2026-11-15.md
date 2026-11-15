# AttributeFix 21.1.3 and the TMM theoretical attribute ceiling

**2026-11-15 · current selection:** Minecraft **1.21.1 / NeoForge**, AttributeFix **21.1.3** on the `test/full-integration` next-build stream, CurseForge project **280510**, file **7115922**. Existing required dependencies **Bookshelf 21.1.81** and **Prickle 21.1.11** are already in the pack. Source: [AttributeFix's 1.21.1 `RangeConfig.java`](https://github.com/Darkhax-Minecraft/AttributeFix/blob/1.21.1/common/src/main/java/net/darkhax/attributefix/common/impl/config/RangeConfig.java), [initialization](https://github.com/Darkhax-Minecraft/AttributeFix/blob/1.21.1/common/src/main/java/net/darkhax/attributefix/common/impl/AttributeFixMod.java), and [pinned published file](https://www.curseforge.com/minecraft/mc-mods/attributefix/files/7115922).

## What actually changes by default

AttributeFix's **1.21.1** source enables `modify_range` by default for **exactly five** registered attributes. Every registered `RangedAttribute` (including eligible modded attributes) has a Prickle configuration entry, but the entries for other attributes default to **disabled**, thus retaining their pre-existing range.

| Attribute ID | Existing maximum | With default AttributeFix | Effect of larger engine bound |
|---|---:|---:|---|
| `minecraft:generic.max_health` | 1,024 | **1,000,000** | Permits huge health if some legal skill/equipment source actually grants it |
| `minecraft:generic.armor` | 30 | **1,000,000** | Allows armor stat over 30, **does not** remove the game's separate armor mitigation cap |
| `minecraft:generic.armor_toughness` | 20 | **1,000,000** | Allows more toughness stat; mitigation still computed by combat formula |
| `minecraft:generic.attack_damage` | 2,048 | **1,000,000** | Prevents raw damage attribute clipping at 2,048; other events/defense remain independent |
| `minecraft:generic.attack_knockback` | 5 | **1,000,000** | Raw knockback may become extreme; projectile/physics/effect and server safety must be tested |

**Unchanged by default:** `minecraft:generic.luck` (max **1,024**), movement speed (1,024), player mining-related ranged attributes, Apothic Attributes cooldown reduction (**0.95**), Apothic dodge (**1.0**) and mining multiplier (**10**), Iron's `max_mana` (**1,000,000**) / `spell_power` (**100**), Ars `ars_nouveau.perk.max_mana` (**10,000**) and Ars `ars_nouveau.perk.spell_damage` (**10,000**), and all other recognized modded ranged attributes. **AttributeFix doesn't add bonuses or change an enchantment/affix's magnitude by itself.**

The broader description of the project mentions Luck and movement speed, but its **specific 1.21.1 default source list** does **not** enable overrides for those two. Changing them would require explicitly enabling `modify_range` for their generated config files and selecting new limits. **We have not opted into that.**

## Three different numbers for every stat

1. **Engine limit:** maximum value its `Attribute` object will accept (five changed by AttributeFix, remaining source-defined max stays).
2. **Highest legal build output:** maximize that attribute over **all compatible specialist tree ranks + actual attainable armor/tools/Curios + enchanted and Iesnium-upgraded gear + Apotheosis mythic affixes, rare four sockets, legal Supremacy/Malice, Perfect gems + any source-specific buffs**. Mutually exclusive modifiers, item slots and source mechanics constrain the result. **No exact complete maxima have yet been proven.**
3. **Actual gameplay outcome:** e.g. armor uses a separate mitigation formula, effective damage has enemy mechanics, Luck only affects consumers that consult Luck, and cooldown/dodge have source-specific gameplay probability caps. Do not derive a naked boss-clear guarantee from a huge raw attribute cap.

## Monk 85% implication

**No numeric Monk per-rank targets change automatically.** For each comparable noncombat property (including Sea Dragon Luck), determine **X = highest LEGALLY ACHIEVABLE all-in specialist final output**, then set **Monk fully ranked + legal Curios final output = 0.85 × X** (Monk no conventional armor/weapons). If a cap rises but available gear/skill bonuses do not reach the old cap, then X and Monk's target **do not move**.

- **Luck:** still has native hard max 1,024. The only reason Monk Luck changes is if our legal fully-specced Sea Dragon + Curios + equipment optimizer discovers a different possible X, not because AttributeFix raises Luck. It does not by default.
- **Maximum health:** higher hard bound could make formerly clipped Dragon/Werewolf/Monk health sources register, but this is governed by combat/physical balance, not the universal **noncombat** 85% rule.
- **Armor, toughness, attack damage, knockback:** physics/combat and separate survival constraints apply; don't scale Monk to 85% of 1,000,000 merely because the registration cap changed.
- **Mana/spell power:** Iron/Ars values and their independent resource systems retain exactly the earlier source hard bounds by default.

## Implementation and QA status

Packwiz has the exact NeoForge 1.21.1 **21.1.3** file pinned from CurseForge file **7115922**, with a corroborated **SHA-1** `7aae849f6d2a6cb1368227407ea5deb552e100ba`. All packwiz index/root checksums are updated. **Bookshelf and Prickle were already present**; no extra copies added. That is only pack source metadata until a complete client/server launch and attribute registry check passes.

The [all-142 attribute machine ledger](tyzs/theoretical-attribute-hard-caps-v1.json) now preserves original `max`, adds `max_with_attributefix_21_1_3_defaults`, and labels the five overrides. `attainable_gear_plus_skills_max` remains **null** for every entry. We should verify the five modified limits via loaded NeoForge registry, check `modify_range` config files generated by Prickle, and test representative >old-limit equipment (notably max health and armor), as well as test knockback instability and server sync. For Luck, mana, spell power, cooldown and dodge, assert **no unexpected default changes**.

**Status: release/source verified and packwiz-authored, not game-smoke-tested.**
