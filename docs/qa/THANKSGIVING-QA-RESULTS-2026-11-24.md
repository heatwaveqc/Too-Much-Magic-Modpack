# Thanksgiving QA results — run 45550879036

Frozen installed SHA: `721ea058294f838b9d7f145af427f5db228d248d`. [Checklist](THANKSGIVING-QA-2026-11-24.md). This is the per-build execution ledger; canonical feature acceptance stays in the feature registry.

Existing CI levels: Integration Preflight / Assembly / Module CI / Server Boot Smoke / Prism QA Package PASS in [45550879036](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/45550879036); rolling publication PASS [45552620361](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/45552620361). Client Launch Smoke and Gameplay Acceptance NOT RUN. No new game process launched while preparing this ledger.

| Case | Status | Evidence or next action |
| --- | --- | --- |
| A01 | PASS | Build 45550879036; read-only publication validation 45552559667 and publisher 45552620361; release digest matches artifact. 251 pins, 4 first-party JARs, root Prism layout, inventory manifest, 36 approved paired NBT previews. Client installed files not yet compared. |
| A02 | PASS | Existing run 45550879036: frozen source preflight, assembly, compile/unit checks, runtime JAR presence, retirement and server smoke PASS. Not an independent recompile or client test. |
| A03 | PASS | Frozen manifest/mods.json and first-party-runtime.json inspected: Sodium 0.8.13, EMI 1.1.24, EMIsemble 1.2.1, Extra Mod Integrations 1.0.3, Multiplayer Spawners 2.0.2, Lootr/Liaison/Twilight Lootr enabled; JEI disabled. Selected runtime tyr_progression, traders_vengeance, tmm_economy, tmm_friendship_garden; no separate MXP or Transformations JAR. |
| A04 | PASS | Frozen ProgressionCommands, MxpMod, NewbieMode, core/monk/hunter path JSON, Economy/Vendetta command roots reviewed. Namespaced IDs use ResourceLocationArgument. No claim registered runtime command works. Monk glide config requires strict Wither milestone; old command failure string still mentions dragon; report inconsistency rather than taking text as mechanic. |
| A05 | BLOCKED | Need fresh installed profile inventory/log copies. Run python scripts/audit_client_inventory.py against instance minecraft directory (packaged expected-mods JSON); compare client inventory and log errors, KubeJS parse, Mixin, recipe decoder, renderer and key spam to Nov 22 results. |
| A06 | BLOCKED | Need B04/B05 exports from exact build. Reconcile actual effective registry against stonecutter/Replication #448/#565, ProjectE gates, enchantment/capture exclusions, food provenance, all-item EMC and potion charm recipes. No prices or gates inferred from recipe files. |
| A07 | PARTIAL | Frozen starter_home_choices.json has 19 choices (generic + 18 custom); source crosswalk #699/#586 and starter tome dossier inspected. Publication verifies 36 paired NBTs. Need effective FTB registry, first-login/claim evidence, actual schematic placement; packaged guide still describes old housing hold, not current quest contract. |
| A08 | PARTIAL | Frozen integration lock has no deferred_pending_runtime entries but pending_runtime_work includes stale historic notes. Explicit Nov 24 #448/#565 survey-dependent work and #656 Lilith deferred; do not claim historical rows are current. Audit each unresolved ready-feature path against actual frozen files before claiming exhaustive inclusion. |
| A09 | PASS | Nov 22 report preserved: 226/226 prior inventory, historical source/command census, Lost Soul surface observation, parchment Rhino failure, invalid key spam. Earlier natural Monk/Hunter initiation and unchanged JAWR baseline accepted/waived as documented; no automatic carryover to changed mechanics. |
| A10 | NOT RUN | Against exact frozen source, run applicable tests/validators for default keybindings, quest/food/EMC, Occultism, native blood, modules and recipe gates in isolated LF checkout. No assembly or boot. Missing tools/JARs report blocked, do not bypass. |
| B01 | NOT RUN | Await client launch and disposable fixture; see checklist. |
| B02 | NOT RUN | Await client launch and disposable fixture; see checklist. |
| B03 | NOT RUN | Await client launch and disposable fixture; see checklist. |
| B04 | NOT RUN | Await client launch and disposable fixture; see checklist. |
| B05 | NOT RUN | Await client launch and disposable fixture; see checklist. |
| B06 | NOT RUN | Await client launch and disposable fixture; see checklist. |
| B07 | NOT RUN | Await client launch and disposable fixture; see checklist. |
| B08 | NOT RUN | Await client launch and disposable fixture; see checklist. |
| B09 | NOT RUN | Await client launch and disposable fixture; see checklist. |
| B10 | NOT RUN | Await client launch and disposable fixture; see checklist. |
| C01 | NOT RUN | P0 — Import, launch, fresh world and logs |
| C02 | NOT RUN | P0 — Sodium + EMI + FTB sidebar |
| C03 | NOT RUN | P0 — New player inventory and Tips & Tricks |
| C04 | NOT RUN | P0 — Quest titles, reward choice and housing |
| C05 | NOT RUN | P0 — Newbie protections and Normie transition |
| C06 | NOT RUN | P0 — Controls, screen and TreeChop regressions |
| C07 | NOT RUN | P0 — EMC and Replication per-player visibility |
| C08 | NOT RUN | P0 — Replica worldgen and effective recipes |
| C09 | NOT RUN | P1 — Universal MXP and Core |
| C10 | NOT RUN | P1 — Monk XP/trials and abilities |
| C11 | NOT RUN | P1 — Monk strict Wither and glide |
| C12 | NOT RUN | P1 — Hunter XP, mutations and specialties |
| C13 | NOT RUN | P1 — Unified food and Dragon Survival |
| C14 | NOT RUN | P1 — Enchanting, potion charms and captures |
| C15 | NOT RUN | P1 — Occultism new recipes and book |
| C16 | NOT RUN | P1 — Magical industry and material/blood bridges |
| C17 | NOT RUN | P1 — ProjectE, quest shop and Bountiful |
| C18 | NOT RUN | P1 — Trader's Vengeance and stock |
| C19 | NOT RUN | P1 — Friendship Garden (multiplayer) |
| C20 | NOT RUN | P1 — Lootr + Multiplayer Spawners (two players) |
| C21 | NOT RUN | P2 — Worldgen, locators, spooky spawns |
| C22 | NOT RUN | P2 — Gateways, enchantment and loot parity |
| C23 | NOT RUN | P2 — Transformation cross-mod acceptance |
| C24 | NOT RUN | P2 — Long-play, multiplayer and persistence |

Append observed evidence with timestamp, case, exact installed SHA, test level, setup, status, actual result, screenshot/log reference and issue follow-up. Do not promote source/CI to gameplay PASS. Never merge historical evidence into this build without verifying scope and dependencies. No private data or full logs.
