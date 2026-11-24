# Cave/Center Dracolich visual pilot

Minecraft 1.21.1 / NeoForge 21.1.265 / Dragon Survival 2.0.74 / GeckoLib 4.9.3.
Scope: **Cave species + Center body + exactly one real lich provider**.
Supported lich providers remain native Goety 3.2.2 or Forbidden Magic 0.5.37.

## Implemented

- Read-only server classification uses actual dragon species/body/model and
  native lich flags. Plain dragons, plain liches, dual-lich conflicts, other
  species/bodies, addon models and unknown versions/APIs retain ordinary visuals.
- A server-to-client-only payload replicates the derived cosmetic flag by player
  UUID. Updates run on login, respawn, dimension change, tracking and state changes
  detected within five server ticks. Logout/server stop clear the server cache;
  client disconnect clears replicated flags. No saved fake lich/dragon state.
- Client model selection is per player and checks its current native species,
  body/model plus the server flag and successful model bake. No shared texture
  override, native body reassignment, skin-setting writes or new transformation.
- A mixin plugin gates the new hooks before injection to exactly Dragon Survival
  2.0.74 + GeckoLib 4.9.3, avoiding injections into unknown API versions.
- During normal GeckoLib resource reload, Java combines original TMM geometry
  with the installed rig in memory. The exact normalized rig fingerprint is
  checked; no Python setup or extracted rig file is needed by players. Invalid
  native geometry produces a marker rejected by the visual selector, preserving
  the ordinary dragon instead of an invisible/incorrectly textured skeleton.
- 407 original Cave/Center cubes, including the hollow skull, ribs, articulated
  limbs/tail/wings, secondary horns/fangs and a visible inner-fire chest core.
- Original 1024x1024 diffuse + emissive atlases, with **2,442 distinct face UV
  rectangles**, charcoal/obsidian bone, orange molten fissures and bright eyes/core.
- Original casting glyphs on MagicRing, MagicCubeRotation and all eight native
  MagicCirclePart controls restore the non-body effects removed by the initial
  skeleton-only cube replacement. They reuse native animation/visibility.
- A dedicated per-player emissive pass uses our atlas and native render-color/
  visibility handling. Ordinary players keep the native glow layer.
- Native armor/claw masks retain their native UV layout. For this form the armor
  layer renders the **installed native armor geometry**, with current skeleton
  bone poses/visibility copied onto it. This avoids painting native armor masks
  onto unrelated Cave UV rectangles. Equipment stats and texture generation stay
  native. Held-item, breath and rider attachment controls remain in the full rig.
- Native skin settings are preserved throughout: curing lichdom or leaving the
  supported species/body scope restores the original renderer/skin after sync.
  The normal inventory player is supported; native fake skin-editor/smithing
  previews intentionally keep their own preview behavior.

All five existing body animation files remain installed and untouched. Only
Center receives this visual; Sea/Forest and East/North/South/West are outside this
pilot. No changes to dragon powers, growth, hitboxes, movement, food, lich resources,
resurrection ordering or acquisition restrictions.

## Source and asset provenance

`src/main/resources/assets/tmm_transformations/` contains only original TMM mesh,
atlases, an assembly marker and a rig fingerprint. No Dragon Survival rig, mesh,
texture or animation is embedded in the module. Native geometry is obtained from
the installed mod at runtime and remains in memory, including the armor pass.
`scripts/build_dracolich_cave_skin.py` reproduces the original pixel atlas/UV mesh.
The static textured GLB and preview in `art/dracolich/` contain original surfaces
and materials, with no upstream animation or rig.

No new Minecraft mod or runtime library is added. The geometry unit tests expose
Minecraft's existing Gson 2.10.1 as a test-only dependency; it is not bundled.

## Validation commands

From the repository root, Java 21 and Gradle 9.2.1:

```bash
gradle --no-daemon -p compat/transformations build
python3 scripts/build_dracolich_cave_skin.py
```

The standalone Java check uses the built module, Gson **2.10.1 first on the
classpath**, GeckoLib 4.9.3 and installed Minecraft/NeoForge runtime libraries:

```bash
java -cp "$check_classpath" scripts/CheckDracolichCave.java \
  /path/to/official/DragonSurvival-1.21.1-v2.0.74-09.11.2026-all.jar \
  /path/to/this/repository /private/output/cave_center.geo.json
python3 scripts/preview_dracolich_cave.py /private/output/cave_center.geo.json
```

The check exercises the actual Java ResourceManager loader and GeckoLib cube bake,
validates 272 native bones/407 authored cubes, verifies native armor pose copying,
and proves a changed rig fails back to the harmless marker. Complete assembled
native-rig output must stay private/outside the repository.

## Required disposable-client acceptance

1. Establish an ordinary Cave/Center dragon with a recognizable custom native
   skin. Confirm ordinary visuals. Acquire **one** native lichdom; run
   `/tmmtransform dracolich` and verify the correct provider and visual eligibility.
   The skeleton should appear within five server ticks without relogging.
2. Test Goety and Forbidden Magic independently. Confirm plain liches, plain
   dragons, Sea/Forest and other bodies keep their original appearance; forced
   invalid dual-lich dragons must not receive this visual.
3. Test idle/walk/run/crawl, swimming, flight/glide/folded wings, attacks, jaw and
   breath origin. Inspect thin bone joints for gaps/clipping throughout motion.
   Repeat growth stages, including Ancient if enabled.
4. Inspect bright light and darkness, hurt/invisibility/transparency, casting
   glyphs and inventory. Native smithing/skin-editor previews are separate.
5. Equip/remove armor, trims, claw/teeth overlays and held/offhand items; test
   mouth/side item settings and rider attachment. Armor should not map random
   native mask pixels across the Cave skin. Verify no pose leakage to a nearby
   ordinary dragon.
6. With a second client, test late join, tracking range, death/resurrection,
   dimension travel, disconnect/reconnect and mixed ordinary/Dracolich players.
7. Cure the chosen lichdom while retaining dragonhood: exact original native
   skin/settings/body should return. Changing to another body/species likewise
   leaves the pilot. Confirm no permanent preset or customization mutation.
8. Reload resources with F3+T. Test a deliberately incompatible native model
   resource pack: a diagnostic warning and normal dragon fallback are expected.

Existing QA records ordinary-dragon/inventory visual glitches and native
`mass_buff`/`flapping_wings_standing` animation parsing errors. Use an ordinary
dragon control and preserve baseline logs when investigating visual failures.

This is an experimental visual pilot until the above client tests pass. A Gradle
build, dedicated-server startup or offline render is not in-game acceptance.
