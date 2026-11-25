#!/usr/bin/env python3
"""Fail closed if the exact pinned ProjectE/Apotheosis hook changes."""
from __future__ import annotations

import argparse
import subprocess
from pathlib import Path


def javap(jar: Path, class_name: str) -> str:
    result = subprocess.run(
        ["javap", "-classpath", str(jar), "-p", "-s", "-c", class_name],
        check=True,
        text=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
    )
    return result.stdout


def require(text: str, needle: str, context: str) -> None:
    if needle not in text:
        raise SystemExit(f"FAIL {context}: missing {needle!r}")


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("projecte", type=Path)
    parser.add_argument("apotheosis", type=Path)
    args = parser.parse_args()

    talisman = javap(args.projecte, "moze_intel.projecte.gameObjs.items.RepairTalisman")
    require(talisman,
            "lambda$static$0(net.minecraft.world.item.ItemStack, java.lang.Void)",
            "ProjectE Repair Talisman predicate")
    require(talisman,
            "(Lnet/minecraft/world/item/ItemStack;Ljava/lang/Void;)Z",
            "ProjectE Repair Talisman predicate descriptor")
    require(talisman, "ItemHelper.isRepairableDamagedItem",
            "ProjectE native repairability guard")
    require(talisman, "PECapabilities.MODE_CHANGER_ITEM_CAPABILITY",
            "ProjectE native mode-changer guard")

    charm = javap(args.apotheosis, "dev.shadowsoffire.apotheosis.item.PotionCharmItem")
    require(charm, "Item$Properties.setNoRepair", "Apotheosis no-repair contract")
    require(charm,
            "public static boolean hasEffect(net.minecraft.world.item.ItemStack)",
            "Apotheosis charm effect contract")
    require(charm,
            "public int getMaxDamage(net.minecraft.world.item.ItemStack)",
            "Apotheosis charm durability contract")

    print("PASS exact pinned Repair Talisman lambda and Potion Charm no-repair ABI; no game boot")


if __name__ == "__main__":
    main()
