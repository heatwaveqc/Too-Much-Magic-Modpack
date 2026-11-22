#!/usr/bin/env python3
"""Stage hash-verified local preview library into an isolated disposable QA game directory only."""
import argparse, hashlib, json, pathlib, shutil
p=argparse.ArgumentParser()
p.add_argument("bundle",type=pathlib.Path,help="Extracted local preview bundle root")
p.add_argument("game_dir",type=pathlib.Path,help="Disposable Minecraft instance/server root")
p.add_argument("--side",choices=("client","dedicatedServer"),required=True)
p.add_argument("--qa-only",action="store_true",required=True)
a=p.parse_args()
catalog=json.loads(pathlib.Path(__file__).with_name("prefab-qa-catalog.json").read_text())
assert catalog["status"]=="QA_PREVIEW_ONLY"
src=a.bundle/"prefab-extension"
dst=a.game_dir/catalog["paths"][a.side]
assert a.game_dir.is_dir(), "Create an isolated QA instance first"
files=[]
for row in catalog["templates"]:
    filename=row["filename"]
    assert pathlib.Path(filename).name==filename and filename.endswith(".nbt")
    inp=src/filename
    assert hashlib.sha256(inp.read_bytes()).hexdigest()==row["sha256"], f"Hash mismatch: {filename}"
    out=dst/filename
    if out.exists():
        assert hashlib.sha256(out.read_bytes()).hexdigest()==row["sha256"], f"Refusing overwrite: {out}"
    files.append((inp,out))
dst.mkdir(parents=True,exist_ok=True)
for inp,out in files:
    if not out.exists(): shutil.copyfile(inp,out)
print(f"Staged {len(files)} static previews in {dst}; not a runtime/placement PASS")
