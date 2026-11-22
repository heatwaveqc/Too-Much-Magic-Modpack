# Transformations is an internal Progression subsystem (issue #468)

Source migration donor: `test/full-integration` @
`2e922405a01ded8893aef08cb06ae0278b9d96ef`.
Stack: PR #470 native provider observations, PR #513 configurable Path admission,
followed by this runtime/internalization PR.

There is exactly one NeoForge `@Mod` after consolidation:
`io.github.heatwaveqc.tyr.progression.ProgressionMod`. The old
`io.github.heatwaveqc.tmm.transformations` package remains an **internal Java
package**, not a separate module or installed JAR. Original JVM package names
are preserved to minimize invasive rewrites of the tested death/render code;
the public resource and packet namespaces are assigned to `tyr_progression`.
Source-native providers continue owning all blood, cures, dragon species,
lich pools, original mechanics, and active abilities.

## Expected parity

- JAWR Vampire/Werewolf and both lich + Dragon Survival native probes
- First-party native form join/acquisition gates with declared Path form-pair policy
- Archlich phylactery before Goety fallback, Dragon-first Dracolich death arbitration
- Mob alliances, retaliation, tag-driven exceptions and native form diagnostics
- JAWR bottle bridging, original Cave/Center Dracolich geometry, renderer and network
- Botania Thermalily and Mahou Catalyst native magic patches, not Path features
- All 10 transplanted JUnit files, mixin definition and full original client assets

Optional target mixins are pinned to known-native revisions and denied on unknown
versions. The old independent `tmm_transformations` JAR must be **removed** from
integration/Packwiz; loading both now fails clearly before duplicate hooks register.
The standalone source directory is retained only as archival/provenance source until
the integration writer reconciles built artifacts, lock files and source manifest.

## Validation gates

- [ ] **A** complete Progression compile/test CI including 10 imported unit suites
- [ ] **A** static one-NeoForge-entry, mixin/plugin and asset validation
- [ ] **A** integration writer reconcile pack/Packwiz/hash/source inventory to one JAR
- [ ] **B** server launch without optional mods and representative subsets
- [ ] **B/C** pairings (#452) both acquisition orders, Newbie/Normie (#455), native cures
- [ ] **B/C** single death save/payment, blood bottles, mob alliances, Thermalily/Mahou
- [ ] **C** Cave/Center Dracolich client model, relog/respawn, resource reload, multiplayer

No owner authorization for Integration Assembly, Server Boot Smoke or Prism package.
