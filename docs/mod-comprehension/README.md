# Mod comprehension — research knowledge base

A persistent, version-aware reference for **what each installed mod actually does**, which options are useful in a normal survival run, and where different mods connect. This exists to support high-quality FTB quest design and evidence-based compatibility decisions.

## Start here

- [Work instructions](WORK-CHAT-INSTRUCTIONS.md): self-directed, repeatable research process; this is the operating contract.
- [Coverage index and queue](INDEX.md): choose unfinished work, track progress and find completed profiles.
- [Mod profile template](templates/MOD-PROFILE.md): one substantive profile for every gameplay mod; abbreviated interface notes for truly mechanical-less libraries.
- [Cross-mod interaction template](templates/CROSS-MOD-INTERACTION.md): one file per coherent relationship/use case, not a giant unsourced matrix.
- [Quest/compatibility handoff template](templates/QUEST-COMPAT-HANDOFF.md): optional actionable synthesis after evidence supports it.

## Paths

| Directory | Content | Filename convention |
| --- | --- | --- |
| `mods/` | Detailed single-mod references | `<manifest-id>.md` |
| `interactions/` | Built-in and potential two-or-more-mod combinations | `<mod-id>--<mod-id>--<short-purpose>.md`, IDs alphabetized |
| `handoffs/` | Quest and compatibility design recommendations | `<topic>-handoff.md` |
| `templates/` | Never treat placeholders as completed findings | `*.md` |

The authoritative roster is **`manifest/mods.json`**; installed packwiz pins are **`index.toml` and `mods/*.pw.toml`**. An approved or `enabled_in_pack` entry is not proof of a successful client load, installed dependency, verified feature or tested interaction. Favor actual pinned 1.21.1 NeoForge versions, and call out stale documents.

## Two passes, separate claims

**Pass 1 — comprehend a mod.** Explain the survival loop, machines, spells, rituals, mobs, interfaces, progression, automation, recipe/command/config hooks, and *what enters and exits a system*. Show a player how to build a few cheap, high-value setups. Identify interactions *within* the mod. Distinguish documented mechanics from version-specific JAR/source observations and actual client tests.

**Pass 2 — connect mods.** Start from the supported inputs/outputs and hooks in two or more profiles. Identify whether a connection works natively, already exists in the pack, or would require new KubeJS/datapack/Java code. Track costs, gates, risks and exact tests; do not turn a plausible idea into a compatibility claim.

A useful profile is not just a list of blocks or JEI recipes. It should answer: **"Why would I pursue this mod, what can I do immediately, what do I build next, and what other systems benefit?"**

## Downstream boundaries

Research may propose quest milestones, Tips & Tricks discoveries, recipe bridges and acceptance tests. It must **not** edit runtime configs, KubeJS, FTB quest content, mod selection, feature status or the game balance without a separate implementation request. Quest authoring must obey [QUEST-DESIGN-RULES](../QUEST-DESIGN-RULES.md), [QUEST-ARCHITECTURE](../QUEST-ARCHITECTURE.md), [DESIGN](../DESIGN.md) and [AGENTS](../../AGENTS.md). Do not inadvertently create early ProjectE access, generic item logistics, or a new forbidden mod dependency.

## Quality bar

Cite primary sources and the **exact version** for concrete mechanics. Use evidence tiers:

- **Observed in pack** — reproducible in a disposable 1.21.1 NeoForge TMM client/world, with version and steps.
- **Inspected pinned artifact/source** — exact-version code, data, recipe, config or JAR examination; describe what is proved, not runtime outcomes.
- **Documented upstream** — official docs/changelog/source for a matching version; do not claim playtesting.
- **Community report / hypothesis** — leads only; label them and include verification steps.

When two sources disagree, preserve the discrepancy and prefer pinned-version artifacts; do not paper over it. Unknown is a useful result if tied to a specific unresolved question.
