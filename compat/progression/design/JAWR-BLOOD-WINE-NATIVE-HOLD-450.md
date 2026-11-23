# #450 Blood Wine — exact BnC 4.5.0 native dependency hold

**The owner-approved Blood Wine remains 5× the actual blood in one full input bottle**, brewed from native Brewin' and Chewin' red wine in its Aging Cask. Preserve native wine effects, intoxication, aging, one empty bottle return, no repeat blood multiplier and JAWR-only native blood credit.

**Type A exact JAR result (November 23, 2026):** `mods/brewin-and-chewin.pw.toml` pins `BrewinAndChewin-neoforge-4.5.0+1.21.1.jar`. An exact SHA-512-verified download and ZIP/class enumeration in [run #45263801220](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/45263801220) proved the installed JAR has **neither `AgingCaskBlockEntity.class` nor `WineItem.class`**. It has Keg/Fermenting and HeatingCask machinery. The upstream `1.21.1` source branch's newer Aging Cask/Wine classes are not in the pinned artifact.

**Disposition:** source attempted optional Wine/Cask Mixins and a distillates-tag extension were **removed from the active feature**, rather than shipping no-op hooks or admitting special JAWR blood into an unguarded machine. Blood Sausage and Blood Pie Slice source is implemented separately within PR #607. No BnC version upgrade or substitute brewing mechanic has been authorized.

**Implementation-ready after technical dependency resolution:** source-verify a real published 1.21.1 NeoForge BnC artifact with native Aging Cask + red wine; verify publisher hash/dependency closure and have designated integration writer review pin change. Reintroduce server-authoritative 1-bottle full-blood cask consumption, component-receipt on aged red wine, one glass return, and on-finish 5× JAWR credit with no second charge on re-aging. If a compatible artifact is unavailable, choosing a nonnative wine system would require an explicit gameplay decision.

**Design blockers:** None for two foods; native Wine dependency/upgrade is an engineering/selection issue. **Type B/C:** all gameplay acceptance pending an owner-authorized candidate. No Minecraft boot, pack assembly or Prism package run.
