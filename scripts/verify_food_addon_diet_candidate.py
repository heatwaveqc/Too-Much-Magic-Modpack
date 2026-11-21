#!/usr/bin/env python3
"""Static guardrails for the 2026-11-21 UNINDEXED food addon diet candidate.

This verifies authored configuration, not the expanded in-game item tags
or the actual KubeJS/NeoForge runtime. Use JEI and a disposable world for QA.
"""
from __future__ import annotations
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
MAP = ROOT / 'docs/qa/candidates/dragon-diet-entries-2026-11-21.json'
SCRIPT = ROOT / 'kubejs/server_scripts/dragon_diets.js'
SPECIES = ('cave_dragon', 'forest_dragon', 'sea_dragon')


def main() -> None:
    data = json.loads(MAP.read_text(encoding='utf-8'))
    assert data.get('replace') is True, 'This file must explicitly replace the upstream duplicate-path resource'
    assert set(data['values']) == {f'dragonsurvival:{s}' for s in SPECIES}
    lists = {s: data['values'][f'dragonsurvival:{s}']['neoforge:value'] for s in SPECIES}
    by_item = {s: {entry['items']: entry for entry in lists[s]} for s in SPECIES}
    for s in SPECIES:
        assert all(entry.get('items') for entry in lists[s])
        assert len(by_item[s]) == len(lists[s]), f'{s}: duplicate selector'
        assert len(lists[s]) > 20, f'{s}: lost upstream diet'
        assert data['values'][f'dragonsurvival:{s}'].get('neoforge:conditions'), f'{s}: conditional registry guard missing'

    sea = by_item['sea_dragon']
    forest = by_item['forest_dragon']
    cave = by_item['cave_dragon']
    for item in ('#c:foods/raw_fish', 'aquaculture:fish_fillet_raw',
                 'aquaculture:sushi', 'minecraft:seagrass', 'minecraft:dried_kelp'):
        assert item in sea, f'Sea Dragon missing {item}'
    for item in ('aquaculture:fish_fillet_cooked', '#c:foods/cooked_fish',
                 'pamhc2foodextended:rawtofishitem', 'pneumaticcraft:raw_salmon_tempura'):
        assert item not in sea, f'Sea Dragon forbidden food: {item}'
    assert sea['#c:foods/raw_fish']['properties']['saturation'] == 1.0
    assert sea['aquaculture:fish_fillet_raw']['properties']['saturation'] == 2.0
    assert sea['aquaculture:sushi']['properties']['saturation'] == 5.0

    assert '#tmm:dragon_food/forest_produce' in forest
    assert 'minecraft:sugar' in forest
    # Ars Nouveau Sourceberries are registered as an edible block item rather
    # than a conventional berry item; do not depend on a common fruit tag.
    assert 'ars_nouveau:sourceberry_bush' in forest
    # This is the vanilla golden carrot, not an unverified modded enchanted carrot.
    assert 'minecraft:golden_carrot' in forest
    assert 'aquaculture:turtle_soup' not in forest
    assert 'minecolonies:chorus_bread' not in forest
    assert '#c:foods/cooked_meat' not in forest
    assert 'dragonsurvival:charred_meat' in cave
    assert 'dragonsurvival:charred_seafood' in cave
    assert 'create:blaze_cake' not in cave
    assert 'nethersdelight:nether_skewer' not in cave
    assert cave['dragonsurvival:charred_seafood']['properties'] == {'nutrition': 7, 'saturation': 11.0}

    # Explicit magical nourishment only; no broad effect-derived or name-based tags.
    magical_allowlist = (
        'evilcraft:flesh_rejuvenated',
        'botania:infinite_fruit',
        'botania:mana_cookie',
        'spectrum:enchanted_star_candy',
        'spectrum:magnificent_star_candy',
        'spectrum:enchanted_golden_carrot',
    )
    for species, entries in by_item.items():
        for item in magical_allowlist:
            assert item in entries, f'{species} missing magical food: {item}'
            assert entries[item]['retain_effects'] is True, (species, item)
            if item in ('evilcraft:flesh_rejuvenated', 'botania:infinite_fruit'):
                # The two reusable items use custom on-use feeding: avoid free
                # extra food points in the Dragon Survival diet data map.
                assert entries[item]['properties']['nutrition'] == 0
                assert entries[item]['properties']['saturation'] == 0
        for forbidden in ('artifacts:eternal_steak', 'spectrum:star_candy'):
            assert forbidden not in entries, (species, forbidden)
    assert 'artifacts:everlasting_beef' in cave
    assert 'artifacts:everlasting_beef' not in forest
    assert 'artifacts:everlasting_beef' not in sea
    assert 'minecraft:rotten_flesh' not in forest
    assert 'born_in_chaos_v1:monster_flesh' not in forest
    assert forest['ars_nouveau:sourceberry_bush'] == {'items': 'ars_nouveau:sourceberry_bush', 'retain_effects': True}

    # 2026-11-21 source-only diet reconciliation: all new addon selectors are
    # actual published item IDs, not guesses, name-prefix expansion, or cooked
    # dishes inferred from a mod-specific broad 'monster_foods' tag.
    cave_addon_raw = (
        'dungeonsdelight:silverfish_abdomen',
        'dungeonsdelight:ghast_calamari',
        'dungeonsdelight:ghast_tentacle',
        'dungeonsdelight:spider_meat',
        'dungeonsdelight:rotten_tripe',
        'dungeonsdelight:brined_flesh',
        'dungeonsdelight:gritty_flesh',
        'dungeonsdelight:bogged_brain',
        'dungeonsdelight:sniffer_shank',
        'endersdelight:mite_crust',
        'endersdelight:shulker_mollusk',
        'endersdelight:shulker_filet',
    )
    for item in cave_addon_raw:
        assert item in cave, f'Cave Dragon missing verified raw animal food {item}'
        assert cave[item]['retain_effects'] is True, item
        assert item not in forest, f'Forest Dragon wrongly accepts animal food {item}'
        assert item not in sea, f'Sea Dragon wrongly accepts land/End monster {item}'
    for item in (
        'dungeonsdelight:rotgourd_slice', 'endersdelight:chorus_juice',
    ):
        assert item in forest, f'Forest Dragon missing source-backed plant food {item}'
        assert forest[item]['retain_effects'] is True, item
        assert item not in cave and item not in sea, item

    # Forest's upstream map included 75 animal/raw-meat selectors contrary to
    # TMM's approved no-meat Forest physiology. Enforce correction instead of
    # allowing later broad tags to silently reintroduce them.
    assert '#c:foods/raw_meat' not in forest
    for item in (
        'minecraft:rabbit', 'minecraft:spider_eye',
        'farmersdelight:chicken_cuts', 'farmersdelight:bacon',
        'twilightforest:raw_venison', 'netherdepthsupgrade:wither_bonefish',
        'dragonsurvival:meat_chorus_mix', 'dragonsurvival:sweet_sour_rabbit',
        'born_in_chaos_v1:corpse_maggot',
    ):
        assert item not in forest, f'Forest Dragon retains meat selector {item}'
    for item in (
        'dungeonsdelight:monster_foods', '#dungeonsdelight:monster_foods',
        'dungeonsdelight:smoked_spider_meat',
        'dungeonsdelight:fried_ghast_calamari',
        'dungeonsdelight:silverfish_fried_rice',
        'dungeonsdelight:monster_burger',
        'endersdelight:uncanny_cookies',
        'endersdelight:pearl_pasta',
        'endersdelight:chorus_pie_slice',
        'crispy:fortune_cookie',
    ):
        assert all(item not in entries for entries in (cave, forest, sea)), item
    for raw_tag in ('#c:foods/raw_meat', '#c:foods/raw_fish'):
        assert raw_tag in cave, f'Cave Dragon missing approved raw diet {raw_tag}'
        assert cave[raw_tag]['retain_effects'] is True
    assert '#c:foods/cooked_meat' not in cave
    assert '#c:foods/cooked_fish' not in sea

    # TeamLapen's Werewolves food tags are intentionally retired.
    # JAWR lycanthropes do not use that removed tag namespace.
    assert not (ROOT / 'kubejs/data/werewolves').exists()

    # Verify regex selectors cover examples but do not admit baked/grain products.
    selectors = [x for x in forest if x.startswith('pamhc2')]
    assert any(re.fullmatch(x.split(':', 1)[1], 'apricotjellyitem') for x in selectors if x.startswith('pamhc2foodextended:'))
    assert not any(re.fullmatch(x.split(':', 1)[1], 'applejellysandwichitem') for x in selectors if x.startswith('pamhc2foodextended:'))
    assert not any(re.fullmatch(x.split(':', 1)[1], 'roastedalmonditem') for x in selectors if x.startswith('pamhc2trees:'))

    js = SCRIPT.read_text(encoding='utf-8')
    for token in ("ServerEvents.tags('item'", "Platform.isLoaded('dragonsurvival')",
                  "'farmersdelight:cabbage'", 'TMM_GRAIN_WORDS',
                  "'dragonsurvival:cooked_mushrooms'", 'event.remove(sourceTag, id)'):
        assert token in js, f'Missing implementation guard: {token}'
    for disallowed in ('ServerEvents.recipes(', 'charred_meat_with_coal',
                       'charred_seafood_with_coal', 'charrable_meat', 'charrable_seafood'):
        assert disallowed not in js, f'Unexpected Cave Dragon recipe shortcut: {disallowed}'
    print('PASS: unindexed food addon candidate selectors, forest meat exclusions, native charring')
    print('NOT PROMOTED: active Packwiz map remains unchanged; no full runtime/client QA')


if __name__ == '__main__':
    main()
