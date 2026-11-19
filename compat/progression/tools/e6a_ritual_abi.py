"""E6a exact artifact reward census and native seams; no game startup or upload."""
import hashlib, json, pathlib, re, subprocess, tempfile, urllib.request, zipfile
PINS = {
    "occultism": (("https://cdn.modrinth.com/data/sbJh4AZw/versions/SFDVXWYt/occultism-1.21.1-neoforge-1.227.0.jar",), "sha512", "063d775420e1efa916d1ed94f3f5bfc3b00b976b1de6fc333eda9886ea3665aa38254407a18469ed0a334bcac67b204392c9bf197fc10916ce36a9e29abd5ef0"),
    "forge": (("https://cdn.modrinth.com/data/MdlnLS7Q/versions/fNjxgZPH/forbidden_arcanus-2.6.1.jar",), "sha512", "012a1d5c779a0173a5c76ea79f429486753f040dfe59739bc45fd0817182d705a80110032f024757f0ac51a9bd10c546a11a730899aa9adbf7251a5040472724"),
    "roots": (("https://mediafilez.forgecdn.net/files/8860/609/RootsClassic-1.21.1-1.5.10.jar","https://edge.forgecdn.net/files/8860/609/RootsClassic-1.21.1-1.5.10.jar"), "sha1", "7d02ec46e4d276ebf556cdd529a75108b4724efd"),
}
METHOD = re.compile(r"finish|complete|start|stop|reset|dropResult|spawnEntity|tick\(|save|load|perform|execute|activate|setStack|ritual|owner|caster|tier|level",re.I)
CLASS = re.compile(r"(GoldenSacrificialBowlBlockEntity|RitualRecipe|RitualManager|ActiveRitualData|RitualRequirements|TierPredicate|HephaestusForgeBlockEntity|Ritual|RitualResult|SummonRitual|CraftRitual|CraftMinerSpiritRitual|Altar.*(?:Tile|BlockEntity)|(?:Tile|BlockEntity).*Altar|Ritual.*(?:Altar|Recipe|Effect))$",re.I)
def download(urls, alg, expected):
    errors=[]
    for u in urls:
        try:
            req=urllib.request.Request(u,headers={"User-Agent":"TMM-static-ritual-audit/1.0"})
            with urllib.request.urlopen(req,timeout=90) as response:
                data=response.read(80_000_001)
            if len(data)>80_000_000 or hashlib.new(alg,data).hexdigest()!=expected:
                raise ValueError("oversize or hash mismatch")
            return data
        except (OSError,ValueError) as exc: errors.append(str(exc))
    raise RuntimeError("No exact artifact: "+" | ".join(errors))
def inspect(jar,key):
    classes=[]
    with zipfile.ZipFile(jar) as z:
        for n in sorted(z.namelist()):
            if n.startswith("data/") and n.endswith(".json") and "ritual" in n.lower() and "/tags/" not in n:
                try: data=json.loads(z.read(n))
                except ValueError: continue
                if isinstance(data,dict):
                    print("RECIPE",key,n,json.dumps(data,separators=(",",":")),flush=True)
            if n.endswith(".class") and not n.startswith("META-INF/") and (CLASS.search(n[:-6].rsplit("/",1)[-1]) or (key == "roots" and (n.startswith("elucent/rootsclassic/ritual/") or n.endswith("/InventoryUtil.class") or n.endswith("/CustomInventory.class")))):
                classes.append(n[:-6].replace("/","."))
    if not classes: raise RuntimeError("No ritual classes for "+key)
    for cls in classes:
        out=subprocess.run(["javap","-classpath",str(jar),"-p","-s","-c",cls],capture_output=True,text=True,check=True,timeout=45).stdout
        print("CLASS",cls,flush=True)
        method=""; relevant=False
        for line in out.splitlines():
            if line.startswith("  ") and not line.startswith("    ") and line.rstrip().endswith(";"):
                method=line.strip(); relevant=key == "roots" or bool(METHOD.search(method)); print("DECL",method)
            elif line.strip().startswith("descriptor:"): print("DESC",line.strip())
            elif relevant and line.lstrip().split(":",1)[0].isdigit(): print("BC",method,"|",line.strip())
        print("END",cls,flush=True)
with tempfile.TemporaryDirectory() as directory:
    for key,(urls,alg,expected) in PINS.items():
        jar=pathlib.Path(directory)/(key+".jar")
        jar.write_bytes(download(urls,alg,expected))
        print("VERIFIED",key,alg,expected,flush=True)
        inspect(jar,key)
