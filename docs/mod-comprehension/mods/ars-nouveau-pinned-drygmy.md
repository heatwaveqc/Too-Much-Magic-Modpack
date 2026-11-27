# Ars Nouveau pinned Drygmy Henge and loot boundaries

Exact `ars_nouveau-1.21.1-5.13.5.jar`, SHA-1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`; direct JSON/CFR0.152. Evidence: `EntityDrygmy`, `DrygmyCharm`, `DrygmyTile`, `CollectEssenceGoal`, `Config`, `BlockUtil.insertItemAdjacent`, native charm/wilden_drop/blacklist JSON. [Parent](ars-nouveau.md), [familiar role](ars-nouveau-pinned-familiars.md), [Containment](ars-nouveau-pinned-rituals.md). This supersedes source-equivalence limits in the parent's earlier Drygmy survey. No actual loot-rate/boss/pack-tag acceptance.

## Acquire and build

Native Notebook **Crafting → Drygmy Charm**, Drygmy/Henge related entries and JEI apparatus recipe. Befriend untamed Drygmy by main-hand interaction or dropped-item pickup of `ars_nouveau:wilden_drop` tag; native membership is Horn, Spike **or Wing**, not Horn-only. Consumes1, sets BEING_TAMED; after>60ticks emits1..2 Drygmy Shards and discards original. A living worker can later be bound for familiar unlock, a separate use of your acquired creature.

Exact `recipe/drygmy_charm.json`: Enchanting Apparatus with Drygmy Shard reagent; Fish-tag item, Wheat, Apple, Carrot, Seed-tag item and three Source-gem-tag items on eight pedestals; sourceCost0. Apply charm to **Mossy Cobblestone** specifically, replacing it with Henge. Shared charm success consumes one in survival. Initial station conversion after120ticks creates first worker; later charm on Henge spawns another with homePos and no existing-worker cap check in that method. Actual spawn success not checked.

Place eligible ordinary live animals/monsters around Henge or suitable **Mob Jars**, provide Source Jars nearby and adjacent output inventory. Start with harmless common mobs and verify delivered items before adding rarer entities. Neither a decorated Henge nor a jar icon proves eligibility/output. Do not teach protected boss/rare farms from this broad loot mechanism without current TMM tag/loot policy review.

## Exact cycle and economy

| Stage | Native behavior |
| --- | --- |
| Habitat refresh | Every100ticks. Ordinary LivingEntity in box from X/Z±10,Y±6 around Henge; includeEntities flag toggles this ordinary set. Adds contained LivingEntity from MobJarTile in per-axis10,10,10 search regardless of includeEntities. Removes Player and EntityDrygmy from combined list. |
| Bonus | Unique entity-type count×configured unique bonus(default2) + min(quantity cap(default5), combined list size). This happens **before** DRYGMY_BLACKLIST check in generation, so blacklisted entities can affect habitat bonus without contributing loot. |
| Worker channel | Requires home, channelCooldown<=0 and !home.isOff. Selects random habitat entity; approaches<=2.3, but null/unreachable path or>160path ticks can mark approached anyway. Channels100ticks, gives1home progress, sets100cooldown. Not one complete batch per100ticks; default20channels plus travel/cooldown and multiple workers. |
| Generation threshold | On100tick boundary, if !needsMana, progress>=configured maximum(default20), and nonempty habitat, generateItems. Initial needsMana defaultsfalse: first ready batch is not shown paying an upfront Source fee. |
| Recharge | Generation always resets progress0 and needsMana=true afterward. Every80tick boundary, while pending, attempts configured Source cost(default1000) using SourceUtil range7; clears flag on success. Source is not XP/mana despite field name. Other work/progress can accumulate while pending; next generation waits for recharge. |
| Loot collection | For each eligible nonblacklisted habitat entity with nonnull loot table, rolls its loot table once with actual entity/origin and fake-player playerAttack damage source, attacker/direct attacker/last damage player plus fake-player luck. Does not kill original in this method. No hand weapon/enchantment player-loadout guarantee. |
| Selection budget | Budget=baseItems(default1)+habitat bonus. Collects all rolled stacks into a common list; repeatedly picks **with replacement**, copies full chosen stack and inserts adjacent until accumulated stack counts>=budget or budget iterations end. Final count can overshoot budget by chosen stack size, and same rolled stack can be picked repeatedly. Not one item of each species or a strict budget of stacks. |
| XP | Sum getExperienceReward(fake player) for eligible entities, restoring Mob.xpReward afterward; floor(total×0.25). Only if result>3, convert into floor(exp/12) Greater Experience Gems plus ceil(remainder/3) lesser gems. Not direct XP-orb payout, and exp<=3 emits none. |
| Delivery failure | BlockUtil.insertItemAdjacent returns uninserted remainder, does not world-drop. DrygmyTile ignores result for items and XP gems; full/missing/rejecting storage can lose output while progress/reset/recharge still advance. |

Config defaults: `drygmyManaCost=1000`, `drygmyMaxProgress=20`, `drygmyUniqueBonus=2`, `drygmyBaseItems=1`, `drygmyQuantityCap=5`; server effective values unverified. Native `tags/entity_type/drygmy_blacklist.json` lists Iron Golem only; **that is not the effective TMM blacklist** and does not prove every absent boss works. Entity loot conditions, addon hooks, effective tags, captured representation and protected resource gates require separate review.

## Controls and teaching

Dominion Wand on Henge toggles includeEntities; jars remain considered when normal nearby entities disabled. Power gates new worker channels through CollectEssenceGoal, but Henge generation branch doesn't test isOff: a completed progress/recharged batch can still output while powered. Treat it as work control, not guaranteed atomic halt or refund. Station serializes progress/bonus/needsMana/includeEntities; habitat cache rebuilt from world. Worker death, Dispel and missing-home behavior are separate from whether station retains progress; don't infer full multi-worker rollback from serializer presence.

Quest route: Wilden-part befriending → apparatus charm → Mossy Cobblestone Henge → suitable harmless habitat → Source/output → observed complete batch → optional species diversity/jar-only control. Native Charm possession is an acquisition proxy, not proof that valuable loot came from Henge. Rewards should be XP and safe habitat/build supplies, not boss fragments/rare drops that would bypass intended gates. Link named Notebook/JEI references at each construction step.

Acceptance: one common mob and empty output storage; no Source first vs next batch; channel progress/cooldown; two species and quantity cap; blacklist entity affecting bonus vs no loot; jar-only switch; full/missing inventory and debit/progress comparison; stack-size overshoot/repeated selection; low-XP threshold; powered ready batch; chunk reload and multiple workers. Protected/boss positive/negative eligibility tests require policy-compliant disposable setup and actual effective tag/loot audit before recommending a farm. Parent remains researching.
