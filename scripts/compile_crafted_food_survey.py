#!/usr/bin/env python3
"""Compile Codex's pinned, ingredient-level JAR census into *candidate* food routes.

Deterministic, source-only: no game boot, magic food values, native FOOD claims,
item NBT, or guessed recipe inputs. Runtime can ingest promoted generated files
after review. An unavailable input or alternative is held, never ignored.
"""
from __future__ import annotations

import argparse
from collections import Counter, defaultdict
import itertools
import json
from pathlib import Path
import re
import tomllib

ID = re.compile(r"^[a-z0-9_.-]+:[a-z0-9_.-]+(?:/[a-z0-9_.-]+)*$")
MAX_ALTERNATIVES = 64
DATA = Path("data/tyr_progression/tyr_progression")
ROUTES = DATA / "food_recipe_routes"
SOURCES = DATA / "food_sources"
TAGS = Path("data/tmm/tags/item/foods/generated")
HEAT = {
    "minecraft:smelting", "minecraft:smoking", "minecraft:campfire_cooking",
    "minecraft:blasting", "farmersdelight:cooking",
}
COLD = {"minecraft:crafting_shapeless", "farmersdelight:cutting"}
HOT_TOOLS = {"pot", "skillet", "saucepan", "bakeware", "oven", "bakingdish", "grill"}
NEUTRAL_ITEMS = {"minecraft:bowl", "minecraft:glass_bottle", "minecraft:bucket",
                 "minecraft:stick", "minecraft:paper"}
# Reviewed *tag semantics*, not per-item nutrition values. These material
# records never create a FoodProperties component, edible item, or extra hunger.
MATERIAL_TAGS = {
    "c:grain": "#tmm:foods/grains",
    "c:crops/rice": "#tmm:foods/grains",
    "c:mushrooms": "#tmm:foods/fungi",
}
MATERIAL_ITEMS = {"minecraft:kelp": "#tmm:foods/sea_plants"}
IDENTITY_RE = re.compile(r"meatballs?", re.I)


class Hold(ValueError):
    pass


def identifier(value):
    return isinstance(value, str) and ID.fullmatch(value) is not None and all(
        segment not in (".", "..") for segment in value.split(":", 1)[1].split("/"))


def read_records(root):
    result = {}
    if not root.is_dir():
        raise ValueError(f"Missing checked-in food evidence directory {root}")
    for path in sorted(root.rglob("*.json")):
        name = path.relative_to(root)
        if len(name.parts) < 2:
            raise ValueError(f"Malformed food evidence path {path}")
        item = f"{name.parts[0]}:{'/'.join(name.parts[1:])[:-5]}"
        if not identifier(item):
            raise ValueError(f"Unsafe food evidence path {path}")
        value = json.loads(path.read_text(encoding="utf-8"))
        if value.get("item") != item or item in result:
            raise ValueError(f"Conflicting food evidence {item}")
        result[item] = value
    return result


def verify_pins(audit, repo_root):
    if (audit.get("schema") != 1 or audit.get("requested_scope") != "pinned"
            or audit.get("failures") or audit.get("minecraft_booted")
            or audit.get("runtime_map_generated")):
        raise ValueError("Expected successful source-only pinned JAR audit")
    releases = audit.get("releases", [])
    if len(releases) != 15:
        raise ValueError("Expected all fifteen installed priority food JARs")
    # Publisher audit slugs and Packwiz filenames are not guaranteed to
    # match (e.g. aquaculture-delight vs aquaculturedelight.pw.toml).
    # Compare exact pinned filename + digest, not guessed slug paths.
    pins = {}
    for pin in sorted((repo_root / "mods").glob("*.pw.toml")):
        data = tomllib.loads(pin.read_text(encoding="utf-8"))
        name = data["filename"]
        if name in pins:
            raise ValueError(f"Ambiguous Packwiz filename {name}")
        pins[name] = data
    seen = set()
    for release in releases:
        slug = release["slug"]
        name = release["filename"]
        data = pins.get(name)
        digest = data.get("download", {}) if data else {}
        if (release["status"] != "verified"
                or release["provenance"] != "publisher_download"
                or not release["existing_indexed_hash_matches"]
                or name in seen
                or digest.get("hash-format") != "sha1"
                or digest.get("hash") != release["sha1"]):
            raise ValueError(f"Outdated or unverified food JAR: {slug}")
        seen.add(name)


class TagIndex:
    """Exact recursive item-tag closure. Unknown tags and cycles are holds."""
    def __init__(self, releases):
        merged = defaultdict(set)
        for release in releases:
            for tag in release["source_evidence"]["item_tags"]:
                for value in tag["values"]:
                    if isinstance(value, dict):
                        value = value.get("id")
                    if not isinstance(value, str) or not (
                            value.startswith("#") and identifier(value[1:])
                            or identifier(value)):
                        raise ValueError(f"Unrecognized tag member in {tag['tag']}")
                    merged[tag["tag"]].add(value)
        self.items = dict(merged)
        self.cache = {}

    def expand(self, tag, visiting=()):
        if tag in self.cache:
            return self.cache[tag]
        if tag in visiting:
            raise Hold(f"tag_cycle:{tag}")
        members = self.items.get(tag)
        if not members:
            raise Hold(f"missing_tag:{tag}")
        results = set()
        for entry in sorted(members):
            if entry.startswith("#"):
                results.update(self.expand(entry[1:], visiting + (tag,)))
            else:
                results.add(entry)
        if not results:
            raise Hold(f"empty_tag:{tag}")
        value = tuple(sorted(results))
        self.cache[tag] = value
        return value

    def ingredient(self, raw):
        if not isinstance(raw, dict):
            raise Hold("unsupported_ingredient")
        if set(raw) == {"item"} and identifier(raw["item"]):
            return (raw["item"],)
        if set(raw) == {"tag"} and identifier(raw["tag"]):
            return self.expand(raw["tag"])
        typ = raw.get("type")
        if typ == "neoforge:compound" and set(raw) == {"type", "children"}:
            return tuple(sorted(set(itertools.chain.from_iterable(
                self.ingredient(child) for child in raw["children"]))))
        if typ == "neoforge:difference" and set(raw) == {"type", "base", "subtracted"}:
            base = set(self.ingredient(raw["base"]))
            return tuple(sorted(base - set(self.ingredient(raw["subtracted"]))))
        if typ == "neoforge:intersection" and set(raw) == {"type", "children"}:
            parts = [set(self.ingredient(child)) for child in raw["children"]]
            return tuple(sorted(set.intersection(*parts))) if parts else ()
        raise Hold("unsupported_ingredient_codec")


def material_sources(tags, roots, known_namespaces):
    """Tag-derived *ingredient* records, not a guess that an item is edible."""
    inferred = defaultdict(set)
    for tag, family in MATERIAL_TAGS.items():
        try:
            members = tags.expand(tag)
        except Hold:
            continue
        for item in members:
            if item.split(":")[0] in known_namespaces:
                inferred[item].add(family)
    for item, family in MATERIAL_ITEMS.items():
        inferred[item].add(family)
    additions = {}
    for item in sorted(inferred):
        if item in roots:
            continue
        if len(inferred[item]) != 1:
            continue  # conflicting tag families must be reviewed
        family = next(iter(inferred[item]))
        additions[item] = {
            "schema": 1, "item": item,
            "provenance": "tmm:source_tag_material",
            "source_units": 1, "inferred_tags": sorted((family, "#tmm:foods/raw")),
        }
    return additions


def source_signature(item, roots):
    src = roots.get(item)
    if src is None:
        return ("route", item)
    return ("source", src["source_units"], tuple(sorted(src["inferred_tags"])),
            tuple(sorted(src.get("preferred_shares", {}).items())))


def method(recipe):
    kind = recipe.get("type")
    if kind in HEAT:
        return "heat"
    if kind == "minecraft:crafting_shapeless":
        for part in recipe.get("ingredient_inputs", []):
            if isinstance(part, dict) and "tag" in part:
                tag = part["tag"]
                if tag.startswith("c:tool_") and any(
                        token in tag.split(":")[-1] for token in HOT_TOOLS):
                    return "heat"
        return "nonthermal"
    if kind == "farmersdelight:cutting":
        return "nonthermal"
    raise Hold("unsupported_processing_method")


def compile_one(recipe, index, available, roots):
    outputs = recipe.get("outputs", [])
    if len(outputs) != 1:
        raise Hold("multiple_output_items")
    output = outputs[0]
    result_count = output.get("count")
    if not isinstance(result_count, int) or not 1 <= result_count <= 64:
        raise Hold("invalid_result_count")
    kind = method(recipe)
    raw = recipe.get("ingredient_inputs")
    if not isinstance(raw, (list, dict)) or not raw:
        raise Hold("missing_full_inputs")
    inputs = [raw] if isinstance(raw, dict) else raw
    slots = []
    for part in inputs:
        if not isinstance(part, dict):
            raise Hold("unsupported_ingredient")
        if set(part) == {"tag"} and part["tag"].startswith("c:tool_"):
            # Exact native tool tags are non-nutritive. We preserve their
            # existence in the audit, not as an edible source.
            slots.append(((part["tag"],), True))
            continue
        options = index.ingredient(part)
        if not options:
            raise Hold("empty_ingredient")
        neutral = all(item in NEUTRAL_ITEMS for item in options)
        if not neutral and any(item in NEUTRAL_ITEMS for item in options):
            raise Hold("mixed_tool_food_tag")
        if not neutral and any(item not in available for item in options):
            raise Hold("unresolved_source_input")
        # Equivalent *verified* source facts have the same dietary budget.
        # Compress ONLY these, never arbitrary names or unexplained outputs.
        if not neutral:
            equivalent = {}
            for item in options:
                equivalent.setdefault(source_signature(item, roots), item)
            options = tuple(sorted(equivalent.values()))
        slots.append((options, neutral))
    if not any(not neutral for _, neutral in slots):
        raise Hold("no_nutritive_inputs")
    variants = 1
    for options, _ in slots:
        variants *= len(options)
        if variants > MAX_ALTERNATIVES:
            raise Hold("too_many_alternatives")
    compiled = []
    for choices in itertools.product(*(o for o, _ in slots)):
        counts = Counter()
        tools = Counter()
        for choice, (_, neutral) in zip(choices, slots):
            if neutral:
                tools[choice] += 1
            else:
                counts[choice] += 1
        # Route records describe only nutritive inputs. Nonfood
        # containers/tool receipts remain in the audit and are not given a
        # phantom source-unit / hunger value.
        compiled.append({
            "receipt": recipe["recipe"], "method": kind,
            "result_count": result_count,
            "ingredients": [{"item": item, "count": count}
                            for item, count in sorted(counts.items())],
        })
    return compiled


def process(audit, checked_sources, checked_routes):
    releases = audit["releases"]
    index = TagIndex(releases)
    known_ns = {"minecraft"}
    for release in releases:
        known_ns.update(release.get("native_versions", {}))
    sources = dict(checked_sources)
    added_sources = material_sources(index, sources, known_ns)
    sources.update(added_sources)
    recipes = defaultdict(list)
    for release in releases:
        for entry in release["source_evidence"]["recipes"]:
            for output in entry["outputs"]:
                if identifier(output.get("item")):
                    recipes[output["item"]].append(entry)
    existing = set(checked_routes)
    accepted = {}
    reasons = {}
    while True:
        changed = False
        available = set(sources) | set(checked_routes) | set(accepted)
        for item in sorted(recipes):
            if item in sources or item in existing or item in accepted:
                continue
            raw_routes = []
            reason = None
            for recipe in recipes[item]:
                try:
                    raw_routes.extend(compile_one(recipe, index, available, sources))
                except Hold as failure:
                    reason = str(failure)
                    break
            if reason:
                reasons[item] = reason
                continue
            by_key = {}
            for route in raw_routes:
                # Many furnace/campfire/smoker recipes are nutritionally
                # identical. Keep one deterministic receipt per equivalence.
                key = (route["method"], route["result_count"],
                       tuple((x["item"], x["count"]) for x in route["ingredients"]))
                if key not in by_key or route["receipt"] < by_key[key]["receipt"]:
                    by_key[key] = route
            choices = sorted(by_key.values(), key=lambda r: (
                r["receipt"], r["method"], str(r["ingredients"])))
            if not choices or len(choices) > MAX_ALTERNATIVES:
                reasons[item] = "too_many_alternatives"
                continue
            accepted[item] = {
                "schema": 1, "item": item,
                "identity": "unfavored_all" if IDENTITY_RE.search(item.split(":")[1])
                            else "ordinary",
                "complete": True, "routes": choices,
            }
            reasons.pop(item, None)
            changed = True
        if not changed:
            break

    category = defaultdict(set)
    for item, document in accepted.items():
        category["crafted"].add(item)
        kinds = {r["method"] for r in document["routes"]}
        for kind in kinds:
            category[kind].add(item)
        if document["identity"] == "unfavored_all":
            category["unfavored_all"].add(item)
    result_files = {}
    for item, document in sorted(accepted.items()):
        ns, name = item.split(":", 1)
        result_files[str(ROUTES / ns / (name + ".json"))] = document
    for item, document in sorted(added_sources.items()):
        ns, name = item.split(":", 1)
        result_files[str(SOURCES / ns / (name + ".json"))] = document
    for tag, ids in sorted(category.items()):
        result_files[str(TAGS / (tag + ".json"))] = {
            "replace": False, "values": sorted(ids),
        }
    reason_counts = dict(sorted(Counter(reasons.values()).items()))
    summary = {
        "schema": 1, "status": "source_candidates_not_verified_edible",
        "source_audit_recipe_count": sum(
            len(r["source_evidence"]["recipes"]) for r in releases),
        "indexed_output_ids": len(recipes),
        "existing_roots": len(checked_sources),
        "generated_material_sources": len(added_sources),
        "existing_reviewed_recipe_outputs": len(checked_routes),
        "generated_candidate_recipe_outputs": len(accepted),
        "held_output_ids": len(reasons),
        "hold_reasons": reason_counts,
        "held_outputs": {k: reasons[k] for k in sorted(reasons)},
        "generated_ids": sorted(accepted),
    }
    return result_files, summary


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--audit", required=True, type=Path)
    parser.add_argument("--repo", required=True, type=Path)
    parser.add_argument("--out", required=True, type=Path)
    args = parser.parse_args()
    audit = json.loads(args.audit.read_text(encoding="utf-8"))
    verify_pins(audit, args.repo)
    native = args.repo / "compat/progression/src/main/resources"
    roots = read_records(native / SOURCES)
    existing = read_records(native / ROUTES)
    files, summary = process(audit, roots, existing)
    for rel, data in sorted(files.items()):
        target = args.out / rel
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_text(json.dumps(data, indent=2, sort_keys=True) + "\n",
                          encoding="utf-8")
    (args.out / "crafted-food-survey-report.json").write_text(
        json.dumps(summary, indent=2, sort_keys=True) + "\n", encoding="utf-8")
    print("Food survey:", {k: v for k, v in summary.items()
                           if k not in ("held_outputs", "generated_ids")})


if __name__ == "__main__":
    main()
