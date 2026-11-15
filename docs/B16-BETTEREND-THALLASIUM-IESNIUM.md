# B16 — BetterEnd: New Dawn / Thallasium Iesnium gate

**Decision:** 2026-11-15. **Status:** approved for next experimental integration build; NOT currently installed or server/client-tested. Do not merge the replacement Spirit Fire recipe into a published build without the four mod pins.

## Next-build roster

| Component | Target | Source | Deployment |
| --- | --- | --- | --- |
| BetterEnd: New Dawn | 21.0.39; Minecraft 1.21.1 NeoForge; CurseForge file 9306604 | https://www.curseforge.com/minecraft/mc-mods/betterend-neoforge/files/9306604 | Client and server |
| BCLib: New Dawn | matching NeoForge 1.21.1 release (pin exact version/hash) | https://modrinth.com/mod/bclib-neoforge | Client and server |
| WunderLib: New Dawn | matching NeoForge 1.21.1 release (pin exact version/hash) | https://modrinth.com/mod/wunderlib-neoforge | Client and server |
| WorldWeaver: New Dawn | 21.0.23 upstream source-listed for NeoForge 1.21.1 (confirm download) | https://modrinth.com/mod/worldweaver-neoforge | Client and server |

Use the **New Dawn NeoForge** files, not original Fabric/Quilt editions. Mod file hashes and complete transitive dependency closure have NOT been resolved in this documentation; `enabled_in_pack=false` is intentional.

## Spirit Fire replacement

- Remove `kubejs/data/tmm/recipe/spirit_fire/iesnium_from_end_draconium.json`.
- Add `kubejs/data/tmm/recipe/spirit_fire/iesnium_from_thallasium.json` as Occultism `occultism:spirit_fire`: `betterend:thallasium_ore` x1 -> `occultism:iesnium_ore` x1.
- Do not retain the End Draconium Ore route as a parallel recipe; the user selected Thallasium as the new ore gate. Do not substitute End Stone.
- This is a **real ore block** input. Determine from the installed BetterEnd version whether harvesting the block requires Silk Touch; don't claim the route is easier until that gameplay test.

## Integration checklist

1. Resolve and SHA-pin four specific NeoForge 1.21.1 mod files in `mods/*.pw.toml`, update `index.toml` and `pack.toml`, and set the four roster entries `enabled_in_pack=true` only after files exist.
2. Confirm the new End biomes, terrain, ore worldgen and portal progression in a **fresh disposable End**. Existing End chunks will not retroactively gain new terrain.
3. Verify `betterend:thallasium_ore` in the live item registry and test intact block acquisition, with and without Silk Touch.
4. Confirm JEI/recipe manager sees the Spirit Fire conversion; throw one ore and a stack, count outputs, and verify End Draconium Ore and End Stone **do not** convert.
5. Run full static packwiz validation, all existing CC/Spirit Fire source checks, dedicated-server boot, and GUI/world client smoke; record blockers and logs. Check terrain compatibility with Tectonic and other End worldgen mods.
6. Do not promote the B16 changes into the integration build until required dependencies and recipe land together; otherwise the old route would be disabled while the new source item does not exist.

**Scope:** End exploration enhancement + Iesnium acquisition adjustment only. No unrelated recipe, quest, existing-world, or loot-table changes.
