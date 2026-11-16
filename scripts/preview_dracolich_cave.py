#!/usr/bin/env python3
"""Render the actual Cave atlas on original mesh vertices; export textured GLB.

Offline inspection only. No native rig or animations enter the GLB/PNG.
Requires the private locally assembled GEO and the repository's original atlases.
"""
import argparse
import io
import json
from pathlib import Path
import struct
import numpy as np
from PIL import Image, ImageDraw, ImageFont
from preview_dracolich_model import around, CORNERS, FACES

ROOT=Path(__file__).resolve().parents[1]
ART=ROOT/'compat/transformations/art/dracolich'
TEX=ROOT/'compat/transformations/src/main/resources/assets/tmm_transformations/textures/entity/dracolich'
SIDES=['north','south','down','up','west','east']


def surfaces(model):
    bones={b['name']:b for b in model['minecraft:geometry'][0]['bones']};cache={};faces=[]
    def world(name):
        if name in cache:return cache[name]
        b=bones[name];m=around(b.get('pivot',[0,0,0]),b.get('rotation',[0,0,0]))
        cache[name]=world(b['parent'])@m if 'parent' in b else m
        return cache[name]
    for name,bone in bones.items():
        if name.startswith('Magic'):continue # Native animations hide casting geometry when idle.
        for c in bone.get('cubes',[]):
            v=CORNERS*np.array(c['size'])+np.array(c['origin'])
            m=world(name)@around(c.get('pivot',[0,0,0]),c.get('rotation',[0,0,0]))
            v=(m@np.c_[v,np.ones(8)].T).T[:,:3]
            for idx,side in zip(FACES,SIDES):
                p=v[idx];u=c['uv'][side]['uv'];w,h=c['uv'][side]['uv_size']
                uv=np.array([[u[0],u[1]+h],[u[0]+w,u[1]+h],[u[0]+w,u[1]],[u[0],u[1]]],float)
                faces.append((name,p,uv))
    return faces


def render(faces, diffuse, glow, size, yaw, elevation):
    width,height=size
    yaw,elevation=np.radians([yaw,elevation])
    view=np.array([np.cos(elevation)*np.cos(yaw),np.sin(elevation),np.cos(elevation)*np.sin(yaw)])
    right=np.cross([0,1,0],view);right/=np.linalg.norm(right)
    up=np.cross(view,right);basis=np.stack([right,up,view])
    points=np.concatenate([v for _,v,_ in faces]);proj=points@basis.T
    lo,hi=proj[:,:2].min(0),proj[:,:2].max(0);center=(lo+hi)/2
    scale=min((width-36)/max(hi[0]-lo[0],1),(height-36)/max(hi[1]-lo[1],1))
    color=np.full((height,width,3),[31,36,44],dtype=np.uint8)
    zbuf=np.full((height,width),-np.inf)
    d=np.array(diffuse);g=np.array(glow)
    light=np.array([-.5,.8,-.3]);light/=np.linalg.norm(light)
    for _,verts,uv in faces:
        normal=np.cross(verts[1]-verts[0],verts[2]-verts[0]);norm=np.linalg.norm(normal)
        if norm<1e-8:continue
        normal/=norm
        shade=.45+.55*abs(float(np.dot(normal,light)))
        projected=verts@basis.T
        projected[:,:2]=(projected[:,:2]-center)*scale
        projected[:,0]+=width/2;projected[:,1]=height/2-projected[:,1]
        for indices in [[0,1,2],[0,2,3]]:
            tri=projected[indices];tuv=uv[indices]
            xmin=max(0,int(np.floor(tri[:,0].min())));xmax=min(width-1,int(np.ceil(tri[:,0].max())))
            ymin=max(0,int(np.floor(tri[:,1].min())));ymax=min(height-1,int(np.ceil(tri[:,1].max())))
            if xmax<xmin or ymax<ymin:continue
            x,y=np.meshgrid(np.arange(xmin,xmax+1)+.5,np.arange(ymin,ymax+1)+.5)
            x0,y0=tri[0,:2];x1,y1=tri[1,:2];x2,y2=tri[2,:2]
            den=(y1-y2)*(x0-x2)+(x2-x1)*(y0-y2)
            if abs(den)<1e-9:continue
            a=((y1-y2)*(x-x2)+(x2-x1)*(y-y2))/den
            b=((y2-y0)*(x-x2)+(x0-x2)*(y-y2))/den;c=1-a-b
            z=a*tri[0,2]+b*tri[1,2]+c*tri[2,2]
            mask=(a>=-1e-7)&(b>=-1e-7)&(c>=-1e-7)&(z>zbuf[ymin:ymax+1,xmin:xmax+1])
            if not mask.any():continue
            coords=a[:,:,None]*tuv[0]+b[:,:,None]*tuv[1]+c[:,:,None]*tuv[2]
            tx=np.clip(coords[:,:,0].astype(int),0,d.shape[1]-1)
            ty=np.clip(coords[:,:,1].astype(int),0,d.shape[0]-1)
            sample=d[ty,tx];emit=g[ty,tx]
            mask &= sample[:,:,3]>127
            # Approximate the normal diffuse pass plus full-bright emissive pass.
            rgb=np.clip(sample[:,:,:3]*shade+emit[:,:,:3]*(emit[:,:,3:4]/255)*(1-shade),0,255).astype(np.uint8)
            color[ymin:ymax+1,xmin:xmax+1][mask]=rgb[mask]
            zbuf[ymin:ymax+1,xmin:xmax+1][mask]=z[mask]
    return Image.fromarray(color)


def export_glb(faces,diffuse,glow,path):
    binary=bytearray();views=[];accessors=[];meshes=[];nodes=[]
    def store(data,target=None):
        while len(binary)%4:binary.append(0)
        offset=len(binary);binary.extend(data)
        entry={'buffer':0,'byteOffset':offset,'byteLength':len(data)}
        if target:entry['target']=target
        views.append(entry);return len(views)-1
    def accessor(array,kind,bounds=False):
        array=np.array(array,dtype='<f4');v=store(array.tobytes(),34962)
        a={'bufferView':v,'componentType':5126,'count':len(array),'type':kind}
        if bounds:a.update(min=array.min(0).tolist(),max=array.max(0).tolist())
        accessors.append(a);return len(accessors)-1
    groups={}
    for name,v,uv in faces:groups.setdefault(name,[]).append((v,uv))
    for name,group in groups.items():
        positions=[];normals=[];uvs=[]
        for v,uv in group:
            n=np.cross(v[1]-v[0],v[2]-v[0]);n/=np.linalg.norm(n)
            for i in [0,1,2,0,2,3]:positions.append(v[i]/16);normals.append(n);uvs.append(uv[i]/np.array(diffuse.size))
        p=accessor(positions,'VEC3',True);n=accessor(normals,'VEC3');u=accessor(uvs,'VEC2')
        meshes.append({'name':name,'primitives':[{'attributes':{'POSITION':p,'NORMAL':n,'TEXCOORD_0':u},'material':0}]})
        nodes.append({'name':name,'mesh':len(meshes)-1})
    images=[]
    for im in [diffuse,glow]:
        data=io.BytesIO();im.save(data,format='PNG');images.append({'bufferView':store(data.getvalue()),'mimeType':'image/png'})
    doc={'asset':{'version':'2.0','generator':'TMM original Cave/Center model inspection'},'scene':0,
        'scenes':[{'nodes':list(range(len(nodes)))}],'nodes':nodes,'meshes':meshes,
        'materials':[{'name':'Cave obsidian bone / inner fire','alphaMode':'MASK','alphaCutoff':.5,
            'pbrMetallicRoughness':{'baseColorTexture':{'index':0},'metallicFactor':0,'roughnessFactor':.8},
            'emissiveTexture':{'index':1},'emissiveFactor':[1,1,1]}],
        'samplers':[{'magFilter':9728,'minFilter':9728,'wrapS':33071,'wrapT':33071}],
        'textures':[{'sampler':0,'source':0},{'sampler':0,'source':1}],
        'images':images,'buffers':[{'byteLength':len(binary)}],'bufferViews':views,'accessors':accessors}
    js=json.dumps(doc,separators=(',',':')).encode();js+=b' '*((-len(js))%4)
    binary.extend(b'\0'*((-len(binary))%4))
    path.write_bytes(struct.pack('<III',0x46546c67,2,12+8+len(js)+8+len(binary))+
        struct.pack('<I4s',len(js),b'JSON')+js+struct.pack('<I4s',len(binary),b'BIN\0')+binary)


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('private_geo',type=Path);a=p.parse_args()
    faces=surfaces(json.loads(a.private_geo.read_text()));diffuse=Image.open(TEX/'cave_center.png');glow=Image.open(TEX/'cave_center_glow.png')
    card=Image.new('RGB',(1600,1000),(24,29,36));card.paste(render(faces,diffuse,glow,(1040,820),-48,22),(20,130))
    detail=[f for f in faces if not f[0].startswith('Wing') and 'Tail' not in f[0]]
    card.paste(render(detail,diffuse,glow,(510,650),-55,14),(1070,200))
    draw=ImageDraw.Draw(card)
    font='/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf'
    draw.text((35,25),'CAVE / CENTER DRACOLICH',font=ImageFont.truetype(font,36),fill=(244,213,175))
    draw.text((36,78),'Actual model + diffuse/emissive atlases · Offline bind-pose render',font=ImageFont.truetype(font,20),fill=(166,177,190))
    draw.text((1090,155),'SKULL / RIB CAGE DETAIL',font=ImageFont.truetype(font,19),fill=(244,213,175))
    draw.text((35,960),'Black obsidian bone · Molten fissures · Inner-fire core · Native animations require client acceptance',font=ImageFont.truetype(font,18),fill=(166,177,190))
    card.save(ART/'dracolich-cave-center-preview.png')
    export_glb(faces,diffuse,glow,ART/'dracolich-cave-center-review.glb')
    print('Exported original textured static GLB and actual-atlas preview; no native rig or animations distributed')
