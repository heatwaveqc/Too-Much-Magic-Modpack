#!/usr/bin/env python3
"""Read-only registry projection + exact native ABI audit. No Minecraft execution."""
import argparse
import json
import subprocess
import tempfile
from pathlib import Path
import zipfile
import fetch_compile_classpath as locked
from fetch_compile_classpath import ROOT, read_lock, fetch

TAG = ROOT / "compat/economy/src/main/resources/data/tmm/tags/entity_type/capture_blacklist.json"
POLICY = {"blacklist_target", "approved_misc_default_deny"}

def project(registry):
    assert registry["schema_version"] == 1 and registry["registry_revision"] == 6
    entities = registry["entities"]
    assert len(entities) == registry["census"]["entity_count"] == 1763
    ids = sorted(i for i, e in entities.items() if e["capture"]["disposition"] in POLICY)
    assert len(ids) == 1196
    assert "neovitae:daemonium_doloris" not in ids
    assert not set(ids) & {"minecraft:villager", "minecraft:iron_golem", "minecraft:snow_golem"}
    assert {"minecraft:wither", "minecraft:ender_dragon", "occultism:marid_unbound", "occultism:afrit_wild", "neovitae:necromancy_summon"} <= set(ids)
    assert not any(i.startswith(("occultism:possessed_", "occultism:wild_")) for i in ids)
    assert not set(ids) & {"minecraft:warden", "minecraft:elder_guardian", "minecraft:vex", "occultism:iesnium_golem", "artifacts:mimic", "goety:irk"}
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
    registry = json.loads(args.registry.read_text(encoding="utf-8"))
    expected = project(registry)
    assert json.loads(TAG.read_text(encoding="utf-8")) == expected
    print("PASS 1196 common additions, additive TMM tag, no held or exception types")
    spirits = sorted(i for i, e in registry["entities"].items() if e["capture"]["disposition"] == "transport_only")
    assert set(spirits) == {"occultism:afrit", "occultism:djinni", "occultism:foliot", "occultism:marid"}
    assert not set(spirits) & {v if isinstance(v, str) else v["id"] for v in expected["values"]}
    assert json.loads((TAG.parent / "replication_blacklist.json").read_text(encoding="utf-8")) == {"replace": False, "values": ["#tmm:capture_blacklist"] + [{"id": i, "required": False} for i in spirits]}
    print("PASS 4 bound worker transport exceptions, 1200 total replication denials")
    consumers = ["apothic_spawners:blacklisted_from_spawners","occultism:soul_gem_deny_list","occultism:fragile_soul_gem_deny_list","ars_nouveau:jar_blacklist","ars_nouveau:drygmy_blacklist","forbidden_arcanus:quantum_catcher_blacklisted"]
    for key in consumers:
        ns, name = key.split(":")
        path = TAG.parents[3] / ns / "tags/entity_type" / (name + ".json")
        expected_tag = "#tmm:replication_blacklist" if key in {"apothic_spawners:blacklisted_from_spawners", "ars_nouveau:drygmy_blacklist"} else "#tmm:capture_blacklist"
        assert json.loads(path.read_text(encoding="utf-8")) == {"replace": False, "values": [expected_tag]}
    print("PASS 6 native consumer aliases; Boss Catcher retains its native player-only restriction")
    neo_tag = TAG.parents[3] / "neovitae/tags/entity_type/deny_imprisonment.json"
    assert json.loads(neo_tag.read_text(encoding="utf-8")) == {"replace": False, "values": [{"id": "#apothic_spawners:blacklisted_from_spawners", "required": False}]}
    print("PASS Neo Vitae imprisonment shares effective native spawner blacklist")
    if args.projection_only:
        return
    names = {"ApothicSpawners-1.21.1-1.4.0.jar", "reliquary-1.21.1-2.0.85.1599.jar", "neovitae-1.21.1-1.1.53.jar"}
    # Audit exact native tag resources for all seven consumers before gameplay acceptance.
    locked.SLUGS.extend(["occultism", "ars-ocultas"])
    names |= {"ars_nouveau-1.21.1-5.13.5.jar", "occultism-1.21.1-neoforge-1.227.0.jar", "forbidden_arcanus-2.6.1.jar", "ars_ocultas-1.21.1-2.6.2.jar"}
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
        with zipfile.ZipFile(neo) as z:
            native_neo = json.loads(z.read("data/neovitae/tags/entity_type/deny_imprisonment.json"))
            assert {"minecraft:ender_dragon", "minecraft:wither", "minecraft:warden"} <= set(native_neo["values"])
        abi(neo, "com.breakinblocks.neovitae.common.event.ImprisonmentArrayHandler", ["static void onLivingDeath(net.neoforged.neoforge.event.entity.living.LivingDeathEvent)", "(Lnet/neoforged/neoforge/event/entity/living/LivingDeathEvent;)V", "DENY_IMPRISONMENT", "SpawnerBlockEntity.setEntityId"])
        consumer_jars = {"apothic_spawners": apoth, "ars_nouveau": dest / "ars_nouveau-1.21.1-5.13.5.jar", "occultism": dest / "occultism-1.21.1-neoforge-1.227.0.jar", "forbidden_arcanus": dest / "forbidden_arcanus-2.6.1.jar"}
        for key in consumers:
            ns, name = key.split(":")
            path = f"data/{ns}/tags/entity_type/{name}.json"
            with zipfile.ZipFile(consumer_jars[ns]) as z:
                native = json.loads(z.read(path))
                print("Exact native consumer tag", key, json.dumps(native, sort_keys=True))
        with zipfile.ZipFile(consumer_jars["forbidden_arcanus"]) as z:
            boss_tag = json.loads(z.read("data/forbidden_arcanus/tags/entity_type/boss_catcher_blacklisted.json"))
            quantum_tag = json.loads(z.read("data/forbidden_arcanus/tags/entity_type/quantum_catcher_blacklisted.json"))
            assert boss_tag.get("values") == ["minecraft:player"], "Boss Catcher native semantics changed"
            assert "#forbidden_arcanus:boss_catcher_blacklisted" in quantum_tag.get("values", [])
            assert "#c:bosses" in quantum_tag.get("values", [])
            for catcher in ("quantum_catcher", "boss_catcher"):
                path = "data/forbidden_arcanus/forbidden_arcanus/hephaestus_forge/ritual/" + catcher + ".json"
                print("Exact native catcher recipe", catcher, json.dumps(json.loads(z.read(path)), sort_keys=True))
        abi(consumer_jars["forbidden_arcanus"], "com.stal111.forbidden_arcanus.common.item.QuantumCatcherItem", ["isValidEntity(net.minecraft.world.entity.LivingEntity)", "calculateAurealCost(net.minecraft.world.entity.LivingEntity)", "blacklistedEntities"])
        with zipfile.ZipFile(consumer_jars["ars_nouveau"]) as z:
            print("Exact native jar whitelist", z.read("data/ars_nouveau/tags/entity_type/jar_whitelist.json").decode())
        with zipfile.ZipFile(consumer_jars["occultism"]) as z:
            print("Exact native gem tag files", sorted(n for n in z.namelist() if "/tags/entity_type/" in n and "gem" in n))
        with zipfile.ZipFile(dest / "ars_ocultas-1.21.1-2.6.2.jar") as z:
            print("Transfer implementation candidates", sorted(n for n in z.namelist() if n.endswith(".class") and any(w in n.lower() for w in ("gem", "jar", "capture", "containment", "conversion"))))
        print("PASS exact native ABI survey; inspect instance and state-transfer paths before runtime acceptance")

if __name__ == "__main__":
    main()
