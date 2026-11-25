import gzip
import json
import sys
import unittest
from pathlib import Path
sys.path.insert(0,str(Path(__file__).resolve().parents[1]/"scripts"))
from audit_worldgen_size import box_metrics, category, geometry, template_metrics, locations

# Minimal vanilla NBT encoder for independent fixtures.
def string(s):
    b=s.encode(); return len(b).to_bytes(2,"big")+b
def integer(n): return n.to_bytes(4,"big",signed=True)
def tag(t,k,p): return bytes([t])+string(k)+p
def compound(*tags): return b"".join(tags)+b"\0"
def seq(t,items): return bytes([t])+integer(len(items))+b"".join(items)
def ints(*items): return seq(3,[integer(i) for i in items])
def block(x,s): return compound(tag(9,"pos",ints(x,0,0)),tag(3,"state",integer(s)))
def state(name): return compound(tag(8,"Name",string(name)))
def fixture(duplicate=False):
    return gzip.compress(b"\x0a\0\0"+compound(
        tag(9,"size",ints(4,1,1)),
        tag(9,"palette",seq(10,[state("minecraft:stone"),state("minecraft:air"),
                                state("minecraft:structure_void"),state("minecraft:jigsaw")])),
        tag(9,"blocks",seq(10,[block(0,0),block(1,1),block(2,2),block(0 if duplicate else 3,3)]))))

class SizeTests(unittest.TestCase):
    def test_box(self):
        self.assertEqual(box_metrics([-2,3,7,2,5,8]),dict(width=5,height=3,depth=2,footprint=10,volume=30))
        with self.assertRaises(ValueError): box_metrics([2,0,0,1,0,0])
    def test_template(self):
        row=template_metrics(fixture())
        self.assertEqual(row["min_non_air"],2)
        self.assertEqual(row["variants"][0]["jigsaw_or_structure_markers"],1)
        self.assertEqual(row["variants"][0]["structure_void"],1)
        self.assertEqual(row["variants"][0]["explicit_air"],1)
        with self.assertRaises(ValueError): template_metrics(fixture(True))
    def test_category(self):
        self.assertEqual(category("astralsorcery:focal_point"),"protected")
        self.assertEqual(category("pastel:ruins"),"deprioritized_subsurface")
        self.assertEqual(category("botania:exploration_island"),"confirmed_surface")
        self.assertEqual(category("yungsextras:ruin"),"unclassified_review")
    def test_geometry(self):
        rows=[dict(id=rid,start_chunk=[0,0],bbox=[0,0,0,2,3,4],pieces=1,pieces_with_bbox=1,in_measured_area=True)
              for rid in ["astralsorcery:focal_point","botania:exploration_island","pastel:ruins"]]
        rows[-1]["pieces_with_bbox"]=0
        result=geometry(dict(complete=True,full_chunks=4096,outer_full_chunks=12544,
            bounds_chunks=[-32,-32,32,32],buffer_chunks=24,starts=rows,
            counts_by_id={r["id"]:1 for r in rows}))
        self.assertEqual(result["raw"]["starts"],3)
        self.assertEqual(result["raw"]["complete_boxes"],2)
        self.assertEqual(result["review"]["starts"],1)
        self.assertEqual(result["review"]["sum_footprint"],15)
    def test_pool(self):
        self.assertEqual(list(locations({"elements":[{"location":"x:y"},{"location":"z"}]})),["x:y","minecraft:z"])

if __name__=="__main__": unittest.main()
