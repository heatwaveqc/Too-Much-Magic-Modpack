"""Stage an explicitly requested, read-only QA capture for the next existing-world rejoin.

Never boots a game. Refuses to overwrite an existing temporary probe or flag.
"""
import argparse,json,hashlib
from pathlib import Path

p=argparse.ArgumentParser()
p.add_argument('--minecraft',type=Path,required=True)
a=p.parse_args()
root=Path(__file__).resolve().parent
target=a.minecraft/'kubejs/server_scripts/zz_tmm_world_observe.js'
flag=a.minecraft/'kubejs/config/tmm-observe-once.json'
build_copy=a.minecraft/'kubejs/config/tmm-survey-build.json'
if flag.exists() or build_copy.exists(): raise SystemExit('Existing QA flag/build copy; inspect before replacing')
# The manually staged command-only probe can be replaced only when byte-identical to our source.
if target.exists() and target.read_bytes()!=(root/'server_scripts/tmm_world_observe.js').read_bytes():
    raise SystemExit('Existing different probe; inspect before replacing')
survey=(root/'server_scripts/tmm_surveys.js').read_text(encoding='utf-8')
survey=survey.replace("recipe: 'tmm_progression:bio_mutagen'", "recipe: 'tyr_progression:bio_mutagen'")
world=(root/'server_scripts/tmm_world_observe.js').read_text(encoding='utf-8')
tail='''
PlayerEvents.loggedIn(event => {
  let flag = JsonIO.read('kubejs/config/tmm-observe-once.json')
  if (!flag || flag.enabled !== true || !event.player.hasPermissions(2)) return
  let p = event.player, server = event.server
  server.scheduleInTicks(40, () => {
    let status = {schema: 1, invokedVia: 'QA API, not command parser', results: []}
    try {
      let world = tmmObserveWorld(p, server)
      status.results.push({capture: 'world', status: world.errors.length ? 'PARTIAL' : 'CAPTURED'})
      let ctx = {getSource: () => p.createCommandSourceStack()}
      status.results.push({capture: 'baseline', status: TMMQA.execute('baseline', ctx, '') ? 'CAPTURED' : 'UNSUPPORTED'})
      status.results.push({capture: 'recipes', status: TMMQA.execute('recipes', ctx, '') ? 'CAPTURED' : 'UNSUPPORTED'})
      status.results.push({capture: 'player', status: TMMQA.execute('player', ctx, 'before session22') ? 'CAPTURED' : 'UNSUPPORTED'})
    } catch(e) {status.error = String(e);console.error('TMM_QA_ONESHOT_FAILED ' + e)}
    JsonIO.write('kubejs/export/tmm-one-shot-status.json', status)
    console.info('TMM_QA_ONESHOT_FINISHED; inspect per-capture status, not this marker, for success')
  })
})
'''
target.parent.mkdir(parents=True,exist_ok=True);flag.parent.mkdir(parents=True,exist_ok=True)
target.write_text(survey+'\n'+world+tail,encoding='utf-8')
flag.write_text('{"enabled":true}\n',encoding='utf-8')
raw=(a.minecraft.parent/'BUILD-MANIFEST.json').read_bytes()
build=json.loads(raw)
selected={k:build.get(k) for k in ['integration_commit','baseline_sha','custom_jar_sha256','pinned_mod_count','pack_sha256','expected_client_mod_count','update_trial']}
selected['manifest_sha256']=hashlib.sha256(raw).hexdigest()
build_copy.write_text(json.dumps(selected,indent=2)+'\n',encoding='utf-8')
print('Staged temporary QA probe; existing world must rejoin before it can run. Remove probe and flag after capture.')
