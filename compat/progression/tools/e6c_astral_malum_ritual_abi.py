"""Pinned Astral personal altar and Malum/Gaze census, static only."""
import pathlib, tempfile, zipfile, subprocess, tomllib, re, json, hashlib, urllib.request
def download(urls, algo, expected):
    errors=[]
    for url in urls:
        try:
            req=urllib.request.Request(url,headers={"User-Agent":"TMM-static-ritual-audit/1.0"})
            with urllib.request.urlopen(req,timeout=120) as response:
                data=response.read(250_000_001)
            if len(data)>250_000_000 or hashlib.new(algo,data).hexdigest()!=expected:
                raise ValueError("size or hash mismatch")
            return data
        except Exception as ex: errors.append(str(ex))
    raise RuntimeError(str(errors))
PINS={
"astral":(("https://mediafilez.forgecdn.net/files/9281/73/AstralSorcery-2.0.3.59.jar","https://mediafilez.forgecdn.net/files/9281/073/AstralSorcery-2.0.3.59.jar","https://media.forgecdn.net/files/9281/73/AstralSorcery-2.0.3.59.jar"),"sha1","66f36655eae7cb404c9c16da6f33db31dff6ed9a"),
"malum":(("https://cdn.modrinth.com/data/jgzwYsAN/versions/BIbVs1fo/malum-1.21.1-1.8.2.jar",),"sha512","dd9ccc1dea3b090f69f807be3e7a8b99ec6d3d7d6b047e2893cb49ca505860bbf640cf767082c1a32ea403b35e2a9f9d1755327890bbfb2f8778058a9597a99e"),
"gaze":(("https://cdn.modrinth.com/data/NlvaJ5WE/versions/od4ltbRo/gaze-1.1.7.1.jar",),"sha512","7e9ecdcc996f376e280bf46f5c0cfd328fb6f1228b1dac469390031458e7a81ad56acff03d9bb6b57b8209586798ab443be1e91fa8fe56297a9e7c9451db03e2")}
import sys
key=sys.argv[1]
urls,algo,sha=PINS[key]
with tempfile.TemporaryDirectory() as d:
    jar=pathlib.Path(d)/(key+".jar")
    jar.write_bytes(download(urls,algo,sha))
    print("VERIFIED",key,sha,flush=True)
    with zipfile.ZipFile(jar) as z:
        print("METADATA",key,tomllib.loads(z.read("META-INF/neoforge.mods.toml").decode())["mods"],flush=True)
        if "META-INF/MANIFEST.MF" in z.namelist():
            print("MANIFEST",z.read("META-INF/MANIFEST.MF").decode(errors="replace"),flush=True)
        classes=[]
        geas_recipes={}
        for n in sorted(z.namelist()):
            if key=="astral":
                if re.search(r"/(?:PlayerAttunementRecipe\$Active|PlayerProgress|ResearchHelper|ResearchManager|TileAttunementAltar(?:\$Data)?|AttunementRecipe\$Active)\.class$",n):
                    classes.append(n[:-6].replace("/","."))
            else:
                if n.endswith(".class") and not n.startswith("META-INF/") and re.search(r"totem|geas|soulbind|soulbrazier|spiritrite",n,re.I):
                    classes.append(n[:-6].replace("/","."))
                elif n.startswith("data/") and n.endswith(".json") and re.search(r"totem|geas|soul.bind|rite",n,re.I):
                    if "/recipe/" in n or "/recipes/" in n:
                        obj=json.loads(z.read(n))
                        if "/recipe/soul_binding/" in n and obj.get("type")=="malum:soul_binding":
                            parts=n.split("/")
                            geas_recipes[obj["result"]]=(parts[1]+":"+"/".join(parts[3:])[:-5],obj["input"].get("count",1),[v.get("count",1) for v in obj["extraInputs"]],[v["count"] for v in obj["spirits"]])
                        print("RECIPE",key,n,json.dumps(obj,separators=(",",":")),flush=True)
        if key!="astral":
            print("RITE_RESOURCES",key,json.dumps([n for n in z.namelist() if n.startswith("data/") and re.search(r"rite|geas",n,re.I) and "/advancement/" not in n and "/recipe/" not in n]),flush=True)
            for n in sorted(z.namelist()):
                if n.startswith("data/") and n.endswith(".json") and re.search(r"rite|geas",n,re.I) and "/advancement/" not in n and "/recipe/" not in n:
                    print("RITE_DATA",n,z.read(n).decode(errors="replace").replace("\n"," "),flush=True)
            for n in z.namelist():
                if n.endswith(".class") and not n.startswith("META-INF/") and re.search(r"(RiteType|RiteEffectType|GeasEffectType|GeasSoulData|RiteAnchorBlockEntity|RiteUnweaverBlockEntity|Mixin)",n):
                    cls=n[:-6].replace("/",".")
                    if cls not in classes: classes.append(cls)
        print("CLASSES",key,json.dumps(classes),flush=True)
    rite_ids=set()
    geas_ids=set()
    for cls in classes:
        # Detailed bytecode only for authoritative lifecycle and registry, not every cosmetic/renderer class.
        detailed=key=="astral" or bool(re.search(r"(TotemBase|TotemPole|SoulBrazier|SoulBindingRecipe|GeasEffectHandler|RiteRegistry|RiteTypes|RiteType|RiteEffectTypes|GeasEffectTypes|GeasEffectTypeRegistry|GeasSoulData|RiteAnchorBlockEntity|RiteUnweaverBlockEntity|Mixin)",cls))
        argv=["javap","-classpath",str(jar),"-p","-s"]+(["-c"] if detailed else [])+[cls]
        out=subprocess.run(argv,capture_output=True,text=True,check=True,timeout=60).stdout
        if key=="astral":
            expected={"PlayerAttunementRecipe$Active":("finishRecipe(","getPlayerUUID()","isFinished(","ResearchHelper.attuneConstellation:"),
                      "PlayerProgress":("wasOnceAttuned()","isAttuned()","getAttunedConstellation()"),
                      "ResearchHelper":("attuneConstellation(","setAttunedConstellation:"),
                      "ResearchManager":("getProgress(net.minecraft.world.entity.player.Player, net.neoforged.fml.LogicalSide)"),
                      "TileAttunementAltar$Data":("getActiveRecipe()",),"AttunementRecipe$Active":("getConstellation()",)}.get(cls.rsplit(".",1)[-1],())
            if isinstance(expected,str): expected=(expected,)
            for token in expected: assert token in out,(cls,token)
        if key!="astral":
            short=cls.rsplit(".",1)[-1]
            expected={
                "TotemBaseBlockEntity":("onUseWithItem(","serverTick(","Field rite:","Method com/sammy/malum/registry/common/magic/rite/MalumSpiritRiteTypes.getRite:","Field com/sammy/malum/common/block/curiosities/totem/TotemBaseBlockEntity$TotemBaseState.ACTIVE:"),
                "SoulBrazierBlockEntity":("Method attemptSoulbinding:","Method addSacrifice:","Method completeSoulBinding:","GeasEffectHandler.addGeasEffect:","BrazierState.BINDING:","BrazierState.IDLE:"),
                "GeasEffectHandler":("boolean addGeasEffect(","boolean hasGeasEffect(","GeasSoulData.tryAddGeasEffect:"),
                "SpiritRiteType":("getRegistryName()","getEffect()"),
                "GeasSoulData":("boolean tryAddGeasEffect(",),
            }.get(short,())
            for token in expected: assert token in out,(cls,token)
            if short in ("MalumSpiritRiteTypes","MalumGeasEffectTypes","GazeGeasEffectTypeRegistry"):
                init=out.split("static {};",1)[-1]
                ids=set(re.findall(r"// String ([a-z0-9_]+)$",init,re.M))
                if short=="MalumSpiritRiteTypes": rite_ids={"malum:"+v for v in ids-{"spirit_rite_types","malum"}}
                else: geas_ids={key+":"+v for v in ids-{"geas_types","malum","gaze"}}
        print("CLASS",cls,flush=True)
        active=False
        for line in out.splitlines():
            if line.startswith("  ") and not line.startswith("    ") and line.rstrip().endswith(";"):
                print("DECL",line.strip(),flush=True)
                active=bool(re.search(r"finish|start|activate|interact|tick|save|load|attune|bind|geas|rite|register|complete|set|add|remove|static \{\}|receiveSpark|canTrigger|getRegistryName|tryAdd",line,re.I))
            elif line.strip().startswith("descriptor:") or (active and line.lstrip().split(":",1)[0].isdigit()):
                print(line,flush=True)
if key=="astral": print("ASTRAL_PERSONAL_ABI_MATCH",flush=True)

if key!="astral":
    catalog=(pathlib.Path(__file__).resolve().parents[1]/"src/main/java/io/github/heatwaveqc/tyr/progression/mxp/core/MalumRitualCatalog.java").read_text()
    mapped={}
    for recipe,effect,main,extras,spirits in re.findall(r'add\(out,"([^"]+)","([^"]+)",(\d+),List.of\(([\d,]*)\),List.of\(([\d,]*)\)\);',catalog):
        if effect.startswith(key+":"):
            mapped[effect]=(recipe,int(main),[int(v) for v in extras.split(",") if v],[int(v) for v in spirits.split(",") if v])
    assert mapped==geas_recipes, (set(mapped)^set(geas_recipes))
    assert set(mapped)==geas_ids-({"malum:creed_of_the_blight_eater"} if key=="malum" else set()),(set(mapped)^geas_ids)
    if key=="malum":
        block=catalog.split("RITES=Set.of(",1)[1].split(");",1)[0]
        assert set(re.findall(r'"([^"]+)"',block))==rite_ids and len(rite_ids)==26
    print("MALUM_CATALOG_MATCH",key,"rites",len(rite_ids),"recipe_geas",len(mapped),flush=True)
