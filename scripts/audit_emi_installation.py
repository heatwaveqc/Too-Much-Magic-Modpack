"""Read-only EMI/JEI provider and recipe-resource inventory. Never launches Minecraft.

Only annotation candidates are inventoried; presence is not proof of runtime registration.
"""
import argparse
import collections
import hashlib
import io
import json
import subprocess
import tomllib
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
PINS = ('emi', 'emisemble', 'extra-mod-integrations', 'almostunified',
        'just-another-witchery-remake', 'mahou-tsukai', 'kubejs')


def providers(binary, label, depth=0):
    if depth > 4:
        raise ValueError('Nested JAR depth exceeds audit limit')
    result = []
    with zipfile.ZipFile(io.BytesIO(binary)) as jar:
        emi, jei = [], []
        for name in jar.namelist():
            if name.endswith('.class'):
                data = jar.read(name)
                if b'Ldev/emi/emi/api/EmiEntrypoint;' in data:
                    emi.append(name[:-6].replace('/', '.'))
                if b'Lmezz/jei/api/JeiPlugin;' in data:
                    jei.append(name[:-6].replace('/', '.'))
            elif name.startswith('META-INF/') and name.endswith('.jar'):
                if jar.getinfo(name).file_size > 110 * 1024 * 1024:
                    raise ValueError('Nested archive too large')
                result.extend(providers(jar.read(name), label + '!' + name, depth + 1))
        if emi or jei:
            result.append({'container': label, 'emi_annotated_candidates': sorted(emi),
                           'jei_annotated_candidates': sorted(jei)})
    return result


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--minecraft', type=Path, required=True)
    parser.add_argument('--java-bin', type=Path, required=True)
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    mc = args.minecraft
    args.output.mkdir(parents=True, exist_ok=True)
    jars, pins = {}, {}
    for slug in PINS:
        pin = tomllib.loads((ROOT / 'mods' / (slug + '.pw.toml')).read_text(encoding='utf-8'))
        path = mc / 'mods' / pin['filename']
        raw = path.read_bytes()
        if hashlib.new(pin['download']['hash-format'], raw).hexdigest() != pin['download']['hash']:
            raise ValueError('Installed JAR differs from audited pin: ' + slug)
        jars[slug] = path
        pins[slug] = {'filename': path.name, 'sha256': hashlib.sha256(raw).hexdigest(),
                      'pin_hash_format': pin['download']['hash-format'], 'pin_hash': pin['download']['hash']}
    inventory = []
    paths = sorted((mc / 'mods').glob('*.jar'))
    for path in paths:
        inventory.extend(providers(path.read_bytes(), path.name))
    with zipfile.ZipFile(jars['just-another-witchery-remake']) as jar:
        types = collections.Counter()
        recipes = []
        for name in jar.namelist():
            if name.startswith('data/witchery/recipe/') and name.endswith('.json'):
                data = json.loads(jar.read(name))
                types[data.get('type', '<missing>')] += 1
                recipes.append({'resource': name, 'type': data.get('type'),
                                'conditions': data.get('neoforge:conditions', [])})
        categories = sorted(n[:-6].replace('/', '.') for n in jar.namelist()
                            if n.startswith('dev/sterner/witchery/integration/jei/') and n.endswith('Category.class'))
        native_emi_api_references = [n for n in jar.namelist() if n.endswith('.class')
                                     and b'dev/emi/emi/api/' in jar.read(n)]
    with zipfile.ZipFile(jars['emisemble']) as jar:
        witchery_bridge_candidates = [n for n in jar.namelist() if n.endswith('.class')
                                      and (b'witchery' in jar.read(n).lower())]
    def bytecode(slug, cls):
        return subprocess.check_output([str(args.java_bin / 'javap.exe'), '-c', '-p',
                                        '-classpath', str(jars[slug]), cls], text=True, encoding='utf-8')
    reload_code = bytecode('emi', 'dev.emi.emi.runtime.EmiReloadManager$ReloadWorker')
    lines = reload_code.splitlines()
    gate = []
    for i, line in enumerate(lines):
        if '// String jei' in line:
            window = lines[i:i + 16]
            if any('EmiAgnos.isModLoaded' in x for x in window) and any('new' in x and 'JemiPlugin' in x for x in window):
                gate = [x.strip() for x in window]
                break
    if not gate:
        raise ValueError('Expected pinned EMI JEMI/JEI gate not found; re-audit native loader')
    au_code = bytecode('almostunified', 'com.almostreliable.unified.compat.viewer.AlmostEMI')
    if 'removeEmiStacks' not in au_code or 'disableStack' not in au_code:
        raise ValueError('Expected AU native EMI hiding path missing')
    config = json.loads((mc / 'config/almostunified/unification/materials.json').read_text(encoding='utf-8'))
    source_config = json.loads((ROOT / 'config/almostunified/unification/materials.json').read_text(encoding='utf-8'))
    result = {'scope': 'OFFLINE_PINNED_BYTECODE_RESOURCE_AUDIT_NOT_LIVE_REGISTRY',
              'installed_source': json.loads((mc.parent / 'BUILD-MANIFEST.json').read_text())['integration_commit'],
              'pins': pins, 'top_level_jars_scanned': len(paths),
              'jei_jar_present': any('jei' in p.name.lower() for p in paths),
              'provider_candidates': inventory,
              'jawr': {'jei_category_classes': categories, 'resource_recipe_count': len(recipes),
                       'resource_recipe_types': dict(sorted(types.items())),
                       'recipe_resources': recipes, 'native_emi_api_references': native_emi_api_references},
              'emisemble_witchery_reference_classes': witchery_bridge_candidates,
              'emi_jemi_gate': gate,
              'almost_unified': {'installed_config_equals_source': config == source_config,
                                'native_emi_hiding_present': True, 'config': config},
              'limits': ['Annotation candidates can be gated, inactive or duplicated; not live category coverage.',
                         'Native resource JSON counts are not an effective RecipeManager census.',
                         'No UI, live recipe query, crafting or consumer-parity test performed.']}
    (args.output / 'audit.json').write_text(json.dumps(result, indent=2) + '\n', encoding='utf-8')
    print(json.dumps({'pins_verified': len(pins), 'jars_scanned': len(paths),
                      'provider_containers': len(inventory), 'jawr_categories': len(categories),
                      'jawr_emi_references': len(native_emi_api_references),
                      'emisemble_witchery_references': len(witchery_bridge_candidates)}))


if __name__ == '__main__':
    main()
