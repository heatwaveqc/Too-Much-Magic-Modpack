# Neo Vitae × JAWR — vampire blood sources

> **Knowledge-base report only.** The bridge exists in both pinned mods and is source-supported; its operation at TMM's exact pair of versions has not been client-tested.

| Field | Value |
| --- | --- |
| Participant manifest IDs / versions | `neovitae` 1.1.53 + `witchery-remake` (actual mod ID `witchery`) 0.5.39 |
| Source profiles | [Neo Vitae](../mods/neovitae.md), [JAWR](../mods/witchery-remake.md) |
| Pack integration base | `test/full-integration` after PR #101; JAWR profile branch from current base |
| Interaction status | source-supported |
| Mechanism class | Native compatibility code shipped in JAWR; conditional Guidebook entry and ritual recipe |
| Recommended game stage | Mid to late; requires advanced vampire progression and Neo Vitae ingredients |
| Reviewed | 2026-11-15 |

## Player-facing payoff

JAWR vampires can draw on Neo Vitae's life-magic ecosystem instead of relying only on JAWR's ordinary feeding routes. JAWR's release history describes two distinct connections: vampire feeding from a Neo Vitae Blood Orb/Ara Vitae, and a ritual-empowered Bloodstone Pendant that replenishes JAWR blood from the wearer's Anima. The pendant can make an Anima network materially useful to a JAWR vampire, but the sources do not establish exact blood-per-Anima rates or that both routes operate together in the pinned versions.

## End-to-end mechanism

| Step | Mod and system | Actual type / registry hook / conditions | Consumed, transferred or transformed? | Evidence |
| --- | --- | --- | --- | --- |
| 1 | Neo Vitae stores personal Anima and altar EV | Anima is a player-attached magical resource associated with Neo Vitae's orbs and tools; EV is the separate fluid/altar resource | Resource storage/draw behavior is Neo Vitae-specific; don't equate Anima with EV or generic blood | Neo Vitae source profile: `2744123`, API and guide entries |
| 2 | JAWR detects Neo Vitae | JAWR 0.5.39 includes `dev/sterner/witchery/integration/neovitae/NeoVitaeCompat.class`; `witchery:rite_of_bloodbinding` guide/recipe unlocks only when `neovitae` is loaded | Optional bridge; absent mod hides the specific Guidebook entry and its recipe path | Exact pinned JAWR JAR; guide entry `ritual/rite_of_bloodbinding` |
| 3 | Empowered Bloodstone Pendant | JAWR guide says the Rite empowers a normal Bloodstone Pendant so it taps the wearer's Anima. Inputs: normal Pendant, Drop of Luck, Weak Blood Shard, Blood Pearl, Necromantic Stone and Nether Star | Ritual consumes recipe ingredients and changes pendant to empowered item/state; exact registry outputs/cost need an in-world test | JAWR guide page and `witchery:ritual/rite_of_bloodbinding` recipe; Upstream 0.5.12 changelog |
| 4 | Vampire feeding | Official JAWR changelog says 0.5.11 changed integration so vampires drink from an Orb of Vitae and Ara Vitae instead of directly from Anima; 0.5.10 described an Anima-fed drink ability while holding a blood orb | Likely source or operation-specific transfer; exact quantities, costs and precedence not established by the release notes | Official JAWR changelog; current 0.5.39 compat class exists |

## Existing support vs work required

- **Natively supported and present?** JAWR contains the optional Neo Vitae compatibility class, bloodstone ritual data and guide entries; both mods are pinned in TMM.
- **Pack implementation already exists?** No custom KubeJS/Java integration is indicated or needed for the native connection. `mods/neovitae.pw.toml` and `mods/just-another-witchery-remake.pw.toml` include both locked artifacts.
- **Smallest remaining work:** disposable client test on Neo Vitae 1.1.53 + JAWR 0.5.39 for both the empowered pendant and vampire Orb/altar feeding routes.
- **What is unestablished:** the exact API calls, resource formula, maximum/cooldown, Anima-vs-EV distinction in the live HUD, failure behavior when Anima is empty, and compatibility with the current JAWR-only-vampire target after removal of TeamLapen Vampirism.

## Minimum practical setup

1. Progress JAWR vampire features to the bloodline stage that supports the Bloodstone Pendant/feeding ability.
2. Progress Neo Vitae until the Weak Blood Shard, Blood Pearl and Necromantic Stone ingredients are available; use the JAWR Guidebook → Rituals → Rite of Bloodbinding page.
3. Empower a Bloodstone Pendant and wear it while observing JAWR blood and Neo Vitae Anima. Separately test Orb of Vitae/Ara Vitae feeding.
4. Compare resource change, operation duration and behavior at empty, partial and full Anima, with an unequipped pendant and without Neo Vitae as boundary controls.

**Cost and power comparison:** Exact altar power and resource amounts were not established in this research pass. Do not infer them from the 500-power bug in unrelated `cauldron_crafting` recipes.

## Balance, safety and compatibility

- **Progression locks and item-gate leakage:** Weak Blood Shards and late-game ingredients gate the pendant path; confirm no lower-tier crafting/loot bypass.
- **Resource loops or duplication:** track JAWR blood, Neo Vitae Anima, Orb of Vitae contents and Ara Vitae EV as separate meters. Test whether vampire feeding drains the intended source once.
- **Entity/world performance:** no extra entities or worldgen are identified for this bridge.
- **Multiplayer, teams, dimensions and persistence:** test rejoin, dimension change, death/respawn, wearer ownership and server/client HUD synchronization.
- **Exclusion risks:** this is an existing native bridge. It does not require restoring TeamLapen Vampirism or adding original Blood Magic.
- **Version changes:** source build for Neo Vitae 1.1.53 declares Occultism 1.224.3 compile-only (not relevant to blood bridge); JAWR 0.5.39 was pinned. Recheck after either mod updates.

## Quest / Tips & Tricks opportunity

If this path remains selected, a later optional quest can teach the Rite of Bloodbinding, identify the six ingredients, show the Guidebook ritual layout, and ask the player to observe blood and Anima changes while wearing the pendant. A crafted pendant alone should not count as verifying blood transfer. The existing JAWR chapter is only an optional checkmark and is unchanged.

## Verification plan

| Test ID | Positive or boundary case with disposable-world steps | Expected observable value/behavior | Observed status |
| --- | --- | --- | --- |
| NEO-JAWR-01 positive | Empower and wear pendant with partial Anima; use documented vampire feeding behavior | JAWR blood rises and Anima changes by a stable amount | not run |
| NEO-JAWR-02 boundary | Repeat with no pendant, unequipped pendant, empty Anima and Neo Vitae absent | No unauthorized drain/refill; optional ritual/entry gated as specified | not run |
| NEO-JAWR-03 source-route | Feed through Orb of Vitae/Ara Vitae separately with normal/empowered pendant | Confirm distinct source path, values and no double-consumption | not run |
| NEO-JAWR-04 persistence | Rejoin, die/respawn and change dimensions before/after blood draw | Blood and Anima state plus HUD synchronize and persist correctly | not run |

## Evidence and open questions

| Claim | Primary source / pinned artifact | What verified / unknown |
| --- | --- | --- |
| JAWR bridge exists | Exact JAWR JAR SHA-512 in [profile](../mods/witchery-remake.md); `NeoVitaeCompat.class` | Class presence and guide/recipe; behavior not decompiled or played |
| Rite contract | Exact JAWR JAR: `data/witchery/modonomicon/books/guidebook/entries/ritual/rite_of_bloodbinding.json`, `data/witchery/recipe/ritual/rite_of_bloodbinding.json` | Conditional page and ingredients; verify outputs/cost in client |
| Compatibility history | https://modrinth.com/mod/just-another-witchery-remake/changelog | 0.5.10–0.5.13 author-reported changes; does not prove current runtime |
| Neo Vitae identity and resource | [profile](../mods/neovitae.md), exact TMM pin and version-aligned source commit `2744123bb6e4f72c602178b23384be41956eef23` | Resource types and Anima distinction; no JAWR endpoint test |

- [ ] Test the exact pinned client pair; inspect blood/Anima deltas and synchronization.
- [ ] Determine whether the Bloodstone Pendant is consumed, transformed in-place or replaced and whether continuous/pulsed transfer is used.
- [ ] Confirm Orb/altar feeding and empowered-pendant feeding coexist without double counting.

## Recommendation

**Disposition:** test native interaction.

**Why:** Both sides ship explicit resource and item support, and JAWR's current release contains an optional compatibility package. The mechanic has direct value to the vampire/Neo Vitae progression but touches two resource economies; do not document amounts or balance until tested.

## Revision notes

- 2026-11-15 — Mapped the native compatibility package, gated Guidebook entry and 0.5.10–0.5.13 integration history from the exact JAWR 0.5.39 artifact. Exact TMM gameplay remains untested.
