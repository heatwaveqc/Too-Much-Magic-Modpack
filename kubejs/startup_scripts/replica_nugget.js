// Owner-approved Replica nugget: ordinary 1/9 ingot fraction, enables Embers dynamic seeds.
// Requires a full game restart for item registration.
StartupEvents.registry('item', event => {
  event.create('tmm:replica_nugget')
    .displayName('Replica Nugget')
    .texture('tmm:item/replica_nugget')
    .maxStackSize(64)
})
