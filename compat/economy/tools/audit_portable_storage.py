#!/usr/bin/env python3
"""Offline structural audit of TMM's native-first portable-storage safety slice.

This is coverage/metadata validation only, NOT an exact-JAR or gameplay pass.
Run: python compat/economy/tools/audit_portable_storage.py
"""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[3]
MODULE = ROOT / "compat/economy/src/main/resources"
TAGS = MODULE / "data/tmm/tags/item"
RISK = TAGS / "non_nestable_embedded_containers.json"
REFERENCE = TAGS / "reference_backed_storage.json"
ARS = MODULE / "data/ars_elemental/tags/item/blacklist_bag_item.json"
MIXINS = MODULE / "tmm_economy.mixins.json"
OLD_ARS = ROOT / "kubejs/data/ars_elemental/tags/item/blacklist_bag_item.json"

EXPECTED_RISK = {
    "ars_elemental:curio_bag", "ars_elemental:caster_bag",
    "occultism:satchel", "occultism:ritual_satchel_t1", "occultism:ritual_satchel_t2",
    "extrabotany:mini_handbag",
    "malum:soulwoven_pouch", "malum:ravenous_pouch",
    "gaze:spirit_channel_pouch",
}
EXPECTED_REFERENCE = {"occultism:ender_satchel"} | {
    f"projecte:{color}_alchemical_bag" for color in
    ("white", "orange", "magenta", "light_blue", "yellow", "lime",
     "pink", "gray", "light_gray", "cyan", "purple", "blue",
     "brown", "green", "red", "black")
}
EXPECTED_MIXINS = {
    "PortableStorageShulkerSlotMixin",
    "PortableStorageShulkerHopperMixin",
    "PortableStorageBundleInsertionMixin",
    "PortableStorageOccultismSatchelMixin",
    "PortableStorageMalumPouchMixin",
    "PortableStorageBotaniaBackedInventoryMixin",
    "PortableStorageMiniHandbagMenuMixin",
}
SHULKERS = {"minecraft:shulker_box", "minecraft:bundle"} | {
    f"minecraft:{color}_shulker_box" for color in
    ("white", "orange", "magenta", "light_blue", "yellow", "lime",
     "pink", "gray", "light_gray", "cyan", "purple", "blue",
     "brown", "green", "red", "black")
}


def values(path: Path) -> set[str]:
    data = json.loads(path.read_text(encoding="utf-8"))
    if data.get("replace") is not False:
        raise ValueError(f"{path}: must extend tags, not replace them")
    return {
        entry if isinstance(entry, str) else entry["id"]
        for entry in data["values"]
    }


def run() -> int:
    errors = []
    risky, external = values(RISK), values(REFERENCE)
    for title, expected, actual in (
        ("embedded-risk", EXPECTED_RISK | SHULKERS, risky),
        ("external-reference", EXPECTED_REFERENCE, external),
        ("registered Economy mixins", EXPECTED_MIXINS,
         set(json.loads(MIXINS.read_text(encoding="utf-8"))["mixins"])),
    ):
        missing = expected - actual
        if missing:
            errors.append(f"{title}: missing {sorted(missing)}")

    overlap = risky & external
    if overlap:
        errors.append(f"embedded/reference tag overlap: {sorted(overlap)}")
    if "#tmm:non_nestable_embedded_containers" not in values(ARS):
        errors.append("Ars native blacklist is not bridged to the risk tag")

    if OLD_ARS.is_file():
        old = values(OLD_ARS)
        if old != {"#tmm:non_nestable_embedded_containers"}:
            errors.append("Indexed KubeJS Ars bridge must extend only the approved embedded-risk tag; "
                          "reference-backed handles must not be blanket-banned")

    print(f"Risk IDs: {len(risky)}; reference handles: {len(external)}")
    print("Source-only inventory: slot/handler and serialization runtime coverage "
          "must still be checked against exact installed JARs")
    for error in errors:
        print(f"ERROR: {error}")
    return 1 if errors else 0


if __name__ == "__main__":
    raise SystemExit(run())
