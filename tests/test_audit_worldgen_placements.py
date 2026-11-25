import sys
import unittest
from pathlib import Path
sys.path.insert(0,str(Path(__file__).resolve().parents[1]/"scripts"))
from audit_worldgen_placements import report
class PlacementTests(unittest.TestCase):
    def test_reconcile(self):
        c=dict(complete=True,outer_full_chunks=1,outer_expected_chunks=1,starts=[
            dict(id="botania:island",start_chunk=[0,0],in_measured_area=True),
            dict(id="astralsorcery:focal_point",start_chunk=[0,0],in_measured_area=True)])
        line="botania:island\t0\t0\t0\t0\t10\t8\t6\t4\t3\t2\t0\n"
        a=report(c,[line])
        self.assertEqual(a["by_id"]["botania:island"]["non_air_positions"],6)
        self.assertTrue(a["complete_start_reconciliation"])
        self.assertFalse(report(c,[])["complete_start_reconciliation"])
        with self.assertRaises(ValueError): report(c,[line,line])
        with self.assertRaises(ValueError): report(c,[line.replace("\t6\t4\t","\t6\t9\t")])
if __name__=="__main__":unittest.main()
