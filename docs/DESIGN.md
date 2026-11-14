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

## Unified character progression; Ars + Iron's shared mana; Psi stays separate (clarified 2026-11-13)
- **Locked user clarification:** Pufferfish provides **one shared cross-school character skill tree** with compatible caster bonuses. **Ars Nouveau and Iron's Spells deliberately share one mana bar** using Ars 'n Spells. **Psi does not join that pool**: its player Psi and CAD batteries remain Psi-native. Hex Casting media and other specialized schools also remain native; do not merge everything into one global resource.
- Interoperability of spell power and utility bonuses is welcome without converting Psi/Hex media into shared Ars/Iron's mana. Preserve each school's spell design, rituals, research, attunement and resource economy.
- **Ars 'n Spells:** user specifically prefers its native shared-mana behavior. Retain/target **`iss_primary` (Iron's authoritative pool)** as the provisional first configuration, subject to inspection of the actual pinned 3.3.5 config, client behavior, costs and damage-double-stacking. `separate`/`disabled` are **not the preferred design**. No config change was made; verify the actual config before claiming it is set.
- **PsiTweaks balancing** remains **provisional**, including its defaults that disable damage-related Psi loss and casting regen cooldown, Ideaspace Storage, mass mining and time acceleration. Keep enabled mod and existing version; do not silently disable features, impose bans or ship tuning yet.
- **Psi-generated FE is desirable as a candidate power source.** Test `psitweaks:trick_supply_fe` for sustainable FE/t, automation, reliability, throughput, routing to Draconic loads and economy exploits. **If sufficiently robust, consider removing BiggerReactors Rework as a dedicated generator**, subject to a later explicit decision; neither generator nor PsiTweaks feature has been removed or changed.
- Avoid duplicate spell multipliers, double mana deductions, accidentally free cross-casting, unbounded stacking, and skill refunds that leave stale bonuses. Pufferfish infrastructure is installed in B6 but the cross-mod tree is still absent; Roots scripts use temporary tags only.

## Scriptor Magicae — cooldown and powerful lecterns (approved 2026-11-13)
- **Do not add Scriptor Mana Mode.** Keep cooldown casting; investigate Pufferfish personal cooldown buffs and persistent quality at spellbook writing / lectern crafting or placement. No values approved yet.
- **Retain capable automated lecterns**, including ore *mining* (not ore conjuration), furnace-recipe smelting without fuel, relocated mob drops, automated combat and other supported actions. Document actual effects before imposing limits. No arbitrary nerfs or mandatory energy costs.
- Resolve the apparent `LecternTargetable.container` null-inventory source issue by *testing the pinned jar*, not silently patching. See [audit](SCRIPTOR-LECTERN-SKILLS-AUDIT-2026-11-13.md).

## Enigmatic Legacy+ — skills decision (approved 2026-11-14)
- **No dedicated Enigmatic Legacy+ Pufferfish skill branch or custom spellstone bridge.** Preserve native artifact progression, Curios spellstones, their cooldowns and effects. Three native source-verified attributes (`enigmaticlegacyplus:lifesteal`, `enigmaticlegacyplus:projectile_deflect`, `enigmaticlegacyplus:etherium_shield`) may be considered for **universal** Pufferfish skills only after balance testing. A confirmed mod attribute does not mandate a mod-specific skill tree.
- Keep independent death, Soul Crystal, Tombstone and Lost Engine global cooldown interoperability tests. This is a skill decision, **not permission to disable its powerful gear or alter death mechanics**. See [audit](ENIGMATIC-LEGACY-PLUS-1.1.2-SKILLS-DEATH-AUDIT-2026-11-14.md).
- **Feywild:** next initial audit [here](FEYWILD-5.6.4-INITIAL-SKILLS-AUDIT-2026-11-14.md). No Feywild-specific Pufferfish skill branch has been approved or rejected yet; treat custom fey ownership and generic pet buffs as runtime questions.

## Feywild — pet skill integration test order (approved 2026-11-14)
- **Test universal Pufferfish pet skills on Feywild summoning entities first.** Only consider a reusable cross-mod player-owner/UUID adapter if tests prove native generic pet skills fail to recognize Feywild's `IOwnable` entities. No standalone Feywild tree or AI goal buff is authorized. Preserve automation, quests and native progression. [Initial audit](FEYWILD-5.6.4-INITIAL-SKILLS-AUDIT-2026-11-14.md).

## JAWR: Forbidden Magic — initial evaluation (pending user decision)
- The addon has **native 10-level lichdom**. Recommend no duplicate Pufferfish lich track, but retain the full native supernatural progression. **No user approval to close its Pufferfish integration yet**; source/JAR hooks and phylactery event behavior remain **Needs investigation**. [Audit](JAWR-FORBIDDEN-MAGIC-0.5.37-INITIAL-AUDIT-2026-11-14.md).
