# Art style guide v0.1 — draft for pilot approval
## North star
Soartex-inspired **64x**: Minecraft remains Minecraft, but craftsmanship reads as tangible material. A smooth, restrained semi-realistic presentation for a serious fantasy world. Recognizable silhouettes and native hue identity are more important than spectacle. The original Soartex 1.12-era diamond is an owner-confirmed reference for a faceted, princess-cut-like gem; its exact historical sprite still needs source inspection. Botania 128x demonstrates a coherent magical botanical language. See references/README.md. These are reference projects, not production sources.

## Fundamentals
- Use a soft upper-left key light in inventory sprites; deep but not black shadows on lower-right, smaller reflected rim only when material warrants it. Terrain tiles use restrained microcontrast and minimal directional baked shadows; the engine provides dynamic light. Avoid cast shadows on transparent canvas; they look strange in slots and item frames.
- Detail hierarchy: silhouette first, plane or material second, texture third. At 16px preview, distinguish item classes and major variants by shape and controlled hue; at 64px, reveal facets, grain, tooling and wear. Anti-alias internal contours with purpose; hard polished edges stay crisp. Preserve alpha at edges; avoid halos. No arbitrary outlines around every object.
- Palette: grounded earthy neutrals, subdued vegetation, readable contrasting ores and magical accents. Keep vanilla colors (iron cool neutral, gold warm, copper reddish, redstone red, lapis blue, diamond pale aqua, emerald green). Natural blocks should not compete with spell effects. Tintable textures remain compatible with native biome or dye tint logic.
- Avoid photoreal photographs, excessive noise, oversaturated cartoons, thick comic outlines, decorative filigree on humble tools, plastic specular shine, baked global glow, illegible realism and random gradients. Magic glows are localized to energy-bearing regions and use distinct signatures; no implied new emissive render behavior.
 
## Material language
| Material | Form and finish | Color intent |
|---|---|---|
| Iron/steel | Brushed fine grain; broad cold highlight; chamfered edge | slate, silver, cool gray |
| Gold | Warm satin mirror on beveled face; moderate contrast | ochre, honey, warm pale highlight |
| Copper | Reddish warm metal, green oxidation only where native variant | terracotta to salmon |
| Lead/silver | Lead soft dark matte; silver bright cool neutral | distinguish by value and luster |
| Gemstones | Clear intentional planes and table shape, selective bright facets | diamond pale cyan, emerald lush green, ruby crimson, lapis opaque blue |
| Stone | Soft mottling at multiple scales, tiny sparse pores, subtle cracks | gray or warm earthy base, defined strata |
| Marble | Quiet veining, smooth polished faces; architectural variants share mineral palette | warm white, gray, blue where native |
| Wood | Long irregular fine grain with restrained knots, end rings, species distinctions; planks use consistent joint widths | natural brown/amber/gray |
| Cloth/leather | Coarse enough to read without grid noise; leather satin crease/edge, cloth matte | muted tan, warm brown, natural dye |
| Glass | Clear/tinted broad planes, sparse highlights, no fake fully opaque fill | subtle cyan or dye tone |
| Botanical | Natural leaf shape, gentle veins, clean stems, recognizably different flower profiles | controlled rich floral hues |
| Bone/organic | Cream and warmer aged edges, non-gory stylization | ivory/ochre; undead variants desaturated |

Proposed palette swatches in references/palette.svg are an original starting point, **not approved final color standards**. Each family has shadow/base/light accents; do not force identical material color in every biome, species or magical school.

## Magic vocabulary
Maintain each mod's own lore and recognition while unifying craft quality. Use color to communicate what player already knows, never invent a new mechanical distinction.
- Ars Nouveau: saturated blue-violet Source and warm gilt or timber/chiseled mineral apparatus; preserve glyph shapes/effect icons.
- Botania: botanic carved livingwood, pale mineral livingrock, blue mana, organic floral shapes; mana-infused equipment stays distinct from ordinary metal. Kaworru's flower/item gallery demonstrates the balance between colorful spell components and quiet substrates.
- Astral Sorcery: cool marble, midnight blue, silver and pinpoint starlight; preserve constellation patterns, runes, altar geometry, crystal shapes, animated sky/lighting.
- Neo Vitae / JAWR / EvilCraft: dark stone, reds, bone/wood/iron and ritual motifs according to original semantics. Distinguish each magic system rather than homogenize all into red.
- Occultism, Goety, Eidolon, Malum, Hexerei, Theurgy, Primal Magick: own sigils and lore motifs retained; leather, stone, metals and glass use shared material finish. Magical effects keep native hues and icon relationships; survey each mod before setting exact theme.
- Psi, Hexcasting, Draconic Evolution, ProjectE: respect existing functional color coding, energy states and renderer layers; avoid adding labels/ornament that alters gameplay interpretation.

## Classes of art
**Items:** orthographic/iconographic view consistent with original model orientation. Keep native asymmetry/orientation (tools diagonal when appropriate); leave safe transparent padding; don't enlarge visually tiny original components to fake realism. Faceted diamond: square-cut/rotated presentation with angular crown and bright table, original geometry not a copied Soartex sprite. Emerald step-cut elongated/different silhouette. Distinguish uncut crystals, ore fragments, ingots, powders and full gems.
**Blocks:** a single seamless square as required, preserving native face selection, top/side/bottom relationships, CTM/tint, borders and tiling. Brick joint continuity and wood plank edges tested in 3x3 or 5x5 array. Keep enough contrast for identifying ores, crops, machine states. Avoid painting perspective on planar cube faces.
**Entities/armor:** use actual UV template; paint surface regions not a rendered portrait; preserve anatomical features, seams and armor overlay. Maintain distinct mob variants and damage/state layers. Respect emission/translucent eyes and alternate skins.
**GUI/particles:** retain hitbox, icon atlas, text contrast, slots, progress bars and timing. Avoid unnecessary ornamental noise. Art must remain readable at GUI scales 1–4 and high-resolution displays; preserve transparent pixels needed by render code. Particle effects are sparse and coherent at actual runtime size.
**Dimensions:** coherent regional material palette, recognizable vanilla-like structure scale, native biome ecology, boss telegraphs and danger colors. Exotic dimensions may be richer or stranger; never flatten their identity into Overworld stone and wood.

## Pilot before scale
Produce a single contact sheet and separate candidate files for diamond/emerald/iron/gold, stone/cobble/oak log and planks, a flower, rune, mana diamond, a magical workstation face, an armor/entity UV sample and one GUI widget. Pick exact asset paths only after inventory; choose one vanilla and one Botania family first. Compare neutral daylight, torchlight, darkness profile, backgrounds and actual icon size. Owner approves or revises visual targets before bulk work. Use the revised guide as an immutable versioned reference for every future batch.
