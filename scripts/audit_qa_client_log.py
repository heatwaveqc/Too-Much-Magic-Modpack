"""Summarize QA client failures without exporting raw logs, player names or locations.

Read-only: python scripts/audit_qa_client_log.py --log <latest.log> --output <summary.json>
Counts describe this file snapshot, not a complete session or gameplay acceptance.
"""
import argparse
import collections
import hashlib
import json
import re
from pathlib import Path


def summarize(raw):
    lines = raw.decode('utf-8', errors='replace').splitlines()
    recipes = collections.Counter()
    recipe_reasons = {}
    scripts = collections.Counter()
    plugins = collections.Counter()
    plugin_causes = {}
    mapping = collections.Counter()
    loads = []
    for index, line in enumerate(lines):
        found = re.search(r'Parsing error loading recipe ([a-z0-9_.-]+:[a-z0-9_./-]+): (.*)', line)
        if found:
            recipes[found[1]] += 1
            recipe_reasons[found[1]] = found[2][:1400]
        found = re.search(r'\[KubeJS Server/\]: ([a-zA-Z0-9_./-]+\.js)#(\d+): (.*)', line)
        if found and '/ERROR]' in line:
            scripts[(found[1], found[2], found[3])] += 1
        found = re.search(r'Loaded (\d+)/(\d+) KubeJS server scripts .*?with (\d+) errors', line)
        if found:
            loads.append(dict(loaded=int(found[1]), total=int(found[2]), errors=int(found[3])))
        found = re.search(r'Exception loading plugin provided by ([a-z0-9_]+)', line)
        if found:
            plugins[found[1]] += 1
            following = lines[index + 1] if index + 1 < len(lines) else ''
            cause = re.fullmatch(r'(?:java\.lang\.)?(NoClassDefFoundError|ClassNotFoundException): ([a-zA-Z0-9_.$/]+)', following.strip())
            if cause:
                plugin_causes[found[1]] = {'exception': cause[1], 'class': cause[2]}
        found = re.search(r'Error mapping recipe ([a-z0-9_.-]+:[a-z0-9_./-]+)\.', line)
        if found:
            mapping[found[1]] += 1
    return dict(scope='READ_ONLY_LOG_SNAPSHOT_NOT_GAMEPLAY_ACCEPTANCE',
        log_sha256=hashlib.sha256(raw).hexdigest(), bytes=len(raw), lines=len(lines),
        invalid_unbound_key_lines=sum('Invalid key -1' in line for line in lines),
        mixin_fatal_lines=sum(any(term in line for term in ('InvalidInjectionException', 'MixinApplyError', 'MixinTransformerError')) for line in lines),
        integrated_world_login_observed=any('logged in with entity id' in line for line in lines),
        loaded_recipe_counts=[int(m[1]) for line in lines if (m := re.search(r'\[net.minecraft.world.item.crafting.RecipeManager/\]: Loaded (\d+) recipes', line))],
        server_script_loads=loads,
        script_failures=[dict(script=s, line=int(n), message=msg, occurrences=count)
                         for (s, n, msg), count in sorted(scripts.items())],
        recipe_failures=[dict(id=k, occurrences=v, reason=recipe_reasons[k]) for k, v in sorted(recipes.items())],
        recipe_failure_namespaces=dict(collections.Counter(k.split(':')[0] for k in recipes)),
        emi_plugin_failures=[dict(plugin=k, occurrences=v, cause=plugin_causes.get(k)) for k, v in sorted(plugins.items())],
        projecte_mapping_failures=dict(mapping))


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--log', type=Path, required=True)
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    report = summarize(args.log.read_bytes())
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(json.dumps(report, indent=2) + '\n', encoding='utf-8')
    print(json.dumps({k: report[k] for k in ('invalid_unbound_key_lines', 'server_script_loads', 'recipe_failure_namespaces', 'emi_plugin_failures', 'projecte_mapping_failures')}))


if __name__ == '__main__':
    main()
