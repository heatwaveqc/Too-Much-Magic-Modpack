#!/usr/bin/env python3
"""Locally graft original cubes onto an installed Dragon Survival rig.

No upstream cubes, UVs, textures, or animations enter the distributable kit.
Generated full-rig files stay LOCAL: do not commit or independently redistribute.
Input is the official installed mod JAR, source tree, or native geo JSON.
"""
import argparse
import copy
import hashlib
import json
from pathlib import Path
import zipfile

ROOT = Path(__file__).resolve().parents[1]
ART = ROOT/'compat/transformations/art/dracolich'
NATIVE = 'assets/dragonsurvival/geo/dragon_model.geo.json'
ANIM = 'assets/dragonsurvival/animations/dragon_{}.json'
BODIES = ['center', 'east', 'north', 'south', 'west']


def read_asset(source, name):
    source = Path(source)
    if source.suffix == '.jar':
        with zipfile.ZipFile(source) as z:
            return json.loads(z.read(name))
    if source.is_file():
        if name != NATIVE: return None
        return json.loads(source.read_text())
    for prefix in ['', 'src/main/resources/']:
        file = source/(prefix+name)
        if file.exists(): return json.loads(file.read_text())
    raise FileNotFoundError(name)


def rig_signature(bones):
    return [{k:v for k,v in b.items() if k != 'cubes'} for b in bones]


def fingerprint(bones):
    return hashlib.sha256(json.dumps(rig_signature(bones), sort_keys=True,
        separators=(',', ':')).encode()).hexdigest()


def assemble(native, mesh, expected):
    bones = native['minecraft:geometry'][0]['bones']
    if fingerprint(bones) != expected:
        raise ValueError('Unsupported native rig fingerprint; review the new rig before assembly')
    names = [b['name'] for b in bones]
    if len(names) != len(set(names)): raise ValueError('Duplicate native bone name')
    missing = set(mesh['attachments'])-set(names)
    if missing: raise ValueError(f'Unknown mesh attachment(s): {sorted(missing)}')
    result = copy.deepcopy(native)
    geometry = result['minecraft:geometry'][0]
    geometry['description'] = {
        'identifier': 'geometry.tmm_dracolich',
        'texture_width': mesh['texture_size'][0],
        'texture_height': mesh['texture_size'][1],
        'visible_bounds_width': 24, 'visible_bounds_height': 24,
        'visible_bounds_offset': [0, 2, 0]}
    for bone in geometry['bones']:
        bone.pop('cubes', None)
        if bone['name'] in mesh['attachments']:
            bone['cubes'] = copy.deepcopy(mesh['attachments'][bone['name']])
    assert rig_signature(geometry['bones']) == rig_signature(bones)
    assert sum(len(b.get('cubes', [])) for b in geometry['bones']) == sum(
        len(c) for c in mesh['attachments'].values())
    return result


def validate(mesh, native, model, source):
    names = {b['name'] for b in native['minecraft:geometry'][0]['bones']}
    counts = {}
    for body in BODIES:
        animation = read_asset(source, ANIM.format(body))
        if animation is None:
            counts[body] = 'not supplied (pass installed JAR or source directory)'
            continue
        tracks = {n for a in animation.get('animations', {}).values()
                  for n in a.get('bones', {})}
        # Report preexisting upstream unmatched tracks separately; never invent bones.
        counts[body] = {'animations': len(animation['animations']),
                        'tracks': len(tracks),
                        'native_missing_tracks': sorted(tracks-names),
                        'new_missing_tracks': []}
    tw, th = mesh['texture_size']
    for bone, cubes in mesh['attachments'].items():
        for c in cubes:
            assert all(v > 0 for v in c['size']), (bone, c)
            for f in c['uv'].values():
                assert 0 <= f['uv'][0] <= tw-f['uv_size'][0]
                assert 0 <= f['uv'][1] <= th-f['uv_size'][1]
    return {'native_bones': len(names), 'authored_attachment_points': len(mesh['attachments']),
            'authored_cubes': sum(map(len, mesh['attachments'].values())),
            'rig_preserved_exactly': rig_signature(native['minecraft:geometry'][0]['bones'])
                == rig_signature(model['minecraft:geometry'][0]['bones']),
            'upstream_cube_count': sum(len(b.get('cubes', [])) for b in native['minecraft:geometry'][0]['bones']),
            'upstream_cubes_retained': 0, 'uv_bounds': 'PASS',
            'body_animation_reference_checks': counts,
            'client_render': 'NOT RUN', 'in_game_animation': 'NOT RUN',
            'automatic_dracolich_switch_and_cure': 'NOT IMPLEMENTED'}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('dragon_survival', type=Path)
    parser.add_argument('--output', type=Path, required=True,
                        help='Local-only output directory; full rig must not be redistributed')
    args = parser.parse_args()
    if args.output.resolve().is_relative_to(ROOT):
        raise ValueError('Full native-rig output must stay outside the repository; choose a private output directory')
    mesh = json.loads((ART/'dracolich.mesh.json').read_text())
    contract = json.loads((ART/'rig-contract.json').read_text())
    native = read_asset(args.dragon_survival, NATIVE)
    model = assemble(native, mesh, contract['rig_sha256'])
    report = validate(mesh, native, model, args.dragon_survival)
    args.output.mkdir(parents=True, exist_ok=True)
    (args.output/'dracolich.geo.json').write_text(json.dumps(model, indent=2)+'\n')
    (args.output/'validation.json').write_text(json.dumps(report, indent=2)+'\n')
    print(json.dumps(report, indent=2))


if __name__ == '__main__': main()
