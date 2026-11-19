"""Hash-pinned E6c Primal ritual census; no runtime startup."""
import pathlib, tempfile, zipfile, json, subprocess, re, tomllib
from e6b_ritual_abi import download
URLS=("https://mediafilez.forgecdn.net/files/6677/090/primalmagick-neoforge-1.21.1-6.0.7.jar","https://edge.forgecdn.net/files/6677/090/primalmagick-neoforge-1.21.1-6.0.7.jar")
HASH="165a849fe2b95355067ede9cda014dec08129731"
with tempfile.TemporaryDirectory() as d:
    jar=pathlib.Path(d)/"primal.jar"
    jar.write_bytes(download(URLS,"sha1",HASH))
    print("VERIFIED",HASH,flush=True)
    with zipfile.ZipFile(jar) as z:
        print("METADATA",tomllib.loads(z.read("META-INF/neoforge.mods.toml").decode())["mods"],flush=True)
        if "META-INF/MANIFEST.MF" in z.namelist():
            print("MANIFEST",z.read("META-INF/MANIFEST.MF").decode(),flush=True)
        count=0
        for n in sorted(z.namelist()):
            if not re.fullmatch(r"data/[^/]+/recipe/.*\.json",n): continue
            obj=json.loads(z.read(n))
            if obj.get("type")=="primalmagick:ritual":
                count+=1
                print("RECIPE",n,json.dumps({k:obj.get(k) for k in ("result","requirement","mana","instability","props","ingredients","expertiseGroup")},separators=(",",":")),flush=True)
        print("COUNT",count,flush=True)
        assert count>0
        classes=[n[:-6].replace("/",".") for n in z.namelist() if re.search(r"/(?:RitualAltarTileEntity(?:Neoforge)?|AbstractTileEntity|AbstractInventoryTileEntity|RitualRecipe|IRitualRecipe|WandItem|ManaManager)\.class$",n)]
    for cls in classes:
        out=subprocess.run(["javap","-classpath",str(jar),"-p","-s","-c",cls],capture_output=True,text=True,check=True,timeout=60).stdout
        print("CLASS",cls,flush=True)
        active=False
        for line in out.splitlines():
            if line.startswith("  ") and not line.startswith("    ") and line.rstrip().endswith(";"):
                print("DECL",line.strip(),flush=True)
                active=bool(re.search(r"finishCraft|startCraft|reset\(|onWandRightClick|saveAdditional|loadAdditional|setItem\(|getItem\(|getActivePlayer|setActivePlayer|canUseRitualRecipe|consumeMana|lambda\$finishCraft|tick\(",line))
            elif line.strip().startswith("descriptor:") or (active and line.lstrip().split(":",1)[0].isdigit()):
                print(line,flush=True)
