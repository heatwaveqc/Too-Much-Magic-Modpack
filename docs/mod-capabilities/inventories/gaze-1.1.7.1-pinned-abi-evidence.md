# Gaze 1.1.7.1 — pinned JAR evidence already captured by TMM Progression

**Evidence status: PINNED JAR / ABI / RECIPE SUBSET VERIFIED; complete gameplay/gear/quest-source survey still open.** This record reconciles existing first-party research with the [Gaze Atlas dossier](../mods/gaze-malum.md). It does **not** mean this Atlas pass independently downloaded the JAR or ran Minecraft.

## Provenance and reuse

- Active [Packwiz pin](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/mods/gaze-a-malum-addon.pw.toml): Modrinth version `od4ltbRo`, filename `gaze-1.1.7.1.jar`, SHA-512 `7e9ecdcc996f376e280bf46f5c0cfd328fb6f1228b1dac469390031458e7a81ad56acff03d9bb6b57b8209586798ab443be1e91fa8fe56297a9e7c9451db03e2`.
- **Already inspected elsewhere in this repository:** [merged PR #375](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/375), frozen head `5498402e868676e7f74e0f1e9e8df8114c349bcd`; [its source audit and evidence log](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/5498402e868676e7f74e0f1e9e8df8114c349bcd/compat/progression/design/BATCH-E6C-MALUM-RITUAL-IMPLEMENTATION.md), [exact-recipes catalog](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/5498402e868676e7f74e0f1e9e8df8114c349bcd/compat/progression/src/main/java/io/github/heatwaveqc/tmm/progression/mxp/core/MalumRitualCatalog.java), and [hash-checking JAR/ABI script](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/5498402e868676e7f74e0f1e9e8df8114c349bcd/compat/progression/tools/e6c_astral_malum_ritual_abi.py).
- PR #375 reports hash-verifying the **exact** Malum 1.8.2 and Gaze 1.1.7.1 binaries and comparing all recipe-backed Geas results/counts against the exact catalogs. CI/static success is not client behavior or installed-pack acceptance. The source PR was merged to `dev/progression`; integration into a given TMM client build must be separately verified.

## Two exact Gaze Geas recipes — no guessed ingredients

The JAR-backed catalog identifies precisely **two recipe-backed Gaze Geas effects**. Importantly, the recipes live in Malum's `malum:soul_binding/` namespace; the *effect IDs* live under `gaze:`. Counts below are from the already audited catalog, **not** claims about item identities, extra-input order in player UI, survival accessibility, or effective current tags.

| Soul Binding recipe | Geas effect ID | Main input count | Extra input counts | Spirit counts |
| --- | --- | ---: | --- | --- |
| `malum:soul_binding/domain_of_swords` | `gaze:domain_of_swords` | 1 | 16, 4, 8 | 1, 64, 64, 64 |
| `malum:soul_binding/pact_of_encroaching_malice` | `gaze:pact_of_encroaching` | 12 | 6, 6, 6 | 1, 60, 60, 60 |

These count constraints are sufficient to **identify the two eligible native operations for receipt accounting** but insufficient for a reliable beginner's step-by-step craft quest. Do not mistake `pact_of_encroaching_malice` (recipe) for the exact effect ID `gaze:pact_of_encroaching`. The subsequent exact-JAR CI-log recovery below now resolves main/extra item and typed Spirit identities; source/GUI inspection is still needed for precise effects, player access, prerequisites and risks.

## Exact two Gaze Soul Binding inputs recovered from CI job logs

The [successful exact-JAR Gaze ABI job #135895273160](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44619003420/job/135895273160), associated with [Progression PR #375](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/375), printed full recipe JSON for both Geases **after** reporting `VERIFIED gaze` and the expected SHA-512. The preceding table's integer vectors can therefore be upgraded to exact named ingredients (these are **not** guessed from another pack):

| Exact recipe ID | Main input | Extra inputs | Exact typed Spirits | Native effect result |
| --- | --- | --- | --- | --- |
| `malum:soul_binding/domain_of_swords` | `gaze:spirit_saber` ×1 | `malum:hallowed_gold_ingot` ×16; `malum:mimicry_relay` ×4; `malum:mnemonic_fragment` ×8 | `malum:umbral` ×1; `malum:arcane` ×64; `malum:aerial` ×64; `malum:infernal` ×64 | `gaze:domain_of_swords` |
| `malum:soul_binding/pact_of_encroaching_malice` | `malum:void_salts` ×12 | `malum:living_flesh` ×6; `malum:imitation_heart` ×6; `malum:malignant_pewter_ingot` ×6 | `malum:umbral` ×1; `malum:arcane` ×60; `malum:eldritch` ×60; `malum:wicked` ×60 | `gaze:pact_of_encroaching` |

**Source-level behavior evidence from that exact binary:** `SwordDomainGeas` declares outgoing-damage interception, a `fireProjectileAtTarget` method, sword recognition, attribute modifiers and ticking, with internal summon-count/range fields. `EncroachingGeas` declares armor, toughness, healing and damage attribute conversions, plus update/remove logic. These are **method-level** facts, not measured numbers or proof that a user can force an outcome in the integrated pack. The earlier ABI script primarily disassembled lifecycle and registry classes, so precise effect thresholds/values remain unconfirmed.

**Quest consequence:** a late optional `ML-43` can now accurately teach *one of two exact Soul Bindings*, using the named materials and actual completion/attached-Geas confirmation. Both consume an Umbral Spirit, so do not place this before the pack's intended native Void/Umbral access. Preserve the distinction between recipe result and Geas item: native Soul Brazier binds the target; it does not output an ordinary crafted Geas.

## Additional publisher release-note evidence, not an exact JAR registry census

[Official 1.1.7 release notes](https://www.curseforge.com/minecraft/mc-mods/gaze-a-malum-addon/files/7250833) identify the **Spirit-Channel Pouch**, **Enchantment Workbench** (strips enchantments onto books), **Spirit Saber** (multiple Spirit forms), **Seidhr**, **Veil's Edge**, **Splintered World**, and **Replica Dharmachakra** (formerly Wheel Artifact); they mention a Bestiary, Soul-related events, and renamed Norse-themed Runes. The notes specifically mention an existing Spawner-activating Corrupted Greater Wicked Rite, a modded-cake interaction for Corrupted Greater Sacred, and a revised Aqueous Rite.

The [1.1.7.1 release notes](https://www.curseforge.com/minecraft/mc-mods/gaze-a-malum-addon/files/7261638) retain Malum 1.8 compatibility, confirm a Fafnir Rune bonus while wearing a **full Malignant armor set**, and report an Anima Bestiary display fix. These are official **publisher descriptions**, not exact loaded registry IDs, replacement recipe data or independent confirmation of successful in-game action. Their descriptive specificity still improves the research guide and suggests concrete targeted client acceptance cases.

## Rites: 26 effect types do not equal 26 new named Rites

PR #375's exact binary audit distinguishes **26 Gaze Rite EFFECT types** from Malum's **26 native registered canonical Rite type IDs**. Native Rite selection uses the `MalumSpiritRiteTypes.RITE_TYPES` registry: variants of effects or substituted effects are not additional rite identities merely because they are defined by Gaze. The first-only Progression receipt system therefore accounts for the same native canonical Rite family and does not pay a separate award for a Gaze variant of it.

Quest consequences: the Atlas must not advertise 26 additional standalone Gaze totem assemblies, invent their patterns, or assign unique completion nodes based on those effect-class counts. A Gaze-enhanced totem may be taught later only after its distinct player-visible setup/effect is verified against this installed release. The **26 native Malum Rites** and **37 native Malum Geases** remain separate, valid base-mod inventories.

## Behavior and integration boundaries

- The existing Progression PR records original player/operation attribution and first-only awards for successful native Rite commissioning and real completed Soul Binding (including the two Gaze Geases), not item possession or visual effect activation alone. This **does not itself implement FTB quest tasks**; refer to the original source for acceptance conditions.
- It does not close Gaze's published **progression screen**, six weapons, eight runes, five Curios, pouch, Anima Bestiary, exact added effect semantics, all recipe prerequisites, or survival acquisition policy.
- Native Malum Soulwoven and Ravenous pouches are broad portable item containers; TMM's no-generic-backpacks policy still applies, and the new Gaze pouch is an unaudited potential conflict. Do not automatically reward or gate on any of these pouches.
- There are still separate test obligations for first-party Progression integration and native mixin weave, especially Gaze's replaced Rite effects and Geas completion, but unchanged upstream mechanics do not require blanket game regression testing.

**Next Atlas research:** open the exact pinned Gaze 1.1.7.1 JAR in an authorized read-only workspace (or reuse a sufficiently detailed hash-pinned artifact), enumerate data/registry/lang/UI content for the unverified families, then document user-visible early/mid/late operations and honest quest observation. Preserve the source/JAR/pack/client distinction at every step.