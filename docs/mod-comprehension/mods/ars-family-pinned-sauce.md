# Pinned Ars family — bundled Sauce identity and scope

**Historical snapshot:** integration `2e922405a01ded8893aef08cb06ae0278b9d96ef` upgrades Elemental to0.7.11.3 with Sauce74.113/[74,). The61.100 family-candidate conclusion below no longer describes that current pin; see [delta receipt](ars-elemental-pinned-utility-rituals.md). Remaining family pins were unchanged in the targeted comparison; new Sauce recipient audit remains.

Research2026-11-22. Exact packwiz filenames/download hashes were recomputed for native Ars plus all seven installed Ars addons: Nouveau5.13.5, Elemental0.7.10.4, Elemancy1.18.3, Additions21.3.0, Controle1.6.16, n Spells3.3.5, Ocultas2.6.2 and Unification1.2.23. Local copied/downloaded artifacts remain outside Git. Relevant fetched integration `4bd316fb7811451442c4b030d59a2b3a0ede5571`; Elemental/Nouveau pin paths unchanged versus prior integration checkpoint. This is metadata/source analysis, not a new loader/assembly run.

## One coordinate, two packaged versions

Only Elemental and Elemancy among those eight exact JARs contain a Sauce entry in `META-INF/jarjar/metadata.json`. Coordinate in both is **`com.alexthw.sauce:sauce-1.21.1`**, isObfuscated=false:

| Parent artifact | Declared supported range | Bundled artifactVersion / nested path | Nested bytes SHA256 |
| --- | --- | --- | --- |
|`ars_elemental-1.21.1-0.7.10.4.jar`|`[0.0.61,)`|`0.0.61.100` / `META-INF/jarjar/sauce-1.21.1-0.0.61.100.jar`|`58ce7a36fc6fb7631b35765d58227b2982ece031559fa4122f1391a17811b251`|
|`ars_elemancy-1.21.1-1.18.3.jar`|`[0.0.29,)`|`0.0.29.73` / `META-INF/jarjar/sauce-1.21.1-0.0.29.73.jar`|`33c51a5241a9728c7428bf4dc4deca15f5b0e3c239d5bde173e0b67610fd026e`|

Other six family JARs have no Sauce entry; Additions has no jarjar metadata at all. Neither nested Sauce JAR has further jarjar metadata. Do not load/audit the two nested versions as independent simultaneous implementations of the same coordinate.

The two declared ranges intersect at **`[0.0.61,)`**. Of the two supplied versions,0.0.29.73 does not satisfy Elemental's minimum, while0.0.61.100 satisfies both. Therefore **0.0.61.100 is the only eligible supplied candidate in this inspected eight-mod family**, without needing an assumption that a runtime blindly selects the largest filename. NeoForged describes Jar-in-Jar as supported-range negotiation and warns that another mod's range can change the runtime version; this supports distinguishing bundled preference from final selected runtime. [Official Jar-in-Jar documentation](https://docs.neoforged.net/toolchain/docs/dependencies/jarinjar/), [official ModDevGradle dependency negotiation](https://docs.neoforged.net/toolchain/docs/plugins/mdg/#jar-in-jar). Documentation consulted2026-11-22; not proof of this pack's actual loader selection.

## Consequence for research

Elemental's exact companion audits decompile **0.0.61.100**, including school focus/bangle attributes, duplicate summon listeners, caster-tome resolver, prism lens and focus-turret implementations. That version is compatible with both inspected metadata ranges. Elemancy's own embedded0.0.29.73 must **not** be assumed the operative recipient when Elemancy is combined with Elemental. Its forthcoming profile needs both its addon call sites and the0.0.61.100 shared recipients, explicitly checking any API/behavior drift. Matching a permitted range alone does not prove every call/interaction works.

Elemental top-level metadata requires Ars Nouveau`[1.21.1-5.13.1,)` and Minecraft`[1.21,)`; Elemancy requires Nouveau`[1.21.1-5.2,)`, Elemental`[0.7.0.9,)` and Minecraft`[1.21,)`. Pinned versions satisfy those declared lower bounds. Optional Elemental Ars Creo dependency is not a mandate to install Creo/Create or expand rejected scope.

**Remaining global boundary:** research checkout contains159 packwiz pin files; all eight family binaries above were hash-checked, not every unrelated binary/nested dependency. No standalone Sauce pin or any third higher nested candidate has been established by this family inspection. A different provider outside this set, external launch override or changed assembled source could affect actual selection. Keep whole-runtime selected Sauce unverified until the authorized assembly/loader evidence identifies it. This is a recorded research-scope limit, not a hard blocker to other mod research and not permission to boot the server.

Future acceptance/source reconciliation: confirm active source manifest/pin revision and all provided Sauce coordinates; verify selected version/hash in authorized runtime receipt; compare exact loaded recipients before promoting family interaction behavior to verified-in-pack. Positive current-family case has only61.100 satisfying both; boundary test adds a deliberately constrained/higher candidate in a separately authorized disposable environment. No dependency pins/configuration/loader changes or runtime tests were made.
