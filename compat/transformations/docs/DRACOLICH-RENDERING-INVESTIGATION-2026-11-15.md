# Dracolich rendering feasibility investigation (2026-11-15)

**Research only — no geometry, textures, client renderer patch, or assets shipped.**
Target: Dragon Survival **1.21.1 v2.0.74**, the version pinned for our next QA.
Canonical upstream source: [DragonSurvivalTeam/DragonSurvival at `1.21.1`](https://github.com/DragonSurvivalTeam/DragonSurvival/tree/1.21.1).

## Crucial correction: five *bodies*, not light/standard/heavy

Native body keys are `dragonsurvival:center`, `east`, `north`, `south`, `west` (plus `no_model`, not a renderable dragon). They each have a unique animation file:
`animations/dragon_center.json`, `dragon_east.json`, `dragon_north.json`,
`dragon_south.json`, `dragon_west.json`, but **all five built-in body entries
select `dragonsurvival:dragon_model`**.

The species are independently selected: `cave_dragon`, `forest_dragon`,
`sea_dragon`. This yields **3 species × 5 standard bodies = 15 normal variants**,
not three bodies and not a different geometric mesh per species/body.

Body-specific animation poses and scaling are important: Center is balanced,
East has reduced wings, North is flattened/swimming-oriented, South favors
running/jumping, West is flight-oriented. It is incorrect to substitute
light/standard/heavy across these five.

**Evidence:** `registry/dragon/body/DragonBodies.java`,
`registry/dragon/body/DragonBody.java`, `client/models/DragonModel.java`.

## Real geometry/animation interface

- Main GeckoLib asset: `assets/dragonsurvival/geo/dragon_model.geo.json`.
  Examined model has **272 named Geo bones**, 151 bones bearing cubes,
  **384 cubes total**, and four top roots (MolangHandlerDragon,
  MolangHandlerItems, Magic, MagicCircle).
- GeckoLib geometry descriptor declares 256×256 UV texture dimensions;
  built-in `DragonBody` declares 512×512 skin output dimensions for the
  layered editor. Do not assume UV coordinates can simply be copied or
  that the two dimensions are interchangeable. Validate atlas layout.
- Named chain includes `Torso`, `Pelvis`, `Head`,
  `Neck1`–`Neck4`, many front/hind limbs, `Tail1`–`Tail5`,
  `WingLeft`, `WingRight`, their articulated child nodes, etc.
  **Preserve bone names, pivots, parent hierarchy and transform conventions**,
  including invisible Molang/Blend handler bones. New bone details can
  attach as children of existing joints rather than requiring new animation
  keyframes.
- Special bones absolutely worth retaining: `MountingBone` for dragon
  riding, `BreathSource` for targeting and projectile origins,
  item-in-hand / jaw item bones, wings toggled by body configuration,
  `Neck` hidden in some first-person views, and armor/backpack
  attachment compatibility.
- `DragonModel.getModelResource(DragonEntity)` currently gets the
  model from `DragonStateProvider.getData(player).getModel()`
  (which returns `dragonBody.value().model()`), and appends
  `geo/*.geo.json`. It separately retrieves animation from the
  body entry, so selecting a different *visual model* does **not**
  require assigning a different body or changing its animation file.
- Growth scales from the player scale/stage through `getVisualScale`,
  rather than selecting a new GeckoLib geo file for each stage.
  Default skin resources exist for newborn, young, adult and optional
  ancient. A shared rig still needs testing at every stage.

**Evidence:** `client/models/DragonModel.java`,
`client/render/entity/dragon/DragonRenderer.java`,
`common/capability/DragonStateHandler.java`,
`assets/dragonsurvival/geo/dragon_model.geo.json`.

## Skin, glow and model extension points

1. **Resource/datapack model support exists.** `DragonBody` uses a
   synced datapack registry with fields `model`, `animation`,
   `texture_size`, `bones_to_hide_for_toggle`, etc. A custom body may
   select a new geometry, but the Dracolich should **not modify a player's
   body registry key**, because that key also determines powers, shape,
   movement, wings, and scale. Use a **client visual substitution**
   conditional on genuine Dracolich state instead.
2. **Custom skin parts are resource-loaded.** `DragonPartLoader`
   reads `assets/<namespace>/skin/parts/<layer>/*.json` and each
   entry has a texture path, applicable species, optional
   `applicable_bodies`, `is_colorable`, `is_glowing`.
   The skin editor composites base/bottom/eyes/horns/spikes/claws/
   teeth/magic/extras into client-generated textures.
   This supports compatible species-specific bone color and small
   cosmetic details without writing new renderer code; **it does not
   remove opaque underlying dragon geometry**.
3. **Glow is already a separate render layer.**
   `DragonGlowLayerRenderer` selects default, external or dynamically
   generated emissive texture and renders with `RenderType.EYES`.
   The proposed Cave inner fire, Sea cyan and Forest green may reuse
   this concept, but the client bridge must select the correct
   per-player emissive atlas; don't set a global texture field shared
   between players.
4. A *true* exposed-bone silhouette therefore needs a **rig-compatible
   skeletal geometry replacement or targeted renderer layer**, not
   just transparent skin pixels. The preferred pilot is a
   `tmm_transformations` client-only per-player render/model/texture
   override while retaining the **native species, body, growth,
   animations, hitbox and skills**.
5. Scope the native-state check to exactly one lich provider:
   Dragon Survival + Goety **xor** Forbidden Magic. Neither pure
   dragon nor invalid dragon-plus-both should receive a Dracolich
   render. Ensure server-to-client flag sync; do not assume every
   upstream attachment is replicated automatically.

**Evidence:** `client/skin_editor_system/{SkinLayer,loader/DragonPartLoader,
DragonEditorHandler}.java`,
`client/render/entity/dragon/DragonGlowLayerRenderer.java`.

## Existing decorative skeleton: useful but not ready-to-render

There are Minecraft **static block/item models**:
`assets/dragonsurvival/models/block/skeleton_dragon_full.json`,
plus independently modeled skulls, chest, neck, pelvis, legs, wings and
tail sections, using at least eight original skeleton block textures.

The full static model has **70 block-model elements** (in 0–16 block
coordinates) and element labels including `Torso`, `Pelvis_s`,
`Neck4_s`, `FrontLegRightUpper`. These names overlap with the
playable GeckoLib rig. They are valuable for shape and conversion
experimentation, **but they are not GeckoLib geometry**, and their
coordinate origin, UV, group structure, cube transforms and pivot
hierarchy cannot simply replace a 272-bone animated model.

**Evidence:** `assets/dragonsurvival/models/block/skeleton_dragon_full.json`
and sibling block models.

## Licensing and distribution gate

**Upstream `LICENSE.txt`, Dragon Survival License v1.2 (Sept 2026)**:
original custom skins are expressly permitted; Java-based addons
can be created, but extracted Dragon Survival Assets may **not**
be redistributed independently. The license defines a privileged
"Addon" as one that lists Dragon Survival as a Required Dependency on
CurseForge, and conditions reuse/derivative asset distribution on that
qualification or prior personal permission.

**Do not commit repackaged copies or converted derivatives of their
skeleton block models/textures in the TMM repository** without confirming
our addon qualifies or acquiring permission. Safer pilot: author original
skeletal geometry/texture art, preserve the native bone *interface*, and
load the installed Dragon Survival animations at runtime. Using the
upstream static models as visual/technical references is not a license
to rehost them. This is a practical license reading, not legal advice.

## Recommended production plan

1. Create an **original, small skeletal geometry prototype** for
   the head, ribs, shoulder and tail tied to a copy/adapter of the
   native rig names and pivots. Prototype on Center/Cave before
   filling out all appendages; keep `MountingBone` and
   `BreathSource` untouched. Do not distribute source-derived
   geometry without appropriate permission.
2. Add **client-only** Dracolich visual switching at
   `DragonModel.getModelResource`, per-player base texture selection at
   `getTextureResource`, and per-player glow selection in
   `DragonGlowLayerRenderer`. Leave `DragonStateHandler` body,
   species, growth, and player skin-preset data unchanged.
3. Test **all five animations** against the new geometry:
   idle/walk/crawl/run, flying/gliding, swimming, jaw/claws/breath,
   posture/first-person neck hiding, item render, saddle/rider
   `MountingBone`, wing toggles and armor/backpack alignment.
4. Build **three original material texture families**, not a separate
   animated rig per species: Cave — black bone / inner-fire orange;
   Sea — prismarine bone / coral encrustation / cyan; Forest — white
   bone / moss and lichen / green. Emissive mask per species.
   First pass may use three base + three glow textures on the same
   atlas; only add growth-stage variants where visual tests show a
   need.
5. Test the 15 species/body combinations, all normal growth
   stages and optional Ancient, multiplayer visibility, dimensions,
   respawn and cure. Client state must update promptly and original
   skin return exactly after cure. Preserve non-Dracolich dragons.
6. Treat models added by outside Dragon Survival addons separately:
   their geometry/animation bone hierarchies may differ. Fail back
   to ordinary visual style unless explicitly supported.

**Recommendation:** **one original skeleton model sharing the 272-bone
interface; three species-specific texture/glow families; five *native*
animation sets**. If the geometry proves too brittle, retain the
existing model and use a transparent skeletal-overlay render layer
only as a pre-alpha fallback, not as final true-skeleton art.

**QA status:** Static source inspection only. No GeckoLib-model asset
created, no client renderer injection compiled, no in-game graphics
validation, and no permission to distribute reused upstream assets
asserted by this document.
