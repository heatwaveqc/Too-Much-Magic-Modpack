# Parchment fragment acquisition (owner decision 2026-11-17)

Tracking: [#318](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/318). Draft, **not integrated or runtime-tested**.

- `improvableskills:parchment_fragment`: disable only native chest generation, not ordinary skill scrolls or abilities. Keep its native shapeless seven-paper reverse recipe, accepting mana-powered paper multiplication.
- Mana infusion: one ordinary paper and 100,000 mana, one fragment. No alchemy catalyst. Cost provisional.
- Fishing: separate one-roll, 10% bonus pool on vanilla fishing treasure; preserve the existing loot. Verify Aquaculture coexistence and actual pool behavior.
- EMC: fixed provisional 224 (seven paper at 32 each); verify both live final values after all mappings; if paper's final value differs, adjust fragment to seven times paper rather than altering paper. Preserve learning/transmutation; no exemption.
- Proposed Gateway rewards, pending reconciliation: 1 fragment on Necromancer, Pyromancer, Cryomancer, Cultist successful completion and 2 on Archevoker. Preserve XP, waves, recipes, ordinary mob drops and failure behavior. No per-wave rewards or Newbie exclusions. The selected gateways and drop amounts are initial tuning choices, not independently specified by the owner.

## Integration handoff

The B19 Gateway files and `manifest/gateway_required_mobs.json` are supplied by the existing frozen `b19-required-mob-gateways` stream (`978c9ede228cc5da2678895c515fc6de28760101`). The five JSONs are generator-owned. **Do not hand-edit them or replay the donor.** The integration writer should extend the current `scripts/build_progression_gateways.py` generator with an optional per-target completion-reward count, add that count to the five targets of the current effective manifest, update its XP-only policy to explicitly permit these completion rewards, regenerate narrowly and preserve all other files. Use `gateways:stack` / `stack: {id: improvableskills:parchment_fragment, count: N}` as used by the existing Caerbannog gateway. Reconcile stream ownership, packwiz index/hashes and test with the B19 validator. Do not blindly copy stale files. The four staged runtime files also require packwiz indexing by that writer. Do not overwrite other ProjectE EMC or Improvable Skills config entries if another PR adds them first. Confirm exact pinned ConfiguredLib and ProjectE parsers using JARs; upstream source-informed format is not gameplay acceptance.

## Deferred pack-wide energy economics standard (owner requested)

Define explicit Mana↔EMC and FE↔EMC reference exchange rates before final balancing. Use reproducible sustained generation, resource consumption, native conversion efficiency and actual loaded conversion recipes; account for Thermalily changes, Psi/Hex FE, and ProjectE Energy Converters. Distinguish reference valuation from any actual bidirectional conversion mechanics, document units/rounding and exceptions, audit profitable conversion cycles, then revisit provisional 100,000 mana pricing. No numeric rates chosen yet.

## QA

A: inspect pinned JAR/config format, verify recipe/schema, source and packwiz hashes, normal pages unchanged, reverse recipe present, exactly five selected Gateway final rewards after regeneration, final mapped EMC parity. B: verify in-game mana debit (less than 100k fails, sufficient mana succeeds) and EMC values using exact commands only after confirming their syntax. C: treasure fishing (including Aquaculture), normal fishing/junk and ordinary chests (no fragments), Gateway success/failure, seven-paper craft, normal skill scroll/ability use in both modes; ensure new fragment acquisition is only the three intended routes before ProjectE. No Integration Assembly, Server Boot Smoke, Prism package or live testing authorized by this record.
