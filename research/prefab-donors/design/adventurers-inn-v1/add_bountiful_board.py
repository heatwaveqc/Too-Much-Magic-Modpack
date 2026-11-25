#!/usr/bin/env python3
"""Patch the original Adventurer's Inn structure with one Bountiful board.

Run AFTER rebuild.py. Uses only stdlib; no game boot, no NBT library, no
re-distribution of mod assets. Fails closed on unexpected source structure.
"""
from __future__ import annotations
import gzip
import hashlib
import struct
from pathlib import Path

HERE = Path(__file__).resolve().parent
ORIGINAL_RAW_SHA256 = "c13847924de17b043f32f15c75792738a654d122f42be45ec15191cb9feeb902"
TARGET = (8, 7, 4)
DECREES = ("tmm_hunting", "tmm_culinary", "tmm_fishing")
BOARD = "bountiful:bountyboard"
BE_TYPE = "bountiful:board-be"

def i32(value: int) -> bytes:
    return struct.pack(">i", value)

def i64(value: int) -> bytes:
    return struct.pack(">q", value)

def string(value: str) -> bytes:
    data = value.encode("utf-8")
    assert len(data) < 65536
    return struct.pack(">H", len(data)) + data

def tag(name: str, kind: int, value: bytes) -> bytes:
    return bytes((kind,)) + string(name) + value

def compound(**fields: tuple[int, bytes]) -> bytes:
    return b"".join(tag(name, kind, body) for name, (kind, body) in fields.items()) + b"\0"

def list_of(kind: int, payloads: list[bytes]) -> bytes:
    return bytes((kind,)) + i32(len(payloads)) + b"".join(payloads)

def require(cond: bool, problem: str) -> None:
    if not cond:
        raise ValueError(problem)

def u16(buf: bytes, off: int) -> int:
    return struct.unpack_from(">H", buf, off)[0]

def sint(buf: bytes, off: int) -> int:
    return struct.unpack_from(">i", buf, off)[0]

def read_string(buf: bytes, off: int) -> tuple[str, int]:
    n = u16(buf, off)
    off += 2
    return buf[off:off+n].decode("utf-8"), off+n

def skip(buf: bytes, off: int, kind: int) -> int:
    if kind in (1, 2, 3, 4, 5, 6):
        return off + {1:1,2:2,3:4,4:8,5:4,6:8}[kind]
    if kind == 8:
        return off + 2 + u16(buf, off)
    if kind in (7, 11, 12):
        count = sint(buf, off)
        require(0 <= count <= 2000000, "unreasonable NBT array length")
        return off + 4 + count * {7:1,11:4,12:8}[kind]
    if kind == 9:
        inner = buf[off]
        count = sint(buf, off + 1)
        require(0 <= count <= 2000000, "unreasonable NBT list length")
        p = off + 5
        for _ in range(count):
            p = skip(buf, p, inner)
        return p
    if kind == 10:
        return fields(buf, off)[1]
    raise ValueError(f"Unexpected NBT tag kind {kind}")

def fields(buf: bytes, off: int) -> tuple[dict[str, tuple[int, int, int]], int]:
    found = {}
    while True:
        kind = buf[off]
        off += 1
        if kind == 0:
            return found, off
        name, off = read_string(buf, off)
        require(name not in found, "duplicate NBT key: " + name)
        start = off
        off = skip(buf, off, kind)
        require(off <= len(buf), "NBT tag escapes buffer")
        found[name] = (kind, start, off)

def named_fields(buf: bytes) -> dict[str, tuple[int, int, int]]:
    require(buf[0] == 10, "expected NBT root compound")
    root_name, off = read_string(buf, 1)
    found, end = fields(buf, off)
    require(end == len(buf), "unexpected trailing structure bytes")
    return found

def entries(buf: bytes, meta: tuple[int, int, int]) -> tuple[int, list[tuple[int, int]]]:
    kind, start, end = meta
    require(kind == 9 and buf[start] == 10, "expected list of NBT compounds")
    count = sint(buf, start + 1)
    require(0 <= count <= 100000, "unreasonable structure compound count")
    pos = start + 5
    spans = []
    for _ in range(count):
        nxt = skip(buf, pos, 10)
        spans.append((pos, nxt))
        pos = nxt
    require(pos == end, "malformed structure list")
    return start, spans

def state_name(buf: bytes, span: tuple[int, int]) -> str:
    record, end = fields(buf, span[0])
    require(end == span[1] and "Name" in record, "bad palette entry")
    kind, start, _ = record["Name"]
    require(kind == 8, "palette Name must be a string")
    return read_string(buf, start)[0]

def block_location(buf: bytes, span: tuple[int, int]) -> tuple[tuple[int, int, int], int]:
    record, end = fields(buf, span[0])
    require(end == span[1] and "pos" in record and "state" in record, "bad block record")
    kind, start, _ = record["pos"]
    require(kind == 9 and buf[start] == 3 and sint(buf, start+1) == 3, "bad block pos")
    pos = tuple(sint(buf, start+5+4*i) for i in range(3))
    kind, start, _ = record["state"]
    require(kind == 3, "bad block palette index")
    return pos, sint(buf, start)

def build_decree(slot: int, decree_id: str) -> bytes:
    components = compound(**{"bountiful:decree_data": (10, compound(
        ids=(9, list_of(8, [string(decree_id)])),
        rank=(3, i32(1))
    ))})
    return compound(
        Slot=(1, bytes((slot,))),
        id=(8, string("bountiful:decree")),
        count=(3, i32(1)),
        components=(10, components)
    )

def build_board_block(palette_id: int) -> bytes:
    decree_slots = [build_decree(i, ident) for i, ident in enumerate(DECREES)]
    block_entity = compound(
        id=(8, string(BE_TYPE)),
        lastUpdated=(4, i64(0)),
        decree_inv=(10, compound(Items=(9, list_of(10, decree_slots)))),
        bounty_inv=(10, compound(Items=(9, list_of(10, []))))
    )
    return compound(
        pos=(9, list_of(3, [i32(i) for i in TARGET])),
        state=(3, i32(palette_id)),
        nbt=(10, block_entity),
    )

def patch(raw: bytes) -> bytes:
    require(hashlib.sha256(raw).hexdigest() == ORIGINAL_RAW_SHA256,
            "unexpected original Inn revision; reconcile builder before overlay")
    root = named_fields(raw)
    require("palette" in root and "blocks" in root and "size" in root, "structure tags missing")
    _, palettes = entries(raw, root["palette"])
    names = [state_name(raw, x) for x in palettes]
    require(BOARD not in names, "board already present; do not double install")
    _, blocks = entries(raw, root["blocks"])
    occupied = []
    for span in blocks:
        pos, state = block_location(raw, span)
        if pos == TARGET:
            require(0 <= state < len(names), "bad palette state index")
            require(names[state] == "minecraft:air", "board position occupied: " + names[state])
            occupied.append(span)
    require(len(occupied) <= 1, "duplicate target block positions")
    board_record = build_board_block(len(names))
    palette_record = compound(Name=(8, string(BOARD)))
    # Replace list payloads as whole spans without touching unrelated NBT tags
    pstart = root["palette"][1]
    bstart = root["blocks"][1]
    new_palette = bytes((10,)) + i32(len(palettes)+1) + raw[pstart+5:root["palette"][2]] + palette_record
    b_records = [raw[x:y] for x,y in blocks if (x,y) not in occupied]
    b_records.append(board_record)
    new_blocks = list_of(10, b_records)
    updates = [(pstart,root["palette"][2],new_palette),(bstart,root["blocks"][2],new_blocks)]
    out = raw
    for start, end, blob in sorted(updates, reverse=True):
        out = out[:start] + blob + out[end:]
    after = named_fields(out)
    _, newpal = entries(out, after["palette"])
    _, newblk = entries(out, after["blocks"])
    require(len(newpal) == len(palettes)+1, "failed to add one palette state")
    require(len(newblk) == len(blocks)+(0 if occupied else 1), "unexpected block change count")
    require(sum(block_location(out, x)[0] == TARGET for x in newblk) == 1, "missing/duplicate board")
    return out

def main() -> None:
    path = HERE / "prefab-extension" / "adventurers_inn.nbt"
    raw = gzip.decompress(path.read_bytes())
    updated = patch(raw)
    result = gzip.compress(updated, mtime=0)
    path.write_bytes(result)
    print("Inn Bountiful board: position", TARGET,
          "decrees", ",".join(DECREES),
          "raw_sha256", hashlib.sha256(updated).hexdigest(),
          "gzip_sha256", hashlib.sha256(result).hexdigest(),
          "bytes", len(result))

if __name__ == "__main__":
    main()
