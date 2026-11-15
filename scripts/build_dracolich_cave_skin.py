#!/usr/bin/env python3
"""Original pixel atlas and Cave/Center mesh. No upstream art is copied.

Every face gets its own UV rectangle; the diffuse and emissive atlases match.
These are exact model UV assets, not generated concept illustrations.
"""
import copy
from decimal import Decimal
import hashlib
import json
import math
from pathlib import Path
import random
from PIL import Image, ImageDraw
import build_dracolich_mesh as author

ROOT = Path(__file__).resolve().parents[1]
RES = ROOT/'compat/transformations/src/main/resources/assets/tmm_transformations'
OUT = RES/'dracolich'
TEX = RES/'textures/entity/dracolich'
SIDES = ['north', 'south', 'east', 'west', 'up', 'down']
ATLAS = 1024


def canonical(value):
    if isinstance(value, dict):
        return '{'+','.join(json.dumps(k)+':'+canonical(value[k]) for k in sorted(value))+'}'
    if isinstance(value, list): return '['+','.join(map(canonical, value))+']'
    if isinstance(value, (int, float)):
        number = format(Decimal(str(value)).normalize(), 'f')
        return '0' if Decimal(number) == 0 else number
    return json.dumps(value, ensure_ascii=False, separators=(',', ':'))


def add_details(mesh):
    author.PARTS = mesh['attachments']
    # Only the large-wing Center mesh is in this pilot.
    for key in list(author.PARTS):
        if key.startswith(('SmallWing', 'FrontFlipper', 'BackFlipper')):
            del author.PARTS[key]
    # Obsidian brow ridges and swept secondary horns; all follow native Head.
    for s in [-1, 1]:
        author.bar('BlendHandlerHead', [s*2.6, 36.7, -14.9], [s*3.1, 37, -10], 1.1, 'horn')
        author.bar('BlendHandlerHead', [s*2.2, 36.7, -11.5], [s*3, 39.4, -9], .85, 'horn')
        author.bar('Snout', [s*1.6, 32.4, -20.8], [s*1.55, 30.4, -21], .58, 'tooth')
    # A real visible inner-fire core in the open rib cage. It follows breathing
    # with Torso; it does not depend on light glowing across empty space.
    for y, z, w in [(14.1, -4.2, 1), (15.3, -4.5, 1.65),
                     (16.8, -4.1, 1.4), (18.1, -3.9, 1), (19.1, -3.6, .5)]:
        author.cube('Torso', [-w/2, y, z-w/2], [w, 1.6, w], 'soul', [0, 0, 12], [0, y, z])
    # Original casting graphics replace the removed native art. Their existing
    # control bones retain native casting motion, scale and visibility.
    author.cube('MagicRing', [-2, 11.74, -19.5], [4, 0, 4], 'soul')
    author.cube('MagicCubeRotation', [-.65, 11.1, -18.15], [1.3, 1.3, 1.3], 'soul')
    for i in range(1, 9):
        author.cube(f'MagicCirclePart{i}', [-5, -200, -80], [10, 70, 0], 'soul')
    return mesh


def face_size(cube, side, effect):
    x, y, z = cube['size']
    w, h = (x, y) if side in ('north', 'south') else (z, y) if side in ('east', 'west') else (x, z)
    if effect: return (24, 96) if h > 20 else (24, 24)
    return max(4, min(96, math.ceil(w*3))), max(4, min(192, math.ceil(h*3)))


def paint(w, h, material, seed, effect):
    rng = random.Random(seed)
    diffuse = Image.new('RGBA', (w, h), (0, 0, 0, 0))
    glow = Image.new('RGBA', (w, h), (0, 0, 0, 0))
    if effect:
        d, g = ImageDraw.Draw(diffuse), ImageDraw.Draw(glow)
        # Original segmented fire runes on a transparent background.
        for painter in (d, g):
            color = (255, 125, 16, 255)
            painter.rectangle((1, 1, w-2, h-2), outline=color, width=1)
            for y in range(5, h-3, 13):
                painter.line([(w//2, y), (w-5, y+4), (w//2, y+8), (4, y+4), (w//2, y)], fill=color, width=2)
        return diffuse, glow
    if material == 'soul':
        for y in range(h):
            for x in range(w):
                t = abs(2*x/max(1,w-1)-1)
                col = (255, int(205-90*t), int(60-45*t), 255)
                diffuse.putpixel((x,y), col); glow.putpixel((x,y), col)
        return diffuse, glow
    bases = {'bone': (30, 27, 33), 'joint': (51, 31, 26),
             'tooth': (98, 63, 44), 'horn': (23, 22, 28)}
    base = bases[material]
    for y in range(h):
        for x in range(w):
            edge = min(x, y, w-1-x, h-1-y)
            noise = rng.randrange(-7, 8) + (8 if edge == 0 else 0)
            diffuse.putpixel((x,y), tuple(max(0, min(255, v+noise)) for v in base)+(255,))
    # Each surface has different fissures. Most surface remains black bone.
    if rng.random() < (.72 if material in ('bone','joint') else .42):
        d, g = ImageDraw.Draw(diffuse), ImageDraw.Draw(glow)
        x = rng.randrange(1, max(2,w-1)); points=[]
        for y in range(0, h, max(1,h//7)):
            x = max(0, min(w-1, x+rng.choice([-1, 0, 1])))
            points.append((x,y))
        d.line(points, fill=(187, 57, 8, 255), width=2 if w>8 else 1)
        g.line(points, fill=(255, 88, 8, 255), width=1)
        if w > 8 and h > 8:
            sx,sy = points[len(points)//2]
            branch=[(sx,sy),(min(w-1,sx+w//4), min(h-1,sy+2)),(min(w-1,sx+w//3),min(h-1,sy+5))]
            d.line(branch,fill=(233, 95, 9, 255),width=1)
            g.line(branch,fill=(255, 108, 10, 255),width=1)
    return diffuse, glow


def build():
    mesh = add_details(copy.deepcopy(author.author()))
    old_material = {int(v[0]/16): k for k,v in mesh['materials'].items()}
    entries = []
    for bone, cubes in mesh['attachments'].items():
        for i, cube in enumerate(cubes):
            material=old_material[int(cube['uv']['north']['uv'][0]/16)]
            for side in SIDES:
                effect=bone.startswith('Magic')
                w,h=face_size(cube,side,effect)
                entries.append((bone,i,side,material,w,h,effect))
    # Shelf pack tallest faces first; a two-pixel gutter avoids atlas bleed.
    entries.sort(key=lambda e:(-e[5],-e[4],e[0],e[1],e[2]))
    diffuse=Image.new('RGBA',(ATLAS,ATLAS),(0,0,0,0))
    emissive=Image.new('RGBA',(ATLAS,ATLAS),(0,0,0,0))
    x=y=row_h=1
    for bone,i,side,material,w,h,effect in entries:
        if x+w+2>ATLAS: x=1; y+=row_h+2; row_h=1
        if y+h+2>ATLAS: raise RuntimeError('Cave atlas overflow')
        seed=int.from_bytes(hashlib.sha256(f'{bone}:{i}:{side}'.encode()).digest()[:8])
        d,g=paint(w,h,material,seed,effect)
        diffuse.paste(d,(x,y));emissive.paste(g,(x,y))
        mesh['attachments'][bone][i]['uv'][side]={'uv':[x,y],'uv_size':[w,h]}
        x+=w+2;row_h=max(row_h,h)
    mesh['identifier']='tmm:dracolich_cave_center_v1'
    mesh['texture_size']=[ATLAS,ATLAS]
    mesh.pop('materials')
    OUT.mkdir(parents=True,exist_ok=True);TEX.mkdir(parents=True,exist_ok=True)
    (OUT/'cave_center.mesh.json').write_text(json.dumps(mesh,separators=(',',':'))+'\n')
    diffuse.save(TEX/'cave_center.png');emissive.save(TEX/'cave_center_glow.png')
    marker=RES/'geo/dracolich_cave_center.geo.json';marker.parent.mkdir(exist_ok=True)
    marker.write_text(json.dumps({'format_version':'1.12.0','minecraft:geometry':[{
        'description':{'identifier':'geometry.tmm_unassembled_marker','texture_width':ATLAS,'texture_height':ATLAS},
        'bones':[{'name':'tmm_unassembled_marker','pivot':[0,0,0]}]}]},indent=2)+'\n')
    print(f'Cave/Center: {sum(map(len,mesh["attachments"].values()))} original cubes, '
          f'{len(entries)} individual face UVs, {ATLAS}x{ATLAS} diffuse/emissive atlases')
    return mesh


if __name__=='__main__': build()
