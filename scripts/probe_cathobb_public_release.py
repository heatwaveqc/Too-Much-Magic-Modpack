#!/usr/bin/env python3
"""Read-only publisher-file probe of Cathobb's Unified 64 v40.

Downloads only into an ephemeral CI directory (NEVER into the git repo or any
Prism artifact), streams the official ZIP, checks structure and reports its
SHA-1/SHA-512 for reviewable Packwiz metadata. No game launch.
"""
from __future__ import annotations
import hashlib
import io
import json
import os
import tempfile
import zipfile
from urllib.error import HTTPError, URLError
from urllib.request import Request, urlopen

FILE_ID=9411491
PROJECT_ID=1771113
FILENAME="cathobbs-Unified-v40-64x-CANDIDATE.zip"
LIMIT=270 * 1024 * 1024
SOURCES=[
 f"https://mediafilez.forgecdn.net/files/{FILE_ID//1000}/{FILE_ID%1000:03d}/{FILENAME}",
 f"https://media.forgecdn.net/files/{FILE_ID//1000}/{FILE_ID%1000:03d}/{FILENAME}",
 f"https://www.curseforge.com/api/v1/mods/{PROJECT_ID}/files/{FILE_ID}/download",
]

def main():
    errors=[]
    with tempfile.TemporaryDirectory(prefix="tmm-cathobb-probe-") as t:
        path=os.path.join(t,"public-cathobb-candidate.zip")
        for url in SOURCES:
            try:
                req=Request(url,headers={"User-Agent":"TooMuchMagic-Packwiz-LicenseSafeProbe/1.0","Accept":"application/octet-stream"})
                h1=hashlib.sha1()
                h512=hashlib.sha512()
                length=0
                with urlopen(req,timeout=120) as response, open(path,"wb") as dst:
                    ct=response.headers.get("Content-Type","")
                    if "text/html" in ct:
                        raise ValueError("Publisher returned HTML instead of resource-pack ZIP")
                    while True:
                        b=response.read(1024*1024)
                        if not b:break
                        length+=len(b)
                        if length>LIMIT:raise ValueError("Publisher file exceeds 270MiB safety limit")
                        h1.update(b)
                        h512.update(b)
                        dst.write(b)
                with zipfile.ZipFile(path) as z:
                    names=z.namelist()
                    if len(names)>300000:raise ValueError("Too many ZIP entries")
                    packs=[s for s in names if s in ("pack.mcmeta","assets/minecraft/pack.mcmeta")]
                    packmeta=json.loads(z.read("pack.mcmeta").decode("utf-8")) if "pack.mcmeta" in names else None
                    bad=z.testzip()
                    if bad:raise ValueError("ZIP CRC failure: "+bad)
                    stats={"file_id":FILE_ID,"project_id":PROJECT_ID,"filename":FILENAME,
                           "official_publisher_url":url,"bytes":length,
                           "sha1":h1.hexdigest(),"sha512":h512.hexdigest(),
                           "file_count":len(names),"root_pack_mcmeta":bool(packmeta),
                           "pack_description":packmeta.get("pack",{}).get("description") if packmeta else None,
                           "has_assets":any(n.startswith("assets/") for n in names),
                           "root_entries":names[:12], "downloaded_binary_published":False}
                print(json.dumps({"status":"OK","cathobb":stats},indent=2))
                if not packmeta or not stats["has_assets"]:
                    raise ValueError("Not a valid directly selectable resource-pack ZIP root")
                print("PASS: official ZIP SHA and structure. Binary NOT saved to repository/artifact.")
                return 0
            except (HTTPError,URLError,TimeoutError,OSError,ValueError,zipfile.BadZipFile,json.JSONDecodeError) as e:
                errors.append({"url":url,"error":str(e)})
                if os.path.exists(path):os.unlink(path)
        print(json.dumps({"status":"BLOCKED","official_sources":errors},indent=2))
        return 1

if __name__=="__main__":
    raise SystemExit(main())
