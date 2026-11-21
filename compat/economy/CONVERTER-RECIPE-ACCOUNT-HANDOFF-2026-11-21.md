# EMC converter recipe and account contract — 2026-11-21

Issue [#322](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/322), owner correction of 2026-11-21. Source-only implementation; **not** an owner-authorized Minecraft assembly or test.

## Exact crafting recipes (source-ready)

| Result | One of each, consumed through ordinary shapeless crafting | Optional-mod conditions |
| --- | --- | --- |
| `tmm_economy:emc_catalyst` | `botania:conjuration_catalyst` + `projectexpansion:basic_emc_link` | Botania and Project Expansion |
| `tmm_economy:emc_source_jar` | `ars_nouveau:source_jar` + `projectexpansion:basic_emc_link` | Ars Nouveau and Project Expansion |

`emc_source_jar.json` **already had** both approved ingredients; it was re-audited, not needlessly changed. `emc_catalyst.json` is updated from the old Alchemy Catalyst to the Conjuration Catalyst. There is **no** supplemental Dark Matter, Philosopher's Stone or recipe alternative. The selected Basic EMC Link is the MK1 Project Expansion item; it is not a generic tag matching later EMC Link tiers.

Native source references (do not confuse source inspection with installed binary execution):
- Botania Revived 1.21.1 registered `botania:conjuration_catalyst` from `LibBlockNames.CONJURATION_CATALYST` / `BotaniaBlocks.conjurationCatalyst`, and generated crafting JSON.
- Project Expansion upstream 1.21.1 `projectexpansion:basic_emc_link`, generated EMC Link recipe and item/block loot table; pinned version `1.0.6`.
- Ars Nouveau's `ars_nouveau:source_jar` was already the input in the installed Economy source.

## Owner UUID and offline EMC behavior

**Source finding, not a changed mechanic:** The current conversion bridge `AccountRefill.tick` looks up the actual saved placer UUID, obtains an online `ServerPlayer` on the server thread, then reads that player's ProjectE `PECapabilities.KNOWLEDGE_CAPABILITY`. No fake balance, account clone, offline player object, or creative/cached EMC is constructed. A null owner/player/provider aborts before mutation. All EMC charges precede actual native Mana/Source receipt; only definitively unaccepted resource batches are refunded. Neither converter forces chunks loaded.

Project Expansion's own upstream `BlockEntityEMCLink` does **not** supply a separately persisted per-owner withdrawable EMC pool. Its `Util.getKnowledgeProvider(UUID)` calls `Util.getPlayer(UUID)`; the latter resolves `ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid)`. It returns `null` while the owner is offline. `BlockEntityEMCLink.ItemHandler.extractItemInternal` and fluid drain explicitly require an online owner; after restart/rejoin they resume via the real knowledge provider. The EMC Link block's separate local `emc` field is an intermediate import buffer, **not an independent account**. Its generic `IEmcStorage` capability cannot substitute for the saved player's balance; using it as a synthetic debit would risk free value or a second wallet.

**Result:** On the inspected upstream 1.21.1 implementation, offline account withdrawal is unavailable, so matching the native EMC Link means **do not withdraw while that owner is offline; resume on login**. The existing Economy bridge already enforces that. Replacing this with an unverified offline provider, alternate per-world balance, or forcing a fake player would violate the owner's single-account/conservation requirements. If the *pinned* Project Expansion 1.0.6 JAR exposes additional authorized offline access, reconcile that exact API and implement it on this workstream **before** claiming the desired offline behavior satisfied. This is an engineering verification dependency, **not** an invitation to change the owner-approved design.

References inspected:
- Project Expansion upstream source `DonovanDMC/ProjectExpansion@168bcf2491b9fde679295fd412ad9c93fd3d93f1`: `util/Util.java`, `block/entity/BlockEntityEMCLink.java`, `block/entity/BlockEntityOwnable.java`.
- Installed pinned `mods/project-expansion.pw.toml` selects `projectexpansion-1.21.1-1.0.6.jar` from official Modrinth file `F5C1OobM`, SHA-512 `97e832da2ca03309f4ef875b42a9250bba3798453f271f82b441e7c0bf739e103ee9f4274839b5e4d9a154a44167cc0f8169bd97ed37b3d16e7e09d34cfe3c85`. Exact pinned Java bytecode was subsequently audited without executing the mod; **no in-world verification of JAR behavior in this slice**.

## Source and QA checklist

- [x] **A:** Exact recipe JSON and dual mod conditions asserted by `compat/economy/tools/verify_converter_contract.py` plus negative fixture tests; linked Economy Module CI runs them.
- [x] **A:** Converter source retains native rates, `20 Mana / 1 EMC` and `4 Source / 3 EMC`; zero mutable EMC conversion algorithm edits.
- [x] **A/source:** Read native Project Expansion account-resolution source and compare its offline access to the existing Economy bridge.
- [x] **A:** Verified the **exact hash-locked Project Expansion 1.0.6 JAR** with `compat/economy/tools/audit_native_emc_link_offline.py` using `javap` (read-only). `Util.getKnowledgeProvider(UUID)` invokes live `Util.getPlayer(UUID)` and returns null without it; `getPlayer(UUID)` resolves current server/online `PlayerList.getPlayer(uuid)`. The pinned EMC Link references that lookup. [Economy Module CI run 44928156902](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44928156902) audit step PASS; no offline provider is present in that route. This does not prove all possible third-party API routes or in-game synchronization.
- [ ] **B/C, after separately authorized build:** Player A places both converters; compare active loaded EMC Link extraction and converter operation with A online, A logged out but chunks still loaded, A relogging, and player B online. Assert no owner substitution, zero EMC debit/receipt while provider inaccessible, no loss/duplication, redstone pause and native rate.
- [ ] **B/C:** Craft each converter with precisely the two approved ingredients; test partial Mana Pool and Source Jar, empty wallet, dimension change/restart, break/re-place and new owner, filled Source Jar component, full pool and no artificial throughput cap.

Preserve the accepted rates, native Source capacity, redstone pause and owner-on-placement semantics. Any future source corrections remain under an isolated Economy writer PR; the modpack integration writer owns shared index/manifest and final inclusion.
