# Updated Prism import for UI fixes

This [GitHub Actions workflow](../.github/workflows/quest-ui-prism.yml) creates a **separate Prism import ZIP** for the corrected 179-quest book. Unlike the previous 168-quest download, it contains the canonical `lang/en_us/chapters/*.snbt` quest translations and deliberate branch layouts.

To install: download the `Too-Much-Magic-Quest-UI-Fix-Prism-140-Mods` artifact from the latest successful workflow run; GitHub supplies an outer ZIP. Extract it, then **Prism → Add Instance → Import** the inner `Too-Much-Magic-Quest-UI-Fix-Prism.zip`. Select Java 21, and create a **new disposable world**. The bootstrap downloads all 140 pinned mod files from their original providers and verifies their hashes. No third-party mod JARs are distributed in this repository.

Important checks: five correct category names, 45 correct chapter names, no `undefined` titles/descriptions, visibly spaced Getting Started and reactor layouts, Getting Started beyond iron, four new early magic tips. If the old test instance was already installed, it does **not** automatically become this exact immutable snapshot; use the new import for deterministic QA.

The refresh does **not** certify actual client rendering or shop behavior; ProjectE/Chaos Guardian gating and several compatibility safeguards remain incomplete. See [QUEST-CLIENT-QA-FIX.md](QUEST-CLIENT-QA-FIX.md).
