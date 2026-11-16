#!/usr/bin/env python3
"""Render/export only the original mesh in the locally assembled bind pose.

This is an offline geometric inspection, not a GeckoLib/client render or a
Molang animation implementation. GLB contains original vertices/materials only,
with attachment names as editable mesh groups; no upstream rig is exported.
Requires numpy, matplotlib and Pillow.
"""
import argparse
import json
import math
from pathlib import Path
import struct
import copy
import numpy as np
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d.art3d import Poly3DCollection

COLORS = [(207,198,171), (153,143,119), (240,229,198), (119,111,94), (236,153,59)]
FACES = [[0,3,2,1], [4,5,6,7], [0,1,5,4], [3,7,6,2], [0,4,7,3], [1,2,6,5]]
CORNERS = np.array([[0,0,0],[1,0,0],[1,1,0],[0,1,0],
                    [0,0,1],[1,0,1],[1,1,1],[0,1,1]],float)


def translation(p):
    m = np.eye(4); m[:3,3] = p; return m


def rotation(r):
    x,y,z = np.radians(r)
    rx = np.array([[1,0,0],[0,np.cos(x),-np.sin(x)],[0,np.sin(x),np.cos(x)]])
    ry = np.array([[np.cos(y),0,np.sin(y)],[0,1,0],[-np.sin(y),0,np.cos(y)]])
    rz = np.array([[np.cos(z),-np.sin(z),0],[np.sin(z),np.cos(z),0],[0,0,1]])
    m = np.eye(4); m[:3,:3] = rz@ry@rx; return m


def around(p, r): return translation(p)@rotation(r)@translation(-np.array(p))


def surfaces(model, alternate=False):
    bones = model['minecraft:geometry'][0]['bones']
    maps = {b['name']: b for b in bones}
    cache = {}
    def world(name):
        if name in cache: return cache[name]
        b = maps[name]
        local = around(b.get('pivot',[0,0,0]),b.get('rotation',[0,0,0]))
        cache[name] = world(b['parent'])@local if 'parent' in b else local
        return cache[name]
    result = []
    for b in bones:
        if not alternate and b['name'].startswith(('SmallWing','FrontFlipper','BackFlipper')):
            continue
        for c in b.get('cubes',[]):
            vertices = CORNERS*np.array(c['size'])+np.array(c['origin'])
            matrix = world(b['name'])@around(c.get('pivot',[0,0,0]),c.get('rotation',[0,0,0]))
            vertices = (matrix@np.c_[vertices,np.ones(8)].T).T[:,:3]
            material = int(c['uv']['north']['uv'][0]/16)
            result.append((b['name'], vertices, material))
    return result


def export_glb(items, path):
    # Flattened static editable meshes: glTF Y-up, units scaled to Minecraft blocks.
    blob = bytearray(); views = []; accessors = []; meshes = []; nodes = []
    groups = {}
    for name, verts, mat in items: groups.setdefault((name,mat),[]).append(verts)
    def array(data, kind, bounds=False):
        data = np.array(data,dtype='<f4')
        offset = len(blob); blob.extend(data.tobytes())
        views.append({'buffer':0,'byteOffset':offset,'byteLength':data.nbytes,'target':34962})
        acc = {'bufferView':len(views)-1,'componentType':5126,'count':len(data),'type':kind}
        if bounds: acc.update(min=data.min(axis=0).tolist(),max=data.max(axis=0).tolist())
        accessors.append(acc); return len(accessors)-1
    for (name,mat), cubes in groups.items():
        positions = []; normals = []
        for verts in cubes:
            verts = verts/16
            for f in FACES:
                normal = np.cross(verts[f[1]]-verts[f[0]],verts[f[2]]-verts[f[0]])
                normal /= np.linalg.norm(normal)
                for idx in [f[0],f[1],f[2],f[0],f[2],f[3]]:
                    positions.append(verts[idx]); normals.append(normal)
        p = array(positions,'VEC3',True); n = array(normals,'VEC3')
        meshes.append({'name':name,'primitives':[{'attributes':{'POSITION':p,'NORMAL':n},'material':mat}]})
        nodes.append({'name':name,'mesh':len(meshes)-1})
    doc = {'asset':{'version':'2.0','generator':'TMM original Dracolich mesh inspector'},
           'scene':0,'scenes':[{'nodes':list(range(len(nodes)))}], 'nodes':nodes,'meshes':meshes,
           'materials':[{'name':n,'doubleSided':False,'pbrMetallicRoughness':{
               'baseColorFactor':[v/255 for v in c]+[1], 'metallicFactor':0,'roughnessFactor':.88}}
               for n,c in zip(['bone','joint','tooth','horn','soul'],COLORS)],
           'buffers':[{'byteLength':len(blob)}],'bufferViews':views,'accessors':accessors}
    js=json.dumps(doc,separators=(',',':')).encode(); js += b' '*((-len(js))%4)
    blob.extend(b'\0'*((-len(blob))%4))
    path.write_bytes(struct.pack('<III',0x46546c67,2,12+8+len(js)+8+len(blob))+
        struct.pack('<I4s',len(js),b'JSON')+js+struct.pack('<I4s',len(blob),b'BIN\0')+blob)


def export_review_geo(model, output):
    # Bake only our newly authored cube surfaces into independent attachment
    # groups. No native pivots, hierarchy, control bones or animations leave
    # the local assembly. This is an editable static review file, NOT the rig.
    from scipy.spatial.transform import Rotation
    bones = model['minecraft:geometry'][0]['bones']
    maps = {b['name']:b for b in bones}
    cache = {}
    def world(name):
        if name in cache: return cache[name]
        b=maps[name]
        local=around(b.get('pivot',[0,0,0]),b.get('rotation',[0,0,0]))
        cache[name]=world(b['parent'])@local if 'parent' in b else local
        return cache[name]
    result=[]
    for b in bones:
        if not b.get('cubes') or b['name'].startswith(('SmallWing','FrontFlipper','BackFlipper')): continue
        cubes=[]
        for c in b['cubes']:
            w=world(b['name']); pivot=np.array(c.get('pivot',[0,0,0]))
            wp=(w@np.r_[pivot,1])[:3]
            r=w[:3,:3]@rotation(c.get('rotation',[0,0,0]))[:3,:3]
            new=copy.deepcopy(c)
            new['origin']=(wp+np.array(c['origin'])-pivot).round(5).tolist()
            new['pivot']=wp.round(5).tolist()
            new['rotation']=Rotation.from_matrix(r).as_euler('xyz',degrees=True).round(5).tolist()
            cubes.append(new)
        result.append({'name':'mesh_'+b['name'],'pivot':[0,0,0],'cubes':cubes})
    geo={'format_version':'1.12.0','minecraft:geometry':[{'description':{
        'identifier':'geometry.tmm_dracolich_review_static','texture_width':128,'texture_height':32,
        'visible_bounds_width':24,'visible_bounds_height':24,'visible_bounds_offset':[0,2,0]},'bones':result}]}
    output.write_text(json.dumps(geo,indent=2)+'\n')


def render(items, output, count):
    plt.rcParams['font.family']='DejaVu Sans'
    fig=plt.figure(figsize=(16,10),facecolor='#151a21')
    views = [(221,24,-55,'SKELETAL DRACOLICH · BIND POSE',False),
             (222,12,0,'SIDE · OPEN RIB CAGE',True),
             (223,24,-60,'SKULL · HOLLOW SOCKETS / SEPARATE JAW',True),
             (224,74,-90,'WING FINGERS · NO MEMBRANE',False)]
    for spec,elev,azim,title,close in views:
        ax=fig.add_subplot(spec,projection='3d',facecolor='#151a21')
        pts=[]
        for name, verts, mat in items:
            if close and name.startswith('Wing'): continue
            if spec==223 and not (name in ['BlendHandlerHead','Snout','BlendHandlerJaw'] or name.startswith('Neck')): continue
            # display X/Z/Y to keep Y as the vertical on the page
            v=verts[:,[0,2,1]]
            polygons=[v[f] for f in FACES]
            color=np.array(COLORS[mat])/255
            shades=[.63,1,.78,.88,.7,.95]
            coll=Poly3DCollection(polygons,facecolors=[np.r_[color*s,1] for s in shades],
                                 edgecolors=(.12,.105,.085,.3),linewidths=.13)
            ax.add_collection3d(coll); pts.extend(v)
        points=np.array(pts); lo=points.min(0); hi=points.max(0); center=(lo+hi)/2
        span=hi-lo
        # Limits fit geometry with true equal unit scale, no foreshortening tricks.
        ax.set_xlim(lo[0]-2,hi[0]+2); ax.set_ylim(lo[1]-2,hi[1]+2); ax.set_zlim(lo[2]-2,hi[2]+2)
        ax.set_box_aspect(span+4); ax.view_init(elev=elev,azim=azim)
        ax.set_proj_type('ortho'); ax.set_axis_off()
        ax.set_title(title,color='#e3dac7',fontsize=10,pad=0,loc='left')
    fig.text(.045,.97,'DRACOLICH / ORIGINAL GEOMETRY PILOT',color='#eee4ce',fontsize=20,weight='bold')
    fig.text(.045,.935,'Neutral bone material · Native rig assembled locally · Offline geometry render',color='#98a8b6',fontsize=11)
    fig.text(.045,.025,f'{count} authored cubes / 54 attachments / 272 native rig bones preserved  •  Client animation and automatic form switching pending',color='#98a8b6',fontsize=10)
    fig.subplots_adjust(left=.04,right=.98,bottom=.065,top=.88,wspace=.06,hspace=.12)
    fig.savefig(output,dpi=150,facecolor=fig.get_facecolor()); plt.close(fig)


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument('local_geo',type=Path); p.add_argument('--output',type=Path,required=True)
    a=p.parse_args(); a.output.mkdir(parents=True,exist_ok=True)
    model=json.loads(a.local_geo.read_text())
    items=surfaces(model)
    count=sum(len(b.get('cubes',[])) for b in model['minecraft:geometry'][0]['bones'])
    render(items,a.output/'dracolich-preview.png',count)
    export_glb(items,a.output/'dracolich-neutral-review.glb')
    export_review_geo(model,a.output/'dracolich-neutral-review.geo.json')
    print(f'Exported {len(items)} original visible cubes; alternate body flippers remain in mesh source')
