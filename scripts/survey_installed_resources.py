"""Read-only static evidence census. Resource IDs are NOT runtime registry entries."""
import argparse, hashlib, json, zipfile
from pathlib import Path

def main():
    p = argparse.ArgumentParser()
    p.add_argument('--minecraft', type=Path, required=True)
    p.add_argument('--output', type=Path, required=True)
    a = p.parse_args()
    resources, artifacts, errors = {}, [], []
    def accept(name, content, provider):
        parts = name.split('/')
        if len(parts) < 4 or parts[0] != 'data' or not name.endswith('.json'):
            return
        kind = parts[2]
        if kind not in ('recipe', 'loot_table', 'tags', 'worldgen', 'pe_custom_conversions'):
            return
        try:
            data = json.loads(content)
        except (ValueError, UnicodeError) as e:
            errors.append({'provider': provider, 'path': name, 'error': str(e)})
            return
        resources[name] = {'provider': provider, 'path': name, 'data': data}
    for jar in sorted((a.minecraft / 'mods').glob('*.jar')):
        digest = hashlib.sha256(jar.read_bytes()).hexdigest()
        artifacts.append({'file': jar.name, 'sha256': digest})
        with zipfile.ZipFile(jar) as z:
            for name in z.namelist():
                if name.startswith('data/') and name.endswith('.json'):
                    accept(name, z.read(name), jar.name)
    for folder in [a.minecraft/'kubejs'/'data', a.minecraft/'saves'/'Test World'/'datapacks']:
        if folder.exists():
            for f in sorted(folder.rglob('*.json')):
                name = ('data/' + f.relative_to(folder).as_posix()) if folder.name == 'data' else ''
                if name:
                    accept(name, f.read_bytes(), 'installed-kubejs')
    records = list(resources.values())
    # Preserve complete native payloads so counts, conditions, tags and components remain reviewable.
    result = {'schema': 1, 'status': 'STATIC_RESOURCE_EVIDENCE_ONLY',
        'limitations': ['Not a registered item/entity census or post-mapping EMC export.',
                       'Native JAR plus installed KubeJS overlay; other datapacks, conditions, generated recipes and death handlers unresolved.',
                       'Recipe/tag/loot candidates are not proof of runtime availability or missing EMC.'],
        'artifacts': artifacts, 'resources': records, 'parse_errors': errors}
    a.output.parent.mkdir(parents=True, exist_ok=True)
    a.output.write_text(json.dumps(result, ensure_ascii=True, separators=(',', ':'))+'\n', encoding='utf-8')
    print(json.dumps({'jars': len(artifacts), 'resources': len(records), 'parse_errors': len(errors), 'bytes': a.output.stat().st_size}))

if __name__ == '__main__':
    main()
