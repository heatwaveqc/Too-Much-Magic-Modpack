# TeamLapen retirement — new-world experimental build (2026-11-15)

This change deliberately removes **Vampirism**, **Werewolves — Become a Beast**, and **Vampire's Delight** from the next integrated test. No separate TeamLapen-only library was pinned; other shared libraries are retained unless their remaining consumers are verified absent. JAWR remains the vampire/lycanthrope owner.

The full-integration runtime no longer compiles/loads the old `tmm_monk` faction or `tmm_transformations` bridge JARs. Instead the `tmm_progression` standalone module (one native Monk/Hunter framework, class-specific custom XP) is compiled, tested and packaged. Not every feature from the separate Monk and Hunter branches is yet included: the graphical tree, hunter mutagenic brews, some classic Monk trials and Tombstone direct-slot restoration require follow-up.

The retirement runs **after all integration imports**. It removes any stale TeamLapen packwiz pins, two FTB quest chapters and locale keys, five Werewolves food tags, two Vampirism Galore stone generator recipes, TeamLapen-only QA configs and the old silver material priority. Eidolon becomes the craftable silver tool **and armor** provider. Eidolon's silver sword retains its native undead ignition; the separate Werewolves silver damage/status implementation is not recreated, and should be a Hunter skill if wanted.

**Known loss:** The experimental transformation bridge's Archlich/Dracolich death arbitration and cross-mod mob-neutrality work are not bundled in this test either. All its previous configurations are intentionally removed. JAWR, Goety, Forbidden Magic and Dragon Survival themselves remain installed.

Only run on **disposable worlds**. Removing mod IDs can erase blocks/items from existing saves and preserve stale player faction NBT; do not open old saves without backups and a migration plan. The CI must pass static validators, Gradle unit tests and a dedicated-server smoke with exactly one first-party JAR before publishing. Client GUI, equipment, JAWR transformations, class XP, skill purchase, tag reload and multiplayer still need hands-on acceptance.
