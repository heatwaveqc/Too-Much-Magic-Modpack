// #328: Only clean newly generated chest loot. Never touch player inventory,
// configured Ars gear/books, deliberate enchantment recipes, or existing loot.
// Pinned Rhino rejects conditional const declarations; use isolated mutable bindings.
;(function () {
  if (Platform.isLoaded('ars_nouveau') && Platform.isLoaded('lootjs')) {
    let TMMReactiveComponents = Java.loadClass('net.minecraft.core.component.DataComponents')
    let TMMReactiveRegistries = Java.loadClass('net.minecraft.core.registries.Registries')
    let TMMReactiveBuiltins = Java.loadClass('net.minecraft.core.registries.BuiltInRegistries')
    let TMMReactiveLocation = Java.loadClass('net.minecraft.resources.ResourceLocation')
    let TMMReactiveTag = Java.loadClass('net.minecraft.tags.TagKey')
    let TMMReactiveEnchant = Java.loadClass('net.minecraft.world.item.enchantment.Enchantment')
    let TMMReactiveMutable = Java.loadClass('net.minecraft.world.item.enchantment.ItemEnchantments$Mutable')
    let TMMReactiveItems = Java.loadClass('net.minecraft.world.item.Items')
    let TMMReactiveId = TMMReactiveLocation.parse('ars_nouveau:reactive')
    let TMMReactiveCasterId = TMMReactiveLocation.parse('ars_nouveau:reactive_caster')
    let TMMReactivePool = TMMReactiveTag.create(TMMReactiveRegistries.ENCHANTMENT,
      TMMReactiveLocation.parse('minecraft:on_random_loot'))

    LootJS.modifiers(event => {
      let warnedEmptyPool = false
      event.addTableModifier(LootType.CHEST).name('tmm:unconfigured_reactive_books')
        .customAction((context, loot) => {
          // Lookup the live reloadable registry/tag at roll time; don't cache holders.
          let registry = context.getLevel().registryAccess().registryOrThrow(TMMReactiveRegistries.ENCHANTMENT)
          let caster = TMMReactiveBuiltins.DATA_COMPONENT_TYPE.get(TMMReactiveCasterId)
          let it = loot.iterator()
          while (it.hasNext()) {
            let stack = it.next()
            if (!stack.is(TMMReactiveItems.ENCHANTED_BOOK)) continue
            // If the optional component cannot be resolved, fail closed.
            if (caster == null || stack.has(caster)) continue
            let stored = stack.get(TMMReactiveComponents.STORED_ENCHANTMENTS)
            if (stored == null) continue
            let bad = null
            let retained = []
            let old = stored.keySet().iterator()
            while (old.hasNext()) {
              let holder = old.next()
              if (holder.is(TMMReactiveId)) bad = holder
              else retained.push(holder)
            }
            if (bad == null) continue

            let candidates = []
            let pool = registry.getTagOrEmpty(TMMReactivePool).iterator()
            while (pool.hasNext()) {
              let candidate = pool.next()
              if (candidate.is(TMMReactiveId) || stored.getLevel(candidate) > 0) continue
              let compatible = true
              for (let other of retained) {
                if (!TMMReactiveEnchant.areCompatible(candidate, other)) {
                  compatible = false
                  break
                }
              }
              if (compatible && candidate.value().getMaxLevel() >= candidate.value().getMinLevel()) {
                candidates.push(candidate)
              }
            }

            // Preserve the loot roll if a broken/empty pack tag would leave a blank book.
            // A mixed book with no compatible replacement can still retain its valid entries.
            if (candidates.length === 0 && retained.length === 0) {
              if (!warnedEmptyPool) {
                console.warn('[TMM Reactive] No eligible random-loot enchantment; leaving single-entry book unchanged')
                warnedEmptyPool = true
              }
              continue
            }
            let edited = new TMMReactiveMutable(stored)
            edited.set(bad, 0)
            if (candidates.length > 0) {
              let random = context.getRandom()
              let chosen = candidates[random.nextInt(candidates.length)]
              let min = chosen.value().getMinLevel()
              let max = chosen.value().getMaxLevel()
              edited.set(chosen, min + random.nextInt(max - min + 1))
            }
            // Mutate only the stored-enchantment component on a copy of the original.
            let result = stack.copy()
            result.set(TMMReactiveComponents.STORED_ENCHANTMENTS, edited.toImmutable())
            it.set(result)
          }
        })
    })
  }
})();
