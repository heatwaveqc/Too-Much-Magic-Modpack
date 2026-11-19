// Offline contract tests only. These do not emulate Minecraft/Rhino integration.
const {test} = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const vm = require('node:vm');
const crypto = require('node:crypto');
const path = require('node:path');
const root = path.resolve(__dirname, '../scripts/qa-surveys');
const serverText = fs.readFileSync(path.join(root, 'server_scripts/tmm_surveys.js'), 'utf8');
const clientText = fs.readFileSync(path.join(root, 'client_scripts/tmm_survey_client.js'), 'utf8');

function harness(enabled = false) {
  const files = new Map(), writes = [], handlers = {}, registered = [], errors = [];
  files.set('kubejs/config/tmm-surveys.json', {enabled});
  files.set('../BUILD-MANIFEST.json', {integration_commit: 'a'.repeat(40)});
  let clock = 1000;
  class JString {constructor(s) {this.value=s;} getBytes() {return Buffer.from(this.value);}}
  const component = {literal: s=>s};
  function builder(name) {
    return {name, requires(f) {this.guard=f;return this;}, executes(f) {this.action=f;return this;}, then(child) {this.child=child;return this;}};
  }
  const classes = {
    'java.lang.String': JString,
    'java.security.MessageDigest': {getInstance: ()=>({digest: bytes=>crypto.createHash('sha256').update(bytes).digest()})},
    'java.lang.System': {currentTimeMillis: ()=>++clock},
    'java.time.Instant': {now: ()=>'2026-11-18T20:00:00Z'},
    'net.minecraft.world.level.storage.LevelResource': {ROOT: 'root'},
    'net.minecraft.network.chat.Component': component,
    'net.minecraft.commands.Commands': {literal: builder, argument: builder},
    'com.mojang.brigadier.arguments.StringArgumentType': {greedyString: ()=>({}), getString: (ctx, key)=>ctx[key]}
  };
  const player = {
    getUUID: ()=>'private-player-uuid', getActiveEffects: ()=>[],
    getHealth: ()=>20, getFoodData: ()=>({getFoodLevel: ()=>20, getSaturationLevel: ()=>5}),
    experienceLevel: 2, experienceProgress: 0.5, totalExperience: 9,
    getInventory: ()=>({getContainerSize: ()=>0})
  };
  const server = {getWorldPath: ()=>'/private/test-world', overworld: ()=>({getGameTime: ()=>clock}), isSingleplayer: ()=>true};
  const source = {hasPermission: ()=>true, getPlayerOrException: ()=>player, getServer: ()=>server,
    sendSuccess: ()=>{}, sendFailure: x=>errors.push(x)};
  const context = {getSource: ()=>source};
  const sandbox = {Java: {loadClass: name=>{if (!(name in classes)) throw Error('Unavailable adapter: '+name);return classes[name];}},
    JsonIO: {read: p=>files.has(p) ? JSON.parse(JSON.stringify(files.get(p))) : null,
      write: (p, data)=>{files.set(p, JSON.parse(JSON.stringify(data)));writes.push(p);}},
    ServerEvents: {commandRegistry: cb=>handlers.commands=cb, tick: cb=>handlers.tick=cb},
    ClientEvents: {loggedIn: cb=>handlers.login=cb, tick: cb=>handlers.clientTick=cb},
    console: {info: ()=>{}, error: ()=>{}}};
  vm.createContext(sandbox);vm.runInContext(serverText, sandbox);
  handlers.commands({register: b=>registered.push(b)});
  return {sandbox, files, writes, handlers, registered, errors, source, context, classes, player, server};
}

test('suite is opt-in and registers eleven operator-gated commands', ()=>{
  assert.equal(harness().registered.length, 0);
  const h=harness(true);assert.equal(h.registered.length, 11);
  for(const cmd of h.registered) {
    assert.equal(cmd.guard({hasPermission: ()=>false}), false);
    assert.equal(cmd.guard({hasPermission: n=>n===2}), true);
  }
});
test('non-OP execution does not access player, classes or files', ()=>{
  const h=harness(true);h.source.hasPermission=()=>false;
  h.source.getPlayerOrException=()=>{throw Error('must not be accessed');};
  assert.equal(h.sandbox.TMMQA.execute('player', h.context, 'before x'), 0);
  assert.equal(h.writes.length, 0);
});
test('console and malformed/path-traversal labels are rejected without writes', ()=>{
  const h=harness(true);
  for(const raw of ['before ../save', 'before /tmp', 'before a.b', 'after', 'before good extra']) {
    assert.equal(h.sandbox.TMMQA.execute('player', h.context, raw), 0);
  }
  h.source.getPlayerOrException=()=>{throw Error('no player');};
  assert.equal(h.sandbox.TMMQA.execute('help', h.context, ''), 0);
  assert.equal(h.writes.length, 0);
});
test('snapshots survive script reload; unavailable adapters remain explicit; baseline cannot be clobbered', ()=>{
  const h=harness(true), q=h.sandbox.TMMQA;
  assert.equal(q.execute('player',h.context,'before cast1'),1);
  const saved=h.writes.find(p=>p.endsWith('-before.json'));
  assert(saved);
  assert.equal(q.execute('player',h.context,'before cast1'),0);
  vm.runInContext(serverText,h.sandbox);
  h.player.experienceLevel=3;
  assert.equal(h.sandbox.TMMQA.execute('player',h.context,'after cast1'),1);
  const report=h.files.get(h.writes.at(-1));
  assert.equal(report.payload.verdict,'MANUAL_EXPECTATION_REQUIRED');
  assert(report.payload.changes.some(r=>r.path==='experienceLevel' && r.delta===1));
  assert.equal(report.payload.after.payload.tyz.status,'UNSUPPORTED');
  assert(!JSON.stringify(report).includes('private-player-uuid'));
  assert(!JSON.stringify(report).includes('/private/test-world'));
});
test('comparison rejects another build and missing baseline', ()=>{
  const h=harness(true), q=h.sandbox.TMMQA;
  assert.equal(q.execute('tree',h.context,'after absent'),0);
  q.execute('tree',h.context,'before logs1');
  h.files.set('../BUILD-MANIFEST.json',{integration_commit:'b'.repeat(40)});
  assert.equal(q.execute('tree',h.context,'after logs1'),0);
  assert(h.errors.at(-1).includes('differs'));
});
test('numeric diffs preserve zero and negative changes and report deleted keys', ()=>{
  const h=harness(), rows=[];
  h.sandbox.TMMQA.diff({xp:9, pool:0, old:true},{xp:3, pool:0},'',rows);
  assert.equal(rows.length,2);assert(rows.some(r=>r.path==='xp' && r.delta===-6));
  assert(rows.some(r=>r.path==='old' && r.after===null));
});
test('absent progression attachments are not initialized by snapshot collection', ()=>{
  const h=harness(true);
  const attachments={};
  for(const name of ['LEDGER','PLAYER_MODES','HUNTER_MUTATIONS','FOOD_HISTORY']) attachments[name]={get: ()=>name};
  h.classes['io.github.heatwaveqc.tmm.progression.state.ProgressionAttachments']=attachments;
  let reads=0;
  h.player['hasData(net.neoforged.neoforge.attachment.AttachmentType)']=()=>false;
  h.player['getData(net.neoforged.neoforge.attachment.AttachmentType)']=()=>{reads++;throw Error('must not initialize');};
  assert.equal(h.sandbox.TMMQA.execute('player',h.context,'before absent'),1);
  assert.equal(reads,0);
});
test('missing recipes produce seven explicit unsupported observations, never match PASS', ()=>{
  const h=harness(true);
  h.classes['net.minecraft.resources.ResourceLocation']={parse: s=>s};
  h.server.getRecipeManager=()=>({byKey: ()=>({isPresent: ()=>false})});
  assert.equal(h.sandbox.TMMQA.execute('recipes',h.context,''),1);
  const rows=h.files.get(h.writes.at(-1)).payload.cases;
  assert.equal(rows.length,7);
  assert(rows.every(row=>row.status==='UNSUPPORTED'));
});
test('monitor summary reports net storage rate and interruptions without a generation PASS', ()=>{
  const h=harness();
  const observed=(elapsed,mana)=>({elapsed,observation:{status:'OBSERVED',value:{
    mana:{status:'OBSERVED',value:mana},currentMana:{status:'UNSUPPORTED'},
    burnTimeLeft:{status:'OBSERVED',value:1},cooldown:{status:'OBSERVED',value:0}}}});
  const w={done:true,samples:[observed(0,0),observed(2,150)]};
  const summary=h.sandbox.TMMQA.monitorSummary(w);
  assert.equal(summary.mana.netPerTick,75);assert.equal(summary.status,'OBSERVED');
  w.samples.push({elapsed:3,observation:{status:'UNSUPPORTED',reason:'unloaded'}});
  assert.equal(h.sandbox.TMMQA.monitorSummary(w).status,'INTERRUPTED');
});
test('monitor enforces duration/coordinates and cannot stop a nonexistent session', ()=>{
  const h=harness(true), q=h.sandbox.TMMQA;
  for(const raw of ['start 0 0 0 1201','start 0 0 0 0','start 0 0 0 NaN','start 0 0 0 1.5','stop'])
    assert.equal(q.execute('botania',h.context,raw),0);
  assert.equal(h.writes.length,0);
});
test('client request refuses dedicated servers and does not report successful capture', ()=>{
  const h=harness(true);h.server.isSingleplayer=()=>false;
  assert.equal(h.sandbox.TMMQA.execute('client',h.context,''),0);
  h.server.isSingleplayer=()=>true;
  assert.equal(h.sandbox.TMMQA.execute('client',h.context,''),1);
  assert.equal(h.files.get(h.writes.at(-1)).payload.status,'REQUESTED_NOT_CAPTURED');
});
test('client ignores stale requests; exports once per new request without changing bindings', ()=>{
  const h=harness(true);
  const req='kubejs/export/tmm-surveys/client-request.json';
  h.files.set(req,{suite:'1.0.0',nonce:'2026-11-18T20:00:00Z'});
  vm.runInContext(clientText,h.sandbox);
  h.handlers.login({});
  const key={getKey: ()=>({getName: ()=>'key.keyboard.k'}),getDefaultKey: ()=>({getName: ()=>'key.keyboard.unknown'}),
    getName: ()=>'key.example.open', getCategory: ()=>'key.categories.example', isUnbound: ()=>false, getKeyModifier: ()=>'NONE'};
  const event={client:{options:{keyMappings:[key]}}};
  for(let i=0;i<20;i++) h.handlers.clientTick(event);
  assert.equal(h.writes.length,0);
  h.files.set(req,{suite:'1.0.0',nonce:'2026-11-18T20:00:01Z'});
  for(let i=0;i<40;i++) h.handlers.clientTick(event);
  assert.equal(h.writes.length,1);
  assert.equal(h.files.get(h.writes[0]).keys[0].changed,true);
});
test('no destructive command execution or gameplay mutator calls in QA server source', ()=>{
  assert(!/runCommand|performPrefixedCommand|\.setData\(|\.setBlock\(|\.setHealth\(|\.awardServerXp\(|\.graduate\(|\.buy\(|\.getOffers\(/.test(serverText));
});
