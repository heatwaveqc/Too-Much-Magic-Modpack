// Too Much Magic — Neo Vitae dust -> Mahou Tsukai blood-circle catalysts.
// EXPERIMENTAL: intended for the dedicated-server + client-world test build.
// Deliberately invokes Mahou's own block interaction, not a duplicate spell
// implementation. This handles right-clicks on a blank mahoujin only;
// Catalyst Exchange's dropped item entities are a separate interaction path.

const TMM_NEO_TO_MAHOU = Object.freeze({
  'neovitae:iron_dust': 'mahoutsukai:powdered_iron',
  'neovitae:gold_dust': 'mahoutsukai:powdered_gold',
  'neovitae:diamond_dust': 'mahoutsukai:powdered_diamond',
  'neovitae:emerald_dust': 'mahoutsukai:powdered_emerald',
  'neovitae:quartz_dust': 'mahoutsukai:powdered_quartz'
})

const TMM_BUILTIN_REGISTRIES = Java.loadClass('net.minecraft.core.registries.BuiltInRegistries')
let tmmMahouBridgeErrorLogged = false

// NeoForge 1.21.1's BLOCK phase occurs just before native block item use.
// This event also lets us return the actual ItemInteractionResult. An inner
// BLOCK event using the Mahou powder will not recurse into this mapping.
NativeEvents.onEvent('net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent', event => {
  if (String(event.getUsePhase()) !== 'BLOCK') return
  const level = event.getLevel()
  if (level.isClientSide()) return

  const player = event.getPlayer()
  if (player == null) return

  const input = event.getItemStack()
  if (input.isEmpty()) return
  const inputId = String(TMM_BUILTIN_REGISTRIES.ITEM.getKey(input.getItem()))
  const catalystId = TMM_NEO_TO_MAHOU[inputId]
  if (!catalystId) return

  const blockState = level.getBlockState(event.getPos())
  const blockId = String(TMM_BUILTIN_REGISTRIES.BLOCK.getKey(blockState.getBlock()))
  if (blockId !== 'mahoutsukai:mahoujin') return

  const hand = event.getHand()
  const originalHeld = player.getItemInHand(hand)
  // Pass a single temporary registered Mahou powder to Mahou's own logic.
  // Restore the original stack and all data components even if it fails.
  const fakeCatalyst = Item.of(catalystId, 1)
  let result = null
  try {
    player.setItemInHand(hand, fakeCatalyst)
    result = blockState.useItemOn(
      fakeCatalyst, level, player, hand, event.getUseOnContext().getHitResult()
    )
  } catch (error) {
    if (!tmmMahouBridgeErrorLogged) {
      console.error('[TMM Mahou/NV] Catalyst bridge failed; original dust retained: ' + error)
      tmmMahouBridgeErrorLogged = true
    }
    return
  } finally {
    player.setItemInHand(hand, originalHeld)
  }

  if (result != null && result.consumesAction()) {
    if (!player.isCreative()) originalHeld.shrink(1)
    event.cancelWithResult(result)
  }
})
