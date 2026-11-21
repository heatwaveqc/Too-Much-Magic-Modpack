# Ars Nouveau pinned potion storage and delivery

Exact5.13.5 JAR SHA1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`. CFR0.152 evidence: PotionJar/Tile/Data, PotionFlask/MultiPotionContents, PotionFlaskRecipe, FlaskCannon (Splash/Lingering inner classes), PotionLauncherData, AlchemistsCrown, PotionDiffuserBlock/Tile, PotionProviderRegistry (anonymous default provider), PotionUtil, PacketConsumePotion/SetLauncher, ItemsRegistry inner Flask variants, ModPotions, Config/Documentation and recipes/locale. [Parent](ars-nouveau.md), [Wixie brewing](ars-nouveau-pinned-wixie.md), [Starbuncle potion transport](ars-nouveau-pinned-starbuncle.md), [Apparatus](ars-nouveau-pinned-crafting.md). [Brewing/effect companion](ars-nouveau-pinned-potion-effects.md) traces all19registered statuses and18potion definitions; this file establishes storage/delivery. No runtime/UI/economy acceptance.

## Build references and exact acquisition

Notebook **Crafting -> Potion Jar / Potion Diffuser**, **Items -> Potion Flask** including both enhanced variants, **Alchemist's Crown**, **Flask Cannons**; JEI corresponding recipes and Flask fill uses. Dominion Wand linked Jar/Diffuser instructions are essential, not inferred from item icons.

Potion Jar shapeless = Source Jar+Abjuration Essence, fresh output. `potion_jar_empty` shapeless recrafts Jar into a fresh one, clearing contents. Three `fill_potion_flask_0/1/2` recipes pair respective Flask with ordinary Potion through custom PotionFlaskRecipe; this is refill data handling, not acquisition of a free filled Flask.

All apparatus rows below have SourceCost0, keepNbt=false. Upgrade an **empty** Flask unless deliberately accepting loss of reagent's stored data; no keep-component promise from these recipes.

| Output | Reagent | Pedestal inputs |
| --- | --- | --- |
| potion_flask | minecraft:glass_bottle | ars_nouveau:abjuration_essence x2, #c:storage_blocks/source x1, #c:storage_blocks/gold x1 |
| potion_flask_amplify | ars_nouveau:potion_flask | ars_nouveau:abjuration_essence x8 |
| potion_flask_extend_time | ars_nouveau:potion_flask | ars_nouveau:manipulation_essence x8 |
| alchemists_crown | minecraft:golden_helmet | minecraft:glass_bottle x3 |
| potion_diffuser | minecraft:campfire | #c:ingots/gold x3, ars_nouveau:archwood_planks x3 |
| splash_flask_cannon | minecraft:dispenser | #c:ingots/gold x2, #c:rods/blaze x2, #c:gunpowders x4 |
| lingering_flask_cannon | ars_nouveau:splash_flask_cannon | minecraft:dragon_breath x1, ars_nouveau:air_essence x2 |

## Potion Jar and Flask charge loop

Jar capacity10000native units =100ordinary potion charges (100each), **not Source or automatically a generic fluid capability**. Use ordinary `minecraft:potion` bottle on Jar to add100 when accepted; survival bottle consumed and glass bottle added, insertion return not checked here. Empty Glass Bottle extracts100 into ordinary Potion; Arrow extracts10 into Tipped Arrow, only when shrinkHandAndAddStack succeeds. Ten arrows per stored charge is native route, not evidence of effective-pack progression balance. Wixie/Starbuncle use separate handlers linked above.

Jar accepts neither WATER nor MUNDANE. canAccept is `(unlocked AND amount<=0) OR (requestedAmount<=remaining AND equalEffects)`, after content-type check. Equality compares **sets of MobEffectInstance**, not potion ID/color identity; two differently named/color sources with equal effects may mix. Lock retains chosen contents when drained and requires matching effects for refill; it doesn't forbid all extraction or refilling. Wand tile toggles lock; native page specifies sneaking Wand gesture. add caps fill; remove clamps0 and clears identity only when unlocked. Reset recipe intentionally loses stored data. Native comparator method computes levels from fill; effective neighbor/comparator behavior remains untested.

All three Flask types default8charges, maxUses8. Use on Jar without sneaking to fill **one**100unit charge; existing nonempty contents require equal effects, empty Flask takes Jar identity. Sneak-use pours one charge into acceptable Jar (100units). Drinking starts only when charges>0, takes32ticks, server applies all modified effects then subtracts one charge; item remains. No Source/personal mana debit in these bodies.

Base Flask returns effects unchanged. Extend Time makes duration `d+d/2` (integer half), same amplifier. Amplify makes duration `d/2`, amplifier min(configured enchantedFlaskCap, oldAmplifier+1); native cap255, allowed2..intmax, an amplifier index rather than displayed potion-level count. Instant effects apply transformed amplifier directly; normal effects add copied instance. Newly constructed enhanced effect instances don't preserve every old ambient/visibility flag by explicit copy. Stored base potion isn't rewritten as stronger merely by drinking; modification happens at effect delivery.

Crafting refill first copies Flask and checks provider room; empty adopts Potion as one charge, nonempty equal-effects adds one. Returns Glass Bottle remainder for Potion input. Native MultiPotionContents stores charge/maxUses/contents, and its provider methods capture the record; caller supplies stack for writeback. It is not a durability-mending resource: isDamaged=false, XP repair ratio0, getDamage reports capacity minus charges for bar purposes. Default low-level consumeUses itself subtracts without clamping; normal caller checks matter.

## Alchemist's Crown and Flask Cannons

Crown is native head-slot-tag item. Equip it and use configured **Head Curio Hotkey** (bind in Minecraft controls) to open potion radial. Menu scans inventory in order for up to9nonempty potion-content items, excluding arrows. Selection sends slot to PacketConsumePotion; server ordinary PotionItem branch applies contents immediately, shrinks one survival and adds bottle; Flask branch checks charge and calls finishUsingItem immediately. It bypasses the32tick drinking animation. No explicit potion-use cooldown or equipped-Crown recheck in this packet body; native client route/actual pack permissions and event policy need testing before treating UI gating as server enforcement. Bottle inventory-add result isn't checked here. Empty menu and no-use provider branches don't establish a successful drink.

Hold Splash or Lingering Cannon and use configured **Open Radial HUD** control to select inventory potion/Flask, then use Cannon. Stored selection is inventory **slot**, not immutable potion identity; moving items can change source. PacketSetLauncher prefers held main-hand Cannon, else offhand; provider-backed sources set launcher data. Native registry recognizes ordinary Potion plus three Flasks; splash/lingering bottle carrying potion contents is not automatically provider-backed (fallback packet branch writes launcher component to source stack itself). Don't promise all modded potion containers work.

Cannon use server checks selected provider/effects/charges, consumes **one provider use before spawning** thrown potion, converts Flask effects through its modifier, then launches matching vanilla Splash/Lingering potion at velocity0.5/inaccuracy1 and applies10tick item cooldown. No Source/mana/gunpowder per shot in these methods. It consumes supply even if projectile later misses or effect is rejected; no hit-success refund. Default ordinary Potion provider shrinks survival stack and adds bottle; Flask provider subtracts charge even for infinite-material player in its own body. Full inventory bottle return is not guaranteed by unchecked add. Selection, last render contents and actual inventory quantity are separate state; count source delta and target effect.

## Diffuser extends one charge across many pulses

Bind **Jar first, Diffuser last** with Dominion Wand; onFinishedConnectionLast accepts PotionJarTile position. No explicit distance cap in bind/obtain bodies, but same-level BlockPos and isLoaded check apply; don't imply cross-dimension/auto-loaded supply. With ticksToConsume<=0, bound jar and worldTime divisible60, obtainPotion requires nonempty potion and>=100, saves contents, sets12000ticks and removes100upfront. No Source/mana fuel. If isOff true tile returns before countdown/work; how effective pack toggles that field still needs control audit.

While remaining ticks>0, decrements; each worldTime divisible300 applies saved potion to **all LivingEntity in block AABB inflated10**, no player/owner/team/species filtering in this loop. Instant effects use full amplifier/strength1; other effects reapply full stored duration. Thus one charge covers a timer window with repeated pulses, not merely one nearby recipient. Tick alignment/countdown means don't promise exactly40pulses for every start. Bind to different contents while cached old potion nonempty calls obtainPotion immediately, potentially replacing paid remaining duration and consuming another charge; no refund.

Client smoke and synchronized state are not server operation proof; tick doesn't wrap all effect/countdown branches in a single server-only guard (obtainPotion itself is server-only). Verify actual authoritative application and reload/disable behavior. Native page's “every10minutes/everyfewseconds” is12000/300game ticks at nominal20TPS, not measured wall-clock timing. Group buff/instant-effect economy, hostile/friendly targeting and claims need pack acceptance.

## Native brewing entry routes and remaining effect audit

ModPotions registers Water+Abjuration Essence -> Awkward; Awkward+Magebloom -> Spell Damage; Awkward+Sourceberry Bush -> Mana Regeneration; Awkward+Mendosteen/Bombegranate/Frostaya/Bastion Pod -> Recovery/Blasting/Freezing/Shielding. Each six family has Glowstone strong and Redstone long variant. Also Water+Wilden Wing -> vanilla Leaping; Horn -> Strength; Spike -> Long Water Breathing. These are Java brewing registrations, not missing JSON recipes; use JEI/brewing/Wixie references. Pod farming remains a separate acquisition branch; actual effect consumers/config formulas are now traced in the brewing/effect companion. No generic potion-name inference about damage/defense is made here.

## Quest and acceptance handoff

Optional route: brew actual potion -> Jar insert/extract -> refill/drink Flask -> choose upgraded Flask -> Crown quick use or Cannon -> bind Diffuser/observe repeated effect and source depletion. Teach units and no-match/full-capacity behavior. Item criteria prove acquisition only; use manual observation or operation-specific detector for brewing, refill, drink/projectile effect and pulses. Reward XP and modest bottles/materials, not prefilled potent potions as self-completing operation proxies.

Unrun tests:100unit bottle/10unit arrow/8charge Flask boundaries, equal effects different IDs/colors, locked empty Jar, full inventory returns and reset/upgrade loss; enhanced duration/amplifier and instant effects; refill mismatch/full capacity; Cannon miss/slot move/unsupported provider, cooldown and charge accounting; Crown no-charge/use animation/bottle return. Diffuser: one charge, exact observed pulses, all-entity targets, rebinding replacement, unloaded Jar, pause/reload/disabled control and effective pack balance/permission. Neither source inspection nor a flying particle establishes successful storage or effect application.
