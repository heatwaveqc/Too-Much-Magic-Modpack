# Dragon Survival + Jade + FTB client UI collision triage (2026-11-16)

**Status:** source-confirmed Jade keyboard collision; GUI placement workarounds documented; **NOT client-tested**. No gameplay code, client settings, or modpack pin changed in this branch. This report does not authorize a full server smoke/build.

## A. Jade overlay appears to disappear after choosing Sea Dragon

**Confirmed default key collision, not a confirmed Sea Dragon rendering defect:**

- [Jade official mod description](https://modrinth.com/mod/jade) documents **Keypad 1 = Toggle Overlay** (Keypad 0 opens Jade settings).
- Dragon Survival 1.21.1 [`Keybind.java`](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/1.21.1/src/main/java/by/dragonsurvivalteam/dragonsurvival/input/Keybind.java) defaults **ABILITY1 = GLFW_KEY_KP_1**, ABILITY2–4 = Keypad 2–4.
- Pressing Keypad 1 in dragon form can activate the ability *and* toggle Jade off, depending on input routing / actual user mappings. Selecting Sea Dragon does **not** itself prove the species disabled Jade.

**Fast manual check:** target an ordinary block, press Keypad 1 once, and see if Jade returns. Also inspect Options → Controls → Key Binds → Jade: Toggle Overlay and Dragon Survival: Skill #1. If the mappings collide, move Jade's toggle to an unused key (and, optionally, move its other keypad shortcuts). Confirm after re-entering the world and swapping dragon forms. If Keypad 1 does not restore it, collect client latest.log and Jade settings rather than assume a keybind collision is the whole problem.

**Policy:** Do not forcibly edit every tester's `options.txt` to implement an assumed preferred key. The full-integration Prism QA builder intentionally forbids shipping invented `key_*` mappings, and the existing local QA preference sync preserves user remappings. A separate opt-in/new-user default policy is needed before changing this.

## B. Dragon inventory top-left overlaps FTB sidebar

- Dragon Survival's [`DragonInventoryScreen.java`](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/1.21.1/src/main/java/by/dragonsurvivalteam/dragonsurvival/client/gui/screens/DragonInventoryScreen.java) positions its native tab strip with `TabButton.addTabButtonsToScreen(this, leftPos + 5, topPos - 26, ...)`; the tab widgets are positioned directly in [`TabButton.java`](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/1.21.1/src/main/java/by/dragonsurvivalteam/dragonsurvival/client/gui/widgets/buttons/TabButton.java). There is no placement option for these tabs in the inspected Dragon Survival client config; a **direct DS tab move requires client-side code/compatibility patch**.
- **Available no-code workaround:** FTB Library 1.21.1 sidebar buttons can be repositioned or hidden. [FTB official documentation](https://docs.feed-the-beast.com/mod-docs/1.21.1/mods/suite/Library/) says **right-click any FTB sidebar button** to enter edit mode. This changes the flexible FTB controls instead of Dragon Survival's hardcoded tabs. A pack creator can supply a reviewed `defaultconfig/ftblibrary-client.snbt` layout; the exact stock config needs to be captured/validated from the pinned 2101.1.x client before shipping it. Do not guess the serialized layout.

## C. Two top-right indicators under FTB Chunks minimap

- [Dragon Survival `GuiMixin.java`](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/1.21.1/src/main/java/by/dragonsurvivalteam/dragonsurvival/mixins/client/GuiMixin.java) adds ability-effect indicators to `Gui.renderEffects` at `guiWidth - 25*effectCount`, top Y=1, which are hard-coded. These **may** be the two controls reported as top-right buttons; their identity has not been confirmed by screenshot.
- FTB Chunks defaults its minimap to top right; its [1.21.1 client settings](https://docs.feed-the-beast.com/mod-docs/1.21.1/mods/suite/Chunks/config/) let users reposition it. Open the map with **M → lower-left gear → Minimap → Position**, and try bottom right (or another non-conflicting location). The file is `config/ftbchunks-client.snbt`, with a possible local override in `local/ftbchunks-client.snbt`. This is a workaround moving FTB's minimap, not a claim that Dragon Survival's two icons have been relocated.
- FTB Chunks normally moves vanilla potion-effect icons to the left of a top-right minimap, but Dragon Survival appends its own indicators through a mixin, which could explain why these still overlap. **This interaction is a hypothesis pending client verification.**

## Next live-client acceptance (do not mark PASS from code inspection)

1. Record actual installed Dragon Survival and Jade filenames and the exact ZIP/build SHA. `test/full-integration` currently has the source for the Dragon Survival v2.0.74 pinning script, but **does not itself contain `mods/dragon-survival.pw.toml`**: do not confuse pinning intent with an installed client JAR.
2. Before pressing dragon ability keys, look at a named vanilla block and record whether Jade appears. Select Sea Dragon, look at the same block, then press Keypad 1 and record changes. Test with Jade's toggle explicitly rebound to an unoccupied key.
3. On the custom dragon inventory, screenshot the FTB sidebar and Dragon Survival tabs at the same GUI scale. Right-click an FTB sidebar button to test edit mode and move buttons away; reopen inventory.
4. Screenshot the two top-right indicators with the minimap visible; hover them or inspect their effects, then move the minimap via FTB Chunks settings and observe. Distinguish clickable buttons from passive ability-effect icons. Check fullscreen/windowed and GUI scale 2/3.
5. If both no-code paths work, document the chosen layout and capture the exact client-side FTB config output before shipping a pack default. If **actual Dragon Survival controls** require repositioning, implement a targeted, optional NeoForge client-only compatibility patch against the pinned v2.0.74 build; preserve gameplay logic and client preferences.
6. Mark results as PLAYER PASS only after client confirmation. This triage branch has **no live GUI proof**.

**Avoid:** distributing guessed config fields, committing a user's personal options.txt, modifying the user's local Prism preference store, or treating a full server smoke as a client HUD validation.
