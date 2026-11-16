# TeamLapen retirement — new-world experimental build (2026-11-15)

This change deliberately removes **Vampirism**, **Werewolves — Become a Beast**, and **Vampire's Delight** from the next integrated test. No separate TeamLapen-only library was pinned; other shared libraries are retained unless their remaining consumers are verified absent. JAWR remains the vampire/lycanthrope owner.

The next full-integration QA compiles/packages **two** first-party JARs: `tmm_progression` with the merged Monk classic physical trials/Bedrock Rest and advanced Hunter trial/specialties/mutations, and the repaired JAWR-only `tmm_transformations`. The retired `tmm_monk` faction and TeamLapen-derived bridge blood, HUD, and cure code are absent. The graphical skill trees, Tombstone direct-slot safety, and live gameplay acceptance still need testing.

The retirement runs **after all integration imports**. It removes any stale TeamLapen packwiz pins, two FTB quest chapters and locale keys, five Werewolves food tags, two Vampirism Galore stone generator recipes, TeamLapen-only QA configs and the old silver material priority. Eidolon becomes the craftable silver tool **and armor** provider. Eidolon's silver sword retains its native undead ignition; the separate Werewolves silver damage/status implementation is not recreated, and should be a Hunter skill if wanted.

**Restored for testing:** the bridge's independent Archlich/Dracolich death priority and retaliation-aware mob neutrality return in a second first-party JAR. JAWR alone owns vampire/lycanthrope forms, nutrition and cures. This is experimental event-ordering code and should only run on disposable worlds.

Only run on **disposable worlds**. Removing mod IDs can erase blocks/items from existing saves and preserve stale player faction NBT; do not open old saves without backups and a migration plan. The CI must pass static validators, Gradle unit tests and a dedicated-server smoke with exactly two first-party JARs before publishing. Client GUI, equipment, JAWR transformations, class XP, skill purchase, tag reload and multiplayer still need hands-on acceptance.
