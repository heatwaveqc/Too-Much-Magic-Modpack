"""Meaningful usage/closure tests; no network, Minecraft or upstream fixtures."""
import unittest
import tempfile
from pathlib import Path
import json
import hashlib
from catalog_registry import load_classifications
from classify_textures import write_shards
from classify_textures import classify,texture_key

def row(key,source='fixture'):
    return {'key':key,'sources':[{'source_id':source,'archive_sha256':'a'*64,
            'asset_sha256':'b'*64,'dimensions':[16,16]}]}
def model(name,parent,textures,**extra):
    return {'source_id':'fixture','path':'assets/fixture/models/'+name+'.json',
            'sha256':'c'*64,'parent':parent,'textures':textures,'faces':[],**extra}

class ClassificationTests(unittest.TestCase):
    def classify(self,rows,models=(),contracts=()):
        inv={'assets':rows};extra={'models':list(models),'fonts':[],'blockstates':[],'classes':[]}
        records,ev,missing=classify(inv,extra,contracts,{'crystalix','rechiseled'})
        return {r['key']:r for r in records},ev
    def test_inherited_custom_model_geometry(self):
        key='assets/fixture/textures/block/surface.png'
        parent=model('block/template',None,{},faces=[{'texture':'#skin','tintindex':0}],element_count=1)
        child=model('block/example','fixture:block/template',{'skin':'fixture:block/surface'})
        rows,_=self.classify([row(key)],[parent,child])
        self.assertEqual(rows[key]['primary_category'],'complex_blocks')
        self.assertIn('tinted',rows[key]['technical_flags'])
    def test_layer_alignment_and_no_filename_guess(self):
        a='assets/fixture/textures/item/base.png';b='assets/fixture/textures/item/mask.png'
        unknown='assets/fixture/textures/entity/not_necessarily_a_mob.png'
        m=model('item/example','minecraft:item/generated',{'layer0':'fixture:item/base','layer1':'fixture:item/mask'})
        rows,_=self.classify([row(a),row(b),row(unknown)],[m])
        self.assertEqual(rows[a]['primary_category'],'layered_items')
        self.assertIn('aligned_layers_and_alpha',rows[b]['required_deliverables'])
        self.assertIsNone(rows[unknown]['primary_category'])
    def test_exclusions_preserve_shared_paths(self):
        exclusive=row('assets/crystalix/textures/block/example.png','crystalix')
        shared=row('assets/minecraft/textures/block/stone.png','rechiseled')
        shared['sources'].append({'source_id':'minecraft'})
        rows,_=self.classify([exclusive,shared])
        self.assertEqual(rows[exclusive['key']]['production_scope'],'excluded-approved')
        self.assertEqual(rows[shared['key']]['production_scope'],'eligible')
    def test_metadata_flags_are_composable(self):
        key='assets/fixture/textures/block/connected.png'
        c={'source_id':'fixture','path':key+'.mcmeta','sha256':'d'*64,
           'contract':{'animation':{'frametime':3},'fusion':{'type':'connecting','layout':'full'}}}
        rows,_=self.classify([row(key)],contracts=[c])
        self.assertEqual(rows[key]['primary_category'],'connected_blocks')
        self.assertTrue({'connected','animated','UV-sensitive'}<=set(rows[key]['technical_flags']))
    def test_texture_namespace_defaults_and_inheritance_plans(self):
        self.assertEqual(texture_key('block/stone'),'assets/minecraft/textures/block/stone.png')
        key='assets/minecraft/textures/block/deepslate_iron_ore.png'
        rows,_=self.classify([row(key)])
        self.assertEqual(rows[key]['required_host_key'],'assets/minecraft/textures/block/deepslate.png')
        self.assertIn('host_ore_mixed_5x5',rows[key]['required_deliverables'])
    def test_classification_source_lease_and_blob_integrity(self):
        with tempfile.TemporaryDirectory() as directory:
            root=Path(directory);key='assets/fixture/textures/item/example.png'
            sources=[{'id':'fixture','sha256':'a'*64}]
            inv={'sources':sources,'assets':[{'key':key}]}
            index={'total':1,'source_catalog_signature':hashlib.sha256(json.dumps([('fixture','a'*64)]).encode()).hexdigest(),
                   'artifacts':write_shards(root,'assets',[{'key':key,'evidence':['native:fixture']}]),
                   'evidence':write_shards(root,'evidence',[{'id':'native:fixture'}])}
            path=root/'classification-index.json';path.write_text(json.dumps(index))
            self.assertIn(key,load_classifications(path,inv))
            inv['sources'][0]['sha256']='b'*64
            with self.assertRaisesRegex(ValueError,'source catalog changed'):load_classifications(path,inv)
            inv['sources'][0]['sha256']='a'*64
            blob=root/index['artifacts'][0]['path'];blob.write_bytes(blob.read_bytes()+b'changed')
            with self.assertRaisesRegex(ValueError,'hash mismatch'):load_classifications(path,inv)

if __name__=='__main__':unittest.main()
