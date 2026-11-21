# Goety Cataclysm — servant care and recovery

Companion to [the profile](goety-cataclysm.md), [all recipes](goety-cataclysm-recipes.md) and [focus summons](goety-cataclysm-focuses.md). Inspected 2026-11-16. **Researching; no gameplay tests.** Uses the exact Goety3.2.2 and addon1.21.1-1.8.4 hashes in [the operating evidence](goety-cataclysm.md#pinned-bytecode-operating-audit--2026-11-16).

## Choose the route before teaching maintenance

Focus casting writes a limited lifetime for the ten servant-producing focuses; altar summons, conversions, constructed golems and recovery items have distinct paths. An item or entity with the same appearance does not establish permanence or recovery eligibility. Read Black Book → Cataclysm → **Summons** and **Mutations**, the individual servant's named page, then the exact JEI summon/conversion recipe. The [focus crosswalk](goety-cataclysm-focuses.md) separately documents temporary life, recall, cap counting and Summon Down. Do not build a farm around temporary servants until death drops and soul credit are tested.

Evidence below comes from original analysis of addon `com/Polarice3/goety_cataclysm/common/entities/ally/**` interaction/death methods, `common/entities/neutral/AbstractDraugrNecromancer.class`, `common/items/revive/*.class`, `common/items/block/{EnderGolemSkullItem,NMHeadItem}.class`, `common/blocks/{EnderGolemSkullBlock,NMHeadBlock}.class`, `config/GCMobsConfig.class`, and parent `common/items/revive/ReviveServantItem.class` and `common/entities/projectiles/FlyingItem.class`. Source names in the tables identify the exact relevant class; inherited or untraced behavior remains explicit.

## Feeding and repair contracts

In the inspected hand-feeding branches, the player must resolve as the servant's true owner and the servant must be below maximum health. Survival mode consumes one accepted item; creative handling often skips consumption and therefore does not test last-item behavior. Health amounts below are **health points**, not hearts. Effects clamp through ordinary entity healing. Full-health/wrong-owner/wrong-item behavior and any superclass fallback still need client checks.

| Servant / method source | Accepted input | Coded healing branch | Limit or failure order |
| --- | --- | --- | --- |
| Cindaria, Clawdian, Hippocamtus, Urchinkin; their `mobInteract`; Deepling variants via `AbstractDeeplingServant.mobInteract` | `minecraft:fishes` item tag | +2 | Fixed heal after consumption; no post-shrink input choice in these branches. |
| Draugr and Elite Draugr; corresponding `mobInteract` | Rotten Flesh or common Bones tag | +2 | Fixed heal; Royal Draugr has its own interaction wrapper and should be tested independently. |
| Koboleton | common Bones tag or Cataclysm Koboleton Bone | +2 | Fixed heal. |
| Kobolediator, Wadjet | Bones tag, Koboleton Bone or Bone Block | Bone Block +6; other accepted input +2 | Selects Bone Block **after** shrink; last Bone Block falls through to +2 in native item semantics. |
| Aptrgangr | Rotten Flesh, Bones tag or Bone Block | Bone Block +6; others +2 | Same post-shrink Bone Block distinction. |
| Watcher | common Iron Ingots tag | +5 | Fixed heal; this is repair, not Fabricator fuel (Mechanized Iron Block). |
| Prowler | common Iron Ingots or Iron Storage Blocks tags | Ingot +5; block +45 | Both item-tag checks happen **after** shrink; a single-item survival stack can be consumed with no selected heal branch. |
| Leviathan | `minecraft:fishes` tag | +5 | Healing branch precedes riding interaction; do not interpret every successful click as feeding. |
| Ancient Remnant | Bones tag, Koboleton Bone or Bone Block | Bone Block +6; others +2 | Post-shrink choice has last-block +2 boundary; interaction also supports riding. |
| Coral Golem | its coral-item helper | Max health /32 | Rechecks coral after shrink; single accepted item can select no heal. Exact coral helper membership still needs full type/variant coverage. |
| Coralssus | Coral block/basic coral/fan matching its Fire/Horn/Tube variant | Matching coral block max/4; matching basic/fan max/32 | Chooses match **after** shrink; single item can select no heal. Wrong variant is not generic coral food. |
| Ender Golem | Cataclysm Void Core or Void Stone | Core max/2; stone max/4 | Both item checks after shrink; single item can be consumed without selecting either branch. |
| Netherite Monstrosity | Lava Bucket, Netherite Storage Block tag, Goety Reinforced Redstone Block, Cataclysm Lava Power Cell | Block/reinforced block max/4; lava/cell max/32 | Server-side branch; non-bucket inputs are shrunk before item selection. Bucket handling returns/replaces a bucket separately; do not equate its object path with stack-shrinking blocks/cells. |
| Ignited Berserker / Ignited Revenant | Bones tag, Blaze Rods tag or Blaze Powder | Default1; bones2; rods4 | Input is shrunk before bone/rod tests; last bone/rod falls to default1. Captured item variable admits Blaze Powder at entry. Revenant also has a separate Netherite Scrap shield-repair branch. |
| Draugr Necromancer via `AbstractDraugrNecromancer.mobInteract` | Rotten Flesh; Soul Jar/Draugr Soul Jar branches have separate upgrade behavior | Rotten Flesh +2; Draugr Soul Jar can raise NecroLevel up to2 and heal configured max-health value | Do not present a jar as ordinary interchangeable food: it can consume a serialized recovery item and alter a progression state. |

**Unestablished feeding channels:** no complete direct-feed contract is claimed here for Lionfish, Zombie Amethyst Crab, Drowned Host/Symbiocto or every inherited Royal Draugr branch. Passive robe/soul healing and entity-specific damage lifesteal are separate from hand feeding and still require a complete family-by-family audit. Do not fill those cells with “fish works for all aquatic mobs.”

### Why single-item stacks matter

Exact addon ordering is `accepted-input/owner/health check → shrink → choose material-specific heal`. The generated Minecraft1.21.1 / NeoForge21.1.265 `net/minecraft/world/item/ItemStack.java` confirms that `getItem()` returns AIR when empty, and `is(Item)`/`is(TagKey)` inspect that resolved item. Thus shrinking count1 to0 can change the later item tests. This is a source-supported consequence, not a recorded in-world result or permission to patch gameplay.

Supporting local source: `compat/progression/build/moddev/artifacts/neoforge-21.1.265-sources.jar` in the separate QA checkout, `net/minecraft/world/item/ItemStack.java`, extracted source SHA-256 `b35770a757f0c62a88276a38c36763d26e204d1a6210ea3fcac71a53526bc389`. This generated source supports the target API semantics; it is not claimed as a hash-pinned mod artifact or source-to-client equivalence proof. Reproduce with survival count1 versus count2, identical damaged servant, same owner and effective config, measuring health and stack deltas. Creative skips shrink and can hide this boundary.

## Death returns and revival are different operations

`ReviveServantItem.setSummon` ejects passengers, saves entity NBT/type (and custom name when present) into item CustomData. `getSummon` creates that entity type and loads its saved data on the server. This preserves much more than a display name; owner, equipment and lifetime behavior must be tested rather than assumed reset. Recovery methods set restored health to maximum explicitly, but do not promise full reset of every saved field.

For ordinary spirit/core items, death methods create a parent **FlyingItem entity**, set its owner and item payload, then add it to the level. `FlyingItem.tick` rises initially, later homes/teleports toward an available owner, gives/drops the stack on delivery and may set an item-specific cooldown. It is not simply a ground drop immediately at death. Owner offline, full inventory, other dimension, chunk unload and restart need persistence tests. No blanket `doMobLoot` or guaranteed-delivery claim follows merely from the word “drop.”

| Death/recovery family | Death evidence / default condition | Actual reconstruction action | Cost and ownership/ordering boundary |
| --- | --- | --- | --- |
| Prowler → `goety_cataclysm:mechanized_core` | `ProwlerServant.die`; true owner and `GCMobsConfig.prowlerCore` (default true); entity saved into item; FlyingItem secondsCool30 | `MechanizedCore.useOn`: click a Mechanized Iron Block at center of a face on a **3×3×3 solid cube** of those blocks, extending three layers inward from clicked face; stored Prowler or unbound default Prowler is restored | 27 blocks consumed after `addFreshEntity` succeeds; core consumed and vanilla30-second cooldown added. Nominal materials for27 fuel blocks:135 Iron Ingots+108 Redstone Dust. Saved servant owner must equal user; cap checked first. A specific Goety cooldown check and vanilla cooldown write coexist; test both layers. |
| Kobolediator → Warrior Spirit | `KobolediatorServant.die`; owner and `kobolediatorSpirit` default true; serialized item and FlyingItem30 | Right-click a Cataclysm Koboleton or addon Koboleton Servant | `WarriorSpirit`: restored owner's identity must equal user; cap passes; add restored full-health entity succeeds, **then target discarded**, item consumed and vanilla30-second cooldown added. It does not explicitly require the target's owner equal user. |
| Wadjet → Arcane Spirit | `WadjetServant.die`; owner and `wadjetSpirit` default true; serialized item/FlyingItem30 | Right-click Cataclysm Koboleton or addon Koboleton Servant | Same spawn-before-target-discard order and restored-owner check; replacement consumes a living entity, not a pedestal item. |
| Ignited Berserker → Ignited Soul | `IgnitedBerserkerServant.die`; owner and `ignitedBerserkerSoul` default true; serialized item/FlyingItem30 | Right-click vanilla Blaze or Goety Blaze Servant | Restore full health after owner/cap checks; on accepted spawn discard target, spend item, add vanilla30-second cooldown. Blaze inventory items do not substitute for entity input. |
| Ignited Revenant → Ignited Helm | `IgnitedRevenantServant.die`; owner and `ignitedRevenantHelm` default true; serialized item/FlyingItem30 | Right-click Goety Wildfire | Same checked restore/replace path; consumes target on accepted spawn. |
| Draugr Necromancer → Draugr Soul Jar | `AbstractDraugrNecromancer.soulJar` requires true owner Player, enabled parent `NecromancerSoulJar` and an Empty Soul Jar in main inventory; serializes into Draugr jar, consumes empty jar in survival, gives/drops item and adds parent cooldowns | `DraugrSoulJar.interactLivingEntity`: right-click ordinary Cataclysm Draugr or addon Draugr Servant | Restored owner=user, cap passes, add succeeds then discard target, consume filled jar and add two Goety cooldowns. Full invocation/death trigger of `soulJar` remains open; method presence alone does not prove every death captures one. |
| Ancient Remnant → Remnant Skull | `AncientRemnantServant.die`; owner; writes owner UUID/name but **does not call setSummon**; FlyingItem secondsCool0 | Aim/use skull onto block with collision-free space; `GCRemnantSkull.use` creates Cataclysm **Modern Remnant**, tamed to saved owner UUID | This is a juvenile recovery step, not instant Ancient Remnant restoration. Survival item consumed after server add attempt; cap/re-advance through the full conversion route must be checked. User does not become owner merely by holding a bound skull. |
| Ender Golem → Ender Golem Skull block item | `onDeathUpdate` exact completion tick: owned servant writes owner/name and emits extended-life item; unowned branch has11% code chance | Rebuild skull's smaller mold structure and place skull last (below) | Stores owner/name only, not full serialized entity. Skull's cap predicate is called with placer, while final owner can be saved UUID; test another player's placement. |
| Netherite Monstrosity → head block item | `onDeathUpdate` exact completion tick: owned servant writes owner/name and emits extended-life item; unowned11% branch | Rebuild the dedicated head structure and place head last (below) | Stores owner/name, not complete entity NBT. `NMHeadBlock.setPlacedBy` goes from shape check to spawn without an explicit summon-cap check in inspected path; original Infernal Forge assembly cap must not be assumed to guard this recovery route. |

No death-return item is established here for every servant or every temporary focus summon. Each table condition is local code evidence, not a universal recovery guarantee, and unrelated per-mob loot tables should not be silently merged with these custom death events.

## Skull/head reconstruction structures

Single vertical layer diagrams below are factual build patterns from exact block recognizers; a dot is required air. Orient the plane as the recognizer/book permits and leave clear spawn space. Use the named Black Book Summons entries for **Ender Golem** and **Netherite Monstrosity**; client rendering and orientation still need validation. These are recovery structures, distinct from initial Animation Core/Infernal Forge structures.

**Ender Golem** — eight Cataclysm Void Stone plus one addon Void Mold Block, with skull placed last:

```text
..S..
VVVVV
.VVV.
..M..
```

`S`: Ender Golem Skull, `V`: Cataclysm Void Stone, `M`: addon Void Mold Block. Void Mold's craft yields four; its exact recipe is in the recipe crosswalk. This recognizer uses Void Stone exactly; do not substitute Obsidian merely because the initial construct accepts it elsewhere.

**Netherite Monstrosity** — head placed last above the following body:

```text
...H...
PGBRBGP
GBRXRBG
.GBRBG.
.GOPOG.
```

`H`: Monstrosity head, `P`: addon Netherite Plating, `G`: Gilded Blackstone, `B`: Nether Bricks, `R`: common Redstone Storage Block tag, `X`: Goety Reinforced Redstone Block, `O`: common Gold Storage Block tag. Dots must be air. Head block has lower/upper halves; placement/update behavior and clearance require a client check.

**Destructive order:** both skull/head `trySpawnGolem` methods clear matched pattern cells to air **before** creating and adding the replacement entity; no rollback is shown if creation/addition fails. Thus these are not transactionally equivalent to spirit items that discard the target only after `addFreshEntity` succeeds. Test denied/cancelled spawns or blocked/protected areas in a disposable world, never around valuable player builds. Document the behavior; do not add a runtime fix during research.

## Passive maintenance still to close

- Netherite Monstrosity heals the configured Cataclysm `natureHeal` amount every4 ticks while dormant and AI-enabled. Standing in Soul Fire, or a lit Soul Campfire, also heals that amount every20 ticks when its no-target timer≤0, AI enabled and natureHeal>0. Effective parent config and target/dormancy behavior determine real throughput; no numerical heal rate is claimed here.
- Ancient Remnant's powered state has a dedicated `healServant` path: per20 ticks under owner/cape/config/eligibility/available-souls conditions, +1 health and owner Soul Energy debit at `UndeadMinionHealCost`; without Player owner, a separate per20-tick branch heals1. Do not call player-owned robe healing free.
- Broad Abyss Robe/Necro Cape upkeep for all other servant classes is not closed by those two examples. Trace parent `Summoned`, service helpers and effective tags before describing a free regeneration loop.

## Quest lessons and unrun acceptance tests

| Lesson | Teach → observable result | Truthful detector / safe reward direction | Positive and boundary check |
| --- | --- | --- | --- |
| Maintain companion | Read named Summons entry → damage servant in disposable practice → use accepted material → confirm health and item delta | Manual operation; material possession cannot prove healing. XP plus modest ordinary repair supplies after global policy check | Count2 positive versus count1, full health, wrong owner/item and creative comparison. |
| Save recovery payload | Explain which exact servant returns which item and prerequisites → safely demonstrate only after recovery path proven | No mandatory killing of expensive companions before verified recovery. A serialized item is not proof the restored entity appeared | Owner online/offline/other dimension, inventory full, chunk save/reload, doMobLoot and config settings. |
| Restore spirit/core | Prepare exact target entity/cube → use item → confirm old target gone, restored entity owner/health/name/gear | Manual operation or validated summoned-entity advancement; item criterion is only acquisition | Correct vs other-owner saved payload, wrong target, at cap, denied entity add, cooldown and item/target/block debit. |
| Skull/head build | Named guide page → complete exact recognizer and place head last → confirm owner and no leftover blocks | Manual build/operation; XP plus safe non-gated material, not a free head/core | Valid vs one wrong block, required air, another placer, cap, denied spawn, head upper-half aftermath. |
| Juvenile Remnant | Use owned Remnant Skull → verify Modern Remnant → follow full Terminus conversion again | Teach actual juvenile form; do not label recovery as instant boss restoration | Collision/invalid placement and saved-owner resolution; confirm full advanced conversion ingredients/research still enforced. |

## Remaining coverage

Healing/recovery for the listed families is now code-backed with precise failure ordering. Still open: inherited feeding/care for unlisted families, complete command/guard/riding controls, servant attack/ally/grief filters, full death-trigger reachability for Necromancer jar, initial construction recognizers and boss-drop acquisition, effective configs, transfer/player persistence and client acceptance. Keep the parent/addon profiles `researching`.
