// QA-only, opt-in, read-only. Remove the flag/probe after capture. No teleport or world creation.
function tmmExportDimensions(server) {
  let config = JsonIO.read('kubejs/config/tmm-dimension-survey.json')
  if (!config || config.enabled !== true) return
  let ids = []
  let errors = []
  try { server.levelKeys().forEach(key => ids.push(String(key.location()))) }
  catch (e) {
    errors.push(String(e))
    ids = []
    server.getAllLevels().forEach(level => ids.push(String(level.dimension)))
  }
  ids.sort()
  if (ids.some(id => !/^[a-z0-9_.-]+:[a-z0-9_./-]+$/.test(id))) throw Error('Unexpected dimension key format')
  let result = {schema: 1, status: 'LIVE_SERVER_LEVEL_KEYS', build: config.build,
    capturedUtc: String(Java.loadClass('java.time.Instant').now()), dimensions: ids, limitations: errors}
  JsonIO.write('kubejs/export/tmm-dimension-registry.json', result)
  console.info('TMM_DIMENSION_REGISTRY_CAPTURED=' + ids.length)
}
PlayerEvents.loggedIn(event => {
  let config = JsonIO.read('kubejs/config/tmm-dimension-survey.json')
  if (!config || config.enabled !== true || !event.player.hasPermissions(2)) return
  event.server.scheduleInTicks(20, () => {
    try { tmmExportDimensions(event.server) }
    catch (e) { console.error('TMM_DIMENSION_REGISTRY_FAILED=' + e) }
  })
})
ServerEvents.basicCommand('tmm_dimension_registry', event => {
  if (!event.player || !event.player.hasPermissions(2)) {event.cancel('Operator permission required'); return}
  tmmExportDimensions(event.server)
  event.cancel('Dimension registry exported if local survey flag is enabled')
})
