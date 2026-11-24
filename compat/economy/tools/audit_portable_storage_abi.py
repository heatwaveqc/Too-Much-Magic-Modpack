#!/usr/bin/env python3
"""Read-only exact-pinned-JAR method/field audit for #16 portable storage.

Use only *already hash-verified* Economy CI classpath JARs. Never loads mod
classes, runs Minecraft, rewrites JARs, or claims Mixin application PASS.
"""
from pathlib import Path
import subprocess
import sys

TARGETS = {
    "malum-1.21.1-1.8.2.jar": {
        "com.sammy.malum.common.data.component.pouch.MalumPouchContentsComponent$Mutable": (
            "tryInsert(net.minecraft.world.item.ItemStack)",
            "tryTransfer(net.minecraft.world.inventory.Slot, net.minecraft.world.entity.player.Player)",
        ),
    },
    "occultism-1.21.1-neoforge-1.227.0.jar": {
        "com.klikli_dev.occultism.common.container.satchel.StorageSatchelSlot": (
            "mayPlace(net.minecraft.world.item.ItemStack)",
        ),
    },
    "Botania-Revived-1.21.1-2.0.2-NeoForge.jar": {
        "velrondevs.botania.common.item.ItemBackedInventory": (
            "net.minecraft.world.item.ItemStack stack;",
            "setChanged()",
        ),
    },
    "ExtraBotany-1.21.1-1.0.4-NeoForge.jar": {
        "velrondevs.extrabotany.client.gui.handbag.MiniHandbagMenu": (
            "MiniHandbagMenu(int, net.minecraft.world.entity.player.Inventory, net.minecraft.world.item.ItemStack)",
            "quickMoveStack(net.minecraft.world.entity.player.Player, int)",
        ),
        "velrondevs.extrabotany.common.item.MiniHandbagItem": (
            "static final int SIZE = 24;",
            "overrideStackedOnOther(",
            "overrideOtherStackedOnMe(",
        ),
    },
}


def javap(jar: Path, cls: str, *, code: bool = False) -> str:
    args = ["javap", "-classpath", str(jar), "-p", "-constants"]
    if code:
        args.append("-c")
    args.append(cls)
    run = subprocess.run(args, text=True, capture_output=True, timeout=40, check=False)
    if run.returncode:
        raise RuntimeError(f"javap failed for {jar.name}!{cls}: {run.stderr.strip()}")
    return run.stdout


def audit(dir_path: Path) -> None:
    if not dir_path.is_dir():
        raise ValueError(f"Missing verified JAR directory: {dir_path}")
    for jarname, classes in TARGETS.items():
        jar = dir_path / jarname
        if not jar.is_file():
            raise ValueError(f"Missing exact pinned JAR: {jarname}")
        for cls, expected in classes.items():
            signature = javap(jar, cls)
            for snippet in expected:
                if snippet not in signature:
                    raise AssertionError(f"{jarname}!{cls}: missing expected ABI: {snippet}")
            if cls.endswith("MiniHandbagMenu"):
                code = javap(jar, cls, code=True)
                if "new           #".replace("#", "") not in code and "new " not in code:
                    raise AssertionError("MiniHandbagMenu bytecode not inspectable")
                if "net/minecraft/world/inventory/Slot" not in code:
                    raise AssertionError("MiniHandbagMenu no longer constructs vanilla Slots")
                if "MiniHandbagItem.getInventory" not in code:
                    raise AssertionError("MiniHandbagMenu no longer opens the expected item inventory")
            print(f"PASS source ABI {jarname}!{cls}", flush=True)
    print("PASS: exact-JAR signatures still match planned targeted hooks; "
          "does NOT establish Mixin application or gameplay safety.")


if __name__ == "__main__":
    if len(sys.argv) != 2:
        raise SystemExit("usage: audit_portable_storage_abi.py <hash-verified-mods-dir>")
    audit(Path(sys.argv[1]).resolve())
