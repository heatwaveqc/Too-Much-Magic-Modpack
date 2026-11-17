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

## First pilot feedback and next review
Owner feedback on the prior Work pilot (not an independent visual inspection by this agent):
- Overall direction promising, no production approval yet.
- Logs visibly fail tiling: redo side grain and end grain as needed; 5×5 review and oriented block previews required.
- Cobble likely fails tiling; stone suspected too. Re-evaluate both in 5×5 previews, reject/repair any failure rather than assume success.
- Gold rejected; rebuild using canonical angled ingot master.
- Iron promising, adjust slight angle and propose as shared ingot template, not approved final.
- Diamond acceptable direction but wrong cut description; redo to round brilliant side profile.
Keep existing pilot branches/records intact; supersede rejected draft decisions in registry, never silently mark approved. Other categories not approved until their own pilots pass. First revision priorities: diamond icon, shared iron/gold ingot family, stone/cobble/log tiling proofs, then independent category pilots. Separate contact sheets by category/family and include native PNGs, 16/32px icon previews, tiled 5×5 face previews, and UV/equipped preview as appropriate. No gameplay change or full boot/Prism authorization.
