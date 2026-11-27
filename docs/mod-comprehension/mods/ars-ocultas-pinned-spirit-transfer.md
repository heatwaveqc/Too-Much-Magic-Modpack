# Ars Ocultas 2.6.2 — spirit workers, output and Soul Gem transfer

Same full-SHA512-verified 82,755-byte addon and 46-class binary as [guide/recipes/Altar audit](ars-ocultas-pinned-altar-recipes.md). Selected native Occultism 1.227.0 SpiritEntity, OccultismAPI and Crusher/Smelter/Crystallizer/Trader/Cleaner jobs plus inner classes were freshly decompiled externally at `ocultas-parent-fresh`; addon at `ocultas-fresh`. Parent [Occultism walkthrough](occultism.md) supplies exact native summoning/job gates. [Addon profile](ars-ocultas.md) remains researching until final route/pilot consolidation. No runtime tests.

## Acquire a real worker and jar

Obtain/open native Ars Worn Notebook and Occultism Dictionary via their parent onboarding; choose **Ars Ocultas → Spirit Jars** and the Dictionary's actual worker ritual/job. Summon and bind the desired worker through Occultism's Foliot→Djinni→Afrit→Marid progression, with matching book, ritual and ingredients; this addon doesn't grant a job or a higher-tier worker from an empty jar. Choose one low-tier useful worker before higher tiers. [Native Containment Jar](ars-nouveau-pinned-mob-jars.md) explains its actual recipe/capture/release rules and Source; this bridge doesn't make every entity capturable.

`IntegrationRegistrar` registers the same SpiritJarBehaviour for exactly Foliot, Djinni, Afrit and Marid entity types. Stored job/owner/inventory state comes from actual serialized spirit, not a recipe that manufactures a new role. Jar possession alone is not proof worker exists or operates. Soul Gem exchange below provides another container transfer path, with important destructive failure ordering; use only eligible owned test entities until accepted.

## Three input channels differ

**Manual use:** server-side jar behavior, secondary-use with empty hand opens spirit GUI. Otherwise it checks the held stack against `OccultismAPI.getItemsToPickUp(spirit)`, simulates insertion into spirit.inventory, performs real insertion if some fits, and sets held count to remainder count. Missing job/pickup list refuses manual feed. It doesn't directly call job.canPickupItem for this path, so don't assume its eligibility equals the configured dropped-item filter in every job. No personal mana or Source charge is added in this use handler.

**Nearby dropped items:** server tick initializes spirit if needed. Every global gameTime%40==0 it scans alive ItemEntities in the jar block's unit AABB inflated3, a seven-block-wide cuboid. Chooses **first** item whose native job.canPickupItem says true, simulates inventory fit and inserts, preserving stack remainder. If that first matching entity doesn't fit, this code doesn't continue to another fitting candidate in the same scan. It isn't an every-tick bulk vacuum or sphere-distance test. Native API delegates canPickupItem to current SpiritJob; Cleaner specifically uses item/tag filter matching and blacklist inversion. Different jobs can implement different rules.

**Capability insertion:** RegisterEvents registers MobJar block ItemHandler that returns contained SpiritEntity's ENTITY item capability, ignoring queried block side. No addon-level manual/pickup predicate is applied in this adapter. The default SpiritEntity constructor uses ItemStackHandler(1), but subtypes may supply another inventory; neither a one-slot default nor a GUI filter establishes a universal item handler acceptance rule. Caller must use native insert/extract/simulate/remainders. The jar bridge isn't a separate universal logistics mod or a claim all hand/hopper/worker channels behave identically.

## Native work and lifetime

Each server jar behavior tick calls optional current job.update, after initialization. Cleaner additionally tries to export its mainhand stack to adjacent inventories and replaces hand with remainder before job.update. Native Cleaner onInit installs movement/pickup/deposit AI goals, but behavior invokes job.update, **not the entity's full AI/movement lifecycle**; don't promise a stationary jar can walk to arbitrary work areas or execute every role that depends on pathfinding.

Pinned native SpiritEntity.aiStep normally initializes, increments spirit age each20ticks when canDieFromAge, dies/removes when age>max and calls job.update while alive. Jar behavior directly calling job.update doesn't execute that age branch. This supports no ordinary age advancement on this contained work path, not immunity to every possible job timer, damage, container failure or mod interaction. Native entity NBT serializes age/max/job/inventory; release can restore remaining age rather than reset it. No added recurring Source debit appears in SpiritJarBehaviour; normal job recipe/input/time rules still apply.

For a Crusher, native current recipe must accept input and tier. At timer threshold it assembles output, applies operationCount and tier multiplier unless ignore flag, shrinks input **before** posting CrusherJobEvent. Defaults/raw-iron source formulas and all tiers are in parent Occultism profile, not a new instant jar conversion. Smelter/Crystallizer/Trader retain their respective native job/event behavior. All four registered entity tiers share addon behavior, not one fixed processing speed.

## Output handoff on both ends

Addon EventHandler subscribes to CrusherJobEvent, SmelterJobEvent, CrystallizerJobEvent and TraderJobEvent. If event entity is SpiritEntity whose blockPosition contains MobJarTile, it dispatches the jar behavior, attempts adjacent inventory insertion and replaces event.result with **remaining** stack. This is a result hook after native job processing, not an output-copy duplication path.

`tryItemNearbyTransfer` finds native adjacent inventories at jar position, uses InventoryManager.insertStackWithReference and returns its remainder; no neighbors/no reference returns original input. Cleaner uses this every behavior tick for mainhand export. Full/partial insertion is different from complete empty remainder; order/sidedness belongs to native adjacent inventory utilities, not player ownership policy.

Fresh native Crusher/Crystallizer/Trader handlers inspect event.getResult after listeners: empty suppresses further output; nonempty ordinarily spawns it with job output tag, with a special native Dimensional Extractor/below-handler path. Smelter follows the same common output pattern. That native special path can ignore insertion remainder; the addon does not automatically repair it. So full-output boundaries and native placement matter even when addon correctly forwards a remainder. Failed spawn/inventory side effects aren't globally rolled back after input consumption. No measured rate or loss-free promise.

## GUI and filter state

Secondary-use empty-hand opens SpiritMenuWrapper: MenuProvider job selects wrapped transporter container, otherwise wrapped spirit container. Server sends jar BlockPos for client binding; native GUI/profile tooltips show job/name rather than proving active processing. SpiritScreenWrapper overrides blacklist toggle and sends MessageSpiritSetFilter(boolean, spirit.blockPosition).

Server filter handler looks up a MobJar at sender's **current dimension/blockPos**, gets SpiritEntity and writes blacklist state. It doesn't check open menu, owner or distance in this method. It isn't evidence of authorized remote configuration or claims enforcement. Stored entity position matching jar after capture/reload and current menu synchronization must be tested; effective filter contents and native job eligibility remain separate.

## Soul Gem ↔ jar transaction order

EventHandler.playerRightClick intercepts RightClickBlock only when held item is a SoulGemItem and target MobJar. Config pickup/place defaults true. Method has no explicit side or mainhand gate; event authority/client synchronization is therefore an acceptance boundary rather than assuming every mutation is server-only.

**Empty gem + filled jar:** pickup disabled cancels FAIL. Checks native jar release blacklist, then whitelist precedence over jar blacklist; normal Soul Gem and Trinity Gem each have additional native deny tags. On allowed entity it saves encode ID plus full NBT to gem ENTITY_DATA, removes entity from jar, cancels SUCCESS. It does not invent a new spirit/job; identity/state preservation depends on serialization. Fragile gem differs on placement, not a blanket exemption from all jar restrictions.

**Filled gem + empty jar:** place disabled returns to other handling rather than explicitly canceling. Enabled branch obtains raw ENTITY_DATA and **removes the component before validating entity type/deny tags or successful creation**. Jar-blacklist rejection sets FAIL/canceled but lacks return there; execution can continue and later set SUCCESS/install entity. Soul Gem/Trinity deny branches return, now with gem data already removed. Null entity creation likewise fails after removal. These are distinct failure cases: not every denial preserves captured data, and not every canceled jar blacklist branch actually stops transfer.

If creation succeeds it loads NBT, calls jar.setEntityData, cancels SUCCESS. Fragile Soul Gem clears **player MAIN_HAND**, regardless of event hand, plays glass break, then broadcasts inventory. Offhand use could affect a different mainhand stack; no offhand-safe promise. Both-full/both-empty cases don't match these exchange branches and continue native handling. No addon Source or mana charge is debited here; creature restrictions, fragile consumption and failure losses still have costs.

## Teaching and unrun acceptance

Three useful choices: compact recipe worker jar with measured one-item output; Janitor-style local collection/export; Soul Gem transport of an already legitimate worker. Teach input channel differences and same-owner/test-fixture use, safe capacity/remainders and restore/release state before designing automation. No new generic pipes or claims bypass are authorized. Future rewards: modest XP/common consumables, not free tier4 spirit, unique altar ingredients or fragile captured entities. Current pilot's jar hardware criterion doesn't certify a contained role or operation.

Not run: all four native tiers and selected job timers/eligibility; manual versus dropped/filter versus capability channels, first-full candidate, adjacent/no/full/partial output, native special extractor case; contained age/release/reload/owner and GUI/filter packet synchronization; gem empty/full/disabled/deny/create failure and offhand fragile branch, item/entity loss and cancellation ordering. All major transfer bodies are now source-traced; final addon guide/walkthrough/pilot/native legacy-recipe review remains before status promotion.
