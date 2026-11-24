"""Read a saved QA player's location and relevant advancement IDs without publishing identity."""
import argparse, collections, gzip, io, json, math, struct, zlib
from pathlib import Path

def nbt(path):
    return decode_nbt(path.read_bytes())

def decode_nbt(raw):
    f=io.BytesIO(gzip.decompress(raw) if raw[:2]==b'\x1f\x8b' else raw)
    def number(fmt): return struct.unpack('>'+fmt,f.read(struct.calcsize('>'+fmt)))[0]
    def string(): return f.read(number('H')).decode('utf-8')
    def value(t):
        if t in (1,2,3,4,5,6): return number({1:'b',2:'h',3:'i',4:'q',5:'f',6:'d'}[t])
        if t==8: return string()
        if t in (7,11,12): return [number({7:'b',11:'i',12:'q'}[t]) for _ in range(number('i'))]
        if t==9:
            child=number('b'); return [value(child) for _ in range(number('i'))]
        if t==10:
            out={}
            while True:
                child=number('b')
                if not child: return out
                name=string(); out[name]=value(child)
        raise ValueError('Unsupported NBT tag '+str(t))
    t=number('b');string();return value(t)

def nearby_saved_entities(world, position):
    found=[];errors=[];timestamps=[]
    for region in sorted((world/'entities').glob('*.mca')):
        raw=region.read_bytes()
        for slot in range(1024):
            offset=int.from_bytes(raw[slot*4:slot*4+3],'big')*4096
            if not offset: continue
            size=int.from_bytes(raw[offset:offset+4],'big');kind=raw[offset+4]
            data=raw[offset+5:offset+4+size]
            try:
                if kind==2: data=zlib.decompress(data)
                elif kind==1: data=gzip.decompress(data)
                elif kind!=3: raise ValueError('Unsupported/external compression '+str(kind))
                chunk=decode_nbt(data)
                timestamps.append(int.from_bytes(raw[4096+slot*4:4100+slot*4],'big'))
                for entity in chunk.get('Entities',[]):
                    pos=entity.get('Pos')
                    if not pos: continue
                    distance=math.dist(position,pos)
                    if distance<=128:
                        found.append({'id':entity.get('id'),'distance':round(distance,1),'heightOffset':round(pos[1]-position[1],1)})
            except (ValueError,EOFError,struct.error,zlib.error) as exc:
                errors.append({'region':region.name,'slot':slot,'error':str(exc)})
    return {'status':'SAVED_ENTITIES_NOT_LIVE','radius':128,'entities':found,'counts':dict(collections.Counter(e['id'] for e in found)),
            'read_errors':errors,'newest_region_chunk_timestamp':max(timestamps,default=None)}

def main():
    p=argparse.ArgumentParser();p.add_argument('world',type=Path);p.add_argument('--output',type=Path,required=True);a=p.parse_args()
    d=nbt(a.world/'level.dat')['Data']; player=d.get('Player',{})
    result={'status':'SAVED_SNAPSHOT_NOT_LIVE','allowCommands':d.get('allowCommands'),
        'gameType':d.get('GameType'),'dimension':player.get('Dimension'),'position':player.get('Pos'),
        'advancements':[]}
    if player.get('Pos') and player.get('Dimension')=='minecraft:overworld':
        result['nearby_saved_entities']=nearby_saved_entities(a.world,player['Pos'])
    for path in sorted((a.world/'advancements').glob('*.json')):
        data=json.loads(path.read_text(encoding='utf-8'))
        for key,row in data.items():
            if key.startswith(('betterend:','bclib:','minecraft:adventure/')):
                result['advancements'].append({'id':key,'done':row.get('done'),'criteria':row.get('criteria')})
    a.output.parent.mkdir(parents=True,exist_ok=True);a.output.write_text(json.dumps(result,indent=2)+'\n',encoding='utf-8')
    print(json.dumps(result))
if __name__=='__main__': main()
