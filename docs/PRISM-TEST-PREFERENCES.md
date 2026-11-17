# Prism personal test preferences — private QA only

**Scope:** developer/tester Prism instances built by `scripts/build_full_integration_prism.py`, Minecraft **1.21.1** (`options.txt` data version **3955**). **Windows PowerShell 5.1** is supported; other platforms must disable the personal sync commands and may copy `options.txt` manually. This feature is not part of the packwiz lock, config folder, or a public modpack.

## Why

Each new Prism ZIP import creates a separate Minecraft directory. Keybindings, GUI size and volume normally live in `minecraft/options.txt` *inside that directory*, so newly imported QA builds do not automatically inherit settings from an earlier instance. Reimporting a test pack should not require redoing dozens of controls.

## New tester experience

1. Import **Too-Much-Magic-Full-Integration-Prism-UNSTABLE**. This QA ZIP alone supplies a minimal `minecraft/options.txt` with safe starter values: GUI scale **3**, master volume **55%**, music **20%**, weather **55%**, ambient **65%**. It also sets vanilla `onboardAccessibility:false` to skip the one-time narrator/accessibility welcome screen on every fresh QA import, without setting the narrator mode or hiding the Accessibility menu. Imported narrator preferences remain intact. Public release defaults are unchanged. It does **not** pretend to know your personal key bindings; those use Minecraft/mod defaults until captured.
2. **Optional one-time step to keep your current bindings:** In Prism, right-click the *new* test instance → **Folder** → `minecraft`; double-click `IMPORT-OLD-TMM-PREFERENCES.cmd`. Select the `minecraft/options.txt` file from your previous configured Prism instance. Do this once, before your next launch. It does not change the old instance.
3. Launch normally. `PreLoadCommand` merges your saved bindings, volumes, chat scale and GUI size **before** the existing packwiz `PreLaunchCommand`. After you close the game, `PostExitCommand` saves any changed values. Future QA ZIP imports use the same local profile automatically.
4. A shared profile lives at `%LOCALAPPDATA%\TooMuchMagic\TestPreferences\options-1.21.1.txt`. GitHub CI cannot read it, and it is **not uploaded** or embedded in future ZIPs. Deleting this file resets the shared personal profile, not game worlds.

The saved profile includes `key_*` for vanilla and any installed mod, `soundCategory_*`, `guiScale`, selected chat UI sizing/opacity and sound-accessibility settings. **It does not share** world saves, server addresses, resources, language, game account, UI mod config files, graphics/performance settings, `resourcePacks`, or other `config/` values. Packwiz can still install/change mod-specific config files when the pack version changes.

**Safety:** The helper requires Minecraft 1.21.1's version marker, limits file size, merges only approved keys and leaves unknown settings alone. An absent or invalid preference profile must never prevent a launch. Test worlds and production worlds remain separate.

## Launcher command contract

In the **QA ZIP only**:

- `PreLoadCommand`: `powershell.exe -NoProfile -ExecutionPolicy Bypass -File "$INST_MC_DIR/tmm-qa-preferences.ps1" -Mode apply -OptionsPath "$INST_MC_DIR/options.txt"`
- `PreLaunchCommand`: unchanged hash-pinned packwiz installer
- `PostExitCommand`: same local helper with `-Mode save`

The scripts are bundled at `minecraft/` level of the **experimental Prism ZIP**, not indexed in packwiz. `scripts/prism_qa_options.txt` is the source of first-import options. These are stable seed values, not an assertion about the tester's existing choices.

Prism offers a manual fallback as well: right-click an existing instance → Copy Instance → enable **Copy game options**. For an imported ZIP, copying only the old `options.txt` from a stopped instance is also fine, but the shared profile removes that repeated step. Always close Minecraft first before modifying its options.

## Release gate — mandatory removal

The **public/release client builder must never reuse this QA configuration unchanged**. Its acceptance check must assert all of the following are absent:

- `minecraft/options.txt` with the personal QA seed.
- `minecraft/tmm-qa-preferences.ps1` and `minecraft/IMPORT-OLD-TMM-PREFERENCES.cmd`.
- `PreLoadCommand` / `PostExitCommand` referencing local tester profile sync.
- Any personal preferences from `%LOCALAPPDATA%`, `options-1.21.1.txt`, or another machine.

Public packwiz will continue distributing approved mod configs only. Public users choose their own controls, volumes, screen scale, and saves. Public release gates are independent of the private QA profile.

## Verification

`tests/test_tmm_qa_preferences.ps1` checks import, automatic apply, post-exit save, mod keybind retention, preserving current resource/graphics settings, excluding former server names, and rejecting other Minecraft versions. GitHub's **Personal QA Prism Preferences (Windows)** workflow runs those tests. The comprehensive build separately verifies the bundled ZIP assets and the pinned packwiz source; neither automated check proves that Prism's GUI performed pre/post hooks on the tester's machine yet.

## Default Options and temporary KeySets trial (2026-11-16)

A one-time **fresh-player** test of Default Options' all-modded-unbound defaults must use a newly empty temporary local QA profile via `TMM_QA_PROFILE_DIR`. Our existing QA importer deliberately preserves previously chosen `key_*` mappings and therefore masks the untouched-player baseline. NEVER delete the normal user's profile or reset established bindings. The authoritative trial plan, export-based generator, and single-run KeySets KEEP/REMOVE gate are in [the targeted client QA guide](qa/DEFAULT-OPTIONS-KEYSETS-ONE-BUILD-2026-11-16.md).
