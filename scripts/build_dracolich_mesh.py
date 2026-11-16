#!/usr/bin/env python3
"""Author original skeletal cubes; no upstream model, texture or rig is copied.

The attachment names are an API. Coordinates use Dragon Survival's model space.
Run with Python 3 + Pillow. The separate assembler supplies the installed rig.
"""
import json
import math
from pathlib import Path
from PIL import Image

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / 'compat/transformations/art/dracolich'
PARTS = {}
TILES = {'bone': (0, 0), 'joint': (16, 0), 'tooth': (32, 0),
         'horn': (48, 0), 'soul': (64, 0)}


def cube(bone, origin, size, material='bone', rotation=None, pivot=None):
    tile = TILES[material]
    obj = {'origin': [round(v, 5) for v in origin],
           'size': [round(v, 5) for v in size],
           'uv': {f: {'uv': list(tile), 'uv_size': [16, 16]}
                  for f in ['north', 'south', 'east', 'west', 'up', 'down']}}
    if rotation:
        obj.update(rotation=[round(v, 5) for v in rotation], pivot=list(pivot))
    PARTS.setdefault(bone, []).append(obj)


def bar(bone, a, b, width=0.7, material='bone'):
    # Y-axis cuboid rotated to its endpoint; no zero-thickness texture planes.
    d = [b[i]-a[i] for i in range(3)]
    length = math.sqrt(sum(v*v for v in d))
    rx = math.degrees(math.atan2(d[2], math.hypot(d[0], d[1])))
    rz = math.degrees(math.atan2(-d[0], d[1]))
    # Rz * Rx maps +Y to the segment vector.
    cube(bone, [a[0]-width/2, a[1], a[2]-width/2],
         [width, length, width], material, [rx, 0, rz], a)


def joint(bone, p, width=1.25):
    cube(bone, [v-width/2 for v in p], [width]*3, 'joint')


def author():
    PARTS.clear()
    # Dorsal spine: spaced vertebrae and neural spines, no solid torso shell.
    for z in [-9, -6.7, -4.4, -2.1, 0.2, 2.5]:
        cube('Torso', [-0.9, 20.6, z-0.65], [1.8, 1.5, 1.3])
        bar('Torso', [0, 21.8, z], [0, 24, z+0.5], .6, 'horn')
    bar('Torso', [0, 21.2, -10], [0, 21.2, 3.4], .6, 'joint')
    # Six pairs of articulated-looking ribs with real openings between them.
    for i, z in enumerate([-8.5, -6.3, -4.1, -1.9, .3, 2.5]):
        width = [3.7, 4.7, 5.15, 5, 4.5, 3.7][i]
        for s in [-1, 1]:
            pts = [[0, 21.3, z], [s*width*.72, 20.8, z+.15],
                   [s*width, 17.8, z+.35], [s*width*.83, 14, z+.55],
                   [s*1.1, 12.6, z+.7]]
            for a, b in zip(pts, pts[1:]):
                bar('Torso', a, b, .65)
    bar('Torso', [0, 12.7, -8], [0, 12.7, 2.9], .8)
    # Shoulder blades and collarbone.
    for s in [-1, 1]:
        bar('Torso', [0, 20.8, -8.7], [s*4.6, 18.2, -7], 1)
        bar('Torso', [s*2, 20.5, -7.4], [s*4.7, 20, -2.7], 1.1)
        bar('Torso', [s*4.7, 20, -2.7], [s*4.7, 17.6, -6.6], .8)
    # Open pelvic girdle (two arches instead of a solid hip block).
    for s in [-1, 1]:
        pts = [[0, 21, 2.6], [s*3.9, 20.6, 3.5], [s*4.2, 16.4, 8.4],
               [s*1.5, 14.6, 10.6], [0, 16.4, 10.7]]
        for a, b in zip(pts, pts[1:]):
            bar('Pelvis_s', a, b, 1.05)
        bar('Pelvis_s', [0, 20.5, 6], [s*3.5, 18.6, 7.7], .85)
    bar('Pelvis_s', [0, 21, 2.5], [0, 20.3, 11.2], 1.1)
    # Neck segments use native deformation/Blend children, never the head root.
    for n, y, z, radius in [(4, 19.4, -11.5, 2.9), (3, 22.1, -11, 2.1),
                             (2, 25.3, -10.6, 1.6), (1, 29.3, -10.7, 1.45)]:
        bone = f'Neck{n}_s'
        bar(bone, [0, y-2.3, z], [0, y+2.6, z+.3], 1.15)
        for dy in [-1.6, .6, 2.2]:
            cube(bone, [-radius/2, y+dy, z-.65], [radius, .8, 1.5])
            for s in [-1, 1]:
                bar(bone, [0, y+dy+.4, z], [s*radius, y+dy+.65, z+1.1], .48)
        bar(bone, [0, y+1, z+.5], [0, y+1.6, z+2.8], .55, 'horn')
    # Skull roof, rear cranium, zygomatic arches and OPEN orbital windows.
    h = 'BlendHandlerHead'
    cube(h, [-2.85, 36.1, -15.5], [5.7, 1, 7.7])
    cube(h, [-2.3, 31.8, -8.7], [4.6, 4.4, 1.05])
    for s in [-1, 1]:
        bar(h, [s*2.8, 36.1, -15], [s*3.7, 35.5, -10.7], .9)
        bar(h, [s*3.7, 35.5, -10.7], [s*3.2, 32.6, -8.6], .9)
        bar(h, [s*3.2, 32.6, -8.6], [s*2.8, 32.1, -15.7], .75)
        bar(h, [s*2.8, 32.1, -15.7], [s*2.8, 36.1, -15], .75)
        # Inset soul ember floats inside a hollow socket; later species glow.
        cube(h, [s*2.3-.35, 33.5, -12.65], [.7, .8, .9], 'soul')
        bar(h, [s*2.6, 36.6, -8.8], [s*4.3, 40, -5.3], 1, 'horn')
        bar(h, [s*4.3, 40, -5.3], [s*4.7, 42, -3.5], .6, 'horn')
    # Bony elongated muzzle with hollow nasal channel.
    for s in [-1, 1]:
        bar('Snout', [s*1.7, 34.9, -14.8], [s*1.2, 34.2, -22.5], .85)
        bar('Snout', [s*2.25, 32.2, -15.6], [s*1.65, 32.1, -22], .8)
    cube('Snout', [-1.8, 32, -22.5], [3.6, .9, .85])
    for z in [-16.2, -18, -19.8, -21.3]:
        for s in [-1, 1]:
            bar('Snout', [s*1.8, 32.1, z], [s*1.7, 30.9, z-.2], .45, 'tooth')
    # Lower jaw: separate native jaw animation, no cheek flesh/tongue.
    j = 'BlendHandlerJaw'
    for s in [-1, 1]:
        bar(j, [s*2.4, 33.3, -10.8], [s*2.0, 30.9, -16], .8)
        bar(j, [s*2.0, 30.9, -16], [s*1.45, 30.8, -21.7], .8)
        for z in [-16.8, -18.5, -20.2]:
            bar(j, [s*1.65, 31.1, z], [s*1.6, 32.1, z-.1], .4, 'tooth')
    bar(j, [-1.45, 30.8, -21.7], [1.45, 30.8, -21.7], .8)
    # Four legs. Native shoulder/hip/upper/lower/hand segmentation preserved.
    for side, s in [('Left', 1), ('Right', -1)]:
        for limb in ['Front', 'Back']:
            front = limb == 'Front'
            prefix = f'{limb}Leg{side}'
            x = s*(4.2 if front else 3)
            z = -5 if front else 8
            shoulder = [s*(5.2 if front else 4), 18, -7 if front else 8]
            elbow = [x, 13 if front else 11, z]
            knee = [x, 6.97385 if front else 6.39747, -5.70114 if front else 7.72448]
            # Endpoints are the actual child joints in the native interface,
            # so the thin bones meet even without a fleshy shell hiding gaps.
            wrist = [x, 7.48146 if front else 7.20033,
                     (-11.42684 if s > 0 else -11.72683) if front else 1.93018]
            upper = prefix+'UpperAll'
            bar(prefix, shoulder, elbow, 1.1)
            joint(prefix, shoulder, 1.65)
            bar(upper, elbow, knee, 1)
            joint(upper, elbow, 1.4)
            lower = prefix+'LowerAll'
            a, b = knee, wrist
            for dx in [-.5, .5]:
                bar(lower, [a[0]+dx, a[1], a[2]], [b[0]+dx, b[1], b[2]], .48)
            joint(lower, b, 1.3)
            foot = prefix+('HandAll' if front else 'Foot')
            fy = wrist[1]
            fz = wrist[2]-1.7
            bar(foot, wrist, [x, fy, fz-.8], .95)
            for dx in [-1.2, 0, 1.2]:
                a = [x+dx*.55, fy, fz]
                b = [x+dx, fy-.15, fz-2]
                c = [x+dx*1.1, fy-.5, fz-3.6]
                bar(foot, a, b, .5)
                joint(foot, b, .65)
                bar(foot, b, c, .4)
                bar(foot, c, [c[0], c[1]-.7, c[2]-1], .32, 'horn')
    # Segmented tapering tail; each section follows its native tail controller.
    for n, start, end, width in [(5, 10, 21, 2), (4, 21, 31, 1.65),
                                (3, 31, 40, 1.3), (2, 40, 49.5, 1),
                                (1, 49.5, 60, .65)]:
        bone = f'Tail{n}_s' if n > 1 else 'MolangHandlerTail1'
        bar(bone, [0, 19.4, start], [0, 19.4, end], width*.55)
        for z in range(math.ceil(start)+1, math.floor(end), 2):
            cube(bone, [-width/2, 19.4-width/2, z-.45], [width, width, .9])
            for s in [-1, 1]:
                bar(bone, [0, 19.4, z], [s*width*1.3, 19.4, z+.4], .38)
            bar(bone, [0, 19.8, z], [0, 20+width, z+.55], .4, 'horn')
    # Wings are a skeleton: humerus, paired forearm, long separated fingers.
    # Split across native arm, hand and edge so folding does not rigidly drag it.
    for side, s in [('Left', 1), ('Right', -1)]:
        main = f'WingNewMain{side}'
        bar(main, [s*5, 21, -6.5], [s*17, 21, -5.5], 1.25)
        joint(main, [s*5.5, 21, -6.5], 1.65)
        arm = f'Wing{side}Arm2'
        for dz in [-.55, .55]:
            bar(arm, [s*17, 21, -5.5+dz], [s*31, 21, -14+dz], .6)
        hand = f'Wing{side}Hand'
        joint(hand, [s*31, 21, -14], 1.8)
        # Three whole fingers follow the wrist. One leading finger has a distal
        # section hinged exactly at the native Edge pivot: no floating finger
        # ends when Edge rotates independently of Hand.
        for z, span in [(-14, 76), (15, 80), (29, 77)]:
            a = [s*31, 21, -14]
            b = [s*52, 21, z*.65-4]
            c = [s*span, 21, z]
            bar(hand, a, b, .64)
            joint(hand, b, .85)
            bar(hand, b, c, .5)
            bar(hand, c, [c[0]+s*2, 20.8, c[2]+1], .28, 'horn')
        hinge = [s*63.989, 21.0005, 5]
        bar(hand, [s*31, 21, -14], hinge, .7)
        joint(hand, hinge, 1)
        edge = f'Wing{side}Edge'
        # Starting at the pivot makes the native rest rotation safe as well.
        tip = [s*94, 21.0005, 5]
        bar(edge, hinge, tip, .48)
        bar(edge, tip, [tip[0]+s*2, 20.8, tip[2]+1], .28, 'horn')
        bar(f'Wing{side}Toe', [s*31, 21, -14], [s*34, 21.2, -18], .65)
        bar(f'Wing{side}Claw', [s*34, 21.2, -18], [s*36, 20.8, -20], .4, 'horn')
        # Alternate bodies' small wings and swimming flippers get real bones too.
        for prefix, x, y, z in [('SmallWing', 5, 22, -6.5),
                                ('FrontFlipper', 5, 18, -6.5),
                                ('BackFlipper', 4, 18, 6.5)]:
            stem = prefix+side
            bar(stem, [s*x, y, z], [s*x, y+10.8, z], .7)
            for dz in [-4, 0, 4]:
                bar(stem, [s*x, y, z], [s*x, y+10.8, z+dz], .42)
                edge = prefix+'Edge'+side
                bar(edge, [s*x, y+10.8, z+dz], [s*x, y+19, z+dz*1.4], .35)
    return {'schema': 1, 'identifier': 'tmm:dracolich_skeleton_v1',
            'texture_size': [128, 32], 'coordinates': 'bedrock_model_space',
            'required_model': 'dragonsurvival:dragon_model',
            'materials': {k: list(v) for k,v in TILES.items()},
            'attachments': PARTS}


def texture():
    image = Image.new('RGBA', (128, 32), (0, 0, 0, 0))
    bases = {'bone': (207, 198, 171), 'joint': (153, 143, 119),
             'tooth': (240, 229, 198), 'horn': (119, 111, 94),
             'soul': (236, 153, 59)}
    for name, (tx, ty) in TILES.items():
        base = bases[name]
        for y in range(16):
            for x in range(16):
                # Deterministic pixel mottling and darker edge wear.
                v = ((x*37+y*61+x*y*3) % 17)-8
                if x in [0, 15] or y in [0, 15]: v -= 18
                image.putpixel((tx+x,ty+y), tuple(max(0,min(255,c+v)) for c in base)+(255,))
    image.save(OUT/'dracolich_neutral.png')


if __name__ == '__main__':
    OUT.mkdir(parents=True, exist_ok=True)
    mesh = author()
    (OUT/'dracolich.mesh.json').write_text(json.dumps(mesh, indent=2)+'\n')
    texture()
    print(f'Authored {sum(map(len, PARTS.values()))} solid cubes on {len(PARTS)} native attachment points')
