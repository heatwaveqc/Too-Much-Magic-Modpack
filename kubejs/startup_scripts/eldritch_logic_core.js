// TMM: the only normal survival source is the Ancient Eldritch Miner's output table.
// Verified against Occultism 1.227.0's occultism:miners/eldritch item tag.
// This KubeJS 1.21.1 item registry requires a full game restart.
StartupEvents.registry('item', event => {
  event.create('tmm:eldritch_logic_core')
    .displayName('Eldritch Logic Core')
    .texture('minecraft:item/ender_eye')
    .maxStackSize(64)
})
