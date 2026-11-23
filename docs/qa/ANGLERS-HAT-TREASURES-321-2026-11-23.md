# Angler's Hat treasure additions — frozen donor #321

**Selected source-only integration (not in-world verified).** Base `test/full-integration@3200a9ba27966d6d1ce4de3fe3aa1fefbe6a7f26`. Donor `feature/angler-hat-rare-materials` exact commit `93ee0e7db4d0cd0426266392d903f179ee97c484` (PR #321). Recovered by Gravekeeper #576; no code or ownership change to food or Progression.

## Changes

- Preserve seven native/TMM treasure tag entries, `replace:false`, the optional `#c:raw_materials` and optional Elder Dragon Dust entry. Add only `draconicevolution:draconium_dust` and `tmm:eldritch_logic_core` at end.
- The prior JSON already existed as unindexed repo source; this promotion **also** registers it in Packwiz for the first time, so its existing entries become part of the installed datapack as well. This is intentional but requires observing actual effective loot and tag behavior.
- Eldritch Logic Core is already registered in `kubejs/startup_scripts/eldritch_logic_core.js` and its miner recipe returns the exact ID. Does not create an earlier crafting path or alter native weights, fishing RNG, loot tables, or abilities. Draconium Dust is from pinned Draconic Evolution.
- Relock index/pack SHA-256 from the exact current integration base, no stale directory replay.

## QA boundaries

- **Type A:** dedicated Python 3.12 source contract plus existing Repository CI / Integration Preflight, verifying tag file is actually indexed and the Logic Core ID is registered. No claim of loaded native tags.
- **B/C after separate owner authorization:** Confirm Angler's Hat resolves all nine tag members including both additions, and actual fishing can yield them at native eligibility. Validate other item pools remain available, no dupe/loot inflation, raw-materials tag expansion and drop behavior, and Core stays Eldritch-gated by normal survival sources.
- **No** Integration Assembly, server boot, Prism package, client launch, or gameplay PASS performed by this change.
