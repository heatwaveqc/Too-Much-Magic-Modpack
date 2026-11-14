# Scriptor Magicae — cooldown / lectern and craft-quality audit

**Decision date:** 2026-11-13 (Phoenix local). **Classification: Compatible with work.** Source reviewed; no Scriptor gameplay test, config change or nerf.

## Locked design choices

- Keep Scriptor **cooldown-based**; **do not install Scriptor Mana Mode** or convert Scriptor into Ars/Iron's or Psi mana.
- Explore Pufferfish **personal cooldown skills** and **permanently imprinted quality on written spellbooks / crafted lecterns**. Coefficients, event hooks, and effects are provisional.
- **Powerful automatic lecterns are intentional.** Audit their exact capabilities, but do not nerf, ban or redesign them just because resource automation is possible. Reconsider only a demonstrated breach of an already locked pack progression gate.

## Source and limitations

Pinned pack target: `scriptor-neoforge-2.0.14-1.21.1-sgd`. Main source reviewed at the September 17, 2026 snapshot [`6adb295`](https://github.com/ssblur/scriptor/tree/6adb295b15e5124319e7748fa7d167710cb0bad2) (close to release date); core actions and lectern also compared with October 21 `main` [`9419067`](https://github.com/ssblur/scriptor/tree/94190672efaec9d48948462ebd8aa12021541bc3). The stale `feature/2.0` branch (April 2026) is *not* reliable for the pinned release; its Bring, Inflame and custom recipe implementations differ. **Exact 2.0.14 JAR was not decompiled or in-world tested.** Source claims below must be verified in a disposable B6 world.

## The automatic lectern

[Lectern implementation](https://github.com/ssblur/scriptor/blob/6adb295b15e5124319e7748fa7d167710cb0bad2/common/src/main/kotlin/com/ssblur/scriptor/blockentity/CastingLecternBlockEntity.kt) stores one spellbook and optional Casting Crystal. **Unpowered redstone** enables repeated casting. Default maximum accepted spell **cost 25**; over-cost spells fizzle and wait **200 ticks** (default multiplier). Successful casts have `max(10, round(cost * 10))` tick cooldown at the default 100% lectern multiplier. No personal mana, FE, or consumable fuel is charged for casting. Targeting crystal requires same dimension and position **≤16 blocks** from lectern; subjects choose targets differently. `cooldown` and `focusTarget` fields appear not persisted in the block entity's ordinary save data: test unload/reload behavior.

### Answers: ore, smelting, mob drops

| Capability | Result from source | What actually happens |
|---|---|---|
| **Conjure ore blocks ex nihilo** | **No stock effect found** | [`Place`](https://github.com/ssblur/scriptor/blob/6adb295b15e5124319e7748fa7d167710cb0bad2/common/src/main/kotlin/com/ssblur/scriptor/word/action/PlaceBlockAction.kt) puts down an existing caster-supplied `BlockItem` (consumes it); without one, conjures a colored *magic block*, **not** iron/diamond/modded ore. Lectern caster's adjacent-container access appears broken, making existing block-item placement from its chest doubtful. |
| **Break existing ore** | **Yes** | [`Break`](https://github.com/ssblur/scriptor/blob/6adb295b15e5124319e7748fa7d167710cb0bad2/common/src/main/kotlin/com/ssblur/scriptor/word/action/BreakBlockAction.kt) uses `level.destroyBlock` with tool-strength-tag checks; stronger word descriptors can mine higher-tier blocks. Drops depend on block and environment; no ore is created. Base `Touch (1) + Break (1)` costs **2**, minimum **20 ticks / 1 s** per cast; higher strength increases spell cost. Check unusual modded ore tags and claim protections. |
| **Smelt items without fuel** | **Yes** | [`Inflame`](https://github.com/ssblur/scriptor/blob/6adb295b15e5124319e7748fa7d167710cb0bad2/common/src/main/kotlin/com/ssblur/scriptor/word/action/InflameAction.kt) uses `RecipeType.SMELTING` to consume input and deposit furnace recipe outputs; stronger descriptors can run multiple iterations in one cast. A chest in front of a lectern with `Touch (1) + Inventory (0) + Inflame (2)` is a concrete candidate: **cost 3**, ~**30 ticks / 1.5 s** per pass, **40 passes/minute** at default settings, provided actual target and storage semantics are confirmed. It does **not** mean unrestricted arbitrary ingredient transmutation. |
| **Collect mob drops** | **Yes, relocate them; direct chest deposit questionable** | [`Nearby Item`](https://github.com/ssblur/scriptor/blob/6adb295b15e5124319e7748fa7d167710cb0bad2/common/src/main/kotlin/com/ssblur/scriptor/word/descriptor/target/NearbyItemDescriptor.kt) selects item entities; [`Bring` / `Swap`](https://github.com/ssblur/scriptor/blob/6adb295b15e5124319e7748fa7d167710cb0bad2/common/src/main/kotlin/com/ssblur/scriptor/word/action/teleport/SwapAction.kt) transfers item *stacks* toward caster's location. `Self (1) + Nearby Item (0.05) + Bring (6)` costs **7.05**, ~**71 ticks / 3.55 s**, likely relocating all targets in a small item-entity pickup area per cast; combine with a coordinate crystal (≤16 blocks) and **vanilla physical hopper / water collector**. Actual delivery into a chest is *not* established. |
| **Kill mobs and harvest normal loot** | **Yes** | `Harm`, `Smite`, `Inflame`, `Explosion` and `Evoker Fangs` can damage mobs with appropriate subjects. `Touch (1) + Harm (3)` costs **4**, ~**40 ticks / 2 s**. Do not assume lectern kills count as *player kills*: experience, Looting and player-only drops need tests. Loot can be transported independently with `Bring`. |

### More useful or dangerous automations

- `Conjure Water` (1.5) and `Dry` (0.6) manipulate liquids; `Place` (1.5) makes magic blocks when no item focus available; `Write` (0.2) makes chalk; `Color` (8) changes supported items/blocks/entities.
- `Heal` (4) heals entities and **repairs damageable items**; regeneration, haste, speed, resistance, night vision, invisibility and other PotionActions make buff stations.
- Conjured food and bound tools carry expiry-time data components; **not permanently generated gear/food** by default.
- `Bring` (6), `Go To` (6), `Swap` (6) move items/living entities; range and world permissions matter.
- `Advance Time` (52), `Rain` (51), and `Clear Weather` (51) exceed base lectern limit 25, **but `Unreliable` ×0.3** and other cost-reducing descriptors may make them castable with a failure chance. Don't mistake the base cost cap for a categorical block against all world-scale spells.
- Stock Scriptor `CastRecipes` data includes examples such as ink→glow ink and arrow→spectral arrow, but the Sept/Oct `Spell.castOnTargets` transformation code is **commented out**. Do not count these recipe transformations as enabled without checking pinned jar. This is distinct from working furnace-recipe smelting in `Inflame`.

## Significant source defect: lectern caster-inventory access

[`LecternTargetable.kt`](https://github.com/ssblur/scriptor/blob/6adb295b15e5124319e7748fa7d167710cb0bad2/common/src/main/kotlin/com/ssblur/scriptor/helpers/targetable/LecternTargetable.kt) initializes `override val container: Container? = null`; its custom getter checks if a chest is below/behind but then returns **`field`**, which remains null. In both the pinned-era public source and Oct main, this likely prevents **using an adjacent chest as the *caster's* inventory** (fuel/reagents/real placed blocks or direct loot deposit) even if the block is positioned correctly.

This does **not** prevent an `InventoryDescriptor` turning a *targeted chest* into a `ContainerTargetable` for operations like `Inflame`. Nor does it prevent `Bring` from spawning item entities at the lectern for external collection. `ItemEntityTargetable.container` creates a fresh `SimpleContainer` for ground items; check that direct ground-item smelting/deposition doesn't swallow output.

**No third-party fix or work-around was applied.** Confirm with actual pinned binary and test world before proposing an upstream report or patch.

## Craftsmanship / Pufferfish integration proposals (not yet implemented)

1. **Personal cooldown skills:** Hook `SpellbookHelper.castFromItem` and/or `EntityCastCooldownExtension` cooldown setter for Pufferfish-derived, bounded personal cooldown scaling. Stock casting cooldown uses a private player `WeakHashMap`, **not a registered player attribute**. Distinguish books, scrolls, spoken spells, inventory enchantments and on-hit casts; avoid double application.
2. **Written spellbook quality:** On validated creation/signing or first legitimate book pickup, imprint persistent item metadata giving scoped cooldown, potency or complex-book benefits. Cast-side bridge reads item's quality; preserve world-specific randomized language and written spell grammar. Verify copying/cloning, found books, trades and non-player equipment.
3. **Lectern craftsmanship:** Quality set at *craft/placement* (not bound to an online/offline owner's ongoing skill). Possible benefits: reduced cooldown multiplier per machine or an increased maximum accepted book cost. Stock config values are **global**, while block entity only serializes 2 item stacks; a true per-block upgrade requires persisted metadata/attachment and a narrow NeoForge bridge or mixin. Avoid silently changing all lecterns at once.
4. No user-approved numerical tiers yet; do not add live mod files, player scripts, gameplay changes or nerfs based solely on this source audit.

## Disposable-world tests (next)

1. `Place` into air with no input; with ore block item stored in a chest below/behind; confirm conjured block type and real item consumption.
2. `Touch + Break` stone, common ores and a late-tech ore; inspect loot, harvesting strength and permissions.
3. `Touch + Inventory + Inflame` on a front chest with iron ore and an available output slot; count items per cast; test high strength, all furnace recipes and full inventory.
4. `Self + Nearby Item + Bring` with a coordinate focus and staged drops near remote target; collect at lectern into vanilla hopper/water machine; check entire stack and chest-getter issue.
5. Mob-farm source with `Touch + Harm`: ordinary vs player-kill drops, XP and owner attribution; note damage source could use the target itself when the caster is not a player entity.
6. Healing/repair, expired conjured items, fluid operations, high-cost time/weather via discounts, redstone on/off, unload/reload, and multiple lecterns for TPS.
7. Separate quality-imprint pilot only after agreeing to one book and one lectern perk; validate persistence, respec, cloning and UI visibility.
