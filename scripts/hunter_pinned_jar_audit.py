#!/usr/bin/env python3
"""Read-only audit of exact TMM packwiz-pinned mod JARs. Never uploads binaries."""
from __future__ import annotations
import csv, hashlib, io, json, pathlib, struct, sys, time, tomllib, urllib.request, zipfile
from urllib.parse import quote, urlparse

ROOT=pathlib.Path(__file__).resolve().parents[1]
OUT=ROOT/"docs/hunter/ci-evidence"
OUT.mkdir(parents=True, exist_ok=True)
TARGETS={
"cataclysm","eidolon-repraised","irons-spells","alexscaves","draconic-evolution",
"goety","goety-cataclysm","deeper-darker","feywild","occultism","evilcraft",
"witchery-remake","neovitae","ars-nouveau","botania-revived","malum",
"primal-magick","hexerei","enchanted-witchcraft","roots-classic",
"forbidden-arcanus","forbidden-magic","gaze-malum"
}
IGNORE={"alexscaves"}   # prospective, not pinned
HOSTS={"cdn.modrinth.com","edge.forgecdn.net","mediafilez.forgecdn.net"}
EVIDENCE=list(csv.DictReader((ROOT/"docs/hunter/registration-evidence.csv").open()))
def allowed_urls(meta):
    d=meta["download"]
    if "url" in d:return [d["url"]]
    if d.get("mode")=="metadata:curseforge":
        cf=meta["update"]["curseforge"];n=int(cf["file-id"])
        path=f"/files/{n//1000}/{n%1000:03d}/{quote(meta['filename'])}"
        return [h+path for h in ("https://mediafilez.forgecdn.net","https://edge.forgecdn.net")]
    return []
def fetch(meta):
    alg=meta["download"]["hash-format"]; expected=meta["download"]["hash"].lower()
    if alg not in ("sha1","sha256","sha512"):raise ValueError("Unsupported digest "+alg)
    errors=[]
    for url in allowed_urls(meta):
        u=urlparse(url)
        if u.scheme!="https" or u.hostname not in HOSTS:raise ValueError("Unapproved download origin")
        for attempt in range(2):
            try:
                req=urllib.request.Request(url,headers={"User-Agent":"TooMuchMagic-Hunter-ReadOnly-CI/1.0"})
                with urllib.request.urlopen(req,timeout=120) as response:data=response.read()
                if hashlib.new(alg,data).hexdigest()!=expected:raise ValueError("HASH_MISMATCH")
                return data,alg,expected,"PASS"
            except Exception as exc:
                errors.append(type(exc).__name__+":"+str(exc)[:130]);time.sleep(1)
    return None,alg,expected,"DOWNLOAD_FAILED:"+";".join(errors)[-500:]
def utf8_constant_strings(data):
    if len(data)<10 or data[:4]!=b"\xCA\xFE\xBA\xBE":return set()
    pos=10;count=struct.unpack_from(">H",data,8)[0];i=1;strings=set()
    try:
        while i<count:
            tag=data[pos];pos+=1
            if tag==1:
                n=struct.unpack_from(">H",data,pos)[0];pos+=2
                raw=data[pos:pos+n];pos+=n
                strings.add(raw.decode("utf-8","replace"))
            elif tag in (3,4,9,10,11,12,17,18):pos+=4
            elif tag in (5,6):pos+=8;i+=1
            elif tag in (7,8,16,19,20):pos+=2
            elif tag==15:pos+=3
            else:return set()
            i+=1
    except (IndexError,ValueError,struct.error):return set()
    return strings
def analyze_jar(data,expected_ids):
    with zipfile.ZipFile(io.BytesIO(data)) as z:
        names=z.namelist();classes=[n for n in names if n.endswith(".class")]
        n=0;matchmap={k:[] for k in expected_ids};regmap={k:[] for k in expected_ids}
        for file in classes:
            name=file.split("/")[-1]
            if not any(t in file.lower() for t in ("entity","registr","entities","mob","common/init")):continue
            try:items=utf8_constant_strings(z.read(file))
            except Exception:continue
            n+=1
            for key in expected_ids:
                suffix=key.split(":",1)[1]
                if suffix in items:
                    if len(matchmap[key])<5:matchmap[key].append(file)
                    if any(q in name.lower() for q in ("entitytypes","modentities","entities","entityregistry","registryentities")):
                        if len(regmap[key])<5:regmap[key].append(file)
        meta_names=[n for n in names if n.endswith("neoforge.mods.toml") or n.endswith("mods.toml") or n.endswith("fabric.mod.json")]
        return {"class_files":len(classes),"classes_scanned":n,"meta_files":meta_names[:5],"matches":matchmap,"registry_matches":regmap}
rows=[];evidence_rows=[];status=[]
index=tomllib.loads((ROOT/"index.toml").read_text())
pin_files={pathlib.Path(x["file"]).stem.removesuffix(".pw"):ROOT/x["file"] for x in index["files"] if x.get("metafile")}
for mod in sorted(TARGETS-IGNORE):
    path=pin_files.get(mod) or ROOT/"mods"/(mod+".pw.toml")
    if not path.exists():
        rows.append({"mod":mod,"pin":"","result":"NOT_PINNED","sha":"","bytes":"","class_files":"","classes_scanned":"","meta_files":""})
        continue
    meta=tomllib.loads(path.read_text())
    if meta.get("side")=="client":continue
    print("FETCH",mod,meta["filename"],flush=True)
    data,alg,sha,result=fetch(meta)
    entry={"mod":mod,"pin":meta["filename"],"result":result,"sha":alg+":"+sha,"bytes":len(data) if data else 0,"class_files":"","classes_scanned":"","meta_files":""}
    if data is not None:
        ids=[r["source_registered_id"] for r in EVIDENCE if r["source_mod"]==mod]
        try:
            info=analyze_jar(data,ids)
            entry.update({"class_files":info["class_files"],"classes_scanned":info["classes_scanned"],"meta_files":"|".join(info["meta_files"])})
            for rid in ids:
                evidence_rows.append({"mod":mod,"registered_id":rid,"pinned_hash_verified":"YES","exact_suffix_in_entity_class":"YES" if info["matches"][rid] else "NO","exact_suffix_in_registration_named_class":"YES" if info["registry_matches"][rid] else "NO","class_paths":"|".join(info["matches"][rid]),"registration_paths":"|".join(info["registry_matches"][rid])})
            print("OK",mod,entry["bytes"],"bytes; IDs:",len(ids),flush=True)
        except Exception as ex:
            entry["result"]="JAR_READ_FAILED:"+str(ex)[:130]
    else: print("BLOCKED",mod,result,flush=True)
    rows.append(entry)
def write(name,records,fields):
    with (OUT/name).open("w",newline="") as fh:
        w=csv.DictWriter(fh,fieldnames=fields);w.writeheader();w.writerows(records)
write("pin-verification.csv",rows,["mod","pin","result","sha","bytes","class_files","classes_scanned","meta_files"])
write("source-id-binary-strings.csv",evidence_rows,["mod","registered_id","pinned_hash_verified","exact_suffix_in_entity_class","exact_suffix_in_registration_named_class","class_paths","registration_paths"])
summary={"targets":len(TARGETS-IGNORE),"hash_verified":sum(r["result"]=="PASS" for r in rows),"errors":[{"mod":r["mod"],"result":r["result"]} for r in rows if r["result"]!="PASS"],"registry_string_matched":sum(r["exact_suffix_in_entity_class"]=="YES" for r in evidence_rows),"registry_string_total":len(evidence_rows),"warning":"Strings in binary classes are corroboration only; not proof of runtime registration or class inheritance; no exact JAR uploads."}
(OUT/"summary.json").write_text(json.dumps(summary,indent=2)+"\n")
print("AUDIT_SUMMARY",json.dumps(summary),flush=True)
if not summary["hash_verified"]:sys.exit("No target JARs verified; audit cannot claim success")
