# JAWR / Werewolves / Lich Food QA — pinned-build audit

**Decision:** Owner approved **Vampire's Delight 0.1.12b BETA** for the next **unstable** combined test only. This is not a blanket beta approval or a stable release.

## What we can actually inspect

- **JAWR 0.5.39+mc1.21.1**, Modrinth `LCO8KZvC`, pinned `TWcJbsjg`.
- **JAWR: Forbidden Magic 0.5.37+mc1.21.1**, Modrinth `pitZv4pV`, pinned `YgPMe2M7`.
- JAWR's public links point to [issues and wiki documentation](https://github.com/mrsterner/witchery-issues), not a publicly published current source tree. Do **not** infer food code from a different Witchery 1.12/1.20 project.
- Use `python3 scripts/audit_jawr_food_jar.py <pinned-ja wr.jar> <pinned-forbidden-magic.jar> --output <report-directory>` (replace placeholder filenames with their actual JAR paths). The next full-integration GitHub Actions job runs this read-only `javap -c -p` inventory on the exact downloaded JARs and retains the inventory and disassembly in **Full-Integration-Test-Evidence**. It is not automatic gameplay proof.
- If source-like control flow is needed, locally decompile the same **verified** JAR with Vineflower, then inspect `ItemStack` / `FoodData` / `FoodProperties` / `LivingEntityUseItemEvent` callers, player tick/event handlers, mixin configs and JAWR-specific lycan/lich state predicates. **Do not redistribute decompiled vendor source in the pack.**
- **Vampire's Delight** source is public at [TheGridExpert/VampiresDelight](https://github.com/TheGridExpert/VampiresDelight/tree/1.21). Its `MixinItem` overrides food properties and consumption for Vampirism-ecosystem factions and its `VDConfiguration` has `overrideFoodPropertiesForFactions=true` and `enableFDFoodEffectsDespiteFaction=false` by default. Those mixins warrant JAWR cross-mod regression tests; no claim that it specifically recognizes JAWR lycans.

## Controlled food tests (disposable world only)

Run one primary physiology at a time, then the specifically permitted unified forms separately. For every case: set or naturally deplete hunger, record HUD/hunger before, whether eating starts/completes, stack loss, effective hunger/saturation gain, exhaustion/regeneration, debuffs, and item effects. Rejoin and repeat to test persistence.

| State | Ordinary beef | Cooked beef | FD raw bacon | Aquaculture raw fish/fillet | Aquaculture cooked fillet | Sweet/glow berries | Bread | Vampire's Delight foods |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Human baseline | control | control | control | control | control | control | control | control |
| Werewolves addon, no **Not Meat?** | test | test | test | test | test | test | reject nonmeat | test werewolf components |
| Werewolves addon, with **Not Meat?** | test | test | test | test | test | test | test skill override | test |
| JAWR lycanthrope, human form | test | test | test | test | test | test | test | test |
| JAWR lycanthrope, wolf form | test | test | test | test | test | test | test | test |
| JAWR Forbidden Magic lich | test | test | test | test | test | test | test | test |
| Goety lich | test, note no hunger | test | test | test | test | test | test | test |
| Combined Werewolf, if bridge permits | verify owner | verify owner | verify owner | verify owner | verify owner | verify owner | verify owner | verify owner |
| Combined Archlich, if bridge permits | verify owner | verify owner | verify owner | verify owner | verify owner | verify owner | verify owner | verify owner |

Additional checks:
- Food **restoration** versus ability to **right-click/eat**. Something can be consumed and still give no ordinary nutrition, especially in undead states.
- Raw fish/fillet tags: Aquaculture's `#c:foods/raw_fish` is **not the same** as Werewolves' legacy `#c:rawmeats` and `#werewolves:raw_fish`; test every catchable Aquaculture fish, not only the known fillet.
- Werewolves' `customMeatItems` and `customRawMeatItems` config options can resolve missing modded meats. Prefer category/tag fixes over forcing a player to unlock **Not Meat?** to eat meat.
- Change one state at a time. For lycanthropy test **human**, **wolf** forms and post-relog behavior; for JAWR lich test phylactery and death/rejoin. Other transformations may introduce competing hunger hooks.
- Toggle Vampire's Delight `overrideFoodPropertiesForFactions` **in a disposable instance** if unexpected JAWR effects occur, then compare with default. Do not turn it off globally without evidence; it underpins the addon's intended faction foods.
- Check AppleSkin HUD and food tooltips to separate visual errors from server food mechanics.
- For Vampire's Delight, specifically test whether **Werewolves-addon** foods give faction-appropriate food properties, and verify no accidental feeding of JAWR vampires through Vampirism-only components.

## Release gates

1. Confirm exact Vampire's Delight **Modrinth project 9Vo2deFq / version oe9B4zXP**, NeoForge Minecraft 1.21.1 **beta** and required pre-installed Vampirism + Farmer's Delight; verify downloaded SHA-512 against provider before creating packwiz pin.
2. Clean dedicated-server boot with Vampires Delight installed; client loads world, has expected food tooltips, and can eat/regenerate.
3. Read generated JAWR/Forbidden Magic bytecode report for authoritative hook candidates, then run the table above.
4. Do not add a cross-lycan/lich hunger override without actual source/bytecode and reproducible gameplay observations.

**Status: next-test source added; JAR bytecode evidence and in-world results pending.**
