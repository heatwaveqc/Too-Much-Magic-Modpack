#!/usr/bin/env python3
"""Evidence-led v0.2 categorization. Missing renderer evidence stays unresolved."""
import argparse
from collections import Counter,defaultdict
import csv
from functools import lru_cache
import gzip
import hashlib
import json
from pathlib import Path
import re
from catalog_registry import load_registry

CATEGORIES=['simple_items','layered_items','simple_blocks','directional_blocks',
 'shaped_blocks','pseudoblocks','complex_blocks','connected_blocks','fluids','mobs',
 'armor_and_wearables','projectiles_and_rendered_objects','gui_elements',
 'micro_icons_and_fonts','particles_and_vfx','environmental_textures']
DELIVERABLES={c:['native_png','native_16_32_preview','category_owner_review'] for c in CATEGORIES}
for c in ['simple_blocks','directional_blocks','connected_blocks']:
    DELIVERABLES[c]+=['five_by_five_both_axes','native_model_preview']
DELIVERABLES['shaped_blocks']+=['shared_material_master','uv_model_preview']
DELIVERABLES['pseudoblocks']+=['cutout_plane_overlap_preview']
for c in ['complex_blocks','mobs','projectiles_and_rendered_objects']:
    DELIVERABLES[c]+=['uv_layout','rendered_model_preview']
DELIVERABLES['armor_and_wearables']+=['inventory_icon','worn_uv_textures','equipped_pose_preview']
DELIVERABLES['gui_elements']+=['native_layout_contract','gui_scale_1_to_4_preview']
DELIVERABLES['fluids']+=['still_and_flowing_pair','animation_loop_preview']
DELIVERABLES['particles_and_vfx']+=['runtime_size_and_timing_preview']
DELIVERABLES['environmental_textures']+=['renderer_mapping_and_lighting_preview']

def location(s):return s if ':' in s else 'minecraft:'+s
def texture_key(s):
    ns,path=location(s).split(':',1)
    return f'assets/{ns}/textures/{path.removeprefix("textures/").removesuffix(".png")}.png'
def model_id(path):
    ns=path.split('/')[1]
    return ns+':'+path.split('/models/',1)[1].removesuffix('.json')

SIMPLE={'minecraft:block/cube_all','minecraft:block/cube_all_inner_faces','minecraft:block/cube_mirrored_all','minecraft:block/leaves'}
DIRECTIONAL={'minecraft:block/cube','minecraft:block/cube_column','minecraft:block/cube_column_horizontal',
 'minecraft:block/cube_bottom_top','minecraft:block/orientable','minecraft:block/orientable_vertical',
 'minecraft:block/orientable_with_bottom','minecraft:block/cube_top','minecraft:block/cube_directional'}
PSEUDO={'minecraft:block/cross','minecraft:block/tinted_cross','minecraft:block/crop',
 'minecraft:block/flower_pot_cross','minecraft:block/flower_pot_cross_emissive','minecraft:block/template_torch',
 'minecraft:block/template_torch_wall','minecraft:block/torch','minecraft:block/torch_wall',
 'minecraft:block/template_vine','minecraft:block/template_dripstone'}
SHAPED={f'minecraft:block/{x}' for x in ['slab','slab_top','stairs','inner_stairs','outer_stairs',
 'fence_post','fence_side','fence_inventory','wall_inventory','template_wall_side',
 'template_wall_side_tall','template_wall_post','button','button_pressed','button_inventory',
 'pressure_plate_up','pressure_plate_down','fence_gate','fence_gate_open','fence_gate_wall','fence_gate_wall_open']}
ICONS={'minecraft:item/generated','minecraft:item/handheld','minecraft:item/handheld_rod','minecraft:builtin/generated'}

def categorize(chain,model):
    lineage=set(chain)
    if lineage&ICONS:return 'simple_items'
    if lineage&PSEUDO:return 'pseudoblocks'
    if lineage&SHAPED:return 'shaped_blocks'
    if lineage&SIMPLE:return 'simple_blocks'
    if lineage&DIRECTIONAL:return 'directional_blocks'
    if model.get('element_count') or model.get('faces'):return 'complex_blocks'
    return None

def classify(inv,extra,contracts,excluded):
    rows={r['key']:r for r in inv['assets']};models=extra['models'];by_id=defaultdict(list)
    evidence={};uses=defaultdict(lambda:defaultdict(set));flags=defaultdict(set)
    families=defaultdict(set);missing=defaultdict(set);related=defaultdict(set)
    def ev(kind,obj,detail=None,qualifier=''):
        key=kind+':'+obj['source_id']+':'+obj['path']+qualifier
        if key not in evidence:
            evidence[key]={'kind':kind,'source_id':obj['source_id'],'path':obj['path'],
                'sha256':obj['sha256']}
            if detail is not None:evidence[key]['detail']=detail
        return key
    def add(key,cat,eid):
        if key in rows:uses[key][cat].add(eid)
    for m in models:by_id[model_id(m['path'])].append(m)
    def choose(sid,mid):
        options=by_id.get(mid,[])
        local=[m for m in options if m['source_id']==sid]
        if len(local)==1:return local[0]
        vanilla=[m for m in options if m['source_id']=='minecraft']
        if mid.startswith('minecraft:') and len(vanilla)==1:return vanilla[0]
        if len(options)==1:return options[0]
        return None
    @lru_cache(maxsize=None)
    def resolve(sid,mid,ancestors=()):
        if mid in ancestors:return {},[],[],[],True
        m=choose(sid,mid)
        if m is None:return {},[mid],[],[],True
        tex={};chain=[mid];faces=[];refs=[m];uncertain=False
        if m.get('parent'):
            parent=location(m['parent'])
            if parent not in {'minecraft:builtin/generated','minecraft:builtin/entity'}:
                tex,pc,faces,pr,uncertain=resolve(m['source_id'],parent,ancestors+(mid,))
                tex=dict(tex);chain+=pc;refs+=pr
            else:chain.append(parent)
        tex.update(m.get('textures',{}))
        if m.get('faces'):faces=m['faces']
        return tex,chain,faces,refs,uncertain
    def deref(value,tex):
        seen=set()
        while isinstance(value,str) and value.startswith('#'):
            if value in seen:return None
            seen.add(value);value=tex.get(value[1:])
        return value if isinstance(value,str) and not value.startswith('#') else None
    for m in models:
        mid=model_id(m['path']);tex,chain,faces,refs,uncertain=resolve(m['source_id'],mid)
        cat=categorize(chain,{**m,'faces':faces})
        layers={k:v for k,v in tex.items() if re.fullmatch(r'layer\d+',k)}
        if cat=='simple_items' and len(layers)>1:cat='layered_items'
        eid=ev('model',m,{'model':mid,'parent_chain':chain,'parent_resolution_incomplete':uncertain})
        values=[]
        if cat in {'simple_items','layered_items'}:values=list(layers.values())
        elif faces:values=[f.get('texture') for f in faces]
        else:values=list(tex.values())
        for value in values:
            value=deref(value,tex)
            if not value:continue
            key=texture_key(value)
            if key not in rows:missing[key].add(eid);continue
            if cat:add(key,cat,eid)
            if any(f.get('tintindex') is not None and deref(f.get('texture'),tex)==value for f in faces):flags[key].add('tinted')
            if cat=='layered_items':flags[key].add('layered')
            if m.get('element_count') or m.get('loader'):flags[key].add('UV-sensitive')
            families[key].add(mid)
        # Record concrete model/renderer clues; unsupported loaders stay visible.
        if m.get('loader'):
            evidence[eid]['detail']['loader']=m['loader']
    # Native atlas sources express actual sprite use, across matching namespaces.
    atlas_categories={'gui':'gui_elements','mob_effects':'micro_icons_and_fonts',
        'map_decorations':'micro_icons_and_fonts','particles':'particles_and_vfx',
        'chests':'complex_blocks','beds':'complex_blocks','signs':'complex_blocks',
        'decorated_pot':'complex_blocks','banner_patterns':'complex_blocks',
        'shield_patterns':'armor_and_wearables','armor_trims':'armor_and_wearables',
        'shulker_boxes':'complex_blocks'}
    for c in contracts:
        if '/particles/' in c['path']:
            eid=ev('particle-definition',c)
            for value in c.get('textures',[]):add(texture_key(value),'particles_and_vfx',eid)
        if '/atlases/' in c['path']:
            name=Path(c['path']).stem;cat=atlas_categories.get(name)
            eid=ev('atlas',c,{'atlas':name})
            for source in c.get('contract',{}).get('sources',[]):
                typ=source.get('type','').split(':')[-1]
                if typ=='directory' and cat:
                    prefix=source['source'].rstrip('/')+'/'
                    for key in rows:
                        rel=key.split('/textures/',1)[1]
                        if rel.startswith(prefix):add(key,cat,eid)
                elif typ=='single' and cat:add(texture_key(source['resource']),cat,eid)
                elif typ=='paletted_permutations':
                    for value in source.get('textures',[]):
                        key=texture_key(value)
                        if cat:add(key,cat,eid)
                        if key in rows:flags[key].update(['tinted','layered','UV-sensitive'])
        if c['path'].endswith('.png.mcmeta'):
            key=c['path'].removesuffix('.mcmeta');obj=c.get('contract',{})
            if key not in rows:continue
            eid=ev('texture-metadata',c)
            if 'animation' in obj:flags[key].add('animated')
            if obj.get('fusion',{}).get('type')=='connecting':
                flags[key].update(['connected','UV-sensitive']);add(key,'connected_blocks',eid)
            if 'gui' in obj:add(key,'gui_elements',eid)
    for f in extra['fonts']:
        eid=ev('font-definition',f)
        for provider in f['contract'].get('providers',[]):
            if isinstance(provider.get('file'),str):add(texture_key(provider['file']),'micro_icons_and_fonts',eid)
    # Link stateful models to their resolved texture keys, without filename guesses.
    for b in extra['blockstates']:
        obj=b['contract'];found=[]
        def walk(node):
            if isinstance(node,dict):
                if isinstance(node.get('model'),str):found.append(location(node['model']))
                for v in node.values():walk(v)
            elif isinstance(node,list):
                for v in node:walk(v)
        walk(obj)
        if len(set(found))<2:continue
        for mid in found:
            tex,_,_,_,_=resolve(b['source_id'],mid)
            for v in tex.values():
                v=deref(v,tex)
                if v and texture_key(v) in rows:flags[texture_key(v)].add('stateful')
    # Class inheritance plus exact literal matching is stronger than a folder hint.
    classes=extra['classes'];class_by_name={c['name']:c for c in classes}
    roots={
      'net/minecraft/client/renderer/entity/MobRenderer':'mobs',
      'net/minecraft/client/renderer/entity/LivingEntityRenderer':'mobs',
      'net/minecraft/client/renderer/entity/layers/HumanoidArmorLayer':'armor_and_wearables',
      'net/minecraft/client/renderer/blockentity/BlockEntityRenderer':'complex_blocks',
      'net/minecraft/client/gui/screens/Screen':'gui_elements',
      'net/minecraft/client/gui/components/AbstractWidget':'gui_elements',
      'net/minecraft/client/particle/Particle':'particles_and_vfx',
      'net/minecraft/client/renderer/entity/EntityRenderer':'projectiles_and_rendered_objects',
      'net/minecraft/client/renderer/LevelRenderer':'environmental_textures',
      'net/minecraft/client/renderer/DimensionSpecialEffects':'environmental_textures',
      'net/minecraft/client/renderer/block/LiquidBlockRenderer':'fluids',
      'net/neoforged/neoforge/client/extensions/common/IClientFluidTypeExtensions':'fluids'}
    roots.update({'software/bernie/geckolib/renderer/GeoEntityRenderer':'mobs',
        'software/bernie/geckolib/renderer/GeoArmorRenderer':'armor_and_wearables',
        'software/bernie/geckolib/renderer/GeoBlockRenderer':'complex_blocks',
        'software/bernie/geckolib/renderer/GeoItemRenderer':'projectiles_and_rendered_objects',
        'software/bernie/geckolib/renderer/GeoObjectRenderer':'projectiles_and_rendered_objects'})
    @lru_cache(maxsize=None)
    def lineage(name):
        found=[];todo=[name];seen=set()
        while todo:
            name=todo.pop()
            if not name or name in seen:continue
            seen.add(name);found.append(name)
            c=class_by_name.get(name)
            if c:todo += [c.get('parent'),*c.get('interfaces',[])]
        return tuple(found)
    by_source_rel=defaultdict(set)
    for key,row in rows.items():
        rel=key.split('/textures/',1)[1]
        for s in row['sources']:by_source_rel[(s['source_id'],rel)].add(key)
    # Renderer constructors identify which GeoModel/EntityModel owns a skin.
    model_owners=defaultdict(set)
    model_bases={'software/bernie/geckolib/model/GeoModel','net/minecraft/client/model/EntityModel'}
    for c in classes:
        role=next((roots[n] for n in lineage(c['name']) if n in roots),None)
        if not role:continue
        for name in c.get('referenced_classes',[]):
            if model_bases.intersection(lineage(name)):model_owners[name].add(role)
    for c in classes:
        ls=lineage(c['name']);cat=next((roots[n] for n in ls if n in roots),None)
        owners=model_owners[c['name']]
        if cat is None and len(owners)==1:cat=next(iter(owners))
        if not cat:continue
        for literal in c['texture_literals']:
            matches=set()
            if literal.startswith('textures/'):
                matches|=by_source_rel[(c['source_id'],literal[9:])]
            elif ':textures/' in literal:
                key=texture_key(literal)
                if key in rows:matches.add(key)
            else:
                matches|=by_source_rel[(c['source_id'],literal if literal.endswith('.png') else literal+'.png')]
            pattern=False
            if not matches and any(token in literal for token in ['\x01','%s','%d']):
                pattern=True
                rel=literal.split(':',1)[-1].removeprefix('textures/')
                expression=re.escape(rel)
                for token in ['\x01','%s','%d']:expression=expression.replace(re.escape(token),'[^/]+')
                if not rel.endswith('.png'):expression+=r'\.png'
                for (sid,path),keys in by_source_rel.items():
                    if sid==c['source_id'] and re.fullmatch(expression,path):matches.update(keys)
            if not matches:continue
            # Literal matches must be unique; dynamic constructors can map a family.
            if not pattern and len(matches)!=1:continue
            for key in matches:
                eid=ev('renderer-pattern' if pattern else 'renderer-class',c,
                    {'class':c['name'],'class_path':c['path'],'inheritance':list(ls),
                     'model_owner_categories':sorted(owners),'literal':literal,'matched_key':key},qualifier='#'+key)
                add(key,cat,eid)
                if pattern:flags[key].add('stateful')
                if cat in {'mobs','armor_and_wearables','complex_blocks','projectiles_and_rendered_objects'}:flags[key].add('UV-sensitive')
    # Explicit vanilla humanoid contracts and pilot workstation model pairings.
    for material in ['iron','gold','diamond','netherite','chainmail','leather']:
        for layer in [1,2]:
            key=f'assets/minecraft/textures/models/armor/{material}_layer_{layer}.png'
            if key in rows:
                uses[key]['armor_and_wearables'].add('manual:vanilla-humanoid-armor')
                flags[key].add('UV-sensitive')
                item_material='golden' if material=='gold' else material
                related[key].update(f'assets/minecraft/textures/item/{item_material}_{slot}.png' for slot in (['helmet','chestplate','boots'] if layer==1 else ['leggings']))
        for slot in ['helmet','chestplate','leggings','boots']:
            item_material='golden' if material=='gold' else material
            key=f'assets/minecraft/textures/item/{item_material}_{slot}.png'
            if key in rows and uses[key]:
                uses[key]['armor_and_wearables'].add('manual:vanilla-humanoid-armor')
    results=[]
    priority=['armor_and_wearables','connected_blocks','simple_blocks','directional_blocks','pseudoblocks',
              'shaped_blocks','complex_blocks','layered_items','simple_items','mobs',
              'gui_elements','micro_icons_and_fonts','particles_and_vfx','projectiles_and_rendered_objects','fluids','environmental_textures']
    for key,row in sorted(rows.items()):
        cats=uses[key];primary=next((c for c in priority if c in cats),None)
        # Mixed atlas/renderer roles are reported, never erased by primary choice.
        production_scope='excluded-approved' if all(s['source_id'] in excluded for s in row['sources']) else 'eligible'
        rel=key.split('/textures/',1)[1].removesuffix('.png');ns=key.split('/')[1]
        stem=Path(rel).name
        family=ns+':'+rel
        family_basis='exact-resource provisional singleton'
        if primary in {'simple_blocks','directional_blocks','shaped_blocks'}:
            base=re.sub(r'_(top|bottom|side|front|back|end|inner|outer|on|off|lit)$','',stem)
            family=ns+':material/'+base;family_basis='model-confirmed surface; face suffix grouping requires family review'
        if key in ['assets/minecraft/textures/item/iron_ingot.png','assets/minecraft/textures/item/gold_ingot.png','assets/minecraft/textures/item/copper_ingot.png']:
            family='tmm_hd:master/ordinary_ingot';family_basis='owner v0.2 shared ordinary-ingot direction'
        master=None
        if key=='assets/botania/textures/item/mana_diamond.png':master='assets/minecraft/textures/item/diamond.png'
        if key=='assets/botania/textures/item/mana_pearl.png':master='assets/minecraft/textures/item/ender_pearl.png'
        chosen_evidence=cats.get(primary,set())
        pattern_only=bool(chosen_evidence) and all(x.startswith('renderer-pattern:') for x in chosen_evidence)
        required=list(DELIVERABLES[primary]) if primary else ['resolve_native_renderer','category_owner_review']
        if 'animated' in flags[key]:required.append('animation_layout_and_loop')
        if 'layered' in flags[key]:required.append('aligned_layers_and_alpha')
        if primary and (primary.endswith('_blocks') or primary=='pseudoblocks'):
            if any(':item/' in mid for mid in families[key]):required.append('block_inventory_model_preview')
        result={'key':key,'primary_category':primary,
          'classification_status':('pattern-supported-needs-review' if pattern_only else 'evidence-supported') if primary else 'needs-renderer-review',
          'family_id':family,'family_basis':family_basis,
          'required_deliverables':required,
          'technical_flags':sorted(flags[key]),'use_categories':sorted(cats),
          'evidence':sorted(set(e for group in cats.values() for e in group)),
          'source_model_count':len(families[key]),'production_scope':production_scope,
          'related_asset_keys':sorted(k for k in related[key] if k in rows),
          'category_approval':'pending','game_test':'not run'}
        if primary is None:
            result['review_reason']='model parent/custom loader unresolved' if families[key] else 'no supported native usage edge located'
            result['candidate_models']=sorted(families[key])[:8]
        if master:result['required_master_key']=master;result['master_status']='approval-pending'
        rune_elements={'water','fire','earth','air','spring','summer','autumn','winter','mana',
                       'lust','gluttony','greed','sloth','wrath','envy','pride'}
        if ns=='botania' and rel.startswith('item/rune_') and stem[5:] in rune_elements:
            result['family_id']='botania:master/rune_substrate'
            result['family_basis']='native Botania rune item family / owner v0.2 shared substrate'
            result['required_template_id']='tmm_hd:master/botania_rune_substrate'
            result['master_status']='approval-pending'
        ore_match=re.fullmatch(r'(deepslate_)?(coal|iron|copper|gold|redstone|emerald|lapis|diamond)_ore',stem) if ns=='minecraft' else None
        if ore_match or (ns=='minecraft' and stem in ['nether_gold_ore','nether_quartz_ore']):
            host='deepslate' if ore_match and ore_match[1] else 'stone' if ore_match else 'netherrack'
            result['required_host_key']=f'assets/minecraft/textures/block/{host}.png'
            result['host_master_status']='approval-pending'
            result['ore_palette_family']='minecraft:'+((ore_match[2]) if ore_match else 'gold' if stem=='nether_gold_ore' else 'quartz')
            result['required_deliverables']+=['host_edge_inheritance','host_ore_mixed_5x5','all_ore_5x5','ore_raw_product_palette_comparison']
        if production_scope=='excluded-approved':
            result['exception']={'authority':'art/CATEGORY-STANDARDS.md v0.2 / owner PR #297',
                'reason':'Crystalix/Rechiseled original-art exclusion; preserve native runtime textures',
                'exact_source_ids':sorted({s['source_id'] for s in row['sources']})}
        results.append(result)
    evidence['manual:vanilla-humanoid-armor']={'kind':'native-contract','description':'Vanilla humanoid armor layer 1/2 and corresponding equipment icons; UV/equipped preview still required'}
    return results,evidence,missing

def write_shards(root,name,rows):
    index=[]
    def emit(part):
        data=gzip.compress((json.dumps(part,separators=(',',':'))+'\n').encode(),mtime=0)
        if len(data)>180000 and len(part)>1:
            n=len(part)//2;emit(part[:n]);emit(part[n:]);return
        path=f'classification/{name}-{len(index):03d}.json.gz'
        target=root/path;target.parent.mkdir(exist_ok=True);target.write_bytes(data)
        index.append({'path':path,'rows':len(part),'sha256':hashlib.sha256(data).hexdigest()})
    for i in range(0,len(rows),4096):emit(rows[i:i+4096])
    return index

def main():
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--inventory',type=Path,required=True)
    p.add_argument('--renderer-evidence',type=Path,required=True);p.add_argument('--out',type=Path,required=True)
    p.add_argument('--csv',type=Path,required=True)
    a=p.parse_args();inv=load_registry(a.inventory);contracts=[]
    raw=json.loads(a.inventory.read_text())
    for s in raw['catalog']['contracts']:contracts+=json.loads(gzip.decompress((a.inventory.parent/s['path']).read_bytes()))
    extra=json.loads(a.renderer_evidence.read_text())
    exclusions=json.loads((a.inventory.parent/'scope-exclusions.json').read_text())
    rows,evidence,missing=classify(inv,extra,contracts,{x['manifest_id'] for x in exclusions['exclusions']})
    a.out.mkdir(parents=True,exist_ok=True)
    summary={'schema_version':'0.2','inventory_integration_sha':inv['integration_sha'],
      'source_catalog_signature':hashlib.sha256(json.dumps(sorted((s['id'],s['sha256']) for s in inv['sources'])).encode()).hexdigest(),
      'classifier_sha256':hashlib.sha256(Path(__file__).read_bytes()).hexdigest(),
      'total':len(rows),'categorized':sum(r['primary_category'] is not None for r in rows),
      'needs_renderer_review':sum(r['primary_category'] is None for r in rows),
      'classification_states':dict(Counter(r['classification_status'] for r in rows)),
      'categories':dict(Counter(r['primary_category'] or 'unresolved' for r in rows)),
      'eligible_categories':dict(Counter(r['primary_category'] or 'unresolved' for r in rows if r['production_scope']=='eligible')),
      'scope':dict(Counter(r['production_scope'] for r in rows)),
      'technical_flags':dict(Counter(f for r in rows for f in r['technical_flags'])),
      'missing_model_texture_references':len(missing),
      'artifacts':write_shards(a.out,'assets',rows),
      'evidence':write_shards(a.out,'evidence',[{'id':k,**v} for k,v in sorted(evidence.items())]),
      'missing_references':write_shards(a.out,'missing-references',[
          {'resource_key':key,'model_reference_evidence':sorted(refs),
           'status':'not in frozen base PNG catalog; generated/overlay/missing applicability review'}
          for key,refs in sorted(missing.items())]),
      'category_approval':'none; evidence-supported is not owner-approved or game-tested'}
    referenced={x['path'] for group in ['artifacts','evidence','missing_references'] for x in summary[group]}
    for stale in (a.out/'classification').glob('*.json.gz'):
        if stale.relative_to(a.out).as_posix() not in referenced:stale.unlink()
    (a.out/'classification-index.json').write_text(json.dumps(summary,indent=2)+'\n')
    with a.csv.open('w') as f:
        w=csv.writer(f,lineterminator='\n');w.writerow(['key','primary_category','classification_status','family_id','technical_flags','production_scope','required_deliverables','evidence_count','required_master_key','category_approval'])
        for r in rows:w.writerow([r['key'],r['primary_category'] or '',r['classification_status'],r['family_id'],';'.join(r['technical_flags']),r['production_scope'],';'.join(r['required_deliverables']),len(r['evidence']),r.get('required_master_key',r.get('required_host_key',r.get('required_template_id',''))),r['category_approval']])
    print(json.dumps({k:v for k,v in summary.items() if k not in ['artifacts','evidence','missing_references']}))

if __name__=='__main__':main()
