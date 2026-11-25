#!/usr/bin/env python3
"""Download hash-pinned Packwiz server mods; quick origin audit never boots Minecraft."""
from __future__ import annotations

import argparse
import hashlib
import pathlib
import re
import time
import tomllib
import urllib.request
from urllib.parse import quote, urlparse

ROOT = pathlib.Path(__file__).resolve().parents[1]
CDN_HOSTS = {"cdn.modrinth.com", "edge.forgecdn.net", "mediafilez.forgecdn.net"}
# Exact release assets previously approved and hash-pinned in #569/#572.
# GitHub is NOT a general-purpose artifact origin; changing these paths requires review.
GITHUB_ASSETS = {
    # Preserve publisher URL and digest; install under a loader-safe local basename.
    "multiplayerspawners-2.0.2.jar":
        "https://github.com/The-Plum-Team/Multiplayer-Spawners-Mod/releases/download/v2.0.2/Multiplayer.Spawners.-.NeoForge.-.1.21.1.-.2.0.2.jar",
    "qshop-neoforge-1.21.1-1.8.5.jar":
        "https://github.com/QLNPLUS/Q-shop/releases/download/v1.8.5-neoforge-1.21.1/qshop-neoforge-1.21.1-1.8.5.jar",
    # Client-only; retained for explicit origin checks, never installed by server smoke.
    "keyset-neoforge-1.3.0+mc1.21.1.jar":
        "https://github.com/Meneleet-official/keyset/releases/download/v1.3.0/keyset-neoforge-1.3.0%2Bmc1.21.1.jar",
}
GITHUB_REDIRECT_HOSTS = {"release-assets.githubusercontent.com", "objects.githubusercontent.com"}


def validate_origin(url: str, filename: str) -> None:
    parsed = urlparse(url)
    if (parsed.scheme == "https" and not parsed.username and not parsed.password
            and parsed.port in (None, 443)):
        if parsed.hostname in CDN_HOSTS:
            return
        if GITHUB_ASSETS.get(filename) == url:
            return
    raise ValueError(f"Unreviewed artifact origin for {filename}: {parsed.hostname}")


class GitHubAssetRedirect(urllib.request.HTTPRedirectHandler):
    """Allow only HTTPS GitHub's release asset delivery hosts after exact URL review."""
    def redirect_request(self, req, fp, code, msg, headers, newurl):
        parsed = urlparse(newurl)
        if (parsed.scheme != "https" or parsed.hostname not in GITHUB_REDIRECT_HOSTS
                or parsed.username or parsed.password or parsed.port not in (None, 443)):
            raise ValueError(f"Unreviewed GitHub release redirect: {parsed.hostname}")
        return super().redirect_request(req, fp, code, msg, headers, newurl)


def source_urls(mod: dict) -> list[str]:
    filename = mod["filename"]
    if (pathlib.PurePosixPath(filename).name != filename or "\\" in filename
            or not filename.endswith(".jar")):
        raise ValueError(f"Unsafe filename: {filename}")
    info = mod["download"]
    urls = [info["url"]] if info.get("url") else []
    if not urls and info.get("mode") == "metadata:curseforge":
        file_id = mod.get("update", {}).get("curseforge", {}).get("file-id")
        if not isinstance(file_id, int) or file_id <= 0:
            raise ValueError(f"CurseForge metadata missing file ID for {filename}")
        remote_path = f"/files/{file_id//1000}/{file_id%1000:03d}/{quote(filename)}"
        urls = [host + remote_path for host in ("https://mediafilez.forgecdn.net", "https://edge.forgecdn.net")]
    if not urls:
        raise ValueError(f"Missing approved download metadata for {filename}")
    algorithm = info["hash-format"]
    if algorithm not in {"sha512", "sha256", "sha1"}:
        raise ValueError(f"Unsupported hash algorithm {algorithm}")
    if not re.fullmatch(r"[0-9a-fA-F]+", info["hash"]) or len(info["hash"]) != hashlib.new(algorithm).digest_size * 2:
        raise ValueError(f"Malformed pinned hash for {filename}")
    for url in urls:
        validate_origin(url, filename)
    return urls


def selected_pins(root: pathlib.Path, *, include_client: bool = False):
    index = tomllib.loads((root / "index.toml").read_text())
    seen = set()
    for item in index.get("files", []):
        path = pathlib.PurePosixPath(item["file"])
        if not item.get("metafile") or path.parts[0] != "mods":
            continue
        if len(path.parts) != 2 or not path.name.endswith(".pw.toml"):
            raise ValueError(f"Unexpected mod metafile path: {path}")
        mod = tomllib.loads((root / path).read_text())
        if mod.get("side", "both") not in {"client", "server", "both"}:
            raise ValueError(f"Invalid mod side: {path}")
        if not include_client and mod.get("side", "both") == "client":
            continue
        filename = mod["filename"]
        if filename in seen:
            raise ValueError(f"Duplicate selected filename: {filename}")
        seen.add(filename)
        yield mod


def fetch_verified(mod: dict) -> bytes:
    filename = mod["filename"]
    info = mod["download"]
    last_error = None
    for url in source_urls(mod):
        opener = (urllib.request.build_opener(GitHubAssetRedirect()).open
                  if urlparse(url).hostname == "github.com" else urllib.request.urlopen)
        for attempt in range(2):
            try:
                req = urllib.request.Request(url, headers={"User-Agent": "TooMuchMagic-CI/0.2"})
                with opener(req, timeout=90) as response:
                    data = response.read()
                if hashlib.new(info["hash-format"], data).hexdigest().lower() != info["hash"].lower():
                    raise ValueError(f"Hash mismatch for {filename}; will not install")
                return data
            except (OSError, TimeoutError) as exc:
                last_error = exc
                time.sleep(1 + attempt)
    raise ValueError(f"Unable to retrieve hash-verified {filename}: {last_error}")


def check_origins(root: pathlib.Path = ROOT, *, verify_github: bool = False) -> int:
    # Audit the exact full selected roster without downloading it. Only the
    # explicit, tiny allowlisted GitHub subset may be retrieved by quick CI.
    pins = list(selected_pins(root, include_client=True))
    if not pins:
        raise ValueError("Refusing empty roster origin audit")
    for mod in pins:
        urls = source_urls(mod)
        if verify_github and any(urlparse(url).hostname == "github.com" for url in urls):
            data = fetch_verified(mod)
            print(f"PASS exact GitHub release hash: {mod['filename']} ({len(data)} bytes)")
    print(f"PASS: reviewed origins and digest formats for {len(pins)} indexed mod pins")
    return len(pins)


def download(out_dir: pathlib.Path) -> int:
    pins = list(selected_pins(ROOT))
    if not pins:
        raise ValueError("Refusing modded smoke with zero mods")
    # Fail on unsupported origins before any network IO or partial installation.
    for mod in pins:
        source_urls(mod)
    out_dir.mkdir(parents=True, exist_ok=True)
    for mod in pins:
        data = fetch_verified(mod)
        (out_dir / mod["filename"]).write_bytes(data)
        print(f"PASS hash verified: {mod['filename']} ({len(data)} bytes)")
    print(f"PASS: fetched and hashed {len(pins)} server-side mod JARs")
    return len(pins)


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("target", nargs="?", type=pathlib.Path)
    parser.add_argument("--check-origins", action="store_true")
    parser.add_argument("--verify-github", action="store_true")
    args = parser.parse_args()
    if args.check_origins or args.verify_github:
        if args.target is not None:
            parser.error("origin audit does not install JARs")
        check_origins(verify_github=args.verify_github)
    elif args.target is not None:
        download(args.target)
    else:
        parser.error("specify a target-mods-dir or --check-origins")
