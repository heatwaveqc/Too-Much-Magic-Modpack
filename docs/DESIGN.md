# Design contract (2026-11-13)

## Vision
Magic is the everyday infrastructure, not a cosmetic wrapper around industrial logistics. Independent magic schools must have sustained roles. Resource transport, crafting, storage, navigation and mining should favor occult/magical mechanics. The technology branch is exceptional and late-game.

## Approved systems
Neo Vitae (not original Blood Magic); Occultism; Ars Nouveau; Iron's Spells 'n Spellbooks; Theurgy; Astral Sorcery; **Botania Revived (approved unofficial port, not yet installed)**; Just Another Witchery Remake and JAWR: Forbidden Magic; Mahou Tsukai; EvilCraft; Roots Classic. Preserve experimental status where appropriate. The extended roster of approved-but-uninstalled systems and exclusions is recorded in [MAGIC-MOD-SURVEY-DECISIONS.md](MAGIC-MOD-SURVEY-DECISIONS.md).
Apotheosis, Artifacts/Relics and supernatural dungeons/bosses fit the power curve subject to item audits. Gateways to Eternity may support renewable combat drops.

## Food, agriculture and fishing
The complete Pam's HarvestCraft 2 family (Food Core, Crops, Trees, Food Extended), Farmer's Delight and Aquaculture 2 are approved. Right-click gentle harvesting is mandatory; Harvest with Ease + Cobweb are the planned QoL method (confirm actual Pam's and Farmer's Delight crop behavior in game). Pam's built-in right-click harvesting was removed. Food content should remain edible and useful; avoid automatically banning food items for species/undead overlays. New farming gardens, orchards and crop generation must be tested against Tectonic. Cross-mod tags and recipes should be consolidated rather than multiplied needlessly. See [FOOD-AND-SPECIES-DESIGN.md](FOOD-AND-SPECIES-DESIGN.md).

## Locked supernatural species and transformation rules
The **authoritative target** is [TRANSFORMATION-TARGET.md](TRANSFORMATION-TARGET.md), overriding previous tentative single-physiology and compatibility-matrix proposals. **One effective vampire physiology** merges Vampirism and JAWR vampire entrance routes and retains **two independently earned trees**; **one effective werewolf physiology** merges Werewolves addon and JAWR lycanthropy routes and retains **two independently earned trees**. **Abomination** is an intentional vampire+werewolf composite retaining **all four native provider identities/progressions** plus **one or two custom trees**. The two lichdom systems are independent and **stack into Archlich**; **Dragon + either one lich = Dracolich**, but dragons cannot be Archliches, Vampires, Werewolves or Abominations. **Hunter** is exclusive garlic-altered human physiology, immune to involuntary transformations but replaceable by confirmed intentional ritual. Do not double-run blood, hunger, sun, attribute, wolf form or death engines even when native source flags are synchronized. These are **locked design goals, not working code**.

## Data-driven transformation content versus custom code
Actual 1.21.x Vampirism/Werewolves skill-tree JSON and Dragon Survival's diet/ability/penalty registries provide substantial datapack design leverage. JAWR exposes documented ritual JSON with a command-output hook. **Do not mistake these for universal mutable species flags**: the currently single-active-faction player handler, mirrored JAWR states, Abomination, Archlich and Dracolich require a NeoForge bridge. See [TRANSFORMATION-DATAPACK-SURVEY.md](TRANSFORMATION-DATAPACK-SURVEY.md). No integration scripts/JSON are installed yet.

## Death and resurrection coordination
The pack has numerous independently implemented fatal-event interceptors: vanilla totems; Ars Nouveau Totem Perk; Occultism Guardian Familiar; Mahou Tsukai Death Collection; Vampirism downed/resurrection; JAWR vampire immortality; two independent lich resurrection anchors; Malum Geas; and Corail Tombstone grave storage (which is a different, post-death stage). See [DEATH-MECHANICS-AUDIT.md](DEATH-MECHANICS-AUDIT.md) and [POST-DEATH-OWNERSHIP.md](POST-DEATH-OWNERSHIP.md). Test common totem-like protections for double spending, but **focus the mandatory integration on actual death**: **Locked: Corail Tombstone is the sole recoverable-inventory/XP grave owner upon actual player death**. Enigmatic Legacy+ Cursed Ring soul-loss and independent ritual death consequences are preserved separately; Eldritch Amulet inventory retention and Storage Crystal drop capture must be disabled, and Escape Scroll drop relocation must not bypass the grave. This is **LOCKED policy, NOT yet configured or gameplay-tested**. See [DEATH-COMPAT-TEST-PLAN.md](DEATH-COMPAT-TEST-PLAN.md).

## Exceptional technology
Only Bigger Reactors (energy source) and Draconic Evolution (energy use/transfer/high-end gear). Avoid introducing another general energy/logistics stack. Uranium and draconic materials should be restricted to deep exploration and/or Occultism extraction. Do not assume these restrictions are implemented.

## World and loot
Rich exploration via Cataclysm, Stalwart/Starlight themed structures and Deeper and Darker where compatible. Evaluate early exploration density and performance. Review and block game-breaking single-hit artifacts in normal loot, not just crafting.

## QoL / performance
Borrow the *idea* of ATM10's tested performance, mapping, chunk claims and UI infrastructure, not the whole ATM10 mod list or proprietary assets. Candidate libraries/performance mods: ModernFix, FerriteCore, Embeddium, Entity Culling, ImmediatelyFast; candidate QoL: JEI, Jade, AppleSkin, Mouse Tweaks, Just Zoom, FTB Chunks. Verify dependency graph and current 1.21.1 loader builds individually.

## Storage
Occultism storage is central. Magical satchels, named magical bags, and portable storage from approved magic mods are allowed after compatibility and balance review. Generic backpacks and generic storage/logistics are not.

## Reproducibility
Eventually every resource, boss drop and progression item has at least one renewable or reproducible route, with multiple distinctive magical paths where practical. Some paths should be expensive or late-game. Renewable != instantly cheap or farmable without limits.

## Non-goals for first prototype
No implemented progression locks, recipe overhaul, questline, substitutions, balance guarantees, compatibility claims beyond tests actually performed.
