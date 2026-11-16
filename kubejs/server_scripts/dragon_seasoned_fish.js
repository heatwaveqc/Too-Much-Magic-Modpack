// TMM Dragon Survival 2.0.74 / Minecraft 1.21.1 / KubeJS.
// Seasoned Fish is a separate food from Sea Dragon Treat. The upstream
// shapeless recipe uses raw salmon + ink sac + kelp; keep its recipe ID,
// remaining ingredients, result and count while broadening only the fish.
// Changing the sea dragon diet data map alone cannot change crafting recipes.
ServerEvents.recipes(event => {
  if (!Platform.isLoaded('dragonsurvival')) return;

  event.replaceInput(
    { type: 'minecraft:crafting_shapeless', output: 'dragonsurvival:seasoned_fish' },
    'minecraft:salmon',
    Ingredient.of('#c:foods/raw_fish')
  );
});
