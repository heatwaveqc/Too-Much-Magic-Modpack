#!/usr/bin/env python3
"""Negative/positive Type-A fixtures for ProjectE early-game recipe safety (#44)."""
from __future__ import annotations

import json
import shutil
import tempfile
import unittest
from pathlib import Path

from validate_projecte_early_recipes import ROOT, validate, require_positive_dust_emc


class ProjectEEarlyRecipeTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        names = [
            "low_covalence_dust", "medium_covalence_dust", "high_covalence_dust",
            "repair_talisman", "repair_talisman_alt",
            "divining_rod_1", "divining_rod_2", "divining_rod_3",
            "evertide_amulet", "volcanite_amulet",
            "collector_mk1", "relay_mk1",
        ]
        for name in names:
            relative = f"kubejs/data/projecte/recipe/{name}.json"
            target = self.root / relative
            target.parent.mkdir(parents=True, exist_ok=True)
            shutil.copy2(ROOT / relative, target)
        mapping = "kubejs/data/tmm/pe_custom_conversions/covalence_dust_virtual.json"
        target = self.root / mapping
        target.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(ROOT / mapping, target)
        materials = self.root / "config/neovitae/materials.json"
        materials.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(ROOT / "config/neovitae/materials.json", materials)

    def rewrite(self, name, mutate):
        path = self.root / f"kubejs/data/projecte/recipe/{name}.json"
        data = json.loads(path.read_text(encoding="utf-8"))
        mutate(data)
        path.write_text(json.dumps(data, indent=2) + "\n", encoding="utf-8")

    def test_current_recipes_pass(self):
        validate(self.root)

    def test_dust_must_not_be_craftable(self):
        self.rewrite("low_covalence_dust", lambda d: d.pop("neoforge:conditions"))
        with self.assertRaises(AssertionError):
            validate(self.root)

    def test_no_old_talisman_alternative(self):
        self.rewrite("repair_talisman_alt", lambda d: d.pop("neoforge:conditions"))
        with self.assertRaises(AssertionError):
            validate(self.root)

    def test_repair_talisman_exact_two_inputs(self):
        self.rewrite("repair_talisman",
                     lambda d: d["ingredients"].append({"item": "projecte:low_covalence_dust"}))
        with self.assertRaises(AssertionError):
            validate(self.root)

    def test_rod_dust_and_predecessor_exact(self):
        self.rewrite("divining_rod_3",
                     lambda d: d["key"]["D"].update(item="neovitae:netherite_scrap_dust"))
        with self.assertRaises(AssertionError):
            validate(self.root)

    def test_water_sigil_required(self):
        self.rewrite("evertide_amulet", lambda d: d["key"]["S"].update(item="minecraft:water_bucket"))
        with self.assertRaises(AssertionError):
            validate(self.root)

    def test_stone_gate_must_stay(self):
        self.rewrite("collector_mk1", lambda d: d["key"]["P"].update(item="minecraft:glass"))
        with self.assertRaises(AssertionError):
            validate(self.root)

    def test_virtual_emc_mapper_presence_is_required(self):
        (self.root / "kubejs/data/tmm/pe_custom_conversions/covalence_dust_virtual.json").unlink()
        with self.assertRaises(FileNotFoundError):
            validate(self.root)

    def test_virtual_emc_mapper_requires_all_three_values(self):
        path = self.root / "kubejs/data/tmm/pe_custom_conversions/covalence_dust_virtual.json"
        data = json.loads(path.read_text(encoding="utf-8"))
        data["groups"]["legacy_covalence_dust_costs"]["conversions"].pop()
        path.write_text(json.dumps(data, indent=2) + "\\n", encoding="utf-8")
        with self.assertRaises(AssertionError):
            validate(self.root)

    def test_virtual_emc_mapper_rejects_changed_value_inputs(self):
        path = self.root / "kubejs/data/tmm/pe_custom_conversions/covalence_dust_virtual.json"
        data = json.loads(path.read_text(encoding="utf-8"))
        data["groups"]["legacy_covalence_dust_costs"]["conversions"][1]["ingredients"][0]["tag"] = "c:ingots/gold"
        path.write_text(json.dumps(data, indent=2) + "\\n", encoding="utf-8")
        with self.assertRaises(AssertionError):
            validate(self.root)

    def test_emc_cannot_be_faked_by_zeros_or_missing(self):
        base = {f"projecte:{name}": i + 1 for i, name in enumerate(
            ("low_covalence_dust", "medium_covalence_dust", "high_covalence_dust"))}
        require_positive_dust_emc(base)
        for name in ("low_covalence_dust", "medium_covalence_dust", "high_covalence_dust"):
            empty = dict(base)
            del empty[f"projecte:{name}"]
            with self.assertRaises(AssertionError):
                require_positive_dust_emc(empty)
            zero = dict(base)
            zero[f"projecte:{name}"] = 0
            with self.assertRaises(AssertionError):
                require_positive_dust_emc(zero)


if __name__ == "__main__":
    unittest.main()
