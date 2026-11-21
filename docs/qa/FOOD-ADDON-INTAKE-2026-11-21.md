# Food addon intake — Huaxia Delight / Crispy! (2026-11-21)

## Owner decision and release scope

The owner expressly approved **Huaxia Delight** and **Crispy!** on 2026-11-21. This is a **two-mod exception** to the prior 2026-11-20 frozen new-mod universe (#390), not approval of other survey candidates. Both have `desired=true` and `enabled_in_pack=false` in the isolated candidate source `manifest/mods.json`. **Neither is installed, hash-pinned, included in an authorized build, or gameplay-tested by this source change.** The integration writer owns all active `mods/*.pw.toml`, `index.toml`, `pack.toml`, source-lock and next-test roster reconciliation.

### Exact public release candidates

| Mod | 1.21.1 NeoForge candidate | Official file | Published requirements / native feature |
| --- | --- | --- | --- |
| Huaxia Delight | 1.2.0 | [CurseForge 8829017](https://www.curseforge.com/minecraft/mc-mods/huaxia-delight/files/8829017), project 1685797 | NeoForge >=21.1.192 and <21.2, Farmer's Delight >=1.2.9 and <1.4, Java 21; current candidate baseline has NeoForge 21.1.265 and FD 1.3.4. New Chinese ingredients, dishes, and wok; Create **not** required. |
| Crispy! | Crispy-NeoForge-1.1 | [CurseForge 6447126](https://www.curseforge.com/minecraft/mc-mods/crispy/files/6447126) | Published 1.21.1 NeoForge release. Fortune Cookie produces a random message and Luck II; also adds several other cookie types and replaces the ordinary cookie presentation. Exact loader metadata and dependency closure still need artifact inspection. |

No new recipe patches, bespoke fortunes, cookie buffs, quest nodes, EMC values, or transformation diet exceptions were approved or implemented here.

## Three work inventories

### Code-ready / implementation-ready slices

- [x] **Source-ready — food roster selection**: two explicit desired/inactive manifest entries, exact candidate provider file links, targeted version, reason for scope exception. Owned by isolated intake branch. Acceptance: valid JSON, unique IDs, active set unchanged.
- [ ] **Ready for designated integration writer — immutable provider pins**: retrieve exact official binaries; verify SHA, release metadata, NeoForge/FD dependency closure and loader sides; prepare proper Packwiz candidate `.pw.toml` and synchronized `index.toml`/`pack.toml` hashes without overwriting concurrent work. Acceptance: audit evidence, correct both-side distribution, quick CI/preflight green before review. Do not mark installed before integration.
- [ ] **Ready after pin — recipe/food compatibility**: inspect real JAR IDs and native recipes against Farmer's Delight, Pam's HarvestCraft 2, Cooking for Blockheads, Occult Cooking, Bountiful and transformation diets. Limit fixes to verified collisions, maintain native cutting/wok/pot behavior and manual specialty stations, no blanket ingredient substitution.
- [ ] **Ready after pin — EMC and balance audit**: verify vanilla cookie replacement versus custom variants; Luck II availability/duration, farmability, recipe costs, ProjectE mapping and obvious loops. Adjust only after demonstrating a problem.

### Design blockers

- **None for including these two mods under their native behavior.** Custom fortune text, adjusted Luck II and extra food addons are *unapproved separate enhancements*, not blockers to basic inclusion. The only hold is engineering integration / tests.

### In-game testing queue

- [ ] **B — awaiting inclusion / authorized QA**: NeoForge dedicated-server and client presence, same published versions, clean log/registry, integrated kitchen recipe book / JEI.
- [ ] **C — awaiting inclusion / authorized QA**: cook a sampling of Huaxia dishes in native stations, source crops and confirm no duplicate recipe outputs; crafting leftovers and CFB/Occult pantry routing must not bypass manual specialty processing.
- [ ] **B/C — awaiting inclusion / authorized QA**: bake and eat a fortune cookie; random message appears, Luck II applies with actual native strength/duration; normal cookies and other Crispy variants are obtainable.
- [ ] **C — awaiting inclusion / authorized QA**: dragon/cave/forest/sea, vampire, werewolf, lich and ordinary human diets respect approved food rules; review enchanted/magical foods separately.
- [ ] **B/C — awaiting inclusion / authorized QA**: verify crop worldgen with Tectonic and loaded worlds, multiplayer/server synchronization, JEI recipes, and EMC/progression exploits.

**No full server smoke, integration assembly, Prism package or gameplay QA is authorized by this intake document.**

## Additional Delight survey — candidates only, not owner selected

- **Dungeon's Delight — highest-interest follow-up.** [1.21.1 NeoForge 1.5.2](https://www.curseforge.com/minecraft/mc-mods/dungeons-delight/files/9446248). Monster Pot, monster meats, dungeon-themed food/effects and spawner-related resources. Distinct gameplay niche, fits magic/exploration; needs spawner-drop and combat/effect/progression review before selection.
- **Ender's Delight — best restrained End-food candidate.** [Official project](https://www.curseforge.com/minecraft/mc-mods/enders-delight) publishes 1.21.1 NeoForge 1.3.1, cooking chorus fruit, Endermen/Shulker items and End resources. Compare but do **not** assume compatibility with BetterEnd. Prefer one End addon rather than stacking with [End's Delight](https://www.curseforge.com/minecraft/mc-mods/ends-delight), whose 1.21.1 NeoForge release is beta and has an older patched fork due to FD stove issues.
- **Cultural Delights — optional, high overlap.** [Official 1.21.1 NeoForge 0.19.8](https://www.curseforge.com/minecraft/mc-mods/cultural-delights). Avocado, cucumber, eggplant, corn, beans and cinnamon; compare with existing four Pam's modules and Huaxia ingredients first.
- **Aquaculture Delight — already proposed in [PR #403](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/403)** with a source-only pin; avoid a duplicate intake. Integrate the existing proposed stream instead.
- **Ocean's Delight / Crabber's Delight — lower priority** relative to installed Aquaculture and the Aquaculture Delight proposal; more seafood, trapping, crabs and coastal trees increase overlap and worldgen work.
- **Fruits Delight — lower priority**: [official project](https://www.curseforge.com/minecraft/mc-mods/fruits-delight) adds 17 fruits, ~90 products, new fruit trees and many special combat/exploration effects. Large balance/crop overlap vs Pam's.
- **Korean Delight — optional cuisines;** verified 1.21.1 NeoForge candidate, but another standalone cuisine addon immediately after Huaxia is not presently justified.
- **Delightful unofficial NeoForge port — hold**: broad ingredients/farming/compatibility expansion overlaps existing kitchen and Pam's modules; requires separate port maturity review.

Already in current baseline manifest: Farmer's Delight, Brewin' and Chewin', Cocktails Delight, My Nether's Delight. Additional mods listed here are **research-only proposals** and do not override the mod-scope freeze.
