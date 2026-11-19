#!/usr/bin/env python3
"""Read-only survey of exact Jade, CC, Replication, Titanium and Brandon's Core
pins. Never load or execute mod bytecode; report only relevant constants and methods."""
import hashlib, json, pathlib, subprocess, sys, tomllib, urllib.parse, zipfile, re, struct
MODS = ("jade", "cc-tweaked", "replication", "titanium", "brandons-core")
def strings(data):
    # Parse class-file constant pool; tag 8 means literal String (not identifiers).
    assert data[:4] == bytes.fromhex("cafebabe")
    count = int.from_bytes(data[8:10], "big")
    pos=10; i=1; utf={}; literals=[]
    while i < count:
        tag=data[pos]; pos+=1
        if tag == 1:
            n=int.from_bytes(data[pos:pos+2],"big");pos+=2
            utf[i]=data[pos:pos+n].decode("utf-8",errors="replace");pos+=n
        elif tag == 8:
            literals.append(int.from_bytes(data[pos:pos+2],"big"));pos+=2
        elif tag in (7,16,19,20):pos+=2
        elif tag in (3,4,9,10,11,12,17,18):pos+=4
        elif tag in (5,6):pos+=8;i+=1
        elif tag == 15:pos+=3
        else:raise ValueError(f"unknown constant pool tag {tag}")
        i+=1
    return [utf[k] for k in literals]
def relevant(s):
    return len(s)<250 and bool(re.search(r"(?<![A-Za-z])(?:RF|FE)(?![A-Za-z])|Forge Energy",s))
def main():
    tmp=pathlib.Path(sys.argv[1]);tmp.mkdir(parents=True,exist_ok=True)
    for name in MODS:
        pin=tomllib.loads(pathlib.Path("mods",name+".pw.toml").read_text())
        jar=tmp/pin["filename"]; dl=pin["download"]; url=dl.get("url")
        if not url:
            fid=int(pin["update"]["curseforge"]["file-id"])
            url=f"https://edge.forgecdn.net/files/{fid//1000}/{fid%1000:03d}/{urllib.parse.quote(jar.name)}"
        subprocess.run(["curl","-fsSL","--retry","1","--max-time","90","-o",str(jar),url],check=True)
        with jar.open("rb") as f: digest=hashlib.file_digest(f,dl["hash-format"]).hexdigest()
        assert digest==dl["hash"],name
        print("PIN",name,jar.name,digest,flush=True)
        with zipfile.ZipFile(jar) as z:
            targets=[]
            for p in z.namelist():
                if p.startswith("assets/") and p.endswith("/lang/en_us.json"):
                    for k,v in json.loads(z.read(p)).items():
                        if isinstance(v,str) and relevant(v): print("LANG",p,json.dumps({k:v}),flush=True)
                if p.endswith(".class") and not p.startswith("META-INF/"):
                    match=sorted(set(s for s in strings(z.read(p)) if relevant(s)))
                    if match: print("LITERALS",name,p,json.dumps(match),flush=True);targets.append(p)
            if name=="jade":
                targets.append("snownee/jade/api/view/EnergyView.class")
                targets.append("snownee/jade/util/CommonProxy.class")
            if name=="cc-tweaked":
                targets.extend(p for p in z.namelist() if p.endswith("/EnergyMethods.class") or p.endswith("/AbstractEnergyMethods.class"))
            for p in sorted(set(targets)):
                if name in ("replication","titanium","cc-tweaked") or (name=="jade" and ("EnergyView" in p or "CommonProxy" in p)):
                    cl=p.removesuffix(".class").replace("/",".")
                    result=subprocess.run(["javap","-classpath",str(jar),"-p","-c","-s",cl],check=True,capture_output=True,text=True).stdout
                    if name=="jade" and "CommonProxy" in cl:
                        m=re.search(r"(?ms)^  public static .* wrapEnergyStorage\(.*?(?=\n  (?:public|private|protected) |\Z)",result)
                        result=m.group(0) if m else ""
                    elif name in ("replication","titanium"):
                        chunks=re.split(r"(?=\n  (?:public|private|protected|static|final) )",result)
                        result="\n".join(c for c in chunks if any(relevant(line.split("// String ",1)[1]) for line in c.splitlines() if "// String " in line))
                    print("BYTECODE",name,cl,result[:12000],flush=True)
            if name=="jade":
                assert "snownee/jade/api/view/EnergyView.class" in z.namelist()
        jar.unlink()
    print("OP display survey complete; no compilation or boot.")
if __name__=="__main__":main()
