# Aquamirae & Nouveau integration — full-strength Depths and class attunement

**Owner-approved, 2026-11-20.** [Tracking issue #404](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/404); [roster #387](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/387). This document is a **source-level design/next-test intake and handoff**, not proof that mods are indexed, an owner-authorized build ran, or live QA passed.

## New owner-approved compatibility addition

The project scope freeze allows these **two** new mods specifically because they provide interoperability between **existing Ars Nouveau** and **approved Aquamirae**. The owner approves the full addon, including its native glyph roster, not just two Depths glyphs. No other new Ars addons are approved by this decision.

| Mod | Version; file | Provider IDs | Install |
| --- | --- | --- | --- |
| **Aquamirae & Nouveau** | **0.1.3**, `aquamirae-nouveau-0.1.3-mc1.21.1-neoforge.jar` | [CurseForge project **1767657**, file **9470029**](https://www.curseforge.com/minecraft/mc-mods/aquamirae-nouveau/files/9470029) | Both client and server |
| **Ars Conflux** (required library) | **0.1.56**, `ars-conflux-0.1.56-mc1.21.1-neoforge.jar` | [CurseForge project **1737308**, file **9505585**](https://www.curseforge.com/minecraft/mc-mods/ars-conflux/files/9505585) | Both client and server |

Dependencies: Ars Nouveau 5.13.5 already in active Packwiz, Aquamirae 7.2.13 and Fragmentum 5.4.0 *source-only candidate pins* in [draft PR #399](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/399), existing GeckoLib 4.9.3. Confirm all **exact JAR requirements** and official SHA-1 by downloading artifacts before committing `mods/*.pw.toml`; **no fabricated hashes**. Shared `index.toml`, `manifest/mods.json`, `manifest/next-qa-target.json` and integration source lock remain exclusive to the designated integration writer. The owner has approved the inclusion attempt, **not a full assembly or server/Prism smoke**.

The addon describes [12 Ars glyphs](https://www.curseforge.com/minecraft/mc-mods/aquamirae-nouveau) on NeoForge, including Fury/Resilience of the Depths buffs for caster/allies with native Mana costs and cooldowns; it consumes Aquamirae ingredients in glyph recipes and uses `aquamirae_nouveau-server.toml` for tuning. First-test policy: retain all, with no preemptive disable/nerf.

## Locked depth profile and transformation targets

1. **Aquamirae difficulty: full strength**, not the newer Balanced/Vanilla weakening profile. Confirm exact config value name against **target 7.2.13 JAR** before authoring config; the public 1.21.1 source branch claims **7.2.7** and lacks the later `difficulty` selector. The earlier 7.2.7 native combat curve is 20% Fury/Resilience effectiveness at 1.0, linearly up to 100% at 2.0, with Resilience also protecting against special critical damage. Keep native tagged creature rules.
2. **Ice Maze biome mobs: MERGE** is the owner-agreed first-test target, rather than native OVERRIDE clearing all other mod/vanilla spawn entries in Deep Cold and Deep Frozen Ocean. Confirm exact 7.2.13 config and resultant spawn table.
3. **Sea Dragon: native Depths Fury V + Resilience V equivalent permanently while Sea Dragon form is active**; no consumable ticking, potion icon required or double modifier accumulation. **Important math:** the publicly inspected attributes are base **1.0**, cap **2.0**; each effect amplifier level adds **+0.1** to its corresponding attribute. Level **V = 1.5 attribute**, *not* the hard cap 2.0. Do not silently grant level X/attribute 2.0. Existing Aquamirae consumables/effects, Ars glyph grants and other modifiers need deterministic refresh/stacking; test transformation start/end, hybrid body states, death-resurrection and relogs.
4. **Monk: aquatic-attunement purchasable branch**, connected to **Water Walking**, **swimming speed** and **water breathing** in the existing Monk mobility progression; explicitly a specialization choice, not automatic protection for every Monk. Both Fury and Resilience can be earned. Exact investment/caps/rank schedule remain for path owner/Progression review; preserve Sea Dragon as innate specialist and distinct from a fully invested Monk. See [AGENTS.md](../../AGENTS.md): executable integration in **`compat/progression`** via the one Progression engine writer; Monk authors own data-only nodes, JSON, quests and descriptions.
5. **Food route retained:** Aquamirae's native Esca, Cooked Spinefish and advanced seafood buffs are available to any player; don't obsolete them with free global attunement. Native buffs/glyphs should work through standard attributes, not a replacement damage equation.

## Player eligibility: technically possible, globally disruptive

[Source `AquamiraeHelper`](https://github.com/ObscuriaLithium/Aquamirae/blob/1.21.1/common/src/main/java/dev/obscuria/aquamirae/AquamiraeHelper.java), [`HookLivingEntity`](https://github.com/ObscuriaLithium/Aquamirae/blob/1.21.1/common/src/main/java/dev/obscuria/aquamirae/integration/hook/HookLivingEntity.java), and [`AquamiraeCombatRules`](https://github.com/ObscuriaLithium/Aquamirae/blob/1.21.1/common/src/main/java/dev/obscuria/aquamirae/common/AquamiraeCombatRules.java) check **entity-type tags**:
- Damage **to** an entity in `#aquamirae:affected_by_depths_fury` is scaled by **attacker's** Fury.
- Damage **from** an entity in `#aquamirae:affected_by_depths_resilience` is affected by **victim's** Resilience armor modifier/special crit susceptibility.
- A datapack entry `minecraft:player` in either tag takes effect **for ALL players at ALL times**, outside the Ice Maze too; a Sea Dragon transformed player remains `minecraft:player` for entity-type tag lookup. Tagging all players does **not** support selectively tagging only transformed Sea Dragons.
- All-player tags would alter PvP **and ordinary mob-vs-player combat globally**. With low Fury, ordinary untagged mobs would deal significantly reduced damage to every player (because the victim is tagged); special Resilience critical-hit logic applies only when the attacker is also tagged (e.g., **player-versus-player** or an Aquamirae-tagged attacker). Overall balance becomes counterintuitive. Spells/projectiles may have unusual source attribution. This is a **possible experimental datapack**, **not owner authorization to deploy globally**.

**Preferred implementation:** first deliver native Fury/Resilience bonuses to Sea Dragon and specialized Monk against *already tagged Aquamirae creatures*, with no player entity-type tag edits. To explore Sea Dragon advantages *against players*, create a separate **scoped attack-context adapter** restricted to intended player-vs-Sea-Dragon encounters or designated arenas; exact PvP policy and implementation require owner discussion. It is not achievable through per-player entity-type tags alone. Implement only with code owner coordination and side-effect matrix tests.

## QA and handoff

- Integrator: exact official hashes, metadata, versioned compatibility; avoid duplicating PR #399 candidate paths, pin two additional mods, reconcile in one coordinated Packwiz/index/manifest build and record per-mod INCLUDED/BLOCKED/OWNER-DEFERRED.
- Source/CI: validate configs against **7.2.13** (new difficulty setting), **0.1.3** addon and **0.1.56** Conflux. Do not rely on stale 7.2.7 source for the final difficulty enum.
- Game QA: full-strength curve against tagged Ice Maze creatures, Sea Dragon Fury+Resilience **1.5** only while species active, no duplicated permanent modifiers or leaked effects, Monk aquatic specialization nodes/prereqs, effect/glyph stacking and spell attribution, other Dragon forms unaffected, seafood still useful.
- Control test: **no global player tags** baseline; then throwaway world with both player tags for comparison (PvP, vanilla mobs, allies/summons, monster boss damage). Do not enable universally just to demonstrate Sea Dragon advantage.
- Scope: any new executable transformation/class logic belongs to **`compat/progression`**; no Java/KubeJS runtime code created by this documentation PR. Only isolated static checks without owner-authorized Integration Assembly, server boot, Prism package or gameplay PASS.

