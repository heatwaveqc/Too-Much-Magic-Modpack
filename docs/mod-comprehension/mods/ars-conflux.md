# Ars Conflux — current artifact receipt and combined-Ars research gap

**Status: researching.** Newly pinned Ars execution/compatibility addon discovered during current integration reconciliation; this is a scoped artifact/startup receipt, **not a completed gameplay or compatibility profile**. Its resolver/carrier changes must be reviewed before the combined Ars walkthrough can claim installed behavior.

| Field | Exact evidence |
| --- | --- |
| Manifest / mod ID | `ars-conflux` / `ars_conflux`; manifest category library, enabled selection |
| Current source snapshot | integration `17a564d85b5ee3f0f4f830b68397e54a824a768e`; manifest notes source-integration PR524, no assembly/client/gameplay PASS |
| Pin / artifact | `mods/ars-conflux.pw.toml` at that revision; `ars-conflux-0.1.56-mc1.21.1-neoforge.jar`, CurseForge project1737308/file9505585 |
| Exact local receipt |552,542bytes; SHA1 `97f3c31c624df19209db648e4fe6dd2e67e08d80`, recomputed after official file download; full CFR0.152 external tree |
| Metadata | MC[1.21.1], NeoForge[21.1.250,21.2), native Ars[5.13.1,6); optional Elemental before/Hex Ars Link after. No jarjar metadata. License MIT(code)/LGPL3(optional-addon translations). |
| Parent / shared dependency | [Ars Nouveau](ars-nouveau.md), [Elemental](ars-elemental.md), [current Sauce](ars-family-pinned-sauce-74113.md) |

Manifest library classification is not evidence of zero gameplay impact. Mixins target spell validation/resolution, mana billing/discounts, cooldown/chant dispatch, projectile conversion/control/reflection, spell damage, optional Controle/Additions/Elemental/n Spells recipients and several absent/optional addon families. Registry/data/config and both ends need substantive audit; listing mixins is not proof they apply successfully.

## Confirmed optional-content boundaries

ConfluxStartupConfig defaults `optional_content.enable_elemental_phalanx=false`, `enable_nearby_allies=false`, `enable_neg_form_propagators=true`. Native mana attribute ceiling override enabled, maximum_mana1,000,000 and maximum_regeneration100,000; these ceilings do not grant actual mana. Compatibility augment extensions/Zero support/fixes/previous-phase targets defaulttrue; absent optional mod implementations do not become installed by these defaults. School power attribute list initially empty. All are artifact defaults, not confirmation of effective pack startup config.

Four recipe resources: `glyph_carian_phalanx`, `glyph_nearby_allies`, `glyph_propagate_carian_phalanx`, `glyph_propagate_nearby_allies`. Presence does not establish unconditional availability; recipe conditions/registration and optional targets remain to audit. Do not add Ars Zero/NEG/Hex Ars Link or excluded mods merely to activate these features.

OptionalElementalRegistrationMixin wraps Elemental registerGlyphs FMLLoader.isProduction: result originalProduction AND NOT startup.elementalPhalanx. When effective option true this can open Elemental's normally development-only Phalanx registration; false preserves native policy. OptionalElementalPhalanxMixin wraps summonPhalanx in VolleyBudget.begin/finish and binds created child resolver to Elemental Phalanx carrier. Budget/propagator/recipe/player route is not yet traced, so do not teach default survival Phalanx or assume original projectile budget unchanged.

ArsUpfrontPaymentMixin targets native onCast/onCastOnBlock/onCastOnEntity: captures billing mode, checks TriggerPaidGlyph/deferred native triggers before method dispatch, may expendMana before dispatch, starts VolleyBudget and suppresses ordinary later debit when marked paid, then finishes/resets on return. **This is only the local wrapper shape**; precise eligibility/charges/refunds/cancellations require billing recipients and native bodies. It is not universal prepayment for every cast or proof Horn direct-resolve path is affected.

Mixin plugin AnsMixinPlugin allows most targets, specially gates Zero through ZeroMixinSupport and optional client Hex service compatibility; OptionalAns support checks expected CrossCastingHandler.castArsSpell signature. Application/compatibility still untested. Neither matching metadata nor optional Pseudo target means successful runtime injection.

## Required next research

1. Read startup/common/server configuration and effective integration overrides, registry/events and all four recipe conditions, native documentation additions/guide access. Decide whether optional glyphs actually enabled.
2. Trace billing/discount/trigger/cooldown APIs, VolleyBudget, resolver inheritance and child contexts through exact native Ars and installed addon recipients; distinguish initial admission, upfront/repeat debit and rejected/canceled costs.
3. Trace carrier registration/conversion/reflection and optional installed Elemental/Controle/Additions/n Spells bridges, including source/type/owner/payment/lifetime and targeting semantics. Omit absent Zero/NEG/Hex targets from promises while documenting boundaries.
4. Map any real player glyph/configured branches into actionable Notebook/JEI walkthrough and honest task/reward matrix; no fictitious separate guidebook or tier ladder. Reconcile all49pilot nodes against combined changed routes.
5. Add minimal positive and boundary acceptance handoffs for payment/reflection/owner/trigger/control behavior. No gameplay tests or implementation changes authorized in this research phase.

No native guide acquisition or complete player walkthrough has been established for Conflux yet. Full research cannot be called complete from this receipt. Historical42-profile denominator now expands to43tracked profiles with this new installed-scope addon; current9source-complete,33researching and1stale(34unfinished).
