# Complete MXP Source Implementation Target List — 42/42 Magic Mods

**Planning only, November 14, 2026.** Compared the [42-mod MXP source inventory](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/design/universal-mxp-progression-registry/docs/MAGIC-MXP-SOURCE-REGISTRY.md) to the 148-entry manifest on the MXP development line. The list below defines **targets for future implementation**. No source hooks, pack configuration, mods, gameplay rules or builds are changed by this document.

**Classification:** R = resource-paid magic; C = successful magical completion (including hybrid systems with no confirmed comparable player reserve); I = inherits parent adapter; B = shared payment bridge; N = no standalone MXP source. **Counts: 16 R + 11 C + 11 I + 1 B + 3 N = 42.** Priorities: P0 = existing prototype needing runtime validation; P1 = next useful adapter; P2 = later/specialized system. This is a proposed work queue, not a schedule.

## Universal award contract

- **Only our MXP dispatcher feeds Tyz's Skills XP.** Native school-specific leveling/research and form progression remain intact; no vanilla XP-to-MXP conversion.
- **Paid magic:** 1,000 base MXP per one fixed, first-access personal or portable resource pool spent. Award uses the accepted action's **nominal pre-efficiency cost**, but only if the action actually succeeds and pays positive resource. Efficiency and regeneration therefore remain desirable. Standard award is then divided by **1.2 to the lifetime earned Tyz levels P**, repriced if an award crosses a level boundary.
- **Ritual:** first successful completion for each player and distinct ritual ID is fixed, **not divided**; later successes award ordinary divided experience. No reward for initiation, failures, unattended output, or mere presence nearby. The ritual must be meaningfully attributable to the player.
- **Boss:** first registered boss defeat per player/type gives **exactly 1,000 effective MXP**; repeat award usually divided. Special fixed-repeat Chaos Guardian **100 effective MXP** is proposed, not enabled pending victory verification.
- **Crafting, new spell, quests:** significant manual achievements may earn divided MXP, **including the first award**. Do not reclassify first-time ordinary crafting as an undivided ritual.
- **Dedup:** one magical action = one MXP transaction, even if a shared resource, parent addon, crafting outcome and summoning event are all observable. Resource transfers, filling reservoirs, factory power, farming vanilla XP/EMC, idle machines, purely decorative spells and free spam are not valid experience faucets.
- Existing source prototypes are **source-compiled, not game-tested**. Existing Ars/Iron/Botania/Neo hooks are in [PR #45](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/45); Psi KubeJS pilot in [PR #40](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/40). Neither is necessarily pinned into a playable pack.

## A. Direct resource-spending sources — 16 mods

| # | Mod | Resource and baseline | Award trigger / exclusions | Priority |
|---:|---|---|---|:---:|
| 1 | **Ars Nouveau** | Ars / Iron's shared mana, B=100 | Successfully paid player spell from native or Ars 'n Spells settlement, based on original glyph cost. No turret, simulation or cross-cast duplicate. **Existing native hook prototype.** | P0 |
| 2 | **Iron's Spells 'n Spellbooks** | Same shared mana, **not a second pool**, B=100 | Confirmed spellbook/sword/channel paid spell, original mana cost; check scrolls, channeled pulses, refunds and ANS shared payment. **Existing prototype.** | P0 |
| 3 | **Mahou Tsukai** | Player Mahou; first accessible max B **TBD** | Successful active paid Mahou spell or directly controlled circle operation. Audit exact 1.36.31 debit path, ongoing circle upkeep vs unattended automation, and pre-efficiency price. | P1 |
| 4 | **Primal Magick** | **Mana stored in wands**; first usable wand capacity B **TBD** | Successfully cast player spell with actual wand mana expenditure; separate manual ritual/craft completion. Do not assume player-wide mana bar or award XP for charging wands and Mana Orbs. | P1 |
| 5 | **Roots Classic** | Terra / personal spell charge and staffs; first capacity B **TBD** | Successful player spell/staff activation with true resource debit. Audit exact Roots Classic **1.5.10** NeoForge resource name, payment method and capacity, not old Forge code. | P1 |
| 6 | **Psi** | Player Psi / CAD batteries; default personal B=5,000 | Successfully executed CAD spell: preSpellCast nominal compiled cost + postSpellCast real personal/CAD Psi debit. **KubeJS pilot written, not game-tested.** Exclude stored Psi transfers, infinite CAD and canceled casts. | P0 |
| 7 | **HexCast NEOFORGED** | **Media**, paid by dust, shards, charged amethyst and casting-item batteries. First practical kit B **TBD** | Successful player Hex with Media actually debited, nominal pattern cost. Count actual actions once across staff, artifacts/cyphers/trinkets, and multiple Media sources. Do not count recharging, autonomous casting or wasted overflow from consuming whole crystals as nominal spell cost. | P1 |
| 10 | **Neo Vitae** | **Anima EV network**, fixed B=**5,000 EV** from Tier 0 Novicius/Weak Orb | Successful positive **network PostSyphon** from player-owned Anima, NOT fluid storage, health substitution or EV addition. **Existing native hook prototype.** Review automatic upkeep vs player practice. | P0 |
| 11 | **Just Another Witchery Remake (JAWR)** | **Three distinct systems:** personal Infusion charge (B **TBD**); paid Symbology (B **TBD**); **vampire blood (B=900)** for Lilith-unlocked JAWR vampires. Do not use 900 for infusion. | Confirmed paid infusion/symbol action, **active vampire blood-spending ability after Lilith's Blood upgrade**, and completed player circle rites. Only actual primary JAWR blood debit counts, not routine hunger or feeding. **Altar power is structure energy, not personal infusion.** | P1 |
| 12 | **EvilCraft** | Portable Blood used by active magic; first usable portable reservoir B **TBD** | Genuine blood debit tied to player item ability/weapon; distinct significant manual Blood crafting if attributable. No XP from collecting, filling, transferring or machine-siphoning blood. | P1 |
| 13 | **Goety** | Personal accessible **Soul Energy**, initial totem/arca B **TBD** | Real Soul Energy spent by an accepted player spell or paid summon; meaningful manual rite completion can separately count. No XP for automatic servant labor, passive Soul Energy generation or autonomous blocks. | P1 |
| 16 | **Forbidden & Arcanus** | Potential player **Aureal** or artifact charges; paid reserve B **TBD** | If resource actually pays for an ability, track the committed cost; otherwise reward successful personal arcane/Hephaestus crafts and rituals. The presence of an Aureal regen attribute is **not proof of spend hook**. | P2 |
| 20 | **Botania Revived** | Portable tablet / standard or greater Mana Ring in inventory, offhand or Curios; B=**500,000** mana | Award only mana actually drawn from eligible carried/equipped items for a magical action or equipment repair; prorate mixed sources. **Existing native prototype.** No pools, mirrors, transfers, storage fills, other mod power or machine output. | P0 |
| 21 | **Pastel** | Portable **Ink** (multiple colors, tools and palettes), first usable Ink reservoir B **TBD** | Accepted player Ink-powered spell/tool with real spend; optional meaningful craft results if manual. Investigate multicolor payment and Curios; avoid separate awards for each color or refilling ink. | P1 |
| 36 | **Embers Re-Ignited** | Portable **Ember Jar, 2,000 Ember** (cartridge larger at 6,000) | **EmberRemoveEvent** after actual player weapon/tool/active augment debit, using pre-reduction nominal cost. Source-confirmed event; exclude machines, item filling, transfer and passive generation. | P1 |
| 38 | **Reliquary Reincarnations** | **Individual artifact fuel/ammunition/reagents**, no confirmed unified mana pool; per-item first spend B or action weight **TBD** | Player genuinely activates fuel-costing magical artifact. Audit one item at a time; if resource normalization isn't sensible, award a smaller divided completed-use milestone. No passive artifact charms or trivial recharge loops. | P2 |

**Resource-audit warning:** simply having a cost property does not make a player activity eligible. Verify the initial source, the original pre-discount price, actual paid units, owner UUID, accepted outcome, and duplication across any shared payment path.

## B. Completed magical work — 11 mods with no comparable general mana source confirmed

| # | Mod | Target successful outcome | Why not a generic mana award | Priority |
|---:|---|---|---|:---:|
| 8 | **Scriptor Magicae** | Manually authored spellbook, meaningful successfully executed **player** spell; possible complexity-weighted milestones | Its base system is **cooldown and spell complexity**, not paid mana. **Automated casting lecterns must not grant MXP.** Do not install Mana Mode. | P1 |
| 9 | **Occultism** | Finished binding/summoning/crafting **ritual**, reward based on tier of its **actual Book of Binding** and canonical recipe identity | A ritual has no equivalent personal mana pool; start/tick/sacrificial bowl input not completion. Mining/crushing servant work is automation. See detailed section below. | P1 |
| 14 | **Malum** | Completed manual spirit work / runework / active paid ritual; optional resource-backed tool if audited | No consistent universal player reserve verified; normal spirit drops/collection or spirit workstations are not XP. | P2 |
| 15 | **Eidolon: Repraised** | Finished chant/altar rite and significant manually completed magic craft | Ceremony progress/ticks are not resource expenditure; attribute the initiating player and true result. | P2 |
| 17 | **Hexerei** | Finished player-owned cauldron potion, meaningful witchcraft craft/binding or paid magic item | Physical kettle/craft operations, no comparable player-wide bar; automated hoppers/cauldrons cannot be farmed for XP. | P2 |
| 18 | **Enchanted: Witchcraft** | **Completed RiteType** circle magic, meaningful Kettle brewing and player poppet binding | World **altar power** is not personal mana. Chalk activation stores initiator UUID; verify actual rite success. No XP from plant recharge, unattended Kettle or passive poppets. | P1 |
| 19 | **Astral Sorcery** | Player-finished altar craft, personal attunement or significant unique research | Starlight is an altar/world resource; keep native Astral perk XP, no XP for passive collector output. | P2 |
| 22 | **Theurgy** | Finished player-initiated transmutation/ritual and significant actual resulting magical craft | Automated alchemy and ore processing do not generate player MXP. True first ritual fixed; ordinary crafts, including first, divided. | P2 |
| 23 | **Totemic** | **Successful ceremony**, with meaningful participating players; registered Baykok first victory is separate | Native ceremonyStartupSuccess event is a **candidate**, but check cancellation and actual final result. No note-by-note XP, passive drums/totems or nearby spectator rewards. | P1 |
| 37 | **Feywild** | Actual native quest completion, consumed **summoning scroll**, manually owned Fey Altar completion if attribution is added; seasonal bosses separate | Fey Altar currently **does not record a crafter UUID**. Do not credit pixie growing/harvesting or autonomous companions; quests and scrolls are safer actor signals. | P2 |
| 39 | **Summoning Rituals** | Finished player-started summoned result/ritual by verified recipe and caster | No general personal mana pool. No XP for placing ingredients, ritual start, automated repeaters or unowned summoned output. | P1 |

**Completion anti-abuse:** completion credit is based on a meaningful actual world effect/result and player attribution, not an easily repeatable start/stop interaction. First ritual is undivided; first plain manual craft/quest is still divided.

## C. Inherited addons and conditional transformation integrations — 11 entries (no duplicate wallet)

| # | Mod | Parent MXP target and check |
|---:|---|---|
| 24 | Ars Additions | Ars paid cast; audit any independently paid item |
| 25 | Ars Controle | Ars paid cast; no extra XP for redirects |
| 26 | Ars Elemancy | Ars paid cast; check unusual cost paths |
| 27 | Ars Elemental | Ars paid cast; no separate augment reward |
| 29 | Ars Ocultas | Either Ars mana cast **or** finished Occultism ritual, depending on action; never both for one effect |
| 30 | Ars Unification | Ars paid cast; audit bypasses only |
| 31 | Extra Botany Revived | Botania portable-spend adapter where the item's mana comes from **inventory/Curios**; audit added portable mana storages |
| 32 | Gaze (Malum addon) | Malum completion or global registered boss victory |
| 33 | Goety Cataclysm | Goety Soul Energy casting and separate registered boss victories; no reward for unattended summoned attacks |
| 34 | Psi: Tweaks and Additions | Paid Psi spell; produced FE is the spell effect, not another XP faucet |
| 40 | **Vampirism — Lilith-upgraded only** | **Inherit the JAWR vampire blood resource, B=900**, for successful active vampire abilities actually paid from the canonical JAWR pool. **Ordinary infection-only Vampirism vampires earn no MXP**. Detect authoritative Lilith/JAWR upgrade state; don't award separately from a mirrored Vampirism blood deduction, routine hunger, filling, or feeding. Native vampire skill/progression remains unchanged. |

## D. Shared resource/payment bridge — 1 entry

| # | Mod | Integration target |
|---:|---|---|
| 28 | **Ars 'n Spells** | Authoritative shared Ars/Iron's payment settlement. **One resource reference, one logical paid spell, one MXP award** even for cross-cast/converted or channeled spells. |

## E. No separate MXP source — 3 entries

| # | Mod | Treatment |
|---:|---|---|
| 35 | JAWR: Forbidden Magic | **Lich** advancement and external tier gates in the dedicated Tyz transformation skill tree; no autonomous magic XP. Genuine JAWR rituals and registered boss victories may earn through separate shared sources. |
| 41 | Werewolves | Native werewolf progression retained and custom Tyz form-tree advancement gating; **no passive or active standalone werewolf MXP source**. Registered boss victories still use the global tracker. |
| 42 | Enigmatic Legacy+ | No distinct magic XP source for free artifact perks; existing unique risks native, registered boss victories global |


## Lilith-upgraded Vampirism — 900-blood MXP bridge (approved target)

**Eligibility gate:** a player who only obtained ordinary Vampirism through infection does **not** earn MXP from Vampirism powers. The **Lilith's Blood upgrade**, which grants unified **JAWR vampirism** via Transformation Compat, unlocks the **vampire blood-spending MXP source**. The active vampire ability may be provided by JAWR *or* regular Vampirism once upgraded; the authoritative spender is the **JAWR blood pool**, not an independent XP category for each vampire mod.

**Canonical base pool:** **900 blood**, as specified for JAWR's initial vampire blood resource. One successful paid active ability costing 9 *nominal* blood yields 10 base MXP before the ordinary 1.2^P divisor; 900 nominal blood spent yields 1,000 base MXP. This is **separate from JAWR's personal Infusion charge**, which retains its own TBD baseline. Never substitute the pool's *current* capacity or mirror/secondary vampire UI meter for the fixed 900-blood reference.

**Award on confirmed activity only:** require an active ability invocation that actually succeeds, a positive **JAWR-primary blood expenditure**, and the player's authoritative Lilith-upgrade state at settlement time. Use the **pre-efficiency nominal blood cost**, not a passive routine blood drain. A spend originating in regular Vampirism, if synchronized to JAWR primary blood, must share **one logical transaction ID** with any JAWR observation so only one award fires.

**Explicitly excluded:** infection-only vampires; vampires feeding to refill blood; hunger or routine JAWR blood maintenance; damage-linked resource loss; converting/mirroring blood between UI pools; attempted/canceled/free abilities; creative/infinite resources; passive vampire buffs; respec/cure; ability cooldown ticks. **No award for merely becoming a vampire or drinking Lilith's Blood**—that unlocks a source and a skill tree, not a free MXP reward.

**Implementation checks (not code in this PR):** identify the exact 0.5.39 JAWR primary blood debit method; identify which 1.21.1 Vampirism ability payments are synchronized by Transformation Compat; verify the one-way Lilith flag persists through relog and that curing disables eligibility; measure nominal versus committed debit, especially with efficiency adjustments. Ensure ordinary Vampirism without JAWR state grants zero. The Vampire **Tyz transformation tree** still uses the shared MXP-earned global skill points and can use external Lilith/advancement gates to unlock tiers.

**Lich/Dragon distinction:** lich and Dragon/Dracolich forms are *not* analogous blood-spending sources. Their main advancement occurs through custom **Lich and Dragon Tyz skill trees**, with vanilla/mod advancement flags and native form milestones serving as external purchase gates. Only a separate, genuine ritual/craft/boss activity may earn MXP; no ongoing transformation XP or passive form-tick rewards.

## Occultism: Book of Binding tier rewards (high-priority implementation target)

**The Dictionary of Spirits is the guide book.** For XP scaling, use the ritual's **Book of Binding** (usually its actual bound activation item), whose four supported spirit tiers are **Foliot, Djinni, Afrit, Marid**. The exact 1.21.1 Occultism branch shows a Golden Sacrificial Bowl keeping **castingPlayerId** and **currentRitualRecipe**, and Ritual.finish(level, pos, bowl, castingPlayer, activationItem) called at actual completion. Prefer a terminal successful ritual outcome, not starting the bowl or watching the duration timer.

| Book/ritual tier | Suggested relative weight | Example first reward — fixed | Example repeat base reward — divided |
|---|---:|---:|---:|
| Foliot (1) | ×1 | 250 MXP | 50 base MXP |
| Djinni (2) | ×2 | 500 MXP | 100 base MXP |
| Afrit (3) | ×4 | 1,000 MXP | 200 base MXP |
| Marid (4) | ×8 | 2,000 MXP | 400 base MXP |

**Only the concept of tier-scaled rewards is a requested target. Values and weights above are illustrative proposals for balancing, NOT approved awards.** Use the completed **recipe ID** as first-time identity. The tier comes from the ritual recipe's required book, not the strongest book the player owns. Some Occultism recipes do **not** use a Book of Binding; classify by verified recipe/spirit/pentacle tier rather than inventing a book tier. Credit actual caster and eligible contributors, not idle bystanders. Summoned crushers/miners running autonomously never award further XP.

Sources:
- [1.21.1 Golden Sacrificial Bowl caster and recipe state](https://github.com/klikli-dev/occultism/blob/version/1.21.1/src/main/java/com/klikli_dev/occultism/common/blockentity/GoldenSacrificialBowlBlockEntity.java)
- [1.21.1 Ritual.finish / Ritual.interrupt](https://github.com/klikli-dev/occultism/blob/version/1.21.1/src/main/java/com/klikli_dev/occultism/common/ritual/Ritual.java)
- [Marid ritual bound-book activation recipe](https://github.com/klikli-dev/occultism/blob/version/1.21.1/src/generated/resources/data/occultism/recipe/ritual/summon_marid_crusher.json)

## Hex Casting: Media is not an infinite or generic mana bar

**Media comes from held/inventory amethyst and batteries in Cyphers, Trinkets and Artifacts.** The [official Hex Book](https://hexcasting.hexxy.media/v/0.11.1-7/1.0.dev21/en_us/) explains that hand casting can consume whole amethyst items while batteries pay more precisely. Record the nominal **pattern Media cost** rather than mistakenly crediting wasted overflow from a larger item. One Hex funded by a mix of batteries and inventory items is one action/award. No XP for recharging items, picking up dust, item storage transfers, automated Hex minds or canceled/creative casting.

The first-access normalized baseline **B is undecided**. A single amethyst dust is a small denomination, **not automatically the meaningful first personal reserve**. Measure a practical novice casting kit and average spell costs in the exact pinned 0.11.1-19 port before choosing the scaling constant.

## Additional pack reconciliation — magic-adjacent, not missing

- **Global bosses, separate from the 42:** Wither, Ender Dragon; Draconic Evolution **Chaos Guardian**; registered Cataclysm, Iron's, Goety/Goety Cataclysm, Totemic **Baykok**, Feywild seasonal and other validated bosses. First genuine per-player victory **1,000 effective MXP**; proposed Chaos repeat 100 fixed. Verify true final scripted victory, entity IDs, co-op meaningful contributions and pet ownership.
- **Draconic Evolution:** the boss tracker is its MXP entry; **FE production, equipment power transfer and energy storage are not generic magical practice**. **L_Ender's Cataclysm** similarly contributes verified bosses, not an invented casting resource.
- **Gateways to Eternity:** possibly count meaningful manually finished gateway challenges as **adventure completion**, after verifying participation; optional scope review, not an approved MXP generator.
- **Apotheosis / Apothic Enchanting, ProjectE, Project Expansion and ordinary enchanting:** no automatic MXP from scalable vanilla XP, EMC, enchanting farms or transmutation. They keep their native systems. Do not create ProjectE pre-Chaos leaks.
- **Dragon Survival:** on the manifest as **desired but not pack-enabled**. Dragon and Dracolich progression primarily uses the **dedicated Tyz Dragon skill tree plus external advancement/milestone gates**, spending the **same global Tyz points**. Dragon form/ability use does **not** independently mint MXP. Global boss MXP remains separate. Custom Monk cultivation likewise uses external skill/progression gates rather than a new currency.
- **Vampirism is conditional, not universally excluded:** only the **Lilith's Blood-upgraded JAWR vampire** may earn MXP from successfully using *active blood-cost abilities*, measured against the **900-blood JAWR primary pool**, even when an ability originated in Vampirism. **Infection-only Vampirism** gives zero MXP. Werewolves, JAWR Forbidden Magic's Liches and Enigmatic Legacy+ have no standalone source; **Liches/Dragons mainly progress in their custom Tyz trees using advancement/tier gates**. Registered bosses and completed qualifying rituals remain independent sources.
- **FTB Quests, KubeJS, Tyz, shops, Improvable Skills:** orchestration or other progression systems, not MXP fountains. A quest that merely detects a completed ritual must not pay its ritual award twice.
- **Not installed or explicitly excluded:** original Botania (we use **Botania Revived**), original Blood Magic (we use **Neo Vitae**), Aspect Alchemy, original Create and other excluded mods. Don't write new adapters for absent mods.
- **Future desired mods:** add a new entry if a genuinely new approved magic gameplay system is installed; a general utility or QoL mod does not automatically qualify.

## Proposed work queue (planning only)

1. **P0: verify existing source prototypes** — Ars/Iron/Ars 'n Spells, Botania inventory+Curios, Neo Vitae **5,000-EV** network and Psi CAD. End-to-end settled cast -> MXP ledger -> Tyz XP, with no duplicate. Confirm correct pinned JARs and disposable-world server/client tests.
2. **P1 resource adapters** — Roots, Hex Casting, Mahou, Primal Magick, Goety, Pastel, EvilCraft, Embers and JAWR, **including the Lilith-unlocked vampire blood-spend adapter at B=900**. Keep ordinary Vampirism out and use the one canonical JAWR blood debit. Choose starting B and source-backed committed debit for other resources.
3. **P1 completion adapters** — Occultism four-tier Books of Binding and completion, Totemic ceremonies, Enchanted Witchcraft rites, Summoning Rituals and meaningful manual Scriptor spell use. No passive lecterns/altars.
4. **P2 specialists** — Malum, Eidolon, Forbidden & Arcanus, Hexerei, Astral Sorcery, Theurgy, Feywild and Reliquary. Audit unusual player-owned resource use before assuming a pool.
5. **Inheritance and boss closeout** — verify all ten addons use the parent payment correctly; finish the boss registry and multiplayer attribution. Reconcile two distinct event families when one magic act also triggers a quest, rite or summon.

**For each source, completion means:** exact pinned MC/mod binary and registry IDs; first accessible B **or** completion-category weight; successful payment/result event; initiator UUID; the first/repeat per-ID policy; cancellation/automation/dupe tests; unit tests; dedicated server boot; client world-entry and confirmed practical MXP/Tyz award. Nothing in this checklist alone is evidence of a passed game test.

## Additional technical source reading

- [Embers 1.21.1-1.5.9 player EmberRemoveEvent](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/design/universal-mxp-progression-registry/docs/EMBERS-SKILLS-AND-ECONOMY-AUDIT-2026-11-13.md)
- [Enchanted Witchcraft 4.2.7 altar and player rite state](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/design/universal-mxp-progression-registry/docs/ENCHANTED-WITCHCRAFT-4.2.7-DEEP-DIVE-2026-11-13.md)
- [Scriptor lecterns, cooldown casting and missing mana](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/design/universal-mxp-progression-registry/docs/SCRIPTOR-LECTERN-SKILLS-AUDIT-2026-11-13.md)
- [Feywild 5.6.4 altar has no crafter UUID](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/design/universal-mxp-progression-registry/docs/FEYWILD-5.6.4-INITIAL-SKILLS-AUDIT-2026-11-14.md)
- [JAWR 0.5.39 Infusion and Symbology audit](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/design/universal-mxp-progression-registry/docs/JAWR-0.5.39-INFUSION-SYMBOLOGY-PUFFERFISH-AUDIT-2026-11-14.md)
- [Primal Magick 6.0.7 change log about wand-stored mana](https://modrinth.com/mod/primal-magick/version/6.0.7)
