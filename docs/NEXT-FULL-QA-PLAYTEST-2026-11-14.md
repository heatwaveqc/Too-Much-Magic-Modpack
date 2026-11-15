# Too Much Magic — next comprehensive client playtest

**Build channel:** `test/full-integration` / rolling UNSTABLE. **Target:** Minecraft 1.21.1, NeoForge 21.1.265, Java 21. **Use a new disposable Prism instance and world. Never test migrations on a valued save.**

## Acceptance ledger

The source, structural, Java unit and dedicated-server startup gates run in GitHub Actions. Every item below requires **client gameplay** or a second human player; CI-green is not gameplay-green. Record the exact `BUILD-MANIFEST.json` integration commit, mods list and `logs/latest.log` with any failure.

| Feature | Must work in game | Status before player test |
| --- | --- | --- |
| Baseline client | Prism 1.21.1 boots, opens new world, joins dedicated server | UNTESTED |
| Dragon Survival v2.0.74 | Sea/Forest/Cave options, forms, innate actions, food rules | UNTESTED |
| Updated dragon foods | Sea eats raw Aquaculture fish/sushi/kelp; refuses cooked fish/grain; Forest eats raw Pam's produce, sugar and fruit preserves but refuses grains/baked dishes; Cave uses native charring only, not raw-plus-coal shortcut | UNTESTED |
| Vampire Ascension v2 | Ordinary infection is Vampirism-only; drinking Lilith's Blood adds JAWR + Vampirism; JAWR native blood/hunger/spending controls ascended form; both cures remove both states; Vampire Forest counts as out of JAWR sunlight | UNTESTED |
| Vampire HUD/health | Ordinary blood bar stays native; ascended JAWR blood bar displayed without duplicate. Feed/drain/refund/cure not double-charged | UNTESTED |
| Mob alliances (new experimental add-on) | Natural undead should avoid proactively targeting validated vampire/lich players; provoked undead may retaliate for 30 seconds; tagged bosses and owner-commanded minions must retain intended AI; forest-dragon/fae and cave-dragon/demon rules only work for real tagged entities | UNTESTED |
| Monk native pilot | 10-second First Sunset, faction/root Step Assist, mastery ranks 2/3/4, native Open Palm/Unarmored Poise/Fleet Footwork, gear-based reversibility | UNTESTED for current build |
| Tyz Core Magic | Four 99-rank Reservoir/Wellspring/Thrift/Potency skills, four original tabs, no Pufferfish Skills, one SP from operator 1000 XP grant, no native activity XP, no duplicate shared magic effects | UNTESTED for current build |
| Colorful Hearts 10.5.9 | Colored one-row hearts at 20/50/100+, half hearts, absorption, AppleSkin and Vampirism/JAWR overlays | UNTESTED |
| Comforts 9.0.5 | Sleeping bags preserve spawn; hammocks skip day to night, sleep/rest works in new world | UNTESTED |
| Quests | 180 entries in 45 chapters/five groups, revised humorous fixed rewards, one Cooking Pot bowl, starter Spyglass and coin claim once | UNTESTED for current build |
| Quest Shop | Sixth custom Ore Blocks category, all 18 ore offers, currency charged once, no exploit via ore processing and resale | UNTESTED |
| Other regressions | FTB Essentials home/RTP, TreeChop, TrashSlot, More Overlays, inventory sorting, Akashic Tome, material unification/Mahou Catalyst Exchange, ProjectE locks, and persisted QA preferences | UNTESTED |

## Practical order

1. **Startup + baseline:** Import the unstable Prism zip, create a new world, check installed mods before using any commands. Verify Dragon Survival, GeckoLib, Comforts, Colorful Hearts and Tyz. Verify Pufferfish Skills is absent and Pufferfish Attributes/Improvable Skills remain installed. Capture any mixin or KubeJS red messages.
2. **Vampire split:** Use an untreated human and a new ordinary vampire (through Sanguinare Vampiris, not Vampire Fangs). Check only Vampirism for ordinary infection. In a separate backed-up throwaway world turn on explicit `tmm_transformations` experimental flags; consume Lilith's Blood, run `/tmmtransform vampire`, confirm both native flags, independent level progression, exactly one charge per paid ability, JAWR hunger tick, no double healing, both cure pathways, Vampire Forest sun exception, and both HUD variants. No untrusted rollback on valuable saves.
3. **Mob alliance regression:** On a separate disposable world, approach ordinary undead as human, Vampirism-only vampire, ascended vampire and Goety lich. Confirm neutrality only for recognized vampire/lich statuses; hit a mob and confirm retaliation and expiry, while boss targeting and explicitly controlled servants remain functional. Log all unclassified entity IDs from the `TMM_MOB_CENSUS` startup output before concluding the dragon/fey/demon rules work. No result in this step is proven by static tags alone.
4. **Monk:** Complete First Sunset; check Step Assist. Practice native mastery: barehand block (+1), directly credited hostile kill (+4), and each genuinely new food (+8 one time). Check thresholds 20/55/105; buy native Open Palm, Unarmored Poise and Fleet Footwork. Ordinary equipment suppresses eligible effects without erasing purchases; rejoin/death saves must not duplicate rewards. Tyz Monk repeatables remain inactive until valid server purchase gates exist.
5. **Dragons:** Play each species. Test positive and forbidden foods before/after world reload, hunger/saturation, natural and transformed state, native Cave charring, common-tag expansions for Pam's and Farmer's Delight; save any food not behaving as expected with item registry ID.
6. **Tyz universal:** `/skills reload`; verify Reservoir/Wellspring/Thrift/Potency. Ordinary vanilla actions yield **zero Tyz XP**. Use operator-only `/skills xp PLAYER add 1000`, buy one rank, inspect actual Ars/Iron's/Psi/Goety etc. attributes, cast, refund, relog. MXP dispatcher is source-only; do not claim normal magic use awards points.
7. **QoL and quests:** Run a 50+ hearts test, colored absorption/poison/AppleSkin and vampire HUD. Sleep in a bag then die/rejoin to test respawn, use a hammock during day. Check quest menu/180 quests/Spyglass reward/coin and humorous gifts; shop 18 ore offers; two-player and repeated-claim cases. Check inventory, FTB Essentials and previous QoL.
8. **Regression:** Run JEI searches for canonical Neo Vitae dust, Mahou circles, silver variants and ProjectE item locks; verify no loss of recipes or new ungated crafting/currency exploitation. Verify cross-dimension, multiplayer, restarts and client performance.

## Completion rules

- **CI pass is server-only**, not a client/world/gameplay acceptance.
- The immutable integration snapshot must record its exact SHA and downloaded ZIP checksum.
- Do not mark survival-ready until important functional cases pass, including vampirism cure and blood, all three dragon diets, Monk native progression, Tyz purchase/refund, QoL UI, and quest/shop transactions.
- Keep older client-tested rollback snapshots. Do not overwrite a previously used world; restore it from backup after any migration experiment.
