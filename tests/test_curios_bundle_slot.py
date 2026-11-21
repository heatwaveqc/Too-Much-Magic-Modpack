"""Static regression cases for Curios bundle icon (#309), no game boot."""
from __future__ import annotations

import copy
import json
import sys
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "scripts"))
from validate_curios_bundle_slot import (  # noqa: E402
    BAG_IDS, CURIO_ICON, main, validate,
)


class BundleSlotIconTest(unittest.TestCase):
    def setUp(self) -> None:
        self.slot = {
            "size": 1, "operation": "SET", "order": 50,
            "icon": CURIO_ICON, "validators": ["curios:tag"],
        }
        self.players = {"entities": ["minecraft:player"], "slots": ["bundle"]}
        self.tag = {"replace": False, "values": sorted(BAG_IDS)}
        self.pin = 'filename = "curios-neoforge-9.5.1+1.21.1.jar"'

    def check(self) -> None:
        validate(self.slot, self.players, self.tag, self.pin)

    def test_current_repo_slot(self):
        main(ROOT)

    def test_good_slot(self):
        self.check()

    def test_plural_old_icon_fails(self):
        self.slot["icon"] = "curios:slot/empty_curios_slot"
        with self.assertRaises(ValueError):
            self.check()

    def test_missing_icon_fails(self):
        del self.slot["icon"]
        with self.assertRaises(ValueError):
            self.check()

    def test_slot_size_order_and_validator_are_preserved(self):
        for key, bad in (("size", 2), ("order", 51), ("validators", [])):
            with self.subTest(key=key):
                old = copy.deepcopy(self.slot)
                self.slot[key] = bad
                with self.assertRaises(ValueError):
                    self.check()
                self.slot = old

    def test_item_tag_cannot_drop_a_bag(self):
        self.tag["values"] = ["ars_elemental:curio_bag"]
        with self.assertRaises(ValueError):
            self.check()

    def test_slot_id_must_stay_assigned_to_player(self):
        self.players["slots"] = ["some_other_slot"]
        with self.assertRaises(ValueError):
            self.check()

    def test_pin_change_requires_icon_reverification(self):
        self.pin = 'filename = "curios-neoforge-10.0.jar"'
        with self.assertRaises(ValueError):
            self.check()


if __name__ == "__main__":
    unittest.main()
