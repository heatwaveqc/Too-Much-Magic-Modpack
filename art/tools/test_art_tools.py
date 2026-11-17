"""Synthetic fixtures only; no Minecraft, downloaded art or mod binaries."""
import hashlib
import json
import struct
import tempfile
import unittest
import zipfile
import zlib
import io
from pathlib import Path

from inventory import inventory, dimensions, audit_archives
from catalog_registry import write_catalog, load_registry, verify_catalog
from acquire_sources import acquire
from validate import validate, png_structure

KEY = "assets/example/textures/item/gem.png"

def png(w, h, rgba):
    def chunk(k, payload):
        body = k + payload
        return struct.pack(">I", len(payload)) + body + struct.pack(">I", zlib.crc32(body) & 0xffffffff)
    ihdr = struct.pack(">IIBBBBB", w, h, 8, 6, 0, 0, 0)
    return b"\x89PNG\r\n\x1a\n" + chunk(b"IHDR", ihdr) + chunk(b"IDAT", zlib.compress((b"\x00"+bytes(rgba)*w)*h)) + chunk(b"IEND", b"")

class ArtToolsTests(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.root = Path(self.tmp.name)
        self.pack = self.root / "pack"
        self.pack.mkdir()
        (self.pack / "pack.mcmeta").write_text(json.dumps({"pack":{"pack_format":34,"description":"test"}}))
        self.src = self.root / "a.jar"
        with zipfile.ZipFile(self.src, "w") as z:
            z.writestr(KEY, png(16,16,(50,100,120,255)))
        self.inv = inventory([f"source={self.src}"])
    def tearDown(self):
        self.tmp.cleanup()
    def test_inventory(self):
        self.assertFalse(self.inv["complete"])
        row = self.inv["assets"][0]
        self.assertEqual(row["key"], KEY)
        self.assertEqual(row["sources"][0]["dimensions"], [16,16])
        self.assertEqual(row["status"], "pending")
    def test_source_collision(self):
        result = inventory([f"one={self.src}", f"two={self.src}"])
        self.assertEqual(len(result["assets"]), 1)
        self.assertEqual(len(result["assets"][0]["sources"]), 2)
        self.assertTrue(any("precedence" in x for x in result["unresolved"]))
    def test_valid_approved(self):
        path = self.pack / KEY
        path.parent.mkdir(parents=True)
        data = png(64,64,(83,150,200,255))
        path.write_bytes(data)
        self.inv["assets"][0].update(status="approved", target_dimensions=[64,64],
             output_sha256=hashlib.sha256(data).hexdigest(), provenance={"method":"synthetic"},
             review={"reviewer":"fixture"}, style_version="test")
        err, warn, counts = validate(self.inv, self.pack)
        self.assertFalse(err, err)
        self.assertEqual(counts["approved_rows"],1)
        self.assertTrue(any("unknown" in x for x in warn))
    def test_missing_approved_and_unregistered(self):
        self.inv["assets"][0]["status"]="approved"
        path=self.pack/"assets/example/textures/item/unregistered.png"
        path.parent.mkdir(parents=True)
        path.write_bytes(png(64,64,(1,2,3,255)))
        errors,_,_=validate(self.inv,self.pack)
        self.assertTrue(any("missing" in e for e in errors))
        self.assertTrue(any("unregistered" in e for e in errors))
    def test_crc(self):
        data=bytearray(png(16,16,(50,100,120,255)))
        data[-1]^=1
        with self.assertRaises(ValueError): png_structure(bytes(data))
    def test_empty(self):
        errors, warnings, counts = validate({"complete":False,"assets":[]},self.pack)
        self.assertEqual(errors,[])
        self.assertEqual(counts["production_png"],0)
        self.assertTrue(warnings)

    def test_nested_overlay_and_metadata(self):
        nested=io.BytesIO()
        with zipfile.ZipFile(nested,'w') as z:
            z.writestr(KEY,png(32,32,(2,3,4,255)))
            z.writestr(KEY+'.mcmeta',json.dumps({'animation':{'frametime':3}}))
        with zipfile.ZipFile(self.src,'a') as z:
            z.writestr('META-INF/jarjar/dependency.jar',nested.getvalue())
            z.writestr('overlay/'+KEY,png(16,16,(4,5,6,255)))
            z.writestr('assets/example/books/illustration.png',png(10,20,(4,5,6,255)))
        result=audit_archives([f'source={self.src}'],{'source'})
        self.assertEqual(len(result['sources']),2)
        self.assertTrue(all(s['provider_hash_verified'] for s in result['sources']))
        self.assertEqual(len(result['assets'][0]['sources']),2)
        self.assertEqual(len(result['overlays']),1)
        self.assertEqual(len(result['special_resources']),1)
        self.assertTrue(any('animation' in c.get('contract',{}) for c in result['contracts']))

    def test_catalog_round_trip_and_tamper(self):
        scan=audit_archives([f'source={self.src}'])
        scan.update(integration_sha='a'*40,first_party_projection={})
        registry=self.root/'registry'
        index=write_catalog(scan,registry)
        path=registry/'inventory.json'
        verify_catalog(path)
        loaded=load_registry(path)
        self.assertEqual(loaded['assets'][0]['sources'],scan['assets'][0]['sources'])
        self.assertEqual(loaded['assets'][0]['status'],'pending')
        shard=registry/index['catalog']['assets'][0]['path']
        shard.write_bytes(shard.read_bytes()+b'tampered')
        with self.assertRaisesRegex(ValueError,'hash mismatch'):verify_catalog(path)

    def test_unreviewed_download_origin_is_blocked(self):
        pin={'id':'fixture','filename':'fixture.jar','provider_hash_algorithm':'sha256',
             'provider_hash':'0'*64,'urls':['https://unreviewed.invalid/artifact.jar']}
        result=acquire(pin,self.root)
        self.assertEqual(result['status'],'blocked')
        self.assertIn('Unreviewed source host',result['errors'][0])

if __name__ == "__main__":
    unittest.main()
