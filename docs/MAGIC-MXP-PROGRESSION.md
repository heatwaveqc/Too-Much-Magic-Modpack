# Universal Magic Experience (MXP) — progression contract

**Decision date:** 2026-11-14. **Status:** design specification only; not yet implemented or smoke-tested. Minecraft 1.21.1 / NeoForge / Pufferfish's Skills 0.19.2. See [MXP source registry](MAGIC-MXP-SOURCE-REGISTRY.md).

## Goal and fundamentals

One universal Pufferfish **Magic XP (MXP)** category and a single broadly applicable magic skill tree. Native mods retain their distinct energy resources, spells, item power, ritual minigames, research, and progression. Ars Nouveau and Iron's Spells intentionally share one mana resource via Ars 'n Spells, provisionally ISS Primary; Psi, Hex media, Pastel Ink, Terra, etc. remain independent. Vanilla XP does not buy magic points: Apotheosis makes vanilla experience easy to farm.

One MXP level gives one skill point; **each level always requires 1,000 effective MXP**. Let **P** be the number of magic skill points earned from leveling **before** an award, not currently invested nodes or points granted by quests/commands.

- **Ordinary award:** effective MXP = base MXP / (1.2 ^ P).
- Recompute the divisor **mid-award** if one award crosses multiple levels. Process the uncredited base amount piecewise at the applicable current point count.
- Store an authoritative server-side player XP ledger, earned-level count, fractional award accumulator and discovery history; support respec without resetting P or discovery flags. A point reset must not make XP more lucrative.
- Preserve fractional awards before forwarding safe, bounded integer experience amounts to Pufferfish's Skills. Verify the actual 0.19.2 API and fixed-threshold configuration before implementation; its publicly documented experience-add command establishes an integration possibility, not that custom implementation is already running.

## Fixed exceptions — user-approved design

1. **First successful completion of each registered ritual or ceremony, per player:** the **entire first-time reward is fixed and is not divided**. Repeat successful completions receive a lower ordinary, divided MXP award.
2. **First kill of each registered boss identity, per player:** the **entire first-kill reward is fixed and is not divided**. Repeated kills ordinarily receive a divided reward.
3. **Selected high-end boss repeats may grant fixed experience**, without the divisor. The initial **proposed** exception is **Draconic Evolution's Chaos Guardian: 100 MXP on each repeat kill**. This fixed repeat award **replaces**, rather than supplements, the normal divided repeat reward. A verifiable boss ID and endgame combat tests are required before activation.
4. **Magical crafting bonuses are still divided**, including first-time craft bonuses, until an explicit separate decision expands the fixed-achievement category to crafting. Do not assume that all first-time activity is exempt.

First-time identity is defined by player UUID and canonical ritual/boss ID (with an explicit policy for variants). Persist results across deaths, sessions, and skill resets. A cooperative ritual or kill can credit multiple meaningfully participating players; mere proximity or summoning an otherwise unengaged boss is not sufficient.

At P=30, ordinary 200-base MXP yields about 0.84 effective MXP; a first 1,000-MXP ceremony pays 1,000; an approved repeat Chaos Guardian pays 100. Ten such fixed repeat victories can provide a whole point at any P, as long as the skill category can still level.

## Mana and magical-item expenditure

For each independently consumed magic resource **r**, configure a fixed starting reference pool **B(r)**: its actual first-access *personal* capacity or canonical first usable *portable* energy item. The baseline does **not** increase when equipment, skills, or native advancement raise capacity. If no personal/portable resource exists, use ritual/craft completion instead of inventing a player meter.

**Base MXP from an accepted magic use = 1,000 × nominal resource cost / B(r).**

- **Nominal cost** means the accepted spell/action's **pre-efficiency** cost. It is **not** the actual final debited resource after personal efficiency bonuses. This choice intentionally means efficiency gives more magic uses per energy earned or regenerated and therefore faster MXP progression; regeneration likewise raises sustainable MXP production. Do not apply a special MXP penalty to regeneration, efficiency, or increased capacity.
- Award only after a genuine server-authoritative, committed player-attributable action with a positive resource debit. Cost previews, failed activations with no payment, resource transfers, filling or draining storage, passive generator output, simulated kills, offline machinery, and freely spammable zero-cost actions earn **zero**.
- A valid noncombat or utility cast may earn MXP; don't require damaging enemies to make a spell count. Do not impose generic MXP-per-minute caps.
- Shared resources **must be counted only once**. Ars + Iron's mana and their spell addons share one transaction adapter in the configured Ars 'n Spells mode. If multiple genuinely distinct resources are consumed by one action, the adapter must explicitly specify attribution and prevent double-counting.
- Item-mediated examples: personal Botania mana tools/tablets, Pastel Ink items, Embers weapons. **Transfer into an item is not expenditure for MXP**; activating the item may be. Do not credit world machine fuel or passive enchantment/ward drains without separate approval.
- Reference capacities and exact transaction hooks must be established against the pinned 1.21.1 jars; no guessed resource baselines should be used in production.

## Completion rewards

**Rituals:** Award only on confirmed successful player-performed ceremony/ritual completion, never on individual ticks, merely starting, or passive machine output. The first per-ID completion has a fixed discovery reward. Later successful executions earn divided XP. Require a meaningful participation policy and deduplication per actual completion instance.

**Crafts:** Significant, manually initiated magical crafts can earn divided MXP for actual resulting output, including a smaller repeat reward. Unattended station output or persistent ownerless automation does not count. A block crafting system that doesn't record its crafter needs an explicit attribution bridge, not an invented creator UUID.

**Bosses:** Centralize first-time and repeat victory awards across boss mods. Track contributors (including owner-attributed pets/summons where appropriate), server-side defeat event, entity/boss registry identity, special multiphase death behavior and exactly-once award. Relevant candidates: vanilla Wither and Ender Dragon; Draconic Chaos Guardian; Cataclysm bosses; Iron's Spells Dead King / Tyros; Goety / Goety Cataclysm bosses; Totemic Baykok; Feywild seasonal bosses. Verify exact entity IDs rather than guessing.

Illustrative anchors, **not approved amounts**: basic ceremony first 1,000, repeat 200; advanced first 2,000, repeat 400; significant magical craft first 750, repeat 100; ordinary boss first 2,000, repeat 500; endgame boss first 5,000, repeat 1,000. The only special candidate proposed in conversation is 100 **fixed repeat** MXP for Chaos Guardian.

## Safety and balance acceptance tests

- Pufferfish level-up across a single large award; correct divisor change between points; accumulated fractional XP; overflow / underflow behavior; respec and bonus points.
- Resource economy: pre-efficiency cost vs real positive debit, stacking of native and skill efficiency, passive charging, power transfers, failed casts, automated activation, logout, death and multiplayer.
- Ars 'n Spells ISS Primary: one **actual debit and one award** regardless of which school's spell was cast.
- Rituals: failure vs success, repeating effect ticks, first/second completion, co-op participant UUID selection, dimension changes, chunk unload and missing crafter ownership.
- Bosses: first vs repeat per player, variants, multiple participants, pet/minion damage, admin-spawned bosses, respawns, death interception and Chaos Guardian's genuine victory event.
- The Chaos Guardian's pre-existing gate for ProjectE and Project Expansion remains unchanged; awarding MXP may not bypass or unlock forbidden technology.
- No gameplay/compatibility claims without disposable-world tests.

## Evidence / technical reading

- [Pufferfish Skills, commands and API](https://www.curseforge.com/minecraft/mc-mods/puffish-skills)
- [Totemic KubeJS ceremonyStartupSuccess event](https://kubejs.com/wiki/addons/totemic)
- [Embers source-backed EmberRemoveEvent review](EMBERS-SKILLS-AND-ECONOMY-AUDIT-2026-11-13.md)
- [Ars 'n Spells shared-mana modes](https://www.curseforge.com/minecraft/mc-mods/ars-n-spells)
- [Neo Vitae personal Anima, sigils and EV ritual machinery](https://modrinth.com/mod/neovitae)
- Previous mod-specific work: [JAWR](JAWR-0.5.39-INFUSION-SYMBOLOGY-PUFFERFISH-AUDIT-2026-11-14.md), [Scriptor](SCRIPTOR-LECTERN-SKILLS-AUDIT-2026-11-13.md), [Enchanted](ENCHANTED-WITCHCRAFT-4.2.7-DEEP-DIVE-2026-11-13.md), [Feywild](FEYWILD-5.6.4-INITIAL-SKILLS-AUDIT-2026-11-14.md).
