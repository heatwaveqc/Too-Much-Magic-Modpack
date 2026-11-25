"""Targeted owner-approved EMI trial artifact review; never installs or boots."""
import concurrent.futures
import hashlib
import io
import json
import pathlib
import tomllib
import urllib.request
import urllib.parse
import zipfile

ROOT = pathlib.Path(__file__).resolve().parents[1]
HEADERS = {"User-Agent": "TooMuchMagic-EMI-audit/1.0"}
CANDIDATES = (
    ("emi", "fRiHVvU7", "5sIPA1To", "emi-1.1.24+1.21.1+neoforge.jar"),
    ("extra-mod-integrations", "bpRHnWUb", "bxgGyUjY", "exmi-neoforge-1.0.3+1.21.1.jar"),
)
def read(url):
    with urllib.request.urlopen(urllib.request.Request(url, headers=HEADERS), timeout=30) as response:
        data = response.read(20 * 1024 * 1024 + 1)
        if len(data) > 20 * 1024 * 1024:
            raise ValueError("Candidate too large")
        return data

def inspect(label, raw, filename, url, hashes, project, version):
    with zipfile.ZipFile(io.BytesIO(raw)) as jar:
        path = next(x for x in ("META-INF/neoforge.mods.toml", "META-INF/mods.toml") if x in jar.namelist())
        metadata = tomllib.loads(jar.read(path).decode("utf-8-sig"))
    actual = {h: hashlib.new(h, raw).hexdigest() for h in ("sha1", "sha256", "sha512")}
    pin_path = ROOT / "mods" / (label + ".pw.toml")
    if pin_path.is_file():
        pin = tomllib.loads(pin_path.read_text())
        assert pin["filename"] == filename and pin["download"]["url"] == url
        assert pin["download"]["hash"] == actual[pin["download"]["hash-format"]]
    with zipfile.ZipFile(io.BytesIO(raw)) as jar:
        embedded = []
        for member in jar.namelist():
            if member.startswith("META-INF/") and member.endswith(".jar"):
                with zipfile.ZipFile(io.BytesIO(jar.read(member))) as nested:
                    for meta_path in ("META-INF/neoforge.mods.toml", "META-INF/mods.toml"):
                        if meta_path in nested.namelist():
                            meta = tomllib.loads(nested.read(meta_path).decode("utf-8-sig"))
                            embedded.append({"member": member, "mods": meta.get("mods"), "dependencies": meta.get("dependencies")})
        print("NESTED " + json.dumps({"label": label, "embedded": embedded}), flush=True)
    for h, digest in hashes.items():
        if actual[h] != digest:
            raise ValueError(f"{label} {h} mismatch")
    print("CANDIDATE " + json.dumps(dict(label=label,filename=filename,url=url,hashes=actual,project=project,version=version,mods=metadata.get("mods"),dependencies=metadata.get("dependencies"),bytes=len(raw))), flush=True)
def mr_candidate(row):
    label, project, version, expected = row
    v = json.loads(read("https://api.modrinth.com/v2/version/" + version))
    assert v["project_id"] == project and v["version_type"] == "release"
    assert "1.21.1" in v["game_versions"] and "neoforge" in v["loaders"]
    f = next(f for f in v["files"] if f["primary"] and f["filename"].endswith(".jar"))
    assert urllib.parse.urlparse(f["url"]).hostname == "cdn.modrinth.com"
    inspect(label, read(f["url"]), f["filename"], f["url"], f["hashes"], project, version)
def cf_candidate():
    url = "https://mediafilez.forgecdn.net/files/9734/643/emisemble-1.2.1.jar"
    raw = read(url)
    # Cross-check exact publisher GitHub tag digest (same approved version).
    inspect("emisemble", raw, "emisemble-1.2.1.jar", url, {"sha256":"02084ad59e4251290142ad9c9ab3c12d5a8adc23c5be89c7d618dd71bd8e19e8"}, 1813595, 9734643)
def survey_pin(row):
    path = ROOT / row["file"]
    pin = tomllib.loads(path.read_text())
    mr = pin.get("update", {}).get("modrinth", {})
    if not mr.get("mod-id"):
        return {"pin":row["file"],"current":pin["filename"],"decision":"hold","reason":"non-Modrinth provider; exact latest compatible release not established"}
    try:
        query = urllib.parse.urlencode({"loaders":json.dumps(["neoforge"]),"game_versions":json.dumps(["1.21.1"])})
        versions = json.loads(read("https://api.modrinth.com/v2/project/" + mr["mod-id"] + "/version?" + query))
        stable = next((v for v in versions if v["version_type"] == "release"),None)
        return {"pin":row["file"],"current":pin["filename"],"version_id":mr.get("version"),"latest_stable":stable and stable["version_number"],"latest_id":stable and stable["id"],"decision":"current" if stable and stable["id"]==mr.get("version") else "hold","reason":"exact baseline retained for controlled viewer migration; review newer releases separately"}
    except Exception as e:
        return {"pin":row["file"],"current":pin["filename"],"decision":"hold","reason":str(e)}
def main():
    errors=[]
    for row in CANDIDATES:
        try: mr_candidate(row)
        except Exception as e: errors.append(str(e)); print("CANDIDATE ERROR",row[0],repr(e),flush=True)
    try: cf_candidate()
    except Exception as e: errors.append(str(e)); print("CANDIDATE ERROR emisemble",repr(e),flush=True)
    try:
        pin = tomllib.loads((ROOT/"mods/more-overlays-updated.pw.toml").read_text())
        raw = read(pin["download"]["url"])
        assert hashlib.new(pin["download"]["hash-format"], raw).hexdigest() == pin["download"]["hash"]
        with zipfile.ZipFile(io.BytesIO(raw)) as jar:
            meta_path = next(x for x in ("META-INF/neoforge.mods.toml", "META-INF/mods.toml") if x in jar.namelist())
            meta = tomllib.loads(jar.read(meta_path).decode("utf-8-sig"))
            print("MORE_OVERLAYS " + json.dumps(meta.get("dependencies", {})), flush=True)
            for deps in meta.get("dependencies", {}).values():
                for dep in deps:
                    if dep.get("modId") == "jei" and (dep.get("type", "").lower() == "required" or dep.get("mandatory") is True):
                        raise ValueError("More Overlays requires JEI")
    except Exception as e: errors.append("More Overlays: " + str(e))
    rows = [r for r in tomllib.loads((ROOT/"index.toml").read_text())["files"] if r["file"].startswith("mods/") and r.get("metafile")]
    print("ROSTER_COUNT",len(rows),flush=True)
    # Read-only metadata survey; never download the existing roster.
    with concurrent.futures.ThreadPoolExecutor(max_workers=6) as pool:
        for entry in pool.map(survey_pin,rows):
            print("UPDATE " + json.dumps(entry),flush=True)
    if errors: raise SystemExit("Candidate evidence incomplete: " + repr(errors))
if __name__ == "__main__": main()
