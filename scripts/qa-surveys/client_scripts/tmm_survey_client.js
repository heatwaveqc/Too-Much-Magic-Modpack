// QA ONLY: local export of registered keys. Triggered by /tmm_survey_client in singleplayer.
// Does not assign/reset any key. Current bindings can be private: review before uploading.
var TMMQAClient = (function () {
  let root = 'kubejs/export/tmm-surveys/';
  let previous = null, ticks = 0, ready = false;
  function request() { return JsonIO.read(root + 'client-request.json'); }
  function initialize() {
    let existing = request();
    previous = existing == null ? null : String(existing.nonce);
    ready = true;
  }
  function exportKeys(client, nonce) {
    let keys = [], conflicts = {}, counts = {};
    client.options.keyMappings.forEach(k => {
      let current = k.getKey(), original = k.getDefaultKey();
      let code = String(current.getName());
      keys.push({name: String(k.getName()), category: String(k.getCategory()),
        defaultKey: String(original.getName()), currentKey: code,
        unbound: Boolean(k.isUnbound()), changed: code !== String(original.getName()),
        modifier: String(k.getKeyModifier())});
      if (!k.isUnbound()) {
        let binding = code + ':' + String(k.getKeyModifier());
        if (!conflicts[binding]) conflicts[binding] = [];
        conflicts[binding].push(String(k.getName()));
      }
      let name = String(k.getName());
      counts[name] = (counts[name] || 0) + 1;
    });
    let shared = Object.keys(conflicts).filter(k => conflicts[k].length > 1)
      .map(k => ({binding: k, keys: conflicts[k]}));
    let duplicateNames = Object.keys(counts).filter(k => counts[k] > 1);
    let file = root + 'client-keys-' + nonce.replace(/[^a-zA-Z0-9_-]/g, '_') + '.json';
    JsonIO.write(file, {schema: 1, suite: '1.1.0', status: 'OBSERVATION_ONLY', requestNonce: nonce,
      capturedUtc: String(Java.loadClass('java.time.Instant').now()), keys: keys,
      sharedBindings: shared, duplicateTranslationKeys: duplicateNames,
      limits: ['Shared bindings may be valid in disjoint conflict contexts; this is not an automatic conflict verdict.',
        'Translation key prefixes do not prove vanilla/mod ownership. Preserve the canonical vanilla allowlist.',
        'Default/reset/restart persistence require captures around the actual user action.',
        'HUD/JEI/rendering, GUI scale and visual legibility still need observation.'],
      privacy: 'Local current bindings included; review before publishing.'});
    console.info('TMM_SURVEY_CLIENT_WRITTEN=' + file);
  }
  function tick(client) {
    if (!ready) { initialize(); return; }
    if (++ticks % 20 !== 0) return;
    let r = request();
    if (r == null || r.suite !== '1.1.0' || String(r.nonce) === previous) return;
    let nonce = String(r.nonce);
    if (!/^[0-9TZ:.\-]{10,40}$/.test(nonce)) { previous = nonce; return; }
    previous = nonce;
    try { exportKeys(client, nonce); }
    catch (e) {
      JsonIO.write(root + 'client-error.json', {schema: 1, requestNonce: nonce,
        status: 'UNSUPPORTED', reason: String(e).slice(0, 600)});
      console.error('TMM survey client capture failed: ' + e);
    }
  }
  return {initialize: initialize, tick: tick};
})();
var TMMQAClientConfig = JsonIO.read('kubejs/config/tmm-surveys.json');
var TMMQAClientEnabled = TMMQAClientConfig != null && TMMQAClientConfig.enabled === true;
ClientEvents.loggedIn(event => { if (TMMQAClientEnabled) TMMQAClient.initialize(); });
ClientEvents.tick(event => {
  if (TMMQAClientEnabled) TMMQAClient.tick(event.client);
});
