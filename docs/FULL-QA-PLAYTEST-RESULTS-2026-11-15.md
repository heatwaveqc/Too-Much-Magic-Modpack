# Full integration client playtest results — November 14–15, 2026

## Scope and tested snapshot

Windows / Prism Launcher, Minecraft 1.21.1, NeoForge 21.1.265, Java 21.0.7; disposable new worlds. User performed gameplay; assistant inspected local files, installed bytecode and logs. Computer Use denied access to the Java window, so gameplay observations below are user-reported, not automated client tests. Two-player tests were excluded by request.

- Tested integration commit recorded in the installed manifest: **`3d3dd9f6c12f52500da5130d14978e657f3b94dc`**.
- Downloaded `Too-Much-Magic-Full-Integration-Prism-UNSTABLE.zip` SHA-256: `f875ef02a5d18eee2c474f56e6bc375f39495e8055b4a8adfde3e1ea6a60cd5b`.
- Packwiz pack-file SHA-256: `4c56949bc60e736d26ec75d36c8cc37a32d5264ebdf2dd26395dc8a1bd8d71b8` (different artifact from the ZIP).
- All **153 mod files** with installer-recorded linked hashes matched local installer metadata. Both custom JAR hashes matched BUILD-MANIFEST: Monk `d6b2152a1bf7d23ec47c5b61ce5eca8809aa417d3d41e716b8d3f2b52f536c55`; Transformations `f1d5292522081aab8ca801e2b1d267f98443e14fd023eac1f09cfcafd04edf04`.
- Dragon Survival 2.0.74, GeckoLib 4.9.3, Comforts 9.0.5, Colorful Hearts 10.5.9, Tyz 6.4.2 installed. Pufferfish Skills absent; Pufferfish Attributes 0.8.3 and Improvable Skills 21.0.2 retained.

These results apply to that installed build only. Newer integration-branch changes and added magical-food cases require a new build/test. This is **not a survival-ready signoff**.

## Confirmed and partial results

| Area | Result | Evidence boundary |
| --- | --- | --- |
| Launch/world | PASS | Client startup log: 40.214 seconds; user created and entered worlds. No dedicated-server join tested in this session. |
| KubeJS | PASS loading | 2/2 startup, 1/1 client and 11/11 server scripts loaded with zero script errors/warnings; later server resource reload also clean. Does not prove every event path. |
| Ordinary vampire infection | PASS | Fresh-world infection gave Vampirism, not JAWR. Injury and hunger worked before ascension. |
| Lilith acquisition | PARTIAL | Lilith's Blood enabled JAWR, but immediate blood initialization/replenishment failed; see blockers. |
| Vampire Forest | PASS observed shelter | No sunlight issue inside; leaving caused instant death. Outside lethality still needs review, not a blanket sunlight PASS. |
| Vampirism native cure | PASS dual cleanup | User explicitly confirmed the cure removed both Vampirism and JAWR. JAWR-native ritual cure deferred. |
| Monk initiation | PASS | First Sunset completed; server log confirms faction join at 20:41:22. |
| Monk progression | PASS to rank 2 | Mastery notifications and rank/point accrual observed. Latest diagnostic: rank 2, 48/105 mastery, four distinct foods, three unspent native skill points. |
| Monk persistence/discipline | PASS reported | Relog retained progress; Step Assist worked; user confirmed the requested equipment suppression/restoration and repeat-food checks. Repeated foods awarded no discovery XP. |
| Tyz custom skills | PARTIAL player acceptance | User observed large mana capacity and fast regeneration and accepted this for this pass. Refunds, precise scaling, Thrift/Potency and zero native activity XP were not individually gameplay-verified. |
| Sea diet | PARTIAL | Fish accepted. Sushi tag issue below. Exact tested fish IDs not captured. |
| Cave diet | PARTIAL | Charred meat accepted; charred recipes looked correct to player. Full negative-food coverage not performed. |
| Colorful Hearts | PASS reported function | User confirms functioning; do not infer every high-health/status/overlay combination passed. Ascended blood HUD remains broken separately. |
| Improvable Skills | PASS | Active on first login to new world. |
| Teleport/QoL | PASS reported baseline | Teleport commands still work; user reports most existing QoL tested. No invented itemized pass for unnamed features. |
| Graphite/furnace | PASS tested cases | Coal routes into fuel slot; diamond smelts into graphite. Charcoal/blast-furnace variants not individually established. |
| Shop | PARTIAL | One coin bought two copper ore; mining yielded eight raw copper. Player reports intended operation. All 18 offers and full Fortune/processing/resale economy not exhaustively tested. |

## Blockers and observed bugs

### Monk native skill tree unavailable despite points

At rank 2 with **three unspent native skill points**, opening Monk's native tree still says **“You Cannot Learn Skills.”** Installed Vampirism maps this to `gui.vampirism.skill_screen.no_tab`, the no-tabs message, rather than specifically insufficient points. Monk's JAR contains its tree, configured tree and four skill nodes. Investigate runtime registry loading, faction/unlock predicate matching and client synchronization. Purchases and native Open Palm/Unarmored Poise/Fleet Footwork effects remain blocked; more grinding is not the remedy. No Monk page in Tyz is expected for this native pilot.

### Ascended blood fails until relog; blood HUD absent even when full

Immediately after Lilith ascension, player reported both pools empty and blood bottles/feeding through either mod ineffective. `/witchery vampire blood set @s 100` echoed success but actual blood reportedly stayed zero. **After relog, bottles replenished blood.** Treat as a relog-sensitive initialization/synchronization/ownership failure, not proof that bottles always fail. Feeding after relog, command-side state versus display, healing and paid-ability debits remain unverified.

No blood bar appeared with **empty or full** pools. Full blood without a bar is a confirmed display failure. Iron's mana bar shifted down to that same level: possible layout interaction, cause unproven. No duplicate bars were reported; absence is not successful duplicate suppression.

Leaving Vampire Forest caused instant death. Review native level-dependent sunlight behavior, starting health, protection and damage source before classifying this as a defect. Installed JAWR guide text describes instantly deadly sunlight at early stages, so the observation alone does not prove excessive damage.

### Dragon food and rendering observations

- Forest cannot eat sourceberries. Compatibility follow-up required; exact tested stack ID not captured.
- Forest can eat an enchanted golden apple but cannot eat an enchanted golden carrot. Review intended magical-food exceptions; capture carrot's registry ID before diagnosis. Neither observation alone defines the approved diet.
- Sushi lacks Sea's dragon-food tag. Identify exact sushi/mod and grain content; enable intended variants without blanket-enabling forbidden rice/grain dishes. Player did not explicitly report a successful consumption attempt for that sushi.
- Player reported visual glitches in dragon form and in inventory during Monk testing. Reproduction details absent; do not equate visual symptoms with a proven cause.

### TreeChop mastery accounting

Four felled trees yielded **four Monk XP total**. Suspected one credit per tree-felling action rather than eligible logs. Compare TreeChop break/drop events against Monk's `BlockDropsEvent` handler and define intended reward per chopping effort/log; prevent duplicate awards and trivial placed-log farming. Root cause not established.

## Owner requirements for the next build

1. **Ritual-only dragon acquisition:** in `config/dragonsurvival-server.toml` under `[general]`, set `start_with_dragon_choice = false` and `allow_dragon_choice_from_inventory = false`; retain `transform_altar = true` and `no_humans_allowed = false`. Both choice shortcuts were enabled in the tested build. Test a genuinely new player.
2. **Rank-1 Monk combat:** increase initial unarmed damage and unarmored defense so unarmed/unarmored hostile combat is a viable preferred progression route immediately after initiation. Exact values not selected; preserve gear suppression and purchase persistence. Add meaningful mastery actions; grass/dirt currently award none because excluded from `training_blocks`, consistent with implementation rather than a demonstrated broken hook.
3. **Magical Monk harvesting:** Ars Nouveau and Iron's block-breaking actions must grant mastery on eligible blocks even with casting implements held. Tool exclusion applies to physical harvesting tools. Current hook explicitly requires an empty tool; solve spell attribution/event coverage too, not just held-item permission. Test physical tool negatives, both spell providers, multi-block casts and duplicate-event prevention. This request does not relax unrelated weapon/combat rules.
4. **Starter rewards:** move both automatically granted Enigmatic trinkets into **Getting Started checkmark quest rewards**, once per player. Identify exact trinket IDs. Do not introduce forbidden items via guessed IDs.
5. **Bag:** move automatic starter bag grant to a quest reward and add a dedicated **bundle Curios slot** for it instead of the generic slot currently used. Confirm exact bag ID, slot acceptance, retained contents and relog behavior.
6. **Akashic Tome:** Tome spawns empty while Astral, Primal and Ars books arrive separately. Investigate supported-book consolidation/population; do not claim the Tome integration works merely because it spawns.

No gameplay/config changes were made during this QA pass. Assistant-suggested extra XP actions are proposals, not approved values or implemented features.

## Backend verification and additional findings

- Parsed 45 chapters / 180 quests / five groups; no duplicate definition IDs or missing quest dependencies. One bowl reward entry and one Spyglass reward entry; no paper/cookie/compass/XP-bottle item rewards. **Structural PASS does not imply valid runtime item IDs.**
- Six custom shop categories / 47 offers, including 18 ore offers. Four active 99-rank Core Magic skills, 99 one-SP prices each; four original category definitions plus Core Magic; level pool 1000 XP / one SP. Generated config disables native activity XP. Parsed 24 custom JSON files without syntax errors.
- Active instance transformation server config had three experimental acquisition/chassis/provider flags true, operator forced cure false. The world's serverconfig readme describes that directory as optional overrides of instance config. No world-specific transformation override existed; missing copied files there was not itself a failure.
- Mob census per run: 613 modded non-misc entity types, 129 classified, 484 unclassified. No forest/cave dragon classifications observed. No live neutrality/provocation/boss/minion behavior tested; static tags/census do not prove alliances.
- Installed Monk eligibility methods omit external dragon/physiology exclusion. A mixed-form success cannot prove form exclusivity. First Sunset bytecode uses 200 continuous ticks in Overworld sunset 11500–12500, within eight degrees of west horizon, no rain, eligible faction/equipment, non-Creative/non-Spectator. Rank thresholds 20/55/105; `/tmmmonk mastery` displays 105 as rank-4 total, not the next-rank requirement. Consider clearer UI wording.
- **Runtime quest item failures:** `cookingforblockheads:oven` and `biggerreactorsrework:cyanite_reprocessor` unknown. FTB files contained missing-item placeholders in Food & Fishing and Exceptional Power/Reactors. Fix references against actual registry before accepting affected tasks/icons.
- **ProjectE/Goety mapping:** container-item lookup for `goety:totem_of_roots` errors while mapping `goety:savage_tooth_craft` and `goety:empty_focus_craft2`. ERROR lines use PECore/FATAL logger marker; world continued. EMC mapping completeness remains open.
- **Animation parsing:** Dragon Survival `mass_buff`/`flapping_wings_standing`; Theurgy calcination oven start/stop (`easeinoutquint` expression); Ars Elemental spike `adjust_size`; Ars Nouveau Wilden Chimera `swim_charge`. Asset owners confirmed by installed JAR inspection. Visual impact requires reproduction.
- **Models/resources:** missing Botania barrel models (alfsteel, elementium, gaia, manasteel, terrasteel/flat variants); missing declared ProjectExpansion `accesstransformer.cfg`; malformed Draconic Evolution `en_us-old.json` and Cataclysm French/Polish translations encountered by PsiTweaks. Startup continued.
- Patchouli book parsing raised registry-creation exceptions referencing Vampire Forest/Werewolf Heaven. This does not prove either biome absent in the actual running world. Other stale references: `apotheosis:long_sundering`, `hexerei:belladonna_flower`, optional PsiTweaks/Mekanism icons. User independently observed Vampire Forest shelter.
- NeoForge normalized Quest Shop/Tyz common configs on startup; resulting activity-XP and mob-coin-drop settings remained disabled. Embeddium mixin-support warning and invalid asset paths also appeared; no crash inferred from those warnings.

## Remaining/deferred acceptance and handoff

- Retest native Monk tree/purchases, ascension before/after relog, blood HUD/healing/resource charges and corrected foods after fixes.
- JAWR-native Rite of Cleansing cure deferred; Vampirism Altar of Cleansing dual cure passed. Earlier garlic-injection advice was wrong: chair + garlic attempts hunter enrollment, not vampire cure; that rejection is not a bridge bug. Verified native vampire cure uses Altar of Cleansing empty-handed.
- Mahou compatibility/Catalyst Exchange testing explicitly deferred until intended compat is present; no unmodded-behavior assumption is counted as a compat PASS.
- Comforts spawn/day-skip, full negative-food/magical-food coverage, exact Tyz refunds/scaling, all shop offers/economy loops, mob AI, death persistence, cross-dimension/restarts/performance and multiplayer are not established by this session. Most existing QoL was accepted by user without an exhaustive new checklist.
- Local evidence retained: manifest/hash audit, startup/world logs, KubeJS server logs, census and bytecode inspection notes. **Raw logs, saves, downloaded mods and machine-specific paths are intentionally not committed**, per AGENTS.md. No new CI or dedicated-server run was performed for this documentation handoff.

The earlier running notes contained provisional observations; this report resolves them using the final player confirmations (notably dual cure PASS, rank-2 points confirmed, and bottles working after relog).