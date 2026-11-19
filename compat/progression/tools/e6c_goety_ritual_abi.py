"""E6c Goety + Cataclysm exact artifact ritual census; static only."""
import pathlib, tempfile, zipfile, json, subprocess, re, tomllib
import hashlib, urllib.request
def download(url, expected):
    # Goety is larger than the E6b 80 MB cap; retain bounded download and exact hash.
    req=urllib.request.Request(url,headers={"User-Agent":"TMM-static-ritual-audit/1.0"})
    with urllib.request.urlopen(req,timeout=120) as response:
        data=response.read(250_000_001)
    actual=hashlib.sha512(data).hexdigest()
    if len(data)>250_000_000 or actual!=expected:
        raise RuntimeError(f"Artifact rejected: bytes={len(data)} sha512={actual}")
    return data
PINS={
"goety":("https://cdn.modrinth.com/data/4ZVIxU8x/versions/xLTYSsZD/goety-3.2.2.jar","d68103012faad1b74cf96017aa22711d80033d04035e98e5452f4f70d6fbc30c6523b4d3f561fedba798e116055caf598432b472b13e8a11dca07d00468b8b46"),
"goety_cataclysm":("https://cdn.modrinth.com/data/YwmuwbXi/versions/gv1JY0TU/goety_cataclysm-1.21.1-1.8.4.jar","45e390fb44ea7aa6b5f81b95b57ca224489a527a66167de1bdcf9a53c71cb5a54f6d29b69fa30b3f342e853c30bee677cf2c3701d141edf76b0be8515b6b6b60")}
actual={}
old_count=0
with tempfile.TemporaryDirectory() as d:
    for key,(url,sha) in PINS.items():
        jar=pathlib.Path(d)/(key+".jar")
        jar.write_bytes(download(url,sha))
        print("VERIFIED",key,sha,flush=True)
        with zipfile.ZipFile(jar) as z:
            print("METADATA",key,tomllib.loads(z.read("META-INF/neoforge.mods.toml").decode())["mods"],flush=True)
            if "META-INF/MANIFEST.MF" in z.namelist():
                for line in z.read("META-INF/MANIFEST.MF").decode(errors="replace").splitlines():
                    if line.startswith("Implementation-Version:"): print("VERSION",key,line,flush=True)
            counts={}
            classes=[]
            for n in sorted(z.namelist()):
                if re.fullmatch(r"data/[^/]+/recipes?/.*\.json",n):
                    obj=json.loads(z.read(n))
                    if isinstance(obj,dict) and obj.get("type")=="goety:ritual":
                        typ=str(obj.get("ritual_type",obj.get("ritualType","default")))
                        counts[typ]=counts.get(typ,0)+1
                        if "/recipe/" in n:
                            parts=n.split("/")
                            recipe=parts[1]+":"+"/".join(parts[3:])[:-5]
                            assert recipe not in actual
                            actual[recipe]=obj
                        else: old_count+=1
                        print("RECIPE",key,n,json.dumps(obj,separators=(",",":")),flush=True)
                elif n.endswith(".class") and not n.startswith("META-INF/") and (re.search(r"/(?:DarkAltarBlock(?:Entity)?|StormEntity|RitualRecipe|Ritual|RitualRequirements|RitualType|RitualTypes|(?:\w+)Ritual)\.class$",n) or (key=="goety_cataclysm" and "ritual" in n.lower())):
                    classes.append(n[:-6].replace("/","."))
            print("COUNTS",key,json.dumps(counts),flush=True)
            print("CLASSES",key,json.dumps(classes),flush=True)
            assert sum(counts.values())>0
        for cls in classes:
            out=subprocess.run(["javap","-classpath",str(jar),"-p","-s","-c",cls],capture_output=True,text=True,check=True,timeout=60).stdout
            expected={
                "DarkAltarBlockEntity":("Method startRitual:","Method com/Polarice3/Goety/common/ritual/Ritual.finish:","Method clearRitual:()V","Field currentRitualRecipeId:","Field castingPlayerId:"),
                "CraftItemRitual":("InterfaceMethod net/neoforged/neoforge/items/IItemHandler.insertItem:",),
                "LocateRitual":("InterfaceMethod net/neoforged/neoforge/items/IItemHandler.insertItem:","Method net/minecraft/world/level/saveddata/maps/MapItemSavedData.addTargetDecoration:"),
                "SummonRitual":("Method createSummonedEntity:","Method net/minecraft/world/level/Level.addFreshEntity:"),
                "ConvertRitual":("Method com/Polarice3/Goety/utils/MobUtil.convertTo:",),
                "EnchantItemRitual":("Method net/minecraft/world/item/enchantment/EnchantmentHelper.getEnchantmentsForCrafting:",),
                "StormEntity":("Method net/minecraft/server/level/ServerLevel.setWeatherParameters:(IIZZ)V","Method discard:()V"),
                "TeleportRitual":("Method net/minecraft/world/entity/player/Player.teleportTo:(DDD)V",),
            }.get(cls.rsplit(".",1)[-1],())
            for token in expected: assert token in out,(cls,token)
            print("CLASS",cls,flush=True)
            active=False
            for line in out.splitlines():
                if line.startswith("  ") and not line.startswith("    ") and line.rstrip().endswith(";"):
                    print("DECL",line.strip(),flush=True)
                    active=bool(re.search(r"finish|start|interrupt|reset|complete|tick\(|use|save|load|ritual|convert|summon|craft|spawn|setOwner|setItem|setPlayer",line,re.I))
                elif line.strip().startswith("descriptor:") or (active and line.lstrip().split(":",1)[0].isdigit() and ("DarkAltarBlockEntity" in cls or "//" in line)):
                    print(line,flush=True)

assert len(actual)==409 and old_count==336
catalog=(pathlib.Path(__file__).resolve().parents[1]/"src/main/java/io/github/heatwaveqc/tmm/progression/mxp/core/GoetyRitualCatalog.java").read_text()
mapped={}
for row in re.findall(r'add\(out,"([^"]+)","[^"]+",Band\.GOETY_\w+,Kind\.(\w+),"([^"]+)","([^"]+)",(\d+),"([^"]+)",(\d+),(\d+),(\d+),"([^"]*)","([^"]*)","([^"]*)",(\d+)\);',catalog):
    key,kind,typ,output,count,craft,souls,duration,ingredients,research,sacrifice,convert,xp=row
    assert key not in mapped
    mapped[key]=(typ,output,int(count),craft,int(souls),int(duration),int(ingredients),research,sacrifice,convert,int(xp))
    obj=actual[key]
    native_type=obj["ritual_type"]
    native_kind={"craft":"ITEM","enchant":"ENCHANT","locate":"LOCATE","teleport":"TELEPORT"}.get(native_type.split(":")[1],"CONVERT" if "convert" in native_type else "SUMMON")
    native_out={"ITEM":lambda:obj["result"]["id"],"ENCHANT":lambda:obj["enchantment"],"LOCATE":lambda:obj["structure_to_locate"]["tag"],"TELEPORT":lambda:"goety:waystone","CONVERT":lambda:obj["entity_to_convert_into"],"SUMMON":lambda:obj["entity_to_summon"]}[native_kind]()
    assert kind==native_kind
    assert mapped[key]==(native_type,native_out,obj["result"].get("count",1) if kind=="ITEM" else 1,obj["craftType"],obj["soulCost"],obj["duration"],len(obj["ingredients"]),obj.get("research",""),obj.get("entity_to_sacrifice",{}).get("tag",""),obj.get("entity_to_convert",{}).get("tag",""),obj.get("xpLevelCost",0)),key
assert len(mapped)==407 and set(actual)-set(mapped)=={"goety:summon_apostle","goety:summon_apostle2"}
print("CATALOG_MATCH",len(mapped),"EXCLUDED_OR_HELD",len(actual)-len(mapped),"LEGACY_IGNORED",old_count,flush=True)
