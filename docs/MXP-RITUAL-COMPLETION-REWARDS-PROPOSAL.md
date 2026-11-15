# MXP Ritual & Magical Completion Balance Proposal — All 11 Target Mods

**Planning proposal, 2026-11-14. No game implementation, config values, scripts, XP rules, pack files or build changes in this revision.** Target pack versions: Scriptor 2.0.14; Occultism 1.227.0; Malum 1.8.2; Eidolon: Repraised 0.5.0.8; Hexerei 0.5.0.3; Enchanted: Witchcraft 4.2.7; Astral Sorcery 2.0.3.59; Theurgy 1.79.0; Totemic 0.12.18; Feywild 5.6.4; Summoning Rituals 3.14.2.

## Reward semantics (critical)

- **F = true ritual/ceremony/attunement**: the player's **first personally completed** unique registered rite pays the stated **fixed effective MXP**, **unaffected** by 1.2^P. Repeated meaningful completions pay a **base** amount divided by 1.2^P. No pay for beginning, ticking, canceling, output retrieval, or being merely nearby.
- **C = craft / learned working / quest**: **both first and repeat amounts are BASE MXP**, divided by 1.2^P, **even on the first**. A "first" craft is an ordinary award under the locked MXP contract, never a first-ritual loophole.
- **B = boss**: global registered boss system pays exactly **1,000 fixed effective MXP on first defeat**; not an extra bonus from a parent magic mod's quest. Fixed proposed Chaos Guardian repeat 100 remains separately gated.
- **Every repeated value below is pre-divisor BASE MXP**; none of these rows creates a new fixed-repeat exception.
- Suggested rough ratio for genuine repeatable rituals is **20% of the first reward as repeat base**. Expensive rites may use up to 25%. Very cheap 2-second "rituals" and unattended stations do not qualify merely by API type.
- One canonical activity ID per result/effect family; distinguish truly higher-tier outcomes, but no first-award duplicates from cosmetic variants, re-binding identical books, spell-name changes, output NBT perturbations, number of batch items, or multiple output events.
- Cooperative participants should satisfy actual performance/contribution requirements, not proximity. Canonical recipe ID + tier/family plus player UUID for first completion; a single server-side transaction ID per execution for dedup.

## Proposed values (MXP)

**F columns are first effective / repeat BASE. C columns are first BASE / repeat BASE. A range is an implementation tier matrix, not random reward rolls.**

| # | Mod | Genuine ritual / attunement (F) | Nonritual craft, manual magical work, or quest (C) | Mechanic and balance note |
|---:|---|---|---|---|
| 8 | **Scriptor Magicae** | **None by default** | **250–1,000 / 0** per genuinely new *spellcraft complexity milestone*, not every trivially altered spell hash | Cooldown-based player spells are free/repeatable; no XP for automated casting lecterns, arbitrary repeated manual spell casts or re-copying equivalent books |
| 9 | **Occultism** | **Foliot 1,000/200; Djinni 1,750/350; Afrit 3,000/600; Marid 5,000/1,000** per genuinely distinct completed binding/summoning ritual | Mundane manual ritual-item crafts **200–600 / 40–120** BASE only if separately attributable; do not double-pay a completed ritual and output craft | Determine tier from actual bound Book of Binding activation item, not guide/strongest carried book; non-book recipes by verified spirit/pentacle category |
| 14 | **Malum** | **No routine fixed ritual by default**; if a true player-owned ceremonial rite is established, **1,500–3,000 / 300–600** | Spirit Infusion / Runeworking **250–700 / 50–140**; difficult Soul Binding **800–1,500 / 160–300** | Malum has very many spirit-workbench recipes; treating each station recipe as undivided fixed ritual would flood Tyz SP. Unattended processing earns nothing |
| 15 | **Eidolon: Repraised** | Brazier rites: **1,000 / 200** basic; **2,000 / 400** advanced; **3,500 / 700** major summoning/ward | Crucible/Worktable magical crafts **250–750 / 50–150** | Genuine completed brazier outcome, not each offering, prayer, altar update, chant tick or minor soul-charge transfer |
| 17 | **Hexerei** | **None currently verified**; only if actual ritual content ships, later consider **1,000–3,000 / 200–600** | Successful manually attributable Cauldron brew **200–450 / 40–90**; rare complicated brew **600–900 / 120–180** | Current mod description lists spells/rituals as future content; don't invent a ritual completion API. Auto-processed cauldrons earn 0 |
| 18 | **Enchanted: Witchcraft** | Chalk rites: **1,000 / 200** introductory; **2,000 / 400** major; **4,000 / 800** exceptional world-scale or costly rites | Kettle potion **250–500 / 50–100**; meaningful player-bound poppet **350–750 / 70–150** | Ring geometry, ingredients, altar world-power demand and conditions justify ritual tier; world altar power is *not* character mana. Canonicalize ~50 stock rite variants by outcome family to avoid duplicate first bonus for trivial targeting variants |
| 19 | **Astral Sorcery 2.0** | **First personal constellation attunement 3,000 fixed**; *reattuning/respec gives 0*, not another "first" reward | Altar crafts by tier **350 / 75** (Resonating), **700 / 140** (Luminous), **1,200 / 240** (Iridescent); significant discovered constellation/research milestones **400–800 / 0** | Modern 1.21.1 removes older ritual pedestal system. Do not pay first ritual bonuses on ordinary altar crafts or repeatedly retune for more awards; no XP for passive starlight/Lumen generation |
| 22 | **Theurgy** | **No blanket fixed ritual** for cauldron/vat alchemy. If a distinct high-effort player-performed ritual exists, **1,500–3,000 / 300–600** after proof | Significant hand-performed transmutation **250–650 / 50–130**; rare/exalted transformation or artifact **900–1,500 / 180–300** | Reusable processing vats, sulfur/salt/mercury stations, high-volume ore conversion and automation are not per-player rituals. Count only attributable successful result, not every step |
| 23 | **Totemic** | Ceremonies: **1,200 / 240** introductory; **2,200 / 440** advanced; **3,500 / 700** especially difficult / multi-instrument | Significant one-off personally performed craft **200–500 / 40–100** when distinct and attributable | Reward *completed* ceremony with actual musician participation; no points per note, aura tick, instrument loop or spectator. Baykok boss handled globally at first 1,000 |
| 37 | **Feywild** | **None by default**; no universal ritual-casting bar | Native seasonal quest milestone **400–1,000 / 0**, seasonal storyline finale **1,500–2,000 / 0**; first meaningful owner-attributed scroll summon **200–350 / 0**; manual Fey Altar craft **300–600 / 60–120** only if crafter identity is actually available | Quests are one-time and **divided**, not fixed; the Fey Altar currently lacks crafter UUID. No passive Pixie farms/companion actions; seasonal boss victory global, no double bonus |
| 39 | **Summoning Rituals** | **0 until the pack adds real recipes**. For future approved, manually performed pack recipes: **1,000 / 200** basic, **2,000 / 400** major, **4,000 / 800** extraordinary | Generally **none** separately: the registered finished ritual already owns the output and MXP transaction | The mod provides a customizable recipe/altar framework but **no recipes by default**. Its *default completion duration is 40 ticks (2 seconds)*, so an inexpensive default-time summoning is not automatically a 1,000-MXP achievement. Assign tiers from expensive ingredients, required construction, boss/advancement gates and meaningful manual effort |

### Recommended values for the standout systems

**Occultism** is especially suited to a four-band reward schedule. Bound book / ritual identity (not the Dictionary of Spirits) determines payout:

| Book | First successful completion, FIXED | Repeat completion, BASE/divided |
|---|---:|---:|
| Foliot | 1,000 | 200 |
| Djinni | 1,750 | 350 |
| Afrit | 3,000 | 600 |
| Marid | 5,000 | 1,000 |

For many cheap similar recipes with the same practical effect, apply a *canonical ritual family* rather than paying a distinct fixed discovery for minor parameter variants. Distinct real rituals do count separately, including learned advanced summons.

**Totemic:** native ceremony requirements (instrument performance, musical timing and actual completion) justify larger flat first payouts than a simple item craft. The proposed three-tier 1,200 / 2,200 / 3,500 band is a starting assumption; map the exact Totemic 0.12.18 ceremony registry/required music difficulty before approving individual assignments.

**Enchanted:** circle rites can consume a few thousand *altar* power, require chalk geometry, ingredients and timing. E.g. an introductory attuned-stone charge can use 1,000; complex curses/summons can use 2,000; rare large environmental rites up to 4,000. Ritual's *initial altar power* is a tier signal, but not a universal personal pool and should not independently generate resource-spend XP.

**Astral:** actual *player attunement* is a major first milestone worth a fixed 3,000. Modern Tier 2/3/4 crafting is meaningful but still C / divided; prevents producing many fixed points by replicating ordinary outputs. The game has native perk XP; MXP must not double-pay ordinary native perk activity.

## Expected effect of the 1.2^P divisor

At P=0, a repeated 400-BASE ritual pays 400 effective MXP. At P=10 it pays ~65. At P=20 it pays ~10.4, and at **P=30 only ~1.69 effective MXP**. A repeat 1,000-BASE Marid ritual at P=30 pays ~4.21 effective MXP. This is a *consequence of the already-approved ordinary MXP curve*, not a special penalty on rituals.

**Balance judgment:** high first-time rewards make exploration of many genuinely different rites meaningful throughout the game, while repeated easy ritual farming is self-limiting. If repeated rituals are supposed to offer worthwhile late-game progression, **the existing 1.2^P formula cannot deliver that without either huge early-game repeat amounts or a new, explicitly approved fixed/minimum ritual-repeat exception**. Do not silently introduce the exception from this planning document.

**First-time counts and quest safety:** even 40 different first 1,000-MXP rites grant 40 lifetime skill points, regardless of P. The full set of Occultism rites and Enchanted variant aliases need an audit before locking per-recipe rewards. Avoid paying separately for cheap cosmetic changes, re-bindings of an equivalent summoning ritual, or crafting a ritual result; those multiply the fixed reward unexpectedly.

## Promotion / implementation policy

1. Inventory **actual ritual recipe identities** and classify each as F, C or ineligible (zero). A mod's name or block label alone doesn't make its recipe a true player ritual.
2. Choose per-recipe tier by ingredients, player actions, difficulty, conditions and meaningful result. *Duration alone* does not indicate work: unattended 10-minute brewing receives no player XP, while an active musical ceremony does.
3. Record actor UUID, canonical activity ID, first/repeat history, successful outcome and transaction UUID. Credit co-op only to genuine participants, never everyone close to the altar.
4. Link any advancement/quest detection for this same ritual to its **existing reward**, not an additional MXP award. Use current MXP-only Tyz global wallet.
5. Test first/repeat, canceled, failed, rerun, recipe change, automated operation, shared-owner/offline edge, relog and XP divisor behavior. **No implementation is authorized by these illustrative amounts.**

## Sources reviewed

- [42-target pack MXP matrix](MXP-ALL-MAGIC-SOURCE-IMPLEMENTATION-TARGETS.md)
- [Occultism 1.21.1 ritual completion and caster state](https://github.com/klikli-dev/occultism/blob/version/1.21.1/src/main/java/com/klikli_dev/occultism/common/ritual/Ritual.java), [ritual bowl recipe and caster](https://github.com/klikli-dev/occultism/blob/version/1.21.1/src/main/java/com/klikli_dev/occultism/common/blockentity/GoldenSacrificialBowlBlockEntity.java)
- [Enchanted 4.2.7 rite research](ENCHANTED-WITCHCRAFT-4.2.7-DEEP-DIVE-2026-11-13.md)
- [Scriptor 2.0.14 manual vs automated spell research](SCRIPTOR-LECTERN-SKILLS-AUDIT-2026-11-13.md)
- [Feywild 5.6.4 quest/scroll/altar ownership analysis](FEYWILD-5.6.4-INITIAL-SKILLS-AUDIT-2026-11-14.md)
- [Summoning Rituals official mod page: no built-in recipes](https://modrinth.com/mod/summoningrituals), [recipe docs: default 40-tick duration and custom outputs](https://docs.almostreliable.com/summoningrituals/recipe/basics.html)
- [Hexerei official mod status](https://modrinth.com/mod/hexerei)
- [Astral Sorcery 2.0 modern tier/attunement overview](https://www.minecraft-guides.com/mod/astral-sorcery/), [modern 1.21.1 research text](https://github.com/HellFirePvP/AstralSorcery/blob/1.21.1/src/main/resources/assets/astralsorcery/lang/en_us.json)
- [Eidolon Repraised 1.21 rituals, prayers and datapackable recipes](https://www.curseforge.com/minecraft/mc-mods/eidolon-repraised)
- [Malum 1.21.1 numerous spirit-recipe families](https://mc-wiki.net/malum/malum-spirits-and-runes-guide/)
- [Theurgy 1.21.1 vat/alchemy and transmutation overview](https://forgebase.johnhalls.com/mods/theurgy)
