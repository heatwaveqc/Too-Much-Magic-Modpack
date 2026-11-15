# ProjectE and Project Expansion — boss-trophy recipe progression

**Owner decision, 2026-11-14:** use **interchangeable boss-associated drops** as the *middle ingredient* of the Philosopher's Stone; keep the other eight ingredients and both orientations. Add a **Draconic Chaos Shard** to the **third (Red/MK3) Power Flower** recipe and close higher-tier direct-recipe bypasses.

**Current status:** a KubeJS implementation exists on `feature/projecte-boss-trophy-stone-and-emc-audit` and is registered for the rolling full integration. **Source/contract checked only until a full KubeJS server and Prism client test confirms it loads.** These are *ingredient/recipe* gates, **not boss-kill progression flags**. Trading, team sharing and later EMC duplication of an acquired drop are allowed.

## Philosopher's Stone — original outer ring, interchangeable center

ProjectE PE1.1.0 has TWO recipes:

```text
R G R       G R G
G D G       R D R
R G R       G R G
```

`R` is redstone dust (`#c:dusts/redstone`), `G` is glowstone dust (`#c:dusts/glowstone`), and `D` was a diamond.

**The only change:** `D` becomes one of these three center items, in either orientation.

| Magic path | Accepted item ID | Gameplay provenance |
| --- | --- | --- |
| Ars Nouveau Wilden Chimera | `ars_nouveau:wilden_tribute` | True Chimera boss drop, also needed for Archmage progression |
| Botania Revived Gaia Guardian | `botania:life_essence` | Gaia Spirit; name **is not** `botania:gaia_spirit` |
| Neo Vitae Foreman / Mines | `neovitae:mine_key` | Mine Dungeon Key. **Also obtainable from Mines trial spawners** in the upstream version, so this path is **not an exclusive Foreman kill proof** |

The three values live in `kubejs/data/tmm/tags/item/philosophers_stone_boss_trophies.json`, and both original ProjectE recipe IDs are replaced with KubeJS shaped variants. Adding another verified boss drop only requires adding its exact item registry ID to that tag. **Do not use Wilden horns, wings or spikes**; those come from regular Wilden, not the Chimera. Avoid adding a boss item available through easy crafting, loot or the Quest Shop unintentionally.

This recipe policy intentionally removes the old requirement for **server-side personal defeat credit**. It allows a boss-killer to trade a trophy to somebody who specializes in another school. That is a design feature, not a bypass.

## Power Flowers: Chaos Shard at MK3

Project Expansion 1.0.6 defines sequential tiers **Basic MK1**, **Dark MK2**, **Red MK3**, then Magenta, Pink, Purple, Violet, Blue, Cyan, Green, Lime, Yellow, Orange, White, Fading and Final. Third is **`projectexpansion:red_power_flower`**, not a generic `tier_3_power_flower` item.

The direct recipe uses two `<tier>_compressed_collector`, one `<tier>_emc_link`, and six `<tier>_relay`. For every **Red-or-higher direct recipe** we replace one relay with `draconicevolution:chaos_shard`:

```text
C L C
R R R
R S R
```

The **Red upgrade** originally uses Dark Power Flower in the center. We preserve that center and instead swap one lower relay to a Chaos Shard:

```text
C L C
R P R
R S R
```

`P` is `projectexpansion:dark_power_flower`. The other higher-tier upgrade recipes still require the preceding Power Flower and retain their own original ingredients. Every direct MK3+ recipe also requires the shard, so crafting a higher tier from scratch cannot evade the entry milestone. The two remaining early flowers can be explored before Chaos if other recipes permit.

**Boundary:** Project Expansion's `ItemMatterUpgrader` code can convert already placed flowers or collectors using learned EMC without invoking a crafting recipe. ProjectE transmutation/knowledge may also synthesize learned flowers or shards. The KubeJS change **does not block those routes**, nor does it prevent admin grants, loot or trades. Test whether the Matter Upgrader is survival-obtainable; if it makes the intended MK3 threshold trivial, add a separate targeted guard instead of claiming these recipes are a universal use lock.

## Universal EMC coverage — additional owner goal

**Target 100%:** every registered item in our actual 1.21.1 NeoForge modpack has an EMC value **greater than zero**, with an explicitly approved short list of cheap exceptions such as **glass panes**. This applies to all installed mods, new mods and variants—not merely vanilla items or objects with normal recipes.

Source-of-truth policy: `manifest/emc-coverage-policy.json`.

Audit workflow:

1. Export **every loaded item registry ID** from the same immutable build (not a guessed mod-list approximation) and ProjectE's **post-mapping** EMC values. The custom override file alone is not a complete map.
2. Run `python3 scripts/audit_emc_coverage.py --items path/to/items.json --emc path/to/final-emc.json --output emc-audit.json --enforce`.
3. ProjectE's native recipe/conversion mappers already assign many values. Give manually researched, positive base values to **every remaining missing root item** using ProjectE's native `custom_emc.json` mechanism. Do **not** install `KubeJS ProjectE` just because old documentation shows an event—it has published Forge builds for 1.19.2, not our 1.21.1 NeoForge target.
4. Re-export the *actual resulting map*, rerun the audit until **zero non-exception misses**, and retain the report as build evidence. Future mod additions must pass the same coverage gate.
5. Run profitable-cycle tests: dust/ore processing, Fortune/Silk Touch, item disassembly, reversible recipes, Quest Shop sales, generated matter, repaired/damaged items, fluid/energy-filled items, and NBT/data-bearing inventory items. Assigning a positive EMC value can enable **synthesis and duplication**; this is especially relevant to boss trophies, Chaos Shards, spawn eggs, relics, phylacteries, filled storage and non-stackable items.
6. Existing expensive Project Expansion items may exceed ProjectE's normal signed-long EMC value bounds; resolve rather than silently setting them to zero. Any extra exception beyond the cheap-item allowlist requires a new owner decision.

The audit script does **not** invent values or silently default missing items to 1. It fails `--enforce` unless every non-excepted item is backed by a positive *observed* value. Only glass panes and the 16 stained variants are initially exempted in the policy. If the owner prefers exact one-item exceptions instead, narrow the list before setting EMC maps.

## Next client QA — essential

- [ ] Both Stone orientations disappear with diamond and appear with Wilden Tribute, Gaia Spirit or Mine Dungeon Key as center.
- [ ] One trophy is consumed, eight original outer ingredients still required, correct output.
- [ ] All boss trophy IDs actually exist in the pinned 1.21.1 registry.
- [ ] No duplicate old Stone recipe remains in JEI or alternate crafting tables.
- [ ] Basic (MK1), Dark (MK2) original flower recipes remain; Red MK3 direct and Red upgrade **both** require Chaos Shard.
- [ ] All higher **direct** Power Flower routes require Chaos Shard; later upgrades retain proper preceding flower dependency.
- [ ] Test knowledge-table EMC recreation and Project Expansion Matter Upgrader for unintended bypass/EMC loops.
- [ ] At least one in-world test for each boss-associated center plus two denied cheap-diamond and zero-shard test crafts.
- [ ] Confirm the exact same final item values on client and dedicated server after complete EMC audit; no exclusions beyond reviewed list.

**Evidence hierarchy:** JS-source/fixture tests → full dedicated server including KubeJS scripts with zero recipe errors → Prism JEI and in-world craft tests → multiplayer/conversion and economy acceptance. Never upgrade status directly from source tests.

### Verified upstream sources

- [ProjectE two original Stone recipes](https://github.com/sinkillerj/ProjectE/tree/mc1.21.1/src/datagen/generated/data/projecte/recipe).
- [Ars Nouveau Wilden Tribute registry](https://github.com/baileyholl/Ars-Nouveau/blob/main/src/main/java/com/hollingsworth/arsnouveau/setup/registry/ItemsRegistry.java).
- [Botania Revived `life_essence` registry](https://github.com/Velron-Devs/Botania-Revived/blob/main/src/main/java/velrondevs/botania/registry/BotaniaItems.java).
- [Neo Vitae Mines / Foreman and trial-spawner key](https://github.com/breakinblocks/NeoVitae/blob/1.21.1/wiki/The-Endless-Dungeon.md).
- [Project Expansion Power Flower recipes and generated tiers](https://github.com/DonovanDMC/ProjectExpansion/tree/1.21.1/src/main/generation/generators/recipe).
- [ProjectE native custom EMC parser](https://github.com/sinkillerj/ProjectE/blob/mc1.21.1/src/main/java/moze_intel/projecte/config/CustomEMCParser.java).
