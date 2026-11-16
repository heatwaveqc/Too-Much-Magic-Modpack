#!/usr/bin/env node
// Static, sandboxed contract check for the Seasoned Fish KubeJS patch.
// This does NOT test Minecraft/NeoForge runtime recipe loading or tag expansion.
'use strict';
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const vm = require('node:vm');

const script = fs.readFileSync(
  path.join(__dirname, '../kubejs/server_scripts/dragon_seasoned_fish.js'),
  'utf8'
);

function exercise(dragonSurvivalInstalled) {
  const calls = [];
  const context = {
    Platform: {
      isLoaded(modId) {
        assert.equal(modId, 'dragonsurvival');
        return dragonSurvivalInstalled;
      }
    },
    Ingredient: {
      of(tag) { return { kind: 'item-tag', tag }; }
    },
    ServerEvents: {
      recipes(callback) {
        callback({
          replaceInput(filter, original, replacement) {
            calls.push({ filter: JSON.parse(JSON.stringify(filter)), original, replacement: JSON.parse(JSON.stringify(replacement)) });
          }
        });
      }
    }
  };
  vm.runInNewContext(script, context, { filename: 'dragon_seasoned_fish.js', timeout: 1000 });
  return calls;
}

assert.deepEqual(exercise(false), [], 'Do not rewrite recipes if Dragon Survival is absent');
assert.deepEqual(exercise(true), [{
  filter: { type: 'minecraft:crafting_shapeless', output: 'dragonsurvival:seasoned_fish' },
  original: 'minecraft:salmon',
  replacement: { kind: 'item-tag', tag: '#c:foods/raw_fish' }
}], 'Only swap the raw salmon ingredient of the existing Seasoned Fish crafting recipe');
assert.doesNotMatch(script, /event\\.(?:remove|shapeless|shaped)\\s*\\(/, 'Keep the original recipe, ink sac, kelp, result and ID');
console.log('PASS: Seasoned Fish KubeJS source swaps salmon for #c:foods/raw_fish only');
console.log('NOT TESTED: actual 2.0.74 recipe matching, expanded tags, JEI, crafting in game');
