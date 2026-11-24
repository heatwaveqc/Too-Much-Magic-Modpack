// Physical counterpart of Q Shop personal coins. Intentionally ProjectE-transmutable.
StartupEvents.registry('item', event => {
  event.create('tmm:coin').displayName('Coin').texture('minecraft:item/gold_nugget').maxStackSize(64)
})
