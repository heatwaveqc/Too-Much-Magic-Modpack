# Friend QA stability repairs — 2026-11-17

Based on integration head `938c401ac304245663137085e7a2234244d57bf3` and the installed snapshot `322cf3f9f0deb303fc6e6c29b340273ed3f481f6`. Source repairs only, no authorized assembly/smoke/publication.

- Restore FTB Library sidebar top-left and FTB Chunks minimap top-right. Confirm on a fresh client; Tyz button placement still needs exact pinned UI config/implementation inspection.
- Halve Ars quest coordinates in runtime chapter, pilot source, and layout ledger; all 49 IDs/tasks/rewards/dependencies preserved. Confirm zoom and line rendering in client.
- Correct both marble stonecutting ingredient codecs and generator (item objects); validate actual RecipeManager loading on next authorized boot.
- Baseline owns the tested Werewolves-free Almost Unified materials file; donor still provides other exact config files. No override exemption and no validation weakening. Confirm strict preflight before assembly.
- Pending: actual Akashic Tome populated component schema and native book list, FTB Quest translation runtime diagnosis, remove custom TreeChop HUD through Progression engine owner, invalid key -1 log root cause. Do not alter working quest rewards or vanilla key defaults to guess at fixes.

Source-side checks in connector: original index hash and every changed existing indexed blob checked with SHA-256; new packwiz hash chain computed, 49 unique compact coordinates, exact silver semantic diff. Repository CI and gameplay not yet run.

## Additional native-source findings

- Bundle slot: remove the explicit missing `curios:slot/empty_curios_slot` icon; Curios' generic fallback is selected when no icon is provided (upstream `SlotType` builder, 1.21.x). Slot size, operation, order, tag validator, and per-player slot definition are otherwise unchanged. Fresh-client rendering pending. Baseline ownership keeps the old donor from restoring the bad path.
- Akashic Tome exact release `release-1.21.1-1.8-30`: `akashictome:tool_content` is a list of native ItemStack codec entries; the attachment recipe adds `akashictome:defined_mod` and disambiguates repeated mod aliases. The existing once-only function gives an empty Tome. Do not blindly include all items named book/tome (spell weapons, progress-gated tablets, enchanted books etc.). Codex must inventory exact installed guidebooks, including component-backed Patchouli book variants, and construct the filled stack via native schema; then verify no loose guidebooks, no duplicates, and relog behavior. Keep two steaks and Improvable Skills unlock.
- FTB Quests pinned `v2101.1.38`: native TranslationManager constructs `objectType.UPPERCASE_16_DIGIT_HEX_ID.subKey`. Current source chapter translation keys match this shape, including Alcohol. Inspect the actual installed en_us.snbt and existing client log/locale synchronization; do not blindly lowercase keys or reintroduce unsupported inline titles.

## Follow-up: native inventory button and quest IDs

- Tyz's Skills 6.4.2 tag `6.4.2` registers CLIENT config and supplies absolute widget coordinates (`TyzsskillsClient`, `Config`, `InventoryButton`). New client-only config sets the visible 20×19 button to (3,72) instead of (3,3); native right-click drag remains available, and FTB stays top-left. Chosen position needs visual verification at different GUI sizes.
- Exact pinned FTB Quests `v2101.1.38` `BaseQuestFile.readID(Tag)` uses `Long.parseLong(hex, 16)` and silently generates a replacement if parsing fails. Alcohol chapter and its 76 child IDs began with `A1C`, outside the positive signed-long range. Map all 77 to `21C` with their lower bits unchanged; update chapter, locale, canonical manifests, dependency links, generator, and layout ledger consistently. No other authored title key has a high-bit ID. Added signed-long and chapter locale exact-coverage guard. Other unnamed chapters, if still present, need actual fresh client evidence; do not reintroduce old split or inline translations. Existing disposable test worlds can contain random FTB-assigned IDs, so use a fresh test world; no saved-team-data migration attempted.
- Custom TreeChop overlay removal is owned by the shared Progression module PR #312; keep the native tooltip and XP logic. Promote only its two reviewed changes, not the older full Progression tree.
