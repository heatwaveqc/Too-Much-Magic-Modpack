#!/usr/bin/env python3
"""Publish only a verified, owner-authorized, already-built Prism QA artifact.
No game boot, code execution from ZIP, downloads of third-party mods, or stable promotion.
"""
from __future__ import annotations
import argparse
import hashlib
import json
import os
from pathlib import Path, PurePosixPath
import re
import subprocess
import tempfile
import zipfile

REPO = "heatwaveqc/Too-Much-Magic-Modpack"
ROOT = Path(__file__).resolve().parents[1]
ARTIFACT = "Too-Much-Magic-Full-Integration-Prism-UNSTABLE"
FIXED = "Tyr-Unstable.zip"
RELEASE = "unstable"
SHA = r"[0-9a-f]{40}"
LABEL = r"QA run ([0-9]+) attempt ([0-9]+) snapshot (" + SHA + r")"

def require(ok, message):
    if not ok:
        raise RuntimeError(message)

def gh(*args):
    result = subprocess.run(["gh", *args], check=True, stdout=subprocess.PIPE)
    return result.stdout

def api(path, *args):
    return json.loads(gh("api", f"repos/{REPO}/{path}", *args))

def read_run(run_id):
    require(re.fullmatch(r"[0-9]+", str(run_id)), "Invalid source run ID")
    run = api(f"actions/runs/{run_id}")
    require(run["id"] == int(run_id) and run["status"] == "completed" and run["conclusion"] == "success"
            and run["event"] == "workflow_dispatch" and run["head_branch"] == "main"
            and run["path"].split("@")[0] == ".github/workflows/full-integration-launcher.yml",
            "Source is not a successful authorized main launcher")
    attempt = run["run_attempt"]
    jobs = api(f"actions/runs/{run_id}/attempts/{attempt}/jobs?per_page=100")["jobs"]
    matches = [j for j in jobs if j["name"].endswith("integration-preflight-and-test") and j["conclusion"] == "success"]
    require(len(matches) == 1, "No unique passing integration job")
    steps = {s["name"]:s["conclusion"] for s in matches[0]["steps"]}
    for name in ("Integration Assembly — explicitly authorized, never on automatic push",
                 "Full dedicated-server smoke with selected first-party runtime modules",
                 "Fail if standalone module is absent or retired modules leak",
                 "Freeze immutable, server-tested integration snapshot without moving rolling branch",
                 "Produce commit-locked all-workstreams Prism client ZIP",
                 "Upload the one comprehensive unstable test build"):
        require(steps.get(name) == "success", "Missing successful evidence: "+name)
    tag = f"integration-qa/{run_id}-{attempt}"
    obj = api("git/ref/tags/"+tag)["object"]
    require(obj["type"] == "tag", "Expected annotated immutable QA tag")
    target = api("git/tags/"+obj["sha"])["object"]
    snap = target["sha"]
    require(target["type"] == "commit" and re.fullmatch(SHA, snap), "Invalid frozen commit")
    matches = [a for a in api(f"actions/runs/{run_id}/artifacts?per_page=100")["artifacts"]
               if a["name"] == ARTIFACT and not a["expired"]]
    require(len(matches) == 1, "No unique unexpired Prism artifact")
    artifact = matches[0]
    require(artifact["workflow_run"]["id"] == int(run_id) and
            re.fullmatch(r"sha256:[a-f0-9]{64}", artifact.get("digest","") or ""),
            "Missing verified source artifact digest")
    return run, attempt, snap, artifact

def download(artifact, destination):
    # curl strips auth on cross-host redirects; never use --location-trusted.
    subprocess.run(["curl","--fail","--silent","--show-error","--location",
                    "--retry","2","--header","Authorization: Bearer "+os.environ["GH_TOKEN"],
                    "--header","Accept: application/vnd.github+json",
                    "--output",str(destination),
                    f"https://api.github.com/repos/{REPO}/actions/artifacts/{int(artifact['id'])}/zip"], check=True)
    digest = hashlib.sha256(destination.read_bytes()).hexdigest()
    require("sha256:"+digest == artifact["digest"], "Artifact digest does not match GitHub")
    return digest

def validate_zip(path, snap, run_id, attempt):
    with zipfile.ZipFile(path) as z:
        names = z.namelist()
        require(len(names) == len(set(names)), "Duplicate members")
        require(all(not PurePosixPath(n).is_absolute() and ".." not in PurePosixPath(n).parts and "\\" not in n for n in names),
                "Unsafe archive member")
        required = {"instance.cfg","mmc-pack.json","BUILD-MANIFEST.json","INSTALL-AND-TEST.txt",
                    "minecraft/packwiz-installer-bootstrap.jar","minecraft/options.txt",
                    "minecraft/tmm-qa-preferences.ps1","minecraft/IMPORT-OLD-TMM-PREFERENCES.cmd"}
        require(required.issubset(names), "Not a single-level personal QA Prism instance")
        meta = json.loads(z.read("BUILD-MANIFEST.json"))
        require(meta["channel"] == "INTEGRATION-NOT-STABLE" and meta["integration_commit"] == snap
                and meta["stable_promoted"] is False and meta["client_tested"] is False
                and meta["personal_settings"]["public_release"] is False, "Wrong build identity/channel")
        require(re.fullmatch(SHA, meta["baseline_sha"]), "Missing approved baseline")
        require(f"https://raw.githubusercontent.com/{REPO}/{snap}/pack.toml".encode() in z.read("instance.cfg"), "Installer floats or uses wrong snapshot")
        require(not any(line.startswith("key_") for line in z.read("minecraft/options.txt").decode().splitlines()), "Personal keybinds in QA seed")
        jars = meta["custom_jar_sha256"]
        require(bool(jars) and all(re.fullmatch(r"(tmm_[a-z0-9_]+|traders_vengence)-[a-zA-Z0-9._-]+\.jar", n) for n in jars), "Unexpected non-first-party JAR")
        allowed = required | {"minecraft/mods/"+n for n in jars}
        require(set(n for n in names if not n.endswith("/")) == allowed, "Unexpected files in QA package")
        for name, digest in jars.items():
            require(hashlib.sha256(z.read("minecraft/mods/"+name)).hexdigest() == digest, "First-party JAR digest mismatch: "+name)
        require(z.testzip() is None, "Bad archive CRC")
    return meta

def release_or_none():
    result = subprocess.run(["gh","api",f"repos/{REPO}/releases/tags/{RELEASE}"],capture_output=True)
    if result.returncode:
        require(b"HTTP 404" in result.stderr, "Cannot read rolling release")
        return None
    rel = json.loads(result.stdout)
    require(rel["tag_name"] == RELEASE and rel["prerelease"] and not rel["draft"] and not rel.get("immutable",False),
            "Refusing to edit a stable, draft, immutable or unexpected release")
    return rel

def current_asset(rel):
    if rel is None: return None
    matches = [a for a in rel["assets"] if a["name"] == FIXED]
    require(len(matches) <= 1, "Ambiguous rolling asset")
    return matches[0] if matches else None

def label_identity(asset):
    match = re.fullmatch(LABEL, asset.get("label") or "")
    require(match is not None, "Existing rolling asset lacks provenance label")
    return int(match[1]), int(match[2]), match[3]

def patch_asset(asset, name, label):
    result = api(f"releases/assets/{asset['id']}","-X","PATCH","-f","name="+name,"-f","label="+label)
    require(result["name"] == name and result["label"] == label,"Asset rename not confirmed")
    return result

def publish(run_id, attempt, snap, artifact, path, digest, meta):
    label = f"QA run {run_id} attempt {attempt} snapshot {snap}"
    identity = (int(run_id), int(attempt))
    rel = release_or_none()
    existing = current_asset(rel)
    if existing:
        old_identity = label_identity(existing)
        if old_identity[:2] >= identity:
            print("Fixed URL already points to this or a newer verified run; no publication.")
            return
    notes = (f"**TEST WORLDS ONLY — UNSTABLE, NOT STABLE OR CLIENT-ACCEPTED.**\n\n"
             f"Personal Windows QA preference hooks are intentionally included, but no actual saved settings or worlds.\n\n"
             f"Latest published source: [run {run_id}](https://github.com/{REPO}/actions/runs/{run_id}), "
             f"frozen snapshot [{snap}](https://github.com/{REPO}/commit/{snap}).\n\n"
             f"Prism ZIP SHA-256: \`{digest}\`.\n"
             f"Download {FIXED}; older named assets are retained. The release tag is a channel anchor, "
             "not the current pack commit; every ZIP pins its actual immutable packwiz source.\n"
             "This does not publish a stable release or certify gameplay. Scope omissions are recorded in the source QA ledger.\n")
    if rel is None:
        gh("release","create",RELEASE,"--repo",REPO,"--target",snap,"--prerelease","--latest=false",
           "--title","Tyr — latest unstable QA (test worlds only)","--notes",notes)
        rel = release_or_none()
    staged_name = f"Tyr-Unstable-{run_id}-{attempt}.zip"
    require(not any(a["name"] == staged_name for a in rel["assets"]), "Stage already exists; inspect publication before retry")
    staged_file = path.with_name(staged_name)
    path.rename(staged_file)
    gh("release","upload",RELEASE,str(staged_file)+"#"+label,"--repo",REPO)
    rel = release_or_none()
    staged = next(a for a in rel["assets"] if a["name"] == staged_name)
    require(staged["state"] == "uploaded" and staged["digest"] == "sha256:"+digest and staged["size"] == staged_file.stat().st_size, "Uploaded staged asset failed integrity check")
    existing = current_asset(rel)
    archived = None
    if existing:
        old_run, old_attempt, old_snap = label_identity(existing)
        require((old_run,old_attempt) < identity,"Newer asset appeared")
        archive_name = f"Tyr-Unstable-{old_run}-{old_attempt}.zip"
        require(not any(a["name"] == archive_name for a in rel["assets"]), "Prior archive already exists; do not overwrite")
        archived = patch_asset(existing, archive_name, existing["label"])
    try:
        final = patch_asset(staged,FIXED,label)
        require(final["digest"] == "sha256:"+digest,"Fixed asset digest changed")
    except Exception:
        # If the server applied the rename despite a lost response, keep the new
        # verified fixed asset; otherwise restore the prior working asset name.
        cur = current_asset(release_or_none())
        if archived and cur is None:
            patch_asset(archived,FIXED,archived["label"])
        raise
    gh("release","edit",RELEASE,"--repo",REPO,"--prerelease","--latest=false","--notes",notes)
    print("PUBLISHED https://github.com/"+REPO+"/releases/download/unstable/"+FIXED)
    print("SHA-256 "+digest)

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--check",action="store_true")
    args = parser.parse_args()
    seed = json.loads((ROOT/"manifest/unstable-publication-seed.json").read_text())
    run_id = os.environ.get("SOURCE_RUN") or str(seed["run_id"])
    run, attempt, snap, artifact = read_run(run_id)
    if not os.environ.get("SOURCE_RUN"):
        require((attempt,snap,artifact["id"],artifact["digest"]) ==
                (seed["attempt"],seed["snapshot"],seed["artifact_id"],seed["digest"]), "Seed identity changed")
    if not args.check:
        existing = current_asset(release_or_none())
        if existing and label_identity(existing)[:2] >= (int(run_id),attempt):
            print("Publication already current/newer; no changes")
            return
    with tempfile.TemporaryDirectory(prefix="tmm-unstable-") as tmp:
        path = Path(tmp)/FIXED
        digest = download(artifact,path)
        meta = validate_zip(path,snap,run_id,attempt)
        print(f"VERIFIED run {run_id}, snapshot {snap}, artifact {artifact['id']}, {meta['pinned_mod_count']} pins, {len(meta['custom_jar_sha256'])} first-party JARs, sha256 {digest}")
        if not args.check:
            publish(run_id,attempt,snap,artifact,path,digest,meta)
        else:
            print("CHECK ONLY — no release or tag writes")
if __name__ == "__main__":
    main()
