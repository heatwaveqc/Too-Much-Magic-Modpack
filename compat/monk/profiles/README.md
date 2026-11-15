> **Current selected backend:** Tyz's Skills 6.4.2. See
> `tyzs-tiered-skills-v1.json` for five-stage x99
> scaling and `../tyzs-prototypes/` for disabled Strength
> proof-of-concept JSONs. Pufferfish one-time, Monk
> native one-time/licenses, Tyz owns leveled ranks.
> Earlier Pufferfish level-backend proposals are superseded.

# Monk profiles — design only

Two **distinct** definitions; never treat them as one tunable config:

1. `original-monk-1.4.json` is the planned **classic-style standalone default** using RWTema's
   original Monk mod (1.12.2) as a historical behavioral reference. It
   remains non-executable until its independent runtime implementation is
   complete; the original mod's source/assets are not copied here.
2. `too-much-magic.json` is the **TMM-specific design target**, authoritatively
   requiring gear-free endgame parity, Curios compatibility, universal
   Apotheosis affix *unlock coverage* and repeatable player-attribute
   investment.

The accompanying inventory snapshots pin **Apotheosis 8.9.0** and
**Apothic Attributes 2.11.0**, matching the modpack manifest at the
time of audit. They are **not** completed skill registries. Each listed
affix requires a tested Monk-specific adapter rather than trying to
equip an invisible piece of gear.

The future installed standalone Monk will select the classic-style default;
the TMM modpack must **explicitly select** its extended profile. The two must
not silently inherit one another's rank caps or power unlocks.

These are checked-in **design inventories, not executable NeoForge config**.
The in-game addon is currently only an inert starter faction. No profile
selector or runtime loader exists yet.

Rationale and parity test plan:
[MONK-GEAR-PARITY.md](../../../docs/MONK-GEAR-PARITY.md).

The registered faction-level limit is now a NeoForge STARTUP config
(default 20), loaded before Vampirism faction registration. This is a
registration-time property: do not use COMMON/SERVER config for it, since
those load later. Clients and servers must have an identical value.

Original Monk factory crosswalk:
`original-monk-abilities-1.4.json` (25 ability types, including
four unused by the historical defaults). This is separate from the
142 attribute registry candidates and 99 upstream Apotheosis affix definitions (98 selected TMM techniques).
Some numerical abilities overlap: do not double-count them or treat
any crosswalk row as an implemented power.

TMM revisions: *no Monk Creative Flight skill*. Legal Draconic Curio
modules provide that capability. Mining has distinct finite harvest
tier and repeatable speed. See `world-tier-unlocks.json`.

Backend-neutral proposed skills: `skill-catalog-v1.json` (172 core and mod-specific including 10 conditional non-Attribute magic talents), `skill-affix-techniques-v1.json` (98 selected affix adaptations; excludes Unbound entirely), `extra-attribute-candidates.json` (two NeoForge attributes discovered from affix source). All 25 historical Monk abilities are mapped. Read `docs/MONK-SKILL-CATALOG.md` before placing skill nodes or choosing a purchase backend.
Unification proposal: `skill-unification-proposal.json` (24 tentative
shared Pufferfish traits covering 43 distinct source attributes).
Single-unlock abilities remain native Monk faction nodes; leveled
abilities have native **licenses** and Pufferfish investment where
technically possible. **Pufferfish stores binary skill-node IDs**;
true unbounded trait ranks and fine-grained faction gate enforcement
are not implemented. See `docs/MONK-TWO-LAYER-SMOKE-TEST.md`.
