# Friendship Garden policy foundation

Source-only Java 21 policy for owner-approved issue #428. This isolated module
defines the persistent binding/presence state machine and remote splash-potion
authorization rules without depending on Minecraft classes.

The frozen implementation base is `test/full-integration` at
`077a34d8706dba51b10ad65dde05a7d34cfb5c96`.

Implemented here:

- immutable crafter UUID, retained display name, original flower registry ID,
  and short/tall form;
- blooming/budding/wilted state with an exact 6,000-tick mourning interval and
  explicit permanent-wilt handling for a future hardcore adapter;
- standard splash-effect transmission to online, alive, non-mourning bound
  players, deduplicated by player UUID and effect ID;
- full preservation of duration/amplifier/instant metadata;
- default-on beneficial and cross-dimension behavior, harmful/unknown opt-in
  plus PvP checks, master switch, and potion/effect blacklists;
- fail-closed exclusion of nonstandard scripted effects.

Not implemented here: flower eligibility tags, crafting/automation behavior,
NeoForge registration, blocks/items/components, placement/break persistence,
rendering/assets, tooltip UX, join/leave/death event wiring, particles/sound,
spatial splash detection, configuration files/screens, network synchronization,
or any mod-specific potion compatibility. Those require exact registry/API
work and later client/gameplay testing; this policy does not establish gameplay
acceptance.
