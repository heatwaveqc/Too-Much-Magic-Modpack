"""Static inspection of pinned dimension IDs; no Minecraft launch or JAR publication."""
import hashlib, json, pathlib, tempfile, tomllib, urllib.request, zipfile
root=pathlib.Path(".")
config=tomllib.loads((root/"config/astralsorcery-client.toml").read_text())["rendering"]
assert config["skyRenderDimensions"] == ["minecraft:overworld"]
overlay=set(config["constellationOverlaySkyDimensions"])
assert len(overlay)==4 and "minecraft:the_end" in overlay
expected=overlay-{"minecraft:the_end"}
for pin in ("eternal-starlight","the-aurorian-unofficial-port","twilight-forest"):
    data=tomllib.loads((root/"mods"/(pin+".pw.toml")).read_text())["download"]
    request=urllib.request.Request(data["url"],headers={"User-Agent":"TMM-dimension-static-audit/1.0"})
    with urllib.request.urlopen(request,timeout=120) as response:
        jar=response.read()
    assert hashlib.new(data["hash-format"],jar).hexdigest()==data["hash"],pin
    with tempfile.TemporaryDirectory() as tmp:
        path=pathlib.Path(tmp)/"pinned.jar";path.write_bytes(jar)
        with zipfile.ZipFile(path) as z:
            dims={}
            for n in z.namelist():
                parts=n.split("/")
                if len(parts)>=4 and parts[0]=="data" and parts[2]=="dimension" and n.endswith(".json"):
                    key=parts[1]+":"+"/".join(parts[3:])[:-5]
                    dims[key]=json.loads(z.read(n)).get("type")
            print("PIN_VERIFIED",pin,"DIMENSIONS",json.dumps(dims,sort_keys=True),flush=True)
            found=expected.intersection(dims)
            expected-=found
            for name in z.namelist():
                if len(name.split("/"))>=4 and name.split("/")[0]=="data" and name.split("/")[2]=="dimension_type" and name.endswith(".json"):
                    fields=json.loads(z.read(name))
                    print("DIMENSION_TYPE",name,{k:fields.get(k) for k in ("fixed_time","natural","has_skylight","has_ceiling","effects")},flush=True)
assert not expected,"Missing configured dimension IDs: "+str(expected)
