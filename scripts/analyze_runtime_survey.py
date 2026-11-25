"""Summarize read-only runtime capture without inventing EMC anchor decisions."""
import argparse, collections, csv, gzip, hashlib, json
from pathlib import Path

def main():
    p=argparse.ArgumentParser()
    p.add_argument('--capture',type=Path,required=True)
    p.add_argument('--static',type=Path,required=True)
    p.add_argument('--output',type=Path,required=True)
    a=p.parse_args(); d=json.loads(a.capture.read_text(encoding='utf-8')); s=json.loads(a.static.read_text())
    a.output.mkdir(parents=True,exist_ok=True)
    recipes=d['recipes']; outputs=collections.defaultdict(list)
    for r in recipes:
        if r.get('output',{}).get('id') not in (None,'minecraft:air'):
            outputs[r['output']['id']].append(r['id'])
    loot=collections.defaultdict(set)
    def walk(x,table):
        if isinstance(x,dict):
            if x.get('type')=='minecraft:item' and isinstance(x.get('name'),str): loot[x['name']].add(table)
            for v in x.values(): walk(v,table)
        elif isinstance(x,list):
            for v in x: walk(v,table)
    for r in s['resources']:
        if r['path'].split('/')[2]=='loot_table': walk(r['data'],r['path'])
    with (a.output/'static-loot-item-references.csv').open('w',newline='',encoding='utf-8') as f:
        w=csv.writer(f);w.writerow(['item_id','loot_resource','status'])
        for item,tables in sorted(loot.items()):
            for table in sorted(tables): w.writerow([item,table,'STATIC_REFERENCE_CONDITIONS_AND_RUNTIME_HANDLERS_UNRESOLVED'])
    counts=collections.defaultdict(collections.Counter)
    rows=[]
    exceptions={'minecraft:glass_pane'}|{'minecraft:'+c+'_stained_glass_pane' for c in ('white','orange','magenta','light_blue','yellow','lime','pink','gray','light_gray','cyan','purple','blue','brown','green','red','black')}
    for x in sorted(d['items'],key=lambda x:x['id']):
        value=int(x['emc']) if 'emc' in x else None
        state='unknown' if value is None else 'positive' if value>0 else 'zero_api_value'
        group='approved_glass_pane_exception' if x['id'] in exceptions else 'priced' if value and value>0 else 'unresolved' if value is None else 'recipe_output_unpriced' if outputs[x['id']] else 'root_or_special_candidate'
        counts[x['id'].split(':')[0]][state]+=1
        rows.append({'id':x['id'],'name':x.get('name',''),'emc':x.get('emc',''),'numeric_precision': 'VERIFY_NATIVE_LONG' if value and value>2**53 else 'within_js_integer_range','has_stored_default_entry':x.get('hasStoredDefaultEntry','unknown'),'state':state,'triage':group,'tags':';'.join(sorted(x.get('tags',[]))),'recipe_outputs':';'.join(outputs[x['id']]),'static_loot_references':';'.join(sorted(loot[x['id']])),'error':x.get('error','')})
    with (a.output/'items.csv').open('w',newline='',encoding='utf-8') as f:
        w=csv.DictWriter(f,fieldnames=list(rows[0]));w.writeheader();w.writerows(rows)
    summary={'items':len(rows),'entities':len(d['entities']),'structures':len(d['structures']),'recipes':len(recipes),'mapped_entries':len(d['mappedEntries']), 'triage':dict(collections.Counter(x['triage'] for x in rows)), 'by_namespace':dict(counts),'capture_errors':d['errors'],'item_errors':sum('error' in x for x in d['items']),'recipe_errors':sum('error' in x for x in recipes),'capture_sha256':hashlib.sha256(a.capture.read_bytes()).hexdigest(),'static_parse_errors':s['parse_errors']}
    summary['default_values_above_js_exact_integer_range']=sum(x['numeric_precision']=='VERIFY_NATIVE_LONG' for x in rows)
    summary['truncated_ingredient_slots']=sum(i['truncated'] for r in recipes for i in r.get('ingredients',[]))
    (a.output/'summary.json').write_text(json.dumps(summary,indent=2)+'\n')
    with gzip.open(a.output/'runtime-capture.json.gz','wt',encoding='utf-8') as f:
        json.dump(d,f,separators=(',',':'),ensure_ascii=True)
    (a.output/'entities.json').write_text(json.dumps(d['entities'],indent=2,ensure_ascii=True)+'\n')
    (a.output/'structures.json').write_text(json.dumps({'structures': sorted(d['structures'])},indent=2)+'\n')
    (a.output/'projecte-mapped-entries.json').write_text(json.dumps(d['mappedEntries'],indent=2,ensure_ascii=True)+'\n')
    (a.output/'installed-artifacts.json').write_text(json.dumps(s['artifacts'],indent=2)+'\n')
    print(json.dumps({k:v for k,v in summary.items() if k not in ('by_namespace','static_parse_errors')}))

if __name__=='__main__': main()
