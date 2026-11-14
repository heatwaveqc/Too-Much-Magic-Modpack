# Design contract (2026-11-13)

## Vision
Magic is the everyday infrastructure, not a cosmetic wrapper around industrial logistics. Independent magic schools must have sustained roles. Resource transport, crafting, storage, navigation and mining should favor occult/magical mechanics. The technology branch is exceptional and late-game.

## Approved systems
Neo Vitae (not original Blood Magic); Occultism; Ars Nouveau; Iron's Spells 'n Spellbooks; Theurgy; Astral Sorcery; Botania; Just Another Witchery Remake; Mahou Tsukai; EvilCraft; Roots Classic. Preserve experimental status where appropriate.
Apotheosis, Artifacts/Relics and supernatural dungeons/bosses fit the power curve subject to item audits. Gateways to Eternity may support renewable combat drops.

## Exceptional technology
Only Bigger Reactors (energy source) and Draconic Evolution (energy use/transfer/high-end gear). Avoid introducing another general energy/logistics stack. Uranium and draconic materials should be restricted to deep exploration and/or Occultism extraction. Do not assume these restrictions are implemented.

## World and loot
Rich exploration via Cataclysm, Stalwart/Starlight themed structures and Deeper and Darker where compatible. Evaluate early exploration density and performance. Review and block game-breaking single-hit artifacts in normal loot, not just crafting.

## QoL / performance
Borrow the *idea* of ATM10's tested performance, mapping, chunk claims and UI infrastructure, not the whole ATM10 mod list or proprietary assets. Candidate libraries/performance mods: ModernFix, FerriteCore, Embeddium, Entity Culling, ImmediatelyFast; candidate QoL: JEI, Jade, AppleSkin, Mouse Tweaks, Just Zoom, FTB Chunks. Verify dependency graph and current 1.21.1 loader builds individually.

## Storage
Occultism storage is central. Magical satchels, named magical bags, and portable storage from approved magic mods are allowed after compatibility and balance review. Generic backpacks and generic storage/logistics are not.

## Reproducibility
Eventually every resource, boss drop and progression item has at least one renewable or reproducible route, with multiple distinctive magical paths where practical. Some paths should be expensive or late-game. Renewable != instantly cheap or farmable without limits.

## Non-goals for first prototype
No implemented progression locks, recipe overhaul, questline, substitutions, balance guarantees, compatibility claims beyond tests actually performed.

## B6 scope and skill systems
- **Ars Nouveau remains a core mod**, not excluded; its approved Ars add-ons remain pinned.
- **Pufferfish's Skills and Attributes** supply a future **custom cross-mod skill tree**. Only the two underlying frameworks are currently present; the tree still needs authoring and functional tests.
- **Improvable Skills 3** is a separate experience-point upgrade system installed on explicit user request. Audit overlap and balance against the custom Pufferfish tree once that is designed.
- New food suite is Pam's HarvestCraft 2 all four modules, Farmer's Delight, Aquaculture 2, Harvest with Ease and Cobweb. **Aspect Alchemy is a current exclusion.**
- **125 mods have passed a server-only boot**, not a client/world/integration certification.

## Unified skill progression, separate magic resources (updated 2026-11-13)
- **Locked user decision:** Pufferfish provides **one shared cross-school character skill tree**, compatible general caster attributes and suitably scoped spell enhancements. **Do not merge the mana/resource pools of different magic systems.** Ars mana, Iron's mana, Psi player reserves/CAD batteries, Hex Casting media and other native resources remain independent.
- Interoperability of compatible spell-power and utility bonuses is welcome **without resource-pool unification**. Preserve system-specific spell design, rituals, research, attunement and power economy.
- **Ars 'n Spells:** its stock `iss_primary` *merges* Ars and Iron's mana and therefore contradicts the newer rule. Candidate test settings: `separate` (distinct pools; cross-casting may charge both) and `disabled` (no cross-pool bridging). **No config change authorized or applied.** Verify exact 3.3.5 behavior, whether crossing casts double-spend and spell-power additive/multiplicative interaction before choosing.
- **PsiTweaks balancing** remains **provisional**, including its defaults that disable damage-related Psi loss and casting regen cooldown, Ideaspace Storage, mass mining and time acceleration. Keep enabled mod and existing version; do not silently disable features, impose bans or ship tuning yet.
- **Psi-generated FE is desirable as a candidate power source.** Test `psitweaks:trick_supply_fe` for sustainable FE/t, automation, reliability, throughput, routing to Draconic loads and economy exploits. **If sufficiently robust, consider removing BiggerReactors Rework as a dedicated generator**, subject to a later explicit decision; neither generator nor PsiTweaks feature has been removed or changed.
- Avoid duplicate spell multipliers, double mana deductions, accidentally free cross-casting, unbounded stacking, and skill refunds that leave stale bonuses. Pufferfish infrastructure is installed in B6 but the cross-mod tree is still absent; Roots scripts use temporary tags only.
