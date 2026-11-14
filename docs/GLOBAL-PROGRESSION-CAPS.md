# Too Much Magic: Pack-wide progression ceiling policy

**Owner direction recorded 2026-11-14. Planning/audit only.**

## Rule

For every installed mod that exposes improvable attributes, skill ranks,
progression levels or personal resources (including mana), do **not** impose
an extra TMM-specific cap below the maximum supported by that mod and its
actual pinned 1.21.1 implementation. Raise shipped defaults/config ceilings
where the upstream system supports it, without patching in hypothetical
limits. Scale into very high totals where technically allowed (e.g., Mahou
personal mana in the hundreds of thousands **if the pinned build permits it**).

**This is a modpack-wide config/compat policy, not a Monk perk.** A future
central compatibility/config layer (potentially alongside, but distinct from,
the transformation bridge) should inventory and enforce configuration. Do not
place Mahou/general Improvable Skills progression changes inside the Monk addon.

## Exceptions are mechanical, not arbitrary

- If an attribute is boolean, it unlocks once (e.g., Elytra Flight).
- If the engine/effect caps, purchase only as many effective ranks as remain
  before that cap. Example: Apothic Attributes cooldown reduction 95%.
- If a trait becomes nonfunctional at some threshold (e.g., extreme Attack
  Speed constrained by Minecraft ticks), define a transparent effective cap
  or alternate benefit; do not allow wasted paid ranks.
- If a trait genuinely scales safely and the native implementation permits
  more ranks, offer additional ranks at rising cost. This is **not** an
  arbitrary 14- or 20-rank cap and not a claim of literally infinite numeric
  storage or infinite engine values.
- Never change someone else's existing world or hidden progression data
  without a tested migration path.

## Source-backed initial inventory / outstanding questions

| System | What actually sets ceiling | Status / next check |
| --- | --- | --- |
| Vampirism addon factions | `IPlayableFactionBuilder.highestLevel(int)`; faction handler checks `getHighestReachableLevel()` | Monk now configures at STARTUP; verify high-level UI, tasks and skill points; vanilla vampire/hunter/werewolf caps unchanged |
| TMM Monk attribute ranks | Not an existing Vampirism repeatable node mechanic | Separate layer; design backend after attribute and skill inventory |
| Pufferfish Skills | Per-category `experience.json` `level_limit`; `skills.json` describes one-time unlock nodes | Configurable category experience level != unlimited per-node purchase; research API for high-level investment and synchronization |
| Apothic Attributes | Per-attribute ranged/percentage/boolean registration values | 22 known built-ins; inspect installed player registry for every mod attribute and real effective max |
| Mahou Tsukai | Server config `mana.MAX_MANA_CAP` for personal mana | Upstream examples show 20,000 default and pack overrides into 200,000 in other releases; **verify exact installed TMM version and accepted range before setting** |
| Improvable Skills | Per-skill growth/maximum depends on mod internals/config | Investigate installed jar and its exact max; avoid assuming arbitrary levels |
| Other magic mods | Each has its own resource, spell-tier or character-rank caps | Per-mod inventory, then maximize only documented configurable character growth; don't erase designed recipe/boss/progression gates |

NeoForge `RegisterEvent` happens **before** COMMON and SERVER config files are
loaded. For faction-registration settings the registered STARTUP config is
read immediately, but not synchronized by NeoForge. Pack/client/server must
distribute identical startup cap, with future handshake or registry validation
before promising dedicated-server safety.

Reference:
- https://docs.neoforged.net/docs/1.21.1/misc/config/
- https://docs.neoforged.net/docs/1.21.1/concepts/registries/
- https://github.com/TeamLapen/Vampirism/blob/version/1.21/latest/src/api/java/de/teamlapen/vampirism/api/entity/factions/IPlayableFactionBuilder.java
- https://puffish.net/skillsmod/docs/creators/configuration/files/experience
- https://puffish.net/skillsmod/docs/creators/configuration/files/skills
- https://forgebase.johnhalls.com/atm11/mods/mahou-tsukai/config

## Deliberately not implemented

No global config patches, no mod version upgrades, no general bypass of
progression, no Monk/Pufferfish leveled-trait ownership selected.
