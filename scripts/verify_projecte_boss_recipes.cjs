// Node-only offline contract test; executes the exact KubeJS recipe callback
// against a deterministic recorder. Does NOT claim in-world KubeJS/JEI PASS.
const fs = require('node:fs');
const vm = require('node:vm');
const assert = require('node:assert/strict');

const path = 'kubejs/server_scripts/projecte_boss_stone_and_powerflowers.js';
const source = fs.readFileSync(path, 'utf8');
const tag = JSON.parse(fs.readFileSync(
  'kubejs/data/tmm/tags/item/philosophers_stone_boss_trophies.json', 'utf8'));
const expectedTrophies = [
  'ars_nouveau:wilden_tribute',
  'botania:life_essence',
  'neovitae:mine_key'
];
assert.deepEqual(tag.values, expectedTrophies);
assert.equal(tag.replace, false);
assert(!tag.values.includes('botania:gaia_spirit'), 'Gaia Spirit ID is botania:life_essence');
assert(!tag.values.includes('neovitae:mine_entrance_key'), 'Entrance key is craftable');

const removed = [];
const added = [];
const event = {
  remove(filter) { assert.equal(typeof filter.id, 'string'); removed.push(filter.id); },
  shaped(output, pattern, key) {
    const obj = {output, pattern, key, id: null};
    added.push(obj);
    return { id(id) {obj.id = id; return this;} };
  }
};
const ServerEvents = { recipes(fn) { fn(event); } };
vm.runInNewContext(source, {ServerEvents}, {timeout:3000,filename:path});
const tiers = [
  'red','magenta','pink','purple','violet','blue','cyan',
  'green','lime','yellow','orange','white','fading','final'
];
const expectedRemovals = [
 'projecte:philosophers_stone',
 'projecte:philosophers_stone_alt',
 ...tiers.map(t => 'projectexpansion:power_flower/' + t),
 'projectexpansion:power_flower/red_upgrade'
];
assert.deepEqual(removed, expectedRemovals);
assert.equal(added.length, 17);
assert.equal(new Set(added.map(x => x.id)).size, added.length);
for (const entry of added) {
 assert(entry.id?.startsWith('tmm:'), 'All new recipes should have unique TMM ID');
 assert(entry.pattern.length === 3 && entry.pattern.every(x => x.length === 3));
 assert(!Object.values(entry.key).some(x => x === 'minecraft:diamond'),
   'Do not let a default cheap center survive');
}
const stone = added.filter(x => x.output === 'projecte:philosophers_stone');
assert.equal(stone.length,2);
assert.equal(stone[0].pattern[1][1], 'T');
assert.equal(stone[1].pattern[1][1], 'T');
assert(stone.every(x => x.key.T === '#tmm:philosophers_stone_boss_trophies'));
assert.deepEqual(stone.map(x=>x.pattern.join('/')), ['RGR/GTG/RGR','GRG/RTR/GRG']);
for (const tier of tiers) {
 const relevant=added.filter(x=>x.output === 'projectexpansion:'+tier+'_power_flower');
 const direct=relevant.find(x=>x.id.endsWith('/'+tier+'_chaos_direct'));
 assert(direct, tier+' direct recipe missing');
 assert(direct.pattern.join('/') === 'CLC/RRR/RSR');
 assert(direct.key.S === 'draconicevolution:chaos_shard');
 assert(direct.key.R === 'projectexpansion:'+tier+'_relay');
}
const redUpgrade=added.find(x=>x.id.endsWith('/red_chaos_upgrade'));
assert(redUpgrade);
assert.equal(redUpgrade.output, 'projectexpansion:red_power_flower');
assert.equal(redUpgrade.pattern.join('/'), 'CLC/RPR/RSR');
assert.equal(redUpgrade.key.P, 'projectexpansion:dark_power_flower');
assert.equal(redUpgrade.key.S, 'draconicevolution:chaos_shard');
assert.equal(removed.length, added.length);
console.log('PASS: both ProjectE vanilla orientations blocked, three center trophies, all MK3+ direct routes + red upgrade require Chaos Shard; 17 replacements');
console.log('WARNING: Matter Upgrader and EMC duplication are separate bypass risks, not covered by crafting recipes');
