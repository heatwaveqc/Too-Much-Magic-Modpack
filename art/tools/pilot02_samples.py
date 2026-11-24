#!/usr/bin/env python3
"""Review-only v0.2 samples. Outputs must remain outside the repository until approval.

Original periodic material painting plus controlled material recoloring. Never
uses upstream texture pixels. Input generation and original Pilot 01 are our art.
"""
import argparse
import hashlib
import io
import json
from pathlib import Path
import subprocess
import numpy as np
from PIL import Image,ImageDraw,ImageFont
from prepare_pilot import PATHS,icon

PREVIOUS='f1b41ea39b857d6ecab355352a877bf923fd4e75'
FONT='/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf'
def font(size):return ImageFont.truetype(FONT,size)
def digest(data):return hashlib.sha256(data).hexdigest()
def original(name):
    return Image.open(io.BytesIO(subprocess.check_output(['git','show',PREVIOUS+':art/too-much-magic-hd/assets/'+PATHS[name]]))).convert('RGBA')
def periodic_noise(x,y,seed,terms=35):
    rng=np.random.default_rng(seed);out=np.zeros(np.broadcast(x,y).shape)
    for i in range(terms):
        fx,fy=rng.integers(-9,10,2)
        if fx==fy==0:continue
        amplitude=1/(1+fx*fx+fy*fy)**.65
        out+=amplitude*np.sin(2*np.pi*(fx*x+fy*y)/64+rng.uniform(0,2*np.pi))
    return out
def stone(x,y,cream=False):
    n=periodic_noise(x,y,41 if not cream else 98)*5
    n+=periodic_noise(x,y,14,80)*1.5
    base=np.array([133,134,130] if not cream else [220,218,204])
    return base+n[...,None]
def cobble(x,y):
    seeds=np.array([(5,6),(23,4),(43,7),(58,22),(39,26),(17,23),(0,42),(22,45),(43,47),(60,59),(6,61),(34,63)])
    dx=(x[...,None]-seeds[:,0]+32)%64-32;dy=(y[...,None]-seeds[:,1]+32)%64-32
    distances=np.sqrt(dx*dx+dy*dy)
    order=np.argsort(distances,axis=-1);which=order[...,0]
    nearest=np.take_along_axis(distances,order[...,:2],axis=-1)
    gap=nearest[...,1]-nearest[...,0]
    blend=np.clip((gap-.3)/1.25,0,1)
    shade=7*np.array([-.3,.7,-.8,.2,-.4,.8,.5,-.1,.7,-.6,.1,-.5])[which]
    shade+=periodic_noise(x,y,19)*3.8+np.minimum(gap,4)*1.2
    face=np.array([132,132,126])+shade[...,None]
    mortar=np.array([80,81,77])+periodic_noise(x,y,4)[...,None]*1.8
    return mortar*(1-blend[...,None])+face*blend[...,None]
def bark(x,y):
    t=2*np.pi
    u=x+1.1*np.sin(t*y/64)+.55*np.sin(3*t*y/64+1)
    phase=9*t*u/64+.28*np.sin(3*t*x/64)
    shade=9*np.sin(phase)+3*np.sin(2*phase+.7)
    shade-=17*np.exp(-((np.sin(phase)+.83)/.15)**2)
    shade+=periodic_noise(x,y,5)*2.7
    return np.array([115,96,73])+shade[...,None]
def oak_end(x,y):
    # A periodic growth field, without the old dark square frame or cropped
    # ring discontinuities. Every ring contour joins continuously on the torus.
    dx=np.sin(np.pi*(x-25)/64);dy=np.sin(np.pi*(y-29)/64)
    radius=np.sqrt((dx*1.02)**2+(dy*.94)**2)*64/np.pi
    ring=radius+.24*periodic_noise(x,y,72)
    shade=-2.6*np.cos(ring*2*np.pi/5.5)+periodic_noise(x,y,12,90)*1.0
    return np.array([171,138,94])+shade[...,None]
def planks(x,y):
    q=(y+5)%16;course=np.floor(((y+5)%64)/16).astype(int)
    grain=1.8*np.sin(2*np.pi*(q/6+.14*np.sin(2*np.pi*x/64)))
    grain+=1.2*np.sin(2*np.pi*(q/3+.12*np.cos(4*np.pi*x/64)))
    tone=np.array([0,3,-2,1])[course]+grain+periodic_noise(x,y,42)*1.8
    wood=np.array([160,125,77])+tone[...,None]
    joint=np.array([10,42,27,56])[course]
    across=np.abs((x-joint+32)%64-32)
    seam=np.maximum(np.exp(-(np.minimum(q,16-q)/.7)**2),np.exp(-(across/.65)**2))
    return wood-seam[...,None]*21
def tile(func):
    y,x=np.mgrid[0:64,0:64];a=func(x.astype(float),y.astype(float))
    return Image.fromarray(np.uint8(np.clip(a,0,255))).convert('RGBA')
def periodic_projection(path):
    """Periodic-plus-smooth decomposition (discrete Poisson boundary correction).

    Unlike mirrored border averaging or blur, subtracts a smooth boundary field
    while retaining the original interior material detail. Visual proof remains
    mandatory: mathematical periodicity is not approval of crossing features.
    """
    a=np.array(Image.open(path).convert('RGB').resize((64,64),Image.Resampling.LANCZOS),dtype=float)
    boundary=np.zeros_like(a)
    boundary[0]+=a[-1]-a[0];boundary[-1]+=a[0]-a[-1]
    boundary[:,0]+=a[:,-1]-a[:,0];boundary[:,-1]+=a[:,0]-a[:,-1]
    y,x=np.mgrid[0:64,0:64]
    denominator=2*np.cos(2*np.pi*x/64)+2*np.cos(2*np.pi*y/64)-4
    denominator[0,0]=1
    smooth_hat=np.fft.fft2(boundary,axes=(0,1))/denominator[...,None]
    smooth_hat[0,0]=0
    smooth=np.fft.ifft2(smooth_hat,axes=(0,1)).real
    return Image.fromarray(np.uint8(np.clip(a-smooth,0,255))).convert('RGBA')
def recolor(im,points):
    a=np.array(im.convert('RGBA'));l=.2126*a[:,:,0]+.7152*a[:,:,1]+.0722*a[:,:,2]
    values,colors=zip(*points);rgb=np.stack([np.interp(l,values,np.array(colors)[:,i]) for i in range(3)],axis=-1)
    a[:,:,:3]=np.uint8(np.clip(rgb,0,255));a[a[:,:,3]==0,:3]=0
    return Image.fromarray(a)
def repeated(im,n=5):
    out=Image.new('RGBA',(im.width*n,im.height*n))
    for y in range(n):
        for x in range(n):out.paste(im,(im.width*x,im.height*y))
    return out
def cube(top,side):
    out=Image.new('RGBA',(280,290));
    def face(im,points,brightness=1):
        source=np.array([[0,0,1],[64,0,1],[64,64,1]])
        dest=np.array(points[:3]);aff=np.linalg.solve(source,dest).T
        mat=np.vstack([aff,[0,0,1]]);inverse=np.linalg.inv(mat)
        draw=im.transform(out.size,Image.Transform.AFFINE,tuple(inverse[:2].reshape(-1)),Image.Resampling.NEAREST)
        a=np.array(draw);a[:,:,:3]=np.uint8(a[:,:,:3].astype(float)*brightness)
        mask=Image.new('L',out.size);ImageDraw.Draw(mask).polygon(points,fill=255)
        out.paste(Image.fromarray(a),(0,0),mask)
    face(top,[(140,12),(268,76),(140,140),(12,76)])
    face(side,[(12,76),(140,140),(140,268),(12,204)],.87)
    face(side,[(140,140),(268,76),(268,204),(140,268)],.75)
    return out
def main():
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--inputs',type=Path,required=True)
    p.add_argument('--out',type=Path,required=True);a=p.parse_args()
    repo=Path(__file__).resolve().parents[2]
    if a.out.resolve().is_relative_to(repo):raise ValueError('Unapproved review output must remain outside Git')
    a.out.mkdir(parents=True,exist_ok=True);inputs=json.loads(a.inputs.read_text())
    samples={name:original(name) for name in PATHS}
    samples['diamond']=icon(inputs['diamond'])
    samples['iron_ingot']=icon(inputs['iron_ingot'])
    paths=dict(PATHS)
    if 'iron_chestplate' in inputs:
        samples['iron_chestplate']=icon(inputs['iron_chestplate'])
        paths['iron_chestplate']='minecraft/textures/item/iron_chestplate.png'
    samples['gold_ingot']=recolor(samples['iron_ingot'],[(0,(36,24,11)),(70,(96,62,23)),(125,(166,119,48)),(185,(224,181,88)),(225,(247,222,154)),(255,(255,251,225))])
    samples['mana_diamond']=recolor(samples['diamond'],[(0,(25,54,98)),(95,(48,104,164)),(150,(82,156,218)),(210,(157,209,242)),(255,(238,250,255))])
    materials={'stone':stone,'cobblestone':cobble,'oak_log':bark,'oak_log_top':oak_end,
               'oak_planks':planks,'livingrock':lambda x,y:stone(x,y,True)}
    for name,func in materials.items():samples[name]=periodic_projection(inputs[name]) if name in inputs and name!='oak_log_top' else tile(func)
    changed={'diamond','mana_diamond','iron_ingot','gold_ingot',*materials}
    manifest=[]
    for name,im in samples.items():
        target=a.out/'assets'/paths[name];target.parent.mkdir(parents=True,exist_ok=True);im.save(target,optimize=True)
        item={'key':'assets/'+paths[name],'review_file':str(target.resolve()),'dimensions':list(im.size),
            'sha256':digest(target.read_bytes()),'revision':'revised' if name in changed else 'carried-forward-unapproved',
            'owner_approval':'pending','game_test':'not run','previous_commit':PREVIOUS,
            'previous_sha256':digest(subprocess.check_output(['git','show',PREVIOUS+':art/too-much-magic-hd/assets/'+PATHS[name]])) if name in PATHS else None}
        if name=='iron_chestplate':
            item['revision']='new-related-deliverable'
            item['paired_uv_key']='assets/'+paths['iron_layer_1']
        if name in materials:
            proof=a.out/(name+'-5x5.png');repeated(im).save(proof,optimize=True)
            item['tiling_proof']=str(proof.resolve());item['tiling_gate']='agent review pending; owner approval pending'
            item['method']='original generation plus periodic Poisson projection' if name in inputs and name!='oak_log_top' else 'original periodic material painting'
        if name in ['mana_diamond','gold_ingot']:
            master='diamond' if name=='mana_diamond' else 'iron_ingot'
            item['master_key']='assets/'+PATHS[master];item['master_sha256']=digest((a.out/'assets'/PATHS[master]).read_bytes())
            assert np.array_equal(np.array(samples[master])[:,:,3],np.array(im)[:,:,3])
            item['exact_alpha_inheritance']='pass';item['geometry_and_detail']='pixelwise controlled monotonic luminance recolor; no resampling'
        manifest.append(item)
    # Category-specific sheets; only our current and previous original art appears.
    def icon_sheet(names,path,title):
        board=Image.new('RGB',(1000,120+240*len(names)),'#1d252d');d=ImageDraw.Draw(board)
        d.text((24,18),title,font=font(27),fill='#f0efe8')
        d.text((24,58),'Original 64px candidates / approval pending / no client test',font=font(16),fill='#b7c4ce')
        for i,name in enumerate(names):
            y=110+i*240;d.text((24,y),name.replace('_',' ').title(),font=font(21),fill='white')
            old=original(name);im=samples[name]
            for xx,src,label in [(24,old,'Prior'),(200,im,'Revision')]:
                board.paste(src.resize((128,128),Image.Resampling.NEAREST),(xx,y+35),src.resize((128,128),Image.Resampling.NEAREST));d.text((xx,y+170),label,font=font(14),fill='#b7c4ce')
            for size,xx in [(64,390),(32,490),(16,555)]:
                d.rectangle((xx-5,y+35,xx+size+5,y+35+max(64,size)),fill='#e0dbce')
                small=im.resize((size,size),Image.Resampling.LANCZOS);board.paste(small,(xx,y+38),small)
                d.text((xx,y+115),str(size)+'px',font=font(13),fill='#b7c4ce')
            d.text((640,y+45),'Exact silhouette inheritance' if name in ['gold_ingot','mana_diamond'] else 'Master proposal',font=font(15),fill='#b7c4ce')
        board.save(a.out/path,optimize=True)
    icon_sheet(['diamond','mana_diamond'],'gem-family.png','Diamond / mana diamond — shared geometry')
    icon_sheet(['iron_ingot','gold_ingot'],'ingot-family.png','Iron / gold — shared ingot master')
    board=Image.new('RGB',(1130,800),'#1d252d');d=ImageDraw.Draw(board)
    d.text((20,15),'Periodic block materials — final native tiles in 5 x 5 repeats',font=font(24),fill='white')
    for i,name in enumerate(materials):
        x=20+(i%3)*370;y=65+(i//3)*365;proof=repeated(samples[name]).convert('RGB')
        board.paste(proof,(x,y+30));d.text((x,y),name.replace('_',' '),font=font(18),fill='#dce5eb')
    board.save(a.out/'block-families.png',optimize=True)
    preview=Image.new('RGB',(1120,420),'#1d252d');d=ImageDraw.Draw(preview)
    d.text((20,15),'Offline model-face previews / no in-game acceptance',font=font(24),fill='white')
    for i,name in enumerate(['stone','cobblestone','oak_planks','oak_log']):
        im=cube(samples['oak_log_top'] if name=='oak_log' else samples[name],samples[name])
        preview.paste(im,(i*280,70),im);d.text((i*280+20,360),name,font=font(18),fill='#dce5eb')
    preview.save(a.out/'model-face-previews.png',optimize=True)
    if 'iron_chestplate' in samples:
        board=Image.new('RGB',(1000,360),'#1d252d');d=ImageDraw.Draw(board)
        d.text((24,18),'Wearable category — inventory icon / existing UV pairing',font=font(25),fill='white')
        d.text((24,58),'Equipped appearance and pose check still pending; not a category approval',font=font(16),fill='#b7c4ce')
        im=samples['iron_chestplate'];large=im.resize((128,128),Image.Resampling.NEAREST)
        board.paste(large,(24,110),large)
        for size,x in [(64,200),(32,290),(16,350)]:
            small=im.resize((size,size),Image.Resampling.LANCZOS);board.paste(small,(x,130),small);d.text((x,210),str(size)+'px',font=font(13),fill='#b7c4ce')
        uv=samples['iron_layer_1'];board.paste(uv,(450,110),uv)
        d.text((450,250),'256 x 128 UV sheet / prior technical candidate',font=font(15),fill='#b7c4ce')
        d.text((24,315),'Material pairing proposal only; no new worn model geometry.',font=font(16),fill='#b7c4ce')
        board.save(a.out/'wearable-pair.png',optimize=True)
    meta=a.out/'assets/minecraft/textures/gui/sprites/widget/button.png.mcmeta'
    meta.write_bytes(subprocess.check_output(['git','show',PREVIOUS+':art/too-much-magic-hd/assets/minecraft/textures/gui/sprites/widget/button.png.mcmeta']))
    (a.out/'sample-manifest.json').write_text(json.dumps(manifest,indent=2)+'\n')
    print(json.dumps({'samples':len(samples),'revised':len(changed),'carried':6,'new_related':int('iron_chestplate' in samples),'directory':str(a.out.resolve())}))

if __name__=='__main__':main()
