# Native first-party dependency closure — QA/CI handoff

References: PR #501 (qualified Jade version), PR #502 (focused repair), PR #299 (Iron's native version). **Execution level: A / source and packaged-metadata checks only.**

## Scope and contract

This change prevents selected built first-party modules from being staged with
dependency ranges that reject the *installed native mod versions* on either
client or server. It does **not** assemble a pack, launch Minecraft, or change
version pins or gameplay.

- Source gate reads manifest/first-party-runtime.json, all compiled module
  META-INF/neoforge.mods.toml files, Packwiz's hash-locked index and selected
  provider JARs downloaded by the existing Economy classpath audit.
- Effective provider versions come from provider metadata and, when applicable,
  META-INF/MANIFEST.MF Implementation-Version (including the plus-neoforge
  qualifier), never the download page or filename.
- Selected optional providers must be present and compatible. An optional
  dependency can be absent only if its mod isn't selected in Packwiz.
  Missing required providers, wrong bytes, unrecognized native IDs, unknown
  dependency mappings and unresolved versions fail closed.
- Java helper uses Apache Maven's VersionRange/DefaultArtifactVersion rather
  than approximated lexical or Python version comparisons. CI tests exact
  qualified versions and bounded lower ranges.
- Full-integration **quick** CI executes the gate after all first-party module
  compilation and before any owner-authorized smoke or Prism work. Repository
  quick CI independently runs offline fixture tests on PR heads.

## Ready work and acceptance

- [x] Source implemented: shared native closure parser, pinned hash and side
  validation, Maven evaluator, offline Python and Java regression fixtures,
  and integration with selected module build.
- [ ] Obtain exact-head CI output: repository fixture tests, selected module
  build and actual provider metadata closure. These are not claimed PASS yet.
- [ ] Design blockers: **none**. No gameplay or version-choice policy changes.
- [ ] C/client QA: corrected snapshot must still reach NeoForge loader and
  apply Jade OP display mixin without injection errors. Not attempted here.

**Limit:** Covers dependencies *declared by TMM first-party modules*, not
all third-party-to-third-party dependencies, embedded jar-in-jar closure,
or runtime mixin ABI success. An external dependency added later must be
mapped to its selected Packwiz slug and made available in the reviewed,
hash-verified compile classpath. There is no fallback that silently skips it.

Designated integration writer reviews and merges into test/full-integration.
No run, package, branch merge, or release authorization is implied.
