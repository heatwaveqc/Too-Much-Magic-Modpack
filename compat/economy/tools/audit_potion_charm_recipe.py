#!/usr/bin/env python3
"""Type-A, no-game-boot ABI/source boundary audit of pinned Apotheosis 8.9.0."""
from __future__ import annotations

import argparse
import hashlib
import json
import subprocess
from pathlib import Path

PINNED_SHA512 = "b3022bc31f4e9f99d298c36486e09cad308d4aa87e2aa65a341809aeca41f0067e744086768884959036a46685d4c0babc4274600884ad8962fe9d080d8ef3ee"


def require(source: str, needle: str) -> None:
    if needle not in source:
        raise SystemExit(f"FAIL Apotheosis 8.9.0 recipe ABI: missing {needle!r}")


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("apotheosis", type=Path)
    args = parser.parse_args()
    actual = hashlib.sha512(args.apotheosis.read_bytes()).hexdigest()
    if args.apotheosis.name != "Apotheosis-1.21.1-8.9.0.jar" or actual != PINNED_SHA512:
        raise SystemExit("FAIL: must audit exact TMM pinned Apotheosis 8.9.0 JAR")

    classname = "dev.shadowsoffire.apotheosis.recipe.PotionCharmRecipe"
    recipe = subprocess.run(
        ["javap", "-classpath", str(args.apotheosis), "-p", "-c", "-s", classname],
        text=True, capture_output=True, check=True, timeout=25,
    ).stdout
    for name in (
        "findPotion(net.minecraft.world.item.crafting.CraftingInput)",
        "(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/world/item/alchemy/PotionContents;",
        "assemble(net.minecraft.world.item.crafting.CraftingInput, net.minecraft.core.HolderLookup$Provider)",
        "(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
        "PotionCharmItem.isValidPotion",
        "PotionContents.potion",
        "DataComponents.POTION_CONTENTS",
    ):
        require(recipe, name)

    charm = subprocess.run(
        ["javap", "-classpath", str(args.apotheosis), "-p", "-c", "-s",
         "dev.shadowsoffire.apotheosis.item.PotionCharmItem"],
        text=True, capture_output=True, check=True, timeout=25,
    ).stdout
    for name in ("getAllEffects", "isInstantenous", "setNoRepair", "POTION_CHARM_BLACKLIST"):
        require(charm, name)

    for name in ("public static boolean isValidPotion(net.minecraft.core.Holder<net.minecraft.world.item.alchemy.Potion>)",
                 "(Lnet/minecraft/core/Holder;)Z"):
        require(charm, name)

    base = Path(__file__).resolve().parents[1]
    mixin = (base / "src/main/java/io/github/heatwaveqc/tmm/economy/mixin/ApotheosisPotionCharmRecipeMixin.java").read_text()
    bridge = (base / "src/main/java/io/github/heatwaveqc/tmm/economy/ApotheosisCharmPotionBridge.java").read_text()
    config = json.loads((base / "src/main/resources/tmm_economy.mixins.json").read_text())
    if config["mixins"].count("ApotheosisPotionCharmRecipeMixin") != 1:
        raise SystemExit("FAIL Apotheosis mixin not registered exactly once")
    discovery = (base / "src/main/java/io/github/heatwaveqc/tmm/economy/mixin/ApotheosisPotionCharmEligibilityMixin.java").read_text()
    if config["mixins"].count("ApotheosisPotionCharmEligibilityMixin") != 1:
        raise SystemExit("FAIL shared viewer eligibility mixin not registered exactly once")
    for name in ("isValidPotion(Lnet/minecraft/core/Holder;)Z", "potion.isBound()",
                 "ApotheosisCharmPotionBridge.permits(new PotionContents(potion))"):
        require(discovery, name)
    require(bridge, "public static boolean permits(PotionContents contents)")
    for name in ("findPotion(", "assemble(", "charm.set(DataComponents.POTION_CONTENTS, contents)", "PotionContents.EMPTY"):
        require(mixin, name)
    for name in ("Items.POTION", "getComponentsPatch", "getAllEffects", "isInstantenous",
                 "MOB_EFFECT.getKey", "found.equals(contents)", "matchingBottles(bottles)"):
        require(bridge, name)
    for name in ("isValidPotion", "POTION_CHARM_BLACKLIST", "potion_charm_denied",
                 "potion_charm_custom_effect_allowlist", "privilegedAmplifier", "neovitae:flight",
                 "minecraft:bad_omen", "minecraft:raid_omen", "minecraft:trial_omen"):
        if name in bridge:
            raise SystemExit(f"FAIL obsolete charm-specific restriction: {name}")
    for name in ("potion_charm_denied", "potion_charm_custom_effect_allowlist"):
        if (base / f"src/main/resources/data/tmm/tags/mob_effect/{name}.json").exists():
            raise SystemExit(f"FAIL obsolete charm tag: {name}")

    print("PASS: exact pinned Apotheosis charm recipe ABI and narrow source boundaries; no game boot")


if __name__ == "__main__":
    main()
