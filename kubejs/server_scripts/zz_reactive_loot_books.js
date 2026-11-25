// #328: Only clean newly generated chest loot. Never touch player inventory,
// configured Ars gear/books, deliberate enchantment recipes, or existing loot.
// Pinned Rhino rejects top-level conditional const declarations; isolate the scope.
;(function () {
  if (Platform.isLoaded('ars_nouveau') && Platform.isLoaded('lootjs')) {
    const TMMReactiveComponents = Java.loadClass('net.minecraft.core.component.DataComponents')
    const TMMReactiveRegistries = Java.loadClass('net.minecraft.core.registries.Registries')
    const TMMReactiveBuiltins = Java.loadClass('net.minecraft.core.registries.BuiltInRegistries')
    const TMMReactiveLocation = Java.loadClass('net.minecraft.resources.ResourceLocation')
    const TMMReactiveTag = Java.loadClass('net.minecraft.tags.TagKey')
    const TMMReactiveEnchant = Java.loadClass('net.minecraft.world.item.enchantment.Enchantment')
    const TMMReactiveMutable = Java.loadClass('net.minecraft.world.item.enchantment.ItemEnchantments$Mutable')
    const TMMReactiveItems = Java.loadClass('net.minecraft.world.item.Items')
    const TMMReactiveId = TMMReactiveLocation.parse('ars_nouveau:reactive')
    const TMMReactiveCasterId = TMMReactiveLocation.parse('ars_nouveau:reactive_caster')
    const TMMReactivePool = TMMReactiveTag.create(TMMReactiveRegistries.ENCHANTMENT,
      TMMReactiveLocation.parse('minecraft:on_random_loot'))

    LootJS.modifiers(event => {
      let warnedEmptyPool = false
      event.addTableModifier(LootType.CHEST).name('tmm:unconfigured_reactive_books')
        .customAction((context, loot) => {
          // Lookup the live reloadable registry/tag at roll time; don't cache holders.
          const registry = context.getLevel().registryAccess().registryOrThrow(TMMReactiveRegistries.ENCHANTMENT)
          const caster = TMMReactiveBuiltins.DATA_COMPONENT_TYPE.get(TMMReactiveCasterId)
          const it = loot.iterator()
          while (it.hasNext()) {
            const stack = it.next()
            if (!stack.is(TMMReactiveItems.ENCHANTED_BOOK)) continue
            // If the optional component cannot be resolved, fail closed.
            if (caster == null || stack.has(caster)) continue
            const stored = stack.get(TMMReactiveComponents.STORED_ENCHANTMENTS)
            if (stored == null) continue
            let bad = null
            const retained = []
            const old = stored.keySet().iterator()
            while (old.hasNext()) {
              const holder = old.next()
              if (holder.is(TMMReactiveId)) bad = holder
              else retained.push(holder)
            }
            if (bad == null) continue

            const candidates = []
            const pool = registry.getTagOrEmpty(TMMReactivePool).iterator()
            while (pool.hasNext()) {
              const candidate = pool.next()
              if (candidate.is(TMMReactiveId) || stored.getLevel(candidate) > 0) continue
              let compatible = true
              for (const other of retained) {
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
            const edited = new TMMReactiveMutable(stored)
            edited.set(bad, 0)
            if (candidates.length > 0) {
              const random = context.getRandom()
              const chosen = candidates[random.nextInt(candidates.length)]
              const min = chosen.value().getMinLevel()
              const max = chosen.value().getMaxLevel()
              edited.set(chosen, min + random.nextInt(max - min + 1))
            }
            // Mutate only the stored-enchantment component on a copy of the original.
            const result = stack.copy()
            result.set(TMMReactiveComponents.STORED_ENCHANTMENTS, edited.toImmutable())
            it.set(result)
          }
        })
    })
  }
})();
