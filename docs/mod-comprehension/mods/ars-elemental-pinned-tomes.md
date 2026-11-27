# Ars Elemental 0.7.10.4 — twelve loot tomes and school casting

Exact addon/hash [receipt](ars-elemental-pinned-guide-urn.md). All12 caster-tome JSON records, six item registrations/SpellCasterRegistry providers; bundled Sauce SchoolCasterTome **including ETomeResolver**, SchoolCasterTomeData; exact native Ars5.13.5 CasterTome/CasterTomeData/CasterTomeRegistry/SpellResolver/SpellContext and [loot/payment audit](ars-nouveau-pinned-spell-records.md). No loot/casting/client tests. This closes tome source routes; whole addon remains researching for other major branches.

## Find and use a configured tome

These `ars_nouveau:caster_tome` files are **spell-data recipes**, not craft-grid or Apparatus recipes. Native matches returns false. Reload gathers **all namespaces'** records and makes result suppliers for the native dungeon-tome pool. Under native default spawnTomes=true, eligible vanilla chest global modifier has rare Tome roll0.1 and uniformly selects loaded supplier; [exact chest list and limitations](ars-nouveau-pinned-spell-records.md). Explore qualifying dungeon/temple/mineshaft/other listed chests, inspect named tome spell/tooltip. No particular chest is guaranteed to contain an Elemental or specific school tome, and this is not a claim about current assembled loot overrides.

Native CasterTome.use client dispatches current caster component's server-cast request; use configured item to cast its fixed spell with personal mana. Survival Scribes Table editing is **blocked** by inherited onScribe requiring creative. It does not teach all component glyphs, promote book tier or let survival player rewrite an empty school tome. No dedicated Elemental tome Notebook page is registered by AEDocumentation; refer to native tome information/tooltip and the precise recipe-data names below, not an invented standalone addon guide.

## Twelve exact preset records

All recipe IDs below start **`ars_elemental:tomes/`**. Native glyph names abbreviated without `ars_nouveau:glyph_`; **E:** means `ars_elemental:glyph_`. SpellData takes precedence over legacy duplicate spell field; both lists match for these12. School item IDs are `ars_elemental:<school>_caster_tome` (Anima item maps Necromancy).

| Recipe / display fallback | School item | Ordered spell recipe |
| --- | --- | --- |
|camel_tome / Humpday|earth|Self → Summon Steed → Extend Time×2|
|dolphin_tome / Poseidon's Steed|water|Self → Summon Steed → AoE → Extend Time×2|
|flower_tome / Poison Blossom|earth|Touch → E:Envenom → E:Poison Spores|
|frostbite_tome / Frostbite|water|E:Homing Projectile → Freeze → Cold Snap|
|glassmaker_tome / Safety Jar|manipulation|Underfoot → E:Conjure Terrain → AoE×3 → Pierce×3 → Crush → Smelt|
|hellflare_tome / Magiflare|fire|E:Homing Projectile → Ignite → Hex → Flare|
|leviosa_tome / Shulkium Leviosa|air|Projectile → Launch → Extend Time×2 → Delay → Wind Shear → Delay → Wind Shear|
|magnet_tome / Gravity Well|earth|E:Arc Projectile → Gravity → Sensitive → AoE → Extend Time → Linger → Sensitive → Launch → Dampen|
|shrimp_pistol_tome / Shrimp Pistol|water|Projectile → Bubble → Delay → Duration Down → E:Cavitate|
|skelehorse_tome / Undead Steed|anima|Self → Summon Steed → Extend Time×2|
|strider_tome / It's Striding Time|fire|Self → Summon Steed → Extend Time×2|
|zap_tome / Static Discharge|air|Touch → E:Spark → E:Discharge|

Name/flavor/color are data, not proof every advertised result occurs. [Steeds](ars-elemental-pinned-steeds.md), [poison combo](ars-elemental-pinned-venom-spores-rage.md), [empowering](ars-elemental-pinned-empowering.md), [Terrain/Spike](ars-elemental-pinned-terrain-spikes.md), [Cavitate](ars-elemental-pinned-cavitation-grave.md) and [Fire/static](ars-elemental-pinned-fire-static.md) explain actual recipient limits. In particular occupied Bubble does not necessarily pop, poison needs initial status, mounts are temporary, and gravity/lookahead/child propagation alter continuation. Do not turn flavor text into additional guaranteed mechanics.

## School component and focus specialization

Six registered SchoolCasterTome items implement **ISchoolFocus**, with school Fire/Water/Air/Earth/Necromancy/Manipulation and getDiscount **0**. ArsNouveauRegistry explicitly registers all six item→**`sauce:school_tome_caster`** component providers. SchoolCasterTomeData uses native AbstractCaster codecs (persistent/network-synchronized registry component), reads **context caster-tool item** to select school, falls back ELEMENTAL if not SchoolCasterTome, creates **ETomeResolver**.

Resolver.hasFocus behaves specially: if queried item implements ISchoolFocus, return whether its school is **exact same school object** as tome, **without consulting worn equipment for that branch**. Query native Shaper's Focus returns true for Manipulation tome; other items fall back native resolver. Thus a school tome can supply focus-dependent behaviors without owning/wearing a separately crafted focus, but does not provide all greater-focus Curios passives or every worn-focus check. NecroticFocus.hasFocus specifically scans worn Curios for its item; an Anima tome in hand is not that test.

ISchoolFocus.getFociSchools separately collects held school items and first worn focus; this is what steed substitutions inspect. Hand-held school tome can therefore select matching mount branch. Because tome getDiscount0 and first-focus discount selection searches hands first, a held school tome can also win over a worn elemental focus for that **event discount**, contributing0 there. It still inherits the tome casting-item discount below. Do not add every focus/Curio bonus automatically from “school tome” terminology.

Native Shaper's Focus block/entity propagation creates child resolver through getNewResolver; **ETomeResolver override preserves school** in that new resolver. The glassmaker preset's school-specific Shaper behavior is therefore supported through the recipient, not merely thematic flavor. Other event-created resolver classes may differ; no universal inheritance promise.

## Personal mana gate and actual expenditure

Inherited CasterTome.getManaDiscount returns integer **base spell cost/2**. Native discount calculation incorporates casting tool; other equipped and school modifiers/events can change final cost. School ETomeResolver enoughMana requires native mana capability, then accepts **discounted resolve cost≤current mana OR current mana==max mana OR creative**. A spell too costly for player's reserve can pass with a **completely full** bar; one point below full can fail. It still runs native spell validation and cancellable cast event.

This is not a zero-cost item. Native resolver expends successful cast's post-event cost through caster wrapper; ManaCap clamps reserve at zero. Full-bar exception does not literally replace cost with minimum(current,max) at the quote stage, and downstream effects/status/resource mutations retain their own gates. Component stores spell data; it does not create a persistent independent item mana tank. Right-click request/grammar/payment and actual original→child effects need client acceptance.

## Acceptance and remaining scope

Future explicit acceptance: pool membership/config/table overrides and loot rarity, preset names/component display, survival editing blocked, each composition grammar/actual output, held tome versus worn focus, full versus nearly-full mana, child school retention, mount/status/area limits and reloaded components. All39glyph recipe routes and12tome records now have source walkthroughs, but helpers/Siren, devices/lenses/relays, bangles/bags/other gear, worldgen/Vexing Caves, global selected Sauce version and final complete addon/pilot review remain open. No gameplay, runtime, quest or pin changes.
