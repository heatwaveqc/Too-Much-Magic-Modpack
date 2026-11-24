#!/usr/bin/env python3
"""Offline signature/locale survey of three exact-pinned mods, no mod code execution."""
from pathlib import Path
import hashlib
import re
import subprocess
import sys
import tomllib
import urllib.parse
import zipfile

CHECKS = {
    "ars-nouveau": {
        "com.hollingsworth.arsnouveau.common.crafting.recipes.GlyphRecipe": ("getInputs", "getOutput", "getExp"),
    },
    "theurgy": {
        "com.klikli_dev.theurgy.content.recipe.DigestionRecipe": ("getSizedIngredients", "getFluid"),
        "com.klikli_dev.theurgy.content.recipe.FermentationRecipe": ("getIngredients", "getFluid"),
        "com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe": ("getIngredient", "getSolvent"),
        "com.klikli_dev.theurgy.content.recipe.IncubationRecipe": ("getMercury", "getSalt", "getSulfur"),
        "com.klikli_dev.theurgy.content.recipe.AccumulationRecipe": ("hasSolute", "getSolute", "hasEvaporant", "getEvaporant", "getResult"),
        "com.klikli_dev.theurgy.content.recipe.CatalysationRecipe": ("getIngredient", "getTotalMercuryFlux"),
        "com.klikli_dev.theurgy.content.recipe.ReformationRecipe": ("getSources", "getTarget", "getResult", "getMercuryFlux"),
    },
    "pastel": {
        "earth.terrarium.pastel.recipe.crystallarieum.CrystallarieumRecipe": ("getGrowthStages", "getIngredientStack", "getSecondsPerGrowthStage", "getInkColor", "getInkPerSecond", "getFluidMedium", "getCatalysts", "growsWithoutCatalyst"),
        "earth.terrarium.pastel.recipe.crystallarieum.CrystallarieumCatalyst": ("ingredient", "growthAccelerationMod", "inkConsumptionMod", "consumeChancePerSecond"),
        "earth.terrarium.pastel.recipe.cinderhearth.CinderhearthRecipe": ("getResultsWithChance", "getCraftingTime"),
        "earth.terrarium.pastel.recipe.fusion_shrine.FusionShrineRecipe": ("getFluid", "getResultItem"),
        "earth.terrarium.pastel.components.InkStorageComponent": ("storedEnergy",),
        "earth.terrarium.pastel.registries.PastelDataComponentTypes": (),
        "earth.terrarium.pastel.blocks.cinderhearth.CinderhearthBlockEntity": (),
    },
}

def main():
    if len(sys.argv) != 2:
        raise SystemExit("usage: audit-specialized-mappers.py <temporary-download-directory>")
    cache = Path(sys.argv[1])
    cache.mkdir(parents=True, exist_ok=True)
    failed = []
    for mod, classes in CHECKS.items():
        pin = tomllib.loads(Path("mods", mod + ".pw.toml").read_text(encoding="utf-8"))
        jar = cache / pin["filename"]
        download = pin["download"]
        url = download.get("url")
        if url is None and download.get("mode") == "metadata:curseforge":
            fid = int(pin["update"]["curseforge"]["file-id"])
            url = f"https://edge.forgecdn.net/files/{fid//1000}/{fid%1000:03d}/{urllib.parse.quote(jar.name)}"
        if not url:
            raise ValueError("no provider URL for " + mod)
        subprocess.run(["curl", "--fail", "--location", "--retry", "1", "--max-time", "90", "--output", str(jar), url], check=True)
        algorithm = download["hash-format"]
        with jar.open("rb") as f:
            digest = hashlib.file_digest(f, algorithm).hexdigest()
        if digest.lower() != download["hash"].lower():
            raise ValueError(f"pin hash mismatch {mod}: {digest}")
        print(f"VERIFIED {jar.name} {algorithm} {digest}", flush=True)
        with zipfile.ZipFile(jar) as z:
            names = set(z.namelist())
            for name, methods in classes.items():
                if name.replace(".", "/") + ".class" not in names:
                    failed.append(name + " (missing class)")
                    continue
                result = subprocess.run(["javap", "-classpath", str(jar), "-p", "-s", name],
                                        check=True, text=True, capture_output=True).stdout
                print(result, flush=True)
                for method in methods:
                    if not re.search(r"\b" + re.escape(method) + r"\(", result):
                        failed.append(name + "#" + method)
                if name.endswith("PastelDataComponentTypes") and "INK_STORAGE;" not in result:
                    failed.append(name + "#INK_STORAGE")
                if name.endswith("CinderhearthBlockEntity") and "INK_COST_PER_TICK;" not in result:
                    failed.append(name + "#INK_COST_PER_TICK")
            if mod == "pastel":
                # For later input/output codec and loot review, list exact paths,
                # not copyrighted full recipe data or binaries.
                for prefix in ("data/pastel/recipe/crystallarieum/", "data/pastel/recipe/fusion_shrine/", "data/pastel/recipe/cinderhearth/"):
                    candidates = sorted(n for n in names if n.startswith(prefix) and n.endswith(".json"))
                    print(prefix, len(candidates), *candidates[:8], sep="\n", flush=True)
        jar.unlink()
    if failed:
        raise SystemExit("Unverified required API:\n" + "\n".join(failed))
    print("All targeted exact-pin API symbol checks passed. No compile or game boot.")

if __name__ == "__main__":
    main()
