#!/usr/bin/env python3
"""Read only exact pinned textures / models. Never extract or publish Ars artwork."""
import base64, hashlib, io, json, pathlib, subprocess, sys, tomllib, urllib.parse, zipfile
from PIL import Image
def acquire(name, tmp):
    pin=tomllib.loads(pathlib.Path("mods",name+".pw.toml").read_text())
    fid=int(pin["update"]["curseforge"]["file-id"])
    file=tmp/pin["filename"]
    url=f"https://edge.forgecdn.net/files/{fid//1000}/{fid%1000:03d}/{urllib.parse.quote(file.name)}"
    subprocess.run(["curl","-fsSL","--max-time","90","-o",str(file),url],check=True)
    with file.open("rb") as f:
        digest=hashlib.file_digest(f,pin["download"]["hash-format"]).hexdigest()
    assert digest==pin["download"]["hash"],name
    return zipfile.ZipFile(file)
tmp=pathlib.Path(sys.argv[1]);tmp.mkdir(parents=True,exist_ok=True)
with acquire("botania-revived",tmp) as z:
    for name in ("alchemy_catalyst","alchemy_catalyst_overlay"):
        path="assets/botania/textures/block/"+name+".png"
        im=Image.open(io.BytesIO(z.read(path))).convert("RGBA")
        print("BOTANIA_DIM",name,im.size)
        # Botania permits adaptation; use a nearest-neighbor preview of one frame for the art pass.
        preview=im.crop((0,0,im.width,im.width)).resize((im.width*8,im.width*8),Image.Resampling.NEAREST)
        buf=io.BytesIO();preview.save(buf,format="PNG")
        print("BOTANIA_PREVIEW_"+name+"="+base64.b64encode(buf.getvalue()).decode())
        print("BOTANIA_META",name,z.read(path+".mcmeta").decode())
    print("BOTANIA_MODEL",z.read("assets/botania/models/block/alchemy_catalyst.json").decode())
with acquire("ars-nouveau",tmp) as z:
    for name in ("source_jar0","source_jar_sliver","source_jar100"):
        model=json.loads(z.read("assets/ars_nouveau/models/block/source_jar/"+name+".json"))
        print("ARS_MODEL_SUMMARY",name,"texture_keys",model.get("textures"),"size",model.get("texture_size"))
        print("ARS_EXTENTS",name,[(e["from"],e["to"],sorted(set(f["texture"] for f in e["faces"].values()))) for e in model["elements"]])
    print("ARS_BLOCKSTATES",json.loads(z.read("assets/ars_nouveau/blockstates/source_jar.json"))["variants"])
    for path in ("assets/ars_nouveau/textures/block/source_jar.png","assets/ars_nouveau/textures/block/mana_still.png"):
        im=Image.open(io.BytesIO(z.read(path)))
        print("ARS_TEXTURE_SIZE",path,im.size)
print("Exact pinned asset survey passed")
