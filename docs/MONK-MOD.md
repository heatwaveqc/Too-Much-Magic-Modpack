> **Backend decision (2026-11-14):** native Monk faction unlocks
> one-time abilities and licenses; **Tyz's Skills 6.4.2** owns leveled
> purchases as successive 99-rank tiers (10× per-rank bonus and price).
> **Pufferfish** stays for nonoverlapping one-time nodes; **Improvable
> Skills** stays installed as optional unrelated gameplay, but does
> **not** charge Monk tier purchases using vanilla XP. This changes
> earlier deferred-backend proposals below. Tyz is approved but not
> yet packwiz-installed. See
> [current tier plan](MONK-TYZS-TIERED-ADVANCEMENT.md).

# Monk addon — charter and technical plan (2026-11-14)

**Owner-approved direction:** a new standalone addon module
`compat/monk/` following Werewolves' Vampirism-faction structure, **not**
merely a Pufferfish Skills tree. It may use Pufferfish as the leveled
upgrade presentation/investment backend. Pinned game: Minecraft 1.21.1; NeoForge 21.1.265;
Vampirism 1.10.13; Werewolves 2.0.3.4.

## Design contract

1. Monk is a playable **Vampirism-style faction**, not a transformation.
2. Ascetic tradeoff: give up **regular weapons and armor**. Magical implements,
   decorative wearables, shields and exception cases need precise item-tag
   policies. Do not delete items; later power logic should detect eligibility
   and temporarily suspend dependent abilities if necessary.
3. Monks forgo supernatural transformation advancement, including dragon and
   lich paths, and also may not coexist with vampire, werewolf, archlich,
   dracolich or abomination. The Vampirism faction slot automatically prevents
   monk/vampire/werewolf/hunter simultaneous membership *within that API*;
   external mods require transition interception by the bridge.
4. Compensations: **more generous skill-tree development**, **increased
   experience gain**, and **exceptional magic proficiency** across the pack's
   magic traditions. The exact multipliers, caps and magic attributes need
   playtesting and owner approval; do not add an unrelated mana pool.
5. Initiation rituals, rank advancement challenges and optional Master tree
   imitate Vampirism's progression loop. The old provisional 14-level ceiling is replaced with a configurable
   faction maximum (startup default 20, independently of any long-term
   attribute ranking); the historical 20-level story is only a default.
6. Data-driven skill tree: JSON under `data/tmm_monk/vampirism/` for
   `skill_tree`, `skill_node`, `configured_skill_tree`, later `tasks`.
   Java registers faction, player attachment, skills and actions. JSON cannot
   directly implement unarmed physics, projectile interception or casting bonuses.
7. Intended four branches: Martial Discipline, Body & Mobility, Contemplation,
   Arcane Mastery. These are placeholders pending design; **not implemented**.

## State of implementation

Scaffold only. One inert root skill and JSON tree; no functioning
initiation or attribute/progression bonuses, no mechanics enforcement.
Unit-tested pure policy is neither a player-state observer nor a protection
hook. Avoid using any new custom faction on meaningful player saves until
the world loading and removal pathways are validated.

## Next engineering gates

1. Build against exact pinned Vampirism artifacts; confirm API compatibility
   and JSON resource parsing. Run combined server boot, client world and UI.
2. Confirm faction registration with console/commands and MonkPlayer attached
   data across logout, respawn and dimension travel.
3. Implement **one initiation ritual** that verifies both faction exclusivity
   and bridge-resolved external physiology *before* changing membership.
4. Implement one ordinary advancement challenge via JSON tasks and one
   custom skill end-to-end. Test points, availability and reset.
5. Define item tag groups for conventional weapons, armor and exemptions;
   enforce per-ability eligibility, with no inventory deletion.
6. Research the game's experience gain callbacks and magic mod API hooks.
   Pick bounded, measurable XP and magic improvements before coding.
7. Advance other branches and the Master track once milestone tests pass.

## Sources and license

- Vampirism 1.10.x API: https://wiki.vampirism.dev/docs/1.10%20NeoForge/api/intro
- Werewolves 1.21: https://github.com/TeamLapen/Werewolves/tree/1.21
- Original Monk mod: https://github.com/rwtema/Monk-Mod
- Upstream code may be studied for implementation, but must not be copied
  without checking original code and asset licenses.
- No third-party JARs or assets are committed to this repository.

## 2026-11-14 progression direction and original research

The owner favors **watching the actual setting sun** as the first Monk
initiation, instead of a crafting ritual. Thereafter advancement should be
**mostly earned from block breaking, defeating enemies and eating distinct
foods**, with **only a few mandatory milestones**. Study the original Monk
mod's entire deed/power roster as a menu of optional feats, tree skills and
capstone inspirations—not as twenty compulsory quest gates.

See [MONK-ORIGINAL-AUDIT.md](MONK-ORIGINAL-AUDIT.md) for the source-based
0–20 audit and proposed event architecture. That design is not gameplay code.
The existing `compat/monk/` scaffold still has **no** initiation, advancement
XP or powers wired into NeoForge events.

## Gear-free endgame progression: owner direction

**Never conflate original Monk 1.4 settings with the Too Much Magic Monk.**
The original is a historical reference; the TMM configuration is a separate,
substantially stronger faction with 98 selected techniques from 99 pinned Apotheosis definitions
(Unbound deliberately excluded)
listed as potential unlocks, and repeatable (not arbitrary-capped) investment
in every applicable player attribute. Curios are allowed, including Draconic
Evolution chestpiece functionality that is proven to work from its Curio slot.
Its balance target is an Abomination, Archlich or Dracolich in the absolute
endgame enchanted, affixed Gem Armor and weapons. The complete agreement,
limitations, registry inventories and test plan are in
[MONK-GEAR-PARITY.md](MONK-GEAR-PARITY.md). No gear/affix/attribute behavior
is wired into live code yet.

## New proposal: Apotheosis-infused Monk food (2026-11-14)

Craft **one edible food** with **one Apotheosis rarity material** to produce
one preserved food item carrying a one-use rarity infusion. On **successful
consumption by a Monk**, grant a fixed configurable amount of **Monastic
Mastery XP based only on rarity**. No XP for merely smithing the food.
This is repeatable by consuming more rarity materials; it does not reset the
one-time unique-food bonus, and re-infusing a marked item is forbidden.

Apotheosis 8.9.0 rarity inventory:

| Rarity | Material item ID | XP |
| --- | --- | --- |
| Common | `apotheosis:mysterious_scrap_metal` | TBD |
| Uncommon | `apotheosis:timeworn_fabric` | TBD |
| Rare | `apotheosis:luminous_crystal_shard` | TBD |
| Epic | `apotheosis:arcane_sands` | TBD |
| Mythic | `apotheosis:godforged_pearl` | TBD |

**Smithing table is favored over the anvil**: a dedicated custom smithing
recipe could consume one food and the material without the anvil's repair/
rename/XP rules. A template ingredient and special handling for arbitrary
edible items need review, and the resulting food must retain nutrition,
buffs and other components while gaining a single persistent marker. Build
as server-authoritative completed-consumption reward, not item use attempts.

Refer to `compat/monk/profiles/rarity-food-infusion.json` for the full
five-rarity data plan and test cases. **No crafting or XP code exists yet.**

## Pack-wide ceilings are independent

See [GLOBAL-PROGRESSION-CAPS.md](GLOBAL-PROGRESSION-CAPS.md): use each mod's
actual maximum supported character progression rather than imposing
artificial TMM lower caps; leave mod-specific resource caps (including
Mahou) to a central config audit. Capped versus scalable Monk upgrade nodes
must be classified individually. Decision between Pufferfish as leveled
backend and a custom repeatable investment layer is deliberately **deferred
until after full player attribute / affix registry inventory**.

## Latest owner revisions: classic ability parity and harvesting

Preserve **all 25 registered abilities** from original Monk, not only
its default-rank skill list: `original-monk-abilities-1.4.json`
maps numeric counterparts, custom behavior and the two historical-only
cases. Original imposed Blindness remains an ordeal; original Creative
Flight is explicitly removed from the TMM tree. A permitted Draconic
Evolution Curio flight module can supply it instead; Monk has only
post-Dragon innate momentum gliding.

Barehand harvest **tier** is a finite capability unlock series,
bounded by the highest *actual* tool requirement in the final pack.
Barehand harvest **speed** is a separate repeatable trait at growing
cost. Existing Dragon Survival mechanics have distinct strength and
speed bonuses; use that architecture without requiring a dragon
transformation. See [MONK-WORLD-TIERS.md](MONK-WORLD-TIERS.md).

## Backend-agnostic Monk skill catalog (v1)

[Full catalog](MONK-SKILL-CATALOG.md): **172 core/mod-specific** skills (mapped to all 144 attribute candidates; three deliberately excluded) plus **98 selected Apotheosis** affix techniques (Unbound excluded). Every registered original Monk ability is accounted for. These remain design candidates and are not live gameplay. Allowable-level semantics are proposed (repeatable, bounded, single, staged, source rarity-limited) before choosing the Vampirism/Pufferfish/custom backend or assigning tiers.
## Two-layer design smoke test — owner proposal (2026-11-14)

Every single-unlock skill (including Step Assist and Momentum Gliding)
is a native Monk faction skill. For **leveled** skills, Monk faction
purchase licenses the trait; the **Pufferfish unified stat tree** handles
purchased ranks, including generic traits shared with non-Monks, and
possible Monk-only expansions. The shared skill must have one modifier
owner and no duplicate spend or effect.

**Not yet proven:** Pufferfish's data-driven nodes persist an unlocked
boolean ID, not a numerical purchased rank. Its category lock/unlock
and skill unlock commands can bootstrap a gate but don't prove
repeatable purchases or a complete per-node eligibility enforcement.
Test a faction-permitted category or unlock-only branch sentinel first;
use custom repeatable rank storage/UI if necessary. No actual
Pufferfish gating or modified skill tree has been implemented.
See [MONK-TWO-LAYER-SMOKE-TEST.md](MONK-TWO-LAYER-SMOKE-TEST.md).

## Ranked-skill backend research (2026-11-14)

[Detailed comparison](MONK-SKILL-BACKEND-EVALUATION.md): Improvable Skills 3,
**already installed**, is now the preferred **first source-backed prototype**
for purchased numeric ranks. Its API supports addon registered skills and
an overridable `canUpgrade` faction gate; its regular upgrade packet calls
that method server-side. Limits: player ranks persisted as Java `short`
(normal maximum 32766), vanilla XP `int` price, separate skills-book UI,
and skill scrolls can bypass gating if enabled. Do **not** enable scrolls
for Monk-gated skills. Pufferfish has levelable categories but stores only
boolean skill-node IDs. Tyz's Skills has a clean cancellable purchase API
but a hard 99-rank cap. **No backend selection or mod addition is locked**, and
no gameplay proof exists.
