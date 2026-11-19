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
                        print("RECIPE",key,n,json.dumps(obj,separators=(",",":")),flush=True)
                elif n.endswith(".class") and not n.startswith("META-INF/") and (re.search(r"/(?:DarkAltarBlock(?:Entity)?|RitualRecipe|Ritual|RitualRequirements|RitualType|RitualTypes|(?:\w+)Ritual)\.class$",n) or (key=="goety_cataclysm" and "ritual" in n.lower())):
                    classes.append(n[:-6].replace("/","."))
            print("COUNTS",key,json.dumps(counts),flush=True)
            print("CLASSES",key,json.dumps(classes),flush=True)
            assert sum(counts.values())>0
        for cls in classes:
            out=subprocess.run(["javap","-classpath",str(jar),"-p","-s","-c",cls],capture_output=True,text=True,check=True,timeout=60).stdout
            print("CLASS",cls,flush=True)
            active=False
            for line in out.splitlines():
                if line.startswith("  ") and not line.startswith("    ") and line.rstrip().endswith(";"):
                    print("DECL",line.strip(),flush=True)
                    active=bool(re.search(r"finish|start|interrupt|reset|complete|tick\(|use|save|load|ritual|convert|summon|craft|spawn|setOwner|setItem|setPlayer",line,re.I))
                elif line.strip().startswith("descriptor:") or (active and line.lstrip().split(":",1)[0].isdigit() and ("DarkAltarBlockEntity" in cls or "//" in line)):
                    print(line,flush=True)
