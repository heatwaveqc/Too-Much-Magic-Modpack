#!/usr/bin/env python3
"""Build a mod-JAR-free Prism Launcher import ZIP for one pinned packwiz snapshot.

The ZIP contains only the MIT-licensed packwiz installer bootstrap and Prism
instance metadata. On first launch packwiz downloads the mods from original
hosts and checks their hashes, including client-only mods.
"""
from __future__ import annotations
import hashlib
import json
import pathlib
import sys
import zipfile

REPOSITORY="heatwaveqc/Too-Much-Magic-Modpack"
BOOT_NAME="packwiz-installer-bootstrap.jar"
ZIP_NAME="Too-Much-Magic-B7-Inventory-Sorting-Prism.zip"
def main(bootstrap,output_dir,snapshot):
    source=f"https://raw.githubusercontent.com/{REPOSITORY}/{snapshot}/pack.toml"
    jar=pathlib.Path(bootstrap)
    out=pathlib.Path(output_dir)/ZIP_NAME
    data=jar.read_bytes()
    if len(data)<90_000 or not data.startswith(b"PK"):
        raise RuntimeError("Unexpected installer bootstrap binary, refusing to package")
    instance={
        "formatVersion":1,
        "components":[
            {"uid":"net.minecraft","version":"1.21.1","important":True},
            {"uid":"net.neoforged","version":"21.1.265"}
        ]
    }
    cfg=f"""[General]
InstanceType=OneSix
name=Too Much Magic - B7 Local Client Test
OverrideCommands=true
PreLaunchCommand="$INST_JAVA" -jar {BOOT_NAME} {source}
OverrideMemory=true
MinMemAlloc=2048
MaxMemAlloc=8192
"""
    instructions=f"""TOO MUCH MAGIC — B7 CLIENT TEST
=================================

Import this ZIP into Prism Launcher:
1. Add Instance > Import, choose this ZIP.
2. In Prism's Java settings, set Java 21 (Temurin or compatible).
3. This instance is configured with up to 8 GiB RAM; adjust if needed.
4. Launch. The MIT-licensed packwiz bootstrapper automatically downloads the
   exact {snapshot} pack manifest and every client/both-side mod from its
   original hosting provider. Mods are NOT included in this ZIP.
5. If a CurseForge file requires manual download, follow the installer prompt
   and use the exact file it names. Do not replace missing mods with substitutes.
6. Create a NEW throwaway survival/creative test world; do not open old saves.
7. Check inventory middle-click sorting with a vanilla chest and personal\n   inventory, that automatic tool restocking works, and that Mouse Tweaks drag\n   and scroll still behave. Then check menus, Astral sky, Botania, Pufferfish\n   Skills, Improvable Skills,
   new crop/fruit tree generation, right-click crop harvesting, recipes, and
   client rendering (Embeddium), then send a screenshot/console log on failure.

The custom Pufferfish skill tree is NOT implemented yet; the two Pufferfish
framework mods can be verified for client loading now.
ProjectE/Project Expansion's Chaos Guardian acquisition restrictions are NOT
implemented; this is a CLIENT COMPATIBILITY TEST, not a balanced survival pack.

Pinned packwiz source:
{source}
Packwiz files / changelog:
https://github.com/{REPOSITORY}/tree/{snapshot}

If a launcher prompts you to trust arbitrary downloads, only accept files
from expected provider hostnames. This ZIP does not contain any third-party
Minecraft mod JARs.
"""
    out.parent.mkdir(parents=True,exist_ok=True)
    with zipfile.ZipFile(out,"w",compression=zipfile.ZIP_DEFLATED) as z:
        z.writestr("instance.cfg",cfg)
        z.writestr("mmc-pack.json",json.dumps(instance,indent=2)+"\n")
        z.writestr("minecraft/"+BOOT_NAME,data)
        z.writestr("INSTALL-AND-TEST.txt",instructions)
    with zipfile.ZipFile(out) as z:
        names=set(z.namelist())
        assert names=={"instance.cfg","mmc-pack.json","minecraft/"+BOOT_NAME,"INSTALL-AND-TEST.txt"}
        assert all(not name.startswith("minecraft/mods/") for name in names)
        assert json.loads(z.read("mmc-pack.json"))["components"][1]["version"]=="21.1.265"
        assert source.encode() in z.read("instance.cfg")
        assert z.read("minecraft/"+BOOT_NAME)==data
    print(json.dumps({
      "zip_path":str(out),"size":out.stat().st_size,
      "sha256":hashlib.sha256(out.read_bytes()).hexdigest(),
      "bootstrap_sha256":hashlib.sha256(data).hexdigest(),
      "mod_jar_count":0,"packwiz_snapshot":snapshot,
      "minecraft":"1.21.1","neoforge":"21.1.265","source":source
    },indent=2),flush=True)
if __name__=="__main__":
    if len(sys.argv)!=4:sys.exit("Usage: build_b7_prism.py BOOTSTRAP_JAR OUTPUT_DIR SNAPSHOT_SHA")
    main(sys.argv[1],sys.argv[2],sys.argv[3])
