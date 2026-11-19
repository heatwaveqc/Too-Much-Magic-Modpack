#!/usr/bin/env python3
"""Read-only registry projection + exact native ABI audit. No Minecraft execution."""
import argparse
import json
import subprocess
import tempfile
from pathlib import Path
import zipfile
from fetch_compile_classpath import ROOT, read_lock, fetch

TAG = ROOT / "compat/economy/src/main/resources/data/apothic_spawners/tags/entity_type/blacklisted_from_spawners.json"
POLICY = {"blacklist_target", "approved_misc_default_deny"}

def project(registry):
    assert registry["schema_version"] == 1 and registry["registry_revision"] == 4
    entities = registry["entities"]
    assert len(entities) == registry["census"]["entity_count"] == 1763
    ids = sorted(i for i, e in entities.items() if e["capture"]["disposition"] in POLICY)
    assert len(ids) == 1240
    assert "neovitae:daemonium_doloris" not in ids
    assert not set(ids) & {"minecraft:villager", "minecraft:iron_golem", "minecraft:snow_golem"}
    assert {"minecraft:wither", "minecraft:ender_dragon"} <= set(ids)
    return {"replace": False, "values": [i if i.startswith("minecraft:") else {"id": i, "required": False} for i in ids]}

def abi(jar, clazz, required):
    output = subprocess.check_output(["javap", "-p", "-s", "-c", "-classpath", str(jar), clazz], text=True)
    print(output)
    for token in required:
        if token not in output:
            raise ValueError(f"Missing {clazz} ABI: {token}")

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--registry", type=Path, required=True)
    parser.add_argument("--projection-only", action="store_true")
    args = parser.parse_args()
    expected = project(json.loads(args.registry.read_text(encoding="utf-8")))
    assert json.loads(TAG.read_text(encoding="utf-8")) == expected
    print("PASS 1240 approved additions, additive native tag, no held or exception types")
    if args.projection_only:
        return
    names = {"ApothicSpawners-1.21.1-1.4.0.jar", "reliquary-1.21.1-2.0.85.1599.jar", "neovitae-1.21.1-1.1.53.jar"}
    pins = {p["filename"]: p for p in read_lock() if p["filename"] in names}
    assert set(pins) == names
    with tempfile.TemporaryDirectory() as tmp:
        dest = Path(tmp)
        for pin in pins.values():
            fetch(pin, dest)
        apoth = dest / "ApothicSpawners-1.21.1-1.4.0.jar"
        reliq = dest / "reliquary-1.21.1-2.0.85.1599.jar"
        neo = dest / "neovitae-1.21.1-1.1.53.jar"
        with zipfile.ZipFile(apoth) as z:
            native = json.loads(z.read("data/apothic_spawners/tags/entity_type/blacklisted_from_spawners.json"))
            print("Native blacklist:", native)
        abi(apoth, "dev.shadowsoffire.apothic_spawners.ASEvents", ["handleCapturing(net.neoforged.neoforge.event.entity.living.LivingDropsEvent)", "(Lnet/neoforged/neoforge/event/entity/living/LivingDropsEvent;)V", "SpawnEggItem.byId"])
        abi(reliq, "reliquary.item.MobCharmRegistry", ["handleAddingFragmentDrops(net.neoforged.neoforge.event.entity.living.LivingDropsEvent)", "getCharmDefinitionFor(net.minecraft.world.entity.Entity)", "(Lnet/minecraft/world/entity/Entity;)Ljava/util/Optional;"])
        abi(neo, "com.breakinblocks.neovitae.common.entity.mob.DaemoniumDolorisEntity", ["isForeman()", "()Z", "IsForeman"])
        print("PASS exact native ABI survey; inspect instance and state-transfer paths before runtime acceptance")

if __name__ == "__main__":
    main()
