#!/usr/bin/env python3
"""Standard-library checks for the authored Cave atlas, UVs and packaged module."""
import argparse
import json
from pathlib import Path
import struct
import zipfile
import zlib

ROOT=Path(__file__).resolve().parents[1]
ASSETS=ROOT/'compat/transformations/src/main/resources/assets/tmm_transformations'


def png(path):
    data=path.read_bytes();assert data[:8]==b'\x89PNG\r\n\x1a\n'
    offset=8;dims=None
    while offset<len(data):
        size=struct.unpack_from('>I',data,offset)[0];kind=data[offset+4:offset+8]
        content=data[offset+8:offset+8+size];crc=struct.unpack_from('>I',data,offset+8+size)[0]
        assert zlib.crc32(kind+content)&0xffffffff==crc,path
        if kind==b'IHDR':
            w,h,depth,color,*_=struct.unpack('>IIBBBBB',content)
            assert (depth,color)==(8,6);dims=(w,h)
        offset+=size+12
    assert offset==len(data)
    return dims


def check(jar=None):
    mesh=json.loads((ASSETS/'dracolich/cave_center.mesh.json').read_text())
    size=mesh['texture_size'];assert size==[1024,1024]
    occupied=bytearray(size[0]*size[1]);faces=0;cubes=0;rects=set()
    assert not any(n.startswith(('SmallWing','FrontFlipper','BackFlipper')) for n in mesh['attachments'])
    for bone,parts in mesh['attachments'].items():
        for c in parts:
            cubes+=1;assert all(v>=0 for v in c['size'])
            if any(v==0 for v in c['size']):assert bone.startswith('Magic')
            for face in c['uv'].values():
                x,y=face['uv'];w,h=face['uv_size'];faces+=1
                assert min(x,y,w,h)>=0 and w>0 and h>0
                assert x+w<=size[0] and y+h<=size[1]
                rect=(x,y,w,h);assert rect not in rects;rects.add(rect)
                for row in range(y,y+h):
                    start=row*size[0]+x
                    assert not any(occupied[start:start+w]),(bone,rect)
                    occupied[start:start+w]=b'\x01'*w
    assert (cubes,faces)==(407,2442)
    for name in ['cave_center.png','cave_center_glow.png']:
        assert png(ASSETS/'textures/entity/dracolich'/name)==tuple(size)
    marker=json.loads((ASSETS/'geo/dracolich_cave_center.geo.json').read_text())
    bones=marker['minecraft:geometry'][0]['bones']
    assert len(bones)==1 and bones[0]['name']=='tmm_unassembled_marker' and 'cubes' not in bones[0]
    for bone in ['MagicRing','MagicCubeRotation']+[f'MagicCirclePart{i}' for i in range(1,9)]:
        assert mesh['attachments'].get(bone)
    if jar:
        with zipfile.ZipFile(jar) as z:
            assert not any(n.startswith('assets/dragonsurvival/') for n in z.namelist())
            for path in ['assets/tmm_transformations/geo/dracolich_cave_center.geo.json',
                         'assets/tmm_transformations/dracolich/cave_center.mesh.json',
                         'assets/tmm_transformations/textures/entity/dracolich/cave_center_glow.png',
                         'io/github/heatwaveqc/tmm/transformations/client/DracolichArmorRenderer.class',
                         'io/github/heatwaveqc/tmm/transformations/network/DracolichVisualPayload.class']:
                assert path in z.namelist(),path
    print('PASS: 407 authored cubes, 2442 distinct nonoverlapping UV faces; matching RGBA diffuse/glow PNG CRCs; original casting meshes; safe marker'+('; packaged classes/assets and no upstream assets' if jar else ''))


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--jar',type=Path)
    a=p.parse_args();check(a.jar)
