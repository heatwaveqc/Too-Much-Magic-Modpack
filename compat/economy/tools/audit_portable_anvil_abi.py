#!/usr/bin/env python3
"""Offline exact-pin ABI gate for the native portable anvil adapter. No game execution."""
import hashlib
from pathlib import Path
import re
import subprocess
import sys
import tomllib
import zipfile

ROOT = Path(__file__).resolve().parents[3]
IS = "org.zeith.improvableskills."
OCC = "com.klikli_dev.occultism."

def jar(directory, slug):
    pin = tomllib.loads((ROOT / "mods" / (slug + ".pw.toml")).read_text())
    path = directory / pin["filename"]
    with path.open("rb") as f:
        digest = hashlib.file_digest(f, pin["download"]["hash-format"]).hexdigest()
    if digest.lower() != pin["download"]["hash"].lower():
        raise ValueError("Unverified pin: " + slug)
    return path

def disassemble(path, cls):
    return subprocess.run(["javap", "-p", "-s", "-c", "-classpath", str(path), cls],
                          text=True, capture_output=True, check=True).stdout

def require(text, *tokens):
    for token in tokens:
        if token not in text:
            raise ValueError("Missing reviewed native ABI token: " + token)

def main():
    directory = Path(sys.argv[1])
    skills = jar(directory, "improvable-skills")
    occultism = jar(directory, "occultism")
    packet = disassemble(skills, IS + "net.PacketOpenPortableAnvil")
    require(packet, "lambda$serverExecute$0", "descriptor: (Lnet/minecraft/server/level/ServerPlayer;Lorg/zeith/improvableskills/api/PlayerSkillData;)V",
            "AbilitiesIS.ANVIL", "PlayerSkillData.hasAbility", "GuiManager.openGuiCallback")
    portable = disassemble(skills, IS + "client.gui.abil.anvil.AnvilMenuPortable")
    require(portable, "extends net.minecraft.world.inventory.AnvilMenu", "descriptor: (ILnet/minecraft/world/entity/player/Inventory;)V", "getType()", "stillValid(")
    data = disassemble(skills, IS + "api.PlayerSkillData")
    require(data, "persistedData", "sync()", "hasAbility(")
    native = disassemble(occultism, OCC + "client.gui.IesniumAnvilMenu")
    require(native, "extends net.minecraft.world.inventory.AnvilMenu",
            "descriptor: (ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V",
            "createResult()", "onTake(", "mayPickup(", "getApothicMaxLevel", "giveExperiencePoints", "levelEvent")
    # A physical menu that actually damages/mutates the anvil block is not safe to
    # reuse at the player's real position. Require fresh review rather than guess.
    if re.search(r"\.(?:setBlock(?:AndUpdate)?|destroyBlock|removeBlock|damageAnvil):", native):
        raise ValueError("Native Iesnium menu now mutates world blocks; portable use requires review")
    with zipfile.ZipFile(occultism) as z:
        if "assets/occultism/models/item/iesnium_anvil.json" not in z.namelist():
            raise ValueError("Expected Iesnium anvil item model missing from exact pin")
    print("PASS exact IS3 native route, anvil constructors, native Iesnium cap/cost and no direct block mutation tokens.")
    print("Native Iesnium bytecode for review:\n" + native)
    print("Type A only: no Mixin application, native registry, UI, save or game behavior verified.")

if __name__ == "__main__":
    main()
