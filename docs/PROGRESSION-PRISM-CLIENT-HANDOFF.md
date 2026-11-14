# Full Progression Prism local test — 2026-11-14

The first-pass **168 quest** version of TMM is built from the immutable packwiz manifest captured at workflow runtime. It is **NOT a balanced survival release**. The Prism artifact is an outer GitHub Actions archive containing an inner importable ZIP. No third-party mod JARs are included; packwiz retrieves the original hosted jars and validates hashes.

- [CI workflow to create the pinned Prism import](../.github/workflows/progression-prism.yml); find its latest successful *Full Progression quest Prism test package* run on the feature branch, then click the `Too-Much-Magic-Progression-Quest-Prism-140-Mods` artifact. The successful run's artifact link is listed in the related PR/test report.
- Prism: **Add Instance → Import** the inner `Too-Much-Magic-Progression-Quests-Prism.zip`. Select **Java 21**; pack memory defaults to **8 GiB maximum**.
- Use a **throwaway test world**. Do not open your normal world while gate enforcement is absent.
- Confirm 45 chapter files, five groups, 168 quest entries, Quest Shop purchases, Cooking for Blockheads, first-log pouch, commodity sales, and 32 full power quests.
- Evaluate BiggerReactors first, then the Draconic **Energy Core power bank**. The catastrophic **Draconic Reactor** must only be tested remotely in a disposable Creative world. Follow [POWER-TECH-QUEST-GUIDE.md](POWER-TECH-QUEST-GUIDE.md) and double-check in-game machine registry/recipes; manual checkmark quests don't assert a structure was correctly built.

Report issue screenshots, Prism console logs, and the relevant `logs/latest.log`. Do not treat this artifact as a permanent/modpack release.
