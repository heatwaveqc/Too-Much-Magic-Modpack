# Celestial Observatory V1 — STATIC_QA_PREVIEW_ONLY
Owner authorized original construction 2026-11-18 following donor reconciliation. This is original layout, not CaptainQ's ObservaTree or Thun's binary. Architectural inspiration only: pale masonry, dark oak, cyan glazing and a stepped blue dome set in an axial courtyard. No external assets reused.

## Plan and space
67 × 24 × 62, local surface Y=3 (placement requires -3 relative to desired floor elevation), bounding volume 99696; 4435 nonair, 31417 explicit air, 19 vanilla states. Ground-level paths connect the four roofless machine pads, two auxiliary pads, relay expansion and four entrances to a two-storey octagonal residence. Ground floor: two beds, crafting table, furnace, three empty barrels; upper: library and ordinary crafting/Enigmatic Legacy+ workspace. No entities or NBT inventory payloads. Lanterns are outside functional reservations; lighting and safe terrain placement are not verified. No free modded altar, focus relay, starlight, crystals, infuser, gate or Lumen machine. Cosmetic ordinary replaceable marker floors only.

| Function | Center X,Z | Origin Y | Reserved W×H×D | Status |
|---|---:|---:|---|---|
| Altar T2–T4 | 15,15 | 4 | 19×10×19 | static geometry pass |
| Attunement | 46,15 | 4 | 23×7×23 | static geometry pass |
| Infuser | 12,39 | 4 | 11×6×11 | static geometry pass |
| Gateway | 51,39 | 4 | 11×4×11 | static geometry pass |
| Lightwell and crystal handling | 12,53 | 4 | 11×6×11 | provisional auxiliary size |
| Lumen and auxiliary machinery | 51,53 | 4 | 11×6×11 | provisional auxiliary size |

Altar T2–T4 share the same pad; inherited patterns reach y=-1..+6, room for T4 15×8×15, no supplied structural recipe. Attunement's full 19×19 plane and relay positions remain open. Infuser reaches y=-2..+1, two ordinary replaceable subfloor layers maintained and no free lapis/liquid; gateway requires open sky. All six clear-air rectangles are disjoint and kept explicitly clear from y=4 through y=23. A separate 4×15 open strip near the altar is reserved for expansion. Exact auxiliary and secondary mod station audit is NOT complete.

## Research gate
Astral upstream 1.21.1 tree 0ec3ff0fcb202d8035fc5aef409681fe402e1d0b, reported 2.0.3; pinned build 59 not independently inspected. The illumination altar is not an ordinary crafting recipe: `recipe/focal_combine/altar_illumination.json` uses four raw marble and crafting table at a focal point (400 ticks). Do not grant a ready altar or worldgen focal point. Lightwell/focus relay are later altar crafts in illumination research; Lumen crystallizer/array are resonance research. Tome uses book + aquamarine tag; leave acquisition intact. Enigmatic Legacy+ exact requirements, external focal-point access, auxiliary/collector network geometry and site starlight conditions need validation. Source paths linked in ../astral-donor-reconciliation.md.

## Rebuild and QA
`node build_observatory.mjs` writes `prefab-extension/celestial_observatory.nbt` (DataVersion 3955, gzip compressed) and `local-audit.json`; no npm dependencies. Preserved run-length voxel grid in grid.json, exact generator compares against it; NBT encoder and independent parser were executed against all 35852 blocks before commit. Gzip wrapper and filesystem stages require a local Node run; no binary runtime deployment. Static report: static-audit.json. Twelve stair tread clearance checks and 12 ground-route points passed on authored geometry. Actual collision, weather, sky above template, lighting/spawn safety, block entity default initialization, terrain/foundation, rotation, offset, claim, owner material/size parity and exact recipes must pass live QA. Above ordinary 16–32 footprint and ~3000 block target; expanded envelope chosen to satisfy independent multiblocks; larger footprint/material cost not yet separately approved. No new dependency/pin/quest/reward or main-branch change. Original prototype not production approved.
