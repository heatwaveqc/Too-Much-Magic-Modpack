"""Synthetic modern Anvil fixture; no game boot."""
import json
import struct
import sys
import tempfile
import unittest
import zlib
from pathlib import Path
sys.path.insert(0, str(Path(__file__).resolve().parents[1]/"scripts"))
from audit_worldgen_regions import census

def string(s):
    b=s.encode()
    return struct.pack(">H",len(b))+b
def tag(t,k,v):
    return bytes([t])+string(k)+v
def integer(k,v):
    return tag(3,k,struct.pack(">i",v))
def text(k,v):
    return tag(8,k,string(v))
def comp(k,*children):
    return tag(10,k,b"".join(children)+b"\0")
def chunk(x,z,status="minecraft:full"):
    bb=tag(11,"BB",struct.pack(">7i",6,x*16,64,z*16,x*16+10,80,z*16+10))
    children=tag(9,"Children",b"\x0a"+struct.pack(">i",1)+bb+b"\0")
    start=comp("test:ruin",text("id","test:ruin"),integer("ChunkX",x),integer("ChunkZ",z),children)
    structures=comp("structures",comp("starts",start,comp("test:invalid",text("id","INVALID"))),
                    comp("References",tag(12,"test:ruin",struct.pack(">iq",1,0))))
    return comp("",integer("xPos",x),integer("zPos",z),text("Status",status),structures)
def region(path,entries):
    header=bytearray(8192)
    data=[]
    for i,payload in entries:
        zipped=zlib.compress(payload)
        record=struct.pack(">I",len(zipped)+1)+b"\x02"+zipped
        assert len(record)<=4096
        sector=2+len(data)
        header[i*4:i*4+4]=sector.to_bytes(3,"big")+b"\x01"
        data.append(record.ljust(4096,b"\0"))
    path.write_bytes(header+b"".join(data))
class RegionTests(unittest.TestCase):
    def test_negative_coordinates_refs_invalid_and_partial(self):
        with tempfile.TemporaryDirectory() as d:
            root=Path(d)
            region(root/"r.-1.0.mca",[(31,chunk(-1,0))])
            region(root/"r.0.0.mca",[(0,chunk(0,0)),(1,chunk(1,0,"minecraft:noise"))])
            report=census(root,(-1,0,1,1))
            self.assertTrue(report["complete"])
            self.assertEqual(report["counts_by_id"],{"test:ruin":2})
            self.assertEqual(report["starts"][0]["bbox"],[-16,64,0,-6,80,10])
            self.assertEqual(report["errors"],[])
            partial=census(root,(-1,0,2,1))
            self.assertEqual(partial["coverage"],2/3)
            self.assertFalse(partial["complete"])
            self.assertIsNone(partial["starts_per_million_blocks2"])
    def test_mismatched_coordinate_is_not_coverage(self):
        with tempfile.TemporaryDirectory() as d:
            root=Path(d)
            region(root/"r.0.0.mca",[(0,chunk(1,0))])
            report=census(root,(0,0,1,1))
            self.assertEqual(report["coverage"],0)
            self.assertEqual(report["errors"][0]["error"],"chunk coordinate mismatch")
if __name__=="__main__":
    unittest.main()
