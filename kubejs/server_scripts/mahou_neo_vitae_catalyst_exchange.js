// Too Much Magic — Neo Vitae dust input for Mahou Tsukai Catalyst Exchange.
// Pinned target: Minecraft 1.21.1, Mahou Tsukai 1.36.31, KubeJS 2101.
// EXPERIMENTAL, needs disposable-world validation before any release.
//
// Verified from the pinned Mahou bytecode:
// CatalystExchangeMahoujinTileEntity.checkForItemClass() checks specifically
// for PowderedCatalyst.class; ifItemFoundAbove() charges native mana, spawns
// one native random powder per paid input, shrinks the original ItemEntity,
// and discards it at count zero. Never duplicate that economy in KubeJS.
//
// We present Neo dust as its Mahou counterpart ONLY during a level tick,
// then restore any remaining input (including all data components). Native
// Mahou alone decides whether to spend mana or produce an output.

const TMM_CEX_MAPPINGS = Object.freeze({
  'neovitae:iron_dust': 'mahoutsukai:powdered_iron',
  'neovitae:gold_dust': 'mahoutsukai:powdered_gold',
  'neovitae:diamond_dust': 'mahoutsukai:powdered_diamond',
  'neovitae:emerald_dust': 'mahoutsukai:powdered_emerald',
  'neovitae:quartz_dust': 'mahoutsukai:powdered_quartz'
})
const TMM_CEX_TARGET = 'mahoutsukai:mahoujin_catalyst_exchange'
const TMM_CEX_REGISTRY = Java.loadClass('net.minecraft.core.registries.BuiltInRegistries')
const TMM_CEX_ITEM_ENTITY = Java.loadClass('net.minecraft.world.entity.item.ItemEntity')

// Track only the five Neo Vitae dust item entities, not all world items.
// Entity tick rediscovery also covers items present across a KubeJS /reload.
const TMM_CEX_CANDIDATES = new Map()
const TMM_CEX_ACTIVE = new Map()
let tmmCexErrorLogged = false
let tmmCexConversionsLogged = 0

function tmmCexItemId(stack) {
  return String(TMM_CEX_REGISTRY.ITEM.getKey(stack.getItem()))
}

// KubeJS remaps Minecraft Entity.getUUID() to Entity.getUuid() in Rhino.
// Never use the Java method name here: it is deliberately hidden from JS.

// Minecraft Entity.level is exposed by Rhino as an object/property, not a
// callable method. Using entity.level() crashed immediately when a starter
// Akashic Tome was spawned into a brand-new world (2026-11-14).
// Level.isClientSide may also resolve to a field instead of a method.
function tmmCexEntityLevel(entity) {
  return entity.level
}
function tmmCexIsClientLevel(level) {
  const flag = level.isClientSide
  if (typeof flag === 'function') return String(flag.call(level)) === 'true'
  // A Java Boolean wrapper for false is still truthy as a JS object.
  // Compare its value, not the wrapper object's truthiness.
  return String(flag) === 'true'
}

function tmmCexTrack(entity) {
  if (!(entity instanceof TMM_CEX_ITEM_ENTITY)) return
  if (tmmCexIsClientLevel(tmmCexEntityLevel(entity))) return
  const uuid = String(entity.getUuid())
  if (entity.isRemoved() || entity.getItem().isEmpty()) {
    TMM_CEX_CANDIDATES.delete(uuid)
    return
  }
  if (TMM_CEX_MAPPINGS[tmmCexItemId(entity.getItem())]) {
    TMM_CEX_CANDIDATES.set(uuid, entity)
  } else if (!TMM_CEX_ACTIVE.has(uuid)) {
    TMM_CEX_CANDIDATES.delete(uuid)
  }
}

function tmmCexNearExchange(level, entity) {
  // Broader than the native AboveChecker pickup box, intentionally:
  // outside its box Mahou won't consume the temporary powder, and Post
  // restores it at identical count. Only actual Catalyst Exchange blocks
  // may trigger a substitution. Never force chunks to load during the scan.
  const pos = entity.blockPosition()
  for (let dy = 0; dy <= 2; dy++) {
    for (let dx = -1; dx <= 1; dx++) {
      for (let dz = -1; dz <= 1; dz++) {
        const candidate = pos.offset(dx, -dy, dz)
        if (!level.hasChunkAt(candidate)) continue
        const block = level.getBlockState(candidate).getBlock()
        if (String(TMM_CEX_REGISTRY.BLOCK.getKey(block)) === TMM_CEX_TARGET) {
          return true
        }
      }
    }
  }
  return false
}

function tmmCexError(error) {
  if (tmmCexErrorLogged) return
  tmmCexErrorLogged = true
  console.error('[TMM Catalyst Exchange] Native dust bridge error; check KubeJS server log: ' + error)
}

function tmmCexRestore(uuid, state) {
  const entity = state.entity
  try {
    if (!entity.isRemoved()) {
      const current = entity.getItem()
      const remain = current.getCount()
      if (remain <= 0) {
        entity.discard()
      } else if (tmmCexItemId(current) === state.mahouItem) {
        // The pinned Mahou implementation shrinks THIS entity in place.
        // Newly generated random catalysts are distinct ItemEntities.
        const restore = state.original.copy()
        if (remain > state.initialCount) {
          tmmCexError('Unexpected merged stack during temporary catalyst substitution')
        }
        // Never silently delete surplus items even if another mod merged
        // into the temporary stack before its tick was cancelled.
        restore.setCount(remain)
        entity.setItem(restore)
        const converted = state.initialCount - remain
        if (converted > 0 && tmmCexConversionsLogged < 5) {
          console.info('[TMM Catalyst Exchange] Native exchange consumed ' + converted
            + ' Neo dust(s); restored ' + remain + ' unconverted dust(s)')
          tmmCexConversionsLogged++
        }
      } else {
        // Another mod changed this entity mid-tick. Do NOT overwrite it or
        // claim a conversion; report the anomaly for manual investigation.
        tmmCexError('Unexpected ItemEntity type during restore: ' + tmmCexItemId(current))
      }
    }
  } catch (error) {
    tmmCexError(error)
  } finally {
    TMM_CEX_ACTIVE.delete(uuid)
    if (entity.isRemoved() || entity.getItem().isEmpty()) {
      TMM_CEX_CANDIDATES.delete(uuid)
    }
  }
}

EntityEvents.spawned('minecraft:item', event => {
  tmmCexTrack(event.entity)
})

// Discovery and safety: temporary native powders must not move, age, merge,
// or be picked up via their own ItemEntity tick before LevelTick.Post restores
// them. They remain accessible to Mahou's independent above-block scanner.
NativeEvents.onEvent('net.neoforged.neoforge.event.tick.EntityTickEvent$Pre', event => {
  const entity = event.getEntity()
  if (!(entity instanceof TMM_CEX_ITEM_ENTITY) || tmmCexIsClientLevel(tmmCexEntityLevel(entity))) return
  const uuid = String(entity.getUuid())
  if (TMM_CEX_ACTIVE.has(uuid)) {
    event.setCanceled(true)
  } else {
    tmmCexTrack(entity)
  }
})

// The Pre/Post pair brackets native block entity ticking. The proxy lives for
// one level tick only, not until the next mana cycle and not across a restart.
NativeEvents.onEvent('net.neoforged.neoforge.event.tick.LevelTickEvent$Pre', event => {
  const level = event.getLevel()
  if (tmmCexIsClientLevel(level)) return

  for (const [uuid, entity] of TMM_CEX_CANDIDATES) {
    if (entity.isRemoved() || entity.getItem().isEmpty()) {
      TMM_CEX_CANDIDATES.delete(uuid)
      continue
    }
    if (!tmmCexEntityLevel(entity).equals(level) || TMM_CEX_ACTIVE.has(uuid)) continue
    const stack = entity.getItem()
    const mahouItem = TMM_CEX_MAPPINGS[tmmCexItemId(stack)]
    if (!mahouItem) {
      TMM_CEX_CANDIDATES.delete(uuid)
      continue
    }
    if (!tmmCexNearExchange(level, entity)) continue

    const original = stack.copy()
    const state = {
      entity: entity,
      original: original,
      initialCount: original.getCount(),
      mahouItem: mahouItem
    }
    try {
      const substitute = Item.of(mahouItem, original.getCount())
      entity.setItem(substitute)
      TMM_CEX_ACTIVE.set(uuid, state)
    } catch (error) {
      tmmCexError(error)
      if (!entity.isRemoved()) entity.setItem(original)
    }
  }
})

NativeEvents.onEvent('net.neoforged.neoforge.event.tick.LevelTickEvent$Post', event => {
  const level = event.getLevel()
  if (tmmCexIsClientLevel(level)) return
  for (const [uuid, state] of TMM_CEX_ACTIVE) {
    if (tmmCexEntityLevel(state.entity).equals(level)) tmmCexRestore(uuid, state)
  }
})

console.info('[TMM Catalyst Exchange] Registered Neo Vitae dust exchange proxy; native Mahou mana logic retained')
