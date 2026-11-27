# Ars Nouveau pinned spell turrets and timer automation

Exact `ars_nouveau-1.21.1-5.13.5.jar`, SHA-1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`; ZIP JSON/CFR0.152, shootSpell Source branch cross-checked with javap. Evidence: BasicSpellTurret/Tile, EnchantedSpellTurret/EnchantedTurretTile, TimerSpellTurret/Tile, RotatingSpellTurret/Tile, EntitySpellResolver, TileCaster and registered native behavior maps. [Parent](ars-nouveau.md), [composition/casting](ars-nouveau-pinned-casting.md), [Source](ars-nouveau-pinned-source.md). Other equipment/remote devices remain separate. No actual farm, redstone or protected-action acceptance.

## Build a first controlled operation

Read native Notebook **Getting Started → Magical Automation**, then **Crafting → Basic Spell Turret**, whose entry includes Basic/Rotating/Enchanted/Timer recipes and operating pages. Use JEI for named recipes and the spellbook composition UI for the actual spell; a turret is a spell consumer, not a glyph-learning substitute.

1. Craft Basic Spell Turret: shaped `xxx/xzy/yyy`, x=Source Gem tag, y=Gold Ingot tag, z=Redstone storage-block tag. Thus four Source Gems, four Gold Ingots, one Redstone Block equivalent.
2. Place aimed toward a safe test position: block facing is nearest player looking direction's **opposite** at placement. Basic Touch acts at its adjacent facing block; Projectile travels forward. Arrange actual target and any block/item inputs required by chosen effect, not just the turret itself.
3. Select a nonempty known **Touch or Projectile** spell on a casting item, then main-hand use it on turret. Native registry accepts any SpellCasterRegistry-backed item carrying that spell; missing form or unsupported form gives corresponding alert. Native maps contain Touch/Projectile only; Self/other methods aren't supported merely because a spellbook can cast them. Programming copies spell and sets playerUUID, consumes no casting item in that method.
4. Supply Source Jar(s) in native range10 search, or explicitly link an extractable Source capability. Even though tile field is getManaCost, **turret pays Source**, not the owner's ordinary personal mana reservoir. Start with one simple effect and observe the actual target change and Source decrement.
5. Apply a redstone rising edge. Basic schedules a shot4ticks later, marks TRIGGERED, and won't schedule another until signal clears/re-rises. Signal can come at turret or block above. Constant power isn't continuous basic firing, and removing signal doesn't itself cancel already scheduled shot. A powered Timer follows different logic below.

Programming stores UUID for fake-player attribution (FakePlayerFactory when known, shared ANFakePlayer otherwise); this is not proof that every mod's protection/loot/ownership integration recognizes the action. Use own ordinary blocks/targets for tutorial; protected/gated farm eligibility remains separate.

## Source debit happens before effect success

Basic/Rotating shootSpell returns for empty spell/client world. Positive cost:

- If explicitly linked provider exists, queries its chosen side, clears broken capability and returns, or simulates extraction of full amount; insufficient single-provider extraction returns. Then actual extractSource(cost,false) runs, return not rechecked; particle follows. **No nearby fallback while a valid linked provider lacks Source.** Clearing an invalid provider doesn't fire using fallback in that same attempt.
- Otherwise uses SourceUtil.takeSourceMultipleWithParticles(range10,cost), which can combine ordinary jar contents and refunds partial draws on insufficient total under native add/remove semantics. See Source companion for box-vs-special-provider distance difference.
- Nonpositive computed cost skips payment. Successful payment then plays shot animation and constructs fake player/resolver before dispatching cast form behavior. Touch resolver can reject syntax/event/target after debit; Projectile dispatch adds projectile before it hits anything. There is no generic Source refund for later miss, rejected effect or failed entity addition in this method.

EntitySpellResolver bypasses ordinary enough-mana admission; generic resolver.expendMana explicitly skips TileCaster type TURRET. This is why personal mana isn't additionally drained by that generic path, **not** evidence turret has no cost. Other effect inputs/state (e.g. inventory, placement material, target permission) remain effect-specific. Native animation/powered state does not prove successful processing.

| Variant | Exact operation and cost |
| --- | --- |
| Basic (`basic_spell_turret`) | Source cost=Spell.getCost raw part/augment total. Two native form behaviors. No player gear/learned-book mana-discount subtraction in getManaCost. |
| Enchanted (`spell_turret`) | Same base behavior; integer division raw spell cost/2, so odd positive costs floor. Apparatus upgrade below, not a higher player spellbook tier. |
| Rotating (`rotating_spell_turret`) | Basic-equivalent cost with separate rotating behavior map. Dominion Wand turret first→aim block last sets requested yaw/pitch; tick moves angles by interpolation. Doesn't track a moving entity automatically in this callback. Projectile uses normalized angle; Touch picks adjacent block by nearest facing and uses angle-derived block hit if no living entity there. No infinite-reach Touch from remote aim point. |
| Timer (`timer_spell_turret`) | Default interval20ticks; elapsed increments, server calls block tick when interval>0,!isOff and elapsed>=interval, resets elapsed. Cost=raw spell cost minus occurrences' own costs for Touch, Projectile, Redstone and Sensitive, clamped0. This makes a simple native signal spell potentially0Source; other effects retain computed costs. Not every timer spell is free. |

Basic Touch selects random LivingEntity in the adjacent block if any, otherwise resolver.onCastOnBlock with native BlockHitResult. Projectile spawns EntityProjectileSpell with speed max(0.1,0.75+accelerationMultiplier/2); no hit/output guarantee. These are distinct resolution paths, not item crafting ingredients directly accepted by the turret.

## Timer and direct Source-link controls

Timer noncaster main-hand use while unlocked adds20ticks; Shift adds200. Left attack while unlocked subtracts20/200, clamps minimum0 and resets elapsed;0 stops automatic shots. Dominion Wand onWanded toggles **configuration lock**, not isOff. Redstone neighbor event sets isOff from signal at timer position: **power pauses timer**, not basic rising-edge fire. Caster-tool use still delegates to programming; lock doesn't universally prevent replacing spell. Timer serializes interval/elapsed/off/locked; countdown after reload is stateful, not assumed fresh full interval.

To link Source explicitly: Dominion Wand provider first→turret last. Last callback requires same dimension, absolute X/Y/Z differences each<=10, Source capability on selected face and maxExtract>0. Repeating identical position+face removes link. Wrong dimension/out-of-range/missing capability rejects; this is a per-axis cube, not strict spherical10. Linked provider's own per-call maxExtract can prevent a high-cost shot even with enough total storage. Keep adjacent jar fallback simple until real sided/capability acceptance is checked.

Rotating target callback and Source-link last callback have different directions/purposes; first turret→target for aim, provider→turret for fuel. Save/restore includes spell/player UUID, link attachment and rotating angle fields. Actual block drop/recraft preservation differs: recipes below do not universally keep caster NBT, so retrieve/program after conversion rather than assume a configured turret survives crafting unchanged.

## Exact variant recipes

All are namespace `ars_nouveau` under `data/ars_nouveau/recipe/`:

| Recipe | Input/operation → output |
| --- | --- |
| basic_spell_turret | Ordinary shaped recipe specified above →1Basic |
| rotating_spell_turret | Shapeless Basic→1Rotating |
| rotating_turret_to_basic_spell_turret | Shapeless Rotating→1Basic |
| spell_turret | Apparatus Basic reagent +1Source Gem Block-tag item +2Blaze Rod-tag items; SourceCost0, keepNbt=false →1Enchanted |
| timer_spell_turret | Apparatus Basic reagent +1Clock; SourceCost0, keepNbt=false →1Timer |

Written Spell Parchment is an alternate supported spell container for programming; it doesn't need to be consumed to program turret. Wipe Spell Parchment shapeless outputs Blank Parchment and loses written spell. Full parchment writing/tomes/other caster equipment remains separate follow-through; a Basic setup can use actual selected spellbook without that optional container branch.

## Quest and acceptance map

Teach first known spell → Basic craft/place → program correct form → Source → one redstone edge → observed effect. Optional branches: Enchanted Source efficiency, Rotating aim, Timer interval/signal behavior, direct-provider connection. Use named Notebook/JEI pages, positive XP and ordinary redstone/workshop supplies; don't award a high-tier encoded spell or protected output as a shortcut.

Acquiring/programming turret is a proxy; animation or Source decrement doesn't certify a block/entity effect or farm. Use independently confirmed native criterion or manual result/count check. Positive/boundary: Touch block vs multiple living entities; Projectile hit/miss; Self rejection; signal held/off before scheduled tick; no Source/refunded partial fallback vs empty linked provider; maxExtract below cost; invalid linked capability; unsupported reloaded spell after debit; Timer0/20/Shift/lock/power, cost exclusion vs nonfree effect; Rotating angle settling/adjacent Touch; unload/reload, second player/protection and actual effect input/remainder. Parent researching.


Installed-addon scope: [Elemental infused turret audit](ars-elemental-pinned-turrets.md) confirms Elemental adds Homing/Arc to both native turret behavior maps; this companion's two-form inventory is base Ars, not the combined installed set. It also covers school resolver/cost and Touch facing-versus-aim boundary.
