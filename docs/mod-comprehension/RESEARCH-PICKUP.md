# Research pickup — local JAR continuation

## Current workstream

- Branch: `research/mod-comprehension-local-jars-20261116`; base `cb8e2dcab59cc625500b3ca0329df1511823610b`.
- User requests completion of major magic-mod research Markdown and regular recovery commits. Research only; preserve QA edits in the separate `qa-source` checkout.
- Start with `AGENTS.md`, `MOD-COMPREHENSION.md`, `WORK-CHAT-INSTRUCTIONS.md`, `INDEX.md`, templates and the quest walkthrough standard. Keep partial profiles `researching`; artifact inspection is not gameplay testing.
- [Artifact audit](LOCAL-ARTIFACT-AUDIT-2026-11-16.md): 156/159 current baseline pins match local files, all 159 metafiles match the index. Missing baseline JARs are Default Options, FTB Filter System and Keysets; all selected Goety research artifacts match. The prior QA checkout had 161/161 matches; those numbers describe different snapshots.

## Active slice and exact next steps

1. Completed this slice: Goety Cataclysm Fabricator and parent training/altar bytecode audit, 61-recipe appendix, 11 conversion routes, all 47 Cataclysm recipe item/block IDs matched registration literals, exact scaffold IDs, and targeted boundary tests. Key corrections: charged ritual advances are 20 game ticks apart; nominal cost is rate × duration; obstructed Fabricator attempts spend a queue entry. Profile remains researching because focus effects, research/structure gates, servant care and full acquisition paths are incomplete.
2. Next: map all focus cast effects/costs, parent research unlock/Forge setup, servant care/death recovery and boss-drop acquisition. Recipe inventory and exact current scaffold reconciliation are done; do not repeat them. The appendix is factual data coverage, not proof of complete progression.
3. Update index and this note, commit each coherent finding, then push and open a reviewable research PR to `test/full-integration`. Never merge into the shared integration branch yourself.
4. Next priorities: base Goety remaining ritual/focus/structure paths; Ars/Occultism exact artifact gaps; Mahou Tsukai exact artifact mechanics. Read existing profiles before duplicating work.

## Inspection method and local-only scratch

The local artifact directory is documented in the audit. `javap.exe` is available in Prism's `java/java-runtime-delta/bin/`. CFR 0.152 from its official site was used only for selected local classes; transient output lives under the Windows user's `AppData/Local/Temp/tmm-magic-research-20261116`. Do not stage decompiled Java, classes, JARs, assets or caches. Recover every claim by inspecting exact hash-matched artifacts; scratch paths are convenience only.

No full smoke, assembly, client launch, gameplay or new quest/config/runtime implementation is authorized by this research task. Workflow gates must be inspected before pushing; only quick CI is appropriate.

## Validation and continuation receipt

- Recovery commits: `9425b041` (local access audit/pickup) and `8efd4af1` (addon recipe/operating audit); subsequent commit corrects base-Goety ritual totals and cleans historical blocker text.
- `python scripts/validate.py`: PASS after canonical LF text normalization in the isolated research checkout (159 active pins, 294 indexed files, 181 desired selections).
- `python scripts/validate_integration_sources.py`: read-only PASS at `8efd4af1`, after fetching the 13 missing frozen donor commits. No assembly or smoke ran. Rerun on the final clean research head.
- Windows Git initially materialized CRLF text outside explicit `eol=lf` patterns. Only isolated-worktree text bytes were normalized and the unchanged index refreshed; no runtime/config changes entered the research commits. The unrelated `qa-source` edits were preserved.
- Local Markdown check found one pre-existing template placeholder in INDEX (`mods/<manifest-id>.md`); exclude that deliberate placeholder from literal link validation. All 61 recipe keys occur exactly once in the appendix.
- Base Goety Forbidden Fragment/Piece acquisition was already expanded on the current integration base. Do not repeat that work or keep the older claim that no sourcing is mapped. Its runtime loot acceptance remains open.

## Published handoff

- Review PR: [#265](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/265), targeting `test/full-integration`; not merged by this research chat.
- Research commit `544a40e7` passed the metadata validator, read-only Integration Preflight (zero baseline replacements), docs-only scope, local Markdown link check and 61-row recipe completeness check. No client/gameplay acceptance is implied.
- Base advanced concurrently to `45f2b6429130a521e4ddb5ca165bff6eb62adfcf`, changing only `manifest/integration-sources.json`; this branch does not alter that file. Review against the current shared baseline and rerun preflight before any eventual merge.
- Resume this branch/worktree for the next slice, then keep commits small and update this file with exact completed and remaining coverage. Do not promote either Goety profile to source-complete yet.

## Continuing after the first PR slice

- User explicitly requested continuing until context exhaustion, completion or a serious blocker. Keep this same PR/branch; do not stop after one small slice by default.
- Completed next slice: `mods/goety-ritual-gates.md` maps exact Forge setup; nine addon-used ritual/environment families; Grave Soil depletion callback; research capability/use behavior; Bygone/Buried/Warred/Terminus recipes/acquisition and prerequisites; blueprint reward-loss boundary. Linked from both parent/addon profiles and index.
- Next active work: all 28 registered addon focuses, default costs/cast/cooldown, meaningful staff modifiers and servant summon effects. Selected classes and `GCSpellConfig`, `GCItems` are decompiled only in local scratch. `EMPSpell` is present as a class but not among the 28 `GCItems` focus registrations; do not claim an obtainable EMP focus from class presence alone.
- Parent research evidence is exact JAR: `ResearchScroll`, `ExtraScroll`, `ResearchList`, `SEHelper`, `DarkAltarBlockEntity.activate`; ritual type classes delegate through `RitualRequirements`. `ModLootInject` is subscribed and points current singular `loot_table` resources at named vanilla tables. Old plural loot resources need separate reachability review.

- Further slice: added `interactions/goety--goety-cataclysm--staged-cast-payment.md` with exact parent start/use/finish/release dispatch and the three addon stage/cooldown handlers. Payment-before-effect cannot be assumed: these classes emit from useSpell and have cooldown-only release handlers. Not gameplay tested. Next finish the 28-focus operation/default table, then servant care/recovery and remaining native gates.

- Completed focus slice: `mods/goety-cataclysm-focuses.md` has all28 GCItems registrations, class/default cost/cast/cooldown/enchantments and producer behavior; temporary summon30–119 seconds×Duration, cap/recall/Summon Down, Ashen Breath20-execution charging and cap comment mismatch. Underlying hit/filter formulas remain partial.
- Extended staged-payment report to fourth path Abyssal Beam. Important distinction: `AbyssMark.tick` discards a mark if player stops casting, so immediate cancellation is guarded; mark nevertheless emits portals after configured80 lifespan while full cast is90. `AbyssBlastPortal` has independent life/emission without cast-state check. Test post-emission/pre-completion; do not claim every early cancel produces damage.
- Next: servant care/healing/death/recovery, full encounter/drop acquisition and remaining base ritual families. The full spell effect delegation audit is large; prioritize consequential HP damage and world/ally filtering before any source-complete label.

- Completed care/recovery slice: `mods/goety-cataclysm-servants.md`. Mapped heal families, last-stack post-shrink loss/reduced-heal boundary, revival spirits/cores, parent serialized entity payload/FlyingItem delivery, Prowler3×3×3 reconstruction, juvenile Remnant skull, Ender/NM head patterns and pre-spawn destructive clearing. Explicit gaps remain for unlisted inherited care and Draugr soulJar invocation. Supporting generated target ItemStack source hash recorded; no runtime fixes.
- Next highest-value closure: remaining six base ritual types (the gate appendix currently covers nine addon-used types); parent conversion `MobUtil.convertTo` target ownership/equipment order; initial construct recognizers. Then follow consequential delegated HP-damage and resource-acquisition gaps, or move to another major mod's exact JAR profile once a coherent Goety slice is saved.

- Completed next closure: gate appendix now covers all15 registered native ritual type predicates (not just nine), candle-count/extinguish, enchant-power integer truncation, Apostle Soul Fire depletion, and shared `MobUtil.convertTo` semantics checked with javap when CFR was unstructured. General target selection is by nearby Mob tag, not owner; tamed result assigned to caster. Gear copied/cleared before add; add boolean ignored, source discarded. Concrete target equipment hooks and actual pack protections remain open.
- Next work should avoid repeating these inventories. Prioritize source-reachable/current-namespace acquisition and delegated damage/ally/grief filtering; source-complete still requires those, complete base Goety recipe/brew/utility branches and guide coverage. Exact JAR access is not blocked.

- Smaller framework closure: `mods/summoning-rituals.md` directly audited exact SHA1-pinned3.14.2 binary. No bundled recipes;31,021 recipe JSON resources across163 then-local JARs had no namespace reference; selected frozen donor paths empty. Corrected false historical two-script roster, item consumption (completion, not start), item-handler extraction (denied), commands (server source), conditions checked at start, inventory-only save state. Exact informational quest IDs/rewards reconciled. Marked source-complete strictly for unused framework baseline; adding a real acquisition/ritual path requires reopening coverage. No test recipe or runtime changes.

- Mahou Tsukai next slice completed: `mods/mahou-tsukai-pinned-onboarding.md` and main profile updates. Exact SHA512 verified;33 crafting resources;53 internal recipes/53 guide matches;35 cloth-required;19 advancement declarations. Starter grid, four-Diamond combined tool vs cheap Hammer, unchanged crafting tool remainders, Dagger2 damage request/Bleeding300ticks, draw-packet placement/failed Bleeding opportunity and counted-unordered recipe equality traced. Main remains researching; mana/circuit/effect/special-weapon chains and18 custom emitters still need work.
- Mahou scratch classes under `decompiled/stepsword/mahoutsukai` in same local temp. Next efficient action: trace `PlayerManaManager`/IMahou/mana-circuit and core Mahoujin lifecycle, then individual high-value exchange/weapon progression. Do not redo the53-row registry or treat cloth-required=false as proof every spell is scroll-capable.

- Completed Mahou mana/circuit slice: `mods/mahou-tsukai-mana-circuits.md`. Payment branch order verified against javap; normal full payment personal/gems/circuits, insufficient cases depend on flags; personal-only growth including donation, range strict X/Z with no Y, owner/shared access and owner-unchecked discrete toggle, redstone inversion, NBT vs implicit components and raw builder defaults recorded. Regen field is deliberately not called a per-game-tick rate. Next trace regen caller cadence/leyline/Fae/Geas/hunger, gem lifecycle and Magitech interfaces, then spell-specific payment/effects and special-weapon acquisition. Most major-mod research remains unfinished; source access is available.

## Context checkpoint after Mahou mana slice

- Latest research content commit: `0065741a4d87e10d04aac7b35f942d62dbd64505`; pushed to the existing research branch/PR265.
- At that head: `python scripts/validate.py` PASS (159 active pins/294 indexed files/181 desired entries); `python scripts/validate_integration_sources.py` read-only PASS, zero baseline replacements; full branch whitespace check and docs-only/local-link checks PASS (excluding deliberate INDEX template placeholder). No runtime or gameplay tests. GitHub metadata checks were still running when inspected; server jobs skipped.
- Context checkpoint only, not completion of the overall research phase. Resume the same worktree and exact next steps above; do not repeat completed inventories or promote partial profiles. Summoning Rituals source-complete means unused-framework baseline only.

- Resumed slice: mana/circuit appendix now traces100-tick regeneration caller, full arithmetic/casts/default examples, sleep guard, coordinate ley score/dimension filter, Fae-spawn side effect/current loot source, exact circuit/gem recipes, consumed Attuner and gem charging from circuits. Magitech is ordinary circuit subclass with capacity/facing changes; no FE capability established. Next inspect spell-specific lifecycle/effect/payment and major weapon acquisition; Geas/William/Death Collection and Fae population/complete drops still partial. Latest fetched integration still differs only in source registry; pins unchanged.

- Completed Caliburn/Morgan acquisition slice: `mods/mahou-tsukai-caliburn-morgan.md`. Native lake11 paid growths/330 cost; >150 connected-water guard; enchanted Player-owned sword/5000 queued payment;83-tier Nether Star cap list, pre-payment offering discard/trigger; output projectile pickup/despawn; default Fun gate off; owned tamed wolf conversion/no extra drain; AbstractVillager growth400/repair30/adult0.2/baby1, dual-hands secret. Important open delegates: terrain tryChangeBlockState, FakeSkeleton/attribute simulation, specialTarget/SmiteEntity, MorganBall/Rage, packet/protection/cancellation. Next audit remaining exchange loops or these combat delegates; do not call whole Mahou source-complete.

- Completed exchange/life-drain tile slice: `mods/mahou-tsukai-exchange-life-drain.md`. Catalyst7 uniform types/50 perunit/ceil(N/2) original-batch loop verified javap, potential output reroll. Durability below-NORTH handler plus separate above callbacks, taxbrackets, cap counts iterations notmana, postdamagecheck/break gap. DrainLife5/20tick, targetcube10/immunities/name, damage2+fallback3.6 and reward regardless second damage boolean; reward health0.5 thenfood1 thenmana1;10-circle cap/payment-beforegate. Updated lake block-change helper with explicit immutable blocks/BreakEvent fakeplayer. Next high-value audit Contract/Geas lifecycle and DrainLife scroll, or other major weapon/artifact branches (Clarent/Emrys/Replica/William/Kodoku) before whole-profile source completeness.
