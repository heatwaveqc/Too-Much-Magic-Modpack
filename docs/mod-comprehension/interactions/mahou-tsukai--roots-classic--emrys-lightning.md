# Roots lightning → Mahou Staff of Emrys

| Field | Value |
| --- | --- |
| Participants | [Mahou Tsukai](../mods/mahou-tsukai.md)1.36.31; [Roots Classic](../mods/roots-classic.md)1.5.10 |
| Classification | Native upstream event path, unverified in TMM; no adapter required by inspected path |
| Evidence | Both exact pinned artifact hashes/classes in linked profiles; generated target NeoForge21.1.265 Minecraft LightningBolt source checked as supporting API evidence |
| Status | Source-supported candidate, no client/world test |

## Complete event path

Roots Poisonous Potato component costs base24 Terra per release (divided by efficiency+1) and creates vanilla LightningBolt with visualOnly=false at ray-hit location. Target MC LightningBolt enumerates alive entities in strike region and calls NeoForge onEntityStruckByLightning before thunderHit. Mahou's subscribed EntityStruckByLightningEvent handler accepts server Player with positive-strengthened exact Stick in **offhand** and DISABLE_EMRYS=false, then replaces offhand stack with Staff of Emrys. See [artifact acquisition](../mods/mahou-tsukai-artifact-acquisition.md) and [Roots spell inventory](../mods/roots-classic-pinned-spells-items.md) for prerequisites.

This could replace waiting for natural weather with an intentional magic action. It does not waive Roots powder/staff/Terra or Mahou Strengthening (native50 mana) and strike eligibility. It does not require sharing Terra and Mahou currencies. No claim that mere lightning-like particles or another mod's electric damage produces the event. Bring one Stick: conversion replaces whole offhand stack.

## Positive and boundary acceptance (not run)

In a disposable world with exact artifacts/effective configs, two cooperating test players prepare the eligible strengthened offhand Stick and Roots real-lightning spell. Isolate a safe area, record inventories/resources, target strike location close enough for intended Player to be in native strike region and observe actual event/output. Positive observable is Staff replacing Stick plus native Mahou trigger, not just lightning appearance. Record health/fire/terrain and resource changes.

Repeat with main-hand Stick, Strengthening0, disabled Emrys and a visual-only bolt test fixture where independently authorized. Those must not be counted as the same positive path. Test event cancellation/protected region: cancellation/delivery order may change whether Mahou handler runs; no bypass is established. Do not add a spell/fixture or change production config in this research pass.

## Quest/pack decision

Optional cross-magic tutorial only after acceptance: cite Roots component page and Mahou Emrys/Strengthening pages, teach exact hands/event and loss risk. Staff possession detects result, not which lightning source was used. XP plus mundane survival supplies is appropriate; awarding Staff would skip acquisition. Actual survivability, claimed terrain, client casting/range and event ordering remain open. No runtime/quest implementation or balance approval follows.
