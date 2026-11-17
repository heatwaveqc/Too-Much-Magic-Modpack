# Occultism — pinned familiar lifecycle and utility tools

Companion to [main profile](occultism.md), [158 ritual routes](occultism-pinned-rituals.md) and [529 processing records](occultism-pinned-processing.md). Exact1.227.0 artifact/SHA512 in those references, hash-matched2026-11-16. **Researching; no client/multiplayer/claims acceptance.** This separates summoning from useful familiar behavior and does not claim every special AI/combat branch is finished.

## Acquire, deploy, equip, recover

Use Dictionary of Spirits **Familiar Rituals**, named familiar entry/pentacle and exact ritual recipe from crosswalk. Native summon_tamed path assigns Familiar owner when caster exists. Casterless automation is not proof of ownership. Base entity also permits interacting to claim a null-owner familiar; empty main-hand owner interaction toggles sitting. Native removeWhenFarAway=false avoids ordinary distance despawn; chunk unloading still stops live AI.

Base FamiliarEntity every10 level ticks adds its familiar effect list to resolved owner at strict distance<8. This method itself does not check sitting or familiar-enabled setting; do not assume disabling every feature removes every already-applied/world aura. Effects often last300ticks and can linger after departure. Individual special effects and equipped rings have their own guards.

Craft Familiar Ring using named binding recipe. Capture server IFamiliar whose owner is player or null into an empty ring; serialize entity, stop riding/eject passengers, remove world entity. Occupied ring stores data/name. Equip in an actual **Curios slot**, not merely ordinary inventory, for ring operation. Its curioTick verifies stored familiar owner is wearer; every20 wearer ticks applies effects only when enabled, then calls familiar-specific curioTick. Thus equipped effects are not identical to all world AI jobs.

Use ring on a block to release; helper restores entity at using player's position and reassigns familiar owner to that player, then clears stored familiar. It does not compare original owner in that release helper. A transferred ring can change ownership on release. Spawn-add success is ignored before clearing data: protection/rejection can lose a familiar; no refund is proven. Test captured data, ring transfer, rejected spawn and cross-level persistence before teaching this as safe transport.

`FamiliarUtil` event lookups prefer equipped familiar, then a live same-owner world familiar in owner-box inflated10. getAll combines both. That helper's nearby range10 differs from base aura distance8 and ring20-tick cadence. Enabled checks are caller-specific. No blanket FTB-team or all-allies permission is established.

Ordinary familiar death prepares Soul Shard with max-health/reset-fall/cleared-effects entity payload, gives it to Player owner when available, otherwise drops it. Guardian/Headless have custom recovery paths. Named Resurrection/Great Resurrection use the shard activation; consume it before loading entity, spawn and reassign eligible owner. A generic empty shard is not a guaranteed pet reconstruction. Acquisition, recovery item and actual live restoration are separate quest stages.

## Familiar choices and useful effects

All rows are selected-class binary evidence; acquisition recipes/pentacles are in ritual appendix. Values are native durations/requests, not final measured damage or yield.

| Familiar | Normal benefit / inspected specialization | Upgrade/limit boundary |
| --- | --- | --- |
| Bat | Night Vision300ticks; upgraded adds Bat Lifesteal effect. | Death handler heals LivingEntity killer1+amplifier on a death with effect; not healing on every hit. Requires source LivingEntity attribution. |
| Beaver | Beaver Harvest300; player log break-speed event multiplies by amplifier+3. Tree-growth event assigns one nearby active non-sitting Beaver a target within30 box. | Upgraded special item interaction exists; full tree-cutting AI/drop/protection needs separate closure. Speed bonus is log-only. |
| Cthulhu | Water Breathing300. Upgraded entity can place its own light and has Prismarine Shard interaction. | Interaction/cooldown and temporary-light cleanup need tests; not a general oxygen capability. |
| Deer | JumpI/SpeedI300 plus Step Height effect; upgrade raises step-effect amplifier0→1. | Actual attribute effect/stacking and terrain movement require client acceptance. |
| Devil | Fire Resistance300. Upgraded owner trades one Golden Apple→Enchanted Golden Apple when world time exceeds last+39600. | Strict cooldown; native starting timestamp can delay first trade. No extra mana in this handler. This is a renewable rare-food acquisition lead, not a boss-drop equivalence claim. |
| Dragon | Dragon Greed300, upgrade raises amplifier and temporary greedy state can raise it further. | Pinned LootEventHandler adds amp+1 to existing nonzero XP drops; no item-loot multiplication in that handler. |
| Fairy | Saves eligible other owned familiar on death via handler; not Guardian/Fairy, not bypass-invulnerability source. | saveFamiliar fails sitting/cooldown>0;400-tick cooldown. Saved pet health2+AbsorptionIII100. Upgraded owner trades Glass Bottle→Dragon Breath after strict>60ticks. Ring ticks decrement cooldown. |
| Mummy | Mummy Dodge300 at amplifier0, upgraded1. | Incoming damage dodge10%×(amp+1); requires source entity and excludes explosion/bypasses-invulnerability. No blanket void/environmental immunity. |
| Headless | Player-attributed kills update stored head type; matching target-type attacks can get×2 incoming amount. Upgraded grants Pumpkin Head300 and special nearby behavior. | Pumpkin Head cancels Enderman anger; death/rebuild and species-head side branches remain a focused AI review. |
| Guardian | Limited-life player death rescue; blacksmith can replenish up to5 lives. | Death handler can spend a life before discovering missing Curios inventory. Success cancels death, health1, removes effects, adds RegenII200/AbsorptionII100; excludes bypass-invulnerability. Ring guardian release/replacement adds special cases. |
| Greedy | Equipped curio scans ItemEntities within wearer-box5 and invokes playerTouch when enabled/pickup allowed. | Honors PreventRemoteMovement; no guaranteed insertion if player inventory full. Upgraded block-search/held-target role has separate settings, not generic logistics permission. |
| Blacksmith | Consumes iron to upgrade other same-owner familiars. | Native18 iron/upgrade, cooldown400, chooses eligible same-owner target in4-inflated box, approaches within squared-distance3. Stone pickup consumes whole stack for one probabilistic2-durability repair (see storage/harvest appendix); all target-specific upgrades need acceptance. |
| Beholder | Detects/glows relevant nearby entities; event handlers can deny Blindness after blacksmith upgrade. | Darkness denial additionally needs Warden-upgrade state. Getter/effect list itself empty; not a permanent potion effect source. |
| Otherworld Bird / Drikwing | Jump amplifier5/60ticks; Double Jump amplifier4 (upgraded9)/120ticks, base includes configured Slow Falling. | Upgraded handler cancels fall/fly-into-wall damage if lookup finds upgraded bird; no all-damage immunity. |
| Goat | Transformation branch uses owner-supplied black dye, Flint, Ender Eye in transformation-biome tag to set black/red-eyes/evil-horns; all three trigger Shub transformation. | Nominal biome requires resolved `ALLOWS_SHUB_NIGGURRATH_TRANSFORMATION`; no arbitrary biome promise. Goat/Chimera combination and mount behavior need AI closure. |
| Shub Niggurath | Alternate Goat transformation/familiar entity and spawned-support branch. | Effect-list getter empty; summon/transform identity alone does not describe combat yield. Full spawned entity filtering remains open. |
| Chimera | Mount/Goat combination interactions; upgraded/sized variants. | Owner Golden Apple can split an attached Goat; mounting has size>80 and sitting/vehicle guards. Full growth/fusion/combat branch remains open. |

### Blacksmith resource and timing details

Feed owner Blacksmith Iron ingots/blocks while below configured maximum. It shrinks offered stack first and then tests whether the remainder is tagged Iron Ingot to choose+1 vs+9: the last ingot's empty-stack identity can select+9. Preserve this as a code boundary requiring actual ItemStack test; do not teach overfeeding as balanced intended behavior. It does not simply accept any metal currency.

UpgradeGoal searches same-owner IFamiliar with canBlacksmithUpgrade, requires stored iron≥18 native, and post-decrements cooldown only when earlier short-circuit conditions pass.400 is not necessarily elapsed ticks while no eligible target/funding exists. Once close and upgrade valid it calls target upgrade, debits18 and triggers advancement. Guardian uses repeated upgrades for lives; others generally single upgrade. A filled ring is not a walking target in that world search.

Familiar settings, same-owner filtering, multiple effects/slots and role-specific interactions can change practical combinations. Paired Fairy+Guardian are not interchangeable: Fairy protects other familiar deaths, Guardian protects player deaths at life cost. A statically observed revive handler is not verified survival insurance.

## Spirit Fire, chalk and Otherworld tools

SpiritFireBlock entityInside only handles matching ItemEntity recipes: discards original entity, assembles result, drops stack and plays sound. `SpiritFireRecipe.assemble` sets result count to **input ItemStack count**, rather than multiplying the configured result count. Native conversion is stackwise one-for-one; invalid item simply has no matching conversion in this method. No successful spawn/refund check. Test unexpected items, whole stacks and protective hooks before building an automatic fire converter.

Chalk use places matching glyph on top/survivable or replaceable position; same chalk type reuse does not damage chalk. Otherwise survival damages chalk1 unless unbreakableChalks config. Brush+shift removes existing glyph. Method returns success even when placement branch does nothing; feedback alone is not pentacle validation. Every advanced ritual still needs exact chalk type/layout from native guide.

OtherworldPickaxeItem and InfusedPickaxeItem both report IOtherworldTool harvest tierTWO. Neither name establishes a different harvest gate on its own. Actual Otherworld reveal/loot/ore handling belongs to shared hooks and effective tags; standard tool mining tier and supernatural harvest tier are separate. Full advanced chalk recipes and Otherworld loot handlers remain main-profile closure work.

## Quest and boundary plan (not run)

Teach named familiar ritual→owner assigned→deploy within useful range or capture/equip actual Curios slot→observe benefit→optional upgrade→controlled recovery. Use item/advancement tasks as honest acquisition proxies; manual operation fallback until detector exists. Give XP plus ordinary care/crafting materials; avoid free upgraded familiar/ring that bypasses the next lesson.

Tests: owned/null-owner/other-owner capture; empty/occupied/transferred ring; wearer owner mismatch, enable toggle, distance7.9/8/10, sitting and lingering effects; blocked release and reconnect;18/17 iron, last-stack ingot/block, eligible target/no target cooldown; Fairy sitting/death source/cooldown; Guardian lives/Curios absent/last life; Dragon/Headless kill attribution; Devil/Fairy exact strict trade cooldown; Spirit Fire stack count/nonmatch; Chalk placement/no-op/durability and correct pentacle. No real pet/player was harmed or client launched.

Evidence: pinned `common/entity/familiar/*` selected classes, `handlers/FamiliarEventHandler`, `util/FamiliarUtil`, `FamiliarRingItem` and nested Curio, `OccultismServerConfig$FamiliarSettings`, Chalk/Otherworld/Infused tools, SpiritFireBlock/SpiritFireRecipe. Additional AI/combat, Otherworld loot/storage and familiar-specific rare branches remain explicit, so main remains researching.
