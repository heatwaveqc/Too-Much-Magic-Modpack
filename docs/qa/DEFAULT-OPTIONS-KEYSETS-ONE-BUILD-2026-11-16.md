# First-run mod keybinding defaults — Default Options and one-build KeySets evaluation

**Status: mod pins and introductory quest authored; all-mod unbinding is BLOCKED until a full loaded-client keybinding inventory exists. No one should mark the requested all-mod-unbound behavior as delivered just because Default Options is installed.** This is a next owner-authorized client-test requirement, not permission to launch a full smoke or Prism build now.

## Goal and verified upstream behavior

- Preserve **all vanilla** controls. Leave every vanilla key absent from the pack's Default Options keybindings file; do not set their defaults to unknown.
- Start **all nonvanilla mod-added** controls at `key.keyboard.unknown`. Let the user assign whichever controls they personally use. Do not periodically reset them; don't distribute a prefilled player `options.txt` or modify the user's profile.
- Default Options 21.1.8 reads **`config/defaultoptions/keybindings.txt`**. Its 1.21.1 source [KeyMappingDefaultsHandler.java](https://github.com/TwelveIterations/DefaultOptions/blob/1.21.1/common/src/main/java/net/blay09/mods/defaultoptions/keys/KeyMappingDefaultsHandler.java) parses individual records `key_<mapping name>:<key name>:<optional modifiers>`. It has **no wildcard for all modded controls**. On a fresh client it replaces registered defaults but skips personalized loaded options. Omission of vanilla entries keeps vanilla defaults.
- Example *only*: `key_key.ability_1:key.keyboard.unknown:`. Do NOT commit fabricated entries; inspect exact registered IDs first. An empty keybindings.txt does not implement the requested behavior.
- Balm is already present; Default Options is client-only. KeySets v1.3.0 is pinned only for a **single targeted acceptance run**, not permanently approved. Existing Controlling and KeyBind Bundles stay installed.

## Required one-time inventory/generation gate before a complete first-run default

1. In an **owner-authorized, fresh, disposable** client carrying **all final mod JARs**, run `/defaultoptions saveKeys` (if supported on the installed screen/client) and copy the resulting `config/defaultoptions/keybindings.txt` to an **external** `loaded-keys.txt`. This records exact registered IDs, not assumptions from source mod filenames. The command may require singleplayer and should be run only in a disposable world. If it is inaccessible, obtain the equivalent mapping dump from a client-side test helper; do not guess.
2. Obtain a second exported `vanilla-keys.txt` from a **vanilla 1.21.1 controls registry** using the same format. Verify that attack/use/forward/back/jump/inventory are present. A modpack export `options.txt` alone is NOT a reliable vanilla whitelist because it may already contain mods' mappings.
3. Run `python3 scripts/generate_defaultoptions_keybindings.py --loaded path/to/loaded-keys.txt --vanilla path/to/vanilla-keys.txt --output config/defaultoptions/keybindings.txt --write` in the source tree. The generator errors on invalid/empty exports and leaves every vanilla entry out. It emits one unbound default per modded mapping. Review diff, index file in packwiz, and update its SHA256 plus pack.toml index hash. Run again with `--check` to fail on any omissions. Repeat generation if the final mod roster changes.
4. Only after this file is complete and hash-indexed may the assembled build claim **ALL modded keys unbound on first launch**. The quest text describes intended release behavior, not a proven current client state.
5. Test separately with no prior QA profile, then with personal bindings, restart, and import a later QA instance. Existing Windows Prism helper `scripts/tmm_qa_preferences.ps1` copies prior `key_*` values into test `options.txt`; **this is deliberate personal preservation but masks fresh default testing**. For isolated test, set `TMM_QA_PROFILE_DIR` to a new temporary empty folder (do not delete the owner's `%LOCALAPPDATA%/TooMuchMagic/TestPreferences`).

## One-shot KeySets targeted test and remove-if-bad decision

- In this one test build **with** KeySets, ensure Controls and KeySets GUI open and register all controls; no client crash, conflict with Controlling/KeyBind Bundles, or unexpected remap on launch, restart or world change. Verify a fresh client starts modded unbound once keybindings.txt is complete; manually bind Jade and one Dragon Survival or magic ability and verify both persist.
- Save and switch profiles using KeySets; test a custom binding across game exit and Prism QA reimport, including when KeySets is disabled. Ensure KeySets does not unexpectedly override Default Options' own first-run behavior or the QA preferences helper. No need to add quest presets or repeatable quests.
- **Decision immediately after one targeted run:** KEEP KeySets only on a recorded explicit PASS covering UI, bind persistence and no destructive interactions. Any FAIL/BLOCKED/INCONCLUSIVE => **REMOVE** `mods/keysets.pw.toml` from next build and `index.toml`, change `manifest/mods.json` entry `enabled_in_pack=false` (retain trial history), recompute `pack.toml`, and document outcome. Do NOT automatically rerun KeySets or retain it indefinitely because the test could not be run. Keep Default Options regardless unless it itself fails.
- User changes in `options.txt` and any KeySets per-user profile files are private. Never commit/export them. If removing KeySets, document a safe player profile migration/backup and preserve the underlying vanilla/Default Options bindings. The test verdict is NOT yet in.

## Acceptance labels

`PINNED`: Default Options 21.1.8 and KeySets 1.3.0, source metadata.
`BLOCKED`: exhaustive modded key inventory and generated first-run `keybindings.txt`.
`NOT RUN`: actual first-run client behavior, persistence, KeySets one-run pass/fail decision, removal if failed.


## Local inventory supplied — 2026-11-16

The [local binding inventory](keybindings/README.md) supplies [315 modded-unbound records](keybindings/modded-unbound-2aacf348.txt) observed in installed build `2aacf34893bc06f8048a7fabae644b3b91a1d1d4`. The 349 saved bindings were separated using 34 exact-1.21.1 vanilla identifiers, independently derived from vanilla Options constructors; personal assigned values were discarded. Every candidate mod record uses `key.keyboard.unknown` and an empty modifier field.

This provides the requested concrete list but is **not a final all-roster certification**: current client options are not a registered-key export; vanilla reference is source-derived, not a standalone vanilla export; Default Options/KeySets and other next-build additions may add keys. Reconcile stale/missing IDs against the final client/vanilla exports before generating and indexing the runtime file. First-run behavior and KeySets trial remain NOT RUN, and the gate above remains in force. No live options/profile was modified.
