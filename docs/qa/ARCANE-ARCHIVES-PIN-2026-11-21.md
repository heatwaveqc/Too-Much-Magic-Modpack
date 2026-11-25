# Arcane Archives: Reborn exact pin evidence

**Status:** source-ready candidate metadata for issue [#387](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/387); not installed, assembled, launched, or gameplay accepted. The isolated branch starts from `test/full-integration` commit `077a34d8706dba51b10ad65dde05a7d34cfb5c96`.

The owner approved full native inclusion of Arcane Archives: Reborn in the next unstable QA candidate. At the start of this slice, issue #387 had no substantive activity for more than twelve hours. Existing intake PR #388 records the QA obligation, while pinning PR #399 explicitly leaves Arcane Archives unresolved rather than supplying a guessed hash. No open or merged PR, branch head, indexed file, frozen source manifest, or dated owner decision found during the preflight already supplied this exact candidate pin.

## Frozen artifact evidence

| Field | Verified value |
| --- | --- |
| Official provider | CurseForge project `1696190`, file `8985321` |
| Release file | `ArcaneArchives-1.0.2+Neoforge-1.21.1.jar` |
| Size | `3,192,671` bytes |
| SHA-1 | `082f5f560981c04c487b9c1f5e5ec9976f8ec2db` |
| SHA-256 | `ad3ce7609f9de5d24387245a68a061e55726e88143b5cb9913a9f744f43bfed4` |
| Declared mod/version | `arcanearchives` / `1.0.2` |
| Declared environment | Minecraft `1.21.1`; NeoForge `[21.1.251,)`; both client and server |
| Required dependency | Modonomicon `[1.120.5,)`, load after, both sides |
| Optional dependency | JEI |
| License | MIT |

The hashes were computed from the exact official CDN bytes for CurseForge file `8985321`; the downloaded JAR remained outside the repository and is not committed. Inspection of its `META-INF/neoforge.mods.toml` established the loader and dependency ranges above. The JAR also contains its MIT license, client mixin declaration, and Modonomicon book data under `data/arcanearchives/modonomicon/books/tome_arcana/`.

The current integration source pins Minecraft `1.21.1`, NeoForge `21.1.265`, and `modonomicon-1.21.1-neoforge-1.123.0.jar` (CurseForge `538392/9384356`, SHA-1 `3971a5ade2595bfcb8a93f4baa2e6e834691e3cf`). Those versions satisfy the artifact's declared minimums. This is metadata compatibility evidence only, not proof of a successful mod load or runtime compatibility.

## Exact branch changes

- Add `mods/arcane-archives-reborn.pw.toml` with the exact official project/file identity and SHA-1.
- Keep that candidate deliberately absent from `index.toml`, so this isolated work does not activate a new runtime mod or claim the shared pack manifest.
- Add `scripts/verify_arcane_archives_candidate.py` to guard the immutable pin, inactive state, no committed JAR, Minecraft/NeoForge target, and current Modonomicon minimum.
- Add this provenance and handoff record.

## Checks and remaining limits

Run the candidate verifier, Python bytecode compilation, repository validation, and `git diff --check`. These checks are static and must not be reported as Minecraft, server, client, book, recipe, storage, automation, or gameplay acceptance.

The integration writer must reconcile the shared `index.toml`, `pack.toml` hash, manifests, and live-inclusion register when actually adding the candidate. A future owner-authorized runtime test must confirm clean dedicated-server and client loading, guidebook access, item/fluid conservation, oversized-stack and storage-upgrade behavior, FTB Chunks interactions, ProjectE/EMC duplication boundaries, recipes/progression, and performance. No Integration Assembly, Minecraft launch, full smoke, or Prism package was authorized or performed by this slice.
