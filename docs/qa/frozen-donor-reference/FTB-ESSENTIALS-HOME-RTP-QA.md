# B13 — Home & random teleport QoL QA

Built from the working 147-mod Quest + Vampire build, with exactly one new official mod: **FTB Essentials 2101.1.10 (NeoForge 1.21.1)**, file 8442866 (CurseForge project 410811).

Player commands: `/sethome [name]`, `/home [name]`, `/delhome [name]`, `/listhomes`, `/rtp`. These require no OP. The mod also provides `/back`, `/spawn` and consent-based `/tpa`/ `/tpahere`.

Conservative initial defaults: max 1 home per player, /home cooldown 10 seconds, /rtp cooldown 600 seconds (10 minutes), RTP at 500–25,000 blocks from spawn. Disable custom distance arguments and End RTP. These are explicit in both `config/ftbessentials.snbt` and `defaultconfigs/ftbessentials-server.snbt`; after changing a running server's config, restart it.

**Warning:** The current upstream FTB Essentials code says RTP does not yet respect claimed chunks; do not use it as a claim-safe land allocator. Test on a disposable world and inspect new destination safety. Also verify that commands remain regular-player-accessible without operator status.

## Test protocol

1. In the new QA Prism instance, create a disposable world and allow cheats OFF.
2. In normal survival as a non-op, run `/sethome`, walk elsewhere, then `/home`. Reconnect and repeat: the home should persist.
3. Test `/listhomes` and `/delhome`; verify the initial one-home limit by trying a second name and checking no silent deletion.
4. Run `/rtp` in the overworld; verify it lands on safe ground outside water, no apparent collision or suffocation. Verify cooldown on a second immediate use.
5. Confirm quests still show localized `&` titles without red errors, 12 commodity sale commands still credit one team coin, and Eternal Steak remains the Reliquified durability/skill version.
6. Keep the vampire test flags OFF unless doing the earlier dedicated disposal-world vampirism tests.

A green dedicated-server boot is not equivalent to a non-OP gameplay command test.
