# Adventurers' Shop access — #523

Owner request 2026-11-27: expose the shop through the quest interface first; migrate remaining sales/purchases and retire individual transaction quests afterwards. This independent slice changes access only, so it can be reviewed, reverted and tested without altering prices or legacy quest progress. Existing economy source is baseline-owned following #569 and #675; this shop-access writer hands off to the designated pack integration writer.

## Implementation and Type A evidence

- Minecraft 1.21.1, NeoForge 21.1.265, Java 21. Exact Packwiz pins: FTB Quests 2101.1.38, FTB Library 2101.1.39, FTB Teams 2101.1.12, Q Shop 1.8.5.
- Inspected upstream tagged source: [FTB Quests toolbar](https://github.com/FTBTeam/FTB-Quests/blob/v2101.1.38/common/src/main/java/dev/ftb/mods/ftbquests/client/gui/quests/OtherButtonsPanelTop.java), [native shop button](https://github.com/FTBTeam/FTB-Quests/blob/v2101.1.38/common/src/main/java/dev/ftb/mods/ftbquests/client/gui/quests/OpenShopButton.java), [TabButton](https://github.com/FTBTeam/FTB-Quests/blob/v2101.1.38/common/src/main/java/dev/ftb/mods/ftbquests/client/gui/quests/TabButton.java), [FTB Library Panel](https://github.com/FTBTeam/FTB-Library/blob/v2101.1.39/common/src/main/java/dev/ftb/mods/ftblibrary/ui/Panel.java) and [Q Shop command](https://github.com/QLNPLUS/Q-shop/blob/v1.8.5-neoforge-1.21.1/src/main/java/com/qshop/cmd/QShopCommands.java). The native top-right panel adds buttons in addWidgets() and vertically aligns them afterwards. Q Shop registers self-open without OP; only opening for another player requires OP2.
- A client-only, provider-present guarded mixin appends a normal TabButton using the theme shop icon and a localized title; click sends the fixed self-open command through the ordinary client connection. No custom network channel, currency mutation, quest completion, elevated command, new keybind or new dependency pin. Existing FTB toolbar buttons remain untouched.
- Extend the existing exact-lock Economy compile classpath with the three already-pinned FTB artifacts. Source inspected; actual exact-JAR compilation and mixin runtime application still pending. The existing module CI is the next verification gate (no game boot). Confirm the exact pinned target method with javap if compile/runtime diagnostics require it.
- No packwiz-indexed asset or catalogue changed. The added translation is packaged in the Economy module.

## Type B/C pending integration and owner-authorized QA

1. In a disposable local world and on a dedicated server, verify /qshop open tmm opens the personal shop for a non-OP. Verify the quest book top-right Shop icon opens the identical shop and hovering displays Adventurers' Shop. No quest completion or wallet change from opening.
2. Reopen quests repeatedly and after resize/reload; one icon, native alignment, no overlap with reward, autopin or other buttons. Test GUI scales, editing on/off, survival/non-OP, Newbie and graduated players. Confirm close/reopen navigation and singleplayer pause behavior.
3. Two players: each opens only their own wallet, can buy/sell existing native entries, and sees existing stock restrictions. A no-Q Shop/FTB client-side optional-provider test must not resolve the mixin target; dedicated server must not load the GUI classes.
4. No hotkey in this slice: retain existing unbound-key policy. Intro guidance and all item catalogue/quest changes are subsequent coordinated source slices.

## Next slice (not implemented)

Inventory all existing individual sale/purchase quests and their exact quest IDs, batch counts, coin prices, introductory sale tiers, repeatability and XP. Translate to native Q Shop trade entries with preserved approved gates, including one-time introductory sale bonuses where applicable; investigate whether existing completion/limits and XP require a migration policy or narrow bridge. Verify a complete mapping before removing those transaction quests; retain instructional quests and actual bounties. Do not silently reset wallet data, change pricing or drop progression rewards. New-world and existing-world serverconfig/limits migration must be tested separately. No full build authorized.
