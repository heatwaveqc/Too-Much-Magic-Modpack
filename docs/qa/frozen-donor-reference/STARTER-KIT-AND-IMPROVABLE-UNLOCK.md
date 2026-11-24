# Starter inventory, first-tick grant, and Improvable Skills

Pack objective: each player on first entry receives one empty `akashictome:tome` and **two ordinary cooked steaks** (`minecraft:cooked_beef`); separate from existing first-log Trinkets Pouch and without special Eternal Steak.

The new `tmm:starter_kit` advancement uses the vanilla `minecraft:tick` trigger and reward function `tmm:rewards/starter_kit`. Once earned it remains earned through logout, death and respawn. No recurring player tick item grant, and no duplication on respawn. A player importing an *existing* save with no recorded advancement will receive the kit once when first loading the update: test in disposable worlds and account for migration in release.

The reward function executes Improvable Skills 3's actual built-in `improvableskills book unlock @s true` command, marking the persisted book-access boolean. It doesn't change XP/skill ranks/individual skill scroll gates and doesn't provide a physical Skills Book. The inventory screen already has an Improvable Skills book-access button, but it was inactive until the flag was set.

Why not require crafting or Akashic transformation? Improvable's `ItemSkillsBook.inventoryTick` sets the same flag whenever the real `improvableskills:skills_book` is in the inventory. Akashic Tome stores real items and transforms into the real selected item; selecting it should likewise trigger the event. However, unlocking directly is cleaner and less brittle, and leaves the Tome free for any books a player earns later.

**In-game acceptance:** New survival player (non-op) starts with one Akashic Tome and 2 cooked beef, Improvable Skills button opens, no individual skills auto-unlock. First log still grants only one separate Trinkets Pouch. Relog/death does not repeat kit; second player gets independent kit. Verify command execution isn't blocked by permissions in the advancement function; server-only startup doesn't simulate player login.

**Registry IDs verified:** `akashictome:tome` in AkashicTome 1.21.1 source Registries.TOME; `improvableskills:skills_book` and `/improvableskills book unlock <targets> <silent>` in ImprovableSkills 1.21 source.
