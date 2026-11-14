# Combined Quest Book + Vampirism QA

This test branch combines the pinned 147-mod B12 QoL/Relics pack (including Reliquified Artifacts and More Overlays) with the experimental `tmm_transformations` NeoForge source module and a corrected FTB Quests 2101.x locale.

The GitHub Actions workflow creates a custom disposable Prism ZIP with a first-party compiled `tmm_transformations-0.1.0-dev.jar`, rather than adding an untested mod to the production packwiz lock. The quest fix merges 47 fragmented translation files into the one `config/ftbquests/quests/lang/en_us.snbt` required by FTB Quests 2101.1.38. It preserves five category names, 45 chapter names and 179 quest descriptions, then rehashes packwiz. Localized quest **client rendering remains unverified** until tested in Prism.

1. Import the inner `Too-Much-Magic-Quest-Vampire-QA-Prism.zip` from the GitHub Actions artifact into Prism. Select Java 21.
2. Create a **new throwaway world**. Do not open an existing save. Verify group names **Introduction, Progression, Magic, Exploration & Combat, Endgame**, the 45 chapters and 179 quest titles. Check the 18-entry Getting Started continuation and the Scriptor fuel-free-smelting tip.
3. Verify `/tmmtransform vampire` shows both providers `ABSENT` on a fresh human.
4. Quit the world and edit `minecraft/saves/<world>/serverconfig/tmm_transformations-server.toml`: enable `experimentalVampireChassis=true`, `experimentalAutomaticDualAcquisition=true`, `experimentalAllowUnverifiedOtherProviders=true`. Keep `experimentalOperatorCure=false`. Restart the test world. These flags are test-only because other form probes aren't implemented.
5. Acquire Vampirism's native Sanguinare Vampiris, then in another throwaway world acquire JAWR vampirism with the vampire fangs. The goal is for both native providers to become `PRESENT`. Re-test blood, hunger, sun, garlic and independent progression. Refer to `docs/VAMPIRE-CHASSIS-V1-TEST-PLAN.md`.
6. Test a fresh Reliquified Artifacts Eternal Steak separately. Test the new TrashSlot and TreeChop/Leaves Be Gone behavior, too.

The workflow must pass full-server smoke and compile before publishing its import ZIP. Even a green workflow is **not** proof that the JAWR/Vampirism synchronization or quest rendering works in-world.

## Quest Shop coin claim compatibility (2026-11-14)

**Previous 1.4.0 custom coin rewards are unusable with FTB Quests 2101.1.38.** Quest Shop defines `void CoinsReward.claim(ServerPlayer, boolean)`, but pinned FTB Quests 2101.1.38 defines `boolean Reward.claim(ServerPlayer, boolean)`. This binary-incompatible signature prevents reliable claims; vanilla FTB XP rewards are unaffected. Also, the old sales specified `team: true` instead of the actual 2101.1.x key `team_reward: true`.

For QA, all **12 copper/emerald/diamond sale quests** now use FTB Quests' supported built-in command reward: `type: "command", command: "hqs coins add {p} 1", permission_level: 2, team_reward: true`. The corresponding XP reward is also `team_reward: true`. This invokes Quest Shop's own team-currency command under the submitting player's identity, without relying on the obsolete custom Java reward method. Task consumption, reward IDs, quest IDs, prices and coin amounts remain unchanged.

**Client QA:** submit 8 copper ingots, claim the 5 XP and the separate *command reward* (which appears as a command-block icon instead of a money bag). Confirm the message that a coin was added, then run `/hqs coins`, open `/hqs shop`, buy a cheap item if funds permit, and verify the balance decreases. Reopen the shop to force a fresh balance sync. In multiplayer, ensure one credit per team, not one per player. Repeat with emerald and diamond sales before approving economy release. A successful server boot does not test command execution at claim time.

**Future:** update/replace Quest Shop 1.4.0 with a version built against the current FTB Quests API, using `CoinsService.add` for UI sync. Only then restore a proper coin-icon reward type. Current QA workaround is intentional and fail-fast validated against accidental regression.
