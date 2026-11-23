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
# Names can *warn* us that nonthermal crafting is insufficient evidence
# for a cooked dish; do not use the name alone to assign a hot nutrition value.
THERMAL_UNCERTAINTY = re.compile(r"grilled|roasted|baked|fried|cookie|toast|steamed|boiled|braised", re.I)


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
                or digest.get("hash-format") not in ("sha1", "sha512")
                or digest.get("hash") != release.get(digest.get("hash-format"))):
            raise ValueError(f"Outdated or unverified food JAR: {slug}")
        seen.add(name)


def load_runtime_food_registry(path):
    """Validated default-stack FOOD census from the active NeoForge server.

    The item tag table is the actual loaded membership after datapacks and
    loader conditions, not a union of all possible source-JAR tag members.
    """
    raw = json.loads(path.read_text(encoding="utf-8"))
    if (raw.get("schema") != 1
            or raw.get("kind") != "tmm:default_item_stack_food_registry"
            or raw.get("scope") != "loaded_server_registry"
            or raw.get("authoritative_for") != "default_stack_food_component"
            or raw.get("recipe_output_stacks_examined") is not False):
        raise ValueError("Expected a default-stack runtime food registry census")
    entries = raw.get("items")
    if not isinstance(entries, list) or not entries:
        raise ValueError("Empty runtime item registry census")
    ids, foods = set(), set()
    membership = defaultdict(set)
    for entry in entries:
        if (not isinstance(entry, dict) or set(entry) != {"id", "food", "tags"}
                or not identifier(entry["id"])
                or type(entry["food"]) is not bool
                or not isinstance(entry["tags"], list)
                or entry["id"] in ids):
            raise ValueError("Malformed or duplicate runtime registry entry")
        item = entry["id"]
        ids.add(item)
        if entry["food"]:
            foods.add(item)
        for tag in entry["tags"]:
            if not identifier(tag):
                raise ValueError(f"Malformed runtime item tag on {item}: {tag}")
            membership[tag].add(item)
    if (raw.get("item_count") != len(ids)
            or raw.get("food_count") != len(foods)):
        raise ValueError("Runtime registry census counts disagree with items")
    return {"ids": ids, "foods": foods, "tags": dict(membership)}


class TagIndex:
    """Exact recursive item-tag closure. Unknown tags and cycles are holds."""
    def __init__(self, releases, runtime=None):
        merged = defaultdict(set)
        invalid = set()
        for release in releases:
            for tag in release["source_evidence"]["item_tags"]:
                for value in tag["values"]:
                    if isinstance(value, dict):
                        value = value.get("id")
                    if not isinstance(value, str) or not (
                            value.startswith("#") and identifier(value[1:])
                            or identifier(value)):
                        # A source JAR can ship malformed tags (Pam Crops'
                        # rabbit_food uses uppercase IDs and stray spaces).
                        # Fail *that tag's closure*, not the entire survey;
                        # never silently accept an incomplete alternative set.
                        invalid.add(tag["tag"])
                        continue
                    merged[tag["tag"]].add(value)
        self.items = dict(merged)
        self.invalid = frozenset(invalid)
        self.live_members = runtime["tags"] if runtime is not None else None
        self.cache = {}

    def expand(self, tag, visiting=()):
        if self.live_members is not None:
            members = self.live_members.get(tag)
            if not members:
                raise Hold(f"missing_runtime_tag:{tag}")
            return tuple(sorted(members))
        if tag in self.invalid:
            raise Hold(f"malformed_native_tag:{tag}")
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


def material_sources(tags, roots, known_namespaces, registered_ids=None):
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
        if item in roots or registered_ids is not None and item not in registered_ids:
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
        outputs = recipe.get("outputs", [])
        if any(THERMAL_UNCERTAINTY.search(output.get("item", "").split(":")[-1])
               for output in outputs):
            raise Hold("ambiguous_thermal_prep")
        return "nonthermal"
    if kind == "farmersdelight:cutting":
        return "nonthermal"
    raise Hold("unsupported_processing_method")


def compile_one(recipe, index, available, roots, registered_ids=None):
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
            if registered_ids is not None:
                index.expand(part["tag"]) # Verify tool actually exists in runtime tags.
            # Exact native tool tags are non-nutritive. We preserve their
            # existence in the audit, not as an edible source.
            slots.append(((part["tag"],), True))
            continue
        options = index.ingredient(part)
        if registered_ids is not None:
            if set(part) == {"item"} and part["item"] not in registered_ids:
                raise Hold("ingredient_not_registered")
            # A source tag's optional references to mods that are absent
            # from the live registry cannot create fictitious recipe routes.
            options = tuple(item for item in options if item in registered_ids)
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


def process(audit, checked_sources, checked_routes, registry=None):
    releases = audit["releases"]
    index = TagIndex(releases, runtime=registry)
    # Positive native *tag evidence* is not equivalent to a registered FOOD
    # component. It is a separate review class, never universal edibility.
    native_food_tagged = set()
    for tag in ("c:foods", "c:juices", "farmersdelight:meals"):
        try:
            native_food_tagged.update(index.expand(tag))
        except Hold:
            pass
    # Prevent tracing seeds and containers into bogus "crafted food" tags.
    # Some Pam's crop seed IDs are tagged only as villager plantable seeds.
    nonfood_outputs = set(NEUTRAL_ITEMS)
    for tag in ("c:seeds", "minecraft:villager_plantable_seeds",
                "farmersdelight:serving_containers",
                "herbalbrews:container_items"):
        try:
            nonfood_outputs.update(index.expand(tag))
        except Hold:
            pass
    known_ns = {"minecraft"}
    for release in releases:
        known_ns.update(release.get("native_versions", {}))
    sources = dict(checked_sources)
    added_sources = material_sources(index, sources, known_ns,
                                    registry["ids"] if registry else None)
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
    excluded = set()
    while True:
        changed = False
        available = set(sources) | set(checked_routes) | set(accepted)
        for item in sorted(recipes):
            if item in sources or item in existing or item in accepted:
                continue
            # Recipes from vanilla and other unscanned mods cannot be proven
            # complete solely from the 15 audited food JARs.
            if item.startswith("minecraft:") or item in nonfood_outputs:
                excluded.add(item)
                reasons.pop(item, None)
                continue
            raw_routes = []
            reason = None
            for recipe in recipes[item]:
                try:
                    raw_routes.extend(compile_one(recipe, index, available, sources,
                        registry["ids"] if registry else None))
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

    # Scope *finished dietary overrides* to food-tagged outputs. Internal
    # recipe graph work still includes untagged intermediates so a food can
    # depend on flour, dough or another ingredient with traceable ancestry.
    # Only transitively necessary intermediates are emitted as support records.
    food_targets = set(recipes) & (
        registry["foods"] if registry is not None else native_food_tagged)
    candidates = set(accepted) & food_targets
    support = set()
    pending = list(sorted(candidates))
    while pending:
        item = pending.pop()
        for route in accepted[item]["routes"]:
            for ingredient in route["ingredients"]:
                dependency = ingredient["item"]
                if dependency in accepted and dependency not in candidates and dependency not in support:
                    support.add(dependency)
                    pending.append(dependency)
    emitted = candidates | support

    # Diagnostics: explain exactly which approved foods need new source
    # evidence. The held foods are known from the OUTPUT tag, so missing
    # ingredient evidence should not be mistaken for "not a food".
    unresolved_by_food = {}
    direct_missing = Counter()
    available_final = set(sources) | set(checked_routes) | set(accepted)
    for item in sorted(food_targets & set(reasons)):
        if reasons[item] != "unresolved_source_input":
            continue
        missing = set()
        for entry in recipes[item]:
            raw = entry.get("ingredient_inputs")
            if not isinstance(raw, (dict, list)):
                continue
            for part in ([raw] if isinstance(raw, dict) else raw):
                if not isinstance(part, dict):
                    continue
                if set(part) == {"tag"} and part["tag"].startswith("c:tool_"):
                    continue
                try:
                    options = index.ingredient(part)
                except Hold:
                    continue
                for ingredient in options:
                    if ingredient not in available_final and ingredient not in NEUTRAL_ITEMS:
                        missing.add(ingredient)
        if missing:
            unresolved_by_food[item] = sorted(missing)
            direct_missing.update(missing)
    untagged_by_namespace = Counter(
        item.split(":", 1)[0] for item in recipes if item not in food_targets)
    category = defaultdict(set)
    for item in sorted(candidates):
        document = accepted[item]
        category["crafted_candidate"].add(item)
        category["native_food_tag_candidate"].add(item)
        for kind in {r["method"] for r in document["routes"]}:
            category[kind].add(item)
        if document["identity"] == "unfavored_all":
            category["unfavored_all"].add(item)
    result_files = {}
    for item in sorted(emitted):
        document = accepted[item]
        ns, name = item.split(":", 1)
        result_files[str(ROUTES / ns / (name + ".json"))] = document
    for item, document in sorted(added_sources.items()):
        ns, name = item.split(":", 1)
        result_files[str(SOURCES / ns / (name + ".json"))] = document
    for tag, ids in sorted(category.items()):
        result_files[str(TAGS / (tag + ".json"))] = {
            "replace": False, "values": sorted(ids),
        }
    summary = {
        "schema": 1, "status": (
            "runtime_default_food_component_scoped"
            if registry is not None else "source_candidates_not_verified_edible"),
        "source_audit_recipe_count": sum(
            len(r["source_evidence"]["recipes"]) for r in releases),
        "indexed_output_ids": len(recipes),
        "existing_roots": len(checked_sources),
        "generated_material_sources": len(added_sources),
        "existing_reviewed_recipe_outputs": len(checked_routes),
        "tagged_recipe_output_ids": len(set(recipes) & native_food_tagged),
        "actual_default_food_recipe_output_ids": (
            len(food_targets) if registry is not None else None),
        "runtime_registered_item_ids": len(registry["ids"]) if registry else None,
        "runtime_default_food_item_ids": len(registry["foods"]) if registry else None,
        "generated_candidate_recipe_outputs": len(candidates),
        "native_food_tagged_candidates": len(candidates),
        "generated_support_recipe_outputs": len(support),
        "emitted_recipe_route_records": len(emitted),
        "support_ids": sorted(support),
        "excluded_known_nonfood_or_vanilla_outputs": len(excluded),
        "excluded_output_ids": sorted(excluded),
        "skipped_untagged_recipe_outputs": len(set(recipes) - food_targets),
        "untagged_recipe_outputs_by_namespace": dict(sorted(untagged_by_namespace.items())),
        "missing_source_input_occurrences": dict(sorted(direct_missing.items(),
            key=lambda entry: (-entry[1], entry[0]))),
        "unresolved_source_inputs_by_tagged_food": unresolved_by_food,
        "held_output_ids": len(food_targets & set(reasons)),
        "hold_reasons": dict(sorted(Counter(reasons[k] for k in food_targets & set(reasons)).items())),
        "held_outputs": {k: reasons[k] for k in sorted(food_targets & set(reasons))},
        "generated_ids": sorted(candidates),
    }
    return result_files, summary


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--audit", required=True, type=Path)
    parser.add_argument("--repo", required=True, type=Path)
    parser.add_argument("--pin-snapshot", type=Path, default=None,
                        help="Exact pinned roster surveyed by Codex, defaulting to --repo")
    parser.add_argument("--registry-json", type=Path,
                        help="Optional authoritative loaded-server default-stack FOOD census")
    parser.add_argument("--out", required=True, type=Path)
    args = parser.parse_args()
    audit = json.loads(args.audit.read_text(encoding="utf-8"))
    verify_pins(audit, args.pin_snapshot or args.repo)
    native = args.repo / "compat/progression/src/main/resources"
    roots = read_records(native / SOURCES)
    existing = read_records(native / ROUTES)
    registry = (load_runtime_food_registry(args.registry_json)
                if args.registry_json else None)
    files, summary = process(audit, roots, existing, registry=registry)
    for rel, data in sorted(files.items()):
        target = args.out / rel
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_text(json.dumps(data, indent=2, sort_keys=True) + "\n",
                          encoding="utf-8")
    (args.out / "crafted-food-survey-report.json").write_text(
        json.dumps(summary, indent=2, sort_keys=True) + "\n", encoding="utf-8")
    print("Food survey:", {k: v for k, v in summary.items()
                           if k not in ("held_outputs", "generated_ids", "excluded_output_ids", "support_ids",
                                       "unresolved_source_inputs_by_tagged_food",
                                       "missing_source_input_occurrences",
                                       "untagged_recipe_outputs_by_namespace")})


if __name__ == "__main__":
    main()
