#!/usr/bin/env python3
"""Verify static export equivalence and fail-closed local rig assembly."""
import copy
import json
from pathlib import Path
import struct
import sys
import numpy as np
from assemble_dracolich_model import ART, NATIVE, assemble, read_asset
from preview_dracolich_model import surfaces


def rejects(fn):
    try: fn()
    except ValueError: return
    raise AssertionError('Unsupported assembly was accepted')


def check(source, assembled):
    native=read_asset(source,NATIVE)
    mesh=json.loads((ART/'dracolich.mesh.json').read_text())
    contract=json.loads((ART/'rig-contract.json').read_text())
    built=json.loads(Path(assembled).read_text())
    # Validate that edited exports really depict the same new cubes as assembly.
    review=json.loads((ART/'dracolich-neutral-review.geo.json').read_text())
    a=surfaces(built); b=surfaces(review)
    assert len(a)==len(b)>300
    error=max(float(np.max(abs(x[1]-y[1]))) for x,y in zip(a,b))
    assert error<.0001, error
    # Changed upstream transforms must not silently break animation alignment.
    changed=copy.deepcopy(native)
    changed['minecraft:geometry'][0]['bones'][0]['pivot'][0] += 1
    rejects(lambda: assemble(changed,mesh,contract['rig_sha256']))
    bad=copy.deepcopy(mesh); bad['attachments']['missing_native_joint']=[]
    rejects(lambda: assemble(native,bad,contract['rig_sha256']))
    # GLB is a complete standalone triangle asset, not an external-resource stub.
    data=(ART/'dracolich-neutral-review.glb').read_bytes()
    magic,version,length=struct.unpack_from('<III',data)
    assert (magic,version,length)==(0x46546c67,2,len(data))
    jslen,jstype=struct.unpack_from('<I4s',data,12)
    gltf=json.loads(data[20:20+jslen])
    binlen,bintype=struct.unpack_from('<I4s',data,20+jslen)
    assert jstype==b'JSON' and bintype==b'BIN\0'
    assert binlen==gltf['buffers'][0]['byteLength']
    assert len(gltf['nodes'])>30 and 'animations' not in gltf and 'skins' not in gltf
    assert not any('uri' in buffer for buffer in gltf['buffers'])
    assert sum(a['count'] for a in gltf['accessors'][::2])==len(a)*36
    print(f'PASS: static geo matches authored assembled surfaces (max vertex error {error:.8f}); '
          'changed rig and unknown joint rejected; standalone GLB buffers and triangle counts valid')


if __name__=='__main__': check(sys.argv[1],sys.argv[2])
