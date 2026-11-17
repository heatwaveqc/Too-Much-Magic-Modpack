# Five resource MXP hook reassessment — pinned 1.21.1 sources

**Date:** 2026-11-14. **Scope:** Ars Nouveau 5.13.5, Iron's Spells 3.16.3, Ars 'n Spells 3.3.5, Neo Vitae 1.1.53, Botania Revived 2.0.2, Psi 110. **Status:** Java source inspected; not decompiled from the pack pins, compiled as an MXP bridge, or tested in-game. These are effort estimates, not declarations of production readiness.

## Verdict and corrected estimates

| Adapter | Prior | Revised | Evidence |
|---|---|---|---|
| Neo Vitae personal EV | Hard | **Low** | Direct, owner-identified server PostSyphon event gives requested ticket cost and actual debit |
| Psi CAD magic | Hard | **Low** | Native built-in KubeJS preSpellCast/spellCast, with compiled base cost available from the context |
| Botania Revived player mana items | Hard | **Medium** | Central requestMana/requestManaExact and tool wrappers; targeted intercept needed, not every item |
| Iron's Spells | Hard | **Medium** | SpellOnCastEvent contains original/adjusted cost and Player; event is just before actual write |
| Ars 'n Spells canonical shared pool | Hard | **Medium** | Verified native BridgeManager and payment write substitution, narrow canonical settlement hook |
| Ars Nouveau | Medium | **Medium** | SpellResolver getExpendedCost posts SpellCostCalcEvent.Post during successful payment; exclude turrets and cross-payment |

The earlier label Hard conflated writing any small adapter with a risky multi-mod integration. The core resource access paths are straightforward. Remaining risk is eligibility, nominal pre-efficiency cost, and duplicate payment across mixed resource providers.

## 1. Neo Vitae 1.1.53 — low

- Source: [AnimaEvent.java](https://github.com/breakinblocks/NeoVitae/blob/1.21.1/src/main/java/com/breakinblocks/neovitae/api/event/AnimaEvent.java), [Anima.java](https://github.com/breakinblocks/NeoVitae/blob/1.21.1/src/main/java/com/breakinblocks/neovitae/common/datacomponent/Anima.java); [API Reference](https://github.com/breakinblocks/NeoVitae/wiki/API-Reference). The 1.21.1 branch has mod_version 1.1.53.
- Personal EV is held in a UUID-keyed **Anima**, not literally a player entity mana attribute. Native AnimaEvent.PostSyphon reports owner UUID, transaction ticket getAmount() (nominal requested EV), and getActualAmount() (committed drain). A built-in upgrade already listens to PostSyphon, confirming supported event consumption.
- **Important correction:** AnimaEvent.PreSyphon is declared in the API, but the exact 1.1.53 Anima.syphon method examined directly posts only PostSyphon; it does **not** post PreSyphon. Use PostSyphon rather than an imagined pre/post pairing.
- The syphon can modify actual drain using Anima Severance upkeep, and syphonAndDamage may substitute player damage for a shortage. MVP: on PostSyphon, if eligible player action is established and actual > 0, award ticket nominal cost / first-access EV baseline. Review partial EV + health-paid cost case explicitly.
- Need verify player-initiated versus non-personal drains (sigils, shield auto-drain, network/ritual, external Animus spell conversion); do not award passive automation. Actual first-access Anima reference capacity remains a playtest value.

## 2. Psi 110 — low, KubeJS-first

- Source: [Psi KubeJS events](https://github.com/VazkiiMods/Psi/blob/master/docs/kubejs.md), [PreSpellCastEvent](https://github.com/VazkiiMods/Psi/blob/master/Xplat/src/main/java/vazkii/psi/api/spell/PreSpellCastEvent.java), [SpellCastEvent](https://github.com/VazkiiMods/Psi/blob/master/Xplat/src/main/java/vazkii/psi/api/spell/SpellCastEvent.java), [ItemCAD.cast](https://github.com/VazkiiMods/Psi/blob/master/Xplat/src/main/java/vazkii/psi/common/item/ItemCAD.java). The master gradle.properties declares mod_version 110 / MC 1.21.1.
- Built-in PsiEvents.preSpellCast exposes player, CAD, compiled context, mutable calculated cost and cancel status. PsiEvents.spellCast is emitted after a successful cast. In ItemCAD.cast, PlayerData.deductPsi(cost,...) happens between them. Attribute paid usage to the successful player action on server, not a cost preview or regeneration event.
- **PreSpellCastEvent.getCost is not nominal:** it comes from ItemCAD.getRealCost, which has already applied CAD efficiency (and container cost modifier) and may subtract a reservoir. For user-approved XP policy, derive nominal cost from compiled spell metadata before those efficiency discounts; compare positive real debit to exclude free-fake casts. KubeJS source's ClassFilter allows the public psi API, but practical access to nested compiled metadata should be smoke-tested; tiny Java helper if needed.
- The CAD itself and player Psi reserves both matter; PsiTweaks FE production spells should count if they genuinely pay Psi, while passive FE output and mere battery movement do not. Continuous/loopcasting should get one award per real paid execution.

## 3. Botania Revived 2.0.2 — medium, narrow handler bridge

- Source: [ManaItemHandlerImpl](https://github.com/Velron-Devs/Botania-Revived/blob/Neo/1.21.1/src/main/java/velrondevs/botania/common/impl/mana/ManaItemHandlerImpl.java), [ManaItemHandler API](https://github.com/Velron-Devs/Botania-Revived/blob/Neo/1.21.1/src/main/java/velrondevs/botania/api/mana/ManaItemHandler.java), [Livingwood Bow auto-repair](https://github.com/Velron-Devs/Botania-Revived/blob/Neo/1.21.1/src/main/java/velrondevs/botania/common/item/equipment/tool/bow/LivingwoodBowItem.java). The Neo/1.21.1 branch gradle.properties is exactly mod_version 2.0.2.
- Centralized requestMana / requestManaExact and *ForTool variants all know Player and requesting ItemStack. A boolean remove flag distinguishes commitment from simulation; base methods return amount delivered / success. The tool wrappers calculate armor/enchantment/magic discounts before base request. Thus one short targeted mixin or maintained code patch can capture all routine player-item payments and nominal outer-tool cost.
- The public ManaItemsEvent merely enumerates player mana sources and is **not a debit event**. Do not credit it, dispatchMana, pool->tablet transfers, flower output or machine fuel.
- Botania equipment self-repair **does consume real mana**. Example LivingwoodBow.inventoryTick checks actual damage, requests 2*40 mana for one repair, then decrements durability. It can count if the owner approves ongoing equipment upkeep as magical practice, or be excluded by source item/action classification. The handler does not itself communicate 'repaired 1 durability' versus 'rod activated' without item context; design filter needed.
- **Cross-mod currency loop:** [ArsManaBridge](https://github.com/Velron-Devs/Botania-Revived/blob/Neo/1.21.1/src/main/java/velrondevs/botania/integration/arsnouveau/ArsManaBridge.java) enables Ars spells to pay deficit using Botania mana items, and Botania tools to draw Ars mana via PlayerManaSources. This can coexist with Ars 'n Spells shared mana. Do not credit a second MXP award for same cast; define the player action as canonical, and regard reserve conversion as settlement rather than additional independent magic use.

## 4. Ars Nouveau 5.13.5 — medium

- Source: [SpellResolver.java](https://github.com/baileyholl/Ars-Nouveau/blob/main/src/main/java/com/hollingsworth/arsnouveau/api/spell/SpellResolver.java), [SpellResolveEvent.java](https://github.com/baileyholl/Ars-Nouveau/blob/main/src/main/java/com/hollingsworth/arsnouveau/api/event/SpellResolveEvent.java). Current main gradle.properties is 5.13.5 / MC 1.21.1.
- The onCast/onCastOnBlock/onCastOnEntity variants check canCast and SpellCastEvent cancellation, and on CastResolveType.SUCCESS call expendMana. That calls getExpendedCost (posts SpellCostCalcEvent.Post) then the caster's expendMana. Pre-spell cost queries exist separately; do not credit at those previews.
- SpellCostCalcEvent.Post offers cost during *actual* payment, but runs before the downstream debit, so award only after accepted payment. Exclude TileCaster turrets and NPC casters. Completion at impact (SpellResolveEvent.Post) may be delayed and can happen once per effect; do not award per damage/effect instead of per paid cast.
- Ars 'n Spells intercepts resource consumption; anchor to a single canonical payment observation in the bridge for reliable shared-pool accounting. The magic resource has exactly one reference B in ISS Primary mode.

## 5. Iron's Spells 3.16.3 + Ars 'n Spells 3.3.5 — medium

- Source: [Iron's SpellOnCastEvent](https://github.com/iron431/irons-spells-n-spellbooks/blob/1.21/src/main/java/io/redspace/ironsspellbooks/api/events/SpellOnCastEvent.java), [AbstractSpell.castSpell](https://github.com/iron431/irons-spells-n-spellbooks/blob/1.21/src/main/java/io/redspace/ironsspellbooks/api/spells/AbstractSpell.java), [Ars 'n Spells 3.3.5 payment-boundary analysis](https://github.com/otectus/ars-n-spells/blob/main/docs/3.3.5/testing.md).
- SpellOnCastEvent exposes Player, spell ID, CastSource, getOriginalManaCost() and getManaCost(). AbstractSpell.castSpell posts this event then does getMana / setMana before onCast. This is an easy way to capture nominal cost at the accepted cast and exclude ordinary scrolls or creative/free casts according to real debit.
- But event is just **before** payment, not proof of payment. In 3.3.5 Ars 'n Spells replaces the actual Iron's mana write at setMana, after pricing at that event, including cross-casts, conversion, native effects and paid channel pulses. Observe this one committed resource transaction, and pair with the nominal spell metadata for MXP.
- Cross-cast sources (Ars in Iron's equipment, Iron's in Ars) may produce both event families; *only one* XP transaction per actually paid spell/pulse. Special LP/aura costs, the Botania-Ars mana bridge, recasts, free scrolls, channelled spell pulses, refund/cancel, server mode and non-default mana conversion require explicit tests, but they do not imply a five-mod hard integration.
- Confirm configured ISS Primary with /ans mode on a disposable test world. Source default is ISS Primary, but the live pack was not checked.

## Revised effort totals for complete 42-entry registry

| Low | Medium | Hard | Inherited | None | Total |
|---:|---:|---:|---:|---:|---:|
| 3 | 18 | 7 | 10 | 4 | 42 |

## Recommended shortest prototype

1. Register Neo Vitaes PostSyphon and grant base MXP for one manually triggered sigil, verifying ticket and actual amount plus owner UUID.
2. Use two Psi KubeJS callbacks on an active CAD spell, checking paid cost and nominal compiler stat. Test twice with different CAD efficiency.
3. Add one Botania handler interception on actual remove=true returns, test rod cost, simulated check (zero), one auto-repair and one transfer (zero).
4. Observe Iron's accepted SpellOnCastEvent with nominal cost and the actual ANS shared debit; then validate one Ars spell, one Iron's spell and a channelled pulse against exactly-once MXP.
5. Test a Botania-powered Ars spell or Ars-powered Botania tool to prove cross-magic mana conversion does not double-credit.

No code integration was performed in this research pass. No numerical first-access pool baselines have been assigned without in-game measurement.