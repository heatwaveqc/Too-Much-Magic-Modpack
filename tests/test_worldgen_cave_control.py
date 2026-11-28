import sys
import unittest
from pathlib import Path
sys.path.insert(0,str(Path(__file__).resolve().parents[1]/"scripts"))
from run_worldgen_cave_control import decode,section_counts

class CaveControlTests(unittest.TestCase):
    def test_single(self):
        self.assertEqual(decode(["x"],None,64,1),["x"]*64)
    def test_non_spanning(self):
        palette=list(range(17))
        values=[i%17 for i in range(64)]
        data=[sum(x<<(5*j) for j,x in enumerate(values[i:i+12])) for i in range(0,64,12)]
        self.assertEqual(decode(palette,data,64,4),values)
    def test_invalid(self):
        for palette,data in [([],None),(["a","b"],[2]),(["a","b"],None)]:
            with self.assertRaises(ValueError): decode(palette,data,16,4)
    def test_deep_section(self):
        layers,bio=section_counts({"Y":-8,"block_states":{"palette":[{"Name":"minecraft:lava"}]},
                                  "biomes":{"palette":["infinity_cave:test"]}})
        self.assertEqual(layers[-128],{"lava":256})
        self.assertEqual(layers[-113],{"lava":256})
        self.assertEqual(bio,{"infinity_cave:test":64})
if __name__=="__main__": unittest.main()
