"""Offline pilot-controller checks. Never launch a JVM."""
import subprocess
import sys
import tempfile
import unittest
from pathlib import Path
sys.path.insert(0,str(Path(__file__).resolve().parents[1]/"scripts"))
from run_worldgen_pilot import header_coverage, INNER, BUFFER, RADIUS, START, BASELINE

class PilotTests(unittest.TestCase):
    def test_geometry(self):
        self.assertEqual(INNER,(-32,-32,32,32))
        self.assertEqual(BUFFER,24)
        self.assertEqual(RADIUS,56)
        self.assertIn("SQUARE 0 0 56 minecraft:overworld NORMAL_GEN",START)
        self.assertEqual(len(BASELINE),40)
    def test_header_hint_and_missing_file(self):
        with tempfile.TemporaryDirectory() as d:
            root=Path(d)
            self.assertEqual(header_coverage(root,(-1,0,1,1)),0)
            b=bytearray(4096)
            b[31*4:32*4]=b"\x00\x00\x02\x01"
            (root/"r.-1.0.mca").write_bytes(b)
            self.assertEqual(header_coverage(root,(-1,0,1,1)),1)
    def test_without_authorization_fails_before_work(self):
        with tempfile.TemporaryDirectory() as d:
            work=Path(d)/"must-not-exist"
            script=Path(__file__).resolve().parents[1]/"scripts/run_worldgen_pilot.py"
            p=subprocess.run([sys.executable,str(script),"--pack-root",d,"--work",str(work),
                              "--carbon-sha256","0"*64],capture_output=True,text=True)
            self.assertEqual(p.returncode,2)
            self.assertIn("explicit audit execution approval required",p.stderr)
            self.assertFalse(work.exists())

if __name__=="__main__":
    unittest.main()
