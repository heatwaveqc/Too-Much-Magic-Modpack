# Direct flight ownership audit: Feywild and Extra Botany

Issue: [#330](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/330)  
Frozen integration source: `74b22da0f1e9c598a0a54a4ce49038b1ef336ea1`  
Audit scope: the two providers explicitly called out by #330 for direct creative-flight permission mutation. This is one completed Type-A slice of the wider all-provider flight inventory.

## Evidence boundary

This report uses the indexed Packwiz pins, the November 18 same-build installed-artifact census, and upstream source at immutable commits. It does **not** claim client, server, or gameplay acceptance. No Integration Assembly, Minecraft boot, or Prism package was run.

| Provider | Indexed binary evidence | Immutable source | Parity limit |
| --- | --- | --- | --- |
| Feywild 5.6.4 | `feywild-1.21.1-5.6.4.jar`; Packwiz SHA-1 `e59b68dd85b92ebbdbde478ea4e52fdb8b84c9d0`; observed SHA-256 `6e115fe731ecfd18bd6d676e052998d3a6c2e12e5c31cf101b4c77b93359c7b8` | [`SaphieNyako/Feywild@2a339f98`](https://github.com/SaphieNyako/Feywild/tree/2a339f98a5a2ea0d5620801538095a7b013b6de2), whose Gradle version is `5.6.4` | The exact installed JAR was not available for class-level comparison. Treat source findings as release-aligned, not proven byte-identical. |
| Extra Botany Revived 1.0.4 | `ExtraBotany-1.21.1-1.0.4-NeoForge.jar`; Packwiz SHA-1 `246e372964413d09c5f2fa0c93b95a4c224d1949`; observed SHA-256 `d0554b57149b46beaefe38f11d20ded9b93dbbc954d4deb2939fd2f58cb19dad` | [`Velron-Devs/Extra-Botany-Revived@b58857d`](https://github.com/Velron-Devs/Extra-Botany-Revived/tree/b58857d3658479f6e8684d3b45f6d3138a294548), tagged `curseforge-neoforge-1.21.1-1.0.4` | Release tag and filename match; the installed JAR was not available for a bytecode/source descriptor comparison. |

Machine-readable provenance and findings are in [`manifest/flight-provider-direct-ownership.json`](../../manifest/flight-provider-direct-ownership.json). `python scripts/verify_flight_provider_direct_ownership.py` checks those frozen values against the current index, pins, and installed-artifact census.

## Findings

### Feywild Pixie Wing Tiaras

The base `feywild:pixie_wing_tiara` drops from Titania, Mab, Oberon, and the Ashen Lord. Seasonal/light/shadow variants are crafted from it. Any recognized tiara equipped in the Curios head slot (or offhand without Curios) refreshes `feywild:fey_flying` to 220 ticks.

The effect's server tick grants creative-style flight only when `abilities.mayfly` is currently false. At that moment it sets both `mayfly=true` and `flying=true`, stores `feywild_granted_flight=true` in player persistent data, and calls `onUpdateAbilities()`. If another provider had already set `mayfly`, Feywild neither changes the flags nor records ownership.

When the effect is absent, `ModEventListener` checks only the Feywild ownership boolean. If true, it unconditionally sets both `mayfly=false` and `flying=false`, removes the boolean, and synchronizes abilities. It does not ask whether a second provider became valid after Feywild originally granted flight.

**Result: order-dependent clobber risk.** If Feywild grants first, a second provider starts later, and the tiara is then removed, Feywild can revoke the shared vanilla flags even though the second provider remains valid. A provider that reasserts `mayfly` every tick may recover permission on the same or next tick depending on event order, but `flying=false` can still drop the player out of active flight. The inverse ordering is safer: when another provider was already active, Feywild does not record ownership and therefore does not revoke on removal. This is consistent with upstream's open [flight report #157](https://github.com/SaphieNyako/Feywild/issues/157), but this audit does not claim that report's mod list or symptoms reproduce in TMM.

### Extra Botany Core of the Void

`extrabotany:core_of_the_void` is a bound relic Curio. Its source route uses an Empty Herrscher Core from the Void Herrscher's limited supply bag/loot and an endgame recipe; the item is also tagged `extrabotany:needs_ego_defeat`. While valid and supplied, it grants creative-style flight and consumes 100 Mana each tick only while the player is actively flying.

`CoreOfTheVoidItem.updatePlayerFlyStatus` runs on every player post-tick. It tracks ownership in a process-local synchronized list keyed by player name plus logical side. A valid bound Core with at least 100 available Mana adds the key and sets `mayfly=true`. Once tracked, it continues to set `mayfly=true`; while `flying`, the server requests the 100-Mana payment.

If the Core is removed, unbound, or cannot satisfy the next 100-Mana availability check, the code unconditionally clears `mayfly`, `flying`, and `invulnerable` for non-creative/non-spectator players and removes its tracking key. It does not consult any other flight provider or remember whether another provider originally supplied flight.

**Result: unconditional revoke clobber risk.** Removing or exhausting the Core can revoke a simultaneous Feywild tiara, Progression ability, or other creative-flight provider until that provider reasserts permission. Even when permission recovers, clearing `flying` can interrupt active flight. The name-and-side process-local tracker also needs death/respawn, reconnect, and player-rename scrutiny; logout removes both side keys, but the tracker is not a persistent UUID-owned grant ledger.

### Extra Botany Gaia arena is a separate intentional restriction

The Extra Botany Gaia arena explicitly forces `flying=false` for participating non-creative players on both client and server while the fight is active. It does not clear `mayfly`. Upstream comments describe this as the arena rule, so it should be tested as a localized suppression rather than automatically patched as an ownership bug. Acceptance must show that a legitimate provider remains available and flight can be re-toggled after leaving or ending the encounter.

## Required coexistence QA

These are gameplay tests for a future exact candidate, not results from this static audit.

| Sequence | Setup and action | Required observation |
| --- | --- | --- |
| Feywild alone | Equip each tiara route; toggle flight; remove it | Flight appears, persists through normal tiara refresh, then cleanly disappears without a stuck flag. |
| Core alone | Equip a correctly bound Core with >100 Mana; fly; remove it, then repeat by exhausting Mana | 100 Mana/tick is charged only during active flight; permission and active flight end at the intended boundary; backfire behavior remains native. |
| Feywild then Core | Start tiara flight, equip Core, remove tiara while Core remains valid | Core permission remains and the player is not dropped by Feywild cleanup. Current source is expected to be order-sensitive; record exact tick/order symptoms. |
| Core then Feywild | Start Core flight, equip tiara, remove or exhaust Core while tiara remains equipped | Tiara permission remains and active flight is not interrupted. Current source is expected to expose Extra Botany's unconditional revoke. |
| Removal in both orders | With both valid, remove each provider first in separate trials; then remove the last provider | First removal preserves the surviving provider; final removal clears flight exactly once. |
| Persistence | Repeat both-provider cases across death/respawn, relog, and dimension change | No stuck flight, unauthorized loss, stale username-side tracking, or need to re-equip a still-valid provider. |
| Mode boundary | Repeat removal while switching survival/creative/spectator | Native mode flight is never revoked; returning to survival reflects only valid provider state. |
| Gaia arena | Enter with each provider and both together; finish/leave arena | Survival flight is suppressed only in the intended encounter; permission is not permanently lost and can be re-toggled afterward. |

At minimum, test two clients with independent equipment so a fix cannot accidentally become a global flag. Record exact integration SHA, item IDs, starting Mana, action order, observed `mayfly`/active-flight behavior, and whether the player fell or merely needed to re-toggle.

## Implementation handoff and remaining limits

- Do not solve this by having TMM blindly set `mayfly=true` every tick; that would fight intentional restrictions and make last-provider removal unsafe.
- A future adapter should aggregate **currently valid provider claims** under one server-authoritative ownership service, preserve creative/spectator state, and clear vanilla permission only when the last participating claim ends. It must also define how intentional no-flight zones temporarily suppress active flight without deleting claims. That architecture belongs to the Progression/shared compatibility owner and is not authorized by this read-only issue slice.
- Exact JAR class/descriptor comparison remains required before writing mixins or relying on private field names. The wider #330 inventory of every flight item, spell, transformation, mount, virtual Elytra, and affix remains open.
- No runtime acceptance is claimed here.
