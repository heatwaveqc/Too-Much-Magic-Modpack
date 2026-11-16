# Lava fishing — Gilded / Cave Dragon feasibility (MC 1.21.1 NeoForge)

**Status 2026-11-16: source research ONLY. No mod added to the manifest, no packwiz pin, no game hooks/XP source implemented and no server/client QA.** The owner asked if lava fishing exists for the release and floated class-exclusive access and earning XP.

## Suitable release: Lava Fishing by TeamSunset

- Mod: **Lava Fishing** (not the same project as the generic Aquaculture 2: Lava Fishing Addon).
- Official release: **1.21.1-1.2.13**, NeoForge Minecraft 1.21.1, available on [Modrinth](https://modrinth.com/mod/lavafishing) and [CurseForge](https://www.curseforge.com/minecraft/mc-mods/lavafishing).
- Published direct dependencies: **Aquaculture 2** (\`aquaculture\`, 2.7.18–2.7.x range) and **Kotlin for Forge** (\`kotlinforforge\` 5.10+); our current canonical full-integration manifest already lists \`Aquaculture-1.21.1-2.7.22\` and \`kotlinforforge-5.12.0-all\` both enabled. Exact 1.2.13 binary/JAR version ABI and hashes still require a formal pin audit.
- Actual [tagged 1.21.1-1.2.13 source](https://github.com/teamsunset/LavaFishing/tree/1.21.1-1.2.13) checked: \`ItemFishingRod\` derives from \`AquaFishingRodItem\`; \`EventFishingHook.onEntityTickPost\` receives **\`AquaFishingBobberEntity\`**, retrieves \`bobber.playerOwner\`, and checks fluid tags against the rod hook. This gives a concrete server-authoritative seam for restricting lava access; simply detecting vanilla FishingHook is insufficient without verification.
- The mod provides **native populated** \`lavafishing:gameplay/fishing/lava/*\` and \`lavafishing:gameplay/fishing/nether/*\` loot tables, several fish, junk, and treasure including gold nuggets, Netherite scrap and its own items. The fish and resources are a balance change, not merely a class XP switch.
- The mod also contains non-fishing tools, armor, bullets/slingshots and Promethium-related content. Review extras before including; a packwide mod install does NOT inherently gate new content to Gilded.

## Other candidates

- **Aquaculture 2: Lava Fishing Addon** (1.21.1 NeoForge) exposes additional lava hooks and separate lava/Nether fishing loot tables, but **explicitly provides no loot of its own**. It suits a custom-datapack catch economy if we do not want a large content mod.
- **Nether Depths Upgrade** (1.21.1 NeoForge) also adds fish and a lava fishing rod. It has many independent Nether creatures/plants and another lava-fishing loot economy; overlap with Lava Fishing needs a content/double-loot audit. It is not in the current full-integration mod manifest.
- Prefer **one** provider initially (recommend **Lava Fishing 1.2.13** because both dependencies are already present and its fish/loot are authored).

## Conditional class-exclusive lava-fishing design (NOT APPROVED AS GAMEPLAY)

1. When a lava-capable Aquaculture bobber encounters a fluid with \`FluidTags.LAVA\`, inspect its actual server-side owner and native class/species, not a copied player tag or client claim.
2. Allow **joined Gilded** (optionally require native rank >= 12 as an additional mid-tier license). **Cave Dragon** from Dragon Survival may also be allowed after verifying the exact native server species API; no blanket "all dragons" rule.
3. Prevent non-eligible lava fishing **before catch**, not just remove caught items after a loot roll. Candidate interception: server \`EntityTickEvent.Post\` on Aquaculture's \`AquaFishingBobberEntity\`, using the current lava \`FluidState\` to force an invalid reel/discard. Better yet, intercept Aquaculture's actual water/lava fluid eligibility or hook choice on creation. Disallow both water-first-then-lava and relogged lingering bobbers. Do not prevent non-Gilded water fishing.
4. Credit Gilded XP only on **verified completed successful lava catch**. Source \`ItemFishedEvent\` supports native vanilla hooks, but **has not yet been demonstrated to fire for \`AquaFishingBobberEntity\`**; inspect the exact 2.7.22 Aquaculture reel/loot code before relying on it. If it doesn't fire, add a narrow mod-specific post-reel source hook. Require one real catch event and correct fluid/owner; no XP for a cast, tick, empty reel, junk invented via command, or automation.
5. **Provisional reward:** +5 native Gilded XP per successful lava catch (gold-ore mining currently gives +10). This is a proposal, not a locked balance choice. Base it on the action, not item count (some rods and bonus hooks double catches).
6. Cave Dragons who have fishing permission do not automatically earn **Gilded** XP. If independently enrolled Gilded and allowed by the eventual physiology rules, their Gilded rank may advance like anyone else's. A Cave Dragon-specific XP award would need a separate Dragon Survival progression contract.
7. Verify native Gilded membership, eligibility during casting vs retrieval, food-diet compatibility for each fish (Nether provenance does not automatically legalize every output), valid Nether and Overworld lava, non-player ownership/automation, multiplayer claims, catch items vs entities, fishing luck, spoilage/duplication, reconnect/dimension change.
8. **Don't** apply Gilded virtual Fortune to fishing loot: it is a *block-mining enchantment bonus*. Fishing uses its own loot/luck mechanics and must not inherit +10 by simply naming it Fortune.

## Proposed acceptance flow if owner selects the mod

- Resolve exact 1.21.1-1.2.13 release artifact and hash; verify Aquaculture 2 and Kotlin for Forge version range on both client/server.
- Add an explicit mod record (\`desired\` then \`enabled\` only when pinned), \`mods/*.pw.toml\` and packwiz index entry in the authorized Gilded QA stream.
- Inspect source for AquaFishingBobberEntity fluid selection, effective cast/hook and successful catch semantics. Implement the smallest actor-aware gate before loot, then XP emitter. No full smoke without explicit instruction.
- First live tests with Gilded, Cave Dragon, normal players and regular fishing rods, including one fish vs multiple output stacks and fishing after leaving the Nether.

**Source references:**
- https://github.com/teamsunset/LavaFishing/tree/1.21.1-1.2.13
- https://modrinth.com/mod/lavafishing
- https://modrinth.com/mod/aquaculture-2-lava-fishing-addon
- https://modrinth.com/mod/nether-depths-upgrade
- https://aldak.netlify.app/javadoc/1.21.1-21.1.x/net/neoforged/neoforge/event/entity/player/itemfishedevent
