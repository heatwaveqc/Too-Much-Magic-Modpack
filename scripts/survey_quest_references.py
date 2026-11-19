"""Compare installed quest references to a captured registry; tolerate count-before-id."""
import argparse, gzip, importlib.util, json, re
from pathlib import Path

p=argparse.ArgumentParser()
p.add_argument('--chapters',type=Path,required=True)
p.add_argument('--capture',type=Path,required=True)
p.add_argument('--output',type=Path,required=True)
a=p.parse_args()
spec=importlib.util.spec_from_file_location('gate',Path(__file__).with_name('quest_registry_gate.py'))
gate=importlib.util.module_from_spec(spec);spec.loader.exec_module(gate)
# Exact additional serialized form observed in installed FTB files; retain fail-closed behavior.
gate.ITEM=re.compile(r'\b(icon|item)\s*:\s*(?:\{\s*(?:count\s*:\s*\d+[bBsSlL]?\s*,?\s*)?id\s*:\s*)?"([^"]+)"')
locations={}; recovered={}
for path in sorted(a.chapters.glob('*.snbt')):
    source=path.read_text(encoding='utf-8')
    for field in gate.FIELD.finditer(source):
        tail=source[field.end():]; item=None
        direct=re.match(r'"([^"]+)"',tail)
        if direct: item=direct.group(1)
        elif tail.startswith('{'):
            depth=0
            for token in re.finditer(r'"(?:\\.|[^"\\])*"|[{}]',tail):
                value=token.group()
                if value=='{': depth+=1
                elif value=='}':
                    depth-=1
                    if depth==0: break
                elif depth==1 and re.search(r'\bid\s*:\s*$',tail[:token.start()]):
                    item=json.loads(value)
            compound=tail[:token.end()]
            for old in re.findall(r'"ftbquests:missing_item"\s*:\s*"([^"]+)"',compound):
                recovered.setdefault(old,[]).append(path.name+':'+str(source.count('\n',0,field.start())+1))
        if not item or not gate.RESOURCE_ID.fullmatch(item): raise ValueError('Unsupported reference: '+path.name+':'+str(source.count('\n',0,field.start())+1))
        locations.setdefault(item,[]).append(path.name+':'+str(source.count('\n',0,field.start())+1)+':'+field.group(1))
with gzip.open(a.capture,'rt',encoding='utf-8') as f: registry={x['id'] for x in json.load(f)['items']}
missing=sorted(set(locations)-registry)
report={'schema':1,'distinct_references':len(locations),'occurrences':sum(map(len,locations.values())),
        'reference_digest':gate.digest(sorted(locations)),'missing':missing,'references':locations,
        'ftb_missing_item_payloads':recovered,'missing_payload_ids_not_registered':sorted(set(recovered)-registry)}
a.output.write_text(json.dumps(report,indent=2)+'\n',encoding='utf-8')
print(json.dumps({k:v for k,v in report.items() if k!='references'}))
