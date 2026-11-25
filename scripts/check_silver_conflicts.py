#!/usr/bin/env python3
"""Acceptance test: native Polymorph must not find overlapping silver gear crafts."""
import json
import re
import sys
from pathlib import Path
audit=json.loads(Path(sys.argv[1]).read_text(encoding="utf-8"))
silver=re.compile(r"^(eidolon_repraised|embers|werewolves):silver_(sword|axe|pickaxe|shovel|hoe|helmet|chestplate|leggings|boots)$")
matches=[]
for group in audit["groups"]:
    if group["recipe_type"] != "minecraft:crafting":
        continue
    ids=[id for id in group["recipe_ids"] if silver.fullmatch(id)]
    if len(ids)>1:
        matches.append((group["group_number"],ids))
assert not matches, f"Silver equipment craft conflicts survived KubeJS: {matches}"
print("PASS: Polymorph's actual loaded crafting recipes contain no competing silver gear output groups")
print("Remaining unrelated recipe conflict groups:",audit["groups_total"])
