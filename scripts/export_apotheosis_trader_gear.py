"""Read-only installed Apotheosis catalog and sanitized saved-trader survey (no game launch)."""
import argparse
import csv
import gzip
import hashlib
import io
import json
import struct
import zipfile
import zlib
from pathlib import Path


def nbt(raw):
    stream = io.BytesIO(gzip.decompress(raw) if raw[:2] == b'\x1f\x8b' else raw)
    def number(fmt):
        return struct.unpack('>' + fmt, stream.read(struct.calcsize('>' + fmt)))[0]
    def string():
        return stream.read(number('H')).decode('utf-8')
    def value(tag):
        if tag in (1, 2, 3, 4, 5, 6):
            return number({1: 'b', 2: 'h', 3: 'i', 4: 'q', 5: 'f', 6: 'd'}[tag])
        if tag == 8:
            return string()
        if tag in (7, 11, 12):
            return [number({7: 'b', 11: 'i', 12: 'q'}[tag]) for _ in range(number('i'))]
        if tag == 9:
            child = number('b')
            return [value(child) for _ in range(number('i'))]
        if tag == 10:
            result = {}
            while True:
                child = number('b')
                if not child:
                    return result
                name = string()
                result[name] = value(child)
        raise ValueError('Unsupported NBT tag')
    root = number('b')
    string()
    return value(root)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--minecraft', type=Path, required=True)
    parser.add_argument('--world', type=Path, required=True)
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    mc, world, out = args.minecraft, args.world, args.output
    out.mkdir(parents=True, exist_ok=True)
    level = nbt((world / 'level.dat').read_bytes())
    mods = {m['ModId']: m['ModVersion'] for m in level['fml']['LoadingModList']}
    jar = next((mc / 'mods').glob('Apotheosis-*.jar'))
    prefix = 'data/apotheosis/wanderer_trades/rare_gear/'
    with zipfile.ZipFile(jar) as archive:
        lang = json.loads(archive.read('assets/apotheosis/lang/en_us.json'))
        rows = []
        for path in sorted(archive.namelist()):
            if not path.startswith(prefix) or not path.endswith('.json'):
                continue
            data = json.loads(archive.read(path))
            conditions = data.get('neoforge:conditions', [])
            assert all(c['type'] == 'neoforge:mod_loaded' for c in conditions), conditions
            for tier, trade in data['trades'].items():
                output = trade['output']
                if not output['id'].startswith('minecraft:diamond_'):
                    continue
                components = output['components']
                key = json.loads(components['minecraft:custom_name'])['translate']
                rows.append(dict(name=lang[key], item=output['id'], tier=tier,
                    inputs=[trade[k] for k in ('input_1', 'input_2') if k in trade],
                    enchantments=components['minecraft:enchantments']['levels'],
                    required_mods=[c['modid'] for c in conditions],
                    conditions_met=all(c['modid'] in mods for c in conditions),
                    rare=trade['rare'], xp=trade['xp'], resource=path,
                    resource_sha256=hashlib.sha256(archive.read(path)).hexdigest()))
        affix_bases = {p: json.loads(archive.read(p)) for p in archive.namelist()
                       if p.startswith('data/apotheosis/affix_loot_entries/diamond_') and p.endswith('.json')}
    # Record every competing resource; do not silently resolve unknown load ordering.
    overrides = []
    targets = {r['resource'] for r in rows}
    for other in sorted((mc / 'mods').glob('*.jar')):
        if other == jar:
            continue
        with zipfile.ZipFile(other) as archive:
            for path in archive.namelist():
                if path in targets or any(path.endswith('/' + t) for t in targets):
                    overrides.append({'provider': other.name, 'resource': path})
    for base in (mc / 'kubejs/data', world / 'datapacks'):
        if not base.exists():
            continue
        for path in base.rglob('*'):
            if path.is_file() and path.suffix == '.zip':
                with zipfile.ZipFile(path) as archive:
                    overrides.extend({'provider': 'world_datapack', 'resource': p}
                                     for p in archive.namelist() if p in targets)
            elif path.is_file() and any(path.as_posix().endswith('/' + t) for t in targets):
                overrides.append({'provider': 'loose_data', 'resource': next(t for t in targets if path.as_posix().endswith(t))})
    saved = {'entity_regions': 0, 'entity_chunks': 0, 'traders': 0, 'diamond_gear_offers': [], 'read_errors': []}
    for path in sorted(world.rglob('entities/*.mca')):
        saved['entity_regions'] += 1
        raw = path.read_bytes()
        for slot in range(1024):
            offset = int.from_bytes(raw[slot * 4:slot * 4 + 3], 'big') * 4096
            if not offset:
                continue
            try:
                length = int.from_bytes(raw[offset:offset + 4], 'big')
                kind = raw[offset + 4]
                data = raw[offset + 5:offset + 4 + length]
                if kind == 2:
                    data = zlib.decompress(data)
                elif kind == 1:
                    data = gzip.decompress(data)
                elif kind != 3:
                    raise ValueError('Unsupported/external compression')
                chunk = nbt(data)
                saved['entity_chunks'] += 1
                for entity in chunk.get('Entities', []):
                    if entity.get('id') != 'minecraft:wandering_trader':
                        continue
                    saved['traders'] += 1
                    for trade in entity.get('Offers', {}).get('Recipes', []):
                        if trade.get('sell', {}).get('id', '').startswith('minecraft:diamond_'):
                            saved['diamond_gear_offers'].append({k: trade[k] for k in ('buy', 'buyB', 'sell') if k in trade})
            except (ValueError, EOFError, struct.error, zlib.error, IndexError) as exc:
                saved['read_errors'].append(type(exc).__name__)
    report = dict(apotheosis_jar=jar.name, jar_sha256=hashlib.sha256(jar.read_bytes()).hexdigest(),
        saved_mod_versions={k: v for k, v in mods.items() if k.startswith('apoth')},
        saved_datapacks=level['Data'].get('DataPacks'), competing_resources=overrides,
        saved_traders=saved, named_enchanted_gear=rows, random_affix_diamond_bases=affix_bases)
    (out / 'catalog.json').write_text(json.dumps(report, indent=2, ensure_ascii=False) + '\n', encoding='utf-8')
    with (out / 'named-gear.csv').open('w', newline='', encoding='utf-8') as handle:
        writer = csv.DictWriter(handle, fieldnames=['name', 'item', 'tier', 'cost', 'enchantments', 'conditions_met'])
        writer.writeheader()
        for row in rows:
            writer.writerow({**{k: row[k] for k in ('name', 'item', 'tier', 'conditions_met')},
                'cost': ' + '.join(f"{i['count']} {i['id']}" for i in row['inputs']),
                'enchantments': '; '.join(f'{k} {v}' for k, v in row['enchantments'].items())})
    print(json.dumps({'named_items': len({r['name'] for r in rows}), 'tier_rows': len(rows),
        'conditions_all_met': all(r['conditions_met'] for r in rows), 'competing_resources': overrides,
        'saved_traders': saved}))


if __name__ == '__main__':
    main()
