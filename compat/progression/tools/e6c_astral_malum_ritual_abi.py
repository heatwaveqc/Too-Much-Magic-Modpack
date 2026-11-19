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
        for n in sorted(z.namelist()):
            if key=="astral":
                if re.search(r"/(?:PlayerAttunementRecipe\$Active|PlayerProgress|ResearchHelper|ResearchManager|TileAttunementAltar(?:\$Data)?|AttunementRecipe\$Active)\.class$",n):
                    classes.append(n[:-6].replace("/","."))
            else:
                if n.endswith(".class") and not n.startswith("META-INF/") and re.search(r"totem|geas|soulbind|soulbrazier|spiritrite",n,re.I):
                    classes.append(n[:-6].replace("/","."))
                elif n.startswith("data/") and n.endswith(".json") and re.search(r"totem|geas|soul.bind|rite",n,re.I):
                    if "/recipe/" in n or "/recipes/" in n:
                        print("RECIPE",key,n,json.dumps(json.loads(z.read(n)),separators=(",",":")),flush=True)
        print("CLASSES",key,json.dumps(classes),flush=True)
    for cls in classes:
        # Detailed bytecode only for authoritative lifecycle and registry, not every cosmetic/renderer class.
        detailed=key=="astral" or bool(re.search(r"(TotemBase|TotemPole|SoulBrazier|SoulBindingRecipe|GeasEffectHandler|RiteRegistry|Rites|GeasEffectRegistry|GeasEffects)",cls))
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
        print("CLASS",cls,flush=True)
        active=False
        for line in out.splitlines():
            if line.startswith("  ") and not line.startswith("    ") and line.rstrip().endswith(";"):
                print("DECL",line.strip(),flush=True)
                active=bool(re.search(r"finish|start|activate|interact|tick|save|load|attune|bind|geas|rite|register|complete|set|add|remove",line,re.I))
            elif line.strip().startswith("descriptor:") or (active and line.lstrip().split(":",1)[0].isdigit()):
                print(line,flush=True)
if key=="astral": print("ASTRAL_PERSONAL_ABI_MATCH",flush=True)
