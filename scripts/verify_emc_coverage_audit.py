#!/usr/bin/env python3
"""Fixture-based contract for EMC coverage audit. Never substitutes for live export."""
from __future__ import annotations

import importlib.util
import json
import tempfile
from pathlib import Path

ROOT=Path(__file__).resolve().parents[1]
path=ROOT/"scripts/audit_emc_coverage.py"
spec=importlib.util.spec_from_file_location("emc_audit",path)
assert spec and spec.loader
mod=importlib.util.module_from_spec(spec)
spec.loader.exec_module(mod)

policy=json.loads((ROOT/"manifest/emc-coverage-policy.json").read_text(encoding="utf-8"))
exceptions=set(policy["explicitly_tiny_exceptions"])
assert "minecraft:glass_pane" in exceptions
registry={"minecraft:glass_pane","minecraft:stick","ars_nouveau:wilden_tribute","neovitae:mine_key"}
data={"item|minecraft:stick":{"emc":4},
      "ars_nouveau:wilden_tribute":{"emc":1048576},
      "neovitae:mine_key":{"emc":4096}}
with tempfile.TemporaryDirectory(prefix="emc-audit-fixture-") as temp:
    root=Path(temp)
    items=root/"items.json"; emc=root/"emc.json"
    items.write_text(json.dumps({"items":sorted(registry)}),encoding="utf-8")
    emc.write_text(json.dumps({"entries":data}),encoding="utf-8")
    result=mod.analyze(mod.items(items),mod.emc_values(emc),exceptions)
    assert result["registry_items"]==4
    assert result["applicable_items"]==3
    assert result["approved_exceptions_in_registry"]==1
    assert result["missing_count"]==0
    assert result["complete"]
    data.pop("neovitae:mine_key")
    emc.write_text(json.dumps({"entries":data}),encoding="utf-8")
    missing=mod.analyze(mod.items(items),mod.emc_values(emc),exceptions)
    assert missing["missing_ids"]==["neovitae:mine_key"]
    assert not missing["complete"]
    data["neovitae:mine_key"]={"emc":0}
    emc.write_text(json.dumps({"entries":data}),encoding="utf-8")
    zeros=mod.analyze(mod.items(items),mod.emc_values(emc),exceptions)
    assert zeros["missing_ids"]==["neovitae:mine_key"]
    assert not zeros["complete"]
    assert policy["coverage_target"].startswith("100%")
print("PASS: 100% EMC requires real positive EMC for each exported item except explicitly approved cheap glass panes")
