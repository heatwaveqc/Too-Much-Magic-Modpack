#!/usr/bin/env python3
"""Type A pinned-bytecode guard for the two Neo Vitae flask dose hazards.

Verifies actual hash-pinned Iron's/Occultism/Neo Vitae binaries and checks
target methods/conversion operations without booting Minecraft or installing
the pack. The on-disk mod jars are read-only compile dependencies.
"""
from __future__ import annotations

import argparse
import hashlib
import subprocess
import tomllib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[3]


def require(text: str, needle: str, context: str) -> None:
    if needle not in text:
        raise SystemExit(f"FAIL {context}: missing {needle!r}")


def exact_jar(path: Path, slug: str) -> None:
    metafile = ROOT / "mods" / f"{slug}.pw.toml"
    pin = tomllib.loads(metafile.read_text())
    if path.name != pin["filename"]:
        raise SystemExit(f"FAIL {slug}: wrong JAR filename {path.name}")
    algo = pin["download"]["hash-format"]
    with path.open("rb") as f:
        actual = hashlib.file_digest(f, algo).hexdigest()
    if actual.lower() != pin["download"]["hash"].lower():
        raise SystemExit(f"FAIL {slug}: pinned hash mismatch")


def javap(jar: Path, classname: str) -> str:
    return subprocess.run(
        ["javap", "-classpath", str(jar), "-p", "-c", "-s", classname],
        check=True, text=True, capture_output=True, timeout=40,
    ).stdout


def main() -> None:
    a = argparse.ArgumentParser()
    a.add_argument("irons", type=Path)
    a.add_argument("occultism", type=Path)
    a.add_argument("neovitae", type=Path)
    args = a.parse_args()
    for jar, slug in (
        (args.irons, "irons-spells-n-spellbooks"),
        (args.occultism, "occultism"),
        (args.neovitae, "neovitae"),
    ):
        exact_jar(jar, slug)

    irons = javap(
        args.irons,
        "io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronTile",
    )
    for needle in (
        "tryExecuteRecipeInteractions(net.minecraft.world.level.Level, net.minecraft.world.item.ItemStack)",
        "(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
        "handleUse(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.entity.player.Player, net.minecraft.world.InteractionHand)",
        "DataComponents.POTION_CONTENTS",
        "GLASS_BOTTLE",
        "FillAlchemistCauldronRecipe",
    ):
        require(irons, needle, "Iron's 3.16.3 unsafe generic cauldron fill")

    partner = javap(
        args.occultism,
        "com.klikli_dev.occultism.common.entity.spirit.demonicpartner.DemonicPartner",
    )
    for needle in (
        "mobInteract(net.minecraft.world.entity.player.Player, net.minecraft.world.InteractionHand)",
        "(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
        "DataComponents.POTION_CONTENTS",
        "ItemStack.shrink",
        "GLASS_BOTTLE",
    ):
        require(partner, needle, "Occultism 1.227.0 partner whole-item potion debit")

    flask = javap(
        args.neovitae,
        "com.breakinblocks.neovitae.common.item.potion.ItemAlchemyFlask",
    )
    for needle in (
        "getRemainingUses(net.minecraft.world.item.ItemStack)",
        "DataComponents.POTION_CONTENTS",
        "NVDataComponents.FLASK_EFFECTS",
    ):
        require(flask, needle, "Neo Vitae 1.1.54 multiuse flask and mirrored potion")

    resources = Path(__file__).resolve().parents[1] / "src/main/resources"
    mixins = (resources / "tmm_economy.mixins.json").read_text()
    for name in ("IronsAlchemistFlaskSafetyMixin", "OccultismPartnerFlaskSafetyMixin"):
        if mixins.count(f'"{name}"') != 1:
            raise SystemExit(f"FAIL mixin registration: {name}")

    print("PASS: exact pinned Iron's, Occultism and Neo Vitae ABI and two guarded injectors; no game boot")


if __name__ == "__main__":
    main()
