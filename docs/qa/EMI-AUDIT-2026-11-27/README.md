# EMI compatibility audit — 2026-11-27

JAWR's missing oven and ritual pages share a category-provider gap in the installed EMI-only stack. Mahou's five ordinary-looking powders remaining visible is consistent with the current owner policy, not evidence that Almost Unified lacks EMI support. This audit changes no gameplay, skills, viewer settings or installed files.

## Provenance and method

- Installed BUILD-MANIFEST source: `2fb5d1cf11fb7c6e2ccaec5824c36698888ef956`.
- Repository baseline: `a77d300d96a42ab229f41544c9c120e7cc2e0f38`.
- Seven installed JARs match their repository SHA-512 pins: EMI 1.1.24, EMIsemble 1.2.1, Extra Mod Integrations 1.0.3, Almost Unified 1.4.2, JAWR 0.5.39, Mahou 1.36.31 and KubeJS build 381. Exact filenames and hashes are in [audit.json](audit.json).
- Read-only archive/annotation scan of 258 top-level installed JARs and embedded JARs found 115 containers with provider annotation candidates. These are discovery candidates, not a runtime-loaded coverage count. Dependencies, conditional plugins, inherited behavior and actual displayed pages still require runtime checks.
- Inspected pinned native classes with JDK 21 `javap`; counted native JSON resources without loading a world. No client launch, reload, game input or server smoke occurred.

## JAWR: ten categories need coverage

JAWR's `WitcheryJeiPlugin.registerCategories` supplies all ten categories below. Its JAR has zero class references to the EMI API. EMIsemble has zero Witchery class references; it supplies selected native integrations, not a universal JEI bridge. Its installed `disabledMods = []` cannot enable an adapter it does not contain.

EMI does contain JEMI, but `EmiReloadManager$ReloadWorker` instantiates `JemiPlugin` only when `EmiAgnos.isModLoaded("jei")` succeeds. No JEI-named JAR is installed. Together with the provider inventory, this identifies the missing route for JAWR's native JEI categories in this installation. Merely having JEI classes inside a mod does not activate that route.

| Native JEI category ID (`witchery:`) | Native recipe type (`witchery:`) | Bundled JSON resources |
| --- | --- | ---: |
| ritual | ritual | 57 |
| brazier | brazier_summoning | 3 |
| brazier_passive | brazier_passive | 5 |
| cauldron_brewing | cauldron_brewing | 23 |
| cauldron_crafting | cauldron_crafting | 10 |
| cauldron_infusion | cauldron_infusion | 25 |
| distilling | distilling | 9 |
| oven | oven | 11 |
| spinning | spinning_wheel | 9 |
| symbology | programmatically generated | Not a JSON count |

The nine recipe-backed categories contain 152 bundled resources. The complete Witchery resource directory contains 314 JSON files, including vanilla crafting/smelting, six specialized crafting resources and a `legacy_landscape:void` resource. Those are **not** all missing custom categories. Symbology must not be dismissed because it has no corresponding JSON resources. Resource counts are not effective RecipeManager counts and may include conditional entries.

An EMI-only repair needs parity across all ten categories, including native catalysts, requirements, extra outputs/chances, ritual and symbology wrappers, and relevant component/subtype distinctions. Adding one oven recipe does not repair category registration. Adding JEI to activate JEMI would be a separate stack decision and has not been tested or performed here. Missing pages do not imply the server recipes were deleted.

## Mahou powders and material visibility

[Issue #592](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/592) explicitly holds dust-hiding work pending viewer selection and full native-consumer parity. The installed Almost Unified materials configuration equals the repository configuration, including `recipe_viewer_hiding: true` and the deliberate exclusion `mahoutsukai:powdered_(iron|gold|diamond|emerald|quartz)`.

Almost Unified's native `AlmostEMI` has both `removeEmiStacks` and `disableStack` routes, using `almostunified:hide` and `almostunified:emi_strict`. Ordinary eligible duplicates can therefore be hidden through native EMI support. The five excluded Mahou powders are intentionally outside that replacement path. `powdered_ender` and `powdered_eye` have no Neo counterpart and must remain available.

Existing `material_unification.js` retains native powder outputs and tool remainders while substituting selected material inputs. `mahou_neo_vitae_circle_bridge.js` supports five Neo dusts at native blank-circle interaction. The native Catalyst Exchange eligibility bridge tracked by [#57](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/57) is another distinct consumer path; its existence is not proof that every spell consumer and displayed recipe accepts Neo dusts.

Mahou already has a native EMI circle recipe class. `MahouCircleEmiRecipe` maps catalyst names to native powder item holders and constructs concrete `EmiStack` inputs, including the five powders plus ender and eye. No evidence here establishes alternate Neo dust display parity. Hiding the original entries now could obscure the ingredients the native pages actually present.

No broad blood, food or special-reagent unification is justified by this audit. Registered items, native mechanics and component-bearing stacks must remain intact. There is no `kubejs/client_scripts` hiding implementation in this baseline; none was added.

## Remaining acceptance and handoff

**Implementation-ready audit slice:** reproducible offline provider/resource census and the category-wide JAWR diagnosis are complete. The evidence supports a dedicated native EMI coverage repair workstream; this PR contains no repair implementation. Related owner reports: [#271](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/271), [#431](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/431).

**Explicit design blockers:** none for this audit. Viewer-stack selection and any later Mahou hiding change remain under #592's hold. Skill design and implementation were explicitly removed from this chat's scope.

**Pending B/C acceptance:** export the current effective recipe/category inventory after an adapter is included; check all ten JAWR categories, recipe/use navigation, catalysts, dynamic symbology, conditions and extra outputs against native behavior. Check all five Neo dusts in blank circles, native spell acquisition and Catalyst Exchange, including success/failure consumption and tool remainders; compare displayed ingredient alternatives and every remaining powder consumer before considering hiding. Check ordinary duplicate hiding while preserving ender/eye and special reagents. No gameplay PASS is claimed.

## Reproduction

Run `scripts/audit_emi_installation.py --minecraft <instance/minecraft> --java-bin <JDK-bin> --output <evidence-directory>` using Python 3.11+ and JDK 21. It reads the installed archives and config, verifies seven source pins, disassembles loader/hiding gates and emits sanitized JSON. It fails if those pins or expected native gates differ. It neither launches Minecraft nor writes into the instance. Paths and player identifiers are not emitted.

Validation: successful real-install execution (7 pins, 258 JARs, 10 JAWR categories, zero JAWR EMI API references, zero EMIsemble Witchery references); Python compilation, JSON parsing and whitespace check. Full native bytecode, JARs, logs and saves are not redistributed.
