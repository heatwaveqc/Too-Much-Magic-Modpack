#!/usr/bin/env python3
"""Read-only exact candidate census. No gameplay, installation or pin writes."""
import concurrent.futures
import hashlib
import io
import json
import tomllib
import urllib.parse
import urllib.request
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
UA = {"User-Agent": "TooMuchMagic-targeted-QA-audit/1.0"}
HOSTS = {"cdn.modrinth.com","mediafilez.forgecdn.net","edge.forgecdn.net"}

def get(url, binary=False):
    parsed = urllib.parse.urlparse(url)
    allowed = HOSTS if binary else {"api.modrinth.com"}
    if parsed.scheme != "https" or parsed.hostname not in allowed:
        raise ValueError("Unapproved URL")
    with urllib.request.urlopen(urllib.request.Request(url, headers=UA), timeout=60) as response:
        if urllib.parse.urlparse(response.geturl()).hostname not in allowed:
            raise ValueError("Unapproved redirected host")
        data = response.read(160_000_001)
        if len(data) > 160_000_000:
            raise ValueError("Artifact too large for targeted audit")
        return data

def audit(row):
    out = dict(row)
    try:
        if row["provider"] == "modrinth":
            if "version_id" in row:
                meta = json.loads(get("https://api.modrinth.com/v2/version/" + row["version_id"]))
                if meta["id"] != row["version_id"]:
                    raise ValueError("Version ID mismatch")
            else:
                entries = json.loads(get("https://api.modrinth.com/v2/project/" + row["project_slug"] + "/version"))
                select = [v for v in entries if v["version_number"] == row["version_number"] and "1.21.1" in v["game_versions"] and "neoforge" in v["loaders"]]
                if len(select) != 1:
                    raise ValueError("Exact version label not unique; verify candidate ID")
                meta = select[0]
            if "1.21.1" not in meta["game_versions"] or "neoforge" not in meta["loaders"]:
                raise ValueError("Wrong game/loader metadata")
            files = [f for f in meta["files"] if f["primary"] and f["filename"].endswith(".jar")]
            if len(files) != 1:
                raise ValueError("No unique primary JAR")
            f = files[0]
            data = get(f["url"], True)
            if hashlib.sha512(data).hexdigest() != f["hashes"]["sha512"]:
                raise ValueError("Provider hash mismatch")
            out.update(project_id=meta["project_id"],version_id=meta["id"],filename=f["filename"],url=f["url"],version=meta["version_number"],channel=meta["version_type"],provider_dependencies=meta["dependencies"])
            project = json.loads(get("https://api.modrinth.com/v2/project/" + meta["project_id"]))
            out.update(client_side=project.get("client_side"), server_side=project.get("server_side"))
        else:
            fid=row["file_id"]
            path=f"/files/{fid//1000}/{fid%1000:03d}/{urllib.parse.quote(row['filename'])}"
            errors=[]
            for host in ("mediafilez.forgecdn.net","edge.forgecdn.net"):
                try:
                    data=get("https://"+host+path,True)
                    with zipfile.ZipFile(io.BytesIO(data)):
                        pass
                    break
                except Exception as exc:
                    errors.append(str(exc))
            else:
                raise ValueError("CDN retrieval failed: " + "; ".join(errors))
        with zipfile.ZipFile(io.BytesIO(data)) as jar:
            paths=set(jar.namelist())
            meta_path=next((p for p in ("META-INF/neoforge.mods.toml","META-INF/mods.toml") if p in paths),None)
            if not meta_path:
                raise ValueError("Missing mod loader metadata")
            metadata=tomllib.loads(jar.read(meta_path).decode())
            out.update(hash_sha1=hashlib.sha1(data).hexdigest(),hash_sha512=hashlib.sha512(data).hexdigest(),
                       jar_metadata=metadata,metadata_path=meta_path,
                       jarjar=json.loads(jar.read("META-INF/jarjar/metadata.json")) if "META-INF/jarjar/metadata.json" in paths else None,
                       status="BYTE_AUDITED_NOT_PINNED")
    except Exception as exc:
        out.update(status="BLOCKED",error=str(exc))
    print(json.dumps(out,sort_keys=True),flush=True)
    return out

def main():
    rows=json.loads((ROOT/"manifest/next-qa-target.json").read_text())["candidate_artifacts"]
    with concurrent.futures.ThreadPoolExecutor(max_workers=4) as pool:
        results=list(pool.map(audit,rows))
    Path("qa-candidate-artifact-audit.json").write_text(json.dumps(results,indent=2)+"\n")
    print("Artifact audits: %d/%d exact bytes examined. Dependency closure and inclusion require reviewed pin changes." % (sum(r["status"]=="BYTE_AUDITED_NOT_PINNED" for r in results),len(results)))
if __name__=="__main__":
    main()
