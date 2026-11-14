#!/usr/bin/env python3
"""One-time FTB Quests 2101.x locale conversion and packwiz SHA-256 refresh."""
from __future__ import annotations
import hashlib, re, pathlib, shutil

ROOT = pathlib.Path(__file__).resolve().parents[1]
LANG = ROOT / "config/ftbquests/quests/lang"
OLD = LANG / "en_us"
NEW = LANG / "en_us.snbt"

def checksum(data: bytes) -> str:
    return hashlib.sha256(data).hexdigest()

def main() -> None:
    if OLD.exists():
        names = ["chapter_group.snbt", "chapter.snbt"] + sorted(p.name for p in (OLD/"chapters").glob("*.snbt"))
        assert len(names) == 47, f"Expected 47 locale fragments, got {len(names)}"
        fragments = [OLD/x if x in ("chapter_group.snbt", "chapter.snbt") else OLD/"chapters"/x for x in names]
        lines = []
        for file in fragments:
            raw = file.read_text(encoding="utf-8").strip()
            assert raw.startswith("{") and raw.endswith("}"), str(file)
            lines += [line for line in raw[1:-1].splitlines() if line.strip()]
        text = "{\n" + "\n".join(lines) + "\n}\n"
        assert len(re.findall(r"(?m)^\s*chapter_group\.[0-9A-F]{16}\.title:", text)) == 5
        assert len(re.findall(r"(?m)^\s*chapter\.[0-9A-F]{16}\.title:", text)) == 45
        assert len(re.findall(r"(?m)^\s*quest\.[0-9A-F]{16}\.title:", text)) == 179
        assert len(re.findall(r"(?m)^\s*quest\.[0-9A-F]{16}\.quest_desc:", text)) == 179
        assert "undefined" not in text.lower()
        NEW.write_text(text, encoding="utf-8")
        shutil.rmtree(OLD)
    assert NEW.exists() and not OLD.exists(), "Expected sole lang/en_us.snbt"
    index_path = ROOT/"index.toml"
    data = index_path.read_text(encoding="utf-8")
    # Delete exactly the 47 obsolete per-file packwiz entries.
    pattern = re.compile(r'(?m)^\[\[files\]\]\nfile = "config/ftbquests/quests/lang/en_us/[^"]+"\nhash = "[0-9a-f]+"\n\n?')
    data, removed = pattern.subn("", data)
    assert removed in (0, 47), f"Expected 47 obsolete hashes or none, found {removed}"
    new_hash = checksum(NEW.read_bytes())
    block = f'[[files]]\nfile = "config/ftbquests/quests/lang/en_us.snbt"\nhash = "{new_hash}"\n'
    pattern_new = re.compile(r'(?m)^\[\[files\]\]\nfile = "config/ftbquests/quests/lang/en_us.snbt"\nhash = "[0-9a-f]+"\n')
    data, existing = pattern_new.subn(block, data)
    assert existing <= 1
    if existing == 0:
        data = data.rstrip() + "\n\n" + block
    index_path.write_text(data, encoding="utf-8")
    pack_path = ROOT/"pack.toml"
    pack = pack_path.read_text(encoding="utf-8")
    pack, n = re.subn(r'^version = "[^"]+"$', 'version = "0.8.10-quest-text-format-qa"', pack, count=1, flags=re.M)
    assert n == 1
    pack, n = re.subn(r'(\[index\]\nfile = "index.toml"\nhash-format = "sha256"\nhash = ")[0-9a-f]+(")',
        lambda m: m.group(1) + checksum(data.encode("utf-8")) + m.group(2), pack, count=1)
    assert n == 1
    pack_path.write_text(pack, encoding="utf-8")
    print("PASS: FTB 2101.x unified lang/en_us.snbt; 179 quests, 45 chapters, 5 categories")
    print("PASS: packwiz index and pack hash chain repaired")

if __name__ == "__main__":
    main()
