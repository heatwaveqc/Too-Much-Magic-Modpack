# Roots Classic × Pufferfish Skills — KubeJS pilot

**Status:** EXPERIMENTAL / NOT YET TESTED IN GAME (2026-11-13). The scripts are opt-in for test players only. They do not install Pufferfish Skills or create production perk trees. JavaScript syntax was checked separately with Node, not validated by a running client. See the repository's AGENTS.md promotion gates.

## Locked design

1. **Crystal Staff craftsmanship** is applied once, by the **first player to pick up the staff**, not the ritual operator. Their crafting perks become permanent bonuses to every nonempty spell in the staff. Subsequent pickups, player transfers, and respecs do not remove or duplicate those bonuses.
2. The staff's one-time marker is stored under \`minecraft:custom_data\` as \`tmm_roots_imprinted\`. Ordinary staff items are not modified. Blank Crystal Staffs are not imprinted.
3. **Maximum Terra** is server authoritative: \`40 + earned skill bonus\`. It is recalculated absolutely rather than incrementally; lower current Terra only when necessary to obey a reduced maximum.
4. **HUD synchronization** uses KubeJS \`player.sendData('tmm:roots_terra', ...)\` on the server and \`NetworkEvents.dataReceived\` on the client. The client copies maximum/current Terra into its local Roots mana attachment; Roots' own HUD reads that attachment.
5. Refresh maximum Terra after login/respawn, and via a five-tick heartbeat that covers dimension changes, newly earned or refunded skills, and occasional missed initial packets. No logout write is necessary; Roots' attachment is not persisted.
6. This is equipment craftsmanship, not a casting-time bonus. A crafter can make better staffs for other people. Final perk values and Pufferfish integration remain to be designed.

## Pilot files

- \`kubejs/server_scripts/roots_staff_first_pickup.js\` — \`ItemEvents.canPickUp\`, native \`SpellDataList\`, immutable one-time imprint.
- \`kubejs/server_scripts/roots_terra_server.js\` — Terra capacity recalculation and authoritative snapshots.
- \`kubejs/client_scripts/roots_terra_client.js\` — updates the client's Terra attachment for the existing HUD.

### Opt-in test commands

The skill-stage identifiers below are **temporary fixtures**, not the real Pufferfish skill IDs. They add +1 each to staff potency/efficiency/size and +20 to Terra capacity:

\`\`\`mcfunction
/tag <player> add tmm_roots_integration_test
/tag <player> add tmm_roots_staff_potency_1
/tag <player> add tmm_roots_staff_efficiency_1
/tag <player> add tmm_roots_staff_size_1
/tag <player> add tmm_roots_terra_capacity_1
\`\`\`

The first flag is mandatory; users without it see no intentional behavior changes. Replace these stage checks with the real Pufferfish skills when the mod and tree are installed.

## Source-backed rationale

- Roots' \`RitualImbuer.doEffect\` creates a world item entity, transferring up to four spell entries from powder without a player argument. Hence we cannot reliably use a normal player crafting-result event.
- Roots' \`SpellDataList\` records are item components; \`CrystalStaffItem.addEffect\` writes them and casting simply reads them. We can replace the list during **pre-pickup** with no casting modification.
- Roots' \`RootsAttachments.MANA\` is registered with \`AttachmentType.builder(ManaAttachment::new).build()\` — no attachment serializer or sync handler. \`ManaBarEvent\` reads the local player's attachment.
- KubeJS 2101 exposes \`ItemEvents.canPickUp\` from NeoForge \`ItemEntityPickupEvent.Pre\`, with the real stack available, and 1.21.1 \`player.sendData\` / \`NetworkEvents.dataReceived\` for small cross-side messages.

Sources:
- https://github.com/Lothrazar/RootsClassic/blob/1.21.1/src/main/java/elucent/rootsclassic/ritual/rituals/RitualImbuer.java
- https://github.com/Lothrazar/RootsClassic/blob/1.21.1/src/main/java/elucent/rootsclassic/datacomponent/SpellDataList.java
- https://github.com/Lothrazar/RootsClassic/blob/1.21.1/src/main/java/elucent/rootsclassic/attachment/RootsAttachments.java
- https://github.com/Lothrazar/RootsClassic/blob/1.21.1/src/main/java/elucent/rootsclassic/client/ui/ManaBarEvent.java
- https://github.com/kube-mods/kubejs/blob/2101/src/main/java/dev/latvian/mods/kubejs/item/KubeJSItemEventHandler.java
- https://kubejs-wiki.tysontheember.dev/1.21.1/guides/networking/

## Test matrix before production promotion

- Server startup: verify scripts load with the pinned Roots Classic 1.5.10, KubeJS 2101.7.2-build.381, and Rhino; check class accessibility and absence of script errors.
- Crystal Staff: create a staff with one through four populated spell slots; first pickup adds correct bonus to nonempty slots, unchanged empty slots; repeated drops/pickups and transfers apply nothing more; blank staffs are not marked.
- No leakage: untagged players receive ordinary staffs, no Terra packets and unchanged maximum.
- Full client: opted-in +20 max Terra appears as **60** in the HUD; current Terra follows the server. Test login, logout/rejoin, death, End and Nether travel, rapid casting, and removal/addition of capacity stage.
- Observe HUD width: Roots renders roughly 8px per four maximum Terra; large maxima could overlap other UI elements, even if data synchronization succeeds.
- Review existing old staffs that lack the marker: on first tagged pickup, this pilot will also imprint them.
- After replacing temporary stage flags with actual Pufferfish skills, test both scripts again and remove the opt-in guard only after approval.

This PR is an **experimental integration test**, not a demonstrated runtime pass or finished skill-tree integration.
