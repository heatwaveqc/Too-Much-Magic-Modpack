# Category-specific production standards v0.2 — owner direction, 2026-11-17
This is the controlling addendum to STYLE-GUIDE.md, ASSET-STANDARDS.md and the production pipeline. Where older text differs, v0.2 wins. The owner likes the general artistic direction, not blanket approval of the pilot. Create a separate representative approved pilot for each category before mass production in that category.

## Classification and instructions
Each texture path has a primary category, family ID, required deliverables, applicable technical flags and review evidence. Work by family, validate by category, inventory by exact asset path. A block's inventory representation and model surfaces are separate related deliverables where applicable. Asset classification is based on actual model/renderer use, not the filename.
| Category | Generation and acceptance emphasis |
|---|---|
| Simple items | Original single icon, clear silhouette, upper-left light, transparent background, readability at 16/32 px. |
| Layered items | Individual aligned texture passes; preserve tint mask, alpha, native overlays. |
| Simple blocks | Same surface on each side. **Every simple block must tile seamlessly in both axes**; no single-tile-only acceptance. |
| Directional blocks | Correct face/axis semantics; repeated side/end surfaces and compatible transitions, natural oriented grain. Deliberate pattern rotations are respected. |
| Shaped blocks | Slabs, stairs, walls, fences: inherit material family, validate native UV and model face usage, no independent repaint where texture is reused. |
| Pseudoblocks | Cross-plane or other flat cutout imagery, e.g. flowers, vines, dripstone; preserve transparency, outline, plane overlaps and recognizable silhouette. |
| Complex blocks | Geometry beyond ordinary shaped blocks; independently texture real model surfaces/UVs, preserve component geometry and state separation, avoid baked fake perspective. |
| Connected blocks | Neighbor and edge-state behavior; check actual connectivity/indices, seams and rotations. |
| Fluids | Still/flowing surface and loop, translucency and native color/animation contract. |
| Mobs | Actual skin UV islands, anatomical identity, seams, pose and variant continuity. |
| Armor and wearables | Both inventory icon and corresponding worn 3D model UV texture(s), shared material; validate equipped appearance and overlays. Do not generate new model geometry by default. |
| Projectiles and rendered objects | Actual geometry, orientation, motion and native renderer layers. |
| GUI elements | Native atlas layout, hitboxes, text contrast, scale and slot alignment. |
| Micro-icons and fonts | Native symbol identity, tiny-scale legibility, exact glyph/atlas coordinates. |
| Particles and VFX | Runtime size, alpha, emission, readability and timing; no excess noise. |
| Environmental textures | Sky, stars, celestial objects, cloud/weather and dimension effects; renderer mapping, orientation and lighting. |

Technical flags are composable, not mutually exclusive categories: animated (frame layout, timing, smooth loop); tinted (grayscale/color mask and renderer semantics); layered (alignment and alpha); emissive (native pass); connected (neighbor state); stateful (family continuity); UV-sensitive (preserve atlas/model). Assign multiple as needed.

## Mandatory tiling gate
For **every simple block** inspect the final native-size tile in a seamless 5×5 repetition in both axes. No visible seam, artificial edge line, abrupt discontinuity, or objectionable obvious repeated motif. Inspect texture at native resolution and at distance. Correct boundary discontinuities by periodic painting/offset-and-repair or other tile-aware production, not by blur alone. Edge pixel equality is neither necessary nor sufficient; assess continuity of features crossing boundaries. Repeat checks for directional faces that tile, using axis-correct side/end placement; check separately on actual models. Record preview/review evidence per asset; if not reviewed, not approved. Do not commit draft/rejected images.

## Master silhouettes
Establish owner-approved original canonical silhouette/geometry for ingots first, using the successful iron pilot as direction, angled slightly diagonally with visible top and bevel planes. Preserve silhouette and relative facet placement across ordinary iron/gold/copper/silver/lead and analogous ingots via recoloring/material-specific finish. Gold must be recognizably metallic, warm rich highlights and believable shadow planes; avoid muddy yellow plastic/cheese. Approve variants as a family. Apply master-silhouette approach to raw metal chunks, nuggets, dusts and other genuinely interchangeable families after checking native semantic distinctions; use separate templates where shape communicates mechanics. Keep original editable template and provenance, not copied third-party art; no blind recolor when material finish or semantic identity demands changes.

## Diamond — corrected owner reference
**Round brilliant-cut diamond viewed from the side/profile, NOT princess cut, NOT a top-down round disk, NOT a rotated square.** Angular, broad girdle, shallow faceted crown with small flat table, tapering pavilion to a point below. Slightly elevated or three-quarter profile only if it retains that recognizable jewelry/Android diamond emoji outline. Pale aqua highlights, clear facet planes, transparent canvas, legible at 16px. Soartex historical diamond and Android emoji are shape references only; original drawing, not tracing. Native diamond sprite still needs exact historical reference inspection if available. Do not propagate earlier princess-cut instructions.

## Host-rock inheritance: ore blocks
Create and approve each host rock's seamless 64x master texture before its ore variants. Every embedded ore inherits the actual approved host texture (stone, deepslate, netherrack, or a mod/dimension-specific substrate as appropriate), with localized mineral seams, veins and inclusions. Preserve the host's edge texture, grain, value and lighting; ordinary edge pixels should remain identical to the corresponding host master. Do not generate ore cubes as unrelated rock art or use a contrasting outline/dark border. Mineral seams use the recognizable palette of the corresponding raw metal, gem, or other ore product family, with natural variation in shading and mineral form. Maintain distinguishability at distance and native ore semantics. When an intentionally edge-crossing seam is needed, design and prove compatible wrapping and blending rather than introducing a seam against plain host stone.
Acceptance: inspect a mixed 5×5 mosaic of host and ore, an all-ore 5×5 repetition, and an actual block/model preview. Check both axes and native 64px output; verify the ore color family beside its raw product. Record host master ID/hash, ore palette family, overlay/compositing provenance and preview evidence. Host revisions require affected ore variants to be revisited.

## Infused materials: silhouette and facet inheritance
Botania Mana Diamond inherits the approved vanilla diamond master (round brilliant-cut side profile); Mana Pearl inherits the approved Ender Pearl master. Preserve the exact corresponding outline, angle, scale, transparent silhouette, facet/contour lines and structural detail. Use a light blue primary mana color with coordinated highlights and shadows, not a new geometry or unrelated redraw. Retain the recognizable difference between diamond and pearl; contrast each mana item with its ordinary counterpart at 16/32px and 64px. Generate from the original approved masters using controlled material recoloring. Link derivatives to master ID/hash; review an aligned side-by-side comparison. Apply analogous inheritance to other genuinely transformed/infused material families when their native semantic identity supports it.

## Runes, sigils and magical families
Botania rune items require a single approved shared substrate silhouette, framing, orientation, scale and lighting. Preserve each rune's individual semantic symbol and distinguishing palette; consistent substrate does not mean identical glyph. Use exact native symbol semantics as technical reference, with original rendering. Define master templates for analogous rune stones, glyph tokens, seals, charms, talismans and other related magical item families across relevant mods. Prefer reuse of the same approved form when items are materially equivalent; keep distinct substrates/geometry where a school's native identity, function, or renderer requires it. Approve each coherent family at native size before batch recoloring, glyph substitution or variants. Do not collapse different magic schools into one interchangeable system merely because their tokens resemble each other.

## Art-scope blacklist (initial owner-directed exclusions)
Crystalix (manifest id `crystalix`) and Rechiseled (manifest id `rechiseled`, the Chipped-like decorative block mod) are excluded from original texture production by default. They stay installed and functional; this is not a modpack/gameplay blacklist. Keep native textures where no replacement is produced. Record the exclusions separately, retain archive inventory/asset counts where obtainable, mark exact matching texture records `excluded-approved` with reason, and report both total inventory and eligible production coverage. Use exact archive provenance, not guessed namespaces or broad string matches. For textures shared with vanilla or an included mod, do not blacklist the shared resource globally. Check that vanilla/base material replacements still render normally in generated decorative variants. No other mod excluded without owner decision. Review this initial list if the owner changes scope.

## First pilot feedback and next review
Owner feedback on the prior Work pilot (not an independent visual inspection by this agent):
- Overall direction promising, no production approval yet.
- Logs visibly fail tiling: redo side grain and end grain as needed; 5×5 review and oriented block previews required.
- Cobble likely fails tiling; stone suspected too. Re-evaluate both in 5×5 previews, reject/repair any failure rather than assume success.
- Gold rejected; rebuild using canonical angled ingot master.
- Iron promising, adjust slight angle and propose as shared ingot template, not approved final.
- Diamond acceptable direction but wrong cut description; redo to round brilliant side profile.
Keep existing pilot branches/records intact; supersede rejected draft decisions in registry, never silently mark approved. Other categories not approved until their own pilots pass. First revision priorities: diamond icon, shared iron/gold ingot family, stone/cobble/log tiling proofs, then host-rock/ore mixed tiling, ordinary/mana diamond and pearl pairs, and Botania runes before broader independent category pilots. Separate contact sheets by category/family and include native PNGs, 16/32px icon previews, tiled 5×5 face previews, and UV/equipped preview as appropriate. No gameplay change or full boot/Prism authorization.
