# Ars 'n Spells — focused addon profile

| Field | Value |
| --- | --- |
| Manifest ID / mod ID | `ars-n-spells` / `ars_n_spells` |
| Parent | [Ars Nouveau](ars-nouveau.md) × Iron's Spells 'n Spellbooks |
| Pack pin | `ars_n_spells-3.3.5.jar`; SHA-512 `21f27911378853800129de4123b406a1b7dff2993a986f3c944761f059becac55509925f77b8f8ce49502dfc6747b397407454c85b9a339f6c003891d13400e2` |
| Source snapshot | [3.3.5 release work commit](https://github.com/otectus/ars-n-spells/commit/f94539f6fcccb6a4ed8b9041663944dfceb27627); the project maintains a separate NeoForge 1.21.1 port. The 3.3.5 changelog and 1.21.1 testing report cover the payment boundary, but no direct JAR/source hash comparison was done. |
| Status | **researching** — core bridge and spell export/binding route mapped; current pack config, full cross-cast interactions and client walk-through remain open. |
| Guide | Spell Loom and binding routes are taught in the addon README/JEI and pilot quest text; no Worn Notebook page title verified. |

## Mechanics and resource contracts

Ars 'n Spells bridges Ars spells and Iron's spell wheel. It adds more than a scroll craft: mana, scaling/progression, inscription/binding and casting all cross the mod boundary.

| Feature | Operation | Resource / output | Important constraint |
| --- | --- | --- | --- |
| Mana modes | Config/command supports `iss_primary`, `ars_primary`, `hybrid`, `separate`, and `disabled`. | In `iss_primary`, Iron's pool is authoritative and Ars casts drain it; in `ars_primary`, Ars mana is authoritative and Iron's spells drain it; `hybrid` shares bidirectional; `separate` splits costs; disabled preserves pools. | TMM's prior design preference is `iss_primary`; current effective server config is not verified here. Do not claim the pack is using it. |
| Cross-school progression | Iron's spell-power attributes scale eligible Ars spells; the addon exposes school/affinity integration. | Turns Iron's spell power and affinity into Ars spell effectiveness/progression where supported. | Exact attribute formula/registry and exclusions need version-specific inventory. |
| Spell Loom / transcription | Craft Loom; select a reusable Ars spell; put source in slot and blank target scroll; inscribe. A 3.3.5 carrier can target real Iron's blank scrolls as well as addon Blank Scrolls. | Creates an inscribed carrier; reusable source stays or is consumed according to chosen input. | Choose correct blank target; inspect/clear output before next inscription. |
| Binding | Use Iron's Inscription Table or Ars *Spellbook Binding* ritual. Ritual method: one carrier and one book within 3 blocks, no other dropped items; empty-hand activation. | Adds an Ars spell entry to Iron's spellbook; carrier is consumed. | Full/duplicate book can refuse the bind. A Loom/scroll item is not proof of binding. |
| Cross-casting | Select bound Ars entry in Iron's native spell wheel and cast. | Cost passes through configured shared/split resource policy; can have added overhead/cooldown. | A cost or payment failure is distinct from a spell effect failure. 3.3.5 specifically repairs Iron payment boundary behavior and cancelled casts. |

## Walkthrough and coverage

1. Craft Spell Loom; JEI recipe. Create a safe low-cost Ars spell.
2. Produce a blank carrier; inspect the selected school/name, source and target slot. Transcribe and collect the output.
3. Bind through the Inscription Table or follow the complete Binding ritual item layout.
4. Select the new spell in Iron's wheel and cast a harmless spell; observe mana debit and effect separately.
5. Test one cheap and one higher-cost cast; verify config mode and cooldown, then retry a cast cancelled before the effect.

The pilot has four separate milestones (Loom, transcription, binding, actual cross-cast) with addon advancements for the latter three. Its exact detector IDs are listed in `manifest/ars_quest_pilot.json`; server registry probing is not client acceptance.

## Evidence and limits

- Pin/hash and all four advancement IDs: pilot manifest.
- [3.3.5 changelog](https://github.com/otectus/ars-n-spells/blob/f94539f6fcccb6a4ed8b9041663944dfceb27627/CHANGELOG.md); [NeoForge 1.21.1 testing report](https://github.com/otectus/ars-n-spells/blob/port/neoforge-1.21.1/docs/3.3.5/testing.md); TMM's source-targeted [MXP integration PR #45](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/45) provides additional code-path context but remains a separate candidate.
- Not run: TMM config mode; one shared-pool cast; cross-cast from each carrier type; cooldown; blocked target; book capacity/duplicate; second player and multiplayer payment.
