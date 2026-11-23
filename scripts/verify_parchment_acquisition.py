#!/usr/bin/env python3
"""Type-A regression gate for integrated #318 Parchment Fragment acquisition.

The five Gateways are generator-owned, and their source must retain exactly
the owner-approved completion rewards. Read files only; no game/bootstrap.
"""
from __future__ import annotations

import hashlib
import json
import sys
import tomllib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
FRAGMENT = "improvableskills:parchment_fragment"
GATEWAYS = {
    "necromancer": 1,
    "pyromancer": 1,
    "cryomancer": 1,
    "cultist": 1,
    "archevoker": 2,
}
GATEWAY_DIR = Path("kubejs/data/tmm/gateways")
FISHING_SCRIPT = Path("kubejs/server_scripts/parchment_fishing.js")


def require(condition: bool, message: str) -> None:
    if not condition:
        raise ValueError(message)


def contains_fragment(node: object) -> bool:
    if isinstance(node, dict):
        return any(contains_fragment(value) for value in node.values())
    if isinstance(node, list):
        return any(contains_fragment(value) for value in node)
    return node == FRAGMENT


def gateway_check(root: Path, gateway: str, count: int) -> Path:
    path = GATEWAY_DIR / ("mob_irons_" + gateway + ".json")
    data = json.loads((root / path).read_text(encoding="utf-8"))
    require(data.get("type") == "gateways:normal", str(path) + ": expected native normal Gateway")
    require(isinstance(data.get("waves"), list) and bool(data["waves"]),
            str(path) + ": no waves to complete")
    rewards = data.get("rewards")
    require(isinstance(rewards, list), str(path) + ": missing completion rewards")
    fragments = [
        reward for reward in rewards
        if isinstance(reward, dict)
        and reward.get("type") == "gateways:stack"
        and isinstance(reward.get("stack"), dict)
        and reward["stack"].get("id") == FRAGMENT
    ]
    require(len(fragments) == 1, str(path) + ": must have exactly one fragment completion reward")
    require(fragments[0]["stack"].get("count") == count,
            str(path) + ": wrong fragment stack count")
    require(sum(
        1 for reward in rewards
        if isinstance(reward, dict)
        and reward.get("type") == "gateways:experience"
        and reward.get("experience") == 40
        and reward.get("orb_size") == 20
    ) == 1, str(path) + ": original 40-XP completion reward missing/duplicated")
    require(not contains_fragment(data.get("failures", [])),
            str(path) + ": forbidden fragment failure reward")
    require(not contains_fragment(data["waves"]),
            str(path) + ": fragment must not be a partial-wave reward")
    return path


def check_packwiz(root: Path, paths: list[Path]) -> None:
    index_file = root / "index.toml"
    index = tomllib.loads(index_file.read_text(encoding="utf-8"))
    entries: dict[str, list[dict]] = {}
    for row in index.get("files", []):
        entries.setdefault(row["file"], []).append(row)
    for path in paths:
        rows = entries.get(path.as_posix(), [])
        require(len(rows) == 1, str(path) + ": expected one Packwiz index entry")
        expected = hashlib.sha256((root / path).read_bytes()).hexdigest()
        require(rows[0].get("hash") == expected,
                str(path) + ": index.toml SHA-256 is stale")
    pack = tomllib.loads((root / "pack.toml").read_text(encoding="utf-8"))
    meta = pack["index"]
    require(meta.get("file") == "index.toml" and meta.get("hash-format") == "sha256",
            "pack.toml: unexpected Packwiz index declaration")
    require(meta.get("hash") == hashlib.sha256(index_file.read_bytes()).hexdigest(),
            "pack.toml: Packwiz index SHA-256 is stale")


def main() -> int:
    root = ROOT
    if len(sys.argv) == 3 and sys.argv[1] == "--root":
        root = Path(sys.argv[2]).resolve()
    elif len(sys.argv) != 1:
        print("Usage: verify_parchment_acquisition.py [--root <repo>]", file=sys.stderr)
        return 2
    try:
        paths = [gateway_check(root, gate, count) for gate, count in GATEWAYS.items()]
        fishing = (root / FISHING_SCRIPT).read_text(encoding="utf-8")
        require("minecraft:gameplay/fishing/treasure" in fishing,
                "Parchment: ordinary fishing treasure table is not selected")
        require("pool.addEntry" in fishing and "LootEntry.of(TMM_PARCHMENT_TREASURE_ID)" in fishing,
                "Parchment: ordinary treasure-pool entry missing")
        require("createPool(" not in fishing and "randomChance(" not in fishing,
                "Parchment: independent bonus chance/pool is forbidden")
        paths.append(FISHING_SCRIPT)
        check_packwiz(root, paths)
    except (ValueError, OSError, KeyError, TypeError, tomllib.TOMLDecodeError,
            json.JSONDecodeError) as exc:
        print("FAIL #318 Parchment Fragment source contract: " + str(exc), file=sys.stderr)
        return 1
    print("PASS #318: five normal Gateway completion rewards (1/1/1/1/2), original XP,")
    print("          weighted ordinary fishing treasure source and exact Packwiz hashes")
    print("          Type A only: no loaded pool, Aquaculture, EMC or gameplay PASS")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
