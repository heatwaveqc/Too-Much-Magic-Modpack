> **2026-11-24 recipe-viewer follow-up:** the native registered-potion discovery predicate `PotionCharmItem.isValidPotion(Holder<Potion>)` now delegates to the same full-content single-registered-noninstant rule as crafting, on both sides. This includes creative-tab enumeration, native JEI and EMIsemble's registered-potion index and recipe pages, without a hard EMI/JEI dependency or alteration of unrelated potion rules. EMI trial #593 selects EMI 1.1.24 and EMIsemble 1.2.1; actual pins/in-game behavior remain integration/QA. Source inspection of EMIsemble PotionCharms.validPotions shows it calls the host predicate and PotionCharmPage cycles registered potions in synchronized inputs/output. Verify exact selected add-on version in candidate. Arbitrary custom-only PotionContents are not registry entries and are not automatically enumerated; no fabricated variants, no custom viewer plugin this slice. Test registered formerly excluded single effects, instant/multi excluded, matching recipes/uses/index and actual craft, plus custom-only craft and lookup limitations. Integrator append only ApotheosisPotionCharmEligibilityMixin to latest manifest; do not overwrite other work.

> **2026-11-24 ALCH-03 owner supersession:** the old effect deny/allow rules below are historical. All actual single, registered, noninstant effects qualify, including native-blacklisted registered base potions, flight, omens, Resistance V+, and custom-only modded effects, with identical ordinary bottles and exact contents. Keep native six blaze powder, three bottles, durability and ticks; reject mixed/empty/multi/instant/unregistered or foreign carrier components. Old TMM charm tags removed. No global potion-tag or Hunter/flask change. Actual craft uses the bridge for all potions; native JEI/creative enumeration may still omit custom or native-tagged variants, not an eligibility restriction. B/C test normal base and custom flight/omens, amplified resistance and custom modded effects, mixed durations/colors, normal crafting cost, refusal cases, exact component preservation and native refresh/repair. Historical donor evidence below remains intact.

# Alchemy compatibility: lossless potion charms and Neo Vitae multi-dose flask guards (#337)

## Immutable donor selection / current integration

- Current tested pack base: `test/full-integration@cde5f21a1d972b22c3df5c02ede124e1ab135e6d`.
- Donor #597 (`feat/alchemy` custom PotionContents charm): exact reviewed PR head `82e7acf0b2ff96d4f8e505ebf7545c8e46d9a517`.
- Donor #602 (`fix/alchemy` native 8-dose flask protection): exact reviewed PR head `e40d204d36178ae213c0a205f47accafea72c466`.
- Both exact-head source workflows SUCCESS: Verify bootstrap, Economy Module CI with exact locked upstream ABI checks, branch lifecycle. This review combines their **source only**, not a Minecraft-world execution result.
- The **14 non-overlapping source files are promoted as exact unchanged donor Git blobs**. Two overlapping files were reconciled by construction: `.github/workflows/economy-module-ci.yml` adds both independently verified audit steps; `compat/economy/src/main/resources/tmm_economy.mixins.json` appends exactly three optional mixins. For each donor, deleting its single added workflow step from its version reproduces the current baseline workflow byte-for-byte; deleting its mixin appended entries also reproduces baseline mixin configuration. All unrelated base names/order, mod metadata, resources and runtime are unchanged.
- This does **not** merge either older feature branch wholesale or add an independent alchemy mod. First-party `tmm_economy` is the sole selected module. No Packwiz version/pin change.

## Exact approved runtime boundaries

### Apotheosis Potion Charm (#597)

- Preserve native shaped recipe/material cost (three ordinary matching potion bottles and six blaze powder), original 192-use effect refresh/durability, and normal registered-base potion handling.
- For custom-only ordinary noninstant single-effect `PotionContents` (or allowed custom-color registered potions), preserve the **whole original PotionContents** on resulting charm instead of throwing away custom effects, amplifier, duration or color.
- Inputs must be genuinely identical and carry no unrelated carrier components. Explicit blocked/unsafe effects denied. Modded custom-only effects denied by default until opt-in `#tmm:potion_charm_custom_effect_allowlist`; `#tmm:potion_charm_denied` and native denies retained. No flight/creative elevation or broader carrier conversion.
- Source-level exact pinned Apotheosis 8.9.0 ABI and unit tests must run on the merged candidate, not merely on #597 alone.

### Neo Vitae multi-dose flask preservation (#602)

- Only the three actual Neo Vitae alchemy flask item IDs (ordinary/throwable/lingering) trigger guard; native potion items remain untouched.
- Reject Iron's Cauldron 250 mB generic single-bottle conversion **before any fluid or bottle transaction**; also block fallback held-item insertion. Reject Occultism Demonic Partner generic single-bottle enhancement/shrink without consuming a flask charge or effect.
- A deliberate server refusal message is allowed; no new 1-dose adapter, fake fluid, bottle remainder or stack mutation. Neo Vitae's own drinking/throwing/rinsing remains native.
- Source-level exact SHA-pinned Iron's 3.16.3, Occultism 1.227.0 and Neo Vitae 1.1.54 ABI must pass with the combined Economy module compile/JUnit.

## Source CI / next authorized B/C

- [ ] Exact-head combined Economy Module CI: BOTH upstream ABI audits, both JUnit policy groups, Java 21 Gradle build, pinned compile classpath, strict integration source preflight and first-party module ID.
- [ ] Verify bootstrap/metadata and branch lifecycle at the same immutable PR head.
- [ ] Postmerge Full Integration Quick CI: all manifest-selected first-party compile/tests and read-only source/recipe preflight; no full assembly or game boot.
- [ ] Next **separately owner-authorized** B/C: three matching custom-only normal potion bottles produce a charm with exact PotionContents and native craft cost/durability; reject mixed/multiple/instant/denied/modded-unlisted/carrier NBT without consuming ingredients. Verify normal registered potions and #535 repaired charms. Test three Neo flask types, 1/8/empty doses in both hands at Iron's Cauldron and Demonic Partner; no stack/component/effect/fluid/bottle loss, and vanilla/native potions still behave normally. Reload both mixin targets, world save and client sync.
- No simulation or Java source check can prove the actual mixins apply to the pinned NeoForge runtime. No full assembly, server boot, Prism packaging, client or gameplay QA authorized or performed.

## Open choices / follow-ups

- No owner decision pending for the **lossless refusal** and strict charm conversion already approved here.
- A positive per-dose flask-to-fluid / Demonic Partner adapter would be independent future implementation requiring an atomic debit and return specification.
- Full broad custom potion effects, flight or non-PotionContents metadata adaptation is **not** in scope.
