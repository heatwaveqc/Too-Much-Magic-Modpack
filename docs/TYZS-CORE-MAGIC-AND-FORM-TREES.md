# Tyz core magic and five transformation categories — design sketch

**Status:** proposal and integration scope, November 14, 2026. **No skill/category config, Java effects or UI extension has been shipped.** Read [Tyz MXP and external gates](TYZS-MXP-EXTERNAL-GATES.md), [MXP progression](MAGIC-MXP-PROGRESSION.md), and [42-mod MXP sources](MAGIC-MXP-SOURCE-REGISTRY.md).

## Commitments

- **MXP is the only source of Tyz experience**, and all Tyz skill categories use its **one global level / skill-point pool**. Vanilla XP and native Tyz activity-based XP are not sources. Existing MXP normalization, 1.2^P divisor and fixed boss/first-ritual exceptions remain unchanged.
- **Core Magic** is a single universal magic-skills category. A rank affects equivalent mechanisms across Ars, Iron's, Roots, Psi, Neo Vitae, Mahou, Botania's eligible personal tools, and other supported systems through narrow adapters. It should not create separate skill families per mod or alter a mod's core minigame.
- **Five additional categories are reserved:** Vampire, Werewolf, Monk, Dragon, Lich. These buy from the same skill points. Transformation Compat owns valid forms/combination rules; Monk mod owns cultivation stage; innate transformation skills still belong to their native mods.
- Each skill family can reserve up to five independently priced tier-nodes with up to 99 ranks each. **Owning one rank of the previous node satisfies the native Tyz prerequisite**; don't demand rank 99. External progression milestones may additionally gate individual nodes/tier starts. Five tiers is structural headroom, not a commitment to ship all 495 ranks immediately.

## Category IDs and layout reservation

Proposed stable IDs: **tmm_magic_core**, **tmm_vampire**, **tmm_werewolf**, **tmm_monk**, **tmm_dragon**, **tmm_lich**. Icons and localization to be provided later.

| Category | Authoritative eligibility | Design scope |
|---|---|---|
| Core Magic | Always | Universal magical capacity, efficiency, spell effects, etc. |
| Vampire | Transformation Compat unified vampire state | Blood powers, survivability, transformation-only boosts; not separate trees for each vampire source |
| Werewolf | Transformation Compat unified werewolf state | Lycanthropic traits, hunting and form bonuses |
| Monk | Monk mod cultivation-stage state | Cultivation techniques and the high-magnitude Monk scaling curve |
| Dragon | Transformation Compat dragon state | Draconic traits, breath and mobility; no new invalid hybrid permissions |
| Lich | Transformation Compat eligible lich/Archlich state | Unified lich category with appropriate specialized high-tier slots |

Multiple eligible categories may coexist for allowed hybrids, but they **do not change** Transformation Compat's prohibited combinations, Hunter exclusivity, or dual-lich/Archlich rules. One category for Lich even if multiple native lich sources are present.

**Config caveat:** Tyz's custom categories data file is an overriding whole-file definition. Preserve all default categories we keep (abilities/combat/misc/nature) when adding these six, or the defaults disappear. The 1.21.1 GUI displays **four categories per page** and supports page navigation; with all four defaults retained, six additional categories means ten tabs.

### Visibility

Upstream Tyz Category stores only id, displayName, icon and order; CategoryLoader publishes a **global** category map, and the client's SortingTools displays it. A skill's 'visible' flag is static, only hiding zero-rank skills, not a per-player conditional. Thus there is **no verified native per-player hide/reveal** for form tabs. Native filtering of ALL/search is also unrelated to a transformation state.

- **MVP (no new client work):** core and existing base tabs accessible; reserve form category IDs in design, and register form tabs as their content is implemented (or show them all locked with a clear prerequisite tooltip). Server-side purchase gates always enforce eligibility.
- **Optional client extension:** sync a per-player allowed/ever-unlocked category mask, and filter tabs, ALL list and search. Reveal on first eligible transformation or Monk cultivation and keep the category visible after cure, to let users inspect owned ranks. Provide an optional 'show locked categories' preference. Sync on login, state changes, cure, respawn and dimension transitions. The mask is **only presentation**; server gates never trust client visibility.
- Previously bought form skills remain invested after cure. A form-specific **effect** should stop when that state no longer exists, unless explicitly designed as a permanent residual benefit. Don't silently delete purchased ranks.

Upstream evidence: [Category](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/api/model/Category.java), [CategoryLoader](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/impl/server/categories/CategoryLoader.java), [SortingTools](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/impl/client/tools/SortingTools.java).

## Core magic skill families — proposed initial catalog

Tyz CUSTOM/GENERIC node JSON can modify **registered NeoForge player attributes** using per-rank arrays; our shared Magic Compatibility mod should register a small group of stable attributes (provisional names in the table) and make mod-specific adapters consume them. This prevents bespoke **per-rank** code, but not bespoke **per-mod** integration. All modifiers are opt-in only where the target system has the relevant concept. Rank curves, SP prices and caps are intentionally undecided.

| Skill family | Universal meaning and applicability | Proposed shared attribute | Code requirement | Priority |
|---|---|---|---|---|
| **Reservoir** | Increase maximum *eligible personal* magical resource capacity: unified Ars/Iron's, Terra, Psi, Mahou, Neo Vitae, etc.; portable Botania/Pastel storage only where a safe capacity boost exists; **not** world machines | tmm:magic_capacity | Player attribute registration + per-resource max-capacity adapter; portable items sometimes special handling | Launch |
| **Wellspring** | Faster native personal mana/Terra/Psi/Mahou/EV regeneration; **not** mana flowers, network generation, passive world factories | tmm:magic_regen | Read and modify each recovery formula; don't invent regen for systems without it | Launch |
| **Thrift** | Lower actual mana/Ink/Terra/EV cost of eligible player spells and activations; MXP still uses **undiscounted nominal cost** so efficiency helps throughput | tmm:magic_efficiency_rating | Per-payment adapter; discount stacking, minimum paid cost, anti-double-award checks | Launch |
| **Potency** | Boost *appropriately scalable* offensive/healing spell effects across supported magic; not every weapon, machine or ritual effect | tmm:magic_potency | Native spell-power attributes where verified; Java adapters for other spell resolvers | Launch pilot |
| **Quickening** | Faster player casting/activation, not increased autonomous block processing or instant animation exploits | tmm:magic_cast_speed | Per-casting-system speed/time hooks and synchronization | Second wave |
| **Projection** | Longer valid caster-directed magical range, including eligible staff tools; not cross-dimensional or unlimited range | tmm:magic_reach | Spell-context/raycast/tool hooks; distinguish reach from area | Second wave |
| **Persistence** | Longer bounded spell buffs/debuffs/temporary summons, not permanent ritual infrastructure | tmm:magic_duration | Spell/effect-aware duration changes, avoid blanket potion multiplier | Second wave |
| **Conjuration** | Stronger *player-owned summoned creatures* from compatible mods (e.g. Goety, Ars, Feywild, Iron's, Occultism where appropriate) | tmm:conjuration_mastery | Owner attribution, initial stat and live refresh, despawn/pet stacking | Second wave |
| **Warding** | Reduce confirmed magical damage to the player; not generic all-damage immunity | tmm:magic_warding | Shared damage-event handler, source type tags and exceptions | Second wave |

**Deferred or conditional node families:**
- **Expansion (spell area)** is separate from Projection and would need enough area-aware spells to justify the hooks; don't claim every projectile has a radius.
- **Artifice** (improved player-crafted magical items) requires real crafter attribution and safe item/output bonuses; MXP crafting rewards can exist without an Artifice stat. Don't make unattended machinery a skill multiplier.
- **Ritual Mastery** must not cheapen Totemic's ceremonies or core minigames. Ritual completion can award MXP while no ritual buffs exist.
- **Research Speed** or bypassing spell research and native tiers is not a core universal stat.

### Dependency layout

- **Foundation roots:** Reservoir I, Wellspring I, Thrift I, Potency I — general access and player choice.
- **Spellcraft branch:** Quickening I, Projection I, Persistence I — optionally require one rank in an appropriate Foundation root if playtesting warrants it; avoid unrelated forced investments.
- **Specialists:** Conjuration I and Warding I — mostly independent root options to support summoners and defenders.
- **Per-family:** I -> II -> III -> IV -> V. Each subsequent node requires **one owned rank** of its predecessor. Advanced nodes **may also** require external milestones: boss, ritual, researched spell tier, quest, Monk stage, or form-state checks. No specific milestone mapping is approved yet.

Tyz is fundamentally a **category/tab-based skill list with prerequisite checks**, not a custom connected visual node graph. The layout above is progression structure, not a promise of branch-line rendering.

## What needs bespoke code?

| Feature | Plain Tyz config sufficient? | What we build |
|---|---|---|
| Six category IDs, icons/translations, per-rank SP prices, native one-rank prerequisites | Yes | Config and assets, preserving existing categories |
| Modifiers to already registered attributes | Yes | Tyz CUSTOM/GENERIC node JSON and valid attribute IDs |
| One universal set of magic attributes | **No** | Small shared NeoForge registration module |
| Resource capacity / regen / efficiency bonuses across different magic mods | **No** | Narrow per-mod read/write/cost adapters, often reusing our MXP spending hooks |
| Spell potency across different schools | Partly | Native attribute mapping where available; per-mod effect adapters elsewhere |
| Quickening, Projection, Persistence, Conjuration | **No** | Significant per-mod cast/target/status/ownership code |
| Warding against magical damage | **No** | Shared NeoForge damage listener + classification/tag policy |
| Purchase restrictions for Monk tier, a specific form, or achievements | Partly | Server-side Tyz PurchasePre event (KubeJS or small Java bridge); do not trust the tab |
| Dynamically hide/reveal category per player | **No** | Optional client-side filtering + server state sync |
| Invalidate *effects* without deleting rank after cure | **No** | Live form-gated behavior/attribute handling |
| Generate MXP by magic use, ritual, craft, boss | **Already in MXP integration plan** | Existing modular award dispatcher; independent of specific skill family |

**Gate integrity:** Tyz's PurchasePre runs before SP debit but **bulk purchases check it only once**, while direct addSkillLevel/setSkillLevel bypass purchase checks. Put meaningful external gates at node boundaries, test both bypasses, protect prerequisite refunds and return an explicit missing-requirement message. The core attribute bonuses must not bypass existing ProjectE/Chaos Guardian or form/hybrid restrictions.

## Balance notes

- Keep the fixed MXP threshold at 1,000 / global Tyz level and **one SP per level**. Native Tyz XP sources disabled. The MXP award divisor 1.2^P and a rank's independent **SP price** compound: don't blindly combine both with tenfold price escalation across five tiers without simulation.
- **Linear percent-cost discounts** must not become negative or free. Consider diminishing returns / efficiency rating and positive minimum real debit, plus native efficiency stacking tests.
- The original Monk idea of five up-to-99-rank magnitude bands remains available for its specialist tree; avoid applying an extreme Monk scale automatically to every standard magic skill.
- Baseline magic capacities, precise attribute IDs, skill rank values, and tier milestones need confirmation before gameplay implementation.

## Acceptance tests

1. Ten tab IDs retain default categories, pagination, titles/icons; test category reload behavior.
2. Reservoir I/II custom-skill test: one rank I unlocks II; zero does not. Verify registered attribute moves with rank and refunds.
3. Server refuses externally locked form/Monk tiers before SP removal even if the player has XP and prerequisites; logs/feedback explain the reason.
4. Bulk buy, direct admin level set, refund dependency, relog, cure and valid hybrid form combinations cannot bypass form-state gates.
5. Tier I Reservoir/Thrift/Wellspring actually affect two or more distinct magic systems with one Tyz node; shared Ars/Iron mana only changed once and MXP tracks undiscounted price.
6. Optional hidden tabs also filter ALL/search and stay recoverable when transforming/cured; hidden UI is never used as an authorization control.

## Source references

- [Tyz skill JSON fields and modifiers](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/docs/json-tweaks/create_skill.md)
- [Native one-rank prerequisites](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/impl/server/model/Skill.java)
- [Purchase and rank grant paths](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/impl/server/skills/SkillManager.java)
