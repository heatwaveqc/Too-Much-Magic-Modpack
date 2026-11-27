# In-world crash — November 25, 21:07 Arizona

Installed build **887a4ffb032c3579b9ee7d2d2cfd23da9dfbac6c**, baseline **aeee4fc6155691a2a6fcf1e98712ed783ab5a5d3**. Crash time **2026-11-25 21:07:14 America/Phoenix** (2026-11-26 04:07:14 UTC). Rechecked **255/255 installed JAR filenames and packaged hashes: PASS**. This is the evening build, not the morning Thanksgiving snapshot.

## Observed failure

`Exception ticking world`, server thread, caused by a Rhino Java-overload ambiguity at **`kubejs/server_scripts/zz_reactive_loot_books.js:29`**:

```javascript
if (!stack.is(TMMReactiveItems.ENCHANTED_BOOK)) continue
```

Rhino 2101.2.7-build.85 cannot choose between `ItemStack.is(net.minecraft.core.HolderSet)` and `ItemStack.is(net.minecraft.world.item.Item)` for the supplied `EnchantedBookItem`. The exception propagates out of the LootJS custom action into Minecraft world ticking.

The stack includes Primal Magick 6.0.7 `AbstractTileSidedInventoryPM.unpackLootTable` → Minecraft `LootTable.fill` → LootJS modifier → Reactive-book script. This proves the container-loot execution path; it does **not** establish a Primal Magick bug or whether the player opened a container. No additional reproduction or world restart was performed.

This is a different failure stage from the morning build's conditional-`const` redeclaration: the current script reaches its loot callback and now fails resolving a native overloaded method. The existing enchanting/loot writer should make the Item overload explicit or use an unambiguous item comparison, then audit the other native overload calls and verify a real chest-loot roll. That is a proposed engineering follow-up, not a patch applied here.

## Evidence

- [Sanitized crash exception and complete initial stack](crash-stack.sanitized.txt).
- [Matching client-log crash excerpt](client-log-crash-excerpt.sanitized.txt).
- [Exact build, script location and original-file SHA-256 provenance](evidence.json).

The original full crash report and logs remain local. Published excerpts omit world seed, player/location details, UUIDs, system/hardware sections and unrelated chat. No save, JAR, screenshot or raw full log uploaded. The crash report and log agree on exception and time; JSON readback, identifier scans and `git diff --check` pass.

## Work-slice handoff

- **Implementation-ready:** owner-requested retrieval/sanitization/publication complete, continuing evening QA PR #796 (morning #708 is merged). This QA collector owns evidence only; existing Reactive/enchanting writer owns repair. Designated integration writer reviews and merges the report.
- **Design blockers:** none for recording the crash; overload repair is engineering work.
- **B/C queue:** C01 **FAIL** on installed 887a4ffb; await corrected inclusion and authorized return to world/container loot test. Remaining evening checklist cases are not promoted. No gameplay code, config, save or launcher changes, new build, game launch or automatic retry performed.
