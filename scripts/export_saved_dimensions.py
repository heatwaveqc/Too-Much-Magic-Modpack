"""Export saved dimension-stem keys and installed resource evidence without starting Minecraft.

Requires read_qa_world_state.py from the QA audit tools (#504). Never exports seeds/player data.
"""
import argparse,csv,hashlib,json,tomllib,zipfile
from datetime import datetime,timezone
from pathlib import Path
from read_qa_world_state import nbt

INITIAL_ANGEL_ALLOW = {
    'minecraft:overworld','aether:the_aether','the_bumblezone:the_bumblezone',
    'twilightforest:twilight_forest','theaurorian:the_aurorian','eternal_starlight:starlight'
}

def dimension_id(path):
    parts=path.split('/')
    if len(parts)<4 or parts[0]!='data' or parts[2]!='dimension' or not path.endswith('.json'):return None
    return parts[1]+':'+('/'.join(parts[3:]))[:-5]

def main():
    p=argparse.ArgumentParser();p.add_argument('--minecraft',type=Path,required=True)
    p.add_argument('--world',type=Path,required=True);p.add_argument('--output',type=Path,required=True)
    a=p.parse_args();manifest_path=a.minecraft.parent/'BUILD-MANIFEST.json'
    manifest=json.loads(manifest_path.read_text(encoding='utf-8'))
    level_path=a.world/'level.dat';data=nbt(level_path)['Data']
    stems=data['WorldGenSettings']['dimensions']
    if not isinstance(stems,dict) or not stems: raise ValueError('Saved dimension registry is empty or unsupported')
    resources=[];errors=[];providers={}
    for jar in sorted((a.minecraft/'mods').glob('*.jar')):
        with zipfile.ZipFile(jar) as z:
            entries=[n for n in z.namelist() if dimension_id(n)]
            if entries:providers[jar.name]=hashlib.sha256(jar.read_bytes()).hexdigest()
            for path in entries:
                row={'id':dimension_id(path),'provider':jar.name,'resource':path}
                try:
                    content=json.loads(z.read(path).decode('utf-8'))
                    row['dimension_type']=content.get('type');row['conditions']=content.get('neoforge:conditions',[])
                except (ValueError,UnicodeError) as exc: errors.append({'provider':jar.name,'resource':path,'error':str(exc)})
                resources.append(row)
    # Include possible local datapack resources as evidence, without claiming pack precedence/conditions.
    for folder,kind in [(a.minecraft/'kubejs/data','installed-kubejs'),(a.world/'datapacks','world-datapack')]:
        if not folder.exists(): continue
        for path in sorted(folder.rglob('*.json')):
            rel=path.relative_to(folder).as_posix()
            if kind=='installed-kubejs': rel='data/'+rel
            else:
                parts=rel.split('/')
                if 'data' not in parts: continue
                rel='/'.join(parts[parts.index('data'):])
            ident=dimension_id(rel)
            if ident:resources.append({'id':ident,'provider':kind,'resource':rel})
        for pack in sorted(folder.glob('*.zip')):
            with zipfile.ZipFile(pack) as z:
                for rel in z.namelist():
                    if dimension_id(rel):resources.append({'id':dimension_id(rel),'provider':'world-datapack:'+pack.name,'resource':rel})
    rows=[]
    for ident,stem in sorted(stems.items()):
        refs=[r for r in resources if r['id']==ident]
        rows.append({'id':ident,'dimension_type':stem.get('type'),'generator_type':stem.get('generator',{}).get('type'),
            'evidence':'SAVED_WORLDGEN_DIMENSION_STEM','providers':sorted({r['provider'] for r in refs}),
            'resources':[r['resource'] for r in refs], 'in_initial_owner_angel_allow_set':ident in INITIAL_ANGEL_ALLOW})
    resource_only=[r for r in resources if r['id'] not in stems]
    config=a.minecraft/'config/botania-common.toml'
    botania_modules=tomllib.loads(config.read_text(encoding='utf-8')).get('modules',{}) if config.exists() else {}
    result={'schema':1,'captured_utc':datetime.now(timezone.utc).isoformat(),
        'evidence_status':'SAVED_REGISTRY_PLUS_INSTALLED_RESOURCE_AUDIT_NOT_LIVE_SERVER_EXPORT',
        'build':{k:manifest.get(k) for k in ['integration_commit','baseline_sha','pinned_mod_count','expected_client_mod_count']},
        'manifest_sha256':hashlib.sha256(manifest_path.read_bytes()).hexdigest(),
        'save_level_dat_sha256':hashlib.sha256(level_path.read_bytes()).hexdigest(),
        'dimensions':rows,'resource_only_not_confirmed_loaded':resource_only,
        'provider_sha256':providers,'botania_modules':botania_modules,'resource_parse_errors':errors,
        'limitations':['Minecraft was closed at capture; server.levelKeys was not observed.',
            'Future/lazy/dynamic dimensions may differ; no invented JAWR Spirit World ID.',
            'A saved registry entry does not prove a visited/generated dimension or safe Angel landing.']}
    policy={'schema':1,'status':'REVIEW_INPUT_NOT_APPLIED',
        'policy_source':'docs/dimensions/ACCESS-SURVIVAL-REGISTRY.md: initial owner-selected six destinations',
        'approved_allow_ids_observed_in_save':sorted(INITIAL_ANGEL_ALLOW & set(stems)),
        'saved_registry_complement_for_native_banned_dimensions_review':sorted(set(stems)-INITIAL_ANGEL_ALLOW),
        'resource_only_unconfirmed':[r['id'] for r in resource_only],
        'note':'Complement is exhaustive only for this saved registry. Native blacklist is not fail-closed for future dimensions. No config changes.'}
    a.output.mkdir(parents=True,exist_ok=True)
    (a.output/'dimension-registry.json').write_text(json.dumps(result,indent=2)+'\n',encoding='utf-8')
    (a.output/'dimension-ids.txt').write_text('\n'.join(sorted(stems))+'\n',encoding='utf-8')
    (a.output/'angel-policy-review.json').write_text(json.dumps(policy,indent=2)+'\n',encoding='utf-8')
    with (a.output/'dimensions.csv').open('w',encoding='utf-8',newline='') as f:
        w=csv.writer(f);w.writerow(['dimension_id','dimension_type','generator_type','providers','initial_owner_allow_set'])
        for row in rows:w.writerow([row['id'],row['dimension_type'],row['generator_type'],';'.join(row['providers']),row['in_initial_owner_angel_allow_set']])
    print(json.dumps({'saved_dimensions':len(rows),'initial_allowed_observed':len(policy['approved_allow_ids_observed_in_save']),
        'saved_deny_complement':len(policy['saved_registry_complement_for_native_banned_dimensions_review']),
        'resource_only':policy['resource_only_unconfirmed'],'resource_parse_errors':len(errors)}))
if __name__=='__main__':main()
