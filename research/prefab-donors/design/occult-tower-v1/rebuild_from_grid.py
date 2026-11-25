#!/usr/bin/env python3
"""Reconstruct Summoner's Lodge QA NBT from the versioned compact voxel grid and palette. Standard library only."""
import gzip, hashlib, json, pathlib, struct, sys
base=pathlib.Path(__file__).resolve().parent
output=pathlib.Path(sys.argv[1]) if len(sys.argv)>1 else base/'prefab-extension'/'summoners_lodge.nbt'
meta=json.loads((base/'tower-grid-palette.json').read_text()); w,h,l=meta['size']; palette=meta['palette']; grid=gzip.decompress((base/'tower-grid.bin.gz').read_bytes()); assert len(grid)==w*h*l
assert hashlib.sha256(grid).hexdigest()==meta['grid_sha256']
pack=lambda fmt,*v:struct.pack('>'+fmt,*v)
def st(s): b=s.encode();return pack('H',len(b))+b
def cp(d):return b''.join(pack('B',t)+st(k)+v for k,(t,v) in d.items())+b'\0'
def li(t,items):return pack('Bi',t,len(items))+b''.join(items)
def ints(a):return li(3,[pack('i',v) for v in a])
entries=[]
for i,v in enumerate(grid):
 if not v:continue
 sid=v-1; p=palette[sid]; x=i%w; y=i//(w*l); z=(i//w)%l; d={'pos':(9,ints([x,y,z])),'state':(3,pack('i',sid))}
 if p['Name']=='crystalix:clear_crystalix_glass': d['nbt']=(10,cp({'id':(8,st('crystalix:glass_tile')),'color':(3,pack('i',0xffffff))}))
 entries.append(cp(d))
pal=[cp({'Name':(8,st(p['Name'])),**({'Properties':(10,cp({k:(8,st(v)) for k,v in p['Properties'].items()}))} if p.get('Properties') else {})}) for p in palette]
raw=b'\x0a\x00\x00'+cp({'DataVersion':(3,pack('i',3955)),'size':(9,ints([w,h,l])),'palette':(9,li(10,pal)),'blocks':(9,li(10,entries)),'entities':(9,li(10,[]))})
assert hashlib.sha256(raw).hexdigest()==meta['uncompressed_nbt_sha256'],'Reconstruction differs from audited layout'
output.parent.mkdir(parents=True,exist_ok=True);output.write_bytes(gzip.compress(raw,mtime=0)); print(output,hashlib.sha256(output.read_bytes()).hexdigest())
