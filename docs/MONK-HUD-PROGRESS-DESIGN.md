# Monk graphical deed progress HUD — required for full release

**Decision:** 2026-11-14. **Status:** approved target for full deployment; **not implemented** in the First Sunset test build. The user confirmed that the current text counter works for now. Do not block continued Monk gameplay prototyping on client HUD polish.

## Source-backed reference (not assets to reuse)

The original [Monk Mod 1.4 `HUDProgress.java`](https://github.com/rwtema/Monk-Mod/blob/master/src/main/java/com/rwtema/monkmod/render/HUDProgress.java) drew a **bottom-right circular/radial progress indicator**. Its [`MessageProgress.java`](https://github.com/rwtema/Monk-Mod/blob/master/src/main/java/com/rwtema/monkmod/network/MessageProgress.java) sent numerical current/max progress to the client. The indicator animated toward the target angle, faded in and faded away when updates stopped. The original source and textures are third-party content; **do not copy its code, circle texture or other artwork**. Build original TMM rendering and graphics for NeoForge 1.21.1.

## Visual and behavior contract

- Small **circular icon / radial filling progress ring**, default position **bottom right**; preserve visual emphasis on an increasing, meaningful completed fraction. Make it a **distinct Monk-style symbol** with original assets, not a copied original texture.
- **Every Monk deed that has measurable progress** can use this indicator, not just sunset meditation: consecutive gaze duration, bare-handed block-break counts, enemy defeat counts, distinct-food discovery, and optional set-piece challenges where meaningful.
- Draw `progress / maximum` clamped to `[0,1]`. Smoothly animate changes while retaining accurate server-reported amount. When a condition breaks, animate or immediately clear the bar as appropriate to the deed's rule; never display retained progress for a reset-on-failure deed.
- Fade in when the deed becomes active; briefly display a full circle on completion, then fade out. After ineligibility, interruption, dimension transition, disconnection, or task switch, remove stale progress. Don't show it to unrelated players.
- Respect GUI scale, safe-area padding, HUD hiding (F1), full-screen screens, and overlays from other installed mods. Provide a config to adjust margin/scale/visibility or disable the ring if it collides with other HUD elements. Test 16:9, ultrawide and common UI scales.
- **Text fallback is retained** for accessibility, disabled-overlay clients, and current development builds. In normal production visual mode, optional text should be concise or hidden to avoid simultaneous clutter. Where helpful, include a short localized task description and accessible numeric count (not color alone).
- **Server owns the truth.** Existing server task handlers compute progress, completion, eligibility and rewards. A small S2C progress update has task identifier, progress, max, and state (active/reset/completed); client interpolation is purely graphical and must NEVER award levels, grant skills, or extend a broken streak.
- Use a **single generic HUD progress interface** fed by the existing faction task/deed framework, not a separate per-ability icon renderer or a second experience tree. Rate-limit progress packets, send immediate reset/completion, and use server snapshots after client login/dimension changes where necessary.

## Implementation steps (client-only renderer, one shared network bridge)

1. Write original circular icon assets for TMM, plus a NeoForge 1.21.1 client GUI overlay with bottom-right placement and a smooth fill fraction.
2. Introduce a small validated S2C packet on the Monk mod's existing event/task system; the server can reuse the Sunset Initiation 0–200 count to pilot it.
3. Wire the same interface to future Monk progression tasks (breaking blocks, enemies, food discoveries) without creating separate HUD systems.
4. Add preference toggles for ring/text, scale/offset and optional progress text. Keep current actionbar counter until the renderer passes integration tests.
5. Verify client and dedicated-server class separation, no classloading of Minecraft client graphics code on the server, reload/logout/reset and multiplayer isolation.

## Acceptance tests

- First Sunset displays 0–100% filling circle in bottom right; missing an uninterrupted tick/looking away resets correctly; completion briefly fills then fades.
- Current text counter remains functional when overlay is disabled, including localization.
- No false completion from lag or a disconnect, no duplicated circles from concurrent tasks, no stale UI on respawn or dimension change.
- Generic block/enemy/food task progress reuses the same interface; no extra UI engine.
- Doesn't overlap major pack HUD elements at common GUI sizes; all artwork is independently created.

**Priority:** Required **before the first public/release deployment**, not required to declare the current First Sunset functional test a success.
