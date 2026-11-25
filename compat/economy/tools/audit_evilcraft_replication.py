#!/usr/bin/env python3
"""Read-only exact-pin EvilCraft replication audit, independent of other capture consumers."""
import argparse
import hashlib
import json
from pathlib import Path
import re
import subprocess
import sys
import tempfile
import tomllib

ROOT = Path(__file__).resolve().parents[3]
ECONOMY = ROOT / "compat/economy"
JAVA = ECONOMY / "src/main/java/io/github/heatwaveqc/tmm/economy"
RES = ECONOMY / "src/main/resources"
TAGS = RES / "data/tmm/tags/entity_type"
TARGETS = {
    "EvilCraftVengeanceSpiritMixin": (
        "org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit",
        "canSustain", "(Lnet/minecraft/world/entity/LivingEntity;)Z"),
    "EvilCraftRandomSpiritMixin": (
        "org.cyclops.evilcraft.entity.monster.EntityVengeanceSpiritData",
        "getRandomInnerEntity", "(Lnet/minecraft/util/RandomSource;)Lnet/minecraft/world/entity/EntityType;"),
    "EvilCraftSpiritFurnaceMixin": (
        "org.cyclops.evilcraft.blockentity.tickaction.spiritfurnace.BoxCookTickAction",
        "canTick", "(Lorg/cyclops/evilcraft/blockentity/BlockEntitySpiritFurnace;Lnet/minecraft/world/item/ItemStack;II)Z"),
    "EvilCraftSpiritReanimatorMixin": (
        "org.cyclops.evilcraft.blockentity.tickaction.spiritreanimator.ReanimateTickAction",
        "canTick", "(Lorg/cyclops/evilcraft/blockentity/BlockEntitySpiritReanimator;Lnet/minecraft/world/item/ItemStack;II)Z"),
}

def require(condition, message):
    if not condition:
        raise ValueError(message)

def digest(path, algorithm):
    with path.open("rb") as stream:
        return hashlib.file_digest(stream, algorithm).hexdigest()

def projection(command):
    return json.loads(subprocess.check_output(
        [sys.executable, str(ROOT / "docs/creatures/registry_tool.py"), command], text=True))

def verify_sources():
    for name, command in (("capture_blacklist", "capture-tag"), ("replication_blacklist", "replication-tag")):
        data = json.loads((TAGS / (name + ".json")).read_text())
        require(data == projection(command), f"{name} drifted from current creature registry")
        print(f"PASS {name}: exact canonical registry projection ({len(data['values'])} entries)")
    mixins = json.loads((RES / "tmm_economy.mixins.json").read_text())["mixins"]
    for name, (target, method, descriptor) in TARGETS.items():
        require(mixins.count(name) == 1, f"{name} not uniquely registered")
        src = (JAVA / "mixin" / (name + ".java")).read_text()
        require(target in src and method + descriptor in src and 'at = @At("HEAD")' in src
                and "cancellable = true" in src and "EvilCraftReplicationPolicy." in src,
                f"{name} source contract changed")
    policy = (JAVA / "EvilCraftReplicationPolicy.java").read_text()
    require('"replication_blacklist"' in policy and 'type.is(BLACKLIST)' in policy
            and 'getTag(BLACKLIST).isEmpty()' in policy, "shared live tag guard changed")
    print("PASS all four native guards use the shared policy; no per-mob Java list")

def pinned_artifact():
    pack = tomllib.loads((ROOT / "pack.toml").read_text())
    index = ROOT / pack["index"]["file"]
    require(digest(index, pack["index"]["hash-format"]) == pack["index"]["hash"], "index hash mismatch")
    row = next(r for r in tomllib.loads(index.read_text())["files"] if r["file"] == "mods/evilcraft.pw.toml")
    pin = ROOT / row["file"]
    require(row.get("metafile") and digest(pin, "sha256") == row["hash"], "EvilCraft pin hash mismatch")
    return tomllib.loads(pin.read_text())

def verify_abi(jar, pin):
    lock = pin["download"]
    require(digest(jar, lock["hash-format"]) == lock["hash"], "wrong EvilCraft artifact bytes")
    def signature(target, method, descriptor):
        out = subprocess.check_output(["javap", "-classpath", str(jar), "-p", "-s", target], text=True)
        require(re.search(r"(?m)^  .*\b" + re.escape(method) + r"\([^\n]*\);\s*\n\s*descriptor: " + re.escape(descriptor) + r"\s*$", out),
                f"ABI changed: {target}.{method}{descriptor}")
        print(f"PASS exact ABI {target}.{method}{descriptor}")
    for target, method, descriptor in TARGETS.values():
        signature(target, method, descriptor)
    signature("org.cyclops.evilcraft.blockentity.BlockEntitySpiritFurnace", "getEntity", "()Lnet/minecraft/world/entity/Entity;")
    signature("org.cyclops.evilcraft.blockentity.BlockEntitySpiritReanimator", "getEntityType", "()Lnet/minecraft/world/entity/EntityType;")

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--jar", type=Path, help="Use an already available exact pinned artifact")
    args = parser.parse_args()
    verify_sources()
    pin = pinned_artifact()
    if args.jar:
        verify_abi(args.jar, pin)
    else:
        with tempfile.TemporaryDirectory(prefix="tmm-evilcraft-abi-") as work:
            jar = Path(work) / pin["filename"]
            url = pin["download"]["url"]
            require(url.startswith("https://cdn.modrinth.com/"), "unreviewed EvilCraft artifact host")
            subprocess.run(["curl", "--fail", "--location", "--retry", "1", "--proto", "=https",
                            "--proto-redir", "=https", "--max-time", "120", "--output", str(jar), url], check=True)
            verify_abi(jar, pin)
    print("TYPE A PASS: source and exact pinned ABI only; no Minecraft launch or gameplay acceptance")

if __name__ == "__main__":
    main()
