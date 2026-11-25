# EvilCraft replication guard — #380

Owner-approved independent extraction from #413 (2026-11-24); separate from #373's unresolved capture adapters. Target test/full-integration, baseline 491cdd18c08a03b84abc6d693d3a4265fb411876. Preserve donor #413 exact 13bc1e0b4893b85105204ea6c63fe3badc5091fb and #373 exact 3393152e8c5a6ab0fb59f5a9272948c911019921 for provenance. Five Java source files transferred unchanged; shared mixin manifest extended from the current baseline, not restored from either donor. Two runtime tags copied from #373, verified against integrated registry revision 6 (registry-v1.json blob d16fb3be0649d544054cdf5a6bb543243c54fb33): 1,196 common denial IDs + four bound worker IDs = 1,200 replication exclusions; zero membership drift. #353 already integrated registry evidence; no Reliquary, Capturing, Neo Vitae, Quantum, Boss Catcher, spawner, Drygmy, or other consumer activation imported. #373's eventual merge must reconcile these shared identical tags and preserve all current mixins.

## Data-driven policy

Author source decisions in docs/creatures/registry-v1.json and project with registry_tool.py capture-tag / replication-tag. Runtime datapack path data/tmm/tags/entity_type/replication_blacklist.json inherits #tmm:capture_blacklist plus occultism:afrit, djinni, foliot, marid. Pack builders can extend via ordinary entity-type datapack tags; the code consults actual live tags each call (including after reload), not a cached literal list. Missing tag denies entity replication; a present empty tag follows the actual data policy. Native EvilCraft config regex/IMC and native canSustain rules remain authoritative; no change to spawn cap/nonplayer-kill defaults, native ordinary Vengeance Essence, player-specific behavior or unrestricted Boss Catcher. Existing denied boxes remain intact.

## Exact current pin

Owner-supplied evilcraft-1.21.1-neoforge-1.2.102.jar SHA512 278ab7cadb9077400563e2a238efdf63aecc71dbcd61b81db7620d020f3ff5420bff522057c60ef57a47cc7efd3bf0f9cb16c69c31ec0b9d9aed0c8fed29a5d5 matches current Packwiz pin. javap -p -c -s on actual supplied artifact confirms native canSustain(LivingEntity):boolean retains config regex and IMC checks; getRandomInnerEntity(RandomSource):EntityType filters MONSTER and samples native list; typed BoxCookTickAction.canTick and ReanimateTickAction.canTick descriptors unchanged; Furnace getEntity():Entity and Reanimator getEntityType():EntityType unchanged. Reanimator onTick drains Blood before producing eggs or clearing box, hence rejection at canTick. Preserve sentinel behavior: native self-spirit fallback is not a farmable inner monster; verify with all denied and missing-tag QA. Historical 1.2.100 checks on #413 are provenance, not current CI. New audit_evilcraft_replication.py checks actual current registry projections, unique source registration, full Packwiz/index/pin hashes and exact method/descriptor pairs on downloaded or supplied JAR. No JAR redistribution. Scoped source/compile CI only.

## Runtime acceptance (not yet run)

- Allowed zombie, possessed/wild Occultism, native player/spirit behavior: no regression in spawn, loot and egg routes.
- Denied Wither, boss, four bound workers, preexisting/synthetic boxes: no repeatable loot, eggs, Blood/input debit or box clearing.
- Reload a datapack adding a previously allowed mob to replication tag; verify death, random and both machine paths observe new tags. Removing only added override restores native behavior, subject to baseline/native deny rules.
- Empty candidate selection / missing tag: no farmable sentinel. Native ordinary essence, spawn cap, nonplayer-kill behavior and native denies unchanged.
- Capture instance identity cannot recover Foreman vs Doloris after serialization; shared type policy remains as authorized, no invented instance metadata.
- Reconcile later #373's shared tags and manifest, do not activate unrelated native adapters in this slice.

Source review and CI do not prove actual Mixin application or gameplay. No full game build, boot, packaging release or direct integration merge authorized in this workstream.
