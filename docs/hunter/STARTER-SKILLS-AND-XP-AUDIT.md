# Hunter starter abilities and progression-XP audit

**Status:** development slice for standalone `tmm_progression`; JSON/source implementation, **not** an activated modpack or an in-world gameplay pass. **Parent:** Hunter Trial of Venom branch (PR #136), itself based on shared chassis PR #127. **Platform:** Minecraft 1.21.1 / NeoForge 21.1.x / Java 21.

## Implemented initial abilities

| Skill | Configured effect | Current cap | Notes |
| --- | --- | --- | --- |
| Rooted | `minecraft:knockback_resistance` +0.10 per rank | Rank 5 (+0.50) | Uses existing transient attribute reconciliation; combines with other sources, subject to attribute bounds |
| Evasion — Undead | Negates eligible incoming hits from entity types in `#tmm_progression:undead` | Rank 5 (20%) | Server-side `LivingIncomingDamageEvent`; source-owner must be a living tagged quarry; environmental damage does not trigger |
| Evasion — Raiders | Same, against `#tmm_progression:raiders` | Rank 5 (20%) | Overlapping tag matches are summed and capped at 75%; never claimed as invulnerable |
| Sword / Axe / Scythe specialist | +0.5 direct melee damage per rank when firing weapon matches its tag | Rank 5 (+2.5) | Scythe tag is a compatibility seam, not populated with claimed modded item IDs |
| Bow / Crossbow specialist | +0.5 arrow damage per rank from the firing weapon | Rank 5 (+2.5) | Uses existing source-arrow weapon capture; non-arrow custom projectiles are not assumed supported |
| Fast Hands — Bow / Crossbow | +0.20 extra charging ticks per rank | Rank 5 (~2x at rank 5) | Server-side `LivingEntityUseItemEvent.Tick`; only instances of vanilla BowItem/CrossbowItem, exact configured weapon matcher; real client animation/release compatibility still untested |

New pilot skills cost 1 Hunter point per rank, with no rising step cost. Existing pilot skills and trial initiation stay unchanged; no new endgame ceiling has been approved. Paths remain mutually exclusive (Hunter vs Monk). The default generic skill schema can express all new nodes without a Hunter-only hard-coded path switch.

**Known integration limits:** modded weapons that do not subclass vanilla BowItem/CrossbowItem or emit vanilla arrows need adapters; scythe registries/tags need reconciliation against pinned pack mods; no creature taxonomy integration beyond the two existing vanilla-only pilot tags. No GUI or client-server full skill state sync exists yet. Gameplay QA must cover fast-draw desync, crossbow multishot, enchant/affix damage stacking, packet authority, Evasion PvP exclusion, and rank removal/respec.

## Current independent Hunter XP model (audit only — rates UNCHANGED)

- Separate from vanilla enchantment XP and Tyz MXP. Server-owned ledger stores `xp`, rank, spendable points and milestones per path; held across deaths/rejoins.
- Trial of Venom starts Hunter at rank 1 with 1 point and 0 XP. Rank 20 requires 28,000 cumulative XP.
- Repeated kill XP is **only** configured for tagged undead (5 XP) and tagged raiders (6 XP). Neither environmental kills nor untargeted hostile mobs receive a baseline activity award.
- One-time `undead_studies` and `raider_studies`: 20 qualifying kills each, 100 XP and 2 points each. `trial_of_venom` records 0 XP / 0 points.
- 20 ranks at 1 point per rank plus 4 available study points = **24 points total** if both study milestones complete; 20 is from starting rank plus 19 rank-ups. Any single existing 10-rank skill at cost 1,2,...,10 requires **55 points** and thus cannot be maxed on the current pilot cap. This is a genuine mismatch, not an XP-rate issue alone.
- Even with both 100-XP milestones, rank 20 takes ~5,560 undead kills at 5 XP each, or ~4,634 raider kills at 6 XP each, absent other awards. Difficulty/rarity is not reflected. For Hunter players who fight mainly outside the two defined tags, rate is zero.

### Reward ownership and exploit surface

Current `LivingDeathEvent` checks `DamageSource.getEntity() instanceof ServerPlayer`: player-owned arrows normally qualify, but minion/ritual/summon ownership needs trusted bridges. The two kill activities have `cooldown_ticks: 0` and no spawn-origin or victim-reward provenance policy; grinders/low-risk farm kills can dominate XP. `BlockDropsEvent` and food discoveries are supported core signals but Hunter does not currently award them. `advancement` and `external` signals exist for future unique hunt milestones. XP activity cooldowns are in-memory, not persisted. No arbitrary client XP grant is allowed.

**Before revising the curve**, agree on the intended Hunter time-to-cap and source mix. Recommended order:
1. Integrate the validated pinned creature taxonomy as server-side tags for eligible quarry, with neutral/friendly/pet/summon/boss safeguards (not a blanket `kill:*`).
2. Add one-time first-species hunts, dangerous hunt/boss accomplishments, and distinct discovery/preparation milestones; keep repeat kills useful but not dominant.
3. Add verified kill provenance / anti-farm protection, summon attribution, death/restart edge tests, plus per-event idempotence.
4. Fix the point economy (either more points or bounded per-skill pricing) and only then tune rank thresholds against a realistic encounter sample.
5. Compare leveling paths for sword, bow, spell-only, summon-focused and Hunter-without-loot builds, and test full respec persistence.

**Scope boundary:** This PR deliberately implements starter gameplay skills while leaving XP quantities as-is for the next balancing discussion. No claim of live survival, modpack boot or client acceptance.
