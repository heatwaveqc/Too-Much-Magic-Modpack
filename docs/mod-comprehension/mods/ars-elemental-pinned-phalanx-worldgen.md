# Ars Elemental 0.7.11.3 — Phalanx registration, styles and remaining worldgen changes

Current artifact hash and initial delta: [pin receipt](ars-elemental-pinned-utility-rituals.md). Direct current/old CFR0.152 bodies, exact ZIP carver data, current ArsNouveauRegistry javap control flow and gzip/Base64-decoded Walker spell payload. [Parent](ars-elemental.md), [other current impact](ars-elemental-pinned-07113-impact.md), [Conflux scope](ars-conflux.md). Integration `c11a95fc14955b3e646885d37e33636b9e37ddf1` fetched; no Ars-family pin delta from17a564d8. No render/worldgen/cast/gameplay tests.

## Corrected current Phalanx availability

**0.7.11.3 registers Carian Phalanx unconditionally**, alongside Homing/Arc in registerGlyphs. The FMLLoader.isProduction branch at end is now empty. javap shows MethodCarianPhalanx.INSTANCE→register at bytecode144/147, production check240 followed by return246; this is not a decompiler-only assumption. Earlier0.7.10.4development-only conclusion does not describe current native registration.

Registration is not a complete acquisition path. Elemental current184recipes contain no glyph_carian_phalanx recipe. Newly pinned Conflux supplies one under **elemental_phalanx_enabled AND item_exists**; condition source returns startup.elementalPhalanx, defaultfalse. Its registration wrapper still toggles the now-empty production branch, so it no longer gates current Elemental registration. It can still gate its **own recipe**. No enable_elemental_phalanx/enable_nearby_allies override was found in current integration config/defaultconfigs/kubejs tracked paths; that bounded search is not an actual generated-world startup-config receipt or assembled donor verification.

When explicitly enabled in effective startup config, Conflux recipe costs native Scribes Table **160XP points**, consumed Homing Projectile glyph +Manipulation Essence +Conjuration Essence +Amethyst Shard →1Elemental Phalanx glyph. Item availability conditions and native learning/tier gates still apply. Research does not enable it or promise default survival craft. See [Conflux](ars-conflux.md) for remaining carrier/billing/optional recipe review.

## Native Phalanx operation if obtained

Method is Tier3, base mana175 before actual modifiers/budgets. onCast/onCastOnBlock/onCastOnEntity require nonempty remaining spell; server summons orbital projectiles, clients only return success. Split nominal count1+Split; tracks per-owner UUID list and cleans removed/launched entries. Above5requested total it removes oldest tracked entries, caps loop bound5and reserves existing indexes. Allocation loops index0..newTotal−1skipping reserved; noncontiguous surviving indexes can produce a different number of fresh projectiles than exactly1+Split. Do not promise a strict one-orb-per-cast count without observing partial formations. Spawn return unchecked. Conflux wraps summon/child resolver with VolleyBudget/carrier binding: source175 is not proof of final combined payment.

Each fresh orb receives child remaining-spell resolver, owner, index, integer acceleration/duration, AoE and ignore predicates from **current Homing.basicIgnores**. This replaces old animal exclusion; animals are no longer inherently excluded by that old predicate. Current default ignores all Players; Sensitive permits survival players while still excluding creative/spectator, plus dead/self/familiar/Flashjack/invisible/team/configured Sauce allies/context filters. Ordinary Homing.onCast's extra owned-pet predicates are **not** part of static basicIgnores. Native Phalanx own/other-owner creature behavior must be tested rather than assuming all pets protected.

Compatible native augments now Split, Accelerate, Decelerate, AoE, Extend Time, Duration Down and Sensitive. **Pierce/Amplify removed** from this form's compatible set although summon method still reads Pierce field; an internal read is not proof valid ordinary crafting accepts it. Effects later in remaining spell have their own augment rules.

Orbital state derives from synced IS_LAUNCHED; owner absent/removed calls attemptRemoval, which removes tracking entry. At age≥20, server checks targets only when age%20==index×2; closest qualifying LivingEntity in orb box inflate12launches with direction toward target×0.5. Without target and age>expiration/2at a check it shoots forward from owner, releases tracking slot. **Expiration=1200+200×integer duration modifier**, replacing old600coefficient. Boundary timing is staggered by index/world entity age, not an instant common timer.

Native orbit position uses owner eye/yaw, five-slot curved/bobbing formation and AoE scale; current server snaps to formation every10age ticks, client lerps each tick. Launched orb homes toward living target while distanceSquared<2500, otherwise uses native projectile movement. Server onHit posts SpellProjectileHitEvent; cancellation returns before resolution. Nonowner entity resolves remaining effects then attempts removal; block resolves only numSensitive>0. No portal traversal. Current entity registration explicitly noSave/fireImmune, tracking final20, velocity updates; save methods alone do not make a persistent saved pet.

## Visual customization, not a new progression currency

PHALANX_TIMELINE is native ProjectileTimeline for this method. Client renderer changes to StyledPhalanxRender; buildEmitters reads trail/resolve/spawn/flair and sounds, and selected ModelProperty may register looping animation. Registry exposes Arrow, Sword, Skull, Gear, Heart, Gust, Orb, Potion Jar and Ars Drake model resources; Potion Jar lacks this automatic run animation. Styled appearance does not itself change hit damage or create inventory items. Actual available GUI options/assets/render synchronization remain client acceptance.

No new physical style currency/crafting tree is established. If player can acquire enabled glyph, use Worn Notebook Glyph Index entry/native Spell Book particle customization UI, with quest text explicitly explaining effective recipe/config gate. These are optional projectile choices, not a compulsory spell tier after Archmage.

## Flashing Walker current preset

ModEntities replaces manually constructed spell/color with Spell.fromBinaryBase64. Gzip payload decodes to JSON, **not NBT**. Ordered recipe remains Projectile →Spark →Launch →Launch →Delay →Wind Shear, so earlier [Walker mechanical route](ars-elemental-pinned-flashing-walker.md) remains the same at that sequence level. Payload adds particle timelines/sounds and spell colorRGB(255,25,180); prior explicit walker.color assignmentRGB(200,150,15) is removed. Do not conflate spell-particle color with model field or claim old palette remains.

No native WealdWalker class body change from this addon delta; Awakening input/scanning and adult/baby lifecycle source findings remain version-scoped in that report. Conflux now overlays some global casting/projectile recipients, so final current combat behavior is still combined-review work.

## Vexing cave and Evaporate changes

Current packaged Vexing cave carver: probability **0.12**(old0.2); vertical origin Uniform aboveBottom8toabsolute80(old180); horizontal radius multiplier Uniform2..5(old2..6); vertical multiplier2..6/yScale2..5/floor−1..−0.4/lava aboveBottom8remain. These are carver parameters, not final biome Y bounds, probability of finding biome, number of trees or actual cave diameter. Changed ModWorldgen generation declaration matches current JSON. Existing [biome report](ars-elemental-pinned-biomes.md) needs this update when teaching exploration; Mage spawn Y/light still independent.

BlackstoneFormation radius3..7and x/z15bound already in [impact report](ars-elemental-pinned-07113-impact.md). ModItems reconstructed body is identical despite ZIP class-byte change; ModRegistry delta adds only SetHorn type/serializer in reviewed body. Other ArsNouveauRegistry additions are variant initialization, new models/timeline and current Phalanx registration; spell-model assets do not imply additional survival recipes.

EvaporateMixin still intercepts native EffectEvaporate block resolution: for exact Mud plus destroyRespectsClaim using resolved/fake player on ServerLevel, set Clay then cancel ordinary branch. New code additionally calls **ShapersFocus.tryPropagateBlockSpell** at that position/face before canceling, so trailing spell can propagate under actual focus/context eligibility. setBlock return unchecked; permission approval does not guarantee another event accepts write. It does not convert arbitrary dirt/soil or consume a crafting Clay input. Native propagation recipient and Conflux geometry/dispatch interactions remain to verify in combined review.

## Future teaching and unrun acceptance

| Branch | Player action/reference | Honest criterion and reward |
| --- | --- | --- |
| Optional enabled Phalanx | Effective startup recipe gate, native Scribes160XP recipe, Tier3book learning, then form followed by a real effect; test orbit/target/timer and stop before unsafe ally use. Notebook Glyph Index →Carian Phalanx/JEI Scribes Table. | Item acquisition only proves glyph possession; manual cast observation. Small positive XP +ordinary supplies, no automatic enabling/admin gift. |
| Current cave expedition | Use existing Archwood/biome guide; explain source carver changes do not guarantee coordinates or Mages. | Exploration/manual observation or authentic biome criterion after task audit; thematic ordinary reward. |
| Mud to Clay follow-up | Obtain Evaporate, valid delivery to Mud; check claim and Shaper eligibility, place a safe trailing effect. Native Glyph Index Evaporate/Shaper reference. | Item Clay is proxy; observe actual conversion/propagation separately. No repeatable conversion profit reward. |

Acceptance: default-versus-enabled Phalanx recipe and current item registry; remaining-spell-empty rejection; orbit count after some slots launch; natural/forced expiry and duration modifiers; animals/owned pets/allies/Sensitive players; canceled projectile hit, chunk unload/noSave, model/sound sync. Observe current Walker spell sequence/visuals independently. Generate disposable new terrain only when authorized to compare carver distributions; test Mud claim denial and trailing Shaper effect, not merely Clay possession.

This closes the specifically identified current Elemental non-Conflux version deltas; full184recipe/registry reconciliation and consolidated progression/pilot review still required. No whole-addon completion promotion.

**Acquisition correction from two-sided Additions review:** [Conflux content companion](ars-conflux-pinned-content.md) finds Ancient Codex candidate filter uses registered/enabled/unknown Tier3and exclusion tag, not Scribes recipe condition. Default Conflux tag does not exclude ars_elemental. Disabled Phalanx Scribes recipe therefore does not prove no random-learning route; effective pool/codex access still needs full Additions acceptance.
