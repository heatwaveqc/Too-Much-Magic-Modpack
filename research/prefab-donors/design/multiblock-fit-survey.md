# Fixed multiblock fit standard and Chromatic Manor survey
2026-11-18. Owner directive: every dedicated magical home must accommodate fixed player-built multiblocks for primary and associated secondary mods, through normal progression including upgrades. Reserved space, not pre-completed advanced machines. Record exact source/pin, orientation, full occupied and required clear volume, floor support or subfloor, overhead, upgrades, circulation, interaction and environmental requirements; verify simultaneously usable independent machines, reuse successive upgrade footprints only where compatible. Flexible/remote/environment-specific installations get designated expansion or suitable external site, not unsupported promises. Keep normal material/research gates; do not grant dimension portals or advanced components. Apply to original builds AND all donor adaptations before release. Layout choices below are provisional; no new asset, dependency or runtime changes.

## Pastel + Hexcasting — source inventory
Pack pin Pastel-1.1.5.5.jar CF 7591337, SHA1 53a582520b78aebd56c9be1fd041472047dca3d3. Inspected all 23 Modonomicon multiblock definitions in upstream 1.21.1 tree 090dfefa6591b6a470e9b93fb02b02aae6642b30 (tree, not exact verified binary): https://github.com/terrarium-earth/Pastel/tree/1.21.1/src/main/resources/data/pastel/modonomicon/multiblocks . Raw pattern dimensions W×H×D, includes explicit padding where present. Proposed rooms reserve two blocks on each horizontal side and two extra vertical blocks beyond raw pattern; check actual floor datum, orientation, fluid, interaction and supports before generation.
| Structure | Raw size | Proposed clear envelope |
|---|---|---|
| Pigment Pedestal simple | 11×4×11 | shared pedestal hall |
| Advanced + speed variants | 13×6×13 | shared pedestal hall |
| Complex, including moonstoneless variant | 13×7×13 | 17×9×17 |
| Fusion Shrine + upgrades | 9×5×9 | 13×7×13 |
| Enchanter + upgrades | 11×5×11 | 15×7×15 |
| Spirit Instiller + upgrades | 8×5×9 | 12×7×13 |
| Cinderhearth, dry and upgrade variants | 4×5×5 maximum | 8×7×9 (nonflammable, future lava) |
| Alloy Forgery integration: basalt, blackslag, bone ash, calcite, pyrite, shale clay | 3×3×3 each | conditional 5×5×5 bays; Alloy Forgery absent from integration index so do not require/install |
| Amethyst geode tutorial projection | 16×14×13 | illustrative, not fixed machine; naturalistic underground target |
| Perturbed Eye portal | 5×4×5 | exploration illustration, not home requirement; no portal blocks granted |
| Abyssal vines | 5×9×5 | illustrated cultivation, not fixed machine; no gated resources granted |
PastelMultiblocks.java registers pedestal tiers, shrine, enchanter, instiller, cinderhearth. Need exact release JAR/pin comparison, supplemental audit of other stations (Crystallarieum, Potion Workshop, ink logistics, Hexcasting staff/tablet/scroll work, media storage; non-Modonomicon/optional structures), access/sky/fluid requirements and upgrade compatibility before final fit approval. Source patterns do not authorize granting complete structures. Proposed two-story manor + generous basement geode, independent machine rooms and garden; pedestal hall tallest; leave empty foundation locations or low-cost removable guide markers. Final dimensions/budding count TBD.

## Donor fit screening
Five advanced donor candidates; imported/raw/source status differs. No clear-volume or live PASS inferred from envelope.
| House (existing external dimensions) | Review requirement | Preliminary result |
|---|---|---|
| Wizard's Tower (17×44×19) | Ars Nouveau Enchanting Apparatus/Arcane Core/pedestal area; source generation, ritual and addons | Re-floor with unobstructed apparatus room, check actual radius/addon layouts; fit not yet proved |
| Celestial Observatory (21×20×18) | Astral Sorcery Attunement Altar (legacy guide 19×19×6), other altar tiers/celestial and sky constraints; Enigmatic Legacy+ | Original width/depth cannot contain 19×19 with walkways; open outdoor platform or roof enlargement required, exact 1.21.1 port check |
| Sanguine Temple (41×18×14) | Neo Vitae Ara Vitae full-tier + ritual stone layouts, EvilCraft, Mahou | Cannot contain full-tier altar in narrow donor; separate temple courtyard or stepped excavated annex. Earlier exact Neo Vitae source 2744123bb6e4f72c602178b23384be41956eef23 altar_tier JSON: weak 1×1×1, apprentice 3×2×3, mage 9×4×9, master 13×6×13, archmage 19×7×19, transcendent 25×9×25 (offset x/z ±12, y -5..+3 relative altar). Reserve provisional 29×11×29 clear volume with subfloor depth, verify source against current 1.1.53 pin and pack overrides; ritual layouts not surveyed |
| Witch's Cottage (11×6×11) | Enchanted chalk circles, altar, cauldron; Roots Classic and Hexerei | Expand outdoor garden/ritual clearing; exact largest ring and associated mod geometries pending |
| Arcane Forge (23×19×21) | Forbidden & Arcanus Hephaestus Forge (1.21.x guide 9×9 footprint), Reliquary | Nominal envelope sufficient; actual smithy clear floor, forge levels and donor palette pending; no actual clean import/fit PASS |
Also assess conditional Goety Graveyard, Primal Stonehenge, Botania Treehouse, cozy Roots Hobbit Hole, remaining originals, minor systems and addons. Occult Tower guide fit statically checked, live still pending. No finished donor acceptance.

Source notes: https://github.com/breakinblocks/NeoVitae/tree/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/neovitae/altar_tier ; https://astral-sorcery.fandom.com/wiki/Attunement_Altar (legacy, not exact port); https://wiki.valhelsia.net/external-mods/forbidden-and-arcanus/wiki/1.21.x/blocks/hephaestus-forge ; https://modrinth.com/mod/enchanted-witchcraft ; exact version verification pending.
