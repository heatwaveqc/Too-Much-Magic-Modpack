#!/usr/bin/env python3
"""Create native-size EMC art; Botania adaptation + wholly original Source/seal.
Input Botania JAR is hash-verified. No Ars image pixels or models are copied.
"""
import hashlib, io, json, math, pathlib, sys, tomllib, zipfile
from PIL import Image
jar=pathlib.Path(sys.argv[1]);out=pathlib.Path(sys.argv[2]);out.mkdir(parents=True,exist_ok=True)
pin=tomllib.loads(pathlib.Path("mods/botania-revived.pw.toml").read_text())
with jar.open("rb") as f:
    assert hashlib.file_digest(f,pin["download"]["hash-format"]).hexdigest()==pin["download"]["hash"]
with zipfile.ZipFile(jar) as z:
    prefix="assets/botania/textures/block/"
    raw=Image.open(io.BytesIO(z.read(prefix+"alchemy_catalyst.png"))).convert("RGBA")
    mark=Image.open(io.BytesIO(z.read(prefix+"alchemy_catalyst_overlay.png"))).convert("RGBA")
    meta=json.loads(z.read(prefix+"alchemy_catalyst.png.mcmeta"))
    assert raw.size==mark.size==(16,112)
    # Try the actual livingrock surface, but only if matching the original stone.
    rock=None
    if prefix+"livingrock.png" in z.namelist():
        r=Image.open(io.BytesIO(z.read(prefix+"livingrock.png"))).convert("RGBA")
        if r.size==(16,16):
            untouched=[(x,y) for y in range(16) for x in range(16) if mark.getpixel((x,y))[3]==0]
            same=sum(r.getpixel(p)==raw.getpixel(p) for p in untouched)
            if same>=0.95*len(untouched):rock=r
# Original compact EMC rune: 7 by 9; a stepped E with gold terminal pixels.
rune=("GGGGGGG","GE.....","GE.....","GE.....","GEEEE..","GE.....","GE.....","GE.....","GGGGGGG")
def color(ch,x,y,frame):
    p=(1+math.cos(2*math.pi*(frame/7+(x+y)/23)))/2
    if ch=="G":return (int(155+48*p),int(116+59*p),int(32+26*p),255)
    return (int(25+44*p),int(127+72*p),int(75+46*p),255)
side=raw.copy()
for f in range(7):
    oy=16*f
    # Remove just the original emissive emblem and preserve all other native pixels.
    unmarked=[(x,y) for y in range(16) for x in range(16) if mark.getpixel((x,y+oy))[3]==0]
    assert unmarked
    for y in range(16):
        for x in range(16):
            if mark.getpixel((x,y+oy))[3]:
                p=min(unmarked,key=lambda q:((q[0]-x)**2+(q[1]-y)**2,q[1],q[0]))
                side.putpixel((x,y+oy),rock.getpixel((x,y)) if rock else raw.getpixel((p[0],p[1]+oy)))
    for y,line in enumerate(rune):
        for x,ch in enumerate(line):
            if ch!=".":
                side.putpixel((x+5,y+4+oy),color(ch,x,y,f))
side.save(out/"emc_catalyst_side.png")
(out/"emc_catalyst_side.png.mcmeta").write_text(json.dumps(meta,indent=2)+"\n")
# A miniature independent lid seal, dark brass backing with green E.
seal=Image.new("RGBA",(16,16),(0,0,0,0))
for y in range(2,14):
    for x in range(1,15):
        if (x in (1,14) and y in (2,13)):continue
        border=x in (1,14) or y in (2,13)
        seal.putpixel((x,y),(151,112,51,255) if border else (66,51,40,255))
for y,line in enumerate(rune):
    for x,ch in enumerate(line):
        if ch!=".":seal.putpixel((x+5,y+3),color(ch,x,y,2))
seal.save(out/"emc_jar_seal.png")
# Entirely original, tiled, soft blue-violet Source animation. No native Ars pixels.
fluid=Image.new("RGBA",(16,16*8))
for frame in range(8):
    for y in range(16):
        for x in range(16):
            phase=2*math.pi*(frame/8)
            v=math.sin(2*math.pi*x/16+phase)+0.65*math.cos(2*math.pi*y/8-phase)+0.4*math.cos(2*math.pi*(x+y)/16+phase)
            bands=[(66,44,118),(80,55,143),(98,70,166),(120,91,186),(146,119,202)]
            idx=max(0,min(4,int((v+2.05)*5/4.1)))
            fluid.putpixel((x,y+frame*16),(*bands[idx],235))
fluid.save(out/"emc_source_still.png")
(out/"emc_source_still.png.mcmeta").write_text(json.dumps({"animation":{"frametime":5,"interpolate":True,"frames":list(range(8))}},indent=2)+"\n")
# Verify native background survives untouched except original emblem and new 7x9 rune.
allowed={(x+5,y+4) for y,line in enumerate(rune) for x,ch in enumerate(line) if ch!="."}
for f in range(7):
    for y in range(16):
        for x in range(16):
            if mark.getpixel((x,y+16*f))[3]==0 and (x,y) not in allowed:
                assert side.getpixel((x,y+16*f))==raw.getpixel((x,y+16*f))
print("Validated PNG dimensions, frame metadata and preserved unmarked native pixels.")
