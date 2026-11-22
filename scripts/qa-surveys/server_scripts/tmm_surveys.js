// QA ONLY. Copy manually into a disposable instance, never into a release pack.
// No commands are executed on behalf of a player. No progression/items/world data are changed.
var TMMQA = (function () {
  let VERSION = '1.1.0';
  let ROOT = 'kubejs/export/tmm-surveys/';
  let watches = {};
  let sequence = 0;
  function cls(name) { return Java.loadClass(name); }
  function error(e) { return String(e).slice(0, 600); }
  function probe(fn) {
    try { return {status: 'OBSERVED', value: fn()}; }
    catch (e) { return {status: 'UNSUPPORTED', reason: error(e)}; }
  }
  function token(value) {
    return String(cls('com.google.common.hash.Hashing').sha256().hashUnencodedChars(String(value)).toString());
  }
  function validLabel(label) { return /^[a-z0-9_-]{1,32}$/.test(label); }
  function validateArgs(args, count) {
    if (args.length !== count) throw Error('Wrong argument count; see /tmm_survey_help');
  }
  function registry() { return cls('net.minecraft.core.registries.BuiltInRegistries'); }
  function resource(id) { return cls('net.minecraft.resources.ResourceLocation').parse(id); }
  function idOf(stack) { return String(registry().ITEM.getKey(stack.getItem())); }
  function time(server) { return Number(server.overworld().getGameTime()); }
  function identity(ctx) {
    let server = ctx.getSource().getServer();
    let player = ctx.getSource().getPlayerOrException();
    let path = server.getWorldPath(cls('net.minecraft.world.level.storage.LevelResource').ROOT);
    return {world: token(path), subject: token(player.uuid)};
  }
  function envelope(ctx, kind, payload) {
    return {schema: 1, suite: VERSION, kind: kind, status: 'OBSERVATION_ONLY',
      identity: identity(ctx), capturedUtc: String(cls('java.time.Instant').now()),
      tick: time(ctx.getSource().getServer()),
      build: probe(() => JsonIO.read('kubejs/config/tmm-survey-build.json')),
      payload: payload};
  }
  function base(ctx) {
    let key = identity(ctx);
    return ROOT + key.world + '/' + key.subject + '/';
  }
  function write(ctx, kind, data) {
    let path = base(ctx) + kind + '-' + String(cls('java.lang.System').currentTimeMillis()) + '-' + (++sequence) + '.json';
    JsonIO.write(path, envelope(ctx, kind, data));
    ctx.getSource().sendSuccess(() => cls('net.minecraft.network.chat.Component').literal('TMM survey saved: ' + path), false);
    return 1;
  }
  function stackRow(stack, slot) {
    let text = String(stack.getComponents());
    return {slot: String(slot), id: idOf(stack), count: stack.getCount(),
      componentsSha256: token(text), components: text.slice(0, 2048),
      componentsTruncated: text.length > 2048};
  }
  function inventory(player) {
    let rows = [];
    let inv = player.getInventory();
    for (let i = 0; i < inv.getContainerSize(); i++) rows.push(stackRow(inv.getItem(i), i));
    let curios = probe(() => {
      let api = cls('top.theillusivec4.curios.api.CuriosApi');
      let optional = api.getCuriosInventory(player);
      if (!optional.isPresent()) throw Error('No Curios inventory');
      let slots = [];
      optional.get().getCurios().forEach((name, handler) => {
        let stacks = handler.getStacks();
        for (let i = 0; i < stacks.getSlots(); i++) slots.push(stackRow(stacks.getStackInSlot(i), String(name) + ':' + i));
      });
      return slots;
    });
    return {slots: rows, curios: curios,
      limits: ['Nested book/container contents are bounded component text, not a recursive inventory proof.']};
  }
  function attachments(player) {
    let result = {};
    let namespace = 'io.github.heatwaveqc.tyr.progression.state.';
    let holders;
    try { holders = cls(namespace + 'ProgressionAttachments'); }
    catch (_) { namespace = 'io.github.heatwaveqc.tmm.progression.state.'; }
    let tyr = namespace.indexOf('.tyr.') >= 0;
    let entries = [['LEDGER', tyr ? 'LedgerCodec' : 'Ledger'], ['PLAYER_MODES', 'Modes'],
      [tyr ? 'MUTATIONS' : 'HUNTER_MUTATIONS', tyr ? 'MutationLedger' : 'HunterMutationState'], ['FOOD_HISTORY', 'Discoveries']];
    entries.forEach(pair => {
      result[pair[0]] = probe(() => {
        let holder = cls(namespace + 'ProgressionAttachments')[pair[0]].get();
        // getData can initialize absent attachments. Never call it until presence is established.
        if (!player['hasData(net.neoforged.neoforge.attachment.AttachmentType)'](holder)) return {present: false};
        let stateType = cls(namespace + pair[1]);
        let codec = pair[0] === 'HUNTER_MUTATIONS' || pair[0] === 'MUTATIONS' ? stateType.codec() : stateType.CODEC;
        let json = codec.encodeStart(cls('com.mojang.serialization.JsonOps').INSTANCE,
          player['getData(net.neoforged.neoforge.attachment.AttachmentType)'](holder)).getOrThrow();
        return {present: true, data: JSON.parse(String(json))};
      });
    });
    return result;
  }
  function playerState(ctx) {
    let p = ctx.getSource().getPlayerOrException();
    let effects = [];
    p.getActiveEffects().forEach(e => effects.push({id: String(registry().MOB_EFFECT.getKey(e.getEffect().value())),
      amplifier: e.getAmplifier(), duration: e.getDuration()}));
    let state = {health: p.getHealth(), food: p.getFoodData().getFoodLevel(),
      saturation: p.getFoodData().getSaturationLevel(), experienceLevel: p.experienceLevel,
      experienceProgress: p.experienceProgress, totalExperience: p.totalExperience,
      effects: effects, attachments: attachments(p), inventory: inventory(p)};
    state.tyz = probe(() => {
      let api = cls('com.tyzsskills.api.TyzsSkillsAPI');
      return {xp: api.xp().getXP(p), level: api.level().getLevel(p)};
    });
    state.mxp = probe(() => {
      let data = cls('io.github.heatwaveqc.tmm.mxp.MxpSavedData');
      let factory = new (cls('net.minecraft.world.level.saveddata.SavedData$Factory'))(
        () => { throw Error('Survey must not create SavedData'); }, (tag, lookup) => data.load(tag, lookup), null);
      // get loads an existing record or returns null; never computeIfAbsent/get(server).
      let saved = ctx.getSource().getServer().overworld().getDataStorage().get(factory, 'tmm_mxp_players_v1');
      if (saved == null) return {present: false};
      let account = saved.snapshot(p.uuid);
      return {present: true, lifetimeLevels: String(account.counter().lifetimeLevels()),
        progress: account.counter().progress(), pendingDelivery: account.pendingDelivery(),
        recentTransactionCount: account.recentTransactions().size()};
    });
    state.arsMana = probe(() => cls('com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry').getMana(p).getCurrentMana());
    state.ironMana = probe(() => cls('io.redspace.ironsspellbooks.api.magic.MagicData').getPlayerMagicData(p).getMana());
    state.limits = ['Native mana gauges may not be the payer in shared/hybrid bridge modes.',
      'JAWR pools are not read by this adapter. Use native status commands as supplementary evidence.',
      'Observed deltas include regeneration and all intervening actions; they do not prove transaction attribution.'];
    return state;
  }
  function diff(before, after, path, out) {
    if (JSON.stringify(before) === JSON.stringify(after)) return;
    if (before !== null && after !== null && typeof before === 'object' && typeof after === 'object'
        && !Array.isArray(before) && !Array.isArray(after)) {
      let keys = Object.keys(before).concat(Object.keys(after)).filter((k, i, all) => all.indexOf(k) === i).sort();
      keys.forEach(k => diff(before[k], after[k], path ? path + '.' + k : k, out));
    } else {
      let row = {path: path, before: before === undefined ? null : before, after: after === undefined ? null : after};
      if (typeof before === 'number' && typeof after === 'number') row.delta = after - before;
      out.push(row);
    }
  }
  function snapshot(ctx, kind, args) {
    validateArgs(args, 2);
    let phase = args[0], label = args[1];
    if (!validLabel(label) || ['before', 'after'].indexOf(phase) < 0) throw Error('Use before|after LABEL (a-z, digits, _ or -, max 32)');
    let current = envelope(ctx, kind, playerState(ctx));
    if (current.build.status !== 'OBSERVED' || current.build.value == null) throw Error('Build manifest missing; cannot certify comparable snapshots');
    let saved = base(ctx) + kind + '-' + label + '-before.json';
    if (phase === 'before') {
      if (JsonIO.read(saved) != null) throw Error('Baseline label already exists. Choose a new label to avoid overwriting evidence.');
      JsonIO.write(saved, current);
      return write(ctx, kind + '-baseline', {label: label, baseline: saved});
    }
    let before = JsonIO.read(saved);
    if (before == null) throw Error('No baseline for this world/player/label');
    if (before.suite !== VERSION || JSON.stringify(before.identity) !== JSON.stringify(current.identity)
        || JSON.stringify(before.build) !== JSON.stringify(current.build)) throw Error('Baseline build/suite/subject differs');
    let changes = [];
    diff(before.payload, current.payload, '', changes);
    return write(ctx, kind + '-comparison', {label: label, before: before, after: current,
      changes: changes, verdict: 'MANUAL_EXPECTATION_REQUIRED'});
  }
  function baseline(ctx) {
    let server = ctx.getSource().getServer(), commands = [];
    server.getCommands().getDispatcher().getRoot().getChildren().forEach(n => commands.push(String(n.getName())));
    let mods = [];
    // FML ModList is intentionally blocked by KubeJS's class filter. Inventory is audited offline.
    mods = {status: 'NOT_CAPTURED', reason: 'Use hash-verified installed inventory; FML ModList is class-filtered.'};
    let structures = [];
    server.registryAccess().registryOrThrow(cls('net.minecraft.core.registries.Registries').STRUCTURE)
      .keySet().forEach(k => structures.push(String(k)));
    let dimensions = [];
    server.levelKeys().forEach(k => dimensions.push(String(k.location())));
    return write(ctx, 'baseline', {commands: commands.sort(), mods: mods, structures: structures.sort(), dimensions: dimensions.sort(),
      permissions: 'Command presence is not a non-OP permission or behavior test.',
      selectedDatapacks: probe(() => { let ids = []; server.getPackRepository().getSelectedIds().forEach(i => ids.push(String(i))); return ids; })});
  }
  function recipes(ctx, args) {
    let server = ctx.getSource().getServer(), player = ctx.getSource().getPlayerOrException();
    let cases = [];
    ['cod', 'salmon', 'tropical_fish', 'pufferfish', 'cooked_cod', 'cooked_salmon'].forEach(fish => {
      cases.push({recipe: 'dragonsurvival:seasoned_fish', items: ['minecraft:' + fish, 'minecraft:ink_sac', 'minecraft:kelp'], expected: fish.indexOf('cooked_') !== 0});
    });
    cases.push({recipe: server.getRecipeManager().byKey(resource('tyr_progression:bio_mutagen')).isPresent() ? 'tyr_progression:bio_mutagen' : 'tmm_progression:bio_mutagen', items: [
      'minecraft:moss_block', 'minecraft:moss_block', 'minecraft:moss_block', 'minecraft:moss_block',
      'minecraft:rotten_flesh', 'minecraft:rotten_flesh', 'minecraft:rotten_flesh', 'minecraft:rotten_flesh', 'minecraft:spider_eye'], expected: true});
    if (args.length) {
      if (args.length < 3 || args.length > 11 || ['match', 'reject'].indexOf(args[0]) < 0)
        throw Error('recipes [match|reject RECIPE_ID SLOT1 ... SLOT9]; use - for an empty slot');
      if (!/^[a-z0-9_.-]+:[a-z0-9_./-]+$/.test(args[1]) || args.slice(2).some(id => id !== '-' && !/^[a-z0-9_.-]+:[a-z0-9_./-]+$/.test(id)))
        throw Error('Recipe and item IDs must be namespaced');
      cases = [{recipe: args[1], items: args.slice(2), expected: args[0] === 'match'}];
    }
    let rows = cases.map(c => {
      let observed = probe(() => {
        let opt = server.getRecipeManager().byKey(resource(c.recipe));
        if (!opt.isPresent()) throw Error('Recipe not registered: ' + c.recipe);
        let list = new (cls('java.util.ArrayList'))();
        for (let i = 0; i < 9; i++) {
          if (i >= c.items.length || c.items[i] === '-') list.add(cls('net.minecraft.world.item.ItemStack').EMPTY);
          else {
            let id = resource(c.items[i]);
            if (!registry().ITEM.containsKey(id)) throw Error('Item absent: ' + c.items[i]);
            list.add(registry().ITEM.get(id).getDefaultInstance().copy());
          }
        }
        let input = cls('net.minecraft.world.item.crafting.CraftingInput').of(3, 3, list);
        return Boolean(opt.get().value()['matches(net.minecraft.world.item.crafting.CraftingInput,net.minecraft.world.level.Level)'](input, player.serverLevel()));
      });
      return {case: c, observed: observed, status: observed.status !== 'OBSERVED' ? 'UNSUPPORTED'
        : observed.value === c.expected ? 'PASS_RECIPE_MATCH_ONLY' : 'FAIL_RECIPE_MATCH'};
    });
    return write(ctx, 'recipes', {cases: rows, limits: ['No real crafting, consumption, brewing, custom ritual costs or completion callbacks were exercised. Recipe IDs are fixtures, not guaranteed across future builds.']});
  }
  function traders(ctx) {
    let p = ctx.getSource().getPlayerOrException(), rows = [];
    let type = cls('net.minecraft.world.entity.EntityType').WANDERING_TRADER;
    p.level().getEntitiesOfClass(cls('net.minecraft.world.entity.npc.WanderingTrader'), p.getBoundingBox().inflate(16))
      .forEach(t => {
        // getOffers lazily rolls stock. Reading saved Offers avoids creating or rerolling trades.
        let saved = new (cls('net.minecraft.nbt.CompoundTag'))();
        t.saveWithoutId(saved);
        rows.push({subject: token(t.uuid), type: String(registry().ENTITY_TYPE.getKey(type)),
          offersPresent: saved.contains('Offers'), offersSnbt: String(saved.getCompound('Offers'))});
      });
    return write(ctx, 'traders', {radius: 16, traders: rows, status: rows.length ? 'OBSERVATION_ONLY' : 'SETUP_MISSING',
      limits: ['Uninitialized offers remain absent until a real trade interaction.', 'Affix/glyph/ink category rules require semantic review; no blanket stock PASS.', 'Natural spawn vetoes, vendetta AI and missiles remain separate tests.']});
  }
  function gateways(ctx) {
    let keys = [], entities = [];
    let loaded = probe(() => {
      cls('dev.shadowsoffire.gateways.gate.GatewayRegistry').INSTANCE.getKeys().forEach(k => keys.push(String(k)));
      return keys.sort();
    });
    let nearby = probe(() => {
      let p = ctx.getSource().getPlayerOrException();
      p.level().getEntitiesOfClass(cls('dev.shadowsoffire.gateways.entity.GatewayEntity'), p.getBoundingBox().inflate(32)).forEach(g => {
        entities.push({subject: token(g.uuid), wave: g.getWave(), activeEnemies: g.getActiveEnemies(),
          lives: g.getRemainingLives(), ticksActive: g.getTicksActive(), waveActive: g.isWaveActive(),
          valid: g.isValid(), completed: g.isCompleted(), failureReason: String(g.getFailureReason())});
      });
      return entities;
    });
    return write(ctx, 'gateways', {registry: loaded, nearby: nearby, radius: 32,
      limits: ['Telemetry is instantaneous. Missing/despawned arena is not proof of successful completion.',
        'Wave/entity definitions, rewards and item delivery still require dedicated validation and before/after inventory observations.']});
  }
  function blockSample(level, pos) {
    if (!level.hasChunkAt(pos)) throw Error('Chunk unloaded; refusing to load it');
    let be = level.getBlockEntity(pos);
    if (be == null) throw Error('No block entity at selected position');
    return {block: String(registry().BLOCK.getKey(level.getBlockState(pos).getBlock())),
      instance: String(cls('java.lang.System').identityHashCode(be)),
      mana: probe(() => be.getMana()), currentMana: probe(() => be.getCurrentMana()),
      maxMana: probe(() => be.getMaxMana()),
      burnTimeLeft: probe(() => be.getBurnTimeLeft()), cooldown: probe(() => be.getCooldown())};
  }
  function monitorSummary(w) {
    let observed = w.samples.filter(s => s.observation.status === 'OBSERVED');
    let first = observed[0], last = observed[observed.length - 1];
    let summary = {status: !w.done ? 'STOPPED_EARLY' : w.samples[w.samples.length - 1].observation.status !== 'OBSERVED' ? 'INTERRUPTED' : 'OBSERVED',
      observedTicks: last.elapsed - first.elapsed, sampleCount: observed.length,
      limits: ['Rate is NET observed storage change, not gross generation or resource conversion efficiency.']};
    ['mana', 'currentMana'].forEach(field => {
      let a = first.observation.value[field], b = last.observation.value[field];
      if (a.status === 'OBSERVED' && b.status === 'OBSERVED' && summary.observedTicks > 0)
        summary[field] = {netChange: b.value - a.value, netPerTick: (b.value - a.value) / summary.observedTicks};
    });
    summary.burningSamples = observed.filter(s => s.observation.value.burnTimeLeft.status === 'OBSERVED' && s.observation.value.burnTimeLeft.value > 0).length;
    summary.cooldownSamples = observed.filter(s => s.observation.value.cooldown.status === 'OBSERVED' && s.observation.value.cooldown.value > 0).length;
    return summary;
  }
  function botania(ctx, args) {
    let key = base(ctx), p = ctx.getSource().getPlayerOrException();
    if (args.length === 1 && args[0] === 'stop') {
      if (!watches[key]) throw Error('No active monitor');
      let result = watches[key]; delete watches[key];
      result.summary = monitorSummary(result);
      return write(ctx, 'botania', result);
    }
    validateArgs(args, 5);
    if (args[0] !== 'start') throw Error('Use start X Y Z TICKS or stop');
    if (watches[key]) throw Error('Stop the existing monitor first');
    if (Object.keys(watches).length >= 8) throw Error('At most eight active/uncollected monitors');
    let values = args.slice(1).map(Number);
    if (values.some(n => !Number.isInteger(n)) || values[3] < 1 || values[3] > 1200) throw Error('Integer coordinates; duration 1..1200 ticks');
    let pos = new (cls('net.minecraft.core.BlockPos'))(values[0], values[1], values[2]);
    if (pos.distSqr(p.blockPosition()) > 256) throw Error('Monitor must be within 16 blocks');
    let level = p.level();
    let first = blockSample(level, pos);
    if (first.block.indexOf('botania:') !== 0)
      throw Error('This monitor is for Botania blocks; converter adapters are not yet supported');
    watches[key] = {dimension: String(level.dimension().location()), coordinates: values.slice(0, 3),
      startedTick: time(ctx.getSource().getServer()), duration: values[3], samples: [{elapsed: 0, observation: {status: 'OBSERVED', value: first}}],
      limits: ['Net buffer deltas are not gross generation when mana is transferred or consumed.',
        'Block replacement, unloading or reload interrupts monitoring. No ticks are forced.'], done: false};
    return write(ctx, 'botania-start', {duration: values[3], sample: first});
  }
  function tick(server) {
    Object.keys(watches).forEach(key => {
      let w = watches[key];
      if (w.done) return;
      let elapsed = time(server) - w.startedTick;
      if (elapsed <= 0 || w.samples[w.samples.length - 1].elapsed === elapsed) return;
      let sample = probe(() => {
        let dim = cls('net.minecraft.resources.ResourceKey').create(cls('net.minecraft.core.registries.Registries').DIMENSION, resource(w.dimension));
        let level = server.getLevel(dim);
        if (level == null) throw Error('Dimension unavailable');
        let pos = new (cls('net.minecraft.core.BlockPos'))(w.coordinates[0], w.coordinates[1], w.coordinates[2]);
        let now = blockSample(level, pos);
        if (now.block !== w.samples[0].observation.value.block || now.instance !== w.samples[0].observation.value.instance) throw Error('Monitored block instance changed');
        return now;
      });
      w.samples.push({elapsed: elapsed, observation: sample});
      w.done = elapsed >= w.duration || sample.status !== 'OBSERVED';
      // Retain only bounded in-memory samples until operator invokes stop.
    });
  }
  function client(ctx) {
    if (!ctx.getSource().getServer().isSingleplayer()) throw Error('Client request file only supports integrated singleplayer');
    let nonce = String(cls('java.time.Instant').now());
    JsonIO.write(ROOT + 'client-request.json', {nonce: nonce, suite: VERSION});
    return write(ctx, 'client-request', {nonce: nonce, status: 'REQUESTED_NOT_CAPTURED'});
  }
  function execute(name, ctx, raw) {
    // Check at execution as well as Brigadier visibility; console is deliberately excluded.
    if (!ctx.getSource().hasPermission(2)) return 0;
    try {
      ctx.getSource().getPlayerOrException();
      let args = String(raw || '').trim().split(/\s+/).filter(s => s.length);
      if (['player', 'tree', 'magic', 'inventory'].indexOf(name) >= 0) return snapshot(ctx, name, args);
      if (name === 'botania') return botania(ctx, args);
      if (name === 'recipes') return recipes(ctx, args);
      validateArgs(args, 0);
      if (name === 'baseline') return baseline(ctx);
      if (name === 'traders') return traders(ctx);
      if (name === 'gateways') return gateways(ctx);
      if (name === 'client') return client(ctx);
      if (name === 'help') return write(ctx, 'help', {commands: commands,
        syntax: ['player|tree|magic|inventory before|after LABEL', 'botania start X Y Z TICKS', 'botania stop'],
        note: 'No world/progression fixtures are performed; unsupported adapters are explicit.'});
      throw Error('Unknown survey');
    } catch (e) {
      ctx.getSource().sendFailure(cls('net.minecraft.network.chat.Component').literal('TMM survey failed: ' + error(e)));
      return 0;
    }
  }
  let commands = ['help', 'baseline', 'player', 'inventory', 'tree', 'magic', 'recipes', 'traders', 'gateways', 'botania', 'client'];
  return {commands: commands, execute: execute, tick: tick, diff: diff,
    validLabel: validLabel, validateArgs: validateArgs, probe: probe, monitorSummary: monitorSummary};
})();

var TMMQAConfig = JsonIO.read('kubejs/config/tmm-surveys.json');
var TMMQAEnabled = TMMQAConfig != null && TMMQAConfig.enabled === true;
ServerEvents.commandRegistry(event => {
  if (!TMMQAEnabled) return;
  let C = Java.loadClass('net.minecraft.commands.Commands');
  let S = Java.loadClass('com.mojang.brigadier.arguments.StringArgumentType');
  TMMQA.commands.forEach(name => {
    event.register(C.literal('tmm_survey_' + name).requires(source => source.hasPermission(2))
      .executes(ctx => TMMQA.execute(name, ctx, ''))
      .then(C.argument('args', S.greedyString()).executes(ctx => TMMQA.execute(name, ctx, S.getString(ctx, 'args')))));
  });
});
ServerEvents.tick(event => {
  if (TMMQAEnabled) TMMQA.tick(event.server);
});
