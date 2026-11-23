# Chaos Bunny — revised combat trial and reactive effects (2026-11-22)

**Authority:** [#549](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/549), owner follow-up after [#546](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/546). This is a **development/QA contract** for a source-only experiment in draft PR #556. Nothing here establishes in-world behavior or a released survival encounter. Preserve the existing 5+10+20+40+80+1 Gateway structure, rabbit identity, no mob loot, completion-only trophy, and deliberately absent Chaos Pearl recipe.

## Verified publisher evidence

The exact **official** Gateways 1.21.1-5.1.0 and Apotheosis 1.21.1-8.9.0 JARs are SHA-512 verified and ZIP/CRC checked by `scripts/probe_chaos_bunny_pinned_jars.py` on an ephemeral GitHub runner. The pinned Apotheosis JAR contains `data/apotheosis/gateways/tiered/pinnacle.json`, whose Gateway uses a native Killer Bunny (`RabbitType:99`) and Apotheosis affix/attribute modifiers, plus the actual compiled `EliteWaveEntity`, `AffixWaveModifier`, `Elite`, `BossStats`, `AffixData` classes. Class presence is not in-game initialization.

**Best native route:** replace *only* the last Gateway `entities` record with an `apotheosis:elite` wrapper around the existing `gateways:standard` rabbit, referencing a TMM-specific `apothic_elites` datapack definition with randomized intrinsic attribute ranges. The existing `apotheosis:affix` modifier alone applies **item affixes** and is insufficient for an unarmed rabbit. The `EliteWaveEntity` sets the target elite marker on the base mob, and `ApothMobEvents.delayedEliteMobs` applies the specific `Elite` on server entity join; `Elite.initElite` applies random attribute modifiers and effects. **This architecture is researched and verified in upstream source and pinned JAR class inventory but is NOT YET implemented or gameplay validated.** Avoid God-tier rarity assertions until exact 8.9 data matches them, and disable normal Elite bonus loot/equipment for the unique no-loot boss.

## Owner-directed combat test changes

| Feature | New source-trial behavior | State |
| --- | --- | --- |
| Hordes | 155 Killer Bunnies still fully vulnerable to magic/ranged/melee | Existing, unchanged |
| Final-boss accepted damage | Direct, player-attributed vanilla melee only; other damage remains canceled and admin `generic_kill` stays an escape hatch | Source prototype; real custom melee needs audit |
| Damage defense | **100-tick (5-second) cooldown after a genuinely positive-damage successful melee hit**, replacing the old 90% random evasion. Consider 200 ticks / 10 seconds as second test scenario, **not locked** | PR #556 source trial; Type B/C pending |
| Tongue | **2% per successful damaging direct boss bite**, not 10%; secondary normal mob-attack damage packet, no `Entity.kill()` or `RemovalReason.KILLED` | PR #556 source trial; actual totem/Monk/lich protections must be tested |
| Lava/fire | Damage canceled as before; attempts produce **five seconds of Speed V** and light the rabbit on fire | PR #556 source trial; actual lava physics/AI pending |
| Aquatic passives | **Conduit Power I and Dolphin's Grace I**, applied by Gateways with max (noncreeper) duration | PR #556 source trial; underwater rabbit AI benefit not assumed |
| Appearance | Real vanilla frog-tongue execution sound and visible lash/telegraph is **not implemented** by triggering a damage packet; implement with short synced client-visible particles/model effect | Still open |
| Apotheosis | Genuine randomized intrinsic high-tier boss modifiers via pinned `apotheosis:elite` Gateway bridge | Still open |

**Fight clock warning:** The boss wave currently allows only **7,200 ticks (six minutes)**. A 100-tick acceptance interval allows at most around 72 damaging hits under ideal continuous melee over that wave; 200 ticks allows around 36. A boss with nominal 1,000 HP would require at least ~13.9 or ~27.8 *post-mitigation* HP per accepted hit respectively, ignoring all movement and downtime. Random high-tier traits could increase the requirements further. Do **not** claim a 10-second window is winnable at this six-minute timer until measuring endgame Monk, bare Zerker, Hunter and weapon damage. This should be harder than Chaos Guardian by mechanics, but difficulty must be achievable, not an automatic timeout.

## Reactive effect inversion — owner-approved direction, not yet active code

The final rabbit should **turn attempted hostile/neutral magic into beneficial capability**, rather than simply ignore everything. Proposed finite, audited, nonstacking **counter table**:

| Incoming effect or event | Inverted boss benefit | Implementation and edge conditions |
| --- | --- | --- |
| Poison or Wither | Regeneration II–III, short refreshable duration | Deny incoming status through `MobEffectEvent.Applicable`, add positive buff; damaging poison/wither ticks still blocked by current nonmelee damage filter |
| Slowness | Speed II–III | Deny detrimental effect; speed max/refresh only, no infinite amplifier stacking |
| Weakness | Strength II | Deny Weakness, ensure ordinary rabbit attack damage calculation uses attribute |
| Mining Fatigue | Haste II | May have negligible direct effect on a rabbit; choose a useful alternative if in-game QA proves inert |
| Blindness / Darkness | Night Vision + short target-tracking bonus | Night Vision alone does not improve AI targeting; only apply real tracking behavior with a dedicated AI rule |
| Levitation | **Actual controlled flight**, temporarily chasing nearest combatant | No vanilla `flight` potion for rabbits. Requires server-driven physics/AI, gravity reset, landing safety, gateway leash, no chunk loads; **do not claim merely giving Levitation is flight** |
| Forced teleportation | **Three seconds of teleport permission** and defensive/retaliatory blink | Generic `EntityTeleportEvent` does not intercept every modded `setPos`; audit triggers, ensure safe teleport destination, no out-of-gate leashes/chunk loads |
| Lava / on-fire / in-fire | Flaming visual + extreme speed (Speed V) for five seconds | Source-prototype already in PR #556; lava cannot be assumed to grant fire immunity by itself, old nonmelee damage veto already applies |
| Other harmful/neutral effects | Replace with a small safe positive response, default to a brief Speed I or Regeneration I | Fail closed for unrecognized modded effects until dedicated effect registry/type audit; no permanent status immunity to beneficial spells |

**Constraints:** this is boss-only and must not also buff 155 horde rabbits. Effects can *refresh* capped durations, never add infinite stacks. State is server authoritative and visible/audible to players so counterplay is understandable. Cross-mod spell transformations, teleports, gravity changes and persistent status cleanses require exact event/API audit. Environmental damage immunity and physical/control immunity remain distinct. No uncontrolled `entity.setNoGravity(true)` without automatic reset. Preserve safe admin kill, deterministic cleanup on failed Gateway and no boss ability to teleport off-map or out of gate.

## Source and B/C evidence boundaries

- [x] Verified exact publisher JAR SHA-512, ZIP and compiled Gateway/Elite class presence (read-only no binary redistribution)
- [x] Isolated KubeJS source callback tests and syntax for five-second window and two-percent lethal *proposal* (requires green CI on final PR head)
- [ ] Verify `/attribute` health, damage, aquatic passive buff durations, boss self-fire/Speed V, native KubeJS wrapper damage packet viability
- [ ] Verify genuine Apotheosis elite randomization on actual rabbit, no armor/weapon or bonus loot leaks
- [ ] Verify both 5s and 10s melee windows with combat logs, KubeJS callbacks, actual per-hit damage and six-minute timer
- [ ] Verify 2% procs statistically over enough **successful damaging boss bites**, not raw attack attempts, with normal Totem of Undying, Monk, Lich, werewolf/vampire and dragon resurrection in single-player/multiplayer
- [ ] Implement and test complete spell inversion table, levitation flight, and teleport retaliation; check modded effect APIs
- [ ] Frog tongue real animation/audio, all 156 mob drops and horde TPS; owner-authorized actual game-only

No actual modded server or player client has been run by this prototype. **Do not merge the partial effect-counter implementation or unlock Chaos Pearl until the owner reviews unfinished behavior.**
