# Monk profiles — design only

Two **distinct** definitions; never treat them as one tunable config:

1. `original-monk-1.4.json` is a **historical reference** to RWTema's
   original Monk mod (1.12.2). It is not active and the original mod's
   source/assets are not copied here.
2. `too-much-magic.json` is the **TMM-specific design target**, authoritatively
   requiring gear-free endgame parity, Curios compatibility, universal
   Apotheosis affix *unlock coverage* and repeatable player-attribute
   investment.

The accompanying inventory snapshots pin **Apotheosis 8.9.0** and
**Apothic Attributes 2.11.0**, matching the modpack manifest at the
time of audit. They are **not** completed skill registries. Each listed
affix requires a tested Monk-specific adapter rather than trying to
equip an invisible piece of gear.

These are checked-in **design inventories, not executable NeoForge config**.
The in-game addon is currently only an inert starter faction. No profile
selector or runtime loader exists yet.

Rationale and parity test plan:
[MONK-GEAR-PARITY.md](../../../docs/MONK-GEAR-PARITY.md).
