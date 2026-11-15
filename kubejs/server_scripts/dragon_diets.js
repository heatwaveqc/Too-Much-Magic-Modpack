// Too Much Magic | Dragon Survival 2.0.74 | Minecraft 1.21.1 NeoForge.
// Species rules: SEA = aquatic fish/plant raw foods; FOREST = raw fruit/vegetable,
// simple preserves and sugar (no grains); CAVE = native foods and charcoal-charred
// meat/seafood. Native cooked foods remain inedible to dragons as meals.
// No global changes to item food properties or common c: tags.
const TMM_GRAIN_WORDS = /(amaranth|barley|buckwheat|corn|millet|oats?|quinoa|rice|rye|sorghum|spelt|teff|wheat|flour|noodle|pasta|bread|toast|tortilla|cereal|popcorn|pancake|waffle|biscuit|cookie|cake|pastry|dumpling|crouton)/i;
const TMM_COOKED_WORDS = /(?:^|_)(?:cooked|baked|fried|roasted|grilled|steamed|boiled|smoked)(?:_|$)/i;
const TMM_SEA_PLANTS = /(kelp|seaweed|seagrass|algae)/i;
const TMM_SPECIAL_FRUIT = new Set([
  'minecraft:chorus_fruit', 'minecraft:sweet_berries', 'minecraft:glow_berries',
  'minecraft:golden_apple', 'minecraft:enchanted_golden_apple',
  'minecraft:golden_carrot'
]);

function tmmForestProduceAllowed(id) {
  const name = String(id);
  const path = name.split(':').slice(1).join(':');
  return !TMM_GRAIN_WORDS.test(path) && !TMM_COOKED_WORDS.test(path) &&
    !TMM_SEA_PLANTS.test(path) && !TMM_SPECIAL_FRUIT.has(name);
}

ServerEvents.tags('item', event => {
  if (!Platform.isLoaded('dragonsurvival')) return;

  // Materialize the *actual* common food tags, rather than blindly including
  // a category tag which could contain corn or grain dishes from other mods.
  for (const sourceTag of ['c:foods/fruit', 'c:foods/vegetable']) {
    for (const value of event.get(sourceTag).getObjectIds()) {
      const id = String(value);
      if (tmmForestProduceAllowed(id)) {
        event.add('tmm:dragon_food/forest_produce', id);
      }
    }
  }

  // Known vanilla/FD produce; covers cases not properly classified by source tags.
  for (const id of [
    'minecraft:apple', 'minecraft:melon_slice', 'minecraft:carrot',
    'minecraft:beetroot', 'minecraft:potato', 'minecraft:pumpkin',
    'minecraft:sugar_cane', 'farmersdelight:cabbage',
    'farmersdelight:cabbage_leaf', 'farmersdelight:onion',
    'farmersdelight:tomato', 'farmersdelight:mixed_salad',
    'farmersdelight:fruit_salad', 'farmersdelight:nether_salad'
  ]) {
    if (tmmForestProduceAllowed(id)) event.add('tmm:dragon_food/forest_produce', id);
  }

  // Charcoal-assisted fallback makes all properly tagged raw meat/fish eligible
  // for Cave Dragon charring even if another mod omits a cooked intermediate.
  event.add('tmm:dragon_food/charrable_meat', '#c:foods/raw_meat');
  for (const id of [
    'farmersdelight:chicken_cuts', 'farmersdelight:bacon',
    'farmersdelight:ham', 'farmersdelight:minced_beef',
    'farmersdelight:mutton_chops'
  ]) event.add('tmm:dragon_food/charrable_meat', id);

  event.add('tmm:dragon_food/charrable_seafood', '#c:foods/raw_fish');
  for (const id of [
    'aquaculture:fish_fillet_raw', 'aquaculture:goldfish',
    'aquaculture:minnow', 'aquaculture:jellyfish'
  ]) event.add('tmm:dragon_food/charrable_seafood', id);
});

ServerEvents.recipes(event => {
  if (!Platform.isLoaded('dragonsurvival')) return;

  // A coal/charcoal is consumed to char uncooked meat or fish. This adds a
  // universal fallback without colliding with native smelting/smoking recipes:
  // cooked meat/fish -> charred meat/seafood remains available unchanged.
  event.shapeless(
    'dragonsurvival:charred_meat',
    ['#tmm:dragon_food/charrable_meat', '#minecraft:coals']
  ).id('tmm:dragon_food/charred_meat_with_coal');

  event.shapeless(
    'dragonsurvival:charred_seafood',
    ['#tmm:dragon_food/charrable_seafood', '#minecraft:coals']
  ).id('tmm:dragon_food/charred_seafood_with_coal');
});
