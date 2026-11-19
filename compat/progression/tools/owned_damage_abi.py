"""Read-only exact-pin source evidence for the Mahou -> Discidia compatibility adapter."""
import hashlib
import pathlib
import subprocess
import tempfile
import urllib.request
import zipfile

PINS = {
    "mahou": ("sha256", "4022fb3cee32b28df02994cc4c5b40069651583516f6abd04775789616739aad",
        ["https://cdn.modrinth.com/data/7jOxn6qJ/versions/GrYWbBFj/mahoutsukai-1.21.1-v1.36.31.jar"]),
    "astral": ("sha1", "66f36655eae7cb404c9c16da6f33db31dff6ed9a",
        ["https://mediafilez.forgecdn.net/files/9281/73/AstralSorcery-2.0.3.59.jar",
         "https://mediafilez.forgecdn.net/files/9281/073/AstralSorcery-2.0.3.59.jar",
         "https://media.forgecdn.net/files/9281/73/AstralSorcery-2.0.3.59.jar"]),
}
def audit(provider, path):
    with zipfile.ZipFile(path) as z:
        names = sorted(n for n in z.namelist() if n.endswith(".class"))
        if provider == "mahou":
            candidates = [n for n in names if "drainlife" in n.lower().replace("_","")]
            candidates += ["stepsword/mahoutsukai/util/EffectUtil.class","stepsword/mahoutsukai/tile/MahoujinTileEntity.class"]
        else:
            candidates = [n for n in names if n.endswith("/RootPerkDiscidia.class")]
        if not candidates:
            raise RuntimeError("No relevant classes for " + provider)
        print("CANDIDATES", provider, candidates, flush=True)
        for n in candidates:
            cls = n[:-6].replace("/", ".")
            print("CLASS", cls, flush=True)
            out = subprocess.run(["javap","-p","-s","-c","-l","-classpath",str(path),cls],
                text=True, capture_output=True, check=True, timeout=60).stdout
            print(out, flush=True)
        if provider == "mahou":
            # Enumerate inheritance/UUID evidence to guide a narrow owner read.
            for n in names:
                if n.startswith("stepsword/mahoutsukai/tile/") and (
                    "mahoujin" in n.lower() or "barrier" in n.lower() or "boundary" in n.lower()):
                    raw=z.read(n)
                    if b"java/util/UUID" in raw:
                        cls=n[:-6].replace("/", ".")
                        print("OWNER_CANDIDATE", cls, flush=True)
                        print(subprocess.run(["javap","-p","-s","-classpath",str(path),cls],
                            text=True,capture_output=True,check=True,timeout=30).stdout,flush=True)
with tempfile.TemporaryDirectory() as tmp:
    failures=[]
    for provider,(algo,expected,urls) in PINS.items():
        try:
            for url in urls:
                try:
                    request=urllib.request.Request(url,headers={"User-Agent":"TMM-static-ABI/1.0"})
                    with urllib.request.urlopen(request,timeout=60) as response:
                        data=response.read()
                    if hashlib.new(algo,data).hexdigest()!=expected:
                        raise ValueError("hash mismatch")
                    break
                except Exception as err:
                    print("DOWNLOAD_ATTEMPT",provider,str(err),flush=True)
            else:
                raise RuntimeError("No hash-verified JAR")
            path=pathlib.Path(tmp)/(provider+".jar")
            path.write_bytes(data)
            print("VERIFIED",provider,expected,flush=True)
            audit(provider,path)
        except Exception as err:
            failures.append(provider)
            print("AUDIT_FAILED",provider,str(err),flush=True)
    if failures:
        raise SystemExit("Failed exact-pin verification: "+",".join(failures))
