#!/usr/bin/env python3
"""Process original generated pilot art; paint original UV/GUI technical samples.

Requires Pillow and NumPy locally. No third-party image is an input. Raw generation
files remain outside Git. The selected 64px outputs are the review artifacts.
"""
import argparse
import json
from pathlib import Path
import numpy as np
from PIL import Image, ImageDraw

PACK=Path('art/too-much-magic-hd')
PATHS={
 'diamond':'minecraft/textures/item/diamond.png',
 'emerald':'minecraft/textures/item/emerald.png',
 'iron_ingot':'minecraft/textures/item/iron_ingot.png',
 'gold_ingot':'minecraft/textures/item/gold_ingot.png',
 'mana_diamond':'botania/textures/item/mana_diamond.png',
 'rune_mana':'botania/textures/item/rune_mana.png',
 'pure_daisy':'botania/textures/block/pure_daisy.png',
 **{n:f'minecraft/textures/block/{n}.png' for n in ['stone','cobblestone','oak_planks','oak_log','oak_log_top']},
 'livingrock':'botania/textures/block/livingrock.png',
 'runic_altar_side':'botania/textures/block/runic_altar_side.png',
 'iron_layer_1':'minecraft/textures/models/armor/iron_layer_1.png',
 'button':'minecraft/textures/gui/sprites/widget/button.png',
}

def save(name,im):
    p=PACK/'assets'/PATHS[name]
    p.parent.mkdir(parents=True,exist_ok=True)
    im.save(p,optimize=True)

def icon(path):
    im=Image.open(path).convert('RGBA')
    a=np.array(im)
    # Remove low-alpha generation debris and keep the main connected silhouette.
    mask=a[:,:,3]>=128
    from collections import deque
    visited=np.zeros(mask.shape,dtype=bool); largest=[]
    for y,x in zip(*np.where(mask)):
        if visited[y,x]: continue
        q=deque([(y,x)]);visited[y,x]=True;component=[]
        while q:
            yy,xx=q.popleft();component.append((yy,xx))
            for dy,dx in [(0,1),(0,-1),(1,0),(-1,0)]:
                ny,nx=yy+dy,xx+dx
                if 0<=ny<mask.shape[0] and 0<=nx<mask.shape[1] and mask[ny,nx] and not visited[ny,nx]:
                    visited[ny,nx]=True;q.append((ny,nx))
        if len(component)>len(largest):largest=component
    clean=np.zeros(mask.shape,dtype=bool)
    yy,xx=zip(*largest);clean[yy,xx]=True
    a[:,:,3]=np.where(clean,255,0)
    im=Image.fromarray(a).crop(Image.fromarray(np.uint8(clean)*255).getbbox())
    im.thumbnail((56,56),Image.Resampling.LANCZOS)
    canvas=Image.new('RGBA',(64,64));canvas.alpha_composite(im,((64-im.width)//2,(64-im.height)//2))
    a=np.array(canvas);a[:,:,3]=np.where(a[:,:,3]>=128,255,0);a[a[:,:,3]==0,:3]=0
    return Image.fromarray(a)

def tile(path):
    im=Image.open(path).convert('RGB').resize((64,64),Image.Resampling.LANCZOS)
    a=np.array(im,dtype=float)
    # Paired edge reconciliation; review 3x3 because equal edges alone are not visual QA.
    for axis in [1,0]:
        b=a if axis==1 else a.transpose(1,0,2)
        for i in range(6):
            avg=(b[:,i]+b[:,-1-i])/2
            weight=(1-i/6)**2
            b[:,i]=b[:,i]*(1-weight)+avg*weight
            b[:,-1-i]=b[:,-1-i]*(1-weight)+avg*weight
    return Image.fromarray(np.uint8(np.clip(a,0,255)))

def marble():
    y,x=np.mgrid[0:64,0:64];t=2*np.pi
    n=1.5*np.sin(t*x/64)+1.7*np.cos(t*(y+x)/64)+np.sin(t*(3*x-2*y)/64)
    vein=np.exp(-((np.sin(t*(x+y)/64)+.2*np.sin(t*3*y/64))/.07)**2)*5
    a=np.stack([222+n-vein,220+n-vein,207+n-vein],axis=-1)
    a[:,0]=(a[:,0]+a[:,-1])/2;a[:,-1]=a[:,0]
    a[0]=(a[0]+a[-1])/2;a[-1]=a[0]
    return Image.fromarray(np.uint8(a))

def altar():
    a=np.array(marble().convert('RGBA'));a[:,:,3]=0
    a[16:40,:,3]=255;a[40:48,16:48,3]=255;a[48:64,8:56,3]=255
    # Exact native cuboid regions from the pinned model, not sampled artwork.
    for y in [16,38,48,62]:
        active=a[y,:,3]>0;a[y,active,:3]=[169,168,152]
    a[41:44,16:48,:3]=[64,189,192]
    a[44:46,16:48,:3]=[43,123,132]
    return Image.fromarray(a)

def armor():
    im=Image.new('RGBA',(256,128));d=ImageDraw.Draw(im)
    def panel(box,side=0):
        x0,y0,x1,y1=[v*4 for v in box]
        for y in range(y0,y1):
            value=int(183-side*14+12*np.cos((y-y0)/max(1,y1-y0)*np.pi))
            d.line((x0,y,x1-1,y),fill=(value-8,value,value+8,255))
        d.line((x0,y1-1,x1-1,y1-1),fill=(83,100,113,255),width=1)
        d.line((x0,y0,x1-1,y0),fill=(221,230,233,255),width=1)
    def box(u,v,w,h,depth):
        panel((u+depth,v,u+depth+w,v+depth))
        panel((u+depth+w,v,u+depth+2*w,v+depth),1)
        for side,(a,b) in enumerate([(u,u+depth),(u+depth,u+depth+w),(u+depth+w,u+2*depth+w),(u+2*depth+w,u+2*depth+2*w)]):
            panel((a,v+depth,b,v+depth+h),side%2)
    box(0,0,8,8,8);box(16,16,8,12,4);box(40,16,4,12,4);box(0,16,4,12,4)
    # Visor, neck and lower leg openings use humanoid layer-1 UV coordinates.
    d.rectangle((9*4,9*4,15*4-1,14*4-1),fill=(0,0,0,0))
    d.rectangle((23*4,20*4,25*4-1,21*4-1),fill=(0,0,0,0))
    d.rectangle((0,16*4,16*4-1,26*4-1),fill=(0,0,0,0))
    # Restrained seams and chest tooling, no runes or new overlay semantics.
    d.line((20*4,25*4,28*4-1,25*4),fill=(106,124,138,255),width=2)
    return im

def button():
    im=Image.new('RGBA',(800,80));d=ImageDraw.Draw(im)
    for y in range(80):
        v=int(100-15*y/79);d.line((0,y,799,y),fill=(v-7,v-3,v,255))
    d.rectangle((0,0,799,79),outline=(38,43,49,255),width=4)
    d.line((4,4,795,4),fill=(151,156,162,255),width=4)
    d.line((4,4,4,75),fill=(139,145,152,255),width=4)
    d.line((4,75,795,75),fill=(52,59,66,255),width=4)
    return im

def main():
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--inputs',type=Path,required=True);args=p.parse_args()
    inputs=json.loads(args.inputs.read_text())
    for name,path in inputs.items():
        save(name,tile(path) if name in ['stone','cobblestone','oak_planks','oak_log','oak_log_top'] else icon(path))
    save('livingrock',marble());save('runic_altar_side',altar());save('iron_layer_1',armor());save('button',button())
    meta=PACK/'assets/minecraft/textures/gui/sprites/widget/button.png.mcmeta'
    meta.write_text(json.dumps({'gui':{'scaling':{'type':'nine_slice','width':200,'height':20,'border':3}}},indent=2)+'\n')

if __name__=='__main__':main()
