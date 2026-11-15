#!/usr/bin/env python3
"""B14 pinned-JAR stone/block-family audit (identifiers and provenance, no mod assets).

Reads hash-verified actual server mod JARs; cross-references blockstate resources,
English block names, and bundled worldgen/structure JSON. Produces detailed
machine inventory plus a reviewable human report. Does not claim a worldgen JSON
reference alone proves the block spawned in a particular live dimension.
"""
from __future__ import annotations
import argparse
import json
import re
import tomllib
import zipfile
from collections import defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
STONE_RE = re.compile(
    r"marble|darkstone|blackstone|stone|(?:^|_)rock|slate|shale|basalt|limestone|calcite|"
    r"andesite|diorite|granite|tuff|netherrack|obsidian|schist|gneiss|gabbro|"
    r"chalk|travertine|mudstone|soapstone|argillite|quartzite|sandstone|"
    r"cobbled|scoria|asurine|veridium|crimsite|ochrum|endstone|deepslate|"
    r"menhir|pumice|ashstone|soulstone|otherstone|livingrock|"
    r"jasper|dolomite|rhyolite|brimstone|flintrock",
    re.I,
)
# Ore is tracked separately even when it appears in a stone family.
ORE_RE = re.compile(r"(?:^|_)(?:deepslate_)?(?:[^/]+_)?ore(?:_|$)|ore$", re.I)
VARIANT_RE = re.compile(
    r"(?:^|_)(?:bricks?|tiles?|slabs?|stairs?|walls?|pillars?|polished|"
    r"chiseled|smooth|carved|cracked|cut|mosaic|pavers?|mossy|"
    r"engrav(?:ed|ing)|runed|etched|fences?|buttons?|pressure_plates?|"
    r"doors?|trapdoors?|shingles?|pedestals?|lanterns?|lamps?|"
    r"bars?|steps?|corbels?|columns?|arches?|plates?|trim|"
    r"reinforced|crimson|warp(?:ed|ing)|ornate)(?:_|$)",
    re.I,
)
WORLDGEN_RE = re.compile(
    r"^data/[^/]+/(?:worldgen/|neoforge/biome_modifier/|forge/biome_modifier/|structure/|"
    r"tags/worldgen/|tags/biome/|dimension/)",
    re.I,
)
# Strict-ish structural references; no loot-table references counted as natural generation.
GROUPS = (
    ("marble", r"marble"),
    ("dark/black stones", r"darkstone|blackstone|black_rock|black_.*stone|dreadstone|shadowstone|voidstone|ashen|scoria"),
    ("slate and shale", r"slate|shale|schist"),
    ("limestone, calcite and chalk", r"limestone|calcite|chalk|travertine|dolomite"),
    ("rock, basalt and volcanic stone", r"basalt|gabbro|obsidian|pumice|rhyolite|scoria|rock"),
    ("magic/end/other stones", r"endstone|end_stone|otherstone|livingrock|soulstone|brimstone|menhir|crimsite|ochrum|veridium|asurine"),
    ("general stone and sandstone", r"stone|granite|andesite|diorite|tuff|cobbled|sandstone|quartzite|mudstone|jasper|argillite"),
)


def classify(stem: str, name: str) -> str:
    low = (stem + " " + name).lower()
    if ORE_RE.search(stem):
        return "ore_stone"
    if VARIANT_RE.search(stem):
        return "worked_variant"
    # Some decorative block names have non-obvious IDs, but we preserve all
    # matching named blocks so that review can catch missing classes.
    if not STONE_RE.search(low):
        return "non_stone"
    return "primary_candidate"


def topic(stem: str, name: str) -> str:
    s = (stem + " " + name).lower()
    for label, pattern in GROUPS:
        if re.search(pattern, s):
            return label
    return "other mineral/stone"


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("jar_directory", type=Path)
    ap.add_argument("--json", type=Path, default=ROOT / "manifest/modded_stone_inventory.json")
    ap.add_argument("--markdown", type=Path, default=ROOT / "docs/MODDED-STONE-INVENTORY.md")
    args = ap.parse_args()

    mod_files = {}
    expected = set()
    for path in (ROOT / "mods").glob("*.pw.toml"):
        mod = tomllib.loads(path.read_text(encoding="utf-8"))
        if mod.get("side", "both") == "client":
            continue
        name = mod["filename"]
        if name in mod_files:
            raise ValueError("Duplicate expected JAR name: " + name)
        mod_files[name] = path.stem
        expected.add(name)
    found = {p.name for p in args.jar_directory.glob("*.jar")}
    if found != expected:
        raise ValueError("Exact hash-pinned JAR audit mismatch: missing "
                         + repr(sorted(expected-found)) + " extra " + repr(sorted(found-expected)))
    entries: dict[str, dict] = {}
    scanner = []
    for jar_path in sorted(args.jar_directory.glob("*.jar")):
        owned = []
        with zipfile.ZipFile(jar_path) as archive:
            names = archive.namelist()
            lang = {}
            for name in names:
                m = re.fullmatch(r"assets/([^/]+)/lang/en_us\.json", name)
                if m:
                    try:
                        raw = json.loads(archive.read(name))
                        for key,value in raw.items():
                            if key.startswith("block.") and isinstance(value,str):
                                lang[key]=value
                    except (json.JSONDecodeError,UnicodeDecodeError):
                        continue
            geofiles = {}
            for path in names:
                if not path.endswith(".json") or not WORLDGEN_RE.match(path):
                    continue
                try:
                    geofiles[path] = archive.read(path).decode("utf-8")
                except UnicodeDecodeError:
                    continue
            for path in names:
                m = re.fullmatch(r"assets/([^/]+)/blockstates/([^/]+)\.json", path)
                if not m:
                    continue
                ns,stem = m.groups()
                if ns == "minecraft":
                    continue
                human = lang.get(f"block.{ns}.{stem}",stem.replace("_"," ").title())
                if not STONE_RE.search(stem+" "+human):
                    continue
                ident = f"{ns}:{stem}"
                evidence = sorted(name for name,raw in geofiles.items()
                                  if ident in raw or
                                  # Some packs use registry objects without
                                  # namespacing only inside their own namespace.
                                  (f"data/{ns}/" in name and
                                   re.search(r'"(?:Name|name|block|state)"\s*:\s*"' + re.escape(stem) + r'"', raw)))
                row = {"id":ident, "name":human, "class":classify(stem,human),
                       "family":topic(stem,human), "owner_packwiz":mod_files[jar_path.name],
                       "jar":jar_path.name,"worldgen_json_refs":evidence,
                       "worldgen_json_evidence":bool(evidence)}
                if ident in entries and entries[ident]["owner_packwiz"] != row["owner_packwiz"]:
                    # A mod can ship other-mod assets for its own compatibility.
                    # Keep first and record the duplicate source explicitly.
                    entries[ident].setdefault("also_shipped_by",[]).append(mod_files[jar_path.name])
                else:
                    entries[ident]=row
                    owned.append(ident)
        scanner.append({"jar":jar_path.name,"packwiz":mod_files[jar_path.name],
                        "matching_stone_blockstates":len(owned)})
    items=sorted(entries.values(),key=lambda e:(e["family"],e["id"]))
    base=[x for x in items if x["class"]=="primary_candidate"]
    worked=[x for x in items if x["class"]=="worked_variant"]
    ores=[x for x in items if x["class"]=="ore_stone"]
    worldgen=[x for x in base if x["worldgen_json_evidence"]]
    inventory={
        "schema_version":1,
        "origin":"exact hash-pinned test-build JARs; static resources only, NO client/world placement test",
        "minecraft":"1.21.1","source_mod_count":len(scanner),
        "notes":[
            "Every listed block is sourced from a present JAR blockstate; does not prove it is obtainable.",
            "worldgen_json_evidence means a bundled worldgen/structure JSON references this block, not that it spawned in-world.",
            "Code-generated terrain, tags, jigsaw templates and hidden registry-only blocks may not be detected by this static scan.",
            "Primary candidates include naturally generated stone, alchemical stone, and some similarly named decorative blocks; review before recipes.",
            "No automatic Galore custom recipe is generated; ore and progression stones must be excluded until approved.",
        ],
        "counts":{"total_stone_blockstates":len(items),"primary_candidates":len(base),
                  "worked_variants":len(worked),"ore_stones":len(ores),
                  "primary_with_worldgen_json_refs":len(worldgen)},
        "scanned_mods":scanner,
        "blocks":items
    }
    args.json.parent.mkdir(parents=True,exist_ok=True)
    args.json.write_text(json.dumps(inventory,indent=2,ensure_ascii=False)+"\n",encoding="utf-8")

    lines=[
        "# Complete pinned-JAR stone-family inventory — B14",
        "",
        "**Data origin:** blockstate identifiers, localizations, and bundled worldgen/structure JSON "
        "from all actual hash-verified Minecraft 1.21.1 NeoForge **server** JARs in the test pack.",
        "**Status:** comprehensive static scan of loaded pack artifacts; NOT a world-generation, "
        "creative-tab, harvest or in-game registry test. Names may be missing for unlocalized blocks.",
        f"**Coverage:** {len(scanner)} server JARs; {len(items)} candidate stone-family blockstates, "
        f"{len(base)} possible primary stones, {len(worked)} worked variants, "
        f"{len(ores)} ore-bearing stones.",
        f"**Evidence:** {len(worldgen)} primary stones are referenced in bundled worldgen/structure JSON. "
        "Absence of a reference does not prove an item cannot naturally generate: mods may "
        "register features in Java, templates or runtime datapacks.",
        "",
        "The machine-readable full inventory at "
        "\`manifest/modded_stone_inventory.json\` lists EVERY matching block ID and "
        "its originating pinned mod, variant/ore classification, and concrete JSON paths.",
        "",
        "## Generator policy (proposal only)",
        "",
        "- Galore **is installed for testing**, but no custom modded-stone recipes are installed.",
        "- Use a seed block (placed below generator) for building-stone renewability; retain world discovery.",
        "- Never automatically generate ores, rare/ritual items, bosses' materials or Aquamarine Shale from ordinary water/lava.",
        "- Astral Aquamarine Shale is already a weighted Liquid Starlight + lava Chalice product, **with fluid consumption**. "
        "Defer a non-consuming late-game source until ProjectE/miner gating is designed.",
        "- Botania Livingrock, Occultism Otherstone, and similarly native magical transformations "
        "should not be duplicated before testing native cost/progression.",
        "",
        "## Primary stone candidates with bundled worldgen/structure evidence",
        "",
        "| Mod | Block | Display | Evidence location (up to 2) |",
        "|---|---|---|---|",
    ]
    for e in worldgen:
        paths=e["worldgen_json_refs"]
        display=e["name"].replace("|","/")
        lines.append(f"| \`{e['owner_packwiz']}\` | \`{e['id']}\` | {display} | "
                     + "; ".join(f"\`{p}\`" for p in paths[:2]) + (" …" if len(paths)>2 else "") + " |")
    if not worldgen:
        lines.append("| — | — | — | No direct JSON evidence found; require world test |")
    lines += ["","## All other primary stone-family candidates (not JSON-worldgen confirmed)","",
              "These can still be natural, craftable, transmutable or implemented in code. "
              "Test the pinned client and mod mechanics before assigning Galore recipes.",""]
    for label,_pattern in GROUPS:
        group=[e for e in base if e["family"]==label and not e["worldgen_json_evidence"]]
        if not group: continue
        lines += [f"### {label.title()} ({len(group)})",""]
        for e in group:
            lines.append(f"- \`{e['id']}\` — {e['name']} (\`{e['owner_packwiz']}\`)")
        lines.append("")
    group=[e for e in base if e["family"]=="other mineral/stone" and not e["worldgen_json_evidence"]]
    if group:
        lines += ["### Other minerals/stones", ""]
        lines += [f"- \`{e['id']}\` — {e['name']} (\`{e['owner_packwiz']}\`)" for e in group]
        lines.append("")
    lines += ["## Worked variants and ore stones (full IDs in machine inventory)","",
              "They are recorded for **completeness**, not recommended as generator outputs. "
              "Worked variants already have ordinary crafting/native mod progression; ores often "
              "have balance/progression implications.","",
              "| Mod | Worked variants | Ore stones |","|---|---:|---:|"]
    families=sorted(set(x["owner_packwiz"] for x in worked+ores))
    for owner in families:
        w=sum(e["owner_packwiz"]==owner for e in worked)
        o=sum(e["owner_packwiz"]==owner for e in ores)
        lines.append(f"| \`{owner}\` | {w} | {o} |")
    lines += ["","## Explicit questions for live-world QA","",
              "1. Verify distinct Astral/other-system **Marble** blocks through JEI, Jade and naturally spawned biome samples; IDs distinguish genuine duplicates from one mod's variants.",
              "2. Check distinct **Darkstone/Blackstone/Scoria/Basalt** families and whether they generate by worldgen, structures, ores or crafting.",
              "3. Find the real starter source/biome and confirm a block can be used as a Galore \`modifier\` seed block.",
              "4. Run \`spark\` measurements of a 100-generator production test at matching items/second and compare with lava+water world-block farms. Stop generators by filling output chest and retest after chunk unload/reload.",
              "5. Test decorative variant inventory/JEI and resist adding new generators for processed Livingrock/Otherstone, magical blocks, boss materials or ore duplicates.",
              "6. Assess the **Aquamarine Shale** shop route only after deciding if it is progression-safe and which currency balance is acceptable. Do not grant free early stacks.",
              "",
              "## Interpretation constraints","",
              "This script uses \`assets/*/blockstates\` plus names, not the runtime registry. "
              "It filters likely geological blocks using explicit substrings, so an unusual "
              "non-stone-named geological material can be missed and there may be false positives. "
              "The complete namespaced ID/evidence ledger makes omissions and classifications "
              "auditable. Client/JEI/worldgen tests are required before claiming an exact natural-spawn inventory.",
              ""
    ]
    args.markdown.parent.mkdir(parents=True,exist_ok=True)
    args.markdown.write_text("\n".join(lines),encoding="utf-8")
    print("PASS stone inventory",json.dumps(inventory["counts"]),
          "JARs",len(scanner),"output",args.json,args.markdown)

if __name__=="__main__":
    main()
