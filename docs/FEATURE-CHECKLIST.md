# Too Much Magic — full feature & test checklist

> Generated from [the machine-readable registry](../manifest/feature-checklist.json) by 
> `python3 scripts/feature_checklist.py render`. Do not edit this Markdown by hand.

**112 acceptance items** across **9 tracks**. **5** have bounded owner-reported player PASS evidence; **13** have partial player checks; **21** are server-smoked only. This is not a stable release.

## Reading this checklist

- **Checked** means owner-reported player-tested PASS for this specific, bounded criterion—not full release sign-off.
- **Source ready** means code or configuration exists, not necessarily installed.
- **Server-smoked** means a component or pack boots; no player mechanic is implied.
- **Player partial** means at least one scenario was seen working, but the full acceptance criterion is still open.
- **Planned/blocked/deferred** are neither implemented nor tested.
- Evidence is retained even if later integration breaks a feature; update the state and evidence for new regressions.
- Every new feature, boss gate, cross-mod bridge or acceptance failure should get a stable ID here.

## Snapshot

| State | Items |
| --- | ---: |
| Player-tested PASS | 5 |
| Player test partial | 13 |
| Server-booted; mechanics unverified | 21 |
| Source ready; gameplay unverified | 20 |
| In progress | 11 |
| Planned | 41 |
| Blocked | 1 |
| Deferred | 0 |

## Master checklist

### Build pipeline (9)

- [ ] **All active implementation branches in one test build** (`build.integrate`) — *Server-booted; mechanics unverified*. Assemble every registered stream; fail on unknown branches and path/version conflicts; verify resulting server startup. [source](../manifest/integration-sources.json) [evidence](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/workflows/full-integration.yml)
- [ ] **Immutable integrated QA snapshot and provenance** (`build.lock`) — *Source ready; gameplay unverified*. Each experimental ZIP names the exact frozen source SHA, first-party jar checksums, upstream pinned mods, and pending features. [source](../scripts/build_full_integration_prism.py)
- [ ] **Prism GUI launch and fresh disposable world** (`build.prism`) — *Player test partial*. Import one full integration ZIP, reach title screen, create a fresh world, rejoin and inspect logs with all selected modules. [source](../docs/USER-TEST-LEDGER-2026-11-14.md) [evidence](../docs/USER-TEST-LEDGER-2026-11-14.md)
- [ ] **Dedicated server and multi-client parity** (`build.client_server`) — *In progress*. Same mod IDs and versions join successfully; no class loading, registry or keybind conflicts across singleplayer and multiplayer. [source](../scripts/smoke_server.sh)
- [ ] **Persistent private QA controls/UI/audio across downloads** (`build.profile`) — *Source ready; gameplay unverified*. Import old options, change binds and GUI scale, quit; next Windows QA instance restores them; no private profile in public pack. [source](../docs/PRISM-TEST-PREFERENCES.md) [evidence](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/workflows/test-prism-preferences.yml)
- [ ] **Candidate and Stable channels with explicit sign-off** (`build.stable`) — *In progress*. Only successful QA SHA becomes candidate; only manually accepted client/world build becomes immutable stable; rollback remains available. [source](../docs/BUILD-CHANNELS-AND-TESTING.md)
- [ ] **Selected 1.21.1 NeoForge mods and dependencies** (`build.modlist`) — *Server-booted; mechanics unverified*. All active mod JAR hashes and dependencies pass a full server boot and current player menus/registries checked. [source](../manifest/mods.json) [evidence](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/workflows/full-integration.yml)
- [ ] **Public release strips QA-only settings and commands** (`build.release`) — *Planned*. Public ZIP contains no developer options, local sync scripts, test commands, unfinished feature claims, or developer save data. [source](../docs/PRISM-TEST-PREFERENCES.md)
- [ ] **Performance and stability soak** (`build.performance`) — *Planned*. Mixed exploration + full magic base for extended single/multiplayer play; measure startup, memory, TPS, and crash-free save/reload. [source](../docs/TESTING.md)

### Introduction and quests (14)

- [x] **Five coherent categories and chapter navigation** (`quests.categories`) — *Player-tested PASS*. Introduction, Progression, Magic, Exploration & Combat, Endgame render and navigate properly; verify every chapter. [source](../docs/USER-TEST-LEDGER-2026-11-14.md) [evidence](../docs/USER-TEST-LEDGER-2026-11-14.md)
- [x] **Quest names, descriptions, localization and layouts** (`quests.localization`) — *Player-tested PASS*. Inspect title/description/icon/dependencies; no ERROR, undefined, untranslated text or overlap in sampled book. [source](../docs/QUEST-CLIENT-QA-FIX.md) [evidence](../docs/USER-TEST-LEDGER-2026-11-14.md)
- [ ] **Starter choice branching and no duplicate claims** (`quests.starter_path`) — *Player test partial*. Choose each option, receive exactly one reward, persist across logout/death; test multiplayer and Claim All. [source](../docs/USER-TEST-LEDGER-2026-11-14.md) [evidence](../docs/USER-TEST-LEDGER-2026-11-14.md)
- [ ] **Akashic Tome, 2 cooked beef and Improvable access** (`quests.starter_items`) — *Source ready; gameplay unverified*. Exactly once per player; survives relogs; no extra copies on old worlds, death or multiplayer join. [source](../docs/USER-TEST-LEDGER-2026-11-14.md)
- [ ] **One-time first-log Trinkets Pouch** (`quests.pouch`) — *Player test partial*. One pouch per player on first true log; not awarded again by quest claim, relog, or new inventory code. [source](../docs/QUEST-DESIGN-RULES.md)
- [x] **Getting Started beyond iron and optional first magic** (`quests.intro`) — *Player-tested PASS*. New player reaches magic/farming/exploration choices without forcing a particular magic school. [source](../docs/QUEST-CLIENT-QA-FIX.md) [evidence](../docs/USER-TEST-LEDGER-2026-11-14.md)
- [ ] **Complete original magic-school quest lines** (`quests.magic`) — *In progress*. Replace every 42 chapter scaffold; correct items/tasks/links and meaningful rewards for each installed school. [source](../manifest/quest_chapters.json)
- [ ] **EvilCraft 9-node pilot and useful Crushed Dark Gem reward** (`quests.evilcraft`) — *Source ready; gameplay unverified*. Confirm registry IDs, quest trigger, reward and dark gem processing on a fresh client. [source](../manifest/integration-sources.json)
- [ ] **A Sufficiently Advanced Magic — Draconic Evolution and CC: Tweaked** (`quests.tech`) — *Source ready; gameplay unverified*. Teach Draconic energy generation/storage/reactor and late-game CC: Tweaked monitor/control after Eldritch Miner; no Bigger Reactors or AE2. [source](../docs/POWER-TECH-QUEST-GUIDE.md)
- [ ] **Exploration and Combat boss progression quests** (`quests.combat`) — *Planned*. Meaningful ordered navigation through structures and bosses; no quest grants gated materials on claim. [source](../docs/QUEST-ARCHITECTURE.md)
- [ ] **Thematic, useful and occasionally funny rewards** (`quests.rewards`) — *Source ready; gameplay unverified*. Replace trash filler; maintain XP; no reward auto-completes a later quest except approved exception; cooking pot earns a bowl. [source](../docs/QUEST-DESIGN-RULES.md)
- [ ] **No XP-bottle rewards or banned filler** (`quests.no_xp_bottles`) — *Source ready; gameplay unverified*. Static validator rejects XP bottles; audit paper, compasses, bowls, cookies and every 42 placeholder. [source](../manifest/quest_content.json)
- [ ] **Task completion/consumption and quest reward delivery** (`quests.detection`) — *Player test partial*. Test every kind of task, multiple claims, xp and item rewards, consumption and team ownership. [source](../docs/USER-TEST-LEDGER-2026-11-14.md)
- [ ] **Sensible icons/links/client experience for 45 chapters** (`quests.tooltip`) — *Player test partial*. No obscured labels, overlapping lines, broken guides, bogus translations or unreadable reward controls. [source](../docs/QUEST-CLIENT-QA-FIX.md)

### Shop and economy (7)

- [x] **In-book Quest Shop button/coin balance** (`shop.button`) — *Player-tested PASS*. From FTB book see current coin balance and open shop. [source](../docs/USER-TEST-LEDGER-2026-11-14.md) [evidence](../docs/USER-TEST-LEDGER-2026-11-14.md)
- [ ] **Copper/emerald/diamond consuming sales** (`shop.coin`) — *Player test partial*. Each of twelve contract tasks consumes correct quantity, awards 1 team coin and XP, never mints duplicate money. [source](../docs/QUEST-SHOP-ECONOMY.md) [evidence](../docs/COMBINED-QUEST-VAMPIRE-QA.md)
- [ ] **Shop buy/claim and insufficient-funds behavior** (`shop.funds`) — *Player test partial*. Purchase common resource, decrement balance once, no purchase without money, reload UI accurately. [source](../docs/QUEST-SHOP-ECONOMY.md)
- [ ] **Useful supplies, magic materials, artifacts and food** (`shop.catalog`) — *Server-booted; mechanics unverified*. All five categories and every entry use actual item IDs; prices and delivery verified in UI. [source](../manifest/quest_shop_catalog.json)
- [ ] **Vanilla ore block market entries** (`shop.ores`) — *Source ready; gameplay unverified*. All 18 ore listings appear, deliver correct ore, prices cannot print net-positive currency with Fortune. [source](../manifest/integration-sources.json)
- [ ] **EMC-to-shop currency permitted, but without coin duplication** (`shop.loops`) — *Planned*. Test late-stage bulk sales and resale arbitrage, commodity pricing and no boss-critical item purchase. [source](../docs/QUEST-SHOP-ECONOMY.md)
- [ ] **Team-shared currency and repeatable-quest safety** (`shop.multiplayer`) — *Planned*. Two teammates cannot claim duplicate coins; cooldown and team switching cannot create bonus money. [source](../docs/QUEST-SHOP-ECONOMY.md)

### Magic schools and cross-mod casting (14)

- [ ] **Occultism summoning, storage and miners** (`magic.occultism`) — *Server-booted; mechanics unverified*. Summoning/crushers/miners/storage operate; Eldritch Ancient Miner is the exclusive survival source of the new Logic Core; Spirit Fire transmutes Silk-Touched End Draconium Ore to Iesnium Ore, both in-world and JEI. [source](../docs/DESIGN.md)
- [ ] **Iron's spells and spellbooks** (`magic.iron`) — *Server-booted; mechanics unverified*. Use spellbooks, casting and scaling; conflicts with Ars and item scripts checked. [source](../docs/DESIGN.md)
- [ ] **Ars Nouveau and approved addons** (`magic.ars`) — *Server-booted; mechanics unverified*. Spell crafting, source machines and addon spells work; no overpowered conversion exploit. [source](../docs/MAGIC-MOD-SURVEY-DECISIONS.md)
- [ ] **Neo Vitae altars, EV networks and tier progression** (`magic.neovitae`) — *Server-booted; mechanics unverified*. EV extraction, upgrades and network use; max-value and torment mechanics remain functional. [source](../docs/DESIGN.md)
- [ ] **Mahou circles, costs and resource interactions** (`magic.mahou`) — *Server-booted; mechanics unverified*. Costs, drain, catalyst exchange and high-maximum cap checked in-world. [source](../docs/QUEST-CLIENT-QA-FIX.md)
- [ ] **Botania Revived progression and mana sources** (`magic.botania`) — *Server-booted; mechanics unverified*. Mana generation, tablets/rings, Terrasteel and Gaia ritual; client and server agree. [source](../docs/MAGIC-MOD-SURVEY-DECISIONS.md)
- [ ] **Astral Sorcery sky/constellations and altars** (`magic.astral`) — *Server-booted; mechanics unverified*. Attunement, starlight, 17x17 structure and effects render and work alongside overlays/shaders. [source](../docs/COMPATIBILITY.md)
- [ ] **Roots Classic Terra and first-staff pickup** (`magic.roots`) — *Source ready; gameplay unverified*. Once-only staff detection, Terra cast, KubeJS link, reload and multiplayer isolation. [source](../manifest/integration-sources.json)
- [ ] **EvilCraft blood, broom, and materials** (`magic.evilcraft`) — *Server-booted; mechanics unverified*. Blood power and broom use function; dark gem processing aligns with unification and quest line. [source](../docs/QUEST-CLIENT-QA-FIX.md)
- [ ] **JAWR witchcraft and Forbidden Magic lich** (`magic.jawr`) — *Server-booted; mechanics unverified*. Rituals, hexes, blood/hunger, lichhood and model effects work without incompatible overlapping states. [source](../docs/TRANSFORMATION-COMPATIBILITY.md)
- [ ] **Goety, Hexerei, Malum, Fey and ritual content** (`magic.other`) — *In progress*. Smoke selected schools; verify progression, rituals, block/items, worldgen and cross-mod identity. [source](../docs/MAGIC-MOD-SURVEY-DECISIONS.md)
- [ ] **HexCast NEOFORGED and Psi programmable magic** (`magic.hexcast`) — *Server-booted; mechanics unverified*. Mana/cast cost behavior, failures, client effects, and no infinite XP/conversion loops. [source](../docs/MAGIC-MOD-SURVEY-DECISIONS.md)
- [ ] **Magic seeds and Farming for Blockheads gating** (`magic.market`) — *Planned*. Market seeds enforce chosen magical scarcity; no alternate shop or loot bypass. [source](../docs/PROGRESSION.md)
- [ ] **Every unique material has renewable magical path** (`magic.renewable`) — *Planned*. Audit all items; document renewable source, tier and test in-world without bypassing gates. [source](../docs/DESIGN.md)

### Resources, dusts and recipes (11)

- [ ] **Single canonical silver, lead and shared raw resources** (`materials.ores`) — *Source ready; gameplay unverified*. Verify configured canonical ingots, ores, blocks and tag entries; no multiple conflicting outputs. [source](../manifest/integration-sources.json)
- [ ] **Neo Vitae dusts as canonical dust outputs** (`materials.neodust`) — *Source ready; gameplay unverified*. Raw ore crushing and alternative recipes consistently yield Neo Vitae dust with shared tags. [source](../manifest/integration-sources.json)
- [ ] **Occultism crushing to canonical outputs** (`materials.occultism`) — *In progress*. One and doubled dust paths yield identical canonical item and correct smelt yield. [source](../docs/QUEST-CLIENT-QA-FIX.md)
- [ ] **Mahou Catalyst Exchange recognizes Neo Vitae dust** (`materials.mahou`) — *Source ready; gameplay unverified*. Dropped items detected and mana paid; zero-mana attempt fails; partial stacks preserve count and no duplicates. [source](../manifest/integration-sources.json)
- [ ] **Unified silver weapons and armor** (`materials.silver`) — *Server-booted; mechanics unverified*. Nine silver slots each resolve to one canonical meaningful output; damage/armor/silver effects in client verified. [source](../docs/COMPATIBILITY.md)
- [ ] **Polymorph retained as failsafe, not needed ideally** (`materials.polymorph`) — *In progress*. Native report lists zero unintended recipe conflicts after fixes; player JEI choices reviewed. [source](../docs/TESTING.md)
- [ ] **Sulfur, quartz, lead, copper and related overlaps** (`materials.other`) — *In progress*. Run recipe census and explicit cross-mod processing exceptions; prevent double ingot printing. [source](../docs/COMPATIBILITY.md)
- [ ] **Deep uranium/draconium scarcity** (`materials.oregate`) — *Planned*. Worldgen and miner recipes restrict these resources; cannot farm normal shallow ores for free. [source](../docs/PROGRESSION.md)
- [ ] **Deepslate Replica Ore exclusive miner route** (`materials.replication`) — *Planned*. No natural ore worldgen/loot bypass; Eldritch Ancient Miner produces ore at controlled late tier. [source](../docs/PROGRESSION.md)
- [ ] **Eldritch Ancient Miner tier recipe** (`materials.miner`) — *Planned*. Two to four Marid, late Cataclysm material requirement, renewable but challenging path. [source](../docs/PROGRESSION.md)
- [ ] **No bag-within-bag across all item handlers** (`materials.bags`) — *Blocked*. Check every magic storage pair incl Curios, vanilla bundles, corpse and nested containers; forbid cycles. [source](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/16)

### Tyz Skills and MXP (12)

- [ ] **Tyz's Skills 6.4.2 pinned, Pufferfish Skills removed** (`skills.backend`) — *In progress*. Exactly pinned Tyz runtime, no Pufferfish Skills, Attributes retained if dependencies require; tree loads in Prism. [source](../manifest/integration-sources.json)
- [ ] **Regular 99-rank Core Magic tree** (`skills.core`) — *Source ready; gameplay unverified*. All configured core nodes, prices, ranks, UX and prerequisites loaded and spend correctly. [source](../manifest/integration-sources.json)
- [ ] **Dedicated Werewolf, Vampire, Monk, Dragon and Lich trees** (`skills.forms`) — *Planned*. Visibility reflects transformation; no crossover purchase, grant bypass, ghost tree or lost points. [source](../manifest/integration-sources.json)
- [ ] **Sequential ranks and external milestone prerequisites** (`skills.ranklocks`) — *Planned*. Rank N requires N-1; external unlock only works when milestone achieved; no bypass via /grant/refund. [source](../manifest/integration-sources.json)
- [ ] **Universal MXP ledger and 1.2^P normalization** (`skills.mxp`) — *In progress*. Only MXP grants global skill XP, correct scaling and accounting; no double count. [source](../manifest/integration-sources.json)
- [ ] **First boss defeat pays 1000 effective MXP** (`skills.boss`) — *Planned*. Exactly once per unique boss per player; all later kills grant no first-kill bonus. [source](../manifest/integration-sources.json)
- [ ] **Ars spent Source -> MXP** (`skills.ars`) — *Planned*. Actual player-spent costs only; no refill or passive producer farming; one credit per cast. [source](../manifest/integration-sources.json)
- [ ] **Iron's spell mana -> MXP** (`skills.iron`) — *Planned*. Actual mana spent only, cancellation/refunds and zero-cost casts handled correctly. [source](../manifest/integration-sources.json)
- [ ] **Botania player-owned inventory/Curios mana -> MXP** (`skills.botania`) — *Planned*. Only mana from carried tablets/rings; network pools, machines, other-player resources excluded. [source](../manifest/integration-sources.json)
- [ ] **Neo Vitae EV-network expenditure -> MXP** (`skills.neo`) — *Planned*. Only network-paid actual EV, not source creation, transfers or buffered/inventory fake expenditure. [source](../manifest/integration-sources.json)
- [ ] **Mahou XP efficiency and very high cap** (`skills.mahou`) — *Planned*. Verify cap beyond 100k; prevent self-powered XP loops and refund exploits. [source](../manifest/integration-sources.json)
- [ ] **Improvable Skills independent engine and starting access** (`skills.improvable`) — *Player test partial*. Personal upgrade path unaffected by Tyz, no duplicate XP economy, book accessible as designed. [source](../docs/USER-TEST-LEDGER-2026-11-14.md)

### Transformations and Monk (14)

- [ ] **JAWR-only vampire acquisition** (`form.vampire_acquire`) — *Planned*. JAWR native infection, progression and cure work without TeamLapen. [source](../docs/VAMPIRE-CHASSIS-V1-TEST-PLAN.md)
- [ ] **JAWR-native blood accounting** (`form.blood`) — *Planned*. JAWR alone owns blood feeding, healing and debit; no mirrored pool. [source](../docs/USER-TEST-LEDGER-2026-11-14.md)
- [ ] **JAWR natural hunger / periodic blood rules** (`form.hunger`) — *In progress*. Native JAWR hunger still acts; regular vs ascended differences intentional and no duplicate upkeep drains. [source](../docs/VAMPIRE-CHASSIS-V1-TEST-PLAN.md)
- [ ] **JAWR vampire cure** (`form.cure`) — *Planned*. JAWR cure clears native vampire state and skills, not an extra faction. [source](../docs/TRANSFORMATION-COMPATIBILITY.md)
- [ ] **JAWR sunlight and protection** (`form.sunlight`) — *Planned*. JAWR sunlight works without the discontinued Vampire Forest. [source](../docs/TRANSFORMATION-COMPATIBILITY.md)
- [ ] **Mahou Drain Life boundary restores vampire hunger** (`form.drainlife`) — *Planned*. Drain Life effects credit appropriate vampire satiation without double-healing exploits. [source](../manifest/integration-sources.json)
- [ ] **JAWR lycanthropy** (`form.werewolf`) — *Planned*. JAWR lycanthrope, moon behavior, food, cure and relog work without Werewolves addon. [source](../docs/TRANSFORMATION-COMPATIBILITY.md)
- [ ] **Dragon Survival and food compatibility** (`form.dragon`) — *Planned*. Approved pin/loader, dietary toggle, AppleSkin, equipment, rendering, flight and cure paths tested. [source](../docs/FOOD-AND-SPECIES-DESIGN.md)
- [ ] **JAWR/Goety lich state and phylactery** (`form.lich`) — *Planned*. Lich upgrades, death/revival, diet, gear and storage functional without lost player state. [source](../docs/TRANSFORMATION-COMPATIBILITY.md)
- [ ] **Exclusive primary physiology and safe transitions** (`form.exclusive`) — *Planned*. Reject incompatible cross-mod transformations; reversible conversions clean all flags; explicit exceptions only. [source](../docs/TRANSFORMATION-COMPATIBILITY.md)
- [ ] **Standalone Monk First Sunset** (`monk.sunset`) — *Planned*. Sunset joins native Monk progression path. [source](../docs/USER-TEST-LEDGER-2026-11-14.md)
- [ ] **Standalone Monk native tree** (`monk.tree`) — *Planned*. tyr_progression skills and XP persist; no Vampirism faction UI. [source](../docs/USER-TEST-LEDGER-2026-11-14.md)
- [ ] **Monk Step Assist +0.5 height** (`monk.step`) — *Player test partial*. Moves over allowed half-block heights only; armor and weapons may suppress where designed. [source](../docs/USER-TEST-LEDGER-2026-11-14.md)
- [ ] **Monk training progression and graphical progress indicator** (`monk.training`) — *Source ready; gameplay unverified*. Actual cultivation earns native points; full UI progress ring replaces temporary actionbar counter. [source](../manifest/integration-sources.json)

### World, combat and endgame (16)

- [ ] **Tectonic world generation and exploration density** (`world.tectonic`) — *Server-booted; mechanics unverified*. Fresh terrain, structures, world boundaries and terrain performance inspected in new worlds. [source](../docs/DESIGN.md)
- [ ] **Cataclysm, Neo Vitae, Deeper and Darker and chosen structures** (`world.dungeons`) — *Server-booted; mechanics unverified*. Locations generate, enemies spawn correctly, loot balanced, no missing structures or softlocks. [source](../docs/DESIGN.md)
- [ ] **Apotheosis affix, loot and player power** (`world.apotheosis`) — *Server-booted; mechanics unverified*. Gear drops and enchantment progression sensible; no one-hit exploits or premature boss skips. [source](../docs/DESIGN.md)
- [ ] **Forbidden items absent everywhere** (`world.loot`) — *Planned*. Ring of Seven Deadly Sins, Re:Avaritia and Extended Crafting unavailable via loot, trade, chest, reward and drops. [source](../docs/PROGRESSION.md)
- [ ] **Boss first-kill, personal/team accounting and replay** (`world.boss_credit`) — *Planned*. Credit only eligible kills including revived/respawned boss; verify offline teammates/party and restart persistence. [source](../docs/PROGRESSION.md)
- [ ] **Renewable boss drops and advanced crafting routes** (`world.survival`) — *Planned*. All boss-critical items and rare resources eventually reproducible by explicit not-trivial path. [source](../docs/DESIGN.md)
- [ ] **Philosopher's Stone with interchangeable magical boss trophies** (`end.projecte_start`) — *Source ready; gameplay unverified*. Remove both original diamond-center recipes, keep original redstone/glowstone outer eight; accept exactly one Wilden Tribute, Gaia Spirit or Neo Vitae Mine Key in center, and test all three paths in JEI and in survival; no personal kill tracking. [source](../docs/PROJECTE-TIERED-GATING-PROPOSAL.md)
- [ ] **Chaos Shard required for Red MK3+ Power Flowers** (`end.projecte_high`) — *Source ready; gameplay unverified*. Direct MK3 and every higher direct Power Flower recipe plus MK3 upgrade consume one Chaos Shard; MK1/2 remain, later upgrades still need previous flower; review Matter Upgrader and EMC bypass. [source](../docs/PROJECTE-TIERED-GATING-PROPOSAL.md)
- [ ] **Project Expansion tier progression and alternative bypasses** (`end.expansion`) — *In progress*. MK1/2 function normally; MK3+ direct and MK3 upgrade use Chaos Shard; no unintended early bypass via Matter Upgrader, EMC knowledge, trading or loot; all related recipes usable in legitimate progression. [source](../docs/PROJECTE-TIERED-GATING-PROPOSAL.md)
- [ ] **Draconic Chaos Guardian encounter and materials** (`end.chaos`) — *Server-booted; mechanics unverified*. Fight spawns correctly, including shields/ranged attacks; materials only from legitimate progression. [source](../docs/PROGRESSION.md)
- [ ] **Draconic Energy Core, pylons and crystal links** (`end.energy`) — *Source ready; gameplay unverified*. Built multiblock and full FE network work; differentiate storage from generation. [source](../docs/POWER-TECH-QUEST-GUIDE.md)
- [ ] **Draconic Reactor and CC computer supervision** (`end.reactor`) — *Source ready; gameplay unverified*. With a disposable reactor, confirm native CC peripherals for stabilizer/injector, Flux Gate and Energy Pylon; test monitor status, safe shutdown and native failsafe independently of Lua; no Bigger Reactors components. [source](../docs/POWER-TECH-QUEST-GUIDE.md)
- [ ] **Prevent EMC-positive loops and unintended acquisition exploits** (`end.emc_abuse`) — *Planned*. Check Fortune/Silk Touch, ore/dust conversion, reversible alchemy, Quest Shop, boss trophy/Chaos Shard EMC duplication, stored inventories, filled tools and dynamic data for profitable bypasses. [source](../docs/PROJECTE-TIERED-GATING-PROPOSAL.md)
- [ ] **Every registered item has EMC, barring approved cheap exceptions** (`end.emc_every_item`) — *Source ready; gameplay unverified*. Export the exact full item registry and post-mapping ProjectE values; positive EMC for 100% of registered items apart from approved tiny glass panes; no silent skips and zero missing in enforced audit. [source](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/feature/projecte-boss-trophy-stone-and-emc-audit/manifest/emc-coverage-policy.json)
- [ ] **EMC values safe for all magic schools and all item state** (`end.emc_balancing`) — *Planned*. Audit expensive bosses, rare loot, recipes, transforms and value limits, NBT/contents and automatic reproduction so setting an EMC number never creates an uncontrollable conversion or duplication loop. [source](../docs/PROJECTE-TIERED-GATING-PROPOSAL.md)
- [ ] **Three verified boss-associated trophy ingredient routes** (`end.projecte_trophies`) — *Source ready; gameplay unverified*. Validate actual registry IDs ars_nouveau:wilden_tribute, botania:life_essence and neovitae:mine_key; document Mine Key also has trial-spawner source and verify all are consumed. [source](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/feature/projecte-boss-trophy-stone-and-emc-audit/kubejs/data/tmm/tags/item/philosophers_stone_boss_trophies.json)

### QoL, food, multiplayer and release (15)

- [x] **FTB Essentials /home, /sethome, /rtp** (`qol.travel`) — *Player-tested PASS*. Basic travel command works; limit, cooldown and claims boundary tests still tracked separately. [source](../docs/USER-TEST-LEDGER-2026-11-14.md) [evidence](../docs/USER-TEST-LEDGER-2026-11-14.md)
- [ ] **FTB Essentials safety, permissions and persistence** (`qol.travel_limits`) — *Planned*. Non-OP one home, /delhome, /listhomes, /rtp safe destination, claims, reconnect and cooldown. [source](../docs/BUILD-CHANNELS-AND-TESTING.md)
- [ ] **Comforts sleeping bags and hammocks** (`qol.comforts`) — *Planned*. Pin exact NeoForge file, server+client boot, portable sleep without respawn change, hammock day-to-night. [source](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/34)
- [ ] **Colored layered hearts instead of tall heart rows** (`qol.hearts`) — *Planned*. Appropriate approved mod loads; clear health display at high maximum HP without conflict. [source](../docs/TRANSFORMATION-COMPATIBILITY.md)
- [ ] **TrashSlot and recover deleted item** (`qol.trash`) — *Player test partial*. Trash hotkey and recover-last behavior; no risk to valuable stack and no conflicts with inventory bindings. [source](../docs/USER-TEST-LEDGER-2026-11-14.md)
- [ ] **TreeChop and Leaves Be Gone** (`qol.trees`) — *Player test partial*. Several hits fell tree; sneak bypass, fast leaf decay, sapling drops, special trees and claims behave. [source](../docs/USER-TEST-LEDGER-2026-11-14.md)
- [ ] **JEI, Jade, light/chunk overlays and tooltips** (`qol.overlays`) — *Player test partial*. F7/F9, hover tooltips, zoom, sorting, recipe search and hotkeys agree in Prism. [source](../docs/USER-TEST-LEDGER-2026-11-14.md)
- [ ] **Backups, Tombstone, Lootr and chunk claims** (`qol.backup`) — *Server-booted; mechanics unverified*. Backup restore, death retrieval, per-player Lootr and permissions/claim protection verified. [source](../docs/DESIGN.md)
- [ ] **Pam's full crop/fruit and right-click harvest** (`food.crops`) — *Server-booted; mechanics unverified*. Mature common, Pam's, and Farmer's crops harvested without duplication, root damage or unwanted replant. [source](../docs/FOOD-AND-SPECIES-DESIGN.md)
- [ ] **Farmer's Delight / Cooking for Blockheads / Pam's** (`food.cooking`) — *Player test partial*. Cooking pot, board, market, ovens and recipe tags provide real edible outputs. [source](../docs/FOOD-AND-SPECIES-DESIGN.md)
- [ ] **Aquaculture fishing and modded recipes** (`food.fishing`) — *Server-booted; mechanics unverified*. Fishing gear, catches, food prep and modded fish tags work across biomes. [source](../docs/FOOD-AND-SPECIES-DESIGN.md)
- [ ] **Food remains useful for different transformations** (`food.nutrition`) — *Planned*. Normal meals, alternative nourishment and vampire/dragon diets retain reasonable gameplay purpose. [source](../docs/FOOD-AND-SPECIES-DESIGN.md)
- [ ] **Two-player real-world acceptance test** (`release.multi`) — *Planned*. Different players join, quests/team coins, dimensions, progression, first loot and death states survive restart. [source](../docs/TESTING.md)
- [ ] **Fresh-world milestone-to-endgame run** (`release.fresh`) — *Planned*. Play introduction, early schools, boss gates, machines, advanced resources, transformations and late bosses without cheat paths. [source](../docs/TESTING.md)
- [ ] **Final change log, support docs and feedback intake** (`release.docs`) — *Planned*. Document mod selection, controls, performance, expected incompatibilities, server install, changelog and rollback. [source](../docs/BUILD-CHANNELS-AND-TESTING.md)

## When to check a box

1. Record the **specific build SHA or artifact** and a client/real-world outcome in the evidence source.
2. Run the concrete acceptance test and meaningful negative/bypass cases (including relog and multiplayer where relevant).
3. Promote **only that row** to `player_pass`; keep adjacent incomplete features open.
4. Do not auto-mark gameplay PASS from a Gradle test, JSON validator, dedicated-server ready line or a generated quest.

Update safely: `python3 scripts/feature_checklist.py mark <id> <state> --evidence <path-or-url>`
followed by `python3 scripts/feature_checklist.py render`. Use `python3 scripts/feature_checklist.py render --check` in CI.

## Gate policy

The former all-post-Chaos ProjectE policy is superseded. See [the boss-trophy recipe policy](PROJECTE-TIERED-GATING-PROPOSAL.md): interchangeable Ars/Gaia/Foreman-associated drops in the Stone's center, and Chaos Shard in Red MK3+ direct Power Flowers and the MK3 upgrade. These recipe changes are source-staged pending in-world QA, **not personal boss-kill permissions**; Matter Upgrader and EMC synthesis are separate tests.
