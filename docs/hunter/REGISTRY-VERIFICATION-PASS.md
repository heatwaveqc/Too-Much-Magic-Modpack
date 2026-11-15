# Hunter creature registry verification — historical source-only pass (2026-11-15)

> **SUPERSEDED:** The exact pinned JARs were subsequently downloaded and hash-verified (22/22); a full integrated NeoForge server boot succeeded and enumerated 1,393 runtime IDs. Read the current [PINNED-RUNTIME-VERIFICATION.md](PINNED-RUNTIME-VERIFICATION.md) and [candidate-runtime-crosswalk.csv](candidate-runtime-crosswalk.csv) rather than treating the source-only caveats below as the current test result. The historical notes below are retained to show what was unknown before actual verification.


**Research-only.** No mods, game behavior, equipment, skills, world-generation settings or AI goals were changed. Baseline: live `test/full-integration` pins, Minecraft 1.21.1 / NeoForge. This pass extends PR #97.

## Result and artifact roles

- [creature-inventory.csv](creature-inventory.csv): **370** candidate entries from **21** source groups (including 44 Alex's Caves candidates; Alex's Caves is **not installed or approved**).
- [registration-evidence.csv](registration-evidence.csv): **95 explicit upstream entity registration IDs**, preserving `MobCategory`, proposed Hunter classes, eligibility policy, pinned version, immutable-vs-moving source evidence and exact source URL. These IDs are **source-derived**, **not verified by reading the pinned binary JAR**.
- [CREATURE-TAXONOMY.md](CREATURE-TAXONOMY.md): 370-entry coverage, current category decisions and faction-neutrality integration plan.

**Source evidence is not a live-registry dump.** The research environment could read GitHub source but could not directly retrieve/inspect the pinned third-party JAR binaries or run a fresh Minecraft server census. Therefore no row is labeled `pinned-jar-verified`. A Git tag matching a version number is stronger than a moving `1.21.1` branch but still not binary provenance.

## Proven source registrations

| Provider | Source version | ID records | Source quality | Notable outcome |
| --- | --- | ---: | --- | --- |
| Deeper and Darker | [v1.4.1 tag](https://github.com/KyaniteMods/DeeperAndDarker/blob/v1.4.1/src/main/java/com/kyanite/deeperdarker/content/DDEntities.java) | 13 | release tag aligned to pinned version | 11 creature IDs and two boats, all documented in sidecar |
| Primal Magick | [6.0.7 tag](https://github.com/daedalus4096/PrimalMagick/blob/6.0.7/common/src/main/java/com/verdantartifice/primalmagick/common/entities/EntityTypesPM.java) | 50 | release tag aligned to pinned version | 36 living mob registrations; includes pixie companions, 3 golems, Treefolk, Inner Demon and Friendly Witch |
| Hexerei | [1.21.1 branch](https://github.com/JoeFoxe/Hexerei/blob/1.21.1/src/main/java/net/joefoxe/hexerei/client/renderer/entity/ModEntityTypes.java) | 6 | moving branch, pinned 0.5.0.3 not binary-compared | Only Crow and Owl are creature registrations; broom, boats and painting are not prey |
| Enchanted: Witchcraft | [1.21.1 branch](https://github.com/Favouriteless/Enchanted/blob/1.21.1/common/src/main/java/net/favouriteless/enchanted/common/init/EEntityTypes.java) | 5 | moving branch, pinned 4.2.7 not binary-compared | Mandrake is a Monster; Familiar Cat a Creature. **Ent registration is commented out and NOT counted** |
| Roots Classic | [1.21.1 branch](https://github.com/Lothrazar/RootsClassic/blob/1.21.1/src/main/java/elucent/rootsclassic/registry/RootsEntities.java) | 3 | moving branch, pinned 1.5.10 not binary-compared | Phantom Skeleton Monster; two accelerator utilities |
| Malum | [1.21.1 branch](https://github.com/SammySemicolon/Malum-Mod/blob/1.21.1/src/main/java/com/sammy/malum/registry/common/entity/MalumCultistEntityTypes.java) | 6 | moving branch, pinned 1.8.2 not binary-compared | Five registered cultist types; `natural_spirit` is a **nonliving resource**, not Undead prey |
| Forbidden & Arcanus | [1.21.1 branch](https://github.com/stal111/Forbidden-Arcanus/blob/1.21.1/neoforge/src/main/java/com/stal111/forbidden_arcanus/core/init/ModEntities.java) | 12 | moving branch, pinned 2.6.1 not binary-compared | Lost Soul / Corrupt Lost Soul / Enchanted Lost Soul plus Dark Trader; 8 noncombat projectiles/boats |

**Coverage arithmetic:** 13 + 50 + 6 + 5 + 3 + 6 + 12 = **95** source registrations. The creature inventory gained **45** creature/NPC records compared with 325-entry pass: Primal 36, Hexerei 2, Enchanted 2, Roots Classic 1, Forbidden & Arcanus 4. The other 50 registry entries are nonliving helpers/projectiles, or registrations already covered by the earlier creature pass.

## Semantic corrections and exclusions

1. **Spirit is not a Hunter category.** Genuinely postmortem spectral beings are Undead: Roots' Phantom Skeleton and Forbidden & Arcanus's Lost Souls are good examples. A supernatural being merely called a 'spirit' is not necessarily dead: Occultism's outsiders remain Demons, Malum's spirit items remain resources.
2. **Do not classify pixies by their magic element.** Primal Magick's Infernal, Blood, Sea, Void, etc. pixies are all **Fae** with different powers. The `pixie_house` registration is `MobCategory.MISC` and not a creature; excluded from Hunter prey even though its ID includes the word pixie.
3. **No Hunter XP from friendly/pet entities.** Hexerei's Crow/Owl; Enchanted Familiar Cat; Primal Magick's friendly golems, pixies and Friendly Witch; and Forbidden & Arcanus's Dark Trader/Lost Souls are not legitimate ordinary hunts, despite their valid living registrations.
4. **Not every MONSTER-category entity is player-aggressive.** Roots Classic's Phantom Skeleton source targets other hostile mobs and retaliates but has no unprovoked player-target goal. Classify as Undead, with defensive-only hunting eligibility until runtime confirmation. [Behavior source](https://github.com/Lothrazar/RootsClassic/blob/1.21.1/src/main/java/elucent/rootsclassic/entity/skeleton/PhantomSkeletonEntity.java).
5. **Registered is not the same as naturally available.** Deeper and Darker's Anger, Fear and Sorrow Pots have entity IDs and MONSTER categories, but the [v1.4.1 release notes](https://github.com/KyaniteMods/DeeperAndDarker/releases/tag/v1.4.1) explicitly say those three were removed from a kill-all advancement until they can spawn in the world. Their ordinary hunt contract eligibility is set to `no` pending a world-spawn change.
6. **A mod's theme is not a creature taxonomy.** The v1.4.1 [Deeper and Darker source classes](https://github.com/KyaniteMods/DeeperAndDarker/tree/v1.4.1/src/main/java/com/kyanite/deeperdarker/content/entities) show AnglerFish extends AbstractFish and hunts players in water (**Aquatic**, not Sculk); Sludge extends vanilla Slime (**Ooze**, not Sculk); OvercastPot is animated pottery (**Construct**, not Sculk). These five rows were corrected in both CSVs. Sculk remains for creatures genuinely connected to sculk, such as the Shattered and named sculk mobs. This prevents a hidden dimension-based classification from reappearing under the Sculk label.
7. **Summons and active equipment entities:** Goety, Neo Vitae, Ars, Primal Magick and others create creatures as player-controlled servants, familiars or combat utility. Hunter XP must not be farmable with player-owned summons; check both *direct ownership* and indirect scripted spawns.

## Taxonomic resolution status

The 370 records have **51 `Unresolved` membership assignments**, many of which are deliberately noncombat vendors, inert effects or unclassified humans rather than failed prey categories. The unresolved set is not a target to eliminate by guessing.

Highest-value unresolved **hostile** cases include Cataclysm's Ignis, Wadjet, Clawdian, The Watcher and some Kobole-related enemies; Iron's Ancient Knight, Citadel Keeper and Echo of Tyros; Goety's Heretic, Sorcerer, Ender Keeper and assorted servants; and ordinary Ghasts/Shulkers/Vexes. Require actual lore and source behavior, not inferred categorization by dimension, spellcasting, name or boss status.

**Additional module checks, no unsupported 'zero new mobs' assertion:**
- **Goety Cataclysm:** pinned addon extends Goety spellcasting and Cataclysm servant mechanics; servant entity provenance/ownership must be checked separately, not assumed to introduce a new species family. [Project](https://modrinth.com/mod/goety-cataclysm).
- **Gaze (Malum addon):** [public feature listing](https://www.curseforge.com/minecraft/mc-mods/gaze-a-malum-addon) emphasizes runes, weapons, progression and geas; 'a new friend' is planned, not proof of a registered mob in the pinned version.
- **Forbidden & Arcanus:** complete above 12 registration source list, including noncombat.
- **Roots Classic:** **not the same mod as MysticMods/Roots**. Only the authorized Roots Classic project/branch was used for source verification.
- **Enchanted: Witchcraft:** the pinned mod's NeoForge port is not the Fabric 4.2.7 download page; the cross-loader GitHub branch is an upstream evidence lead, not proof of NeoForge binary identity.
- **Alex's Caves:** still prospective; do not select a NeoForge port implicitly.

## Completion criteria for the exact pinned-JAR audit

1. Download every pinned mod artifact from the **packwiz lock URL/CurseForge file ID**, verify SHA-512/SHA-1 against `mods/*.pw.toml` and preserve the exact SHA in an evidence ledger; never commit these JARs or unpacked copyrighted assets.
2. Inspect every JAR's `META-INF/neoforge.mods.toml` and entity registration classes/data. Compare IDs against [registration-evidence.csv](registration-evidence.csv) and record absent/renamed/new registrations. Important: source release tags are evidence, not a substitute for this.
3. Boot a disposable NeoForge server with the **current full integrated pack**, register a diagnostic that enumerates **all entity types**, including MISC, distinguishing `LivingEntity`/Mob from projectiles/items/vehicles. Log registry namespace, source mod, type, class, vanilla tags, Hunter tags, summons/owners when available, natural spawn eligibility, and current hostility behavior; don't treat MobCategory alone as a hostility test.
4. Compare runtime log against the source census, correct entries and certify each mod independently. Test tagged families and no-target AI with and without player transformations, summoned owners, retaliation, social overrides, and boss exemptions.
5. Verify that **no Hunter XP** is awarded for tamed creatures, player-owned servants, neutral vendors, noncombat souls, routine animal farms, or infinitely repeatable self-summoned targets. Test multi-tag damage stacking against Dragon + Void and Undead + Aquatic.
6. Keep a separate **neutrality matrix** from creature classes. Vampire-to-Undead and werewolf-to-Beast are pack affinity policies, not inherent proof of every entity's behavior.

**No runtime proof was manufactured in this pass.** The inventory can support planning and data-map authoring, but should not be marked complete or gameplay-tested until the pinned-JAR and server census checks pass.
