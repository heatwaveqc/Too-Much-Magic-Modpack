# Progression skill screen blur regression — #506

**Source symptom:** 2026-11-22 owner Experimental client reported that all skill-tree headings, nodes and wallets were obscured like a missing modal, even after graduating from Newbie to Normie (historically called Regular). The source code builds a custom full-screen GUI; no modal was declared. This is the graphical Progression screen, **not FTB Quests' quest chapters**.

## Root cause — verified source ordering

At the frozen `dev/progression` baseline, `ProgressionScreen.render` explicitly called `renderBackground(g,mx,my,partial)`, painted its own near-opaque panel, headings, graph, wallet, tooltip, then invoked `super.render(g,mx,my,partial)` to draw the ordinary vanilla button widgets. In **Minecraft 1.21.1**, `Screen.render` invokes `renderBackground` **again** before drawing its widgets. The base `Screen.renderBackground` runs the game renderer's blur and menu darkening paths. This second pass happens **after the tree has already been painted**, so it can blur/obscure the foreground UI. A single `renderBackground` call at the beginning was not the only issue; `super.render` was applying it a second time.

Source cross-check:
- [1.21.1 Screen.render source](https://github.com/Soumeh/1.21.1-Deobfuscated/blob/main/minecraft/src/net/minecraft/client/gui/screen/Screen.java): renderBackground followed by child drawables.
- [1.21.1 Screen.renderBackground](https://lexxie.dev/neoforge/1.21.1/net/minecraft/client/gui/screens/Screen.html): includes `renderBlurredBackground` plus menu background.
- [NeoForge renderer issue #1504](https://github.com/neoforged/NeoForge/issues/1504): documented 1.21.1 background-blur/layer interaction; fixed in 21.1.84 (the pack pins later NeoForge). It is corroborating background, **not proof that this exact upstream bug persists**.

## Surgical code change

In `ProgressionScreen` only:
- Override `renderBackground` with an intentional no-op. This prevents the implicit `Screen.render` background/blur pass from painting over foreground content while still allowing vanilla child button rendering and narration.
- Remove the redundant early `renderBackground` call, and draw a fully **opaque** Progression canvas at the beginning of `render`, followed by its panel, headings, graph and wallet.
- Preserve `super.render` at the end so the existing buttons stay on top and keep their original action/mouse behavior. Preserve server snapshot paths, enrollment, rank, purchases, choices, navigation, pan, scissor, tooltips and MXP accounting unchanged.
- Add `ProgressionScreenLayerContractTest` source-level assertions to catch inadvertent reintroduction of blur, double background, missing opaque panel or removal of normal widgets.

**What this does not change:** the Core path's source JSON is registered but empty, unlike Monk and Hunter; its skill content is a separate #455 / PR #379 integration matter. Owner reported Ars Elemental Conjure Block and Break Block already yield MXP after graduation, so absence of chat popups is not a known XP failure from this symptom.

## QA ownership

- **Source/Module CI:** Progression engine branch; run JUnit/Gradle compile and tests through existing `.github/workflows/progression-core.yml`. This checks ordering, not GPU pixel output.
- **Integration:** PR targets `dev/progression` for the designated Progression writer, then reviewed promotion to `test/full-integration`. No direct merge/pin/manifest changes from this writer.
- **Gameplay Type C, next authorized client:** open tree immediately as Newbie and after Normie graduation; read headings, nodes and wallets without any screen blur; hover, select, pan, scroll, switch tree, learn and respec; ensure buttons/tooltip legibility and world rendering restored on exit and reopen. Check both standard and altered shader/resource-pack states if available. The empty Core tree must say no configured skills rather than being misdiagnosed as a visual issue.
- **Status:** Source fix only until in-client test. No full modded smoke, world boot or Prism package authorized by this PR.
