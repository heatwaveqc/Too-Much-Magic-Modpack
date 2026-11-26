import copy
import importlib.util
import json
import sys
import unittest
from pathlib import Path

ROOT=Path(__file__).resolve().parents[1]
sys.path.insert(0,str(ROOT/"scripts"))
from check_surface_density import check, expected, path_for

class SurfaceDensityTests(unittest.TestCase):
    def test_current_overlay(self):
        check(ROOT)
    def test_probability_only(self):
        original={"placement":{"type":"minecraft:random_spread","frequency":0.6,"salt":123,"spacing":40,"separation":12,"frequency_reduction_method":"legacy_type_1","exclusion_zone":{"chunk_count":4,"other_set":"minecraft:villages"}},"structures":[{"structure":"example:ruin","weight":2}],"neoforge:conditions":[{"type":"example:enabled"}]}
        before=copy.deepcopy(original)
        modified=expected(original)
        self.assertEqual(modified["placement"]["frequency"],0.3)
        modified["placement"]["frequency"]=0.6
        self.assertEqual(modified,before)
        self.assertEqual(original,before)
    def test_default_probability(self):
        self.assertEqual(expected({"placement":{"type":"minecraft:random_spread"}})["placement"]["frequency"],0.5)
    def test_reject_custom_codec(self):
        with self.assertRaises(ValueError):
            expected({"placement":{"type":"custom:placement"}})
    def test_path(self):
        self.assertEqual(path_for("foo:bar"),"kubejs/data/foo/worldgen/structure_set/bar.json")
        with self.assertRaises(ValueError): path_for("foo:../bar")

if __name__=="__main__":
    unittest.main()
