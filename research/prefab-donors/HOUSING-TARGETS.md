# Tyr starter housing — approved targets and donor tracker
Updated 2026-11-18. Authority: issue #236; next comprehensive QA required for base Prefab (#287, PR #352). Research only; do not index raw donors or launch a build.

## Approved design
One generic and fourteen magical options, available in either player mode. Flexible footprint 16–32 blocks on each horizontal axis; roughly 500–3,000 ordinary placed blocks, not a hard cap. Basic living quarters, dedicated expandable magical workspace, comparable ordinary material/EMC value, no generated loot or entities, no advanced progression equipment. Generally include first functional crafting station if obtainable with basic vanilla materials (iron, wood, stone) or earliest readily available magical resources (blood, Source Gems, Dark Gems). Minor stations where justified; audit exact recipes and IDs later. Never bypass research, initiation, dimension, boss or tier gates. Expansions inherit parent housing; exclude Summoning Rituals, late-game Draconic Evolution/ProjectE/Expansion/Replication, universal artifacts/skills/CC:Tweaked, species-specific Dragon Survival.
Prefab 1.0.9 NeoForge 1.21.1 approved for next test; custom addon + LDLib2 still research candidates, no new dependency approval inferred. Verify placement, claims, one-time per-player reward, EMC/recipe bypass, terrain/air, preview and rotation.

## Planned buildings
| House | Primary | Secondary | Source / current disposition |
| --- | --- | --- | --- |
| Wizard's Tower | Ars Nouveau | — | Vanillin Structures 1.2.2; bundled CC0 / metadata MIT mismatch, https://www.curseforge.com/minecraft/mc-mods/vanillin-structures/files/4421300 ; extracted and parsed locally; conditional rework, see audit below |
| Mage's Academy | Iron's Spells | Scriptor Magicae, Apotheosis | Faerithia Mage Tower, MIT advertised, https://modrinth.com/project/faerithia ; conditional |
| Elven Garden | Botania | Feywild | Original cottage + greenhouse; oversized CC BY reference rejected https://www.schemcraft.com/schematics/ultimate-cottagecore-greenhouse-survival-base |
| Sanguine Temple | Neo Vitae | EvilCraft, Mahou Tsukai | CedricD0812 Japanese House CC BY 4.0 https://www.planetminecraft.com/project/japanese-house-6691798/ ; alternate temple https://www.planetminecraft.com/project/japanese-temple-6691443/ ; awaiting file |
| Celestial Observatory | Astral Sorcery | Enigmatic Legacy+ | Thun's Observatory MIT advertised https://modrinth.com/datapack/thuns-structures/version/1.1.0 ; awaiting ZIP |
| Summoner's Lodge | Occultism | Psi, Hexcasting | Original ritual courtyard and dry lab |
| Arcane Sanctum | Primal Magick | Totemic | Thun's Badlands Temple https://modrinth.com/datapack/thuns-structures ; conditional |
| Chromatic Manor | Pastel | — | Original pigment workshop |
| Dark Citadel | Goety | Eidolon, Corail Tombstone | Thun's Castle https://modrinth.com/datapack/thuns-structures ; conditional, compact keep only |
| Spirit Shrine | Malum | — | Original; Edo Japan donor rejected due dependencies https://modrinth.com/datapack/ancient-structures-edo-japan-dp/version/0.4 |
| Dwarven Foundry | Embers | Theurgy | Faerithia Forge Halls https://modrinth.com/project/faerithia ; conditional |
| Witch's Manor | JAWR | Roots Classic, Hexerei | Original Gothic Tudor manor |
| Witch's Cottage | Enchanted | Roots Classic, Hexerei | Thun's Witch Hut https://modrinth.com/datapack/thuns-structures/version/1.1.0 ; awaiting ZIP; alternate https://modrinth.com/mod/vanilla-plus_structures |
| Arcane Forge | Forbidden & Arcanus | Reliquary | Mimicry Wayside Forge MIT, exact 1.21.1 NBT preserved in mimicry/ (upstream blob 8768c5c10f3a7fffc38bd16394a221857472434e); 23x19x21 per source; palette audit pending |
| Generic | None | — | Prefab built-in starter house |

## Acceptance gates
Inspect actual binary format, size, palette and namespaces, entities, block entities, loot, markers, foundation and air/void. Confirm exact asset license and credit, normalize to 1.21.1 and strip unsafe or gated contents; inspect habitation/workspace; then disposable-world import and claim/rotation/preview tests. No raw asset is approved for runtime. Donor mods need not be installed.

## Vanillin Wizard Tower — actual donor audit (2026-11-18)
Owner attachment VanillinStructures-1.2.2.jar (162733 bytes; SHA256 5b3befa6c61471edc42a587556c734857f3d06812b8d2460a0a08170efba1e92) passed ZIP CRC. Exact member data/vanillinstructures/structures/wizardtower.nbt extracted locally, 41560 bytes; SHA256 3f87b751e40912ca958ea9957a3a6430e68c38248274c2f6bfff69109cbd0ecc; Git blob 81451474b768fcf0b9f9e7cd5dc5c43a01bd75a3. Author Genesis; JAR bundled LICENSE_VanillinStructures is CC0 1.0 Universal while fabric.mod.json says MIT; preserve both and reconcile scope before redistribution. No raw binary copied into GitHub in this pass (local extraction only).
Actual NBT: DataVersion 3218 / MC 1.19.3; size 17 x 44 x 19; 2438 non-air, 11774 explicit air, 70 palette states, all minecraft namespace. 1111 concrete powder, 100 amethyst blocks, 38 budding amethyst, Nether wood, 1 Ender Chest, 3 filled chests with 6 enchanted golden apples plus chorus fruit, invisibility potions and spyglass; one armed illusioner. Only substantial elevated floor at y=34, no bed/door/crafting table/ladder palette states. **Conditional — donor architecture, not a ready starter house**. Rework internal floor/access and workspace, remove entity and loot, replace costly/progression blocks and consider stable non-gravity palette; verify material parity, terrain/air and genuine 1.21.1 import. Other JAR templates not selected: tradercamp (command blocks, gold blocks and entities), creepertemple (43x25x41, 15149 nonair and spawner), pond, swordinstone, airship. Do not place raw templates. Exact extraction reproducible with Python zipfile via member above. Awaiting Thun ZIP and Japanese House schematic; Mimicry raw in repo, pending palette audit. Unrelated audio/pistol JAR not used. No game run, pin, or runtime change.
