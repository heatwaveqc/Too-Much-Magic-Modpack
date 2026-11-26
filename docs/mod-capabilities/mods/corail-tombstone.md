# Corail Tombstone 9.5.6 — Mod Capability Atlas

**Research scope:** exact TMM pin for Minecraft 1.21.1 NeoForge; static binary/resource inspection, 2026-11-25. Owner directed full 29-axis research in addition to (not replaced by) the 11-domain classification. Evidence-only work; no quest, recipe, config, registry, path, pin or gameplay changes. Tracker [#410](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410).

## 1. Provenance and census

[Packwiz pin](../../../mods/corail-tombstone.pw.toml), upstream [Modrinth project](https://modrinth.com/mod/YT5K34AB), version e68NhQJm. Exact Drive-delivered tombstone-neoforge-1.21.1-9.5.6.jar (2,520,705 bytes), SHA-512 c987d0321bf9c38107c33882c23761963c2a76bd6b24baf24b83ae846efd56d8f29ecb9bec8a80092f1624910ed10d2b7e3d07cf2dde3984524fb65d894a539c, matches pin. ZIP CRC: all 1757 entries readable. 590 compiled classes; 103 recipe JSON records, 238 advancement JSON records = 127 adventure + 22 event + 89 recipe; 149 non-recipe. 63 grave_guardian_trades, 55 loot tables, 40 native tag JSONs, 12 enchantments, 7 worldgen records. 27 registered effects and 10 registered potion types. Resource paths: data/tombstone/{recipe,advancement,enchantment,grave_guardian_trades,loot_table,worldgen}; assets/tombstone/lang/en_us.json and text/*_en_us.txt. Compiled namespace ovh.corail.tombstone; javap -p -c inspection of perk, registry, capability, event, item, grave, config, effect and prayer classes. Counts are records not guaranteed unique playable operations; no copyright JAR redistribution.

## 2. The actual first-to-mature experience

Acquire Grave Dust from eligible undead/loot, craft grave marble and a decorative grave, then wait for a soul. Make an Ankh of Prayer (4 Grave Dust, 4 wooden rods, 1 gold ingot), pray near a grave. PrayerHelper awards one Knowledge of Death point and a beneficial effect on success; Elyra's Diary unlock changes it from one to four effect attempts. First perk point at knowledge 2, not vanilla XP level 2. Choose a branch, not a required all-perk campaign. A player's actual death produces a separate player grave and key under configured rules. Never force death to finish the introductory teaching spine. Activate crafted magic scrolls/tablets or books with a grave soul. Parallel paths: death protection/recovery, character perks, equipment augmentation, alignment prayers, companions, Guardian trade, fishing, exploration and five Watcher mysteries. All are usable specialties; neither alignment pole is mandated.

## 3. All 29 capability axes

| Axis | Status | Concrete native mechanism and upper boundary |
| --- | --- | --- |
| gear | present, supplemental | Ankh of Prayer, bone needle/scepter, Misadventure rod, magic books, scrolls, tablets, Voodoo Poppet, Flying Carpet. No complete weapon/armor/tool tier set; enchant externally supplied equipment. |
| movement | present | Shadowalker sneak speed; Feather Fall, Aquatic Life, Weaver's Walk; soul-enchanted Recall/Home/Assistance/Lost tablets; Flying Carpet unlocked via Watcher lore. No unconditional free travel. |
| ore_processing | absent | Recycling returns eligible crafting ingredients, not a general ore crusher/smelter. No ore doubling or refining machine. |
| storage | limited, special-purpose | Graves hold death inventory, Scroll of Knowledge stores XP at loss, Receptacle stores a dead familiar's identity; none is general living-player storage. |
| crafting | present | Grave dust + marble graves; Strange Scroll/Tablet blanks; soul activation; 7 book classes, recipes and component-preserving operations; no general crafting network. |
| logistics | limited, special-purpose | Ancient Recall/Home tablets move nearby eligible creatures; Grave Key retrieves death contents. No item pipes, sorting or transfer network. |
| buffs | present | Scroll of Feather Fall/Reach/True Sight/Aquatic Life/lightning/frost resistance/Purification/Mercy/Preservation/Unstable Intangibility; prayers, potions, Watcher rites and perks. Timed, target-specific or conditional. |
| external_compatibility | present, bounded | Native NeoForge death, drops, visibility, enchantment, damage, bonemeal and conversion events; native integration tags for Capsule, Carry On, MineColonies, Twilight Forest; tags do not prove installed interaction. TMM Smart Keep, Ars Soulbound, Apotheosis enchantments and Tyr Path hooks are separate. |
| mob_farming | limited | Grave Dust/loot from eligible undead and Treasure Seeker improve drops; controlled undead and spectral wolves fight, but no dedicated automated spawner or virtual mob loot machine established. |
| ore_generation | absent | No renewable ore generator or magical miner found. |
| permanent_power | core | Knowledge of Death unlocks purchased perks, alignment rewards/prerequisites and five persistent Watcher unlocks. See formulas; distinct from MXP. |
| general_generation | limited | Book of Recycling returns recipe ingredients, Scribe copies scrolls, Earthly Garden places/grows plants, graves grow souls. No arbitrary matter duplication. |
| direct_damage | present | Gladiator damage multiplier, weapon enchantments Frostbite/Plague Bringer/Decrepitude/Sanctified/Ruthless Strike, Dust of Frost, magical arrows, Bone Shield and Spectral Bite; mostly modifies external weapons or reactive/special magic. |
| block_breaking | limited | Reach scroll extends interaction; no mining spell or tool progression. Recycling processes an existing item, not block harvest. |
| permanent_pets | present | Prayer of Empathy/Harmonization and Undead/Dissonance bind eligible unowned creatures; Bone Scepter controls sitting undead; Receptacle of Familiar or gemstone restores a dead familiar. Ownership/eligibility matter. |
| temporary_pets | present | Tablet of Guard and Spectral Conjurer summon finite spectral wolves; ancient tablet extends duration. Grave Guardian is a special merchant/protector, not an arbitrary tamed worker. |
| defense | present | Guardian perk, Blessing, Curse of Bones, spectral armor procs, resist scrolls, Unstable Intangibility, projectile reflection, ghostly shape, shadow concealment. No armor tier set. |
| healing_recovery | present | Restoration potion, Purification scroll, Sanctified healing on hit, Voodoo Poppet and Soul Receptacle intercept death, grave recovery; pet revival is separate from player resurrection. |
| debuff_control | present | Plague Bringer, Frostbite, Incurable Wounds, Decrepitude, Dust of Frost; Purification and Mercy, Discretion/Dust of Vanishing and Silent Bond affect targeting. Use correct effect/weapon target. |
| energy | present, typed | Grave Soul discrete weak/strong at decorative grave; consumable Grave Dust, charged items, cooldowns, Knowledge and alignment; Flying Carpet item/entity energy 0–1000. No FE or player mana pool. |
| automation_control | limited | Passive soul generation and periodic effects; undead sit command. No native programmable factory, automated soul extraction or general redstone control demonstrated. |
| agriculture | present, supplemental | Earthly Garden applies plant growth/placement and bonemeal behavior; Bag of Seeds and spring trades, ritual flute restores dead coral. No general automatic crop harvesting/replant network. |
| detection_information | present | Grave Key marker, True Sight, Seeker Rod secret-knowledge search, Lost Tablet structure search and Archaeologist/Treasure Seeker, readable scroll hints. Location detection not resource extraction. |
| equipment_augmentation | core | Book of Soulbound, Repairing, Disenchantment, Recycling, Magic Impregnation, Scribe, Oblivion; 12 registered enchantment definitions. Bone needle binds creature type, not gear upgrades. Native caps/config separate from Apotheosis. |
| world_manipulation | limited | Earthly Garden plant placement, coral revival, grave placement and prayer-related villager conversion; not arbitrary building/terrain editing. |
| world_content | present | Decorative graves, marble variants, abandoned graves, Thorn Veil, Grave Guardian, spectral wolves and Flying Carpet; 7 worldgen JSON records, 55 loot tables, five Watcher stories and seasonal events. No independent dimension demonstrated. |
| death_respawn | core | Player grave inventory/XP, Grave Key, ghostly shape, Soulbound, Preservation, Voodoo Poppet/Receptacle of Soul, Memento Mori, Jailer, recovery modes and finite protection. Separate actual death from intercept and familiar resurrection. |
| trade_economy | present | 63 native Grave Guardian trade records across ordinary/seasonal pools; Grave Dust, scrolls, tablets, gemstones, books, equipment and plant rewards. Gemstone of Merchant improves offers; alignment impacts choices. No independent currency. |
| progression_tiers | core | Four perk branches (18 perks, purchased levels, branch spend gates), nine alignment levels, five four-fragment Watcher unlocks; weak/strong souls and enchanted/ancient variants. No mandatory linear campaign. |

## 4. Typed resources, costs, and limits

- Decorative graves: BlockEntityDecorativeGrave tick computes probability 1−(1−p/100)^n for n elapsed configured intervals. Default interval 10 minutes, p=25, hence 25% per interval (NOT 25/1000 despite misleading config tooltip). Existing soul prevents another generation. SoulType NONE/WEAK/STRONG; generateSoulType has 10% strong probability after success, 30% around Halloween. Strong soul may summon a Grave Guardian (50% branch if enabled/no nearby guardian); default distance 100, restock 60 min. Grave Soul is discrete state, not FE, XP or a numeric mana battery. Use appropriate soul strength; strong-soul interactions need explicit native gate. No claim of unbounded concurrent souls.
- Grave Dust and Essence of Undeath are physical reagents. Impregnated Diamond = 8 dust surrounding diamond; Strange Scroll = 4 wooden rods + 3 paper + 2 dust; Strange Tablet = 2 grave marble + 2 dust. Book of Soulbound = 4 dust, 2 lapis, 2 diamond, book. Receptacle of Familiar = 4 ghast tears + 4 iron + Impregnated Diamond. Voodoo Poppet = 4 dust, 2 clay balls, leather, dead bush, string. Recipe JSON and allowed-item flags govern actual availability; no new balancing.
- Casting item families use charged/enchanted or ancient states and cooldowns, not continuous player mana. SharedConfigTombstone defaults: ordinary scroll duration 12000 ticks (10 min), tablets cooldown 300 seconds, magic scroll level ceiling 4, prayer cooldown 60 min, Book of Oblivion reset cooldown 120 min. Config ranges respectively 1200–120000 ticks, 60–1200 sec, 0–255, 1–60 min, 20–1440 min. Actual scroll/effect/amplifier behavior is item-specific; do not apply one generic formula to every spell.
- Flying Carpet energy clamped 0–1000. ItemFlyingCarpet.inventoryTick adds 1 per 100 entity ticks while below cap; FlyingCarpet entity also has Soul Sand charging/energy usage logic, and item use checks server flight permission. No unlimited creative flight guarantee. Item/component variant 0–3.
- Scroll of Knowledge transfers vanilla XP with loss, returns stored XP on use; no exchange with Knowledge of Death or TMM MXP. Knowledge is persistent integer; alignment is separate signed value; Watcher unlocks are bitset. No FE interoperability identified.

## 5. Knowledge of Death — actual perk graph and numerical handoff

TBPlayerCapabilityHandler.getKnowledgeForLevel(n) = n²+1 for n>0, else 0. getTotalPerkPoints computes integer sqrt(knowledge−1); threshold examples 2, 5, 10, 26, 101, 401 for 1,2,3,5,10,20 points. Perk.getCost(level)=1 for positive level. EntityHelper.hasPerkBranchTierRequirement: tier 1 no prior branch spend; tier 2 requires 5 spent in that branch, tier 3 requires 10. canUpgradePerk also checks parent purchased at least once, config/alignment eligibility, native level max and total unspent points. getPerkLevelWithBonus caps purchased+conditional bonus at getLevelMax and returns 0 for disabled perks. Seasonal/free bonuses do not grant spent points. Native bonus displays below, not universal independent attributes.

| Branch | Tier | Perk | Base max | Per-level displayed bonus or function | Parent/condition |
| --- | ---: | --- | ---: | --- | --- |
| Animathurgy | 1 | Alchemist | 5 | 20% potion duration/level | — |
| Animathurgy | 1 | Resiliomancer | 3 | 30% interruption avoidance/level | — |
| Animathurgy | 1 | Rune Inscriber | 5 | 10% tablet-use preservation/level | — |
| Animathurgy | 2 | Channeler | 3 | 15% casting speed/level | Resiliomancer |
| Animathurgy | 2 | Glyphographer | 5 | 10% scroll duration/level | — |
| Animathurgy | 2 | Scribe | 5 | 2 additional enchantments extracted/level (display) | — |
| Animathurgy | 3 | Priest | 5 | 5% prayer cooldown avoidance/level | Channeler |
| Athanocraty | 1 | Guardian | 5 | 5% less incoming damage/level | — |
| Athanocraty | 1 | Witch Doctor | 5 | 10% poppet preservation/level, capped 100% display | — |
| Athanocraty | 2 | Gladiator | 5 | 5% more outgoing damage/level | Guardian |
| Thanatomancy | 1 | Jailer | 5 − floor(config chance/20) | 20 percentage points per level + configured chance, capped 100%; disabled at −1 or 100 | — |
| Thanatomancy | 1 | Memento Mori | 5 | 20 percentage points XP retained/level, plus 100 − configured loss, capped 100% display | — |
| Thanatomancy | 2 | Necromancer | 5 | 20% controlled-undead spellcaster chance/level (display) | Negative alignment eligibility |
| Arkanotropy | 1 | Shadowalker | 5 | 10% sneaking speed/level | — |
| Arkanotropy | 1 | Archaeologist | 5 | 10% structure discovery chance/level | — |
| Arkanotropy | 2 | Shadowveil | 5 | 10% enemy detection range reduction/level | Shadowalker |
| Arkanotropy | 2 | Treasure Seeker | 5 | 20% special undead loot chance/level (display) | Archaeologist |
| Arkanotropy | 3 | Nightveiler | 1 | Sleep near creatures | Shadowveil |

**Verified actual combat implementation:** EventHandler.onLivingDamagePre multiplies new damage by (1+0.05×effective Gladiator level) when valid player attacker; player victim damage multiplied by (1−0.05×effective Guardian level). Max +25% and 25% reduction, not additive generic attribute percentages; applied to that event's new damage. Frost/lightning resistance divides matching damage by 4 (75% reduction) in same method. Cross-mod event ordering and Monk policy are not established by these native formulas. PerkNecromancer has around-Halloween bonus of 5 but eligibility still matters. Jailer uses actual config chance; disabled −1 or 100; default 0 still eligible in compiled method despite stale localization. Memento Mori disabled when XP-loss config 0; displayed retention = min(100,100−configured loss+20×level). Max 5; default loss 95 gives displayed 5/25/45/65/85/100% at levels 0–5. Do not infer all 18 perks simultaneously maxable under current alignment or runtime settings. Native perk classes inspected; full cross-mod optimized loadout is a different scope.

## 6. Alignment and prayer operations

TBPlayerCapabilityHandler bounds alignment to −5000..5000. Levels: −4 at −5000; −3 at ≤−3000; −2 at ≤−1500; −1 at ≤−500; neutral between −500 and +500 exclusive; +1 at ≥500, +2 ≥1500, +3 ≥3000, +4 ≥5000. Config defaults: freeing receptacle soul +50, plundering player grave −10, exorcism +20, zombify −20, tagged kills ±10, Cupidity −10, protection prayer +20, Earthly Garden +10, killing tamed creature −10. These are configured inputs, not promises all targets qualify.
ItemAnkhOfPrayer.interactLivingEntity selects branch by alignment/target: negative level permits villager zombification; eligible unowned undead at level −2 or lower control, at −3 or lower stronger Dissonance; nonnegative permits exorcism of zombie villagers; eligible unowned non-undead at +2 or above Empathy, +3 or above Harmonization; positive protection prayer for eligible villagers/golems. Undead conversion checks peaceful difficulty and ownership. Target prayer applies 200-tick Prayer state and finishes through native helper/event; cooldown, target validity and interruptions matter. Bone Scepter sets undead servant sitting; no arbitrary hostile mob ownership. Alignment effects and perk eligibility must be taught as choices, not a single mandatory path.

## 7. Death and equipment operational contracts

- Actual player death: key identifies grave; ordinary recovery right-click or sneak-over according to native handler/client settings, optional soul-charge or configured recipe grants teleport. Default SharedConfig player-death grave unlock −1 (disabled), XP loss 95%, Ghostly Shape 120 sec. PlayerDeath defaults no chance or percent loss of items, PvP grave unlock true, stolen XP 30%, loss restriction to abandoned/stackable, protect outside world border true. These are native defaults, not loaded TMM settings. DeathHandler and EventHandler hooks manage Soulbound drops, respawn, clone and actual death. Ghostly Shape targeting protection is not invulnerability to all sources.
- Voodoo Poppet: craft, activate on grave soul, retain active item in inventory for lethal-event interception; Witch Doctor can preserve charge/item. Receptacle of Soul captures a soul and can separately prevent death or release soul. These prevent actual death, not resurrection of committed player death. Native interaction with other lethal interceptors not guaranteed.
- Book of Soulbound is offhand operation at grave soul; ItemBookOfSoulbound.setEnchant requires nonempty mainhand max stack size 1 and rejects already-soulbound target, sets SOULBOUND boolean component true, shrinks book by one and returns success with one soul consumption. Separate from native tombstone:soulbound enchantment JSON (durability tag, max I); don't assume all component-bearing/Curios stacks share the same drop ownership. Native onHandleSoulbound delegates to DeathHandler. TMM Smart Keep and Ars Elemental death/respawn ordering are pack-specific. Book of Repairing requires damaged target, sets DAMAGE 0, consumes book/soul, recalculates REPAIR_COST based on name/enchant count (not universally zero).
- Book of Disenchantment extracts eligible enchantments as books; Book of Recycling returns eligible recipe ingredients, not arbitrary component-preserving item cloning; Book of Magic Impregnation improves eligible enchantable item; Book of Scribe copies scrolls; Book of Oblivion resets perks, not player's full progression or knowledge. Use offhand book/mainhand target and correct soul. Config can disallow damaged recycling, enchanted book extraction and specific items. Native defaults allow damaged recycling, deny disenchanting enchanted books. Record selected operation rather than claiming infinite recycling loops or bypassing TMM exclusions.
- Native 12 enchantment definitions (data/tombstone/enchantment/*.json): Soulbound max I; all other eleven max X. Weapons (mainhand): Decrepitude, Frostbite, Incurable Wounds, Magic Siphon, Plague Bringer, Ruthless Strike, Sanctified. Armor: Blessing (armor), Curse of Bones (chest), Spectral Bite (feet), Spectral Conjurer (head). Effects respectively health reduction over time, freezing/damage, healing reduction, beneficial-effect theft, harmful effect, critical damage, holy damage/healing; beneficial on-hit, bone shield/reflection, area retaliation, temporary wolf. JSON max not guarantee enchanting access/Apotheosis cap or event stacking. Fishing Bad Luck translation is not a thirteenth registered enchantment JSON.
- Familiar restoration: Receptacle of Familiar stores dead familiar's soul, needs vital essence before soul interaction revives; rare Gemstone of Familiar directly attempts last familiar revival. Not general player resurrection. Native ownership and permission conditions need retained; do not promise arbitrary mob taming.

## 8. Scrolls, travel, rituals and world content

Registered ordinary buff scroll paths include Preservation, Unstable Intangibility, Feather Fall, Purification, True Sight, Reach, Lightning/Frost Resistance, Aquatic Life, Mercy; Projectile Reflection localization/effect exists (no ordinary standalone recipe in the 103 records; discover eligible loot/magic-scroll path before assigning acquisition). ModPotions registers Spectral, Earthly Garden, Bait, Frostbite, Darkness, Discretion, Restoration, Weaver Walk, Giant Strength, Little World; brewing/config eligibility differs. EarthlyGardenEffect applies sampled plant placement/growth and NeoForge bonemeal event; not general autonomous crop harvesting. Dust of Vanishing provides escape; Misadventure rod enchanted at soul becomes faster/unbreakable, still fishing not direct ore mining.

Five tablet types: Recall (soul-linked decorative grave, sneak near another to change target), Home (actual respawn), Assistance (engraved player; ancient invites help), Cupidity (risky teleport, ancient safer), Guard (finite spectral wolf). Ancient Recall/Home can transport nearby eligible creatures. Lost Tablet is discovery/delay + soul activation + valid destination; native defaults search other dimensions and modded structures, configurable deny lists; no TMM blanket portal permission inferred. Seeker Rod locates secret knowledge. No Waystones dependency or change to pack travel restrictions.

**Watcher knowledge:** ItemReadableScroll.ReadableScrollType registers five sets, each four parts, and TBPlayerCapabilityHandler stores unlock bits independently of perk points. English in-JAR texts supply player instructions; ItemRitualFlute confirms real block/stance matching and count gate. Teach collecting and reading parts, then performing the corresponding action.
| Collection | Native operation / result | Evidence |
| --- | --- | --- |
| Rite of Silent Bond | Collect four sequential fragments (3% roll per eligible undead kill with qualifying nearby player); crouch at decorative grave with Ritual Flute and play; undead regard player as kin until attacked | LootHelper.handleMobDrops; ItemRitualFlute.trySilentBond/useOn |
| Elyra's Diary | First three fragments via eligible undead kills (3% roll); then kill Wither for fourth and ordinary Nether Star (supplied if missing from drops). Offer that star to decorative grave with strong soul; consumes star and soul, grants unlock and 10 knowledge. Grave prayer effect attempts increase 1→4 | LootHelper.handleMobDrops; BlockDecorativeGrave.useItemOn; PrayerHelper |
| The Coral Chant | First fragment via waterlogged abandoned grave or eligible Misadventure fishing; heal dead coral with Ritual Flute for second. Plant restored coral near naturally living coral (within 3 horizontal/1 vertical blocks of player; healed count must exceed planted count); first qualifying placement gives third, tenth gives fourth. Play flute at naturally living coral to unlock | BlockAbandonedGrave; ItemFishingRodOfMisadventure; ItemRitualFlute; EventHandler.onBlockPlaced |
| Erdos's Fragments | Use loot-marked Lost Scroll of Erdos (Scroll of Knowledge with stored positive XP); awards next fragment and halves stored XP per discovery. Obtain four, play flute at lectern for Remanence XP enhancement | ItemScrollOfKnowledge; ItemRitualFlute.tryRemanence/useOn |
| Nights of Nour | Enter a qualifying desert pyramid for first fragment; eligible Husk kill gives second, subsequent third and fourth each have 20% roll. Fourth grants Sarheed's Lamp (vanilla Soul Lantern with specific tb_quest=nights_of_nour data, NOT arbitrary lantern). Offer to strong grave soul: consumes lamp and soul, unlocks and grants 10 knowledge plus Flying Carpet of random variant | EventHandler.onEnteringSection; LootHelper.handleMobDrops; BlockDecorativeGrave.useItemOn |

Grave Guardian: native trade data has 63 records, including ordinary pools (bones/rotten flesh→dust, grave marble→Lost Tablet, skeleton/wither skull→magic scroll, grave dust→Restoration potion/armor trim, rabbit foot→Seeker Rod, ancient debris→netherite upgrade) plus season-dependent lollipop/seed/Thorn Veil and guardian reward routes. No claim all offers simultaneously available. No native currency change authorized. Gemstone of Merchant applies to merchants; Gemstone of Guardian invokes guardian, Prayer gemstone resets prayer. Seasonal branches optional, not mandatory main chapter.

## 9. Instructional dependency graph (research candidates, not FTB GUIDs)

Every row is a named teachable action and backward-only prerequisite. Optional alternatives should not force player death, season or alignment switch. Use actual native advancements where they express the event (e.g. adventure/use_soulbound, first_knowledge, mastery_1/2, pray_of_protection, revive_familiar, teleport_to_grave); items only where acquisition is the goal. 43 candidates are not 43 mandated quests.

| ID | Prerequisites | Lesson | Observable action / boundary |
| --- | --- | --- | --- |
| T01 | — | Discover grave dust | Obtain actual dust from eligible undead/loot; do not require player death. |
| T02 | T01 | Create grave marble | Craft one native marble; choose decorative style. |
| T03 | T02 | Decorative grave | Place grave and distinguish it from player death grave; engrave a memorial optionally. |
| T04 | T03 | Grave soul | Observe weak or strong soul; use one for a real magical operation. |
| T05 | T01,T03 | Ankh and first prayer | Craft Ankh; hold use at decorative grave, gain knowledge; identify cooldown. |
| T06 | T05 | Knowledge and first perk | Earn first point at 2 knowledge, choose one tier-1 perk in native interface. |
| T07 | T06 | Specialize a branch | Spend five points in one branch, meet parent requirement, purchase a tier-2 perk. |
| T08 | T07 | Mastery and reset | Earn 10/20 points as independent milestones; Book of Oblivion resets allocation with cooldown. |
| T09 | T05 | Alignment | Observe actual positive/negative action and alignment gauge; optional branches, no forced ethics. |
| T10 | T03 | Death grave and key | Explain actual committed death, inventory/XP, location and safe recovery; do not force death for mandatory completion. |
| T11 | T04,T10 | Enchanted Grave Key | Charge at soul or allowed key recipe; optional actual teleport/recovery. |
| T12 | T04 | Strange Scroll and spell | Craft blank then chosen scroll, activate at soul, cast with real effect. |
| T13 | T12 | Travel/survival scrolls | Choose Feather Fall, Aquatic Life, Reach or resistance; observe effect rather than acquire every icon. |
| T14 | T12 | Preservation and Purification | Show status preservation versus cleansing; separate from item retention. |
| T15 | T04 | Voodoo Poppet | Craft, activate at soul; explain inventory-held lethal protection, Witch Doctor preservation. |
| T16 | T04 | Receptacle of Soul | Acquire eligible container, capture/release soul as distinct from familiar; explain finite death protection. |
| T17 | T04 | Soulbound equipment | Book offhand, unstackable item mainhand, grave soul, observe component; don't double-require Ars enchant. |
| T18 | T04 | Scroll of Knowledge | Store actual vanilla XP with stated transfer loss and reclaim; not Knowledge of Death/MXP. |
| T19 | T04 | Magic book repairs | Repair damaged item using offhand book and soul; check damage zero. |
| T20 | T04 | Disenchantment | Extract enchantments to books; Scribe perk affects extraction. |
| T21 | T04 | Recycling | Recover eligible ingredients; respect deny tags/recipe choice and damaged-item settings. |
| T22 | T04 | Magic Impregnation | Apply eligible magic to an enchantable item; teach gear remains external. |
| T23 | T12 | Book of Scribe | Copy a magic scroll with soul and original, distinguish from perk named Scribe. |
| T24 | T04 | Strange Tablet | Craft blank, Home or Recall tablet, enchant with soul. |
| T25 | T24 | Home and Recall | Cast toward valid respawn or memorized grave; change Recall near grave; respect cooldown. |
| T26 | T24 | Assistance and Guard | Name a consenting player or summon guard wolf; optional variants. |
| T27 | T24 | Cupidity and Lost Tablet | Explain risk, discovery delay, structure target and charge before teleport; ancient alternatives optional. |
| T28 | T09 | Positive prayers | Protect eligible villager/golem, exorcise zombie villager, or pursue empathy/harmonization at sufficient alignment. |
| T29 | T09 | Negative prayers | Zombify or bind eligible undead at required negative alignment, respect ownership and difficulty. |
| T30 | T29 | Bone Scepter | Craft and command owned undead servant to sit. |
| T31 | T04 | Familiar restoration | Craft Receptacle, capture dead familiar identity, nourish, use grave soul; rare gemstone alternate. No player resurrection. |
| T32 | T04 | Grave Guardian trade | Find/summon native guardian, perform actual eligible exchange, choose nonseasonal offer. |
| T33 | T01 | Misadventure fishing | Craft rod, soul-enchant, fish for native special loot; no crop/ore farm. |
| T34 | T32 | Earthly Garden | Acquire/consume potion; observe plant growth/placement; optional Thorn Veil and seed routes. |
| T35 | T04 | Watcher scroll fragments | Find/read four parts of a chosen collection; use native text guidance, not arbitrary checklist. |
| T36 | T35 | Silent Bond | All four fragments; crouch and play Ritual Flute at decorative grave; undead cease ordinary aggression unless attacked. |
| T37 | T35 | Elyra's Diary | Three undead fragments, Wither fourth, Nether Star at strong grave soul; enhanced grave prayer. |
| T38 | T35 | Coral Chant | Waterlogged grave/fishing first, heal dead coral second, first and tenth valid plantings third/fourth; play at naturally living coral. |
| T39 | T35 | Remanence | Four fragments via loot-marked XP scrolls; play Ritual Flute at lectern, unlock XP-enhancing rite. |
| T40 | T35 | Nights of Nour | Desert pyramid and three Husk fragments; special Sarheed lamp on strong grave soul unlocks Flying Carpet. |
| T41 | T40 | Flying Carpet | Ride in air/water, monitor charge, recharge over Soul Sand or while item rests; server flight permission. |
| T42 | T22 | Enchantment specializations | Choose meaningful reactive armor or weapon enchant; document native max vs pack-adjusted cap. |
| T43 | T06,T12 | Perk and spell synergy | Select a relevant perk and demonstrate a duration/casting/defense benefit; no mandatory all-perk grind. |

## 10. TMM boundary and classification overlay

Existing Atlas Occultism dossier explicitly assigns committed inventory/XP death recovery to Corail Tombstone under TMM design. Current baseline a98eadfbd625260f8d2c984cd51844ee76f198af has exact Tombstone pin, no dedicated Tombstone FTB chapter file identified. Do not revise death ownership or suppress native tools merely because overlap exists. Newbie/Smart Keep actual-death and Ars Soulbound ordering, Monk hiding and perk stacking, Apotheosis effective max levels, loaded server config, and TMM portal/claims rules belong to their existing integration/QA owners. Native behavior presumed functional; no blanket gameplay retest. Watcher Star and lamp acquisition have been traced to their exact compiled event and interaction paths; no fabricated recipes.

**Separate owner-approved editorial rating overlay (0 absent, 1 some assistance, 2 significant assistance, 3 independent vs vanilla, 4 superior, 5 transformative):** Combat 2, Protection 2, Survival 2, Gathering 1, Industry 1, Construction 0, Transportation 4, Logistics/storage 0, Life/agriculture 2, Death/recovery 5, Advancement 5. Total 24/55; 3 independent, 3 superior, 2 transformative domains. Proposed Substantial Magic, qualitative breadth beyond three independent categories, not a mandated threshold. Rating is not the 29-axis taxonomy and no quest length is derived from it. Corail death, advancement and travel are most distinctive; external gear and normal mining/building/logistics remain necessary.

**Completion status:** full 29-axis static pass and instructional DAG complete, exact release bytes verified; all five Watcher acquisition chains traced; per-effect numerical or effective server-config followup remains outside this completed native capability pass. No game boot, build, runtime acceptance, code changes or full cross-mod stacking test performed. Status: source research ready for review, followup limited to pack-specific integrations and optional numerical details. 
