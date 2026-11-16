# Local keybinding inventory — 2026-11-16

[Modded-unbound records](modded-unbound-2aacf348.txt) provide **315 exact observed mod mapping IDs**, each assigned `key.keyboard.unknown` with an empty modifier field. This is the requested list for Default Options integration; it includes mod GUI/mouse/modifier bindings as well as abilities, rather than only obvious conflict keys.

## Provenance and checks

- Observed `key_*` records in the installed Prism client's `options.txt`, build `2aacf34893bc06f8048a7fabae644b3b91a1d1d4`, Minecraft 1.21.1 / NeoForge 21.1.265, 161 mod pins.
- 349 distinct saved mapping IDs; **34 vanilla IDs omitted**, leaving **315 modded IDs**. Of those modded entries, 114 were already unbound locally. No personal key assignments, other options, profile files or machine paths are published.
- [Vanilla identifier reference](vanilla-1.21.1-ids.json) derives from KeyMapping/ToggleKeyMapping constructor declarations in the exact-1.21.1 vanilla Options source used by local NeoForm compilation. Only identifiers are included; no upstream source/assets are redistributed. Essential attack/use/movement/jump/inventory entries and full reference presence were asserted.
- Verified distinct IDs, no vanilla overlap, all 315 output records explicitly unbound, and all modifier fields cleared. Candidate SHA-256 (LF/UTF-8): `2af3b812811c7461c7e5fd772068d1f8f4f53261986da9420a584926e94ede11`.

## Integration boundary

This is an **observed-options inventory and candidate list**, not `/defaultoptions saveKeys` output or a final loaded-registry certification. `options.txt` may retain stale mappings or omit registrations not yet saved. The vanilla identifier reference is source-backed, not the guide's requested standalone vanilla registry export. Thus this inventory reduces the blocker but **does not waive the final export gate** in [the targeted guide](../DEFAULT-OPTIONS-KEYSETS-ONE-BUILD-2026-11-16.md).

Default Options and KeySets are not in this installed snapshot; next-build additions may register more keys. Before claiming ALL modded controls unbound, obtain the final assembled client's and vanilla client's registered-key exports, diff against this candidate, regenerate with `scripts/generate_defaultoptions_keybindings.py`, and index the final `config/defaultoptions/keybindings.txt` in packwiz. Do not install this dated candidate as an exhaustive next-roster list without reconciliation.

No live settings/profile were changed; no mod added, client launched, test build made or full smoke triggered. Keep vanilla records omitted, preserve established player remaps, and use a separate empty temporary QA profile for first-run testing. All-unbound includes JEI, map, inventory controls and the KeyBind Bundles menu: fresh users must assign controls themselves per the current owner policy; do not silently exempt a mod.
