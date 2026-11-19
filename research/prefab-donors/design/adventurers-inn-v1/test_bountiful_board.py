"""Read-only static proof for one in-office Bountiful board with three decrees."""
import importlib.util, unittest
from pathlib import Path
HERE=Path(__file__).resolve().parent
spec=importlib.util.spec_from_file_location("patch_inn",HERE/"add_bountiful_board.py")
mod=importlib.util.module_from_spec(spec)
spec.loader.exec_module(mod)
class InnBoard(unittest.TestCase):
    def test_decrees_exact(self):
        self.assertEqual(mod.TARGET,(8,7,4))
        self.assertEqual(mod.DECREES,("tmm_hunting","tmm_culinary","tmm_fishing"))
        data=mod.build_decree(0,"tmm_hunting")
        tags,at=mod.fields(data,0)
        self.assertEqual(at,len(data))
        self.assertEqual(mod.read_string(data,tags["id"][1])[0],"bountiful:decree")
        item_comp, _ = mod.fields(data,tags["components"][1])
        self.assertIn("bountiful:decree_data",item_comp)
        dc,_=mod.fields(data,item_comp["bountiful:decree_data"][1])
        self.assertEqual(mod.sint(data,dc["rank"][1]),1)
    def test_block_entity(self):
        data=mod.build_board_block(53)
        block,_=mod.fields(data,0)
        be,_=mod.fields(data,block["nbt"][1])
        self.assertEqual(mod.read_string(data,be["id"][1])[0],"bountiful:board-be")
        d,_=mod.fields(data,be["decree_inv"][1])
        _,records=mod.entries(data,d["Items"])
        self.assertEqual(len(records),3)
        b,_=mod.fields(data,be["bounty_inv"][1])
        _,records=mod.entries(data,b["Items"])
        self.assertEqual(len(records),0)
if __name__=="__main__":unittest.main()
