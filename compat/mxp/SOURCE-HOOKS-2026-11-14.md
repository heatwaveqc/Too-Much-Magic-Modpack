# MXP source hooks — Ars, Iron's, Botania Revived, Neo Vitae

**2026-11-14; feature branch only.** This is a **source-compiled prototype**, not an installed or game-smoked MXP subsystem. First-defeat MXP and source restrictions are user-approved; exact native Mixin application, balances under Ars 'n Spells 3.3.5, Curios stacks, and persistence need runtime validation. No downloaded mod JARs committed.

## Locked boss rule

- Every player’s **first successful defeat of each registered boss** is **1,000 effective MXP**, without 1.2^P divisor. This is a central `MxpService.FIRST_BOSS_MXP=1000` constant, not an optional value in the boss registry.
- Only **repeat** amounts need registration: `MxpService.registerBossRule(entityId, repeatMXP, fixedRepeat)`. Repeat rewards are divided except explicit fixed-repeat exceptions. **Chaos Guardian fixed repeat 100 MXP remains proposed and unregistered** until its source registry ID and defeat lifecycle are verified.
- An unregistered mob or boss gives **zero** MXP. The original final-blow-only generic observer remains a pilot; multiplayer damage contributions and multi-stage final-victory handlers remain pending.

## Hook matrix

| Source | Hook | Eligible costs | Explicitly ineligible | Baseline/default |
|---|---|---|---|---|
| Ars Nouveau 5.13.5 | `SpellResolver.expendMana` HEAD and RETURN; `ArsCastPayments.commit` RETURN when Ars 'n Spells owns the payment | Player-cast spells with real native mana subtraction, or ANS committed paid mana quote | Previews, fake players, turrets, failed/refunded casts, alternative LP/aura payments, duplicate imported cross-casts | 100 base starting mana from Ars ServerConfig.baseMax |
| Iron's Spells 3.16.3 | `SpellOnCastEvent` constructor captures original nominal mana cost and payer; `AbstractSpell.castSpell` RETURN confirms actual paid pool delta | Spellbook/eligible sword/continuous casts with positive actual payment | Free scrolls, mobs/commands, creative, cost events without real payment | 100 native MAX_MANA attribute default |
| Botania Revived 2.0.2 | `ManaItemHandlerImpl.requestMana` / `requestManaExact`, with `*ForTool` wrappers preserving undiscounted request | Real mana decrease on **mana tablet / mana ring / greater mana ring** physically in the player's main inventory, offhand or equipped Curios; equipment repairs count | World pool/mirror, remote external mana, Ars-supplied mana, simulated checks, transfers or attempted charges to a portable reservoir, unowned storage | 500,000 from native mana tablet / ordinary ring capacities |
| Neo Vitae 1.1.53 | `AnimaEvent.PostSyphon` after actual `IAnima.syphon` drain | Only EV spent from that **online player's UUID-owned EV network**, actual debit >0 | Blood/health substituted for EV, EV addition, charges not drawn from network, offline-owner activity | **5,000 EV** — Tier 0 Novicius/Weak Blood Orb Anima capacity |

### Important semantics

- **Mana efficiency is rewarded**. Credit nominal original spell/tool cost while requiring an actual positive paid resource debit; improved native/Tyz efficiency and regen allow more casts and therefore more MXP.
- One logical cast can touch both Ars and Iron's in Ars 'n Spells; the adapter reuses one transaction identity and debits the authoritative payer pool through the ANS `BridgeManager`. The Ars reservation path is evaluated at committed `ArsCastPayments.commit`, not at a canceled/preview quote. A try/catch in the server handler suppresses any accidental XP on a rejected event. **Exact 3.3.5 ABI must be verified**; source main may not precisely match the pinned jar.
- Botania's portable payment hook does **not** use `requestMana`'s entire granted amount (which can include `PlayerManaSources`, including Ars) as a reward. It samples the particular in-scope ItemStacks, tracks their actual decrease, and prorates the *nominal* cost to the eligible fraction of any mixed payment. A stack inside a backpack is not eligible merely because a mod exposes it via the ManaItemsEvent.
- Neo Vitae's `IAnima.add(ticket, maximum)` receives its capacity from the linked orb. The authoritative `blood_orb_stats.json` and `BloodOrbStats.bootstrap` confirm the Tier 0 Novicius/Weak Blood Orb supplies **5,000 EV of Anima capacity** (its separate fluid reservoir is only 4,000). Our approved fixed MXP normalization baseline is therefore `neovitae_ev_network_initial=5000`, **enabled by default**. The source's `AnimaTicket` carries only a numeric amount, so it does not identify whether a network debit was from a manual sigil, ritual, or automated upkeep. With this hook, a real online-owner EV-network expenditure qualifies regardless of provenance; decide whether unattended activity should be disallowed before a survival release.
- The native intercepts are **optional, soft Mixins**. The mod compiles without redistributing the other mods. They must be verified in a real pack with the intended mod IDs/version pins; a successful Gradle build alone is not proof they attach.

## Configuration and tests

Configuration: on first run, NeoForge generates `config/tmm_mxp-common.toml` with source_baselines: Ars 100, Iron's 100, Botania portable 500000, Neo EV network 5000. These values are fixed references and never change with the player's progression.

1. Compile and run JUnit: `cd compat/mxp && gradle --no-daemon clean build`. Validate JAR contains the mixin metadata and mixin classes. The dedicated server and graphical client are separately required.
2. Test canonical `/tmmmxp status` before and after first and repeat **approved registered boss** defeats; exactly 1,000 effective MXP first even at P=30, repeat divided (unless fixed repeat). No reward for an unregistered boss.
3. With Ars' native mana path and Ars 'n Spells ISS_PRIMARY, cast normal, cross-cast, failed, turret and creative spells. Compare actual mana debit to awarded MXP and test Thrift 0/50/99, cross-payment and failed/refunded casts. One logical payment must produce at most one award.
4. Iron's normal spellbook, continuous channel pulse, swords with configured mana cost, scroll, recast and zero-cost spell. Confirm only actual paid invocations award and original mana cost is used.
5. Botania carrying tablet/ring, equipped Curios ring, offhand tablet, worn-item repair and partially drained mixed-source request: count only observed eligible portable debit. Test an item in a backpack (zero), a mana mirror linked to a pool (zero), remote pool transfer (zero), simulated request (zero), another tablet charging (zero), creative tablet (zero), Ars-financed Botania tool (Botania-side zero).
6. Neo Vitae: verify the default Tier 0 reference is 5000 in test TOML; trigger network-funded sigil/ritual use; verify actual PostSyphon amount credited and only to the online owner. Test EV additions (zero), health-paid deficit (only actual network debit), offline-owner machine operations (zero), and network upkeep while owner online (currently counted).
7. Save and reload; verify P, fractional pending XP and boss/ritual first IDs survive. Test Tyz XP source restrictions, no vanilla XP conversion, no client-side duplication.

## Review / promotion gates

- **Static build:** run the existing `.github/workflows/mxp-core.yml` Gradle and JUnit checks plus test the included mixin metadata in the generated JAR.
- **Actual compatibility:** use pinned Ars Nouveau 5.13.5, Iron's 3.16.3, Ars 'n Spells **3.3.5**, Botania Revived **2.0.2**, Neo Vitae **1.1.53**, Tyz's Skills **6.4.2** and Java 21 in a disposable Minecraft 1.21.1 NeoForge 21.1.265 test build.
- Do **not** mark source integration `game-tested` until tested in a dedicated server and Prism client with logs showing no failed Mixin targets; an optional Mixin can compile but fail to target a changed method.
- Do **not** include the MXP module in the next full integrated test build without a separate decision and a clean boot. The currently working Prism test snapshot must stay immutable.

### Source references

- Ars: https://github.com/baileyholl/Ars-Nouveau/blob/main/src/main/java/com/hollingsworth/arsnouveau/api/spell/SpellResolver.java
- ANS: https://github.com/otectus/ars-n-spells/blob/main/src/main/java/com/otectus/arsnspells/casting/ArsCastPayments.java
- Iron: https://github.com/iron431/irons-spells-n-spellbooks/blob/1.21/src/main/java/io/redspace/ironsspellbooks/api/spells/AbstractSpell.java
- Botania: https://github.com/Velron-Devs/Botania-Revived/blob/Neo/1.21.1/src/main/java/velrondevs/botania/common/impl/mana/ManaItemHandlerImpl.java
- Neo: https://github.com/breakinblocks/NeoVitae/blob/1.21.1/src/main/java/com/breakinblocks/neovitae/api/event/AnimaEvent.java
