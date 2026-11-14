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
