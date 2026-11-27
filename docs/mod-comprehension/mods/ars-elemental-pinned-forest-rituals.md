# Ars Elemental0.7.11.3 — Archwood island and Forestation

Current exact binary/hash and integration snapshot: [utility-ritual/delta receipt](ars-elemental-pinned-utility-rituals.md). Direct CFR0.152: ArchwoodForestRitual, ArchwoodForestationRitual, new ArchwoodVariantsRegistry, ArchpodFeature/PlaceableLightFeature and native Ars5.13.5 FeaturePlacementRitual, ConjureBiomeRitual, ManhattenTracker, RitualUtil, feature helpers/BlockUtil. [Parent](ars-elemental.md), [native ritual loading/payment/refund](ars-nouveau-pinned-rituals.md). No world placement/biome/persistence acceptance.

## Two different operations

These are optional material/environment branches; neither upgrades the personal Spell Book. Use native Worn Notebook **Ritual Index → Performing Rituals**, **Conjure Island: Archwood Forest** (`ars_elemental:ritual_archwood_forest`) and **Forestation - Archwood** (`ars_elemental:ritual_archwood_forestation`). Native Documentation generates entries and recipe pages from the ritual registry. Craft tablet, install into Ritual Brazier, feed augments while not running, then empty-hand activate. Choose an expendable area; island work changes blocks and biome storage, and no rollback/refund after operation is promised.

| Tablet | Exact shapeless craft, output1 | Pre-start consumed augments | Endpoint |
| --- | --- | --- | --- |
| `ars_elemental:ritual_archwood_forest` |1×`#c:logs/archwood` +native Abjuration Essence +Conjuration Essence | Native Source Gem tag adds1radius per unit; at most1BlockItem whose block is ArchfruitPod chooses thematic biome. **Pod item, not a loose harvested fruit food.** | Place replaceable terrain below brazier and request biome changes; no automatic forest tree/structure generation. |
| `ars_elemental:ritual_archwood_forestation` |1×`#c:logs/archwood` +`ars_nouveau:ritual_forestation` +Air/Earth/Water/Fire Essence each1 | Source Gem tag adds1checkRadius/unit; registered Archwood sapling chooses resource variant, with admission/order caveat below. | One finite pass of growth/tree/plant/pod/light placement attempts; no automatic biome conversion, perpetual farm or mob spawning. |

All augments split1unit per accepted native call, consumed independently of the tablet craft. No real player inventory bone meal, trees, plants or pod stacks are repeatedly charged by the feature loops: their helpers create temporary stacks/states. These are code-produced world blocks subject to placement/survival/space and event behavior, not deterministic inventory outputs.

## Conjure Island: Archwood Forest

Default target is native `ars_nouveau:archwood_forest`. On start, native parent increments radius7 by consumed Source Gems and creates ManhattenTracker centered at brazier.below(3), x/z extent radius and y extent2. Therefore candidate height is **brazierY−5..Y−1**, further restricted by distance≤radius from brazier center; top layer atY−1 is Grass Block, lower accepted layers Dirt. This is a shallow island volume, not a 7×7 flat platform. Existing nonreplaceable blocks are skipped, not excavated, and no guaranteed footprint/final number of blocks follows from radius alone.

Each server tick inspects up to radius candidates until one accepted placement; at most1accepted position per tick. It sets terrain with flag2, calls RitualUtil.changeBiome, plays placement sound, increments blocksPlaced, and returns. **After5accepted placements** it resets counter and requests50Source; next primary-brazier tick must pay via range6 or pause/backoff. Initial needsSource=false permits first5placements without prior payment. Final remainder<5need not incur a further request. A setBlock return is not checked before biome call/counter; claim interference or rejected writes require observation rather than exact yield arithmetic.

This is not passive natural worldgen. RitualUtil resolves the target biome registry key, checks current biome, edits quart-coordinate biome palette cells across chunk sections, marks chunk unsaved and sends ChangeBiomePacket to tracking clients. Thus influence need not match one placed block or a strict island-shaped3D volume; exact vertical palette/client behavior needs acceptance. The helper does not generate trees, caves, structures, ores or mobs. Choosing Vexing Caves does not construct its cave geometry and does not bypass MageY>45/light spawning requirements in the [biome/encounter audit](ars-elemental-pinned-biomes.md).

After native onStart, addon scans consumed stacks for the first ArchfruitPod BlockItem. New variant registry maps its **pod item** to thematic biome. Unknown pod falls back to native Archwood Forest. After one pod is recorded, predicate rejects further pods unless independently matching native Source Gem tag; do not teach repeated fruits as radius boosts. Biome choice is written if nondefault, and restored from its key; native parent saves tracker/radius but **not blocksPlaced**. Reload can lose partial progress toward the next5block Source request. Generic consumed list remains stored; no loaded-world persistence PASS.

## Forestation - Archwood

Native FeaturePlacementRitual.checkRadius defaults7; each consumed Source Gem increases it1. Guide prose calls this7×7, but source builds x/z bounds±7then filters actual Euclidean distance≤7: **radius7, diameter15 bounding grid**, normally a circular same-Y candidate plane. Archpod offsets additionally scanY..Y+10 but retain that3D distance cap, so not every high column candidate is eligible.

Feature order: Bonemeal(distance6,chance0.4), tree(distance8), plant(distance2,chance0.1), pod(distance1,chance0.15), light(distance4,chance0.1). Without a selected sapling, tree randomly chooses among **all registered variants** and the ritual includes every variant's plant and pod feature in registry order. This0.7.11.3default differs from0.7.10.4's single Sourceberry plant/default pods0.5spacing/0.1chance. Registry initialized in ArsNouveauRegistry.postInit; five built-ins:

| Sapling/variant | Plant helper | Pod helper | Island pod selects |
| --- | --- | --- | --- |
| Blazing / native red | Torchflower | Bombegrante Pod | `ars_elemental:blazing_forest` |
| Cascading / native blue | Pitcher Plant | Frostaya Pod | `ars_elemental:cascading_forest` |
| Vexing / native purple | Sourceberry Bush | Bastion Pod | `ars_elemental:vexing_caves` |
| Flourishing / native green | Ground Spore Blossom | Mendosteen Pod | `ars_elemental:flourishing_forest` |
| Flashing / addon yellow | Sparkflower | Flashpine Pod | `ars_elemental:flashing_forest` |

No consumption of a natural tree pod is required for Forestation's **pod placement helper**; sapling choice plus valid supporting logs can bootstrap pod placements. This is a separate route from natural trunk generation, which [plant audit](ars-elemental-pinned-plants.md) found disabled for ordinary sapling growth. Native recipe `flashing_sapling` accepts any of the four native school saplings +Air Essence→1addon yellow sapling; therefore this branch need not begin with finding a natural Flashing forest. Current exact recipe resource was inspected separately; ordinary growth itself still does not attach pods automatically.

**Selection-order caveat:** current selectedSapling is a field changed by canConsumeItem, not recomputed from consumedItems at start. Any registered sapling sets it and returns true, with no one-sapling cap. A later **nonvariant item check clears it to null before delegating**, including accepted Source Gems or rejected arbitrary items. To teach a chosen variant, supply radius gems first, selected sapling last, avoid other item admission checks before empty-hand start, and confirm actual trees. Nearby loose items scanned before starting can clear it. Field is not serialized; reloading between augmentation/start or during operation reconstructs default selection. Old0.7.10.4biomeType behavior differs; do not carry the earlier selection assumptions forward.

Native feature engine shuffles candidates, attempts each feature until exhausted, and returns after a successful helper; at most1reported success/tick, but it may examine many failed candidates in one tick. It stores successes per feature name to reject positions within distance≤spacing. Success returns before incrementing positionIndex; next tick spacing normally makes that position fail before advancing. Different plants and light all inherit feature name `block` and **share spacing history**; pods name their own block description, trees use `random_tree`. Do not multiply probability constants by all grid positions and call it exact production.

- BonemealFeature creates temporary64BoneMeal stack, rolls0.4, applies to position.below with ANFakePlayer; no player-stack debit. This can grow eligible existing ground vegetation.
- RandomTreeFeature picks supplied sapling, requires replaceable target and sapling.canSurvive, sets sapling and calls advanceTree up to twice if still a SaplingBlock. Constructor0.8chance is stored but **never consulted in onPlace**. Successful helper can leave a sapling if actual growth fails; no80%tree-success guarantee.
- PlaceBlockFeature rolls0.1, requires non-air support below and target state.canSurvive, creates BlockItem fake-player placement with Direction.DOWN; it returns true after call regardless of placement result, or without a BlockItem. Target state is a support test, not proof final placed state matches every property. Flowers/pitcher multi-block space can reject placement.
- ArchpodFeature tries NORTH/SOUTH/EAST/WEST states in order, independent0.15roll and canSurvive at candidate. First eligible orientation triggers fake-player BlockItem.place using generic downward hit context; it does not pass selected oriented state into placement. Returns true regardless of placement return. Actual facing/support/log/pod outcome must be observed; do not guarantee a pod per chosen tree.
- PlaceableLightFeature rolls0.1and requires air; sets native Light Block, then randomizes LightTile RGBchannels with min10. Return does not check placement success. Lighting is produced as world blocks, no item loot/cost promised.

**Source cost inherited0**, no Source request in addon/helpers/FeaturePlacementRitual. Source Gems here enlarge the finite pass and are consumed augments, not fuel. Completion occurs when all feature candidate lists exhausted; the ritual clears on following brazier tick. No automatic repeat/reset or item output chest. Native save stores featureIndex/positionIndex/checkRadius, but setup rebuilds features/shuffled candidates; featureMap and chosen sapling are not persisted. Do not promise exact resumption/spacing/yield after reload; a reconstructed list may skip/revisit positions.

## Useful route and future quest coverage

An optional low-infrastructure material garden: acquire native sapling+Air Essence through ordinary Ars route, craft yellow sapling, make Archwood Forestation tablet from native Forestation tablet and four essences, prepare valid soil/space and logs in an expendable area, optionally radius gems, then yellow sapling last, activate. Look for actual yellow tree/Sparkflower/Flashpine pod and native light blocks. Repeat with another tablet only if needed; no guaranteed first-pass pod count. This is a plant/material bootstrap, not permission for generic logistics, an automated harvest economy or a mandatory forest biome quest.

Island branch requires Conjuration/Abjuration materials and renewable Source for continued terrain. Optional pod changes target biome; a tree-growing follow-up requires **separate Forestation tablet**, not an assumed island side effect. Notebook entries above and JEI show recipes; future quest text must add safe-area, actual radius, Source order and sapling-order corrections that guide prose omits.

| Future optional lesson | Teaching and observable output | Truthful detector/reward direction |
| --- | --- | --- |
| Conjure workable ground | Load island tablet, optional pod/gems, empty-hand start near Source; inspect Grass/Dirt and biome separately. | Tablet acquisition proxy +manual world/biome observation, positive XP and ordinary gardening supplies. No arbitrary detector claim. |
| Choose an Archwood garden | Make yellow sapling, gems first/sapling last, start Forestation in suitable area; inspect generated resources. | Tablet/sapling acquisition proxies do not prove successful features. Manual check actual tree/pod; positive XP +ordinary supplies, no boss/EMC bypass. |
| Understand biome versus features | Island sets terrain/biome request; Forestation places vegetation/lights with no biome conversion. | Manual acknowledgement or existing authentic event only after separate task-capability audit. No reward-generated auto-completion chain. |

Pilot35–37does not cover either operation. These two reports now audit all8registered Elemental rituals, **not whole-addon research completion**; current changed glyph/gear/Sauce recipients and final progression/pilot consolidation remain.

## Unrun acceptance cases

1. Island in replaceable air: observe first5terrain attempts before Source request; empty Source pauses further work, refill resumes. Solid nonreplaceable platform is skipped; test partial final batch, chunk/height boundary and reload counter separately.
2. Island native no-pod versus each of five valid **pod BlockItems**: compare target biome keys, placed layer shape and affected vertical/quart palette; unknown ArchfruitPod falls back, ordinary fruit item rejected. No automatic structures/trees expected from inspected call path.
3. Forestation no-source/default variant versus yellow-selected: compare actual plants/pods/trees; test gems-before/sapling-after and inverse order/rejected loose item/reload, including RNG and denied fake-player placement. Verify no Source debit, augment consumption and finite completion separately.
4. Obstructed/unsupported tree/pod/pitcher positions: record failed placements versus helper success/spacing; inspect natural-pod versus ritual-pod acquisition, actual facing, bounding radius and LightTile color. Do not classify a no-pod finite pass as evidence of guaranteed renewable yield.
