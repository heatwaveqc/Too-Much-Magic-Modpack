# Personal enchanting filter — shared Progression engine foundation (#328)

**Status:** isolated **source implementation in progress** in `feature/progression-personal-enchant-filter-328-20261121`; not a player-usable finished Improvable Skills ability and not gameplay-tested. Parent issue #328 includes a separate portable 32-slot shelf/candle undertaking; this PR does **not** claim that subsystem is implemented.

## Owner-locked behavior
- Craftable Improvable Skills *ability* called Arcane Discrimination (internal proposed ID `tyr_progression:arcane_discrimination`), gating a **personal six-slot escrow** for *real single-enchantment enchanted books*.
- Each single-enchantment book supplies one excluded enchantment **type** for **native random candidate selection only**. Duplicate types have no additional effect; no enchantment is deleted or mutated. Native original RNG ordering, native exclusion/curse/treasure/compatibility tags and all resource costs are unchanged.
- No ability unlock means native original random behavior, regardless of previously escrowed books. Never veto deterministic/crafted spells, stored enchantments, actual anvil application, Botania, Pastel, Ars Nouveau, etc.
- Books preserve every component; deposits consume one exact actual source book, withdrawals return its exact saved stack. Stored books follow player across death/relog; locking/disabling ability must **not delete escrow** or grant extra book/item.
- Implement preview and commit using the same server-owner-captured predicate and revalidate on commit. If all candidates are removed, no output and no XP/other resource charges.

## This implementation slice (source-ready after CI)
- [x] `foundation/PersonalEnchantFilterPolicy`: pure Java six-position duplicate-insensitive exclusion and native-ordered candidate filtering, with JUnit tests for locked/empty, duplicate books, one/multiple removals, all removed, and malformed position count.
- [x] `state/PersonalEnchantFilterState`: immutable, defensive-copied six-slot real `ItemStack` escrow with exact `ItemStack.OPTIONAL_CODEC` serialization and validation of one *stored* enchantment on `Items.ENCHANTED_BOOK`. Unknown or stale books do not add an exclusion but remain refundable. Never copies enchantments into arbitrary items or overwrites native registry.
- [x] `ProgressionAttachments.PERSONAL_ENCHANT_FILTER`: native server-owned serializable/copy-on-death attachment. `PersonalEnchantFilterService` copies the same state on non-death clones, enforces server-only deposit/withdraw and no-withdraw-when-inventory-full, and exposes `removeExcludedRandomCandidates(ServerPlayer,List<Holder<Enchantment>>)` as **one shared API**.
- [x] `PersonalEnchantFilterAbilityGate`: fail-closed reflective lookup of **native** Improvable Skills `PlayerDataManager.getDataFor(player)`, `ImprovableSkills.ABILITIES` and `PlayerSkillData.hasAbility`. No duplicate Progression-owned unlock bit. **Until the actual ability is registered, this correctly returns false and no gameplay is changed**.
- [x] No new global enchant blacklist, no static cross-player filtered sets, no GUI, no enchantment-altering mixin, no new active skill; source isolated to `compat/progression/**`.

## Code-ready follow-up / implementation handoff

- [ ] **Register native ability** with exact pinned Improvable Skills 21.0.2 `PlayerAbilityBase` API + HammerLib registration, with actual craftable ability parchment/scroll recipe and screen-open action. Proposed name/recipe text is editorial; do not assume a free unlock. Must verify pinned published JAR compatibility and native data lifecycle; failing optional API must keep all filters inert.
- [ ] **Open protected server-authoritative six-slot GUI** through registered native ability click, or equivalent authenticated menu. Deposit/withdraw must call this engine's **actual inventory-consuming** service, never directly edit stored attachments. Add client-synced view without write access except validated requests. Ensure atomic server inventory transfer, no dupe on shift-click/cursor, preservation of exact book components and no withdraw loss at inventory-full.
- [ ] **Apothic/vanilla/Raven/portable random roll adapter**: source pin 1.6.4 and exact menu candidate selection seam. Filter only native candidate list after its other eligibility constraints and before native selection at both preview and commit, without changing World Tier cap, Eterna/Quanta/Arcana, enchanting costs/infusion, or permanent enchanted books.
- [ ] **Eidolon Soul Enchanter** [#407](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/407): source branch `alexthw.eidolon_repraised.gui.SoulEnchanterContainer`; capture `Inventory.player` in constructor and filter private `getEnchantmentList(RegistryAccess, ItemStack, int)` candidate `valid` after native restrictions but before its `rand.nextInt(valid.size())`. Both slotsChanged and clickMenuButton use it. Ensure invalidation on filter update and no soul-shard/XP charge if empty. Exact pinned 0.5.0.8 binary/MixinExtras verification before adding hook. Not a separate item-storage system.
- [ ] **Portable shelf/candle state** is an additional #328 capability, **not** bundled into six-book filter state: native 32-slot virtual shelves, accurate colored candle `ceil(countPerType/4)` positions with true `CANDLES=1..4` Arcana (no fabricated Eterna), and real World Tier Max Eterna limits. Coordinate with other Progression feature PRs and lock sources before editing menus.

## Design and engineering blockers
- **No unresolved owner gameplay decision** on six slots, unique single-enchant books, random-only filtering, persistence, refundable items or two-player behavior. Exact scroll recipe/name/icon and any additional UI placement are editorial/recipe choices; native ABI and adapter seams are engineering research.
- **Dependency:** #407 must not implement until shared ability registration + escrow menu + this API are source-ready. No direct writes to `dev/progression` or `test/full-integration`; designated writer merges reviewed PR to the former, integration writer freezes donor and promotes to latter.

## Test queue
- **A (source):** pure policy JUnit pass, native NeoForge compile, external source ABI inspection; complete once CI reports green.
- [ ] **B (game):** registered ability / parchment recipe / server inventory transaction and attachment persistence; skill unlocked/locked; loaded player and absent optional mod; no fake client unlock, same state through death/relog; true two-player isolation.
- [ ] **C (manual):** 0/1/6 stored books, unique IDs vs duplicates, rejecting multi-enchant books, malformed/unsupported book still refundable, full inventory withdrawals, exact item components, shift/cursor duplication attempts, preview updates with GUI open, all candidates excluded results in no resource debit.
- [ ] **C (manual):** physical/portable Apothic/Raven random-roll parity, native treasure/curse/incompatibility, Eidolon Soul Enchanter same-owner/current restrictions/seeded reroll/XP/shards, deliberate anvil/recipes/books unaffected.
- [ ] **C (separate #328):** 32 position shelf, candle type/color grouping 1/4/5/8 + mixed, real block-state stats and live datapack reload, tier cap, no fabricated Eterna or nonrefundable source.

**QA authorization:** no Integration Assembly, full NeoForge boot, Prism package, client launch, or gameplay pass was requested/performed by this source slice.
